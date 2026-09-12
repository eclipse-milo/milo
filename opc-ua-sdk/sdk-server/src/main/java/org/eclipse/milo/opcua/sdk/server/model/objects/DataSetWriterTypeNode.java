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
import com.digitalpetri.opcua.uanodeset.runtime.server.ServerPropertyValues;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

public class DataSetWriterTypeNode extends BaseObjectTypeNode implements DataSetWriterType {
  public DataSetWriterTypeNode(
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

  public DataSetWriterTypeNode(
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
  public PropertyTypeNode getDataSetWriterIdNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DataSetWriterId",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DataSetWriterId (declaration i=21092, owner i=15298)"));
  }

  @Override
  public @Nullable UShort getDataSetWriterId() {
    var node = getDataSetWriterIdNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetWriterId (declaration i=21092, owner i=15298)"
              + " on "
              + getNodeId());
    }
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setDataSetWriterId(@Nullable UShort value) {
    var node = getDataSetWriterIdNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetWriterId (declaration i=21092, owner i=15298)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getDataSetFieldContentMaskNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DataSetFieldContentMask",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DataSetFieldContentMask (declaration i=21093, owner"
                + " i=15298)"));
  }

  @Override
  public @Nullable DataSetFieldContentMask getDataSetFieldContentMask() {
    var node = getDataSetFieldContentMaskNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetFieldContentMask (declaration i=21093, owner"
              + " i=15298) on "
              + getNodeId());
    }
    return (DataSetFieldContentMask) node.getValue().getValue().getValue();
  }

  @Override
  public void setDataSetFieldContentMask(@Nullable DataSetFieldContentMask value) {
    var node = getDataSetFieldContentMaskNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetFieldContentMask (declaration i=21093, owner"
              + " i=15298) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getKeyFrameCountNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "KeyFrameCount",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:KeyFrameCount (declaration i=21094, owner i=15298)"));
  }

  @Override
  public @Nullable UInteger getKeyFrameCount() {
    var node = getKeyFrameCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:KeyFrameCount (declaration i=21094, owner i=15298)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setKeyFrameCount(@Nullable UInteger value) {
    var node = getKeyFrameCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:KeyFrameCount (declaration i=21094, owner i=15298)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getDataSetWriterPropertiesNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DataSetWriterProperties",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DataSetWriterProperties (declaration i=17493, owner"
                + " i=15298)"));
  }

  @Override
  public @Nullable KeyValuePair @Nullable [] getDataSetWriterProperties() {
    var node = getDataSetWriterPropertiesNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetWriterProperties (declaration i=17493, owner"
              + " i=15298) on "
              + getNodeId());
    }
    return ServerPropertyValues.decode(
        getNodeContext().getServer().getStaticEncodingContext(),
        node.getValue().getValue().getValue(),
        KeyValuePair[].class,
        KeyValuePair.class,
        "http://opcfoundation.org/UA/:DataSetWriterProperties (declaration i=17493, owner"
            + " i=15298)");
  }

  @Override
  public void setDataSetWriterProperties(@Nullable KeyValuePair @Nullable [] value) {
    var node = getDataSetWriterPropertiesNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetWriterProperties (declaration i=17493, owner"
              + " i=15298) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable DataSetWriterTransportTypeNode getTransportSettingsNode() {
    return ServerMembers.lookup(
        this,
        DataSetWriterTransportTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "TransportSettings",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            true,
            "http://opcfoundation.org/UA/:TransportSettings (declaration i=15303, owner i=15298)"));
  }

  @Override
  public @Nullable DataSetWriterMessageTypeNode getMessageSettingsNode() {
    return ServerMembers.lookup(
        this,
        DataSetWriterMessageTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MessageSettings",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            true,
            "http://opcfoundation.org/UA/:MessageSettings (declaration i=21095, owner i=15298)"));
  }

  @Override
  public PubSubStatusTypeNode getStatusNode() {
    return ServerMembers.lookup(
        this,
        PubSubStatusTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Status",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:Status (declaration i=15299, owner i=15298)"));
  }

  @Override
  public @Nullable PubSubDiagnosticsDataSetWriterTypeNode getDiagnosticsNode() {
    return ServerMembers.lookup(
        this,
        PubSubDiagnosticsDataSetWriterTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Diagnostics",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            true,
            "http://opcfoundation.org/UA/:Diagnostics (declaration i=19550, owner i=15298)"));
  }
}
