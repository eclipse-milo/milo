package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AuditClientUpdateMethodResultEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.37">Model
 *     documentation</a>
 */
public interface AuditClientUpdateMethodResultEventType extends AuditClientEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 23926L);

  QualifiedProperty<StatusCode> StatusCodeId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "StatusCodeId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 19L),
          -1,
          StatusCode.class);

  QualifiedProperty<Variant[]> InputArguments_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "InputArguments",
          ExpandedNodeId.of(Namespaces.OPC_UA, 24L),
          1,
          Variant[].class);

  QualifiedProperty<Variant[]> OutputArguments_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "OutputArguments",
          ExpandedNodeId.of(Namespaces.OPC_UA, 24L),
          1,
          Variant[].class);

  QualifiedProperty<ExpandedNodeId> MethodId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MethodId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 18L),
          -1,
          ExpandedNodeId.class);

  QualifiedProperty<ExpandedNodeId> ObjectId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ObjectId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 18L),
          -1,
          ExpandedNodeId.class);

  /**
   * Resolves the mandatory StatusCodeId child, a PropertyType with DataType StatusCode.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getStatusCodeIdNode() throws UaException;

  /** Asynchronous form of {@link #getStatusCodeIdNode()}. */
  CompletableFuture<? extends PropertyType> getStatusCodeIdNodeAsync();

  /**
   * Reads the Value of the StatusCodeId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable StatusCode readStatusCodeId() throws UaException;

  /**
   * Writes the Value of the StatusCodeId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeStatusCodeId(@Nullable StatusCode value) throws UaException;

  /** Asynchronous form of {@link #readStatusCodeId()}. */
  CompletableFuture<? extends @Nullable StatusCode> readStatusCodeIdAsync();

  /** Asynchronous form of {@link #writeStatusCodeId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeStatusCodeIdAsync(@Nullable StatusCode value);

  /**
   * Resolves the mandatory InputArguments child, a PropertyType with DataType BaseDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getInputArgumentsNode() throws UaException;

  /** Asynchronous form of {@link #getInputArgumentsNode()}. */
  CompletableFuture<? extends PropertyType> getInputArgumentsNodeAsync();

  /**
   * Reads the Value of the InputArguments child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Variant @Nullable [] readInputArguments() throws UaException;

  /**
   * Writes the Value of the InputArguments child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeInputArguments(@Nullable Variant @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readInputArguments()}. */
  CompletableFuture<? extends @Nullable Variant @Nullable []> readInputArgumentsAsync();

  /** Asynchronous form of {@link #writeInputArguments}; completes with the operation status. */
  CompletableFuture<StatusCode> writeInputArgumentsAsync(@Nullable Variant @Nullable [] value);

  /**
   * Resolves the mandatory OutputArguments child, a PropertyType with DataType BaseDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getOutputArgumentsNode() throws UaException;

  /** Asynchronous form of {@link #getOutputArgumentsNode()}. */
  CompletableFuture<? extends PropertyType> getOutputArgumentsNodeAsync();

  /**
   * Reads the Value of the OutputArguments child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Variant @Nullable [] readOutputArguments() throws UaException;

  /**
   * Writes the Value of the OutputArguments child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeOutputArguments(@Nullable Variant @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readOutputArguments()}. */
  CompletableFuture<? extends @Nullable Variant @Nullable []> readOutputArgumentsAsync();

  /** Asynchronous form of {@link #writeOutputArguments}; completes with the operation status. */
  CompletableFuture<StatusCode> writeOutputArgumentsAsync(@Nullable Variant @Nullable [] value);

  /**
   * Resolves the mandatory MethodId child, a PropertyType with DataType ExpandedNodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getMethodIdNode() throws UaException;

  /** Asynchronous form of {@link #getMethodIdNode()}. */
  CompletableFuture<? extends PropertyType> getMethodIdNodeAsync();

  /**
   * Reads the Value of the MethodId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ExpandedNodeId readMethodId() throws UaException;

  /**
   * Writes the Value of the MethodId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMethodId(@Nullable ExpandedNodeId value) throws UaException;

  /** Asynchronous form of {@link #readMethodId()}. */
  CompletableFuture<? extends @Nullable ExpandedNodeId> readMethodIdAsync();

  /** Asynchronous form of {@link #writeMethodId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMethodIdAsync(@Nullable ExpandedNodeId value);

  /**
   * Resolves the mandatory ObjectId child, a PropertyType with DataType ExpandedNodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getObjectIdNode() throws UaException;

  /** Asynchronous form of {@link #getObjectIdNode()}. */
  CompletableFuture<? extends PropertyType> getObjectIdNodeAsync();

  /**
   * Reads the Value of the ObjectId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ExpandedNodeId readObjectId() throws UaException;

  /**
   * Writes the Value of the ObjectId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeObjectId(@Nullable ExpandedNodeId value) throws UaException;

  /** Asynchronous form of {@link #readObjectId()}. */
  CompletableFuture<? extends @Nullable ExpandedNodeId> readObjectIdAsync();

  /** Asynchronous form of {@link #writeObjectId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeObjectIdAsync(@Nullable ExpandedNodeId value);
}
