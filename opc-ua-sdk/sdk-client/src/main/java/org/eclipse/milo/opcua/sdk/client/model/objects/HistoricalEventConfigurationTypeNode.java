package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
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
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link HistoricalEventConfigurationType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.4.3">Model
 *     documentation</a>
 */
public class HistoricalEventConfigurationTypeNode extends BaseObjectTypeNode
    implements HistoricalEventConfigurationType {
  public HistoricalEventConfigurationTypeNode(
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
  public FolderTypeNode getEventTypesNode() throws UaException {
    return ClientNodeSupport.await(getEventTypesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends FolderTypeNode> getEventTypesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "EventTypes",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        FolderTypeNode.class)));
  }

  @Override
  public @Nullable PropertyTypeNode getStartOfArchiveNode() throws UaException {
    return ClientNodeSupport.await(getStartOfArchiveNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getStartOfArchiveNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "StartOfArchive",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable DateTime readStartOfArchive() throws UaException {
    return ClientNodeSupport.await(readStartOfArchiveAsync());
  }

  @Override
  public void writeStartOfArchive(@Nullable DateTime value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeStartOfArchiveAsync(value)),
        "http://opcfoundation.org/UA/}StartOfArchive");
  }

  @Override
  public CompletableFuture<? extends @Nullable DateTime> readStartOfArchiveAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getStartOfArchiveNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}StartOfArchive",
                            false,
                            DateTime.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DateTime) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeStartOfArchiveAsync(@Nullable DateTime value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getStartOfArchiveNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}StartOfArchive",
                        value,
                        DateTime.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getSortByEventFieldsNode() throws UaException {
    return ClientNodeSupport.await(getSortByEventFieldsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getSortByEventFieldsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SortByEventFields",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable SimpleAttributeOperand @Nullable [] readSortByEventFields() throws UaException {
    return ClientNodeSupport.await(readSortByEventFieldsAsync());
  }

  @Override
  public void writeSortByEventFields(@Nullable SimpleAttributeOperand @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSortByEventFieldsAsync(value)),
        "http://opcfoundation.org/UA/}SortByEventFields");
  }

  @Override
  public CompletableFuture<? extends @Nullable SimpleAttributeOperand @Nullable []>
      readSortByEventFieldsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSortByEventFieldsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SortByEventFields",
                            false,
                            SimpleAttributeOperand.class,
                            1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable SimpleAttributeOperand @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSortByEventFieldsAsync(
      @Nullable SimpleAttributeOperand @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSortByEventFieldsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SortByEventFields",
                        value,
                        SimpleAttributeOperand.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getStartOfOnlineArchiveNode() throws UaException {
    return ClientNodeSupport.await(getStartOfOnlineArchiveNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getStartOfOnlineArchiveNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "StartOfOnlineArchive",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable DateTime readStartOfOnlineArchive() throws UaException {
    return ClientNodeSupport.await(readStartOfOnlineArchiveAsync());
  }

  @Override
  public void writeStartOfOnlineArchive(@Nullable DateTime value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeStartOfOnlineArchiveAsync(value)),
        "http://opcfoundation.org/UA/}StartOfOnlineArchive");
  }

  @Override
  public CompletableFuture<? extends @Nullable DateTime> readStartOfOnlineArchiveAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getStartOfOnlineArchiveNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}StartOfOnlineArchive",
                            false,
                            DateTime.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DateTime) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeStartOfOnlineArchiveAsync(@Nullable DateTime value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getStartOfOnlineArchiveNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}StartOfOnlineArchive",
                        value,
                        DateTime.class,
                        -1,
                        null)));
  }
}
