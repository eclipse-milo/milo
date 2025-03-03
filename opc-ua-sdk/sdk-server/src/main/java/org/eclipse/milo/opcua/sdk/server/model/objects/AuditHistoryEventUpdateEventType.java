/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryEventFieldList;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.2">https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.2</a>
 */
public interface AuditHistoryEventUpdateEventType extends AuditHistoryUpdateEventType {
  QualifiedProperty<NodeId> UPDATED_NODE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UpdatedNode",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<PerformUpdateType> PERFORM_INSERT_REPLACE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PerformInsertReplace",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11293"),
          -1,
          PerformUpdateType.class);

  QualifiedProperty<EventFilter> FILTER =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Filter",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=725"),
          -1,
          EventFilter.class);

  QualifiedProperty<HistoryEventFieldList[]> NEW_VALUES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NewValues",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=920"),
          1,
          HistoryEventFieldList[].class);

  QualifiedProperty<HistoryEventFieldList[]> OLD_VALUES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OldValues",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=920"),
          1,
          HistoryEventFieldList[].class);

  NodeId getUpdatedNode();

  void setUpdatedNode(NodeId value);

  PropertyType getUpdatedNodeNode();

  PerformUpdateType getPerformInsertReplace();

  void setPerformInsertReplace(PerformUpdateType value);

  PropertyType getPerformInsertReplaceNode();

  EventFilter getFilter();

  void setFilter(EventFilter value);

  PropertyType getFilterNode();

  HistoryEventFieldList[] getNewValues();

  void setNewValues(HistoryEventFieldList[] value);

  PropertyType getNewValuesNode();

  HistoryEventFieldList[] getOldValues();

  void setOldValues(HistoryEventFieldList[] value);

  PropertyType getOldValuesNode();
}
