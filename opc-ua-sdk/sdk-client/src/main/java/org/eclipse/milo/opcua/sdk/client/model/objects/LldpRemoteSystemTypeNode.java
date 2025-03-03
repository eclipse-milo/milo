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
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.ChassisIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PortIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpManagementAddressType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpSystemCapabilitiesMap;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpTlvType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class LldpRemoteSystemTypeNode extends BaseObjectTypeNode implements LldpRemoteSystemType {
  public LldpRemoteSystemTypeNode(
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
  public UInteger getTimeMark() throws UaException {
    BaseDataVariableTypeNode node = getTimeMarkNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setTimeMark(UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getTimeMarkNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readTimeMark() throws UaException {
    try {
      return readTimeMarkAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeTimeMark(UInteger value) throws UaException {
    try {
      writeTimeMarkAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readTimeMarkAsync() {
    return getTimeMarkNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeTimeMarkAsync(UInteger timeMark) {
    DataValue value = DataValue.valueOnly(new Variant(timeMark));
    return getTimeMarkNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getTimeMarkNode() throws UaException {
    try {
      return getTimeMarkNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getTimeMarkNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "TimeMark", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public UInteger getRemoteIndex() throws UaException {
    BaseDataVariableTypeNode node = getRemoteIndexNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setRemoteIndex(UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getRemoteIndexNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readRemoteIndex() throws UaException {
    try {
      return readRemoteIndexAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeRemoteIndex(UInteger value) throws UaException {
    try {
      writeRemoteIndexAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readRemoteIndexAsync() {
    return getRemoteIndexNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeRemoteIndexAsync(UInteger remoteIndex) {
    DataValue value = DataValue.valueOnly(new Variant(remoteIndex));
    return getRemoteIndexNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getRemoteIndexNode() throws UaException {
    try {
      return getRemoteIndexNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getRemoteIndexNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "RemoteIndex",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public ChassisIdSubtype getChassisIdSubtype() throws UaException {
    BaseDataVariableTypeNode node = getChassisIdSubtypeNode();
    Object value = node.getValue().getValue().getValue();

    if (value instanceof Integer) {
      return ChassisIdSubtype.from((Integer) value);
    } else if (value instanceof ChassisIdSubtype) {
      return (ChassisIdSubtype) value;
    } else {
      return null;
    }
  }

  @Override
  public void setChassisIdSubtype(ChassisIdSubtype value) throws UaException {
    BaseDataVariableTypeNode node = getChassisIdSubtypeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public ChassisIdSubtype readChassisIdSubtype() throws UaException {
    try {
      return readChassisIdSubtypeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeChassisIdSubtype(ChassisIdSubtype value) throws UaException {
    try {
      writeChassisIdSubtypeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends ChassisIdSubtype> readChassisIdSubtypeAsync() {
    return getChassisIdSubtypeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(
            v -> {
              Object value = v.getValue().getValue();
              if (value instanceof Integer) {
                return ChassisIdSubtype.from((Integer) value);
              } else {
                return null;
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeChassisIdSubtypeAsync(
      ChassisIdSubtype chassisIdSubtype) {
    DataValue value = DataValue.valueOnly(new Variant(chassisIdSubtype));
    return getChassisIdSubtypeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getChassisIdSubtypeNode() throws UaException {
    try {
      return getChassisIdSubtypeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getChassisIdSubtypeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ChassisIdSubtype",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public String getChassisId() throws UaException {
    BaseDataVariableTypeNode node = getChassisIdNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setChassisId(String value) throws UaException {
    BaseDataVariableTypeNode node = getChassisIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readChassisId() throws UaException {
    try {
      return readChassisIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeChassisId(String value) throws UaException {
    try {
      writeChassisIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readChassisIdAsync() {
    return getChassisIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeChassisIdAsync(String chassisId) {
    DataValue value = DataValue.valueOnly(new Variant(chassisId));
    return getChassisIdNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getChassisIdNode() throws UaException {
    try {
      return getChassisIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getChassisIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "ChassisId", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public PortIdSubtype getPortIdSubtype() throws UaException {
    BaseDataVariableTypeNode node = getPortIdSubtypeNode();
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
    BaseDataVariableTypeNode node = getPortIdSubtypeNode();
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
  public BaseDataVariableTypeNode getPortIdSubtypeNode() throws UaException {
    try {
      return getPortIdSubtypeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getPortIdSubtypeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "PortIdSubtype",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public String getPortId() throws UaException {
    BaseDataVariableTypeNode node = getPortIdNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setPortId(String value) throws UaException {
    BaseDataVariableTypeNode node = getPortIdNode();
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
  public BaseDataVariableTypeNode getPortIdNode() throws UaException {
    try {
      return getPortIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getPortIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "PortId", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public String getPortDescription() throws UaException {
    BaseDataVariableTypeNode node = getPortDescriptionNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setPortDescription(String value) throws UaException {
    BaseDataVariableTypeNode node = getPortDescriptionNode();
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
  public BaseDataVariableTypeNode getPortDescriptionNode() throws UaException {
    try {
      return getPortDescriptionNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getPortDescriptionNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "PortDescription",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public String getSystemName() throws UaException {
    BaseDataVariableTypeNode node = getSystemNameNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setSystemName(String value) throws UaException {
    BaseDataVariableTypeNode node = getSystemNameNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readSystemName() throws UaException {
    try {
      return readSystemNameAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSystemName(String value) throws UaException {
    try {
      writeSystemNameAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readSystemNameAsync() {
    return getSystemNameNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSystemNameAsync(String systemName) {
    DataValue value = DataValue.valueOnly(new Variant(systemName));
    return getSystemNameNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getSystemNameNode() throws UaException {
    try {
      return getSystemNameNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getSystemNameNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "SystemName", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public String getSystemDescription() throws UaException {
    BaseDataVariableTypeNode node = getSystemDescriptionNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setSystemDescription(String value) throws UaException {
    BaseDataVariableTypeNode node = getSystemDescriptionNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readSystemDescription() throws UaException {
    try {
      return readSystemDescriptionAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSystemDescription(String value) throws UaException {
    try {
      writeSystemDescriptionAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readSystemDescriptionAsync() {
    return getSystemDescriptionNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSystemDescriptionAsync(String systemDescription) {
    DataValue value = DataValue.valueOnly(new Variant(systemDescription));
    return getSystemDescriptionNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getSystemDescriptionNode() throws UaException {
    try {
      return getSystemDescriptionNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getSystemDescriptionNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SystemDescription",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public LldpSystemCapabilitiesMap getSystemCapabilitiesSupported() throws UaException {
    BaseDataVariableTypeNode node = getSystemCapabilitiesSupportedNode();
    return (LldpSystemCapabilitiesMap) node.getValue().getValue().getValue();
  }

  @Override
  public void setSystemCapabilitiesSupported(LldpSystemCapabilitiesMap value) throws UaException {
    BaseDataVariableTypeNode node = getSystemCapabilitiesSupportedNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LldpSystemCapabilitiesMap readSystemCapabilitiesSupported() throws UaException {
    try {
      return readSystemCapabilitiesSupportedAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSystemCapabilitiesSupported(LldpSystemCapabilitiesMap value) throws UaException {
    try {
      writeSystemCapabilitiesSupportedAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LldpSystemCapabilitiesMap>
      readSystemCapabilitiesSupportedAsync() {
    return getSystemCapabilitiesSupportedNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LldpSystemCapabilitiesMap) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSystemCapabilitiesSupportedAsync(
      LldpSystemCapabilitiesMap systemCapabilitiesSupported) {
    DataValue value = DataValue.valueOnly(new Variant(systemCapabilitiesSupported));
    return getSystemCapabilitiesSupportedNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getSystemCapabilitiesSupportedNode() throws UaException {
    try {
      return getSystemCapabilitiesSupportedNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getSystemCapabilitiesSupportedNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SystemCapabilitiesSupported",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public LldpSystemCapabilitiesMap getSystemCapabilitiesEnabled() throws UaException {
    BaseDataVariableTypeNode node = getSystemCapabilitiesEnabledNode();
    return (LldpSystemCapabilitiesMap) node.getValue().getValue().getValue();
  }

  @Override
  public void setSystemCapabilitiesEnabled(LldpSystemCapabilitiesMap value) throws UaException {
    BaseDataVariableTypeNode node = getSystemCapabilitiesEnabledNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LldpSystemCapabilitiesMap readSystemCapabilitiesEnabled() throws UaException {
    try {
      return readSystemCapabilitiesEnabledAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSystemCapabilitiesEnabled(LldpSystemCapabilitiesMap value) throws UaException {
    try {
      writeSystemCapabilitiesEnabledAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LldpSystemCapabilitiesMap>
      readSystemCapabilitiesEnabledAsync() {
    return getSystemCapabilitiesEnabledNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LldpSystemCapabilitiesMap) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSystemCapabilitiesEnabledAsync(
      LldpSystemCapabilitiesMap systemCapabilitiesEnabled) {
    DataValue value = DataValue.valueOnly(new Variant(systemCapabilitiesEnabled));
    return getSystemCapabilitiesEnabledNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getSystemCapabilitiesEnabledNode() throws UaException {
    try {
      return getSystemCapabilitiesEnabledNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getSystemCapabilitiesEnabledNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SystemCapabilitiesEnabled",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public Boolean getRemoteChanges() throws UaException {
    BaseDataVariableTypeNode node = getRemoteChangesNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setRemoteChanges(Boolean value) throws UaException {
    BaseDataVariableTypeNode node = getRemoteChangesNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readRemoteChanges() throws UaException {
    try {
      return readRemoteChangesAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeRemoteChanges(Boolean value) throws UaException {
    try {
      writeRemoteChangesAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readRemoteChangesAsync() {
    return getRemoteChangesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeRemoteChangesAsync(Boolean remoteChanges) {
    DataValue value = DataValue.valueOnly(new Variant(remoteChanges));
    return getRemoteChangesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getRemoteChangesNode() throws UaException {
    try {
      return getRemoteChangesNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getRemoteChangesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "RemoteChanges",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public Boolean getRemoteTooManyNeighbors() throws UaException {
    BaseDataVariableTypeNode node = getRemoteTooManyNeighborsNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setRemoteTooManyNeighbors(Boolean value) throws UaException {
    BaseDataVariableTypeNode node = getRemoteTooManyNeighborsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readRemoteTooManyNeighbors() throws UaException {
    try {
      return readRemoteTooManyNeighborsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeRemoteTooManyNeighbors(Boolean value) throws UaException {
    try {
      writeRemoteTooManyNeighborsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readRemoteTooManyNeighborsAsync() {
    return getRemoteTooManyNeighborsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeRemoteTooManyNeighborsAsync(
      Boolean remoteTooManyNeighbors) {
    DataValue value = DataValue.valueOnly(new Variant(remoteTooManyNeighbors));
    return getRemoteTooManyNeighborsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getRemoteTooManyNeighborsNode() throws UaException {
    try {
      return getRemoteTooManyNeighborsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getRemoteTooManyNeighborsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "RemoteTooManyNeighbors",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public LldpManagementAddressType[] getManagementAddress() throws UaException {
    BaseDataVariableTypeNode node = getManagementAddressNode();
    return cast(node.getValue().getValue().getValue(), LldpManagementAddressType[].class);
  }

  @Override
  public void setManagementAddress(LldpManagementAddressType[] value) throws UaException {
    BaseDataVariableTypeNode node = getManagementAddressNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public LldpManagementAddressType[] readManagementAddress() throws UaException {
    try {
      return readManagementAddressAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeManagementAddress(LldpManagementAddressType[] value) throws UaException {
    try {
      writeManagementAddressAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LldpManagementAddressType[]> readManagementAddressAsync() {
    return getManagementAddressNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), LldpManagementAddressType[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeManagementAddressAsync(
      LldpManagementAddressType[] managementAddress) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), managementAddress);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getManagementAddressNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getManagementAddressNode() throws UaException {
    try {
      return getManagementAddressNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getManagementAddressNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ManagementAddress",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public LldpTlvType[] getRemoteUnknownTlv() throws UaException {
    BaseDataVariableTypeNode node = getRemoteUnknownTlvNode();
    return cast(node.getValue().getValue().getValue(), LldpTlvType[].class);
  }

  @Override
  public void setRemoteUnknownTlv(LldpTlvType[] value) throws UaException {
    BaseDataVariableTypeNode node = getRemoteUnknownTlvNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public LldpTlvType[] readRemoteUnknownTlv() throws UaException {
    try {
      return readRemoteUnknownTlvAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeRemoteUnknownTlv(LldpTlvType[] value) throws UaException {
    try {
      writeRemoteUnknownTlvAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LldpTlvType[]> readRemoteUnknownTlvAsync() {
    return getRemoteUnknownTlvNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), LldpTlvType[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeRemoteUnknownTlvAsync(LldpTlvType[] remoteUnknownTlv) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), remoteUnknownTlv);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getRemoteUnknownTlvNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getRemoteUnknownTlvNode() throws UaException {
    try {
      return getRemoteUnknownTlvNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getRemoteUnknownTlvNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "RemoteUnknownTlv",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }
}
