package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.AcknowledgeableConditionTypeAcknowledge;
import org.eclipse.milo.opcua.sdk.core.model.methods.AcknowledgeableConditionTypeConfirm;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
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
 * Node implementation of {@link AcknowledgeableConditionType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.2">Model
 *     documentation</a>
 */
public class AcknowledgeableConditionTypeNode extends ConditionTypeNode
    implements AcknowledgeableConditionType {
  public AcknowledgeableConditionTypeNode(
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
  public TwoStateVariableTypeNode getAckedStateNode() throws UaException {
    return ClientNodeSupport.await(getAckedStateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends TwoStateVariableTypeNode> getAckedStateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AckedState",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        TwoStateVariableTypeNode.class)));
  }

  @Override
  public @Nullable LocalizedText readAckedState() throws UaException {
    return ClientNodeSupport.await(readAckedStateAsync());
  }

  @Override
  public void writeAckedState(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeAckedStateAsync(value)),
        "http://opcfoundation.org/UA/}AckedState");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readAckedStateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getAckedStateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}AckedState",
                            true,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeAckedStateAsync(@Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getAckedStateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}AckedState",
                        value,
                        LocalizedText.class,
                        -1,
                        null)));
  }

  @Override
  public TwoStateVariableTypeNode getEnabledStateNode() throws UaException {
    return ClientNodeSupport.await(getEnabledStateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends TwoStateVariableTypeNode> getEnabledStateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "EnabledState",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        TwoStateVariableTypeNode.class)));
  }

  @Override
  public @Nullable TwoStateVariableTypeNode getConfirmedStateNode() throws UaException {
    return ClientNodeSupport.await(getConfirmedStateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable TwoStateVariableTypeNode>
      getConfirmedStateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ConfirmedState",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        TwoStateVariableTypeNode.class)));
  }

  @Override
  public @Nullable LocalizedText readConfirmedState() throws UaException {
    return ClientNodeSupport.await(readConfirmedStateAsync());
  }

  @Override
  public void writeConfirmedState(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeConfirmedStateAsync(value)),
        "http://opcfoundation.org/UA/}ConfirmedState");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readConfirmedStateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getConfirmedStateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ConfirmedState",
                            false,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeConfirmedStateAsync(@Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getConfirmedStateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ConfirmedState",
                        value,
                        LocalizedText.class,
                        -1,
                        null)));
  }

  @Override
  public UaMethodNode getAcknowledgeMethodNode() throws UaException {
    return ClientNodeSupport.await(getAcknowledgeMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getAcknowledgeMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "Acknowledge",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void acknowledge(@Nullable ByteString eventId, @Nullable LocalizedText comment)
      throws UaException {
    ClientNodeSupport.await(acknowledgeAsync(eventId, comment));
  }

  @Override
  public MethodCallResult<Void> callAcknowledge(
      @Nullable ByteString eventId, @Nullable LocalizedText comment) throws UaException {
    return ClientNodeSupport.await(callAcknowledgeAsync(eventId, comment));
  }

  @Override
  public MethodCallResult<Void> callAcknowledgeWith(
      MethodCallOptions options, @Nullable ByteString eventId, @Nullable LocalizedText comment)
      throws UaException {
    return ClientNodeSupport.await(callAcknowledgeWithAsync(options, eventId, comment));
  }

  @Override
  public CompletableFuture<Void> acknowledgeAsync(
      @Nullable ByteString eventId, @Nullable LocalizedText comment) {
    return ClientNodeSupport.compose(
        callAcknowledgeAsync(eventId, comment),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callAcknowledgeAsync(
      @Nullable ByteString eventId, @Nullable LocalizedText comment) {
    return callAcknowledgeWithAsync(MethodCallOptions.DEFAULT, eventId, comment);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callAcknowledgeWithAsync(
      MethodCallOptions options, @Nullable ByteString eventId, @Nullable LocalizedText comment) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new AcknowledgeableConditionTypeAcknowledge.Inputs(eventId, comment)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAcknowledgeMethodNodeAsync(),
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
  public @Nullable UaMethodNode getConfirmMethodNode() throws UaException {
    return ClientNodeSupport.await(getConfirmMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getConfirmMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "Confirm",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void confirm(@Nullable ByteString eventId, @Nullable LocalizedText comment)
      throws UaException {
    ClientNodeSupport.await(confirmAsync(eventId, comment));
  }

  @Override
  public MethodCallResult<Void> callConfirm(
      @Nullable ByteString eventId, @Nullable LocalizedText comment) throws UaException {
    return ClientNodeSupport.await(callConfirmAsync(eventId, comment));
  }

  @Override
  public MethodCallResult<Void> callConfirmWith(
      MethodCallOptions options, @Nullable ByteString eventId, @Nullable LocalizedText comment)
      throws UaException {
    return ClientNodeSupport.await(callConfirmWithAsync(options, eventId, comment));
  }

  @Override
  public CompletableFuture<Void> confirmAsync(
      @Nullable ByteString eventId, @Nullable LocalizedText comment) {
    return ClientNodeSupport.compose(
        callConfirmAsync(eventId, comment),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callConfirmAsync(
      @Nullable ByteString eventId, @Nullable LocalizedText comment) {
    return callConfirmWithAsync(MethodCallOptions.DEFAULT, eventId, comment);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callConfirmWithAsync(
      MethodCallOptions options, @Nullable ByteString eventId, @Nullable LocalizedText comment) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new AcknowledgeableConditionTypeConfirm.Inputs(eventId, comment)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getConfirmMethodNodeAsync(),
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
