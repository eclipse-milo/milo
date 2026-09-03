/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.identity.IdentityProvider;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.channel.SecurityKeysListener;
import org.eclipse.milo.opcua.stack.core.security.CertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.CertificateIdentitySelector;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateIdentitySelector;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.jspecify.annotations.Nullable;

public class OpcUaClientConfigBuilder {

  private EndpointDescription endpoint;
  private List<EndpointDescription> discoveryEndpoints;
  private @Nullable CertificateGroup certificateGroup;
  private @Nullable KeyPair identityKeyPair;
  private X509Certificate @Nullable [] identityCertificateChain;
  private CertificateIdentitySelector certificateIdentitySelector =
      DefaultCertificateIdentitySelector.create();
  private @Nullable NodeId certificateTypeId;
  private @Nullable CertificateValidator certificateValidator;

  private LocalizedText applicationName =
      LocalizedText.english("Eclipse Milo application name not configured");
  private @Nullable String applicationUri;
  private String productUri = "https://github.com/eclipse-milo/milo";

  private Supplier<String> sessionName;
  private String[] sessionLocaleIds = new String[0];
  private UInteger sessionTimeout = uint(120_000);

  private UInteger requestTimeout = uint(60_000);
  private IdentityProvider identityProvider = new AnonymousProvider();

  private EncodingLimits encodingLimits = EncodingLimits.DEFAULT;
  private UInteger maxResponseMessageSize = uint(0);
  private UInteger maxPendingPublishRequests = uint(UInteger.MAX_VALUE);

  private UInteger keepAliveFailuresAllowed = uint(1);
  private UInteger keepAliveInterval = uint(5000);
  private UInteger keepAliveTimeout = uint(5000);

  private boolean sessionEndpointValidationEnabled = false;

  private @Nullable SecurityKeysListener securityKeysListener;

  public OpcUaClientConfigBuilder setApplicationName(LocalizedText applicationName) {
    this.applicationName = applicationName;
    return this;
  }

  /**
   * Set the client application URI explicitly.
   *
   * <p>An explicitly configured URI takes precedence over the URI in the effective client
   * certificate identity.
   *
   * @param applicationUri the client application URI.
   * @return this builder.
   */
  public OpcUaClientConfigBuilder setApplicationUri(String applicationUri) {
    this.applicationUri = applicationUri;
    return this;
  }

  public OpcUaClientConfigBuilder setProductUri(String productUri) {
    this.productUri = productUri;
    return this;
  }

  public OpcUaClientConfigBuilder setSessionName(Supplier<String> sessionName) {
    this.sessionName = sessionName;
    return this;
  }

  public OpcUaClientConfigBuilder setSessionLocaleIds(String[] sessionLocaleIds) {
    this.sessionLocaleIds = sessionLocaleIds;
    return this;
  }

  public OpcUaClientConfigBuilder setSessionTimeout(UInteger sessionTimeout) {
    this.sessionTimeout = sessionTimeout;
    return this;
  }

  public OpcUaClientConfigBuilder setRequestTimeout(UInteger requestTimeout) {
    this.requestTimeout = requestTimeout;
    return this;
  }

  public OpcUaClientConfigBuilder setEncodingLimits(EncodingLimits encodingLimits) {
    this.encodingLimits = encodingLimits;
    return this;
  }

  public OpcUaClientConfigBuilder setMaxResponseMessageSize(UInteger maxResponseMessageSize) {
    this.maxResponseMessageSize = maxResponseMessageSize;
    return this;
  }

  public OpcUaClientConfigBuilder setMaxPendingPublishRequests(UInteger maxPendingPublishRequests) {
    this.maxPendingPublishRequests = maxPendingPublishRequests;
    return this;
  }

  public OpcUaClientConfigBuilder setIdentityProvider(IdentityProvider identityProvider) {
    this.identityProvider = identityProvider;
    return this;
  }

  public OpcUaClientConfigBuilder setKeepAliveFailuresAllowed(UInteger keepAliveFailuresAllowed) {
    this.keepAliveFailuresAllowed = keepAliveFailuresAllowed;
    return this;
  }

