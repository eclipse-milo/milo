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

import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.SecurityKeysListener;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateIdentity;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.jspecify.annotations.Nullable;

/**
 * What a client application supplies to a client transport: the endpoint to connect to, the local
 * certificate identity to present on secured connections, the validator for the server's
 * certificate, and encoding and timeout settings.
 */
public interface ClientApplicationContext {

  /**
   * Get the {@link EndpointDescription} the client is connecting to.
   *
   * @return the {@link EndpointDescription} the client is connecting to.
   */
  EndpointDescription getEndpoint();

  /**
   * Get the client certificate identity to present for {@code securityPolicyProfile}.
   *
   * <p>A secured connection cannot proceed without an identity; the transport fails
   * OpenSecureChannel with {@code Bad_ConfigurationError} when this returns empty for a policy
   * other than {@code None}.
   *
   * @param securityPolicyProfile the selected endpoint security-policy profile.
   * @return the selected identity, or empty when the client has none for the profile.
   * @throws UaException if identity selection fails while evaluating candidates.
   */
  Optional<CertificateIdentity> getCertificateIdentity(SecurityPolicyProfile securityPolicyProfile)
      throws UaException;

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

  /**
   * Get the {@link SecurityKeysListener}, if configured.
   *
   * <p>When non-null, the listener is invoked after symmetric keys are derived during an
   * OpenSecureChannel handshake.
   *
   * @return the {@link SecurityKeysListener}, or {@code null} if key logging is not enabled.
   */
  default @Nullable SecurityKeysListener getSecurityKeysListener() {
    return null;
  }
}
