package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.TwoStateVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the NonExclusiveLimitAlarmType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.20">Model
 *     documentation</a>
 */
public interface NonExclusiveLimitAlarmType extends LimitAlarmType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 9906L);

  /**
   * Resolves the mandatory ActiveState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  TwoStateVariableType getActiveStateNode() throws UaException;

  /** Asynchronous form of {@link #getActiveStateNode()}. */
  CompletableFuture<? extends TwoStateVariableType> getActiveStateNodeAsync();

  /**
   * Resolves the optional LowLowState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  @Nullable TwoStateVariableType getLowLowStateNode() throws UaException;

  /** Asynchronous form of {@link #getLowLowStateNode()}. */
  CompletableFuture<? extends @Nullable TwoStateVariableType> getLowLowStateNodeAsync();

  /**
   * Reads the Value of the LowLowState child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readLowLowState() throws UaException;

  /**
   * Writes the Value of the LowLowState child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLowLowState(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readLowLowState()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readLowLowStateAsync();

  /** Asynchronous form of {@link #writeLowLowState}; completes with the operation status. */
  CompletableFuture<StatusCode> writeLowLowStateAsync(@Nullable LocalizedText value);

  /**
   * Resolves the optional HighHighState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  @Nullable TwoStateVariableType getHighHighStateNode() throws UaException;

  /** Asynchronous form of {@link #getHighHighStateNode()}. */
  CompletableFuture<? extends @Nullable TwoStateVariableType> getHighHighStateNodeAsync();

  /**
   * Reads the Value of the HighHighState child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readHighHighState() throws UaException;

  /**
   * Writes the Value of the HighHighState child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeHighHighState(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readHighHighState()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readHighHighStateAsync();

  /** Asynchronous form of {@link #writeHighHighState}; completes with the operation status. */
  CompletableFuture<StatusCode> writeHighHighStateAsync(@Nullable LocalizedText value);

  /**
   * Resolves the optional LowState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  @Nullable TwoStateVariableType getLowStateNode() throws UaException;

  /** Asynchronous form of {@link #getLowStateNode()}. */
  CompletableFuture<? extends @Nullable TwoStateVariableType> getLowStateNodeAsync();

  /**
   * Reads the Value of the LowState child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readLowState() throws UaException;

  /**
   * Writes the Value of the LowState child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLowState(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readLowState()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readLowStateAsync();

  /** Asynchronous form of {@link #writeLowState}; completes with the operation status. */
  CompletableFuture<StatusCode> writeLowStateAsync(@Nullable LocalizedText value);

  /**
   * Resolves the optional HighState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  @Nullable TwoStateVariableType getHighStateNode() throws UaException;

  /** Asynchronous form of {@link #getHighStateNode()}. */
  CompletableFuture<? extends @Nullable TwoStateVariableType> getHighStateNodeAsync();

  /**
   * Reads the Value of the HighState child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readHighState() throws UaException;

  /**
   * Writes the Value of the HighState child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeHighState(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readHighState()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readHighStateAsync();

  /** Asynchronous form of {@link #writeHighState}; completes with the operation status. */
  CompletableFuture<StatusCode> writeHighStateAsync(@Nullable LocalizedText value);
}
