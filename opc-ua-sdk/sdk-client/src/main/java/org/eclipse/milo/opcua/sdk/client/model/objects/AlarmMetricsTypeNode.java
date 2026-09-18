package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.AlarmRateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
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
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link AlarmMetricsType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/9.2">Model
 *     documentation</a>
 */
public class AlarmMetricsTypeNode extends BaseObjectTypeNode implements AlarmMetricsType {
  public AlarmMetricsTypeNode(
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
  public UaVariableNode getAlarmCountNode() throws UaException {
    return ClientNodeSupport.await(getAlarmCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getAlarmCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AlarmCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readAlarmCount() throws UaException {
    return ClientNodeSupport.await(readAlarmCountAsync());
  }

  @Override
  public void writeAlarmCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeAlarmCountAsync(value)),
        "http://opcfoundation.org/UA/}AlarmCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readAlarmCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getAlarmCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}AlarmCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeAlarmCountAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getAlarmCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}AlarmCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getMaximumUnAckNode() throws UaException {
    return ClientNodeSupport.await(getMaximumUnAckNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getMaximumUnAckNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaximumUnAck",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable Double readMaximumUnAck() throws UaException {
    return ClientNodeSupport.await(readMaximumUnAckAsync());
  }

  @Override
  public void writeMaximumUnAck(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaximumUnAckAsync(value)),
        "http://opcfoundation.org/UA/}MaximumUnAck");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readMaximumUnAckAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaximumUnAckNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaximumUnAck",
                            true,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaximumUnAckAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaximumUnAckNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaximumUnAck",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public AlarmRateVariableTypeNode getAverageAlarmRateNode() throws UaException {
    return ClientNodeSupport.await(getAverageAlarmRateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends AlarmRateVariableTypeNode> getAverageAlarmRateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AverageAlarmRate",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        AlarmRateVariableTypeNode.class)));
  }

  @Override
  public @Nullable Double readAverageAlarmRate() throws UaException {
    return ClientNodeSupport.await(readAverageAlarmRateAsync());
  }

  @Override
  public void writeAverageAlarmRate(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeAverageAlarmRateAsync(value)),
        "http://opcfoundation.org/UA/}AverageAlarmRate");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readAverageAlarmRateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getAverageAlarmRateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}AverageAlarmRate",
                            true,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeAverageAlarmRateAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getAverageAlarmRateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}AverageAlarmRate",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public AlarmRateVariableTypeNode getCurrentAlarmRateNode() throws UaException {
    return ClientNodeSupport.await(getCurrentAlarmRateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends AlarmRateVariableTypeNode> getCurrentAlarmRateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CurrentAlarmRate",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        AlarmRateVariableTypeNode.class)));
  }

  @Override
  public @Nullable Double readCurrentAlarmRate() throws UaException {
    return ClientNodeSupport.await(readCurrentAlarmRateAsync());
  }

  @Override
  public void writeCurrentAlarmRate(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCurrentAlarmRateAsync(value)),
        "http://opcfoundation.org/UA/}CurrentAlarmRate");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readCurrentAlarmRateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCurrentAlarmRateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CurrentAlarmRate",
                            true,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCurrentAlarmRateAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCurrentAlarmRateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CurrentAlarmRate",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public AlarmRateVariableTypeNode getMaximumAlarmRateNode() throws UaException {
    return ClientNodeSupport.await(getMaximumAlarmRateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends AlarmRateVariableTypeNode> getMaximumAlarmRateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaximumAlarmRate",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        AlarmRateVariableTypeNode.class)));
  }

  @Override
  public @Nullable Double readMaximumAlarmRate() throws UaException {
    return ClientNodeSupport.await(readMaximumAlarmRateAsync());
  }

  @Override
  public void writeMaximumAlarmRate(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaximumAlarmRateAsync(value)),
        "http://opcfoundation.org/UA/}MaximumAlarmRate");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readMaximumAlarmRateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaximumAlarmRateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaximumAlarmRate",
                            true,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaximumAlarmRateAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaximumAlarmRateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaximumAlarmRate",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getMaximumActiveStateNode() throws UaException {
    return ClientNodeSupport.await(getMaximumActiveStateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getMaximumActiveStateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaximumActiveState",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable Double readMaximumActiveState() throws UaException {
    return ClientNodeSupport.await(readMaximumActiveStateAsync());
  }

  @Override
  public void writeMaximumActiveState(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaximumActiveStateAsync(value)),
        "http://opcfoundation.org/UA/}MaximumActiveState");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readMaximumActiveStateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaximumActiveStateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaximumActiveState",
                            true,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaximumActiveStateAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaximumActiveStateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaximumActiveState",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getMaximumReAlarmCountNode() throws UaException {
    return ClientNodeSupport.await(getMaximumReAlarmCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getMaximumReAlarmCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaximumReAlarmCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readMaximumReAlarmCount() throws UaException {
    return ClientNodeSupport.await(readMaximumReAlarmCountAsync());
  }

  @Override
  public void writeMaximumReAlarmCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaximumReAlarmCountAsync(value)),
        "http://opcfoundation.org/UA/}MaximumReAlarmCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaximumReAlarmCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaximumReAlarmCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaximumReAlarmCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaximumReAlarmCountAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaximumReAlarmCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaximumReAlarmCount",
                        value,
                        UInteger.class,
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
  public UaMethodNode getResetMethodNode() throws UaException {
    return ClientNodeSupport.await(getResetMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getResetMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "Reset",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void reset() throws UaException {
    ClientNodeSupport.await(resetAsync());
  }

  @Override
  public MethodCallResult<Void> callReset() throws UaException {
    return ClientNodeSupport.await(callResetAsync());
  }

  @Override
  public MethodCallResult<Void> callResetWith(MethodCallOptions options) throws UaException {
    return ClientNodeSupport.await(callResetWithAsync(options));
  }

  @Override
  public CompletableFuture<Void> resetAsync() {
    return ClientNodeSupport.compose(
        callResetAsync(),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callResetAsync() {
    return callResetWithAsync(MethodCallOptions.DEFAULT);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callResetWithAsync(MethodCallOptions options) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs = new Variant[0];
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getResetMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    if (values == null || values.length != 0)
                                      throw new UaException(
                                          StatusCodes.Bad_DecodingError,
                                          "expected no Method outputs");
                                    return null;
                                  }))));
        });
  }
}
