package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the StateVariableType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.3">Model
 *     documentation</a>
 */
public interface StateVariableType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2755L);

  QualifiedProperty<LocalizedText> EffectiveDisplayName_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EffectiveDisplayName",
          ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
          -1,
          LocalizedText.class);

  QualifiedProperty<Variant> Id_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA, "Id", ExpandedNodeId.of(Namespaces.OPC_UA, 24L), -1, Variant.class);

  QualifiedProperty<QualifiedName> Name_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Name",
          ExpandedNodeId.of(Namespaces.OPC_UA, 20L),
          -1,
          QualifiedName.class);

  QualifiedProperty<UInteger> Number_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Number",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  /**
   * Resolves the optional EffectiveDisplayName child, a PropertyType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getEffectiveDisplayNameNode() throws UaException;

  /** Asynchronous form of {@link #getEffectiveDisplayNameNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getEffectiveDisplayNameNodeAsync();

  /**
   * Reads the Value of the EffectiveDisplayName child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readEffectiveDisplayName() throws UaException;

  /**
   * Writes the Value of the EffectiveDisplayName child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEffectiveDisplayName(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readEffectiveDisplayName()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readEffectiveDisplayNameAsync();

  /**
   * Asynchronous form of {@link #writeEffectiveDisplayName}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeEffectiveDisplayNameAsync(@Nullable LocalizedText value);

  /**
   * Resolves the mandatory Id child, a PropertyType with DataType BaseDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getIdNode() throws UaException;

  /** Asynchronous form of {@link #getIdNode()}. */
  CompletableFuture<? extends PropertyType> getIdNodeAsync();

  /**
   * Reads the Value of the Id child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Variant readId() throws UaException;

  /**
   * Writes the Value of the Id child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeId(@Nullable Variant value) throws UaException;

  /** Asynchronous form of {@link #readId()}. */
  CompletableFuture<? extends @Nullable Variant> readIdAsync();

  /** Asynchronous form of {@link #writeId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeIdAsync(@Nullable Variant value);

  /**
   * Resolves the optional Name child, a PropertyType with DataType QualifiedName.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getNameNode() throws UaException;

  /** Asynchronous form of {@link #getNameNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getNameNodeAsync();

  /**
   * Reads the Value of the Name child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable QualifiedName readName() throws UaException;

  /**
   * Writes the Value of the Name child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeName(@Nullable QualifiedName value) throws UaException;

  /** Asynchronous form of {@link #readName()}. */
  CompletableFuture<? extends @Nullable QualifiedName> readNameAsync();

  /** Asynchronous form of {@link #writeName}; completes with the operation status. */
  CompletableFuture<StatusCode> writeNameAsync(@Nullable QualifiedName value);

  /**
   * Resolves the optional Number child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getNumberNode() throws UaException;

  /** Asynchronous form of {@link #getNumberNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getNumberNodeAsync();

  /**
   * Reads the Value of the Number child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readNumber() throws UaException;

  /**
   * Writes the Value of the Number child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeNumber(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readNumber()}. */
  CompletableFuture<? extends @Nullable UInteger> readNumberAsync();

  /** Asynchronous form of {@link #writeNumber}; completes with the operation status. */
  CompletableFuture<StatusCode> writeNumberAsync(@Nullable UInteger value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable LocalizedText value);
}
