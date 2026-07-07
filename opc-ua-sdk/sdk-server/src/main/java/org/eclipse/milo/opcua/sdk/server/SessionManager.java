/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static java.util.Objects.requireNonNullElse;
import static org.eclipse.milo.opcua.sdk.server.servicesets.AbstractServiceSet.createResponseHeader;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.util.DigestUtil.sha1;

import com.google.common.base.Objects;
import com.google.common.math.DoubleMath;
import java.math.RoundingMode;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.server.identity.Identity;
import org.eclipse.milo.opcua.sdk.server.identity.IdentityValidator;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.channel.SecureChannel;
import org.eclipse.milo.opcua.stack.core.security.CertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.ChannelBoundSignatureData;
import org.eclipse.milo.opcua.stack.core.security.EccEncryptedSecret;
import org.eclipse.milo.opcua.stack.core.security.EnhancedUserTokenAdditionalHeader;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.AnonymousIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.CloseSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CloseSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EphemeralKeyType;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;
import org.eclipse.milo.opcua.stack.core.types.structured.UserIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;
import org.eclipse.milo.opcua.stack.core.util.TaskQueue;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionManager {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final Map<NodeId, Session> createdSessions = new ConcurrentHashMap<>();
  private final Map<NodeId, Session> activeSessions = new ConcurrentHashMap<>();

  /**
   * Guards the session-count limit check together with the eviction, registration, and activation
   * of sessions so that concurrent requests cannot exceed the configured maximum or evict a session
   * that is being activated.
   */
  private final Object sessionLock = new Object();

  private final List<SessionListener> sessionListeners = new CopyOnWriteArrayList<>();
  private final TaskQueue sessionListenerTaskQueue;

  /**
   * Marks executor threads while they are inside {@link SessionListener} callbacks.
   *
   * <p>Shutdown normally waits for in-flight listener callbacks, but a listener is allowed to
   * trigger shutdown itself. In that re-entrant path the callback must not wait for the queue
   * currently waiting for the callback to return.
   */
  private final ThreadLocal<Boolean> sessionListenerCallback = new ThreadLocal<>();

  /** Completes when the one-time session shutdown cleanup has finished. */
  private final CountDownLatch shutdownComplete = new CountDownLatch(1);

  /**
   * Terminal shutdown state for session creation and listener delivery.
   *
   * <p>{@link #beginShutdown()} moves the manager out of {@link ShutdownState#RUNNING} before
   * transports are unbound, which rejects new CreateSession requests while existing listener work
   * can still drain. {@link #shutdown()} performs the one-time cleanup and wakes any concurrent
   * callers waiting on {@link #shutdownComplete}.
   */
  private final AtomicReference<ShutdownState> shutdownState =
      new AtomicReference<>(ShutdownState.RUNNING);

  /**
   * Store the last N client nonces and to make sure they aren't re-used.
   *
   * <p>This number is arbitrary; trying to prevent clients from re-using nonces is merely to
   * satisfy the CTT.
   */
  private final List<ByteString> clientNonces = new CopyOnWriteArrayList<>();

  private final OpcUaServer server;

  SessionManager(OpcUaServer server, Executor executor) {
    this.server = server;

    sessionListenerTaskQueue = new TaskQueue(executor);
  }

  /** Session-manager shutdown phases. */
  private enum ShutdownState {
    /** Normal operation: CreateSession requests and listener notifications may proceed. */
    RUNNING,
    /** New sessions are rejected, but listener quiescence and session cleanup have not started. */
    REQUESTED,
    /** The one-time queue drain and session close pass have started. */
    CLEANUP_STARTED
  }

  /**
   * Kill the session identified by {@code nodeId} and optionally delete all its subscriptions.
   *
   * @param nodeId the {@link NodeId} identifying the session to kill.
   * @param deleteSubscriptions {@code true} if all its subscriptions should be deleted as well.
   */
  public void killSession(NodeId nodeId, boolean deleteSubscriptions) {
    activeSessions.values().stream()
        .filter(s -> s.getSessionId().equals(nodeId))
        .findFirst()
        .ifPresent(s -> s.close(deleteSubscriptions));
  }

  /**
   * Add {@code listener} to be notified when sessions are created.
   *
   * @param listener the {@link SessionListener} to add.
   */
  public void addSessionListener(SessionListener listener) {
    sessionListeners.add(listener);
  }

  /**
   * Remove {@code listener} from the {@link SessionListener} list.
   *
   * @param listener the {@link SessionListener} to remove.
   */
  public void removeSessionListener(SessionListener listener) {
    sessionListeners.remove(listener);
  }

  /**
   * Get a list of all the current {@link Session}s. This includes sessions that have been created
   * but not yet activated.
   *
   * @return a list of all the current {@link Session}s.
   */
  public List<Session> getAllSessions() {
    List<Session> sessions = new ArrayList<>();
    sessions.addAll(createdSessions.values());
    sessions.addAll(activeSessions.values());
    return sessions;
  }

  /**
   * Get the current session count, including session that have been created but not yet activated.
   *
   * @return the current session count, including session that have been created but not yet
   *     activated.
   */
  public UInteger getCurrentSessionCount() {
    return uint(createdSessions.size() + activeSessions.size());
  }

  public Session getSession(ServiceRequestContext context, RequestHeader requestHeader)
      throws UaException {
    long secureChannelId = context.getSecureChannel().getChannelId();
    NodeId authToken = requestHeader.getAuthenticationToken();

    Session session = activeSessions.get(authToken);

    if (session == null) {
      // session is either not activated or doesn't exist
      session = createdSessions.get(authToken);

      if (session != null) {
        session.close(true);

        throw new UaException(StatusCodes.Bad_SessionNotActivated);
      } else {
        throw new UaException(StatusCodes.Bad_SessionIdInvalid);
      }
    } else {
      // session exists and is activated
      if (session.getSecureChannelId() != secureChannelId) {
        session.getSessionDiagnostics().getUnauthorizedRequestCount().increment();
        throw new UaException(StatusCodes.Bad_SecureChannelIdInvalid);
      }

      session.updateLastActivity();

      return session;
    }
  }

  public CreateSessionResponse createSession(
      ServiceRequestContext context, CreateSessionRequest request) throws UaException {

    if (isShutdownRequested()) {
      throw new UaException(StatusCodes.Bad_Shutdown);
    }

    ByteString serverNonce = NonceUtil.generateNonce(32);
    NodeId authenticationToken = new NodeId(0, NonceUtil.generateNonce(32));
    long maxRequestMessageSize = server.getConfig().getEncodingLimits().getMaxMessageSize();
    double revisedSessionTimeout =
        Math.max(
            5000,
            Math.min(
                server.getConfig().getLimits().getMaxSessionTimeout(),
                request.getRequestedSessionTimeout()));

    ApplicationDescription clientDescription = request.getClientDescription();

    long secureChannelId = context.getSecureChannel().getChannelId();
    SecurityPolicy securityPolicy = context.getSecureChannel().getSecurityPolicy();
    String transportProfileUri = context.getTransportProfile().getUri();

    EndpointDescription[] serverEndpoints =
        server.getApplicationContext().getEndpointDescriptions().stream()
            .filter(
                ed -> {
                  String endpointUrl = ed.getEndpointUrl();
                  return endpointUrl == null || !endpointUrl.endsWith("/discovery");
                })
            .filter(ed -> endpointMatchesUrl(ed, request.getEndpointUrl()))
            .filter(ed -> Objects.equal(transportProfileUri, ed.getTransportProfileUri()))
            .map(SessionManager::stripNonEssentialFields)
            .toArray(EndpointDescription[]::new);

    if (serverEndpoints.length == 0) {
      // GetEndpoints in UaStackServer returns *all* endpoints regardless of a hostname
      // match in the endpoint URL if the result after filtering is 0 endpoints. Do the
      // same here.
      serverEndpoints =
          server.getApplicationContext().getEndpointDescriptions().stream()
              .filter(
                  ed -> {
                    String endpointUrl = ed.getEndpointUrl();
                    return endpointUrl == null || !endpointUrl.endsWith("/discovery");
                  })
              .filter(ed -> Objects.equal(transportProfileUri, ed.getTransportProfileUri()))
              .map(SessionManager::stripNonEssentialFields)
              .toArray(EndpointDescription[]::new);
    }

    ByteString clientNonce = request.getClientNonce();

    if (securityPolicy != SecurityPolicy.None) {
      NonceUtil.validateNonce(clientNonce);

      if (clientNonces.contains(clientNonce)) {
        throw new UaException(StatusCodes.Bad_NonceInvalid);
      }
    }

    if (securityPolicy != SecurityPolicy.None && clientNonce.isNotNull()) {
      clientNonces.add(clientNonce);
      while (clientNonces.size() > 64) {
        clientNonces.remove(0);
      }
    }

    if (securityPolicy != SecurityPolicy.None) {
      X509Certificate clientCertificateFromRequest =
          CertificateUtil.decodeCertificate(request.getClientCertificate().bytesOrEmpty());

      if (!Objects.equal(
          clientCertificateFromRequest, context.getSecureChannel().getRemoteCertificate())) {

        throw new UaException(
            StatusCodes.Bad_SecurityChecksFailed,
            "certificate used to open secure channel "
                + "differs from certificate used to create session");
      }
    }

    SecurityConfiguration securityConfiguration =
        createSecurityConfiguration(context.getSecureChannel());

    if (securityPolicy != SecurityPolicy.None) {
      X509Certificate clientCertificate = securityConfiguration.getClientCertificate();

      List<X509Certificate> clientCertificateChain =
          securityConfiguration.getClientCertificateChain();

      if (clientCertificate == null || clientCertificateChain == null) {
        throw new UaException(
            StatusCodes.Bad_SecurityChecksFailed, "client certificate must be non-null");
      }

      X509Certificate serverCertificate = securityConfiguration.getServerCertificate();

      if (serverCertificate == null) {
        throw new UaException(StatusCodes.Bad_InternalError, "server certificate must be non-null");
      }

      CertificateGroup certificateGroup =
          server
              .getConfig()
              .getCertificateManager()
              .getCertificateGroup(CertificateUtil.thumbprint(serverCertificate))
              .orElseThrow(
                  () ->
                      new UaException(
                          StatusCodes.Bad_ConfigurationError,
                          "no certificate group for server certificate"));

      certificateGroup
          .getCertificateValidator()
          .validateCertificateChain(
              clientCertificateChain,
              clientDescription.getApplicationUri(),
              null,
              securityPolicy.getProfile());
    }

    // SignatureData must be created using only the bytes of the client
    // leaf certificate, not the bytes of the client certificate chain.
    SignatureData serverSignature =
        getServerSignature(securityPolicy, securityConfiguration, clientNonce, serverNonce);

    NodeId sessionId = new NodeId(1, "Session:" + UUID.randomUUID());
    String sessionName = request.getSessionName();
    Duration sessionTimeout =
        Duration.ofMillis(DoubleMath.roundToLong(revisedSessionTimeout, RoundingMode.UP));

    Session session =
        new Session(
            server,
            sessionId,
            sessionName,
            sessionTimeout,
            clientDescription,
            request.getServerUri(),
            request.getMaxResponseMessageSize(),
            findSessionEndpoint(context),
            secureChannelId,
            securityConfiguration);

    session.setLastNonce(serverNonce);
    session.setClientNonce(clientNonce);
    session.setClientAddress(context.clientAddress());

    ExtensionObject additionalHeader =
        createSessionAdditionalHeader(request, securityConfiguration, session);

    // Enforce the session limit and register the new session atomically so that concurrent
    // CreateSession requests cannot exceed the maximum. Done after validation so that a request
    // which is going to fail never evicts another client's pending session.
    synchronized (sessionLock) {
      if (isShutdownRequested()) {
        session.close(false);
        throw new UaException(StatusCodes.Bad_Shutdown);
      }

      long maxSessionCount = server.getConfig().getLimits().getMaxSessions().longValue();
      if (createdSessions.size() + activeSessions.size() >= maxSessionCount) {
        // OPC UA Part 4, 5.7.2: at the session limit the Server shall close the oldest Session
        // that has not yet been activated before rejecting a new request. Only if every existing
        // Session has already been activated is Bad_TooManySessions returned.
        Session oldestUnactivated =
            createdSessions.values().stream()
                .min(Comparator.comparingLong(s -> s.getConnectionTime().getUtcTime()))
                .orElse(null);

        if (oldestUnactivated == null) {
          // Release the timeout task of the session we are not going to register. No lifecycle
          // listener has been added yet, so this fires no session-closed notifications.
          session.close(false);
          throw new UaException(StatusCodes.Bad_TooManySessions);
        }

        oldestUnactivated.close(false);
      }

      session.addLifecycleListener(
          (s, remove) -> {
            createdSessions.remove(authenticationToken);
            activeSessions.remove(authenticationToken);

            fireSessionClosed(s);
          });

      createdSessions.put(authenticationToken, session);
    }

    fireSessionCreated(session);

    return new CreateSessionResponse(
        createResponseHeader(request, StatusCode.GOOD, additionalHeader),
        sessionId,
        authenticationToken,
        revisedSessionTimeout,
        serverNonce,
        session.getEndpoint().getServerCertificate(),
        serverEndpoints,
        new SignedSoftwareCertificate[0],
        serverSignature,
        uint(maxRequestMessageSize));
  }

  private @Nullable ExtensionObject createSessionAdditionalHeader(
      CreateSessionRequest request, SecurityConfiguration securityConfiguration, Session session)
      throws UaException {

    /*
     * Enhanced ECC and RSA-DH username-token policies need a server ephemeral key before
     * ActivateSession can encrypt the password. The client requests that key by policy URI in the
     * CreateSession RequestHeader AdditionalHeader, and the server answers with a signed
     * EphemeralKeyType in the response AdditionalHeader.
     */
    return resolveUserTokenEphemeralKeyHeader(
        request.getRequestHeader().getAdditionalHeader(), securityConfiguration, session);
  }

  /**
   * Resolve the response AdditionalHeader for an enhanced user-token key negotiation, honoring the
   * Part 4 "ignore unrecognized header" and Part 6, 6.8.2 "report failures in-parameter" rules.
   *
   * <p>An unrecognized request header (absent, undecodable, or not an AdditionalParametersType)
   * yields {@code null}, so the service proceeds with no additional header. A request for an
   * unknown or non-enhanced policy URI succeeds and returns {@code ECDHKey =
   * StatusCode(Bad_SecurityPolicyRejected)} rather than a service fault, matching the convention
   * the Milo client already accepts.
   *
   * @param additionalHeader the request AdditionalHeader, if any.
   * @param securityConfiguration the security configuration used to select the signing key pair.
   * @param session the session that owns the negotiated key material.
   * @return the response AdditionalHeader, or {@code null} when no negotiation was requested.
   * @throws UaException if the header payload is malformed or key material cannot be created.
   */
  private @Nullable ExtensionObject resolveUserTokenEphemeralKeyHeader(
      @Nullable ExtensionObject additionalHeader,
      SecurityConfiguration securityConfiguration,
      Session session)
      throws UaException {

    EnhancedUserTokenAdditionalHeader.NegotiationRequest negotiationRequest =
        EnhancedUserTokenAdditionalHeader.decodeRequest(
            server.getStaticEncodingContext(), additionalHeader);

    if (negotiationRequest
        instanceof EnhancedUserTokenAdditionalHeader.NegotiationRequest.Supported supported) {
      return issueUserTokenEphemeralKey(supported.securityPolicy(), securityConfiguration, session);
    } else if (negotiationRequest
        instanceof EnhancedUserTokenAdditionalHeader.NegotiationRequest.Unsupported unsupported) {
      logger.debug(
          "rejecting enhanced user-token negotiation for unavailable policy: {}",
          unsupported.securityPolicyUri());

      return EnhancedUserTokenAdditionalHeader.createResponse(
          server.getStaticEncodingContext(),
          new StatusCode(StatusCodes.Bad_SecurityPolicyRejected));
    } else {
      return null;
    }
  }

  /**
   * Rotate the session's enhanced user-token ephemeral key after a successful ActivateSession.
   *
   * <p>Part 6, 6.8.2 requires the receiver EphemeralKey to be single-use: once an ActivateSession
   * consumes it, the server must reject the same key and hand the client a fresh one. The client
   * requests the new key by repeating the {@code ECDHPolicyUri} in the ActivateSession request
   * AdditionalHeader, and the server returns a new signed {@link EphemeralKeyType} in the response
   * AdditionalHeader. Generating a new key pair here replaces the one just consumed, so any replay
   * of the previous EphemeralKey fails the receiver-key match in {@link
   * EccEncryptedSecret#decrypt}.
   *
   * <p>A request for an unknown or non-enhanced policy URI does not fail the activation; it returns
   * an {@code ECDHKey} status code as described on {@link #resolveUserTokenEphemeralKeyHeader}.
   *
   * @param request the ActivateSession request whose AdditionalHeader may request a fresh key.
   * @param securityConfiguration the security configuration the session is (now) bound to.
   * @param session the session being activated.
   * @return the response AdditionalHeader carrying the fresh signed key, an in-parameter status
   *     code, or {@code null} when no enhanced user-token key was requested.
   * @throws UaException if the header payload is malformed or key material cannot be created.
   */
  private @Nullable ExtensionObject activateSessionAdditionalHeader(
      ActivateSessionRequest request, SecurityConfiguration securityConfiguration, Session session)
      throws UaException {

    return resolveUserTokenEphemeralKeyHeader(
        request.getRequestHeader().getAdditionalHeader(), securityConfiguration, session);
  }

  /**
   * Generate, store, and sign a fresh enhanced user-token ephemeral key for {@code session}.
   *
   * <p>Shared by CreateSession (initial issue) and ActivateSession (single-use rotation). Storing
   * the new key pair on the session overwrites any previously issued one, which is what enforces
   * the Part 6, 6.8.2 single-use property.
   *
   * @param securityPolicy the requested enhanced user-token security policy.
   * @param securityConfiguration the security configuration used to select the signing key pair.
   * @param session the session that owns the key material and whose endpoint advertises the policy.
   * @return the encoded response AdditionalHeader carrying the signed {@link EphemeralKeyType}.
   * @throws UaException if the policy is unavailable on the endpoint or key material cannot be
   *     created.
   */
  private ExtensionObject issueUserTokenEphemeralKey(
      SecurityPolicy securityPolicy, SecurityConfiguration securityConfiguration, Session session)
      throws UaException {

    if (!EnhancedUserTokenAdditionalHeader.hasUsernameTokenSecurityPolicy(
        session.getEndpoint(), securityPolicy)) {
      throw new UaException(
          StatusCodes.Bad_SecurityPolicyRejected,
          "requested enhanced user-token policy is not available on the selected endpoint");
    }

    KeyPair signingKeyPair = selectUserTokenSigningKeyPair(securityConfiguration, session);

    KeyPair ephemeralKeyPair =
        EccEncryptedSecret.generateEphemeralKeyPair(securityPolicy.getProfile());
    ByteString ephemeralPublicKey =
        EccEncryptedSecret.encodeEphemeralPublicKey(
            securityPolicy.getProfile(), ephemeralKeyPair.getPublic());
    EphemeralKeyType ephemeralKey =
        EccEncryptedSecret.createEphemeralKey(
            securityPolicy.getProfile(), signingKeyPair, ephemeralKeyPair);

    session.setUserTokenEphemeralKeyPair(ephemeralKeyPair, ephemeralPublicKey);

    return EnhancedUserTokenAdditionalHeader.createResponse(
        server.getStaticEncodingContext(), securityPolicy, ephemeralKey);
  }

  private KeyPair selectUserTokenSigningKeyPair(
      SecurityConfiguration securityConfiguration, Session session) throws UaException {

    /*
     * The client verifies the EphemeralKeyType signature with the certificate advertised by the
     * selected endpoint. SecureChannel key material is used only when it is the same endpoint
     * identity; otherwise the key is resolved from the certificate manager by endpoint thumbprint.
     */
    if (securityConfiguration.getKeyPair() != null
        && securityConfiguration.getServerCertificate() != null
        && serverCertificateMatchesEndpoint(securityConfiguration, session)) {
      return securityConfiguration.getKeyPair();
    }

    ByteString endpointCertificateBytes = session.getEndpoint().getServerCertificate();

    if (endpointCertificateBytes == null || endpointCertificateBytes.isNullOrEmpty()) {
      throw new UaException(
          StatusCodes.Bad_ConfigurationError,
          "enhanced user-token negotiation requires an advertised server certificate");
    }

    X509Certificate endpointCertificate =
        CertificateUtil.decodeCertificate(endpointCertificateBytes.bytesOrEmpty());

    return server
        .getConfig()
        .getCertificateManager()
        .getKeyPair(CertificateUtil.thumbprint(endpointCertificate))
        .orElseThrow(
            () ->
                new UaException(
                    StatusCodes.Bad_ConfigurationError,
                    "no server application key pair found for advertised certificate"));
  }

  private static boolean serverCertificateMatchesEndpoint(
      SecurityConfiguration securityConfiguration, Session session) throws UaException {

    ByteString endpointCertificateBytes = session.getEndpoint().getServerCertificate();

    return endpointCertificateBytes != null
        && endpointCertificateBytes.equals(securityConfiguration.getServerCertificateBytes());
  }

  /**
   * Drain session listener callbacks, stop accepting queued listener work, and close all sessions.
   *
   * <p>The first caller performs cleanup. Concurrent callers outside a listener callback wait for
   * that cleanup to complete so their caller can safely continue to namespace teardown. If the
   * caller is itself a session listener callback, shutdown stops the queue without waiting for the
   * current callback and discards queued callbacks that have not started.
   */
  void shutdown() {
    boolean sessionListenerCallback = isSessionListenerCallback();

    beginShutdown();

    if (!shutdownState.compareAndSet(ShutdownState.REQUESTED, ShutdownState.CLEANUP_STARTED)) {
      if (!sessionListenerCallback) {
        awaitShutdownComplete();
      }
      return;
    }

    try {
      sessionListenerTaskQueue.shutdown(!sessionListenerCallback);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      logger.warn("Interrupted while waiting for session listener callbacks to complete", e);
    } finally {
      try {
        List<Session> sessions;
        synchronized (sessionLock) {
          sessions = getAllSessions();
          createdSessions.clear();
          activeSessions.clear();
        }

        sessions.forEach(s -> s.close(true));
      } finally {
        shutdownComplete.countDown();
      }
    }
  }

  /**
   * Mark shutdown as requested before transports are unbound.
   *
   * <p>This early transition is visible to {@link #createSession(ServiceRequestContext,
   * CreateSessionRequest)}, causing new CreateSession requests to fail with {@link
   * StatusCodes#Bad_Shutdown} while the server drains already-started session listener callbacks.
   */
  void beginShutdown() {
    shutdownState.compareAndSet(ShutdownState.RUNNING, ShutdownState.REQUESTED);
  }

  private boolean isShutdownRequested() {
    return shutdownState.get() != ShutdownState.RUNNING;
  }

  /**
   * Return whether the current thread is executing a {@link SessionListener} notification.
   *
   * <p>{@link OpcUaServer#shutdown()} uses this to avoid returning an in-progress shutdown future
   * to the callback that the in-progress shutdown is waiting for.
   *
   * @return {@code true} if the current thread is inside session listener notification dispatch.
   */
  boolean isSessionListenerCallback() {
    return Boolean.TRUE.equals(sessionListenerCallback.get());
  }

  /** Wait for the first shutdown caller to finish the session cleanup pass. */
  private void awaitShutdownComplete() {
    try {
      shutdownComplete.await();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      logger.warn("Interrupted while waiting for session shutdown to complete", e);
    }
  }

  /** Queue a session-created notification unless shutdown has already started. */
  private void fireSessionCreated(Session session) {
    if (!isShutdownRequested()) {
      sessionListenerTaskQueue.execute(
          () -> {
            if (!isShutdownRequested()) {
              notifySessionCreated(session);
            }
          });
    }
  }

  /** Queue a session-closed notification unless shutdown has already started. */
  private void fireSessionClosed(Session session) {
    if (!isShutdownRequested()) {
      sessionListenerTaskQueue.execute(
          () -> {
            if (!isShutdownRequested()) {
              notifySessionClosed(session);
            }
          });
    }
  }

  /** Notify listeners until shutdown starts or the listener snapshot is exhausted. */
  private void notifySessionCreated(Session session) {
    withSessionListenerCallback(
        () -> {
          for (SessionListener listener : sessionListeners) {
            if (isShutdownRequested()) {
              break;
            }

            listener.onSessionCreated(session);
          }
        });
  }

  /** Notify listeners until shutdown starts or the listener snapshot is exhausted. */
  private void notifySessionClosed(Session session) {
    withSessionListenerCallback(
        () -> {
          for (SessionListener listener : sessionListeners) {
            if (isShutdownRequested()) {
              break;
            }

            listener.onSessionClosed(session);
          }
        });
  }

  /** Run listener dispatch with re-entrant shutdown detection enabled for the current thread. */
  private void withSessionListenerCallback(Runnable notification) {
    Boolean previous = sessionListenerCallback.get();
    sessionListenerCallback.set(true);
    try {
      notification.run();
    } finally {
      if (previous != null) {
        sessionListenerCallback.set(previous);
      } else {
        sessionListenerCallback.remove();
      }
    }
  }

  private SecurityConfiguration createSecurityConfiguration(SecureChannel secureChannel)
      throws UaException {
    SecurityPolicy securityPolicy = secureChannel.getSecurityPolicy();
    MessageSecurityMode securityMode = secureChannel.getMessageSecurityMode();

    X509Certificate clientCertificate = null;
    List<X509Certificate> clientCertificateChain = null;

    KeyPair keyPair = null;
    X509Certificate serverCertificate = null;
    List<X509Certificate> serverCertificateChain = null;

    if (securityPolicy != SecurityPolicy.None) {
      clientCertificate = secureChannel.getRemoteCertificate();
      clientCertificateChain = secureChannel.getRemoteCertificateChain();

      ByteString thumbprint = ByteString.of(sha1(secureChannel.getLocalCertificateBytes().bytes()));

      keyPair =
          server
              .getConfig()
              .getCertificateManager()
              .getKeyPair(thumbprint)
              .orElseThrow(() -> new UaException(StatusCodes.Bad_ConfigurationError));

      serverCertificate =
          server
              .getConfig()
              .getCertificateManager()
              .getCertificate(thumbprint)
              .orElseThrow(() -> new UaException(StatusCodes.Bad_ConfigurationError));

      serverCertificateChain =
          server
              .getConfig()
              .getCertificateManager()
              .getCertificateChain(thumbprint)
              .map(List::of)
              .orElseThrow(() -> new UaException(StatusCodes.Bad_ConfigurationError));
    }

    return new SecurityConfiguration(
        securityPolicy,
        securityMode,
        keyPair,
        serverCertificate,
        serverCertificateChain,
        clientCertificate,
        clientCertificateChain,
        secureChannel.getChannelThumbprint());
  }

  /**
   * @param endpoint an {@link EndpointDescription}.
   * @param requestedEndpointUrl an endpoint URL.
   * @return {@code true} if the host in {@code endpoint} matches the host in {@code
   *     requestedEndpointUrl}.
   */
  private static boolean endpointMatchesUrl(
      EndpointDescription endpoint, String requestedEndpointUrl) {
    String endpointHost = EndpointUtil.getHost(requireNonNullElse(endpoint.getEndpointUrl(), ""));
    String requestedHost = EndpointUtil.getHost(requireNonNullElse(requestedEndpointUrl, ""));

    return requireNonNullElse(endpointHost, "")
        .equalsIgnoreCase(requireNonNullElse(requestedHost, ""));
  }

  /**
   * Strip the non-essential fields from an EndpointDescription and its ApplicationDescription for
   * return by the CreateSession service.
   *
   * <p>See Part 4, 5.6.6.2 for details.
   *
   * @param endpoint the {@link EndpointDescription} to strip non-essential fields from.
   * @return a new {@link EndpointDescription} with only the essential fields.
   */
  private static EndpointDescription stripNonEssentialFields(EndpointDescription endpoint) {
    // It is recommended that Servers only include the server.applicationUri, endpointUrl,
    // securityMode, securityPolicyUri, userIdentityTokens, transportProfileUri, and
    // securityLevel with all other parameters set to null. Only the recommended parameters
    // shall be verified by the client.
    ApplicationDescription applicationDescription = endpoint.getServer();
    ApplicationDescription newApplicationDescription =
        new ApplicationDescription(
            applicationDescription.getApplicationUri(),
            null,
            null,
            ApplicationType.Server,
            null,
            null,
            null);
    return new EndpointDescription(
        endpoint.getEndpointUrl(),
        newApplicationDescription,
        ByteString.NULL_VALUE,
        endpoint.getSecurityMode(),
        endpoint.getSecurityPolicyUri(),
        endpoint.getUserIdentityTokens(),
        endpoint.getTransportProfileUri(),
        endpoint.getSecurityLevel());
  }

  public ActivateSessionResponse activateSession(
      ServiceRequestContext context, ActivateSessionRequest request) throws UaException {
    long secureChannelId = context.getSecureChannel().getChannelId();
    NodeId authToken = request.getRequestHeader().getAuthenticationToken();
    SignedSoftwareCertificate[] clientSoftwareCertificates =
        requireNonNullElse(
            request.getClientSoftwareCertificates(), new SignedSoftwareCertificate[0]);

    Session session = createdSessions.get(authToken);

    if (session == null) {
      session = activeSessions.get(authToken);

      if (session == null) {
        throw new UaException(StatusCodes.Bad_SessionIdInvalid);
      } else {
        SecurityConfiguration securityConfiguration = session.getSecurityConfiguration();

        if (session.getSecureChannelId() == secureChannelId) {
          verifyClientSignature(session, request, securityConfiguration, session.getEndpoint());

          /*
           * Identity change
           */
          UserIdentityToken identityToken =
              decodeIdentityToken(
                  request.getUserIdentityToken(), session.getEndpoint().getUserIdentityTokens());

          Identity identity =
              validateIdentityToken(session, identityToken, request.getUserTokenSignature());

          StatusCode[] results = new StatusCode[clientSoftwareCertificates.length];
          Arrays.fill(results, StatusCode.GOOD);

          ByteString serverNonce = NonceUtil.generateNonce(32);

          session.setClientAddress(context.clientAddress());
          session.setIdentity(identity, identityToken);
          session.setLastNonce(serverNonce);
          session.setLocaleIds(request.getLocaleIds());

          ExtensionObject additionalHeader =
              activateSessionAdditionalHeader(request, securityConfiguration, session);

          return new ActivateSessionResponse(
              createResponseHeader(request, StatusCode.GOOD, additionalHeader),
              serverNonce,
              results,
              new DiagnosticInfo[0]);
        } else {
          /*
           * Reactivation signatures are bound to the SecureChannel carrying this request. Verify
           * with the candidate channel configuration before moving the Session to it.
           */
          SecurityConfiguration newSecurityConfiguration =
              createSecurityConfiguration(context.getSecureChannel());

          EndpointDescription endpoint = findSessionEndpoint(context);

          verifyClientSignature(session, request, newSecurityConfiguration, endpoint);

          ByteString clientCertificateBytes =
              context.getSecureChannel().getRemoteCertificateBytes();

          UserIdentityToken identityToken =
              decodeIdentityToken(
                  request.getUserIdentityToken(), session.getEndpoint().getUserIdentityTokens());

          /*
           * The user-token signature for enhanced (ECC/RSA-DH) policies is bound to the SecureChannel
           * carrying this request, and the IdentityValidator reconstructs those channel-bound inputs
           * from the Session's SecurityConfiguration and Endpoint. Make the candidate channel context
           * current before validating the identity token and roll it back if activation does not
           * complete, so a rejected reactivation leaves the Session bound to its existing channel.
           */
          EndpointDescription previousEndpoint = session.getEndpoint();
          session.setEndpoint(endpoint);
          session.setSecurityConfiguration(newSecurityConfiguration);

          boolean activated = false;

          try {
            Identity identity =
                validateIdentityToken(session, identityToken, request.getUserTokenSignature());

            boolean sameIdentity = identity.equalTo(session.getIdentity());

            // Compare against the original Session's client certificate (the prior configuration
            // captured above), confirming the same client is reactivating.
            boolean sameCertificate =
                Objects.equal(
                    clientCertificateBytes, securityConfiguration.getClientCertificateBytes());

            if (sameIdentity && sameCertificate) {
              session.setSecureChannelId(secureChannelId);

              logger.debug(
                  "Session id={} is now associated with secureChannelId={}",
                  session.getSessionId(),
                  secureChannelId);

              StatusCode[] results = new StatusCode[clientSoftwareCertificates.length];
              Arrays.fill(results, StatusCode.GOOD);

              ByteString serverNonce = NonceUtil.generateNonce(32);

              session.setClientAddress(context.clientAddress());
              session.setLastNonce(serverNonce);
              session.setLocaleIds(request.getLocaleIds());

              ExtensionObject additionalHeader =
                  activateSessionAdditionalHeader(request, newSecurityConfiguration, session);

              activated = true;

              return new ActivateSessionResponse(
                  createResponseHeader(request, StatusCode.GOOD, additionalHeader),
                  serverNonce,
                  results,
                  new DiagnosticInfo[0]);
            } else {
              throw new UaException(StatusCodes.Bad_SecurityChecksFailed);
            }
          } finally {
            if (!activated) {
              session.setEndpoint(previousEndpoint);
              session.setSecurityConfiguration(securityConfiguration);
            }
          }
        }
      }
    } else {
      if (secureChannelId != session.getSecureChannelId()) {
        throw new UaException(StatusCodes.Bad_SecurityChecksFailed);
      }

      verifyClientSignature(
          session, request, session.getSecurityConfiguration(), session.getEndpoint());

      UserIdentityToken identityToken =
          decodeIdentityToken(
              request.getUserIdentityToken(), session.getEndpoint().getUserIdentityTokens());

      Identity identity =
          validateIdentityToken(session, identityToken, request.getUserTokenSignature());

      // Move the session from created to active atomically with respect to the limit check in
      // createSession, so a concurrent CreateSession cannot evict a session that is activating.
      synchronized (sessionLock) {
        if (createdSessions.remove(authToken) == null) {
          // The session was closed (e.g. evicted at the session limit) during activation.
          throw new UaException(StatusCodes.Bad_SessionIdInvalid);
        }
        activeSessions.put(authToken, session);
      }

      StatusCode[] results = new StatusCode[clientSoftwareCertificates.length];
      Arrays.fill(results, StatusCode.GOOD);

      ByteString serverNonce = NonceUtil.generateNonce(32);

      session.setClientAddress(context.clientAddress());
      session.setIdentity(identity, identityToken);
      session.setLocaleIds(request.getLocaleIds());
      session.setLastNonce(serverNonce);

      ExtensionObject additionalHeader =
          activateSessionAdditionalHeader(request, session.getSecurityConfiguration(), session);

      return new ActivateSessionResponse(
          createResponseHeader(request, StatusCode.GOOD, additionalHeader),
          serverNonce,
          results,
          new DiagnosticInfo[0]);
    }
  }

  private EndpointDescription findSessionEndpoint(ServiceRequestContext context)
      throws UaException {
    return server.getApplicationContext().getEndpointDescriptions().stream()
        .filter(
            e -> {
              boolean transportMatch =
                  java.util.Objects.equals(
                      e.getTransportProfileUri(), context.getTransportProfile().getUri());

              boolean pathMatch =
                  java.util.Objects.equals(
                      EndpointUtil.getPath(e.getEndpointUrl()),
                      EndpointUtil.getPath(context.getEndpointUrl()));

              boolean securityPolicyMatch =
                  java.util.Objects.equals(
                      e.getSecurityPolicyUri(),
                      context.getSecureChannel().getSecurityPolicy().getUri());

              boolean securityModeMatch =
                  java.util.Objects.equals(
                      e.getSecurityMode(), context.getSecureChannel().getMessageSecurityMode());

              return transportMatch && pathMatch && securityPolicyMatch && securityModeMatch;
            })
        .findFirst()
        .orElseThrow(
            () -> {
              String message =
                  String.format(
                      "no matching endpoint found: transportProfile=%s, "
                          + "endpointUrl=%s, securityPolicy=%s, securityMode=%s",
                      context.getTransportProfile(),
                      context.getEndpointUrl(),
                      context.getSecureChannel().getSecurityPolicy(),
                      context.getSecureChannel().getMessageSecurityMode());

              return new UaException(StatusCodes.Bad_SecurityChecksFailed, message);
            });
  }

  private static void verifyClientSignature(
      Session session,
      ActivateSessionRequest request,
      SecurityConfiguration securityConfiguration,
      EndpointDescription endpoint)
      throws UaException {
    if (securityConfiguration.getSecurityPolicy() != SecurityPolicy.None) {
      SignatureData clientSignature = request.getClientSignature();
      SecurityPolicy securityPolicy = securityConfiguration.getSecurityPolicy();
      ByteString serverCertificateBs =
          securityPolicy.getProfile().secureChannelEnhancements()
              ? endpoint.getServerCertificate()
              : securityConfiguration.getServerCertificateBytes();
      ByteString lastNonceBs = session.getLastNonce();

      try {
        byte[] dataBytes =
            ChannelBoundSignatureData.clientSignatureData(
                securityPolicy.getProfile(),
                securityConfiguration.getChannelThumbprint(),
                lastNonceBs,
                serverCertificateBs,
                securityConfiguration.getServerChannelCertificateBytes(),
                securityConfiguration.getClientChannelCertificateBytes(),
                session.getClientNonce());

        try {
          verifyApplicationSignature(
              securityPolicy,
              securityConfiguration.getClientCertificate(),
              clientSignature,
              dataBytes);
        } catch (UaException e) {
          throw new UaException(StatusCodes.Bad_ApplicationSignatureInvalid, e);
        }
      } catch (UaException e) {
        // Maybe try again using the full certificate chain bytes instead

        ByteString serverCertificateChainBs =
            securityConfiguration.getServerCertificateChainBytes();

        if (serverCertificateBs.equals(serverCertificateChainBs)) {
          throw e;
        } else {
          byte[] dataBytes =
              ChannelBoundSignatureData.clientSignatureData(
                  securityPolicy.getProfile(),
                  securityConfiguration.getChannelThumbprint(),
                  lastNonceBs,
                  serverCertificateChainBs,
                  securityConfiguration.getServerChannelCertificateBytes(),
                  securityConfiguration.getClientChannelCertificateBytes(),
                  session.getClientNonce());

          try {
            verifyApplicationSignature(
                securityPolicy,
                securityConfiguration.getClientCertificate(),
                clientSignature,
                dataBytes);
          } catch (UaException ex) {
            throw new UaException(StatusCodes.Bad_ApplicationSignatureInvalid, e);
          }
        }
      }
    }
  }

  /**
   * Decode a {@link UserIdentityToken}.
   *
   * <p>Null or empty tokens are interpreted as {@link AnonymousIdentityToken}, as per the spec.
   *
   * @param identityTokenXo the {@link ExtensionObject} to decode.
   * @param tokenPolicies the {@link UserTokenPolicy}s from the Session's Endpoint.
   * @return a {@link UserIdentityToken} object.
   */
  @NonNull
  private UserIdentityToken decodeIdentityToken(
      @Nullable ExtensionObject identityTokenXo, @Nullable UserTokenPolicy[] tokenPolicies) {

    if (identityTokenXo != null && !identityTokenXo.isNull()) {
      Object identityToken;
      try {
        identityToken = identityTokenXo.decode(server.getStaticEncodingContext());
      } catch (Exception ignored) {
        identityToken = null;
      }

      if (identityToken instanceof UserIdentityToken) {
        return (UserIdentityToken) identityToken;
      }
    }

    String policyId =
        Stream.of(tokenPolicies)
            .filter(p -> p != null && p.getTokenType() == UserTokenType.Anonymous)
            .findFirst()
            .map(UserTokenPolicy::getPolicyId)
            .orElse(null);

    return new AnonymousIdentityToken(policyId);
  }

  private Identity validateIdentityToken(
      Session session, Object tokenObject, SignatureData tokenSignature) throws UaException {

    IdentityValidator identityValidator = server.getConfig().getIdentityValidator();
    UserTokenPolicy tokenPolicy = validatePolicyId(session, tokenObject);

    if (tokenObject instanceof UserIdentityToken) {
      return identityValidator.validateIdentityToken(
          session, (UserIdentityToken) tokenObject, tokenPolicy, tokenSignature);
    } else {
      throw new UaException(StatusCodes.Bad_IdentityTokenInvalid);
    }
  }

  /**
   * Validates the policyId on a {@link UserIdentityToken} Object is a policyId that exists on the
   * Endpoint that {@code session} is connected to.
   *
   * @param session the current {@link Session}
   * @param tokenObject the {@link UserIdentityToken} Object from the client.
   * @return the first {@link UserTokenPolicy} on the Endpoint matching the policyId.
   * @throws UaException if the token object is invalid or no matching policy is found.
   */
  private UserTokenPolicy validatePolicyId(Session session, Object tokenObject) throws UaException {
    if (tokenObject instanceof UserIdentityToken token) {
      String policyId = token.getPolicyId();

      UserTokenPolicy[] userIdentityTokens =
          requireNonNullElse(session.getEndpoint().getUserIdentityTokens(), new UserTokenPolicy[0]);

      Optional<UserTokenPolicy> policy =
          Stream.of(userIdentityTokens)
              .filter(t -> Objects.equal(policyId, t.getPolicyId()))
              .findFirst();

      return policy.orElseThrow(
          () ->
              new UaException(
                  StatusCodes.Bad_IdentityTokenInvalid, "policy not found: " + policyId));
    } else {
      throw new UaException(StatusCodes.Bad_IdentityTokenInvalid);
    }
  }

  public CloseSessionResponse closeSession(
      CloseSessionRequest request, ServiceRequestContext context) throws UaException {

    long secureChannelId = context.getSecureChannel().getChannelId();
    NodeId authToken = request.getRequestHeader().getAuthenticationToken();

    Session session = activeSessions.get(authToken);

    if (session != null) {
      if (session.getSecureChannelId() != secureChannelId) {
        throw new UaException(StatusCodes.Bad_SessionIdInvalid);
      } else {
        activeSessions.remove(authToken);
        session.close(request.getDeleteSubscriptions());
        return new CloseSessionResponse(createResponseHeader(request));
      }
    } else {
      session = createdSessions.get(authToken);

      if (session == null) {
        throw new UaException(StatusCodes.Bad_SessionIdInvalid);
      } else if (session.getSecureChannelId() != secureChannelId) {
        throw new UaException(StatusCodes.Bad_SessionIdInvalid);
      } else {
        createdSessions.remove(authToken);
        session.close(request.getDeleteSubscriptions());
        return new CloseSessionResponse(createResponseHeader(request));
      }
    }
  }

  private SignatureData getServerSignature(
      SecurityPolicy securityPolicy,
      SecurityConfiguration securityConfiguration,
      ByteString clientNonce,
      ByteString serverNonce)
      throws UaException {

    if (securityPolicy == SecurityPolicy.None) {
      return new SignatureData(null, null);
    } else {
      try {
        KeyPair keyPair = securityConfiguration.getKeyPair();
        ByteString clientCertificate = securityConfiguration.getClientCertificateBytes();

        if (keyPair == null || clientCertificate.isNullOrEmpty() || clientNonce.isNullOrEmpty()) {
          throw new UaException(StatusCodes.Bad_SecurityChecksFailed);
        }

        byte[] data =
            ChannelBoundSignatureData.serverSignatureData(
                securityPolicy.getProfile(),
                securityConfiguration.getChannelThumbprint(),
                clientNonce,
                securityConfiguration.getServerChannelCertificateBytes(),
                securityConfiguration.getClientChannelCertificateBytes(),
                serverNonce,
                clientCertificate);

        // ECC policies sign with ECDSA/EdDSA, RSA-DH and legacy policies with the policy's
        // algorithm; the wire algorithm URI is populated only for legacy policies (Part 4 §7.36).
        return ChannelBoundSignatureData.sign(securityPolicy, keyPair.getPrivate(), data);
      } catch (UaRuntimeException e) {
        throw new UaException(StatusCodes.Bad_SecurityChecksFailed);
      }
    }
  }

  private static void verifyApplicationSignature(
      SecurityPolicy securityPolicy,
      X509Certificate certificate,
      SignatureData signature,
      byte[] dataBytes)
      throws UaException {

    // ECC policies verify with ECDSA/EdDSA, RSA-DH and legacy policies with the policy's algorithm;
    // for legacy policies the algorithm is read from the wire URI (Part 4 §7.36).
    ChannelBoundSignatureData.verify(securityPolicy, certificate, dataBytes, signature);
  }
}
