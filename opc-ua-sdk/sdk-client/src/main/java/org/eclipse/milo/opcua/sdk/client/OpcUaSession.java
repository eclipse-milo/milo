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
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class OpcUaSession extends ConcurrentHashMap<String, Object> implements UaSession {

  private volatile ByteString serverNonce = ByteString.NULL_VALUE;

  private final NodeId authToken;
  private final NodeId sessionId;
  private final String sessionName;
  private final double sessionTimeout;
  private final UInteger maxRequestSize;
  private final ByteString serverCertificate;
  private final SignedSoftwareCertificate[] serverSoftwareCertificates;

  public OpcUaSession(
      NodeId authToken,
      NodeId sessionId,
      String sessionName,
      double sessionTimeout,
      UInteger maxRequestSize,
      ByteString serverCertificate,
      SignedSoftwareCertificate[] serverSoftwareCertificates) {

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

  @Override
  public ByteString getServerCertificate() {
    return serverCertificate;
  }

  @Override
  public ByteString getServerNonce() {
    return serverNonce;
  }

  public void setServerNonce(ByteString serverNonce) {
    this.serverNonce = serverNonce;
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
