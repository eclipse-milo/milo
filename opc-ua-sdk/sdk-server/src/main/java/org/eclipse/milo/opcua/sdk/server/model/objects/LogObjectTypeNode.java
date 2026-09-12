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
import org.eclipse.milo.opcua.sdk.core.model.methods.LogObjectTypeGetRecordsOutputs;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.InvalidArgumentException;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.methods.LogObjectTypeGetRecordsDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.LogObjectTypeGetRecordsHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.LogObjectTypeReleaseContinuationPointDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.LogObjectTypeReleaseContinuationPointHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.LogRecordMask;
import org.eclipse.milo.opcua.stack.core.types.structured.LogRecordsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

public class LogObjectTypeNode extends BaseObjectTypeNode implements LogObjectType {
  public LogObjectTypeNode(
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

  public LogObjectTypeNode(
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
  public @Nullable PropertyTypeNode getMaxRecordsNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=46").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:MaxRecords (declaration i=19356, owner i=19352)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "MaxRecords");
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
                    + " while resolving http://opcfoundation.org/UA/:MaxRecords (declaration"
                    + " i=19356, owner i=19352) on "
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
              "http://opcfoundation.org/UA/:MaxRecords (declaration i=19356, owner i=19352)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:MaxRecords (declaration i=19356, owner i=19352)"
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
              "http://opcfoundation.org/UA/:MaxRecords (declaration i=19356, owner i=19352)"
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
            "http://opcfoundation.org/UA/:MaxRecords (declaration i=19356, owner i=19352)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:MaxRecords (declaration i=19356, owner i=19352)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof PropertyTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:MaxRecords (declaration i=19356, owner i=19352)"
              + " on "
              + getNodeId());
    }
    return (PropertyTypeNode) parent;
  }

  @Override
  public @Nullable UInteger getMaxRecords() {
    var node = getMaxRecordsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxRecords (declaration i=19356, owner i=19352)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxRecords(@Nullable UInteger value) {
    var node = getMaxRecordsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxRecords (declaration i=19356, owner i=19352)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxStorageDurationNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=46").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:MaxStorageDuration (declaration i=19357, owner i=19352)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "MaxStorageDuration");
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
                    + " while resolving http://opcfoundation.org/UA/:MaxStorageDuration"
                    + " (declaration i=19357, owner i=19352) on "
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
              "http://opcfoundation.org/UA/:MaxStorageDuration (declaration i=19357, owner i=19352)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:MaxStorageDuration (declaration i=19357, owner i=19352)"
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
              "http://opcfoundation.org/UA/:MaxStorageDuration (declaration i=19357, owner i=19352)"
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
            "http://opcfoundation.org/UA/:MaxStorageDuration (declaration i=19357, owner i=19352)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:MaxStorageDuration (declaration i=19357, owner i=19352)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof PropertyTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:MaxStorageDuration (declaration i=19357, owner i=19352)"
              + " on "
              + getNodeId());
    }
    return (PropertyTypeNode) parent;
  }

  @Override
  public @Nullable Double getMaxStorageDuration() {
    var node = getMaxStorageDurationNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxStorageDuration (declaration i=19357, owner i=19352)"
              + " on "
              + getNodeId());
    }
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxStorageDuration(@Nullable Double value) {
    var node = getMaxStorageDurationNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxStorageDuration (declaration i=19357, owner i=19352)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMinimumSeverityNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=46").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:MinimumSeverity (declaration i=19744, owner i=19352)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "MinimumSeverity");
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
                    + " while resolving http://opcfoundation.org/UA/:MinimumSeverity (declaration"
                    + " i=19744, owner i=19352) on "
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
              "http://opcfoundation.org/UA/:MinimumSeverity (declaration i=19744, owner i=19352)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:MinimumSeverity (declaration i=19744, owner i=19352)"
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
              "http://opcfoundation.org/UA/:MinimumSeverity (declaration i=19744, owner i=19352)"
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
            "http://opcfoundation.org/UA/:MinimumSeverity (declaration i=19744, owner i=19352)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:MinimumSeverity (declaration i=19744, owner i=19352)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof PropertyTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:MinimumSeverity (declaration i=19744, owner i=19352)"
              + " on "
              + getNodeId());
    }
    return (PropertyTypeNode) parent;
  }

  @Override
  public @Nullable UShort getMinimumSeverity() {
    var node = getMinimumSeverityNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MinimumSeverity (declaration i=19744, owner i=19352)"
              + " on "
              + getNodeId());
    }
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setMinimumSeverity(@Nullable UShort value) {
    var node = getMinimumSeverityNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MinimumSeverity (declaration i=19744, owner i=19352)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public UaMethodNode getGetRecordsMethodNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:GetRecords (declaration i=19353, owner i=19352)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "GetRecords");
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
                    + " while resolving http://opcfoundation.org/UA/:GetRecords (declaration"
                    + " i=19353, owner i=19352) on "
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
              "http://opcfoundation.org/UA/:GetRecords (declaration i=19353, owner i=19352)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:GetRecords (declaration i=19353, owner i=19352)"
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
              "http://opcfoundation.org/UA/:GetRecords (declaration i=19353, owner i=19352)"
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
            "http://opcfoundation.org/UA/:GetRecords (declaration i=19353, owner i=19352)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:GetRecords (declaration i=19353, owner i=19352)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Method) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:GetRecords (declaration i=19353, owner i=19352)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof UaMethodNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:GetRecords (declaration i=19353, owner i=19352)"
              + " on "
              + getNodeId());
    }
    return (UaMethodNode) parent;
  }

  @Override
  public MethodBinding bindGetRecords(
      MethodBindings bindings, LogObjectTypeGetRecordsHandler handler) throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getGetRecordsMethodNode();
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
                  "StartTime",
                  ExpandedNodeId.parse("i=13")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, "")),
              new Argument(
                  "EndTime",
                  ExpandedNodeId.parse("i=13")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, "")),
              new Argument(
                  "MaxReturnRecords",
                  ExpandedNodeId.parse("i=7")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, "")),
              new Argument(
                  "MinimumSeverity",
                  ExpandedNodeId.parse("i=5")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, "")),
              new Argument(
                  "RequestMask",
                  ExpandedNodeId.parse("i=19749")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, "")),
              new Argument(
                  "ContinuationPointIn",
                  ExpandedNodeId.parse("i=15")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, ""))
            };
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {
              new Argument(
                  "Results",
                  ExpandedNodeId.parse("i=19745")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, "")),
              new Argument(
                  "ContinuationPointOut",
                  ExpandedNodeId.parse("i=15")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, ""))
            };
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 6;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            @Nullable DateTime callbackInput0 = null;
            if (inputValues.length > 0) {
              try {
                @Nullable DateTime convertedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput0 = (DateTime) methodValue;
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
                @Nullable DateTime projectedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput0 = (DateTime) methodValue;
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
            @Nullable DateTime callbackInput1 = null;
            if (inputValues.length > 1) {
              try {
                @Nullable DateTime convertedInput1;
                {
                  Object methodValue = inputValues[1].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput1 = (DateTime) methodValue;
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
                @Nullable DateTime projectedInput1;
                {
                  Object methodValue = inputValues[1].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput1 = (DateTime) methodValue;
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
                callbackInput1 = projectedInput1;
              } catch (UaException failure) {
                inputResults[1] = failure.getStatusCode();
              }
            }
            @Nullable UInteger callbackInput2 = null;
            if (inputValues.length > 2) {
              try {
                @Nullable UInteger convertedInput2;
                {
                  Object methodValue = inputValues[2].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput2 = (UInteger) methodValue;
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
                @Nullable UInteger projectedInput2;
                {
                  Object methodValue = inputValues[2].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput2 = (UInteger) methodValue;
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
                callbackInput2 = projectedInput2;
              } catch (UaException failure) {
                inputResults[2] = failure.getStatusCode();
              }
            }
            @Nullable UShort callbackInput3 = null;
            if (inputValues.length > 3) {
              try {
                @Nullable UShort convertedInput3;
                {
                  Object methodValue = inputValues[3].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput3 = (UShort) methodValue;
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
                @Nullable UShort projectedInput3;
                {
                  Object methodValue = inputValues[3].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput3 = (UShort) methodValue;
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
                callbackInput3 = projectedInput3;
              } catch (UaException failure) {
                inputResults[3] = failure.getStatusCode();
              }
            }
            @Nullable LogRecordMask callbackInput4 = null;
            if (inputValues.length > 4) {
              try {
                @Nullable LogRecordMask convertedInput4;
                {
                  Object methodValue = inputValues[4].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput4 =
                        methodValue == null || methodValue instanceof LogRecordMask
                            ? (LogRecordMask) methodValue
                            : new LogRecordMask((UInteger) methodValue);
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
                @Nullable LogRecordMask projectedInput4;
                {
                  Object methodValue = inputValues[4].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput4 =
                        methodValue == null || methodValue instanceof LogRecordMask
                            ? (LogRecordMask) methodValue
                            : new LogRecordMask((UInteger) methodValue);
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
                callbackInput4 = projectedInput4;
              } catch (UaException failure) {
                inputResults[4] = failure.getStatusCode();
              }
            }
            @Nullable ByteString callbackInput5 = null;
            if (inputValues.length > 5) {
              try {
                @Nullable ByteString convertedInput5;
                {
                  Object methodValue = inputValues[5].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput5 = (ByteString) methodValue;
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
                @Nullable ByteString projectedInput5;
                {
                  Object methodValue = inputValues[5].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput5 = (ByteString) methodValue;
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
                callbackInput5 = projectedInput5;
              } catch (UaException failure) {
                inputResults[5] = failure.getStatusCode();
              }
            }
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              LogObjectTypeGetRecordsOutputs outputs =
                  handler.invoke(
                      context,
                      callbackInput0,
                      callbackInput1,
                      callbackInput2,
                      callbackInput3,
                      callbackInput4,
                      callbackInput5);
              if (outputs == null) {
                throw new UaException(
                    StatusCodes.Bad_InternalError,
                    "A multiple-output Method handler returned a null output container");
              }
              Variant outputValue0;
              {
                @Nullable LogRecordsDataType convertedValue;
                {
                  Object methodValue = outputs.results();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    NamespaceTable namespaceTable = context.getServer().getNamespaceTable();
                    DataTypeTree dataTypeTree = context.getServer().getDataTypeTree();
                    NodeId argumentDataTypeId =
                        ExpandedNodeId.parse("i=19745")
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
                          "Method argument Results (effective property i=19355, DataType i=19745)"
                              + " is unavailable in the effective type tree; resolved DataType: "
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
                      if (!(valueRank == -1)) {
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
                    convertedValue = (LogRecordsDataType) methodValue;
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
              Variant outputValue1;
              {
                @Nullable ByteString convertedValue;
                {
                  Object methodValue = outputs.continuationPointOut();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    NamespaceTable namespaceTable = context.getServer().getNamespaceTable();
                    DataTypeTree dataTypeTree = context.getServer().getDataTypeTree();
                    NodeId argumentDataTypeId =
                        ExpandedNodeId.parse("i=15")
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
                          "Method argument ContinuationPointOut (effective property i=19355,"
                              + " DataType i=15) is unavailable in the effective type tree;"
                              + " resolved DataType: "
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
                      if (!(valueRank == -1)) {
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
                    convertedValue = (ByteString) methodValue;
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
                  outputValue1 = Variant.of(wireValue);
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
                  new Variant[] {outputValue0, outputValue1});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }

  @Override
  public MethodBinding bindGetRecordsDetailed(
      MethodBindings bindings, LogObjectTypeGetRecordsDetailedHandler handler) throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getGetRecordsMethodNode();
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
                  "StartTime",
                  ExpandedNodeId.parse("i=13")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, "")),
              new Argument(
                  "EndTime",
                  ExpandedNodeId.parse("i=13")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, "")),
              new Argument(
                  "MaxReturnRecords",
                  ExpandedNodeId.parse("i=7")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, "")),
              new Argument(
                  "MinimumSeverity",
                  ExpandedNodeId.parse("i=5")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, "")),
              new Argument(
                  "RequestMask",
                  ExpandedNodeId.parse("i=19749")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, "")),
              new Argument(
                  "ContinuationPointIn",
                  ExpandedNodeId.parse("i=15")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, ""))
            };
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {
              new Argument(
                  "Results",
                  ExpandedNodeId.parse("i=19745")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, "")),
              new Argument(
                  "ContinuationPointOut",
                  ExpandedNodeId.parse("i=15")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, ""))
            };
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 6;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            @Nullable DateTime callbackInput0 = null;
            if (inputValues.length > 0) {
              try {
                @Nullable DateTime convertedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput0 = (DateTime) methodValue;
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
                @Nullable DateTime projectedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput0 = (DateTime) methodValue;
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
            @Nullable DateTime callbackInput1 = null;
            if (inputValues.length > 1) {
              try {
                @Nullable DateTime convertedInput1;
                {
                  Object methodValue = inputValues[1].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput1 = (DateTime) methodValue;
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
                @Nullable DateTime projectedInput1;
                {
                  Object methodValue = inputValues[1].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput1 = (DateTime) methodValue;
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
                callbackInput1 = projectedInput1;
              } catch (UaException failure) {
                inputResults[1] = failure.getStatusCode();
              }
            }
            @Nullable UInteger callbackInput2 = null;
            if (inputValues.length > 2) {
              try {
                @Nullable UInteger convertedInput2;
                {
                  Object methodValue = inputValues[2].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput2 = (UInteger) methodValue;
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
                @Nullable UInteger projectedInput2;
                {
                  Object methodValue = inputValues[2].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput2 = (UInteger) methodValue;
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
                callbackInput2 = projectedInput2;
              } catch (UaException failure) {
                inputResults[2] = failure.getStatusCode();
              }
            }
            @Nullable UShort callbackInput3 = null;
            if (inputValues.length > 3) {
              try {
                @Nullable UShort convertedInput3;
                {
                  Object methodValue = inputValues[3].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput3 = (UShort) methodValue;
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
                @Nullable UShort projectedInput3;
                {
                  Object methodValue = inputValues[3].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput3 = (UShort) methodValue;
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
                callbackInput3 = projectedInput3;
              } catch (UaException failure) {
                inputResults[3] = failure.getStatusCode();
              }
            }
            @Nullable LogRecordMask callbackInput4 = null;
            if (inputValues.length > 4) {
              try {
                @Nullable LogRecordMask convertedInput4;
                {
                  Object methodValue = inputValues[4].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput4 =
                        methodValue == null || methodValue instanceof LogRecordMask
                            ? (LogRecordMask) methodValue
                            : new LogRecordMask((UInteger) methodValue);
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
                @Nullable LogRecordMask projectedInput4;
                {
                  Object methodValue = inputValues[4].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput4 =
                        methodValue == null || methodValue instanceof LogRecordMask
                            ? (LogRecordMask) methodValue
                            : new LogRecordMask((UInteger) methodValue);
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
                callbackInput4 = projectedInput4;
              } catch (UaException failure) {
                inputResults[4] = failure.getStatusCode();
              }
            }
            @Nullable ByteString callbackInput5 = null;
            if (inputValues.length > 5) {
              try {
                @Nullable ByteString convertedInput5;
                {
                  Object methodValue = inputValues[5].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput5 = (ByteString) methodValue;
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
                @Nullable ByteString projectedInput5;
                {
                  Object methodValue = inputValues[5].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput5 = (ByteString) methodValue;
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
                callbackInput5 = projectedInput5;
              } catch (UaException failure) {
                inputResults[5] = failure.getStatusCode();
              }
            }
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              var result =
                  handler.invoke(
                      context,
                      callbackInput0,
                      callbackInput1,
                      callbackInput2,
                      callbackInput3,
                      callbackInput4,
                      callbackInput5);
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
              if (outputs == null) {
                throw new UaException(
                    StatusCodes.Bad_InternalError,
                    "A multiple-output Method handler returned a null output container");
              }
              Variant outputValue0;
              {
                @Nullable LogRecordsDataType convertedValue;
                {
                  Object methodValue = outputs.results();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    NamespaceTable namespaceTable = context.getServer().getNamespaceTable();
                    DataTypeTree dataTypeTree = context.getServer().getDataTypeTree();
                    NodeId argumentDataTypeId =
                        ExpandedNodeId.parse("i=19745")
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
                          "Method argument Results (effective property i=19355, DataType i=19745)"
                              + " is unavailable in the effective type tree; resolved DataType: "
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
                      if (!(valueRank == -1)) {
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
                    convertedValue = (LogRecordsDataType) methodValue;
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
              Variant outputValue1;
              {
                @Nullable ByteString convertedValue;
                {
                  Object methodValue = outputs.continuationPointOut();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    NamespaceTable namespaceTable = context.getServer().getNamespaceTable();
                    DataTypeTree dataTypeTree = context.getServer().getDataTypeTree();
                    NodeId argumentDataTypeId =
                        ExpandedNodeId.parse("i=15")
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
                          "Method argument ContinuationPointOut (effective property i=19355,"
                              + " DataType i=15) is unavailable in the effective type tree;"
                              + " resolved DataType: "
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
                      if (!(valueRank == -1)) {
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
                    convertedValue = (ByteString) methodValue;
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
                  outputValue1 = Variant.of(wireValue);
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
                  new Variant[] {outputValue0, outputValue1});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }

  @Override
  public @Nullable UaMethodNode getReleaseContinuationPointMethodNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:ReleaseContinuationPoint (declaration i=24372, owner"
                + " i=19352) on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "ReleaseContinuationPoint");
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
                    + " while resolving http://opcfoundation.org/UA/:ReleaseContinuationPoint"
                    + " (declaration i=24372, owner i=19352) on "
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
              "http://opcfoundation.org/UA/:ReleaseContinuationPoint (declaration i=24372, owner"
                  + " i=19352) on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:ReleaseContinuationPoint (declaration i=24372, owner"
                  + " i=19352) on "
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
              "http://opcfoundation.org/UA/:ReleaseContinuationPoint (declaration i=24372, owner"
                  + " i=19352) on "
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
            "http://opcfoundation.org/UA/:ReleaseContinuationPoint (declaration i=24372, owner"
                + " i=19352) on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Method) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:ReleaseContinuationPoint (declaration i=24372, owner"
                + " i=19352) on "
                + getNodeId());
      }
    }
    if (!(parent instanceof UaMethodNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:ReleaseContinuationPoint (declaration i=24372, owner"
              + " i=19352) on "
              + getNodeId());
    }
    return (UaMethodNode) parent;
  }

  @Override
  public MethodBinding bindReleaseContinuationPoint(
      MethodBindings bindings, LogObjectTypeReleaseContinuationPointHandler handler)
      throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getReleaseContinuationPointMethodNode();
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
                  "ContinuationPointIn",
                  ExpandedNodeId.parse("i=15")
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
            @Nullable ByteString callbackInput0 = null;
            if (inputValues.length > 0) {
              try {
                @Nullable ByteString convertedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput0 = (ByteString) methodValue;
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
                @Nullable ByteString projectedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput0 = (ByteString) methodValue;
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
  public MethodBinding bindReleaseContinuationPointDetailed(
      MethodBindings bindings, LogObjectTypeReleaseContinuationPointDetailedHandler handler)
      throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getReleaseContinuationPointMethodNode();
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
                  "ContinuationPointIn",
                  ExpandedNodeId.parse("i=15")
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
            @Nullable ByteString callbackInput0 = null;
            if (inputValues.length > 0) {
              try {
                @Nullable ByteString convertedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput0 = (ByteString) methodValue;
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
                @Nullable ByteString projectedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput0 = (ByteString) methodValue;
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
}
