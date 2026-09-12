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

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.InvalidArgumentException;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.methods.AlarmConditionTypeGetGroupMembershipsDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.AlarmConditionTypeGetGroupMembershipsHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.AlarmConditionTypePlaceInService2DetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.AlarmConditionTypePlaceInService2Handler;
import org.eclipse.milo.opcua.sdk.server.model.methods.AlarmConditionTypePlaceInServiceDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.AlarmConditionTypePlaceInServiceHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.AlarmConditionTypeRemoveFromService2DetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.AlarmConditionTypeRemoveFromService2Handler;
import org.eclipse.milo.opcua.sdk.server.model.methods.AlarmConditionTypeRemoveFromServiceDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.AlarmConditionTypeRemoveFromServiceHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.AlarmConditionTypeReset2DetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.AlarmConditionTypeReset2Handler;
import org.eclipse.milo.opcua.sdk.server.model.methods.AlarmConditionTypeResetDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.AlarmConditionTypeResetHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.AlarmConditionTypeSilenceDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.AlarmConditionTypeSilenceHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.AlarmConditionTypeSuppress2DetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.AlarmConditionTypeSuppress2Handler;
import org.eclipse.milo.opcua.sdk.server.model.methods.AlarmConditionTypeSuppressDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.AlarmConditionTypeSuppressHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.AlarmConditionTypeUnsuppress2DetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.AlarmConditionTypeUnsuppress2Handler;
import org.eclipse.milo.opcua.sdk.server.model.methods.AlarmConditionTypeUnsuppressDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.AlarmConditionTypeUnsuppressHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.AudioVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UNumber;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

