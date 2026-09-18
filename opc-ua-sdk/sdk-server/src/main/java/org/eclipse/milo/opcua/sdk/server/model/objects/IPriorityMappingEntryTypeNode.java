package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link IPriorityMappingEntryType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.15">Model
 *     documentation</a>
 */
public class IPriorityMappingEntryTypeNode extends BaseInterfaceTypeNode
    implements IPriorityMappingEntryType {
  public IPriorityMappingEntryTypeNode(
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

  public IPriorityMappingEntryTypeNode(
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
  public BaseDataVariableTypeNode getMappingUriNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MappingUri",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable String getMappingUri() {
    return ServerNodeSupport.read(this, getMappingUriNode(), String.class, null);
  }

  @Override
  public void setMappingUri(@Nullable String value) {
    ServerNodeSupport.write(this, getMappingUriNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getPriorityLabelNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "PriorityLabel",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable String getPriorityLabel() {
    return ServerNodeSupport.read(this, getPriorityLabelNode(), String.class, null);
  }

  @Override
  public void setPriorityLabel(@Nullable String value) {
    ServerNodeSupport.write(this, getPriorityLabelNode(), value, false, false, false);
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getPriorityValue_DSCPNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "PriorityValue_DSCP",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getPriorityValue_DSCP() {
    return ServerNodeSupport.read(this, getPriorityValue_DSCPNode(), UInteger.class, null);
  }

  @Override
  public void setPriorityValue_DSCP(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getPriorityValue_DSCPNode(),
        Namespaces.OPC_UA,
        "PriorityValue_DSCP",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getPriorityValue_PCPNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "PriorityValue_PCP",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 3L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UByte getPriorityValue_PCP() {
    return ServerNodeSupport.read(this, getPriorityValue_PCPNode(), UByte.class, null);
  }

  @Override
  public void setPriorityValue_PCP(@Nullable UByte value) {
    ServerNodeSupport.write(
        this,
        getPriorityValue_PCPNode(),
        Namespaces.OPC_UA,
        "PriorityValue_PCP",
        value,
        false,
        false,
        false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getMappingUriNode();
    getPriorityLabelNode();
    getPriorityValue_DSCPNode();
    getPriorityValue_PCPNode();
  }
}
