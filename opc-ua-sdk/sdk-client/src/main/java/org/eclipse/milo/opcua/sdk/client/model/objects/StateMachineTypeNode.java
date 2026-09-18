package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.StateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.TransitionVariableTypeNode;
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
 * Node implementation of {@link StateMachineType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.2">Model
 *     documentation</a>
 */
public class StateMachineTypeNode extends BaseObjectTypeNode implements StateMachineType {
  public StateMachineTypeNode(
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
  public StateVariableTypeNode getCurrentStateNode() throws UaException {
    return ClientNodeSupport.await(getCurrentStateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends StateVariableTypeNode> getCurrentStateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CurrentState",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        StateVariableTypeNode.class)));
  }

  @Override
  public @Nullable LocalizedText readCurrentState() throws UaException {
    return ClientNodeSupport.await(readCurrentStateAsync());
  }

  @Override
  public void writeCurrentState(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCurrentStateAsync(value)),
        "http://opcfoundation.org/UA/}CurrentState");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readCurrentStateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCurrentStateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CurrentState",
                            true,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCurrentStateAsync(@Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCurrentStateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CurrentState",
                        value,
                        LocalizedText.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable TransitionVariableTypeNode getLastTransitionNode() throws UaException {
    return ClientNodeSupport.await(getLastTransitionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable TransitionVariableTypeNode>
      getLastTransitionNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LastTransition",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        TransitionVariableTypeNode.class)));
  }

  @Override
  public @Nullable LocalizedText readLastTransition() throws UaException {
    return ClientNodeSupport.await(readLastTransitionAsync());
  }

  @Override
  public void writeLastTransition(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLastTransitionAsync(value)),
        "http://opcfoundation.org/UA/}LastTransition");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readLastTransitionAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLastTransitionNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LastTransition",
                            false,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLastTransitionAsync(@Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLastTransitionNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LastTransition",
                        value,
                        LocalizedText.class,
                        -1,
                        null)));
  }
}
