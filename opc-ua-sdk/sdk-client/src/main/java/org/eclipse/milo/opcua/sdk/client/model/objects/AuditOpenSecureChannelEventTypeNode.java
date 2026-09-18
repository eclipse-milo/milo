package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.SecurityTokenRequestType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link AuditOpenSecureChannelEventType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.6">Model
 *     documentation</a>
 */
public class AuditOpenSecureChannelEventTypeNode extends AuditChannelEventTypeNode
    implements AuditOpenSecureChannelEventType {
  public AuditOpenSecureChannelEventTypeNode(
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
      UByte eventNotifier) {
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
        eventNotifier);
  }

  @Override
  public PropertyTypeNode getRequestTypeNode() throws UaException {
    return ClientNodeSupport.await(getRequestTypeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getRequestTypeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "RequestType",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable SecurityTokenRequestType readRequestType() throws UaException {
    return ClientNodeSupport.await(readRequestTypeAsync());
  }

  @Override
  public void writeRequestType(@Nullable SecurityTokenRequestType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeRequestTypeAsync(value)),
        "http://opcfoundation.org/UA/}RequestType");
  }

  @Override
  public CompletableFuture<? extends @Nullable SecurityTokenRequestType> readRequestTypeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getRequestTypeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}RequestType",
                            true,
                            SecurityTokenRequestType.class,
                            -1,
                            SecurityTokenRequestType::from)),
                v -> CompletableFuture.completedFuture((@Nullable SecurityTokenRequestType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeRequestTypeAsync(
      @Nullable SecurityTokenRequestType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getRequestTypeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}RequestType",
                        value,
                        SecurityTokenRequestType.class,
                        -1,
                        SecurityTokenRequestType::from)));
  }

  @Override
  public PropertyTypeNode getSecurityModeNode() throws UaException {
    return ClientNodeSupport.await(getSecurityModeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSecurityModeNodeAsync() {
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
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
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
  public PropertyTypeNode getClientCertificateNode() throws UaException {
    return ClientNodeSupport.await(getClientCertificateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getClientCertificateNodeAsync() {
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
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
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
  public PropertyTypeNode getRequestedLifetimeNode() throws UaException {
    return ClientNodeSupport.await(getRequestedLifetimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getRequestedLifetimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "RequestedLifetime",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Double readRequestedLifetime() throws UaException {
    return ClientNodeSupport.await(readRequestedLifetimeAsync());
  }

  @Override
  public void writeRequestedLifetime(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeRequestedLifetimeAsync(value)),
        "http://opcfoundation.org/UA/}RequestedLifetime");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readRequestedLifetimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getRequestedLifetimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}RequestedLifetime",
                            true,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeRequestedLifetimeAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getRequestedLifetimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}RequestedLifetime",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getSecurityPolicyUriNode() throws UaException {
    return ClientNodeSupport.await(getSecurityPolicyUriNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSecurityPolicyUriNodeAsync() {
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
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
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
  public @Nullable PropertyTypeNode getCertificateErrorEventIdNode() throws UaException {
    return ClientNodeSupport.await(getCertificateErrorEventIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getCertificateErrorEventIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CertificateErrorEventId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable ByteString readCertificateErrorEventId() throws UaException {
    return ClientNodeSupport.await(readCertificateErrorEventIdAsync());
  }

  @Override
  public void writeCertificateErrorEventId(@Nullable ByteString value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCertificateErrorEventIdAsync(value)),
        "http://opcfoundation.org/UA/}CertificateErrorEventId");
  }

  @Override
  public CompletableFuture<? extends @Nullable ByteString> readCertificateErrorEventIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCertificateErrorEventIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CertificateErrorEventId",
                            false,
                            ByteString.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ByteString) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCertificateErrorEventIdAsync(
      @Nullable ByteString value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCertificateErrorEventIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CertificateErrorEventId",
                        value,
                        ByteString.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getClientCertificateThumbprintNode() throws UaException {
    return ClientNodeSupport.await(getClientCertificateThumbprintNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getClientCertificateThumbprintNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ClientCertificateThumbprint",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readClientCertificateThumbprint() throws UaException {
    return ClientNodeSupport.await(readClientCertificateThumbprintAsync());
  }

  @Override
  public void writeClientCertificateThumbprint(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeClientCertificateThumbprintAsync(value)),
        "http://opcfoundation.org/UA/}ClientCertificateThumbprint");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readClientCertificateThumbprintAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getClientCertificateThumbprintNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ClientCertificateThumbprint",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeClientCertificateThumbprintAsync(
      @Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getClientCertificateThumbprintNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ClientCertificateThumbprint",
                        value,
                        String.class,
                        -1,
                        null)));
  }
}
