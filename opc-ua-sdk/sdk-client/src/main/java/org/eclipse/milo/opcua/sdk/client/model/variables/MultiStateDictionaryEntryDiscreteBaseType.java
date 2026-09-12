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
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part19/7.1">https://reference.opcfoundation.org/v105/Core/docs/Part19/7.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
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

  /** Gets the existing node's local value. */
  @Nullable Object getEnumDictionaryEntries() throws UaException;

  /** Sets the existing node's local value. */
  void setEnumDictionaryEntries(@Nullable Object value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Object readEnumDictionaryEntries() throws UaException;

  /** Writes the value remotely. */
  void writeEnumDictionaryEntries(@Nullable Object value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Object> readEnumDictionaryEntriesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEnumDictionaryEntriesAsync(@Nullable Object value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getEnumDictionaryEntriesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getEnumDictionaryEntriesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable NodeId @Nullable [] getValueAsDictionaryEntries() throws UaException;

  /** Sets the existing node's local value. */
  void setValueAsDictionaryEntries(@Nullable NodeId @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId @Nullable [] readValueAsDictionaryEntries() throws UaException;

  /** Writes the value remotely. */
  void writeValueAsDictionaryEntries(@Nullable NodeId @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId @Nullable []> readValueAsDictionaryEntriesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeValueAsDictionaryEntriesAsync(
      @Nullable NodeId @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getValueAsDictionaryEntriesNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getValueAsDictionaryEntriesNodeAsync();
}
