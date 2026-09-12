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
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.7">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.7</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface BuildInfoType extends BaseDataVariableType {
  /** Gets the existing node's local value. */
  @Nullable String getProductUri() throws UaException;

  /** Sets the existing node's local value. */
  void setProductUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readProductUri() throws UaException;

  /** Writes the value remotely. */
  void writeProductUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readProductUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeProductUriAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getProductUriNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getProductUriNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getManufacturerName() throws UaException;

  /** Sets the existing node's local value. */
  void setManufacturerName(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readManufacturerName() throws UaException;

  /** Writes the value remotely. */
  void writeManufacturerName(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readManufacturerNameAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeManufacturerNameAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getManufacturerNameNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getManufacturerNameNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getProductName() throws UaException;

  /** Sets the existing node's local value. */
  void setProductName(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readProductName() throws UaException;

  /** Writes the value remotely. */
  void writeProductName(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readProductNameAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeProductNameAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getProductNameNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getProductNameNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getSoftwareVersion() throws UaException;

  /** Sets the existing node's local value. */
  void setSoftwareVersion(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readSoftwareVersion() throws UaException;

  /** Writes the value remotely. */
  void writeSoftwareVersion(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readSoftwareVersionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSoftwareVersionAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSoftwareVersionNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSoftwareVersionNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getBuildNumber() throws UaException;

  /** Sets the existing node's local value. */
  void setBuildNumber(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readBuildNumber() throws UaException;

  /** Writes the value remotely. */
  void writeBuildNumber(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readBuildNumberAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeBuildNumberAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getBuildNumberNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getBuildNumberNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DateTime getBuildDate() throws UaException;

  /** Sets the existing node's local value. */
  void setBuildDate(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readBuildDate() throws UaException;

  /** Writes the value remotely. */
  void writeBuildDate(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readBuildDateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeBuildDateAsync(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getBuildDateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getBuildDateNodeAsync();
}
