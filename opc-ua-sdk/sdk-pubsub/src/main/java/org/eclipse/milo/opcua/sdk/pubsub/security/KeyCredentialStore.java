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

import java.util.Optional;

/**
 * SPI resolving a resource URI to the {@link KeyCredential} used to authenticate against it.
 *
 * <p>The primary consumer is the SKS pull provider ({@code milo-sdk-pubsub-sks}), which looks up
 * credentials for the USERNAME identity path keyed by the SKS entry's {@code
 * server.applicationUri}; the store reaches the provider through its builder ({@code
 * SksSecurityKeyProvider.builder().keyCredentialStore(store)}), not through {@code PubSubBindings}.
 *
 * <p>Implementations must be thread-safe: {@code lookup} may be called from provider-owned executor
 * threads.
 */
public interface KeyCredentialStore {

  /**
   * Look up the credential for a resource URI.
   *
   * <p>The returned credential's {@code secret} is owned by the caller, which must wipe it (e.g.
   * {@code Arrays.fill(secret, '\0')}) after use.
   *
   * @param resourceUri the URI identifying the resource the credential authenticates against.
   * @return the {@link KeyCredential} for {@code resourceUri}, or empty if none is stored.
   */
  Optional<KeyCredential> lookup(String resourceUri);
}
