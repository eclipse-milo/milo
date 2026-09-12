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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.25">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.25</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AuditWriteUpdateEventType extends AuditUpdateEventType {
  QualifiedProperty<UInteger> ATTRIBUTE_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AttributeId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<String> INDEX_RANGE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "IndexRange",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=291"),
          -1,
          String.class);

  QualifiedProperty<Object> OLD_VALUE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OldValue",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24"),
          -1,
          Object.class);

  QualifiedProperty<Object> NEW_VALUE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NewValue",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24"),
          -1,
          Object.class);

  /** Gets the existing node's local value. */
  @Nullable UInteger getAttributeId() throws UaException;

  /** Sets the existing node's local value. */
  void setAttributeId(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readAttributeId() throws UaException;

  /** Writes the value remotely. */
  void writeAttributeId(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readAttributeIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAttributeIdAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getAttributeIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getAttributeIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getIndexRange() throws UaException;

  /** Sets the existing node's local value. */
  void setIndexRange(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readIndexRange() throws UaException;

  /** Writes the value remotely. */
  void writeIndexRange(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readIndexRangeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeIndexRangeAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getIndexRangeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getIndexRangeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Object getOldValue() throws UaException;

  /** Sets the existing node's local value. */
  void setOldValue(@Nullable Object value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Object readOldValue() throws UaException;

  /** Writes the value remotely. */
  void writeOldValue(@Nullable Object value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Object> readOldValueAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeOldValueAsync(@Nullable Object value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getOldValueNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getOldValueNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Object getNewValue() throws UaException;

  /** Sets the existing node's local value. */
  void setNewValue(@Nullable Object value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Object readNewValue() throws UaException;

  /** Writes the value remotely. */
  void writeNewValue(@Nullable Object value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Object> readNewValueAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeNewValueAsync(@Nullable Object value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getNewValueNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getNewValueNodeAsync();
}
