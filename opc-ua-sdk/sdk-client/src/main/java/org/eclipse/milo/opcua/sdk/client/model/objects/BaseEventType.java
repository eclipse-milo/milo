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
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the BaseEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.2">Model
 *     documentation</a>
 */
public interface BaseEventType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2041L);

  QualifiedProperty<String> SourceName_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SourceName",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<NodeId> SourceNode_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SourceNode",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  QualifiedProperty<DateTime> ReceiveTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ReceiveTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
          -1,
          DateTime.class);

  QualifiedProperty<NodeId> ConditionClassId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ConditionClassId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  QualifiedProperty<LocalizedText> ConditionClassName_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ConditionClassName",
          ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
          -1,
          LocalizedText.class);

  QualifiedProperty<NodeId[]> ConditionSubClassId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ConditionSubClassId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          1,
          NodeId[].class);

  QualifiedProperty<LocalizedText[]> ConditionSubClassName_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ConditionSubClassName",
          ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
          1,
          LocalizedText[].class);

  QualifiedProperty<DateTime> Time_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Time",
          ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
          -1,
          DateTime.class);

  QualifiedProperty<ByteString> EventId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EventId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15L),
          -1,
          ByteString.class);

  QualifiedProperty<LocalizedText> Message_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Message",
          ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
          -1,
          LocalizedText.class);

  QualifiedProperty<UShort> Severity_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Severity",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
          -1,
          UShort.class);

  QualifiedProperty<NodeId> EventType_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EventType",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  QualifiedProperty<TimeZoneDataType> LocalTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LocalTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 8912L),
          -1,
          TimeZoneDataType.class);

  /**
   * Resolves the mandatory SourceName child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSourceNameNode() throws UaException;

  /** Asynchronous form of {@link #getSourceNameNode()}. */
  CompletableFuture<? extends PropertyType> getSourceNameNodeAsync();

  /**
   * Reads the Value of the SourceName child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readSourceName() throws UaException;

  /**
   * Writes the Value of the SourceName child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSourceName(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readSourceName()}. */
  CompletableFuture<? extends @Nullable String> readSourceNameAsync();

  /** Asynchronous form of {@link #writeSourceName}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSourceNameAsync(@Nullable String value);

  /**
   * Resolves the mandatory SourceNode child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSourceNodeNode() throws UaException;

  /** Asynchronous form of {@link #getSourceNodeNode()}. */
  CompletableFuture<? extends PropertyType> getSourceNodeNodeAsync();

  /**
   * Reads the Value of the SourceNode child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readSourceNode() throws UaException;

  /**
   * Writes the Value of the SourceNode child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSourceNode(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readSourceNode()}. */
  CompletableFuture<? extends @Nullable NodeId> readSourceNodeAsync();

  /** Asynchronous form of {@link #writeSourceNode}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSourceNodeAsync(@Nullable NodeId value);

  /**
   * Resolves the mandatory ReceiveTime child, a PropertyType with DataType UtcTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getReceiveTimeNode() throws UaException;

  /** Asynchronous form of {@link #getReceiveTimeNode()}. */
  CompletableFuture<? extends PropertyType> getReceiveTimeNodeAsync();

  /**
   * Reads the Value of the ReceiveTime child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readReceiveTime() throws UaException;

  /**
   * Writes the Value of the ReceiveTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeReceiveTime(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readReceiveTime()}. */
  CompletableFuture<? extends @Nullable DateTime> readReceiveTimeAsync();

  /** Asynchronous form of {@link #writeReceiveTime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeReceiveTimeAsync(@Nullable DateTime value);

  /**
   * Resolves the optional ConditionClassId child, a PropertyType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getConditionClassIdNode() throws UaException;

  /** Asynchronous form of {@link #getConditionClassIdNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getConditionClassIdNodeAsync();

  /**
   * Reads the Value of the ConditionClassId child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readConditionClassId() throws UaException;

  /**
   * Writes the Value of the ConditionClassId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeConditionClassId(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readConditionClassId()}. */
  CompletableFuture<? extends @Nullable NodeId> readConditionClassIdAsync();

  /** Asynchronous form of {@link #writeConditionClassId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeConditionClassIdAsync(@Nullable NodeId value);

  /**
   * Resolves the optional ConditionClassName child, a PropertyType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getConditionClassNameNode() throws UaException;

  /** Asynchronous form of {@link #getConditionClassNameNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getConditionClassNameNodeAsync();

  /**
   * Reads the Value of the ConditionClassName child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readConditionClassName() throws UaException;

  /**
   * Writes the Value of the ConditionClassName child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeConditionClassName(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readConditionClassName()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readConditionClassNameAsync();

  /** Asynchronous form of {@link #writeConditionClassName}; completes with the operation status. */
  CompletableFuture<StatusCode> writeConditionClassNameAsync(@Nullable LocalizedText value);

  /**
   * Resolves the optional ConditionSubClassId child, a PropertyType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getConditionSubClassIdNode() throws UaException;

  /** Asynchronous form of {@link #getConditionSubClassIdNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getConditionSubClassIdNodeAsync();

  /**
   * Reads the Value of the ConditionSubClassId child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  NodeId @Nullable [] readConditionSubClassId() throws UaException;

  /**
   * Writes the Value of the ConditionSubClassId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeConditionSubClassId(NodeId @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readConditionSubClassId()}. */
  CompletableFuture<? extends NodeId @Nullable []> readConditionSubClassIdAsync();

  /**
   * Asynchronous form of {@link #writeConditionSubClassId}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeConditionSubClassIdAsync(NodeId @Nullable [] value);

  /**
   * Resolves the optional ConditionSubClassName child, a PropertyType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getConditionSubClassNameNode() throws UaException;

  /** Asynchronous form of {@link #getConditionSubClassNameNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getConditionSubClassNameNodeAsync();

  /**
   * Reads the Value of the ConditionSubClassName child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  LocalizedText @Nullable [] readConditionSubClassName() throws UaException;

  /**
   * Writes the Value of the ConditionSubClassName child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeConditionSubClassName(LocalizedText @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readConditionSubClassName()}. */
  CompletableFuture<? extends LocalizedText @Nullable []> readConditionSubClassNameAsync();

  /**
   * Asynchronous form of {@link #writeConditionSubClassName}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeConditionSubClassNameAsync(LocalizedText @Nullable [] value);

  /**
   * Resolves the mandatory Time child, a PropertyType with DataType UtcTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getTimeNode() throws UaException;

  /** Asynchronous form of {@link #getTimeNode()}. */
  CompletableFuture<? extends PropertyType> getTimeNodeAsync();

  /**
   * Reads the Value of the Time child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readTime() throws UaException;

  /**
   * Writes the Value of the Time child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTime(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readTime()}. */
  CompletableFuture<? extends @Nullable DateTime> readTimeAsync();

  /** Asynchronous form of {@link #writeTime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTimeAsync(@Nullable DateTime value);

  /**
   * Resolves the mandatory EventId child, a PropertyType with DataType ByteString.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getEventIdNode() throws UaException;

  /** Asynchronous form of {@link #getEventIdNode()}. */
  CompletableFuture<? extends PropertyType> getEventIdNodeAsync();

  /**
   * Reads the Value of the EventId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ByteString readEventId() throws UaException;

  /**
   * Writes the Value of the EventId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEventId(@Nullable ByteString value) throws UaException;

  /** Asynchronous form of {@link #readEventId()}. */
  CompletableFuture<? extends @Nullable ByteString> readEventIdAsync();

  /** Asynchronous form of {@link #writeEventId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeEventIdAsync(@Nullable ByteString value);

  /**
   * Resolves the mandatory Message child, a PropertyType with DataType LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getMessageNode() throws UaException;

  /** Asynchronous form of {@link #getMessageNode()}. */
  CompletableFuture<? extends PropertyType> getMessageNodeAsync();

  /**
   * Reads the Value of the Message child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readMessage() throws UaException;

  /**
   * Writes the Value of the Message child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMessage(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readMessage()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readMessageAsync();

  /** Asynchronous form of {@link #writeMessage}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMessageAsync(@Nullable LocalizedText value);

  /**
   * Resolves the mandatory Severity child, a PropertyType with DataType UInt16.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSeverityNode() throws UaException;

  /** Asynchronous form of {@link #getSeverityNode()}. */
  CompletableFuture<? extends PropertyType> getSeverityNodeAsync();

  /**
   * Reads the Value of the Severity child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readSeverity() throws UaException;

  /**
   * Writes the Value of the Severity child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSeverity(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readSeverity()}. */
  CompletableFuture<? extends @Nullable UShort> readSeverityAsync();

  /** Asynchronous form of {@link #writeSeverity}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSeverityAsync(@Nullable UShort value);

  /**
   * Resolves the mandatory EventType child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getEventTypeNode() throws UaException;

  /** Asynchronous form of {@link #getEventTypeNode()}. */
  CompletableFuture<? extends PropertyType> getEventTypeNodeAsync();

  /**
   * Reads the Value of the EventType child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readEventType() throws UaException;

  /**
   * Writes the Value of the EventType child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEventType(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readEventType()}. */
  CompletableFuture<? extends @Nullable NodeId> readEventTypeAsync();

  /** Asynchronous form of {@link #writeEventType}; completes with the operation status. */
  CompletableFuture<StatusCode> writeEventTypeAsync(@Nullable NodeId value);

  /**
   * Resolves the optional LocalTime child, a PropertyType with DataType TimeZoneDataType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getLocalTimeNode() throws UaException;

  /** Asynchronous form of {@link #getLocalTimeNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getLocalTimeNodeAsync();

  /**
   * Reads the Value of the LocalTime child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable TimeZoneDataType readLocalTime() throws UaException;

  /**
   * Writes the Value of the LocalTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLocalTime(@Nullable TimeZoneDataType value) throws UaException;

  /** Asynchronous form of {@link #readLocalTime()}. */
  CompletableFuture<? extends @Nullable TimeZoneDataType> readLocalTimeAsync();

  /** Asynchronous form of {@link #writeLocalTime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeLocalTimeAsync(@Nullable TimeZoneDataType value);
}
