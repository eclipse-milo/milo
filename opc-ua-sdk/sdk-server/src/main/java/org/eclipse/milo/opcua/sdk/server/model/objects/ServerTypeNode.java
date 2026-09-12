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
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerTypeGetMonitoredItemsOutputs;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.InvalidArgumentException;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.methods.ServerTypeGetMonitoredItemsDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.ServerTypeGetMonitoredItemsHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.ServerTypeRequestServerStateChangeDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.ServerTypeRequestServerStateChangeHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.ServerTypeResendDataDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.ServerTypeResendDataHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.ServerTypeSetSubscriptionDurableDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.ServerTypeSetSubscriptionDurableHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ServerStatusTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerStatusDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.TimeZoneDataType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

public class ServerTypeNode extends BaseObjectTypeNode implements ServerType {
  public ServerTypeNode(
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

  public ServerTypeNode(
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
  public PropertyTypeNode getServerArrayNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=46").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:ServerArray (declaration i=2005, owner i=2004)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "ServerArray");
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
                    + " while resolving http://opcfoundation.org/UA/:ServerArray (declaration"
                    + " i=2005, owner i=2004) on "
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
              "http://opcfoundation.org/UA/:ServerArray (declaration i=2005, owner i=2004)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:ServerArray (declaration i=2005, owner i=2004)"
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
              "http://opcfoundation.org/UA/:ServerArray (declaration i=2005, owner i=2004)"
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
            "http://opcfoundation.org/UA/:ServerArray (declaration i=2005, owner i=2004)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:ServerArray (declaration i=2005, owner i=2004)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:ServerArray (declaration i=2005, owner i=2004)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof PropertyTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:ServerArray (declaration i=2005, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (PropertyTypeNode) parent;
  }

  @Override
  public @Nullable String @Nullable [] getServerArray() {
    var node = getServerArrayNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServerArray (declaration i=2005, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (String[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setServerArray(@Nullable String @Nullable [] value) {
    var node = getServerArrayNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServerArray (declaration i=2005, owner i=2004)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getNamespaceArrayNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=46").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:NamespaceArray (declaration i=2006, owner i=2004)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "NamespaceArray");
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
                    + " while resolving http://opcfoundation.org/UA/:NamespaceArray (declaration"
                    + " i=2006, owner i=2004) on "
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
              "http://opcfoundation.org/UA/:NamespaceArray (declaration i=2006, owner i=2004)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:NamespaceArray (declaration i=2006, owner i=2004)"
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
              "http://opcfoundation.org/UA/:NamespaceArray (declaration i=2006, owner i=2004)"
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
            "http://opcfoundation.org/UA/:NamespaceArray (declaration i=2006, owner i=2004)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:NamespaceArray (declaration i=2006, owner i=2004)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:NamespaceArray (declaration i=2006, owner i=2004)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof PropertyTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:NamespaceArray (declaration i=2006, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (PropertyTypeNode) parent;
  }

  @Override
  public @Nullable String @Nullable [] getNamespaceArray() {
    var node = getNamespaceArrayNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NamespaceArray (declaration i=2006, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (String[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setNamespaceArray(@Nullable String @Nullable [] value) {
    var node = getNamespaceArrayNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NamespaceArray (declaration i=2006, owner i=2004)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getUrisVersionNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=46").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:UrisVersion (declaration i=15003, owner i=2004)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "UrisVersion");
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
                    + " while resolving http://opcfoundation.org/UA/:UrisVersion (declaration"
                    + " i=15003, owner i=2004) on "
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
              "http://opcfoundation.org/UA/:UrisVersion (declaration i=15003, owner i=2004)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:UrisVersion (declaration i=15003, owner i=2004)"
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
              "http://opcfoundation.org/UA/:UrisVersion (declaration i=15003, owner i=2004)"
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
            "http://opcfoundation.org/UA/:UrisVersion (declaration i=15003, owner i=2004)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:UrisVersion (declaration i=15003, owner i=2004)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof PropertyTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:UrisVersion (declaration i=15003, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (PropertyTypeNode) parent;
  }

  @Override
  public @Nullable UInteger getUrisVersion() {
    var node = getUrisVersionNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UrisVersion (declaration i=15003, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setUrisVersion(@Nullable UInteger value) {
    var node = getUrisVersionNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UrisVersion (declaration i=15003, owner i=2004)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getServiceLevelNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=46").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:ServiceLevel (declaration i=2008, owner i=2004)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "ServiceLevel");
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
                    + " while resolving http://opcfoundation.org/UA/:ServiceLevel (declaration"
                    + " i=2008, owner i=2004) on "
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
              "http://opcfoundation.org/UA/:ServiceLevel (declaration i=2008, owner i=2004)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:ServiceLevel (declaration i=2008, owner i=2004)"
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
              "http://opcfoundation.org/UA/:ServiceLevel (declaration i=2008, owner i=2004)"
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
            "http://opcfoundation.org/UA/:ServiceLevel (declaration i=2008, owner i=2004)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:ServiceLevel (declaration i=2008, owner i=2004)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:ServiceLevel (declaration i=2008, owner i=2004)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof PropertyTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:ServiceLevel (declaration i=2008, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (PropertyTypeNode) parent;
  }

  @Override
  public @Nullable UByte getServiceLevel() {
    var node = getServiceLevelNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServiceLevel (declaration i=2008, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (UByte) node.getValue().getValue().getValue();
  }

  @Override
  public void setServiceLevel(@Nullable UByte value) {
    var node = getServiceLevelNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServiceLevel (declaration i=2008, owner i=2004)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getAuditingNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=46").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:Auditing (declaration i=2742, owner i=2004)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "Auditing");
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
                    + " while resolving http://opcfoundation.org/UA/:Auditing (declaration i=2742,"
                    + " owner i=2004) on "
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
              "http://opcfoundation.org/UA/:Auditing (declaration i=2742, owner i=2004)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:Auditing (declaration i=2742, owner i=2004)"
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
              "http://opcfoundation.org/UA/:Auditing (declaration i=2742, owner i=2004)"
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
            "http://opcfoundation.org/UA/:Auditing (declaration i=2742, owner i=2004)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:Auditing (declaration i=2742, owner i=2004)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:Auditing (declaration i=2742, owner i=2004)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof PropertyTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:Auditing (declaration i=2742, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (PropertyTypeNode) parent;
  }

  @Override
  public @Nullable Boolean getAuditing() {
    var node = getAuditingNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Auditing (declaration i=2742, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setAuditing(@Nullable Boolean value) {
    var node = getAuditingNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Auditing (declaration i=2742, owner i=2004)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getEstimatedReturnTimeNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=46").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:EstimatedReturnTime (declaration i=12882, owner i=2004)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "EstimatedReturnTime");
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
                    + " while resolving http://opcfoundation.org/UA/:EstimatedReturnTime"
                    + " (declaration i=12882, owner i=2004) on "
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
              "http://opcfoundation.org/UA/:EstimatedReturnTime (declaration i=12882, owner i=2004)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:EstimatedReturnTime (declaration i=12882, owner i=2004)"
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
              "http://opcfoundation.org/UA/:EstimatedReturnTime (declaration i=12882, owner i=2004)"
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
            "http://opcfoundation.org/UA/:EstimatedReturnTime (declaration i=12882, owner i=2004)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:EstimatedReturnTime (declaration i=12882, owner i=2004)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof PropertyTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:EstimatedReturnTime (declaration i=12882, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (PropertyTypeNode) parent;
  }

  @Override
  public @Nullable DateTime getEstimatedReturnTime() {
    var node = getEstimatedReturnTimeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EstimatedReturnTime (declaration i=12882, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setEstimatedReturnTime(@Nullable DateTime value) {
    var node = getEstimatedReturnTimeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EstimatedReturnTime (declaration i=12882, owner i=2004)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getLocalTimeNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=46").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:LocalTime (declaration i=17612, owner i=2004)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "LocalTime");
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
                    + " while resolving http://opcfoundation.org/UA/:LocalTime (declaration"
                    + " i=17612, owner i=2004) on "
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
              "http://opcfoundation.org/UA/:LocalTime (declaration i=17612, owner i=2004)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:LocalTime (declaration i=17612, owner i=2004)"
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
              "http://opcfoundation.org/UA/:LocalTime (declaration i=17612, owner i=2004)"
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
            "http://opcfoundation.org/UA/:LocalTime (declaration i=17612, owner i=2004)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:LocalTime (declaration i=17612, owner i=2004)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof PropertyTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:LocalTime (declaration i=17612, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (PropertyTypeNode) parent;
  }

  @Override
  public @Nullable TimeZoneDataType getLocalTime() {
    var node = getLocalTimeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LocalTime (declaration i=17612, owner i=2004)"
              + " on "
              + getNodeId());
    }
    Object decoded =
        ExtensionObject.decodeValue(
            getNodeContext().getServer().getStaticEncodingContext(),
            node.getValue().getValue().getValue());
    if (decoded == null) {
      return null;
    }
    if (!(decoded instanceof TimeZoneDataType)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:LocalTime (declaration i=17612, owner i=2004)");
    }
    return (TimeZoneDataType) decoded;
  }

  @Override
  public void setLocalTime(@Nullable TimeZoneDataType value) {
    var node = getLocalTimeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LocalTime (declaration i=17612, owner i=2004)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public ServerStatusTypeNode getServerStatusNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:ServerStatus (declaration i=2007, owner i=2004)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "ServerStatus");
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
                    + " while resolving http://opcfoundation.org/UA/:ServerStatus (declaration"
                    + " i=2007, owner i=2004) on "
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
              "http://opcfoundation.org/UA/:ServerStatus (declaration i=2007, owner i=2004)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:ServerStatus (declaration i=2007, owner i=2004)"
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
              "http://opcfoundation.org/UA/:ServerStatus (declaration i=2007, owner i=2004)"
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
            "http://opcfoundation.org/UA/:ServerStatus (declaration i=2007, owner i=2004)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:ServerStatus (declaration i=2007, owner i=2004)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:ServerStatus (declaration i=2007, owner i=2004)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof ServerStatusTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:ServerStatus (declaration i=2007, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (ServerStatusTypeNode) parent;
  }

  @Override
  public @Nullable ServerStatusDataType getServerStatus() {
    var node = getServerStatusNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServerStatus (declaration i=2007, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (ServerStatusDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setServerStatus(@Nullable ServerStatusDataType value) {
    var node = getServerStatusNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServerStatus (declaration i=2007, owner i=2004)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public ServerCapabilitiesTypeNode getServerCapabilitiesNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:ServerCapabilities (declaration i=2009, owner i=2004)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "ServerCapabilities");
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
                    + " while resolving http://opcfoundation.org/UA/:ServerCapabilities"
                    + " (declaration i=2009, owner i=2004) on "
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
              "http://opcfoundation.org/UA/:ServerCapabilities (declaration i=2009, owner i=2004)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:ServerCapabilities (declaration i=2009, owner i=2004)"
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
              "http://opcfoundation.org/UA/:ServerCapabilities (declaration i=2009, owner i=2004)"
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
            "http://opcfoundation.org/UA/:ServerCapabilities (declaration i=2009, owner i=2004)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:ServerCapabilities (declaration i=2009, owner i=2004)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Object) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:ServerCapabilities (declaration i=2009, owner i=2004)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof ServerCapabilitiesTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:ServerCapabilities (declaration i=2009, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (ServerCapabilitiesTypeNode) parent;
  }

  @Override
  public ServerDiagnosticsTypeNode getServerDiagnosticsNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:ServerDiagnostics (declaration i=2010, owner i=2004)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "ServerDiagnostics");
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
                    + " while resolving http://opcfoundation.org/UA/:ServerDiagnostics (declaration"
                    + " i=2010, owner i=2004) on "
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
              "http://opcfoundation.org/UA/:ServerDiagnostics (declaration i=2010, owner i=2004)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:ServerDiagnostics (declaration i=2010, owner i=2004)"
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
              "http://opcfoundation.org/UA/:ServerDiagnostics (declaration i=2010, owner i=2004)"
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
            "http://opcfoundation.org/UA/:ServerDiagnostics (declaration i=2010, owner i=2004)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:ServerDiagnostics (declaration i=2010, owner i=2004)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Object) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:ServerDiagnostics (declaration i=2010, owner i=2004)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof ServerDiagnosticsTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:ServerDiagnostics (declaration i=2010, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (ServerDiagnosticsTypeNode) parent;
  }

  @Override
  public VendorServerInfoTypeNode getVendorServerInfoNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:VendorServerInfo (declaration i=2011, owner i=2004)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "VendorServerInfo");
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
                    + " while resolving http://opcfoundation.org/UA/:VendorServerInfo (declaration"
                    + " i=2011, owner i=2004) on "
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
              "http://opcfoundation.org/UA/:VendorServerInfo (declaration i=2011, owner i=2004)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:VendorServerInfo (declaration i=2011, owner i=2004)"
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
              "http://opcfoundation.org/UA/:VendorServerInfo (declaration i=2011, owner i=2004)"
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
            "http://opcfoundation.org/UA/:VendorServerInfo (declaration i=2011, owner i=2004)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:VendorServerInfo (declaration i=2011, owner i=2004)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Object) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:VendorServerInfo (declaration i=2011, owner i=2004)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof VendorServerInfoTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:VendorServerInfo (declaration i=2011, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (VendorServerInfoTypeNode) parent;
  }

  @Override
  public ServerRedundancyTypeNode getServerRedundancyNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:ServerRedundancy (declaration i=2012, owner i=2004)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "ServerRedundancy");
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
                    + " while resolving http://opcfoundation.org/UA/:ServerRedundancy (declaration"
                    + " i=2012, owner i=2004) on "
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
              "http://opcfoundation.org/UA/:ServerRedundancy (declaration i=2012, owner i=2004)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:ServerRedundancy (declaration i=2012, owner i=2004)"
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
              "http://opcfoundation.org/UA/:ServerRedundancy (declaration i=2012, owner i=2004)"
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
            "http://opcfoundation.org/UA/:ServerRedundancy (declaration i=2012, owner i=2004)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:ServerRedundancy (declaration i=2012, owner i=2004)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Object) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:ServerRedundancy (declaration i=2012, owner i=2004)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof ServerRedundancyTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:ServerRedundancy (declaration i=2012, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (ServerRedundancyTypeNode) parent;
  }

  @Override
  public @Nullable NamespacesTypeNode getNamespacesNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:Namespaces (declaration i=11527, owner i=2004)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "Namespaces");
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
                    + " while resolving http://opcfoundation.org/UA/:Namespaces (declaration"
                    + " i=11527, owner i=2004) on "
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
              "http://opcfoundation.org/UA/:Namespaces (declaration i=11527, owner i=2004)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:Namespaces (declaration i=11527, owner i=2004)"
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
              "http://opcfoundation.org/UA/:Namespaces (declaration i=11527, owner i=2004)"
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
            "http://opcfoundation.org/UA/:Namespaces (declaration i=11527, owner i=2004)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Object) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:Namespaces (declaration i=11527, owner i=2004)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof NamespacesTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:Namespaces (declaration i=11527, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (NamespacesTypeNode) parent;
  }

  @Override
  public @Nullable UaMethodNode getGetMonitoredItemsMethodNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:GetMonitoredItems (declaration i=11489, owner i=2004)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "GetMonitoredItems");
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
                    + " while resolving http://opcfoundation.org/UA/:GetMonitoredItems (declaration"
                    + " i=11489, owner i=2004) on "
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
              "http://opcfoundation.org/UA/:GetMonitoredItems (declaration i=11489, owner i=2004)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:GetMonitoredItems (declaration i=11489, owner i=2004)"
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
              "http://opcfoundation.org/UA/:GetMonitoredItems (declaration i=11489, owner i=2004)"
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
            "http://opcfoundation.org/UA/:GetMonitoredItems (declaration i=11489, owner i=2004)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Method) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:GetMonitoredItems (declaration i=11489, owner i=2004)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof UaMethodNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:GetMonitoredItems (declaration i=11489, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (UaMethodNode) parent;
  }

  @Override
  public MethodBinding bindGetMonitoredItems(
      MethodBindings bindings, ServerTypeGetMonitoredItemsHandler handler) throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getGetMonitoredItemsMethodNode();
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
                  "SubscriptionId",
                  ExpandedNodeId.parse("i=7")
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
                  "ServerHandles",
                  ExpandedNodeId.parse("i=7")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText(null, "")),
              new Argument(
                  "ClientHandles",
                  ExpandedNodeId.parse("i=7")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText(null, ""))
            };
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
            @Nullable UInteger callbackInput0 = null;
            if (inputValues.length > 0) {
              try {
                @Nullable UInteger convertedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput0 = (UInteger) methodValue;
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
                @Nullable UInteger projectedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput0 = (UInteger) methodValue;
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
              ServerTypeGetMonitoredItemsOutputs outputs = handler.invoke(context, callbackInput0);
              if (outputs == null) {
                throw new UaException(
                    StatusCodes.Bad_InternalError,
                    "A multiple-output Method handler returned a null output container");
              }
              Variant outputValue0;
              {
                @Nullable UInteger @Nullable [] convertedValue;
                {
                  Object methodValue = outputs.serverHandles();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    NamespaceTable namespaceTable = context.getServer().getNamespaceTable();
                    DataTypeTree dataTypeTree = context.getServer().getDataTypeTree();
                    NodeId argumentDataTypeId =
                        ExpandedNodeId.parse("i=7")
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
                          "Method argument ServerHandles (effective property i=11491, DataType i=7)"
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
                      convertedValue = new UInteger[Array.getLength(methodValue)];
                      for (int valueIndex = 0; valueIndex < convertedValue.length; valueIndex++) {
                        Object valueElement = Array.get(methodValue, valueIndex);
                        convertedValue[valueIndex] = (UInteger) valueElement;
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
              Variant outputValue1;
              {
                @Nullable UInteger @Nullable [] convertedValue;
                {
                  Object methodValue = outputs.clientHandles();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    NamespaceTable namespaceTable = context.getServer().getNamespaceTable();
                    DataTypeTree dataTypeTree = context.getServer().getDataTypeTree();
                    NodeId argumentDataTypeId =
                        ExpandedNodeId.parse("i=7")
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
                          "Method argument ClientHandles (effective property i=11491, DataType i=7)"
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
                      convertedValue = new UInteger[Array.getLength(methodValue)];
                      for (int valueIndex = 0; valueIndex < convertedValue.length; valueIndex++) {
                        Object valueElement = Array.get(methodValue, valueIndex);
                        convertedValue[valueIndex] = (UInteger) valueElement;
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
  public MethodBinding bindGetMonitoredItemsDetailed(
      MethodBindings bindings, ServerTypeGetMonitoredItemsDetailedHandler handler)
      throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getGetMonitoredItemsMethodNode();
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
                  "SubscriptionId",
                  ExpandedNodeId.parse("i=7")
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
                  "ServerHandles",
                  ExpandedNodeId.parse("i=7")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText(null, "")),
              new Argument(
                  "ClientHandles",
                  ExpandedNodeId.parse("i=7")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText(null, ""))
            };
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
            @Nullable UInteger callbackInput0 = null;
            if (inputValues.length > 0) {
              try {
                @Nullable UInteger convertedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput0 = (UInteger) methodValue;
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
                @Nullable UInteger projectedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput0 = (UInteger) methodValue;
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
              var outputs = result.outputs();
              if (outputs == null) {
                throw new UaException(
                    StatusCodes.Bad_InternalError,
                    "A multiple-output Method handler returned a null output container");
              }
              Variant outputValue0;
              {
                @Nullable UInteger @Nullable [] convertedValue;
                {
                  Object methodValue = outputs.serverHandles();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    NamespaceTable namespaceTable = context.getServer().getNamespaceTable();
                    DataTypeTree dataTypeTree = context.getServer().getDataTypeTree();
                    NodeId argumentDataTypeId =
                        ExpandedNodeId.parse("i=7")
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
                          "Method argument ServerHandles (effective property i=11491, DataType i=7)"
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
                      convertedValue = new UInteger[Array.getLength(methodValue)];
                      for (int valueIndex = 0; valueIndex < convertedValue.length; valueIndex++) {
                        Object valueElement = Array.get(methodValue, valueIndex);
                        convertedValue[valueIndex] = (UInteger) valueElement;
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
              Variant outputValue1;
              {
                @Nullable UInteger @Nullable [] convertedValue;
                {
                  Object methodValue = outputs.clientHandles();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    NamespaceTable namespaceTable = context.getServer().getNamespaceTable();
                    DataTypeTree dataTypeTree = context.getServer().getDataTypeTree();
                    NodeId argumentDataTypeId =
                        ExpandedNodeId.parse("i=7")
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
                          "Method argument ClientHandles (effective property i=11491, DataType i=7)"
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
                      convertedValue = new UInteger[Array.getLength(methodValue)];
                      for (int valueIndex = 0; valueIndex < convertedValue.length; valueIndex++) {
                        Object valueElement = Array.get(methodValue, valueIndex);
                        convertedValue[valueIndex] = (UInteger) valueElement;
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
  public @Nullable UaMethodNode getResendDataMethodNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:ResendData (declaration i=12871, owner i=2004)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "ResendData");
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
                    + " while resolving http://opcfoundation.org/UA/:ResendData (declaration"
                    + " i=12871, owner i=2004) on "
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
              "http://opcfoundation.org/UA/:ResendData (declaration i=12871, owner i=2004)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:ResendData (declaration i=12871, owner i=2004)"
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
              "http://opcfoundation.org/UA/:ResendData (declaration i=12871, owner i=2004)"
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
            "http://opcfoundation.org/UA/:ResendData (declaration i=12871, owner i=2004)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Method) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:ResendData (declaration i=12871, owner i=2004)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof UaMethodNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:ResendData (declaration i=12871, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (UaMethodNode) parent;
  }

  @Override
  public MethodBinding bindResendData(MethodBindings bindings, ServerTypeResendDataHandler handler)
      throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getResendDataMethodNode();
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
                  "SubscriptionId",
                  ExpandedNodeId.parse("i=7")
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
            @Nullable UInteger callbackInput0 = null;
            if (inputValues.length > 0) {
              try {
                @Nullable UInteger convertedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput0 = (UInteger) methodValue;
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
                @Nullable UInteger projectedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput0 = (UInteger) methodValue;
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
  public MethodBinding bindResendDataDetailed(
      MethodBindings bindings, ServerTypeResendDataDetailedHandler handler) throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getResendDataMethodNode();
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
                  "SubscriptionId",
                  ExpandedNodeId.parse("i=7")
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
            @Nullable UInteger callbackInput0 = null;
            if (inputValues.length > 0) {
              try {
                @Nullable UInteger convertedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput0 = (UInteger) methodValue;
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
                @Nullable UInteger projectedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput0 = (UInteger) methodValue;
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
  public @Nullable UaMethodNode getSetSubscriptionDurableMethodNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:SetSubscriptionDurable (declaration i=12746, owner"
                + " i=2004) on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "SetSubscriptionDurable");
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
                    + " while resolving http://opcfoundation.org/UA/:SetSubscriptionDurable"
                    + " (declaration i=12746, owner i=2004) on "
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
              "http://opcfoundation.org/UA/:SetSubscriptionDurable (declaration i=12746, owner"
                  + " i=2004) on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:SetSubscriptionDurable (declaration i=12746, owner"
                  + " i=2004) on "
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
              "http://opcfoundation.org/UA/:SetSubscriptionDurable (declaration i=12746, owner"
                  + " i=2004) on "
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
            "http://opcfoundation.org/UA/:SetSubscriptionDurable (declaration i=12746, owner"
                + " i=2004) on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Method) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:SetSubscriptionDurable (declaration i=12746, owner"
                + " i=2004) on "
                + getNodeId());
      }
    }
    if (!(parent instanceof UaMethodNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:SetSubscriptionDurable (declaration i=12746, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (UaMethodNode) parent;
  }

  @Override
  public MethodBinding bindSetSubscriptionDurable(
      MethodBindings bindings, ServerTypeSetSubscriptionDurableHandler handler) throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getSetSubscriptionDurableMethodNode();
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
                  "SubscriptionId",
                  ExpandedNodeId.parse("i=7")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, "")),
              new Argument(
                  "LifetimeInHours",
                  ExpandedNodeId.parse("i=7")
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
                  "RevisedLifetimeInHours",
                  ExpandedNodeId.parse("i=7")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, ""))
            };
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 2;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            @Nullable UInteger callbackInput0 = null;
            if (inputValues.length > 0) {
              try {
                @Nullable UInteger convertedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput0 = (UInteger) methodValue;
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
                @Nullable UInteger projectedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput0 = (UInteger) methodValue;
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
            @Nullable UInteger callbackInput1 = null;
            if (inputValues.length > 1) {
              try {
                @Nullable UInteger convertedInput1;
                {
                  Object methodValue = inputValues[1].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput1 = (UInteger) methodValue;
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
                @Nullable UInteger projectedInput1;
                {
                  Object methodValue = inputValues[1].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput1 = (UInteger) methodValue;
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
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              @Nullable UInteger outputs = handler.invoke(context, callbackInput0, callbackInput1);
              Variant outputValue0;
              {
                @Nullable UInteger convertedValue;
                {
                  Object methodValue = outputs;
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    NamespaceTable namespaceTable = context.getServer().getNamespaceTable();
                    DataTypeTree dataTypeTree = context.getServer().getDataTypeTree();
                    NodeId argumentDataTypeId =
                        ExpandedNodeId.parse("i=7")
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
                          "Method argument RevisedLifetimeInHours (effective property i=12748,"
                              + " DataType i=7) is unavailable in the effective type tree; resolved"
                              + " DataType: "
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
                    convertedValue = (UInteger) methodValue;
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
  public MethodBinding bindSetSubscriptionDurableDetailed(
      MethodBindings bindings, ServerTypeSetSubscriptionDurableDetailedHandler handler)
      throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getSetSubscriptionDurableMethodNode();
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
                  "SubscriptionId",
                  ExpandedNodeId.parse("i=7")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, "")),
              new Argument(
                  "LifetimeInHours",
                  ExpandedNodeId.parse("i=7")
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
                  "RevisedLifetimeInHours",
                  ExpandedNodeId.parse("i=7")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, ""))
            };
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 2;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            @Nullable UInteger callbackInput0 = null;
            if (inputValues.length > 0) {
              try {
                @Nullable UInteger convertedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput0 = (UInteger) methodValue;
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
                @Nullable UInteger projectedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput0 = (UInteger) methodValue;
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
            @Nullable UInteger callbackInput1 = null;
            if (inputValues.length > 1) {
              try {
                @Nullable UInteger convertedInput1;
                {
                  Object methodValue = inputValues[1].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput1 = (UInteger) methodValue;
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
                @Nullable UInteger projectedInput1;
                {
                  Object methodValue = inputValues[1].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput1 = (UInteger) methodValue;
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
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              var result = handler.invoke(context, callbackInput0, callbackInput1);
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
                @Nullable UInteger convertedValue;
                {
                  Object methodValue = outputs;
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    NamespaceTable namespaceTable = context.getServer().getNamespaceTable();
                    DataTypeTree dataTypeTree = context.getServer().getDataTypeTree();
                    NodeId argumentDataTypeId =
                        ExpandedNodeId.parse("i=7")
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
                          "Method argument RevisedLifetimeInHours (effective property i=12748,"
                              + " DataType i=7) is unavailable in the effective type tree; resolved"
                              + " DataType: "
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
                    convertedValue = (UInteger) methodValue;
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

  @Override
  public @Nullable UaMethodNode getRequestServerStateChangeMethodNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:RequestServerStateChange (declaration i=12883, owner"
                + " i=2004) on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "RequestServerStateChange");
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
                    + " while resolving http://opcfoundation.org/UA/:RequestServerStateChange"
                    + " (declaration i=12883, owner i=2004) on "
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
              "http://opcfoundation.org/UA/:RequestServerStateChange (declaration i=12883, owner"
                  + " i=2004) on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:RequestServerStateChange (declaration i=12883, owner"
                  + " i=2004) on "
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
              "http://opcfoundation.org/UA/:RequestServerStateChange (declaration i=12883, owner"
                  + " i=2004) on "
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
            "http://opcfoundation.org/UA/:RequestServerStateChange (declaration i=12883, owner"
                + " i=2004) on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Method) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:RequestServerStateChange (declaration i=12883, owner"
                + " i=2004) on "
                + getNodeId());
      }
    }
    if (!(parent instanceof UaMethodNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:RequestServerStateChange (declaration i=12883, owner"
              + " i=2004) on "
              + getNodeId());
    }
    return (UaMethodNode) parent;
  }

  @Override
  public MethodBinding bindRequestServerStateChange(
      MethodBindings bindings, ServerTypeRequestServerStateChangeHandler handler)
      throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getRequestServerStateChangeMethodNode();
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
                  "State",
                  ExpandedNodeId.parse("i=852")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, "")),
              new Argument(
                  "EstimatedReturnTime",
                  ExpandedNodeId.parse("i=13")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, "")),
              new Argument(
                  "SecondsTillShutdown",
                  ExpandedNodeId.parse("i=7")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, "")),
              new Argument(
                  "Reason",
                  ExpandedNodeId.parse("i=21")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, "")),
              new Argument(
                  "Restart",
                  ExpandedNodeId.parse("i=1")
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
            return 5;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            @Nullable ServerState callbackInput0 = null;
            if (inputValues.length > 0) {
              try {
                @Nullable ServerState convertedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    {
                      if (methodValue == null
                          || methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                        convertedInput0 = null;
                      } else {
                        Object elements =
                            methodValue instanceof Matrix
                                ? ((Matrix) methodValue).getElements()
                                : methodValue;
                        int rank =
                            methodValue instanceof Matrix
                                ? ((Matrix) methodValue).getValueRank()
                                : ArrayUtil.getValueRank(methodValue);
                        boolean permitted = rank == -1;
                        if (!permitted) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "Method argument State: ValueRank=-1 does not permit rank " + rank);
                        }
                        if (methodValue != null && !((Object) methodValue instanceof ServerState)) {
                          if (!(methodValue instanceof Integer)) {
                            throw new UaException(
                                StatusCodes.Bad_TypeMismatch,
                                "Method argument State: expected"
                                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState"
                                    + " or Int32, got "
                                    + methodValue);
                          }
                          if (ServerState.from((Integer) methodValue) == null) {
                            throw new UaException(
                                StatusCodes.Bad_OutOfRange,
                                "Method argument State: unknown"
                                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState"
                                    + " value "
                                    + methodValue);
                          }
                        }
                        convertedInput0 =
                            methodValue == null || methodValue instanceof ServerState
                                ? (ServerState) methodValue
                                : ServerState.from((Integer) methodValue);
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
                @Nullable ServerState projectedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    {
                      if (methodValue == null
                          || methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                        projectedInput0 = null;
                      } else {
                        Object elements =
                            methodValue instanceof Matrix
                                ? ((Matrix) methodValue).getElements()
                                : methodValue;
                        int rank =
                            methodValue instanceof Matrix
                                ? ((Matrix) methodValue).getValueRank()
                                : ArrayUtil.getValueRank(methodValue);
                        boolean permitted = rank == -1;
                        if (!permitted) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "Method argument State: ValueRank=-1 does not permit rank " + rank);
                        }
                        if (methodValue != null && !((Object) methodValue instanceof ServerState)) {
                          if (!(methodValue instanceof Integer)) {
                            throw new UaException(
                                StatusCodes.Bad_TypeMismatch,
                                "Method argument State: expected"
                                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState"
                                    + " or Int32, got "
                                    + methodValue);
                          }
                          if (ServerState.from((Integer) methodValue) == null) {
                            throw new UaException(
                                StatusCodes.Bad_OutOfRange,
                                "Method argument State: unknown"
                                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState"
                                    + " value "
                                    + methodValue);
                          }
                        }
                        projectedInput0 =
                            methodValue == null || methodValue instanceof ServerState
                                ? (ServerState) methodValue
                                : ServerState.from((Integer) methodValue);
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
            @Nullable LocalizedText callbackInput3 = null;
            if (inputValues.length > 3) {
              try {
                @Nullable LocalizedText convertedInput3;
                {
                  Object methodValue = inputValues[3].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput3 = (LocalizedText) methodValue;
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
                @Nullable LocalizedText projectedInput3;
                {
                  Object methodValue = inputValues[3].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput3 = (LocalizedText) methodValue;
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
            @Nullable Boolean callbackInput4 = null;
            if (inputValues.length > 4) {
              try {
                @Nullable Boolean convertedInput4;
                {
                  Object methodValue = inputValues[4].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput4 = (Boolean) methodValue;
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
                @Nullable Boolean projectedInput4;
                {
                  Object methodValue = inputValues[4].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput4 = (Boolean) methodValue;
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
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              handler.invoke(
                  context,
                  callbackInput0,
                  callbackInput1,
                  callbackInput2,
                  callbackInput3,
                  callbackInput4);
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
  public MethodBinding bindRequestServerStateChangeDetailed(
      MethodBindings bindings, ServerTypeRequestServerStateChangeDetailedHandler handler)
      throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getRequestServerStateChangeMethodNode();
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
                  "State",
                  ExpandedNodeId.parse("i=852")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, "")),
              new Argument(
                  "EstimatedReturnTime",
                  ExpandedNodeId.parse("i=13")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, "")),
              new Argument(
                  "SecondsTillShutdown",
                  ExpandedNodeId.parse("i=7")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, "")),
              new Argument(
                  "Reason",
                  ExpandedNodeId.parse("i=21")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, "")),
              new Argument(
                  "Restart",
                  ExpandedNodeId.parse("i=1")
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
            return 5;
          }

          @Override
          protected CallMethodResult invokeResult(
              AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            @Nullable ServerState callbackInput0 = null;
            if (inputValues.length > 0) {
              try {
                @Nullable ServerState convertedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    {
                      if (methodValue == null
                          || methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                        convertedInput0 = null;
                      } else {
                        Object elements =
                            methodValue instanceof Matrix
                                ? ((Matrix) methodValue).getElements()
                                : methodValue;
                        int rank =
                            methodValue instanceof Matrix
                                ? ((Matrix) methodValue).getValueRank()
                                : ArrayUtil.getValueRank(methodValue);
                        boolean permitted = rank == -1;
                        if (!permitted) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "Method argument State: ValueRank=-1 does not permit rank " + rank);
                        }
                        if (methodValue != null && !((Object) methodValue instanceof ServerState)) {
                          if (!(methodValue instanceof Integer)) {
                            throw new UaException(
                                StatusCodes.Bad_TypeMismatch,
                                "Method argument State: expected"
                                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState"
                                    + " or Int32, got "
                                    + methodValue);
                          }
                          if (ServerState.from((Integer) methodValue) == null) {
                            throw new UaException(
                                StatusCodes.Bad_OutOfRange,
                                "Method argument State: unknown"
                                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState"
                                    + " value "
                                    + methodValue);
                          }
                        }
                        convertedInput0 =
                            methodValue == null || methodValue instanceof ServerState
                                ? (ServerState) methodValue
                                : ServerState.from((Integer) methodValue);
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
                @Nullable ServerState projectedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    {
                      if (methodValue == null
                          || methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                        projectedInput0 = null;
                      } else {
                        Object elements =
                            methodValue instanceof Matrix
                                ? ((Matrix) methodValue).getElements()
                                : methodValue;
                        int rank =
                            methodValue instanceof Matrix
                                ? ((Matrix) methodValue).getValueRank()
                                : ArrayUtil.getValueRank(methodValue);
                        boolean permitted = rank == -1;
                        if (!permitted) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "Method argument State: ValueRank=-1 does not permit rank " + rank);
                        }
                        if (methodValue != null && !((Object) methodValue instanceof ServerState)) {
                          if (!(methodValue instanceof Integer)) {
                            throw new UaException(
                                StatusCodes.Bad_TypeMismatch,
                                "Method argument State: expected"
                                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState"
                                    + " or Int32, got "
                                    + methodValue);
                          }
                          if (ServerState.from((Integer) methodValue) == null) {
                            throw new UaException(
                                StatusCodes.Bad_OutOfRange,
                                "Method argument State: unknown"
                                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState"
                                    + " value "
                                    + methodValue);
                          }
                        }
                        projectedInput0 =
                            methodValue == null || methodValue instanceof ServerState
                                ? (ServerState) methodValue
                                : ServerState.from((Integer) methodValue);
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
            @Nullable LocalizedText callbackInput3 = null;
            if (inputValues.length > 3) {
              try {
                @Nullable LocalizedText convertedInput3;
                {
                  Object methodValue = inputValues[3].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput3 = (LocalizedText) methodValue;
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
                @Nullable LocalizedText projectedInput3;
                {
                  Object methodValue = inputValues[3].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput3 = (LocalizedText) methodValue;
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
            @Nullable Boolean callbackInput4 = null;
            if (inputValues.length > 4) {
              try {
                @Nullable Boolean convertedInput4;
                {
                  Object methodValue = inputValues[4].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput4 = (Boolean) methodValue;
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
                @Nullable Boolean projectedInput4;
                {
                  Object methodValue = inputValues[4].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput4 = (Boolean) methodValue;
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
                      callbackInput4);
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
