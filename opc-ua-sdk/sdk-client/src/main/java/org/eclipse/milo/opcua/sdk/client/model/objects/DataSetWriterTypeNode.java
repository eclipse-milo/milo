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
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link DataSetWriterType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.7/#9.1.7.2">Model
 *     documentation</a>
 */
public class DataSetWriterTypeNode extends BaseObjectTypeNode implements DataSetWriterType {
  public DataSetWriterTypeNode(
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
  public @Nullable PubSubDiagnosticsDataSetWriterTypeNode getDiagnosticsNode() throws UaException {
    return ClientNodeSupport.await(getDiagnosticsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PubSubDiagnosticsDataSetWriterTypeNode>
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
                        PubSubDiagnosticsDataSetWriterTypeNode.class)));
  }

  @Override
  public @Nullable PropertyTypeNode getKeyFrameCountNode() throws UaException {
    return ClientNodeSupport.await(getKeyFrameCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getKeyFrameCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
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
                            false,
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
  public @Nullable DataSetWriterMessageTypeNode getMessageSettingsNode() throws UaException {
    return ClientNodeSupport.await(getMessageSettingsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable DataSetWriterMessageTypeNode>
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
                        DataSetWriterMessageTypeNode.class)));
  }

  @Override
  public @Nullable DataSetWriterTransportTypeNode getTransportSettingsNode() throws UaException {
    return ClientNodeSupport.await(getTransportSettingsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable DataSetWriterTransportTypeNode>
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
                        DataSetWriterTransportTypeNode.class)));
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
  public PropertyTypeNode getDataSetWriterPropertiesNode() throws UaException {
    return ClientNodeSupport.await(getDataSetWriterPropertiesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetWriterPropertiesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DataSetWriterProperties",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable KeyValuePair @Nullable [] readDataSetWriterProperties() throws UaException {
    return ClientNodeSupport.await(readDataSetWriterPropertiesAsync());
  }

  @Override
  public void writeDataSetWriterProperties(@Nullable KeyValuePair @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDataSetWriterPropertiesAsync(value)),
        "http://opcfoundation.org/UA/}DataSetWriterProperties");
  }

  @Override
  public CompletableFuture<? extends @Nullable KeyValuePair @Nullable []>
      readDataSetWriterPropertiesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDataSetWriterPropertiesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DataSetWriterProperties",
                            true,
                            KeyValuePair.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable KeyValuePair @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetWriterPropertiesAsync(
      @Nullable KeyValuePair @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDataSetWriterPropertiesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DataSetWriterProperties",
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
}
