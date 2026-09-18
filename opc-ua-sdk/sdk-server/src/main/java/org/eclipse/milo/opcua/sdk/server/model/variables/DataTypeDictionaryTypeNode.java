package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/** Node implementation of {@link DataTypeDictionaryType}. */
public class DataTypeDictionaryTypeNode extends BaseDataVariableTypeNode
    implements DataTypeDictionaryType {
  public DataTypeDictionaryTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger @Nullable [] arrayDimensions) {
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

  public DataTypeDictionaryTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger @Nullable [] arrayDimensions,
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

  @Override
  public @Nullable PropertyTypeNode getDataTypeVersion_Node() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "DataTypeVersion",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getDataTypeVersion_() {
    return ServerNodeSupport.read(this, getDataTypeVersion_Node(), String.class, null);
  }

  @Override
  public void setDataTypeVersion_(@Nullable String value) {
    ServerNodeSupport.write(
        this,
        getDataTypeVersion_Node(),
        Namespaces.OPC_UA,
        "DataTypeVersion",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getDeprecatedNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "Deprecated",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getDeprecated() {
    return ServerNodeSupport.read(this, getDeprecatedNode(), Boolean.class, null);
  }

  @Override
  public void setDeprecated(@Nullable Boolean value) {
    ServerNodeSupport.write(
        this, getDeprecatedNode(), Namespaces.OPC_UA, "Deprecated", value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getNamespaceUriNode() {
    return ServerNodeSupport.optionalChild(
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
    ServerNodeSupport.write(
        this, getNamespaceUriNode(), Namespaces.OPC_UA, "NamespaceUri", value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getDataTypeVersion_Node();
    getDeprecatedNode();
    getNamespaceUriNode();
  }

  @Override
  public @Nullable ByteString getTypedValue() {
    return ServerNodeSupport.read(this, this, ByteString.class, null);
  }

  @Override
  public void setTypedValue(@Nullable ByteString value) {
    ServerNodeSupport.write(this, this, value, false, false, false);
  }
}
