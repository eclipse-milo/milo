package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.UUID;
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
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link UadpDataSetReaderMessageType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.3">Model
 *     documentation</a>
 */
public class UadpDataSetReaderMessageTypeNode extends DataSetReaderMessageTypeNode
    implements UadpDataSetReaderMessageType {
  public UadpDataSetReaderMessageTypeNode(
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
  public PropertyTypeNode getGroupVersionNode() throws UaException {
    return ClientNodeSupport.await(getGroupVersionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getGroupVersionNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "GroupVersion",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readGroupVersion() throws UaException {
    return ClientNodeSupport.await(readGroupVersionAsync());
  }

  @Override
  public void writeGroupVersion(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeGroupVersionAsync(value)),
        "http://opcfoundation.org/UA/}GroupVersion");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readGroupVersionAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getGroupVersionNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}GroupVersion",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeGroupVersionAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getGroupVersionNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}GroupVersion",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getDataSetOffsetNode() throws UaException {
    return ClientNodeSupport.await(getDataSetOffsetNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetOffsetNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DataSetOffset",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UShort readDataSetOffset() throws UaException {
    return ClientNodeSupport.await(readDataSetOffsetAsync());
  }

  @Override
  public void writeDataSetOffset(@Nullable UShort value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDataSetOffsetAsync(value)),
        "http://opcfoundation.org/UA/}DataSetOffset");
  }

  @Override
  public CompletableFuture<? extends @Nullable UShort> readDataSetOffsetAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDataSetOffsetNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DataSetOffset",
                            true,
                            UShort.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UShort) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetOffsetAsync(@Nullable UShort value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDataSetOffsetNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DataSetOffset",
                        value,
                        UShort.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getReceiveOffsetNode() throws UaException {
    return ClientNodeSupport.await(getReceiveOffsetNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getReceiveOffsetNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ReceiveOffset",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Double readReceiveOffset() throws UaException {
    return ClientNodeSupport.await(readReceiveOffsetAsync());
  }

  @Override
  public void writeReceiveOffset(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeReceiveOffsetAsync(value)),
        "http://opcfoundation.org/UA/}ReceiveOffset");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readReceiveOffsetAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getReceiveOffsetNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ReceiveOffset",
                            true,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeReceiveOffsetAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getReceiveOffsetNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ReceiveOffset",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getDataSetClassIdNode() throws UaException {
    return ClientNodeSupport.await(getDataSetClassIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetClassIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DataSetClassId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UUID readDataSetClassId() throws UaException {
    return ClientNodeSupport.await(readDataSetClassIdAsync());
  }

  @Override
  public void writeDataSetClassId(@Nullable UUID value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDataSetClassIdAsync(value)),
        "http://opcfoundation.org/UA/}DataSetClassId");
  }

  @Override
  public CompletableFuture<? extends @Nullable UUID> readDataSetClassIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDataSetClassIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DataSetClassId",
                            true,
                            UUID.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UUID) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetClassIdAsync(@Nullable UUID value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDataSetClassIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DataSetClassId",
                        value,
                        UUID.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getProcessingOffsetNode() throws UaException {
    return ClientNodeSupport.await(getProcessingOffsetNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getProcessingOffsetNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ProcessingOffset",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Double readProcessingOffset() throws UaException {
    return ClientNodeSupport.await(readProcessingOffsetAsync());
  }

  @Override
  public void writeProcessingOffset(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeProcessingOffsetAsync(value)),
        "http://opcfoundation.org/UA/}ProcessingOffset");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readProcessingOffsetAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getProcessingOffsetNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ProcessingOffset",
                            true,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeProcessingOffsetAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getProcessingOffsetNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ProcessingOffset",
                        value,
                        Double.class,
                        -1,
                        null)));
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
  public PropertyTypeNode getNetworkMessageNumberNode() throws UaException {
    return ClientNodeSupport.await(getNetworkMessageNumberNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getNetworkMessageNumberNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "NetworkMessageNumber",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UShort readNetworkMessageNumber() throws UaException {
    return ClientNodeSupport.await(readNetworkMessageNumberAsync());
  }

  @Override
  public void writeNetworkMessageNumber(@Nullable UShort value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeNetworkMessageNumberAsync(value)),
        "http://opcfoundation.org/UA/}NetworkMessageNumber");
  }

  @Override
  public CompletableFuture<? extends @Nullable UShort> readNetworkMessageNumberAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getNetworkMessageNumberNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}NetworkMessageNumber",
                            true,
                            UShort.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UShort) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeNetworkMessageNumberAsync(@Nullable UShort value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getNetworkMessageNumberNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}NetworkMessageNumber",
                        value,
                        UShort.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getDataSetMessageContentMaskNode() throws UaException {
    return ClientNodeSupport.await(getDataSetMessageContentMaskNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetMessageContentMaskNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DataSetMessageContentMask",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UadpDataSetMessageContentMask readDataSetMessageContentMask()
      throws UaException {
    return ClientNodeSupport.await(readDataSetMessageContentMaskAsync());
  }

  @Override
  public void writeDataSetMessageContentMask(@Nullable UadpDataSetMessageContentMask value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDataSetMessageContentMaskAsync(value)),
        "http://opcfoundation.org/UA/}DataSetMessageContentMask");
  }

  @Override
  public CompletableFuture<? extends @Nullable UadpDataSetMessageContentMask>
      readDataSetMessageContentMaskAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDataSetMessageContentMaskNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DataSetMessageContentMask",
                            true,
                            UadpDataSetMessageContentMask.class,
                            -1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable UadpDataSetMessageContentMask) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetMessageContentMaskAsync(
      @Nullable UadpDataSetMessageContentMask value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDataSetMessageContentMaskNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DataSetMessageContentMask",
                        value,
                        UadpDataSetMessageContentMask.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getNetworkMessageContentMaskNode() throws UaException {
    return ClientNodeSupport.await(getNetworkMessageContentMaskNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getNetworkMessageContentMaskNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "NetworkMessageContentMask",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UadpNetworkMessageContentMask readNetworkMessageContentMask()
      throws UaException {
    return ClientNodeSupport.await(readNetworkMessageContentMaskAsync());
  }

  @Override
  public void writeNetworkMessageContentMask(@Nullable UadpNetworkMessageContentMask value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeNetworkMessageContentMaskAsync(value)),
        "http://opcfoundation.org/UA/}NetworkMessageContentMask");
  }

  @Override
  public CompletableFuture<? extends @Nullable UadpNetworkMessageContentMask>
      readNetworkMessageContentMaskAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getNetworkMessageContentMaskNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}NetworkMessageContentMask",
                            true,
                            UadpNetworkMessageContentMask.class,
                            -1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable UadpNetworkMessageContentMask) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeNetworkMessageContentMaskAsync(
      @Nullable UadpNetworkMessageContentMask value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getNetworkMessageContentMaskNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}NetworkMessageContentMask",
                        value,
                        UadpNetworkMessageContentMask.class,
                        -1,
                        null)));
  }
}
