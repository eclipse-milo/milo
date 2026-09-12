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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
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
  @Nullable ByteString getEventId() throws UaException;

  /** Sets the existing node's local value. */
  void setEventId(@Nullable ByteString value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ByteString readEventId() throws UaException;

  /** Writes the value remotely. */
  void writeEventId(@Nullable ByteString value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ByteString> readEventIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEventIdAsync(@Nullable ByteString value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getEventIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getEventIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable NodeId getEventType() throws UaException;

  /** Sets the existing node's local value. */
  void setEventType(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId readEventType() throws UaException;

  /** Writes the value remotely. */
  void writeEventType(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId> readEventTypeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEventTypeAsync(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getEventTypeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getEventTypeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable NodeId getSourceNode() throws UaException;

  /** Sets the existing node's local value. */
  void setSourceNode(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId readSourceNode() throws UaException;

  /** Writes the value remotely. */
  void writeSourceNode(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId> readSourceNodeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSourceNodeAsync(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSourceNodeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getSourceNodeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getSourceName() throws UaException;

  /** Sets the existing node's local value. */
  void setSourceName(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readSourceName() throws UaException;

  /** Writes the value remotely. */
  void writeSourceName(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readSourceNameAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSourceNameAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSourceNameNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getSourceNameNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DateTime getTime() throws UaException;

  /** Sets the existing node's local value. */
  void setTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readTime() throws UaException;

  /** Writes the value remotely. */
  void writeTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeTimeAsync(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getTimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DateTime getReceiveTime() throws UaException;

  /** Sets the existing node's local value. */
  void setReceiveTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readReceiveTime() throws UaException;

  /** Writes the value remotely. */
  void writeReceiveTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readReceiveTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeReceiveTimeAsync(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getReceiveTimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getReceiveTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable TimeZoneDataType getLocalTime() throws UaException;

  /** Sets the existing node's local value. */
  void setLocalTime(@Nullable TimeZoneDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable TimeZoneDataType readLocalTime() throws UaException;

  /** Writes the value remotely. */
  void writeLocalTime(@Nullable TimeZoneDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable TimeZoneDataType> readLocalTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLocalTimeAsync(@Nullable TimeZoneDataType value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getLocalTimeNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getLocalTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getMessage() throws UaException;

  /** Sets the existing node's local value. */
  void setMessage(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readMessage() throws UaException;

  /** Writes the value remotely. */
  void writeMessage(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readMessageAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMessageAsync(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMessageNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getMessageNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UShort getSeverity() throws UaException;

  /** Sets the existing node's local value. */
  void setSeverity(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readSeverity() throws UaException;

  /** Writes the value remotely. */
  void writeSeverity(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readSeverityAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSeverityAsync(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSeverityNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getSeverityNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable NodeId getConditionClassId() throws UaException;

  /** Sets the existing node's local value. */
  void setConditionClassId(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId readConditionClassId() throws UaException;

  /** Writes the value remotely. */
  void writeConditionClassId(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId> readConditionClassIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeConditionClassIdAsync(@Nullable NodeId value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getConditionClassIdNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getConditionClassIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getConditionClassName() throws UaException;

  /** Sets the existing node's local value. */
  void setConditionClassName(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readConditionClassName() throws UaException;

  /** Writes the value remotely. */
  void writeConditionClassName(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readConditionClassNameAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeConditionClassNameAsync(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getConditionClassNameNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getConditionClassNameNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable NodeId @Nullable [] getConditionSubClassId() throws UaException;

  /** Sets the existing node's local value. */
  void setConditionSubClassId(@Nullable NodeId @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId @Nullable [] readConditionSubClassId() throws UaException;

  /** Writes the value remotely. */
  void writeConditionSubClassId(@Nullable NodeId @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId @Nullable []> readConditionSubClassIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeConditionSubClassIdAsync(@Nullable NodeId @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getConditionSubClassIdNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getConditionSubClassIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText @Nullable [] getConditionSubClassName() throws UaException;

  /** Sets the existing node's local value. */
  void setConditionSubClassName(@Nullable LocalizedText @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText @Nullable [] readConditionSubClassName() throws UaException;

  /** Writes the value remotely. */
  void writeConditionSubClassName(@Nullable LocalizedText @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText @Nullable []>
      readConditionSubClassNameAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeConditionSubClassNameAsync(
      @Nullable LocalizedText @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getConditionSubClassNameNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getConditionSubClassNameNodeAsync();
}
