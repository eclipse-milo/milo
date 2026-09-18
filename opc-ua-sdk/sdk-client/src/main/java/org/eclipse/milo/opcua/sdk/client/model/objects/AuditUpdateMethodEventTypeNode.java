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
 * Node implementation of {@link AuditUpdateMethodEventType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.27">Model
 *     documentation</a>
 */
public class AuditUpdateMethodEventTypeNode extends AuditEventTypeNode
    implements AuditUpdateMethodEventType {
  public AuditUpdateMethodEventTypeNode(
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
  public @Nullable PropertyTypeNode getStatusCodeIdNode() throws UaException {
    return ClientNodeSupport.await(getStatusCodeIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getStatusCodeIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
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
                            false,
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
  public @Nullable PropertyTypeNode getOutputArgumentsNode() throws UaException {
    return ClientNodeSupport.await(getOutputArgumentsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getOutputArgumentsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
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
                            false,
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
  public @Nullable NodeId readMethodId() throws UaException {
    return ClientNodeSupport.await(readMethodIdAsync());
  }

  @Override
  public void writeMethodId(@Nullable NodeId value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMethodIdAsync(value)),
        "http://opcfoundation.org/UA/}MethodId");
  }

  @Override
  public CompletableFuture<? extends @Nullable NodeId> readMethodIdAsync() {
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
                            NodeId.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable NodeId) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMethodIdAsync(@Nullable NodeId value) {
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
                        NodeId.class,
                        -1,
                        null)));
  }
}
