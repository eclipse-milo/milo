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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/8.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/8.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AlarmStateVariableType extends BaseDataVariableType {
  QualifiedProperty<UShort> HIGHEST_ACTIVE_SEVERITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "HighestActiveSeverity",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<UShort> HIGHEST_UNACK_SEVERITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "HighestUnackSeverity",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<UInteger> ACTIVE_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ActiveCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> UNACKNOWLEDGED_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UnacknowledgedCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> UNCONFIRMED_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UnconfirmedCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<ContentFilter> FILTER =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Filter",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=586"),
          -1,
          ContentFilter.class);

  /** Gets the existing node's local value. */
  @Nullable UShort getHighestActiveSeverity() throws UaException;

  /** Sets the existing node's local value. */
  void setHighestActiveSeverity(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readHighestActiveSeverity() throws UaException;

  /** Writes the value remotely. */
  void writeHighestActiveSeverity(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readHighestActiveSeverityAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeHighestActiveSeverityAsync(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getHighestActiveSeverityNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getHighestActiveSeverityNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UShort getHighestUnackSeverity() throws UaException;

  /** Sets the existing node's local value. */
  void setHighestUnackSeverity(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readHighestUnackSeverity() throws UaException;

  /** Writes the value remotely. */
  void writeHighestUnackSeverity(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readHighestUnackSeverityAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeHighestUnackSeverityAsync(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getHighestUnackSeverityNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getHighestUnackSeverityNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getActiveCount() throws UaException;

  /** Sets the existing node's local value. */
  void setActiveCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readActiveCount() throws UaException;

  /** Writes the value remotely. */
  void writeActiveCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readActiveCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeActiveCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getActiveCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getActiveCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getUnacknowledgedCount() throws UaException;

  /** Sets the existing node's local value. */
  void setUnacknowledgedCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readUnacknowledgedCount() throws UaException;

  /** Writes the value remotely. */
  void writeUnacknowledgedCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readUnacknowledgedCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeUnacknowledgedCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getUnacknowledgedCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getUnacknowledgedCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getUnconfirmedCount() throws UaException;

  /** Sets the existing node's local value. */
  void setUnconfirmedCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readUnconfirmedCount() throws UaException;

  /** Writes the value remotely. */
  void writeUnconfirmedCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readUnconfirmedCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeUnconfirmedCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getUnconfirmedCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getUnconfirmedCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ContentFilter getFilter() throws UaException;

  /** Sets the existing node's local value. */
  void setFilter(@Nullable ContentFilter value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ContentFilter readFilter() throws UaException;

  /** Writes the value remotely. */
  void writeFilter(@Nullable ContentFilter value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ContentFilter> readFilterAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeFilterAsync(@Nullable ContentFilter value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getFilterNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getFilterNodeAsync();
}
