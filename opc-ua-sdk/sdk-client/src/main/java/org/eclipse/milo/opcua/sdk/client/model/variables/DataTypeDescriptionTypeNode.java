/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class DataTypeDescriptionTypeNode extends BaseDataVariableTypeNode
    implements DataTypeDescriptionType {
  public DataTypeDescriptionTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger[] arrayDimensions,
      UByte accessLevel,
      UByte userAccessLevel,
      Double minimumSamplingInterval,
      Boolean historizing,
      AccessLevelExType accessLevelEx) {
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
        value,
        dataType,
        valueRank,
        arrayDimensions,
        accessLevel,
        userAccessLevel,
        minimumSamplingInterval,
        historizing,
        accessLevelEx);
  }

  @Override
  public String getDataTypeVersion() throws UaException {
    PropertyTypeNode node = getDataTypeVersionNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setDataTypeVersion(String value) throws UaException {
    PropertyTypeNode node = getDataTypeVersionNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readDataTypeVersion() throws UaException {
    try {
      return readDataTypeVersionAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDataTypeVersion(String value) throws UaException {
    try {
      writeDataTypeVersionAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readDataTypeVersionAsync() {
    return getDataTypeVersionNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeDataTypeVersionAsync(String dataTypeVersion) {
    DataValue value = DataValue.valueOnly(new Variant(dataTypeVersion));
    return getDataTypeVersionNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDataTypeVersionNode() throws UaException {
    try {
      return getDataTypeVersionNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataTypeVersionNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DataTypeVersion",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public ByteString getDictionaryFragment() throws UaException {
    PropertyTypeNode node = getDictionaryFragmentNode();
    return (ByteString) node.getValue().getValue().getValue();
  }

  @Override
  public void setDictionaryFragment(ByteString value) throws UaException {
    PropertyTypeNode node = getDictionaryFragmentNode();
    node.setValue(new Variant(value));
  }

  @Override
  public ByteString readDictionaryFragment() throws UaException {
    try {
      return readDictionaryFragmentAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDictionaryFragment(ByteString value) throws UaException {
    try {
      writeDictionaryFragmentAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends ByteString> readDictionaryFragmentAsync() {
    return getDictionaryFragmentNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (ByteString) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeDictionaryFragmentAsync(ByteString dictionaryFragment) {
    DataValue value = DataValue.valueOnly(new Variant(dictionaryFragment));
    return getDictionaryFragmentNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDictionaryFragmentNode() throws UaException {
    try {
      return getDictionaryFragmentNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDictionaryFragmentNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DictionaryFragment",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
