/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface DataTypeDictionaryType extends BaseDataVariableType {
  QualifiedProperty<String> DATA_TYPE_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataTypeVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> NAMESPACE_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NamespaceUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<Boolean> DEPRECATED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Deprecated",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  /** Gets the existing node's local value. */
  @Nullable String getDataTypeVersionProperty() throws UaException;

  /** Sets the existing node's local value. */
  void setDataTypeVersionProperty(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readDataTypeVersionProperty() throws UaException;

  /** Writes the value remotely. */
  void writeDataTypeVersionProperty(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readDataTypeVersionPropertyAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDataTypeVersionPropertyAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDataTypeVersionNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getDataTypeVersionNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getNamespaceUri() throws UaException;

  /** Sets the existing node's local value. */
  void setNamespaceUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readNamespaceUri() throws UaException;

  /** Writes the value remotely. */
  void writeNamespaceUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readNamespaceUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeNamespaceUriAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getNamespaceUriNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getNamespaceUriNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getDeprecated() throws UaException;

  /** Sets the existing node's local value. */
  void setDeprecated(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readDeprecated() throws UaException;

  /** Writes the value remotely. */
  void writeDeprecated(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readDeprecatedAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDeprecatedAsync(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDeprecatedNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getDeprecatedNodeAsync();
}
