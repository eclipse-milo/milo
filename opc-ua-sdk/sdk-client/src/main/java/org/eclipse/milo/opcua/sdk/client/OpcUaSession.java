/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import com.google.common.base.MoreObjects;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class OpcUaSession extends ConcurrentHashMap<String, Object> implements UaSession {

  private record ReceiverEphemeralKey(
      ByteString publicKey, @Nullable SecurityPolicy securityPolicy) {}

  private volatile ByteString serverNonce = ByteString.NULL_VALUE;
  private volatile ByteString clientNonce = ByteString.NULL_VALUE;
  private volatile @Nullable ReceiverEphemeralKey userTokenReceiverEphemeralKey;
  private volatile @Nullable StatusCode lastActivateSessionServiceResult;

  private final NodeId authToken;
  private final NodeId sessionId;
  private final String sessionName;
  private final double sessionTimeout;
  private final UInteger maxRequestSize;
  private final ByteString serverCertificate;
  private final @Nullable EndpointDescription endpoint;
  private final @Nullable ByteString clientCertificate;
  private final SignedSoftwareCertificate[] serverSoftwareCertificates;

  public OpcUaSession(
      NodeId authToken,
      NodeId sessionId,
      String sessionName,
      double sessionTimeout,
      UInteger maxRequestSize,
      ByteString serverCertificate,
      SignedSoftwareCertificate[] serverSoftwareCertificates) {

    this(
        authToken,
        sessionId,
        sessionName,
        sessionTimeout,
        maxRequestSize,
        serverCertificate,
        serverSoftwareCertificates,
        null,
        null);
  }

  /**
   * Create a Session retaining the endpoint and client certificate used at creation.
   *
   * <p>These security inputs remain unchanged when reactivating on a replacement channel.
   */
  public OpcUaSession(
      NodeId authToken,
      NodeId sessionId,
      String sessionName,
      double sessionTimeout,
      UInteger maxRequestSize,
      ByteString serverCertificate,
      SignedSoftwareCertificate[] serverSoftwareCertificates,
      @Nullable EndpointDescription endpoint,
      @Nullable ByteString clientCertificate) {
    this.endpoint = endpoint;
    this.clientCertificate = clientCertificate;
    this.authToken = authToken;
    this.sessionId = sessionId;
    this.sessionName = sessionName;
    this.sessionTimeout = sessionTimeout;
    this.maxRequestSize = maxRequestSize;
    this.serverCertificate = serverCertificate;
    this.serverSoftwareCertificates = serverSoftwareCertificates;
  }

  @Override
  public NodeId getAuthenticationToken() {
    return authToken;
  }

  @Override
  public NodeId getSessionId() {
    return sessionId;
  }

  @Override
  public String getSessionName() {
    return sessionName;
  }

  @Override
  public Double getSessionTimeout() {
    return sessionTimeout;
  }

  @Override
  public UInteger getMaxRequestSize() {
    return maxRequestSize;
  }

  @Override
  public SignedSoftwareCertificate[] getServerSoftwareCertificates() {
    return serverSoftwareCertificates;
  }

  /**
   * Get the endpoint originally used to create this Session.
   *
   * @return the original endpoint, or empty for a manually constructed Session without metadata.
   */
  public Optional<EndpointDescription> getEndpoint() {
    return Optional.ofNullable(endpoint);
  }

  /**
   * Get the application certificate originally used by the client for this Session.
   *
   * <p>This is the DER-encoded leaf certificate, without any issuer certificates the client sent
   * with it in CreateSession.
   *
   * @return the original certificate, or empty for a manually constructed Session without metadata.
   */
  public Optional<ByteString> getClientCertificate() {
    return Optional.ofNullable(clientCertificate);
  }

  @Override
  public ByteString getServerCertificate() {
    return serverCertificate;
  }

  @Override
  public ByteString getServerNonce() {
    return serverNonce;
  }

  @Override
  public Optional<StatusCode> getLastActivateSessionServiceResult() {
    return Optional.ofNullable(lastActivateSessionServiceResult);
  }

  public void setServerNonce(ByteString serverNonce) {
    this.serverNonce = serverNonce;
  }

  /**
   * Get the client nonce originally sent in CreateSession.
   *
   * <p>ActivateSession signatures for SecureChannel-enhancement policies continue to include the
   * CreateSession client nonce when the session is reactivated on a new SecureChannel.
   *
   * @return the CreateSession client nonce.
   */
  public ByteString getClientNonce() {
    return clientNonce;
  }

  /**
   * Store the client nonce used to create this session.
   *
   * @param clientNonce the CreateSession client nonce.
   */
  public void setClientNonce(ByteString clientNonce) {
    this.clientNonce = clientNonce;
  }

  /**
   * Get the server session public key used for enhanced username-token activation.
   *
   * <p>The key is learned from the CreateSession or ActivateSession additional header and reused
   * when the session is reactivated or changes identity on the same negotiated user-token policy.
   * Both ECC and RSA-DH policies store their ephemeral receiver key here.
   *
   * @return the receiver public key advertised by the server for enhanced username-token
   *     encryption.
   */
  public Optional<ByteString> getUserTokenReceiverEphemeralPublicKey() {
    ReceiverEphemeralKey key = userTokenReceiverEphemeralKey;
    return key != null && key.publicKey().isNotNull()
        ? Optional.of(key.publicKey())
        : Optional.empty();
  }

  /**
   * Get the server session public key negotiated for {@code securityPolicy}, if one is currently
   * held.
   *
   * @param securityPolicy the enhanced user-token security policy to match.
   * @return the receiver public key advertised by the server for {@code securityPolicy}, or empty
   *     if no key is held or the held key was negotiated for a different policy.
   */
  public Optional<ByteString> getUserTokenReceiverEphemeralPublicKey(
      SecurityPolicy securityPolicy) {
    Objects.requireNonNull(securityPolicy, "securityPolicy");
    ReceiverEphemeralKey key = userTokenReceiverEphemeralKey;
    return key != null && key.publicKey().isNotNull() && securityPolicy.equals(key.securityPolicy())
        ? Optional.of(key.publicKey())
        : Optional.empty();
  }

  /**
   * Get the enhanced user-token security policy the held receiver ephemeral public key was
   * negotiated for, if known.
   *
   * @return the negotiated enhanced user-token security policy, or empty if no key or policy is
   *     held.
   */
  public Optional<SecurityPolicy> getUserTokenReceiverEphemeralPublicKeyPolicy() {
    ReceiverEphemeralKey key = userTokenReceiverEphemeralKey;
    return key != null && key.publicKey().isNotNull()
        ? Optional.ofNullable(key.securityPolicy())
        : Optional.empty();
  }

  /**
   * Store the server session public key returned during enhanced username-token negotiation.
   *
   * @param userTokenReceiverEphemeralPublicKey the receiver public key advertised by the server.
   */
  public void setUserTokenReceiverEphemeralPublicKey(
      ByteString userTokenReceiverEphemeralPublicKey) {
    setUserTokenReceiverEphemeralPublicKey(userTokenReceiverEphemeralPublicKey, null);
  }

  /**
   * Store the server session public key returned during enhanced username-token negotiation along
   * with the enhanced user-token policy it was negotiated for.
   *
   * @param userTokenReceiverEphemeralPublicKey the receiver public key advertised by the server.
   * @param securityPolicy the enhanced user-token security policy the key was negotiated for, or
   *     {@code null} if unknown.
   */
  public void setUserTokenReceiverEphemeralPublicKey(
      ByteString userTokenReceiverEphemeralPublicKey, @Nullable SecurityPolicy securityPolicy) {
    if (userTokenReceiverEphemeralPublicKey == null
        || userTokenReceiverEphemeralPublicKey.isNull()) {
      userTokenReceiverEphemeralKey = null;
    } else {
      userTokenReceiverEphemeralKey =
          new ReceiverEphemeralKey(userTokenReceiverEphemeralPublicKey, securityPolicy);
    }
  }

  /** Clear any stored server ephemeral public key for enhanced username-token encryption. */
  public void clearUserTokenReceiverEphemeralPublicKey() {
    userTokenReceiverEphemeralKey = null;
  }

  public void setLastActivateSessionServiceResult(
      @Nullable StatusCode lastActivateSessionServiceResult) {
    this.lastActivateSessionServiceResult = lastActivateSessionServiceResult;
  }

  @Nullable
  @Override
  public Object getAttribute(@NonNull String name) {
    return get(name);
  }

  @Nullable
  @Override
  public Object setAttribute(@NonNull String name, @NonNull Object value) {
    return put(name, value);
  }

  @Override
  public Object removeAttribute(@NonNull String name) {
    return remove(name);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("sessionId", sessionId)
        .add("sessionName", sessionName)
        .toString();
  }
}
