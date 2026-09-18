package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the TwoStateVariableType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">Model
 *     documentation</a>
 */
public interface TwoStateVariableType extends StateVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 8995L);

  QualifiedProperty<LocalizedText> FalseState_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "FalseState",
          ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
          -1,
          LocalizedText.class);

  QualifiedProperty<DateTime> TransitionTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "TransitionTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
          -1,
          DateTime.class);

  QualifiedProperty<DateTime> EffectiveTransitionTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EffectiveTransitionTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
          -1,
          DateTime.class);

  QualifiedProperty<Boolean> TwoStateVariableTypeId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA, "Id", ExpandedNodeId.of(Namespaces.OPC_UA, 1L), -1, Boolean.class);

  QualifiedProperty<LocalizedText> TrueState_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "TrueState",
          ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
          -1,
          LocalizedText.class);

  /**
   * Resolves the optional FalseState child, a PropertyType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getFalseStateNode() throws UaException;

  /** Asynchronous form of {@link #getFalseStateNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getFalseStateNodeAsync();

  /**
   * Reads the Value of the FalseState child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
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
   * Resolves the optional TransitionTime child, a PropertyType with DataType UtcTime.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getTransitionTimeNode() throws UaException;

  /** Asynchronous form of {@link #getTransitionTimeNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getTransitionTimeNodeAsync();

  /**
   * Reads the Value of the TransitionTime child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readTransitionTime() throws UaException;

  /**
   * Writes the Value of the TransitionTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTransitionTime(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readTransitionTime()}. */
  CompletableFuture<? extends @Nullable DateTime> readTransitionTimeAsync();

  /** Asynchronous form of {@link #writeTransitionTime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTransitionTimeAsync(@Nullable DateTime value);

  /**
   * Resolves the optional EffectiveTransitionTime child, a PropertyType with DataType UtcTime.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getEffectiveTransitionTimeNode() throws UaException;

  /** Asynchronous form of {@link #getEffectiveTransitionTimeNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getEffectiveTransitionTimeNodeAsync();

  /**
   * Reads the Value of the EffectiveTransitionTime child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readEffectiveTransitionTime() throws UaException;

  /**
   * Writes the Value of the EffectiveTransitionTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEffectiveTransitionTime(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readEffectiveTransitionTime()}. */
  CompletableFuture<? extends @Nullable DateTime> readEffectiveTransitionTimeAsync();

  /**
   * Asynchronous form of {@link #writeEffectiveTransitionTime}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeEffectiveTransitionTimeAsync(@Nullable DateTime value);

  /**
   * Resolves the mandatory Id child, a PropertyType with DataType Boolean.
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
  @Nullable Boolean readTwoStateVariableTypeId() throws UaException;

  /**
   * Writes the Value of the Id child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTwoStateVariableTypeId(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readTwoStateVariableTypeId()}. */
  CompletableFuture<? extends @Nullable Boolean> readTwoStateVariableTypeIdAsync();

  /**
   * Asynchronous form of {@link #writeTwoStateVariableTypeId}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeTwoStateVariableTypeIdAsync(@Nullable Boolean value);

  /**
   * Resolves the optional TrueState child, a PropertyType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getTrueStateNode() throws UaException;

  /** Asynchronous form of {@link #getTrueStateNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getTrueStateNodeAsync();

  /**
   * Reads the Value of the TrueState child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
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
}
