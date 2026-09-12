/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import com.digitalpetri.opcua.uanodeset.runtime.members.MemberDeclaration;
import com.digitalpetri.opcua.uanodeset.runtime.server.ServerMembers;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

public class BuildInfoTypeNode extends BaseDataVariableTypeNode implements BuildInfoType {
  public BuildInfoTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger[] arrayDimensions,
      UByte accessLevel,
      UByte userAccessLevel,
      Double minimumSamplingInterval,
      boolean historizing,
      AccessLevelExType accessLevelEx) {
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
        value,
        dataType,
        valueRank,
        arrayDimensions,
        accessLevel,
        userAccessLevel,
        minimumSamplingInterval,
        historizing,
        accessLevelEx);
  }

  public BuildInfoTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger[] arrayDimensions) {
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
        value,
        dataType,
        valueRank,
        arrayDimensions);
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
  public BaseDataVariableTypeNode getProductUriNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ProductUri",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ProductUri (declaration i=3052, owner i=3051)"));
  }

  @Override
  public @Nullable String getProductUri() {
    var node = getProductUriNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ProductUri (declaration i=3052, owner i=3051)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setProductUri(@Nullable String value) {
    var node = getProductUriNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ProductUri (declaration i=3052, owner i=3051)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getManufacturerNameNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ManufacturerName",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ManufacturerName (declaration i=3053, owner i=3051)"));
  }

  @Override
  public @Nullable String getManufacturerName() {
    var node = getManufacturerNameNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ManufacturerName (declaration i=3053, owner i=3051)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setManufacturerName(@Nullable String value) {
    var node = getManufacturerNameNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ManufacturerName (declaration i=3053, owner i=3051)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getProductNameNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ProductName",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ProductName (declaration i=3054, owner i=3051)"));
  }

  @Override
  public @Nullable String getProductName() {
    var node = getProductNameNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ProductName (declaration i=3054, owner i=3051)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setProductName(@Nullable String value) {
    var node = getProductNameNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ProductName (declaration i=3054, owner i=3051)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getSoftwareVersionNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SoftwareVersion",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SoftwareVersion (declaration i=3055, owner i=3051)"));
  }

  @Override
  public @Nullable String getSoftwareVersion() {
    var node = getSoftwareVersionNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SoftwareVersion (declaration i=3055, owner i=3051)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setSoftwareVersion(@Nullable String value) {
    var node = getSoftwareVersionNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SoftwareVersion (declaration i=3055, owner i=3051)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getBuildNumberNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "BuildNumber",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:BuildNumber (declaration i=3056, owner i=3051)"));
  }

  @Override
  public @Nullable String getBuildNumber() {
    var node = getBuildNumberNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:BuildNumber (declaration i=3056, owner i=3051)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setBuildNumber(@Nullable String value) {
    var node = getBuildNumberNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:BuildNumber (declaration i=3056, owner i=3051)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getBuildDateNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "BuildDate",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:BuildDate (declaration i=3057, owner i=3051)"));
  }

  @Override
  public @Nullable DateTime getBuildDate() {
    var node = getBuildDateNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:BuildDate (declaration i=3057, owner i=3051)"
              + " on "
              + getNodeId());
    }
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setBuildDate(@Nullable DateTime value) {
    var node = getBuildDateNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:BuildDate (declaration i=3057, owner i=3051)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }
}
