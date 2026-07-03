/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.security;

/**
 * A credential returned by a {@link KeyCredentialStore}: an identifier (e.g. a username) and its
 * secret.
 *
 * <p>The secret is a {@code char[]} so it can be wiped; never convert it to a {@link String}.
 * Callers own the returned array and must wipe it ({@code Arrays.fill(secret, '\0')}) after use.
 *
 * <p>Record caveat: the array component gives instances identity-based {@code equals}/{@code
 * hashCode}; credentials are not value-compared, so this is acceptable.
 *
 * @param credentialId the credential identifier, e.g. a username.
 * @param secret the secret; owned by the receiver, which must wipe it after use.
 */
public record KeyCredential(String credentialId, char[] secret) {

  @Override
  public String toString() {
    return "KeyCredential{credentialId=%s}".formatted(credentialId);
  }
}
