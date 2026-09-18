package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionSecurityDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link SessionSecurityDiagnosticsType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.16">Model
 *     documentation</a>
 */
public class SessionSecurityDiagnosticsTypeNode extends BaseDataVariableTypeNode
    implements SessionSecurityDiagnosticsType {
  public SessionSecurityDiagnosticsTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger @Nullable [] arrayDimensions) {
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

  public SessionSecurityDiagnosticsTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger @Nullable [] arrayDimensions,
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

  @Override
  public BaseDataVariableTypeNode getAuthenticationMechanismNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "AuthenticationMechanism",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable String getAuthenticationMechanism() {
    return ServerNodeSupport.read(this, getAuthenticationMechanismNode(), String.class, null);
  }

  @Override
  public void setAuthenticationMechanism(@Nullable String value) {
    ServerNodeSupport.write(this, getAuthenticationMechanismNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getClientCertificateNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ClientCertificate",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 15L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ByteString getClientCertificate() {
    return ServerNodeSupport.read(this, getClientCertificateNode(), ByteString.class, null);
  }

  @Override
  public void setClientCertificate(@Nullable ByteString value) {
    ServerNodeSupport.write(this, getClientCertificateNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getClientUserIdHistoryNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ClientUserIdHistory",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable String @Nullable [] getClientUserIdHistory() {
    return ServerNodeSupport.readArray(this, getClientUserIdHistoryNode(), String.class, null);
  }

  @Override
  public void setClientUserIdHistory(@Nullable String @Nullable [] value) {
    ServerNodeSupport.write(this, getClientUserIdHistoryNode(), value, true, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getClientUserIdOfSessionNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ClientUserIdOfSession",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable String getClientUserIdOfSession() {
    return ServerNodeSupport.read(this, getClientUserIdOfSessionNode(), String.class, null);
  }

  @Override
  public void setClientUserIdOfSession(@Nullable String value) {
    ServerNodeSupport.write(this, getClientUserIdOfSessionNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getEncodingNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Encoding",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable String getEncoding() {
    return ServerNodeSupport.read(this, getEncodingNode(), String.class, null);
  }

  @Override
  public void setEncoding(@Nullable String value) {
    ServerNodeSupport.write(this, getEncodingNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getSecurityModeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SecurityMode",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 302L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable MessageSecurityMode getSecurityMode() {
    return ServerNodeSupport.read(
        this, getSecurityModeNode(), MessageSecurityMode.class, MessageSecurityMode::from);
  }

  @Override
  public void setSecurityMode(@Nullable MessageSecurityMode value) {
    ServerNodeSupport.write(this, getSecurityModeNode(), value, false, true, false);
  }

  @Override
  public BaseDataVariableTypeNode getSecurityPolicyUriNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SecurityPolicyUri",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable String getSecurityPolicyUri() {
    return ServerNodeSupport.read(this, getSecurityPolicyUriNode(), String.class, null);
  }

  @Override
  public void setSecurityPolicyUri(@Nullable String value) {
    ServerNodeSupport.write(this, getSecurityPolicyUriNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getSessionIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SessionId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable NodeId getSessionId() {
    return ServerNodeSupport.read(this, getSessionIdNode(), NodeId.class, null);
  }

  @Override
  public void setSessionId(@Nullable NodeId value) {
    ServerNodeSupport.write(this, getSessionIdNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getTransportProtocolNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "TransportProtocol",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable String getTransportProtocol() {
    return ServerNodeSupport.read(this, getTransportProtocolNode(), String.class, null);
  }

  @Override
  public void setTransportProtocol(@Nullable String value) {
    ServerNodeSupport.write(this, getTransportProtocolNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getAuthenticationMechanismNode();
    getClientCertificateNode();
    getClientUserIdHistoryNode();
    getClientUserIdOfSessionNode();
    getEncodingNode();
    getSecurityModeNode();
    getSecurityPolicyUriNode();
    getSessionIdNode();
    getTransportProtocolNode();
  }

  @Override
  public @Nullable SessionSecurityDiagnosticsDataType getTypedValue() {
    return ServerNodeSupport.read(this, this, SessionSecurityDiagnosticsDataType.class, null);
  }

  @Override
  public void setTypedValue(@Nullable SessionSecurityDiagnosticsDataType value) {
    ServerNodeSupport.write(this, this, value, false, false, true);
  }
}
