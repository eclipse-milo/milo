/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.config;

/**
 * A {@link FieldAddress} backed by an application-defined key, for standalone use without any
 * address space.
 *
 * @param key the application-defined key identifying the field source.
 */
public record KeyFieldAddress(String key) implements FieldAddress {

  /**
   * Create a {@link KeyFieldAddress} for {@code key}.
   *
   * @param key the application-defined key identifying the field source.
   * @return a new {@link KeyFieldAddress}.
   */
  public static KeyFieldAddress of(String key) {
    return new KeyFieldAddress(key);
  }
}
