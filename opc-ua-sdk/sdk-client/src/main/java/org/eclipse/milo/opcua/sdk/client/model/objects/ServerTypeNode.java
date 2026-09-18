package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.ServerStatusTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerTypeGetMonitoredItems;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerTypeRequestServerStateChange;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerTypeResendData;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerTypeSetSubscriptionDurable;
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerStatusDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.TimeZoneDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ServerType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.1">Model
 *     documentation</a>
 */
public class ServerTypeNode extends BaseObjectTypeNode implements ServerType {
  public ServerTypeNode(
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
  public @Nullable NamespacesTypeNode getNamespacesNode() throws UaException {
    return ClientNodeSupport.await(getNamespacesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable NamespacesTypeNode> getNamespacesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Namespaces",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        NamespacesTypeNode.class)));
  }

  @Override
  public PropertyTypeNode getServerArrayNode() throws UaException {
    return ClientNodeSupport.await(getServerArrayNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getServerArrayNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ServerArray",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String @Nullable [] readServerArray() throws UaException {
    return ClientNodeSupport.await(readServerArrayAsync());
  }

  @Override
  public void writeServerArray(@Nullable String @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeServerArrayAsync(value)),
        "http://opcfoundation.org/UA/}ServerArray");
  }

  @Override
  public CompletableFuture<? extends @Nullable String @Nullable []> readServerArrayAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getServerArrayNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ServerArray",
                            true,
                            String.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeServerArrayAsync(@Nullable String @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getServerArrayNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ServerArray",
                        value,
                        String.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getUrisVersionNode() throws UaException {
    return ClientNodeSupport.await(getUrisVersionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getUrisVersionNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "UrisVersion",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readUrisVersion() throws UaException {
    return ClientNodeSupport.await(readUrisVersionAsync());
  }

  @Override
  public void writeUrisVersion(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeUrisVersionAsync(value)),
        "http://opcfoundation.org/UA/}UrisVersion");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readUrisVersionAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getUrisVersionNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}UrisVersion",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeUrisVersionAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getUrisVersionNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}UrisVersion",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public ServerStatusTypeNode getServerStatusNode() throws UaException {
    return ClientNodeSupport.await(getServerStatusNodeAsync());
  }

  @Override
  public CompletableFuture<? extends ServerStatusTypeNode> getServerStatusNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ServerStatus",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        ServerStatusTypeNode.class)));
  }

  @Override
  public @Nullable ServerStatusDataType readServerStatus() throws UaException {
    return ClientNodeSupport.await(readServerStatusAsync());
  }

  @Override
  public void writeServerStatus(@Nullable ServerStatusDataType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeServerStatusAsync(value)),
        "http://opcfoundation.org/UA/}ServerStatus");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServerStatusDataType> readServerStatusAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getServerStatusNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ServerStatus",
                            true,
                            ServerStatusDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServerStatusDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeServerStatusAsync(
      @Nullable ServerStatusDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getServerStatusNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ServerStatus",
                        value,
                        ServerStatusDataType.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getServiceLevelNode() throws UaException {
    return ClientNodeSupport.await(getServiceLevelNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getServiceLevelNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ServiceLevel",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UByte readServiceLevel() throws UaException {
    return ClientNodeSupport.await(readServiceLevelAsync());
  }

  @Override
  public void writeServiceLevel(@Nullable UByte value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeServiceLevelAsync(value)),
        "http://opcfoundation.org/UA/}ServiceLevel");
  }

