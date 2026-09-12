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
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.7">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.7</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface NetworkAddressUrlType extends NetworkAddressType {
  /** Gets the existing node's local value. */
  @Nullable String getUrl() throws UaException;

  /** Sets the existing node's local value. */
  void setUrl(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readUrl() throws UaException;

  /** Writes the value remotely. */
  void writeUrl(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readUrlAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeUrlAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getUrlNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getUrlNodeAsync();
}
