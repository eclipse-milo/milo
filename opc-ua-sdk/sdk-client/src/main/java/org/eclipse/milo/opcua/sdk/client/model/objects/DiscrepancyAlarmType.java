package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the DiscrepancyAlarmType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.25">Model
 *     documentation</a>
 */
public interface DiscrepancyAlarmType extends AlarmConditionType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 17080L);

  QualifiedProperty<Double> ExpectedTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ExpectedTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  QualifiedProperty<NodeId> TargetValueNode_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "TargetValueNode",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  QualifiedProperty<Double> Tolerance_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Tolerance",
          ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
          -1,
          Double.class);

  /**
   * Resolves the mandatory ExpectedTime child, a PropertyType with DataType Duration.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getExpectedTimeNode() throws UaException;

  /** Asynchronous form of {@link #getExpectedTimeNode()}. */
  CompletableFuture<? extends PropertyType> getExpectedTimeNodeAsync();

  /**
   * Reads the Value of the ExpectedTime child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readExpectedTime() throws UaException;

  /**
   * Writes the Value of the ExpectedTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeExpectedTime(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readExpectedTime()}. */
  CompletableFuture<? extends @Nullable Double> readExpectedTimeAsync();

  /** Asynchronous form of {@link #writeExpectedTime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeExpectedTimeAsync(@Nullable Double value);

  /**
   * Resolves the mandatory TargetValueNode child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getTargetValueNodeNode() throws UaException;

  /** Asynchronous form of {@link #getTargetValueNodeNode()}. */
  CompletableFuture<? extends PropertyType> getTargetValueNodeNodeAsync();

  /**
   * Reads the Value of the TargetValueNode child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readTargetValueNode() throws UaException;

  /**
   * Writes the Value of the TargetValueNode child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTargetValueNode(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readTargetValueNode()}. */
  CompletableFuture<? extends @Nullable NodeId> readTargetValueNodeAsync();

  /** Asynchronous form of {@link #writeTargetValueNode}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTargetValueNodeAsync(@Nullable NodeId value);

  /**
   * Resolves the optional Tolerance child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getToleranceNode() throws UaException;

  /** Asynchronous form of {@link #getToleranceNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getToleranceNodeAsync();

  /**
   * Reads the Value of the Tolerance child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readTolerance() throws UaException;

  /**
   * Writes the Value of the Tolerance child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTolerance(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readTolerance()}. */
  CompletableFuture<? extends @Nullable Double> readToleranceAsync();

  /** Asynchronous form of {@link #writeTolerance}; completes with the operation status. */
  CompletableFuture<StatusCode> writeToleranceAsync(@Nullable Double value);
}
