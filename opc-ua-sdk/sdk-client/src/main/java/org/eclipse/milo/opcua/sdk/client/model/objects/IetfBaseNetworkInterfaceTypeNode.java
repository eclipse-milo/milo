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
import org.eclipse.milo.opcua.sdk.client.model.variables.AnalogUnitTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceAdminStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceOperStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class IetfBaseNetworkInterfaceTypeNode extends BaseObjectTypeNode
    implements IetfBaseNetworkInterfaceType {
  public IetfBaseNetworkInterfaceTypeNode(
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
  public InterfaceAdminStatus getAdminStatus() throws UaException {
    BaseDataVariableTypeNode node = getAdminStatusNode();
    Object value = node.getValue().getValue().getValue();

    if (value instanceof Integer) {
      return InterfaceAdminStatus.from((Integer) value);
    } else if (value instanceof InterfaceAdminStatus) {
      return (InterfaceAdminStatus) value;
    } else {
      return null;
    }
  }

  @Override
  public void setAdminStatus(InterfaceAdminStatus value) throws UaException {
    BaseDataVariableTypeNode node = getAdminStatusNode();
    node.setValue(new Variant(value));
  }

  @Override
  public InterfaceAdminStatus readAdminStatus() throws UaException {
    try {
      return readAdminStatusAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeAdminStatus(InterfaceAdminStatus value) throws UaException {
    try {
      writeAdminStatusAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends InterfaceAdminStatus> readAdminStatusAsync() {
    return getAdminStatusNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(
            v -> {
              Object value = v.getValue().getValue();
              if (value instanceof Integer) {
                return InterfaceAdminStatus.from((Integer) value);
              } else {
                return null;
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeAdminStatusAsync(InterfaceAdminStatus adminStatus) {
    DataValue value = DataValue.valueOnly(new Variant(adminStatus));
    return getAdminStatusNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getAdminStatusNode() throws UaException {
    try {
      return getAdminStatusNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getAdminStatusNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "AdminStatus",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public InterfaceOperStatus getOperStatus() throws UaException {
    BaseDataVariableTypeNode node = getOperStatusNode();
    Object value = node.getValue().getValue().getValue();

    if (value instanceof Integer) {
      return InterfaceOperStatus.from((Integer) value);
    } else if (value instanceof InterfaceOperStatus) {
      return (InterfaceOperStatus) value;
    } else {
      return null;
    }
  }

  @Override
  public void setOperStatus(InterfaceOperStatus value) throws UaException {
    BaseDataVariableTypeNode node = getOperStatusNode();
    node.setValue(new Variant(value));
  }

  @Override
  public InterfaceOperStatus readOperStatus() throws UaException {
    try {
      return readOperStatusAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeOperStatus(InterfaceOperStatus value) throws UaException {
    try {
      writeOperStatusAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends InterfaceOperStatus> readOperStatusAsync() {
    return getOperStatusNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(
            v -> {
              Object value = v.getValue().getValue();
              if (value instanceof Integer) {
                return InterfaceOperStatus.from((Integer) value);
              } else {
                return null;
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeOperStatusAsync(InterfaceOperStatus operStatus) {
    DataValue value = DataValue.valueOnly(new Variant(operStatus));
    return getOperStatusNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getOperStatusNode() throws UaException {
    try {
      return getOperStatusNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getOperStatusNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "OperStatus", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public String getPhysAddress() throws UaException {
    BaseDataVariableTypeNode node = getPhysAddressNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setPhysAddress(String value) throws UaException {
    BaseDataVariableTypeNode node = getPhysAddressNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readPhysAddress() throws UaException {
    try {
      return readPhysAddressAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writePhysAddress(String value) throws UaException {
    try {
      writePhysAddressAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readPhysAddressAsync() {
    return getPhysAddressNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writePhysAddressAsync(String physAddress) {
    DataValue value = DataValue.valueOnly(new Variant(physAddress));
    return getPhysAddressNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getPhysAddressNode() throws UaException {
    try {
      return getPhysAddressNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getPhysAddressNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "PhysAddress",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public ULong getSpeed() throws UaException {
    AnalogUnitTypeNode node = getSpeedNode();
    return (ULong) node.getValue().getValue().getValue();
  }

  @Override
  public void setSpeed(ULong value) throws UaException {
    AnalogUnitTypeNode node = getSpeedNode();
    node.setValue(new Variant(value));
  }

  @Override
  public ULong readSpeed() throws UaException {
    try {
      return readSpeedAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSpeed(ULong value) throws UaException {
    try {
      writeSpeedAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends ULong> readSpeedAsync() {
    return getSpeedNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (ULong) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSpeedAsync(ULong speed) {
    DataValue value = DataValue.valueOnly(new Variant(speed));
    return getSpeedNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public AnalogUnitTypeNode getSpeedNode() throws UaException {
    try {
      return getSpeedNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends AnalogUnitTypeNode> getSpeedNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Speed", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (AnalogUnitTypeNode) node);
  }
}
