package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryEventFieldList;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link AuditHistoryEventDeleteEventType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.8">Model
 *     documentation</a>
 */
public class AuditHistoryEventDeleteEventTypeNode extends AuditHistoryDeleteEventTypeNode
    implements AuditHistoryEventDeleteEventType {
  public AuditHistoryEventDeleteEventTypeNode(
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
  public PropertyTypeNode getEventIdsNode() throws UaException {
    return ClientNodeSupport.await(getEventIdsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getEventIdsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "EventIds",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public ByteString @Nullable [] readEventIds() throws UaException {
    return ClientNodeSupport.await(readEventIdsAsync());
  }

  @Override
  public void writeEventIds(ByteString @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeEventIdsAsync(value)),
        "http://opcfoundation.org/UA/}EventIds");
  }

  @Override
  public CompletableFuture<? extends ByteString @Nullable []> readEventIdsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getEventIdsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}EventIds",
                            true,
                            ByteString.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((ByteString @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeEventIdsAsync(ByteString @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getEventIdsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}EventIds",
                        value,
                        ByteString.class,
                        1,
                        null)));
  }

  @Override
  public PropertyTypeNode getOldValuesNode() throws UaException {
    return ClientNodeSupport.await(getOldValuesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getOldValuesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "OldValues",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable HistoryEventFieldList readOldValues() throws UaException {
    return ClientNodeSupport.await(readOldValuesAsync());
  }

  @Override
  public void writeOldValues(@Nullable HistoryEventFieldList value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeOldValuesAsync(value)),
        "http://opcfoundation.org/UA/}OldValues");
  }

  @Override
  public CompletableFuture<? extends @Nullable HistoryEventFieldList> readOldValuesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getOldValuesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}OldValues",
                            true,
                            HistoryEventFieldList.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable HistoryEventFieldList) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeOldValuesAsync(@Nullable HistoryEventFieldList value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getOldValuesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}OldValues",
                        value,
                        HistoryEventFieldList.class,
                        -1,
                        null)));
  }
}
