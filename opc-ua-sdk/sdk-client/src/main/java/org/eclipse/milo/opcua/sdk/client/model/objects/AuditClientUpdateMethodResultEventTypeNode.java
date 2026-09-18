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
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link AuditClientUpdateMethodResultEventType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.37">Model
 *     documentation</a>
 */
public class AuditClientUpdateMethodResultEventTypeNode extends AuditClientEventTypeNode
    implements AuditClientUpdateMethodResultEventType {
  public AuditClientUpdateMethodResultEventTypeNode(
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
  public PropertyTypeNode getStatusCodeIdNode() throws UaException {
    return ClientNodeSupport.await(getStatusCodeIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getStatusCodeIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "StatusCodeId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable StatusCode readStatusCodeId() throws UaException {
    return ClientNodeSupport.await(readStatusCodeIdAsync());
  }

  @Override
  public void writeStatusCodeId(@Nullable StatusCode value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeStatusCodeIdAsync(value)),
        "http://opcfoundation.org/UA/}StatusCodeId");
  }

  @Override
  public CompletableFuture<? extends @Nullable StatusCode> readStatusCodeIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getStatusCodeIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}StatusCodeId",
                            true,
                            StatusCode.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable StatusCode) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeStatusCodeIdAsync(@Nullable StatusCode value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getStatusCodeIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}StatusCodeId",
                        value,
                        StatusCode.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getInputArgumentsNode() throws UaException {
    return ClientNodeSupport.await(getInputArgumentsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getInputArgumentsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "InputArguments",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Variant @Nullable [] readInputArguments() throws UaException {
    return ClientNodeSupport.await(readInputArgumentsAsync());
  }

  @Override
  public void writeInputArguments(@Nullable Variant @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeInputArgumentsAsync(value)),
        "http://opcfoundation.org/UA/}InputArguments");
  }

  @Override
  public CompletableFuture<? extends @Nullable Variant @Nullable []> readInputArgumentsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getInputArgumentsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}InputArguments",
                            true,
                            Variant.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Variant @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeInputArgumentsAsync(
      @Nullable Variant @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getInputArgumentsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}InputArguments",
                        value,
                        Variant.class,
                        1,
                        null)));
  }

  @Override
  public PropertyTypeNode getOutputArgumentsNode() throws UaException {
    return ClientNodeSupport.await(getOutputArgumentsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getOutputArgumentsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "OutputArguments",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Variant @Nullable [] readOutputArguments() throws UaException {
    return ClientNodeSupport.await(readOutputArgumentsAsync());
  }

  @Override
  public void writeOutputArguments(@Nullable Variant @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeOutputArgumentsAsync(value)),
        "http://opcfoundation.org/UA/}OutputArguments");
  }

  @Override
  public CompletableFuture<? extends @Nullable Variant @Nullable []> readOutputArgumentsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getOutputArgumentsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}OutputArguments",
                            true,
                            Variant.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Variant @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeOutputArgumentsAsync(
      @Nullable Variant @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getOutputArgumentsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}OutputArguments",
                        value,
                        Variant.class,
                        1,
                        null)));
  }

  @Override
  public PropertyTypeNode getMethodIdNode() throws UaException {
    return ClientNodeSupport.await(getMethodIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMethodIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MethodId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable ExpandedNodeId readMethodId() throws UaException {
    return ClientNodeSupport.await(readMethodIdAsync());
  }

  @Override
  public void writeMethodId(@Nullable ExpandedNodeId value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMethodIdAsync(value)),
        "http://opcfoundation.org/UA/}MethodId");
  }

  @Override
  public CompletableFuture<? extends @Nullable ExpandedNodeId> readMethodIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMethodIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MethodId",
                            true,
                            ExpandedNodeId.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ExpandedNodeId) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMethodIdAsync(@Nullable ExpandedNodeId value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMethodIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MethodId",
                        value,
                        ExpandedNodeId.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getObjectIdNode() throws UaException {
    return ClientNodeSupport.await(getObjectIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getObjectIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ObjectId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable ExpandedNodeId readObjectId() throws UaException {
    return ClientNodeSupport.await(readObjectIdAsync());
  }

  @Override
  public void writeObjectId(@Nullable ExpandedNodeId value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeObjectIdAsync(value)),
        "http://opcfoundation.org/UA/}ObjectId");
  }

  @Override
  public CompletableFuture<? extends @Nullable ExpandedNodeId> readObjectIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getObjectIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ObjectId",
                            true,
                            ExpandedNodeId.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ExpandedNodeId) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeObjectIdAsync(@Nullable ExpandedNodeId value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getObjectIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ObjectId",
                        value,
                        ExpandedNodeId.class,
                        -1,
                        null)));
  }
}
