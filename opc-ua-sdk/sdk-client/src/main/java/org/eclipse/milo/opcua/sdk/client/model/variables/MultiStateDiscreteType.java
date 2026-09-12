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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.3/#5.3.3.3">https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.3/#5.3.3.3</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface MultiStateDiscreteType extends DiscreteItemType {
  QualifiedProperty<LocalizedText[]> ENUM_STRINGS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EnumStrings",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          1,
          LocalizedText[].class);

  /** Gets the existing node's local value. */
  @Nullable LocalizedText @Nullable [] getEnumStrings() throws UaException;

  /** Sets the existing node's local value. */
  void setEnumStrings(@Nullable LocalizedText @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText @Nullable [] readEnumStrings() throws UaException;

  /** Writes the value remotely. */
  void writeEnumStrings(@Nullable LocalizedText @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText @Nullable []> readEnumStringsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEnumStringsAsync(@Nullable LocalizedText @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getEnumStringsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getEnumStringsNodeAsync();
}
