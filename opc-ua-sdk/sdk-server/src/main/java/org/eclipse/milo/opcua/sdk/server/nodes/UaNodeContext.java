/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes;

import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;

public interface UaNodeContext {

  /**
   * @return the {@link OpcUaServer} instance.
   */
  OpcUaServer getServer();

  /**
   * @return the {@link NodeManager} for this context.
   */
  NodeManager<UaNode> getNodeManager();

  /**
   * @return the Server's {@link NamespaceTable}.
   */
  default NamespaceTable getNamespaceTable() {
    return getServer().getNamespaceTable();
  }
}
