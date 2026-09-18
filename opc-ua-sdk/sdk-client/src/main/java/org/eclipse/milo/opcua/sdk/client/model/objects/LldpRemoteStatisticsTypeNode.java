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
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link LldpRemoteStatisticsType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.4">Model
 *     documentation</a>
 */
public class LldpRemoteStatisticsTypeNode extends BaseObjectTypeNode
    implements LldpRemoteStatisticsType {
  public LldpRemoteStatisticsTypeNode(
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
  public UaVariableNode getRemoteDropsNode() throws UaException {
    return ClientNodeSupport.await(getRemoteDropsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getRemoteDropsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "RemoteDrops",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readRemoteDrops() throws UaException {
    return ClientNodeSupport.await(readRemoteDropsAsync());
  }

  @Override
  public void writeRemoteDrops(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeRemoteDropsAsync(value)),
        "http://opcfoundation.org/UA/}RemoteDrops");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readRemoteDropsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getRemoteDropsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}RemoteDrops",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeRemoteDropsAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getRemoteDropsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}RemoteDrops",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getRemoteAgeoutsNode() throws UaException {
    return ClientNodeSupport.await(getRemoteAgeoutsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getRemoteAgeoutsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "RemoteAgeouts",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readRemoteAgeouts() throws UaException {
    return ClientNodeSupport.await(readRemoteAgeoutsAsync());
  }

  @Override
  public void writeRemoteAgeouts(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeRemoteAgeoutsAsync(value)),
        "http://opcfoundation.org/UA/}RemoteAgeouts");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readRemoteAgeoutsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getRemoteAgeoutsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}RemoteAgeouts",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeRemoteAgeoutsAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getRemoteAgeoutsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}RemoteAgeouts",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getRemoteDeletesNode() throws UaException {
    return ClientNodeSupport.await(getRemoteDeletesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getRemoteDeletesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "RemoteDeletes",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readRemoteDeletes() throws UaException {
    return ClientNodeSupport.await(readRemoteDeletesAsync());
  }

  @Override
  public void writeRemoteDeletes(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeRemoteDeletesAsync(value)),
        "http://opcfoundation.org/UA/}RemoteDeletes");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readRemoteDeletesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getRemoteDeletesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}RemoteDeletes",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeRemoteDeletesAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getRemoteDeletesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}RemoteDeletes",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getRemoteInsertsNode() throws UaException {
    return ClientNodeSupport.await(getRemoteInsertsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getRemoteInsertsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "RemoteInserts",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readRemoteInserts() throws UaException {
    return ClientNodeSupport.await(readRemoteInsertsAsync());
  }

  @Override
  public void writeRemoteInserts(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeRemoteInsertsAsync(value)),
        "http://opcfoundation.org/UA/}RemoteInserts");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readRemoteInsertsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getRemoteInsertsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}RemoteInserts",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeRemoteInsertsAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getRemoteInsertsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}RemoteInserts",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getLastChangeTimeNode() throws UaException {
    return ClientNodeSupport.await(getLastChangeTimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getLastChangeTimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LastChangeTime",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readLastChangeTime() throws UaException {
    return ClientNodeSupport.await(readLastChangeTimeAsync());
  }

  @Override
  public void writeLastChangeTime(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLastChangeTimeAsync(value)),
        "http://opcfoundation.org/UA/}LastChangeTime");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readLastChangeTimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLastChangeTimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LastChangeTime",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLastChangeTimeAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLastChangeTimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LastChangeTime",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }
}
