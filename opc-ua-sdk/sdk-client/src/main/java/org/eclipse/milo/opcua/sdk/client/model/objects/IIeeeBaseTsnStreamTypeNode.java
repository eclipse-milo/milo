package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnStreamState;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link IIeeeBaseTsnStreamType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.7">Model
 *     documentation</a>
 */
public class IIeeeBaseTsnStreamTypeNode extends BaseInterfaceTypeNode
    implements IIeeeBaseTsnStreamType {
  public IIeeeBaseTsnStreamTypeNode(
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
  public UaVariableNode getStreamNameNode() throws UaException {
    return ClientNodeSupport.await(getStreamNameNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getStreamNameNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "StreamName",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable String readStreamName() throws UaException {
    return ClientNodeSupport.await(readStreamNameAsync());
  }

  @Override
  public void writeStreamName(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeStreamNameAsync(value)),
        "http://opcfoundation.org/UA/}StreamName");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readStreamNameAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getStreamNameNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}StreamName",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeStreamNameAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getStreamNameNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}StreamName",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable UaVariableNode getAccumulatedLatencyNode() throws UaException {
    return ClientNodeSupport.await(getAccumulatedLatencyNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable UaVariableNode> getAccumulatedLatencyNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AccumulatedLatency",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readAccumulatedLatency() throws UaException {
    return ClientNodeSupport.await(readAccumulatedLatencyAsync());
  }

  @Override
  public void writeAccumulatedLatency(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeAccumulatedLatencyAsync(value)),
        "http://opcfoundation.org/UA/}AccumulatedLatency");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readAccumulatedLatencyAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getAccumulatedLatencyNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}AccumulatedLatency",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeAccumulatedLatencyAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getAccumulatedLatencyNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}AccumulatedLatency",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getStateNode() throws UaException {
    return ClientNodeSupport.await(getStateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getStateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "State",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable TsnStreamState readState() throws UaException {
    return ClientNodeSupport.await(readStateAsync());
  }

  @Override
  public void writeState(@Nullable TsnStreamState value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeStateAsync(value)), "http://opcfoundation.org/UA/}State");
  }

  @Override
  public CompletableFuture<? extends @Nullable TsnStreamState> readStateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getStateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}State",
                            true,
                            TsnStreamState.class,
                            -1,
                            TsnStreamState::from)),
                v -> CompletableFuture.completedFuture((@Nullable TsnStreamState) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeStateAsync(@Nullable TsnStreamState value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getStateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}State",
                        value,
                        TsnStreamState.class,
                        -1,
                        TsnStreamState::from)));
  }

  @Override
  public UaVariableNode getStreamIdNode() throws UaException {
    return ClientNodeSupport.await(getStreamIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getStreamIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "StreamId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public UByte @Nullable [] readStreamId() throws UaException {
    return ClientNodeSupport.await(readStreamIdAsync());
  }

  @Override
  public void writeStreamId(UByte @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeStreamIdAsync(value)),
        "http://opcfoundation.org/UA/}StreamId");
  }

  @Override
  public CompletableFuture<? extends UByte @Nullable []> readStreamIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getStreamIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}StreamId",
                            true,
                            UByte.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((UByte @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeStreamIdAsync(UByte @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getStreamIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}StreamId",
                        value,
                        UByte.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable UaVariableNode getSrClassIdNode() throws UaException {
    return ClientNodeSupport.await(getSrClassIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable UaVariableNode> getSrClassIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SrClassId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UByte readSrClassId() throws UaException {
    return ClientNodeSupport.await(readSrClassIdAsync());
  }

  @Override
  public void writeSrClassId(@Nullable UByte value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSrClassIdAsync(value)),
        "http://opcfoundation.org/UA/}SrClassId");
  }

  @Override
  public CompletableFuture<? extends @Nullable UByte> readSrClassIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSrClassIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SrClassId",
                            false,
                            UByte.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UByte) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSrClassIdAsync(@Nullable UByte value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSrClassIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SrClassId",
                        value,
                        UByte.class,
                        -1,
                        null)));
  }
}
