package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.TwoStateVariableTypeNode;
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
 * Node implementation of {@link NonExclusiveLimitAlarmType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.20">Model
 *     documentation</a>
 */
public class NonExclusiveLimitAlarmTypeNode extends LimitAlarmTypeNode
    implements NonExclusiveLimitAlarmType {
  public NonExclusiveLimitAlarmTypeNode(
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
  public TwoStateVariableTypeNode getActiveStateNode() throws UaException {
    return ClientNodeSupport.await(getActiveStateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends TwoStateVariableTypeNode> getActiveStateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ActiveState",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        TwoStateVariableTypeNode.class)));
  }

  @Override
  public @Nullable TwoStateVariableTypeNode getLowLowStateNode() throws UaException {
    return ClientNodeSupport.await(getLowLowStateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable TwoStateVariableTypeNode> getLowLowStateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LowLowState",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        TwoStateVariableTypeNode.class)));
  }

  @Override
  public @Nullable LocalizedText readLowLowState() throws UaException {
    return ClientNodeSupport.await(readLowLowStateAsync());
  }

  @Override
  public void writeLowLowState(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLowLowStateAsync(value)),
        "http://opcfoundation.org/UA/}LowLowState");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readLowLowStateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLowLowStateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LowLowState",
                            false,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLowLowStateAsync(@Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLowLowStateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LowLowState",
                        value,
                        LocalizedText.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable TwoStateVariableTypeNode getHighHighStateNode() throws UaException {
    return ClientNodeSupport.await(getHighHighStateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable TwoStateVariableTypeNode>
      getHighHighStateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "HighHighState",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        TwoStateVariableTypeNode.class)));
  }

  @Override
  public @Nullable LocalizedText readHighHighState() throws UaException {
    return ClientNodeSupport.await(readHighHighStateAsync());
  }

  @Override
  public void writeHighHighState(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeHighHighStateAsync(value)),
        "http://opcfoundation.org/UA/}HighHighState");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readHighHighStateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getHighHighStateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}HighHighState",
                            false,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeHighHighStateAsync(@Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getHighHighStateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}HighHighState",
                        value,
                        LocalizedText.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable TwoStateVariableTypeNode getLowStateNode() throws UaException {
    return ClientNodeSupport.await(getLowStateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable TwoStateVariableTypeNode> getLowStateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LowState",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        TwoStateVariableTypeNode.class)));
  }

  @Override
  public @Nullable LocalizedText readLowState() throws UaException {
    return ClientNodeSupport.await(readLowStateAsync());
  }

  @Override
  public void writeLowState(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLowStateAsync(value)),
        "http://opcfoundation.org/UA/}LowState");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readLowStateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLowStateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LowState",
                            false,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLowStateAsync(@Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLowStateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LowState",
                        value,
                        LocalizedText.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable TwoStateVariableTypeNode getHighStateNode() throws UaException {
    return ClientNodeSupport.await(getHighStateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable TwoStateVariableTypeNode> getHighStateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "HighState",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        TwoStateVariableTypeNode.class)));
  }

  @Override
  public @Nullable LocalizedText readHighState() throws UaException {
    return ClientNodeSupport.await(readHighStateAsync());
  }

  @Override
  public void writeHighState(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeHighStateAsync(value)),
        "http://opcfoundation.org/UA/}HighState");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readHighStateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getHighStateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}HighState",
                            false,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeHighStateAsync(@Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getHighStateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}HighState",
                        value,
                        LocalizedText.class,
                        -1,
                        null)));
  }
}
