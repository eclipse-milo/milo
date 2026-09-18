package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the PublishedEventsType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.1">Model
 *     documentation</a>
 */
public interface PublishedEventsType extends PublishedDataSetType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 14572L);

  QualifiedProperty<NodeId> EventNotifier__PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EventNotifier",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  QualifiedProperty<SimpleAttributeOperand[]> SelectedFields_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SelectedFields",
          ExpandedNodeId.of(Namespaces.OPC_UA, 601L),
          1,
          SimpleAttributeOperand[].class);

  QualifiedProperty<ContentFilter> Filter_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Filter",
          ExpandedNodeId.of(Namespaces.OPC_UA, 586L),
          -1,
          ContentFilter.class);

  /**
   * Resolves the mandatory EventNotifier child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getEventNotifier_Node() throws UaException;

  /** Asynchronous form of {@link #getEventNotifier_Node()}. */
  CompletableFuture<? extends PropertyType> getEventNotifier_NodeAsync();

  /**
   * Reads the Value of the EventNotifier child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readEventNotifier_() throws UaException;

  /**
   * Writes the Value of the EventNotifier child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEventNotifier_(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readEventNotifier_()}. */
  CompletableFuture<? extends @Nullable NodeId> readEventNotifier_Async();

  /** Asynchronous form of {@link #writeEventNotifier_}; completes with the operation status. */
  CompletableFuture<StatusCode> writeEventNotifier_Async(@Nullable NodeId value);

  /**
   * Resolves the mandatory SelectedFields child, a PropertyType with DataType
   * SimpleAttributeOperand.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSelectedFieldsNode() throws UaException;

  /** Asynchronous form of {@link #getSelectedFieldsNode()}. */
  CompletableFuture<? extends PropertyType> getSelectedFieldsNodeAsync();

  /**
   * Reads the Value of the SelectedFields child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable SimpleAttributeOperand @Nullable [] readSelectedFields() throws UaException;

  /**
   * Writes the Value of the SelectedFields child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSelectedFields(@Nullable SimpleAttributeOperand @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readSelectedFields()}. */
  CompletableFuture<? extends @Nullable SimpleAttributeOperand @Nullable []>
      readSelectedFieldsAsync();

  /** Asynchronous form of {@link #writeSelectedFields}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSelectedFieldsAsync(
      @Nullable SimpleAttributeOperand @Nullable [] value);

  /**
   * Resolves the mandatory Filter child, a PropertyType with DataType ContentFilter.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getFilterNode() throws UaException;

  /** Asynchronous form of {@link #getFilterNode()}. */
  CompletableFuture<? extends PropertyType> getFilterNodeAsync();

  /**
   * Reads the Value of the Filter child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ContentFilter readFilter() throws UaException;

  /**
   * Writes the Value of the Filter child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeFilter(@Nullable ContentFilter value) throws UaException;

  /** Asynchronous form of {@link #readFilter()}. */
  CompletableFuture<? extends @Nullable ContentFilter> readFilterAsync();

  /** Asynchronous form of {@link #writeFilter}; completes with the operation status. */
  CompletableFuture<StatusCode> writeFilterAsync(@Nullable ContentFilter value);

  /**
   * Resolves the optional ModifyFieldSelection Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.2">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getModifyFieldSelectionMethodNode() throws UaException;

  /** Asynchronous form of {@link #getModifyFieldSelectionMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getModifyFieldSelectionMethodNodeAsync();

  /**
   * Calls the ModifyFieldSelection Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.2">Model
   *     documentation</a>
   */
  @Nullable ConfigurationVersionDataType modifyFieldSelection(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable String @Nullable [] fieldNameAliases,
      Boolean @Nullable [] promotedFields,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields)
      throws UaException;

  /**
   * Calls the ModifyFieldSelection Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable ConfigurationVersionDataType> callModifyFieldSelection(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable String @Nullable [] fieldNameAliases,
      Boolean @Nullable [] promotedFields,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields)
      throws UaException;

  /**
   * Calls the ModifyFieldSelection Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable ConfigurationVersionDataType> callModifyFieldSelectionWith(
      MethodCallOptions options,
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable String @Nullable [] fieldNameAliases,
      Boolean @Nullable [] promotedFields,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields)
      throws UaException;

  /** Asynchronous form of {@link #modifyFieldSelection}. */
  CompletableFuture<@Nullable ConfigurationVersionDataType> modifyFieldSelectionAsync(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable String @Nullable [] fieldNameAliases,
      Boolean @Nullable [] promotedFields,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields);

  /** Asynchronous form of {@link #callModifyFieldSelection}. */
  CompletableFuture<MethodCallResult<@Nullable ConfigurationVersionDataType>>
      callModifyFieldSelectionAsync(
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable String @Nullable [] fieldNameAliases,
          Boolean @Nullable [] promotedFields,
          @Nullable SimpleAttributeOperand @Nullable [] selectedFields);

  /** Asynchronous form of {@link #callModifyFieldSelectionWith}. */
  CompletableFuture<MethodCallResult<@Nullable ConfigurationVersionDataType>>
      callModifyFieldSelectionWithAsync(
          MethodCallOptions options,
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable String @Nullable [] fieldNameAliases,
          Boolean @Nullable [] promotedFields,
          @Nullable SimpleAttributeOperand @Nullable [] selectedFields);
}
