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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerStatusDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ServerStatusType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.6">Model
 *     documentation</a>
 */
public class ServerStatusTypeNode extends BaseDataVariableTypeNode implements ServerStatusType {
  public ServerStatusTypeNode(
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
  public UaVariableNode getCurrentTimeNode() throws UaException {
    return ClientNodeSupport.await(getCurrentTimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getCurrentTimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CurrentTime",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable DateTime readCurrentTime() throws UaException {
    return ClientNodeSupport.await(readCurrentTimeAsync());
  }

  @Override
  public void writeCurrentTime(@Nullable DateTime value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCurrentTimeAsync(value)),
        "http://opcfoundation.org/UA/}CurrentTime");
  }

  @Override
  public CompletableFuture<? extends @Nullable DateTime> readCurrentTimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCurrentTimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CurrentTime",
                            true,
                            DateTime.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DateTime) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCurrentTimeAsync(@Nullable DateTime value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCurrentTimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CurrentTime",
                        value,
                        DateTime.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getShutdownReasonNode() throws UaException {
    return ClientNodeSupport.await(getShutdownReasonNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getShutdownReasonNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ShutdownReason",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable LocalizedText readShutdownReason() throws UaException {
    return ClientNodeSupport.await(readShutdownReasonAsync());
  }

  @Override
  public void writeShutdownReason(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeShutdownReasonAsync(value)),
        "http://opcfoundation.org/UA/}ShutdownReason");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readShutdownReasonAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getShutdownReasonNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ShutdownReason",
                            true,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeShutdownReasonAsync(@Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getShutdownReasonNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ShutdownReason",
                        value,
                        LocalizedText.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getSecondsTillShutdownNode() throws UaException {
    return ClientNodeSupport.await(getSecondsTillShutdownNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getSecondsTillShutdownNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SecondsTillShutdown",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readSecondsTillShutdown() throws UaException {
    return ClientNodeSupport.await(readSecondsTillShutdownAsync());
  }

  @Override
  public void writeSecondsTillShutdown(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSecondsTillShutdownAsync(value)),
        "http://opcfoundation.org/UA/}SecondsTillShutdown");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readSecondsTillShutdownAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSecondsTillShutdownNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SecondsTillShutdown",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSecondsTillShutdownAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSecondsTillShutdownNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SecondsTillShutdown",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getStateNode() throws UaException {
    return ClientNodeSupport.await(getStateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getStateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "State",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServerState readState() throws UaException {
    return ClientNodeSupport.await(readStateAsync());
  }

  @Override
  public void writeState(@Nullable ServerState value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeStateAsync(value)), "http://opcfoundation.org/UA/}State");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServerState> readStateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getStateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}State",
                            true,
                            ServerState.class,
                            -1,
                            ServerState::from)),
                v -> CompletableFuture.completedFuture((@Nullable ServerState) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeStateAsync(@Nullable ServerState value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getStateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}State",
                        value,
                        ServerState.class,
                        -1,
                        ServerState::from)));
  }

  @Override
  public BuildInfoTypeNode getBuildInfoNode() throws UaException {
    return ClientNodeSupport.await(getBuildInfoNodeAsync());
  }

  @Override
  public CompletableFuture<? extends BuildInfoTypeNode> getBuildInfoNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "BuildInfo",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        BuildInfoTypeNode.class)));
  }

  @Override
  public @Nullable BuildInfo readBuildInfo() throws UaException {
    return ClientNodeSupport.await(readBuildInfoAsync());
  }

  @Override
  public void writeBuildInfo(@Nullable BuildInfo value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeBuildInfoAsync(value)),
        "http://opcfoundation.org/UA/}BuildInfo");
  }

  @Override
  public CompletableFuture<? extends @Nullable BuildInfo> readBuildInfoAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getBuildInfoNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}BuildInfo",
                            true,
                            BuildInfo.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable BuildInfo) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeBuildInfoAsync(@Nullable BuildInfo value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getBuildInfoNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}BuildInfo",
                        value,
                        BuildInfo.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getStartTimeNode() throws UaException {
    return ClientNodeSupport.await(getStartTimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getStartTimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "StartTime",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable DateTime readStartTime() throws UaException {
    return ClientNodeSupport.await(readStartTimeAsync());
  }

  @Override
  public void writeStartTime(@Nullable DateTime value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeStartTimeAsync(value)),
        "http://opcfoundation.org/UA/}StartTime");
  }

  @Override
  public CompletableFuture<? extends @Nullable DateTime> readStartTimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getStartTimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}StartTime",
                            true,
                            DateTime.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DateTime) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeStartTimeAsync(@Nullable DateTime value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getStartTimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}StartTime",
                        value,
                        DateTime.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable ServerStatusDataType readTypedValue() throws UaException {
    return ClientNodeSupport.await(readTypedValueAsync());
  }

  @Override
  public void writeTypedValue(@Nullable ServerStatusDataType value) throws UaException {
    ClientNodeSupport.good(ClientNodeSupport.await(writeTypedValueAsync(value)), "Value");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServerStatusDataType> readTypedValueAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    CompletableFuture.completedFuture(this),
                    n ->
                        ClientNodeSupport.read(
                            client, n, this, "Value", true, ServerStatusDataType.class, -1, null)),
                v -> CompletableFuture.completedFuture((@Nullable ServerStatusDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable ServerStatusDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                n ->
                    ClientNodeSupport.write(
                        client, n, this, "Value", value, ServerStatusDataType.class, -1, null)));
  }
}
