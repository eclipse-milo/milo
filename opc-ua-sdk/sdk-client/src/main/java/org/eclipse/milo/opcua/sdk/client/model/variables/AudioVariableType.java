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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.19">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.19</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AudioVariableType extends BaseDataVariableType {
  QualifiedProperty<String> LIST_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ListId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> AGENCY_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AgencyId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> VERSION_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "VersionId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /** Gets the existing node's local value. */
  @Nullable String getListId() throws UaException;

  /** Sets the existing node's local value. */
  void setListId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readListId() throws UaException;

  /** Writes the value remotely. */
  void writeListId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readListIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeListIdAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getListIdNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getListIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getAgencyId() throws UaException;

  /** Sets the existing node's local value. */
  void setAgencyId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readAgencyId() throws UaException;

  /** Writes the value remotely. */
  void writeAgencyId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readAgencyIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAgencyIdAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getAgencyIdNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getAgencyIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getVersionId() throws UaException;

  /** Sets the existing node's local value. */
  void setVersionId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readVersionId() throws UaException;

  /** Writes the value remotely. */
  void writeVersionId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readVersionIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeVersionIdAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getVersionIdNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getVersionIdNodeAsync();
}
