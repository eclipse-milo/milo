/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes.instantiation;

import java.util.List;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;

/**
 * A read-only view of the complete constructed-but-unpublished node graph, passed to {@code onNode}
 * hooks during apply so cross-node wiring sees fully-built neighbors before anything is committed
 * or browsable.
 *
 * <p>The interface is part of the request surface (hooks are request data); the apply engine
 * provides the implementation.
 */
public interface StagedGraph {

  /**
   * @return the staged root node.
   */
  UaNode root();

  /**
   * @param path the occurrence path to look up.
   * @return the staged node realized at {@code path}, if one was planned and staged.
   */
  Optional<UaNode> node(BrowsePath path);

  /**
   * @return every staged node, browse-path ordered, root first.
   */
  List<UaNode> nodes();
}