public class AlarmConditionTypeNode extends AcknowledgeableConditionTypeNode
    implements AlarmConditionType {
  public AlarmConditionTypeNode(
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

  public AlarmConditionTypeNode(
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
  public PropertyTypeNode getInputNodeNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=46").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:InputNode (declaration i=11120, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "InputNode");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:InputNode (declaration"
                    + " i=11120, owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:InputNode (declaration i=11120, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:InputNode (declaration i=11120, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:InputNode (declaration i=11120, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NotFound,
            "http://opcfoundation.org/UA/:InputNode (declaration i=11120, owner i=2915)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:InputNode (declaration i=11120, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:InputNode (declaration i=11120, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof PropertyTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:InputNode (declaration i=11120, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (PropertyTypeNode) parent;
  }

  @Override
  public @Nullable NodeId getInputNode() {
    var node = getInputNodeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InputNode (declaration i=11120, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setInputNode(@Nullable NodeId value) {
    var node = getInputNodeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InputNode (declaration i=11120, owner i=2915)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getSuppressedOrShelvedNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=46").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:SuppressedOrShelved (declaration i=9215, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "SuppressedOrShelved");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:SuppressedOrShelved"
                    + " (declaration i=9215, owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:SuppressedOrShelved (declaration i=9215, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:SuppressedOrShelved (declaration i=9215, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:SuppressedOrShelved (declaration i=9215, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NotFound,
            "http://opcfoundation.org/UA/:SuppressedOrShelved (declaration i=9215, owner i=2915)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:SuppressedOrShelved (declaration i=9215, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:SuppressedOrShelved (declaration i=9215, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof PropertyTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:SuppressedOrShelved (declaration i=9215, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (PropertyTypeNode) parent;
  }

  @Override
  public @Nullable Boolean getSuppressedOrShelved() {
    var node = getSuppressedOrShelvedNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SuppressedOrShelved (declaration i=9215, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setSuppressedOrShelved(@Nullable Boolean value) {
    var node = getSuppressedOrShelvedNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SuppressedOrShelved (declaration i=9215, owner i=2915)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxTimeShelvedNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=46").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:MaxTimeShelved (declaration i=9216, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "MaxTimeShelved");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:MaxTimeShelved (declaration"
                    + " i=9216, owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:MaxTimeShelved (declaration i=9216, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:MaxTimeShelved (declaration i=9216, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:MaxTimeShelved (declaration i=9216, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        return null;
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:MaxTimeShelved (declaration i=9216, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:MaxTimeShelved (declaration i=9216, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof PropertyTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:MaxTimeShelved (declaration i=9216, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (PropertyTypeNode) parent;
  }

  @Override
  public @Nullable Double getMaxTimeShelved() {
    var node = getMaxTimeShelvedNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxTimeShelved (declaration i=9216, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxTimeShelved(@Nullable Double value) {
    var node = getMaxTimeShelvedNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxTimeShelved (declaration i=9216, owner i=2915)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getAudibleEnabledNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=46").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:AudibleEnabled (declaration i=16389, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "AudibleEnabled");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:AudibleEnabled (declaration"
                    + " i=16389, owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:AudibleEnabled (declaration i=16389, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:AudibleEnabled (declaration i=16389, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:AudibleEnabled (declaration i=16389, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        return null;
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:AudibleEnabled (declaration i=16389, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:AudibleEnabled (declaration i=16389, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof PropertyTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:AudibleEnabled (declaration i=16389, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (PropertyTypeNode) parent;
  }

  @Override
  public @Nullable Boolean getAudibleEnabled() {
    var node = getAudibleEnabledNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AudibleEnabled (declaration i=16389, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setAudibleEnabled(@Nullable Boolean value) {
    var node = getAudibleEnabledNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AudibleEnabled (declaration i=16389, owner i=2915)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getOnDelayNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=46").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:OnDelay (declaration i=16395, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "OnDelay");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:OnDelay (declaration i=16395,"
                    + " owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:OnDelay (declaration i=16395, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:OnDelay (declaration i=16395, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:OnDelay (declaration i=16395, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        return null;
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:OnDelay (declaration i=16395, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:OnDelay (declaration i=16395, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof PropertyTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:OnDelay (declaration i=16395, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (PropertyTypeNode) parent;
  }

  @Override
  public @Nullable Double getOnDelay() {
    var node = getOnDelayNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:OnDelay (declaration i=16395, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setOnDelay(@Nullable Double value) {
    var node = getOnDelayNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:OnDelay (declaration i=16395, owner i=2915)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getOffDelayNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=46").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:OffDelay (declaration i=16396, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "OffDelay");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:OffDelay (declaration i=16396,"
                    + " owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:OffDelay (declaration i=16396, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:OffDelay (declaration i=16396, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:OffDelay (declaration i=16396, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        return null;
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:OffDelay (declaration i=16396, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:OffDelay (declaration i=16396, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof PropertyTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:OffDelay (declaration i=16396, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (PropertyTypeNode) parent;
  }

  @Override
  public @Nullable Double getOffDelay() {
    var node = getOffDelayNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:OffDelay (declaration i=16396, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setOffDelay(@Nullable Double value) {
    var node = getOffDelayNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:OffDelay (declaration i=16396, owner i=2915)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getReAlarmTimeNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=46").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:ReAlarmTime (declaration i=16400, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "ReAlarmTime");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:ReAlarmTime (declaration"
                    + " i=16400, owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:ReAlarmTime (declaration i=16400, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:ReAlarmTime (declaration i=16400, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:ReAlarmTime (declaration i=16400, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        return null;
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:ReAlarmTime (declaration i=16400, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:ReAlarmTime (declaration i=16400, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof PropertyTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:ReAlarmTime (declaration i=16400, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (PropertyTypeNode) parent;
  }

  @Override
  public @Nullable Double getReAlarmTime() {
    var node = getReAlarmTimeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ReAlarmTime (declaration i=16400, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setReAlarmTime(@Nullable Double value) {
    var node = getReAlarmTimeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ReAlarmTime (declaration i=16400, owner i=2915)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public TwoStateVariableTypeNode getEnabledStateNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:EnabledState (declaration i=9118, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "EnabledState");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:EnabledState (declaration"
                    + " i=9118, owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:EnabledState (declaration i=9118, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:EnabledState (declaration i=9118, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:EnabledState (declaration i=9118, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NotFound,
            "http://opcfoundation.org/UA/:EnabledState (declaration i=9118, owner i=2915)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:EnabledState (declaration i=9118, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:EnabledState (declaration i=9118, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof TwoStateVariableTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:EnabledState (declaration i=9118, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (TwoStateVariableTypeNode) parent;
  }

  @Override
  public @Nullable LocalizedText getEnabledState() {
    var node = getEnabledStateNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EnabledState (declaration i=9118, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setEnabledState(@Nullable LocalizedText value) {
    var node = getEnabledStateNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EnabledState (declaration i=9118, owner i=2915)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public TwoStateVariableTypeNode getActiveStateNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:ActiveState (declaration i=9160, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "ActiveState");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:ActiveState (declaration"
                    + " i=9160, owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:ActiveState (declaration i=9160, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:ActiveState (declaration i=9160, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:ActiveState (declaration i=9160, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NotFound,
            "http://opcfoundation.org/UA/:ActiveState (declaration i=9160, owner i=2915)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:ActiveState (declaration i=9160, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:ActiveState (declaration i=9160, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof TwoStateVariableTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:ActiveState (declaration i=9160, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (TwoStateVariableTypeNode) parent;
  }

  @Override
  public @Nullable LocalizedText getActiveState() {
    var node = getActiveStateNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ActiveState (declaration i=9160, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setActiveState(@Nullable LocalizedText value) {
    var node = getActiveStateNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ActiveState (declaration i=9160, owner i=2915)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable TwoStateVariableTypeNode getSuppressedStateNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:SuppressedState (declaration i=9169, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "SuppressedState");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:SuppressedState (declaration"
                    + " i=9169, owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:SuppressedState (declaration i=9169, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:SuppressedState (declaration i=9169, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:SuppressedState (declaration i=9169, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        return null;
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:SuppressedState (declaration i=9169, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:SuppressedState (declaration i=9169, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof TwoStateVariableTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:SuppressedState (declaration i=9169, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (TwoStateVariableTypeNode) parent;
  }

  @Override
  public @Nullable LocalizedText getSuppressedState() {
    var node = getSuppressedStateNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SuppressedState (declaration i=9169, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setSuppressedState(@Nullable LocalizedText value) {
    var node = getSuppressedStateNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SuppressedState (declaration i=9169, owner i=2915)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable TwoStateVariableTypeNode getOutOfServiceStateNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:OutOfServiceState (declaration i=16371, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "OutOfServiceState");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:OutOfServiceState (declaration"
                    + " i=16371, owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:OutOfServiceState (declaration i=16371, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:OutOfServiceState (declaration i=16371, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:OutOfServiceState (declaration i=16371, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        return null;
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:OutOfServiceState (declaration i=16371, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:OutOfServiceState (declaration i=16371, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof TwoStateVariableTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:OutOfServiceState (declaration i=16371, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (TwoStateVariableTypeNode) parent;
  }

  @Override
  public @Nullable LocalizedText getOutOfServiceState() {
    var node = getOutOfServiceStateNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:OutOfServiceState (declaration i=16371, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setOutOfServiceState(@Nullable LocalizedText value) {
    var node = getOutOfServiceStateNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:OutOfServiceState (declaration i=16371, owner i=2915)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable ShelvedStateMachineTypeNode getShelvingStateNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:ShelvingState (declaration i=9178, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "ShelvingState");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:ShelvingState (declaration"
                    + " i=9178, owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:ShelvingState (declaration i=9178, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:ShelvingState (declaration i=9178, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:ShelvingState (declaration i=9178, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        return null;
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:ShelvingState (declaration i=9178, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Object) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:ShelvingState (declaration i=9178, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof ShelvedStateMachineTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:ShelvingState (declaration i=9178, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (ShelvedStateMachineTypeNode) parent;
  }

  @Override
  public @Nullable AudioVariableTypeNode getAudibleSoundNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:AudibleSound (declaration i=16390, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "AudibleSound");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:AudibleSound (declaration"
                    + " i=16390, owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:AudibleSound (declaration i=16390, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:AudibleSound (declaration i=16390, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:AudibleSound (declaration i=16390, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        return null;
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:AudibleSound (declaration i=16390, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:AudibleSound (declaration i=16390, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof AudioVariableTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:AudibleSound (declaration i=16390, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (AudioVariableTypeNode) parent;
  }

  @Override
  public @Nullable ByteString getAudibleSound() {
    var node = getAudibleSoundNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AudibleSound (declaration i=16390, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (ByteString) node.getValue().getValue().getValue();
  }

  @Override
  public void setAudibleSound(@Nullable ByteString value) {
    var node = getAudibleSoundNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AudibleSound (declaration i=16390, owner i=2915)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable TwoStateVariableTypeNode getSilenceStateNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:SilenceState (declaration i=16380, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "SilenceState");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:SilenceState (declaration"
                    + " i=16380, owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:SilenceState (declaration i=16380, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:SilenceState (declaration i=16380, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:SilenceState (declaration i=16380, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        return null;
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:SilenceState (declaration i=16380, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:SilenceState (declaration i=16380, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof TwoStateVariableTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:SilenceState (declaration i=16380, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (TwoStateVariableTypeNode) parent;
  }

  @Override
  public @Nullable LocalizedText getSilenceState() {
    var node = getSilenceStateNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SilenceState (declaration i=16380, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setSilenceState(@Nullable LocalizedText value) {
    var node = getSilenceStateNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SilenceState (declaration i=16380, owner i=2915)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getFirstInGroupFlagNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:FirstInGroupFlag (declaration i=16397, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "FirstInGroupFlag");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:FirstInGroupFlag (declaration"
                    + " i=16397, owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:FirstInGroupFlag (declaration i=16397, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:FirstInGroupFlag (declaration i=16397, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:FirstInGroupFlag (declaration i=16397, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        return null;
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:FirstInGroupFlag (declaration i=16397, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:FirstInGroupFlag (declaration i=16397, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof BaseDataVariableTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:FirstInGroupFlag (declaration i=16397, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (BaseDataVariableTypeNode) parent;
  }

  @Override
  public @Nullable Boolean getFirstInGroupFlag() {
    var node = getFirstInGroupFlagNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:FirstInGroupFlag (declaration i=16397, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setFirstInGroupFlag(@Nullable Boolean value) {
    var node = getFirstInGroupFlagNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:FirstInGroupFlag (declaration i=16397, owner i=2915)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable AlarmGroupTypeNode getFirstInGroupNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:FirstInGroup (declaration i=16398, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "FirstInGroup");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:FirstInGroup (declaration"
                    + " i=16398, owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:FirstInGroup (declaration i=16398, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:FirstInGroup (declaration i=16398, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:FirstInGroup (declaration i=16398, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        return null;
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:FirstInGroup (declaration i=16398, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Object) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:FirstInGroup (declaration i=16398, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof AlarmGroupTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:FirstInGroup (declaration i=16398, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (AlarmGroupTypeNode) parent;
  }

  @Override
  public @Nullable TwoStateVariableTypeNode getLatchedStateNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:LatchedState (declaration i=18190, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "LatchedState");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:LatchedState (declaration"
                    + " i=18190, owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:LatchedState (declaration i=18190, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:LatchedState (declaration i=18190, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:LatchedState (declaration i=18190, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        return null;
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:LatchedState (declaration i=18190, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:LatchedState (declaration i=18190, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof TwoStateVariableTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:LatchedState (declaration i=18190, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (TwoStateVariableTypeNode) parent;
  }

  @Override
  public @Nullable LocalizedText getLatchedState() {
    var node = getLatchedStateNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LatchedState (declaration i=18190, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setLatchedState(@Nullable LocalizedText value) {
    var node = getLatchedStateNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LatchedState (declaration i=18190, owner i=2915)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getReAlarmRepeatCountNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:ReAlarmRepeatCount (declaration i=16401, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "ReAlarmRepeatCount");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:ReAlarmRepeatCount"
                    + " (declaration i=16401, owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:ReAlarmRepeatCount (declaration i=16401, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:ReAlarmRepeatCount (declaration i=16401, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:ReAlarmRepeatCount (declaration i=16401, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        return null;
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:ReAlarmRepeatCount (declaration i=16401, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:ReAlarmRepeatCount (declaration i=16401, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof BaseDataVariableTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:ReAlarmRepeatCount (declaration i=16401, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (BaseDataVariableTypeNode) parent;
  }

  @Override
  public @Nullable Short getReAlarmRepeatCount() {
    var node = getReAlarmRepeatCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ReAlarmRepeatCount (declaration i=16401, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (Short) node.getValue().getValue().getValue();
  }

  @Override
  public void setReAlarmRepeatCount(@Nullable Short value) {
    var node = getReAlarmRepeatCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ReAlarmRepeatCount (declaration i=16401, owner i=2915)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable UaMethodNode getSilenceMethodNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:Silence (declaration i=16402, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "Silence");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:Silence (declaration i=16402,"
                    + " owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:Silence (declaration i=16402, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:Silence (declaration i=16402, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:Silence (declaration i=16402, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        return null;
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:Silence (declaration i=16402, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Method) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:Silence (declaration i=16402, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof UaMethodNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:Silence (declaration i=16402, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (UaMethodNode) parent;
  }

  @Override
  public MethodBinding bindSilence(
      MethodBindings bindings, AlarmConditionTypeSilenceHandler handler) throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getSilenceMethodNode();
    if (methodNode == null) {
      throw new UaException(StatusCodes.Bad_NotFound, "Cannot bind an absent Method");
    }
    return bindings.bind(
        this,
        methodNode,
        new AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return new Argument[] {};
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {};
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 0;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              handler.invoke(context);
              return new CallMethodResult(
                  StatusCode.GOOD, new StatusCode[0], new DiagnosticInfo[0], new Variant[] {});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }

  @Override
  public MethodBinding bindSilenceDetailed(
      MethodBindings bindings, AlarmConditionTypeSilenceDetailedHandler handler)
      throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getSilenceMethodNode();
    if (methodNode == null) {
      throw new UaException(StatusCodes.Bad_NotFound, "Cannot bind an absent Method");
    }
    return bindings.bind(
        this,
        methodNode,
        new AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return new Argument[] {};
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {};
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 0;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              var result = handler.invoke(context);
              if (result == null) {
                throw new UaException(
                    StatusCodes.Bad_InternalError, "A detailed Method handler returned null");
              }
              if (!result.hasOutputs()) {
                return new CallMethodResult(
                    result.status(),
                    result.inputResults(),
                    result.inputDiagnostics(),
                    new Variant[0]);
              }
              return new CallMethodResult(
                  result.status(), new StatusCode[0], new DiagnosticInfo[0], new Variant[] {});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }

  @Override
  public @Nullable UaMethodNode getSuppressMethodNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:Suppress (declaration i=16403, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "Suppress");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:Suppress (declaration i=16403,"
                    + " owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:Suppress (declaration i=16403, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:Suppress (declaration i=16403, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:Suppress (declaration i=16403, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        return null;
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:Suppress (declaration i=16403, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Method) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:Suppress (declaration i=16403, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof UaMethodNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:Suppress (declaration i=16403, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (UaMethodNode) parent;
  }

  @Override
  public MethodBinding bindSuppress(
      MethodBindings bindings, AlarmConditionTypeSuppressHandler handler) throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getSuppressMethodNode();
    if (methodNode == null) {
      throw new UaException(StatusCodes.Bad_NotFound, "Cannot bind an absent Method");
    }
    return bindings.bind(
        this,
        methodNode,
        new AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return new Argument[] {};
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {};
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 0;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              handler.invoke(context);
              return new CallMethodResult(
                  StatusCode.GOOD, new StatusCode[0], new DiagnosticInfo[0], new Variant[] {});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }

  @Override
  public MethodBinding bindSuppressDetailed(
      MethodBindings bindings, AlarmConditionTypeSuppressDetailedHandler handler)
      throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getSuppressMethodNode();
    if (methodNode == null) {
      throw new UaException(StatusCodes.Bad_NotFound, "Cannot bind an absent Method");
    }
    return bindings.bind(
        this,
        methodNode,
        new AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return new Argument[] {};
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {};
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 0;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              var result = handler.invoke(context);
              if (result == null) {
                throw new UaException(
                    StatusCodes.Bad_InternalError, "A detailed Method handler returned null");
              }
              if (!result.hasOutputs()) {
                return new CallMethodResult(
                    result.status(),
                    result.inputResults(),
                    result.inputDiagnostics(),
                    new Variant[0]);
              }
              return new CallMethodResult(
                  result.status(), new StatusCode[0], new DiagnosticInfo[0], new Variant[] {});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }

  @Override
  public @Nullable UaMethodNode getSuppress2MethodNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:Suppress2 (declaration i=24316, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "Suppress2");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:Suppress2 (declaration"
                    + " i=24316, owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:Suppress2 (declaration i=24316, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:Suppress2 (declaration i=24316, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:Suppress2 (declaration i=24316, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        return null;
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:Suppress2 (declaration i=24316, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Method) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:Suppress2 (declaration i=24316, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof UaMethodNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:Suppress2 (declaration i=24316, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (UaMethodNode) parent;
  }

  @Override
  public MethodBinding bindSuppress2(
      MethodBindings bindings, AlarmConditionTypeSuppress2Handler handler) throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getSuppress2MethodNode();
    if (methodNode == null) {
      throw new UaException(StatusCodes.Bad_NotFound, "Cannot bind an absent Method");
    }
    return bindings.bind(
        this,
        methodNode,
        new AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return new Argument[] {
              new Argument(
                  "Comment",
                  ExpandedNodeId.parse("i=21")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, ""))
            };
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {};
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 1;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            @Nullable LocalizedText callbackInput0 = null;
            if (inputValues.length > 0) {
              try {
                @Nullable LocalizedText convertedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput0 = (LocalizedText) methodValue;
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                @Nullable LocalizedText projectedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput0 = (LocalizedText) methodValue;
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                callbackInput0 = projectedInput0;
              } catch (UaException failure) {
                inputResults[0] = failure.getStatusCode();
              }
            }
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              handler.invoke(context, callbackInput0);
              return new CallMethodResult(
                  StatusCode.GOOD, new StatusCode[0], new DiagnosticInfo[0], new Variant[] {});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }

  @Override
  public MethodBinding bindSuppress2Detailed(
      MethodBindings bindings, AlarmConditionTypeSuppress2DetailedHandler handler)
      throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getSuppress2MethodNode();
    if (methodNode == null) {
      throw new UaException(StatusCodes.Bad_NotFound, "Cannot bind an absent Method");
    }
    return bindings.bind(
        this,
        methodNode,
        new AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return new Argument[] {
              new Argument(
                  "Comment",
                  ExpandedNodeId.parse("i=21")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, ""))
            };
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {};
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 1;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            @Nullable LocalizedText callbackInput0 = null;
            if (inputValues.length > 0) {
              try {
                @Nullable LocalizedText convertedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput0 = (LocalizedText) methodValue;
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                @Nullable LocalizedText projectedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput0 = (LocalizedText) methodValue;
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                callbackInput0 = projectedInput0;
              } catch (UaException failure) {
                inputResults[0] = failure.getStatusCode();
              }
            }
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              var result = handler.invoke(context, callbackInput0);
              if (result == null) {
                throw new UaException(
                    StatusCodes.Bad_InternalError, "A detailed Method handler returned null");
              }
              if (!result.hasOutputs()) {
                return new CallMethodResult(
                    result.status(),
                    result.inputResults(),
                    result.inputDiagnostics(),
                    new Variant[0]);
              }
              return new CallMethodResult(
                  result.status(), new StatusCode[0], new DiagnosticInfo[0], new Variant[] {});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }

  @Override
  public @Nullable UaMethodNode getUnsuppressMethodNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:Unsuppress (declaration i=17868, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "Unsuppress");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:Unsuppress (declaration"
                    + " i=17868, owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:Unsuppress (declaration i=17868, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:Unsuppress (declaration i=17868, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:Unsuppress (declaration i=17868, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        return null;
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:Unsuppress (declaration i=17868, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Method) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:Unsuppress (declaration i=17868, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof UaMethodNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:Unsuppress (declaration i=17868, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (UaMethodNode) parent;
  }

  @Override
  public MethodBinding bindUnsuppress(
      MethodBindings bindings, AlarmConditionTypeUnsuppressHandler handler) throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getUnsuppressMethodNode();
    if (methodNode == null) {
      throw new UaException(StatusCodes.Bad_NotFound, "Cannot bind an absent Method");
    }
    return bindings.bind(
        this,
        methodNode,
        new AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return new Argument[] {};
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {};
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 0;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              handler.invoke(context);
              return new CallMethodResult(
                  StatusCode.GOOD, new StatusCode[0], new DiagnosticInfo[0], new Variant[] {});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }

  @Override
  public MethodBinding bindUnsuppressDetailed(
      MethodBindings bindings, AlarmConditionTypeUnsuppressDetailedHandler handler)
      throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getUnsuppressMethodNode();
    if (methodNode == null) {
      throw new UaException(StatusCodes.Bad_NotFound, "Cannot bind an absent Method");
    }
    return bindings.bind(
        this,
        methodNode,
        new AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return new Argument[] {};
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {};
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 0;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              var result = handler.invoke(context);
              if (result == null) {
                throw new UaException(
                    StatusCodes.Bad_InternalError, "A detailed Method handler returned null");
              }
              if (!result.hasOutputs()) {
                return new CallMethodResult(
                    result.status(),
                    result.inputResults(),
                    result.inputDiagnostics(),
                    new Variant[0]);
              }
              return new CallMethodResult(
                  result.status(), new StatusCode[0], new DiagnosticInfo[0], new Variant[] {});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }

  @Override
  public @Nullable UaMethodNode getUnsuppress2MethodNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:Unsuppress2 (declaration i=24318, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "Unsuppress2");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:Unsuppress2 (declaration"
                    + " i=24318, owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:Unsuppress2 (declaration i=24318, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:Unsuppress2 (declaration i=24318, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:Unsuppress2 (declaration i=24318, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        return null;
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:Unsuppress2 (declaration i=24318, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Method) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:Unsuppress2 (declaration i=24318, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof UaMethodNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:Unsuppress2 (declaration i=24318, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (UaMethodNode) parent;
  }

  @Override
  public MethodBinding bindUnsuppress2(
      MethodBindings bindings, AlarmConditionTypeUnsuppress2Handler handler) throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getUnsuppress2MethodNode();
    if (methodNode == null) {
      throw new UaException(StatusCodes.Bad_NotFound, "Cannot bind an absent Method");
    }
    return bindings.bind(
        this,
        methodNode,
        new AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return new Argument[] {
              new Argument(
                  "Comment",
                  ExpandedNodeId.parse("i=21")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, ""))
            };
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {};
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 1;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            @Nullable LocalizedText callbackInput0 = null;
            if (inputValues.length > 0) {
              try {
                @Nullable LocalizedText convertedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput0 = (LocalizedText) methodValue;
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                @Nullable LocalizedText projectedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput0 = (LocalizedText) methodValue;
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                callbackInput0 = projectedInput0;
              } catch (UaException failure) {
                inputResults[0] = failure.getStatusCode();
              }
            }
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              handler.invoke(context, callbackInput0);
              return new CallMethodResult(
                  StatusCode.GOOD, new StatusCode[0], new DiagnosticInfo[0], new Variant[] {});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }

  @Override
  public MethodBinding bindUnsuppress2Detailed(
      MethodBindings bindings, AlarmConditionTypeUnsuppress2DetailedHandler handler)
      throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getUnsuppress2MethodNode();
    if (methodNode == null) {
      throw new UaException(StatusCodes.Bad_NotFound, "Cannot bind an absent Method");
    }
    return bindings.bind(
        this,
        methodNode,
        new AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return new Argument[] {
              new Argument(
                  "Comment",
                  ExpandedNodeId.parse("i=21")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, ""))
            };
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {};
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 1;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            @Nullable LocalizedText callbackInput0 = null;
            if (inputValues.length > 0) {
              try {
                @Nullable LocalizedText convertedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput0 = (LocalizedText) methodValue;
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                @Nullable LocalizedText projectedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput0 = (LocalizedText) methodValue;
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                callbackInput0 = projectedInput0;
              } catch (UaException failure) {
                inputResults[0] = failure.getStatusCode();
              }
            }
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              var result = handler.invoke(context, callbackInput0);
              if (result == null) {
                throw new UaException(
                    StatusCodes.Bad_InternalError, "A detailed Method handler returned null");
              }
              if (!result.hasOutputs()) {
                return new CallMethodResult(
                    result.status(),
                    result.inputResults(),
                    result.inputDiagnostics(),
                    new Variant[0]);
              }
              return new CallMethodResult(
                  result.status(), new StatusCode[0], new DiagnosticInfo[0], new Variant[] {});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }

  @Override
  public @Nullable UaMethodNode getRemoveFromServiceMethodNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:RemoveFromService (declaration i=17869, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "RemoveFromService");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:RemoveFromService (declaration"
                    + " i=17869, owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:RemoveFromService (declaration i=17869, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:RemoveFromService (declaration i=17869, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:RemoveFromService (declaration i=17869, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        return null;
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:RemoveFromService (declaration i=17869, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Method) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:RemoveFromService (declaration i=17869, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof UaMethodNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:RemoveFromService (declaration i=17869, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (UaMethodNode) parent;
  }

  @Override
  public MethodBinding bindRemoveFromService(
      MethodBindings bindings, AlarmConditionTypeRemoveFromServiceHandler handler)
      throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getRemoveFromServiceMethodNode();
    if (methodNode == null) {
      throw new UaException(StatusCodes.Bad_NotFound, "Cannot bind an absent Method");
    }
    return bindings.bind(
        this,
        methodNode,
        new AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return new Argument[] {};
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {};
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 0;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              handler.invoke(context);
              return new CallMethodResult(
                  StatusCode.GOOD, new StatusCode[0], new DiagnosticInfo[0], new Variant[] {});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }

  @Override
  public MethodBinding bindRemoveFromServiceDetailed(
      MethodBindings bindings, AlarmConditionTypeRemoveFromServiceDetailedHandler handler)
      throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getRemoveFromServiceMethodNode();
    if (methodNode == null) {
      throw new UaException(StatusCodes.Bad_NotFound, "Cannot bind an absent Method");
    }
    return bindings.bind(
        this,
        methodNode,
        new AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return new Argument[] {};
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {};
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 0;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              var result = handler.invoke(context);
              if (result == null) {
                throw new UaException(
                    StatusCodes.Bad_InternalError, "A detailed Method handler returned null");
              }
              if (!result.hasOutputs()) {
                return new CallMethodResult(
                    result.status(),
                    result.inputResults(),
                    result.inputDiagnostics(),
                    new Variant[0]);
              }
              return new CallMethodResult(
                  result.status(), new StatusCode[0], new DiagnosticInfo[0], new Variant[] {});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }

  @Override
  public @Nullable UaMethodNode getRemoveFromService2MethodNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:RemoveFromService2 (declaration i=24320, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "RemoveFromService2");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:RemoveFromService2"
                    + " (declaration i=24320, owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:RemoveFromService2 (declaration i=24320, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:RemoveFromService2 (declaration i=24320, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:RemoveFromService2 (declaration i=24320, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        return null;
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:RemoveFromService2 (declaration i=24320, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Method) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:RemoveFromService2 (declaration i=24320, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof UaMethodNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:RemoveFromService2 (declaration i=24320, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (UaMethodNode) parent;
  }

  @Override
  public MethodBinding bindRemoveFromService2(
      MethodBindings bindings, AlarmConditionTypeRemoveFromService2Handler handler)
      throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getRemoveFromService2MethodNode();
    if (methodNode == null) {
      throw new UaException(StatusCodes.Bad_NotFound, "Cannot bind an absent Method");
    }
    return bindings.bind(
        this,
        methodNode,
        new AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return new Argument[] {
              new Argument(
                  "Comment",
                  ExpandedNodeId.parse("i=21")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, ""))
            };
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {};
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 1;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            @Nullable LocalizedText callbackInput0 = null;
            if (inputValues.length > 0) {
              try {
                @Nullable LocalizedText convertedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput0 = (LocalizedText) methodValue;
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                @Nullable LocalizedText projectedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput0 = (LocalizedText) methodValue;
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                callbackInput0 = projectedInput0;
              } catch (UaException failure) {
                inputResults[0] = failure.getStatusCode();
              }
            }
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              handler.invoke(context, callbackInput0);
              return new CallMethodResult(
                  StatusCode.GOOD, new StatusCode[0], new DiagnosticInfo[0], new Variant[] {});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }

  @Override
  public MethodBinding bindRemoveFromService2Detailed(
      MethodBindings bindings, AlarmConditionTypeRemoveFromService2DetailedHandler handler)
      throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getRemoveFromService2MethodNode();
    if (methodNode == null) {
      throw new UaException(StatusCodes.Bad_NotFound, "Cannot bind an absent Method");
    }
    return bindings.bind(
        this,
        methodNode,
        new AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return new Argument[] {
              new Argument(
                  "Comment",
                  ExpandedNodeId.parse("i=21")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, ""))
            };
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {};
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 1;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            @Nullable LocalizedText callbackInput0 = null;
            if (inputValues.length > 0) {
              try {
                @Nullable LocalizedText convertedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput0 = (LocalizedText) methodValue;
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                @Nullable LocalizedText projectedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput0 = (LocalizedText) methodValue;
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                callbackInput0 = projectedInput0;
              } catch (UaException failure) {
                inputResults[0] = failure.getStatusCode();
              }
            }
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              var result = handler.invoke(context, callbackInput0);
              if (result == null) {
                throw new UaException(
                    StatusCodes.Bad_InternalError, "A detailed Method handler returned null");
              }
              if (!result.hasOutputs()) {
                return new CallMethodResult(
                    result.status(),
                    result.inputResults(),
                    result.inputDiagnostics(),
                    new Variant[0]);
              }
              return new CallMethodResult(
                  result.status(), new StatusCode[0], new DiagnosticInfo[0], new Variant[] {});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }

  @Override
  public @Nullable UaMethodNode getPlaceInServiceMethodNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:PlaceInService (declaration i=17870, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "PlaceInService");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:PlaceInService (declaration"
                    + " i=17870, owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:PlaceInService (declaration i=17870, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:PlaceInService (declaration i=17870, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:PlaceInService (declaration i=17870, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        return null;
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:PlaceInService (declaration i=17870, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Method) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:PlaceInService (declaration i=17870, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof UaMethodNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:PlaceInService (declaration i=17870, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (UaMethodNode) parent;
  }

  @Override
  public MethodBinding bindPlaceInService(
      MethodBindings bindings, AlarmConditionTypePlaceInServiceHandler handler) throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getPlaceInServiceMethodNode();
    if (methodNode == null) {
      throw new UaException(StatusCodes.Bad_NotFound, "Cannot bind an absent Method");
    }
    return bindings.bind(
        this,
        methodNode,
        new AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return new Argument[] {};
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {};
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 0;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              handler.invoke(context);
              return new CallMethodResult(
                  StatusCode.GOOD, new StatusCode[0], new DiagnosticInfo[0], new Variant[] {});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }

  @Override
  public MethodBinding bindPlaceInServiceDetailed(
      MethodBindings bindings, AlarmConditionTypePlaceInServiceDetailedHandler handler)
      throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getPlaceInServiceMethodNode();
    if (methodNode == null) {
      throw new UaException(StatusCodes.Bad_NotFound, "Cannot bind an absent Method");
    }
    return bindings.bind(
        this,
        methodNode,
        new AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return new Argument[] {};
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {};
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 0;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              var result = handler.invoke(context);
              if (result == null) {
                throw new UaException(
                    StatusCodes.Bad_InternalError, "A detailed Method handler returned null");
              }
              if (!result.hasOutputs()) {
                return new CallMethodResult(
                    result.status(),
                    result.inputResults(),
                    result.inputDiagnostics(),
                    new Variant[0]);
              }
              return new CallMethodResult(
                  result.status(), new StatusCode[0], new DiagnosticInfo[0], new Variant[] {});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }

  @Override
  public @Nullable UaMethodNode getPlaceInService2MethodNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:PlaceInService2 (declaration i=24322, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "PlaceInService2");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:PlaceInService2 (declaration"
                    + " i=24322, owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:PlaceInService2 (declaration i=24322, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:PlaceInService2 (declaration i=24322, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:PlaceInService2 (declaration i=24322, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        return null;
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:PlaceInService2 (declaration i=24322, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Method) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:PlaceInService2 (declaration i=24322, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof UaMethodNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:PlaceInService2 (declaration i=24322, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (UaMethodNode) parent;
  }

  @Override
  public MethodBinding bindPlaceInService2(
      MethodBindings bindings, AlarmConditionTypePlaceInService2Handler handler)
      throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getPlaceInService2MethodNode();
    if (methodNode == null) {
      throw new UaException(StatusCodes.Bad_NotFound, "Cannot bind an absent Method");
    }
    return bindings.bind(
        this,
        methodNode,
        new AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return new Argument[] {
              new Argument(
                  "Comment",
                  ExpandedNodeId.parse("i=21")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, ""))
            };
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {};
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 1;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            @Nullable LocalizedText callbackInput0 = null;
            if (inputValues.length > 0) {
              try {
                @Nullable LocalizedText convertedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput0 = (LocalizedText) methodValue;
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                @Nullable LocalizedText projectedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput0 = (LocalizedText) methodValue;
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                callbackInput0 = projectedInput0;
              } catch (UaException failure) {
                inputResults[0] = failure.getStatusCode();
              }
            }
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              handler.invoke(context, callbackInput0);
              return new CallMethodResult(
                  StatusCode.GOOD, new StatusCode[0], new DiagnosticInfo[0], new Variant[] {});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }

  @Override
  public MethodBinding bindPlaceInService2Detailed(
      MethodBindings bindings, AlarmConditionTypePlaceInService2DetailedHandler handler)
      throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getPlaceInService2MethodNode();
    if (methodNode == null) {
      throw new UaException(StatusCodes.Bad_NotFound, "Cannot bind an absent Method");
    }
    return bindings.bind(
        this,
        methodNode,
        new AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return new Argument[] {
              new Argument(
                  "Comment",
                  ExpandedNodeId.parse("i=21")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, ""))
            };
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {};
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 1;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            @Nullable LocalizedText callbackInput0 = null;
            if (inputValues.length > 0) {
              try {
                @Nullable LocalizedText convertedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput0 = (LocalizedText) methodValue;
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                @Nullable LocalizedText projectedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput0 = (LocalizedText) methodValue;
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                callbackInput0 = projectedInput0;
              } catch (UaException failure) {
                inputResults[0] = failure.getStatusCode();
              }
            }
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              var result = handler.invoke(context, callbackInput0);
              if (result == null) {
                throw new UaException(
                    StatusCodes.Bad_InternalError, "A detailed Method handler returned null");
              }
              if (!result.hasOutputs()) {
                return new CallMethodResult(
                    result.status(),
                    result.inputResults(),
                    result.inputDiagnostics(),
                    new Variant[0]);
              }
              return new CallMethodResult(
                  result.status(), new StatusCode[0], new DiagnosticInfo[0], new Variant[] {});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }

  @Override
  public @Nullable UaMethodNode getResetMethodNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:Reset (declaration i=18199, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "Reset");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:Reset (declaration i=18199,"
                    + " owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:Reset (declaration i=18199, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:Reset (declaration i=18199, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:Reset (declaration i=18199, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        return null;
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:Reset (declaration i=18199, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Method) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:Reset (declaration i=18199, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof UaMethodNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:Reset (declaration i=18199, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (UaMethodNode) parent;
  }

  @Override
  public MethodBinding bindReset(MethodBindings bindings, AlarmConditionTypeResetHandler handler)
      throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getResetMethodNode();
    if (methodNode == null) {
      throw new UaException(StatusCodes.Bad_NotFound, "Cannot bind an absent Method");
    }
    return bindings.bind(
        this,
        methodNode,
        new AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return new Argument[] {};
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {};
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 0;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              handler.invoke(context);
              return new CallMethodResult(
                  StatusCode.GOOD, new StatusCode[0], new DiagnosticInfo[0], new Variant[] {});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }

  @Override
  public MethodBinding bindResetDetailed(
      MethodBindings bindings, AlarmConditionTypeResetDetailedHandler handler) throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getResetMethodNode();
    if (methodNode == null) {
      throw new UaException(StatusCodes.Bad_NotFound, "Cannot bind an absent Method");
    }
    return bindings.bind(
        this,
        methodNode,
        new AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return new Argument[] {};
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {};
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 0;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              var result = handler.invoke(context);
              if (result == null) {
                throw new UaException(
                    StatusCodes.Bad_InternalError, "A detailed Method handler returned null");
              }
              if (!result.hasOutputs()) {
                return new CallMethodResult(
                    result.status(),
                    result.inputResults(),
                    result.inputDiagnostics(),
                    new Variant[0]);
              }
              return new CallMethodResult(
                  result.status(), new StatusCode[0], new DiagnosticInfo[0], new Variant[] {});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }

  @Override
  public @Nullable UaMethodNode getReset2MethodNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:Reset2 (declaration i=24324, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "Reset2");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:Reset2 (declaration i=24324,"
                    + " owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:Reset2 (declaration i=24324, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:Reset2 (declaration i=24324, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:Reset2 (declaration i=24324, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        return null;
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:Reset2 (declaration i=24324, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Method) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:Reset2 (declaration i=24324, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof UaMethodNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:Reset2 (declaration i=24324, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (UaMethodNode) parent;
  }

  @Override
  public MethodBinding bindReset2(MethodBindings bindings, AlarmConditionTypeReset2Handler handler)
      throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getReset2MethodNode();
    if (methodNode == null) {
      throw new UaException(StatusCodes.Bad_NotFound, "Cannot bind an absent Method");
    }
    return bindings.bind(
        this,
        methodNode,
        new AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return new Argument[] {
              new Argument(
                  "Comment",
                  ExpandedNodeId.parse("i=21")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, ""))
            };
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {};
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 1;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            @Nullable LocalizedText callbackInput0 = null;
            if (inputValues.length > 0) {
              try {
                @Nullable LocalizedText convertedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput0 = (LocalizedText) methodValue;
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                @Nullable LocalizedText projectedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput0 = (LocalizedText) methodValue;
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                callbackInput0 = projectedInput0;
              } catch (UaException failure) {
                inputResults[0] = failure.getStatusCode();
              }
            }
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              handler.invoke(context, callbackInput0);
              return new CallMethodResult(
                  StatusCode.GOOD, new StatusCode[0], new DiagnosticInfo[0], new Variant[] {});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }

  @Override
  public MethodBinding bindReset2Detailed(
      MethodBindings bindings, AlarmConditionTypeReset2DetailedHandler handler) throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getReset2MethodNode();
    if (methodNode == null) {
      throw new UaException(StatusCodes.Bad_NotFound, "Cannot bind an absent Method");
    }
    return bindings.bind(
        this,
        methodNode,
        new AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return new Argument[] {
              new Argument(
                  "Comment",
                  ExpandedNodeId.parse("i=21")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, ""))
            };
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {};
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 1;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            @Nullable LocalizedText callbackInput0 = null;
            if (inputValues.length > 0) {
              try {
                @Nullable LocalizedText convertedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput0 = (LocalizedText) methodValue;
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                @Nullable LocalizedText projectedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput0 = (LocalizedText) methodValue;
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                callbackInput0 = projectedInput0;
              } catch (UaException failure) {
                inputResults[0] = failure.getStatusCode();
              }
            }
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              var result = handler.invoke(context, callbackInput0);
              if (result == null) {
                throw new UaException(
                    StatusCodes.Bad_InternalError, "A detailed Method handler returned null");
              }
              if (!result.hasOutputs()) {
                return new CallMethodResult(
                    result.status(),
                    result.inputResults(),
                    result.inputDiagnostics(),
                    new Variant[0]);
              }
              return new CallMethodResult(
                  result.status(), new StatusCode[0], new DiagnosticInfo[0], new Variant[] {});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }

  @Override
  public @Nullable UaMethodNode getGetGroupMembershipsMethodNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:GetGroupMemberships (declaration i=24744, owner i=2915)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "GetGroupMemberships");
      var matches = new LinkedHashMap<NodeId, UaNode>();
      for (var reference : parent.getReferences()) {
        if (!reference.isForward()) {
          continue;
        }
        if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
          var referenceTypeTree = parent.getNodeContext().getServer().getReferenceTypeTree();
          if (!referenceTypeTree.containsType(reference.getReferenceTypeId())) {
            throw new UaRuntimeException(
                StatusCodes.Bad_NodeIdUnknown,
                "Unavailable ReferenceType "
                    + reference.getReferenceTypeId()
                    + " while resolving http://opcfoundation.org/UA/:GetGroupMemberships"
                    + " (declaration i=24744, owner i=2915) on "
                    + getNodeId());
          }
          if (!(referenceTypeTree.isSubtypeOf(
              reference.getReferenceTypeId(), referenceId.orElseThrow()))) {
            continue;
          }
        }
        if (!reference.getTargetNodeId().isLocal()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NotSupported,
              "http://opcfoundation.org/UA/:GetGroupMemberships (declaration i=24744, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:GetGroupMemberships (declaration i=24744, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        var local = parent.getNodeManager().getNode(targetId.orElseThrow());
        var target =
            local.isPresent()
                ? local.orElseThrow()
                : parent
                    .getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(targetId.orElseThrow())
                    .orElse(null);
        if (target == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdUnknown,
              "http://opcfoundation.org/UA/:GetGroupMemberships (declaration i=24744, owner i=2915)"
                  + " on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        return null;
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:GetGroupMemberships (declaration i=24744, owner i=2915)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Method) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:GetGroupMemberships (declaration i=24744, owner i=2915)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof UaMethodNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:GetGroupMemberships (declaration i=24744, owner i=2915)"
              + " on "
              + getNodeId());
    }
    return (UaMethodNode) parent;
  }

  @Override
  public MethodBinding bindGetGroupMemberships(
      MethodBindings bindings, AlarmConditionTypeGetGroupMembershipsHandler handler)
      throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getGetGroupMembershipsMethodNode();
    if (methodNode == null) {
      throw new UaException(StatusCodes.Bad_NotFound, "Cannot bind an absent Method");
    }
    return bindings.bind(
        this,
        methodNode,
        new AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return new Argument[] {};
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {
              new Argument(
                  "Groups",
                  ExpandedNodeId.parse("i=17")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText(null, ""))
            };
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 0;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              @Nullable NodeId @Nullable [] outputs = handler.invoke(context);
              Variant outputValue0;
              {
                @Nullable NodeId @Nullable [] convertedValue;
                {
                  Object methodValue = outputs;
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    NamespaceTable namespaceTable = context.getServer().getNamespaceTable();
                    DataTypeTree dataTypeTree = context.getServer().getDataTypeTree();
                    NodeId argumentDataTypeId =
                        ExpandedNodeId.parse("i=17")
                            .toNodeId(namespaceTable)
                            .orElseThrow(
                                () ->
                                    new UaException(
                                        StatusCodes.Bad_NodeIdInvalid,
                                        "Method argument DataType namespace is unavailable"));
                    if (!OpcUaDataType.isBuiltin(argumentDataTypeId)
                        && dataTypeTree.getDataType(argumentDataTypeId) == null) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "Method argument Groups (effective property i=25154, DataType i=17) is"
                              + " unavailable in the effective type tree; resolved DataType: "
                              + argumentDataTypeId);
                    }
                    Object numericElements =
                        methodValue instanceof Matrix
                            ? ((Matrix) methodValue).getElements()
                            : methodValue;
                    if (numericElements != null
                        && numericElements.getClass().isArray()
                        && (numericElements.getClass().getComponentType() == Number.class
                            || numericElements.getClass().getComponentType() == UNumber.class)
                        && (argumentDataTypeId.equals(NodeIds.Number)
                            || dataTypeTree.isSubtypeOf(argumentDataTypeId, NodeIds.Number))) {
                      Class<?> numericElementType = null;
                      for (int numericIndex = 0;
                          numericIndex < Array.getLength(numericElements);
                          numericIndex++) {
                        Object numericElement = Array.get(numericElements, numericIndex);
                        if (numericElement != null) {
                          if (numericElementType != null
                              && numericElementType != numericElement.getClass()) {
                            throw new UaException(
                                StatusCodes.Bad_TypeMismatch,
                                "An abstract numeric array requires one homogeneous wire element"
                                    + " type");
                          }
                          numericElementType = numericElement.getClass();
                        }
                      }
                      if (numericElementType == null) {
                        numericElementType = dataTypeTree.getBackingClass(argumentDataTypeId);
                      }
                      if (numericElementType == Number.class
                          || numericElementType == UNumber.class) {
                        throw new UaException(
                            StatusCodes.Bad_TypeMismatch,
                            "An empty or all-null abstract numeric array requires a concretely"
                                + " typed array");
                      }
                      Object numericArray =
                          Array.newInstance(numericElementType, Array.getLength(numericElements));
                      for (int numericIndex = 0;
                          numericIndex < Array.getLength(numericElements);
                          numericIndex++) {
                        Array.set(
                            numericArray, numericIndex, Array.get(numericElements, numericIndex));
                      }
                      if (methodValue instanceof Matrix) {
                        methodValue =
                            new Matrix(
                                numericArray,
                                ((Matrix) methodValue).getDimensions().clone(),
                                ((Matrix) methodValue)
                                    .getDataType()
                                    .orElseThrow(
                                        () ->
                                            new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "A numeric Matrix requires an explicit wire"
                                                    + " DataType")),
                                ((Matrix) methodValue).getDataTypeId().orElse(null));
                      } else {
                        methodValue = numericArray;
                      }
                    }
                    if (methodValue != null) {
                      Object shapeElements =
                          methodValue instanceof Matrix
                              ? ((Matrix) methodValue).getElements()
                              : methodValue;
                      int valueRank =
                          methodValue instanceof Matrix
                              ? ((Matrix) methodValue).getValueRank()
                              : ArrayUtil.getValueRank(methodValue);
                      boolean emptyArray =
                          methodValue.getClass().isArray()
                              && ArrayUtil.getValueRank(methodValue) == 1
                              && Array.getLength(methodValue) == 0;
                      if (!(valueRank == 1 || emptyArray)) {
                        throw new UaException(
                            StatusCodes.Bad_TypeMismatch, "Method argument ValueRank mismatch");
                      }
                      if (methodValue instanceof Matrix) {
                        int[] dimensions = ((Matrix) methodValue).getDimensions();
                        if (dimensions.length < 2
                            || !shapeElements.getClass().isArray()
                            || ArrayUtil.getValueRank(shapeElements) != 1) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "Malformed Method Matrix representation");
                        }
                        long elementCount = 1;
                        for (int dimension : dimensions) {
                          if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                            throw new UaException(
                                StatusCodes.Bad_TypeMismatch, "Malformed Method Matrix dimensions");
                          }
                          elementCount *= dimension;
                        }
                        if (elementCount != Array.getLength(shapeElements)) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "Method Matrix dimensions do not match its elements");
                        }
                        if (!(((Matrix) methodValue)
                            .getDataType()
                            .equals(Variant.of(shapeElements).getDataType()))) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "Method Matrix DataType does not match its elements");
                        }
                      }
                      Variant.of(shapeElements);
                      if (!emptyArray) {
                        int[] dimensions =
                            methodValue instanceof Matrix
                                ? ((Matrix) methodValue).getDimensions()
                                : ArrayUtil.getDimensions(methodValue);
                        long[] maximumDimensions = new long[] {0L};
                        if (dimensions.length != maximumDimensions.length) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "Method argument ArrayDimensions mismatch");
                        }
                        for (int dimensionIndex = 0;
                            dimensionIndex < dimensions.length;
                            dimensionIndex++) {
                          if (maximumDimensions[dimensionIndex] != 0
                              && dimensions[dimensionIndex] > maximumDimensions[dimensionIndex]) {
                            throw new UaException(
                                StatusCodes.Bad_TypeMismatch,
                                "Method argument exceeds ArrayDimensions maximum");
                          }
                        }
                      }
                    }
                    if (methodValue != null) {
                      Object typedElements =
                          methodValue instanceof Matrix
                              ? ((Matrix) methodValue).getElements()
                              : methodValue;
                      if (NodeIds.Structure.equals(argumentDataTypeId)
                          || dataTypeTree.isStructType(argumentDataTypeId)) {
                        var declaredType = dataTypeTree.getType(argumentDataTypeId);
                        if (typedElements.getClass().isArray()) {
                          var structureCodec =
                              context
                                  .getServer()
                                  .getStaticEncodingContext()
                                  .getDataTypeManager()
                                  .getCodec(argumentDataTypeId);
                          Class<?> structureClass =
                              structureCodec == null
                                  ? UaStructuredType.class
                                  : structureCodec.getType();
                          Object decodedStructures =
                              Array.newInstance(structureClass, Array.getLength(typedElements));
                          for (int structureIndex = 0;
                              structureIndex < Array.getLength(typedElements);
                              structureIndex++) {
                            Object structure = Array.get(typedElements, structureIndex);
                            if (structure instanceof ExtensionObject) {
                              structure =
                                  ((ExtensionObject) structure).isNull()
                                      ? null
                                      : ((ExtensionObject) structure)
                                          .decode(context.getServer().getStaticEncodingContext());
                            }
                            if (structure != null) {
                              if (!(structure instanceof UaStructuredType)) {
                                throw new UaException(
                                    StatusCodes.Bad_TypeMismatch,
                                    "Method argument requires a Structure value");
                              }
                              if (NodeIds.Structure.equals(argumentDataTypeId)
                                  || declaredType != null && declaredType.isAbstract()) {
                                if (!dataTypeTree.isSubtypeOf(
                                    ((UaStructuredType) structure)
                                        .getTypeId()
                                        .toNodeId(namespaceTable)
                                        .orElse(NodeId.NULL_VALUE),
                                    argumentDataTypeId)) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch,
                                      "Method Structure is not a subtype of the effective"
                                          + " DataType");
                                }
                              } else {
                                if (!argumentDataTypeId.equals(
                                    ((UaStructuredType) structure)
                                        .getTypeId()
                                        .toNodeId(namespaceTable)
                                        .orElse(NodeId.NULL_VALUE))) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch,
                                      "Method Structure does not match the effective DataType");
                                }
                              }
                            }
                            Array.set(decodedStructures, structureIndex, structure);
                          }
                          methodValue =
                              methodValue instanceof Matrix
                                  ? new Matrix(
                                      decodedStructures,
                                      ((Matrix) methodValue).getDimensions().clone())
                                  : decodedStructures;
                        } else {
                          if (typedElements instanceof ExtensionObject) {
                            typedElements =
                                ((ExtensionObject) typedElements).isNull()
                                    ? null
                                    : ((ExtensionObject) typedElements)
                                        .decode(context.getServer().getStaticEncodingContext());
                          }
                          if (typedElements != null) {
                            if (!(typedElements instanceof UaStructuredType)) {
                              throw new UaException(
                                  StatusCodes.Bad_TypeMismatch,
                                  "Method argument requires a Structure value");
                            }
                            if (NodeIds.Structure.equals(argumentDataTypeId)
                                || declaredType != null && declaredType.isAbstract()) {
                              if (!dataTypeTree.isSubtypeOf(
                                  ((UaStructuredType) typedElements)
                                      .getTypeId()
                                      .toNodeId(namespaceTable)
                                      .orElse(NodeId.NULL_VALUE),
                                  argumentDataTypeId)) {
                                throw new UaException(
                                    StatusCodes.Bad_TypeMismatch,
                                    "Method Structure is not a subtype of the effective DataType");
                              }
                            } else {
                              if (!argumentDataTypeId.equals(
                                  ((UaStructuredType) typedElements)
                                      .getTypeId()
                                      .toNodeId(namespaceTable)
                                      .orElse(NodeId.NULL_VALUE))) {
                                throw new UaException(
                                    StatusCodes.Bad_TypeMismatch,
                                    "Method Structure does not match the effective DataType");
                              }
                            }
                          }
                          methodValue = typedElements;
                        }
                      } else {
                        Variant.of(typedElements);
                        NodeId assignableDataTypeId =
                            dataTypeTree.getBackingClass(argumentDataTypeId) == Number.class
                                    && dataTypeTree.isSubtypeOf(argumentDataTypeId, NodeIds.Integer)
                                ? NodeIds.Integer
                                : argumentDataTypeId;
                        if (dataTypeTree.getBackingClass(argumentDataTypeId) != Variant.class
                            && !dataTypeTree.isAssignable(
                                assignableDataTypeId, ArrayUtil.getBoxedType(typedElements))) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch, "Method argument DataType mismatch");
                        }
                      }
                    }
                    if (methodValue == null) {
                      convertedValue = null;
                    } else {
                      convertedValue = new NodeId[Array.getLength(methodValue)];
                      for (int valueIndex = 0; valueIndex < convertedValue.length; valueIndex++) {
                        Object valueElement = Array.get(methodValue, valueIndex);
                        convertedValue[valueIndex] = (NodeId) valueElement;
                      }
                    }
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                try {
                  Object wireValue = convertedValue;
                  Object wireElements =
                      wireValue instanceof Matrix ? ((Matrix) wireValue).getElements() : wireValue;
                  var numericWireValues = new ArrayDeque<Object[]>();
                  var numericWirePath =
                      Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>());
                  if (wireValue != null) {
                    numericWireValues.push(new Object[] {wireValue, false});
                  }
                  while (!numericWireValues.isEmpty()) {
                    Object[] numericWireFrame = numericWireValues.pop();
                    Object numericWireValue = numericWireFrame[0];
                    if ((Boolean) numericWireFrame[1]) {
                      numericWirePath.remove(numericWireValue);
                      continue;
                    }
                    while (numericWireValue instanceof Variant
                        || numericWireValue instanceof DataValue) {
                      if (numericWireValue instanceof DataValue) {
                        if (((DataValue) numericWireValue).getValue() == null) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "A DataValue requires a value wrapper; use Variant.NULL_VALUE for"
                                  + " null");
                        }
                        if (((DataValue) numericWireValue).getStatusCode() == null) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "A DataValue requires a StatusCode; use StatusCode.GOOD for Good");
                        }
                        numericWireValue = ((DataValue) numericWireValue).getValue();
                      } else {
                        numericWireValue = ((Variant) numericWireValue).getValue();
                      }
                    }
                    if (numericWireValue instanceof Matrix) {
                      numericWireValue = ((Matrix) numericWireValue).getElements();
                    }
                    if (numericWireValue != null && numericWireValue.getClass().isArray()) {
                      if (!numericWirePath.add(numericWireValue)) {
                        throw new UaException(
                            StatusCodes.Bad_TypeMismatch,
                            "Cyclic Variant arrays cannot be encoded");
                      }
                      numericWireValues.push(new Object[] {numericWireValue, true});
                      for (int numericWireIndex = 0;
                          numericWireIndex < Array.getLength(numericWireValue);
                          numericWireIndex++) {
                        Object numericWireElement = Array.get(numericWireValue, numericWireIndex);
                        if (numericWireElement == null
                            && ArrayUtil.getBoxedType(numericWireValue) == Variant.class) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "A Variant wire array requires a wrapper for every element; use"
                                  + " Variant.NULL_VALUE for null");
                        }
                        if (numericWireElement == null
                            && (UaEnumeratedType.class.isAssignableFrom(
                                    ArrayUtil.getBoxedType(numericWireValue))
                                || OptionSetUInteger.class.isAssignableFrom(
                                    ArrayUtil.getBoxedType(numericWireValue)))) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "An enum or OptionSet wire array cannot encode a null element");
                        }
                        if (numericWireElement == null
                            && ArrayUtil.getBoxedType(numericWireValue) == Boolean.class) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "A Boolean wire array cannot retain a null element; Milo encodes it"
                                  + " as false");
                        }
                        if (numericWireElement == null
                            && ArrayUtil.getBoxedType(numericWireValue) == StatusCode.class) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "A StatusCode wire array cannot retain a null element; Milo encodes"
                                  + " it as Good");
                        }
                        if (numericWireElement == null
                            && Number.class.isAssignableFrom(
                                ArrayUtil.getBoxedType(numericWireValue))) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "A numeric wire array cannot retain a null element; Milo encodes it"
                                  + " as zero");
                        }
                        if (numericWireElement instanceof Variant
                            || numericWireElement instanceof DataValue) {
                          numericWireValues.push(new Object[] {numericWireElement, false});
                        }
                      }
                    }
                  }
                  wireValue =
                      ExtensionObject.encodeValue(
                          context.getServer().getStaticEncodingContext(), wireValue);
                  outputValue0 = Variant.of(wireValue);
                } catch (UaSerializationException encodingFailure) {
                  throw new UaException(
                      encodingFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                          ? StatusCodes.Bad_OutOfRange
                          : StatusCodes.Bad_TypeMismatch,
                      encodingFailure);
                } catch (ClassCastException | IllegalArgumentException encodingFailure) {
                  throw new UaException(StatusCodes.Bad_TypeMismatch, encodingFailure);
                }
              }
              return new CallMethodResult(
                  StatusCode.GOOD,
                  new StatusCode[0],
                  new DiagnosticInfo[0],
                  new Variant[] {outputValue0});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }

  @Override
  public MethodBinding bindGetGroupMembershipsDetailed(
      MethodBindings bindings, AlarmConditionTypeGetGroupMembershipsDetailedHandler handler)
      throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getGetGroupMembershipsMethodNode();
    if (methodNode == null) {
      throw new UaException(StatusCodes.Bad_NotFound, "Cannot bind an absent Method");
    }
    return bindings.bind(
        this,
        methodNode,
        new AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return new Argument[] {};
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {
              new Argument(
                  "Groups",
                  ExpandedNodeId.parse("i=17")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText(null, ""))
            };
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 0;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              var result = handler.invoke(context);
              if (result == null) {
                throw new UaException(
                    StatusCodes.Bad_InternalError, "A detailed Method handler returned null");
              }
              if (!result.hasOutputs()) {
                return new CallMethodResult(
                    result.status(),
                    result.inputResults(),
                    result.inputDiagnostics(),
                    new Variant[0]);
              }
              var outputs = result.outputs();
              Variant outputValue0;
              {
                @Nullable NodeId @Nullable [] convertedValue;
                {
                  Object methodValue = outputs;
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    NamespaceTable namespaceTable = context.getServer().getNamespaceTable();
                    DataTypeTree dataTypeTree = context.getServer().getDataTypeTree();
                    NodeId argumentDataTypeId =
                        ExpandedNodeId.parse("i=17")
                            .toNodeId(namespaceTable)
                            .orElseThrow(
                                () ->
                                    new UaException(
                                        StatusCodes.Bad_NodeIdInvalid,
                                        "Method argument DataType namespace is unavailable"));
                    if (!OpcUaDataType.isBuiltin(argumentDataTypeId)
                        && dataTypeTree.getDataType(argumentDataTypeId) == null) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "Method argument Groups (effective property i=25154, DataType i=17) is"
                              + " unavailable in the effective type tree; resolved DataType: "
                              + argumentDataTypeId);
                    }
                    Object numericElements =
                        methodValue instanceof Matrix
                            ? ((Matrix) methodValue).getElements()
                            : methodValue;
                    if (numericElements != null
                        && numericElements.getClass().isArray()
                        && (numericElements.getClass().getComponentType() == Number.class
                            || numericElements.getClass().getComponentType() == UNumber.class)
                        && (argumentDataTypeId.equals(NodeIds.Number)
                            || dataTypeTree.isSubtypeOf(argumentDataTypeId, NodeIds.Number))) {
                      Class<?> numericElementType = null;
                      for (int numericIndex = 0;
                          numericIndex < Array.getLength(numericElements);
                          numericIndex++) {
                        Object numericElement = Array.get(numericElements, numericIndex);
                        if (numericElement != null) {
                          if (numericElementType != null
                              && numericElementType != numericElement.getClass()) {
                            throw new UaException(
                                StatusCodes.Bad_TypeMismatch,
                                "An abstract numeric array requires one homogeneous wire element"
                                    + " type");
                          }
                          numericElementType = numericElement.getClass();
                        }
                      }
                      if (numericElementType == null) {
                        numericElementType = dataTypeTree.getBackingClass(argumentDataTypeId);
                      }
                      if (numericElementType == Number.class
                          || numericElementType == UNumber.class) {
                        throw new UaException(
                            StatusCodes.Bad_TypeMismatch,
                            "An empty or all-null abstract numeric array requires a concretely"
                                + " typed array");
                      }
                      Object numericArray =
                          Array.newInstance(numericElementType, Array.getLength(numericElements));
                      for (int numericIndex = 0;
                          numericIndex < Array.getLength(numericElements);
                          numericIndex++) {
                        Array.set(
                            numericArray, numericIndex, Array.get(numericElements, numericIndex));
                      }
                      if (methodValue instanceof Matrix) {
                        methodValue =
                            new Matrix(
                                numericArray,
                                ((Matrix) methodValue).getDimensions().clone(),
                                ((Matrix) methodValue)
                                    .getDataType()
                                    .orElseThrow(
                                        () ->
                                            new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "A numeric Matrix requires an explicit wire"
                                                    + " DataType")),
                                ((Matrix) methodValue).getDataTypeId().orElse(null));
                      } else {
                        methodValue = numericArray;
                      }
                    }
                    if (methodValue != null) {
                      Object shapeElements =
                          methodValue instanceof Matrix
                              ? ((Matrix) methodValue).getElements()
                              : methodValue;
                      int valueRank =
                          methodValue instanceof Matrix
                              ? ((Matrix) methodValue).getValueRank()
                              : ArrayUtil.getValueRank(methodValue);
                      boolean emptyArray =
                          methodValue.getClass().isArray()
                              && ArrayUtil.getValueRank(methodValue) == 1
                              && Array.getLength(methodValue) == 0;
                      if (!(valueRank == 1 || emptyArray)) {
                        throw new UaException(
                            StatusCodes.Bad_TypeMismatch, "Method argument ValueRank mismatch");
                      }
                      if (methodValue instanceof Matrix) {
                        int[] dimensions = ((Matrix) methodValue).getDimensions();
                        if (dimensions.length < 2
                            || !shapeElements.getClass().isArray()
                            || ArrayUtil.getValueRank(shapeElements) != 1) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "Malformed Method Matrix representation");
                        }
                        long elementCount = 1;
                        for (int dimension : dimensions) {
                          if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                            throw new UaException(
                                StatusCodes.Bad_TypeMismatch, "Malformed Method Matrix dimensions");
                          }
                          elementCount *= dimension;
                        }
                        if (elementCount != Array.getLength(shapeElements)) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "Method Matrix dimensions do not match its elements");
                        }
                        if (!(((Matrix) methodValue)
                            .getDataType()
                            .equals(Variant.of(shapeElements).getDataType()))) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "Method Matrix DataType does not match its elements");
                        }
                      }
                      Variant.of(shapeElements);
                      if (!emptyArray) {
                        int[] dimensions =
                            methodValue instanceof Matrix
                                ? ((Matrix) methodValue).getDimensions()
                                : ArrayUtil.getDimensions(methodValue);
                        long[] maximumDimensions = new long[] {0L};
                        if (dimensions.length != maximumDimensions.length) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "Method argument ArrayDimensions mismatch");
                        }
                        for (int dimensionIndex = 0;
                            dimensionIndex < dimensions.length;
                            dimensionIndex++) {
                          if (maximumDimensions[dimensionIndex] != 0
                              && dimensions[dimensionIndex] > maximumDimensions[dimensionIndex]) {
                            throw new UaException(
                                StatusCodes.Bad_TypeMismatch,
                                "Method argument exceeds ArrayDimensions maximum");
                          }
                        }
                      }
                    }
                    if (methodValue != null) {
                      Object typedElements =
                          methodValue instanceof Matrix
                              ? ((Matrix) methodValue).getElements()
                              : methodValue;
                      if (NodeIds.Structure.equals(argumentDataTypeId)
                          || dataTypeTree.isStructType(argumentDataTypeId)) {
                        var declaredType = dataTypeTree.getType(argumentDataTypeId);
                        if (typedElements.getClass().isArray()) {
                          var structureCodec =
                              context
                                  .getServer()
                                  .getStaticEncodingContext()
                                  .getDataTypeManager()
                                  .getCodec(argumentDataTypeId);
                          Class<?> structureClass =
                              structureCodec == null
                                  ? UaStructuredType.class
                                  : structureCodec.getType();
                          Object decodedStructures =
                              Array.newInstance(structureClass, Array.getLength(typedElements));
                          for (int structureIndex = 0;
                              structureIndex < Array.getLength(typedElements);
                              structureIndex++) {
                            Object structure = Array.get(typedElements, structureIndex);
                            if (structure instanceof ExtensionObject) {
                              structure =
                                  ((ExtensionObject) structure).isNull()
                                      ? null
                                      : ((ExtensionObject) structure)
                                          .decode(context.getServer().getStaticEncodingContext());
                            }
                            if (structure != null) {
                              if (!(structure instanceof UaStructuredType)) {
                                throw new UaException(
                                    StatusCodes.Bad_TypeMismatch,
                                    "Method argument requires a Structure value");
                              }
                              if (NodeIds.Structure.equals(argumentDataTypeId)
                                  || declaredType != null && declaredType.isAbstract()) {
                                if (!dataTypeTree.isSubtypeOf(
                                    ((UaStructuredType) structure)
                                        .getTypeId()
                                        .toNodeId(namespaceTable)
                                        .orElse(NodeId.NULL_VALUE),
                                    argumentDataTypeId)) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch,
                                      "Method Structure is not a subtype of the effective"
                                          + " DataType");
                                }
                              } else {
                                if (!argumentDataTypeId.equals(
                                    ((UaStructuredType) structure)
                                        .getTypeId()
                                        .toNodeId(namespaceTable)
                                        .orElse(NodeId.NULL_VALUE))) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch,
                                      "Method Structure does not match the effective DataType");
                                }
                              }
                            }
                            Array.set(decodedStructures, structureIndex, structure);
                          }
                          methodValue =
                              methodValue instanceof Matrix
                                  ? new Matrix(
                                      decodedStructures,
                                      ((Matrix) methodValue).getDimensions().clone())
                                  : decodedStructures;
                        } else {
                          if (typedElements instanceof ExtensionObject) {
                            typedElements =
                                ((ExtensionObject) typedElements).isNull()
                                    ? null
                                    : ((ExtensionObject) typedElements)
                                        .decode(context.getServer().getStaticEncodingContext());
                          }
                          if (typedElements != null) {
                            if (!(typedElements instanceof UaStructuredType)) {
                              throw new UaException(
                                  StatusCodes.Bad_TypeMismatch,
                                  "Method argument requires a Structure value");
                            }
                            if (NodeIds.Structure.equals(argumentDataTypeId)
                                || declaredType != null && declaredType.isAbstract()) {
                              if (!dataTypeTree.isSubtypeOf(
                                  ((UaStructuredType) typedElements)
                                      .getTypeId()
                                      .toNodeId(namespaceTable)
                                      .orElse(NodeId.NULL_VALUE),
                                  argumentDataTypeId)) {
                                throw new UaException(
                                    StatusCodes.Bad_TypeMismatch,
                                    "Method Structure is not a subtype of the effective DataType");
                              }
                            } else {
                              if (!argumentDataTypeId.equals(
                                  ((UaStructuredType) typedElements)
                                      .getTypeId()
                                      .toNodeId(namespaceTable)
                                      .orElse(NodeId.NULL_VALUE))) {
                                throw new UaException(
                                    StatusCodes.Bad_TypeMismatch,
                                    "Method Structure does not match the effective DataType");
                              }
                            }
                          }
                          methodValue = typedElements;
                        }
                      } else {
                        Variant.of(typedElements);
                        NodeId assignableDataTypeId =
                            dataTypeTree.getBackingClass(argumentDataTypeId) == Number.class
                                    && dataTypeTree.isSubtypeOf(argumentDataTypeId, NodeIds.Integer)
                                ? NodeIds.Integer
                                : argumentDataTypeId;
                        if (dataTypeTree.getBackingClass(argumentDataTypeId) != Variant.class
                            && !dataTypeTree.isAssignable(
                                assignableDataTypeId, ArrayUtil.getBoxedType(typedElements))) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch, "Method argument DataType mismatch");
                        }
                      }
                    }
                    if (methodValue == null) {
                      convertedValue = null;
                    } else {
                      convertedValue = new NodeId[Array.getLength(methodValue)];
                      for (int valueIndex = 0; valueIndex < convertedValue.length; valueIndex++) {
                        Object valueElement = Array.get(methodValue, valueIndex);
                        convertedValue[valueIndex] = (NodeId) valueElement;
                      }
                    }
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                try {
                  Object wireValue = convertedValue;
                  Object wireElements =
                      wireValue instanceof Matrix ? ((Matrix) wireValue).getElements() : wireValue;
                  var numericWireValues = new ArrayDeque<Object[]>();
                  var numericWirePath =
                      Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>());
                  if (wireValue != null) {
                    numericWireValues.push(new Object[] {wireValue, false});
                  }
                  while (!numericWireValues.isEmpty()) {
                    Object[] numericWireFrame = numericWireValues.pop();
                    Object numericWireValue = numericWireFrame[0];
                    if ((Boolean) numericWireFrame[1]) {
                      numericWirePath.remove(numericWireValue);
                      continue;
                    }
                    while (numericWireValue instanceof Variant
                        || numericWireValue instanceof DataValue) {
                      if (numericWireValue instanceof DataValue) {
                        if (((DataValue) numericWireValue).getValue() == null) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "A DataValue requires a value wrapper; use Variant.NULL_VALUE for"
                                  + " null");
                        }
                        if (((DataValue) numericWireValue).getStatusCode() == null) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "A DataValue requires a StatusCode; use StatusCode.GOOD for Good");
                        }
                        numericWireValue = ((DataValue) numericWireValue).getValue();
                      } else {
                        numericWireValue = ((Variant) numericWireValue).getValue();
                      }
                    }
                    if (numericWireValue instanceof Matrix) {
                      numericWireValue = ((Matrix) numericWireValue).getElements();
                    }
                    if (numericWireValue != null && numericWireValue.getClass().isArray()) {
                      if (!numericWirePath.add(numericWireValue)) {
                        throw new UaException(
                            StatusCodes.Bad_TypeMismatch,
                            "Cyclic Variant arrays cannot be encoded");
                      }
                      numericWireValues.push(new Object[] {numericWireValue, true});
                      for (int numericWireIndex = 0;
                          numericWireIndex < Array.getLength(numericWireValue);
                          numericWireIndex++) {
                        Object numericWireElement = Array.get(numericWireValue, numericWireIndex);
                        if (numericWireElement == null
                            && ArrayUtil.getBoxedType(numericWireValue) == Variant.class) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "A Variant wire array requires a wrapper for every element; use"
                                  + " Variant.NULL_VALUE for null");
                        }
                        if (numericWireElement == null
                            && (UaEnumeratedType.class.isAssignableFrom(
                                    ArrayUtil.getBoxedType(numericWireValue))
                                || OptionSetUInteger.class.isAssignableFrom(
                                    ArrayUtil.getBoxedType(numericWireValue)))) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "An enum or OptionSet wire array cannot encode a null element");
                        }
                        if (numericWireElement == null
                            && ArrayUtil.getBoxedType(numericWireValue) == Boolean.class) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "A Boolean wire array cannot retain a null element; Milo encodes it"
                                  + " as false");
                        }
                        if (numericWireElement == null
                            && ArrayUtil.getBoxedType(numericWireValue) == StatusCode.class) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "A StatusCode wire array cannot retain a null element; Milo encodes"
                                  + " it as Good");
                        }
                        if (numericWireElement == null
                            && Number.class.isAssignableFrom(
                                ArrayUtil.getBoxedType(numericWireValue))) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "A numeric wire array cannot retain a null element; Milo encodes it"
                                  + " as zero");
                        }
                        if (numericWireElement instanceof Variant
                            || numericWireElement instanceof DataValue) {
                          numericWireValues.push(new Object[] {numericWireElement, false});
                        }
                      }
                    }
                  }
                  wireValue =
                      ExtensionObject.encodeValue(
                          context.getServer().getStaticEncodingContext(), wireValue);
                  outputValue0 = Variant.of(wireValue);
                } catch (UaSerializationException encodingFailure) {
                  throw new UaException(
                      encodingFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                          ? StatusCodes.Bad_OutOfRange
                          : StatusCodes.Bad_TypeMismatch,
                      encodingFailure);
                } catch (ClassCastException | IllegalArgumentException encodingFailure) {
                  throw new UaException(StatusCodes.Bad_TypeMismatch, encodingFailure);
                }
              }
              return new CallMethodResult(
                  result.status(),
                  new StatusCode[0],
                  new DiagnosticInfo[0],
                  new Variant[] {outputValue0});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }
}
