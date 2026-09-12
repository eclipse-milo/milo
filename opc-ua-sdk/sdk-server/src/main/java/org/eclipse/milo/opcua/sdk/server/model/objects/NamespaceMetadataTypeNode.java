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
import java.lang.reflect.Array;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.IdType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

public class NamespaceMetadataTypeNode extends BaseObjectTypeNode implements NamespaceMetadataType {
  public NamespaceMetadataTypeNode(
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

  public NamespaceMetadataTypeNode(
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
  public PropertyTypeNode getNamespaceUriNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "NamespaceUri",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:NamespaceUri (declaration i=11617, owner i=11616)"));
  }

  @Override
  public @Nullable String getNamespaceUri() {
    var node = getNamespaceUriNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NamespaceUri (declaration i=11617, owner i=11616)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setNamespaceUri(@Nullable String value) {
    var node = getNamespaceUriNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NamespaceUri (declaration i=11617, owner i=11616)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getNamespaceVersionNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "NamespaceVersion",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:NamespaceVersion (declaration i=11618, owner i=11616)"));
  }

  @Override
  public @Nullable String getNamespaceVersion() {
    var node = getNamespaceVersionNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NamespaceVersion (declaration i=11618, owner i=11616)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setNamespaceVersion(@Nullable String value) {
    var node = getNamespaceVersionNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NamespaceVersion (declaration i=11618, owner i=11616)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getNamespacePublicationDateNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "NamespacePublicationDate",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:NamespacePublicationDate (declaration i=11619, owner"
                + " i=11616)"));
  }

  @Override
  public @Nullable DateTime getNamespacePublicationDate() {
    var node = getNamespacePublicationDateNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NamespacePublicationDate (declaration i=11619, owner"
              + " i=11616) on "
              + getNodeId());
    }
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setNamespacePublicationDate(@Nullable DateTime value) {
    var node = getNamespacePublicationDateNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NamespacePublicationDate (declaration i=11619, owner"
              + " i=11616) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getIsNamespaceSubsetNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "IsNamespaceSubset",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:IsNamespaceSubset (declaration i=11620, owner i=11616)"));
  }

  @Override
  public @Nullable Boolean getIsNamespaceSubset() {
    var node = getIsNamespaceSubsetNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:IsNamespaceSubset (declaration i=11620, owner i=11616)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setIsNamespaceSubset(@Nullable Boolean value) {
    var node = getIsNamespaceSubsetNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:IsNamespaceSubset (declaration i=11620, owner i=11616)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getStaticNodeIdTypesNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "StaticNodeIdTypes",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:StaticNodeIdTypes (declaration i=11621, owner i=11616)"));
  }

  @Override
  public @Nullable IdType @Nullable [] getStaticNodeIdTypes() {
    var node = getStaticNodeIdTypesNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StaticNodeIdTypes (declaration i=11621, owner i=11616)"
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
        boolean permitted = rank == 1 || (rank == 1 && Array.getLength(elements) == 0);
        if (!permitted) {
          throw new UaRuntimeException(
              StatusCodes.Bad_TypeMismatch,
              "StaticNodeIdTypes: ValueRank=1 does not permit rank " + rank);
        }
        IdType[] converted = new IdType[Array.getLength(elements)];
        if (elements != null && elements.getClass().isArray()) {
          for (int i = 0; i < Array.getLength(elements); i++) {
            Object element = Array.get(elements, i);
            if (element != null && !((Object) element instanceof IdType)) {
              if (!(element instanceof Integer)) {
                throw new UaRuntimeException(
                    StatusCodes.Bad_TypeMismatch,
                    "StaticNodeIdTypes: expected"
                        + " org.eclipse.milo.opcua.stack.core.types.enumerated.IdType or Int32, got"
                        + " "
                        + element);
              }
              if (IdType.from((Integer) element) == null) {
                throw new UaRuntimeException(
                    StatusCodes.Bad_OutOfRange,
                    "StaticNodeIdTypes: unknown"
                        + " org.eclipse.milo.opcua.stack.core.types.enumerated.IdType value "
                        + element);
              }
            }
            converted[i] =
                element == null || element instanceof IdType
                    ? (IdType) element
                    : IdType.from((Integer) element);
          }
        } else {
          if (elements != null && !((Object) elements instanceof IdType)) {
            if (!(elements instanceof Integer)) {
              throw new UaRuntimeException(
                  StatusCodes.Bad_TypeMismatch,
                  "StaticNodeIdTypes: expected"
                      + " org.eclipse.milo.opcua.stack.core.types.enumerated.IdType or Int32, got "
                      + elements);
            }
            if (IdType.from((Integer) elements) == null) {
              throw new UaRuntimeException(
                  StatusCodes.Bad_OutOfRange,
                  "StaticNodeIdTypes: unknown"
                      + " org.eclipse.milo.opcua.stack.core.types.enumerated.IdType value "
                      + elements);
            }
          }
        }
        convertedValue = converted;
      }
    }
    return (IdType[]) convertedValue;
  }

