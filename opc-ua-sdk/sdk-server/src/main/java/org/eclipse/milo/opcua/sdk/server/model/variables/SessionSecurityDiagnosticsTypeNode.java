/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class SessionSecurityDiagnosticsTypeNode extends BaseDataVariableTypeNode
    implements SessionSecurityDiagnosticsType {
  public SessionSecurityDiagnosticsTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger[] arrayDimensions,
      UByte accessLevel,
      UByte userAccessLevel,
      Double minimumSamplingInterval,
      boolean historizing,
      AccessLevelExType accessLevelEx) {
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
        value,
        dataType,
        valueRank,
        arrayDimensions,
        accessLevel,
        userAccessLevel,
        minimumSamplingInterval,
        historizing,
        accessLevelEx);
  }

  public SessionSecurityDiagnosticsTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger[] arrayDimensions) {
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
        value,
        dataType,
        valueRank,
        arrayDimensions);
  }

  @Override
  public BaseDataVariableTypeNode getSessionIdNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SessionId");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public NodeId getSessionId() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SessionId");
    return component.map(node -> (NodeId) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setSessionId(NodeId value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SessionId")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getClientUserIdOfSessionNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ClientUserIdOfSession");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public String getClientUserIdOfSession() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ClientUserIdOfSession");
    return component.map(node -> (String) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setClientUserIdOfSession(String value) {
    getVariableComponent("http://opcfoundation.org/UA/", "ClientUserIdOfSession")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getClientUserIdHistoryNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ClientUserIdHistory");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public String[] getClientUserIdHistory() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ClientUserIdHistory");
    return component.map(node -> (String[]) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setClientUserIdHistory(String[] value) {
    getVariableComponent("http://opcfoundation.org/UA/", "ClientUserIdHistory")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getAuthenticationMechanismNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "AuthenticationMechanism");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public String getAuthenticationMechanism() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "AuthenticationMechanism");
    return component.map(node -> (String) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setAuthenticationMechanism(String value) {
    getVariableComponent("http://opcfoundation.org/UA/", "AuthenticationMechanism")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getEncodingNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "Encoding");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public String getEncoding() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "Encoding");
    return component.map(node -> (String) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setEncoding(String value) {
    getVariableComponent("http://opcfoundation.org/UA/", "Encoding")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getTransportProtocolNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "TransportProtocol");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public String getTransportProtocol() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "TransportProtocol");
    return component.map(node -> (String) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setTransportProtocol(String value) {
    getVariableComponent("http://opcfoundation.org/UA/", "TransportProtocol")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getSecurityModeNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SecurityMode");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public MessageSecurityMode getSecurityMode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SecurityMode");
    return component
        .map(node -> (MessageSecurityMode) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setSecurityMode(MessageSecurityMode value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SecurityMode")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getSecurityPolicyUriNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SecurityPolicyUri");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public String getSecurityPolicyUri() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SecurityPolicyUri");
    return component.map(node -> (String) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setSecurityPolicyUri(String value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SecurityPolicyUri")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getClientCertificateNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ClientCertificate");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ByteString getClientCertificate() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ClientCertificate");
    return component.map(node -> (ByteString) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setClientCertificate(ByteString value) {
    getVariableComponent("http://opcfoundation.org/UA/", "ClientCertificate")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }
}
