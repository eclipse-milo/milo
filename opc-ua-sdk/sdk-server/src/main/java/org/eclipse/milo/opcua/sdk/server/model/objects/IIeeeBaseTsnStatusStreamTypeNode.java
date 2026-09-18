package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnFailureCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnListenerStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnTalkerStatus;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link IIeeeBaseTsnStatusStreamType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.9">Model
 *     documentation</a>
 */
public class IIeeeBaseTsnStatusStreamTypeNode extends BaseInterfaceTypeNode
    implements IIeeeBaseTsnStatusStreamType {
  public IIeeeBaseTsnStatusStreamTypeNode(
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

  public IIeeeBaseTsnStatusStreamTypeNode(
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
  public BaseDataVariableTypeNode getFailureCodeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "FailureCode",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 24218L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable TsnFailureCode getFailureCode() {
    return ServerNodeSupport.read(
        this, getFailureCodeNode(), TsnFailureCode.class, TsnFailureCode::from);
  }

  @Override
  public void setFailureCode(@Nullable TsnFailureCode value) {
    ServerNodeSupport.write(this, getFailureCodeNode(), value, false, true, false);
  }

  @Override
  public BaseDataVariableTypeNode getFailureSystemIdentifierNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "FailureSystemIdentifier",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 3L),
        2,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable Variant getFailureSystemIdentifier() {
    return ServerNodeSupport.readVariant(
        this, getFailureSystemIdentifierNode(), 2, UByte.class, null);
  }

  @Override
  public void setFailureSystemIdentifier(@Nullable Variant value) {
    ServerNodeSupport.writeVariant(
        this,
        getFailureSystemIdentifierNode(),
        null,
        "FailureSystemIdentifier",
        value,
        2,
        UByte.class,
        null);
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getListenerStatusNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ListenerStatus",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 24224L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable TsnListenerStatus getListenerStatus() {
    return ServerNodeSupport.read(
        this, getListenerStatusNode(), TsnListenerStatus.class, TsnListenerStatus::from);
  }

  @Override
  public void setListenerStatus(@Nullable TsnListenerStatus value) {
    ServerNodeSupport.write(
        this,
        getListenerStatusNode(),
        Namespaces.OPC_UA,
        "ListenerStatus",
        value,
        false,
        true,
        false);
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getTalkerStatusNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "TalkerStatus",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 24222L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable TsnTalkerStatus getTalkerStatus() {
    return ServerNodeSupport.read(
        this, getTalkerStatusNode(), TsnTalkerStatus.class, TsnTalkerStatus::from);
  }

  @Override
  public void setTalkerStatus(@Nullable TsnTalkerStatus value) {
    ServerNodeSupport.write(
        this, getTalkerStatusNode(), Namespaces.OPC_UA, "TalkerStatus", value, false, true, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getFailureCodeNode();
    getFailureSystemIdentifierNode();
    getListenerStatusNode();
    getTalkerStatusNode();
  }
}
