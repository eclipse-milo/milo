package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PubSubDiagnosticsCounterTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link PubSubDiagnosticsType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.2">Model
 *     documentation</a>
 */
public class PubSubDiagnosticsTypeNode extends BaseObjectTypeNode implements PubSubDiagnosticsType {
  public PubSubDiagnosticsTypeNode(
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
  public UaObjectNode getLiveValuesNode() throws UaException {
    return ClientNodeSupport.await(getLiveValuesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaObjectNode> getLiveValuesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LiveValues",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        UaObjectNode.class)));
  }

  @Override
  public PubSubDiagnosticsCounterTypeNode getTotalErrorNode() throws UaException {
    return ClientNodeSupport.await(getTotalErrorNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PubSubDiagnosticsCounterTypeNode> getTotalErrorNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "TotalError",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        PubSubDiagnosticsCounterTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readTotalError() throws UaException {
    return ClientNodeSupport.await(readTotalErrorAsync());
  }

  @Override
  public void writeTotalError(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeTotalErrorAsync(value)),
        "http://opcfoundation.org/UA/}TotalError");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readTotalErrorAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getTotalErrorNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}TotalError",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTotalErrorAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getTotalErrorNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}TotalError",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getDiagnosticsLevelNode() throws UaException {
    return ClientNodeSupport.await(getDiagnosticsLevelNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getDiagnosticsLevelNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DiagnosticsLevel",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable DiagnosticsLevel readDiagnosticsLevel() throws UaException {
    return ClientNodeSupport.await(readDiagnosticsLevelAsync());
  }

  @Override
  public void writeDiagnosticsLevel(@Nullable DiagnosticsLevel value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDiagnosticsLevelAsync(value)),
        "http://opcfoundation.org/UA/}DiagnosticsLevel");
  }

  @Override
  public CompletableFuture<? extends @Nullable DiagnosticsLevel> readDiagnosticsLevelAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDiagnosticsLevelNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DiagnosticsLevel",
                            true,
                            DiagnosticsLevel.class,
                            -1,
                            DiagnosticsLevel::from)),
                v -> CompletableFuture.completedFuture((@Nullable DiagnosticsLevel) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDiagnosticsLevelAsync(
      @Nullable DiagnosticsLevel value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDiagnosticsLevelNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DiagnosticsLevel",
                        value,
                        DiagnosticsLevel.class,
                        -1,
                        DiagnosticsLevel::from)));
  }

  @Override
  public PubSubDiagnosticsCounterTypeNode getTotalInformationNode() throws UaException {
    return ClientNodeSupport.await(getTotalInformationNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PubSubDiagnosticsCounterTypeNode>
      getTotalInformationNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "TotalInformation",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        PubSubDiagnosticsCounterTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readTotalInformation() throws UaException {
    return ClientNodeSupport.await(readTotalInformationAsync());
  }

  @Override
  public void writeTotalInformation(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeTotalInformationAsync(value)),
        "http://opcfoundation.org/UA/}TotalInformation");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readTotalInformationAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getTotalInformationNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}TotalInformation",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTotalInformationAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getTotalInformationNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}TotalInformation",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaObjectNode getCountersNode() throws UaException {
    return ClientNodeSupport.await(getCountersNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaObjectNode> getCountersNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Counters",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        UaObjectNode.class)));
  }

  @Override
  public UaVariableNode getSubErrorNode() throws UaException {
    return ClientNodeSupport.await(getSubErrorNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getSubErrorNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SubError",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable Boolean readSubError() throws UaException {
    return ClientNodeSupport.await(readSubErrorAsync());
  }

  @Override
  public void writeSubError(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSubErrorAsync(value)),
        "http://opcfoundation.org/UA/}SubError");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readSubErrorAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSubErrorNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SubError",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSubErrorAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSubErrorNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SubError",
                        value,
                        Boolean.class,
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
