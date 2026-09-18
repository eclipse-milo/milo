package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link LimitAlarmType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.18">Model
 *     documentation</a>
 */
public class LimitAlarmTypeNode extends AlarmConditionTypeNode implements LimitAlarmType {
  public LimitAlarmTypeNode(
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

  public LimitAlarmTypeNode(
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
  public @Nullable PropertyTypeNode getBaseHighHighLimitNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "BaseHighHighLimit",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getBaseHighHighLimit() {
    return ServerNodeSupport.read(this, getBaseHighHighLimitNode(), Double.class, null);
  }

  @Override
  public void setBaseHighHighLimit(@Nullable Double value) {
    ServerNodeSupport.write(
        this,
        getBaseHighHighLimitNode(),
        Namespaces.OPC_UA,
        "BaseHighHighLimit",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getBaseHighLimitNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "BaseHighLimit",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getBaseHighLimit() {
    return ServerNodeSupport.read(this, getBaseHighLimitNode(), Double.class, null);
  }

  @Override
  public void setBaseHighLimit(@Nullable Double value) {
    ServerNodeSupport.write(
        this,
        getBaseHighLimitNode(),
        Namespaces.OPC_UA,
        "BaseHighLimit",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getBaseLowLimitNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "BaseLowLimit",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getBaseLowLimit() {
    return ServerNodeSupport.read(this, getBaseLowLimitNode(), Double.class, null);
  }

  @Override
  public void setBaseLowLimit(@Nullable Double value) {
    ServerNodeSupport.write(
        this, getBaseLowLimitNode(), Namespaces.OPC_UA, "BaseLowLimit", value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getBaseLowLowLimitNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "BaseLowLowLimit",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getBaseLowLowLimit() {
    return ServerNodeSupport.read(this, getBaseLowLowLimitNode(), Double.class, null);
  }

  @Override
  public void setBaseLowLowLimit(@Nullable Double value) {
    ServerNodeSupport.write(
        this,
        getBaseLowLowLimitNode(),
        Namespaces.OPC_UA,
        "BaseLowLowLimit",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getHighDeadbandNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "HighDeadband",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getHighDeadband() {
    return ServerNodeSupport.read(this, getHighDeadbandNode(), Double.class, null);
  }

  @Override
  public void setHighDeadband(@Nullable Double value) {
    ServerNodeSupport.write(
        this, getHighDeadbandNode(), Namespaces.OPC_UA, "HighDeadband", value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getHighHighDeadbandNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "HighHighDeadband",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getHighHighDeadband() {
    return ServerNodeSupport.read(this, getHighHighDeadbandNode(), Double.class, null);
  }

  @Override
  public void setHighHighDeadband(@Nullable Double value) {
    ServerNodeSupport.write(
        this,
        getHighHighDeadbandNode(),
        Namespaces.OPC_UA,
        "HighHighDeadband",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getHighHighLimitNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "HighHighLimit",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getHighHighLimit() {
    return ServerNodeSupport.read(this, getHighHighLimitNode(), Double.class, null);
  }

  @Override
  public void setHighHighLimit(@Nullable Double value) {
    ServerNodeSupport.write(
        this,
        getHighHighLimitNode(),
        Namespaces.OPC_UA,
        "HighHighLimit",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getHighLimitNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "HighLimit",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getHighLimit() {
    return ServerNodeSupport.read(this, getHighLimitNode(), Double.class, null);
  }

  @Override
  public void setHighLimit(@Nullable Double value) {
    ServerNodeSupport.write(
        this, getHighLimitNode(), Namespaces.OPC_UA, "HighLimit", value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getLowDeadbandNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "LowDeadband",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getLowDeadband() {
    return ServerNodeSupport.read(this, getLowDeadbandNode(), Double.class, null);
  }

  @Override
  public void setLowDeadband(@Nullable Double value) {
    ServerNodeSupport.write(
        this, getLowDeadbandNode(), Namespaces.OPC_UA, "LowDeadband", value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getLowLimitNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "LowLimit",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getLowLimit() {
    return ServerNodeSupport.read(this, getLowLimitNode(), Double.class, null);
  }

  @Override
  public void setLowLimit(@Nullable Double value) {
    ServerNodeSupport.write(
        this, getLowLimitNode(), Namespaces.OPC_UA, "LowLimit", value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getLowLowDeadbandNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "LowLowDeadband",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getLowLowDeadband() {
    return ServerNodeSupport.read(this, getLowLowDeadbandNode(), Double.class, null);
  }

  @Override
  public void setLowLowDeadband(@Nullable Double value) {
    ServerNodeSupport.write(
        this,
        getLowLowDeadbandNode(),
        Namespaces.OPC_UA,
        "LowLowDeadband",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getLowLowLimitNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "LowLowLimit",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getLowLowLimit() {
    return ServerNodeSupport.read(this, getLowLowLimitNode(), Double.class, null);
  }

  @Override
  public void setLowLowLimit(@Nullable Double value) {
    ServerNodeSupport.write(
        this, getLowLowLimitNode(), Namespaces.OPC_UA, "LowLowLimit", value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getSeverityHighNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SeverityHigh",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UShort getSeverityHigh() {
    return ServerNodeSupport.read(this, getSeverityHighNode(), UShort.class, null);
  }

  @Override
  public void setSeverityHigh(@Nullable UShort value) {
    ServerNodeSupport.write(
        this, getSeverityHighNode(), Namespaces.OPC_UA, "SeverityHigh", value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getSeverityHighHighNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SeverityHighHigh",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UShort getSeverityHighHigh() {
    return ServerNodeSupport.read(this, getSeverityHighHighNode(), UShort.class, null);
  }

  @Override
  public void setSeverityHighHigh(@Nullable UShort value) {
    ServerNodeSupport.write(
        this,
        getSeverityHighHighNode(),
        Namespaces.OPC_UA,
        "SeverityHighHigh",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getSeverityLowNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SeverityLow",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UShort getSeverityLow() {
    return ServerNodeSupport.read(this, getSeverityLowNode(), UShort.class, null);
  }

  @Override
  public void setSeverityLow(@Nullable UShort value) {
    ServerNodeSupport.write(
        this, getSeverityLowNode(), Namespaces.OPC_UA, "SeverityLow", value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getSeverityLowLowNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SeverityLowLow",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UShort getSeverityLowLow() {
    return ServerNodeSupport.read(this, getSeverityLowLowNode(), UShort.class, null);
  }

  @Override
  public void setSeverityLowLow(@Nullable UShort value) {
    ServerNodeSupport.write(
        this,
        getSeverityLowLowNode(),
        Namespaces.OPC_UA,
        "SeverityLowLow",
        value,
        false,
        false,
        false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getBaseHighHighLimitNode();
    getBaseHighLimitNode();
    getBaseLowLimitNode();
    getBaseLowLowLimitNode();
    getHighDeadbandNode();
    getHighHighDeadbandNode();
    getHighHighLimitNode();
    getHighLimitNode();
    getLowDeadbandNode();
    getLowLimitNode();
    getLowLowDeadbandNode();
    getLowLowLimitNode();
    getSeverityHighNode();
    getSeverityHighHighNode();
    getSeverityLowNode();
    getSeverityLowLowNode();
  }

  @Override
  public void setMethods(LimitAlarmType.@Nullable Methods methods) {
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
