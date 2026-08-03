/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.test.aliases;

import static org.eclipse.milo.opcua.sdk.test.aliases.AliasTestSupport.readLastChange;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasVersionStore;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * An in-memory {@link AliasVersionStore} that records every save together with the {@code
 * LastChange} Property value visible at save time, letting tests assert the persist-then-publish
 * ordering, inspect exactly which categories were persisted (and that their store keys are
 * namespace-URI-qualified), and count how many times a mutation call persisted a category's {@code
 * LastChange}.
 */
final class RecordingVersionStore implements AliasVersionStore {

  /**
   * One recorded {@link #save}: the URI-qualified store key and its runtime NodeId resolution, the
   * persisted value, and the {@code LastChange} Property value that was observable at the moment
   * the store was called.
   */
  record SaveRecord(
      ExpandedNodeId storeKey,
      NodeId categoryId,
      UInteger value,
      @Nullable UInteger lastChangeAtSave) {}

  private final Map<ExpandedNodeId, UInteger> entries = new ConcurrentHashMap<>();
  private final List<SaveRecord> saves = new CopyOnWriteArrayList<>();

  private final OpcUaServer server;

  RecordingVersionStore(OpcUaServer server) {
    this.server = server;
  }

  @Override
  public Map<ExpandedNodeId, UInteger> load() {
    return Map.copyOf(entries);
  }

  @Override
  public void save(ExpandedNodeId categoryId, UInteger value) {
    NodeId localCategoryId = categoryId.toNodeId(server.getNamespaceTable()).orElseThrow();

    // Capture the Property value BEFORE recording, so tests can verify the manager persists
    // each version before publishing it via the Property.
    saves.add(
        new SaveRecord(
            categoryId, localCategoryId, value, readLastChange(server, localCategoryId)));
    entries.put(categoryId, value);
  }

  @Override
  public void delete(ExpandedNodeId categoryId) {
    entries.remove(categoryId);
  }

  List<SaveRecord> saves() {
    return saves;
  }

  /** The number of {@link #save} calls recorded for the category with {@code categoryId}. */
  long savesFor(NodeId categoryId) {
    return saves.stream().filter(record -> categoryId.equals(record.categoryId())).count();
  }
}
