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
import org.eclipse.milo.opcua.sdk.client.model.variables.ServerStatusTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerStatusDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.TimeZoneDataType;

public class ServerTypeNode extends BaseObjectTypeNode implements ServerType {
  public ServerTypeNode(
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
  public String[] getServerArray() throws UaException {
    PropertyTypeNode node = getServerArrayNode();
    return (String[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setServerArray(String[] value) throws UaException {
    PropertyTypeNode node = getServerArrayNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String[] readServerArray() throws UaException {
    try {
      return readServerArrayAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeServerArray(String[] value) throws UaException {
    try {
      writeServerArrayAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String[]> readServerArrayAsync() {
    return getServerArrayNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeServerArrayAsync(String[] serverArray) {
    DataValue value = DataValue.valueOnly(new Variant(serverArray));
    return getServerArrayNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getServerArrayNode() throws UaException {
    try {
      return getServerArrayNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getServerArrayNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ServerArray",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String[] getNamespaceArray() throws UaException {
    PropertyTypeNode node = getNamespaceArrayNode();
    return (String[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setNamespaceArray(String[] value) throws UaException {
    PropertyTypeNode node = getNamespaceArrayNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String[] readNamespaceArray() throws UaException {
    try {
      return readNamespaceArrayAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeNamespaceArray(String[] value) throws UaException {
    try {
      writeNamespaceArrayAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String[]> readNamespaceArrayAsync() {
    return getNamespaceArrayNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeNamespaceArrayAsync(String[] namespaceArray) {
    DataValue value = DataValue.valueOnly(new Variant(namespaceArray));
    return getNamespaceArrayNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getNamespaceArrayNode() throws UaException {
    try {
      return getNamespaceArrayNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getNamespaceArrayNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "NamespaceArray",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getUrisVersion() throws UaException {
    PropertyTypeNode node = getUrisVersionNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setUrisVersion(UInteger value) throws UaException {
    PropertyTypeNode node = getUrisVersionNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readUrisVersion() throws UaException {
    try {
      return readUrisVersionAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeUrisVersion(UInteger value) throws UaException {
    try {
      writeUrisVersionAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readUrisVersionAsync() {
    return getUrisVersionNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeUrisVersionAsync(UInteger urisVersion) {
    DataValue value = DataValue.valueOnly(new Variant(urisVersion));
    return getUrisVersionNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getUrisVersionNode() throws UaException {
    try {
      return getUrisVersionNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUrisVersionNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "UrisVersion",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UByte getServiceLevel() throws UaException {
    PropertyTypeNode node = getServiceLevelNode();
    return (UByte) node.getValue().getValue().getValue();
  }

  @Override
  public void setServiceLevel(UByte value) throws UaException {
    PropertyTypeNode node = getServiceLevelNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UByte readServiceLevel() throws UaException {
    try {
      return readServiceLevelAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeServiceLevel(UByte value) throws UaException {
    try {
      writeServiceLevelAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UByte> readServiceLevelAsync() {
    return getServiceLevelNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UByte) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeServiceLevelAsync(UByte serviceLevel) {
    DataValue value = DataValue.valueOnly(new Variant(serviceLevel));
    return getServiceLevelNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getServiceLevelNode() throws UaException {
    try {
      return getServiceLevelNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getServiceLevelNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ServiceLevel",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getAuditing() throws UaException {
    PropertyTypeNode node = getAuditingNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setAuditing(Boolean value) throws UaException {
    PropertyTypeNode node = getAuditingNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readAuditing() throws UaException {
    try {
      return readAuditingAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeAuditing(Boolean value) throws UaException {
    try {
      writeAuditingAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readAuditingAsync() {
    return getAuditingNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeAuditingAsync(Boolean auditing) {
    DataValue value = DataValue.valueOnly(new Variant(auditing));
    return getAuditingNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getAuditingNode() throws UaException {
    try {
      return getAuditingNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getAuditingNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Auditing", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public DateTime getEstimatedReturnTime() throws UaException {
    PropertyTypeNode node = getEstimatedReturnTimeNode();
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setEstimatedReturnTime(DateTime value) throws UaException {
    PropertyTypeNode node = getEstimatedReturnTimeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public DateTime readEstimatedReturnTime() throws UaException {
    try {
      return readEstimatedReturnTimeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeEstimatedReturnTime(DateTime value) throws UaException {
    try {
      writeEstimatedReturnTimeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends DateTime> readEstimatedReturnTimeAsync() {
    return getEstimatedReturnTimeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (DateTime) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeEstimatedReturnTimeAsync(DateTime estimatedReturnTime) {
    DataValue value = DataValue.valueOnly(new Variant(estimatedReturnTime));
    return getEstimatedReturnTimeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getEstimatedReturnTimeNode() throws UaException {
    try {
      return getEstimatedReturnTimeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getEstimatedReturnTimeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "EstimatedReturnTime",
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
  public ServerStatusDataType getServerStatus() throws UaException {
    ServerStatusTypeNode node = getServerStatusNode();
    return cast(node.getValue().getValue().getValue(), ServerStatusDataType.class);
  }

  @Override
  public void setServerStatus(ServerStatusDataType value) throws UaException {
    ServerStatusTypeNode node = getServerStatusNode();
    ExtensionObject encoded = ExtensionObject.encode(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public ServerStatusDataType readServerStatus() throws UaException {
    try {
      return readServerStatusAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeServerStatus(ServerStatusDataType value) throws UaException {
    try {
      writeServerStatusAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends ServerStatusDataType> readServerStatusAsync() {
    return getServerStatusNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), ServerStatusDataType.class));
  }

  @Override
  public CompletableFuture<StatusCode> writeServerStatusAsync(ServerStatusDataType serverStatus) {
    ExtensionObject encoded =
        ExtensionObject.encode(client.getStaticEncodingContext(), serverStatus);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getServerStatusNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public ServerStatusTypeNode getServerStatusNode() throws UaException {
    try {
      return getServerStatusNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends ServerStatusTypeNode> getServerStatusNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ServerStatus",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (ServerStatusTypeNode) node);
  }

  @Override
  public ServerCapabilitiesTypeNode getServerCapabilitiesNode() throws UaException {
    try {
      return getServerCapabilitiesNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends ServerCapabilitiesTypeNode> getServerCapabilitiesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ServerCapabilities",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (ServerCapabilitiesTypeNode) node);
  }

  @Override
  public ServerDiagnosticsTypeNode getServerDiagnosticsNode() throws UaException {
    try {
      return getServerDiagnosticsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends ServerDiagnosticsTypeNode> getServerDiagnosticsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ServerDiagnostics",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (ServerDiagnosticsTypeNode) node);
  }

  @Override
  public VendorServerInfoTypeNode getVendorServerInfoNode() throws UaException {
    try {
      return getVendorServerInfoNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends VendorServerInfoTypeNode> getVendorServerInfoNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "VendorServerInfo",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (VendorServerInfoTypeNode) node);
  }

  @Override
  public ServerRedundancyTypeNode getServerRedundancyNode() throws UaException {
    try {
      return getServerRedundancyNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends ServerRedundancyTypeNode> getServerRedundancyNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ServerRedundancy",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (ServerRedundancyTypeNode) node);
  }

  @Override
  public NamespacesTypeNode getNamespacesNode() throws UaException {
    try {
      return getNamespacesNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends NamespacesTypeNode> getNamespacesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Namespaces", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (NamespacesTypeNode) node);
  }
}
