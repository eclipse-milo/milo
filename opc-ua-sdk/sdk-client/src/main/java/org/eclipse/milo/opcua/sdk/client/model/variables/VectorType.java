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
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.21">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.21</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface VectorType extends BaseDataVariableType {
  QualifiedProperty<EUInformation> VECTOR_UNIT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "VectorUnit",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=887"),
          -1,
          EUInformation.class);

  /** Gets the existing node's local value. */
  @Nullable EUInformation getVectorUnit() throws UaException;

  /** Sets the existing node's local value. */
  void setVectorUnit(@Nullable EUInformation value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable EUInformation readVectorUnit() throws UaException;

  /** Writes the value remotely. */
  void writeVectorUnit(@Nullable EUInformation value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable EUInformation> readVectorUnitAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeVectorUnitAsync(@Nullable EUInformation value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getVectorUnitNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getVectorUnitNodeAsync();
}
