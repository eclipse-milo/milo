package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.PublishedEventsTypeModifyFieldSelection;
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
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link PublishedEventsType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.1">Model
 *     documentation</a>
 */
public class PublishedEventsTypeNode extends PublishedDataSetTypeNode
    implements PublishedEventsType {
  public PublishedEventsTypeNode(
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
  public PropertyTypeNode getEventNotifier_Node() throws UaException {
    return ClientNodeSupport.await(getEventNotifier_NodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getEventNotifier_NodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "EventNotifier",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable NodeId readEventNotifier_() throws UaException {
    return ClientNodeSupport.await(readEventNotifier_Async());
  }

  @Override
  public void writeEventNotifier_(@Nullable NodeId value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeEventNotifier_Async(value)),
        "http://opcfoundation.org/UA/}EventNotifier");
  }

  @Override
  public CompletableFuture<? extends @Nullable NodeId> readEventNotifier_Async() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getEventNotifier_NodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}EventNotifier",
                            true,
                            NodeId.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable NodeId) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeEventNotifier_Async(@Nullable NodeId value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getEventNotifier_NodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}EventNotifier",
                        value,
                        NodeId.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getSelectedFieldsNode() throws UaException {
    return ClientNodeSupport.await(getSelectedFieldsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSelectedFieldsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SelectedFields",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable SimpleAttributeOperand @Nullable [] readSelectedFields() throws UaException {
    return ClientNodeSupport.await(readSelectedFieldsAsync());
  }

  @Override
  public void writeSelectedFields(@Nullable SimpleAttributeOperand @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSelectedFieldsAsync(value)),
        "http://opcfoundation.org/UA/}SelectedFields");
  }

  @Override
  public CompletableFuture<? extends @Nullable SimpleAttributeOperand @Nullable []>
      readSelectedFieldsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSelectedFieldsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SelectedFields",
                            true,
                            SimpleAttributeOperand.class,
                            1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable SimpleAttributeOperand @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSelectedFieldsAsync(
      @Nullable SimpleAttributeOperand @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSelectedFieldsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SelectedFields",
                        value,
                        SimpleAttributeOperand.class,
                        1,
                        null)));
  }

  @Override
  public PropertyTypeNode getFilterNode() throws UaException {
    return ClientNodeSupport.await(getFilterNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getFilterNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Filter",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable ContentFilter readFilter() throws UaException {
    return ClientNodeSupport.await(readFilterAsync());
  }

  @Override
  public void writeFilter(@Nullable ContentFilter value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeFilterAsync(value)), "http://opcfoundation.org/UA/}Filter");
  }

  @Override
  public CompletableFuture<? extends @Nullable ContentFilter> readFilterAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getFilterNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Filter",
                            true,
                            ContentFilter.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ContentFilter) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeFilterAsync(@Nullable ContentFilter value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getFilterNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Filter",
                        value,
                        ContentFilter.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable UaMethodNode getModifyFieldSelectionMethodNode() throws UaException {
    return ClientNodeSupport.await(getModifyFieldSelectionMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getModifyFieldSelectionMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "ModifyFieldSelection",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable ConfigurationVersionDataType modifyFieldSelection(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable String @Nullable [] fieldNameAliases,
      Boolean @Nullable [] promotedFields,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields)
      throws UaException {
    return ClientNodeSupport.await(
        modifyFieldSelectionAsync(
            configurationVersion, fieldNameAliases, promotedFields, selectedFields));
  }

  @Override
  public MethodCallResult<@Nullable ConfigurationVersionDataType> callModifyFieldSelection(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable String @Nullable [] fieldNameAliases,
      Boolean @Nullable [] promotedFields,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields)
      throws UaException {
    return ClientNodeSupport.await(
        callModifyFieldSelectionAsync(
            configurationVersion, fieldNameAliases, promotedFields, selectedFields));
  }

  @Override
  public MethodCallResult<@Nullable ConfigurationVersionDataType> callModifyFieldSelectionWith(
      MethodCallOptions options,
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable String @Nullable [] fieldNameAliases,
      Boolean @Nullable [] promotedFields,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields)
      throws UaException {
    return ClientNodeSupport.await(
        callModifyFieldSelectionWithAsync(
            options, configurationVersion, fieldNameAliases, promotedFields, selectedFields));
  }

  @Override
  public CompletableFuture<@Nullable ConfigurationVersionDataType> modifyFieldSelectionAsync(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable String @Nullable [] fieldNameAliases,
      Boolean @Nullable [] promotedFields,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields) {
    return ClientNodeSupport.compose(
        callModifyFieldSelectionAsync(
            configurationVersion, fieldNameAliases, promotedFields, selectedFields),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable ConfigurationVersionDataType>>
      callModifyFieldSelectionAsync(
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable String @Nullable [] fieldNameAliases,
          Boolean @Nullable [] promotedFields,
          @Nullable SimpleAttributeOperand @Nullable [] selectedFields) {
    return callModifyFieldSelectionWithAsync(
        MethodCallOptions.DEFAULT,
        configurationVersion,
        fieldNameAliases,
        promotedFields,
        selectedFields);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable ConfigurationVersionDataType>>
      callModifyFieldSelectionWithAsync(
          MethodCallOptions options,
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable String @Nullable [] fieldNameAliases,
          Boolean @Nullable [] promotedFields,
          @Nullable SimpleAttributeOperand @Nullable [] selectedFields) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new PublishedEventsTypeModifyFieldSelection.Inputs(
                      configurationVersion, fieldNameAliases, promotedFields, selectedFields)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getModifyFieldSelectionMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return PublishedEventsTypeModifyFieldSelection.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values)
                                        .newConfigurationVersion();
                                  }))));
        });
  }
}
