/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.reverse;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * A one-shot selector registration with the reverse-connect manager.
 *
 * <p>The registration completes when one matching candidate is claimed. Closing the registration
 * removes the selector if it has not yet matched a candidate.
 */
public final class ReverseConnectRegistration implements AutoCloseable {

  private final ReverseConnectManager manager;
  private final ReverseConnectManager.SelectorRegistration registration;

  ReverseConnectRegistration(
      ReverseConnectManager manager, ReverseConnectManager.SelectorRegistration registration) {

    this.manager = manager;
    this.registration = registration;
  }

  /**
   * Get the manager-assigned selector registration identifier.
   *
   * <p>The identifier is useful when correlating application-side registration state with manager
   * diagnostics. It is not the same value as any candidate or channel identifier.
   *
   * @return the selector registration identifier.
   */
  public UUID id() {
    return registration.id;
  }

  /**
   * Get the one-shot claim future for this registration.
   *
   * <p>The future completes with a {@link ReverseConnectConnection} after the first matching
   * candidate is claimed. It completes exceptionally if the registration is closed before a match,
   * if the selector throws while matching, or if the manager shuts down while the registration is
   * still waiting.
   *
   * @return the claim future.
   */
  public CompletableFuture<ReverseConnectConnection> connectionFuture() {
    return registration.connectionFuture;
  }

  /**
   * Remove this selector registration if it has not claimed a candidate yet.
   *
   * <p>Closing is idempotent after the registration has matched because the manager removes
   * one-shot registrations when they claim a candidate.
   */
  @Override
  public void close() {
    manager.unregisterSelector(registration.id);
  }
}
