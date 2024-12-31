/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part19/7.1">https://reference.opcfoundation.org/v105/Core/docs/Part19/7.1</a>
 */
public interface MultiStateDictionaryEntryDiscreteBaseType extends MultiStateValueDiscreteType {
  QualifiedProperty<Object> ENUM_DICTIONARY_ENTRIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EnumDictionaryEntries",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          2,
          Object.class);

  QualifiedProperty<NodeId[]> VALUE_AS_DICTIONARY_ENTRIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ValueAsDictionaryEntries",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          1,
          NodeId[].class);

  /**
   * Get the local value of the EnumDictionaryEntries Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the EnumDictionaryEntries Node.
   * @throws UaException if an error occurs creating or getting the EnumDictionaryEntries Node.
   */
  Object getEnumDictionaryEntries() throws UaException;

  /**
   * Set the local value of the EnumDictionaryEntries Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the EnumDictionaryEntries Node.
   * @throws UaException if an error occurs creating or getting the EnumDictionaryEntries Node.
   */
  void setEnumDictionaryEntries(Object value) throws UaException;

  /**
   * Read the value of the EnumDictionaryEntries Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link Object} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Object readEnumDictionaryEntries() throws UaException;

  /**
   * Write a new value for the EnumDictionaryEntries Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link Object} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeEnumDictionaryEntries(Object value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readEnumDictionaryEntries}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<?> readEnumDictionaryEntriesAsync();

  /**
   * An asynchronous implementation of {@link #writeEnumDictionaryEntries}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeEnumDictionaryEntriesAsync(Object value);

  /**
   * Get the EnumDictionaryEntries {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the EnumDictionaryEntries {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getEnumDictionaryEntriesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getEnumDictionaryEntriesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getEnumDictionaryEntriesNodeAsync();

  /**
   * Get the local value of the ValueAsDictionaryEntries Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ValueAsDictionaryEntries Node.
   * @throws UaException if an error occurs creating or getting the ValueAsDictionaryEntries Node.
   */
  NodeId[] getValueAsDictionaryEntries() throws UaException;

  /**
   * Set the local value of the ValueAsDictionaryEntries Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ValueAsDictionaryEntries Node.
   * @throws UaException if an error occurs creating or getting the ValueAsDictionaryEntries Node.
   */
  void setValueAsDictionaryEntries(NodeId[] value) throws UaException;

  /**
   * Read the value of the ValueAsDictionaryEntries Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link NodeId[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId[] readValueAsDictionaryEntries() throws UaException;

  /**
   * Write a new value for the ValueAsDictionaryEntries Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link NodeId[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeValueAsDictionaryEntries(NodeId[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readValueAsDictionaryEntries}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId[]> readValueAsDictionaryEntriesAsync();

  /**
   * An asynchronous implementation of {@link #writeValueAsDictionaryEntries}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeValueAsDictionaryEntriesAsync(NodeId[] value);

  /**
   * Get the ValueAsDictionaryEntries {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ValueAsDictionaryEntries {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getValueAsDictionaryEntriesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getValueAsDictionaryEntriesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getValueAsDictionaryEntriesNodeAsync();
}