  public OpcUaClientConfigBuilder setKeepAliveInterval(UInteger keepAliveInterval) {
    this.keepAliveInterval = keepAliveInterval;
    return this;
  }

  public OpcUaClientConfigBuilder setKeepAliveTimeout(UInteger keepAliveTimeout) {
    this.keepAliveTimeout = keepAliveTimeout;
    return this;
  }

  public OpcUaClientConfigBuilder setSessionEndpointValidationEnabled(
      boolean validateSessionEndpointsEnabled) {

    this.sessionEndpointValidationEnabled = validateSessionEndpointsEnabled;
    return this;
  }

  public OpcUaClientConfigBuilder setEndpoint(EndpointDescription endpoint) {
    this.endpoint = endpoint;
    return this;
  }

  public OpcUaClientConfigBuilder setDiscoveryEndpoints(
      List<EndpointDescription> discoveryEndpoints) {

    this.discoveryEndpoints = discoveryEndpoints;
    return this;
  }

  /**
   * Set the {@link CertificateGroup} holding this client's identity and trust material.
   *
   * <p>The client selects the identity it presents on a secured endpoint from this group. Unless
   * {@link #setCertificateValidator} is also called, the group's validator validates server
   * certificates.
   *
   * <p>A single key pair and certificate chain become a group through {@code
   * DefaultCertificateGroup.forIdentity}:
   *
   * <pre>{@code
   * builder.setCertificateGroup(
   *     DefaultCertificateGroup.forIdentity(
   *         keyPair, certificateChain, trustListManager, certificateQuarantine, validator));
   * }</pre>
   *
   * <p>Replaces any identity set with {@link #setCertificateIdentity}.
   *
   * @param certificateGroup the client's certificate group.
   * @return this builder.
   */
  public OpcUaClientConfigBuilder setCertificateGroup(CertificateGroup certificateGroup) {
    this.certificateGroup = certificateGroup;
    this.identityKeyPair = null;
    this.identityCertificateChain = null;
    return this;
  }

  /**
   * Set the key pair and certificate chain this client presents on secured endpoints.
   *
   * <p>This is the simple path for a client that has one certificate on hand and no trust material
   * of its own. Server certificates are validated by the validator passed to {@link
   * #setCertificateValidator}, or not at all when none is set. Clients that need a trust list, for
   * example to install one pulled from a GDS, configure a {@link CertificateGroup} through {@link
   * #setCertificateGroup} instead.
   *
   * <pre>{@code
   * builder
   *     .setCertificateIdentity(keyPair, certificateChain)
   *     .setCertificateValidator(new DefaultClientCertificateValidator(trustListManager, quarantine));
   * }</pre>
   *
   * <p>Replaces any group set with {@link #setCertificateGroup}. The certificate type is inferred
   * from the leaf certificate; {@link #build()} throws {@link IllegalArgumentException} if it
   * cannot be, or if the key pair does not match the leaf certificate.
   *
   * <p>The inferred type gates which security policies the identity is offered for. An RSA
   * certificate with a SHA-1 signature is RsaMin, so connecting to a Basic256Sha256 endpoint fails
   * client-side with {@code Bad_ConfigurationError} instead of being sent and rejected by the
   * server. {@link DefaultCertificateGroup#forIdentity} applies the same inference.
   *
   * @param keyPair the key pair belonging to the leaf certificate.
   * @param certificateChain the leaf certificate followed by any issuer certificates.
   * @return this builder.
   */
  public OpcUaClientConfigBuilder setCertificateIdentity(
      KeyPair keyPair, X509Certificate... certificateChain) {

    this.identityKeyPair = keyPair;
    this.identityCertificateChain = certificateChain.clone();
    this.certificateGroup = null;
    return this;
  }

  /**
   * Set the selector used to choose a local certificate identity.
   *
   * @param certificateIdentitySelector the certificate identity selector.
   * @return this builder.
   */
  public OpcUaClientConfigBuilder setCertificateIdentitySelector(
      CertificateIdentitySelector certificateIdentitySelector) {

    this.certificateIdentitySelector = certificateIdentitySelector;
    return this;
  }

