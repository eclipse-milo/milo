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
import org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Annotation;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link AuditHistoryAnnotationUpdateEventType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.4">Model
 *     documentation</a>
 */
public class AuditHistoryAnnotationUpdateEventTypeNode extends AuditHistoryUpdateEventTypeNode
    implements AuditHistoryAnnotationUpdateEventType {
  public AuditHistoryAnnotationUpdateEventTypeNode(
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
  public PropertyTypeNode getPerformInsertReplaceNode() throws UaException {
    return ClientNodeSupport.await(getPerformInsertReplaceNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPerformInsertReplaceNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "PerformInsertReplace",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable PerformUpdateType readPerformInsertReplace() throws UaException {
    return ClientNodeSupport.await(readPerformInsertReplaceAsync());
  }

  @Override
  public void writePerformInsertReplace(@Nullable PerformUpdateType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writePerformInsertReplaceAsync(value)),
        "http://opcfoundation.org/UA/}PerformInsertReplace");
  }

  @Override
  public CompletableFuture<? extends @Nullable PerformUpdateType> readPerformInsertReplaceAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getPerformInsertReplaceNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}PerformInsertReplace",
                            true,
                            PerformUpdateType.class,
                            -1,
                            PerformUpdateType::from)),
                v -> CompletableFuture.completedFuture((@Nullable PerformUpdateType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writePerformInsertReplaceAsync(
      @Nullable PerformUpdateType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getPerformInsertReplaceNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}PerformInsertReplace",
                        value,
                        PerformUpdateType.class,
                        -1,
                        PerformUpdateType::from)));
  }

  @Override
  public PropertyTypeNode getNewValuesNode() throws UaException {
    return ClientNodeSupport.await(getNewValuesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getNewValuesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "NewValues",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Annotation @Nullable [] readNewValues() throws UaException {
    return ClientNodeSupport.await(readNewValuesAsync());
  }

  @Override
  public void writeNewValues(@Nullable Annotation @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeNewValuesAsync(value)),
        "http://opcfoundation.org/UA/}NewValues");
  }

  @Override
  public CompletableFuture<? extends @Nullable Annotation @Nullable []> readNewValuesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getNewValuesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}NewValues",
                            true,
                            Annotation.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Annotation @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeNewValuesAsync(
      @Nullable Annotation @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getNewValuesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}NewValues",
                        value,
                        Annotation.class,
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
  public @Nullable Annotation @Nullable [] readOldValues() throws UaException {
    return ClientNodeSupport.await(readOldValuesAsync());
  }

  @Override
  public void writeOldValues(@Nullable Annotation @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeOldValuesAsync(value)),
        "http://opcfoundation.org/UA/}OldValues");
  }

  @Override
  public CompletableFuture<? extends @Nullable Annotation @Nullable []> readOldValuesAsync() {
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
                            Annotation.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Annotation @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeOldValuesAsync(
      @Nullable Annotation @Nullable [] value) {
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
                        Annotation.class,
                        1,
                        null)));
  }
}
