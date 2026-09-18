package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.ProgramDiagnostic2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ProgramDiagnostic2Type}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.9">Model
 *     documentation</a>
 */
public class ProgramDiagnostic2TypeNode extends BaseDataVariableTypeNode
    implements ProgramDiagnostic2Type {
  public ProgramDiagnostic2TypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger @Nullable [] arrayDimensions,
      UByte accessLevel,
      UByte userAccessLevel,
      Double minimumSamplingInterval,
      Boolean historizing,
      @Nullable AccessLevelExType accessLevelEx) {
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
        value,
        dataType,
        valueRank,
        arrayDimensions,
        accessLevel,
        userAccessLevel,
        minimumSamplingInterval,
        historizing,
        accessLevelEx);
  }

  @Override
  public UaVariableNode getLastMethodCallNode() throws UaException {
    return ClientNodeSupport.await(getLastMethodCallNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getLastMethodCallNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LastMethodCall",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable String readLastMethodCall() throws UaException {
    return ClientNodeSupport.await(readLastMethodCallAsync());
  }

  @Override
  public void writeLastMethodCall(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLastMethodCallAsync(value)),
        "http://opcfoundation.org/UA/}LastMethodCall");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readLastMethodCallAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLastMethodCallNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LastMethodCall",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLastMethodCallAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLastMethodCallNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LastMethodCall",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getCreateSessionIdNode() throws UaException {
    return ClientNodeSupport.await(getCreateSessionIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getCreateSessionIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CreateSessionId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable NodeId readCreateSessionId() throws UaException {
    return ClientNodeSupport.await(readCreateSessionIdAsync());
  }

  @Override
  public void writeCreateSessionId(@Nullable NodeId value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCreateSessionIdAsync(value)),
        "http://opcfoundation.org/UA/}CreateSessionId");
  }

  @Override
  public CompletableFuture<? extends @Nullable NodeId> readCreateSessionIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCreateSessionIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CreateSessionId",
                            true,
                            NodeId.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable NodeId) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCreateSessionIdAsync(@Nullable NodeId value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCreateSessionIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CreateSessionId",
                        value,
                        NodeId.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getCreateClientNameNode() throws UaException {
    return ClientNodeSupport.await(getCreateClientNameNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getCreateClientNameNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CreateClientName",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable String readCreateClientName() throws UaException {
    return ClientNodeSupport.await(readCreateClientNameAsync());
  }

  @Override
  public void writeCreateClientName(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCreateClientNameAsync(value)),
        "http://opcfoundation.org/UA/}CreateClientName");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readCreateClientNameAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCreateClientNameNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CreateClientName",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCreateClientNameAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCreateClientNameNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CreateClientName",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getLastMethodCallTimeNode() throws UaException {
    return ClientNodeSupport.await(getLastMethodCallTimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getLastMethodCallTimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LastMethodCallTime",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable DateTime readLastMethodCallTime() throws UaException {
    return ClientNodeSupport.await(readLastMethodCallTimeAsync());
  }

  @Override
  public void writeLastMethodCallTime(@Nullable DateTime value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLastMethodCallTimeAsync(value)),
        "http://opcfoundation.org/UA/}LastMethodCallTime");
  }

  @Override
  public CompletableFuture<? extends @Nullable DateTime> readLastMethodCallTimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLastMethodCallTimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LastMethodCallTime",
                            true,
                            DateTime.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DateTime) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLastMethodCallTimeAsync(@Nullable DateTime value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLastMethodCallTimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LastMethodCallTime",
                        value,
                        DateTime.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getLastTransitionTimeNode() throws UaException {
    return ClientNodeSupport.await(getLastTransitionTimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLastTransitionTimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LastTransitionTime",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable DateTime readLastTransitionTime() throws UaException {
    return ClientNodeSupport.await(readLastTransitionTimeAsync());
  }

  @Override
  public void writeLastTransitionTime(@Nullable DateTime value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLastTransitionTimeAsync(value)),
        "http://opcfoundation.org/UA/}LastTransitionTime");
  }

  @Override
  public CompletableFuture<? extends @Nullable DateTime> readLastTransitionTimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLastTransitionTimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LastTransitionTime",
                            true,
                            DateTime.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DateTime) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLastTransitionTimeAsync(@Nullable DateTime value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLastTransitionTimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LastTransitionTime",
                        value,
                        DateTime.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getLastMethodSessionIdNode() throws UaException {
    return ClientNodeSupport.await(getLastMethodSessionIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getLastMethodSessionIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LastMethodSessionId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable NodeId readLastMethodSessionId() throws UaException {
    return ClientNodeSupport.await(readLastMethodSessionIdAsync());
  }

  @Override
  public void writeLastMethodSessionId(@Nullable NodeId value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLastMethodSessionIdAsync(value)),
        "http://opcfoundation.org/UA/}LastMethodSessionId");
  }

  @Override
  public CompletableFuture<? extends @Nullable NodeId> readLastMethodSessionIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLastMethodSessionIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LastMethodSessionId",
                            true,
                            NodeId.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable NodeId) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLastMethodSessionIdAsync(@Nullable NodeId value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLastMethodSessionIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LastMethodSessionId",
                        value,
                        NodeId.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getLastMethodInputValuesNode() throws UaException {
    return ClientNodeSupport.await(getLastMethodInputValuesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getLastMethodInputValuesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LastMethodInputValues",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable Variant @Nullable [] readLastMethodInputValues() throws UaException {
    return ClientNodeSupport.await(readLastMethodInputValuesAsync());
  }

  @Override
  public void writeLastMethodInputValues(@Nullable Variant @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLastMethodInputValuesAsync(value)),
        "http://opcfoundation.org/UA/}LastMethodInputValues");
  }

  @Override
  public CompletableFuture<? extends @Nullable Variant @Nullable []>
      readLastMethodInputValuesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLastMethodInputValuesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LastMethodInputValues",
                            true,
                            Variant.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Variant @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLastMethodInputValuesAsync(
      @Nullable Variant @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLastMethodInputValuesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LastMethodInputValues",
                        value,
                        Variant.class,
                        1,
                        null)));
  }

  @Override
  public UaVariableNode getInvocationCreationTimeNode() throws UaException {
    return ClientNodeSupport.await(getInvocationCreationTimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getInvocationCreationTimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "InvocationCreationTime",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable DateTime readInvocationCreationTime() throws UaException {
    return ClientNodeSupport.await(readInvocationCreationTimeAsync());
  }

  @Override
  public void writeInvocationCreationTime(@Nullable DateTime value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeInvocationCreationTimeAsync(value)),
        "http://opcfoundation.org/UA/}InvocationCreationTime");
  }

  @Override
  public CompletableFuture<? extends @Nullable DateTime> readInvocationCreationTimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getInvocationCreationTimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}InvocationCreationTime",
                            true,
                            DateTime.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DateTime) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeInvocationCreationTimeAsync(@Nullable DateTime value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getInvocationCreationTimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}InvocationCreationTime",
                        value,
                        DateTime.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getLastMethodOutputValuesNode() throws UaException {
    return ClientNodeSupport.await(getLastMethodOutputValuesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getLastMethodOutputValuesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LastMethodOutputValues",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable Variant @Nullable [] readLastMethodOutputValues() throws UaException {
    return ClientNodeSupport.await(readLastMethodOutputValuesAsync());
  }

  @Override
  public void writeLastMethodOutputValues(@Nullable Variant @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLastMethodOutputValuesAsync(value)),
        "http://opcfoundation.org/UA/}LastMethodOutputValues");
  }

  @Override
  public CompletableFuture<? extends @Nullable Variant @Nullable []>
      readLastMethodOutputValuesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLastMethodOutputValuesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LastMethodOutputValues",
                            true,
                            Variant.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Variant @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLastMethodOutputValuesAsync(
      @Nullable Variant @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLastMethodOutputValuesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LastMethodOutputValues",
                        value,
                        Variant.class,
                        1,
                        null)));
  }

  @Override
  public UaVariableNode getLastMethodReturnStatusNode() throws UaException {
    return ClientNodeSupport.await(getLastMethodReturnStatusNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getLastMethodReturnStatusNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LastMethodReturnStatus",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable StatusCode readLastMethodReturnStatus() throws UaException {
    return ClientNodeSupport.await(readLastMethodReturnStatusAsync());
  }

  @Override
  public void writeLastMethodReturnStatus(@Nullable StatusCode value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLastMethodReturnStatusAsync(value)),
        "http://opcfoundation.org/UA/}LastMethodReturnStatus");
  }

  @Override
  public CompletableFuture<? extends @Nullable StatusCode> readLastMethodReturnStatusAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLastMethodReturnStatusNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LastMethodReturnStatus",
                            true,
                            StatusCode.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable StatusCode) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLastMethodReturnStatusAsync(
      @Nullable StatusCode value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLastMethodReturnStatusNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LastMethodReturnStatus",
                        value,
                        StatusCode.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getLastMethodInputArgumentsNode() throws UaException {
    return ClientNodeSupport.await(getLastMethodInputArgumentsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getLastMethodInputArgumentsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LastMethodInputArguments",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable Argument @Nullable [] readLastMethodInputArguments() throws UaException {
    return ClientNodeSupport.await(readLastMethodInputArgumentsAsync());
  }

  @Override
  public void writeLastMethodInputArguments(@Nullable Argument @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLastMethodInputArgumentsAsync(value)),
        "http://opcfoundation.org/UA/}LastMethodInputArguments");
  }

  @Override
  public CompletableFuture<? extends @Nullable Argument @Nullable []>
      readLastMethodInputArgumentsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLastMethodInputArgumentsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LastMethodInputArguments",
                            true,
                            Argument.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Argument @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLastMethodInputArgumentsAsync(
      @Nullable Argument @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLastMethodInputArgumentsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LastMethodInputArguments",
                        value,
                        Argument.class,
                        1,
                        null)));
  }

  @Override
  public UaVariableNode getLastMethodOutputArgumentsNode() throws UaException {
    return ClientNodeSupport.await(getLastMethodOutputArgumentsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getLastMethodOutputArgumentsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LastMethodOutputArguments",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable Argument @Nullable [] readLastMethodOutputArguments() throws UaException {
    return ClientNodeSupport.await(readLastMethodOutputArgumentsAsync());
  }

  @Override
  public void writeLastMethodOutputArguments(@Nullable Argument @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLastMethodOutputArgumentsAsync(value)),
        "http://opcfoundation.org/UA/}LastMethodOutputArguments");
  }

  @Override
  public CompletableFuture<? extends @Nullable Argument @Nullable []>
      readLastMethodOutputArgumentsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLastMethodOutputArgumentsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LastMethodOutputArguments",
                            true,
                            Argument.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Argument @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLastMethodOutputArgumentsAsync(
      @Nullable Argument @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLastMethodOutputArgumentsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LastMethodOutputArguments",
                        value,
                        Argument.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable ProgramDiagnostic2DataType readTypedValue() throws UaException {
    return ClientNodeSupport.await(readTypedValueAsync());
  }

  @Override
  public void writeTypedValue(@Nullable ProgramDiagnostic2DataType value) throws UaException {
    ClientNodeSupport.good(ClientNodeSupport.await(writeTypedValueAsync(value)), "Value");
  }

  @Override
  public CompletableFuture<? extends @Nullable ProgramDiagnostic2DataType> readTypedValueAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    CompletableFuture.completedFuture(this),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "Value",
                            true,
                            ProgramDiagnostic2DataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ProgramDiagnostic2DataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTypedValueAsync(
      @Nullable ProgramDiagnostic2DataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "Value",
                        value,
                        ProgramDiagnostic2DataType.class,
                        -1,
                        null)));
  }
}
