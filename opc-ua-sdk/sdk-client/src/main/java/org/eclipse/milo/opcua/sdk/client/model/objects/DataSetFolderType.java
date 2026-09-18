package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.DataSetFolderTypeAddPublishedDataItems;
import org.eclipse.milo.opcua.sdk.core.model.methods.DataSetFolderTypeAddPublishedDataItemsTemplate;
import org.eclipse.milo.opcua.sdk.core.model.methods.DataSetFolderTypeAddPublishedEvents;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldFlags;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedVariableDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the DataSetFolderType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.1">Model
 *     documentation</a>
 */
public interface DataSetFolderType extends FolderType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 14477L);

  /**
   * Resolves the optional AddDataSetFolder Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.7">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddDataSetFolderMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddDataSetFolderMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getAddDataSetFolderMethodNodeAsync();

  /**
   * Calls the AddDataSetFolder Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.7">Model
   *     documentation</a>
   */
  @Nullable NodeId addDataSetFolder(@Nullable String name) throws UaException;

  /**
   * Calls the AddDataSetFolder Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddDataSetFolder(@Nullable String name) throws UaException;

  /**
   * Calls the AddDataSetFolder Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddDataSetFolderWith(
      MethodCallOptions options, @Nullable String name) throws UaException;

  /** Asynchronous form of {@link #addDataSetFolder}. */
  CompletableFuture<@Nullable NodeId> addDataSetFolderAsync(@Nullable String name);

  /** Asynchronous form of {@link #callAddDataSetFolder}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddDataSetFolderAsync(
      @Nullable String name);

  /** Asynchronous form of {@link #callAddDataSetFolderWith}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddDataSetFolderWithAsync(
      MethodCallOptions options, @Nullable String name);

  /**
   * Resolves the optional AddPublishedDataItems Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.2">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddPublishedDataItemsMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddPublishedDataItemsMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getAddPublishedDataItemsMethodNodeAsync();

  /**
   * Calls the AddPublishedDataItems Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.2">Model
   *     documentation</a>
   */
  DataSetFolderTypeAddPublishedDataItems.Outputs addPublishedDataItems(
      @Nullable String name,
      @Nullable String @Nullable [] fieldNameAliases,
      DataSetFieldFlags @Nullable [] fieldFlags,
      @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
      throws UaException;

  /**
   * Calls the AddPublishedDataItems Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<DataSetFolderTypeAddPublishedDataItems.Outputs> callAddPublishedDataItems(
      @Nullable String name,
      @Nullable String @Nullable [] fieldNameAliases,
      DataSetFieldFlags @Nullable [] fieldFlags,
      @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
      throws UaException;

  /**
   * Calls the AddPublishedDataItems Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<DataSetFolderTypeAddPublishedDataItems.Outputs> callAddPublishedDataItemsWith(
      MethodCallOptions options,
      @Nullable String name,
      @Nullable String @Nullable [] fieldNameAliases,
      DataSetFieldFlags @Nullable [] fieldFlags,
      @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
      throws UaException;

  /** Asynchronous form of {@link #addPublishedDataItems}. */
  CompletableFuture<DataSetFolderTypeAddPublishedDataItems.Outputs> addPublishedDataItemsAsync(
      @Nullable String name,
      @Nullable String @Nullable [] fieldNameAliases,
      DataSetFieldFlags @Nullable [] fieldFlags,
      @Nullable PublishedVariableDataType @Nullable [] variablesToAdd);

  /** Asynchronous form of {@link #callAddPublishedDataItems}. */
  CompletableFuture<MethodCallResult<DataSetFolderTypeAddPublishedDataItems.Outputs>>
      callAddPublishedDataItemsAsync(
          @Nullable String name,
          @Nullable String @Nullable [] fieldNameAliases,
          DataSetFieldFlags @Nullable [] fieldFlags,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd);

  /** Asynchronous form of {@link #callAddPublishedDataItemsWith}. */
  CompletableFuture<MethodCallResult<DataSetFolderTypeAddPublishedDataItems.Outputs>>
      callAddPublishedDataItemsWithAsync(
          MethodCallOptions options,
          @Nullable String name,
          @Nullable String @Nullable [] fieldNameAliases,
          DataSetFieldFlags @Nullable [] fieldFlags,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd);

  /**
   * Resolves the optional AddPublishedDataItemsTemplate Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.4">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddPublishedDataItemsTemplateMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddPublishedDataItemsTemplateMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getAddPublishedDataItemsTemplateMethodNodeAsync();

  /**
   * Calls the AddPublishedDataItemsTemplate Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.4">Model
   *     documentation</a>
   */
  DataSetFolderTypeAddPublishedDataItemsTemplate.Outputs addPublishedDataItemsTemplate(
      @Nullable String name,
      @Nullable DataSetMetaDataType dataSetMetaData,
      @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
      throws UaException;

  /**
   * Calls the AddPublishedDataItemsTemplate Method and returns the complete result, including a Bad
   * status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<DataSetFolderTypeAddPublishedDataItemsTemplate.Outputs>
      callAddPublishedDataItemsTemplate(
          @Nullable String name,
          @Nullable DataSetMetaDataType dataSetMetaData,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
          throws UaException;

  /**
   * Calls the AddPublishedDataItemsTemplate Method with explicit options and returns the complete
   * result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<DataSetFolderTypeAddPublishedDataItemsTemplate.Outputs>
      callAddPublishedDataItemsTemplateWith(
          MethodCallOptions options,
          @Nullable String name,
          @Nullable DataSetMetaDataType dataSetMetaData,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
          throws UaException;

  /** Asynchronous form of {@link #addPublishedDataItemsTemplate}. */
  CompletableFuture<DataSetFolderTypeAddPublishedDataItemsTemplate.Outputs>
      addPublishedDataItemsTemplateAsync(
          @Nullable String name,
          @Nullable DataSetMetaDataType dataSetMetaData,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd);

  /** Asynchronous form of {@link #callAddPublishedDataItemsTemplate}. */
  CompletableFuture<MethodCallResult<DataSetFolderTypeAddPublishedDataItemsTemplate.Outputs>>
      callAddPublishedDataItemsTemplateAsync(
          @Nullable String name,
          @Nullable DataSetMetaDataType dataSetMetaData,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd);

  /** Asynchronous form of {@link #callAddPublishedDataItemsTemplateWith}. */
  CompletableFuture<MethodCallResult<DataSetFolderTypeAddPublishedDataItemsTemplate.Outputs>>
      callAddPublishedDataItemsTemplateWithAsync(
          MethodCallOptions options,
          @Nullable String name,
          @Nullable DataSetMetaDataType dataSetMetaData,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd);

  /**
   * Resolves the optional AddPublishedEvents Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddPublishedEventsMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddPublishedEventsMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getAddPublishedEventsMethodNodeAsync();

  /**
   * Calls the AddPublishedEvents Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.3">Model
   *     documentation</a>
   */
  DataSetFolderTypeAddPublishedEvents.Outputs addPublishedEvents(
      @Nullable String name,
      @Nullable NodeId eventNotifier,
      @Nullable String @Nullable [] fieldNameAliases,
      DataSetFieldFlags @Nullable [] fieldFlags,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
      @Nullable ContentFilter filter)
      throws UaException;

  /**
   * Calls the AddPublishedEvents Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<DataSetFolderTypeAddPublishedEvents.Outputs> callAddPublishedEvents(
      @Nullable String name,
      @Nullable NodeId eventNotifier,
      @Nullable String @Nullable [] fieldNameAliases,
      DataSetFieldFlags @Nullable [] fieldFlags,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
      @Nullable ContentFilter filter)
      throws UaException;

  /**
   * Calls the AddPublishedEvents Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<DataSetFolderTypeAddPublishedEvents.Outputs> callAddPublishedEventsWith(
      MethodCallOptions options,
      @Nullable String name,
      @Nullable NodeId eventNotifier,
      @Nullable String @Nullable [] fieldNameAliases,
      DataSetFieldFlags @Nullable [] fieldFlags,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
      @Nullable ContentFilter filter)
      throws UaException;

  /** Asynchronous form of {@link #addPublishedEvents}. */
  CompletableFuture<DataSetFolderTypeAddPublishedEvents.Outputs> addPublishedEventsAsync(
      @Nullable String name,
      @Nullable NodeId eventNotifier,
      @Nullable String @Nullable [] fieldNameAliases,
      DataSetFieldFlags @Nullable [] fieldFlags,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
      @Nullable ContentFilter filter);

  /** Asynchronous form of {@link #callAddPublishedEvents}. */
  CompletableFuture<MethodCallResult<DataSetFolderTypeAddPublishedEvents.Outputs>>
      callAddPublishedEventsAsync(
          @Nullable String name,
          @Nullable NodeId eventNotifier,
          @Nullable String @Nullable [] fieldNameAliases,
          DataSetFieldFlags @Nullable [] fieldFlags,
          @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
          @Nullable ContentFilter filter);

  /** Asynchronous form of {@link #callAddPublishedEventsWith}. */
  CompletableFuture<MethodCallResult<DataSetFolderTypeAddPublishedEvents.Outputs>>
      callAddPublishedEventsWithAsync(
          MethodCallOptions options,
          @Nullable String name,
          @Nullable NodeId eventNotifier,
          @Nullable String @Nullable [] fieldNameAliases,
          DataSetFieldFlags @Nullable [] fieldFlags,
          @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
          @Nullable ContentFilter filter);

  /**
   * Resolves the optional AddPublishedEventsTemplate Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.5">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddPublishedEventsTemplateMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddPublishedEventsTemplateMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getAddPublishedEventsTemplateMethodNodeAsync();

  /**
   * Calls the AddPublishedEventsTemplate Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.5">Model
   *     documentation</a>
   */
  @Nullable NodeId addPublishedEventsTemplate(
      @Nullable String name,
      @Nullable DataSetMetaDataType dataSetMetaData,
      @Nullable NodeId eventNotifier,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
      @Nullable ContentFilter filter)
      throws UaException;

  /**
   * Calls the AddPublishedEventsTemplate Method and returns the complete result, including a Bad
   * status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddPublishedEventsTemplate(
      @Nullable String name,
      @Nullable DataSetMetaDataType dataSetMetaData,
      @Nullable NodeId eventNotifier,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
      @Nullable ContentFilter filter)
      throws UaException;

  /**
   * Calls the AddPublishedEventsTemplate Method with explicit options and returns the complete
   * result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddPublishedEventsTemplateWith(
      MethodCallOptions options,
      @Nullable String name,
      @Nullable DataSetMetaDataType dataSetMetaData,
      @Nullable NodeId eventNotifier,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
      @Nullable ContentFilter filter)
      throws UaException;

  /** Asynchronous form of {@link #addPublishedEventsTemplate}. */
  CompletableFuture<@Nullable NodeId> addPublishedEventsTemplateAsync(
      @Nullable String name,
      @Nullable DataSetMetaDataType dataSetMetaData,
      @Nullable NodeId eventNotifier,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
      @Nullable ContentFilter filter);

  /** Asynchronous form of {@link #callAddPublishedEventsTemplate}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddPublishedEventsTemplateAsync(
      @Nullable String name,
      @Nullable DataSetMetaDataType dataSetMetaData,
      @Nullable NodeId eventNotifier,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
      @Nullable ContentFilter filter);

  /** Asynchronous form of {@link #callAddPublishedEventsTemplateWith}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddPublishedEventsTemplateWithAsync(
      MethodCallOptions options,
      @Nullable String name,
      @Nullable DataSetMetaDataType dataSetMetaData,
      @Nullable NodeId eventNotifier,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
      @Nullable ContentFilter filter);

  /**
   * Resolves the optional RemoveDataSetFolder Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.8">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveDataSetFolderMethodNode() throws UaException;

  /** Asynchronous form of {@link #getRemoveDataSetFolderMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getRemoveDataSetFolderMethodNodeAsync();

  /**
   * Calls the RemoveDataSetFolder Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.8">Model
   *     documentation</a>
   */
  void removeDataSetFolder(@Nullable NodeId dataSetFolderNodeId) throws UaException;

  /**
   * Calls the RemoveDataSetFolder Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveDataSetFolder(@Nullable NodeId dataSetFolderNodeId)
      throws UaException;

  /**
   * Calls the RemoveDataSetFolder Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveDataSetFolderWith(
      MethodCallOptions options, @Nullable NodeId dataSetFolderNodeId) throws UaException;

  /** Asynchronous form of {@link #removeDataSetFolder}. */
  CompletableFuture<Void> removeDataSetFolderAsync(@Nullable NodeId dataSetFolderNodeId);

  /** Asynchronous form of {@link #callRemoveDataSetFolder}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveDataSetFolderAsync(
      @Nullable NodeId dataSetFolderNodeId);

  /** Asynchronous form of {@link #callRemoveDataSetFolderWith}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveDataSetFolderWithAsync(
      MethodCallOptions options, @Nullable NodeId dataSetFolderNodeId);

  /**
   * Resolves the optional RemovePublishedDataSet Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.6">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemovePublishedDataSetMethodNode() throws UaException;

  /** Asynchronous form of {@link #getRemovePublishedDataSetMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getRemovePublishedDataSetMethodNodeAsync();

  /**
   * Calls the RemovePublishedDataSet Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.6">Model
   *     documentation</a>
   */
  void removePublishedDataSet(@Nullable NodeId dataSetNodeId) throws UaException;

  /**
   * Calls the RemovePublishedDataSet Method and returns the complete result, including a Bad
   * status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemovePublishedDataSet(@Nullable NodeId dataSetNodeId)
      throws UaException;

  /**
   * Calls the RemovePublishedDataSet Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemovePublishedDataSetWith(
      MethodCallOptions options, @Nullable NodeId dataSetNodeId) throws UaException;

  /** Asynchronous form of {@link #removePublishedDataSet}. */
  CompletableFuture<Void> removePublishedDataSetAsync(@Nullable NodeId dataSetNodeId);

  /** Asynchronous form of {@link #callRemovePublishedDataSet}. */
  CompletableFuture<MethodCallResult<Void>> callRemovePublishedDataSetAsync(
      @Nullable NodeId dataSetNodeId);

  /** Asynchronous form of {@link #callRemovePublishedDataSetWith}. */
  CompletableFuture<MethodCallResult<Void>> callRemovePublishedDataSetWithAsync(
      MethodCallOptions options, @Nullable NodeId dataSetNodeId);
}
