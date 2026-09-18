package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.DataSetFolderTypeAddDataSetFolder;
import org.eclipse.milo.opcua.sdk.core.model.methods.DataSetFolderTypeAddPublishedDataItems;
import org.eclipse.milo.opcua.sdk.core.model.methods.DataSetFolderTypeAddPublishedDataItemsTemplate;
import org.eclipse.milo.opcua.sdk.core.model.methods.DataSetFolderTypeAddPublishedEvents;
import org.eclipse.milo.opcua.sdk.core.model.methods.DataSetFolderTypeAddPublishedEventsTemplate;
import org.eclipse.milo.opcua.sdk.core.model.methods.DataSetFolderTypeRemoveDataSetFolder;
import org.eclipse.milo.opcua.sdk.core.model.methods.DataSetFolderTypeRemovePublishedDataSet;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldFlags;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedVariableDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link DataSetFolderType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.1">Model
 *     documentation</a>
 */
public class DataSetFolderTypeNode extends FolderTypeNode implements DataSetFolderType {
  public DataSetFolderTypeNode(
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
  public @Nullable UaMethodNode getAddDataSetFolderMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddDataSetFolderMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getAddDataSetFolderMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddDataSetFolder",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable NodeId addDataSetFolder(@Nullable String name) throws UaException {
    return ClientNodeSupport.await(addDataSetFolderAsync(name));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddDataSetFolder(@Nullable String name)
      throws UaException {
    return ClientNodeSupport.await(callAddDataSetFolderAsync(name));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddDataSetFolderWith(
      MethodCallOptions options, @Nullable String name) throws UaException {
    return ClientNodeSupport.await(callAddDataSetFolderWithAsync(options, name));
  }

  @Override
  public CompletableFuture<@Nullable NodeId> addDataSetFolderAsync(@Nullable String name) {
    return ClientNodeSupport.compose(
        callAddDataSetFolderAsync(name),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddDataSetFolderAsync(
      @Nullable String name) {
    return callAddDataSetFolderWithAsync(MethodCallOptions.DEFAULT, name);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddDataSetFolderWithAsync(
      MethodCallOptions options, @Nullable String name) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new DataSetFolderTypeAddDataSetFolder.Inputs(name)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddDataSetFolderMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return DataSetFolderTypeAddDataSetFolder.Outputs.fromVariants(
                                            client.getStaticEncodingContext(), values)
                                        .dataSetFolderNodeId();
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getAddPublishedDataItemsMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddPublishedDataItemsMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getAddPublishedDataItemsMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddPublishedDataItems",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public DataSetFolderTypeAddPublishedDataItems.Outputs addPublishedDataItems(
      @Nullable String name,
      @Nullable String @Nullable [] fieldNameAliases,
      DataSetFieldFlags @Nullable [] fieldFlags,
      @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
      throws UaException {
    return ClientNodeSupport.await(
        addPublishedDataItemsAsync(name, fieldNameAliases, fieldFlags, variablesToAdd));
  }

  @Override
  public MethodCallResult<DataSetFolderTypeAddPublishedDataItems.Outputs> callAddPublishedDataItems(
      @Nullable String name,
      @Nullable String @Nullable [] fieldNameAliases,
      DataSetFieldFlags @Nullable [] fieldFlags,
      @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
      throws UaException {
    return ClientNodeSupport.await(
        callAddPublishedDataItemsAsync(name, fieldNameAliases, fieldFlags, variablesToAdd));
  }

  @Override
  public MethodCallResult<DataSetFolderTypeAddPublishedDataItems.Outputs>
      callAddPublishedDataItemsWith(
          MethodCallOptions options,
          @Nullable String name,
          @Nullable String @Nullable [] fieldNameAliases,
          DataSetFieldFlags @Nullable [] fieldFlags,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
          throws UaException {
    return ClientNodeSupport.await(
        callAddPublishedDataItemsWithAsync(
            options, name, fieldNameAliases, fieldFlags, variablesToAdd));
  }

  @Override
  public CompletableFuture<DataSetFolderTypeAddPublishedDataItems.Outputs>
      addPublishedDataItemsAsync(
          @Nullable String name,
          @Nullable String @Nullable [] fieldNameAliases,
          DataSetFieldFlags @Nullable [] fieldFlags,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd) {
    return ClientNodeSupport.compose(
        callAddPublishedDataItemsAsync(name, fieldNameAliases, fieldFlags, variablesToAdd),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<DataSetFolderTypeAddPublishedDataItems.Outputs>>
      callAddPublishedDataItemsAsync(
          @Nullable String name,
          @Nullable String @Nullable [] fieldNameAliases,
          DataSetFieldFlags @Nullable [] fieldFlags,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd) {
    return callAddPublishedDataItemsWithAsync(
        MethodCallOptions.DEFAULT, name, fieldNameAliases, fieldFlags, variablesToAdd);
  }

  @Override
  public CompletableFuture<MethodCallResult<DataSetFolderTypeAddPublishedDataItems.Outputs>>
      callAddPublishedDataItemsWithAsync(
          MethodCallOptions options,
          @Nullable String name,
          @Nullable String @Nullable [] fieldNameAliases,
          DataSetFieldFlags @Nullable [] fieldFlags,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new DataSetFolderTypeAddPublishedDataItems.Inputs(
                      name, fieldNameAliases, fieldFlags, variablesToAdd)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddPublishedDataItemsMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return DataSetFolderTypeAddPublishedDataItems.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values);
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getAddPublishedDataItemsTemplateMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddPublishedDataItemsTemplateMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode>
      getAddPublishedDataItemsTemplateMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddPublishedDataItemsTemplate",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public DataSetFolderTypeAddPublishedDataItemsTemplate.Outputs addPublishedDataItemsTemplate(
      @Nullable String name,
      @Nullable DataSetMetaDataType dataSetMetaData,
      @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
      throws UaException {
    return ClientNodeSupport.await(
        addPublishedDataItemsTemplateAsync(name, dataSetMetaData, variablesToAdd));
  }

  @Override
  public MethodCallResult<DataSetFolderTypeAddPublishedDataItemsTemplate.Outputs>
      callAddPublishedDataItemsTemplate(
          @Nullable String name,
          @Nullable DataSetMetaDataType dataSetMetaData,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
          throws UaException {
    return ClientNodeSupport.await(
        callAddPublishedDataItemsTemplateAsync(name, dataSetMetaData, variablesToAdd));
  }

  @Override
  public MethodCallResult<DataSetFolderTypeAddPublishedDataItemsTemplate.Outputs>
      callAddPublishedDataItemsTemplateWith(
          MethodCallOptions options,
          @Nullable String name,
          @Nullable DataSetMetaDataType dataSetMetaData,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
          throws UaException {
    return ClientNodeSupport.await(
        callAddPublishedDataItemsTemplateWithAsync(options, name, dataSetMetaData, variablesToAdd));
  }

  @Override
  public CompletableFuture<DataSetFolderTypeAddPublishedDataItemsTemplate.Outputs>
      addPublishedDataItemsTemplateAsync(
          @Nullable String name,
          @Nullable DataSetMetaDataType dataSetMetaData,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd) {
    return ClientNodeSupport.compose(
        callAddPublishedDataItemsTemplateAsync(name, dataSetMetaData, variablesToAdd),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<DataSetFolderTypeAddPublishedDataItemsTemplate.Outputs>>
      callAddPublishedDataItemsTemplateAsync(
          @Nullable String name,
          @Nullable DataSetMetaDataType dataSetMetaData,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd) {
    return callAddPublishedDataItemsTemplateWithAsync(
        MethodCallOptions.DEFAULT, name, dataSetMetaData, variablesToAdd);
  }

  @Override
  public CompletableFuture<MethodCallResult<DataSetFolderTypeAddPublishedDataItemsTemplate.Outputs>>
      callAddPublishedDataItemsTemplateWithAsync(
          MethodCallOptions options,
          @Nullable String name,
          @Nullable DataSetMetaDataType dataSetMetaData,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new DataSetFolderTypeAddPublishedDataItemsTemplate.Inputs(
                      name, dataSetMetaData, variablesToAdd)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddPublishedDataItemsTemplateMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return DataSetFolderTypeAddPublishedDataItemsTemplate.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values);
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getAddPublishedEventsMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddPublishedEventsMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getAddPublishedEventsMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddPublishedEvents",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public DataSetFolderTypeAddPublishedEvents.Outputs addPublishedEvents(
      @Nullable String name,
      @Nullable NodeId eventNotifier,
      @Nullable String @Nullable [] fieldNameAliases,
      DataSetFieldFlags @Nullable [] fieldFlags,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
      @Nullable ContentFilter filter)
      throws UaException {
    return ClientNodeSupport.await(
        addPublishedEventsAsync(
            name, eventNotifier, fieldNameAliases, fieldFlags, selectedFields, filter));
  }

  @Override
  public MethodCallResult<DataSetFolderTypeAddPublishedEvents.Outputs> callAddPublishedEvents(
      @Nullable String name,
      @Nullable NodeId eventNotifier,
      @Nullable String @Nullable [] fieldNameAliases,
      DataSetFieldFlags @Nullable [] fieldFlags,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
      @Nullable ContentFilter filter)
      throws UaException {
    return ClientNodeSupport.await(
        callAddPublishedEventsAsync(
            name, eventNotifier, fieldNameAliases, fieldFlags, selectedFields, filter));
  }

  @Override
  public MethodCallResult<DataSetFolderTypeAddPublishedEvents.Outputs> callAddPublishedEventsWith(
      MethodCallOptions options,
      @Nullable String name,
      @Nullable NodeId eventNotifier,
      @Nullable String @Nullable [] fieldNameAliases,
      DataSetFieldFlags @Nullable [] fieldFlags,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
      @Nullable ContentFilter filter)
      throws UaException {
    return ClientNodeSupport.await(
        callAddPublishedEventsWithAsync(
            options, name, eventNotifier, fieldNameAliases, fieldFlags, selectedFields, filter));
  }

  @Override
  public CompletableFuture<DataSetFolderTypeAddPublishedEvents.Outputs> addPublishedEventsAsync(
      @Nullable String name,
      @Nullable NodeId eventNotifier,
      @Nullable String @Nullable [] fieldNameAliases,
      DataSetFieldFlags @Nullable [] fieldFlags,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
      @Nullable ContentFilter filter) {
    return ClientNodeSupport.compose(
        callAddPublishedEventsAsync(
            name, eventNotifier, fieldNameAliases, fieldFlags, selectedFields, filter),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<DataSetFolderTypeAddPublishedEvents.Outputs>>
      callAddPublishedEventsAsync(
          @Nullable String name,
          @Nullable NodeId eventNotifier,
          @Nullable String @Nullable [] fieldNameAliases,
          DataSetFieldFlags @Nullable [] fieldFlags,
          @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
          @Nullable ContentFilter filter) {
    return callAddPublishedEventsWithAsync(
        MethodCallOptions.DEFAULT,
        name,
        eventNotifier,
        fieldNameAliases,
        fieldFlags,
        selectedFields,
        filter);
  }

  @Override
  public CompletableFuture<MethodCallResult<DataSetFolderTypeAddPublishedEvents.Outputs>>
      callAddPublishedEventsWithAsync(
          MethodCallOptions options,
          @Nullable String name,
          @Nullable NodeId eventNotifier,
          @Nullable String @Nullable [] fieldNameAliases,
          DataSetFieldFlags @Nullable [] fieldFlags,
          @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
          @Nullable ContentFilter filter) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new DataSetFolderTypeAddPublishedEvents.Inputs(
                      name, eventNotifier, fieldNameAliases, fieldFlags, selectedFields, filter)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddPublishedEventsMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return DataSetFolderTypeAddPublishedEvents.Outputs.fromVariants(
                                        client.getStaticEncodingContext(), values);
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getAddPublishedEventsTemplateMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddPublishedEventsTemplateMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getAddPublishedEventsTemplateMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddPublishedEventsTemplate",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable NodeId addPublishedEventsTemplate(
      @Nullable String name,
      @Nullable DataSetMetaDataType dataSetMetaData,
      @Nullable NodeId eventNotifier,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
      @Nullable ContentFilter filter)
      throws UaException {
    return ClientNodeSupport.await(
        addPublishedEventsTemplateAsync(
            name, dataSetMetaData, eventNotifier, selectedFields, filter));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddPublishedEventsTemplate(
      @Nullable String name,
      @Nullable DataSetMetaDataType dataSetMetaData,
      @Nullable NodeId eventNotifier,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
      @Nullable ContentFilter filter)
      throws UaException {
    return ClientNodeSupport.await(
        callAddPublishedEventsTemplateAsync(
            name, dataSetMetaData, eventNotifier, selectedFields, filter));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddPublishedEventsTemplateWith(
      MethodCallOptions options,
      @Nullable String name,
      @Nullable DataSetMetaDataType dataSetMetaData,
      @Nullable NodeId eventNotifier,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
      @Nullable ContentFilter filter)
      throws UaException {
    return ClientNodeSupport.await(
        callAddPublishedEventsTemplateWithAsync(
            options, name, dataSetMetaData, eventNotifier, selectedFields, filter));
  }

  @Override
  public CompletableFuture<@Nullable NodeId> addPublishedEventsTemplateAsync(
      @Nullable String name,
      @Nullable DataSetMetaDataType dataSetMetaData,
      @Nullable NodeId eventNotifier,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
      @Nullable ContentFilter filter) {
    return ClientNodeSupport.compose(
        callAddPublishedEventsTemplateAsync(
            name, dataSetMetaData, eventNotifier, selectedFields, filter),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddPublishedEventsTemplateAsync(
      @Nullable String name,
      @Nullable DataSetMetaDataType dataSetMetaData,
      @Nullable NodeId eventNotifier,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
      @Nullable ContentFilter filter) {
    return callAddPublishedEventsTemplateWithAsync(
        MethodCallOptions.DEFAULT, name, dataSetMetaData, eventNotifier, selectedFields, filter);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>>
      callAddPublishedEventsTemplateWithAsync(
          MethodCallOptions options,
          @Nullable String name,
          @Nullable DataSetMetaDataType dataSetMetaData,
          @Nullable NodeId eventNotifier,
          @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
          @Nullable ContentFilter filter) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new DataSetFolderTypeAddPublishedEventsTemplate.Inputs(
                      name, dataSetMetaData, eventNotifier, selectedFields, filter)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddPublishedEventsTemplateMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return DataSetFolderTypeAddPublishedEventsTemplate.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values)
                                        .dataSetNodeId();
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getRemoveDataSetFolderMethodNode() throws UaException {
    return ClientNodeSupport.await(getRemoveDataSetFolderMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getRemoveDataSetFolderMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "RemoveDataSetFolder",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void removeDataSetFolder(@Nullable NodeId dataSetFolderNodeId) throws UaException {
    ClientNodeSupport.await(removeDataSetFolderAsync(dataSetFolderNodeId));
  }

  @Override
  public MethodCallResult<Void> callRemoveDataSetFolder(@Nullable NodeId dataSetFolderNodeId)
      throws UaException {
    return ClientNodeSupport.await(callRemoveDataSetFolderAsync(dataSetFolderNodeId));
  }

  @Override
  public MethodCallResult<Void> callRemoveDataSetFolderWith(
      MethodCallOptions options, @Nullable NodeId dataSetFolderNodeId) throws UaException {
    return ClientNodeSupport.await(callRemoveDataSetFolderWithAsync(options, dataSetFolderNodeId));
  }

  @Override
  public CompletableFuture<Void> removeDataSetFolderAsync(@Nullable NodeId dataSetFolderNodeId) {
    return ClientNodeSupport.compose(
        callRemoveDataSetFolderAsync(dataSetFolderNodeId),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveDataSetFolderAsync(
      @Nullable NodeId dataSetFolderNodeId) {
    return callRemoveDataSetFolderWithAsync(MethodCallOptions.DEFAULT, dataSetFolderNodeId);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveDataSetFolderWithAsync(
      MethodCallOptions options, @Nullable NodeId dataSetFolderNodeId) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new DataSetFolderTypeRemoveDataSetFolder.Inputs(dataSetFolderNodeId)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRemoveDataSetFolderMethodNodeAsync(),
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
  public @Nullable UaMethodNode getRemovePublishedDataSetMethodNode() throws UaException {
    return ClientNodeSupport.await(getRemovePublishedDataSetMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getRemovePublishedDataSetMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "RemovePublishedDataSet",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void removePublishedDataSet(@Nullable NodeId dataSetNodeId) throws UaException {
    ClientNodeSupport.await(removePublishedDataSetAsync(dataSetNodeId));
  }

  @Override
  public MethodCallResult<Void> callRemovePublishedDataSet(@Nullable NodeId dataSetNodeId)
      throws UaException {
    return ClientNodeSupport.await(callRemovePublishedDataSetAsync(dataSetNodeId));
  }

  @Override
  public MethodCallResult<Void> callRemovePublishedDataSetWith(
      MethodCallOptions options, @Nullable NodeId dataSetNodeId) throws UaException {
    return ClientNodeSupport.await(callRemovePublishedDataSetWithAsync(options, dataSetNodeId));
  }

  @Override
  public CompletableFuture<Void> removePublishedDataSetAsync(@Nullable NodeId dataSetNodeId) {
    return ClientNodeSupport.compose(
        callRemovePublishedDataSetAsync(dataSetNodeId),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemovePublishedDataSetAsync(
      @Nullable NodeId dataSetNodeId) {
    return callRemovePublishedDataSetWithAsync(MethodCallOptions.DEFAULT, dataSetNodeId);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemovePublishedDataSetWithAsync(
      MethodCallOptions options, @Nullable NodeId dataSetNodeId) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new DataSetFolderTypeRemovePublishedDataSet.Inputs(dataSetNodeId)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRemovePublishedDataSetMethodNodeAsync(),
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
