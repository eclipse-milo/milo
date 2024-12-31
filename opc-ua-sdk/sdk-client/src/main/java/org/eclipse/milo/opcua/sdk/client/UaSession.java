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

import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface UaSession {

  /**
   * Get the authentication token assigned by the server.
   *
   * @return a unique {@link NodeId} assigned by the server to the session.
   */
  NodeId getAuthenticationToken();

  /**
   * Get the session id assigned by the server.
   *
   * <p>This identifier is used to access the diagnostics information for the session in the server
   * address space. It is also used in the audit logs and any events that report information related
   * to the session.
   *
   * @return a unique {@link NodeId} assigned by the server to the session.
   */
  NodeId getSessionId();

  /**
   * @return the human-readable name assigned to this session by the client.
   */
  String getSessionName();

  /**
   * Get the revised session timeout, that is, the number of milliseconds a session may remain open
   * without activity.
   *
   * @return the revised session timeout.
   */
  Double getSessionTimeout();

  /**
   * @return the maximum allowable size for any request sent to the server.
   */
  UInteger getMaxRequestSize();

  /**
   * @return the last nonce received from the server.
   */
  ByteString getServerNonce();

  /**
   * @return the server application instance certificate.
   */
  ByteString getServerCertificate();

  /**
   * @return the server {@link SignedSoftwareCertificate}s.
   */
  SignedSoftwareCertificate[] getServerSoftwareCertificates();

  /**
   * Returns the attribute bound to {@code name} in this session, or <code>null</code> if no
   * attribute is bound under that name.
   *
   * @param name the name the attribute is bound to.
   * @return the attribute value.
   */
  @Nullable Object getAttribute(@NonNull String name);

  /**
   * Binds an attribute to this session, using the name specified.
   *
   * <p>If an object of the same name is already bound to the session, the object is replaced.
   *
   * @param name the name to bind the attribute to.
   * @param value the attribute value.
   * @return the previously bound value, or {@code null} if none exists.
   */
  @Nullable Object setAttribute(@NonNull String name, @NonNull Object value);

  /**
   * Removes the attribute bound with the specified name from this session.
   *
   * <p>If the session does not have an attribute bound with the specified name, this method does
   * nothing.
   *
   * @param name the name the attribute is bound to.
   * @return the previously bound value, or {@code null} if none exists.
   */
  @Nullable Object removeAttribute(@NonNull String name);
}
