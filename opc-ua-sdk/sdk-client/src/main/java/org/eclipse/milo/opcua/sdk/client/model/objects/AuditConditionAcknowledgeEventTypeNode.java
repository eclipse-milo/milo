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
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link AuditConditionAcknowledgeEventType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.10.6">Model
 *     documentation</a>
 */
public class AuditConditionAcknowledgeEventTypeNode extends AuditConditionEventTypeNode
    implements AuditConditionAcknowledgeEventType {
  public AuditConditionAcknowledgeEventTypeNode(
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
  public PropertyTypeNode getConditionEventIdNode() throws UaException {
    return ClientNodeSupport.await(getConditionEventIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getConditionEventIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ConditionEventId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable ByteString readConditionEventId() throws UaException {
    return ClientNodeSupport.await(readConditionEventIdAsync());
  }

  @Override
  public void writeConditionEventId(@Nullable ByteString value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeConditionEventIdAsync(value)),
        "http://opcfoundation.org/UA/}ConditionEventId");
  }

  @Override
  public CompletableFuture<? extends @Nullable ByteString> readConditionEventIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getConditionEventIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ConditionEventId",
                            true,
                            ByteString.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ByteString) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeConditionEventIdAsync(@Nullable ByteString value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getConditionEventIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ConditionEventId",
                        value,
                        ByteString.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getCommentNode() throws UaException {
    return ClientNodeSupport.await(getCommentNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getCommentNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Comment",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable LocalizedText readComment() throws UaException {
    return ClientNodeSupport.await(readCommentAsync());
  }

  @Override
  public void writeComment(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCommentAsync(value)), "http://opcfoundation.org/UA/}Comment");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readCommentAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCommentNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Comment",
                            true,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCommentAsync(@Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCommentNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Comment",
                        value,
                        LocalizedText.class,
                        -1,
                        null)));
  }
}
