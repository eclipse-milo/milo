/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
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
  @Nullable UInteger getMaxNodesPerRead();

  /** Sets the existing node's local value. */
  void setMaxNodesPerRead(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxNodesPerReadNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNodesPerHistoryReadData();

  /** Sets the existing node's local value. */
  void setMaxNodesPerHistoryReadData(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxNodesPerHistoryReadDataNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNodesPerHistoryReadEvents();

  /** Sets the existing node's local value. */
  void setMaxNodesPerHistoryReadEvents(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxNodesPerHistoryReadEventsNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNodesPerWrite();

  /** Sets the existing node's local value. */
  void setMaxNodesPerWrite(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxNodesPerWriteNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNodesPerHistoryUpdateData();

  /** Sets the existing node's local value. */
  void setMaxNodesPerHistoryUpdateData(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxNodesPerHistoryUpdateDataNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNodesPerHistoryUpdateEvents();

  /** Sets the existing node's local value. */
  void setMaxNodesPerHistoryUpdateEvents(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxNodesPerHistoryUpdateEventsNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNodesPerMethodCall();

  /** Sets the existing node's local value. */
  void setMaxNodesPerMethodCall(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxNodesPerMethodCallNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNodesPerBrowse();

  /** Sets the existing node's local value. */
  void setMaxNodesPerBrowse(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxNodesPerBrowseNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNodesPerRegisterNodes();

  /** Sets the existing node's local value. */
  void setMaxNodesPerRegisterNodes(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxNodesPerRegisterNodesNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNodesPerTranslateBrowsePathsToNodeIds();

  /** Sets the existing node's local value. */
  void setMaxNodesPerTranslateBrowsePathsToNodeIds(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxNodesPerTranslateBrowsePathsToNodeIdsNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNodesPerNodeManagement();

  /** Sets the existing node's local value. */
  void setMaxNodesPerNodeManagement(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxNodesPerNodeManagementNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxMonitoredItemsPerCall();

  /** Sets the existing node's local value. */
  void setMaxMonitoredItemsPerCall(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxMonitoredItemsPerCallNode();
}
