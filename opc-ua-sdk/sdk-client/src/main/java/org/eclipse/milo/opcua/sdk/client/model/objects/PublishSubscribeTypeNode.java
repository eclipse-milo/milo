package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.PublishSubscribeTypeAddConnection;
import org.eclipse.milo.opcua.sdk.core.model.methods.PublishSubscribeTypeRemoveConnection;
import org.eclipse.milo.opcua.sdk.core.model.methods.PublishSubscribeTypeSetSecurityKeys;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link PublishSubscribeType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.2">Model
 *     documentation</a>
 */
public class PublishSubscribeTypeNode extends PubSubKeyServiceTypeNode
    implements PublishSubscribeType {
  public PublishSubscribeTypeNode(
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
  public @Nullable PubSubDiagnosticsRootTypeNode getDiagnosticsNode() throws UaException {
    return ClientNodeSupport.await(getDiagnosticsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PubSubDiagnosticsRootTypeNode>
      getDiagnosticsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Diagnostics",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        PubSubDiagnosticsRootTypeNode.class)));
  }

  @Override
  public @Nullable FolderTypeNode getDataSetClassesNode() throws UaException {
    return ClientNodeSupport.await(getDataSetClassesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable FolderTypeNode> getDataSetClassesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DataSetClasses",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        FolderTypeNode.class)));
  }

  @Override
  public @Nullable PubSubCapabilitiesTypeNode getPubSubCapablitiesNode() throws UaException {
    return ClientNodeSupport.await(getPubSubCapablitiesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PubSubCapabilitiesTypeNode>
      getPubSubCapablitiesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "PubSubCapablities",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        PubSubCapabilitiesTypeNode.class)));
  }

  @Override
  public DataSetFolderTypeNode getPublishedDataSetsNode() throws UaException {
    return ClientNodeSupport.await(getPublishedDataSetsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends DataSetFolderTypeNode> getPublishedDataSetsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "PublishedDataSets",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        DataSetFolderTypeNode.class)));
  }

  @Override
  public @Nullable SubscribedDataSetFolderTypeNode getSubscribedDataSetsNode() throws UaException {
    return ClientNodeSupport.await(getSubscribedDataSetsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable SubscribedDataSetFolderTypeNode>
      getSubscribedDataSetsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SubscribedDataSets",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        SubscribedDataSetFolderTypeNode.class)));
  }

  @Override
  public @Nullable PubSubConfigurationTypeNode getPubSubConfigurationNode() throws UaException {
    return ClientNodeSupport.await(getPubSubConfigurationNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PubSubConfigurationTypeNode>
      getPubSubConfigurationNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "PubSubConfiguration",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        PubSubConfigurationTypeNode.class)));
  }

  @Override
  public @Nullable PropertyTypeNode getConfigurationVersionNode() throws UaException {
    return ClientNodeSupport.await(getConfigurationVersionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getConfigurationVersionNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ConfigurationVersion",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readConfigurationVersion() throws UaException {
    return ClientNodeSupport.await(readConfigurationVersionAsync());
  }

  @Override
  public void writeConfigurationVersion(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeConfigurationVersionAsync(value)),
        "http://opcfoundation.org/UA/}ConfigurationVersion");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readConfigurationVersionAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getConfigurationVersionNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ConfigurationVersion",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeConfigurationVersionAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getConfigurationVersionNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ConfigurationVersion",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getConfigurationPropertiesNode() throws UaException {
    return ClientNodeSupport.await(getConfigurationPropertiesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getConfigurationPropertiesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ConfigurationProperties",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable KeyValuePair @Nullable [] readConfigurationProperties() throws UaException {
    return ClientNodeSupport.await(readConfigurationPropertiesAsync());
  }

  @Override
  public void writeConfigurationProperties(@Nullable KeyValuePair @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeConfigurationPropertiesAsync(value)),
        "http://opcfoundation.org/UA/}ConfigurationProperties");
  }

  @Override
  public CompletableFuture<? extends @Nullable KeyValuePair @Nullable []>
      readConfigurationPropertiesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getConfigurationPropertiesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ConfigurationProperties",
                            false,
                            KeyValuePair.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable KeyValuePair @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeConfigurationPropertiesAsync(
      @Nullable KeyValuePair @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getConfigurationPropertiesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ConfigurationProperties",
                        value,
                        KeyValuePair.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getDefaultDatagramPublisherIdNode() throws UaException {
    return ClientNodeSupport.await(getDefaultDatagramPublisherIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getDefaultDatagramPublisherIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DefaultDatagramPublisherId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable ULong readDefaultDatagramPublisherId() throws UaException {
    return ClientNodeSupport.await(readDefaultDatagramPublisherIdAsync());
  }

  @Override
  public void writeDefaultDatagramPublisherId(@Nullable ULong value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDefaultDatagramPublisherIdAsync(value)),
        "http://opcfoundation.org/UA/}DefaultDatagramPublisherId");
  }

  @Override
  public CompletableFuture<? extends @Nullable ULong> readDefaultDatagramPublisherIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDefaultDatagramPublisherIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DefaultDatagramPublisherId",
                            false,
                            ULong.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ULong) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDefaultDatagramPublisherIdAsync(@Nullable ULong value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDefaultDatagramPublisherIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DefaultDatagramPublisherId",
                        value,
                        ULong.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getDefaultSecurityKeyServicesNode() throws UaException {
    return ClientNodeSupport.await(getDefaultSecurityKeyServicesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getDefaultSecurityKeyServicesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DefaultSecurityKeyServices",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable EndpointDescription @Nullable [] readDefaultSecurityKeyServices()
      throws UaException {
    return ClientNodeSupport.await(readDefaultSecurityKeyServicesAsync());
  }

  @Override
  public void writeDefaultSecurityKeyServices(@Nullable EndpointDescription @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDefaultSecurityKeyServicesAsync(value)),
        "http://opcfoundation.org/UA/}DefaultSecurityKeyServices");
  }

  @Override
  public CompletableFuture<? extends @Nullable EndpointDescription @Nullable []>
      readDefaultSecurityKeyServicesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDefaultSecurityKeyServicesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DefaultSecurityKeyServices",
                            false,
                            EndpointDescription.class,
                            1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable EndpointDescription @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDefaultSecurityKeyServicesAsync(
      @Nullable EndpointDescription @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDefaultSecurityKeyServicesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DefaultSecurityKeyServices",
                        value,
                        EndpointDescription.class,
                        1,
                        null)));
  }

  @Override
  public PropertyTypeNode getSupportedTransportProfilesNode() throws UaException {
    return ClientNodeSupport.await(getSupportedTransportProfilesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSupportedTransportProfilesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SupportedTransportProfiles",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String @Nullable [] readSupportedTransportProfiles() throws UaException {
    return ClientNodeSupport.await(readSupportedTransportProfilesAsync());
  }

  @Override
  public void writeSupportedTransportProfiles(@Nullable String @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSupportedTransportProfilesAsync(value)),
        "http://opcfoundation.org/UA/}SupportedTransportProfiles");
  }

  @Override
  public CompletableFuture<? extends @Nullable String @Nullable []>
      readSupportedTransportProfilesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSupportedTransportProfilesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SupportedTransportProfiles",
                            true,
                            String.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSupportedTransportProfilesAsync(
      @Nullable String @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSupportedTransportProfilesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SupportedTransportProfiles",
                        value,
                        String.class,
                        1,
                        null)));
  }

  @Override
  public PubSubStatusTypeNode getStatusNode() throws UaException {
    return ClientNodeSupport.await(getStatusNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PubSubStatusTypeNode> getStatusNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Status",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        PubSubStatusTypeNode.class)));
  }

  @Override
  public @Nullable UaMethodNode getAddConnectionMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddConnectionMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getAddConnectionMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddConnection",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable NodeId addConnection(@Nullable PubSubConnectionDataType configuration)
      throws UaException {
    return ClientNodeSupport.await(addConnectionAsync(configuration));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddConnection(
      @Nullable PubSubConnectionDataType configuration) throws UaException {
    return ClientNodeSupport.await(callAddConnectionAsync(configuration));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddConnectionWith(
      MethodCallOptions options, @Nullable PubSubConnectionDataType configuration)
      throws UaException {
    return ClientNodeSupport.await(callAddConnectionWithAsync(options, configuration));
  }

  @Override
  public CompletableFuture<@Nullable NodeId> addConnectionAsync(
      @Nullable PubSubConnectionDataType configuration) {
    return ClientNodeSupport.compose(
        callAddConnectionAsync(configuration),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddConnectionAsync(
      @Nullable PubSubConnectionDataType configuration) {
    return callAddConnectionWithAsync(MethodCallOptions.DEFAULT, configuration);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddConnectionWithAsync(
      MethodCallOptions options, @Nullable PubSubConnectionDataType configuration) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new PublishSubscribeTypeAddConnection.Inputs(configuration)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddConnectionMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return PublishSubscribeTypeAddConnection.Outputs.fromVariants(
                                            client.getStaticEncodingContext(), values)
                                        .connectionId();
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getRemoveConnectionMethodNode() throws UaException {
    return ClientNodeSupport.await(getRemoveConnectionMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getRemoveConnectionMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "RemoveConnection",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void removeConnection(@Nullable NodeId connectionId) throws UaException {
    ClientNodeSupport.await(removeConnectionAsync(connectionId));
  }

  @Override
  public MethodCallResult<Void> callRemoveConnection(@Nullable NodeId connectionId)
      throws UaException {
    return ClientNodeSupport.await(callRemoveConnectionAsync(connectionId));
  }

  @Override
  public MethodCallResult<Void> callRemoveConnectionWith(
      MethodCallOptions options, @Nullable NodeId connectionId) throws UaException {
    return ClientNodeSupport.await(callRemoveConnectionWithAsync(options, connectionId));
  }

  @Override
  public CompletableFuture<Void> removeConnectionAsync(@Nullable NodeId connectionId) {
    return ClientNodeSupport.compose(
        callRemoveConnectionAsync(connectionId),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveConnectionAsync(
      @Nullable NodeId connectionId) {
    return callRemoveConnectionWithAsync(MethodCallOptions.DEFAULT, connectionId);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveConnectionWithAsync(
      MethodCallOptions options, @Nullable NodeId connectionId) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new PublishSubscribeTypeRemoveConnection.Inputs(connectionId)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRemoveConnectionMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    if (values == null || values.length != 0)
                                      throw new UaException(
                                          StatusCodes.Bad_DecodingError,
                                          "expected no Method outputs");
                                    return null;
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getSetSecurityKeysMethodNode() throws UaException {
    return ClientNodeSupport.await(getSetSecurityKeysMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getSetSecurityKeysMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "SetSecurityKeys",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void setSecurityKeys(
      @Nullable String securityGroupId,
      @Nullable String securityPolicyUri,
      @Nullable UInteger currentTokenId,
      @Nullable ByteString currentKey,
      ByteString @Nullable [] futureKeys,
      @Nullable Double timeToNextKey,
      @Nullable Double keyLifetime)
      throws UaException {
    ClientNodeSupport.await(
        setSecurityKeysAsync(
            securityGroupId,
            securityPolicyUri,
            currentTokenId,
            currentKey,
            futureKeys,
            timeToNextKey,
            keyLifetime));
  }

  @Override
  public MethodCallResult<Void> callSetSecurityKeys(
      @Nullable String securityGroupId,
      @Nullable String securityPolicyUri,
      @Nullable UInteger currentTokenId,
      @Nullable ByteString currentKey,
      ByteString @Nullable [] futureKeys,
      @Nullable Double timeToNextKey,
      @Nullable Double keyLifetime)
      throws UaException {
    return ClientNodeSupport.await(
        callSetSecurityKeysAsync(
            securityGroupId,
            securityPolicyUri,
            currentTokenId,
            currentKey,
            futureKeys,
            timeToNextKey,
            keyLifetime));
  }

  @Override
  public MethodCallResult<Void> callSetSecurityKeysWith(
      MethodCallOptions options,
      @Nullable String securityGroupId,
      @Nullable String securityPolicyUri,
      @Nullable UInteger currentTokenId,
      @Nullable ByteString currentKey,
      ByteString @Nullable [] futureKeys,
      @Nullable Double timeToNextKey,
      @Nullable Double keyLifetime)
      throws UaException {
    return ClientNodeSupport.await(
        callSetSecurityKeysWithAsync(
            options,
            securityGroupId,
            securityPolicyUri,
            currentTokenId,
            currentKey,
            futureKeys,
            timeToNextKey,
            keyLifetime));
  }

  @Override
  public CompletableFuture<Void> setSecurityKeysAsync(
      @Nullable String securityGroupId,
      @Nullable String securityPolicyUri,
      @Nullable UInteger currentTokenId,
      @Nullable ByteString currentKey,
      ByteString @Nullable [] futureKeys,
      @Nullable Double timeToNextKey,
      @Nullable Double keyLifetime) {
    return ClientNodeSupport.compose(
        callSetSecurityKeysAsync(
            securityGroupId,
            securityPolicyUri,
            currentTokenId,
            currentKey,
            futureKeys,
            timeToNextKey,
            keyLifetime),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callSetSecurityKeysAsync(
      @Nullable String securityGroupId,
      @Nullable String securityPolicyUri,
      @Nullable UInteger currentTokenId,
      @Nullable ByteString currentKey,
      ByteString @Nullable [] futureKeys,
      @Nullable Double timeToNextKey,
      @Nullable Double keyLifetime) {
    return callSetSecurityKeysWithAsync(
        MethodCallOptions.DEFAULT,
        securityGroupId,
        securityPolicyUri,
        currentTokenId,
        currentKey,
        futureKeys,
        timeToNextKey,
        keyLifetime);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callSetSecurityKeysWithAsync(
      MethodCallOptions options,
      @Nullable String securityGroupId,
      @Nullable String securityPolicyUri,
      @Nullable UInteger currentTokenId,
      @Nullable ByteString currentKey,
      ByteString @Nullable [] futureKeys,
      @Nullable Double timeToNextKey,
      @Nullable Double keyLifetime) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new PublishSubscribeTypeSetSecurityKeys.Inputs(
                      securityGroupId,
                      securityPolicyUri,
                      currentTokenId,
                      currentKey,
                      futureKeys,
                      timeToNextKey,
                      keyLifetime)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getSetSecurityKeysMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    if (values == null || values.length != 0)
                                      throw new UaException(
                                          StatusCodes.Bad_DecodingError,
                                          "expected no Method outputs");
                                    return null;
                                  }))));
        });
  }
}
