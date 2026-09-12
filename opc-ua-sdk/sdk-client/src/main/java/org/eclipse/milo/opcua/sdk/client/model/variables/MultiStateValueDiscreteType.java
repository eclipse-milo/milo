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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumValueType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.3/#5.3.3.4">https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.3/#5.3.3.4</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
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

  /** Gets the existing node's local value. */
  @Nullable EnumValueType @Nullable [] getEnumValues() throws UaException;

  /** Sets the existing node's local value. */
  void setEnumValues(@Nullable EnumValueType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable EnumValueType @Nullable [] readEnumValues() throws UaException;

  /** Writes the value remotely. */
  void writeEnumValues(@Nullable EnumValueType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable EnumValueType @Nullable []> readEnumValuesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEnumValuesAsync(@Nullable EnumValueType @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getEnumValuesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getEnumValuesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getValueAsTextProperty() throws UaException;

  /** Sets the existing node's local value. */
  void setValueAsTextProperty(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readValueAsTextProperty() throws UaException;

  /** Writes the value remotely. */
  void writeValueAsTextProperty(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readValueAsTextPropertyAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeValueAsTextPropertyAsync(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getValueAsTextNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getValueAsTextNodeAsync();
}
