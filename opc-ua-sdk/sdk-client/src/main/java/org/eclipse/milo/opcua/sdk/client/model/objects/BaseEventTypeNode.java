package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.TimeZoneDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link BaseEventType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.2">Model
 *     documentation</a>
 */
public class BaseEventTypeNode extends BaseObjectTypeNode implements BaseEventType {
  public BaseEventTypeNode(
      OpcUaClient client,
      NodeId nodeId,
      NodeClass nodeClass,
      QualifiedName browseName,
      LocalizedText displayName,
      @Nullable LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType @Nullable [] rolePermissions,
      RolePermissionType @Nullable [] userRolePermissions,
      @Nullable AccessRestrictionType accessRestrictions,
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
  public PropertyTypeNode getSourceNameNode() throws UaException {
    return ClientNodeSupport.await(getSourceNameNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSourceNameNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SourceName",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readSourceName() throws UaException {
    return ClientNodeSupport.await(readSourceNameAsync());
  }

  @Override
  public void writeSourceName(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSourceNameAsync(value)),
        "http://opcfoundation.org/UA/}SourceName");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readSourceNameAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSourceNameNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SourceName",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSourceNameAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSourceNameNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SourceName",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getSourceNodeNode() throws UaException {
    return ClientNodeSupport.await(getSourceNodeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSourceNodeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SourceNode",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable NodeId readSourceNode() throws UaException {
    return ClientNodeSupport.await(readSourceNodeAsync());
  }

  @Override
  public void writeSourceNode(@Nullable NodeId value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSourceNodeAsync(value)),
        "http://opcfoundation.org/UA/}SourceNode");
  }

