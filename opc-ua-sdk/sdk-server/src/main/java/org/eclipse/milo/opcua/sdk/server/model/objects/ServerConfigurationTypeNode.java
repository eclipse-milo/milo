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
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectNode;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class ServerConfigurationTypeNode extends BaseObjectTypeNode
    implements ServerConfigurationType {
  public ServerConfigurationTypeNode(
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

  public ServerConfigurationTypeNode(
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
    Optional<VariableNode> propertyNode = getPropertyNode(ServerConfigurationType.APPLICATION_URI);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getApplicationUri() {
    return getProperty(ServerConfigurationType.APPLICATION_URI).orElse(null);
  }

  @Override
  public void setApplicationUri(String value) {
    setProperty(ServerConfigurationType.APPLICATION_URI, value);
  }

  @Override
  public PropertyTypeNode getProductUriNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ServerConfigurationType.PRODUCT_URI);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getProductUri() {
    return getProperty(ServerConfigurationType.PRODUCT_URI).orElse(null);
  }

  @Override
  public void setProductUri(String value) {
    setProperty(ServerConfigurationType.PRODUCT_URI, value);
  }

  @Override
  public PropertyTypeNode getApplicationTypeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ServerConfigurationType.APPLICATION_TYPE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public ApplicationType getApplicationType() {
    return getProperty(ServerConfigurationType.APPLICATION_TYPE).orElse(null);
  }

  @Override
  public void setApplicationType(ApplicationType value) {
    setProperty(ServerConfigurationType.APPLICATION_TYPE, value);
  }

  @Override
  public PropertyTypeNode getServerCapabilitiesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ServerConfigurationType.SERVER_CAPABILITIES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String[] getServerCapabilities() {
    return getProperty(ServerConfigurationType.SERVER_CAPABILITIES).orElse(null);
  }

  @Override
  public void setServerCapabilities(String[] value) {
    setProperty(ServerConfigurationType.SERVER_CAPABILITIES, value);
  }

  @Override
  public PropertyTypeNode getSupportedPrivateKeyFormatsNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ServerConfigurationType.SUPPORTED_PRIVATE_KEY_FORMATS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String[] getSupportedPrivateKeyFormats() {
    return getProperty(ServerConfigurationType.SUPPORTED_PRIVATE_KEY_FORMATS).orElse(null);
  }

  @Override
  public void setSupportedPrivateKeyFormats(String[] value) {
    setProperty(ServerConfigurationType.SUPPORTED_PRIVATE_KEY_FORMATS, value);
  }

  @Override
  public PropertyTypeNode getMaxTrustListSizeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ServerConfigurationType.MAX_TRUST_LIST_SIZE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxTrustListSize() {
    return getProperty(ServerConfigurationType.MAX_TRUST_LIST_SIZE).orElse(null);
  }

  @Override
  public void setMaxTrustListSize(UInteger value) {
    setProperty(ServerConfigurationType.MAX_TRUST_LIST_SIZE, value);
  }

  @Override
  public PropertyTypeNode getMulticastDnsEnabledNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ServerConfigurationType.MULTICAST_DNS_ENABLED);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getMulticastDnsEnabled() {
    return getProperty(ServerConfigurationType.MULTICAST_DNS_ENABLED).orElse(null);
  }

  @Override
  public void setMulticastDnsEnabled(Boolean value) {
    setProperty(ServerConfigurationType.MULTICAST_DNS_ENABLED, value);
  }

  @Override
  public PropertyTypeNode getHasSecureElementNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ServerConfigurationType.HAS_SECURE_ELEMENT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getHasSecureElement() {
    return getProperty(ServerConfigurationType.HAS_SECURE_ELEMENT).orElse(null);
  }

  @Override
  public void setHasSecureElement(Boolean value) {
    setProperty(ServerConfigurationType.HAS_SECURE_ELEMENT, value);
  }

  @Override
  public CertificateGroupFolderTypeNode getCertificateGroupsNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "CertificateGroups");
    return (CertificateGroupFolderTypeNode) component.orElse(null);
  }

  @Override
  public UaMethodNode getUpdateCertificateMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "UpdateCertificate",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getGetCertificatesMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "GetCertificates",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getApplyChangesMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "ApplyChanges",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getCancelChangesMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "CancelChanges",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getCreateSigningRequestMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "CreateSigningRequest",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getGetRejectedListMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "GetRejectedList",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getResetToServerDefaultsMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "ResetToServerDefaults",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public TransactionDiagnosticsTypeNode getTransactionDiagnosticsNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "TransactionDiagnostics");
    return (TransactionDiagnosticsTypeNode) component.orElse(null);
  }
}
