/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.TimeZoneDataType;

public class BaseEventTypeNode extends BaseObjectTypeNode implements BaseEventType {
  public BaseEventTypeNode(
      OpcUaClient client,
      NodeId nodeId,
      NodeClass nodeClass,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions,
      UByte eventNotifier) {
    super(
        client,
        nodeId,
        nodeClass,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions,
        eventNotifier);
  }

  @Override
  public ByteString getEventId() throws UaException {
    PropertyTypeNode node = getEventIdNode();
    return (ByteString) node.getValue().getValue().getValue();
  }

  @Override
  public void setEventId(ByteString value) throws UaException {
    PropertyTypeNode node = getEventIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public ByteString readEventId() throws UaException {
    try {
      return readEventIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeEventId(ByteString value) throws UaException {
    try {
      writeEventIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends ByteString> readEventIdAsync() {
    return getEventIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (ByteString) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeEventIdAsync(ByteString eventId) {
    DataValue value = DataValue.valueOnly(new Variant(eventId));
    return getEventIdNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getEventIdNode() throws UaException {
    try {
      return getEventIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getEventIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "EventId", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public NodeId getEventType() throws UaException {
    PropertyTypeNode node = getEventTypeNode();
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setEventType(NodeId value) throws UaException {
    PropertyTypeNode node = getEventTypeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public NodeId readEventType() throws UaException {
    try {
      return readEventTypeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeEventType(NodeId value) throws UaException {
    try {
      writeEventTypeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends NodeId> readEventTypeAsync() {
    return getEventTypeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (NodeId) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeEventTypeAsync(NodeId eventType) {
    DataValue value = DataValue.valueOnly(new Variant(eventType));
    return getEventTypeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getEventTypeNode() throws UaException {
    try {
      return getEventTypeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getEventTypeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "EventType", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public NodeId getSourceNode() throws UaException {
    PropertyTypeNode node = getSourceNodeNode();
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setSourceNode(NodeId value) throws UaException {
    PropertyTypeNode node = getSourceNodeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public NodeId readSourceNode() throws UaException {
    try {
      return readSourceNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSourceNode(NodeId value) throws UaException {
    try {
      writeSourceNodeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends NodeId> readSourceNodeAsync() {
    return getSourceNodeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (NodeId) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSourceNodeAsync(NodeId sourceNode) {
    DataValue value = DataValue.valueOnly(new Variant(sourceNode));
    return getSourceNodeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getSourceNodeNode() throws UaException {
    try {
      return getSourceNodeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSourceNodeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "SourceNode", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getSourceName() throws UaException {
    PropertyTypeNode node = getSourceNameNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setSourceName(String value) throws UaException {
    PropertyTypeNode node = getSourceNameNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readSourceName() throws UaException {
    try {
      return readSourceNameAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSourceName(String value) throws UaException {
    try {
      writeSourceNameAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readSourceNameAsync() {
    return getSourceNameNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSourceNameAsync(String sourceName) {
    DataValue value = DataValue.valueOnly(new Variant(sourceName));
    return getSourceNameNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getSourceNameNode() throws UaException {
    try {
      return getSourceNameNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSourceNameNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "SourceName", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public DateTime getTime() throws UaException {
    PropertyTypeNode node = getTimeNode();
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setTime(DateTime value) throws UaException {
    PropertyTypeNode node = getTimeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public DateTime readTime() throws UaException {
    try {
      return readTimeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeTime(DateTime value) throws UaException {
    try {
      writeTimeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends DateTime> readTimeAsync() {
    return getTimeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (DateTime) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeTimeAsync(DateTime time) {
    DataValue value = DataValue.valueOnly(new Variant(time));
    return getTimeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getTimeNode() throws UaException {
    try {
      return getTimeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getTimeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Time", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public DateTime getReceiveTime() throws UaException {
    PropertyTypeNode node = getReceiveTimeNode();
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setReceiveTime(DateTime value) throws UaException {
    PropertyTypeNode node = getReceiveTimeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public DateTime readReceiveTime() throws UaException {
    try {
      return readReceiveTimeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeReceiveTime(DateTime value) throws UaException {
    try {
      writeReceiveTimeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends DateTime> readReceiveTimeAsync() {
    return getReceiveTimeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (DateTime) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeReceiveTimeAsync(DateTime receiveTime) {
    DataValue value = DataValue.valueOnly(new Variant(receiveTime));
    return getReceiveTimeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getReceiveTimeNode() throws UaException {
    try {
      return getReceiveTimeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getReceiveTimeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ReceiveTime",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public TimeZoneDataType getLocalTime() throws UaException {
    PropertyTypeNode node = getLocalTimeNode();
    return cast(node.getValue().getValue().getValue(), TimeZoneDataType.class);
  }

  @Override
  public void setLocalTime(TimeZoneDataType value) throws UaException {
    PropertyTypeNode node = getLocalTimeNode();
    ExtensionObject encoded = ExtensionObject.encode(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public TimeZoneDataType readLocalTime() throws UaException {
    try {
      return readLocalTimeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLocalTime(TimeZoneDataType value) throws UaException {
    try {
      writeLocalTimeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends TimeZoneDataType> readLocalTimeAsync() {
    return getLocalTimeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), TimeZoneDataType.class));
  }

  @Override
  public CompletableFuture<StatusCode> writeLocalTimeAsync(TimeZoneDataType localTime) {
    ExtensionObject encoded = ExtensionObject.encode(client.getStaticEncodingContext(), localTime);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getLocalTimeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getLocalTimeNode() throws UaException {
    try {
      return getLocalTimeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLocalTimeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "LocalTime", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public LocalizedText getMessage() throws UaException {
    PropertyTypeNode node = getMessageNode();
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setMessage(LocalizedText value) throws UaException {
    PropertyTypeNode node = getMessageNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText readMessage() throws UaException {
    try {
      return readMessageAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMessage(LocalizedText value) throws UaException {
    try {
      writeMessageAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText> readMessageAsync() {
    return getMessageNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMessageAsync(LocalizedText message) {
    DataValue value = DataValue.valueOnly(new Variant(message));
    return getMessageNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMessageNode() throws UaException {
    try {
      return getMessageNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMessageNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Message", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UShort getSeverity() throws UaException {
    PropertyTypeNode node = getSeverityNode();
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setSeverity(UShort value) throws UaException {
    PropertyTypeNode node = getSeverityNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UShort readSeverity() throws UaException {
    try {
      return readSeverityAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSeverity(UShort value) throws UaException {
    try {
      writeSeverityAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UShort> readSeverityAsync() {
    return getSeverityNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UShort) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSeverityAsync(UShort severity) {
    DataValue value = DataValue.valueOnly(new Variant(severity));
    return getSeverityNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getSeverityNode() throws UaException {
    try {
      return getSeverityNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSeverityNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Severity", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public NodeId getConditionClassId() throws UaException {
    PropertyTypeNode node = getConditionClassIdNode();
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setConditionClassId(NodeId value) throws UaException {
    PropertyTypeNode node = getConditionClassIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public NodeId readConditionClassId() throws UaException {
    try {
      return readConditionClassIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeConditionClassId(NodeId value) throws UaException {
    try {
      writeConditionClassIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends NodeId> readConditionClassIdAsync() {
    return getConditionClassIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (NodeId) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeConditionClassIdAsync(NodeId conditionClassId) {
    DataValue value = DataValue.valueOnly(new Variant(conditionClassId));
    return getConditionClassIdNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getConditionClassIdNode() throws UaException {
    try {
      return getConditionClassIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getConditionClassIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ConditionClassId",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public LocalizedText getConditionClassName() throws UaException {
    PropertyTypeNode node = getConditionClassNameNode();
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setConditionClassName(LocalizedText value) throws UaException {
    PropertyTypeNode node = getConditionClassNameNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText readConditionClassName() throws UaException {
    try {
      return readConditionClassNameAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeConditionClassName(LocalizedText value) throws UaException {
    try {
      writeConditionClassNameAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText> readConditionClassNameAsync() {
    return getConditionClassNameNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeConditionClassNameAsync(
      LocalizedText conditionClassName) {
    DataValue value = DataValue.valueOnly(new Variant(conditionClassName));
    return getConditionClassNameNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getConditionClassNameNode() throws UaException {
    try {
      return getConditionClassNameNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getConditionClassNameNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ConditionClassName",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public NodeId[] getConditionSubClassId() throws UaException {
    PropertyTypeNode node = getConditionSubClassIdNode();
    return (NodeId[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setConditionSubClassId(NodeId[] value) throws UaException {
    PropertyTypeNode node = getConditionSubClassIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public NodeId[] readConditionSubClassId() throws UaException {
    try {
      return readConditionSubClassIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeConditionSubClassId(NodeId[] value) throws UaException {
    try {
      writeConditionSubClassIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends NodeId[]> readConditionSubClassIdAsync() {
    return getConditionSubClassIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (NodeId[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeConditionSubClassIdAsync(NodeId[] conditionSubClassId) {
    DataValue value = DataValue.valueOnly(new Variant(conditionSubClassId));
    return getConditionSubClassIdNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getConditionSubClassIdNode() throws UaException {
    try {
      return getConditionSubClassIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getConditionSubClassIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ConditionSubClassId",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public LocalizedText[] getConditionSubClassName() throws UaException {
    PropertyTypeNode node = getConditionSubClassNameNode();
    return (LocalizedText[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setConditionSubClassName(LocalizedText[] value) throws UaException {
    PropertyTypeNode node = getConditionSubClassNameNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText[] readConditionSubClassName() throws UaException {
    try {
      return readConditionSubClassNameAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeConditionSubClassName(LocalizedText[] value) throws UaException {
    try {
      writeConditionSubClassNameAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText[]> readConditionSubClassNameAsync() {
    return getConditionSubClassNameNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeConditionSubClassNameAsync(
      LocalizedText[] conditionSubClassName) {
    DataValue value = DataValue.valueOnly(new Variant(conditionSubClassName));
    return getConditionSubClassNameNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getConditionSubClassNameNode() throws UaException {
    try {
      return getConditionSubClassNameNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getConditionSubClassNameNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ConditionSubClassName",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
