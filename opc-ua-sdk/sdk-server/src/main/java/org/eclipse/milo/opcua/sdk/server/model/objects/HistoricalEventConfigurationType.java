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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.4.3">https://reference.opcfoundation.org/v105/Core/docs/Part11/5.4.3</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface HistoricalEventConfigurationType extends BaseObjectType {
  QualifiedProperty<DateTime> START_OF_ARCHIVE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "StartOfArchive",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<DateTime> START_OF_ONLINE_ARCHIVE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "StartOfOnlineArchive",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<SimpleAttributeOperand[]> SORT_BY_EVENT_FIELDS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SortByEventFields",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=601"),
          1,
          SimpleAttributeOperand[].class);

  /** Gets the existing node's local value. */
  @Nullable DateTime getStartOfArchive();

  /** Sets the existing node's local value. */
  void setStartOfArchive(@Nullable DateTime value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getStartOfArchiveNode();

  /** Gets the existing node's local value. */
  @Nullable DateTime getStartOfOnlineArchive();

  /** Sets the existing node's local value. */
  void setStartOfOnlineArchive(@Nullable DateTime value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getStartOfOnlineArchiveNode();

  /** Gets the existing node's local value. */
  @Nullable SimpleAttributeOperand @Nullable [] getSortByEventFields();

  /** Sets the existing node's local value. */
  void setSortByEventFields(@Nullable SimpleAttributeOperand @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSortByEventFieldsNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  FolderType getEventTypesNode();
}
