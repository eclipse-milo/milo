package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaException;
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
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;
import org.eclipse.milo.opcua.stack.core.types.structured.UserIdentityToken;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link AuditActivateSessionEventType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.10">Model
 *     documentation</a>
 */
public class AuditActivateSessionEventTypeNode extends AuditSessionEventTypeNode
    implements AuditActivateSessionEventType {
  public AuditActivateSessionEventTypeNode(
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
  public @Nullable PropertyTypeNode getCurrentRoleIdsNode() throws UaException {
    return ClientNodeSupport.await(getCurrentRoleIdsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getCurrentRoleIdsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CurrentRoleIds",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public NodeId @Nullable [] readCurrentRoleIds() throws UaException {
    return ClientNodeSupport.await(readCurrentRoleIdsAsync());
  }

  @Override
  public void writeCurrentRoleIds(NodeId @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCurrentRoleIdsAsync(value)),
        "http://opcfoundation.org/UA/}CurrentRoleIds");
  }

  @Override
  public CompletableFuture<? extends NodeId @Nullable []> readCurrentRoleIdsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCurrentRoleIdsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CurrentRoleIds",
                            false,
                            NodeId.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((NodeId @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCurrentRoleIdsAsync(NodeId @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCurrentRoleIdsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CurrentRoleIds",
                        value,
                        NodeId.class,
                        1,
                        null)));
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
  public PropertyTypeNode getUserIdentityTokenNode() throws UaException {
    return ClientNodeSupport.await(getUserIdentityTokenNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUserIdentityTokenNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "UserIdentityToken",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UserIdentityToken readUserIdentityToken() throws UaException {
    return ClientNodeSupport.await(readUserIdentityTokenAsync());
  }

  @Override
  public void writeUserIdentityToken(@Nullable UserIdentityToken value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeUserIdentityTokenAsync(value)),
        "http://opcfoundation.org/UA/}UserIdentityToken");
  }

  @Override
  public CompletableFuture<? extends @Nullable UserIdentityToken> readUserIdentityTokenAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getUserIdentityTokenNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}UserIdentityToken",
                            true,
                            UserIdentityToken.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UserIdentityToken) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeUserIdentityTokenAsync(
      @Nullable UserIdentityToken value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getUserIdentityTokenNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}UserIdentityToken",
                        value,
                        UserIdentityToken.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getClientSoftwareCertificatesNode() throws UaException {
    return ClientNodeSupport.await(getClientSoftwareCertificatesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getClientSoftwareCertificatesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ClientSoftwareCertificates",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable SignedSoftwareCertificate @Nullable [] readClientSoftwareCertificates()
      throws UaException {
    return ClientNodeSupport.await(readClientSoftwareCertificatesAsync());
  }

  @Override
  public void writeClientSoftwareCertificates(
      @Nullable SignedSoftwareCertificate @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeClientSoftwareCertificatesAsync(value)),
        "http://opcfoundation.org/UA/}ClientSoftwareCertificates");
  }

  @Override
  public CompletableFuture<? extends @Nullable SignedSoftwareCertificate @Nullable []>
      readClientSoftwareCertificatesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getClientSoftwareCertificatesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ClientSoftwareCertificates",
                            true,
                            SignedSoftwareCertificate.class,
                            1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable SignedSoftwareCertificate @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeClientSoftwareCertificatesAsync(
      @Nullable SignedSoftwareCertificate @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getClientSoftwareCertificatesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ClientSoftwareCertificates",
                        value,
                        SignedSoftwareCertificate.class,
                        1,
                        null)));
  }
}
