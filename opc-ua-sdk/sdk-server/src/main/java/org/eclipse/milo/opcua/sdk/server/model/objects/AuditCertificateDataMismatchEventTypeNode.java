/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class AuditCertificateDataMismatchEventTypeNode extends AuditCertificateEventTypeNode
    implements AuditCertificateDataMismatchEventType {
  public AuditCertificateDataMismatchEventTypeNode(
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

  public AuditCertificateDataMismatchEventTypeNode(
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
  public PropertyTypeNode getInvalidHostnameNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditCertificateDataMismatchEventType.INVALID_HOSTNAME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getInvalidHostname() {
    return getProperty(AuditCertificateDataMismatchEventType.INVALID_HOSTNAME).orElse(null);
  }

  @Override
  public void setInvalidHostname(String value) {
    setProperty(AuditCertificateDataMismatchEventType.INVALID_HOSTNAME, value);
  }

  @Override
  public PropertyTypeNode getInvalidUriNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditCertificateDataMismatchEventType.INVALID_URI);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getInvalidUri() {
    return getProperty(AuditCertificateDataMismatchEventType.INVALID_URI).orElse(null);
  }

  @Override
  public void setInvalidUri(String value) {
    setProperty(AuditCertificateDataMismatchEventType.INVALID_URI, value);
  }
}
