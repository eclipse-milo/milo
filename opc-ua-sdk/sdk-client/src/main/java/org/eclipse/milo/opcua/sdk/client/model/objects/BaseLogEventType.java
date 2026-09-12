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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part26/6.3">https://reference.opcfoundation.org/v105/Core/docs/Part26/6.3</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
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

  /** Gets the existing node's local value. */
  @Nullable NodeId getConditionClassId() throws UaException;

  /** Sets the existing node's local value. */
  void setConditionClassId(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId readConditionClassId() throws UaException;

  /** Writes the value remotely. */
  void writeConditionClassId(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId> readConditionClassIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeConditionClassIdAsync(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getConditionClassIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getConditionClassIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getConditionClassName() throws UaException;

  /** Sets the existing node's local value. */
  void setConditionClassName(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readConditionClassName() throws UaException;

  /** Writes the value remotely. */
  void writeConditionClassName(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readConditionClassNameAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeConditionClassNameAsync(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getConditionClassNameNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getConditionClassNameNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable StatusCode getErrorCode() throws UaException;

  /** Sets the existing node's local value. */
  void setErrorCode(@Nullable StatusCode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable StatusCode readErrorCode() throws UaException;

  /** Writes the value remotely. */
  void writeErrorCode(@Nullable StatusCode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable StatusCode> readErrorCodeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeErrorCodeAsync(@Nullable StatusCode value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getErrorCodePropertyNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getErrorCodePropertyNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable NodeId getErrorCodeNode() throws UaException;

  /** Sets the existing node's local value. */
  void setErrorCodeNode(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId readErrorCodeNode() throws UaException;

  /** Writes the value remotely. */
  void writeErrorCodeNode(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId> readErrorCodeNodeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeErrorCodeNodeAsync(@Nullable NodeId value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getErrorCodeNodeNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getErrorCodeNodeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable TraceContextDataType getTraceContext() throws UaException;

  /** Sets the existing node's local value. */
  void setTraceContext(@Nullable TraceContextDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable TraceContextDataType readTraceContext() throws UaException;

  /** Writes the value remotely. */
  void writeTraceContext(@Nullable TraceContextDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable TraceContextDataType> readTraceContextAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeTraceContextAsync(@Nullable TraceContextDataType value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getTraceContextNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getTraceContextNodeAsync();
}
