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
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.PortIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpManagementAddressTxPortType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class LldpPortInformationTypeNode extends BaseObjectTypeNode
    implements LldpPortInformationType {
  public LldpPortInformationTypeNode(
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
  public String getIetfBaseNetworkInterfaceName() throws UaException {
    PropertyTypeNode node = getIetfBaseNetworkInterfaceNameNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setIetfBaseNetworkInterfaceName(String value) throws UaException {
    PropertyTypeNode node = getIetfBaseNetworkInterfaceNameNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readIetfBaseNetworkInterfaceName() throws UaException {
    try {
      return readIetfBaseNetworkInterfaceNameAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeIetfBaseNetworkInterfaceName(String value) throws UaException {
    try {
      writeIetfBaseNetworkInterfaceNameAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readIetfBaseNetworkInterfaceNameAsync() {
    return getIetfBaseNetworkInterfaceNameNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeIetfBaseNetworkInterfaceNameAsync(
      String ietfBaseNetworkInterfaceName) {
    DataValue value = DataValue.valueOnly(new Variant(ietfBaseNetworkInterfaceName));
    return getIetfBaseNetworkInterfaceNameNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getIetfBaseNetworkInterfaceNameNode() throws UaException {
    try {
      return getIetfBaseNetworkInterfaceNameNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getIetfBaseNetworkInterfaceNameNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "IetfBaseNetworkInterfaceName",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UByte[] getDestMacAddress() throws UaException {
    PropertyTypeNode node = getDestMacAddressNode();
    return (UByte[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setDestMacAddress(UByte[] value) throws UaException {
    PropertyTypeNode node = getDestMacAddressNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UByte[] readDestMacAddress() throws UaException {
    try {
      return readDestMacAddressAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDestMacAddress(UByte[] value) throws UaException {
    try {
      writeDestMacAddressAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UByte[]> readDestMacAddressAsync() {
    return getDestMacAddressNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UByte[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeDestMacAddressAsync(UByte[] destMacAddress) {
    DataValue value = DataValue.valueOnly(new Variant(destMacAddress));
    return getDestMacAddressNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDestMacAddressNode() throws UaException {
    try {
      return getDestMacAddressNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDestMacAddressNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DestMacAddress",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public PortIdSubtype getPortIdSubtype() throws UaException {
    PropertyTypeNode node = getPortIdSubtypeNode();
    Object value = node.getValue().getValue().getValue();

    if (value instanceof Integer) {
      return PortIdSubtype.from((Integer) value);
    } else if (value instanceof PortIdSubtype) {
      return (PortIdSubtype) value;
    } else {
      return null;
    }
  }

  @Override
  public void setPortIdSubtype(PortIdSubtype value) throws UaException {
    PropertyTypeNode node = getPortIdSubtypeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public PortIdSubtype readPortIdSubtype() throws UaException {
    try {
      return readPortIdSubtypeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writePortIdSubtype(PortIdSubtype value) throws UaException {
    try {
      writePortIdSubtypeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends PortIdSubtype> readPortIdSubtypeAsync() {
    return getPortIdSubtypeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(
            v -> {
              Object value = v.getValue().getValue();
              if (value instanceof Integer) {
                return PortIdSubtype.from((Integer) value);
              } else {
                return null;
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writePortIdSubtypeAsync(PortIdSubtype portIdSubtype) {
    DataValue value = DataValue.valueOnly(new Variant(portIdSubtype));
    return getPortIdSubtypeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getPortIdSubtypeNode() throws UaException {
    try {
      return getPortIdSubtypeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPortIdSubtypeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "PortIdSubtype",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getPortId() throws UaException {
    PropertyTypeNode node = getPortIdNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setPortId(String value) throws UaException {
    PropertyTypeNode node = getPortIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readPortId() throws UaException {
    try {
      return readPortIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writePortId(String value) throws UaException {
    try {
      writePortIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readPortIdAsync() {
    return getPortIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writePortIdAsync(String portId) {
    DataValue value = DataValue.valueOnly(new Variant(portId));
    return getPortIdNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getPortIdNode() throws UaException {
    try {
      return getPortIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPortIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "PortId", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getPortDescription() throws UaException {
    PropertyTypeNode node = getPortDescriptionNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setPortDescription(String value) throws UaException {
    PropertyTypeNode node = getPortDescriptionNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readPortDescription() throws UaException {
    try {
      return readPortDescriptionAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writePortDescription(String value) throws UaException {
    try {
      writePortDescriptionAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readPortDescriptionAsync() {
    return getPortDescriptionNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writePortDescriptionAsync(String portDescription) {
    DataValue value = DataValue.valueOnly(new Variant(portDescription));
    return getPortDescriptionNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getPortDescriptionNode() throws UaException {
    try {
      return getPortDescriptionNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPortDescriptionNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "PortDescription",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public LldpManagementAddressTxPortType[] getManagementAddressTxPort() throws UaException {
    PropertyTypeNode node = getManagementAddressTxPortNode();
    return cast(node.getValue().getValue().getValue(), LldpManagementAddressTxPortType[].class);
  }

  @Override
  public void setManagementAddressTxPort(LldpManagementAddressTxPortType[] value)
      throws UaException {
    PropertyTypeNode node = getManagementAddressTxPortNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public LldpManagementAddressTxPortType[] readManagementAddressTxPort() throws UaException {
    try {
      return readManagementAddressTxPortAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeManagementAddressTxPort(LldpManagementAddressTxPortType[] value)
      throws UaException {
    try {
      writeManagementAddressTxPortAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LldpManagementAddressTxPortType[]>
      readManagementAddressTxPortAsync() {
    return getManagementAddressTxPortNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), LldpManagementAddressTxPortType[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeManagementAddressTxPortAsync(
      LldpManagementAddressTxPortType[] managementAddressTxPort) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), managementAddressTxPort);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getManagementAddressTxPortNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getManagementAddressTxPortNode() throws UaException {
    try {
      return getManagementAddressTxPortNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getManagementAddressTxPortNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ManagementAddressTxPort",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public FolderTypeNode getRemoteSystemsDataNode() throws UaException {
    try {
      return getRemoteSystemsDataNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends FolderTypeNode> getRemoteSystemsDataNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "RemoteSystemsData",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (FolderTypeNode) node);
  }
}