  @Override
  public CompletableFuture<? extends @Nullable UByte> readServiceLevelAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getServiceLevelNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ServiceLevel",
                            true,
                            UByte.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UByte) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeServiceLevelAsync(@Nullable UByte value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getServiceLevelNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ServiceLevel",
                        value,
                        UByte.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getNamespaceArrayNode() throws UaException {
    return ClientNodeSupport.await(getNamespaceArrayNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getNamespaceArrayNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "NamespaceArray",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String @Nullable [] readNamespaceArray() throws UaException {
    return ClientNodeSupport.await(readNamespaceArrayAsync());
  }

  @Override
  public void writeNamespaceArray(@Nullable String @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeNamespaceArrayAsync(value)),
        "http://opcfoundation.org/UA/}NamespaceArray");
  }

  @Override
  public CompletableFuture<? extends @Nullable String @Nullable []> readNamespaceArrayAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getNamespaceArrayNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}NamespaceArray",
                            true,
                            String.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeNamespaceArrayAsync(
      @Nullable String @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getNamespaceArrayNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}NamespaceArray",
                        value,
                        String.class,
                        1,
                        null)));
  }

  @Override
  public ServerRedundancyTypeNode getServerRedundancyNode() throws UaException {
    return ClientNodeSupport.await(getServerRedundancyNodeAsync());
  }

  @Override
  public CompletableFuture<? extends ServerRedundancyTypeNode> getServerRedundancyNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ServerRedundancy",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        ServerRedundancyTypeNode.class)));
  }

  @Override
  public VendorServerInfoTypeNode getVendorServerInfoNode() throws UaException {
    return ClientNodeSupport.await(getVendorServerInfoNodeAsync());
  }

  @Override
  public CompletableFuture<? extends VendorServerInfoTypeNode> getVendorServerInfoNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "VendorServerInfo",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        VendorServerInfoTypeNode.class)));
  }

  @Override
  public ServerDiagnosticsTypeNode getServerDiagnosticsNode() throws UaException {
    return ClientNodeSupport.await(getServerDiagnosticsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends ServerDiagnosticsTypeNode> getServerDiagnosticsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ServerDiagnostics",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        ServerDiagnosticsTypeNode.class)));
  }

  @Override
  public ServerCapabilitiesTypeNode getServerCapabilitiesNode() throws UaException {
    return ClientNodeSupport.await(getServerCapabilitiesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends ServerCapabilitiesTypeNode> getServerCapabilitiesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ServerCapabilities",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        ServerCapabilitiesTypeNode.class)));
  }

  @Override
  public @Nullable PropertyTypeNode getEstimatedReturnTimeNode() throws UaException {
    return ClientNodeSupport.await(getEstimatedReturnTimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getEstimatedReturnTimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "EstimatedReturnTime",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable DateTime readEstimatedReturnTime() throws UaException {
    return ClientNodeSupport.await(readEstimatedReturnTimeAsync());
  }

  @Override
  public void writeEstimatedReturnTime(@Nullable DateTime value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeEstimatedReturnTimeAsync(value)),
        "http://opcfoundation.org/UA/}EstimatedReturnTime");
  }

  @Override
  public CompletableFuture<? extends @Nullable DateTime> readEstimatedReturnTimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getEstimatedReturnTimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}EstimatedReturnTime",
                            false,
                            DateTime.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DateTime) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeEstimatedReturnTimeAsync(@Nullable DateTime value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getEstimatedReturnTimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}EstimatedReturnTime",
                        value,
                        DateTime.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getAuditingNode() throws UaException {
    return ClientNodeSupport.await(getAuditingNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getAuditingNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Auditing",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readAuditing() throws UaException {
    return ClientNodeSupport.await(readAuditingAsync());
  }

  @Override
  public void writeAuditing(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeAuditingAsync(value)),
        "http://opcfoundation.org/UA/}Auditing");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readAuditingAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getAuditingNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Auditing",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeAuditingAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getAuditingNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Auditing",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getLocalTimeNode() throws UaException {
    return ClientNodeSupport.await(getLocalTimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getLocalTimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LocalTime",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable TimeZoneDataType readLocalTime() throws UaException {
    return ClientNodeSupport.await(readLocalTimeAsync());
  }

  @Override
  public void writeLocalTime(@Nullable TimeZoneDataType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLocalTimeAsync(value)),
        "http://opcfoundation.org/UA/}LocalTime");
  }

  @Override
  public CompletableFuture<? extends @Nullable TimeZoneDataType> readLocalTimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLocalTimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LocalTime",
                            false,
                            TimeZoneDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable TimeZoneDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLocalTimeAsync(@Nullable TimeZoneDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLocalTimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LocalTime",
                        value,
                        TimeZoneDataType.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable UaMethodNode getGetMonitoredItemsMethodNode() throws UaException {
    return ClientNodeSupport.await(getGetMonitoredItemsMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getGetMonitoredItemsMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "GetMonitoredItems",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public ServerTypeGetMonitoredItems.Outputs getMonitoredItems(@Nullable UInteger subscriptionId)
      throws UaException {
    return ClientNodeSupport.await(getMonitoredItemsAsync(subscriptionId));
  }

  @Override
  public MethodCallResult<ServerTypeGetMonitoredItems.Outputs> callGetMonitoredItems(
      @Nullable UInteger subscriptionId) throws UaException {
    return ClientNodeSupport.await(callGetMonitoredItemsAsync(subscriptionId));
  }

  @Override
  public MethodCallResult<ServerTypeGetMonitoredItems.Outputs> callGetMonitoredItemsWith(
      MethodCallOptions options, @Nullable UInteger subscriptionId) throws UaException {
    return ClientNodeSupport.await(callGetMonitoredItemsWithAsync(options, subscriptionId));
  }

  @Override
  public CompletableFuture<ServerTypeGetMonitoredItems.Outputs> getMonitoredItemsAsync(
      @Nullable UInteger subscriptionId) {
    return ClientNodeSupport.compose(
        callGetMonitoredItemsAsync(subscriptionId),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<ServerTypeGetMonitoredItems.Outputs>>
      callGetMonitoredItemsAsync(@Nullable UInteger subscriptionId) {
    return callGetMonitoredItemsWithAsync(MethodCallOptions.DEFAULT, subscriptionId);
  }

  @Override
  public CompletableFuture<MethodCallResult<ServerTypeGetMonitoredItems.Outputs>>
      callGetMonitoredItemsWithAsync(MethodCallOptions options, @Nullable UInteger subscriptionId) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new ServerTypeGetMonitoredItems.Inputs(subscriptionId)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getGetMonitoredItemsMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return ServerTypeGetMonitoredItems.Outputs.fromVariants(
                                        client.getStaticEncodingContext(), values);
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getRequestServerStateChangeMethodNode() throws UaException {
    return ClientNodeSupport.await(getRequestServerStateChangeMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getRequestServerStateChangeMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "RequestServerStateChange",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void requestServerStateChange(
      @Nullable ServerState state,
      @Nullable DateTime estimatedReturnTime,
      @Nullable UInteger secondsTillShutdown,
      @Nullable LocalizedText reason,
      @Nullable Boolean restart)
      throws UaException {
    ClientNodeSupport.await(
        requestServerStateChangeAsync(
            state, estimatedReturnTime, secondsTillShutdown, reason, restart));
  }

  @Override
  public MethodCallResult<Void> callRequestServerStateChange(
      @Nullable ServerState state,
      @Nullable DateTime estimatedReturnTime,
      @Nullable UInteger secondsTillShutdown,
      @Nullable LocalizedText reason,
      @Nullable Boolean restart)
      throws UaException {
    return ClientNodeSupport.await(
        callRequestServerStateChangeAsync(
            state, estimatedReturnTime, secondsTillShutdown, reason, restart));
  }

  @Override
  public MethodCallResult<Void> callRequestServerStateChangeWith(
      MethodCallOptions options,
      @Nullable ServerState state,
      @Nullable DateTime estimatedReturnTime,
      @Nullable UInteger secondsTillShutdown,
      @Nullable LocalizedText reason,
      @Nullable Boolean restart)
      throws UaException {
    return ClientNodeSupport.await(
        callRequestServerStateChangeWithAsync(
            options, state, estimatedReturnTime, secondsTillShutdown, reason, restart));
  }

  @Override
  public CompletableFuture<Void> requestServerStateChangeAsync(
      @Nullable ServerState state,
      @Nullable DateTime estimatedReturnTime,
      @Nullable UInteger secondsTillShutdown,
      @Nullable LocalizedText reason,
      @Nullable Boolean restart) {
    return ClientNodeSupport.compose(
        callRequestServerStateChangeAsync(
            state, estimatedReturnTime, secondsTillShutdown, reason, restart),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRequestServerStateChangeAsync(
      @Nullable ServerState state,
      @Nullable DateTime estimatedReturnTime,
      @Nullable UInteger secondsTillShutdown,
      @Nullable LocalizedText reason,
      @Nullable Boolean restart) {
    return callRequestServerStateChangeWithAsync(
        MethodCallOptions.DEFAULT,
        state,
        estimatedReturnTime,
        secondsTillShutdown,
        reason,
        restart);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRequestServerStateChangeWithAsync(
      MethodCallOptions options,
      @Nullable ServerState state,
      @Nullable DateTime estimatedReturnTime,
      @Nullable UInteger secondsTillShutdown,
      @Nullable LocalizedText reason,
      @Nullable Boolean restart) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new ServerTypeRequestServerStateChange.Inputs(
                      state, estimatedReturnTime, secondsTillShutdown, reason, restart)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRequestServerStateChangeMethodNodeAsync(),
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

  @Override
  public @Nullable UaMethodNode getResendDataMethodNode() throws UaException {
    return ClientNodeSupport.await(getResendDataMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getResendDataMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "ResendData",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void resendData(@Nullable UInteger subscriptionId) throws UaException {
    ClientNodeSupport.await(resendDataAsync(subscriptionId));
  }

  @Override
  public MethodCallResult<Void> callResendData(@Nullable UInteger subscriptionId)
      throws UaException {
    return ClientNodeSupport.await(callResendDataAsync(subscriptionId));
  }

  @Override
  public MethodCallResult<Void> callResendDataWith(
      MethodCallOptions options, @Nullable UInteger subscriptionId) throws UaException {
    return ClientNodeSupport.await(callResendDataWithAsync(options, subscriptionId));
  }

  @Override
  public CompletableFuture<Void> resendDataAsync(@Nullable UInteger subscriptionId) {
    return ClientNodeSupport.compose(
        callResendDataAsync(subscriptionId),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callResendDataAsync(
      @Nullable UInteger subscriptionId) {
    return callResendDataWithAsync(MethodCallOptions.DEFAULT, subscriptionId);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callResendDataWithAsync(
      MethodCallOptions options, @Nullable UInteger subscriptionId) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new ServerTypeResendData.Inputs(subscriptionId)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getResendDataMethodNodeAsync(),
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

  @Override
  public @Nullable UaMethodNode getSetSubscriptionDurableMethodNode() throws UaException {
    return ClientNodeSupport.await(getSetSubscriptionDurableMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getSetSubscriptionDurableMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "SetSubscriptionDurable",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable UInteger setSubscriptionDurable(
      @Nullable UInteger subscriptionId, @Nullable UInteger lifetimeInHours) throws UaException {
    return ClientNodeSupport.await(setSubscriptionDurableAsync(subscriptionId, lifetimeInHours));
  }

  @Override
  public MethodCallResult<@Nullable UInteger> callSetSubscriptionDurable(
      @Nullable UInteger subscriptionId, @Nullable UInteger lifetimeInHours) throws UaException {
    return ClientNodeSupport.await(
        callSetSubscriptionDurableAsync(subscriptionId, lifetimeInHours));
  }

  @Override
  public MethodCallResult<@Nullable UInteger> callSetSubscriptionDurableWith(
      MethodCallOptions options,
      @Nullable UInteger subscriptionId,
      @Nullable UInteger lifetimeInHours)
      throws UaException {
    return ClientNodeSupport.await(
        callSetSubscriptionDurableWithAsync(options, subscriptionId, lifetimeInHours));
  }

  @Override
  public CompletableFuture<@Nullable UInteger> setSubscriptionDurableAsync(
      @Nullable UInteger subscriptionId, @Nullable UInteger lifetimeInHours) {
    return ClientNodeSupport.compose(
        callSetSubscriptionDurableAsync(subscriptionId, lifetimeInHours),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable UInteger>> callSetSubscriptionDurableAsync(
      @Nullable UInteger subscriptionId, @Nullable UInteger lifetimeInHours) {
    return callSetSubscriptionDurableWithAsync(
        MethodCallOptions.DEFAULT, subscriptionId, lifetimeInHours);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable UInteger>>
      callSetSubscriptionDurableWithAsync(
          MethodCallOptions options,
          @Nullable UInteger subscriptionId,
          @Nullable UInteger lifetimeInHours) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new ServerTypeSetSubscriptionDurable.Inputs(subscriptionId, lifetimeInHours)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getSetSubscriptionDurableMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return ServerTypeSetSubscriptionDurable.Outputs.fromVariants(
                                            client.getStaticEncodingContext(), values)
                                        .revisedLifetimeInHours();
                                  }))));
        });
  }
}
