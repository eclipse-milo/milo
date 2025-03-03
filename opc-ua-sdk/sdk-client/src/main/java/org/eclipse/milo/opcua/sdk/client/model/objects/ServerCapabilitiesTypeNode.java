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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;

public class ServerCapabilitiesTypeNode extends BaseObjectTypeNode
    implements ServerCapabilitiesType {
  public ServerCapabilitiesTypeNode(
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
  public String[] getServerProfileArray() throws UaException {
    PropertyTypeNode node = getServerProfileArrayNode();
    return (String[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setServerProfileArray(String[] value) throws UaException {
    PropertyTypeNode node = getServerProfileArrayNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String[] readServerProfileArray() throws UaException {
    try {
      return readServerProfileArrayAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeServerProfileArray(String[] value) throws UaException {
    try {
      writeServerProfileArrayAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String[]> readServerProfileArrayAsync() {
    return getServerProfileArrayNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeServerProfileArrayAsync(String[] serverProfileArray) {
    DataValue value = DataValue.valueOnly(new Variant(serverProfileArray));
    return getServerProfileArrayNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getServerProfileArrayNode() throws UaException {
    try {
      return getServerProfileArrayNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getServerProfileArrayNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ServerProfileArray",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String[] getLocaleIdArray() throws UaException {
    PropertyTypeNode node = getLocaleIdArrayNode();
    return (String[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setLocaleIdArray(String[] value) throws UaException {
    PropertyTypeNode node = getLocaleIdArrayNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String[] readLocaleIdArray() throws UaException {
    try {
      return readLocaleIdArrayAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLocaleIdArray(String[] value) throws UaException {
    try {
      writeLocaleIdArrayAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String[]> readLocaleIdArrayAsync() {
    return getLocaleIdArrayNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLocaleIdArrayAsync(String[] localeIdArray) {
    DataValue value = DataValue.valueOnly(new Variant(localeIdArray));
    return getLocaleIdArrayNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getLocaleIdArrayNode() throws UaException {
    try {
      return getLocaleIdArrayNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLocaleIdArrayNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LocaleIdArray",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getMinSupportedSampleRate() throws UaException {
    PropertyTypeNode node = getMinSupportedSampleRateNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setMinSupportedSampleRate(Double value) throws UaException {
    PropertyTypeNode node = getMinSupportedSampleRateNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readMinSupportedSampleRate() throws UaException {
    try {
      return readMinSupportedSampleRateAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMinSupportedSampleRate(Double value) throws UaException {
    try {
      writeMinSupportedSampleRateAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readMinSupportedSampleRateAsync() {
    return getMinSupportedSampleRateNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMinSupportedSampleRateAsync(
      Double minSupportedSampleRate) {
    DataValue value = DataValue.valueOnly(new Variant(minSupportedSampleRate));
    return getMinSupportedSampleRateNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMinSupportedSampleRateNode() throws UaException {
    try {
      return getMinSupportedSampleRateNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMinSupportedSampleRateNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MinSupportedSampleRate",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UShort getMaxBrowseContinuationPoints() throws UaException {
    PropertyTypeNode node = getMaxBrowseContinuationPointsNode();
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxBrowseContinuationPoints(UShort value) throws UaException {
    PropertyTypeNode node = getMaxBrowseContinuationPointsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UShort readMaxBrowseContinuationPoints() throws UaException {
    try {
      return readMaxBrowseContinuationPointsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxBrowseContinuationPoints(UShort value) throws UaException {
    try {
      writeMaxBrowseContinuationPointsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UShort> readMaxBrowseContinuationPointsAsync() {
    return getMaxBrowseContinuationPointsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UShort) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxBrowseContinuationPointsAsync(
      UShort maxBrowseContinuationPoints) {
    DataValue value = DataValue.valueOnly(new Variant(maxBrowseContinuationPoints));
    return getMaxBrowseContinuationPointsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxBrowseContinuationPointsNode() throws UaException {
    try {
      return getMaxBrowseContinuationPointsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxBrowseContinuationPointsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxBrowseContinuationPoints",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UShort getMaxQueryContinuationPoints() throws UaException {
    PropertyTypeNode node = getMaxQueryContinuationPointsNode();
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxQueryContinuationPoints(UShort value) throws UaException {
    PropertyTypeNode node = getMaxQueryContinuationPointsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UShort readMaxQueryContinuationPoints() throws UaException {
    try {
      return readMaxQueryContinuationPointsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxQueryContinuationPoints(UShort value) throws UaException {
    try {
      writeMaxQueryContinuationPointsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UShort> readMaxQueryContinuationPointsAsync() {
    return getMaxQueryContinuationPointsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UShort) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxQueryContinuationPointsAsync(
      UShort maxQueryContinuationPoints) {
    DataValue value = DataValue.valueOnly(new Variant(maxQueryContinuationPoints));
    return getMaxQueryContinuationPointsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxQueryContinuationPointsNode() throws UaException {
    try {
      return getMaxQueryContinuationPointsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxQueryContinuationPointsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxQueryContinuationPoints",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UShort getMaxHistoryContinuationPoints() throws UaException {
    PropertyTypeNode node = getMaxHistoryContinuationPointsNode();
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxHistoryContinuationPoints(UShort value) throws UaException {
    PropertyTypeNode node = getMaxHistoryContinuationPointsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UShort readMaxHistoryContinuationPoints() throws UaException {
    try {
      return readMaxHistoryContinuationPointsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxHistoryContinuationPoints(UShort value) throws UaException {
    try {
      writeMaxHistoryContinuationPointsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UShort> readMaxHistoryContinuationPointsAsync() {
    return getMaxHistoryContinuationPointsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UShort) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxHistoryContinuationPointsAsync(
      UShort maxHistoryContinuationPoints) {
    DataValue value = DataValue.valueOnly(new Variant(maxHistoryContinuationPoints));
    return getMaxHistoryContinuationPointsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxHistoryContinuationPointsNode() throws UaException {
    try {
      return getMaxHistoryContinuationPointsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxHistoryContinuationPointsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxHistoryContinuationPoints",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public SignedSoftwareCertificate[] getSoftwareCertificates() throws UaException {
    PropertyTypeNode node = getSoftwareCertificatesNode();
    return cast(node.getValue().getValue().getValue(), SignedSoftwareCertificate[].class);
  }

  @Override
  public void setSoftwareCertificates(SignedSoftwareCertificate[] value) throws UaException {
    PropertyTypeNode node = getSoftwareCertificatesNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public SignedSoftwareCertificate[] readSoftwareCertificates() throws UaException {
    try {
      return readSoftwareCertificatesAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSoftwareCertificates(SignedSoftwareCertificate[] value) throws UaException {
    try {
      writeSoftwareCertificatesAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends SignedSoftwareCertificate[]> readSoftwareCertificatesAsync() {
    return getSoftwareCertificatesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), SignedSoftwareCertificate[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeSoftwareCertificatesAsync(
      SignedSoftwareCertificate[] softwareCertificates) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), softwareCertificates);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getSoftwareCertificatesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getSoftwareCertificatesNode() throws UaException {
    try {
      return getSoftwareCertificatesNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSoftwareCertificatesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SoftwareCertificates",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxArrayLength() throws UaException {
    PropertyTypeNode node = getMaxArrayLengthNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxArrayLength(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxArrayLengthNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxArrayLength() throws UaException {
    try {
      return readMaxArrayLengthAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxArrayLength(UInteger value) throws UaException {
    try {
      writeMaxArrayLengthAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxArrayLengthAsync() {
    return getMaxArrayLengthNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxArrayLengthAsync(UInteger maxArrayLength) {
    DataValue value = DataValue.valueOnly(new Variant(maxArrayLength));
    return getMaxArrayLengthNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxArrayLengthNode() throws UaException {
    try {
      return getMaxArrayLengthNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxArrayLengthNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxArrayLength",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxStringLength() throws UaException {
    PropertyTypeNode node = getMaxStringLengthNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxStringLength(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxStringLengthNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxStringLength() throws UaException {
    try {
      return readMaxStringLengthAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxStringLength(UInteger value) throws UaException {
    try {
      writeMaxStringLengthAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxStringLengthAsync() {
    return getMaxStringLengthNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxStringLengthAsync(UInteger maxStringLength) {
    DataValue value = DataValue.valueOnly(new Variant(maxStringLength));
    return getMaxStringLengthNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxStringLengthNode() throws UaException {
    try {
      return getMaxStringLengthNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxStringLengthNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxStringLength",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxByteStringLength() throws UaException {
    PropertyTypeNode node = getMaxByteStringLengthNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxByteStringLength(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxByteStringLengthNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxByteStringLength() throws UaException {
    try {
      return readMaxByteStringLengthAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxByteStringLength(UInteger value) throws UaException {
    try {
      writeMaxByteStringLengthAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxByteStringLengthAsync() {
    return getMaxByteStringLengthNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxByteStringLengthAsync(UInteger maxByteStringLength) {
    DataValue value = DataValue.valueOnly(new Variant(maxByteStringLength));
    return getMaxByteStringLengthNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxByteStringLengthNode() throws UaException {
    try {
      return getMaxByteStringLengthNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxByteStringLengthNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxByteStringLength",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxSessions() throws UaException {
    PropertyTypeNode node = getMaxSessionsNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxSessions(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxSessionsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxSessions() throws UaException {
    try {
      return readMaxSessionsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxSessions(UInteger value) throws UaException {
    try {
      writeMaxSessionsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxSessionsAsync() {
    return getMaxSessionsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxSessionsAsync(UInteger maxSessions) {
    DataValue value = DataValue.valueOnly(new Variant(maxSessions));
    return getMaxSessionsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxSessionsNode() throws UaException {
    try {
      return getMaxSessionsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxSessionsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxSessions",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxSubscriptions() throws UaException {
    PropertyTypeNode node = getMaxSubscriptionsNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxSubscriptions(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxSubscriptionsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxSubscriptions() throws UaException {
    try {
      return readMaxSubscriptionsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxSubscriptions(UInteger value) throws UaException {
    try {
      writeMaxSubscriptionsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxSubscriptionsAsync() {
    return getMaxSubscriptionsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxSubscriptionsAsync(UInteger maxSubscriptions) {
    DataValue value = DataValue.valueOnly(new Variant(maxSubscriptions));
    return getMaxSubscriptionsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxSubscriptionsNode() throws UaException {
    try {
      return getMaxSubscriptionsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxSubscriptionsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxSubscriptions",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxMonitoredItems() throws UaException {
    PropertyTypeNode node = getMaxMonitoredItemsNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxMonitoredItems(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxMonitoredItemsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxMonitoredItems() throws UaException {
    try {
      return readMaxMonitoredItemsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxMonitoredItems(UInteger value) throws UaException {
    try {
      writeMaxMonitoredItemsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxMonitoredItemsAsync() {
    return getMaxMonitoredItemsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxMonitoredItemsAsync(UInteger maxMonitoredItems) {
    DataValue value = DataValue.valueOnly(new Variant(maxMonitoredItems));
    return getMaxMonitoredItemsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxMonitoredItemsNode() throws UaException {
    try {
      return getMaxMonitoredItemsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxMonitoredItemsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxMonitoredItems",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxSubscriptionsPerSession() throws UaException {
    PropertyTypeNode node = getMaxSubscriptionsPerSessionNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxSubscriptionsPerSession(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxSubscriptionsPerSessionNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxSubscriptionsPerSession() throws UaException {
    try {
      return readMaxSubscriptionsPerSessionAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxSubscriptionsPerSession(UInteger value) throws UaException {
    try {
      writeMaxSubscriptionsPerSessionAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxSubscriptionsPerSessionAsync() {
    return getMaxSubscriptionsPerSessionNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxSubscriptionsPerSessionAsync(
      UInteger maxSubscriptionsPerSession) {
    DataValue value = DataValue.valueOnly(new Variant(maxSubscriptionsPerSession));
    return getMaxSubscriptionsPerSessionNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxSubscriptionsPerSessionNode() throws UaException {
    try {
      return getMaxSubscriptionsPerSessionNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxSubscriptionsPerSessionNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxSubscriptionsPerSession",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxMonitoredItemsPerSubscription() throws UaException {
    PropertyTypeNode node = getMaxMonitoredItemsPerSubscriptionNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxMonitoredItemsPerSubscription(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxMonitoredItemsPerSubscriptionNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxMonitoredItemsPerSubscription() throws UaException {
    try {
      return readMaxMonitoredItemsPerSubscriptionAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxMonitoredItemsPerSubscription(UInteger value) throws UaException {
    try {
      writeMaxMonitoredItemsPerSubscriptionAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxMonitoredItemsPerSubscriptionAsync() {
    return getMaxMonitoredItemsPerSubscriptionNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxMonitoredItemsPerSubscriptionAsync(
      UInteger maxMonitoredItemsPerSubscription) {
    DataValue value = DataValue.valueOnly(new Variant(maxMonitoredItemsPerSubscription));
    return getMaxMonitoredItemsPerSubscriptionNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxMonitoredItemsPerSubscriptionNode() throws UaException {
    try {
      return getMaxMonitoredItemsPerSubscriptionNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode>
      getMaxMonitoredItemsPerSubscriptionNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxMonitoredItemsPerSubscription",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxSelectClauseParameters() throws UaException {
    PropertyTypeNode node = getMaxSelectClauseParametersNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxSelectClauseParameters(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxSelectClauseParametersNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxSelectClauseParameters() throws UaException {
    try {
      return readMaxSelectClauseParametersAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxSelectClauseParameters(UInteger value) throws UaException {
    try {
      writeMaxSelectClauseParametersAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxSelectClauseParametersAsync() {
    return getMaxSelectClauseParametersNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxSelectClauseParametersAsync(
      UInteger maxSelectClauseParameters) {
    DataValue value = DataValue.valueOnly(new Variant(maxSelectClauseParameters));
    return getMaxSelectClauseParametersNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxSelectClauseParametersNode() throws UaException {
    try {
      return getMaxSelectClauseParametersNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxSelectClauseParametersNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxSelectClauseParameters",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxWhereClauseParameters() throws UaException {
    PropertyTypeNode node = getMaxWhereClauseParametersNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxWhereClauseParameters(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxWhereClauseParametersNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxWhereClauseParameters() throws UaException {
    try {
      return readMaxWhereClauseParametersAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxWhereClauseParameters(UInteger value) throws UaException {
    try {
      writeMaxWhereClauseParametersAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxWhereClauseParametersAsync() {
    return getMaxWhereClauseParametersNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxWhereClauseParametersAsync(
      UInteger maxWhereClauseParameters) {
    DataValue value = DataValue.valueOnly(new Variant(maxWhereClauseParameters));
    return getMaxWhereClauseParametersNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxWhereClauseParametersNode() throws UaException {
    try {
      return getMaxWhereClauseParametersNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxWhereClauseParametersNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxWhereClauseParameters",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxMonitoredItemsQueueSize() throws UaException {
    PropertyTypeNode node = getMaxMonitoredItemsQueueSizeNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxMonitoredItemsQueueSize(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxMonitoredItemsQueueSizeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxMonitoredItemsQueueSize() throws UaException {
    try {
      return readMaxMonitoredItemsQueueSizeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxMonitoredItemsQueueSize(UInteger value) throws UaException {
    try {
      writeMaxMonitoredItemsQueueSizeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxMonitoredItemsQueueSizeAsync() {
    return getMaxMonitoredItemsQueueSizeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxMonitoredItemsQueueSizeAsync(
      UInteger maxMonitoredItemsQueueSize) {
    DataValue value = DataValue.valueOnly(new Variant(maxMonitoredItemsQueueSize));
    return getMaxMonitoredItemsQueueSizeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxMonitoredItemsQueueSizeNode() throws UaException {
    try {
      return getMaxMonitoredItemsQueueSizeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxMonitoredItemsQueueSizeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxMonitoredItemsQueueSize",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public QualifiedName[] getConformanceUnits() throws UaException {
    PropertyTypeNode node = getConformanceUnitsNode();
    return (QualifiedName[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setConformanceUnits(QualifiedName[] value) throws UaException {
    PropertyTypeNode node = getConformanceUnitsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public QualifiedName[] readConformanceUnits() throws UaException {
    try {
      return readConformanceUnitsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeConformanceUnits(QualifiedName[] value) throws UaException {
    try {
      writeConformanceUnitsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends QualifiedName[]> readConformanceUnitsAsync() {
    return getConformanceUnitsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (QualifiedName[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeConformanceUnitsAsync(
      QualifiedName[] conformanceUnits) {
    DataValue value = DataValue.valueOnly(new Variant(conformanceUnits));
    return getConformanceUnitsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getConformanceUnitsNode() throws UaException {
    try {
      return getConformanceUnitsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getConformanceUnitsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ConformanceUnits",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public OperationLimitsTypeNode getOperationLimitsNode() throws UaException {
    try {
      return getOperationLimitsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends OperationLimitsTypeNode> getOperationLimitsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "OperationLimits",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (OperationLimitsTypeNode) node);
  }

  @Override
  public FolderTypeNode getModellingRulesNode() throws UaException {
    try {
      return getModellingRulesNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends FolderTypeNode> getModellingRulesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ModellingRules",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (FolderTypeNode) node);
  }

  @Override
  public FolderTypeNode getAggregateFunctionsNode() throws UaException {
    try {
      return getAggregateFunctionsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends FolderTypeNode> getAggregateFunctionsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "AggregateFunctions",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (FolderTypeNode) node);
  }

  @Override
  public RoleSetTypeNode getRoleSetNode() throws UaException {
    try {
      return getRoleSetNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends RoleSetTypeNode> getRoleSetNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "RoleSet", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (RoleSetTypeNode) node);
  }
}
