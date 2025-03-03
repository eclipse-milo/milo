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
import org.eclipse.milo.opcua.sdk.client.model.variables.SamplingIntervalDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.ServerDiagnosticsSummaryTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.SubscriptionDiagnosticsArrayTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.SamplingIntervalDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerDiagnosticsSummaryDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;

public class ServerDiagnosticsTypeNode extends BaseObjectTypeNode implements ServerDiagnosticsType {
  public ServerDiagnosticsTypeNode(
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
  public Boolean getEnabledFlag() throws UaException {
    PropertyTypeNode node = getEnabledFlagNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setEnabledFlag(Boolean value) throws UaException {
    PropertyTypeNode node = getEnabledFlagNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readEnabledFlag() throws UaException {
    try {
      return readEnabledFlagAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeEnabledFlag(Boolean value) throws UaException {
    try {
      writeEnabledFlagAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readEnabledFlagAsync() {
    return getEnabledFlagNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeEnabledFlagAsync(Boolean enabledFlag) {
    DataValue value = DataValue.valueOnly(new Variant(enabledFlag));
    return getEnabledFlagNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getEnabledFlagNode() throws UaException {
    try {
      return getEnabledFlagNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getEnabledFlagNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "EnabledFlag",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public ServerDiagnosticsSummaryDataType getServerDiagnosticsSummary() throws UaException {
    ServerDiagnosticsSummaryTypeNode node = getServerDiagnosticsSummaryNode();
    return cast(node.getValue().getValue().getValue(), ServerDiagnosticsSummaryDataType.class);
  }

  @Override
  public void setServerDiagnosticsSummary(ServerDiagnosticsSummaryDataType value)
      throws UaException {
    ServerDiagnosticsSummaryTypeNode node = getServerDiagnosticsSummaryNode();
    ExtensionObject encoded = ExtensionObject.encode(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public ServerDiagnosticsSummaryDataType readServerDiagnosticsSummary() throws UaException {
    try {
      return readServerDiagnosticsSummaryAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeServerDiagnosticsSummary(ServerDiagnosticsSummaryDataType value)
      throws UaException {
    try {
      writeServerDiagnosticsSummaryAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends ServerDiagnosticsSummaryDataType>
      readServerDiagnosticsSummaryAsync() {
    return getServerDiagnosticsSummaryNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), ServerDiagnosticsSummaryDataType.class));
  }

  @Override
  public CompletableFuture<StatusCode> writeServerDiagnosticsSummaryAsync(
      ServerDiagnosticsSummaryDataType serverDiagnosticsSummary) {
    ExtensionObject encoded =
        ExtensionObject.encode(client.getStaticEncodingContext(), serverDiagnosticsSummary);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getServerDiagnosticsSummaryNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public ServerDiagnosticsSummaryTypeNode getServerDiagnosticsSummaryNode() throws UaException {
    try {
      return getServerDiagnosticsSummaryNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends ServerDiagnosticsSummaryTypeNode>
      getServerDiagnosticsSummaryNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ServerDiagnosticsSummary",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (ServerDiagnosticsSummaryTypeNode) node);
  }

  @Override
  public SamplingIntervalDiagnosticsDataType[] getSamplingIntervalDiagnosticsArray()
      throws UaException {
    SamplingIntervalDiagnosticsArrayTypeNode node = getSamplingIntervalDiagnosticsArrayNode();
    return cast(node.getValue().getValue().getValue(), SamplingIntervalDiagnosticsDataType[].class);
  }

  @Override
  public void setSamplingIntervalDiagnosticsArray(SamplingIntervalDiagnosticsDataType[] value)
      throws UaException {
    SamplingIntervalDiagnosticsArrayTypeNode node = getSamplingIntervalDiagnosticsArrayNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public SamplingIntervalDiagnosticsDataType[] readSamplingIntervalDiagnosticsArray()
      throws UaException {
    try {
      return readSamplingIntervalDiagnosticsArrayAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSamplingIntervalDiagnosticsArray(SamplingIntervalDiagnosticsDataType[] value)
      throws UaException {
    try {
      writeSamplingIntervalDiagnosticsArrayAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends SamplingIntervalDiagnosticsDataType[]>
      readSamplingIntervalDiagnosticsArrayAsync() {
    return getSamplingIntervalDiagnosticsArrayNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), SamplingIntervalDiagnosticsDataType[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeSamplingIntervalDiagnosticsArrayAsync(
      SamplingIntervalDiagnosticsDataType[] samplingIntervalDiagnosticsArray) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(
            client.getStaticEncodingContext(), samplingIntervalDiagnosticsArray);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getSamplingIntervalDiagnosticsArrayNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public SamplingIntervalDiagnosticsArrayTypeNode getSamplingIntervalDiagnosticsArrayNode()
      throws UaException {
    try {
      return getSamplingIntervalDiagnosticsArrayNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends SamplingIntervalDiagnosticsArrayTypeNode>
      getSamplingIntervalDiagnosticsArrayNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SamplingIntervalDiagnosticsArray",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (SamplingIntervalDiagnosticsArrayTypeNode) node);
  }

  @Override
  public SubscriptionDiagnosticsDataType[] getSubscriptionDiagnosticsArray() throws UaException {
    SubscriptionDiagnosticsArrayTypeNode node = getSubscriptionDiagnosticsArrayNode();
    return cast(node.getValue().getValue().getValue(), SubscriptionDiagnosticsDataType[].class);
  }

  @Override
  public void setSubscriptionDiagnosticsArray(SubscriptionDiagnosticsDataType[] value)
      throws UaException {
    SubscriptionDiagnosticsArrayTypeNode node = getSubscriptionDiagnosticsArrayNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public SubscriptionDiagnosticsDataType[] readSubscriptionDiagnosticsArray() throws UaException {
    try {
      return readSubscriptionDiagnosticsArrayAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSubscriptionDiagnosticsArray(SubscriptionDiagnosticsDataType[] value)
      throws UaException {
    try {
      writeSubscriptionDiagnosticsArrayAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends SubscriptionDiagnosticsDataType[]>
      readSubscriptionDiagnosticsArrayAsync() {
    return getSubscriptionDiagnosticsArrayNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), SubscriptionDiagnosticsDataType[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeSubscriptionDiagnosticsArrayAsync(
      SubscriptionDiagnosticsDataType[] subscriptionDiagnosticsArray) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(
            client.getStaticEncodingContext(), subscriptionDiagnosticsArray);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getSubscriptionDiagnosticsArrayNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public SubscriptionDiagnosticsArrayTypeNode getSubscriptionDiagnosticsArrayNode()
      throws UaException {
    try {
      return getSubscriptionDiagnosticsArrayNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends SubscriptionDiagnosticsArrayTypeNode>
      getSubscriptionDiagnosticsArrayNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SubscriptionDiagnosticsArray",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (SubscriptionDiagnosticsArrayTypeNode) node);
  }

  @Override
  public SessionsDiagnosticsSummaryTypeNode getSessionsDiagnosticsSummaryNode() throws UaException {
    try {
      return getSessionsDiagnosticsSummaryNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends SessionsDiagnosticsSummaryTypeNode>
      getSessionsDiagnosticsSummaryNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SessionsDiagnosticsSummary",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (SessionsDiagnosticsSummaryTypeNode) node);
  }
}
