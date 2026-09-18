package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.DialogConditionTypeRespond;
import org.eclipse.milo.opcua.sdk.core.model.methods.DialogConditionTypeRespond2;
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
 * Node implementation of {@link DialogConditionType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.2">Model
 *     documentation</a>
 */
public class DialogConditionTypeNode extends ConditionTypeNode implements DialogConditionType {
  public DialogConditionTypeNode(
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
  public PropertyTypeNode getOkResponseNode() throws UaException {
    return ClientNodeSupport.await(getOkResponseNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getOkResponseNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "OkResponse",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Integer readOkResponse() throws UaException {
    return ClientNodeSupport.await(readOkResponseAsync());
  }

  @Override
  public void writeOkResponse(@Nullable Integer value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeOkResponseAsync(value)),
        "http://opcfoundation.org/UA/}OkResponse");
  }

  @Override
  public CompletableFuture<? extends @Nullable Integer> readOkResponseAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getOkResponseNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}OkResponse",
                            true,
                            Integer.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Integer) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeOkResponseAsync(@Nullable Integer value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getOkResponseNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}OkResponse",
                        value,
                        Integer.class,
                        -1,
                        null)));
  }

  @Override
  public TwoStateVariableTypeNode getDialogStateNode() throws UaException {
    return ClientNodeSupport.await(getDialogStateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends TwoStateVariableTypeNode> getDialogStateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DialogState",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        TwoStateVariableTypeNode.class)));
  }

  @Override
  public @Nullable LocalizedText readDialogState() throws UaException {
    return ClientNodeSupport.await(readDialogStateAsync());
  }

  @Override
  public void writeDialogState(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDialogStateAsync(value)),
        "http://opcfoundation.org/UA/}DialogState");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readDialogStateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDialogStateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DialogState",
                            true,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDialogStateAsync(@Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDialogStateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DialogState",
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
  public PropertyTypeNode getLastResponseNode() throws UaException {
    return ClientNodeSupport.await(getLastResponseNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLastResponseNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LastResponse",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Integer readLastResponse() throws UaException {
    return ClientNodeSupport.await(readLastResponseAsync());
  }

  @Override
  public void writeLastResponse(@Nullable Integer value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLastResponseAsync(value)),
        "http://opcfoundation.org/UA/}LastResponse");
  }

  @Override
  public CompletableFuture<? extends @Nullable Integer> readLastResponseAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLastResponseNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LastResponse",
                            true,
                            Integer.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Integer) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLastResponseAsync(@Nullable Integer value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLastResponseNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LastResponse",
                        value,
                        Integer.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getCancelResponseNode() throws UaException {
    return ClientNodeSupport.await(getCancelResponseNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getCancelResponseNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CancelResponse",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Integer readCancelResponse() throws UaException {
    return ClientNodeSupport.await(readCancelResponseAsync());
  }

  @Override
  public void writeCancelResponse(@Nullable Integer value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCancelResponseAsync(value)),
        "http://opcfoundation.org/UA/}CancelResponse");
  }

  @Override
  public CompletableFuture<? extends @Nullable Integer> readCancelResponseAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCancelResponseNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CancelResponse",
                            true,
                            Integer.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Integer) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCancelResponseAsync(@Nullable Integer value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCancelResponseNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CancelResponse",
                        value,
                        Integer.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getDefaultResponseNode() throws UaException {
    return ClientNodeSupport.await(getDefaultResponseNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDefaultResponseNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DefaultResponse",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Integer readDefaultResponse() throws UaException {
    return ClientNodeSupport.await(readDefaultResponseAsync());
  }

  @Override
  public void writeDefaultResponse(@Nullable Integer value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDefaultResponseAsync(value)),
        "http://opcfoundation.org/UA/}DefaultResponse");
  }

  @Override
  public CompletableFuture<? extends @Nullable Integer> readDefaultResponseAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDefaultResponseNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DefaultResponse",
                            true,
                            Integer.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Integer) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDefaultResponseAsync(@Nullable Integer value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDefaultResponseNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DefaultResponse",
                        value,
                        Integer.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getResponseOptionSetNode() throws UaException {
    return ClientNodeSupport.await(getResponseOptionSetNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getResponseOptionSetNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ResponseOptionSet",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public LocalizedText @Nullable [] readResponseOptionSet() throws UaException {
    return ClientNodeSupport.await(readResponseOptionSetAsync());
  }

  @Override
  public void writeResponseOptionSet(LocalizedText @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeResponseOptionSetAsync(value)),
        "http://opcfoundation.org/UA/}ResponseOptionSet");
  }

  @Override
  public CompletableFuture<? extends LocalizedText @Nullable []> readResponseOptionSetAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getResponseOptionSetNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ResponseOptionSet",
                            true,
                            LocalizedText.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((LocalizedText @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeResponseOptionSetAsync(
      LocalizedText @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getResponseOptionSetNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ResponseOptionSet",
                        value,
                        LocalizedText.class,
                        1,
                        null)));
  }

  @Override
  public PropertyTypeNode getPromptNode() throws UaException {
    return ClientNodeSupport.await(getPromptNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPromptNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Prompt",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable LocalizedText readPrompt() throws UaException {
    return ClientNodeSupport.await(readPromptAsync());
  }

  @Override
  public void writePrompt(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writePromptAsync(value)), "http://opcfoundation.org/UA/}Prompt");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readPromptAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getPromptNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Prompt",
                            true,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writePromptAsync(@Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getPromptNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Prompt",
                        value,
                        LocalizedText.class,
                        -1,
                        null)));
  }

  @Override
  public UaMethodNode getRespondMethodNode() throws UaException {
    return ClientNodeSupport.await(getRespondMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getRespondMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "Respond",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void respond(@Nullable Integer selectedResponse) throws UaException {
    ClientNodeSupport.await(respondAsync(selectedResponse));
  }

  @Override
  public MethodCallResult<Void> callRespond(@Nullable Integer selectedResponse) throws UaException {
    return ClientNodeSupport.await(callRespondAsync(selectedResponse));
  }

  @Override
  public MethodCallResult<Void> callRespondWith(
      MethodCallOptions options, @Nullable Integer selectedResponse) throws UaException {
    return ClientNodeSupport.await(callRespondWithAsync(options, selectedResponse));
  }

  @Override
  public CompletableFuture<Void> respondAsync(@Nullable Integer selectedResponse) {
    return ClientNodeSupport.compose(
        callRespondAsync(selectedResponse),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRespondAsync(
      @Nullable Integer selectedResponse) {
    return callRespondWithAsync(MethodCallOptions.DEFAULT, selectedResponse);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRespondWithAsync(
      MethodCallOptions options, @Nullable Integer selectedResponse) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new DialogConditionTypeRespond.Inputs(selectedResponse)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRespondMethodNodeAsync(),
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
  public @Nullable UaMethodNode getRespond2MethodNode() throws UaException {
    return ClientNodeSupport.await(getRespond2MethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getRespond2MethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "Respond2",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void respond2(@Nullable Integer selectedResponse, @Nullable LocalizedText comment)
      throws UaException {
    ClientNodeSupport.await(respond2Async(selectedResponse, comment));
  }

  @Override
  public MethodCallResult<Void> callRespond2(
      @Nullable Integer selectedResponse, @Nullable LocalizedText comment) throws UaException {
    return ClientNodeSupport.await(callRespond2Async(selectedResponse, comment));
  }

  @Override
  public MethodCallResult<Void> callRespond2With(
      MethodCallOptions options,
      @Nullable Integer selectedResponse,
      @Nullable LocalizedText comment)
      throws UaException {
    return ClientNodeSupport.await(callRespond2WithAsync(options, selectedResponse, comment));
  }

  @Override
  public CompletableFuture<Void> respond2Async(
      @Nullable Integer selectedResponse, @Nullable LocalizedText comment) {
    return ClientNodeSupport.compose(
        callRespond2Async(selectedResponse, comment),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRespond2Async(
      @Nullable Integer selectedResponse, @Nullable LocalizedText comment) {
    return callRespond2WithAsync(MethodCallOptions.DEFAULT, selectedResponse, comment);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRespond2WithAsync(
      MethodCallOptions options,
      @Nullable Integer selectedResponse,
      @Nullable LocalizedText comment) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new DialogConditionTypeRespond2.Inputs(selectedResponse, comment)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRespond2MethodNodeAsync(),
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
