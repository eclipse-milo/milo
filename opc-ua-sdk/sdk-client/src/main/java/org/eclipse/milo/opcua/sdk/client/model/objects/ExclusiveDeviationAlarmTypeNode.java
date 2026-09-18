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
 * Node implementation of {@link ExclusiveDeviationAlarmType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.22/#5.8.22.3">Model
 *     documentation</a>
 */
public class ExclusiveDeviationAlarmTypeNode extends ExclusiveLimitAlarmTypeNode
    implements ExclusiveDeviationAlarmType {
  public ExclusiveDeviationAlarmTypeNode(
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
  public PropertyTypeNode getSetpointNodeNode() throws UaException {
    return ClientNodeSupport.await(getSetpointNodeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSetpointNodeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SetpointNode",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable NodeId readSetpointNode() throws UaException {
    return ClientNodeSupport.await(readSetpointNodeAsync());
  }

  @Override
  public void writeSetpointNode(@Nullable NodeId value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSetpointNodeAsync(value)),
        "http://opcfoundation.org/UA/}SetpointNode");
  }

  @Override
  public CompletableFuture<? extends @Nullable NodeId> readSetpointNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSetpointNodeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SetpointNode",
                            true,
                            NodeId.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable NodeId) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSetpointNodeAsync(@Nullable NodeId value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSetpointNodeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SetpointNode",
                        value,
                        NodeId.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getBaseSetpointNodeNode() throws UaException {
    return ClientNodeSupport.await(getBaseSetpointNodeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getBaseSetpointNodeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "BaseSetpointNode",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable NodeId readBaseSetpointNode() throws UaException {
    return ClientNodeSupport.await(readBaseSetpointNodeAsync());
  }

  @Override
  public void writeBaseSetpointNode(@Nullable NodeId value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeBaseSetpointNodeAsync(value)),
        "http://opcfoundation.org/UA/}BaseSetpointNode");
  }

  @Override
  public CompletableFuture<? extends @Nullable NodeId> readBaseSetpointNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getBaseSetpointNodeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}BaseSetpointNode",
                            false,
                            NodeId.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable NodeId) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeBaseSetpointNodeAsync(@Nullable NodeId value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getBaseSetpointNodeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}BaseSetpointNode",
                        value,
                        NodeId.class,
                        -1,
                        null)));
  }
}
