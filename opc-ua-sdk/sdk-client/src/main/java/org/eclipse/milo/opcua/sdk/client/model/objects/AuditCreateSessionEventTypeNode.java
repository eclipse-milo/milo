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
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link AuditCreateSessionEventType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.8">Model
 *     documentation</a>
 */
public class AuditCreateSessionEventTypeNode extends AuditSessionEventTypeNode
    implements AuditCreateSessionEventType {
  public AuditCreateSessionEventTypeNode(
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
  public PropertyTypeNode getSecureChannelIdNode() throws UaException {
    return ClientNodeSupport.await(getSecureChannelIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSecureChannelIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SecureChannelId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readSecureChannelId() throws UaException {
    return ClientNodeSupport.await(readSecureChannelIdAsync());
  }

  @Override
  public void writeSecureChannelId(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSecureChannelIdAsync(value)),
        "http://opcfoundation.org/UA/}SecureChannelId");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readSecureChannelIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSecureChannelIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SecureChannelId",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSecureChannelIdAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSecureChannelIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SecureChannelId",
                        value,
                        String.class,
                        -1,
                        null)));
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
  public PropertyTypeNode getRevisedSessionTimeoutNode() throws UaException {
    return ClientNodeSupport.await(getRevisedSessionTimeoutNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getRevisedSessionTimeoutNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "RevisedSessionTimeout",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Double readRevisedSessionTimeout() throws UaException {
    return ClientNodeSupport.await(readRevisedSessionTimeoutAsync());
  }

  @Override
  public void writeRevisedSessionTimeout(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeRevisedSessionTimeoutAsync(value)),
        "http://opcfoundation.org/UA/}RevisedSessionTimeout");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readRevisedSessionTimeoutAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getRevisedSessionTimeoutNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}RevisedSessionTimeout",
                            true,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeRevisedSessionTimeoutAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getRevisedSessionTimeoutNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}RevisedSessionTimeout",
                        value,
                        Double.class,
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
