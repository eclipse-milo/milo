/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.access;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.Nullable;

/**
 * The read access answers an invalidation covers: which Sessions and which Nodes.
 *
 * <p>Build a scope with the static factories, narrow it to one Session with {@link
 * #forSession(Session)} if needed, and pass it to {@link
 * AccessControlManager#invalidateReadAccess(ReadAccessScope)}. A scope that names the Nodes
 * explicitly, from {@link #node(NodeId)} or {@link #nodes(Collection)}, lets the {@link
 * ReadAccessCache} drop exactly those entries; {@link #namespace(UShort)} and {@link
 * #matching(Predicate)} cover a Node set the caller cannot enumerate cheaply, such as every Node of
 * a provider or device.
 *
 * <pre>{@code
 * // The user mapping behind every Session changed.
 * server.getAccessControlManager().invalidateReadAccess(ReadAccessScope.all());
 *
 * // One device's Nodes changed their permissions.
 * server.getAccessControlManager().invalidateReadAccess(ReadAccessScope.matching(nodeId -> isDeviceNode(nodeId)));
 * }</pre>
 */
public final class ReadAccessScope {

  private static final ReadAccessScope ALL = new ReadAccessScope(null, null, nodeId -> true);

  private final @Nullable Session session;
  private final @Nullable Set<NodeId> nodeIds;
  private final Predicate<NodeId> nodeIdFilter;

  private ReadAccessScope(
      @Nullable Session session, @Nullable Set<NodeId> nodeIds, Predicate<NodeId> nodeIdFilter) {

    this.session = session;
    this.nodeIds = nodeIds;
    this.nodeIdFilter = nodeIdFilter;
  }

  /**
   * Every answer for every Session.
   *
   * @return a scope covering every Session and every Node.
   */
  public static ReadAccessScope all() {
    return ALL;
  }

  /**
   * Every answer for one Session.
   *
   * @param session the Session whose answers may have changed.
   * @return a scope covering every Node for {@code session}.
   */
  public static ReadAccessScope session(Session session) {
    return ALL.forSession(session);
  }

  /**
   * Every Session's answer for one Node.
   *
   * @param nodeId the Node whose answers may have changed.
   * @return a scope covering {@code nodeId} for every Session.
   */
  public static ReadAccessScope node(NodeId nodeId) {
    return nodes(Set.of(nodeId));
  }

  /**
   * Every Session's answer for a set of Nodes.
   *
   * @param nodeIds the Nodes whose answers may have changed.
   * @return a scope covering {@code nodeIds} for every Session.
   */
  public static ReadAccessScope nodes(Collection<NodeId> nodeIds) {
    Set<NodeId> ids = Set.copyOf(nodeIds);

    return new ReadAccessScope(null, ids, ids::contains);
  }

  /**
   * Every Session's answer for every Node in a namespace.
   *
   * @param namespaceIndex the index of the namespace whose Nodes' answers may have changed.
   * @return a scope covering every Node in that namespace for every Session.
   */
  public static ReadAccessScope namespace(UShort namespaceIndex) {
    return matching(nodeId -> namespaceIndex.equals(nodeId.getNamespaceIndex()));
  }

  /**
   * Every Session's answer for every Node that matches a predicate.
   *
   * <p>The predicate is evaluated against each cached Node while the invalidation runs, so it
   * should be cheap and must not block.
   *
   * @param nodeIdFilter decides which Nodes' answers may have changed.
   * @return a scope covering the matching Nodes for every Session.
   */
  public static ReadAccessScope matching(Predicate<NodeId> nodeIdFilter) {
    return new ReadAccessScope(null, null, nodeIdFilter);
  }

  /**
   * The same Nodes as this scope, for one Session only.
   *
   * @param session the only Session the returned scope covers.
   * @return a scope covering this scope's Nodes for {@code session}.
   */
  public ReadAccessScope forSession(Session session) {
    return new ReadAccessScope(session, nodeIds, nodeIdFilter);
  }

  /**
   * Get the Session this scope is limited to, if any.
   *
   * @return the Session this scope is limited to, or empty if it covers every Session.
   */
  public Optional<Session> session() {
    return Optional.ofNullable(session);
  }

  /**
   * Get the Nodes this scope covers, when it names them explicitly.
   *
   * @return the Nodes this scope covers, or empty if it covers every Node or decides by predicate.
   */
  public Optional<Set<NodeId>> nodeIds() {
    return Optional.ofNullable(nodeIds);
  }

  /**
   * Whether this scope covers every Node.
   *
   * @return {@code true} if this scope covers every Node.
   */
  public boolean coversAllNodes() {
    return this == ALL || (nodeIds == null && nodeIdFilter == ALL.nodeIdFilter);
  }

  /**
   * Whether this scope covers answers for {@code session}.
   *
   * @param session the Session to test.
   * @return {@code true} if this scope covers answers for {@code session}.
   */
  public boolean includesSession(Session session) {
    return this.session == null || this.session == session;
  }

  /**
   * Whether this scope covers answers for {@code nodeId}.
   *
   * @param nodeId the Node to test.
   * @return {@code true} if this scope covers answers for {@code nodeId}.
   */
  public boolean includesNode(NodeId nodeId) {
    return nodeIdFilter.test(nodeId);
  }

  /**
   * Whether this scope covers the answer for {@code nodeId} under {@code session}.
   *
   * @param session the Session to test.
   * @param nodeId the Node to test.
   * @return {@code true} if this scope covers that answer.
   */
  public boolean includes(Session session, NodeId nodeId) {
    return includesSession(session) && includesNode(nodeId);
  }

  @Override
  public String toString() {
    String nodes = coversAllNodes() ? "all" : nodeIds != null ? nodeIds.toString() : "matching";
    String sessions = session != null ? session.getSessionId().toString() : "all";

    return "ReadAccessScope{sessions=" + sessions + ", nodes=" + nodes + "}";
  }
}
