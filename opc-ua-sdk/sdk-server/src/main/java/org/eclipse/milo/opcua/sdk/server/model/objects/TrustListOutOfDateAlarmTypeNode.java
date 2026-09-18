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
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link TrustListOutOfDateAlarmType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.11">Model
 *     documentation</a>
 */
public class TrustListOutOfDateAlarmTypeNode extends SystemOffNormalAlarmTypeNode
    implements TrustListOutOfDateAlarmType {
  public TrustListOutOfDateAlarmTypeNode(
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

  public TrustListOutOfDateAlarmTypeNode(
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
  public PropertyTypeNode getLastUpdateTimeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LastUpdateTime",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable DateTime getLastUpdateTime() {
    return ServerNodeSupport.read(this, getLastUpdateTimeNode(), DateTime.class, null);
  }

  @Override
  public void setLastUpdateTime(@Nullable DateTime value) {
    ServerNodeSupport.write(this, getLastUpdateTimeNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getTrustListIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "TrustListId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable NodeId getTrustListId() {
    return ServerNodeSupport.read(this, getTrustListIdNode(), NodeId.class, null);
  }

  @Override
  public void setTrustListId(@Nullable NodeId value) {
    ServerNodeSupport.write(this, getTrustListIdNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getUpdateFrequencyNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "UpdateFrequency",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getUpdateFrequency() {
    return ServerNodeSupport.read(this, getUpdateFrequencyNode(), Double.class, null);
  }

  @Override
  public void setUpdateFrequency(@Nullable Double value) {
    ServerNodeSupport.write(this, getUpdateFrequencyNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getLastUpdateTimeNode();
    getTrustListIdNode();
    getUpdateFrequencyNode();
  }

  @Override
  public void setMethods(TrustListOutOfDateAlarmType.@Nullable Methods methods) {
    setAcknowledgeHandler(methods == null ? null : methods::acknowledge);
    setAddCommentHandler(methods == null ? null : methods::addComment);
    if (getConfirmMethodNode() != null) {
      setConfirmHandler(methods == null ? null : methods::confirm);
    }
    setDisableHandler(methods == null ? null : methods::disable);
    setEnableHandler(methods == null ? null : methods::enable);
    if (getGetGroupMembershipsMethodNode() != null) {
      setGetGroupMembershipsHandler(methods == null ? null : methods::getGroupMemberships);
    }
    if (getPlaceInServiceMethodNode() != null) {
      setPlaceInServiceHandler(methods == null ? null : methods::placeInService);
    }
    if (getPlaceInService2MethodNode() != null) {
      setPlaceInService2Handler(methods == null ? null : methods::placeInService2);
    }
    if (getRemoveFromServiceMethodNode() != null) {
      setRemoveFromServiceHandler(methods == null ? null : methods::removeFromService);
    }
    if (getRemoveFromService2MethodNode() != null) {
      setRemoveFromService2Handler(methods == null ? null : methods::removeFromService2);
    }
    if (getResetMethodNode() != null) {
      setResetHandler(methods == null ? null : methods::reset);
    }
    if (getReset2MethodNode() != null) {
      setReset2Handler(methods == null ? null : methods::reset2);
    }
    if (getSilenceMethodNode() != null) {
      setSilenceHandler(methods == null ? null : methods::silence);
    }
    if (getSuppressMethodNode() != null) {
      setSuppressHandler(methods == null ? null : methods::suppress);
    }
    if (getSuppress2MethodNode() != null) {
      setSuppress2Handler(methods == null ? null : methods::suppress2);
    }
    if (getUnsuppressMethodNode() != null) {
      setUnsuppressHandler(methods == null ? null : methods::unsuppress);
    }
    if (getUnsuppress2MethodNode() != null) {
      setUnsuppress2Handler(methods == null ? null : methods::unsuppress2);
    }
  }
}
