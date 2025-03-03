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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.7.2">https://reference.opcfoundation.org/v105/Core/docs/Part11/5.7.2</a>
 */
public interface HistoryServerCapabilitiesType extends BaseObjectType {
  QualifiedProperty<Boolean> ACCESS_HISTORY_DATA_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AccessHistoryDataCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> ACCESS_HISTORY_EVENTS_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AccessHistoryEventsCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<UInteger> MAX_RETURN_DATA_VALUES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxReturnDataValues",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_RETURN_EVENT_VALUES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxReturnEventValues",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<Boolean> INSERT_DATA_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InsertDataCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> REPLACE_DATA_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ReplaceDataCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> UPDATE_DATA_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UpdateDataCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> DELETE_RAW_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DeleteRawCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> DELETE_AT_TIME_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DeleteAtTimeCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> INSERT_EVENT_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InsertEventCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> REPLACE_EVENT_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ReplaceEventCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> UPDATE_EVENT_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UpdateEventCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> DELETE_EVENT_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DeleteEventCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> INSERT_ANNOTATION_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InsertAnnotationCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> SERVER_TIMESTAMP_SUPPORTED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ServerTimestampSupported",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  Boolean getAccessHistoryDataCapability();

  void setAccessHistoryDataCapability(Boolean value);

  PropertyType getAccessHistoryDataCapabilityNode();

  Boolean getAccessHistoryEventsCapability();

  void setAccessHistoryEventsCapability(Boolean value);

  PropertyType getAccessHistoryEventsCapabilityNode();

  UInteger getMaxReturnDataValues();

  void setMaxReturnDataValues(UInteger value);

  PropertyType getMaxReturnDataValuesNode();

  UInteger getMaxReturnEventValues();

  void setMaxReturnEventValues(UInteger value);

  PropertyType getMaxReturnEventValuesNode();

  Boolean getInsertDataCapability();

  void setInsertDataCapability(Boolean value);

  PropertyType getInsertDataCapabilityNode();

  Boolean getReplaceDataCapability();

  void setReplaceDataCapability(Boolean value);

  PropertyType getReplaceDataCapabilityNode();

  Boolean getUpdateDataCapability();

  void setUpdateDataCapability(Boolean value);

  PropertyType getUpdateDataCapabilityNode();

  Boolean getDeleteRawCapability();

  void setDeleteRawCapability(Boolean value);

  PropertyType getDeleteRawCapabilityNode();

  Boolean getDeleteAtTimeCapability();

  void setDeleteAtTimeCapability(Boolean value);

  PropertyType getDeleteAtTimeCapabilityNode();

  Boolean getInsertEventCapability();

  void setInsertEventCapability(Boolean value);

  PropertyType getInsertEventCapabilityNode();

  Boolean getReplaceEventCapability();

  void setReplaceEventCapability(Boolean value);

  PropertyType getReplaceEventCapabilityNode();

  Boolean getUpdateEventCapability();

  void setUpdateEventCapability(Boolean value);

  PropertyType getUpdateEventCapabilityNode();

  Boolean getDeleteEventCapability();

  void setDeleteEventCapability(Boolean value);

  PropertyType getDeleteEventCapabilityNode();

  Boolean getInsertAnnotationCapability();

  void setInsertAnnotationCapability(Boolean value);

  PropertyType getInsertAnnotationCapabilityNode();

  Boolean getServerTimestampSupported();

  void setServerTimestampSupported(Boolean value);

  PropertyType getServerTimestampSupportedNode();

  FolderType getAggregateFunctionsNode();
}
