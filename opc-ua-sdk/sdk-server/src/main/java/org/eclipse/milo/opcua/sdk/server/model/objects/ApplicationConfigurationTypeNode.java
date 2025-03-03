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
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class ApplicationConfigurationTypeNode extends ServerConfigurationTypeNode
    implements ApplicationConfigurationType {
  public ApplicationConfigurationTypeNode(
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

  public ApplicationConfigurationTypeNode(
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
  public PropertyTypeNode getApplicationUriNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ApplicationConfigurationType.APPLICATION_URI);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getApplicationUri() {
    return getProperty(ApplicationConfigurationType.APPLICATION_URI).orElse(null);
  }

  @Override
  public void setApplicationUri(String value) {
    setProperty(ApplicationConfigurationType.APPLICATION_URI, value);
  }

  @Override
  public PropertyTypeNode getProductUriNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ApplicationConfigurationType.PRODUCT_URI);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getProductUri() {
    return getProperty(ApplicationConfigurationType.PRODUCT_URI).orElse(null);
  }

  @Override
  public void setProductUri(String value) {
    setProperty(ApplicationConfigurationType.PRODUCT_URI, value);
  }

  @Override
  public PropertyTypeNode getApplicationTypeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ApplicationConfigurationType.APPLICATION_TYPE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public ApplicationType getApplicationType() {
    return getProperty(ApplicationConfigurationType.APPLICATION_TYPE).orElse(null);
  }

  @Override
  public void setApplicationType(ApplicationType value) {
    setProperty(ApplicationConfigurationType.APPLICATION_TYPE, value);
  }

  @Override
  public PropertyTypeNode getEnabledNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ApplicationConfigurationType.ENABLED);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getEnabled() {
    return getProperty(ApplicationConfigurationType.ENABLED).orElse(null);
  }

  @Override
  public void setEnabled(Boolean value) {
    setProperty(ApplicationConfigurationType.ENABLED, value);
  }
}
