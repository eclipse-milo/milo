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

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.model.objects.AliasNameCategoryType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AliasNameCategoryTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Owns the {@code LastChange} ({@code VersionTime}) state of alias categories: computes new values,
 * propagates bumps through ancestor categories, persists values through the {@link
 * AliasVersionStore}, and writes the {@code LastChange} Property Nodes.
 *
 * <p>Version maintenance is split into two phases so persistence happens <em>before</em> the
 * AddressSpace mutation a bump describes: {@link #prepare} computes the next value for each
 * affected category (and its ancestors) and saves it through the store, throwing — and thereby
 * aborting the mutation before it is applied — if a save fails; {@link #publishPending} then writes
 * the prepared values to the {@code LastChange} Property Nodes, called from a {@code finally} so
 * values whose mutation partially applied are still published. Because no value becomes observable
 * before it is persisted, a restart can never re-produce an observed {@code LastChange} value for
 * different content — the failure mode that would leave Client caches undetectably stale.
 *
 * <p>Store entries are keyed by namespace-URI-qualified {@link ExpandedNodeId}s (see {@link
 * AliasVersionStore}); this class converts to and from runtime NodeIds at the store boundary.
 *
 * <p>Internal to the aliases package. Not thread-safe on its own: the owning manager serializes
 * every call under its write lock, so version computation, persistence, and Property publication
 * never interleave.
 *
 * <p>{@code VersionTime} values are seconds since 2000-01-01T00:00:00Z. Each bump computes {@code
 * next = max(secondsSince2000(now), previous + 1)}, so values are strictly monotonic per category
 * even under clock rollback. Wraparound of the 32-bit range (year 2136) is not handled; Part 4
 * §7.43 defines no wraparound behavior.
 */
class AliasVersionManager {

  /** The {@code VersionTime} epoch, 2000-01-01T00:00:00Z, as a Unix epoch second. */
  private static final long VERSION_TIME_EPOCH_SECOND = 946684800L;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final Map<NodeId, UInteger> versions = new HashMap<>();

  /**
   * Values persisted by {@link #prepare} but not yet written to their {@code LastChange} Property
   * Nodes, in preparation order. Bounded by the categories one mutation call touches: every
   * prepare/publish pair runs under the owning manager's lock.
   */
  private final Map<NodeId, UInteger> pending = new LinkedHashMap<>();

  private final OpcUaServer server;
  private final AliasTypes aliasTypes;
  private final AliasVersionStore store;

  AliasVersionManager(OpcUaServer server, AliasVersionStore store) {
    this.server = server;
    this.aliasTypes = new AliasTypes(server);
    this.store = store;
  }

  /**
   * Load persisted category versions from the store and seed the in-memory version state.
   *
   * <p>Called once at manager startup, before any bump. Nothing is written to the {@code
   * LastChange} Property Nodes yet — the caller invokes {@link #publishLoaded} once the rest of
   * startup has succeeded, so a startup that fails after loading leaves no trace in the
   * AddressSpace. A stored entry whose namespace URI is not in the Server's namespace table cannot
   * belong to any live category; it is skipped with a warning and remains untouched in the store.
   *
   * @return an immutable copy of the loaded versions, keyed by category NodeId.
   * @throws UaException if the store cannot be read; the caller must fail startup, because
   *     continuing with silently reset versions would violate the persistence contract.
   */
  Map<NodeId, UInteger> loadPersisted() throws UaException {
    var loaded = new HashMap<NodeId, UInteger>();

    store
        .load()
        .forEach(
            (storeKey, value) -> {
              Optional<NodeId> categoryId = storeKey.toNodeId(server.getNamespaceTable());

              if (categoryId.isPresent()) {
                loaded.put(categoryId.get(), value);
              } else {
                logger.warn(
                    "Ignoring persisted LastChange with unregistered namespace: key={}, value={}",
                    storeKey.toParseableString(),
                    value);
              }
            });

    versions.putAll(loaded);

    return Map.copyOf(loaded);
  }

  /**
   * Write every version loaded by {@link #loadPersisted} to its category's {@code LastChange}
   * Property Node, where that Node exists.
   *
   * <p>Called as the final step of a successful startup — after every step that can fail — so a
   * failed startup never leaves loaded values published.
   */
  void publishLoaded() {
    versions.forEach(this::writeLastChangeProperty);
  }

  /**
   * Compute and persist the next version of each category in {@code categoryIds} and of every
   * ancestor category reachable from them, without publishing anything yet.
   *
   * <p>Called <em>before</em> the AddressSpace mutation the bump describes. A category already
   * prepared since the last {@link #publishPending} is skipped, so a multi-entry mutation call
   * persists (and ultimately publishes) one new value per category, no matter how many entries
   * touch it.
   *
   * <p>Ancestors are discovered by walking inverse {@code Organizes} References while the parent is
   * an {@code AliasNameCategoryType} instance, stopping at (and including) the standard root {@code
   * Aliases} Object when reached. Categories without a {@code LastChange} Property (it is Optional
   * per category; the standard {@code TagVariables} and {@code Topics} Objects have none) still get
   * their version persisted and their in-memory value advanced — only the eventual Property write
   * is skipped.
   *
   * @param categoryIds the NodeIds of the categories directly affected by the impending mutation.
   * @throws UaException with {@code Bad_InternalError} if a store save fails; the caller must abort
   *     the mutation. Values saved before the failure stay pending and are still published, which
   *     at worst bumps a category without a content change — the safe direction.
   */
  void prepare(Collection<NodeId> categoryIds) throws UaException {
    Set<NodeId> affected = new LinkedHashSet<>();

    for (NodeId categoryId : categoryIds) {
      if (affected.add(categoryId)) {
        collectAncestorCategories(categoryId, affected);
      }
    }

    for (NodeId categoryId : affected) {
      if (pending.containsKey(categoryId)) {
        continue;
      }

      UInteger previous = versions.get(categoryId);
      long previousValue = previous != null ? previous.longValue() : 0L;

      long next = Math.max(secondsSince2000(), previousValue + 1);
      UInteger value = UInteger.valueOf(next);

      try {
        store.save(categoryId.expanded(server.getNamespaceTable()), value);
      } catch (Exception e) {
        throw new UaException(
            StatusCodes.Bad_InternalError,
            "failed to persist LastChange for category " + categoryId.toParseableString(),
            e);
      }

      versions.put(categoryId, value);
      pending.put(categoryId, value);
    }
  }

  /**
   * Write every value {@link #prepare} persisted since the last publish to its category's {@code
   * LastChange} Property Node, then clear the pending set.
   *
   * <p>Called from a {@code finally} after the mutation, so prepared values are published even when
   * the mutation partially applied. Publishing when nothing is pending is a no-op. A Property write
   * failure is logged and the remaining values still publish: the value is already persisted and in
   * memory, and a secondary failure must not mask the mutation's own outcome.
   */
  void publishPending() {
    pending.forEach(
        (categoryId, value) -> {
          try {
            writeLastChangeProperty(categoryId, value);
          } catch (RuntimeException e) {
            logger.warn(
                "Failed to write LastChange Property: category={}, value={}",
                categoryId.toParseableString(),
                value,
                e);
          }
        });

    pending.clear();
  }

  /**
   * Bump the version of a single category and its ancestor categories: {@link #prepare} and {@link
   * #publishPending} as one step, for callers with no surrounding mutation to order against.
   *
   * @param categoryId the NodeId of the category to bump.
   * @throws UaException with {@code Bad_InternalError} if a store save fails.
   */
  void touch(NodeId categoryId) throws UaException {
    try {
      prepare(List.of(categoryId));
    } finally {
      publishPending();
    }
  }

  /**
   * The current version of a category, if one has been loaded or computed.
   *
   * @param categoryId the NodeId of the category.
   * @return the category's current version, or empty if it has never been bumped or loaded.
   */
  Optional<UInteger> get(NodeId categoryId) {
    return Optional.ofNullable(versions.get(categoryId));
  }

  /**
   * The ancestor categories of {@code categoryId}: every {@code AliasNameCategoryType} instance
   * reachable by walking inverse {@code Organizes} References, up to and including the standard
   * root {@code Aliases} Object. {@code categoryId} itself is not included.
   *
   * <p>Subject to the visibility limitation documented on {@link #collectAncestorCategories}:
   * parent linkage stored only as a one-directional forward Reference on the parent's side is not
   * discovered.
   *
   * @param categoryId the NodeId of the category whose ancestors are collected.
   * @return the ancestor category NodeIds, in discovery order.
   */
  List<NodeId> getAncestorCategories(NodeId categoryId) {
    var ancestors = new LinkedHashSet<NodeId>();
    collectAncestorCategories(categoryId, ancestors);
    return List.copyOf(ancestors);
  }

  /**
   * Drop the version entry of a category that no longer exists, in memory and — best-effort — in
   * the store.
   *
   * <p>The store delete is cleanup, not correctness: a failure is logged and the removal proceeds,
   * and stores whose {@link AliasVersionStore#delete} is the default no-op simply keep the entry. A
   * leftover entry is inert unless a category with the same NodeId is created again, in which case
   * the monotonic version sequence resumes from the persisted value — which is the safe direction.
   *
   * @param categoryId the NodeId of the removed category.
   */
  void remove(NodeId categoryId) {
    versions.remove(categoryId);
    pending.remove(categoryId);

    try {
      store.delete(categoryId.expanded(server.getNamespaceTable()));
    } catch (Exception e) {
      logger.warn(
          "Failed to delete persisted LastChange for category {}",
          categoryId.toParseableString(),
          e);
    }
  }

  private long secondsSince2000() {
    return Math.max(0L, Instant.now().getEpochSecond() - VERSION_TIME_EPOCH_SECOND);
  }

  /**
   * Walk inverse {@code Organizes} References from {@code categoryId}, adding every ancestor that
   * is an {@code AliasNameCategoryType} instance to {@code affected}, stopping at (and including)
   * the standard root {@code Aliases} Object.
   *
   * <p>References are read through the AddressSpaceManager, which aggregates every registered
   * NodeManager, so linkage stored by either side's NodeManager is visible as long as both
   * directions were recorded. A parent linkage recorded only as a one-directional forward Reference
   * on the parent category's side — possible when References are added out-of-band, e.g. by a
   * NodeSet loader that does not write inverses — is invisible from the child and is missed, so
   * version bumps do not propagate across such an edge.
   */
  private void collectAncestorCategories(NodeId categoryId, Set<NodeId> affected) {
    Deque<NodeId> queue = new ArrayDeque<>();
    queue.push(categoryId);

    while (!queue.isEmpty()) {
      NodeId current = queue.pop();

      List<Reference> references =
          server
              .getAddressSpaceManager()
              .getManagedReferences(current, Reference.ORGANIZED_BY_PREDICATE);

      for (Reference reference : references) {
        Optional<NodeId> parentId =
            reference.getTargetNodeId().toNodeId(server.getNamespaceTable());

        parentId.ifPresent(
            id -> {
              if (NodeIds.Aliases.equals(id)) {
                // The standard root: include it, but never walk beyond it.
                affected.add(id);
              } else if (aliasTypes.isAliasNameCategoryInstance(id) && affected.add(id)) {
                queue.push(id);
              }
            });
      }
    }
  }

  /**
   * Write {@code value} to the category's {@code LastChange} Property Node, if that Node exists.
   *
   * <p>The Property is never created here: it is Optional per category, and whether a category has
   * one is decided when the category is materialized (or by the NodeSet that defined it).
   */
  private void writeLastChangeProperty(NodeId categoryId, UInteger value) {
    Optional<UaNode> node = server.getAddressSpaceManager().getManagedNode(categoryId);

    node.ifPresent(
        categoryNode -> {
          if (categoryNode instanceof AliasNameCategoryTypeNode typedNode) {
            // setLastChange would create the Property if absent; only write where it exists.
            if (typedNode.getLastChangeNode() != null) {
              typedNode.setLastChange(value);
            }
          } else {
            Optional<VariableNode> propertyNode =
                categoryNode.getPropertyNode(AliasNameCategoryType.LAST_CHANGE);

            propertyNode.ifPresent(
                property -> property.setValue(new DataValue(new Variant(value))));
          }
        });
  }
}
