package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.DataSetReaderTypeCreateDataSetMirror;
import org.eclipse.milo.opcua.sdk.core.model.methods.DataSetReaderTypeCreateTargetVariables;
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldTargetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link DataSetReaderType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.2">Model
 *     documentation</a>
 */
public class DataSetReaderTypeNode extends BaseObjectTypeNode implements DataSetReaderType {
  public DataSetReaderTypeNode(
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
  public @Nullable PubSubDiagnosticsDataSetReaderTypeNode getDiagnosticsNode() throws UaException {
    return ClientNodeSupport.await(getDiagnosticsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PubSubDiagnosticsDataSetReaderTypeNode>
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
                        PubSubDiagnosticsDataSetReaderTypeNode.class)));
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
  public @Nullable PropertyTypeNode getSecurityModeNode() throws UaException {
    return ClientNodeSupport.await(getSecurityModeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getSecurityModeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
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
                            false,
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
  public PropertyTypeNode getKeyFrameCountNode() throws UaException {
    return ClientNodeSupport.await(getKeyFrameCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getKeyFrameCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "KeyFrameCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readKeyFrameCount() throws UaException {
    return ClientNodeSupport.await(readKeyFrameCountAsync());
  }

  @Override
  public void writeKeyFrameCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeKeyFrameCountAsync(value)),
        "http://opcfoundation.org/UA/}KeyFrameCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readKeyFrameCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getKeyFrameCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}KeyFrameCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeKeyFrameCountAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getKeyFrameCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}KeyFrameCount",
                        value,
                        UInteger.class,
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
  public PropertyTypeNode getDataSetMetaDataNode() throws UaException {
    return ClientNodeSupport.await(getDataSetMetaDataNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetMetaDataNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DataSetMetaData",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable DataSetMetaDataType readDataSetMetaData() throws UaException {
    return ClientNodeSupport.await(readDataSetMetaDataAsync());
  }

  @Override
  public void writeDataSetMetaData(@Nullable DataSetMetaDataType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDataSetMetaDataAsync(value)),
        "http://opcfoundation.org/UA/}DataSetMetaData");
  }

  @Override
  public CompletableFuture<? extends @Nullable DataSetMetaDataType> readDataSetMetaDataAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDataSetMetaDataNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DataSetMetaData",
                            true,
                            DataSetMetaDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DataSetMetaDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetMetaDataAsync(
      @Nullable DataSetMetaDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDataSetMetaDataNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DataSetMetaData",
                        value,
                        DataSetMetaDataType.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getDataSetWriterIdNode() throws UaException {
    return ClientNodeSupport.await(getDataSetWriterIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetWriterIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DataSetWriterId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UShort readDataSetWriterId() throws UaException {
    return ClientNodeSupport.await(readDataSetWriterIdAsync());
  }

  @Override
  public void writeDataSetWriterId(@Nullable UShort value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDataSetWriterIdAsync(value)),
        "http://opcfoundation.org/UA/}DataSetWriterId");
  }

  @Override
  public CompletableFuture<? extends @Nullable UShort> readDataSetWriterIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDataSetWriterIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DataSetWriterId",
                            true,
                            UShort.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UShort) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetWriterIdAsync(@Nullable UShort value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDataSetWriterIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DataSetWriterId",
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
  public @Nullable DataSetReaderMessageTypeNode getMessageSettingsNode() throws UaException {
    return ClientNodeSupport.await(getMessageSettingsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable DataSetReaderMessageTypeNode>
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
                        DataSetReaderMessageTypeNode.class)));
  }

  @Override
  public @Nullable PropertyTypeNode getSecurityGroupIdNode() throws UaException {
    return ClientNodeSupport.await(getSecurityGroupIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getSecurityGroupIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SecurityGroupId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readSecurityGroupId() throws UaException {
    return ClientNodeSupport.await(readSecurityGroupIdAsync());
  }

  @Override
  public void writeSecurityGroupId(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSecurityGroupIdAsync(value)),
        "http://opcfoundation.org/UA/}SecurityGroupId");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readSecurityGroupIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSecurityGroupIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SecurityGroupId",
                            false,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSecurityGroupIdAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSecurityGroupIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SecurityGroupId",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public SubscribedDataSetTypeNode getSubscribedDataSetNode() throws UaException {
    return ClientNodeSupport.await(getSubscribedDataSetNodeAsync());
  }

