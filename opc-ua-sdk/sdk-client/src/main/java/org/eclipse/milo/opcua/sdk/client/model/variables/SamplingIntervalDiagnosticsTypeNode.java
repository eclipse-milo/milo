package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
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
import org.eclipse.milo.opcua.stack.core.types.structured.SamplingIntervalDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link SamplingIntervalDiagnosticsType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.10">Model
 *     documentation</a>
 */
public class SamplingIntervalDiagnosticsTypeNode extends BaseDataVariableTypeNode
    implements SamplingIntervalDiagnosticsType {
  public SamplingIntervalDiagnosticsTypeNode(
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
  public UaVariableNode getSamplingIntervalNode() throws UaException {
    return ClientNodeSupport.await(getSamplingIntervalNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getSamplingIntervalNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SamplingInterval",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable Double readSamplingInterval() throws UaException {
    return ClientNodeSupport.await(readSamplingIntervalAsync());
  }

  @Override
  public void writeSamplingInterval(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSamplingIntervalAsync(value)),
        "http://opcfoundation.org/UA/}SamplingInterval");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readSamplingIntervalAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSamplingIntervalNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SamplingInterval",
                            true,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSamplingIntervalAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSamplingIntervalNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SamplingInterval",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getSampledMonitoredItemsCountNode() throws UaException {
    return ClientNodeSupport.await(getSampledMonitoredItemsCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getSampledMonitoredItemsCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SampledMonitoredItemsCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readSampledMonitoredItemsCount() throws UaException {
    return ClientNodeSupport.await(readSampledMonitoredItemsCountAsync());
  }

  @Override
  public void writeSampledMonitoredItemsCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSampledMonitoredItemsCountAsync(value)),
        "http://opcfoundation.org/UA/}SampledMonitoredItemsCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readSampledMonitoredItemsCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSampledMonitoredItemsCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SampledMonitoredItemsCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSampledMonitoredItemsCountAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSampledMonitoredItemsCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SampledMonitoredItemsCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getMaxSampledMonitoredItemsCountNode() throws UaException {
    return ClientNodeSupport.await(getMaxSampledMonitoredItemsCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getMaxSampledMonitoredItemsCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxSampledMonitoredItemsCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxSampledMonitoredItemsCount() throws UaException {
    return ClientNodeSupport.await(readMaxSampledMonitoredItemsCountAsync());
  }

  @Override
  public void writeMaxSampledMonitoredItemsCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxSampledMonitoredItemsCountAsync(value)),
        "http://opcfoundation.org/UA/}MaxSampledMonitoredItemsCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxSampledMonitoredItemsCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxSampledMonitoredItemsCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxSampledMonitoredItemsCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxSampledMonitoredItemsCountAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxSampledMonitoredItemsCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxSampledMonitoredItemsCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getDisabledMonitoredItemsSamplingCountNode() throws UaException {
    return ClientNodeSupport.await(getDisabledMonitoredItemsSamplingCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode>
      getDisabledMonitoredItemsSamplingCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DisabledMonitoredItemsSamplingCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readDisabledMonitoredItemsSamplingCount() throws UaException {
    return ClientNodeSupport.await(readDisabledMonitoredItemsSamplingCountAsync());
  }

  @Override
  public void writeDisabledMonitoredItemsSamplingCount(@Nullable UInteger value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDisabledMonitoredItemsSamplingCountAsync(value)),
        "http://opcfoundation.org/UA/}DisabledMonitoredItemsSamplingCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger>
      readDisabledMonitoredItemsSamplingCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDisabledMonitoredItemsSamplingCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DisabledMonitoredItemsSamplingCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDisabledMonitoredItemsSamplingCountAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDisabledMonitoredItemsSamplingCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DisabledMonitoredItemsSamplingCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable SamplingIntervalDiagnosticsDataType readTypedValue() throws UaException {
    return ClientNodeSupport.await(readTypedValueAsync());
  }

  @Override
  public void writeTypedValue(@Nullable SamplingIntervalDiagnosticsDataType value)
      throws UaException {
    ClientNodeSupport.good(ClientNodeSupport.await(writeTypedValueAsync(value)), "Value");
  }

  @Override
  public CompletableFuture<? extends @Nullable SamplingIntervalDiagnosticsDataType>
      readTypedValueAsync() {
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
                            SamplingIntervalDiagnosticsDataType.class,
                            -1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable SamplingIntervalDiagnosticsDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTypedValueAsync(
      @Nullable SamplingIntervalDiagnosticsDataType value) {
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
                        SamplingIntervalDiagnosticsDataType.class,
                        -1,
                        null)));
  }
}
