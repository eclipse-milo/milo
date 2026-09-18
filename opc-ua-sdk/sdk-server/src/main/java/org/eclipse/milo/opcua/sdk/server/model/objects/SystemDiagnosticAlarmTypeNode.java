package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link SystemDiagnosticAlarmType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.24/#5.8.24.6">Model
 *     documentation</a>
 */
public class SystemDiagnosticAlarmTypeNode extends OffNormalAlarmTypeNode
    implements SystemDiagnosticAlarmType {
  public SystemDiagnosticAlarmTypeNode(
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

  public SystemDiagnosticAlarmTypeNode(
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
  public void setMethods(SystemDiagnosticAlarmType.@Nullable Methods methods) {
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
