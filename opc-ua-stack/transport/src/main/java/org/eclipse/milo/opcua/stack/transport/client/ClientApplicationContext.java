/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;

public interface ClientApplicationContext {

  /**
   * Get the {@link EndpointDescription} the client is connecting to.
   *
   * @return the {@link EndpointDescription} the client is connecting to.
   */
  EndpointDescription getEndpoint();

  /**
   * Get the client {@link KeyPair}, if configured.
   *
   * <p>A KeyPair is required for secured connections.
   *
   * @return the client {@link KeyPair}, if configured.
   */
  Optional<KeyPair> getKeyPair();

  /**
   * Get the client application instance certificate, if configured.
   *
   * <p>An application instance certificate is required for secured connections.
   *
   * @return the client application instance certificate, if configured.
   */
  Optional<X509Certificate> getCertificate();

  /**
   * Get the client application instance certificate chain, if configured.
   *
   * <p>An application instance certificate chain is required for secured connections.
   *
   * @return the client application instance certificate chain, if configured.
   */
  Optional<X509Certificate[]> getCertificateChain();

  /**
   * Get the client's {@link CertificateValidator}.
   *
   * @return the client's {@link CertificateValidator}.
   */
  CertificateValidator getCertificateValidator();

  /**
   * Get the client's static {@link EncodingContext}.
   *
   * @return the client's static {@link EncodingContext}.
   */
  EncodingContext getEncodingContext();

  /**
   * Get the client request timeout to use when opening or renewing a secure channel.
   *
   * @return the client request timeout to use when opening or renewing a secure channel.
   */
  UInteger getRequestTimeout();
}
