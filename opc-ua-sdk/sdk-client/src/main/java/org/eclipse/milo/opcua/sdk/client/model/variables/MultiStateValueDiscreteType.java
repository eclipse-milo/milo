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
import org.eclipse.milo.opcua.stack.core.types.structured.EnumValueType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.3/#5.3.3.4">https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.3/#5.3.3.4</a>
 */
public interface MultiStateValueDiscreteType extends DiscreteItemType {
  QualifiedProperty<EnumValueType[]> ENUM_VALUES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EnumValues",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7594"),
          1,
          EnumValueType[].class);

  QualifiedProperty<LocalizedText> VALUE_AS_TEXT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ValueAsText",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          -1,
          LocalizedText.class);

  /**
   * Get the local value of the EnumValues Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the EnumValues Node.
   * @throws UaException if an error occurs creating or getting the EnumValues Node.
   */
  EnumValueType[] getEnumValues() throws UaException;

  /**
   * Set the local value of the EnumValues Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the EnumValues Node.
   * @throws UaException if an error occurs creating or getting the EnumValues Node.
   */
  void setEnumValues(EnumValueType[] value) throws UaException;

  /**
   * Read the value of the EnumValues Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link EnumValueType[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  EnumValueType[] readEnumValues() throws UaException;

  /**
   * Write a new value for the EnumValues Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link EnumValueType[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeEnumValues(EnumValueType[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readEnumValues}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends EnumValueType[]> readEnumValuesAsync();

  /**
   * An asynchronous implementation of {@link #writeEnumValues}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeEnumValuesAsync(EnumValueType[] value);

  /**
   * Get the EnumValues {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the EnumValues {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getEnumValuesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getEnumValuesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getEnumValuesNodeAsync();

  /**
   * Get the local value of the ValueAsText Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ValueAsText Node.
   * @throws UaException if an error occurs creating or getting the ValueAsText Node.
   */
  LocalizedText getValueAsText() throws UaException;

  /**
   * Set the local value of the ValueAsText Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ValueAsText Node.
   * @throws UaException if an error occurs creating or getting the ValueAsText Node.
   */
  void setValueAsText(LocalizedText value) throws UaException;

  /**
   * Read the value of the ValueAsText Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readValueAsText() throws UaException;

  /**
   * Write a new value for the ValueAsText Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeValueAsText(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readValueAsText}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readValueAsTextAsync();

  /**
   * An asynchronous implementation of {@link #writeValueAsText}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeValueAsTextAsync(LocalizedText value);

  /**
   * Get the ValueAsText {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ValueAsText {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getValueAsTextNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getValueAsTextNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getValueAsTextNodeAsync();
}