  @Override
  public CompletableFuture<? extends @Nullable NodeId> readSourceNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSourceNodeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SourceNode",
                            true,
                            NodeId.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable NodeId) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSourceNodeAsync(@Nullable NodeId value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSourceNodeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SourceNode",
                        value,
                        NodeId.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getReceiveTimeNode() throws UaException {
    return ClientNodeSupport.await(getReceiveTimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getReceiveTimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ReceiveTime",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable DateTime readReceiveTime() throws UaException {
    return ClientNodeSupport.await(readReceiveTimeAsync());
  }

  @Override
  public void writeReceiveTime(@Nullable DateTime value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeReceiveTimeAsync(value)),
        "http://opcfoundation.org/UA/}ReceiveTime");
  }

  @Override
  public CompletableFuture<? extends @Nullable DateTime> readReceiveTimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getReceiveTimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ReceiveTime",
                            true,
                            DateTime.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DateTime) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeReceiveTimeAsync(@Nullable DateTime value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getReceiveTimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ReceiveTime",
                        value,
                        DateTime.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getConditionClassIdNode() throws UaException {
    return ClientNodeSupport.await(getConditionClassIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getConditionClassIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ConditionClassId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable NodeId readConditionClassId() throws UaException {
    return ClientNodeSupport.await(readConditionClassIdAsync());
  }

  @Override
  public void writeConditionClassId(@Nullable NodeId value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeConditionClassIdAsync(value)),
        "http://opcfoundation.org/UA/}ConditionClassId");
  }

  @Override
  public CompletableFuture<? extends @Nullable NodeId> readConditionClassIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getConditionClassIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ConditionClassId",
                            false,
                            NodeId.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable NodeId) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeConditionClassIdAsync(@Nullable NodeId value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getConditionClassIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ConditionClassId",
                        value,
                        NodeId.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getConditionClassNameNode() throws UaException {
    return ClientNodeSupport.await(getConditionClassNameNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getConditionClassNameNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ConditionClassName",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable LocalizedText readConditionClassName() throws UaException {
    return ClientNodeSupport.await(readConditionClassNameAsync());
  }

  @Override
  public void writeConditionClassName(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeConditionClassNameAsync(value)),
        "http://opcfoundation.org/UA/}ConditionClassName");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readConditionClassNameAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getConditionClassNameNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ConditionClassName",
                            false,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeConditionClassNameAsync(@Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getConditionClassNameNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ConditionClassName",
                        value,
                        LocalizedText.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getConditionSubClassIdNode() throws UaException {
    return ClientNodeSupport.await(getConditionSubClassIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getConditionSubClassIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ConditionSubClassId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public NodeId @Nullable [] readConditionSubClassId() throws UaException {
    return ClientNodeSupport.await(readConditionSubClassIdAsync());
  }

  @Override
  public void writeConditionSubClassId(NodeId @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeConditionSubClassIdAsync(value)),
        "http://opcfoundation.org/UA/}ConditionSubClassId");
  }

  @Override
  public CompletableFuture<? extends NodeId @Nullable []> readConditionSubClassIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getConditionSubClassIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ConditionSubClassId",
                            false,
                            NodeId.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((NodeId @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeConditionSubClassIdAsync(NodeId @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getConditionSubClassIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ConditionSubClassId",
                        value,
                        NodeId.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getConditionSubClassNameNode() throws UaException {
    return ClientNodeSupport.await(getConditionSubClassNameNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getConditionSubClassNameNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ConditionSubClassName",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public LocalizedText @Nullable [] readConditionSubClassName() throws UaException {
    return ClientNodeSupport.await(readConditionSubClassNameAsync());
  }

  @Override
  public void writeConditionSubClassName(LocalizedText @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeConditionSubClassNameAsync(value)),
        "http://opcfoundation.org/UA/}ConditionSubClassName");
  }

  @Override
  public CompletableFuture<? extends LocalizedText @Nullable []> readConditionSubClassNameAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getConditionSubClassNameNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ConditionSubClassName",
                            false,
                            LocalizedText.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((LocalizedText @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeConditionSubClassNameAsync(
      LocalizedText @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getConditionSubClassNameNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ConditionSubClassName",
                        value,
                        LocalizedText.class,
                        1,
                        null)));
  }

  @Override
  public PropertyTypeNode getTimeNode() throws UaException {
    return ClientNodeSupport.await(getTimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getTimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Time",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable DateTime readTime() throws UaException {
    return ClientNodeSupport.await(readTimeAsync());
  }

  @Override
  public void writeTime(@Nullable DateTime value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeTimeAsync(value)), "http://opcfoundation.org/UA/}Time");
  }

  @Override
  public CompletableFuture<? extends @Nullable DateTime> readTimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getTimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Time",
                            true,
                            DateTime.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DateTime) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTimeAsync(@Nullable DateTime value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getTimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Time",
                        value,
                        DateTime.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getEventIdNode() throws UaException {
    return ClientNodeSupport.await(getEventIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getEventIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "EventId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable ByteString readEventId() throws UaException {
    return ClientNodeSupport.await(readEventIdAsync());
  }

  @Override
  public void writeEventId(@Nullable ByteString value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeEventIdAsync(value)), "http://opcfoundation.org/UA/}EventId");
  }

  @Override
  public CompletableFuture<? extends @Nullable ByteString> readEventIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getEventIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}EventId",
                            true,
                            ByteString.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ByteString) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeEventIdAsync(@Nullable ByteString value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getEventIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}EventId",
                        value,
                        ByteString.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getMessageNode() throws UaException {
    return ClientNodeSupport.await(getMessageNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMessageNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Message",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable LocalizedText readMessage() throws UaException {
    return ClientNodeSupport.await(readMessageAsync());
  }

  @Override
  public void writeMessage(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMessageAsync(value)), "http://opcfoundation.org/UA/}Message");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readMessageAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMessageNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Message",
                            true,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMessageAsync(@Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMessageNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Message",
                        value,
                        LocalizedText.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getSeverityNode() throws UaException {
    return ClientNodeSupport.await(getSeverityNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSeverityNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Severity",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UShort readSeverity() throws UaException {
    return ClientNodeSupport.await(readSeverityAsync());
  }

  @Override
  public void writeSeverity(@Nullable UShort value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSeverityAsync(value)),
        "http://opcfoundation.org/UA/}Severity");
  }

  @Override
  public CompletableFuture<? extends @Nullable UShort> readSeverityAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSeverityNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Severity",
                            true,
                            UShort.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UShort) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSeverityAsync(@Nullable UShort value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSeverityNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Severity",
                        value,
                        UShort.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getEventTypeNode() throws UaException {
    return ClientNodeSupport.await(getEventTypeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getEventTypeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "EventType",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable NodeId readEventType() throws UaException {
    return ClientNodeSupport.await(readEventTypeAsync());
  }

  @Override
  public void writeEventType(@Nullable NodeId value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeEventTypeAsync(value)),
        "http://opcfoundation.org/UA/}EventType");
  }

  @Override
  public CompletableFuture<? extends @Nullable NodeId> readEventTypeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getEventTypeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}EventType",
                            true,
                            NodeId.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable NodeId) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeEventTypeAsync(@Nullable NodeId value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getEventTypeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}EventType",
                        value,
                        NodeId.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getLocalTimeNode() throws UaException {
    return ClientNodeSupport.await(getLocalTimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getLocalTimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LocalTime",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable TimeZoneDataType readLocalTime() throws UaException {
    return ClientNodeSupport.await(readLocalTimeAsync());
  }

  @Override
  public void writeLocalTime(@Nullable TimeZoneDataType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLocalTimeAsync(value)),
        "http://opcfoundation.org/UA/}LocalTime");
  }

  @Override
  public CompletableFuture<? extends @Nullable TimeZoneDataType> readLocalTimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLocalTimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LocalTime",
                            false,
                            TimeZoneDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable TimeZoneDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLocalTimeAsync(@Nullable TimeZoneDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLocalTimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LocalTime",
                        value,
                        TimeZoneDataType.class,
                        -1,
                        null)));
  }
}
