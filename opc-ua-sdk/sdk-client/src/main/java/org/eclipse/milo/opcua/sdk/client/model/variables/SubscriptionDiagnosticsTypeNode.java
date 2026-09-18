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
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link SubscriptionDiagnosticsType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.12">Model
 *     documentation</a>
 */
public class SubscriptionDiagnosticsTypeNode extends BaseDataVariableTypeNode
    implements SubscriptionDiagnosticsType {
  public SubscriptionDiagnosticsTypeNode(
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
  public UaVariableNode getEnableCountNode() throws UaException {
    return ClientNodeSupport.await(getEnableCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getEnableCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "EnableCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readEnableCount() throws UaException {
    return ClientNodeSupport.await(readEnableCountAsync());
  }

  @Override
  public void writeEnableCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeEnableCountAsync(value)),
        "http://opcfoundation.org/UA/}EnableCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readEnableCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getEnableCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}EnableCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeEnableCountAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getEnableCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}EnableCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getModifyCountNode() throws UaException {
    return ClientNodeSupport.await(getModifyCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getModifyCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ModifyCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readModifyCount() throws UaException {
    return ClientNodeSupport.await(readModifyCountAsync());
  }

  @Override
  public void writeModifyCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeModifyCountAsync(value)),
        "http://opcfoundation.org/UA/}ModifyCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readModifyCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getModifyCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ModifyCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeModifyCountAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getModifyCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ModifyCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getDisableCountNode() throws UaException {
    return ClientNodeSupport.await(getDisableCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getDisableCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DisableCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readDisableCount() throws UaException {
    return ClientNodeSupport.await(readDisableCountAsync());
  }

  @Override
  public void writeDisableCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDisableCountAsync(value)),
        "http://opcfoundation.org/UA/}DisableCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readDisableCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDisableCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DisableCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDisableCountAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDisableCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DisableCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getSubscriptionIdNode() throws UaException {
    return ClientNodeSupport.await(getSubscriptionIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getSubscriptionIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SubscriptionId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readSubscriptionId() throws UaException {
    return ClientNodeSupport.await(readSubscriptionIdAsync());
  }

  @Override
  public void writeSubscriptionId(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSubscriptionIdAsync(value)),
        "http://opcfoundation.org/UA/}SubscriptionId");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readSubscriptionIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSubscriptionIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SubscriptionId",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSubscriptionIdAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSubscriptionIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SubscriptionId",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getMaxLifetimeCountNode() throws UaException {
    return ClientNodeSupport.await(getMaxLifetimeCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getMaxLifetimeCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxLifetimeCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxLifetimeCount() throws UaException {
    return ClientNodeSupport.await(readMaxLifetimeCountAsync());
  }

  @Override
  public void writeMaxLifetimeCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxLifetimeCountAsync(value)),
        "http://opcfoundation.org/UA/}MaxLifetimeCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxLifetimeCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxLifetimeCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxLifetimeCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxLifetimeCountAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxLifetimeCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxLifetimeCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getMaxKeepAliveCountNode() throws UaException {
    return ClientNodeSupport.await(getMaxKeepAliveCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getMaxKeepAliveCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxKeepAliveCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxKeepAliveCount() throws UaException {
    return ClientNodeSupport.await(readMaxKeepAliveCountAsync());
  }

  @Override
  public void writeMaxKeepAliveCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxKeepAliveCountAsync(value)),
        "http://opcfoundation.org/UA/}MaxKeepAliveCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxKeepAliveCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxKeepAliveCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxKeepAliveCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxKeepAliveCountAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxKeepAliveCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxKeepAliveCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getPublishingEnabledNode() throws UaException {
    return ClientNodeSupport.await(getPublishingEnabledNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getPublishingEnabledNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "PublishingEnabled",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable Boolean readPublishingEnabled() throws UaException {
    return ClientNodeSupport.await(readPublishingEnabledAsync());
  }

  @Override
  public void writePublishingEnabled(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writePublishingEnabledAsync(value)),
        "http://opcfoundation.org/UA/}PublishingEnabled");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readPublishingEnabledAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getPublishingEnabledNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}PublishingEnabled",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writePublishingEnabledAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getPublishingEnabledNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}PublishingEnabled",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getMonitoredItemCountNode() throws UaException {
    return ClientNodeSupport.await(getMonitoredItemCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getMonitoredItemCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MonitoredItemCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readMonitoredItemCount() throws UaException {
    return ClientNodeSupport.await(readMonitoredItemCountAsync());
  }

  @Override
  public void writeMonitoredItemCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMonitoredItemCountAsync(value)),
        "http://opcfoundation.org/UA/}MonitoredItemCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMonitoredItemCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMonitoredItemCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MonitoredItemCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMonitoredItemCountAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMonitoredItemCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MonitoredItemCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getNextSequenceNumberNode() throws UaException {
    return ClientNodeSupport.await(getNextSequenceNumberNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getNextSequenceNumberNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "NextSequenceNumber",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readNextSequenceNumber() throws UaException {
    return ClientNodeSupport.await(readNextSequenceNumberAsync());
  }

  @Override
  public void writeNextSequenceNumber(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeNextSequenceNumberAsync(value)),
        "http://opcfoundation.org/UA/}NextSequenceNumber");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readNextSequenceNumberAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getNextSequenceNumberNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}NextSequenceNumber",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeNextSequenceNumberAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getNextSequenceNumberNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}NextSequenceNumber",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getNotificationsCountNode() throws UaException {
    return ClientNodeSupport.await(getNotificationsCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getNotificationsCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "NotificationsCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readNotificationsCount() throws UaException {
    return ClientNodeSupport.await(readNotificationsCountAsync());
  }

  @Override
  public void writeNotificationsCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeNotificationsCountAsync(value)),
        "http://opcfoundation.org/UA/}NotificationsCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readNotificationsCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getNotificationsCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}NotificationsCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeNotificationsCountAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getNotificationsCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}NotificationsCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getPublishingIntervalNode() throws UaException {
    return ClientNodeSupport.await(getPublishingIntervalNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getPublishingIntervalNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "PublishingInterval",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable Double readPublishingInterval() throws UaException {
    return ClientNodeSupport.await(readPublishingIntervalAsync());
  }

  @Override
  public void writePublishingInterval(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writePublishingIntervalAsync(value)),
        "http://opcfoundation.org/UA/}PublishingInterval");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readPublishingIntervalAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getPublishingIntervalNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}PublishingInterval",
                            true,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writePublishingIntervalAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getPublishingIntervalNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}PublishingInterval",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getPublishRequestCountNode() throws UaException {
    return ClientNodeSupport.await(getPublishRequestCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getPublishRequestCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "PublishRequestCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readPublishRequestCount() throws UaException {
    return ClientNodeSupport.await(readPublishRequestCountAsync());
  }

  @Override
  public void writePublishRequestCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writePublishRequestCountAsync(value)),
        "http://opcfoundation.org/UA/}PublishRequestCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readPublishRequestCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getPublishRequestCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}PublishRequestCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writePublishRequestCountAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getPublishRequestCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}PublishRequestCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getCurrentLifetimeCountNode() throws UaException {
    return ClientNodeSupport.await(getCurrentLifetimeCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getCurrentLifetimeCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CurrentLifetimeCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readCurrentLifetimeCount() throws UaException {
    return ClientNodeSupport.await(readCurrentLifetimeCountAsync());
  }

  @Override
  public void writeCurrentLifetimeCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCurrentLifetimeCountAsync(value)),
        "http://opcfoundation.org/UA/}CurrentLifetimeCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readCurrentLifetimeCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCurrentLifetimeCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CurrentLifetimeCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCurrentLifetimeCountAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCurrentLifetimeCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CurrentLifetimeCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getTransferRequestCountNode() throws UaException {
    return ClientNodeSupport.await(getTransferRequestCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getTransferRequestCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "TransferRequestCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readTransferRequestCount() throws UaException {
    return ClientNodeSupport.await(readTransferRequestCountAsync());
  }

  @Override
  public void writeTransferRequestCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeTransferRequestCountAsync(value)),
        "http://opcfoundation.org/UA/}TransferRequestCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readTransferRequestCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getTransferRequestCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}TransferRequestCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTransferRequestCountAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getTransferRequestCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}TransferRequestCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getCurrentKeepAliveCountNode() throws UaException {
    return ClientNodeSupport.await(getCurrentKeepAliveCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getCurrentKeepAliveCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CurrentKeepAliveCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readCurrentKeepAliveCount() throws UaException {
    return ClientNodeSupport.await(readCurrentKeepAliveCountAsync());
  }

  @Override
  public void writeCurrentKeepAliveCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCurrentKeepAliveCountAsync(value)),
        "http://opcfoundation.org/UA/}CurrentKeepAliveCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readCurrentKeepAliveCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCurrentKeepAliveCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CurrentKeepAliveCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCurrentKeepAliveCountAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCurrentKeepAliveCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CurrentKeepAliveCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getDiscardedMessageCountNode() throws UaException {
    return ClientNodeSupport.await(getDiscardedMessageCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getDiscardedMessageCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DiscardedMessageCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readDiscardedMessageCount() throws UaException {
    return ClientNodeSupport.await(readDiscardedMessageCountAsync());
  }

  @Override
  public void writeDiscardedMessageCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDiscardedMessageCountAsync(value)),
        "http://opcfoundation.org/UA/}DiscardedMessageCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readDiscardedMessageCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDiscardedMessageCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DiscardedMessageCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDiscardedMessageCountAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDiscardedMessageCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DiscardedMessageCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getRepublishMessageCountNode() throws UaException {
    return ClientNodeSupport.await(getRepublishMessageCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getRepublishMessageCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "RepublishMessageCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readRepublishMessageCount() throws UaException {
    return ClientNodeSupport.await(readRepublishMessageCountAsync());
  }

  @Override
  public void writeRepublishMessageCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeRepublishMessageCountAsync(value)),
        "http://opcfoundation.org/UA/}RepublishMessageCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readRepublishMessageCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getRepublishMessageCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}RepublishMessageCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeRepublishMessageCountAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getRepublishMessageCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}RepublishMessageCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getRepublishRequestCountNode() throws UaException {
    return ClientNodeSupport.await(getRepublishRequestCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getRepublishRequestCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "RepublishRequestCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readRepublishRequestCount() throws UaException {
    return ClientNodeSupport.await(readRepublishRequestCountAsync());
  }

  @Override
  public void writeRepublishRequestCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeRepublishRequestCountAsync(value)),
        "http://opcfoundation.org/UA/}RepublishRequestCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readRepublishRequestCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getRepublishRequestCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}RepublishRequestCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeRepublishRequestCountAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getRepublishRequestCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}RepublishRequestCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getEventNotificationsCountNode() throws UaException {
    return ClientNodeSupport.await(getEventNotificationsCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getEventNotificationsCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "EventNotificationsCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readEventNotificationsCount() throws UaException {
    return ClientNodeSupport.await(readEventNotificationsCountAsync());
  }

  @Override
  public void writeEventNotificationsCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeEventNotificationsCountAsync(value)),
        "http://opcfoundation.org/UA/}EventNotificationsCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readEventNotificationsCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getEventNotificationsCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}EventNotificationsCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeEventNotificationsCountAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getEventNotificationsCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}EventNotificationsCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getEventQueueOverflowCountNode() throws UaException {
    return ClientNodeSupport.await(getEventQueueOverflowCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getEventQueueOverflowCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "EventQueueOverflowCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readEventQueueOverflowCount() throws UaException {
    return ClientNodeSupport.await(readEventQueueOverflowCountAsync());
  }

  @Override
  public void writeEventQueueOverflowCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeEventQueueOverflowCountAsync(value)),
        "http://opcfoundation.org/UA/}EventQueueOverflowCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readEventQueueOverflowCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getEventQueueOverflowCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}EventQueueOverflowCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeEventQueueOverflowCountAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getEventQueueOverflowCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}EventQueueOverflowCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getLatePublishRequestCountNode() throws UaException {
    return ClientNodeSupport.await(getLatePublishRequestCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getLatePublishRequestCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LatePublishRequestCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readLatePublishRequestCount() throws UaException {
    return ClientNodeSupport.await(readLatePublishRequestCountAsync());
  }

  @Override
  public void writeLatePublishRequestCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLatePublishRequestCountAsync(value)),
        "http://opcfoundation.org/UA/}LatePublishRequestCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readLatePublishRequestCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLatePublishRequestCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LatePublishRequestCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLatePublishRequestCountAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLatePublishRequestCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LatePublishRequestCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getDisabledMonitoredItemCountNode() throws UaException {
    return ClientNodeSupport.await(getDisabledMonitoredItemCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getDisabledMonitoredItemCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DisabledMonitoredItemCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readDisabledMonitoredItemCount() throws UaException {
    return ClientNodeSupport.await(readDisabledMonitoredItemCountAsync());
  }

  @Override
  public void writeDisabledMonitoredItemCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDisabledMonitoredItemCountAsync(value)),
        "http://opcfoundation.org/UA/}DisabledMonitoredItemCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readDisabledMonitoredItemCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDisabledMonitoredItemCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DisabledMonitoredItemCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDisabledMonitoredItemCountAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDisabledMonitoredItemCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DisabledMonitoredItemCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getMaxNotificationsPerPublishNode() throws UaException {
    return ClientNodeSupport.await(getMaxNotificationsPerPublishNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getMaxNotificationsPerPublishNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxNotificationsPerPublish",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxNotificationsPerPublish() throws UaException {
    return ClientNodeSupport.await(readMaxNotificationsPerPublishAsync());
  }

  @Override
  public void writeMaxNotificationsPerPublish(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxNotificationsPerPublishAsync(value)),
        "http://opcfoundation.org/UA/}MaxNotificationsPerPublish");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxNotificationsPerPublishAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxNotificationsPerPublishNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxNotificationsPerPublish",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNotificationsPerPublishAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxNotificationsPerPublishNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxNotificationsPerPublish",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getUnacknowledgedMessageCountNode() throws UaException {
    return ClientNodeSupport.await(getUnacknowledgedMessageCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getUnacknowledgedMessageCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "UnacknowledgedMessageCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readUnacknowledgedMessageCount() throws UaException {
    return ClientNodeSupport.await(readUnacknowledgedMessageCountAsync());
  }

  @Override
  public void writeUnacknowledgedMessageCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeUnacknowledgedMessageCountAsync(value)),
        "http://opcfoundation.org/UA/}UnacknowledgedMessageCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readUnacknowledgedMessageCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getUnacknowledgedMessageCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}UnacknowledgedMessageCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeUnacknowledgedMessageCountAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getUnacknowledgedMessageCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}UnacknowledgedMessageCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getTransferredToAltClientCountNode() throws UaException {
    return ClientNodeSupport.await(getTransferredToAltClientCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getTransferredToAltClientCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "TransferredToAltClientCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readTransferredToAltClientCount() throws UaException {
    return ClientNodeSupport.await(readTransferredToAltClientCountAsync());
  }

  @Override
  public void writeTransferredToAltClientCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeTransferredToAltClientCountAsync(value)),
        "http://opcfoundation.org/UA/}TransferredToAltClientCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readTransferredToAltClientCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getTransferredToAltClientCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}TransferredToAltClientCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTransferredToAltClientCountAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getTransferredToAltClientCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}TransferredToAltClientCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getDataChangeNotificationsCountNode() throws UaException {
    return ClientNodeSupport.await(getDataChangeNotificationsCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getDataChangeNotificationsCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DataChangeNotificationsCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readDataChangeNotificationsCount() throws UaException {
    return ClientNodeSupport.await(readDataChangeNotificationsCountAsync());
  }

  @Override
  public void writeDataChangeNotificationsCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDataChangeNotificationsCountAsync(value)),
        "http://opcfoundation.org/UA/}DataChangeNotificationsCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readDataChangeNotificationsCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDataChangeNotificationsCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DataChangeNotificationsCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDataChangeNotificationsCountAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDataChangeNotificationsCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DataChangeNotificationsCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getMonitoringQueueOverflowCountNode() throws UaException {
    return ClientNodeSupport.await(getMonitoringQueueOverflowCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getMonitoringQueueOverflowCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MonitoringQueueOverflowCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readMonitoringQueueOverflowCount() throws UaException {
    return ClientNodeSupport.await(readMonitoringQueueOverflowCountAsync());
  }

  @Override
  public void writeMonitoringQueueOverflowCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMonitoringQueueOverflowCountAsync(value)),
        "http://opcfoundation.org/UA/}MonitoringQueueOverflowCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMonitoringQueueOverflowCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMonitoringQueueOverflowCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MonitoringQueueOverflowCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMonitoringQueueOverflowCountAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMonitoringQueueOverflowCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MonitoringQueueOverflowCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getRepublishMessageRequestCountNode() throws UaException {
    return ClientNodeSupport.await(getRepublishMessageRequestCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getRepublishMessageRequestCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "RepublishMessageRequestCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readRepublishMessageRequestCount() throws UaException {
    return ClientNodeSupport.await(readRepublishMessageRequestCountAsync());
  }

  @Override
  public void writeRepublishMessageRequestCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeRepublishMessageRequestCountAsync(value)),
        "http://opcfoundation.org/UA/}RepublishMessageRequestCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readRepublishMessageRequestCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getRepublishMessageRequestCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}RepublishMessageRequestCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeRepublishMessageRequestCountAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getRepublishMessageRequestCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}RepublishMessageRequestCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getTransferredToSameClientCountNode() throws UaException {
    return ClientNodeSupport.await(getTransferredToSameClientCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getTransferredToSameClientCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "TransferredToSameClientCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readTransferredToSameClientCount() throws UaException {
    return ClientNodeSupport.await(readTransferredToSameClientCountAsync());
  }

  @Override
  public void writeTransferredToSameClientCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeTransferredToSameClientCountAsync(value)),
        "http://opcfoundation.org/UA/}TransferredToSameClientCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readTransferredToSameClientCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getTransferredToSameClientCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}TransferredToSameClientCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTransferredToSameClientCountAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getTransferredToSameClientCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}TransferredToSameClientCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getPriorityNode() throws UaException {
    return ClientNodeSupport.await(getPriorityNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getPriorityNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Priority",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UByte readPriority() throws UaException {
    return ClientNodeSupport.await(readPriorityAsync());
  }

  @Override
  public void writePriority(@Nullable UByte value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writePriorityAsync(value)),
        "http://opcfoundation.org/UA/}Priority");
  }

  @Override
  public CompletableFuture<? extends @Nullable UByte> readPriorityAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getPriorityNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Priority",
                            true,
                            UByte.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UByte) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writePriorityAsync(@Nullable UByte value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getPriorityNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Priority",
                        value,
                        UByte.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getSessionIdNode() throws UaException {
    return ClientNodeSupport.await(getSessionIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getSessionIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SessionId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable NodeId readSessionId() throws UaException {
    return ClientNodeSupport.await(readSessionIdAsync());
  }

  @Override
  public void writeSessionId(@Nullable NodeId value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSessionIdAsync(value)),
        "http://opcfoundation.org/UA/}SessionId");
  }

  @Override
  public CompletableFuture<? extends @Nullable NodeId> readSessionIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSessionIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SessionId",
                            true,
                            NodeId.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable NodeId) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSessionIdAsync(@Nullable NodeId value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSessionIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SessionId",
                        value,
                        NodeId.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable SubscriptionDiagnosticsDataType readTypedValue() throws UaException {
    return ClientNodeSupport.await(readTypedValueAsync());
  }

  @Override
  public void writeTypedValue(@Nullable SubscriptionDiagnosticsDataType value) throws UaException {
    ClientNodeSupport.good(ClientNodeSupport.await(writeTypedValueAsync(value)), "Value");
  }

  @Override
  public CompletableFuture<? extends @Nullable SubscriptionDiagnosticsDataType>
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
                            SubscriptionDiagnosticsDataType.class,
                            -1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable SubscriptionDiagnosticsDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTypedValueAsync(
      @Nullable SubscriptionDiagnosticsDataType value) {
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
                        SubscriptionDiagnosticsDataType.class,
                        -1,
                        null)));
  }
}
