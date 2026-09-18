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
 * Node implementation of {@link OperationLimitsType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.11">Model
 *     documentation</a>
 */
public class OperationLimitsTypeNode extends FolderTypeNode implements OperationLimitsType {
  public OperationLimitsTypeNode(
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
  public @Nullable PropertyTypeNode getMaxNodesPerReadNode() throws UaException {
    return ClientNodeSupport.await(getMaxNodesPerReadNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxNodesPerReadNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxNodesPerRead",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxNodesPerRead() throws UaException {
    return ClientNodeSupport.await(readMaxNodesPerReadAsync());
  }

  @Override
  public void writeMaxNodesPerRead(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxNodesPerReadAsync(value)),
        "http://opcfoundation.org/UA/}MaxNodesPerRead");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerReadAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxNodesPerReadNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxNodesPerRead",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNodesPerReadAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxNodesPerReadNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxNodesPerRead",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerWriteNode() throws UaException {
    return ClientNodeSupport.await(getMaxNodesPerWriteNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxNodesPerWriteNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxNodesPerWrite",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxNodesPerWrite() throws UaException {
    return ClientNodeSupport.await(readMaxNodesPerWriteAsync());
  }

  @Override
  public void writeMaxNodesPerWrite(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxNodesPerWriteAsync(value)),
        "http://opcfoundation.org/UA/}MaxNodesPerWrite");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerWriteAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxNodesPerWriteNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxNodesPerWrite",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNodesPerWriteAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxNodesPerWriteNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxNodesPerWrite",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerBrowseNode() throws UaException {
    return ClientNodeSupport.await(getMaxNodesPerBrowseNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxNodesPerBrowseNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxNodesPerBrowse",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxNodesPerBrowse() throws UaException {
    return ClientNodeSupport.await(readMaxNodesPerBrowseAsync());
  }

  @Override
  public void writeMaxNodesPerBrowse(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxNodesPerBrowseAsync(value)),
        "http://opcfoundation.org/UA/}MaxNodesPerBrowse");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerBrowseAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxNodesPerBrowseNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxNodesPerBrowse",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNodesPerBrowseAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxNodesPerBrowseNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxNodesPerBrowse",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerMethodCallNode() throws UaException {
    return ClientNodeSupport.await(getMaxNodesPerMethodCallNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxNodesPerMethodCallNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxNodesPerMethodCall",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxNodesPerMethodCall() throws UaException {
    return ClientNodeSupport.await(readMaxNodesPerMethodCallAsync());
  }

  @Override
  public void writeMaxNodesPerMethodCall(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxNodesPerMethodCallAsync(value)),
        "http://opcfoundation.org/UA/}MaxNodesPerMethodCall");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerMethodCallAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxNodesPerMethodCallNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxNodesPerMethodCall",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNodesPerMethodCallAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxNodesPerMethodCallNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxNodesPerMethodCall",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxMonitoredItemsPerCallNode() throws UaException {
    return ClientNodeSupport.await(getMaxMonitoredItemsPerCallNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxMonitoredItemsPerCallNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxMonitoredItemsPerCall",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxMonitoredItemsPerCall() throws UaException {
    return ClientNodeSupport.await(readMaxMonitoredItemsPerCallAsync());
  }

  @Override
  public void writeMaxMonitoredItemsPerCall(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxMonitoredItemsPerCallAsync(value)),
        "http://opcfoundation.org/UA/}MaxMonitoredItemsPerCall");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxMonitoredItemsPerCallAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxMonitoredItemsPerCallNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxMonitoredItemsPerCall",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxMonitoredItemsPerCallAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxMonitoredItemsPerCallNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxMonitoredItemsPerCall",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerRegisterNodesNode() throws UaException {
    return ClientNodeSupport.await(getMaxNodesPerRegisterNodesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxNodesPerRegisterNodesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxNodesPerRegisterNodes",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxNodesPerRegisterNodes() throws UaException {
    return ClientNodeSupport.await(readMaxNodesPerRegisterNodesAsync());
  }

  @Override
  public void writeMaxNodesPerRegisterNodes(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxNodesPerRegisterNodesAsync(value)),
        "http://opcfoundation.org/UA/}MaxNodesPerRegisterNodes");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerRegisterNodesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxNodesPerRegisterNodesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxNodesPerRegisterNodes",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNodesPerRegisterNodesAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxNodesPerRegisterNodesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxNodesPerRegisterNodes",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerNodeManagementNode() throws UaException {
    return ClientNodeSupport.await(getMaxNodesPerNodeManagementNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxNodesPerNodeManagementNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxNodesPerNodeManagement",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxNodesPerNodeManagement() throws UaException {
    return ClientNodeSupport.await(readMaxNodesPerNodeManagementAsync());
  }

  @Override
  public void writeMaxNodesPerNodeManagement(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxNodesPerNodeManagementAsync(value)),
        "http://opcfoundation.org/UA/}MaxNodesPerNodeManagement");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerNodeManagementAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxNodesPerNodeManagementNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxNodesPerNodeManagement",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNodesPerNodeManagementAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxNodesPerNodeManagementNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxNodesPerNodeManagement",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerHistoryReadDataNode() throws UaException {
    return ClientNodeSupport.await(getMaxNodesPerHistoryReadDataNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxNodesPerHistoryReadDataNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxNodesPerHistoryReadData",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxNodesPerHistoryReadData() throws UaException {
    return ClientNodeSupport.await(readMaxNodesPerHistoryReadDataAsync());
  }

  @Override
  public void writeMaxNodesPerHistoryReadData(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxNodesPerHistoryReadDataAsync(value)),
        "http://opcfoundation.org/UA/}MaxNodesPerHistoryReadData");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerHistoryReadDataAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxNodesPerHistoryReadDataNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxNodesPerHistoryReadData",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNodesPerHistoryReadDataAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxNodesPerHistoryReadDataNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxNodesPerHistoryReadData",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerHistoryReadEventsNode() throws UaException {
    return ClientNodeSupport.await(getMaxNodesPerHistoryReadEventsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxNodesPerHistoryReadEventsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxNodesPerHistoryReadEvents",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxNodesPerHistoryReadEvents() throws UaException {
    return ClientNodeSupport.await(readMaxNodesPerHistoryReadEventsAsync());
  }

  @Override
  public void writeMaxNodesPerHistoryReadEvents(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxNodesPerHistoryReadEventsAsync(value)),
        "http://opcfoundation.org/UA/}MaxNodesPerHistoryReadEvents");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerHistoryReadEventsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxNodesPerHistoryReadEventsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxNodesPerHistoryReadEvents",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNodesPerHistoryReadEventsAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxNodesPerHistoryReadEventsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxNodesPerHistoryReadEvents",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerHistoryUpdateDataNode() throws UaException {
    return ClientNodeSupport.await(getMaxNodesPerHistoryUpdateDataNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxNodesPerHistoryUpdateDataNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxNodesPerHistoryUpdateData",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxNodesPerHistoryUpdateData() throws UaException {
    return ClientNodeSupport.await(readMaxNodesPerHistoryUpdateDataAsync());
  }

  @Override
  public void writeMaxNodesPerHistoryUpdateData(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxNodesPerHistoryUpdateDataAsync(value)),
        "http://opcfoundation.org/UA/}MaxNodesPerHistoryUpdateData");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerHistoryUpdateDataAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxNodesPerHistoryUpdateDataNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxNodesPerHistoryUpdateData",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNodesPerHistoryUpdateDataAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxNodesPerHistoryUpdateDataNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxNodesPerHistoryUpdateData",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerHistoryUpdateEventsNode() throws UaException {
    return ClientNodeSupport.await(getMaxNodesPerHistoryUpdateEventsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxNodesPerHistoryUpdateEventsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxNodesPerHistoryUpdateEvents",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxNodesPerHistoryUpdateEvents() throws UaException {
    return ClientNodeSupport.await(readMaxNodesPerHistoryUpdateEventsAsync());
  }

  @Override
  public void writeMaxNodesPerHistoryUpdateEvents(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxNodesPerHistoryUpdateEventsAsync(value)),
        "http://opcfoundation.org/UA/}MaxNodesPerHistoryUpdateEvents");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerHistoryUpdateEventsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxNodesPerHistoryUpdateEventsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxNodesPerHistoryUpdateEvents",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNodesPerHistoryUpdateEventsAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxNodesPerHistoryUpdateEventsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxNodesPerHistoryUpdateEvents",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerTranslateBrowsePathsToNodeIdsNode()
      throws UaException {
    return ClientNodeSupport.await(getMaxNodesPerTranslateBrowsePathsToNodeIdsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxNodesPerTranslateBrowsePathsToNodeIdsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxNodesPerTranslateBrowsePathsToNodeIds",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxNodesPerTranslateBrowsePathsToNodeIds() throws UaException {
    return ClientNodeSupport.await(readMaxNodesPerTranslateBrowsePathsToNodeIdsAsync());
  }

  @Override
  public void writeMaxNodesPerTranslateBrowsePathsToNodeIds(@Nullable UInteger value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxNodesPerTranslateBrowsePathsToNodeIdsAsync(value)),
        "http://opcfoundation.org/UA/}MaxNodesPerTranslateBrowsePathsToNodeIds");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger>
      readMaxNodesPerTranslateBrowsePathsToNodeIdsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxNodesPerTranslateBrowsePathsToNodeIdsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxNodesPerTranslateBrowsePathsToNodeIds",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNodesPerTranslateBrowsePathsToNodeIdsAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxNodesPerTranslateBrowsePathsToNodeIdsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxNodesPerTranslateBrowsePathsToNodeIds",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }
}
