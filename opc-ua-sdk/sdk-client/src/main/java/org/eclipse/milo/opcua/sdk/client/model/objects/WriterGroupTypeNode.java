package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.WriterGroupTypeAddDataSetWriter;
import org.eclipse.milo.opcua.sdk.core.model.methods.WriterGroupTypeRemoveDataSetWriter;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link WriterGroupType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.3">Model
 *     documentation</a>
 */
public class WriterGroupTypeNode extends PubSubGroupTypeNode implements WriterGroupType {
  public WriterGroupTypeNode(
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
  public @Nullable PubSubDiagnosticsWriterGroupTypeNode getDiagnosticsNode() throws UaException {
    return ClientNodeSupport.await(getDiagnosticsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PubSubDiagnosticsWriterGroupTypeNode>
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
                        PubSubDiagnosticsWriterGroupTypeNode.class)));
  }

  @Override
  public PropertyTypeNode getKeepAliveTimeNode() throws UaException {
    return ClientNodeSupport.await(getKeepAliveTimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getKeepAliveTimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "KeepAliveTime",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Double readKeepAliveTime() throws UaException {
    return ClientNodeSupport.await(readKeepAliveTimeAsync());
  }

  @Override
  public void writeKeepAliveTime(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeKeepAliveTimeAsync(value)),
        "http://opcfoundation.org/UA/}KeepAliveTime");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readKeepAliveTimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getKeepAliveTimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}KeepAliveTime",
                            true,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeKeepAliveTimeAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getKeepAliveTimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}KeepAliveTime",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getWriterGroupIdNode() throws UaException {
    return ClientNodeSupport.await(getWriterGroupIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getWriterGroupIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "WriterGroupId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UShort readWriterGroupId() throws UaException {
    return ClientNodeSupport.await(readWriterGroupIdAsync());
  }

  @Override
  public void writeWriterGroupId(@Nullable UShort value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeWriterGroupIdAsync(value)),
        "http://opcfoundation.org/UA/}WriterGroupId");
  }

  @Override
  public CompletableFuture<? extends @Nullable UShort> readWriterGroupIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getWriterGroupIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}WriterGroupId",
                            true,
                            UShort.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UShort) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeWriterGroupIdAsync(@Nullable UShort value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getWriterGroupIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}WriterGroupId",
                        value,
                        UShort.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getHeaderLayoutUriNode() throws UaException {
    return ClientNodeSupport.await(getHeaderLayoutUriNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getHeaderLayoutUriNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "HeaderLayoutUri",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readHeaderLayoutUri() throws UaException {
    return ClientNodeSupport.await(readHeaderLayoutUriAsync());
  }

  @Override
  public void writeHeaderLayoutUri(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeHeaderLayoutUriAsync(value)),
        "http://opcfoundation.org/UA/}HeaderLayoutUri");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readHeaderLayoutUriAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getHeaderLayoutUriNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}HeaderLayoutUri",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeHeaderLayoutUriAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getHeaderLayoutUriNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}HeaderLayoutUri",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable WriterGroupMessageTypeNode getMessageSettingsNode() throws UaException {
    return ClientNodeSupport.await(getMessageSettingsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable WriterGroupMessageTypeNode>
      getMessageSettingsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MessageSettings",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        WriterGroupMessageTypeNode.class)));
  }

  @Override
  public @Nullable WriterGroupTransportTypeNode getTransportSettingsNode() throws UaException {
    return ClientNodeSupport.await(getTransportSettingsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable WriterGroupTransportTypeNode>
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
                        WriterGroupTransportTypeNode.class)));
  }

  @Override
  public PropertyTypeNode getPublishingIntervalNode() throws UaException {
    return ClientNodeSupport.await(getPublishingIntervalNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPublishingIntervalNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "PublishingInterval",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Double readPublishingInterval() throws UaException {
    return ClientNodeSupport.await(readPublishingIntervalAsync());
  }

  @Override
  public void writePublishingInterval(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writePublishingIntervalAsync(value)),
        "http://opcfoundation.org/UA/}PublishingInterval");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readPublishingIntervalAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getPublishingIntervalNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}PublishingInterval",
                            true,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writePublishingIntervalAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getPublishingIntervalNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}PublishingInterval",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getPriorityNode() throws UaException {
    return ClientNodeSupport.await(getPriorityNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPriorityNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Priority",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UByte readPriority() throws UaException {
    return ClientNodeSupport.await(readPriorityAsync());
  }

  @Override
  public void writePriority(@Nullable UByte value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writePriorityAsync(value)),
        "http://opcfoundation.org/UA/}Priority");
  }

  @Override
  public CompletableFuture<? extends @Nullable UByte> readPriorityAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getPriorityNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Priority",
                            true,
                            UByte.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UByte) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writePriorityAsync(@Nullable UByte value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getPriorityNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Priority",
                        value,
                        UByte.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getLocaleIdsNode() throws UaException {
    return ClientNodeSupport.await(getLocaleIdsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLocaleIdsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LocaleIds",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String @Nullable [] readLocaleIds() throws UaException {
    return ClientNodeSupport.await(readLocaleIdsAsync());
  }

  @Override
  public void writeLocaleIds(@Nullable String @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLocaleIdsAsync(value)),
        "http://opcfoundation.org/UA/}LocaleIds");
  }

  @Override
  public CompletableFuture<? extends @Nullable String @Nullable []> readLocaleIdsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLocaleIdsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LocaleIds",
                            true,
                            String.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLocaleIdsAsync(@Nullable String @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLocaleIdsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LocaleIds",
                        value,
                        String.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable UaMethodNode getAddDataSetWriterMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddDataSetWriterMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getAddDataSetWriterMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddDataSetWriter",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable NodeId addDataSetWriter(@Nullable DataSetWriterDataType configuration)
      throws UaException {
    return ClientNodeSupport.await(addDataSetWriterAsync(configuration));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddDataSetWriter(
      @Nullable DataSetWriterDataType configuration) throws UaException {
    return ClientNodeSupport.await(callAddDataSetWriterAsync(configuration));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddDataSetWriterWith(
      MethodCallOptions options, @Nullable DataSetWriterDataType configuration) throws UaException {
    return ClientNodeSupport.await(callAddDataSetWriterWithAsync(options, configuration));
  }

  @Override
  public CompletableFuture<@Nullable NodeId> addDataSetWriterAsync(
      @Nullable DataSetWriterDataType configuration) {
    return ClientNodeSupport.compose(
        callAddDataSetWriterAsync(configuration),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddDataSetWriterAsync(
      @Nullable DataSetWriterDataType configuration) {
    return callAddDataSetWriterWithAsync(MethodCallOptions.DEFAULT, configuration);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddDataSetWriterWithAsync(
      MethodCallOptions options, @Nullable DataSetWriterDataType configuration) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new WriterGroupTypeAddDataSetWriter.Inputs(configuration)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddDataSetWriterMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return WriterGroupTypeAddDataSetWriter.Outputs.fromVariants(
                                            client.getStaticEncodingContext(), values)
                                        .dataSetWriterNodeId();
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getRemoveDataSetWriterMethodNode() throws UaException {
    return ClientNodeSupport.await(getRemoveDataSetWriterMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getRemoveDataSetWriterMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "RemoveDataSetWriter",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void removeDataSetWriter(@Nullable NodeId dataSetWriterNodeId) throws UaException {
    ClientNodeSupport.await(removeDataSetWriterAsync(dataSetWriterNodeId));
  }

  @Override
  public MethodCallResult<Void> callRemoveDataSetWriter(@Nullable NodeId dataSetWriterNodeId)
      throws UaException {
    return ClientNodeSupport.await(callRemoveDataSetWriterAsync(dataSetWriterNodeId));
  }

  @Override
  public MethodCallResult<Void> callRemoveDataSetWriterWith(
      MethodCallOptions options, @Nullable NodeId dataSetWriterNodeId) throws UaException {
    return ClientNodeSupport.await(callRemoveDataSetWriterWithAsync(options, dataSetWriterNodeId));
  }

  @Override
  public CompletableFuture<Void> removeDataSetWriterAsync(@Nullable NodeId dataSetWriterNodeId) {
    return ClientNodeSupport.compose(
        callRemoveDataSetWriterAsync(dataSetWriterNodeId),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveDataSetWriterAsync(
      @Nullable NodeId dataSetWriterNodeId) {
    return callRemoveDataSetWriterWithAsync(MethodCallOptions.DEFAULT, dataSetWriterNodeId);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveDataSetWriterWithAsync(
      MethodCallOptions options, @Nullable NodeId dataSetWriterNodeId) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new WriterGroupTypeRemoveDataSetWriter.Inputs(dataSetWriterNodeId)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRemoveDataSetWriterMethodNodeAsync(),
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
