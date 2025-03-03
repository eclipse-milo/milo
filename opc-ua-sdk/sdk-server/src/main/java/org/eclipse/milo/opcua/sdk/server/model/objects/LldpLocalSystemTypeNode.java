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
import org.eclipse.milo.opcua.stack.core.types.enumerated.ChassisIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpSystemCapabilitiesMap;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class LldpLocalSystemTypeNode extends BaseObjectTypeNode implements LldpLocalSystemType {
  public LldpLocalSystemTypeNode(
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

  public LldpLocalSystemTypeNode(
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
  public PropertyTypeNode getChassisIdSubtypeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(LldpLocalSystemType.CHASSIS_ID_SUBTYPE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public ChassisIdSubtype getChassisIdSubtype() {
    return getProperty(LldpLocalSystemType.CHASSIS_ID_SUBTYPE).orElse(null);
  }

  @Override
  public void setChassisIdSubtype(ChassisIdSubtype value) {
    setProperty(LldpLocalSystemType.CHASSIS_ID_SUBTYPE, value);
  }

  @Override
  public PropertyTypeNode getChassisIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(LldpLocalSystemType.CHASSIS_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getChassisId() {
    return getProperty(LldpLocalSystemType.CHASSIS_ID).orElse(null);
  }

  @Override
  public void setChassisId(String value) {
    setProperty(LldpLocalSystemType.CHASSIS_ID, value);
  }

  @Override
  public PropertyTypeNode getSystemNameNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(LldpLocalSystemType.SYSTEM_NAME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getSystemName() {
    return getProperty(LldpLocalSystemType.SYSTEM_NAME).orElse(null);
  }

  @Override
  public void setSystemName(String value) {
    setProperty(LldpLocalSystemType.SYSTEM_NAME, value);
  }

  @Override
  public PropertyTypeNode getSystemDescriptionNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(LldpLocalSystemType.SYSTEM_DESCRIPTION);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getSystemDescription() {
    return getProperty(LldpLocalSystemType.SYSTEM_DESCRIPTION).orElse(null);
  }

  @Override
  public void setSystemDescription(String value) {
    setProperty(LldpLocalSystemType.SYSTEM_DESCRIPTION, value);
  }

  @Override
  public PropertyTypeNode getSystemCapabilitiesSupportedNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(LldpLocalSystemType.SYSTEM_CAPABILITIES_SUPPORTED);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public LldpSystemCapabilitiesMap getSystemCapabilitiesSupported() {
    return getProperty(LldpLocalSystemType.SYSTEM_CAPABILITIES_SUPPORTED).orElse(null);
  }

  @Override
  public void setSystemCapabilitiesSupported(LldpSystemCapabilitiesMap value) {
    setProperty(LldpLocalSystemType.SYSTEM_CAPABILITIES_SUPPORTED, value);
  }

  @Override
  public PropertyTypeNode getSystemCapabilitiesEnabledNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(LldpLocalSystemType.SYSTEM_CAPABILITIES_ENABLED);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public LldpSystemCapabilitiesMap getSystemCapabilitiesEnabled() {
    return getProperty(LldpLocalSystemType.SYSTEM_CAPABILITIES_ENABLED).orElse(null);
  }

  @Override
  public void setSystemCapabilitiesEnabled(LldpSystemCapabilitiesMap value) {
    setProperty(LldpLocalSystemType.SYSTEM_CAPABILITIES_ENABLED, value);
  }
}
