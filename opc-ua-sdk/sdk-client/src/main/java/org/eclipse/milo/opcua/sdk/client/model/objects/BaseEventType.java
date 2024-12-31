/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.2">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.2</a>
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

  /**
   * Get the local value of the EventId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the EventId Node.
   * @throws UaException if an error occurs creating or getting the EventId Node.
   */
  ByteString getEventId() throws UaException;

  /**
   * Set the local value of the EventId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the EventId Node.
   * @throws UaException if an error occurs creating or getting the EventId Node.
   */
  void setEventId(ByteString value) throws UaException;

  /**
   * Read the value of the EventId Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link ByteString} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ByteString readEventId() throws UaException;

  /**
   * Write a new value for the EventId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link ByteString} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeEventId(ByteString value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readEventId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ByteString> readEventIdAsync();

  /**
   * An asynchronous implementation of {@link #writeEventId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeEventIdAsync(ByteString value);

  /**
   * Get the EventId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the EventId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getEventIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getEventIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getEventIdNodeAsync();

  /**
   * Get the local value of the EventType Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the EventType Node.
   * @throws UaException if an error occurs creating or getting the EventType Node.
   */
  NodeId getEventType() throws UaException;

  /**
   * Set the local value of the EventType Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the EventType Node.
   * @throws UaException if an error occurs creating or getting the EventType Node.
   */
  void setEventType(NodeId value) throws UaException;

  /**
   * Read the value of the EventType Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link NodeId} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId readEventType() throws UaException;

  /**
   * Write a new value for the EventType Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link NodeId} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeEventType(NodeId value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readEventType}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId> readEventTypeAsync();

  /**
   * An asynchronous implementation of {@link #writeEventType}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeEventTypeAsync(NodeId value);

  /**
   * Get the EventType {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the EventType {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getEventTypeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getEventTypeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getEventTypeNodeAsync();

  /**
   * Get the local value of the SourceNode Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SourceNode Node.
   * @throws UaException if an error occurs creating or getting the SourceNode Node.
   */
  NodeId getSourceNode() throws UaException;

  /**
   * Set the local value of the SourceNode Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SourceNode Node.
   * @throws UaException if an error occurs creating or getting the SourceNode Node.
   */
  void setSourceNode(NodeId value) throws UaException;

  /**
   * Read the value of the SourceNode Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link NodeId} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId readSourceNode() throws UaException;

  /**
   * Write a new value for the SourceNode Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link NodeId} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSourceNode(NodeId value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSourceNode}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId> readSourceNodeAsync();

  /**
   * An asynchronous implementation of {@link #writeSourceNode}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSourceNodeAsync(NodeId value);

  /**
   * Get the SourceNode {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SourceNode {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSourceNodeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSourceNodeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSourceNodeNodeAsync();

  /**
   * Get the local value of the SourceName Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SourceName Node.
   * @throws UaException if an error occurs creating or getting the SourceName Node.
   */
  String getSourceName() throws UaException;

  /**
   * Set the local value of the SourceName Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SourceName Node.
   * @throws UaException if an error occurs creating or getting the SourceName Node.
   */
  void setSourceName(String value) throws UaException;

  /**
   * Read the value of the SourceName Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readSourceName() throws UaException;

  /**
   * Write a new value for the SourceName Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSourceName(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSourceName}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readSourceNameAsync();

  /**
   * An asynchronous implementation of {@link #writeSourceName}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSourceNameAsync(String value);

  /**
   * Get the SourceName {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SourceName {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSourceNameNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSourceNameNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSourceNameNodeAsync();

  /**
   * Get the local value of the Time Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Time Node.
   * @throws UaException if an error occurs creating or getting the Time Node.
   */
  DateTime getTime() throws UaException;

  /**
   * Set the local value of the Time Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Time Node.
   * @throws UaException if an error occurs creating or getting the Time Node.
   */
  void setTime(DateTime value) throws UaException;

  /**
   * Read the value of the Time Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link DateTime} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DateTime readTime() throws UaException;

  /**
   * Write a new value for the Time Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link DateTime} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeTime(DateTime value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readTime}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DateTime> readTimeAsync();

  /**
   * An asynchronous implementation of {@link #writeTime}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeTimeAsync(DateTime value);

  /**
   * Get the Time {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Time {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getTimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getTimeNodeAsync();

  /**
   * Get the local value of the ReceiveTime Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ReceiveTime Node.
   * @throws UaException if an error occurs creating or getting the ReceiveTime Node.
   */
  DateTime getReceiveTime() throws UaException;

  /**
   * Set the local value of the ReceiveTime Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ReceiveTime Node.
   * @throws UaException if an error occurs creating or getting the ReceiveTime Node.
   */
  void setReceiveTime(DateTime value) throws UaException;

  /**
   * Read the value of the ReceiveTime Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link DateTime} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DateTime readReceiveTime() throws UaException;

  /**
   * Write a new value for the ReceiveTime Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link DateTime} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeReceiveTime(DateTime value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readReceiveTime}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DateTime> readReceiveTimeAsync();

  /**
   * An asynchronous implementation of {@link #writeReceiveTime}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeReceiveTimeAsync(DateTime value);

  /**
   * Get the ReceiveTime {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ReceiveTime {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getReceiveTimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getReceiveTimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getReceiveTimeNodeAsync();

  /**
   * Get the local value of the LocalTime Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LocalTime Node.
   * @throws UaException if an error occurs creating or getting the LocalTime Node.
   */
  TimeZoneDataType getLocalTime() throws UaException;

  /**
   * Set the local value of the LocalTime Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LocalTime Node.
   * @throws UaException if an error occurs creating or getting the LocalTime Node.
   */
  void setLocalTime(TimeZoneDataType value) throws UaException;

  /**
   * Read the value of the LocalTime Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link TimeZoneDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  TimeZoneDataType readLocalTime() throws UaException;

  /**
   * Write a new value for the LocalTime Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link TimeZoneDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLocalTime(TimeZoneDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLocalTime}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends TimeZoneDataType> readLocalTimeAsync();

  /**
   * An asynchronous implementation of {@link #writeLocalTime}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLocalTimeAsync(TimeZoneDataType value);

  /**
   * Get the LocalTime {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LocalTime {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getLocalTimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLocalTimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getLocalTimeNodeAsync();

  /**
   * Get the local value of the Message Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Message Node.
   * @throws UaException if an error occurs creating or getting the Message Node.
   */
  LocalizedText getMessage() throws UaException;

  /**
   * Set the local value of the Message Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Message Node.
   * @throws UaException if an error occurs creating or getting the Message Node.
   */
  void setMessage(LocalizedText value) throws UaException;

  /**
   * Read the value of the Message Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readMessage() throws UaException;

  /**
   * Write a new value for the Message Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMessage(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMessage}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readMessageAsync();

  /**
   * An asynchronous implementation of {@link #writeMessage}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMessageAsync(LocalizedText value);

  /**
   * Get the Message {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Message {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMessageNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMessageNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMessageNodeAsync();

  /**
   * Get the local value of the Severity Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Severity Node.
   * @throws UaException if an error occurs creating or getting the Severity Node.
   */
  UShort getSeverity() throws UaException;

  /**
   * Set the local value of the Severity Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Severity Node.
   * @throws UaException if an error occurs creating or getting the Severity Node.
   */
  void setSeverity(UShort value) throws UaException;

  /**
   * Read the value of the Severity Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readSeverity() throws UaException;

  /**
   * Write a new value for the Severity Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSeverity(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSeverity}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readSeverityAsync();

  /**
   * An asynchronous implementation of {@link #writeSeverity}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSeverityAsync(UShort value);

  /**
   * Get the Severity {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Severity {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSeverityNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSeverityNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSeverityNodeAsync();

  /**
   * Get the local value of the ConditionClassId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ConditionClassId Node.
   * @throws UaException if an error occurs creating or getting the ConditionClassId Node.
   */
  NodeId getConditionClassId() throws UaException;

  /**
   * Set the local value of the ConditionClassId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ConditionClassId Node.
   * @throws UaException if an error occurs creating or getting the ConditionClassId Node.
   */
  void setConditionClassId(NodeId value) throws UaException;

  /**
   * Read the value of the ConditionClassId Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link NodeId} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId readConditionClassId() throws UaException;

  /**
   * Write a new value for the ConditionClassId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link NodeId} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeConditionClassId(NodeId value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readConditionClassId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId> readConditionClassIdAsync();

  /**
   * An asynchronous implementation of {@link #writeConditionClassId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeConditionClassIdAsync(NodeId value);

  /**
   * Get the ConditionClassId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ConditionClassId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getConditionClassIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getConditionClassIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getConditionClassIdNodeAsync();

  /**
   * Get the local value of the ConditionClassName Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ConditionClassName Node.
   * @throws UaException if an error occurs creating or getting the ConditionClassName Node.
   */
  LocalizedText getConditionClassName() throws UaException;

  /**
   * Set the local value of the ConditionClassName Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ConditionClassName Node.
   * @throws UaException if an error occurs creating or getting the ConditionClassName Node.
   */
  void setConditionClassName(LocalizedText value) throws UaException;

  /**
   * Read the value of the ConditionClassName Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readConditionClassName() throws UaException;

  /**
   * Write a new value for the ConditionClassName Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeConditionClassName(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readConditionClassName}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readConditionClassNameAsync();

  /**
   * An asynchronous implementation of {@link #writeConditionClassName}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeConditionClassNameAsync(LocalizedText value);

  /**
   * Get the ConditionClassName {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ConditionClassName {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getConditionClassNameNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getConditionClassNameNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getConditionClassNameNodeAsync();

  /**
   * Get the local value of the ConditionSubClassId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ConditionSubClassId Node.
   * @throws UaException if an error occurs creating or getting the ConditionSubClassId Node.
   */
  NodeId[] getConditionSubClassId() throws UaException;

  /**
   * Set the local value of the ConditionSubClassId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ConditionSubClassId Node.
   * @throws UaException if an error occurs creating or getting the ConditionSubClassId Node.
   */
  void setConditionSubClassId(NodeId[] value) throws UaException;

  /**
   * Read the value of the ConditionSubClassId Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link NodeId[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId[] readConditionSubClassId() throws UaException;

  /**
   * Write a new value for the ConditionSubClassId Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link NodeId[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeConditionSubClassId(NodeId[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readConditionSubClassId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId[]> readConditionSubClassIdAsync();

  /**
   * An asynchronous implementation of {@link #writeConditionSubClassId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeConditionSubClassIdAsync(NodeId[] value);

  /**
   * Get the ConditionSubClassId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ConditionSubClassId {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getConditionSubClassIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getConditionSubClassIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getConditionSubClassIdNodeAsync();

  /**
   * Get the local value of the ConditionSubClassName Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ConditionSubClassName Node.
   * @throws UaException if an error occurs creating or getting the ConditionSubClassName Node.
   */
  LocalizedText[] getConditionSubClassName() throws UaException;

  /**
   * Set the local value of the ConditionSubClassName Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ConditionSubClassName Node.
   * @throws UaException if an error occurs creating or getting the ConditionSubClassName Node.
   */
  void setConditionSubClassName(LocalizedText[] value) throws UaException;

  /**
   * Read the value of the ConditionSubClassName Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link LocalizedText[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText[] readConditionSubClassName() throws UaException;

  /**
   * Write a new value for the ConditionSubClassName Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link LocalizedText[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeConditionSubClassName(LocalizedText[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readConditionSubClassName}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText[]> readConditionSubClassNameAsync();

  /**
   * An asynchronous implementation of {@link #writeConditionSubClassName}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeConditionSubClassNameAsync(LocalizedText[] value);

  /**
   * Get the ConditionSubClassName {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ConditionSubClassName {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getConditionSubClassNameNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getConditionSubClassNameNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getConditionSubClassNameNodeAsync();
}
