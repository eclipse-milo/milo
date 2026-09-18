package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the SelectionListType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.18">Model
 *     documentation</a>
 */
public interface SelectionListType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 16309L);

  QualifiedProperty<Variant[]> Selections_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Selections",
          ExpandedNodeId.of(Namespaces.OPC_UA, 24L),
          1,
          Variant[].class);

  QualifiedProperty<Boolean> RestrictToList_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "RestrictToList",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<LocalizedText[]> SelectionDescriptions_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SelectionDescriptions",
          ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
          1,
          LocalizedText[].class);

  /**
   * Resolves the mandatory Selections child, a PropertyType with DataType BaseDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSelectionsNode() throws UaException;

  /** Asynchronous form of {@link #getSelectionsNode()}. */
  CompletableFuture<? extends PropertyType> getSelectionsNodeAsync();

  /**
   * Reads the Value of the Selections child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Variant @Nullable [] readSelections() throws UaException;

  /**
   * Writes the Value of the Selections child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSelections(@Nullable Variant @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readSelections()}. */
  CompletableFuture<? extends @Nullable Variant @Nullable []> readSelectionsAsync();

  /** Asynchronous form of {@link #writeSelections}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSelectionsAsync(@Nullable Variant @Nullable [] value);

  /**
   * Resolves the optional RestrictToList child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getRestrictToListNode() throws UaException;

  /** Asynchronous form of {@link #getRestrictToListNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getRestrictToListNodeAsync();

  /**
   * Reads the Value of the RestrictToList child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readRestrictToList() throws UaException;

  /**
   * Writes the Value of the RestrictToList child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRestrictToList(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readRestrictToList()}. */
  CompletableFuture<? extends @Nullable Boolean> readRestrictToListAsync();

  /** Asynchronous form of {@link #writeRestrictToList}; completes with the operation status. */
  CompletableFuture<StatusCode> writeRestrictToListAsync(@Nullable Boolean value);

  /**
   * Resolves the optional SelectionDescriptions child, a PropertyType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getSelectionDescriptionsNode() throws UaException;

  /** Asynchronous form of {@link #getSelectionDescriptionsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getSelectionDescriptionsNodeAsync();

  /**
   * Reads the Value of the SelectionDescriptions child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  LocalizedText @Nullable [] readSelectionDescriptions() throws UaException;

  /**
   * Writes the Value of the SelectionDescriptions child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSelectionDescriptions(LocalizedText @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readSelectionDescriptions()}. */
  CompletableFuture<? extends LocalizedText @Nullable []> readSelectionDescriptionsAsync();

  /**
   * Asynchronous form of {@link #writeSelectionDescriptions}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeSelectionDescriptionsAsync(LocalizedText @Nullable [] value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Variant readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable Variant value) throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable Variant> readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable Variant value);
}
