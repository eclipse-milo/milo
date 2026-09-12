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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.TimeZoneDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.2">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface BaseEventType extends BaseObjectType {
  QualifiedProperty<ByteString> EVENT_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EventId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15"),
          -1,
          ByteString.class);

  QualifiedProperty<NodeId> EVENT_TYPE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EventType",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<NodeId> SOURCE_NODE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SourceNode",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<String> SOURCE_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SourceName",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<DateTime> TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Time",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<DateTime> RECEIVE_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ReceiveTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<TimeZoneDataType> LOCAL_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LocalTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=8912"),
          -1,
          TimeZoneDataType.class);

  QualifiedProperty<LocalizedText> MESSAGE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Message",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          -1,
          LocalizedText.class);

  QualifiedProperty<UShort> SEVERITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Severity",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<NodeId> CONDITION_CLASS_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConditionClassId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<LocalizedText> CONDITION_CLASS_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConditionClassName",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          -1,
          LocalizedText.class);

  QualifiedProperty<NodeId[]> CONDITION_SUB_CLASS_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConditionSubClassId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          1,
          NodeId[].class);

  QualifiedProperty<LocalizedText[]> CONDITION_SUB_CLASS_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConditionSubClassName",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          1,
          LocalizedText[].class);

  /** Gets the existing node's local value. */
  @Nullable ByteString getEventId();

  /** Sets the existing node's local value. */
  void setEventId(@Nullable ByteString value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getEventIdNode();

  /** Gets the existing node's local value. */
  @Nullable NodeId getEventType();

  /** Sets the existing node's local value. */
  void setEventType(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getEventTypeNode();

  /** Gets the existing node's local value. */
  @Nullable NodeId getSourceNode();

  /** Sets the existing node's local value. */
  void setSourceNode(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSourceNodeNode();

  /** Gets the existing node's local value. */
  @Nullable String getSourceName();

  /** Sets the existing node's local value. */
  void setSourceName(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSourceNameNode();

  /** Gets the existing node's local value. */
  @Nullable DateTime getTime();

  /** Sets the existing node's local value. */
  void setTime(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getTimeNode();

  /** Gets the existing node's local value. */
  @Nullable DateTime getReceiveTime();

  /** Sets the existing node's local value. */
  void setReceiveTime(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getReceiveTimeNode();

  /** Gets the existing node's local value. */
  @Nullable TimeZoneDataType getLocalTime();

  /** Sets the existing node's local value. */
  void setLocalTime(@Nullable TimeZoneDataType value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getLocalTimeNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getMessage();

  /** Sets the existing node's local value. */
  void setMessage(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMessageNode();

  /** Gets the existing node's local value. */
  @Nullable UShort getSeverity();

  /** Sets the existing node's local value. */
  void setSeverity(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSeverityNode();

  /** Gets the existing node's local value. */
  @Nullable NodeId getConditionClassId();

  /** Sets the existing node's local value. */
  void setConditionClassId(@Nullable NodeId value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getConditionClassIdNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getConditionClassName();

  /** Sets the existing node's local value. */
  void setConditionClassName(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getConditionClassNameNode();

  /** Gets the existing node's local value. */
  @Nullable NodeId @Nullable [] getConditionSubClassId();

  /** Sets the existing node's local value. */
  void setConditionSubClassId(@Nullable NodeId @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getConditionSubClassIdNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText @Nullable [] getConditionSubClassName();

  /** Sets the existing node's local value. */
  void setConditionSubClassName(@Nullable LocalizedText @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getConditionSubClassNameNode();
}
