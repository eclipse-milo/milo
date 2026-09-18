package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.SelectionListTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubConnectionTypeAddReaderGroup;
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubConnectionTypeAddWriterGroup;
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubConnectionTypeRemoveGroup;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.ReaderGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link PubSubConnectionType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.2">Model
 *     documentation</a>
 */
public class PubSubConnectionTypeNode extends BaseObjectTypeNode implements PubSubConnectionType {
  public PubSubConnectionTypeNode(
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
  public @Nullable PubSubDiagnosticsConnectionTypeNode getDiagnosticsNode() throws UaException {
    return ClientNodeSupport.await(getDiagnosticsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PubSubDiagnosticsConnectionTypeNode>
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
                        PubSubDiagnosticsConnectionTypeNode.class)));
  }

  @Override
  public PropertyTypeNode getPublisherIdNode() throws UaException {
    return ClientNodeSupport.await(getPublisherIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPublisherIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "PublisherId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Variant readPublisherId() throws UaException {
    return ClientNodeSupport.await(readPublisherIdAsync());
  }

  @Override
  public void writePublisherId(@Nullable Variant value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writePublisherIdAsync(value)),
        "http://opcfoundation.org/UA/}PublisherId");
  }

  @Override
  public CompletableFuture<? extends @Nullable Variant> readPublisherIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getPublisherIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}PublisherId",
                            true,
                            Variant.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Variant) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writePublisherIdAsync(@Nullable Variant value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getPublisherIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}PublisherId",
                        value,
                        Variant.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable ConnectionTransportTypeNode getTransportSettingsNode() throws UaException {
    return ClientNodeSupport.await(getTransportSettingsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable ConnectionTransportTypeNode>
      getTransportSettingsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "TransportSettings",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        ConnectionTransportTypeNode.class)));
  }

  @Override
  public SelectionListTypeNode getTransportProfileUriNode() throws UaException {
    return ClientNodeSupport.await(getTransportProfileUriNodeAsync());
  }

  @Override
  public CompletableFuture<? extends SelectionListTypeNode> getTransportProfileUriNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "TransportProfileUri",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        SelectionListTypeNode.class)));
  }

  @Override
  public @Nullable String readTransportProfileUri() throws UaException {
    return ClientNodeSupport.await(readTransportProfileUriAsync());
  }

  @Override
  public void writeTransportProfileUri(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeTransportProfileUriAsync(value)),
        "http://opcfoundation.org/UA/}TransportProfileUri");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readTransportProfileUriAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getTransportProfileUriNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}TransportProfileUri",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTransportProfileUriAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getTransportProfileUriNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}TransportProfileUri",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getConnectionPropertiesNode() throws UaException {
    return ClientNodeSupport.await(getConnectionPropertiesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getConnectionPropertiesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ConnectionProperties",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable KeyValuePair @Nullable [] readConnectionProperties() throws UaException {
    return ClientNodeSupport.await(readConnectionPropertiesAsync());
  }

  @Override
  public void writeConnectionProperties(@Nullable KeyValuePair @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeConnectionPropertiesAsync(value)),
        "http://opcfoundation.org/UA/}ConnectionProperties");
  }

  @Override
  public CompletableFuture<? extends @Nullable KeyValuePair @Nullable []>
      readConnectionPropertiesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getConnectionPropertiesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ConnectionProperties",
                            true,
                            KeyValuePair.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable KeyValuePair @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeConnectionPropertiesAsync(
      @Nullable KeyValuePair @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getConnectionPropertiesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ConnectionProperties",
                        value,
                        KeyValuePair.class,
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
  public NetworkAddressTypeNode getAddressNode() throws UaException {
    return ClientNodeSupport.await(getAddressNodeAsync());
  }

  @Override
  public CompletableFuture<? extends NetworkAddressTypeNode> getAddressNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Address",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        NetworkAddressTypeNode.class)));
  }

  @Override
  public @Nullable UaMethodNode getAddReaderGroupMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddReaderGroupMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getAddReaderGroupMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddReaderGroup",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable NodeId addReaderGroup(@Nullable ReaderGroupDataType configuration)
      throws UaException {
    return ClientNodeSupport.await(addReaderGroupAsync(configuration));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddReaderGroup(
      @Nullable ReaderGroupDataType configuration) throws UaException {
    return ClientNodeSupport.await(callAddReaderGroupAsync(configuration));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddReaderGroupWith(
      MethodCallOptions options, @Nullable ReaderGroupDataType configuration) throws UaException {
    return ClientNodeSupport.await(callAddReaderGroupWithAsync(options, configuration));
  }

  @Override
  public CompletableFuture<@Nullable NodeId> addReaderGroupAsync(
      @Nullable ReaderGroupDataType configuration) {
    return ClientNodeSupport.compose(
        callAddReaderGroupAsync(configuration),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddReaderGroupAsync(
      @Nullable ReaderGroupDataType configuration) {
    return callAddReaderGroupWithAsync(MethodCallOptions.DEFAULT, configuration);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddReaderGroupWithAsync(
      MethodCallOptions options, @Nullable ReaderGroupDataType configuration) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new PubSubConnectionTypeAddReaderGroup.Inputs(configuration)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddReaderGroupMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return PubSubConnectionTypeAddReaderGroup.Outputs.fromVariants(
                                            client.getStaticEncodingContext(), values)
                                        .groupId();
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getAddWriterGroupMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddWriterGroupMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getAddWriterGroupMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddWriterGroup",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable NodeId addWriterGroup(@Nullable WriterGroupDataType configuration)
      throws UaException {
    return ClientNodeSupport.await(addWriterGroupAsync(configuration));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddWriterGroup(
      @Nullable WriterGroupDataType configuration) throws UaException {
    return ClientNodeSupport.await(callAddWriterGroupAsync(configuration));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddWriterGroupWith(
      MethodCallOptions options, @Nullable WriterGroupDataType configuration) throws UaException {
    return ClientNodeSupport.await(callAddWriterGroupWithAsync(options, configuration));
  }

  @Override
  public CompletableFuture<@Nullable NodeId> addWriterGroupAsync(
      @Nullable WriterGroupDataType configuration) {
    return ClientNodeSupport.compose(
        callAddWriterGroupAsync(configuration),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddWriterGroupAsync(
      @Nullable WriterGroupDataType configuration) {
    return callAddWriterGroupWithAsync(MethodCallOptions.DEFAULT, configuration);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddWriterGroupWithAsync(
      MethodCallOptions options, @Nullable WriterGroupDataType configuration) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new PubSubConnectionTypeAddWriterGroup.Inputs(configuration)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddWriterGroupMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return PubSubConnectionTypeAddWriterGroup.Outputs.fromVariants(
                                            client.getStaticEncodingContext(), values)
                                        .groupId();
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getRemoveGroupMethodNode() throws UaException {
    return ClientNodeSupport.await(getRemoveGroupMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getRemoveGroupMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "RemoveGroup",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void removeGroup(@Nullable NodeId groupId) throws UaException {
    ClientNodeSupport.await(removeGroupAsync(groupId));
  }

  @Override
  public MethodCallResult<Void> callRemoveGroup(@Nullable NodeId groupId) throws UaException {
    return ClientNodeSupport.await(callRemoveGroupAsync(groupId));
  }

  @Override
  public MethodCallResult<Void> callRemoveGroupWith(
      MethodCallOptions options, @Nullable NodeId groupId) throws UaException {
    return ClientNodeSupport.await(callRemoveGroupWithAsync(options, groupId));
  }

  @Override
  public CompletableFuture<Void> removeGroupAsync(@Nullable NodeId groupId) {
    return ClientNodeSupport.compose(
        callRemoveGroupAsync(groupId),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveGroupAsync(@Nullable NodeId groupId) {
    return callRemoveGroupWithAsync(MethodCallOptions.DEFAULT, groupId);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveGroupWithAsync(
      MethodCallOptions options, @Nullable NodeId groupId) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new PubSubConnectionTypeRemoveGroup.Inputs(groupId)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRemoveGroupMethodNodeAsync(),
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
