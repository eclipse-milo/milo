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
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataSetOrderingType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link UadpWriterGroupMessageType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.1">Model
 *     documentation</a>
 */
public class UadpWriterGroupMessageTypeNode extends WriterGroupMessageTypeNode
    implements UadpWriterGroupMessageType {
  public UadpWriterGroupMessageTypeNode(
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
  public @Nullable PropertyTypeNode getSamplingOffsetNode() throws UaException {
    return ClientNodeSupport.await(getSamplingOffsetNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getSamplingOffsetNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SamplingOffset",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Double readSamplingOffset() throws UaException {
    return ClientNodeSupport.await(readSamplingOffsetAsync());
  }

  @Override
  public void writeSamplingOffset(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSamplingOffsetAsync(value)),
        "http://opcfoundation.org/UA/}SamplingOffset");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readSamplingOffsetAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSamplingOffsetNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SamplingOffset",
                            false,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSamplingOffsetAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSamplingOffsetNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SamplingOffset",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getDataSetOrderingNode() throws UaException {
    return ClientNodeSupport.await(getDataSetOrderingNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetOrderingNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DataSetOrdering",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable DataSetOrderingType readDataSetOrdering() throws UaException {
    return ClientNodeSupport.await(readDataSetOrderingAsync());
  }

  @Override
  public void writeDataSetOrdering(@Nullable DataSetOrderingType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDataSetOrderingAsync(value)),
        "http://opcfoundation.org/UA/}DataSetOrdering");
  }

  @Override
  public CompletableFuture<? extends @Nullable DataSetOrderingType> readDataSetOrderingAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDataSetOrderingNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DataSetOrdering",
                            true,
                            DataSetOrderingType.class,
                            -1,
                            DataSetOrderingType::from)),
                v -> CompletableFuture.completedFuture((@Nullable DataSetOrderingType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetOrderingAsync(
      @Nullable DataSetOrderingType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDataSetOrderingNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DataSetOrdering",
                        value,
                        DataSetOrderingType.class,
                        -1,
                        DataSetOrderingType::from)));
  }

  @Override
  public PropertyTypeNode getPublishingOffsetNode() throws UaException {
    return ClientNodeSupport.await(getPublishingOffsetNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPublishingOffsetNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "PublishingOffset",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public Double @Nullable [] readPublishingOffset() throws UaException {
    return ClientNodeSupport.await(readPublishingOffsetAsync());
  }

  @Override
  public void writePublishingOffset(Double @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writePublishingOffsetAsync(value)),
        "http://opcfoundation.org/UA/}PublishingOffset");
  }

  @Override
  public CompletableFuture<? extends Double @Nullable []> readPublishingOffsetAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getPublishingOffsetNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}PublishingOffset",
                            true,
                            Double.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((Double @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writePublishingOffsetAsync(Double @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getPublishingOffsetNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}PublishingOffset",
                        value,
                        Double.class,
                        1,
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
