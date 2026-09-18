package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
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
 * Node implementation of {@link CertificateExpirationAlarmType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.24/#5.8.24.7">Model
 *     documentation</a>
 */
public class CertificateExpirationAlarmTypeNode extends SystemOffNormalAlarmTypeNode
    implements CertificateExpirationAlarmType {
  public CertificateExpirationAlarmTypeNode(
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

  public CertificateExpirationAlarmTypeNode(
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
  public PropertyTypeNode getCertificateNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Certificate",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 15L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable ByteString getCertificate() {
    return ServerNodeSupport.read(this, getCertificateNode(), ByteString.class, null);
  }

  @Override
  public void setCertificate(@Nullable ByteString value) {
    ServerNodeSupport.write(this, getCertificateNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getCertificateTypeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CertificateType",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable NodeId getCertificateType() {
    return ServerNodeSupport.read(this, getCertificateTypeNode(), NodeId.class, null);
  }

  @Override
  public void setCertificateType(@Nullable NodeId value) {
    ServerNodeSupport.write(this, getCertificateTypeNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getExpirationDateNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ExpirationDate",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 13L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable DateTime getExpirationDate() {
    return ServerNodeSupport.read(this, getExpirationDateNode(), DateTime.class, null);
  }

  @Override
  public void setExpirationDate(@Nullable DateTime value) {
    ServerNodeSupport.write(this, getExpirationDateNode(), value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getExpirationLimitNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ExpirationLimit",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getExpirationLimit() {
    return ServerNodeSupport.read(this, getExpirationLimitNode(), Double.class, null);
  }

  @Override
  public void setExpirationLimit(@Nullable Double value) {
    ServerNodeSupport.write(
        this,
        getExpirationLimitNode(),
        Namespaces.OPC_UA,
        "ExpirationLimit",
        value,
        false,
        false,
        false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getCertificateNode();
    getCertificateTypeNode();
    getExpirationDateNode();
    getExpirationLimitNode();
  }

  @Override
  public void setMethods(CertificateExpirationAlarmType.@Nullable Methods methods) {
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
