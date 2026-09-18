package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.FiniteStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.FiniteTransitionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.ProgramDiagnostic2TypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaObjectNode;
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
import org.eclipse.milo.opcua.stack.core.types.structured.ProgramDiagnostic2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ProgramStateMachineType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.1">Model
 *     documentation</a>
 */
public class ProgramStateMachineTypeNode extends FiniteStateMachineTypeNode
    implements ProgramStateMachineType {
  public ProgramStateMachineTypeNode(
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
  public PropertyTypeNode getAutoDeleteNode() throws UaException {
    return ClientNodeSupport.await(getAutoDeleteNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getAutoDeleteNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AutoDelete",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readAutoDelete() throws UaException {
    return ClientNodeSupport.await(readAutoDeleteAsync());
  }

  @Override
  public void writeAutoDelete(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeAutoDeleteAsync(value)),
        "http://opcfoundation.org/UA/}AutoDelete");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readAutoDeleteAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getAutoDeleteNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}AutoDelete",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeAutoDeleteAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getAutoDeleteNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}AutoDelete",
                        value,
                        Boolean.class,
                        -1,
                        null)));
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
  public PropertyTypeNode getRecycleCountNode() throws UaException {
    return ClientNodeSupport.await(getRecycleCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getRecycleCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "RecycleCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Integer readRecycleCount() throws UaException {
    return ClientNodeSupport.await(readRecycleCountAsync());
  }

  @Override
  public void writeRecycleCount(@Nullable Integer value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeRecycleCountAsync(value)),
        "http://opcfoundation.org/UA/}RecycleCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable Integer> readRecycleCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getRecycleCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}RecycleCount",
                            true,
                            Integer.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Integer) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeRecycleCountAsync(@Nullable Integer value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getRecycleCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}RecycleCount",
                        value,
                        Integer.class,
                        -1,
                        null)));
  }

  @Override
  public FiniteTransitionVariableTypeNode getLastTransitionNode() throws UaException {
    return ClientNodeSupport.await(getLastTransitionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends FiniteTransitionVariableTypeNode>
      getLastTransitionNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LastTransition",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        FiniteTransitionVariableTypeNode.class)));
  }

  @Override
  public @Nullable UaObjectNode getFinalResultDataNode() throws UaException {
    return ClientNodeSupport.await(getFinalResultDataNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable UaObjectNode> getFinalResultDataNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "FinalResultData",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        UaObjectNode.class)));
  }

  @Override
  public @Nullable ProgramDiagnostic2TypeNode getProgramDiagnosticNode() throws UaException {
    return ClientNodeSupport.await(getProgramDiagnosticNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable ProgramDiagnostic2TypeNode>
      getProgramDiagnosticNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ProgramDiagnostic",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        ProgramDiagnostic2TypeNode.class)));
  }

  @Override
  public @Nullable ProgramDiagnostic2DataType readProgramDiagnostic() throws UaException {
    return ClientNodeSupport.await(readProgramDiagnosticAsync());
  }

  @Override
  public void writeProgramDiagnostic(@Nullable ProgramDiagnostic2DataType value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeProgramDiagnosticAsync(value)),
        "http://opcfoundation.org/UA/}ProgramDiagnostic");
  }

  @Override
  public CompletableFuture<? extends @Nullable ProgramDiagnostic2DataType>
      readProgramDiagnosticAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getProgramDiagnosticNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ProgramDiagnostic",
                            false,
                            ProgramDiagnostic2DataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ProgramDiagnostic2DataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeProgramDiagnosticAsync(
      @Nullable ProgramDiagnostic2DataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getProgramDiagnosticNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ProgramDiagnostic",
                        value,
                        ProgramDiagnostic2DataType.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getDeletableNode() throws UaException {
    return ClientNodeSupport.await(getDeletableNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDeletableNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Deletable",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readDeletable() throws UaException {
    return ClientNodeSupport.await(readDeletableAsync());
  }

  @Override
  public void writeDeletable(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDeletableAsync(value)),
        "http://opcfoundation.org/UA/}Deletable");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readDeletableAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDeletableNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Deletable",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDeletableAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDeletableNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Deletable",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }
}
