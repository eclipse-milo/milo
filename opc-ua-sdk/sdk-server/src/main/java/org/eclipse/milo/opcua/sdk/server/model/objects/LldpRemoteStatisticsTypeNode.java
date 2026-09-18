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
 * Node implementation of {@link LldpRemoteStatisticsType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.4">Model
 *     documentation</a>
 */
public class LldpRemoteStatisticsTypeNode extends BaseObjectTypeNode
    implements LldpRemoteStatisticsType {
  public LldpRemoteStatisticsTypeNode(
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

  public LldpRemoteStatisticsTypeNode(
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
  public BaseDataVariableTypeNode getLastChangeTimeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LastChangeTime",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getLastChangeTime() {
    return ServerNodeSupport.read(this, getLastChangeTimeNode(), UInteger.class, null);
  }

  @Override
  public void setLastChangeTime(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getLastChangeTimeNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getRemoteAgeoutsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "RemoteAgeouts",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getRemoteAgeouts() {
    return ServerNodeSupport.read(this, getRemoteAgeoutsNode(), UInteger.class, null);
  }

  @Override
  public void setRemoteAgeouts(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getRemoteAgeoutsNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getRemoteDeletesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "RemoteDeletes",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getRemoteDeletes() {
    return ServerNodeSupport.read(this, getRemoteDeletesNode(), UInteger.class, null);
  }

  @Override
  public void setRemoteDeletes(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getRemoteDeletesNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getRemoteDropsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "RemoteDrops",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getRemoteDrops() {
    return ServerNodeSupport.read(this, getRemoteDropsNode(), UInteger.class, null);
  }

  @Override
  public void setRemoteDrops(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getRemoteDropsNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getRemoteInsertsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "RemoteInserts",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getRemoteInserts() {
    return ServerNodeSupport.read(this, getRemoteInsertsNode(), UInteger.class, null);
  }

  @Override
  public void setRemoteInserts(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getRemoteInsertsNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getLastChangeTimeNode();
    getRemoteAgeoutsNode();
    getRemoteDeletesNode();
    getRemoteDropsNode();
    getRemoteInsertsNode();
  }
}
