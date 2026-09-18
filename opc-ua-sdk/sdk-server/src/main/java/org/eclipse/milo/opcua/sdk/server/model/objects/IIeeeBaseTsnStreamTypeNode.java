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
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnStreamState;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link IIeeeBaseTsnStreamType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.7">Model
 *     documentation</a>
 */
public class IIeeeBaseTsnStreamTypeNode extends BaseInterfaceTypeNode
    implements IIeeeBaseTsnStreamType {
  public IIeeeBaseTsnStreamTypeNode(
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

  public IIeeeBaseTsnStreamTypeNode(
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
  public @Nullable BaseDataVariableTypeNode getAccumulatedLatencyNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "AccumulatedLatency",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getAccumulatedLatency() {
    return ServerNodeSupport.read(this, getAccumulatedLatencyNode(), UInteger.class, null);
  }

  @Override
  public void setAccumulatedLatency(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getAccumulatedLatencyNode(),
        Namespaces.OPC_UA,
        "AccumulatedLatency",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getSrClassIdNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SrClassId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 3L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UByte getSrClassId() {
    return ServerNodeSupport.read(this, getSrClassIdNode(), UByte.class, null);
  }

  @Override
  public void setSrClassId(@Nullable UByte value) {
    ServerNodeSupport.write(
        this, getSrClassIdNode(), Namespaces.OPC_UA, "SrClassId", value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getStateNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "State",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 24220L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable TsnStreamState getState() {
    return ServerNodeSupport.read(this, getStateNode(), TsnStreamState.class, TsnStreamState::from);
  }

  @Override
  public void setState(@Nullable TsnStreamState value) {
    ServerNodeSupport.write(this, getStateNode(), value, false, true, false);
  }

  @Override
  public BaseDataVariableTypeNode getStreamIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "StreamId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 3L),
        1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public UByte @Nullable [] getStreamId() {
    return ServerNodeSupport.readArray(this, getStreamIdNode(), UByte.class, null);
  }

  @Override
  public void setStreamId(UByte @Nullable [] value) {
    ServerNodeSupport.write(this, getStreamIdNode(), value, true, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getStreamNameNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "StreamName",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable String getStreamName() {
    return ServerNodeSupport.read(this, getStreamNameNode(), String.class, null);
  }

  @Override
  public void setStreamName(@Nullable String value) {
    ServerNodeSupport.write(this, getStreamNameNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getAccumulatedLatencyNode();
    getSrClassIdNode();
    getStateNode();
    getStreamIdNode();
    getStreamNameNode();
  }
}
