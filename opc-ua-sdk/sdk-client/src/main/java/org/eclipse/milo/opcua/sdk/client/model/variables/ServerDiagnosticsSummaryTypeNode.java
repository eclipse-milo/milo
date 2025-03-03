/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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

public class ServerDiagnosticsSummaryTypeNode extends BaseDataVariableTypeNode
    implements ServerDiagnosticsSummaryType {
  public ServerDiagnosticsSummaryTypeNode(
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
  public UInteger getServerViewCount() throws UaException {
    BaseDataVariableTypeNode node = getServerViewCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setServerViewCount(UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getServerViewCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readServerViewCount() throws UaException {
    try {
      return readServerViewCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeServerViewCount(UInteger value) throws UaException {
    try {
      writeServerViewCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readServerViewCountAsync() {
    return getServerViewCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeServerViewCountAsync(UInteger serverViewCount) {
    DataValue value = DataValue.valueOnly(new Variant(serverViewCount));
    return getServerViewCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getServerViewCountNode() throws UaException {
    try {
      return getServerViewCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getServerViewCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ServerViewCount",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public UInteger getCurrentSessionCount() throws UaException {
    BaseDataVariableTypeNode node = getCurrentSessionCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setCurrentSessionCount(UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getCurrentSessionCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readCurrentSessionCount() throws UaException {
    try {
      return readCurrentSessionCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeCurrentSessionCount(UInteger value) throws UaException {
    try {
      writeCurrentSessionCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readCurrentSessionCountAsync() {
    return getCurrentSessionCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeCurrentSessionCountAsync(UInteger currentSessionCount) {
    DataValue value = DataValue.valueOnly(new Variant(currentSessionCount));
    return getCurrentSessionCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getCurrentSessionCountNode() throws UaException {
    try {
      return getCurrentSessionCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getCurrentSessionCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "CurrentSessionCount",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public UInteger getCumulatedSessionCount() throws UaException {
    BaseDataVariableTypeNode node = getCumulatedSessionCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setCumulatedSessionCount(UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getCumulatedSessionCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readCumulatedSessionCount() throws UaException {
    try {
      return readCumulatedSessionCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeCumulatedSessionCount(UInteger value) throws UaException {
    try {
      writeCumulatedSessionCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readCumulatedSessionCountAsync() {
    return getCumulatedSessionCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeCumulatedSessionCountAsync(
      UInteger cumulatedSessionCount) {
    DataValue value = DataValue.valueOnly(new Variant(cumulatedSessionCount));
    return getCumulatedSessionCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getCumulatedSessionCountNode() throws UaException {
    try {
      return getCumulatedSessionCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getCumulatedSessionCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "CumulatedSessionCount",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public UInteger getSecurityRejectedSessionCount() throws UaException {
    BaseDataVariableTypeNode node = getSecurityRejectedSessionCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setSecurityRejectedSessionCount(UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getSecurityRejectedSessionCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readSecurityRejectedSessionCount() throws UaException {
    try {
      return readSecurityRejectedSessionCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSecurityRejectedSessionCount(UInteger value) throws UaException {
    try {
      writeSecurityRejectedSessionCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readSecurityRejectedSessionCountAsync() {
    return getSecurityRejectedSessionCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSecurityRejectedSessionCountAsync(
      UInteger securityRejectedSessionCount) {
    DataValue value = DataValue.valueOnly(new Variant(securityRejectedSessionCount));
    return getSecurityRejectedSessionCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getSecurityRejectedSessionCountNode() throws UaException {
    try {
      return getSecurityRejectedSessionCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getSecurityRejectedSessionCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SecurityRejectedSessionCount",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public UInteger getRejectedSessionCount() throws UaException {
    BaseDataVariableTypeNode node = getRejectedSessionCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setRejectedSessionCount(UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getRejectedSessionCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readRejectedSessionCount() throws UaException {
    try {
      return readRejectedSessionCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeRejectedSessionCount(UInteger value) throws UaException {
    try {
      writeRejectedSessionCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readRejectedSessionCountAsync() {
    return getRejectedSessionCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeRejectedSessionCountAsync(
      UInteger rejectedSessionCount) {
    DataValue value = DataValue.valueOnly(new Variant(rejectedSessionCount));
    return getRejectedSessionCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getRejectedSessionCountNode() throws UaException {
    try {
      return getRejectedSessionCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getRejectedSessionCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "RejectedSessionCount",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public UInteger getSessionTimeoutCount() throws UaException {
    BaseDataVariableTypeNode node = getSessionTimeoutCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setSessionTimeoutCount(UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getSessionTimeoutCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readSessionTimeoutCount() throws UaException {
    try {
      return readSessionTimeoutCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSessionTimeoutCount(UInteger value) throws UaException {
    try {
      writeSessionTimeoutCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readSessionTimeoutCountAsync() {
    return getSessionTimeoutCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSessionTimeoutCountAsync(UInteger sessionTimeoutCount) {
    DataValue value = DataValue.valueOnly(new Variant(sessionTimeoutCount));
    return getSessionTimeoutCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getSessionTimeoutCountNode() throws UaException {
    try {
      return getSessionTimeoutCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getSessionTimeoutCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SessionTimeoutCount",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public UInteger getSessionAbortCount() throws UaException {
    BaseDataVariableTypeNode node = getSessionAbortCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setSessionAbortCount(UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getSessionAbortCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readSessionAbortCount() throws UaException {
    try {
      return readSessionAbortCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSessionAbortCount(UInteger value) throws UaException {
    try {
      writeSessionAbortCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readSessionAbortCountAsync() {
    return getSessionAbortCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSessionAbortCountAsync(UInteger sessionAbortCount) {
    DataValue value = DataValue.valueOnly(new Variant(sessionAbortCount));
    return getSessionAbortCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getSessionAbortCountNode() throws UaException {
    try {
      return getSessionAbortCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getSessionAbortCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SessionAbortCount",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public UInteger getPublishingIntervalCount() throws UaException {
    BaseDataVariableTypeNode node = getPublishingIntervalCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setPublishingIntervalCount(UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getPublishingIntervalCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readPublishingIntervalCount() throws UaException {
    try {
      return readPublishingIntervalCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writePublishingIntervalCount(UInteger value) throws UaException {
    try {
      writePublishingIntervalCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readPublishingIntervalCountAsync() {
    return getPublishingIntervalCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writePublishingIntervalCountAsync(
      UInteger publishingIntervalCount) {
    DataValue value = DataValue.valueOnly(new Variant(publishingIntervalCount));
    return getPublishingIntervalCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getPublishingIntervalCountNode() throws UaException {
    try {
      return getPublishingIntervalCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getPublishingIntervalCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "PublishingIntervalCount",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public UInteger getCurrentSubscriptionCount() throws UaException {
    BaseDataVariableTypeNode node = getCurrentSubscriptionCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setCurrentSubscriptionCount(UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getCurrentSubscriptionCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readCurrentSubscriptionCount() throws UaException {
    try {
      return readCurrentSubscriptionCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeCurrentSubscriptionCount(UInteger value) throws UaException {
    try {
      writeCurrentSubscriptionCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readCurrentSubscriptionCountAsync() {
    return getCurrentSubscriptionCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeCurrentSubscriptionCountAsync(
      UInteger currentSubscriptionCount) {
    DataValue value = DataValue.valueOnly(new Variant(currentSubscriptionCount));
    return getCurrentSubscriptionCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getCurrentSubscriptionCountNode() throws UaException {
    try {
      return getCurrentSubscriptionCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getCurrentSubscriptionCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "CurrentSubscriptionCount",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public UInteger getCumulatedSubscriptionCount() throws UaException {
    BaseDataVariableTypeNode node = getCumulatedSubscriptionCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setCumulatedSubscriptionCount(UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getCumulatedSubscriptionCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readCumulatedSubscriptionCount() throws UaException {
    try {
      return readCumulatedSubscriptionCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeCumulatedSubscriptionCount(UInteger value) throws UaException {
    try {
      writeCumulatedSubscriptionCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readCumulatedSubscriptionCountAsync() {
    return getCumulatedSubscriptionCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeCumulatedSubscriptionCountAsync(
      UInteger cumulatedSubscriptionCount) {
    DataValue value = DataValue.valueOnly(new Variant(cumulatedSubscriptionCount));
    return getCumulatedSubscriptionCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getCumulatedSubscriptionCountNode() throws UaException {
    try {
      return getCumulatedSubscriptionCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getCumulatedSubscriptionCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "CumulatedSubscriptionCount",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public UInteger getSecurityRejectedRequestsCount() throws UaException {
    BaseDataVariableTypeNode node = getSecurityRejectedRequestsCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setSecurityRejectedRequestsCount(UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getSecurityRejectedRequestsCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readSecurityRejectedRequestsCount() throws UaException {
    try {
      return readSecurityRejectedRequestsCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSecurityRejectedRequestsCount(UInteger value) throws UaException {
    try {
      writeSecurityRejectedRequestsCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readSecurityRejectedRequestsCountAsync() {
    return getSecurityRejectedRequestsCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSecurityRejectedRequestsCountAsync(
      UInteger securityRejectedRequestsCount) {
    DataValue value = DataValue.valueOnly(new Variant(securityRejectedRequestsCount));
    return getSecurityRejectedRequestsCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getSecurityRejectedRequestsCountNode() throws UaException {
    try {
      return getSecurityRejectedRequestsCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getSecurityRejectedRequestsCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SecurityRejectedRequestsCount",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public UInteger getRejectedRequestsCount() throws UaException {
    BaseDataVariableTypeNode node = getRejectedRequestsCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setRejectedRequestsCount(UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getRejectedRequestsCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readRejectedRequestsCount() throws UaException {
    try {
      return readRejectedRequestsCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeRejectedRequestsCount(UInteger value) throws UaException {
    try {
      writeRejectedRequestsCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readRejectedRequestsCountAsync() {
    return getRejectedRequestsCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeRejectedRequestsCountAsync(
      UInteger rejectedRequestsCount) {
    DataValue value = DataValue.valueOnly(new Variant(rejectedRequestsCount));
    return getRejectedRequestsCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getRejectedRequestsCountNode() throws UaException {
    try {
      return getRejectedRequestsCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getRejectedRequestsCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "RejectedRequestsCount",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }
}
