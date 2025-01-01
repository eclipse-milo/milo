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

import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

public class UaFolderNode extends UaObjectNode {

  public UaFolderNode(
      UaNodeContext context, NodeId nodeId, QualifiedName browseName, LocalizedText displayName) {

    super(
        context,
        nodeId,
        browseName,
        displayName,
        LocalizedText.NULL_VALUE,
        UInteger.MIN,
        UInteger.MIN);

    addReference(
        new Reference(getNodeId(), NodeIds.HasTypeDefinition, NodeIds.FolderType.expanded(), true));
  }

  /**
   * Add an 'Organizes' reference from this folder to {@code node} and an inverse 'Organized By'
   * reference from {@code node} back to this folder.
   *
   * @param node the node to be organized by this folder.
   */
  public void addOrganizes(UaNode node) {
    addReference(new Reference(getNodeId(), NodeIds.Organizes, node.getNodeId().expanded(), true));
  }

  /**
   * Remove the 'Organizes' reference from this folder to {@code node} and the inverse 'Organized
   * By' reference from {@code node} back to this folder.
   *
   * @param node the node to be organized by this folder.
   */
  public void removeOrganizes(UaNode node) {
    removeReference(
        new Reference(getNodeId(), NodeIds.Organizes, node.getNodeId().expanded(), true));
  }
}
