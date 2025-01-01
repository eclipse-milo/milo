/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class SecurityGroupTypeNode extends BaseObjectTypeNode implements SecurityGroupType {
  public SecurityGroupTypeNode(
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
  public String getSecurityGroupId() throws UaException {
    PropertyTypeNode node = getSecurityGroupIdNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setSecurityGroupId(String value) throws UaException {
    PropertyTypeNode node = getSecurityGroupIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readSecurityGroupId() throws UaException {
    try {
      return readSecurityGroupIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSecurityGroupId(String value) throws UaException {
    try {
      writeSecurityGroupIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readSecurityGroupIdAsync() {
    return getSecurityGroupIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSecurityGroupIdAsync(String securityGroupId) {
    DataValue value = DataValue.valueOnly(new Variant(securityGroupId));
    return getSecurityGroupIdNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getSecurityGroupIdNode() throws UaException {
    try {
      return getSecurityGroupIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSecurityGroupIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SecurityGroupId",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getKeyLifetime() throws UaException {
    PropertyTypeNode node = getKeyLifetimeNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setKeyLifetime(Double value) throws UaException {
    PropertyTypeNode node = getKeyLifetimeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readKeyLifetime() throws UaException {
    try {
      return readKeyLifetimeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeKeyLifetime(Double value) throws UaException {
    try {
      writeKeyLifetimeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readKeyLifetimeAsync() {
    return getKeyLifetimeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeKeyLifetimeAsync(Double keyLifetime) {
    DataValue value = DataValue.valueOnly(new Variant(keyLifetime));
    return getKeyLifetimeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getKeyLifetimeNode() throws UaException {
    try {
      return getKeyLifetimeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getKeyLifetimeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "KeyLifetime",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getSecurityPolicyUri() throws UaException {
    PropertyTypeNode node = getSecurityPolicyUriNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setSecurityPolicyUri(String value) throws UaException {
    PropertyTypeNode node = getSecurityPolicyUriNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readSecurityPolicyUri() throws UaException {
    try {
      return readSecurityPolicyUriAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSecurityPolicyUri(String value) throws UaException {
    try {
      writeSecurityPolicyUriAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readSecurityPolicyUriAsync() {
    return getSecurityPolicyUriNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSecurityPolicyUriAsync(String securityPolicyUri) {
    DataValue value = DataValue.valueOnly(new Variant(securityPolicyUri));
    return getSecurityPolicyUriNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getSecurityPolicyUriNode() throws UaException {
    try {
      return getSecurityPolicyUriNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSecurityPolicyUriNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SecurityPolicyUri",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxFutureKeyCount() throws UaException {
    PropertyTypeNode node = getMaxFutureKeyCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxFutureKeyCount(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxFutureKeyCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxFutureKeyCount() throws UaException {
    try {
      return readMaxFutureKeyCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxFutureKeyCount(UInteger value) throws UaException {
    try {
      writeMaxFutureKeyCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxFutureKeyCountAsync() {
    return getMaxFutureKeyCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxFutureKeyCountAsync(UInteger maxFutureKeyCount) {
    DataValue value = DataValue.valueOnly(new Variant(maxFutureKeyCount));
    return getMaxFutureKeyCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxFutureKeyCountNode() throws UaException {
    try {
      return getMaxFutureKeyCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxFutureKeyCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxFutureKeyCount",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxPastKeyCount() throws UaException {
    PropertyTypeNode node = getMaxPastKeyCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxPastKeyCount(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxPastKeyCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxPastKeyCount() throws UaException {
    try {
      return readMaxPastKeyCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxPastKeyCount(UInteger value) throws UaException {
    try {
      writeMaxPastKeyCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxPastKeyCountAsync() {
    return getMaxPastKeyCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxPastKeyCountAsync(UInteger maxPastKeyCount) {
    DataValue value = DataValue.valueOnly(new Variant(maxPastKeyCount));
    return getMaxPastKeyCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxPastKeyCountNode() throws UaException {
    try {
      return getMaxPastKeyCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxPastKeyCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxPastKeyCount",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
