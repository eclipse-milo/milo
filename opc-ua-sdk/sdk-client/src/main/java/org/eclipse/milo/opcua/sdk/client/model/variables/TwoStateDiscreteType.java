package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the TwoStateDiscreteType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.3/#5.3.3.2">Model
 *     documentation</a>
 */
public interface TwoStateDiscreteType extends DiscreteItemType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2373L);

  QualifiedProperty<LocalizedText> FalseState_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "FalseState",
          ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
          -1,
          LocalizedText.class);

  QualifiedProperty<LocalizedText> TrueState_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "TrueState",
          ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
          -1,
          LocalizedText.class);

  /**
   * Resolves the mandatory FalseState child, a PropertyType with DataType LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getFalseStateNode() throws UaException;

  /** Asynchronous form of {@link #getFalseStateNode()}. */
  CompletableFuture<? extends PropertyType> getFalseStateNodeAsync();

  /**
   * Reads the Value of the FalseState child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readFalseState() throws UaException;

  /**
   * Writes the Value of the FalseState child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeFalseState(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readFalseState()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readFalseStateAsync();

  /** Asynchronous form of {@link #writeFalseState}; completes with the operation status. */
  CompletableFuture<StatusCode> writeFalseStateAsync(@Nullable LocalizedText value);

  /**
   * Resolves the mandatory TrueState child, a PropertyType with DataType LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getTrueStateNode() throws UaException;

  /** Asynchronous form of {@link #getTrueStateNode()}. */
  CompletableFuture<? extends PropertyType> getTrueStateNodeAsync();

  /**
   * Reads the Value of the TrueState child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readTrueState() throws UaException;

  /**
   * Writes the Value of the TrueState child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTrueState(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readTrueState()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readTrueStateAsync();

  /** Asynchronous form of {@link #writeTrueState}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTrueStateAsync(@Nullable LocalizedText value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Object readTwoStateDiscreteValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTwoStateDiscreteValue(@Nullable Object value) throws UaException;

  /** Asynchronous form of {@link #readTwoStateDiscreteValue()}. */
  CompletableFuture<? extends @Nullable Object> readTwoStateDiscreteValueAsync();

  /**
   * Asynchronous form of {@link #writeTwoStateDiscreteValue}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeTwoStateDiscreteValueAsync(@Nullable Object value);
}
