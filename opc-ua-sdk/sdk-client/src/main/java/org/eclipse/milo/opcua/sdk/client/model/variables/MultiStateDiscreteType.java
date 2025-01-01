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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.3/#5.3.3.3">https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.3/#5.3.3.3</a>
 */
public interface MultiStateDiscreteType extends DiscreteItemType {
  QualifiedProperty<LocalizedText[]> ENUM_STRINGS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EnumStrings",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          1,
          LocalizedText[].class);

  /**
   * Get the local value of the EnumStrings Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the EnumStrings Node.
   * @throws UaException if an error occurs creating or getting the EnumStrings Node.
   */
  LocalizedText[] getEnumStrings() throws UaException;

  /**
   * Set the local value of the EnumStrings Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the EnumStrings Node.
   * @throws UaException if an error occurs creating or getting the EnumStrings Node.
   */
  void setEnumStrings(LocalizedText[] value) throws UaException;

  /**
   * Read the value of the EnumStrings Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText[] readEnumStrings() throws UaException;

  /**
   * Write a new value for the EnumStrings Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LocalizedText[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeEnumStrings(LocalizedText[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readEnumStrings}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText[]> readEnumStringsAsync();

  /**
   * An asynchronous implementation of {@link #writeEnumStrings}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeEnumStringsAsync(LocalizedText[] value);

  /**
   * Get the EnumStrings {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the EnumStrings {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getEnumStringsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getEnumStringsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getEnumStringsNodeAsync();
}
