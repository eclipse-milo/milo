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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.7.2">https://reference.opcfoundation.org/v105/Core/docs/Part11/5.7.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
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

  /** Gets the existing node's local value. */
  @Nullable Boolean getAccessHistoryDataCapability();

  /** Sets the existing node's local value. */
  void setAccessHistoryDataCapability(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getAccessHistoryDataCapabilityNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getAccessHistoryEventsCapability();

  /** Sets the existing node's local value. */
  void setAccessHistoryEventsCapability(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getAccessHistoryEventsCapabilityNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxReturnDataValues();

  /** Sets the existing node's local value. */
  void setMaxReturnDataValues(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxReturnDataValuesNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxReturnEventValues();

  /** Sets the existing node's local value. */
  void setMaxReturnEventValues(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxReturnEventValuesNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getInsertDataCapability();

  /** Sets the existing node's local value. */
  void setInsertDataCapability(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getInsertDataCapabilityNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getReplaceDataCapability();

  /** Sets the existing node's local value. */
  void setReplaceDataCapability(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getReplaceDataCapabilityNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getUpdateDataCapability();

  /** Sets the existing node's local value. */
  void setUpdateDataCapability(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getUpdateDataCapabilityNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getDeleteRawCapability();

  /** Sets the existing node's local value. */
  void setDeleteRawCapability(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDeleteRawCapabilityNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getDeleteAtTimeCapability();

  /** Sets the existing node's local value. */
  void setDeleteAtTimeCapability(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDeleteAtTimeCapabilityNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getInsertEventCapability();

  /** Sets the existing node's local value. */
  void setInsertEventCapability(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getInsertEventCapabilityNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getReplaceEventCapability();

  /** Sets the existing node's local value. */
  void setReplaceEventCapability(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getReplaceEventCapabilityNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getUpdateEventCapability();

  /** Sets the existing node's local value. */
  void setUpdateEventCapability(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getUpdateEventCapabilityNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getDeleteEventCapability();

  /** Sets the existing node's local value. */
  void setDeleteEventCapability(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDeleteEventCapabilityNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getInsertAnnotationCapability();

  /** Sets the existing node's local value. */
  void setInsertAnnotationCapability(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getInsertAnnotationCapabilityNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getServerTimestampSupported();

  /** Sets the existing node's local value. */
  void setServerTimestampSupported(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getServerTimestampSupportedNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  FolderType getAggregateFunctionsNode();
}
