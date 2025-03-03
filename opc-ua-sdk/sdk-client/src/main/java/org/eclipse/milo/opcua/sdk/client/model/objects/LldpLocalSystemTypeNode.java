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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ChassisIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpSystemCapabilitiesMap;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class LldpLocalSystemTypeNode extends BaseObjectTypeNode implements LldpLocalSystemType {
  public LldpLocalSystemTypeNode(
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
  public ChassisIdSubtype getChassisIdSubtype() throws UaException {
    PropertyTypeNode node = getChassisIdSubtypeNode();
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
    PropertyTypeNode node = getChassisIdSubtypeNode();
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
  public PropertyTypeNode getChassisIdSubtypeNode() throws UaException {
    try {
      return getChassisIdSubtypeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getChassisIdSubtypeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ChassisIdSubtype",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getChassisId() throws UaException {
    PropertyTypeNode node = getChassisIdNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setChassisId(String value) throws UaException {
    PropertyTypeNode node = getChassisIdNode();
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
  public PropertyTypeNode getChassisIdNode() throws UaException {
    try {
      return getChassisIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getChassisIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "ChassisId", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getSystemName() throws UaException {
    PropertyTypeNode node = getSystemNameNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setSystemName(String value) throws UaException {
    PropertyTypeNode node = getSystemNameNode();
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
  public PropertyTypeNode getSystemNameNode() throws UaException {
    try {
      return getSystemNameNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSystemNameNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "SystemName", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getSystemDescription() throws UaException {
    PropertyTypeNode node = getSystemDescriptionNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setSystemDescription(String value) throws UaException {
    PropertyTypeNode node = getSystemDescriptionNode();
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
  public PropertyTypeNode getSystemDescriptionNode() throws UaException {
    try {
      return getSystemDescriptionNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSystemDescriptionNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SystemDescription",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public LldpSystemCapabilitiesMap getSystemCapabilitiesSupported() throws UaException {
    PropertyTypeNode node = getSystemCapabilitiesSupportedNode();
    return (LldpSystemCapabilitiesMap) node.getValue().getValue().getValue();
  }

  @Override
  public void setSystemCapabilitiesSupported(LldpSystemCapabilitiesMap value) throws UaException {
    PropertyTypeNode node = getSystemCapabilitiesSupportedNode();
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
  public PropertyTypeNode getSystemCapabilitiesSupportedNode() throws UaException {
    try {
      return getSystemCapabilitiesSupportedNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSystemCapabilitiesSupportedNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SystemCapabilitiesSupported",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public LldpSystemCapabilitiesMap getSystemCapabilitiesEnabled() throws UaException {
    PropertyTypeNode node = getSystemCapabilitiesEnabledNode();
    return (LldpSystemCapabilitiesMap) node.getValue().getValue().getValue();
  }

  @Override
  public void setSystemCapabilitiesEnabled(LldpSystemCapabilitiesMap value) throws UaException {
    PropertyTypeNode node = getSystemCapabilitiesEnabledNode();
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
  public PropertyTypeNode getSystemCapabilitiesEnabledNode() throws UaException {
    try {
      return getSystemCapabilitiesEnabledNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSystemCapabilitiesEnabledNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SystemCapabilitiesEnabled",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
