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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.15">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.15</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface IPriorityMappingEntryType extends BaseInterfaceType {
  /** Gets the existing node's local value. */
  @Nullable String getMappingUri() throws UaException;

  /** Sets the existing node's local value. */
  void setMappingUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readMappingUri() throws UaException;

  /** Writes the value remotely. */
  void writeMappingUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readMappingUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMappingUriAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMappingUriNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMappingUriNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getPriorityLabel() throws UaException;

  /** Sets the existing node's local value. */
  void setPriorityLabel(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readPriorityLabel() throws UaException;

  /** Writes the value remotely. */
  void writePriorityLabel(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readPriorityLabelAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePriorityLabelAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getPriorityLabelNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getPriorityLabelNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UByte getPriorityValuePcp() throws UaException;

  /** Sets the existing node's local value. */
  void setPriorityValuePcp(@Nullable UByte value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UByte readPriorityValuePcp() throws UaException;

  /** Writes the value remotely. */
  void writePriorityValuePcp(@Nullable UByte value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UByte> readPriorityValuePcpAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePriorityValuePcpAsync(@Nullable UByte value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getPriorityValuePcpNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable BaseDataVariableType> getPriorityValuePcpNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getPriorityValueDscp() throws UaException;

  /** Sets the existing node's local value. */
  void setPriorityValueDscp(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readPriorityValueDscp() throws UaException;

  /** Writes the value remotely. */
  void writePriorityValueDscp(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readPriorityValueDscpAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePriorityValueDscpAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getPriorityValueDscpNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable BaseDataVariableType> getPriorityValueDscpNodeAsync();
}
