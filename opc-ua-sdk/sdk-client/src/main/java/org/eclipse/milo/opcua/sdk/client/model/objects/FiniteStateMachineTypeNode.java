package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.FiniteStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.FiniteTransitionVariableTypeNode;
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
 * Node implementation of {@link FiniteStateMachineType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.5">Model
 *     documentation</a>
 */
public class FiniteStateMachineTypeNode extends StateMachineTypeNode
    implements FiniteStateMachineType {
  public FiniteStateMachineTypeNode(
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
  public FiniteStateVariableTypeNode getCurrentStateNode() throws UaException {
    return ClientNodeSupport.await(getCurrentStateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends FiniteStateVariableTypeNode> getCurrentStateNodeAsync() {
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
                        FiniteStateVariableTypeNode.class)));
  }

  @Override
  public @Nullable FiniteTransitionVariableTypeNode getLastTransitionNode() throws UaException {
    return ClientNodeSupport.await(getLastTransitionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable FiniteTransitionVariableTypeNode>
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
                        FiniteTransitionVariableTypeNode.class)));
  }

  @Override
  public @Nullable UaVariableNode getAvailableStatesNode() throws UaException {
    return ClientNodeSupport.await(getAvailableStatesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable UaVariableNode> getAvailableStatesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AvailableStates",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public NodeId @Nullable [] readAvailableStates() throws UaException {
    return ClientNodeSupport.await(readAvailableStatesAsync());
  }

  @Override
  public void writeAvailableStates(NodeId @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeAvailableStatesAsync(value)),
        "http://opcfoundation.org/UA/}AvailableStates");
  }

  @Override
  public CompletableFuture<? extends NodeId @Nullable []> readAvailableStatesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getAvailableStatesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}AvailableStates",
                            false,
                            NodeId.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((NodeId @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeAvailableStatesAsync(NodeId @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getAvailableStatesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}AvailableStates",
                        value,
                        NodeId.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable UaVariableNode getAvailableTransitionsNode() throws UaException {
    return ClientNodeSupport.await(getAvailableTransitionsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable UaVariableNode> getAvailableTransitionsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AvailableTransitions",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public NodeId @Nullable [] readAvailableTransitions() throws UaException {
    return ClientNodeSupport.await(readAvailableTransitionsAsync());
  }

  @Override
  public void writeAvailableTransitions(NodeId @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeAvailableTransitionsAsync(value)),
        "http://opcfoundation.org/UA/}AvailableTransitions");
  }

  @Override
  public CompletableFuture<? extends NodeId @Nullable []> readAvailableTransitionsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getAvailableTransitionsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}AvailableTransitions",
                            false,
                            NodeId.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((NodeId @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeAvailableTransitionsAsync(NodeId @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getAvailableTransitionsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}AvailableTransitions",
                        value,
                        NodeId.class,
                        1,
                        null)));
  }
}
