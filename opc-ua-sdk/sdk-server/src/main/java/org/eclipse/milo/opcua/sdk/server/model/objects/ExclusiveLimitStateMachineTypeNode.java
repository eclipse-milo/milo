/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import com.digitalpetri.opcua.uanodeset.runtime.members.MemberDeclaration;
import com.digitalpetri.opcua.uanodeset.runtime.server.ServerMembers;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
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
      UaNodeContext context,
      NodeId nodeId,
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
        context,
        nodeId,
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

  public ExclusiveLimitStateMachineTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions) {
    super(
        context,
        nodeId,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions);
  }

  @Override
  public Optional<VariableNode> getPropertyNode(QualifiedName browseName) {
    return findNode(
            browseName,
            n -> n instanceof VariableNode,
            r ->
                r.isForward()
                    && (r.getReferenceTypeId().equals(NodeIds.HasProperty)
                        || getNodeContext()
                            .getServer()
                            .getReferenceTypeTree()
                            .isSubtypeOf(r.getReferenceTypeId(), NodeIds.HasProperty)))
        .map(n -> (VariableNode) n);
  }

  @Override
  public StateTypeNode getHighHighNode() {
    return ServerMembers.lookup(
        this,
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
  public StateTypeNode getHighNode() {
    return ServerMembers.lookup(
        this,
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
  public StateTypeNode getLowNode() {
    return ServerMembers.lookup(
        this,
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
  public StateTypeNode getLowLowNode() {
    return ServerMembers.lookup(
        this,
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
  public TransitionTypeNode getLowLowToLowNode() {
    return ServerMembers.lookup(
        this,
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
  public TransitionTypeNode getLowToLowLowNode() {
    return ServerMembers.lookup(
        this,
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
  public TransitionTypeNode getHighHighToHighNode() {
    return ServerMembers.lookup(
        this,
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
  public TransitionTypeNode getHighToHighHighNode() {
    return ServerMembers.lookup(
        this,
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