  @Override
  public CompletableFuture<? extends SubscribedDataSetTypeNode> getSubscribedDataSetNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SubscribedDataSet",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        SubscribedDataSetTypeNode.class)));
  }

  @Override
  public @Nullable DataSetReaderTransportTypeNode getTransportSettingsNode() throws UaException {
    return ClientNodeSupport.await(getTransportSettingsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable DataSetReaderTransportTypeNode>
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
                        DataSetReaderTransportTypeNode.class)));
  }

  @Override
  public @Nullable PropertyTypeNode getSecurityKeyServicesNode() throws UaException {
    return ClientNodeSupport.await(getSecurityKeyServicesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getSecurityKeyServicesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SecurityKeyServices",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable EndpointDescription @Nullable [] readSecurityKeyServices() throws UaException {
    return ClientNodeSupport.await(readSecurityKeyServicesAsync());
  }

  @Override
  public void writeSecurityKeyServices(@Nullable EndpointDescription @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSecurityKeyServicesAsync(value)),
        "http://opcfoundation.org/UA/}SecurityKeyServices");
  }

  @Override
  public CompletableFuture<? extends @Nullable EndpointDescription @Nullable []>
      readSecurityKeyServicesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSecurityKeyServicesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SecurityKeyServices",
                            false,
                            EndpointDescription.class,
                            1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable EndpointDescription @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSecurityKeyServicesAsync(
      @Nullable EndpointDescription @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSecurityKeyServicesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SecurityKeyServices",
                        value,
                        EndpointDescription.class,
                        1,
                        null)));
  }

  @Override
  public PropertyTypeNode getMessageReceiveTimeoutNode() throws UaException {
    return ClientNodeSupport.await(getMessageReceiveTimeoutNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMessageReceiveTimeoutNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MessageReceiveTimeout",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Double readMessageReceiveTimeout() throws UaException {
    return ClientNodeSupport.await(readMessageReceiveTimeoutAsync());
  }

  @Override
  public void writeMessageReceiveTimeout(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMessageReceiveTimeoutAsync(value)),
        "http://opcfoundation.org/UA/}MessageReceiveTimeout");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readMessageReceiveTimeoutAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMessageReceiveTimeoutNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MessageReceiveTimeout",
                            true,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMessageReceiveTimeoutAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMessageReceiveTimeoutNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MessageReceiveTimeout",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getDataSetFieldContentMaskNode() throws UaException {
    return ClientNodeSupport.await(getDataSetFieldContentMaskNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetFieldContentMaskNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DataSetFieldContentMask",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable DataSetFieldContentMask readDataSetFieldContentMask() throws UaException {
    return ClientNodeSupport.await(readDataSetFieldContentMaskAsync());
  }

  @Override
  public void writeDataSetFieldContentMask(@Nullable DataSetFieldContentMask value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDataSetFieldContentMaskAsync(value)),
        "http://opcfoundation.org/UA/}DataSetFieldContentMask");
  }

  @Override
  public CompletableFuture<? extends @Nullable DataSetFieldContentMask>
      readDataSetFieldContentMaskAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDataSetFieldContentMaskNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DataSetFieldContentMask",
                            true,
                            DataSetFieldContentMask.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DataSetFieldContentMask) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetFieldContentMaskAsync(
      @Nullable DataSetFieldContentMask value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDataSetFieldContentMaskNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DataSetFieldContentMask",
                        value,
                        DataSetFieldContentMask.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getDataSetReaderPropertiesNode() throws UaException {
    return ClientNodeSupport.await(getDataSetReaderPropertiesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetReaderPropertiesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DataSetReaderProperties",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable KeyValuePair @Nullable [] readDataSetReaderProperties() throws UaException {
    return ClientNodeSupport.await(readDataSetReaderPropertiesAsync());
  }

  @Override
  public void writeDataSetReaderProperties(@Nullable KeyValuePair @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDataSetReaderPropertiesAsync(value)),
        "http://opcfoundation.org/UA/}DataSetReaderProperties");
  }

  @Override
  public CompletableFuture<? extends @Nullable KeyValuePair @Nullable []>
      readDataSetReaderPropertiesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDataSetReaderPropertiesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DataSetReaderProperties",
                            true,
                            KeyValuePair.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable KeyValuePair @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetReaderPropertiesAsync(
      @Nullable KeyValuePair @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDataSetReaderPropertiesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DataSetReaderProperties",
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
  public @Nullable UaMethodNode getCreateDataSetMirrorMethodNode() throws UaException {
    return ClientNodeSupport.await(getCreateDataSetMirrorMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getCreateDataSetMirrorMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "CreateDataSetMirror",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable NodeId createDataSetMirror(
      @Nullable String parentNodeName, @Nullable RolePermissionType @Nullable [] rolePermissions)
      throws UaException {
    return ClientNodeSupport.await(createDataSetMirrorAsync(parentNodeName, rolePermissions));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callCreateDataSetMirror(
      @Nullable String parentNodeName, @Nullable RolePermissionType @Nullable [] rolePermissions)
      throws UaException {
    return ClientNodeSupport.await(callCreateDataSetMirrorAsync(parentNodeName, rolePermissions));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callCreateDataSetMirrorWith(
      MethodCallOptions options,
      @Nullable String parentNodeName,
      @Nullable RolePermissionType @Nullable [] rolePermissions)
      throws UaException {
    return ClientNodeSupport.await(
        callCreateDataSetMirrorWithAsync(options, parentNodeName, rolePermissions));
  }

  @Override
  public CompletableFuture<@Nullable NodeId> createDataSetMirrorAsync(
      @Nullable String parentNodeName, @Nullable RolePermissionType @Nullable [] rolePermissions) {
    return ClientNodeSupport.compose(
        callCreateDataSetMirrorAsync(parentNodeName, rolePermissions),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callCreateDataSetMirrorAsync(
      @Nullable String parentNodeName, @Nullable RolePermissionType @Nullable [] rolePermissions) {
    return callCreateDataSetMirrorWithAsync(
        MethodCallOptions.DEFAULT, parentNodeName, rolePermissions);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callCreateDataSetMirrorWithAsync(
      MethodCallOptions options,
      @Nullable String parentNodeName,
      @Nullable RolePermissionType @Nullable [] rolePermissions) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new DataSetReaderTypeCreateDataSetMirror.Inputs(parentNodeName, rolePermissions)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getCreateDataSetMirrorMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return DataSetReaderTypeCreateDataSetMirror.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values)
                                        .parentNodeId();
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getCreateTargetVariablesMethodNode() throws UaException {
    return ClientNodeSupport.await(getCreateTargetVariablesMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getCreateTargetVariablesMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "CreateTargetVariables",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public StatusCode @Nullable [] createTargetVariables(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
      throws UaException {
    return ClientNodeSupport.await(
        createTargetVariablesAsync(configurationVersion, targetVariablesToAdd));
  }

  @Override
  public MethodCallResult<StatusCode @Nullable []> callCreateTargetVariables(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
      throws UaException {
    return ClientNodeSupport.await(
        callCreateTargetVariablesAsync(configurationVersion, targetVariablesToAdd));
  }

  @Override
  public MethodCallResult<StatusCode @Nullable []> callCreateTargetVariablesWith(
      MethodCallOptions options,
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
      throws UaException {
    return ClientNodeSupport.await(
        callCreateTargetVariablesWithAsync(options, configurationVersion, targetVariablesToAdd));
  }

  @Override
  public CompletableFuture<StatusCode @Nullable []> createTargetVariablesAsync(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd) {
    return ClientNodeSupport.compose(
        callCreateTargetVariablesAsync(configurationVersion, targetVariablesToAdd),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<StatusCode @Nullable []>>
      callCreateTargetVariablesAsync(
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd) {
    return callCreateTargetVariablesWithAsync(
        MethodCallOptions.DEFAULT, configurationVersion, targetVariablesToAdd);
  }

  @Override
  public CompletableFuture<MethodCallResult<StatusCode @Nullable []>>
      callCreateTargetVariablesWithAsync(
          MethodCallOptions options,
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new DataSetReaderTypeCreateTargetVariables.Inputs(
                      configurationVersion, targetVariablesToAdd)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getCreateTargetVariablesMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return DataSetReaderTypeCreateTargetVariables.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values)
                                        .addResults();
                                  }))));
        });
  }
}