  /**
   * Set the requested certificate type for client identity selection.
   *
   * @param certificateTypeId the certificate type ID, or {@code null} to let the endpoint security
   *     policy choose.
   * @return this builder.
   */
  public OpcUaClientConfigBuilder setCertificateTypeId(@Nullable NodeId certificateTypeId) {
    this.certificateTypeId = certificateTypeId;
    return this;
  }

  /**
   * Set the {@link CertificateValidator} used to validate server certificates.
   *
   * <p>Overrides the validator of the configured {@link CertificateGroup}. When neither this nor a
   * group is set, server certificates are not validated.
   *
   * @param certificateValidator the validator for server certificates.
   * @return this builder.
   */
  public OpcUaClientConfigBuilder setCertificateValidator(
      CertificateValidator certificateValidator) {
    this.certificateValidator = certificateValidator;
    return this;
  }

  public OpcUaClientConfigBuilder setSecurityKeysListener(
      @Nullable SecurityKeysListener securityKeysListener) {
    this.securityKeysListener = securityKeysListener;
    return this;
  }

  public OpcUaClientConfig build() {
    if (sessionName == null) {
      sessionName =
          () ->
              String.format("UaSession:%s:%s", applicationName.text(), System.currentTimeMillis());
    }

    CertificateValidator effectiveCertificateValidator = certificateValidator;
    if (effectiveCertificateValidator == null) {
      effectiveCertificateValidator =
          certificateGroup != null
              ? certificateGroup.getCertificateValidator()
              : new CertificateValidator.InsecureCertificateValidator();
    }

    CertificateGroup effectiveCertificateGroup = certificateGroup;
    if (identityKeyPair != null && identityCertificateChain != null) {
      // A fixed identity has no trust material of its own; the client validates servers with the
      // effective validator and never reads this group's trust list or quarantine.
      effectiveCertificateGroup =
          DefaultCertificateGroup.forIdentity(
              identityKeyPair,
              identityCertificateChain,
              new MemoryTrustListManager(),
              new MemoryCertificateQuarantine(),
              effectiveCertificateValidator);
    }

    return new OpcUaClientConfigImpl(
        endpoint,
        discoveryEndpoints,
        effectiveCertificateGroup,
        certificateIdentitySelector,
        certificateTypeId,
        effectiveCertificateValidator,
        applicationName,
        applicationUri,
        productUri,
        sessionName,
        sessionLocaleIds,
        sessionTimeout,
        requestTimeout,
        encodingLimits,
        maxResponseMessageSize,
        maxPendingPublishRequests,
        identityProvider,
        keepAliveFailuresAllowed,
        keepAliveInterval,
        keepAliveTimeout,
        sessionEndpointValidationEnabled,
        securityKeysListener);
  }

  static class OpcUaClientConfigImpl implements OpcUaClientConfig {

    private final EndpointDescription endpoint;
    private final List<EndpointDescription> discoveryEndpoints;
    private final @Nullable CertificateGroup certificateGroup;
    private final CertificateIdentitySelector certificateIdentitySelector;
    private final @Nullable NodeId certificateTypeId;
    private final CertificateValidator certificateValidator;
    private final LocalizedText applicationName;
    private final @Nullable String applicationUri;
    private final String productUri;
    private final Supplier<String> sessionName;
    private final String[] sessionLocaleIds;
    private final UInteger sessionTimeout;

    private final UInteger requestTimeout;
    private final EncodingLimits encodingLimits;
    private final UInteger maxResponseMessageSize;
    private final UInteger maxPendingPublishRequests;
    private final IdentityProvider identityProvider;
    private final UInteger keepAliveFailuresAllowed;
    private final UInteger keepAliveInterval;
    private final UInteger keepAliveTimeout;
    private final boolean sessionEndpointValidationEnabled;
    private final @Nullable SecurityKeysListener securityKeysListener;

