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

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.InvalidArgumentException;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.methods.PubSubDiagnosticsTypeResetDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.PubSubDiagnosticsTypeResetHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PubSubDiagnosticsCounterTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

public class PubSubDiagnosticsTypeNode extends BaseObjectTypeNode implements PubSubDiagnosticsType {
  public PubSubDiagnosticsTypeNode(
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

  public PubSubDiagnosticsTypeNode(
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
  public BaseDataVariableTypeNode getDiagnosticsLevelNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:DiagnosticsLevel (declaration i=19678, owner i=19677)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "DiagnosticsLevel");
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
                    + " while resolving http://opcfoundation.org/UA/:DiagnosticsLevel (declaration"
                    + " i=19678, owner i=19677) on "
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
              "http://opcfoundation.org/UA/:DiagnosticsLevel (declaration i=19678, owner i=19677)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:DiagnosticsLevel (declaration i=19678, owner i=19677)"
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
              "http://opcfoundation.org/UA/:DiagnosticsLevel (declaration i=19678, owner i=19677)"
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
            "http://opcfoundation.org/UA/:DiagnosticsLevel (declaration i=19678, owner i=19677)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:DiagnosticsLevel (declaration i=19678, owner i=19677)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:DiagnosticsLevel (declaration i=19678, owner i=19677)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof BaseDataVariableTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:DiagnosticsLevel (declaration i=19678, owner i=19677)"
              + " on "
              + getNodeId());
    }
    return (BaseDataVariableTypeNode) parent;
  }

  @Override
  public @Nullable DiagnosticsLevel getDiagnosticsLevel() {
    var node = getDiagnosticsLevelNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DiagnosticsLevel (declaration i=19678, owner i=19677)"
              + " on "
              + getNodeId());
    }
    Object value = node.getValue().getValue().getValue();
    Object convertedValue;
    {
      if (value == null || value instanceof Matrix && ((Matrix) value).isNull()) {
        convertedValue = null;
      } else {
        Object elements = value instanceof Matrix ? ((Matrix) value).getElements() : value;
        int rank =
            value instanceof Matrix
                ? ((Matrix) value).getValueRank()
                : ArrayUtil.getValueRank(value);
        boolean permitted = rank == -1;
        if (!permitted) {
          throw new UaRuntimeException(
              StatusCodes.Bad_TypeMismatch,
              "DiagnosticsLevel: ValueRank=-1 does not permit rank " + rank);
        }
        if (value != null && !((Object) value instanceof DiagnosticsLevel)) {
          if (!(value instanceof Integer)) {
            throw new UaRuntimeException(
                StatusCodes.Bad_TypeMismatch,
                "DiagnosticsLevel: expected"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel or"
                    + " Int32, got "
                    + value);
          }
          if (DiagnosticsLevel.from((Integer) value) == null) {
            throw new UaRuntimeException(
                StatusCodes.Bad_OutOfRange,
                "DiagnosticsLevel: unknown"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel value "
                    + value);
          }
        }
        convertedValue =
            value == null || value instanceof DiagnosticsLevel
                ? (DiagnosticsLevel) value
                : DiagnosticsLevel.from((Integer) value);
      }
    }
    return (DiagnosticsLevel) convertedValue;
  }

  @Override
  public void setDiagnosticsLevel(@Nullable DiagnosticsLevel value) {
    var node = getDiagnosticsLevelNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DiagnosticsLevel (declaration i=19678, owner i=19677)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PubSubDiagnosticsCounterTypeNode getTotalInformationNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:TotalInformation (declaration i=19679, owner i=19677)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "TotalInformation");
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
                    + " while resolving http://opcfoundation.org/UA/:TotalInformation (declaration"
                    + " i=19679, owner i=19677) on "
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
              "http://opcfoundation.org/UA/:TotalInformation (declaration i=19679, owner i=19677)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:TotalInformation (declaration i=19679, owner i=19677)"
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
              "http://opcfoundation.org/UA/:TotalInformation (declaration i=19679, owner i=19677)"
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
            "http://opcfoundation.org/UA/:TotalInformation (declaration i=19679, owner i=19677)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:TotalInformation (declaration i=19679, owner i=19677)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:TotalInformation (declaration i=19679, owner i=19677)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof PubSubDiagnosticsCounterTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:TotalInformation (declaration i=19679, owner i=19677)"
              + " on "
              + getNodeId());
    }
    return (PubSubDiagnosticsCounterTypeNode) parent;
  }

  @Override
  public @Nullable UInteger getTotalInformation() {
    var node = getTotalInformationNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TotalInformation (declaration i=19679, owner i=19677)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setTotalInformation(@Nullable UInteger value) {
    var node = getTotalInformationNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TotalInformation (declaration i=19679, owner i=19677)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PubSubDiagnosticsCounterTypeNode getTotalErrorNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:TotalError (declaration i=19684, owner i=19677)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "TotalError");
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
                    + " while resolving http://opcfoundation.org/UA/:TotalError (declaration"
                    + " i=19684, owner i=19677) on "
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
              "http://opcfoundation.org/UA/:TotalError (declaration i=19684, owner i=19677)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:TotalError (declaration i=19684, owner i=19677)"
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
              "http://opcfoundation.org/UA/:TotalError (declaration i=19684, owner i=19677)"
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
            "http://opcfoundation.org/UA/:TotalError (declaration i=19684, owner i=19677)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:TotalError (declaration i=19684, owner i=19677)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:TotalError (declaration i=19684, owner i=19677)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof PubSubDiagnosticsCounterTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:TotalError (declaration i=19684, owner i=19677)"
              + " on "
              + getNodeId());
    }
    return (PubSubDiagnosticsCounterTypeNode) parent;
  }

  @Override
  public @Nullable UInteger getTotalError() {
    var node = getTotalErrorNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TotalError (declaration i=19684, owner i=19677)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setTotalError(@Nullable UInteger value) {
    var node = getTotalErrorNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TotalError (declaration i=19684, owner i=19677)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public UaMethodNode getResetMethodNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:Reset (declaration i=19689, owner i=19677)"
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
                    + " while resolving http://opcfoundation.org/UA/:Reset (declaration i=19689,"
                    + " owner i=19677) on "
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
              "http://opcfoundation.org/UA/:Reset (declaration i=19689, owner i=19677)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:Reset (declaration i=19689, owner i=19677)"
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
              "http://opcfoundation.org/UA/:Reset (declaration i=19689, owner i=19677)"
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
            "http://opcfoundation.org/UA/:Reset (declaration i=19689, owner i=19677)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:Reset (declaration i=19689, owner i=19677)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Method) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:Reset (declaration i=19689, owner i=19677)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof UaMethodNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:Reset (declaration i=19689, owner i=19677)"
              + " on "
              + getNodeId());
    }
    return (UaMethodNode) parent;
  }

  @Override
  public MethodBinding bindReset(MethodBindings bindings, PubSubDiagnosticsTypeResetHandler handler)
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
      MethodBindings bindings, PubSubDiagnosticsTypeResetDetailedHandler handler)
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
  public BaseDataVariableTypeNode getSubErrorNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:SubError (declaration i=19690, owner i=19677)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "SubError");
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
                    + " while resolving http://opcfoundation.org/UA/:SubError (declaration i=19690,"
                    + " owner i=19677) on "
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
              "http://opcfoundation.org/UA/:SubError (declaration i=19690, owner i=19677)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:SubError (declaration i=19690, owner i=19677)"
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
              "http://opcfoundation.org/UA/:SubError (declaration i=19690, owner i=19677)"
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
            "http://opcfoundation.org/UA/:SubError (declaration i=19690, owner i=19677)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:SubError (declaration i=19690, owner i=19677)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:SubError (declaration i=19690, owner i=19677)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof BaseDataVariableTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:SubError (declaration i=19690, owner i=19677)"
              + " on "
              + getNodeId());
    }
    return (BaseDataVariableTypeNode) parent;
  }

  @Override
  public @Nullable Boolean getSubError() {
    var node = getSubErrorNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SubError (declaration i=19690, owner i=19677)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setSubError(@Nullable Boolean value) {
    var node = getSubErrorNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SubError (declaration i=19690, owner i=19677)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseObjectTypeNode getCountersNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:Counters (declaration i=19691, owner i=19677)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "Counters");
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
                    + " while resolving http://opcfoundation.org/UA/:Counters (declaration i=19691,"
                    + " owner i=19677) on "
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
              "http://opcfoundation.org/UA/:Counters (declaration i=19691, owner i=19677)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:Counters (declaration i=19691, owner i=19677)"
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
              "http://opcfoundation.org/UA/:Counters (declaration i=19691, owner i=19677)"
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
            "http://opcfoundation.org/UA/:Counters (declaration i=19691, owner i=19677)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:Counters (declaration i=19691, owner i=19677)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Object) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:Counters (declaration i=19691, owner i=19677)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof BaseObjectTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:Counters (declaration i=19691, owner i=19677)"
              + " on "
              + getNodeId());
    }
    return (BaseObjectTypeNode) parent;
  }

  @Override
  public BaseObjectTypeNode getLiveValuesNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:LiveValues (declaration i=19722, owner i=19677)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "LiveValues");
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
                    + " while resolving http://opcfoundation.org/UA/:LiveValues (declaration"
                    + " i=19722, owner i=19677) on "
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
              "http://opcfoundation.org/UA/:LiveValues (declaration i=19722, owner i=19677)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:LiveValues (declaration i=19722, owner i=19677)"
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
              "http://opcfoundation.org/UA/:LiveValues (declaration i=19722, owner i=19677)"
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
            "http://opcfoundation.org/UA/:LiveValues (declaration i=19722, owner i=19677)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:LiveValues (declaration i=19722, owner i=19677)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Object) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:LiveValues (declaration i=19722, owner i=19677)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof BaseObjectTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:LiveValues (declaration i=19722, owner i=19677)"
              + " on "
              + getNodeId());
    }
    return (BaseObjectTypeNode) parent;
  }
}
