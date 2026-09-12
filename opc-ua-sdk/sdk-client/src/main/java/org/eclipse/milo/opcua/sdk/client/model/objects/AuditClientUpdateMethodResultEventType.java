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
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.37">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.37</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AuditClientUpdateMethodResultEventType extends AuditClientEventType {
  QualifiedProperty<ExpandedNodeId> OBJECT_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ObjectId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=18"),
          -1,
          ExpandedNodeId.class);

  QualifiedProperty<ExpandedNodeId> METHOD_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MethodId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=18"),
          -1,
          ExpandedNodeId.class);

  QualifiedProperty<StatusCode> STATUS_CODE_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "StatusCodeId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19"),
          -1,
          StatusCode.class);

  QualifiedProperty<Object[]> INPUT_ARGUMENTS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InputArguments",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24"),
          1,
          Object[].class);

  QualifiedProperty<Object[]> OUTPUT_ARGUMENTS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OutputArguments",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24"),
          1,
          Object[].class);

  /** Gets the existing node's local value. */
  @Nullable ExpandedNodeId getObjectId() throws UaException;

  /** Sets the existing node's local value. */
  void setObjectId(@Nullable ExpandedNodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ExpandedNodeId readObjectId() throws UaException;

  /** Writes the value remotely. */
  void writeObjectId(@Nullable ExpandedNodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ExpandedNodeId> readObjectIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeObjectIdAsync(@Nullable ExpandedNodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getObjectIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getObjectIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ExpandedNodeId getMethodId() throws UaException;

  /** Sets the existing node's local value. */
  void setMethodId(@Nullable ExpandedNodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ExpandedNodeId readMethodId() throws UaException;

  /** Writes the value remotely. */
  void writeMethodId(@Nullable ExpandedNodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ExpandedNodeId> readMethodIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMethodIdAsync(@Nullable ExpandedNodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMethodIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getMethodIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable StatusCode getStatusCodeId() throws UaException;

  /** Sets the existing node's local value. */
  void setStatusCodeId(@Nullable StatusCode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable StatusCode readStatusCodeId() throws UaException;

  /** Writes the value remotely. */
  void writeStatusCodeId(@Nullable StatusCode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable StatusCode> readStatusCodeIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeStatusCodeIdAsync(@Nullable StatusCode value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getStatusCodeIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getStatusCodeIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Object @Nullable [] getInputArguments() throws UaException;

  /** Sets the existing node's local value. */
  void setInputArguments(@Nullable Object @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Object @Nullable [] readInputArguments() throws UaException;

  /** Writes the value remotely. */
  void writeInputArguments(@Nullable Object @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Object @Nullable []> readInputArgumentsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeInputArgumentsAsync(@Nullable Object @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getInputArgumentsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getInputArgumentsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Object @Nullable [] getOutputArguments() throws UaException;

  /** Sets the existing node's local value. */
  void setOutputArguments(@Nullable Object @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Object @Nullable [] readOutputArguments() throws UaException;

  /** Writes the value remotely. */
  void writeOutputArguments(@Nullable Object @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Object @Nullable []> readOutputArgumentsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeOutputArgumentsAsync(@Nullable Object @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getOutputArgumentsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getOutputArgumentsNodeAsync();
}
