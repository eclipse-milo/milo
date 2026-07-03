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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * An in-memory {@link KeyCredentialStore}.
 *
 * <p>Copy semantics: {@link #put} stores a copy of the secret; {@link #remove} (and replacement by
 * a subsequent {@code put}) wipes the stored copy; {@link #lookup} returns a fresh copy that the
 * caller must wipe after use. All methods are thread-safe.
 */
public final class InMemoryKeyCredentialStore implements KeyCredentialStore {

  private final Map<String, KeyCredential> credentials = new HashMap<>();

  /**
   * Store a credential for a resource URI, replacing (and wiping) any previously stored one.
   *
   * @param resourceUri the URI identifying the resource the credential authenticates against.
   * @param credentialId the credential identifier, e.g. a username.
   * @param secret the secret; a copy is stored, so the caller retains ownership of (and should
   *     wipe) the passed array.
   */
  public synchronized void put(String resourceUri, String credentialId, char[] secret) {
    KeyCredential previous =
        credentials.put(resourceUri, new KeyCredential(credentialId, secret.clone()));

    if (previous != null) {
      Arrays.fill(previous.secret(), '\0');
    }
  }

  /**
   * Remove the credential stored for a resource URI, wiping the stored secret copy.
   *
   * @param resourceUri the URI the credential was stored under.
   */
  public synchronized void remove(String resourceUri) {
    KeyCredential removed = credentials.remove(resourceUri);

    if (removed != null) {
      Arrays.fill(removed.secret(), '\0');
    }
  }

  @Override
  public synchronized Optional<KeyCredential> lookup(String resourceUri) {
    KeyCredential credential = credentials.get(resourceUri);

    if (credential == null) {
      return Optional.empty();
    }

    return Optional.of(new KeyCredential(credential.credentialId(), credential.secret().clone()));
  }
}
