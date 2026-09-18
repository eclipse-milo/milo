package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.ShelvedStateMachineTypeOneShotShelve2;
import org.eclipse.milo.opcua.sdk.core.model.methods.ShelvedStateMachineTypeTimedShelve;
import org.eclipse.milo.opcua.sdk.core.model.methods.ShelvedStateMachineTypeTimedShelve2;
import org.eclipse.milo.opcua.sdk.core.model.methods.ShelvedStateMachineTypeUnshelve2;
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ShelvedStateMachineType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.1">Model
 *     documentation</a>
 */
public class ShelvedStateMachineTypeNode extends FiniteStateMachineTypeNode
    implements ShelvedStateMachineType {
  public ShelvedStateMachineTypeNode(
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
  public PropertyTypeNode getUnshelveTimeNode() throws UaException {
    return ClientNodeSupport.await(getUnshelveTimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUnshelveTimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "UnshelveTime",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Double readUnshelveTime() throws UaException {
    return ClientNodeSupport.await(readUnshelveTimeAsync());
  }

  @Override
  public void writeUnshelveTime(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeUnshelveTimeAsync(value)),
        "http://opcfoundation.org/UA/}UnshelveTime");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readUnshelveTimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getUnshelveTimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}UnshelveTime",
                            true,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeUnshelveTimeAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getUnshelveTimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}UnshelveTime",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public UaMethodNode getOneShotShelveMethodNode() throws UaException {
    return ClientNodeSupport.await(getOneShotShelveMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getOneShotShelveMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "OneShotShelve",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void oneShotShelve() throws UaException {
    ClientNodeSupport.await(oneShotShelveAsync());
  }

  @Override
  public MethodCallResult<Void> callOneShotShelve() throws UaException {
    return ClientNodeSupport.await(callOneShotShelveAsync());
  }

  @Override
  public MethodCallResult<Void> callOneShotShelveWith(MethodCallOptions options)
      throws UaException {
    return ClientNodeSupport.await(callOneShotShelveWithAsync(options));
  }

  @Override
  public CompletableFuture<Void> oneShotShelveAsync() {
    return ClientNodeSupport.compose(
        callOneShotShelveAsync(),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callOneShotShelveAsync() {
    return callOneShotShelveWithAsync(MethodCallOptions.DEFAULT);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callOneShotShelveWithAsync(
      MethodCallOptions options) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs = new Variant[0];
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getOneShotShelveMethodNodeAsync(),
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
  public @Nullable UaMethodNode getOneShotShelve2MethodNode() throws UaException {
    return ClientNodeSupport.await(getOneShotShelve2MethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getOneShotShelve2MethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "OneShotShelve2",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void oneShotShelve2(@Nullable LocalizedText comment) throws UaException {
    ClientNodeSupport.await(oneShotShelve2Async(comment));
  }

  @Override
  public MethodCallResult<Void> callOneShotShelve2(@Nullable LocalizedText comment)
      throws UaException {
    return ClientNodeSupport.await(callOneShotShelve2Async(comment));
  }

  @Override
  public MethodCallResult<Void> callOneShotShelve2With(
      MethodCallOptions options, @Nullable LocalizedText comment) throws UaException {
    return ClientNodeSupport.await(callOneShotShelve2WithAsync(options, comment));
  }

  @Override
  public CompletableFuture<Void> oneShotShelve2Async(@Nullable LocalizedText comment) {
    return ClientNodeSupport.compose(
        callOneShotShelve2Async(comment),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callOneShotShelve2Async(
      @Nullable LocalizedText comment) {
    return callOneShotShelve2WithAsync(MethodCallOptions.DEFAULT, comment);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callOneShotShelve2WithAsync(
      MethodCallOptions options, @Nullable LocalizedText comment) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new ShelvedStateMachineTypeOneShotShelve2.Inputs(comment)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getOneShotShelve2MethodNodeAsync(),
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
  public UaMethodNode getTimedShelveMethodNode() throws UaException {
    return ClientNodeSupport.await(getTimedShelveMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getTimedShelveMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "TimedShelve",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void timedShelve(@Nullable Double shelvingTime) throws UaException {
    ClientNodeSupport.await(timedShelveAsync(shelvingTime));
  }

  @Override
  public MethodCallResult<Void> callTimedShelve(@Nullable Double shelvingTime) throws UaException {
    return ClientNodeSupport.await(callTimedShelveAsync(shelvingTime));
  }

  @Override
  public MethodCallResult<Void> callTimedShelveWith(
      MethodCallOptions options, @Nullable Double shelvingTime) throws UaException {
    return ClientNodeSupport.await(callTimedShelveWithAsync(options, shelvingTime));
  }

  @Override
  public CompletableFuture<Void> timedShelveAsync(@Nullable Double shelvingTime) {
    return ClientNodeSupport.compose(
        callTimedShelveAsync(shelvingTime),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callTimedShelveAsync(
      @Nullable Double shelvingTime) {
    return callTimedShelveWithAsync(MethodCallOptions.DEFAULT, shelvingTime);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callTimedShelveWithAsync(
      MethodCallOptions options, @Nullable Double shelvingTime) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new ShelvedStateMachineTypeTimedShelve.Inputs(shelvingTime)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getTimedShelveMethodNodeAsync(),
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
  public @Nullable UaMethodNode getTimedShelve2MethodNode() throws UaException {
    return ClientNodeSupport.await(getTimedShelve2MethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getTimedShelve2MethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "TimedShelve2",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void timedShelve2(@Nullable Double shelvingTime, @Nullable LocalizedText comment)
      throws UaException {
    ClientNodeSupport.await(timedShelve2Async(shelvingTime, comment));
  }

  @Override
  public MethodCallResult<Void> callTimedShelve2(
      @Nullable Double shelvingTime, @Nullable LocalizedText comment) throws UaException {
    return ClientNodeSupport.await(callTimedShelve2Async(shelvingTime, comment));
  }

  @Override
  public MethodCallResult<Void> callTimedShelve2With(
      MethodCallOptions options, @Nullable Double shelvingTime, @Nullable LocalizedText comment)
      throws UaException {
    return ClientNodeSupport.await(callTimedShelve2WithAsync(options, shelvingTime, comment));
  }

  @Override
  public CompletableFuture<Void> timedShelve2Async(
      @Nullable Double shelvingTime, @Nullable LocalizedText comment) {
    return ClientNodeSupport.compose(
        callTimedShelve2Async(shelvingTime, comment),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callTimedShelve2Async(
      @Nullable Double shelvingTime, @Nullable LocalizedText comment) {
    return callTimedShelve2WithAsync(MethodCallOptions.DEFAULT, shelvingTime, comment);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callTimedShelve2WithAsync(
      MethodCallOptions options, @Nullable Double shelvingTime, @Nullable LocalizedText comment) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new ShelvedStateMachineTypeTimedShelve2.Inputs(shelvingTime, comment)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getTimedShelve2MethodNodeAsync(),
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
  public UaMethodNode getUnshelveMethodNode() throws UaException {
    return ClientNodeSupport.await(getUnshelveMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getUnshelveMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "Unshelve",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void unshelve() throws UaException {
    ClientNodeSupport.await(unshelveAsync());
  }

  @Override
  public MethodCallResult<Void> callUnshelve() throws UaException {
    return ClientNodeSupport.await(callUnshelveAsync());
  }

  @Override
  public MethodCallResult<Void> callUnshelveWith(MethodCallOptions options) throws UaException {
    return ClientNodeSupport.await(callUnshelveWithAsync(options));
  }

  @Override
  public CompletableFuture<Void> unshelveAsync() {
    return ClientNodeSupport.compose(
        callUnshelveAsync(),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callUnshelveAsync() {
    return callUnshelveWithAsync(MethodCallOptions.DEFAULT);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callUnshelveWithAsync(
      MethodCallOptions options) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs = new Variant[0];
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getUnshelveMethodNodeAsync(),
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
  public @Nullable UaMethodNode getUnshelve2MethodNode() throws UaException {
    return ClientNodeSupport.await(getUnshelve2MethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getUnshelve2MethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "Unshelve2",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void unshelve2(@Nullable LocalizedText comment) throws UaException {
    ClientNodeSupport.await(unshelve2Async(comment));
  }

  @Override
  public MethodCallResult<Void> callUnshelve2(@Nullable LocalizedText comment) throws UaException {
    return ClientNodeSupport.await(callUnshelve2Async(comment));
  }

  @Override
  public MethodCallResult<Void> callUnshelve2With(
      MethodCallOptions options, @Nullable LocalizedText comment) throws UaException {
    return ClientNodeSupport.await(callUnshelve2WithAsync(options, comment));
  }

  @Override
  public CompletableFuture<Void> unshelve2Async(@Nullable LocalizedText comment) {
    return ClientNodeSupport.compose(
        callUnshelve2Async(comment),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callUnshelve2Async(
      @Nullable LocalizedText comment) {
    return callUnshelve2WithAsync(MethodCallOptions.DEFAULT, comment);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callUnshelve2WithAsync(
      MethodCallOptions options, @Nullable LocalizedText comment) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new ShelvedStateMachineTypeUnshelve2.Inputs(comment)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getUnshelve2MethodNodeAsync(),
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
