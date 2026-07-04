/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.sks;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.UnaryOperator;
import org.eclipse.milo.opcua.sdk.pubsub.security.KeyCredentialStore;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeyProvider;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeyServicesValidator;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeySet;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.DefaultClientCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.TrustListManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link SecurityKeyProvider} that pulls key material from a Security Key Service by calling
 * {@code GetSecurityKeys} (ns=0;i=15215) on the well-known PublishSubscribe Object (ns=0;i=14443).
 *
 * <p><b>Resolution (Part 14 Table 40):</b> each SecurityKeyServices entry is an SKS identity record
 * keyed by {@code server.applicationUri}, not directly a connectable endpoint. Per fetch, entries
 * are tried in array order (a previously successful entry is tried first): GetEndpoints at the
 * entry's {@code server.discoveryUrls}, filter by ApplicationUri match and {@code SignAndEncrypt},
 * constrain by the entry's SecurityPolicyUri (else rank by the discovered endpoints'
 * securityLevel), then connect with a validating certificate check and an identity chosen per the
 * entry's {@code UserIdentityTokens}. Tolerance fallback (on by default): an entry with a filled
 * {@code endpointUrl} and empty {@code discoveryUrls} uses the endpointUrl as its discovery target,
 * warned once at build time.
 *
 * <p><b>Caching and failover:</b> the resolved endpoint is cached per entry and refreshed only
 * after a failure (Part 4 §6.1.4). Failures fail over to the next entry in array order, except an
 * operation-level {@code Bad_NotFound} — an unknown SecurityGroupId is a configuration error that
 * would 404 at every redundant instance, so it fails the fetch immediately without failover.
 *
 * <p><b>Sessions:</b> each fetch uses an ephemeral connect-call-disconnect session; the client is
 * always disconnected, even when connecting fails, so no reconnect machinery outlives a fetch.
 *
 * <p><b>Threading:</b> {@code getKeys} never blocks the calling thread; fetches run on a
 * provider-owned single-threaded executor, which also serializes concurrent fetches. The provider
 * is {@link AutoCloseable}: {@link #close()} rejects new fetches and shuts down the owned executor
 * (an executor supplied via {@link Builder#executor(ExecutorService)} is the caller's to shut
 * down).
 *
 * <p>The returned {@link SecurityKeySet#securityPolicyUri()} is passed through verbatim; checking
 * it against the configured SecurityGroup policy is the consuming key manager's job.
 */
public final class SksSecurityKeyProvider implements SecurityKeyProvider, AutoCloseable {

  private static final AtomicLong INSTANCE_ID = new AtomicLong();

  private final Logger logger = LoggerFactory.getLogger(getClass());

  /** Resolution cache, keyed by entry index. Confined to the provider executor thread. */
  private final Map<Integer, ResolvedEndpoint> resolutionCache = new HashMap<>();

  /** Entry index to try first, or -1. Confined to the provider executor thread. */
  private int preferredEntry = -1;

  private volatile boolean closed = false;

  private final List<EndpointDescription> keyServices;
  private final List<Integer> serverEntries;
  private final KeyPair keyPair;
  private final X509Certificate[] certificateChain;
  private final String applicationUri;
  private final CertificateValidator certificateValidator;
  private final @Nullable KeyCredentialStore keyCredentialStore;
  private final @Nullable X509Certificate userIdentityCertificate;
  private final @Nullable PrivateKey userIdentityPrivateKey;
  private final @Nullable UnaryOperator<EndpointDescription> endpointTransform;
  private final boolean toleranceFallback;
  private final Duration requestTimeout;
  private final SksClientOperations clientOperations;
  private final ExecutorService executor;
  private final boolean ownsExecutor;

  private SksSecurityKeyProvider(
      Builder builder,
      List<Integer> serverEntries,
      KeyPair keyPair,
      X509Certificate[] certificateChain,
      String applicationUri,
      CertificateValidator certificateValidator,
      SksClientOperations clientOperations,
      ExecutorService executor,
      boolean ownsExecutor) {

    this.keyServices = List.copyOf(builder.keyServices);
    this.serverEntries = List.copyOf(serverEntries);
    this.keyPair = keyPair;
    this.certificateChain = certificateChain;
    this.applicationUri = applicationUri;
    this.certificateValidator = certificateValidator;
    this.keyCredentialStore = builder.keyCredentialStore;
    this.userIdentityCertificate = builder.userIdentityCertificate;
    this.userIdentityPrivateKey = builder.userIdentityPrivateKey;
    this.endpointTransform = builder.endpointTransform;
    this.toleranceFallback = builder.toleranceFallback;
    this.requestTimeout = builder.requestTimeout;
    this.clientOperations = clientOperations;
    this.executor = executor;
    this.ownsExecutor = ownsExecutor;
  }

  @Override
  public CompletableFuture<SecurityKeySet> getKeys(
      String securityGroupId, UInteger startingTokenId, UInteger requestedKeyCount) {

    var future = new CompletableFuture<SecurityKeySet>();

    if (closed) {
      future.completeExceptionally(
          new UaException(StatusCodes.Bad_InvalidState, "provider is closed"));
      return future;
    }

    try {
      executor.execute(
          () -> {
            try {
              future.complete(fetch(securityGroupId, startingTokenId, requestedKeyCount));
            } catch (Throwable t) {
              future.completeExceptionally(t);
            }
          });
    } catch (RejectedExecutionException e) {
      future.completeExceptionally(
          new UaException(StatusCodes.Bad_InvalidState, "provider is closed", e));
    }

    return future;
  }

  /**
   * Close this provider: new {@code getKeys} calls fail, and the owned executor is shut down
   * (letting an in-flight fetch finish). Idempotent.
   */
  @Override
  public void close() {
    closed = true;
    if (ownsExecutor) {
      executor.shutdown();
    }
  }

  /** Runs on the provider executor thread. */
  private SecurityKeySet fetch(
      String securityGroupId, UInteger startingTokenId, UInteger requestedKeyCount)
      throws UaException {

    var failures = new ArrayList<String>();
    Throwable lastCause = null;

    for (int index : fetchOrder()) {
      EndpointDescription entry = keyServices.get(index);
      try {
        SecurityKeySet keySet =
            fetchFromEntry(index, entry, securityGroupId, startingTokenId, requestedKeyCount);
        preferredEntry = index;
        return keySet;
      } catch (FatalFetchException e) {
        throw e;
      } catch (Exception e) {
        resolutionCache.remove(index);
        if (preferredEntry == index) {
          preferredEntry = -1;
        }
        String applicationUri =
            entry.getServer() != null ? entry.getServer().getApplicationUri() : null;
        failures.add(
            "securityKeyServices[%d] (%s): %s".formatted(index, applicationUri, e.getMessage()));
        lastCause = e;
        logger.debug(
            "GetSecurityKeys attempt failed for SecurityGroup '{}' at securityKeyServices[{}]",
            securityGroupId,
            index,
            e);
      }
    }

    throw new UaException(
        statusOf(lastCause),
        "GetSecurityKeys failed for SecurityGroup '%s': %s"
            .formatted(securityGroupId, String.join("; ", failures)),
        lastCause);
  }

  private List<Integer> fetchOrder() {
    int preferred = preferredEntry;
    if (preferred < 0) {
      return serverEntries;
    }
    var order = new ArrayList<Integer>(serverEntries.size());
    order.add(preferred);
    for (int index : serverEntries) {
      if (index != preferred) {
        order.add(index);
      }
    }
    return order;
  }

  private SecurityKeySet fetchFromEntry(
      int index,
      EndpointDescription entry,
      String securityGroupId,
      UInteger startingTokenId,
      UInteger requestedKeyCount)
      throws Exception {

    ResolvedEndpoint resolved = resolutionCache.get(index);
    if (resolved == null) {
      resolved = resolve(index, entry);
      resolutionCache.put(index, resolved);
    }

    SksIdentitySelector.IdentitySelection identity =
        SksIdentitySelector.select(
            entry,
            resolved.endpoint(),
            keyCredentialStore,
            certificateValidator,
            userIdentityCertificate,
            userIdentityPrivateKey);

    try (SksClientOperations.SksSession session =
        clientOperations.connect(
            resolved.endpoint(), resolved.discoveredEndpoints(), identity.identityProvider())) {

      var request =
          new CallMethodRequest(
              NodeIds.PublishSubscribe,
              NodeIds.PublishSubscribe_GetSecurityKeys,
              new Variant[] {
                Variant.ofString(securityGroupId),
                Variant.ofUInt32(startingTokenId),
                Variant.ofUInt32(requestedKeyCount)
              });

      CallMethodResult result = session.call(request);

      StatusCode statusCode = result.getStatusCode();
      if (statusCode.isGood()) {
        return GetSecurityKeysResponse.parse(result);
      }

      long code = statusCode.value();
      if (code == StatusCodes.Bad_NotFound) {
        // Unknown SecurityGroupId: a configuration error that would 404 at every redundant
        // instance of the SKS, so fail the fetch without an entry-failover retry storm.
        throw new FatalFetchException(
            ("SecurityGroupId '%s' is unknown at the SKS (securityKeyServices[%d], %s); check the"
                    + " configured SecurityGroupId")
                .formatted(securityGroupId, index, resolved.endpoint().getEndpointUrl()));
      } else if (code == StatusCodes.Bad_UserAccessDenied) {
        throw new UaException(
            code,
            "SKS denied access to SecurityGroup '%s' using %s identity (securityKeyServices[%d])"
                .formatted(securityGroupId, identity.tokenType(), index));
      } else if (code == StatusCodes.Bad_SecurityModeInsufficient) {
        logger.error(
            "SKS reported Bad_SecurityModeInsufficient over an endpoint selected as SignAndEncrypt"
                + " ({}) — this is a resolution bug",
            resolved.endpoint().getEndpointUrl());
        throw new UaException(
            code,
            "SKS requires an encrypted channel (securityKeyServices[%d], %s)"
                .formatted(index, resolved.endpoint().getEndpointUrl()));
      } else {
        throw new UaException(code, "GetSecurityKeys failed: " + statusCode);
      }
    } finally {
      identity.wipeSecret();
    }
  }

  private ResolvedEndpoint resolve(int index, EndpointDescription entry) throws UaException {
    SksEndpointSelector.DiscoveryTargets targets = SksEndpointSelector.discoveryTargets(entry);
    if (targets.urls().isEmpty()) {
      // Unreachable after build-time validation; defensive.
      throw new UaException(
          StatusCodes.Bad_ConfigurationError,
          "securityKeyServices[%d]: no discovery target".formatted(index));
    }
    if (targets.fromEndpointUrlFallback()) {
      logger.debug(
          "securityKeyServices[{}]: using endpointUrl {} as discovery target (tolerance fallback)",
          index,
          targets.urls().get(0));
    }

    String applicationUri =
        entry.getServer() != null ? entry.getServer().getApplicationUri() : null;
    if (applicationUri == null || applicationUri.isEmpty()) {
      // Unreachable after build-time validation; defensive.
      throw new UaException(
          StatusCodes.Bad_ConfigurationError,
          "securityKeyServices[%d]: no server.applicationUri".formatted(index));
    }

    Throwable lastCause = null;

    for (String discoveryUrl : targets.urls()) {
      List<EndpointDescription> endpoints;
      try {
        endpoints = clientOperations.getEndpoints(discoveryUrl);
      } catch (Exception e) {
        if (targets.fromEndpointUrlFallback() && !discoveryUrl.endsWith("/discovery")) {
          // The fallback URL is a session endpoint; some servers serve discovery only at the
          // Part 6 "/discovery" suffix.
          String suffixed =
              (discoveryUrl.endsWith("/") ? discoveryUrl : discoveryUrl + "/") + "discovery";
          try {
            endpoints = clientOperations.getEndpoints(suffixed);
          } catch (Exception e2) {
            lastCause = e2;
            continue;
          }
        } else {
          lastCause = e;
          continue;
        }
      }

      Optional<EndpointDescription> selected =
          SksEndpointSelector.selectEndpoint(
              endpoints, applicationUri, entry.getSecurityPolicyUri());

      if (selected.isPresent()) {
        EndpointDescription endpoint = selected.get();
        if (endpointTransform != null) {
          endpoint = endpointTransform.apply(endpoint);
        }
        return new ResolvedEndpoint(endpoint, endpoints);
      } else {
        String policyConstraint =
            entry.getSecurityPolicyUri() == null || entry.getSecurityPolicyUri().isEmpty()
                ? ""
                : " and securityPolicyUri " + entry.getSecurityPolicyUri();
        lastCause =
            new UaException(
                StatusCodes.Bad_ConfigurationError,
                "no SignAndEncrypt endpoint matching applicationUri %s%s at %s"
                    .formatted(applicationUri, policyConstraint, discoveryUrl));
      }
    }

    throw new UaException(
        statusOf(lastCause),
        "securityKeyServices[%d]: resolution failed: %s"
            .formatted(index, lastCause != null ? lastCause.getMessage() : "unknown"),
        lastCause);
  }

  private static long statusOf(@Nullable Throwable cause) {
    if (cause == null) {
      return StatusCodes.Bad_UnexpectedError;
    }
    return UaException.extract(cause)
        .map(ua -> ua.getStatusCode().value())
        .orElse(StatusCodes.Bad_UnexpectedError);
  }

  /**
   * Create a new {@link Builder}.
   *
   * @return a new {@link Builder}.
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Create a new {@link Builder} initialized with the values this provider was built from.
   *
   * <p>The provider-owned executor is not carried over (each built provider owns its own); an
   * executor supplied via {@link Builder#executor(ExecutorService)} is.
   *
   * @return a new {@link Builder} initialized with the values of this provider.
   */
  public Builder toBuilder() {
    var builder = new Builder();
    builder.keyServices.addAll(keyServices);
    builder.keyPair = keyPair;
    builder.certificateChain = certificateChain;
    builder.applicationUri = applicationUri;
    builder.certificateValidator = certificateValidator;
    builder.keyCredentialStore = keyCredentialStore;
    builder.userIdentityCertificate = userIdentityCertificate;
    builder.userIdentityPrivateKey = userIdentityPrivateKey;
    builder.endpointTransform = endpointTransform;
    builder.toleranceFallback = toleranceFallback;
    builder.requestTimeout = requestTimeout;
    if (!ownsExecutor) {
      builder.executor = executor;
    }
    return builder;
  }

  /** Builds {@link SksSecurityKeyProvider} instances. */
  public static final class Builder {

    private final List<EndpointDescription> keyServices = new ArrayList<>();
    private @Nullable KeyPair keyPair;
    private X509Certificate @Nullable [] certificateChain;
    private @Nullable String applicationUri;
    private @Nullable CertificateValidator certificateValidator;
    private @Nullable KeyCredentialStore keyCredentialStore;
    private @Nullable X509Certificate userIdentityCertificate;
    private @Nullable PrivateKey userIdentityPrivateKey;
    private @Nullable UnaryOperator<EndpointDescription> endpointTransform;
    private boolean toleranceFallback = true;
    private Duration requestTimeout = Duration.ofSeconds(60);
    private @Nullable ExecutorService executor;
    private @Nullable SksClientOperations clientOperations;

    private Builder() {}

    /**
     * Set the SecurityKeyServices entries identifying the SKS and its redundant instances,
     * replacing any previously configured entries.
     *
     * <p>Source: the effective SecurityKeyServices of the SecurityGroup being served, e.g. {@code
     * SecurityGroupConfig.getKeyServices()}.
     *
     * @param keyServices the SecurityKeyServices entries.
     * @return this {@link Builder}.
     */
    public Builder keyServices(List<EndpointDescription> keyServices) {
      this.keyServices.clear();
      this.keyServices.addAll(keyServices);
      return this;
    }

    /**
     * Add a SecurityKeyServices entry.
     *
     * @param keyService the entry to add.
     * @return this {@link Builder}.
     */
    public Builder keyService(EndpointDescription keyService) {
      this.keyServices.add(keyService);
      return this;
    }

    /**
     * Set the application instance key pair. Required: the SKS connection is always SignAndEncrypt.
     *
     * @param keyPair the application instance {@link KeyPair}.
     * @return this {@link Builder}.
     */
    public Builder keyPair(KeyPair keyPair) {
      this.keyPair = keyPair;
      return this;
    }

    /**
     * Set the application instance certificate (a chain of length 1).
     *
     * @param certificate the application instance certificate.
     * @return this {@link Builder}.
     */
    public Builder certificate(X509Certificate certificate) {
      this.certificateChain = new X509Certificate[] {certificate};
      return this;
    }

    /**
     * Set the application instance certificate chain.
     *
     * @param certificateChain the application instance certificate chain; the first element is the
     *     application instance certificate.
     * @return this {@link Builder}.
     */
    public Builder certificateChain(X509Certificate[] certificateChain) {
      this.certificateChain = certificateChain.clone();
      return this;
    }

    /**
     * Set the ApplicationUri of the PubSub application. Required; must match the SAN URI of the
     * application instance certificate.
     *
     * @param applicationUri the ApplicationUri.
     * @return this {@link Builder}.
     */
    public Builder applicationUri(String applicationUri) {
      this.applicationUri = applicationUri;
      return this;
    }

    /**
     * Set the {@link CertificateValidator} used to validate the SKS certificate.
     *
     * <p>Required, with no permissive default: an SKS connection is exactly the place where an
     * insecure default would be a vulnerability. See {@link #trustListManager(TrustListManager)}
     * for a convenience wrapper.
     *
     * @param certificateValidator the validator.
     * @return this {@link Builder}.
     */
    public Builder certificateValidator(CertificateValidator certificateValidator) {
      this.certificateValidator = certificateValidator;
      return this;
    }

    /**
     * Convenience for {@link #certificateValidator(CertificateValidator)}: wraps {@code
     * trustListManager} in a {@link DefaultClientCertificateValidator}.
     *
     * @param trustListManager the trust list the SKS certificate is validated against.
     * @return this {@link Builder}.
     */
    public Builder trustListManager(TrustListManager trustListManager) {
      this.certificateValidator =
          new DefaultClientCertificateValidator(
              trustListManager, new MemoryCertificateQuarantine());
      return this;
    }

    /**
     * Set the {@link KeyCredentialStore} consulted for USERNAME identities, keyed by the SKS
     * ApplicationUri. Optional; without it USERNAME entries fail over to other listed token types.
     *
     * @param keyCredentialStore the credential store.
     * @return this {@link Builder}.
     */
    public Builder keyCredentialStore(KeyCredentialStore keyCredentialStore) {
      this.keyCredentialStore = keyCredentialStore;
      return this;
    }

    /**
     * Set the user identity certificate used for CERTIFICATE (X.509) identity tokens. Optional;
     * distinct from the application instance certificate.
     *
     * @param certificate the user identity certificate.
     * @param privateKey the corresponding private key.
     * @return this {@link Builder}.
     */
    public Builder userIdentityCertificate(X509Certificate certificate, PrivateKey privateKey) {
      this.userIdentityCertificate = certificate;
      this.userIdentityPrivateKey = privateKey;
      return this;
    }

    /**
     * Set a transform applied to the selected endpoint before connecting, e.g. {@code e ->
     * EndpointUtil.updateUrl(e, host)} for servers that return endpoints under unresolvable
     * hostnames. Applied after filtering/ranking, before caching.
     *
     * @param endpointTransform the transform.
     * @return this {@link Builder}.
     */
    public Builder endpointTransform(UnaryOperator<EndpointDescription> endpointTransform) {
      this.endpointTransform = endpointTransform;
      return this;
    }

    /**
     * Enable or disable the Table 40 tolerance fallback: a non-conformant entry with a filled
     * {@code endpointUrl} and empty {@code discoveryUrls} uses the endpointUrl as its discovery
     * target. Enabled by default (warned at build time); disabling makes such entries fail {@link
     * #build()}.
     *
     * @param toleranceFallback true to enable the fallback.
     * @return this {@link Builder}.
     */
    public Builder toleranceFallback(boolean toleranceFallback) {
      this.toleranceFallback = toleranceFallback;
      return this;
    }

    /**
     * Set the request timeout for service calls to the SKS.
     *
     * @param requestTimeout the request timeout.
     * @return this {@link Builder}.
     */
    public Builder requestTimeout(Duration requestTimeout) {
      this.requestTimeout = requestTimeout;
      return this;
    }

    /**
     * Set the executor fetches run on, overriding the provider-owned single-threaded default.
     *
     * <p>The caller retains ownership: {@link SksSecurityKeyProvider#close()} does not shut a
     * supplied executor down. The executor must serialize submitted tasks (fetch state is confined
     * to it).
     *
     * @param executor the executor to run fetches on.
     * @return this {@link Builder}.
     */
    public Builder executor(ExecutorService executor) {
      this.executor = executor;
      return this;
    }

    /** Test seam: replaces the discovery/connect/call operations. */
    Builder clientOperations(SksClientOperations clientOperations) {
      this.clientOperations = clientOperations;
      return this;
    }

    /**
     * Build an {@link SksSecurityKeyProvider} from the values configured on this builder.
     *
     * <p>SecurityKeyServices entries are validated here against Part 14 Table 40 via {@code
     * SecurityKeyServicesValidator}: hard violations fail, soft non-conformances are logged at
     * WARN.
     *
     * @return a new {@link SksSecurityKeyProvider}.
     * @throws UaException with {@code Bad_ConfigurationError} if required values are missing or the
     *     SecurityKeyServices entries are invalid.
     */
    public SksSecurityKeyProvider build() throws UaException {
      Logger logger = LoggerFactory.getLogger(SksSecurityKeyProvider.class);

      if (keyServices.isEmpty()) {
        throw configurationError("keyServices is required");
      }
      KeyPair keyPair = this.keyPair;
      if (keyPair == null) {
        throw configurationError("keyPair is required (the SKS connection is SignAndEncrypt)");
      }
      X509Certificate[] certificateChain = this.certificateChain;
      if (certificateChain == null || certificateChain.length == 0) {
        throw configurationError(
            "certificate or certificateChain is required (the SKS connection is SignAndEncrypt)");
      }
      String applicationUri = this.applicationUri;
      if (applicationUri == null || applicationUri.isEmpty()) {
        throw configurationError("applicationUri is required");
      }
      CertificateValidator certificateValidator = this.certificateValidator;
      if (certificateValidator == null) {
        throw configurationError(
            "certificateValidator is required; there is no insecure default. Supply"
                + " certificateValidator(...) or trustListManager(...)");
      }

      Optional<String> sanUri = CertificateUtil.getSanUri(certificateChain[0]);
      if (sanUri.isPresent() && !sanUri.get().equals(applicationUri)) {
        logger.warn(
            "applicationUri {} does not match the certificate SAN URI {}; ActivateSession at the"
                + " SKS is likely to fail",
            applicationUri,
            sanUri.get());
      }

      List<String> warnings = SecurityKeyServicesValidator.validate(keyServices);
      warnings.forEach(warning -> logger.warn("SecurityKeyServices: {}", warning));

      var serverEntries = new ArrayList<Integer>();
      for (int i = 0; i < keyServices.size(); i++) {
        EndpointDescription entry = keyServices.get(i);
        if (entry.getServer() != null
            && entry.getServer().getApplicationType() == ApplicationType.Server) {
          if (!toleranceFallback
              && SksEndpointSelector.discoveryTargets(entry).fromEndpointUrlFallback()) {
            throw configurationError(
                ("securityKeyServices[%d]: server.discoveryUrls is empty and the endpointUrl"
                        + " tolerance fallback is disabled")
                    .formatted(i));
          }
          serverEntries.add(i);
        }
      }
      if (serverEntries.isEmpty()) {
        throw configurationError(
            "keyServices contains no Server-typed (pull) entries; Client-typed entries are"
                + " push-identity records");
      }

      ExecutorService executor = this.executor;
      boolean ownsExecutor = executor == null;
      if (executor == null) {
        String threadName = "milo-sks-provider-" + INSTANCE_ID.incrementAndGet();
        executor =
            Executors.newSingleThreadExecutor(
                r -> {
                  var thread = new Thread(r, threadName);
                  thread.setDaemon(true);
                  return thread;
                });
      }

      SksClientOperations clientOperations = this.clientOperations;
      if (clientOperations == null) {
        clientOperations =
            new DefaultSksClientOperations(
                applicationUri, keyPair, certificateChain, certificateValidator, requestTimeout);
      }

      return new SksSecurityKeyProvider(
          this,
          serverEntries,
          keyPair,
          certificateChain,
          applicationUri,
          certificateValidator,
          clientOperations,
          executor,
          ownsExecutor);
    }

    private static UaException configurationError(String message) {
      return new UaException(
          StatusCodes.Bad_ConfigurationError, "SksSecurityKeyProvider: " + message);
    }
  }

  /** A cached Table 40 resolution: the selected endpoint plus the list it was selected from. */
  private record ResolvedEndpoint(
      EndpointDescription endpoint, List<EndpointDescription> discoveredEndpoints) {}

  /** Marker for failures that must not fail over to the next entry. */
  private static final class FatalFetchException extends UaException {

    private FatalFetchException(String message) {
      super(StatusCodes.Bad_NotFound, message);
    }
  }
}
