/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class CertificateExpirationAlarmTypeNode extends SystemOffNormalAlarmTypeNode
    implements CertificateExpirationAlarmType {
  public CertificateExpirationAlarmTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions,
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

  public CertificateExpirationAlarmTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions) {
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

  @Override
  public PropertyTypeNode getExpirationDateNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(CertificateExpirationAlarmType.EXPIRATION_DATE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DateTime getExpirationDate() {
    return getProperty(CertificateExpirationAlarmType.EXPIRATION_DATE).orElse(null);
  }

  @Override
  public void setExpirationDate(DateTime value) {
    setProperty(CertificateExpirationAlarmType.EXPIRATION_DATE, value);
  }

  @Override
  public PropertyTypeNode getExpirationLimitNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(CertificateExpirationAlarmType.EXPIRATION_LIMIT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getExpirationLimit() {
    return getProperty(CertificateExpirationAlarmType.EXPIRATION_LIMIT).orElse(null);
  }

  @Override
  public void setExpirationLimit(Double value) {
    setProperty(CertificateExpirationAlarmType.EXPIRATION_LIMIT, value);
  }

  @Override
  public PropertyTypeNode getCertificateTypeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(CertificateExpirationAlarmType.CERTIFICATE_TYPE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId getCertificateType() {
    return getProperty(CertificateExpirationAlarmType.CERTIFICATE_TYPE).orElse(null);
  }

  @Override
  public void setCertificateType(NodeId value) {
    setProperty(CertificateExpirationAlarmType.CERTIFICATE_TYPE, value);
  }

  @Override
  public PropertyTypeNode getCertificateNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(CertificateExpirationAlarmType.CERTIFICATE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public ByteString getCertificate() {
    return getProperty(CertificateExpirationAlarmType.CERTIFICATE).orElse(null);
  }

  @Override
  public void setCertificate(ByteString value) {
    setProperty(CertificateExpirationAlarmType.CERTIFICATE, value);
  }
}
