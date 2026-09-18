package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableTypeNode;
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
 * Node implementation of {@link NonExclusiveLimitAlarmType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.20">Model
 *     documentation</a>
 */
public class NonExclusiveLimitAlarmTypeNode extends LimitAlarmTypeNode
    implements NonExclusiveLimitAlarmType {
  public NonExclusiveLimitAlarmTypeNode(
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

  public NonExclusiveLimitAlarmTypeNode(
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
  public @Nullable TwoStateVariableTypeNode getHighHighStateNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "HighHighState",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 8995L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        TwoStateVariableTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getHighHighState() {
    return ServerNodeSupport.read(this, getHighHighStateNode(), LocalizedText.class, null);
  }

  @Override
  public void setHighHighState(@Nullable LocalizedText value) {
    ServerNodeSupport.write(
        this,
        getHighHighStateNode(),
        Namespaces.OPC_UA,
        "HighHighState",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable TwoStateVariableTypeNode getHighStateNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "HighState",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 8995L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        TwoStateVariableTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getHighState() {
    return ServerNodeSupport.read(this, getHighStateNode(), LocalizedText.class, null);
  }

  @Override
  public void setHighState(@Nullable LocalizedText value) {
    ServerNodeSupport.write(
        this, getHighStateNode(), Namespaces.OPC_UA, "HighState", value, false, false, false);
  }

  @Override
  public @Nullable TwoStateVariableTypeNode getLowLowStateNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "LowLowState",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 8995L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        TwoStateVariableTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getLowLowState() {
    return ServerNodeSupport.read(this, getLowLowStateNode(), LocalizedText.class, null);
  }

  @Override
  public void setLowLowState(@Nullable LocalizedText value) {
    ServerNodeSupport.write(
        this, getLowLowStateNode(), Namespaces.OPC_UA, "LowLowState", value, false, false, false);
  }

  @Override
  public @Nullable TwoStateVariableTypeNode getLowStateNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "LowState",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 8995L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        TwoStateVariableTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getLowState() {
    return ServerNodeSupport.read(this, getLowStateNode(), LocalizedText.class, null);
  }

  @Override
  public void setLowState(@Nullable LocalizedText value) {
    ServerNodeSupport.write(
        this, getLowStateNode(), Namespaces.OPC_UA, "LowState", value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getHighHighStateNode();
    getHighStateNode();
    getLowLowStateNode();
    getLowStateNode();
  }

  @Override
  public void setMethods(NonExclusiveLimitAlarmType.@Nullable Methods methods) {
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
