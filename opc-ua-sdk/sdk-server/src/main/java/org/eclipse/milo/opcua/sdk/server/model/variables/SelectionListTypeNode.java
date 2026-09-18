package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link SelectionListType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.18">Model
 *     documentation</a>
 */
public class SelectionListTypeNode extends BaseDataVariableTypeNode implements SelectionListType {
  public SelectionListTypeNode(
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

  public SelectionListTypeNode(
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
  public @Nullable PropertyTypeNode getRestrictToListNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "RestrictToList",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getRestrictToList() {
    return ServerNodeSupport.read(this, getRestrictToListNode(), Boolean.class, null);
  }

  @Override
  public void setRestrictToList(@Nullable Boolean value) {
    ServerNodeSupport.write(
        this,
        getRestrictToListNode(),
        Namespaces.OPC_UA,
        "RestrictToList",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getSelectionDescriptionsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SelectionDescriptions",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public LocalizedText @Nullable [] getSelectionDescriptions() {
    return ServerNodeSupport.readArray(
        this, getSelectionDescriptionsNode(), LocalizedText.class, null);
  }

  @Override
  public void setSelectionDescriptions(LocalizedText @Nullable [] value) {
    ServerNodeSupport.write(
        this,
        getSelectionDescriptionsNode(),
        Namespaces.OPC_UA,
        "SelectionDescriptions",
        value,
        true,
        false,
        false);
  }

  @Override
  public PropertyTypeNode getSelectionsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Selections",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 24L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Variant @Nullable [] getSelections() {
    return ServerNodeSupport.readArray(this, getSelectionsNode(), Variant.class, null);
  }

  @Override
  public void setSelections(@Nullable Variant @Nullable [] value) {
    ServerNodeSupport.write(this, getSelectionsNode(), value, true, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getRestrictToListNode();
    getSelectionDescriptionsNode();
    getSelectionsNode();
  }

  @Override
  public @Nullable Variant getTypedValue() {
    return ServerNodeSupport.read(this, this, Variant.class, null);
  }

  @Override
  public void setTypedValue(@Nullable Variant value) {
    ServerNodeSupport.write(this, this, value, false, false, false);
  }
}
