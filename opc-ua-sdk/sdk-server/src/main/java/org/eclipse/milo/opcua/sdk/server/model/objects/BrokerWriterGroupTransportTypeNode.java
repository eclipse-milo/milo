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

import java.util.LinkedHashMap;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

public class BrokerWriterGroupTransportTypeNode extends WriterGroupTransportTypeNode
    implements BrokerWriterGroupTransportType {
  public BrokerWriterGroupTransportTypeNode(
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

  public BrokerWriterGroupTransportTypeNode(
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
  public PropertyTypeNode getQueueNameNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=46").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:QueueName (declaration i=21137, owner i=21136)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "QueueName");
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
                    + " while resolving http://opcfoundation.org/UA/:QueueName (declaration"
                    + " i=21137, owner i=21136) on "
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
              "http://opcfoundation.org/UA/:QueueName (declaration i=21137, owner i=21136)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:QueueName (declaration i=21137, owner i=21136)"
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
              "http://opcfoundation.org/UA/:QueueName (declaration i=21137, owner i=21136)"
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
            "http://opcfoundation.org/UA/:QueueName (declaration i=21137, owner i=21136)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:QueueName (declaration i=21137, owner i=21136)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:QueueName (declaration i=21137, owner i=21136)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof PropertyTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:QueueName (declaration i=21137, owner i=21136)"
              + " on "
              + getNodeId());
    }
    return (PropertyTypeNode) parent;
  }

  @Override
  public @Nullable String getQueueName() {
    var node = getQueueNameNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:QueueName (declaration i=21137, owner i=21136)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setQueueName(@Nullable String value) {
    var node = getQueueNameNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:QueueName (declaration i=21137, owner i=21136)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getResourceUriNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=46").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:ResourceUri (declaration i=15246, owner i=21136)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "ResourceUri");
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
                    + " while resolving http://opcfoundation.org/UA/:ResourceUri (declaration"
                    + " i=15246, owner i=21136) on "
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
              "http://opcfoundation.org/UA/:ResourceUri (declaration i=15246, owner i=21136)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:ResourceUri (declaration i=15246, owner i=21136)"
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
              "http://opcfoundation.org/UA/:ResourceUri (declaration i=15246, owner i=21136)"
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
            "http://opcfoundation.org/UA/:ResourceUri (declaration i=15246, owner i=21136)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:ResourceUri (declaration i=15246, owner i=21136)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:ResourceUri (declaration i=15246, owner i=21136)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof PropertyTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:ResourceUri (declaration i=15246, owner i=21136)"
              + " on "
              + getNodeId());
    }
    return (PropertyTypeNode) parent;
  }

  @Override
  public @Nullable String getResourceUri() {
    var node = getResourceUriNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ResourceUri (declaration i=15246, owner i=21136)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setResourceUri(@Nullable String value) {
    var node = getResourceUriNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ResourceUri (declaration i=15246, owner i=21136)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getAuthenticationProfileUriNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=46").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:AuthenticationProfileUri (declaration i=15247, owner"
                + " i=21136) on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "AuthenticationProfileUri");
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
                    + " while resolving http://opcfoundation.org/UA/:AuthenticationProfileUri"
                    + " (declaration i=15247, owner i=21136) on "
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
              "http://opcfoundation.org/UA/:AuthenticationProfileUri (declaration i=15247, owner"
                  + " i=21136) on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:AuthenticationProfileUri (declaration i=15247, owner"
                  + " i=21136) on "
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
              "http://opcfoundation.org/UA/:AuthenticationProfileUri (declaration i=15247, owner"
                  + " i=21136) on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NotFound,
            "http://opcfoundation.org/UA/:AuthenticationProfileUri (declaration i=15247, owner"
                + " i=21136) on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:AuthenticationProfileUri (declaration i=15247, owner"
                + " i=21136) on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:AuthenticationProfileUri (declaration i=15247, owner"
                + " i=21136) on "
                + getNodeId());
      }
    }
    if (!(parent instanceof PropertyTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:AuthenticationProfileUri (declaration i=15247, owner"
              + " i=21136) on "
              + getNodeId());
    }
    return (PropertyTypeNode) parent;
  }

  @Override
  public @Nullable String getAuthenticationProfileUri() {
    var node = getAuthenticationProfileUriNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AuthenticationProfileUri (declaration i=15247, owner"
              + " i=21136) on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setAuthenticationProfileUri(@Nullable String value) {
    var node = getAuthenticationProfileUriNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AuthenticationProfileUri (declaration i=15247, owner"
              + " i=21136) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getRequestedDeliveryGuaranteeNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=46").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:RequestedDeliveryGuarantee (declaration i=15249, owner"
                + " i=21136) on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "RequestedDeliveryGuarantee");
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
                    + " while resolving http://opcfoundation.org/UA/:RequestedDeliveryGuarantee"
                    + " (declaration i=15249, owner i=21136) on "
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
              "http://opcfoundation.org/UA/:RequestedDeliveryGuarantee (declaration i=15249, owner"
                  + " i=21136) on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:RequestedDeliveryGuarantee (declaration i=15249, owner"
                  + " i=21136) on "
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
              "http://opcfoundation.org/UA/:RequestedDeliveryGuarantee (declaration i=15249, owner"
                  + " i=21136) on "
                  + getNodeId());
        }
        if (browseName.equals(target.getBrowseName())) {
          matches.put(target.getNodeId(), target);
        }
      }
      if (matches.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NotFound,
            "http://opcfoundation.org/UA/:RequestedDeliveryGuarantee (declaration i=15249, owner"
                + " i=21136) on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:RequestedDeliveryGuarantee (declaration i=15249, owner"
                + " i=21136) on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:RequestedDeliveryGuarantee (declaration i=15249, owner"
                + " i=21136) on "
                + getNodeId());
      }
    }
    if (!(parent instanceof PropertyTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:RequestedDeliveryGuarantee (declaration i=15249, owner"
              + " i=21136) on "
              + getNodeId());
    }
    return (PropertyTypeNode) parent;
  }

  @Override
  public @Nullable BrokerTransportQualityOfService getRequestedDeliveryGuarantee() {
    var node = getRequestedDeliveryGuaranteeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RequestedDeliveryGuarantee (declaration i=15249, owner"
              + " i=21136) on "
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
              "RequestedDeliveryGuarantee: ValueRank=-1 does not permit rank " + rank);
        }
        if (value != null && !((Object) value instanceof BrokerTransportQualityOfService)) {
          if (!(value instanceof Integer)) {
            throw new UaRuntimeException(
                StatusCodes.Bad_TypeMismatch,
                "RequestedDeliveryGuarantee: expected"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService"
                    + " or Int32, got "
                    + value);
          }
          if (BrokerTransportQualityOfService.from((Integer) value) == null) {
            throw new UaRuntimeException(
                StatusCodes.Bad_OutOfRange,
                "RequestedDeliveryGuarantee: unknown"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService"
                    + " value "
                    + value);
          }
        }
        convertedValue =
            value == null || value instanceof BrokerTransportQualityOfService
                ? (BrokerTransportQualityOfService) value
                : BrokerTransportQualityOfService.from((Integer) value);
      }
    }
    return (BrokerTransportQualityOfService) convertedValue;
  }

  @Override
  public void setRequestedDeliveryGuarantee(@Nullable BrokerTransportQualityOfService value) {
    var node = getRequestedDeliveryGuaranteeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RequestedDeliveryGuarantee (declaration i=15249, owner"
              + " i=21136) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }
}
