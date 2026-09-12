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
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.11">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.11</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface OperationLimitsType extends FolderType {
  QualifiedProperty<UInteger> MAX_NODES_PER_READ =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxNodesPerRead",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_NODES_PER_HISTORY_READ_DATA =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxNodesPerHistoryReadData",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_NODES_PER_HISTORY_READ_EVENTS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxNodesPerHistoryReadEvents",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_NODES_PER_WRITE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxNodesPerWrite",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_NODES_PER_HISTORY_UPDATE_DATA =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxNodesPerHistoryUpdateData",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_NODES_PER_HISTORY_UPDATE_EVENTS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxNodesPerHistoryUpdateEvents",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_NODES_PER_METHOD_CALL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxNodesPerMethodCall",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_NODES_PER_BROWSE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxNodesPerBrowse",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_NODES_PER_REGISTER_NODES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxNodesPerRegisterNodes",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_NODES_PER_TRANSLATE_BROWSE_PATHS_TO_NODE_IDS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxNodesPerTranslateBrowsePathsToNodeIds",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_NODES_PER_NODE_MANAGEMENT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxNodesPerNodeManagement",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_MONITORED_ITEMS_PER_CALL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxMonitoredItemsPerCall",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNodesPerRead() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxNodesPerRead(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxNodesPerRead() throws UaException;

  /** Writes the value remotely. */
  void writeMaxNodesPerRead(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerReadAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxNodesPerReadAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxNodesPerReadNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxNodesPerReadNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNodesPerHistoryReadData() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxNodesPerHistoryReadData(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxNodesPerHistoryReadData() throws UaException;

  /** Writes the value remotely. */
  void writeMaxNodesPerHistoryReadData(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerHistoryReadDataAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxNodesPerHistoryReadDataAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxNodesPerHistoryReadDataNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxNodesPerHistoryReadDataNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNodesPerHistoryReadEvents() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxNodesPerHistoryReadEvents(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxNodesPerHistoryReadEvents() throws UaException;

  /** Writes the value remotely. */
  void writeMaxNodesPerHistoryReadEvents(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerHistoryReadEventsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxNodesPerHistoryReadEventsAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxNodesPerHistoryReadEventsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxNodesPerHistoryReadEventsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNodesPerWrite() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxNodesPerWrite(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxNodesPerWrite() throws UaException;

  /** Writes the value remotely. */
  void writeMaxNodesPerWrite(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerWriteAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxNodesPerWriteAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxNodesPerWriteNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxNodesPerWriteNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNodesPerHistoryUpdateData() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxNodesPerHistoryUpdateData(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxNodesPerHistoryUpdateData() throws UaException;

  /** Writes the value remotely. */
  void writeMaxNodesPerHistoryUpdateData(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerHistoryUpdateDataAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxNodesPerHistoryUpdateDataAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxNodesPerHistoryUpdateDataNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxNodesPerHistoryUpdateDataNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNodesPerHistoryUpdateEvents() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxNodesPerHistoryUpdateEvents(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxNodesPerHistoryUpdateEvents() throws UaException;

  /** Writes the value remotely. */
  void writeMaxNodesPerHistoryUpdateEvents(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerHistoryUpdateEventsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxNodesPerHistoryUpdateEventsAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxNodesPerHistoryUpdateEventsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxNodesPerHistoryUpdateEventsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNodesPerMethodCall() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxNodesPerMethodCall(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxNodesPerMethodCall() throws UaException;

  /** Writes the value remotely. */
  void writeMaxNodesPerMethodCall(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerMethodCallAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxNodesPerMethodCallAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxNodesPerMethodCallNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxNodesPerMethodCallNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNodesPerBrowse() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxNodesPerBrowse(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxNodesPerBrowse() throws UaException;

  /** Writes the value remotely. */
  void writeMaxNodesPerBrowse(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerBrowseAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxNodesPerBrowseAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxNodesPerBrowseNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxNodesPerBrowseNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNodesPerRegisterNodes() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxNodesPerRegisterNodes(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxNodesPerRegisterNodes() throws UaException;

  /** Writes the value remotely. */
  void writeMaxNodesPerRegisterNodes(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerRegisterNodesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxNodesPerRegisterNodesAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxNodesPerRegisterNodesNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxNodesPerRegisterNodesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNodesPerTranslateBrowsePathsToNodeIds() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxNodesPerTranslateBrowsePathsToNodeIds(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxNodesPerTranslateBrowsePathsToNodeIds() throws UaException;

  /** Writes the value remotely. */
  void writeMaxNodesPerTranslateBrowsePathsToNodeIds(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger>
      readMaxNodesPerTranslateBrowsePathsToNodeIdsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxNodesPerTranslateBrowsePathsToNodeIdsAsync(
      @Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxNodesPerTranslateBrowsePathsToNodeIdsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType>
      getMaxNodesPerTranslateBrowsePathsToNodeIdsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNodesPerNodeManagement() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxNodesPerNodeManagement(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxNodesPerNodeManagement() throws UaException;

  /** Writes the value remotely. */
  void writeMaxNodesPerNodeManagement(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerNodeManagementAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxNodesPerNodeManagementAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxNodesPerNodeManagementNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxNodesPerNodeManagementNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxMonitoredItemsPerCall() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxMonitoredItemsPerCall(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxMonitoredItemsPerCall() throws UaException;

  /** Writes the value remotely. */
  void writeMaxMonitoredItemsPerCall(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxMonitoredItemsPerCallAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxMonitoredItemsPerCallAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxMonitoredItemsPerCallNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxMonitoredItemsPerCallNodeAsync();
}
