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
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * SPI for persisting alias category {@code LastChange} versions across Server restarts.
 *
 * <p>Part 17 §6.3.1 requires the {@code LastChange} value of the root {@code Aliases} Object to be
 * persisted: Clients cache alias lookups keyed by it, and a value that silently resets after a
 * restart would leave those caches undetectably stale. The store persists nothing else — alias and
 * category definitions are the application's concern.
 *
 * <p>Entries are keyed by a namespace-URI-qualified {@link ExpandedNodeId} of the category's
 * NodeId, with the category's current {@code VersionTime} (seconds since 2000-01-01T00:00:00Z) as
 * the value. Keys carry the namespace URI rather than a namespace index because indices are a
 * runtime artifact of namespace-table registration order: a URI-qualified key stays valid across
 * restarts even when the table assigns the namespace a different index. A store only ever holds
 * entries written by {@link #save}, so the key set mirrors the categories whose versions have been
 * bumped at least once.
 *
 * <p>Implementations do not need to be thread-safe; the manager serializes all store access.
 */
public interface AliasVersionStore {

  /**
   * Load all persisted category versions.
   *
   * <p>Called once, at manager startup. A thrown exception fails startup deliberately: continuing
   * with silently reset versions would violate the Part 17 §6.3.1 persistence contract
   * undetectably. A store with nothing persisted yet returns an empty map.
   *
   * @return the persisted versions, keyed by namespace-URI-qualified category ExpandedNodeId; empty
   *     if nothing has been persisted.
   * @throws UaException if the persisted state cannot be read.
   */
  Map<ExpandedNodeId, UInteger> load() throws UaException;

  /**
   * Persist the version of a single category.
   *
   * <p>Called whenever a category's {@code LastChange} value is about to be bumped, <em>before</em>
   * the AddressSpace mutation the bump describes is applied and before the new value becomes
   * observable. A thrown exception aborts the mutating operation ({@code Bad_InternalError}), so
   * every value a Client can ever observe has been persisted first — after a restart, {@code
   * LastChange} can then never repeat an observed value for different content, which would leave
   * Client caches undetectably stale.
   *
   * @param categoryId the namespace-URI-qualified ExpandedNodeId of the category the version
   *     belongs to.
   * @param value the category's new {@code VersionTime} value.
   * @throws UaException if the value cannot be persisted.
   */
  void save(ExpandedNodeId categoryId, UInteger value) throws UaException;

  /**
   * Remove the persisted version of a category that no longer exists.
   *
   * <p>Called when a manager-created category is removed, so durable stores do not accumulate
   * entries forever. Best-effort cleanup: a thrown exception is logged by the caller and the
   * removal proceeds — a leftover entry is inert unless a category with the same NodeId is created
   * again, in which case the version sequence resumes from it. Within the same manager lifetime, an
   * in-memory high-water mark preserves the sequence even if the stored entry is deleted. Across
   * manager restarts, preserving the sequence for reused NodeIds requires retaining their entries.
   * The default implementation does nothing, for stores that prefer to keep (or externally expire)
   * old entries.
   *
   * @param categoryId the namespace-URI-qualified ExpandedNodeId of the removed category.
   * @throws UaException if the entry cannot be removed.
   */
  default void delete(ExpandedNodeId categoryId) throws UaException {}
}
