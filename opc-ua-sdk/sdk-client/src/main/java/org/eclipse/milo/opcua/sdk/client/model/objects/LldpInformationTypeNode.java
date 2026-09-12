/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import com.digitalpetri.opcua.uanodeset.runtime.client.ClientMembers;
import com.digitalpetri.opcua.uanodeset.runtime.client.ClientViews;
import com.digitalpetri.opcua.uanodeset.runtime.members.MemberDeclaration;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

public class LldpInformationTypeNode extends BaseObjectTypeNode implements LldpInformationType {
  public LldpInformationTypeNode(
      OpcUaClient client,
      NodeId nodeId,
      NodeClass nodeClass,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions,
      UByte eventNotifier) {
    super(
        client,
        nodeId,
        nodeClass,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions,
        eventNotifier);
  }

  /**
   * Creates an independently owned view context retaining this exact existing node and its client.
   * Cached attributes remain shared even after SDK cache eviction. Close the returned context when
   * its views are no longer needed.
   */
  public static ClientViews createViews(LldpInformationTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable LldpRemoteStatisticsTypeNode getRemoteStatisticsNode() throws UaException {
    return ClientMembers.await(getRemoteStatisticsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable LldpRemoteStatisticsTypeNode>
      getRemoteStatisticsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        LldpRemoteStatisticsTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RemoteStatistics",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            true,
            "http://opcfoundation.org/UA/:RemoteStatistics (declaration i=18974, owner i=18973)"));
  }

  @Override
  public LldpLocalSystemTypeNode getLocalSystemDataNode() throws UaException {
    return ClientMembers.await(getLocalSystemDataNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends LldpLocalSystemTypeNode> getLocalSystemDataNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        LldpLocalSystemTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "LocalSystemData",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:LocalSystemData (declaration i=18980, owner i=18973)"));
  }

  @Override
  public FolderTypeNode getPortsNode() throws UaException {
    return ClientMembers.await(getPortsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends FolderTypeNode> getPortsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        FolderTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Ports",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:Ports (declaration i=18987, owner i=18973)"));
  }
}
