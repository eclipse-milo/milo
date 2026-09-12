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
import org.eclipse.milo.opcua.stack.core.types.structured.BitFieldDefinition;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.29">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.29</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface BitFieldType extends BaseDataVariableType {
  QualifiedProperty<BitFieldDefinition[]> BIT_FIELDS_DEFINITIONS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "BitFieldsDefinitions",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=32421"),
          1,
          BitFieldDefinition[].class);

  /** Gets the existing node's local value. */
  @Nullable BitFieldDefinition @Nullable [] getBitFieldsDefinitions() throws UaException;

  /** Sets the existing node's local value. */
  void setBitFieldsDefinitions(@Nullable BitFieldDefinition @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable BitFieldDefinition @Nullable [] readBitFieldsDefinitions() throws UaException;

  /** Writes the value remotely. */
  void writeBitFieldsDefinitions(@Nullable BitFieldDefinition @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable BitFieldDefinition @Nullable []>
      readBitFieldsDefinitionsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeBitFieldsDefinitionsAsync(
      @Nullable BitFieldDefinition @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getBitFieldsDefinitionsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getBitFieldsDefinitionsNodeAsync();
}
