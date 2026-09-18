package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link BuildInfoType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.7">Model
 *     documentation</a>
 */
public class BuildInfoTypeNode extends BaseDataVariableTypeNode implements BuildInfoType {
  public BuildInfoTypeNode(
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

  public BuildInfoTypeNode(
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
  public BaseDataVariableTypeNode getBuildDateNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "BuildDate",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable DateTime getBuildDate() {
    return ServerNodeSupport.read(this, getBuildDateNode(), DateTime.class, null);
  }

  @Override
  public void setBuildDate(@Nullable DateTime value) {
    ServerNodeSupport.write(this, getBuildDateNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getBuildNumberNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "BuildNumber",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable String getBuildNumber() {
    return ServerNodeSupport.read(this, getBuildNumberNode(), String.class, null);
  }

  @Override
  public void setBuildNumber(@Nullable String value) {
    ServerNodeSupport.write(this, getBuildNumberNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getManufacturerNameNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ManufacturerName",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable String getManufacturerName() {
    return ServerNodeSupport.read(this, getManufacturerNameNode(), String.class, null);
  }

  @Override
  public void setManufacturerName(@Nullable String value) {
    ServerNodeSupport.write(this, getManufacturerNameNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getProductNameNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ProductName",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable String getProductName() {
    return ServerNodeSupport.read(this, getProductNameNode(), String.class, null);
  }

  @Override
  public void setProductName(@Nullable String value) {
    ServerNodeSupport.write(this, getProductNameNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getProductUriNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ProductUri",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable String getProductUri() {
    return ServerNodeSupport.read(this, getProductUriNode(), String.class, null);
  }

  @Override
  public void setProductUri(@Nullable String value) {
    ServerNodeSupport.write(this, getProductUriNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getSoftwareVersionNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SoftwareVersion",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable String getSoftwareVersion() {
    return ServerNodeSupport.read(this, getSoftwareVersionNode(), String.class, null);
  }

  @Override
  public void setSoftwareVersion(@Nullable String value) {
    ServerNodeSupport.write(this, getSoftwareVersionNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getBuildDateNode();
    getBuildNumberNode();
    getManufacturerNameNode();
    getProductNameNode();
    getProductUriNode();
    getSoftwareVersionNode();
  }

  @Override
  public @Nullable BuildInfo getTypedValue() {
    return ServerNodeSupport.read(this, this, BuildInfo.class, null);
  }

  @Override
  public void setTypedValue(@Nullable BuildInfo value) {
    ServerNodeSupport.write(this, this, value, false, false, true);
  }
}
