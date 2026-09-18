package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.IdType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link NamespaceMetadataType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.13">Model
 *     documentation</a>
 */
public class NamespaceMetadataTypeNode extends BaseObjectTypeNode implements NamespaceMetadataType {
  public NamespaceMetadataTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      @Nullable LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType @Nullable [] rolePermissions,
      RolePermissionType @Nullable [] userRolePermissions,
      @Nullable AccessRestrictionType accessRestrictions) {
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

  public NamespaceMetadataTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      @Nullable LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType @Nullable [] rolePermissions,
      RolePermissionType @Nullable [] userRolePermissions,
      @Nullable AccessRestrictionType accessRestrictions,
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

  @Override
  public @Nullable PropertyTypeNode getConfigurationVersionNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ConfigurationVersion",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 20998L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getConfigurationVersion() {
    return ServerNodeSupport.read(this, getConfigurationVersionNode(), UInteger.class, null);
  }

  @Override
  public void setConfigurationVersion(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getConfigurationVersionNode(),
        Namespaces.OPC_UA,
        "ConfigurationVersion",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getDefaultAccessRestrictionsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "DefaultAccessRestrictions",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 95L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable AccessRestrictionType getDefaultAccessRestrictions() {
    return ServerNodeSupport.read(
        this, getDefaultAccessRestrictionsNode(), AccessRestrictionType.class, null);
  }

  @Override
  public void setDefaultAccessRestrictions(@Nullable AccessRestrictionType value) {
    ServerNodeSupport.write(
        this,
        getDefaultAccessRestrictionsNode(),
        Namespaces.OPC_UA,
        "DefaultAccessRestrictions",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getDefaultRolePermissionsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "DefaultRolePermissions",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 96L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable RolePermissionType @Nullable [] getDefaultRolePermissions() {
    return ServerNodeSupport.readArray(
        this, getDefaultRolePermissionsNode(), RolePermissionType.class, null);
  }

  @Override
  public void setDefaultRolePermissions(@Nullable RolePermissionType @Nullable [] value) {
    ServerNodeSupport.write(
        this,
        getDefaultRolePermissionsNode(),
        Namespaces.OPC_UA,
        "DefaultRolePermissions",
        value,
        true,
        false,
        true);
  }

  @Override
  public @Nullable PropertyTypeNode getDefaultUserRolePermissionsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "DefaultUserRolePermissions",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 96L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable RolePermissionType @Nullable [] getDefaultUserRolePermissions() {
    return ServerNodeSupport.readArray(
        this, getDefaultUserRolePermissionsNode(), RolePermissionType.class, null);
  }

  @Override
  public void setDefaultUserRolePermissions(@Nullable RolePermissionType @Nullable [] value) {
    ServerNodeSupport.write(
        this,
        getDefaultUserRolePermissionsNode(),
        Namespaces.OPC_UA,
        "DefaultUserRolePermissions",
        value,
        true,
        false,
        true);
  }

  @Override
  public PropertyTypeNode getIsNamespaceSubsetNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "IsNamespaceSubset",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getIsNamespaceSubset() {
    return ServerNodeSupport.read(this, getIsNamespaceSubsetNode(), Boolean.class, null);
  }

  @Override
  public void setIsNamespaceSubset(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getIsNamespaceSubsetNode(), value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getModelVersionNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ModelVersion",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 24263L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getModelVersion() {
    return ServerNodeSupport.read(this, getModelVersionNode(), String.class, null);
  }

  @Override
  public void setModelVersion(@Nullable String value) {
    ServerNodeSupport.write(
        this, getModelVersionNode(), Namespaces.OPC_UA, "ModelVersion", value, false, false, false);
  }

  @Override
  public @Nullable AddressSpaceFileTypeNode getNamespaceFileNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "NamespaceFile",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11595L),
        null,
        -1,
        AddressSpaceFileTypeNode.class);
  }

  @Override
  public PropertyTypeNode getNamespacePublicationDateNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "NamespacePublicationDate",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 13L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable DateTime getNamespacePublicationDate() {
    return ServerNodeSupport.read(this, getNamespacePublicationDateNode(), DateTime.class, null);
  }

  @Override
  public void setNamespacePublicationDate(@Nullable DateTime value) {
    ServerNodeSupport.write(this, getNamespacePublicationDateNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getNamespaceUriNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "NamespaceUri",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getNamespaceUri() {
    return ServerNodeSupport.read(this, getNamespaceUriNode(), String.class, null);
  }

  @Override
  public void setNamespaceUri(@Nullable String value) {
    ServerNodeSupport.write(this, getNamespaceUriNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getNamespaceVersionNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "NamespaceVersion",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getNamespaceVersion() {
    return ServerNodeSupport.read(this, getNamespaceVersionNode(), String.class, null);
  }

  @Override
  public void setNamespaceVersion(@Nullable String value) {
    ServerNodeSupport.write(this, getNamespaceVersionNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getStaticNodeIdTypesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "StaticNodeIdTypes",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 256L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public IdType @Nullable [] getStaticNodeIdTypes() {
    return ServerNodeSupport.readArray(
        this, getStaticNodeIdTypesNode(), IdType.class, IdType::from);
  }

  @Override
  public void setStaticNodeIdTypes(IdType @Nullable [] value) {
    ServerNodeSupport.write(this, getStaticNodeIdTypesNode(), value, true, true, false);
  }

  @Override
  public PropertyTypeNode getStaticNumericNodeIdRangeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "StaticNumericNodeIdRange",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 291L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String @Nullable [] getStaticNumericNodeIdRange() {
    return ServerNodeSupport.readArray(this, getStaticNumericNodeIdRangeNode(), String.class, null);
  }

  @Override
  public void setStaticNumericNodeIdRange(@Nullable String @Nullable [] value) {
    ServerNodeSupport.write(this, getStaticNumericNodeIdRangeNode(), value, true, false, false);
  }

  @Override
  public PropertyTypeNode getStaticStringNodeIdPatternNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "StaticStringNodeIdPattern",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getStaticStringNodeIdPattern() {
    return ServerNodeSupport.read(this, getStaticStringNodeIdPatternNode(), String.class, null);
  }

  @Override
  public void setStaticStringNodeIdPattern(@Nullable String value) {
    ServerNodeSupport.write(this, getStaticStringNodeIdPatternNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getConfigurationVersionNode();
    getDefaultAccessRestrictionsNode();
    getDefaultRolePermissionsNode();
    getDefaultUserRolePermissionsNode();
    getIsNamespaceSubsetNode();
    getModelVersionNode();
    getNamespaceFileNode();
    getNamespacePublicationDateNode();
    getNamespaceUriNode();
    getNamespaceVersionNode();
    getStaticNodeIdTypesNode();
    getStaticNumericNodeIdRangeNode();
    getStaticStringNodeIdPatternNode();
  }
}
