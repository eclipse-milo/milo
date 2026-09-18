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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ApplicationConfigurationFileType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.20">Model
 *     documentation</a>
 */
public class ApplicationConfigurationFileTypeNode extends ConfigurationFileTypeNode
    implements ApplicationConfigurationFileType {
  public ApplicationConfigurationFileTypeNode(
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
  public PropertyTypeNode getMaxEndpointsNode() throws UaException {
    return ClientNodeSupport.await(getMaxEndpointsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxEndpointsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxEndpoints",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UShort readMaxEndpoints() throws UaException {
    return ClientNodeSupport.await(readMaxEndpointsAsync());
  }

  @Override
  public void writeMaxEndpoints(@Nullable UShort value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxEndpointsAsync(value)),
        "http://opcfoundation.org/UA/}MaxEndpoints");
  }

  @Override
  public CompletableFuture<? extends @Nullable UShort> readMaxEndpointsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxEndpointsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxEndpoints",
                            true,
                            UShort.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UShort) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxEndpointsAsync(@Nullable UShort value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxEndpointsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxEndpoints",
                        value,
                        UShort.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getAvailablePortsNode() throws UaException {
    return ClientNodeSupport.await(getAvailablePortsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getAvailablePortsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AvailablePorts",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readAvailablePorts() throws UaException {
    return ClientNodeSupport.await(readAvailablePortsAsync());
  }

  @Override
  public void writeAvailablePorts(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeAvailablePortsAsync(value)),
        "http://opcfoundation.org/UA/}AvailablePorts");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readAvailablePortsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getAvailablePortsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}AvailablePorts",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeAvailablePortsAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getAvailablePortsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}AvailablePorts",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getUserTokenTypesNode() throws UaException {
    return ClientNodeSupport.await(getUserTokenTypesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUserTokenTypesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "UserTokenTypes",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UserTokenPolicy @Nullable [] readUserTokenTypes() throws UaException {
    return ClientNodeSupport.await(readUserTokenTypesAsync());
  }

  @Override
  public void writeUserTokenTypes(@Nullable UserTokenPolicy @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeUserTokenTypesAsync(value)),
        "http://opcfoundation.org/UA/}UserTokenTypes");
  }

  @Override
  public CompletableFuture<? extends @Nullable UserTokenPolicy @Nullable []>
      readUserTokenTypesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getUserTokenTypesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}UserTokenTypes",
                            true,
                            UserTokenPolicy.class,
                            1,
                            null)),
                v ->
                    CompletableFuture.completedFuture((@Nullable UserTokenPolicy @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeUserTokenTypesAsync(
      @Nullable UserTokenPolicy @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getUserTokenTypesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}UserTokenTypes",
                        value,
                        UserTokenPolicy.class,
                        1,
                        null)));
  }

  @Override
  public PropertyTypeNode getCertificateTypesNode() throws UaException {
    return ClientNodeSupport.await(getCertificateTypesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getCertificateTypesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CertificateTypes",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public NodeId @Nullable [] readCertificateTypes() throws UaException {
    return ClientNodeSupport.await(readCertificateTypesAsync());
  }

  @Override
  public void writeCertificateTypes(NodeId @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCertificateTypesAsync(value)),
        "http://opcfoundation.org/UA/}CertificateTypes");
  }

  @Override
  public CompletableFuture<? extends NodeId @Nullable []> readCertificateTypesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCertificateTypesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CertificateTypes",
                            true,
                            NodeId.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((NodeId @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCertificateTypesAsync(NodeId @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCertificateTypesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CertificateTypes",
                        value,
                        NodeId.class,
                        1,
                        null)));
  }

  @Override
  public PropertyTypeNode getAvailableNetworksNode() throws UaException {
    return ClientNodeSupport.await(getAvailableNetworksNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getAvailableNetworksNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AvailableNetworks",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String @Nullable [] readAvailableNetworks() throws UaException {
    return ClientNodeSupport.await(readAvailableNetworksAsync());
  }

  @Override
  public void writeAvailableNetworks(@Nullable String @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeAvailableNetworksAsync(value)),
        "http://opcfoundation.org/UA/}AvailableNetworks");
  }

  @Override
  public CompletableFuture<? extends @Nullable String @Nullable []> readAvailableNetworksAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getAvailableNetworksNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}AvailableNetworks",
                            true,
                            String.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeAvailableNetworksAsync(
      @Nullable String @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getAvailableNetworksNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}AvailableNetworks",
                        value,
                        String.class,
                        1,
                        null)));
  }

  @Override
  public PropertyTypeNode getSecurityPolicyUrisNode() throws UaException {
    return ClientNodeSupport.await(getSecurityPolicyUrisNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSecurityPolicyUrisNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SecurityPolicyUris",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String @Nullable [] readSecurityPolicyUris() throws UaException {
    return ClientNodeSupport.await(readSecurityPolicyUrisAsync());
  }

  @Override
  public void writeSecurityPolicyUris(@Nullable String @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSecurityPolicyUrisAsync(value)),
        "http://opcfoundation.org/UA/}SecurityPolicyUris");
  }

  @Override
  public CompletableFuture<? extends @Nullable String @Nullable []> readSecurityPolicyUrisAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSecurityPolicyUrisNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SecurityPolicyUris",
                            true,
                            String.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSecurityPolicyUrisAsync(
      @Nullable String @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSecurityPolicyUrisNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SecurityPolicyUris",
                        value,
                        String.class,
                        1,
                        null)));
  }

  @Override
  public PropertyTypeNode getMaxCertificateGroupsNode() throws UaException {
    return ClientNodeSupport.await(getMaxCertificateGroupsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxCertificateGroupsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxCertificateGroups",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UShort readMaxCertificateGroups() throws UaException {
    return ClientNodeSupport.await(readMaxCertificateGroupsAsync());
  }

  @Override
  public void writeMaxCertificateGroups(@Nullable UShort value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxCertificateGroupsAsync(value)),
        "http://opcfoundation.org/UA/}MaxCertificateGroups");
  }

  @Override
  public CompletableFuture<? extends @Nullable UShort> readMaxCertificateGroupsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxCertificateGroupsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxCertificateGroups",
                            true,
                            UShort.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UShort) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxCertificateGroupsAsync(@Nullable UShort value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxCertificateGroupsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxCertificateGroups",
                        value,
                        UShort.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getCertificateGroupPurposesNode() throws UaException {
    return ClientNodeSupport.await(getCertificateGroupPurposesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getCertificateGroupPurposesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CertificateGroupPurposes",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public NodeId @Nullable [] readCertificateGroupPurposes() throws UaException {
    return ClientNodeSupport.await(readCertificateGroupPurposesAsync());
  }

  @Override
  public void writeCertificateGroupPurposes(NodeId @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCertificateGroupPurposesAsync(value)),
        "http://opcfoundation.org/UA/}CertificateGroupPurposes");
  }

  @Override
  public CompletableFuture<? extends NodeId @Nullable []> readCertificateGroupPurposesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCertificateGroupPurposesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CertificateGroupPurposes",
                            true,
                            NodeId.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((NodeId @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCertificateGroupPurposesAsync(
      NodeId @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCertificateGroupPurposesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CertificateGroupPurposes",
                        value,
                        NodeId.class,
                        1,
                        null)));
  }
}
