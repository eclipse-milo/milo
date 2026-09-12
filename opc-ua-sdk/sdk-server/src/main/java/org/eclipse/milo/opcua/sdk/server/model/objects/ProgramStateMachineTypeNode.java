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
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteTransitionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ProgramDiagnostic2TypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ProgramDiagnostic2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

public class ProgramStateMachineTypeNode extends FiniteStateMachineTypeNode
    implements ProgramStateMachineType {
  public ProgramStateMachineTypeNode(
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

  public ProgramStateMachineTypeNode(
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
  public PropertyTypeNode getCreatableNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Creatable",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:Creatable (declaration i=2392, owner i=2391)"));
  }

  @Override
  public @Nullable Boolean getCreatable() {
    var node = getCreatableNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Creatable (declaration i=2392, owner i=2391)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setCreatable(@Nullable Boolean value) {
    var node = getCreatableNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Creatable (declaration i=2392, owner i=2391)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getDeletableNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Deletable",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:Deletable (declaration i=2393, owner i=2391)"));
  }

  @Override
  public @Nullable Boolean getDeletable() {
    var node = getDeletableNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Deletable (declaration i=2393, owner i=2391)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setDeletable(@Nullable Boolean value) {
    var node = getDeletableNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Deletable (declaration i=2393, owner i=2391)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getAutoDeleteNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "AutoDelete",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:AutoDelete (declaration i=2394, owner i=2391)"));
  }

  @Override
  public @Nullable Boolean getAutoDelete() {
    var node = getAutoDeleteNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AutoDelete (declaration i=2394, owner i=2391)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setAutoDelete(@Nullable Boolean value) {
    var node = getAutoDeleteNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AutoDelete (declaration i=2394, owner i=2391)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getRecycleCountNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RecycleCount",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:RecycleCount (declaration i=2395, owner i=2391)"));
  }

  @Override
  public @Nullable Integer getRecycleCount() {
    var node = getRecycleCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RecycleCount (declaration i=2395, owner i=2391)"
              + " on "
              + getNodeId());
    }
    return (Integer) node.getValue().getValue().getValue();
  }

  @Override
  public void setRecycleCount(@Nullable Integer value) {
    var node = getRecycleCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RecycleCount (declaration i=2395, owner i=2391)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getInstanceCountNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "InstanceCount",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:InstanceCount (declaration i=2396, owner i=2391)"));
  }

  @Override
  public @Nullable UInteger getInstanceCount() {
    var node = getInstanceCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InstanceCount (declaration i=2396, owner i=2391)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setInstanceCount(@Nullable UInteger value) {
    var node = getInstanceCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InstanceCount (declaration i=2396, owner i=2391)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getMaxInstanceCountNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxInstanceCount",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxInstanceCount (declaration i=2397, owner i=2391)"));
  }

  @Override
  public @Nullable UInteger getMaxInstanceCount() {
    var node = getMaxInstanceCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxInstanceCount (declaration i=2397, owner i=2391)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxInstanceCount(@Nullable UInteger value) {
    var node = getMaxInstanceCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxInstanceCount (declaration i=2397, owner i=2391)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getMaxRecycleCountNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxRecycleCount",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxRecycleCount (declaration i=2398, owner i=2391)"));
  }

  @Override
  public @Nullable UInteger getMaxRecycleCount() {
    var node = getMaxRecycleCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxRecycleCount (declaration i=2398, owner i=2391)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxRecycleCount(@Nullable UInteger value) {
    var node = getMaxRecycleCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxRecycleCount (declaration i=2398, owner i=2391)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public FiniteStateVariableTypeNode getCurrentStateNode() {
    return ServerMembers.lookup(
        this,
        FiniteStateVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CurrentState",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:CurrentState (declaration i=3830, owner i=2391)"));
  }

  @Override
  public @Nullable LocalizedText getCurrentState() {
    var node = getCurrentStateNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CurrentState (declaration i=3830, owner i=2391)"
              + " on "
              + getNodeId());
    }
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setCurrentState(@Nullable LocalizedText value) {
    var node = getCurrentStateNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CurrentState (declaration i=3830, owner i=2391)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public FiniteTransitionVariableTypeNode getLastTransitionNode() {
    return ServerMembers.lookup(
        this,
        FiniteTransitionVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "LastTransition",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:LastTransition (declaration i=3835, owner i=2391)"));
  }

  @Override
  public @Nullable LocalizedText getLastTransition() {
    var node = getLastTransitionNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LastTransition (declaration i=3835, owner i=2391)"
              + " on "
              + getNodeId());
    }
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastTransition(@Nullable LocalizedText value) {
    var node = getLastTransitionNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LastTransition (declaration i=3835, owner i=2391)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable ProgramDiagnostic2TypeNode getProgramDiagnosticNode() {
    return ServerMembers.lookup(
        this,
        ProgramDiagnostic2TypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ProgramDiagnostic",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:ProgramDiagnostic (declaration i=2399, owner i=2391)"));
  }

  @Override
  public @Nullable ProgramDiagnostic2DataType getProgramDiagnostic() {
    var node = getProgramDiagnosticNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ProgramDiagnostic (declaration i=2399, owner i=2391)"
              + " on "
              + getNodeId());
    }
    return (ProgramDiagnostic2DataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setProgramDiagnostic(@Nullable ProgramDiagnostic2DataType value) {
    var node = getProgramDiagnosticNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ProgramDiagnostic (declaration i=2399, owner i=2391)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable BaseObjectTypeNode getFinalResultDataNode() {
    return ServerMembers.lookup(
        this,
        BaseObjectTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "FinalResultData",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            true,
            "http://opcfoundation.org/UA/:FinalResultData (declaration i=3850, owner i=2391)"));
  }

  @Override
  public StateTypeNode getHaltedNode() {
    return ServerMembers.lookup(
        this,
        StateTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Halted",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:Halted (declaration i=2406, owner i=2391)"));
  }

  @Override
  public StateTypeNode getReadyNode() {
    return ServerMembers.lookup(
        this,
        StateTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Ready",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:Ready (declaration i=2400, owner i=2391)"));
  }

  @Override
  public StateTypeNode getRunningNode() {
    return ServerMembers.lookup(
        this,
        StateTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Running",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:Running (declaration i=2402, owner i=2391)"));
  }

  @Override
  public StateTypeNode getSuspendedNode() {
    return ServerMembers.lookup(
        this,
        StateTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Suspended",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:Suspended (declaration i=2404, owner i=2391)"));
  }

  @Override
  public TransitionTypeNode getHaltedToReadyNode() {
    return ServerMembers.lookup(
        this,
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "HaltedToReady",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:HaltedToReady (declaration i=2408, owner i=2391)"));
  }

  @Override
  public TransitionTypeNode getReadyToRunningNode() {
    return ServerMembers.lookup(
        this,
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ReadyToRunning",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:ReadyToRunning (declaration i=2410, owner i=2391)"));
  }

  @Override
  public TransitionTypeNode getRunningToHaltedNode() {
    return ServerMembers.lookup(
        this,
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RunningToHalted",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:RunningToHalted (declaration i=2412, owner i=2391)"));
  }

  @Override
  public TransitionTypeNode getRunningToReadyNode() {
    return ServerMembers.lookup(
        this,
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RunningToReady",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:RunningToReady (declaration i=2414, owner i=2391)"));
  }

  @Override
  public TransitionTypeNode getRunningToSuspendedNode() {
    return ServerMembers.lookup(
        this,
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RunningToSuspended",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:RunningToSuspended (declaration i=2416, owner i=2391)"));
  }

  @Override
  public TransitionTypeNode getSuspendedToRunningNode() {
    return ServerMembers.lookup(
        this,
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SuspendedToRunning",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:SuspendedToRunning (declaration i=2418, owner i=2391)"));
  }

  @Override
  public TransitionTypeNode getSuspendedToHaltedNode() {
    return ServerMembers.lookup(
        this,
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SuspendedToHalted",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:SuspendedToHalted (declaration i=2420, owner i=2391)"));
  }

  @Override
  public TransitionTypeNode getSuspendedToReadyNode() {
    return ServerMembers.lookup(
        this,
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SuspendedToReady",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:SuspendedToReady (declaration i=2422, owner i=2391)"));
  }

  @Override
  public TransitionTypeNode getReadyToHaltedNode() {
    return ServerMembers.lookup(
        this,
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ReadyToHalted",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:ReadyToHalted (declaration i=2424, owner i=2391)"));
  }
}
