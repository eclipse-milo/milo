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
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
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
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

public class LldpRemoteStatisticsTypeNode extends BaseObjectTypeNode
    implements LldpRemoteStatisticsType {
  public LldpRemoteStatisticsTypeNode(
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

  public LldpRemoteStatisticsTypeNode(
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
  public BaseDataVariableTypeNode getLastChangeTimeNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:LastChangeTime (declaration i=18997, owner i=18996)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "LastChangeTime");
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
                    + " while resolving http://opcfoundation.org/UA/:LastChangeTime (declaration"
                    + " i=18997, owner i=18996) on "
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
              "http://opcfoundation.org/UA/:LastChangeTime (declaration i=18997, owner i=18996)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:LastChangeTime (declaration i=18997, owner i=18996)"
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
              "http://opcfoundation.org/UA/:LastChangeTime (declaration i=18997, owner i=18996)"
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
            "http://opcfoundation.org/UA/:LastChangeTime (declaration i=18997, owner i=18996)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:LastChangeTime (declaration i=18997, owner i=18996)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:LastChangeTime (declaration i=18997, owner i=18996)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof BaseDataVariableTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:LastChangeTime (declaration i=18997, owner i=18996)"
              + " on "
              + getNodeId());
    }
    return (BaseDataVariableTypeNode) parent;
  }

  @Override
  public @Nullable UInteger getLastChangeTime() {
    var node = getLastChangeTimeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LastChangeTime (declaration i=18997, owner i=18996)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastChangeTime(@Nullable UInteger value) {
    var node = getLastChangeTimeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LastChangeTime (declaration i=18997, owner i=18996)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getRemoteInsertsNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:RemoteInserts (declaration i=18998, owner i=18996)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "RemoteInserts");
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
                    + " while resolving http://opcfoundation.org/UA/:RemoteInserts (declaration"
                    + " i=18998, owner i=18996) on "
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
              "http://opcfoundation.org/UA/:RemoteInserts (declaration i=18998, owner i=18996)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:RemoteInserts (declaration i=18998, owner i=18996)"
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
              "http://opcfoundation.org/UA/:RemoteInserts (declaration i=18998, owner i=18996)"
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
            "http://opcfoundation.org/UA/:RemoteInserts (declaration i=18998, owner i=18996)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:RemoteInserts (declaration i=18998, owner i=18996)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:RemoteInserts (declaration i=18998, owner i=18996)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof BaseDataVariableTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:RemoteInserts (declaration i=18998, owner i=18996)"
              + " on "
              + getNodeId());
    }
    return (BaseDataVariableTypeNode) parent;
  }

  @Override
  public @Nullable UInteger getRemoteInserts() {
    var node = getRemoteInsertsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RemoteInserts (declaration i=18998, owner i=18996)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setRemoteInserts(@Nullable UInteger value) {
    var node = getRemoteInsertsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RemoteInserts (declaration i=18998, owner i=18996)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getRemoteDeletesNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:RemoteDeletes (declaration i=18999, owner i=18996)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "RemoteDeletes");
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
                    + " while resolving http://opcfoundation.org/UA/:RemoteDeletes (declaration"
                    + " i=18999, owner i=18996) on "
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
              "http://opcfoundation.org/UA/:RemoteDeletes (declaration i=18999, owner i=18996)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:RemoteDeletes (declaration i=18999, owner i=18996)"
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
              "http://opcfoundation.org/UA/:RemoteDeletes (declaration i=18999, owner i=18996)"
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
            "http://opcfoundation.org/UA/:RemoteDeletes (declaration i=18999, owner i=18996)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:RemoteDeletes (declaration i=18999, owner i=18996)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:RemoteDeletes (declaration i=18999, owner i=18996)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof BaseDataVariableTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:RemoteDeletes (declaration i=18999, owner i=18996)"
              + " on "
              + getNodeId());
    }
    return (BaseDataVariableTypeNode) parent;
  }

  @Override
  public @Nullable UInteger getRemoteDeletes() {
    var node = getRemoteDeletesNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RemoteDeletes (declaration i=18999, owner i=18996)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setRemoteDeletes(@Nullable UInteger value) {
    var node = getRemoteDeletesNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RemoteDeletes (declaration i=18999, owner i=18996)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getRemoteDropsNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:RemoteDrops (declaration i=19000, owner i=18996)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "RemoteDrops");
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
                    + " while resolving http://opcfoundation.org/UA/:RemoteDrops (declaration"
                    + " i=19000, owner i=18996) on "
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
              "http://opcfoundation.org/UA/:RemoteDrops (declaration i=19000, owner i=18996)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:RemoteDrops (declaration i=19000, owner i=18996)"
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
              "http://opcfoundation.org/UA/:RemoteDrops (declaration i=19000, owner i=18996)"
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
            "http://opcfoundation.org/UA/:RemoteDrops (declaration i=19000, owner i=18996)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:RemoteDrops (declaration i=19000, owner i=18996)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:RemoteDrops (declaration i=19000, owner i=18996)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof BaseDataVariableTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:RemoteDrops (declaration i=19000, owner i=18996)"
              + " on "
              + getNodeId());
    }
    return (BaseDataVariableTypeNode) parent;
  }

  @Override
  public @Nullable UInteger getRemoteDrops() {
    var node = getRemoteDropsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RemoteDrops (declaration i=19000, owner i=18996)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setRemoteDrops(@Nullable UInteger value) {
    var node = getRemoteDropsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RemoteDrops (declaration i=19000, owner i=18996)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getRemoteAgeoutsNode() {
    UaNode parent = this;
    {
      var namespaceTable = parent.getNodeContext().getNamespaceTable();
      var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
      var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
      if (namespaceIndex == null || referenceId.isEmpty()) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeIdInvalid,
            "http://opcfoundation.org/UA/:RemoteAgeouts (declaration i=19001, owner i=18996)"
                + " on "
                + getNodeId());
      }
      var browseName = new QualifiedName(namespaceIndex, "RemoteAgeouts");
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
                    + " while resolving http://opcfoundation.org/UA/:RemoteAgeouts (declaration"
                    + " i=19001, owner i=18996) on "
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
              "http://opcfoundation.org/UA/:RemoteAgeouts (declaration i=19001, owner i=18996)"
                  + " on "
                  + getNodeId());
        }
        var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
        if (targetId.isEmpty()) {
          throw new UaRuntimeException(
              StatusCodes.Bad_NodeIdInvalid,
              "http://opcfoundation.org/UA/:RemoteAgeouts (declaration i=19001, owner i=18996)"
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
              "http://opcfoundation.org/UA/:RemoteAgeouts (declaration i=19001, owner i=18996)"
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
            "http://opcfoundation.org/UA/:RemoteAgeouts (declaration i=19001, owner i=18996)"
                + " on "
                + getNodeId());
      }
      if (matches.size() > 1) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches,
            "http://opcfoundation.org/UA/:RemoteAgeouts (declaration i=19001, owner i=18996)"
                + " on "
                + getNodeId());
      }
      parent = matches.values().iterator().next();
      if (parent.getNodeClass() != NodeClass.Variable) {
        throw new UaRuntimeException(
            StatusCodes.Bad_NodeClassInvalid,
            "http://opcfoundation.org/UA/:RemoteAgeouts (declaration i=19001, owner i=18996)"
                + " on "
                + getNodeId());
      }
    }
    if (!(parent instanceof BaseDataVariableTypeNode)) {
      throw new UaRuntimeException(
          StatusCodes.Bad_TypeMismatch,
          "http://opcfoundation.org/UA/:RemoteAgeouts (declaration i=19001, owner i=18996)"
              + " on "
              + getNodeId());
    }
    return (BaseDataVariableTypeNode) parent;
  }

  @Override
  public @Nullable UInteger getRemoteAgeouts() {
    var node = getRemoteAgeoutsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RemoteAgeouts (declaration i=19001, owner i=18996)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setRemoteAgeouts(@Nullable UInteger value) {
    var node = getRemoteAgeoutsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RemoteAgeouts (declaration i=19001, owner i=18996)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }
}
