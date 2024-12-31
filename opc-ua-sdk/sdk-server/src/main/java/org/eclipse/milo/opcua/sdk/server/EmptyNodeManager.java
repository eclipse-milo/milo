/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.nodes.Node;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * An empty {@link NodeManager}.
 *
 * <p>Gets are empty, Add and remove operations are no-ops, and {@link #containsNode(NodeId)}
 * returns {@code false}.
 *
 * @param <T> the type of {@link Node}.
 */
public class EmptyNodeManager<T extends Node> implements NodeManager<T> {

  /** An {@link EmptyNodeManager} for {@link UaNode}s. */
  public static EmptyNodeManager<UaNode> INSTANCE = new EmptyNodeManager<>();

  public EmptyNodeManager() {}

  @Override
  public boolean containsNode(NodeId nodeId) {
    return false;
  }

  @Override
  public boolean containsNode(ExpandedNodeId nodeId, NamespaceTable namespaceTable) {
    return false;
  }

  @Override
  public Optional<T> addNode(T node) {
    return Optional.empty();
  }

  @Override
  public Optional<T> getNode(NodeId nodeId) {
    return Optional.empty();
  }

  @Override
  public Optional<T> getNode(ExpandedNodeId nodeId, NamespaceTable namespaceTable) {
    return Optional.empty();
  }

  @Override
  public Optional<T> removeNode(NodeId nodeId) {
    return Optional.empty();
  }

  @Override
  public Optional<T> removeNode(ExpandedNodeId nodeId, NamespaceTable namespaceTable) {
    return Optional.empty();
  }

  @Override
  public void addReference(Reference reference) {}

  @Override
  public void addReferences(Reference reference, NamespaceTable namespaceTable) {}

  @Override
  public void removeReferences(Reference reference, NamespaceTable namespaceTable) {}

  @Override
  public void removeReference(Reference reference) {}

  @Override
  public List<Reference> getReferences(NodeId nodeId) {
    return Collections.emptyList();
  }

  @Override
  public List<Reference> getReferences(NodeId nodeId, Predicate<Reference> filter) {
    return Collections.emptyList();
  }
}
