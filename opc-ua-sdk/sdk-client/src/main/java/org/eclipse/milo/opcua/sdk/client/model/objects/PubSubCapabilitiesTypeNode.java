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
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link PubSubCapabilitiesType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.12/#9.1.12.1">Model
 *     documentation</a>
 */
public class PubSubCapabilitiesTypeNode extends BaseObjectTypeNode
    implements PubSubCapabilitiesType {
  public PubSubCapabilitiesTypeNode(
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
  public @Nullable PropertyTypeNode getMaxPushTargetsNode() throws UaException {
    return ClientNodeSupport.await(getMaxPushTargetsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxPushTargetsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxPushTargets",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxPushTargets() throws UaException {
    return ClientNodeSupport.await(readMaxPushTargetsAsync());
  }

  @Override
  public void writeMaxPushTargets(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxPushTargetsAsync(value)),
        "http://opcfoundation.org/UA/}MaxPushTargets");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxPushTargetsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxPushTargetsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxPushTargets",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxPushTargetsAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxPushTargetsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxPushTargets",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getMaxReaderGroupsNode() throws UaException {
    return ClientNodeSupport.await(getMaxReaderGroupsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxReaderGroupsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxReaderGroups",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxReaderGroups() throws UaException {
    return ClientNodeSupport.await(readMaxReaderGroupsAsync());
  }

  @Override
  public void writeMaxReaderGroups(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxReaderGroupsAsync(value)),
        "http://opcfoundation.org/UA/}MaxReaderGroups");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxReaderGroupsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxReaderGroupsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxReaderGroups",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxReaderGroupsAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxReaderGroupsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxReaderGroups",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getMaxWriterGroupsNode() throws UaException {
    return ClientNodeSupport.await(getMaxWriterGroupsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxWriterGroupsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxWriterGroups",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxWriterGroups() throws UaException {
    return ClientNodeSupport.await(readMaxWriterGroupsAsync());
  }

  @Override
  public void writeMaxWriterGroups(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxWriterGroupsAsync(value)),
        "http://opcfoundation.org/UA/}MaxWriterGroups");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxWriterGroupsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxWriterGroupsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxWriterGroups",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxWriterGroupsAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxWriterGroupsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxWriterGroups",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getMaxDataSetReadersNode() throws UaException {
    return ClientNodeSupport.await(getMaxDataSetReadersNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxDataSetReadersNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxDataSetReaders",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxDataSetReaders() throws UaException {
    return ClientNodeSupport.await(readMaxDataSetReadersAsync());
  }

  @Override
  public void writeMaxDataSetReaders(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxDataSetReadersAsync(value)),
        "http://opcfoundation.org/UA/}MaxDataSetReaders");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxDataSetReadersAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxDataSetReadersNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxDataSetReaders",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxDataSetReadersAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxDataSetReadersNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxDataSetReaders",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getMaxDataSetWritersNode() throws UaException {
    return ClientNodeSupport.await(getMaxDataSetWritersNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxDataSetWritersNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxDataSetWriters",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxDataSetWriters() throws UaException {
    return ClientNodeSupport.await(readMaxDataSetWritersAsync());
  }

  @Override
  public void writeMaxDataSetWriters(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxDataSetWritersAsync(value)),
        "http://opcfoundation.org/UA/}MaxDataSetWriters");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxDataSetWritersAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxDataSetWritersNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxDataSetWriters",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxDataSetWritersAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxDataSetWritersNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxDataSetWriters",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxSecurityGroupsNode() throws UaException {
    return ClientNodeSupport.await(getMaxSecurityGroupsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxSecurityGroupsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxSecurityGroups",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxSecurityGroups() throws UaException {
    return ClientNodeSupport.await(readMaxSecurityGroupsAsync());
  }

  @Override
  public void writeMaxSecurityGroups(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxSecurityGroupsAsync(value)),
        "http://opcfoundation.org/UA/}MaxSecurityGroups");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxSecurityGroupsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxSecurityGroupsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxSecurityGroups",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxSecurityGroupsAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxSecurityGroupsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxSecurityGroups",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getMaxFieldsPerDataSetNode() throws UaException {
    return ClientNodeSupport.await(getMaxFieldsPerDataSetNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxFieldsPerDataSetNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxFieldsPerDataSet",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxFieldsPerDataSet() throws UaException {
    return ClientNodeSupport.await(readMaxFieldsPerDataSetAsync());
  }

  @Override
  public void writeMaxFieldsPerDataSet(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxFieldsPerDataSetAsync(value)),
        "http://opcfoundation.org/UA/}MaxFieldsPerDataSet");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxFieldsPerDataSetAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxFieldsPerDataSetNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxFieldsPerDataSet",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxFieldsPerDataSetAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxFieldsPerDataSetNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxFieldsPerDataSet",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getMaxPubSubConnectionsNode() throws UaException {
    return ClientNodeSupport.await(getMaxPubSubConnectionsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxPubSubConnectionsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxPubSubConnections",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxPubSubConnections() throws UaException {
    return ClientNodeSupport.await(readMaxPubSubConnectionsAsync());
  }

  @Override
  public void writeMaxPubSubConnections(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxPubSubConnectionsAsync(value)),
        "http://opcfoundation.org/UA/}MaxPubSubConnections");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxPubSubConnectionsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxPubSubConnectionsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxPubSubConnections",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxPubSubConnectionsAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxPubSubConnectionsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxPubSubConnections",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxPublishedDataSetsNode() throws UaException {
    return ClientNodeSupport.await(getMaxPublishedDataSetsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxPublishedDataSetsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxPublishedDataSets",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxPublishedDataSets() throws UaException {
    return ClientNodeSupport.await(readMaxPublishedDataSetsAsync());
  }

  @Override
  public void writeMaxPublishedDataSets(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxPublishedDataSetsAsync(value)),
        "http://opcfoundation.org/UA/}MaxPublishedDataSets");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxPublishedDataSetsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxPublishedDataSetsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxPublishedDataSets",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxPublishedDataSetsAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxPublishedDataSetsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxPublishedDataSets",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getSupportSecurityKeyPullNode() throws UaException {
    return ClientNodeSupport.await(getSupportSecurityKeyPullNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getSupportSecurityKeyPullNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SupportSecurityKeyPull",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readSupportSecurityKeyPull() throws UaException {
    return ClientNodeSupport.await(readSupportSecurityKeyPullAsync());
  }

  @Override
  public void writeSupportSecurityKeyPull(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSupportSecurityKeyPullAsync(value)),
        "http://opcfoundation.org/UA/}SupportSecurityKeyPull");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readSupportSecurityKeyPullAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSupportSecurityKeyPullNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SupportSecurityKeyPull",
                            false,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSupportSecurityKeyPullAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSupportSecurityKeyPullNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SupportSecurityKeyPull",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getSupportSecurityKeyPushNode() throws UaException {
    return ClientNodeSupport.await(getSupportSecurityKeyPushNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getSupportSecurityKeyPushNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SupportSecurityKeyPush",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readSupportSecurityKeyPush() throws UaException {
    return ClientNodeSupport.await(readSupportSecurityKeyPushAsync());
  }

  @Override
  public void writeSupportSecurityKeyPush(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSupportSecurityKeyPushAsync(value)),
        "http://opcfoundation.org/UA/}SupportSecurityKeyPush");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readSupportSecurityKeyPushAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSupportSecurityKeyPushNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SupportSecurityKeyPush",
                            false,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSupportSecurityKeyPushAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSupportSecurityKeyPushNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SupportSecurityKeyPush",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getSupportSecurityKeyServerNode() throws UaException {
    return ClientNodeSupport.await(getSupportSecurityKeyServerNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getSupportSecurityKeyServerNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SupportSecurityKeyServer",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readSupportSecurityKeyServer() throws UaException {
    return ClientNodeSupport.await(readSupportSecurityKeyServerAsync());
  }

  @Override
  public void writeSupportSecurityKeyServer(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSupportSecurityKeyServerAsync(value)),
        "http://opcfoundation.org/UA/}SupportSecurityKeyServer");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readSupportSecurityKeyServerAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSupportSecurityKeyServerNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SupportSecurityKeyServer",
                            false,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSupportSecurityKeyServerAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSupportSecurityKeyServerNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SupportSecurityKeyServer",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxDataSetWritersPerGroupNode() throws UaException {
    return ClientNodeSupport.await(getMaxDataSetWritersPerGroupNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxDataSetWritersPerGroupNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxDataSetWritersPerGroup",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxDataSetWritersPerGroup() throws UaException {
    return ClientNodeSupport.await(readMaxDataSetWritersPerGroupAsync());
  }

  @Override
  public void writeMaxDataSetWritersPerGroup(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxDataSetWritersPerGroupAsync(value)),
        "http://opcfoundation.org/UA/}MaxDataSetWritersPerGroup");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxDataSetWritersPerGroupAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxDataSetWritersPerGroupNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxDataSetWritersPerGroup",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxDataSetWritersPerGroupAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxDataSetWritersPerGroupNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxDataSetWritersPerGroup",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNetworkMessageSizeBrokerNode() throws UaException {
    return ClientNodeSupport.await(getMaxNetworkMessageSizeBrokerNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxNetworkMessageSizeBrokerNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxNetworkMessageSizeBroker",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxNetworkMessageSizeBroker() throws UaException {
    return ClientNodeSupport.await(readMaxNetworkMessageSizeBrokerAsync());
  }

  @Override
  public void writeMaxNetworkMessageSizeBroker(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxNetworkMessageSizeBrokerAsync(value)),
        "http://opcfoundation.org/UA/}MaxNetworkMessageSizeBroker");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxNetworkMessageSizeBrokerAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxNetworkMessageSizeBrokerNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxNetworkMessageSizeBroker",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNetworkMessageSizeBrokerAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxNetworkMessageSizeBrokerNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxNetworkMessageSizeBroker",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNetworkMessageSizeDatagramNode() throws UaException {
    return ClientNodeSupport.await(getMaxNetworkMessageSizeDatagramNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxNetworkMessageSizeDatagramNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxNetworkMessageSizeDatagram",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxNetworkMessageSizeDatagram() throws UaException {
    return ClientNodeSupport.await(readMaxNetworkMessageSizeDatagramAsync());
  }

  @Override
  public void writeMaxNetworkMessageSizeDatagram(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxNetworkMessageSizeDatagramAsync(value)),
        "http://opcfoundation.org/UA/}MaxNetworkMessageSizeDatagram");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxNetworkMessageSizeDatagramAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxNetworkMessageSizeDatagramNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxNetworkMessageSizeDatagram",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNetworkMessageSizeDatagramAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxNetworkMessageSizeDatagramNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxNetworkMessageSizeDatagram",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxStandaloneSubscribedDataSetsNode() throws UaException {
    return ClientNodeSupport.await(getMaxStandaloneSubscribedDataSetsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxStandaloneSubscribedDataSetsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxStandaloneSubscribedDataSets",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxStandaloneSubscribedDataSets() throws UaException {
    return ClientNodeSupport.await(readMaxStandaloneSubscribedDataSetsAsync());
  }

  @Override
  public void writeMaxStandaloneSubscribedDataSets(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxStandaloneSubscribedDataSetsAsync(value)),
        "http://opcfoundation.org/UA/}MaxStandaloneSubscribedDataSets");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger>
      readMaxStandaloneSubscribedDataSetsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxStandaloneSubscribedDataSetsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxStandaloneSubscribedDataSets",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxStandaloneSubscribedDataSetsAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxStandaloneSubscribedDataSetsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxStandaloneSubscribedDataSets",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }
}