    OpcUaClientConfigImpl(
        EndpointDescription endpoint,
        List<EndpointDescription> discoveryEndpoints,
        @Nullable CertificateGroup certificateGroup,
        CertificateIdentitySelector certificateIdentitySelector,
        @Nullable NodeId certificateTypeId,
        CertificateValidator certificateValidator,
        LocalizedText applicationName,
        @Nullable String applicationUri,
        String productUri,
        Supplier<String> sessionName,
        String[] sessionLocaleIds,
        UInteger sessionTimeout,
        UInteger requestTimeout,
        EncodingLimits encodingLimits,
        UInteger maxResponseMessageSize,
        UInteger maxPendingPublishRequests,
        IdentityProvider identityProvider,
        UInteger keepAliveFailuresAllowed,
        UInteger keepAliveInterval,
        UInteger keepAliveTimeout,
        boolean sessionEndpointValidationEnabled,
        @Nullable SecurityKeysListener securityKeysListener) {

      this.endpoint = endpoint;
      this.discoveryEndpoints = discoveryEndpoints;
      this.certificateGroup = certificateGroup;
      this.certificateIdentitySelector = certificateIdentitySelector;
      this.certificateTypeId = certificateTypeId;
      this.certificateValidator = certificateValidator;
      this.applicationName = applicationName;
      this.applicationUri = applicationUri;
      this.productUri = productUri;
      this.sessionName = sessionName;
      this.sessionLocaleIds = sessionLocaleIds;
      this.sessionTimeout = sessionTimeout;
      this.requestTimeout = requestTimeout;
      this.encodingLimits = encodingLimits;
      this.maxResponseMessageSize = maxResponseMessageSize;
      this.maxPendingPublishRequests = maxPendingPublishRequests;
      this.identityProvider = identityProvider;
      this.keepAliveFailuresAllowed = keepAliveFailuresAllowed;
      this.keepAliveInterval = keepAliveInterval;
      this.keepAliveTimeout = keepAliveTimeout;
      this.sessionEndpointValidationEnabled = sessionEndpointValidationEnabled;
      this.securityKeysListener = securityKeysListener;
    }

    @Override
    public EndpointDescription getEndpoint() {
      return endpoint;
    }

    @Override
    public List<EndpointDescription> getDiscoveryEndpoints() {
      return discoveryEndpoints;
    }

    @Override
    public Optional<CertificateGroup> getCertificateGroup() {
      return Optional.ofNullable(certificateGroup);
    }

    @Override
    public CertificateIdentitySelector getCertificateIdentitySelector() {
      return certificateIdentitySelector;
    }

    @Override
    public Optional<NodeId> getCertificateTypeId() {
      return Optional.ofNullable(certificateTypeId);
    }

    @Override
    public CertificateValidator getCertificateValidator() {
      return certificateValidator;
    }

    @Override
    public LocalizedText getApplicationName() {
      return applicationName;
    }

    @Override
    public Optional<String> getApplicationUri() {
      return Optional.ofNullable(applicationUri);
    }

    @Override
    public String getProductUri() {
      return productUri;
    }

    @Override
    public Supplier<String> getSessionName() {
      return sessionName;
    }

    @Override
    public String[] getSessionLocaleIds() {
      return sessionLocaleIds;
    }

    @Override
    public UInteger getSessionTimeout() {
      return sessionTimeout;
    }

    @Override
    public UInteger getRequestTimeout() {
      return requestTimeout;
    }

    @Override
    public EncodingLimits getEncodingLimits() {
      return encodingLimits;
    }

    @Override
    public UInteger getMaxResponseMessageSize() {
      return maxResponseMessageSize;
    }

    @Override
    public UInteger getMaxPendingPublishRequests() {
      return maxPendingPublishRequests;
    }

    @Override
    public IdentityProvider getIdentityProvider() {
      return identityProvider;
    }

    @Override
    public UInteger getKeepAliveFailuresAllowed() {
      return keepAliveFailuresAllowed;
    }

    @Override
    public UInteger getKeepAliveInterval() {
      return keepAliveInterval;
    }

    @Override
    public UInteger getKeepAliveTimeout() {
      return keepAliveTimeout;
    }

    @Override
    public boolean isSessionEndpointValidationEnabled() {
      return sessionEndpointValidationEnabled;
    }

    @Override
    public Optional<SecurityKeysListener> getSecurityKeysListener() {
      return Optional.ofNullable(securityKeysListener);
    }
  }
}
