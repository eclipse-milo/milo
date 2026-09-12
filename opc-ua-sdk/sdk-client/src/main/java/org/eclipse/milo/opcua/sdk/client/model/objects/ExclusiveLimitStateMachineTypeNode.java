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

public class ExclusiveLimitStateMachineTypeNode extends FiniteStateMachineTypeNode
    implements ExclusiveLimitStateMachineType {
  public ExclusiveLimitStateMachineTypeNode(
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
  public static ClientViews createViews(ExclusiveLimitStateMachineTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public StateTypeNode getHighHighNode() throws UaException {
    return ClientMembers.await(getHighHighNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends StateTypeNode> getHighHighNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        StateTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "HighHigh",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:HighHigh (declaration i=9329, owner i=9318)"));
  }

  @Override
  public StateTypeNode getHighNode() throws UaException {
    return ClientMembers.await(getHighNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends StateTypeNode> getHighNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        StateTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "High",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:High (declaration i=9331, owner i=9318)"));
  }

  @Override
  public StateTypeNode getLowNode() throws UaException {
    return ClientMembers.await(getLowNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends StateTypeNode> getLowNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        StateTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Low",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:Low (declaration i=9333, owner i=9318)"));
  }

  @Override
  public StateTypeNode getLowLowNode() throws UaException {
    return ClientMembers.await(getLowLowNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends StateTypeNode> getLowLowNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        StateTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "LowLow",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:LowLow (declaration i=9335, owner i=9318)"));
  }

  @Override
  public TransitionTypeNode getLowLowToLowNode() throws UaException {
    return ClientMembers.await(getLowLowToLowNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getLowLowToLowNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "LowLowToLow",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:LowLowToLow (declaration i=9337, owner i=9318)"));
  }

  @Override
  public TransitionTypeNode getLowToLowLowNode() throws UaException {
    return ClientMembers.await(getLowToLowLowNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getLowToLowLowNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "LowToLowLow",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:LowToLowLow (declaration i=9338, owner i=9318)"));
  }

  @Override
  public TransitionTypeNode getHighHighToHighNode() throws UaException {
    return ClientMembers.await(getHighHighToHighNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getHighHighToHighNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "HighHighToHigh",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:HighHighToHigh (declaration i=9339, owner i=9318)"));
  }

  @Override
  public TransitionTypeNode getHighToHighHighNode() throws UaException {
    return ClientMembers.await(getHighToHighHighNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getHighToHighHighNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "HighToHighHigh",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:HighToHighHigh (declaration i=9340, owner i=9318)"));
  }
}
