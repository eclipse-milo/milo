/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import java.net.InetAddress;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.server.diagnostics.SessionDiagnostics;
import org.eclipse.milo.opcua.sdk.server.diagnostics.SessionSecurityDiagnostics;
import org.eclipse.milo.opcua.sdk.server.identity.Identity;
import org.eclipse.milo.opcua.sdk.server.subscriptions.SubscriptionManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.AnonymousIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.IssuedIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.UserIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.UserNameIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.X509IdentityToken;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Session {

  private static final int IDENTITY_HISTORY_MAX_SIZE = 10;

  private static final int CONCURRENT_CALL_LIMIT =
      Integer.getInteger("milo.session.concurrentCallLimit", 64);

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final List<LifecycleListener> listeners = new CopyOnWriteArrayList<>();

  private final SubscriptionManager subscriptionManager;

  private final LinkedList<String> clientUserIdHistory = new LinkedList<>();

  private final Map<ByteString, ContinuationPoint> browseContinuationPoints =
      new ConcurrentHashMap<>();

  private final Semaphore callSemaphore = new Semaphore(CONCURRENT_CALL_LIMIT, true);

  private volatile UserIdentityToken identityToken;
  private volatile Identity identity;

  private volatile ByteString lastNonce = ByteString.NULL_VALUE;

  private volatile long lastActivityNanos = System.nanoTime();
  private volatile ScheduledFuture<?> checkTimeoutFuture;

  private volatile EndpointDescription endpoint;
  private volatile long secureChannelId;
  private volatile SecurityConfiguration securityConfiguration;
  private volatile InetAddress clientAddress;
  private volatile String[] localeIds;
  private volatile DateTime lastContactTime;

  private final DateTime connectTime = DateTime.now();
  private final SessionDiagnostics sessionDiagnostics;
  private final SessionSecurityDiagnostics sessionSecurityDiagnostics;

  private final OpcUaServer server;
  private final NodeId sessionId;
  private final String sessionName;
  private final Duration sessionTimeout;
  private final ApplicationDescription clientDescription;
  private final String serverUri;
  private final UInteger maxResponseMessageSize;

  public Session(
      OpcUaServer server,
      NodeId sessionId,
      String sessionName,
      Duration sessionTimeout,
      ApplicationDescription clientDescription,
      String serverUri,
      UInteger maxResponseMessageSize,
      EndpointDescription endpoint,
      long secureChannelId,
      SecurityConfiguration securityConfiguration) {

    this.server = server;
    this.sessionId = sessionId;
    this.sessionName = sessionName;
    this.sessionTimeout = sessionTimeout;
    this.clientDescription = clientDescription;
    this.serverUri = serverUri;
    this.maxResponseMessageSize = maxResponseMessageSize;
    this.secureChannelId = secureChannelId;
    this.securityConfiguration = securityConfiguration;
    this.endpoint = endpoint;

    sessionDiagnostics = new SessionDiagnostics(this);
    sessionSecurityDiagnostics = new SessionSecurityDiagnostics(this);

    subscriptionManager = new SubscriptionManager(this, server);

    checkTimeoutFuture =
        server
            .getScheduledExecutorService()
            .schedule(this::checkTimeout, sessionTimeout.toNanos(), TimeUnit.NANOSECONDS);
  }

  public OpcUaServer getServer() {
    return server;
  }

  public long getSecureChannelId() {
    return secureChannelId;
  }

  public SecurityConfiguration getSecurityConfiguration() {
    return securityConfiguration;
  }

  public EndpointDescription getEndpoint() {
    return endpoint;
  }

  public @Nullable Identity getIdentity() {
    return identity;
  }

  public @Nullable UserIdentityToken getIdentityToken() {
    return identityToken;
  }

  public @Nullable UserTokenType getTokenType() {
    UserIdentityToken token = identityToken;

    return token != null ? getTokenType(token) : null;
  }

  /**
   * If this Server has a {@link RoleMapper} configured, use it to get the Roles mapped to this
   * Session.
   *
   * @return a List of Roles mapped to this Session, or an empty list if no {@link RoleMapper} is
   *     configured.
   * @see RoleMapper#getRoleIds(Identity)
   * @see RoleMapper#getRoleIds(Identity, String, EndpointDescription)
   */
  public Optional<List<NodeId>> getRoleIds() {
    return server
        .getRoleMapper()
        .map(
            roleMapper ->
                roleMapper.getRoleIds(identity, clientDescription.getApplicationUri(), endpoint));
  }

  /**
   * The client user id identifies the user of the client requesting an action. The client user id
   * is obtained from the UserIdentityToken passed in the ActivateSession call.
   *
   * <p>If the UserIdentityToken is a UserNameIdentityToken then the ClientUserId is the UserName.
   *
   * <p>If the UserIdentityToken is an X509IdentityToken then the ClientUserId is the X509 Subject
   * Name of the Certificate.
   *
   * <p>If the UserIdentityToken is an IssuedIdentityToken then the ClientUserId shall be a string
   * that represents the owner of the token. The best choice for the string depends on the type of
   * IssuedIdentityToken.
   *
   * <p>If an AnonymousIdentityToken was used, the value is null.
   *
   * @return the clientUserId of this {@link Session}.
   */
  public @Nullable String getClientUserId() {
    return getClientUserId(identityToken);
  }

  /**
   * @return a list containing the (possibly abbreviated) history of client user ids. This list may
   *     contain null entries.
   * @see #getClientUserId()
   */
  public List<String> getClientUserIdHistory() {
    synchronized (clientUserIdHistory) {
      return new ArrayList<>(clientUserIdHistory);
    }
  }

  public Map<ByteString, ContinuationPoint> getBrowseContinuationPoints() {
    return browseContinuationPoints;
  }

  public void setSecureChannelId(long secureChannelId) {
    this.secureChannelId = secureChannelId;
  }

  public void setIdentity(Identity identity, UserIdentityToken identityToken) {
    this.identity = identity;
    this.identityToken = identityToken;

    synchronized (clientUserIdHistory) {
      clientUserIdHistory.addLast(getClientUserId(identityToken));

      while (clientUserIdHistory.size() > IDENTITY_HISTORY_MAX_SIZE) {
        clientUserIdHistory.removeFirst();
      }
    }
  }

  public void setEndpoint(EndpointDescription endpoint) {
    this.endpoint = endpoint;
  }

  public void setSecurityConfiguration(SecurityConfiguration securityConfiguration) {
    this.securityConfiguration = securityConfiguration;
  }

  public void setClientAddress(InetAddress clientAddress) {
    this.clientAddress = clientAddress;
  }

  /**
   * Get the {@link InetAddress} of the client that activated this session.
   *
   * <p>The address is set or updated when time the session is activated or re-activated.
   *
   * @return the {@link InetAddress} of the client that activated this session.
   */
  public InetAddress getClientAddress() {
    return clientAddress;
  }

  public SessionDiagnostics getSessionDiagnostics() {
    return sessionDiagnostics;
  }

  public SessionSecurityDiagnostics getSessionSecurityDiagnostics() {
    return sessionSecurityDiagnostics;
  }

  public void addLifecycleListener(LifecycleListener listener) {
    listeners.add(listener);
  }

  void updateLastActivity() {
    lastActivityNanos = System.nanoTime();
    lastContactTime = DateTime.now();
  }

  public ApplicationDescription getClientDescription() {
    return clientDescription;
  }

  public String getServerUri() {
    return serverUri;
  }

  public Double getSessionTimeout() {
    return (double) sessionTimeout.toMillis();
  }

  public UInteger getMaxResponseMessageSize() {
    return maxResponseMessageSize;
  }

  public DateTime getConnectionTime() {
    return connectTime;
  }

  public DateTime getLastContactTime() {
    return lastContactTime;
  }

  void setLastNonce(ByteString lastNonce) {
    this.lastNonce = lastNonce;
  }

  public ByteString getLastNonce() {
    return lastNonce;
  }

  private void checkTimeout() {
    long elapsed = Math.abs(System.nanoTime() - lastActivityNanos);

    if (elapsed > sessionTimeout.toNanos()) {
      logger.debug("Session id={} lifetime expired ({}ms).", sessionId, sessionTimeout.toMillis());

      close(true);

      server.getDiagnosticsSummary().getSessionTimeoutCount().increment();
    } else {
      long remaining = sessionTimeout.toNanos() - elapsed;
      logger.trace(
          "Session id={} timeout scheduled for +{}s.",
          sessionId,
          Duration.ofNanos(remaining).getSeconds());

      checkTimeoutFuture =
          server
              .getScheduledExecutorService()
              .schedule(this::checkTimeout, remaining, TimeUnit.NANOSECONDS);
    }
  }

  public NodeId getSessionId() {
    return sessionId;
  }

  public String getSessionName() {
    return sessionName;
  }

  @Nullable
  public String[] getLocaleIds() {
    return localeIds;
  }

  public void setLocaleIds(@Nullable String[] localeIds) {
    this.localeIds = localeIds;
  }

  public SubscriptionManager getSubscriptionManager() {
    return subscriptionManager;
  }

  public Semaphore getCallSemaphore() {
    return callSemaphore;
  }

  void close(boolean deleteSubscriptions) {
    if (checkTimeoutFuture != null) {
      checkTimeoutFuture.cancel(false);
    }

    subscriptionManager.sessionClosed(deleteSubscriptions);

    listeners.forEach(listener -> listener.onSessionClosed(this, deleteSubscriptions));
  }

  @Nullable
  private static String getClientUserId(UserIdentityToken identityToken) {
    UserTokenType tokenType = getTokenType(identityToken);

    if (tokenType == null) {
      return null;
    }

    switch (tokenType) {
      case Anonymous:
        return null;

      case UserName:
        return ((UserNameIdentityToken) identityToken).getUserName();

      case Certificate:
        {
          try {
            ByteString bs = ((X509IdentityToken) identityToken).getCertificateData();
            X509Certificate certificate = CertificateUtil.decodeCertificate(bs.bytesOrEmpty());
            return certificate.getSubjectX500Principal().getName();
          } catch (Throwable t) {
            return null;
          }
        }
      case IssuedToken:
        return "IssuedToken";

      default:
        throw new IllegalStateException("unhandled UserIdentityToken: " + identityToken);
    }
  }

  private static UserTokenType getTokenType(UserIdentityToken identityToken) {
    UserTokenType identityType = null;
    if (identityToken instanceof AnonymousIdentityToken) {
      identityType = UserTokenType.Anonymous;
    } else if (identityToken instanceof UserNameIdentityToken) {
      identityType = UserTokenType.UserName;
    } else if (identityToken instanceof X509IdentityToken) {
      identityType = UserTokenType.Certificate;
    } else if (identityToken instanceof IssuedIdentityToken) {
      identityType = UserTokenType.IssuedToken;
    }
    return identityType;
  }

  public interface LifecycleListener {
    void onSessionClosed(Session session, boolean subscriptionsDeleted);
  }
}
