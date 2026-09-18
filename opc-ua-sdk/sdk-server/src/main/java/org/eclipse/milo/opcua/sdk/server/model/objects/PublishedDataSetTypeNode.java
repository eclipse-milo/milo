package org.eclipse.milo.opcua.sdk.server.model.objects;

import java.util.UUID;
import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link PublishedDataSetType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.1">Model
 *     documentation</a>
 */
public class PublishedDataSetTypeNode extends BaseObjectTypeNode implements PublishedDataSetType {
  public PublishedDataSetTypeNode(
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

  public PublishedDataSetTypeNode(
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
  public PropertyTypeNode getConfigurationVersionNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ConfigurationVersion",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 14593L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable ConfigurationVersionDataType getConfigurationVersion() {
    return ServerNodeSupport.read(
        this, getConfigurationVersionNode(), ConfigurationVersionDataType.class, null);
  }

  @Override
  public void setConfigurationVersion(@Nullable ConfigurationVersionDataType value) {
    ServerNodeSupport.write(this, getConfigurationVersionNode(), value, false, false, true);
  }

  @Override
  public @Nullable PropertyTypeNode getCyclicDataSetNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "CyclicDataSet",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getCyclicDataSet() {
    return ServerNodeSupport.read(this, getCyclicDataSetNode(), Boolean.class, null);
  }

  @Override
  public void setCyclicDataSet(@Nullable Boolean value) {
    ServerNodeSupport.write(
        this,
        getCyclicDataSetNode(),
        Namespaces.OPC_UA,
        "CyclicDataSet",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getDataSetClassIdNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "DataSetClassId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 14L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UUID getDataSetClassId() {
    return ServerNodeSupport.read(this, getDataSetClassIdNode(), UUID.class, null);
  }

  @Override
  public void setDataSetClassId(@Nullable UUID value) {
    ServerNodeSupport.write(
        this,
        getDataSetClassIdNode(),
        Namespaces.OPC_UA,
        "DataSetClassId",
        value,
        false,
        false,
        false);
  }

  @Override
  public PropertyTypeNode getDataSetMetaDataNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DataSetMetaData",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 14523L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable DataSetMetaDataType getDataSetMetaData() {
    return ServerNodeSupport.read(this, getDataSetMetaDataNode(), DataSetMetaDataType.class, null);
  }

  @Override
  public void setDataSetMetaData(@Nullable DataSetMetaDataType value) {
    ServerNodeSupport.write(this, getDataSetMetaDataNode(), value, false, false, true);
  }

  @Override
  public @Nullable ExtensionFieldsTypeNode getExtensionFieldsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ExtensionFields",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 15489L),
        null,
        -1,
        ExtensionFieldsTypeNode.class);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getConfigurationVersionNode();
    getCyclicDataSetNode();
    getDataSetClassIdNode();
    getDataSetMetaDataNode();
    getExtensionFieldsNode();
  }
}
