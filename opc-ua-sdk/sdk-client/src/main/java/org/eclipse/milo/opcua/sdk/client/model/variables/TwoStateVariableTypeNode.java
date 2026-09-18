package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
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
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link TwoStateVariableType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">Model
 *     documentation</a>
 */
public class TwoStateVariableTypeNode extends StateVariableTypeNode
    implements TwoStateVariableType {
  public TwoStateVariableTypeNode(
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
  public @Nullable PropertyTypeNode getFalseStateNode() throws UaException {
    return ClientNodeSupport.await(getFalseStateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getFalseStateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "FalseState",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable LocalizedText readFalseState() throws UaException {
    return ClientNodeSupport.await(readFalseStateAsync());
  }

  @Override
  public void writeFalseState(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeFalseStateAsync(value)),
        "http://opcfoundation.org/UA/}FalseState");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readFalseStateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getFalseStateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}FalseState",
                            false,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeFalseStateAsync(@Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getFalseStateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}FalseState",
                        value,
                        LocalizedText.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getTransitionTimeNode() throws UaException {
    return ClientNodeSupport.await(getTransitionTimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getTransitionTimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "TransitionTime",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable DateTime readTransitionTime() throws UaException {
    return ClientNodeSupport.await(readTransitionTimeAsync());
  }

  @Override
  public void writeTransitionTime(@Nullable DateTime value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeTransitionTimeAsync(value)),
        "http://opcfoundation.org/UA/}TransitionTime");
  }

  @Override
  public CompletableFuture<? extends @Nullable DateTime> readTransitionTimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getTransitionTimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}TransitionTime",
                            false,
                            DateTime.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DateTime) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTransitionTimeAsync(@Nullable DateTime value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getTransitionTimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}TransitionTime",
                        value,
                        DateTime.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getEffectiveTransitionTimeNode() throws UaException {
    return ClientNodeSupport.await(getEffectiveTransitionTimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getEffectiveTransitionTimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "EffectiveTransitionTime",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable DateTime readEffectiveTransitionTime() throws UaException {
    return ClientNodeSupport.await(readEffectiveTransitionTimeAsync());
  }

  @Override
  public void writeEffectiveTransitionTime(@Nullable DateTime value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeEffectiveTransitionTimeAsync(value)),
        "http://opcfoundation.org/UA/}EffectiveTransitionTime");
  }

  @Override
  public CompletableFuture<? extends @Nullable DateTime> readEffectiveTransitionTimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getEffectiveTransitionTimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}EffectiveTransitionTime",
                            false,
                            DateTime.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DateTime) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeEffectiveTransitionTimeAsync(@Nullable DateTime value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getEffectiveTransitionTimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}EffectiveTransitionTime",
                        value,
                        DateTime.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getIdNode() throws UaException {
    return ClientNodeSupport.await(getIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Id",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readTwoStateVariableTypeId() throws UaException {
    return ClientNodeSupport.await(readTwoStateVariableTypeIdAsync());
  }

  @Override
  public void writeTwoStateVariableTypeId(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeTwoStateVariableTypeIdAsync(value)),
        "http://opcfoundation.org/UA/}Id");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readTwoStateVariableTypeIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Id",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTwoStateVariableTypeIdAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Id",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getTrueStateNode() throws UaException {
    return ClientNodeSupport.await(getTrueStateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getTrueStateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "TrueState",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable LocalizedText readTrueState() throws UaException {
    return ClientNodeSupport.await(readTrueStateAsync());
  }

  @Override
  public void writeTrueState(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeTrueStateAsync(value)),
        "http://opcfoundation.org/UA/}TrueState");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readTrueStateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getTrueStateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}TrueState",
                            false,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTrueStateAsync(@Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getTrueStateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}TrueState",
                        value,
                        LocalizedText.class,
                        -1,
                        null)));
  }
}
