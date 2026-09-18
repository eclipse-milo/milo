package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.TraceContextDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the BaseLogEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part26/6.3">Model
 *     documentation</a>
 */
public interface BaseLogEventType extends BaseEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 19362L);

  QualifiedProperty<TraceContextDataType> TraceContext_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "TraceContext",
          ExpandedNodeId.of(Namespaces.OPC_UA, 19747L),
          -1,
          TraceContextDataType.class);

  QualifiedProperty<NodeId> ErrorCodeNode_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ErrorCodeNode",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  QualifiedProperty<StatusCode> ErrorCode__PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ErrorCode",
          ExpandedNodeId.of(Namespaces.OPC_UA, 19L),
          -1,
          StatusCode.class);

  /**
   * Resolves the optional TraceContext child, a PropertyType with DataType TraceContextDataType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getTraceContextNode() throws UaException;

  /** Asynchronous form of {@link #getTraceContextNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getTraceContextNodeAsync();

  /**
   * Reads the Value of the TraceContext child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable TraceContextDataType readTraceContext() throws UaException;

  /**
   * Writes the Value of the TraceContext child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTraceContext(@Nullable TraceContextDataType value) throws UaException;

  /** Asynchronous form of {@link #readTraceContext()}. */
  CompletableFuture<? extends @Nullable TraceContextDataType> readTraceContextAsync();

  /** Asynchronous form of {@link #writeTraceContext}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTraceContextAsync(@Nullable TraceContextDataType value);

  /**
   * Resolves the optional ErrorCodeNode child, a PropertyType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getErrorCodeNodeNode() throws UaException;

  /** Asynchronous form of {@link #getErrorCodeNodeNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getErrorCodeNodeNodeAsync();

  /**
   * Reads the Value of the ErrorCodeNode child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readErrorCodeNode() throws UaException;

  /**
   * Writes the Value of the ErrorCodeNode child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeErrorCodeNode(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readErrorCodeNode()}. */
  CompletableFuture<? extends @Nullable NodeId> readErrorCodeNodeAsync();

  /** Asynchronous form of {@link #writeErrorCodeNode}; completes with the operation status. */
  CompletableFuture<StatusCode> writeErrorCodeNodeAsync(@Nullable NodeId value);

  /**
   * Resolves the mandatory ConditionClassId child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getConditionClassIdNode() throws UaException;

  /** Asynchronous form of {@link #getConditionClassIdNode()}. */
  CompletableFuture<? extends PropertyType> getConditionClassIdNodeAsync();

  /**
   * Resolves the mandatory ConditionClassName child, a PropertyType with DataType LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getConditionClassNameNode() throws UaException;

  /** Asynchronous form of {@link #getConditionClassNameNode()}. */
  CompletableFuture<? extends PropertyType> getConditionClassNameNodeAsync();

  /**
   * Resolves the optional ErrorCode child, a PropertyType with DataType StatusCode.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getErrorCode_Node() throws UaException;

  /** Asynchronous form of {@link #getErrorCode_Node()}. */
  CompletableFuture<? extends @Nullable PropertyType> getErrorCode_NodeAsync();

  /**
   * Reads the Value of the ErrorCode child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable StatusCode readErrorCode_() throws UaException;

  /**
   * Writes the Value of the ErrorCode child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeErrorCode_(@Nullable StatusCode value) throws UaException;

  /** Asynchronous form of {@link #readErrorCode_()}. */
  CompletableFuture<? extends @Nullable StatusCode> readErrorCode_Async();

  /** Asynchronous form of {@link #writeErrorCode_}; completes with the operation status. */
  CompletableFuture<StatusCode> writeErrorCode_Async(@Nullable StatusCode value);
}
