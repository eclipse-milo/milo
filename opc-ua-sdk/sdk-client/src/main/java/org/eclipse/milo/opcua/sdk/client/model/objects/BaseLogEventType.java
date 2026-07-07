/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.TraceContextDataType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part26/6.3">https://reference.opcfoundation.org/v105/Core/docs/Part26/6.3</a>
 */
public interface BaseLogEventType extends BaseEventType {
  QualifiedProperty<NodeId> CONDITION_CLASS_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConditionClassId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<LocalizedText> CONDITION_CLASS_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConditionClassName",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          -1,
          LocalizedText.class);

  QualifiedProperty<StatusCode> ERROR_CODE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ErrorCode",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19"),
          -1,
          StatusCode.class);

  QualifiedProperty<NodeId> ERROR_CODE_NODE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ErrorCodeNode",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<TraceContextDataType> TRACE_CONTEXT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "TraceContext",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19747"),
          -1,
          TraceContextDataType.class);

  /**
   * Get the local value of the ConditionClassId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ConditionClassId Node.
   * @throws UaException if an error occurs creating or getting the ConditionClassId Node.
   */
  NodeId getConditionClassId() throws UaException;

  /**
   * Set the local value of the ConditionClassId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ConditionClassId Node.
   * @throws UaException if an error occurs creating or getting the ConditionClassId Node.
   */
  void setConditionClassId(NodeId value) throws UaException;

  /**
   * Read the value of the ConditionClassId Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link NodeId} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId readConditionClassId() throws UaException;

  /**
   * Write a new value for the ConditionClassId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link NodeId} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeConditionClassId(NodeId value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readConditionClassId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId> readConditionClassIdAsync();

  /**
   * An asynchronous implementation of {@link #writeConditionClassId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeConditionClassIdAsync(NodeId value);

  /**
   * Get the ConditionClassId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ConditionClassId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getConditionClassIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getConditionClassIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getConditionClassIdNodeAsync();

  /**
   * Get the local value of the ConditionClassName Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ConditionClassName Node.
   * @throws UaException if an error occurs creating or getting the ConditionClassName Node.
   */
  LocalizedText getConditionClassName() throws UaException;

  /**
   * Set the local value of the ConditionClassName Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ConditionClassName Node.
   * @throws UaException if an error occurs creating or getting the ConditionClassName Node.
   */
  void setConditionClassName(LocalizedText value) throws UaException;

  /**
   * Read the value of the ConditionClassName Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readConditionClassName() throws UaException;

  /**
   * Write a new value for the ConditionClassName Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeConditionClassName(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readConditionClassName}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readConditionClassNameAsync();

  /**
   * An asynchronous implementation of {@link #writeConditionClassName}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeConditionClassNameAsync(LocalizedText value);

  /**
   * Get the ConditionClassName {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ConditionClassName {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getConditionClassNameNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getConditionClassNameNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getConditionClassNameNodeAsync();

  /**
   * Get the local value of the ErrorCode Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ErrorCode Node.
   * @throws UaException if an error occurs creating or getting the ErrorCode Node.
   */
  StatusCode getErrorCode() throws UaException;

  /**
   * Set the local value of the ErrorCode Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ErrorCode Node.
   * @throws UaException if an error occurs creating or getting the ErrorCode Node.
   */
  void setErrorCode(StatusCode value) throws UaException;

  /**
   * Read the value of the ErrorCode Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link StatusCode} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  StatusCode readErrorCode() throws UaException;

  /**
   * Write a new value for the ErrorCode Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link StatusCode} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeErrorCode(StatusCode value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readErrorCode}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends StatusCode> readErrorCodeAsync();

  /**
   * An asynchronous implementation of {@link #writeErrorCode}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeErrorCodeAsync(StatusCode value);

  /**
   * Get the ErrorCode {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ErrorCode {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getErrorCodePropertyNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getErrorCodePropertyNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getErrorCodePropertyNodeAsync();

  /**
   * Get the local value of the ErrorCodeNode Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ErrorCodeNode Node.
   * @throws UaException if an error occurs creating or getting the ErrorCodeNode Node.
   */
  NodeId getErrorCodeNode() throws UaException;

  /**
   * Set the local value of the ErrorCodeNode Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ErrorCodeNode Node.
   * @throws UaException if an error occurs creating or getting the ErrorCodeNode Node.
   */
  void setErrorCodeNode(NodeId value) throws UaException;

  /**
   * Read the value of the ErrorCodeNode Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link NodeId} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId readErrorCodeNode() throws UaException;

  /**
   * Write a new value for the ErrorCodeNode Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link NodeId} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeErrorCodeNode(NodeId value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readErrorCodeNode}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId> readErrorCodeNodeAsync();

  /**
   * An asynchronous implementation of {@link #writeErrorCodeNode}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeErrorCodeNodeAsync(NodeId value);

  /**
   * Get the ErrorCodeNode {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ErrorCodeNode {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getErrorCodeNodeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getErrorCodeNodeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getErrorCodeNodeNodeAsync();

  /**
   * Get the local value of the TraceContext Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the TraceContext Node.
   * @throws UaException if an error occurs creating or getting the TraceContext Node.
   */
  TraceContextDataType getTraceContext() throws UaException;

  /**
   * Set the local value of the TraceContext Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the TraceContext Node.
   * @throws UaException if an error occurs creating or getting the TraceContext Node.
   */
  void setTraceContext(TraceContextDataType value) throws UaException;

  /**
   * Read the value of the TraceContext Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link TraceContextDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  TraceContextDataType readTraceContext() throws UaException;

  /**
   * Write a new value for the TraceContext Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link TraceContextDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeTraceContext(TraceContextDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readTraceContext}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends TraceContextDataType> readTraceContextAsync();

  /**
   * An asynchronous implementation of {@link #writeTraceContext}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeTraceContextAsync(TraceContextDataType value);

  /**
   * Get the TraceContext {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TraceContext {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getTraceContextNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTraceContextNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getTraceContextNodeAsync();
}