  @Override
  public void setStaticNodeIdTypes(@Nullable IdType @Nullable [] value) {
    var node = getStaticNodeIdTypesNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StaticNodeIdTypes (declaration i=11621, owner i=11616)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getStaticNumericNodeIdRangeNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "StaticNumericNodeIdRange",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:StaticNumericNodeIdRange (declaration i=11622, owner"
                + " i=11616)"));
  }

  @Override
  public @Nullable String @Nullable [] getStaticNumericNodeIdRange() {
    var node = getStaticNumericNodeIdRangeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StaticNumericNodeIdRange (declaration i=11622, owner"
              + " i=11616) on "
              + getNodeId());
    }
    return (String[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setStaticNumericNodeIdRange(@Nullable String @Nullable [] value) {
    var node = getStaticNumericNodeIdRangeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StaticNumericNodeIdRange (declaration i=11622, owner"
              + " i=11616) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getStaticStringNodeIdPatternNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "StaticStringNodeIdPattern",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:StaticStringNodeIdPattern (declaration i=11623, owner"
                + " i=11616)"));
  }

  @Override
  public @Nullable String getStaticStringNodeIdPattern() {
    var node = getStaticStringNodeIdPatternNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StaticStringNodeIdPattern (declaration i=11623, owner"
              + " i=11616) on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setStaticStringNodeIdPattern(@Nullable String value) {
    var node = getStaticStringNodeIdPatternNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StaticStringNodeIdPattern (declaration i=11623, owner"
              + " i=11616) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getDefaultRolePermissionsNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DefaultRolePermissions",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:DefaultRolePermissions (declaration i=16137, owner"
                + " i=11616)"));
  }

  @Override
  public @Nullable RolePermissionType @Nullable [] getDefaultRolePermissions() {
    var node = getDefaultRolePermissionsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DefaultRolePermissions (declaration i=16137, owner i=11616)"
              + " on "
              + getNodeId());
    }
    return ServerPropertyValues.decode(
        getNodeContext().getServer().getStaticEncodingContext(),
        node.getValue().getValue().getValue(),
        RolePermissionType[].class,
        RolePermissionType.class,
        "http://opcfoundation.org/UA/:DefaultRolePermissions (declaration i=16137, owner i=11616)");
  }

  @Override
  public void setDefaultRolePermissions(@Nullable RolePermissionType @Nullable [] value) {
    var node = getDefaultRolePermissionsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DefaultRolePermissions (declaration i=16137, owner i=11616)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getDefaultUserRolePermissionsNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DefaultUserRolePermissions",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:DefaultUserRolePermissions (declaration i=16138, owner"
                + " i=11616)"));
  }

  @Override
  public @Nullable RolePermissionType @Nullable [] getDefaultUserRolePermissions() {
    var node = getDefaultUserRolePermissionsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DefaultUserRolePermissions (declaration i=16138, owner"
              + " i=11616) on "
              + getNodeId());
    }
    return ServerPropertyValues.decode(
        getNodeContext().getServer().getStaticEncodingContext(),
        node.getValue().getValue().getValue(),
        RolePermissionType[].class,
        RolePermissionType.class,
        "http://opcfoundation.org/UA/:DefaultUserRolePermissions (declaration i=16138, owner"
            + " i=11616)");
  }

  @Override
  public void setDefaultUserRolePermissions(@Nullable RolePermissionType @Nullable [] value) {
    var node = getDefaultUserRolePermissionsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DefaultUserRolePermissions (declaration i=16138, owner"
              + " i=11616) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getDefaultAccessRestrictionsNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DefaultAccessRestrictions",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:DefaultAccessRestrictions (declaration i=16139, owner"
                + " i=11616)"));
  }

  @Override
  public @Nullable AccessRestrictionType getDefaultAccessRestrictions() {
    var node = getDefaultAccessRestrictionsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DefaultAccessRestrictions (declaration i=16139, owner"
              + " i=11616) on "
              + getNodeId());
    }
    return (AccessRestrictionType) node.getValue().getValue().getValue();
  }

  @Override
  public void setDefaultAccessRestrictions(@Nullable AccessRestrictionType value) {
    var node = getDefaultAccessRestrictionsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DefaultAccessRestrictions (declaration i=16139, owner"
              + " i=11616) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getConfigurationVersionNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ConfigurationVersion",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:ConfigurationVersion (declaration i=25267, owner"
                + " i=11616)"));
  }

  @Override
  public @Nullable UInteger getConfigurationVersion() {
    var node = getConfigurationVersionNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ConfigurationVersion (declaration i=25267, owner i=11616)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setConfigurationVersion(@Nullable UInteger value) {
    var node = getConfigurationVersionNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ConfigurationVersion (declaration i=25267, owner i=11616)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getModelVersionNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ModelVersion",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:ModelVersion (declaration i=32419, owner i=11616)"));
  }

  @Override
  public @Nullable String getModelVersion() {
    var node = getModelVersionNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ModelVersion (declaration i=32419, owner i=11616)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setModelVersion(@Nullable String value) {
    var node = getModelVersionNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ModelVersion (declaration i=32419, owner i=11616)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable AddressSpaceFileTypeNode getNamespaceFileNode() {
    return ServerMembers.lookup(
        this,
        AddressSpaceFileTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "NamespaceFile",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            true,
            "http://opcfoundation.org/UA/:NamespaceFile (declaration i=11624, owner i=11616)"));
  }
}
