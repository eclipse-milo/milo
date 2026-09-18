package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
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
      OpcUaClient client,
      NodeId nodeId,
      NodeClass nodeClass,
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
      Boolean historizing,
      @Nullable AccessLevelExType accessLevelEx) {
    super(
        client,
        nodeId,
        nodeClass,
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
  public UaVariableNode getSecurityModeNode() throws UaException {
    return ClientNodeSupport.await(getSecurityModeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getSecurityModeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SecurityMode",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable MessageSecurityMode readSecurityMode() throws UaException {
    return ClientNodeSupport.await(readSecurityModeAsync());
  }

  @Override
  public void writeSecurityMode(@Nullable MessageSecurityMode value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSecurityModeAsync(value)),
        "http://opcfoundation.org/UA/}SecurityMode");
  }

  @Override
  public CompletableFuture<? extends @Nullable MessageSecurityMode> readSecurityModeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSecurityModeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SecurityMode",
                            true,
                            MessageSecurityMode.class,
                            -1,
                            MessageSecurityMode::from)),
                v -> CompletableFuture.completedFuture((@Nullable MessageSecurityMode) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSecurityModeAsync(@Nullable MessageSecurityMode value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSecurityModeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SecurityMode",
                        value,
                        MessageSecurityMode.class,
                        -1,
                        MessageSecurityMode::from)));
  }

  @Override
  public UaVariableNode getClientCertificateNode() throws UaException {
    return ClientNodeSupport.await(getClientCertificateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getClientCertificateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ClientCertificate",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ByteString readClientCertificate() throws UaException {
    return ClientNodeSupport.await(readClientCertificateAsync());
  }

  @Override
  public void writeClientCertificate(@Nullable ByteString value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeClientCertificateAsync(value)),
        "http://opcfoundation.org/UA/}ClientCertificate");
  }

  @Override
  public CompletableFuture<? extends @Nullable ByteString> readClientCertificateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getClientCertificateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ClientCertificate",
                            true,
                            ByteString.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ByteString) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeClientCertificateAsync(@Nullable ByteString value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getClientCertificateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ClientCertificate",
                        value,
                        ByteString.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getSecurityPolicyUriNode() throws UaException {
    return ClientNodeSupport.await(getSecurityPolicyUriNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getSecurityPolicyUriNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SecurityPolicyUri",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable String readSecurityPolicyUri() throws UaException {
    return ClientNodeSupport.await(readSecurityPolicyUriAsync());
  }

  @Override
  public void writeSecurityPolicyUri(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSecurityPolicyUriAsync(value)),
        "http://opcfoundation.org/UA/}SecurityPolicyUri");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readSecurityPolicyUriAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSecurityPolicyUriNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SecurityPolicyUri",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSecurityPolicyUriAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSecurityPolicyUriNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SecurityPolicyUri",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getTransportProtocolNode() throws UaException {
    return ClientNodeSupport.await(getTransportProtocolNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getTransportProtocolNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "TransportProtocol",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable String readTransportProtocol() throws UaException {
    return ClientNodeSupport.await(readTransportProtocolAsync());
  }

  @Override
  public void writeTransportProtocol(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeTransportProtocolAsync(value)),
        "http://opcfoundation.org/UA/}TransportProtocol");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readTransportProtocolAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getTransportProtocolNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}TransportProtocol",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTransportProtocolAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getTransportProtocolNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}TransportProtocol",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getClientUserIdHistoryNode() throws UaException {
    return ClientNodeSupport.await(getClientUserIdHistoryNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getClientUserIdHistoryNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ClientUserIdHistory",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable String @Nullable [] readClientUserIdHistory() throws UaException {
    return ClientNodeSupport.await(readClientUserIdHistoryAsync());
  }

  @Override
  public void writeClientUserIdHistory(@Nullable String @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeClientUserIdHistoryAsync(value)),
        "http://opcfoundation.org/UA/}ClientUserIdHistory");
  }

  @Override
  public CompletableFuture<? extends @Nullable String @Nullable []> readClientUserIdHistoryAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getClientUserIdHistoryNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ClientUserIdHistory",
                            true,
                            String.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeClientUserIdHistoryAsync(
      @Nullable String @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getClientUserIdHistoryNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ClientUserIdHistory",
                        value,
                        String.class,
                        1,
                        null)));
  }

  @Override
  public UaVariableNode getClientUserIdOfSessionNode() throws UaException {
    return ClientNodeSupport.await(getClientUserIdOfSessionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getClientUserIdOfSessionNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ClientUserIdOfSession",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable String readClientUserIdOfSession() throws UaException {
    return ClientNodeSupport.await(readClientUserIdOfSessionAsync());
  }

  @Override
  public void writeClientUserIdOfSession(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeClientUserIdOfSessionAsync(value)),
        "http://opcfoundation.org/UA/}ClientUserIdOfSession");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readClientUserIdOfSessionAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getClientUserIdOfSessionNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ClientUserIdOfSession",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeClientUserIdOfSessionAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getClientUserIdOfSessionNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ClientUserIdOfSession",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getAuthenticationMechanismNode() throws UaException {
    return ClientNodeSupport.await(getAuthenticationMechanismNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getAuthenticationMechanismNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AuthenticationMechanism",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable String readAuthenticationMechanism() throws UaException {
    return ClientNodeSupport.await(readAuthenticationMechanismAsync());
  }

  @Override
  public void writeAuthenticationMechanism(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeAuthenticationMechanismAsync(value)),
        "http://opcfoundation.org/UA/}AuthenticationMechanism");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readAuthenticationMechanismAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getAuthenticationMechanismNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}AuthenticationMechanism",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeAuthenticationMechanismAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getAuthenticationMechanismNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}AuthenticationMechanism",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getEncodingNode() throws UaException {
    return ClientNodeSupport.await(getEncodingNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getEncodingNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Encoding",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable String readEncoding() throws UaException {
    return ClientNodeSupport.await(readEncodingAsync());
  }

  @Override
  public void writeEncoding(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeEncodingAsync(value)),
        "http://opcfoundation.org/UA/}Encoding");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readEncodingAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getEncodingNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Encoding",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeEncodingAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getEncodingNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Encoding",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getSessionIdNode() throws UaException {
    return ClientNodeSupport.await(getSessionIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getSessionIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SessionId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable NodeId readSessionId() throws UaException {
    return ClientNodeSupport.await(readSessionIdAsync());
  }

  @Override
  public void writeSessionId(@Nullable NodeId value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSessionIdAsync(value)),
        "http://opcfoundation.org/UA/}SessionId");
  }

  @Override
  public CompletableFuture<? extends @Nullable NodeId> readSessionIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSessionIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SessionId",
                            true,
                            NodeId.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable NodeId) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSessionIdAsync(@Nullable NodeId value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSessionIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SessionId",
                        value,
                        NodeId.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable SessionSecurityDiagnosticsDataType readTypedValue() throws UaException {
    return ClientNodeSupport.await(readTypedValueAsync());
  }

  @Override
  public void writeTypedValue(@Nullable SessionSecurityDiagnosticsDataType value)
      throws UaException {
    ClientNodeSupport.good(ClientNodeSupport.await(writeTypedValueAsync(value)), "Value");
  }

  @Override
  public CompletableFuture<? extends @Nullable SessionSecurityDiagnosticsDataType>
      readTypedValueAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    CompletableFuture.completedFuture(this),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "Value",
                            true,
                            SessionSecurityDiagnosticsDataType.class,
                            -1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable SessionSecurityDiagnosticsDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTypedValueAsync(
      @Nullable SessionSecurityDiagnosticsDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "Value",
                        value,
                        SessionSecurityDiagnosticsDataType.class,
                        -1,
                        null)));
  }
}
