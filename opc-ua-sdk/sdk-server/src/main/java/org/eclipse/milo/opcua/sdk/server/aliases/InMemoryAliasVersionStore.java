/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.aliases;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * An {@link AliasVersionStore} that holds versions in memory only.
 *
 * <p><b>This store does not satisfy the Part 17 §6.3.1 persistence requirement</b>: all versions
 * are lost when the process exits, so {@code LastChange} regresses on every restart and Clients
 * relying on it must discard their caches. It exists so the manager is usable with zero
 * configuration in tests and demos; production applications must supply a durable implementation.
 *
 * <p>Thread-safe.
 */
public final class InMemoryAliasVersionStore implements AliasVersionStore {

  private final ConcurrentHashMap<ExpandedNodeId, UInteger> versions = new ConcurrentHashMap<>();

  @Override
  public Map<ExpandedNodeId, UInteger> load() {
    return Map.copyOf(versions);
  }

  @Override
  public void save(ExpandedNodeId categoryId, UInteger value) {
    versions.put(categoryId, value);
  }

  @Override
  public void delete(ExpandedNodeId categoryId) {
    versions.remove(categoryId);
  }
}
