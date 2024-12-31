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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
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
import org.eclipse.milo.opcua.stack.core.types.structured.TrustListValidationOptions;

public class TrustListTypeNode extends FileTypeNode implements TrustListType {
  public TrustListTypeNode(
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
  public DateTime getLastUpdateTime() throws UaException {
    PropertyTypeNode node = getLastUpdateTimeNode();
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastUpdateTime(DateTime value) throws UaException {
    PropertyTypeNode node = getLastUpdateTimeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public DateTime readLastUpdateTime() throws UaException {
    try {
      return readLastUpdateTimeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLastUpdateTime(DateTime value) throws UaException {
    try {
      writeLastUpdateTimeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends DateTime> readLastUpdateTimeAsync() {
    return getLastUpdateTimeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (DateTime) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLastUpdateTimeAsync(DateTime lastUpdateTime) {
    DataValue value = DataValue.valueOnly(new Variant(lastUpdateTime));
    return getLastUpdateTimeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getLastUpdateTimeNode() throws UaException {
    try {
      return getLastUpdateTimeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLastUpdateTimeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LastUpdateTime",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getUpdateFrequency() throws UaException {
    PropertyTypeNode node = getUpdateFrequencyNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setUpdateFrequency(Double value) throws UaException {
    PropertyTypeNode node = getUpdateFrequencyNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readUpdateFrequency() throws UaException {
    try {
      return readUpdateFrequencyAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeUpdateFrequency(Double value) throws UaException {
    try {
      writeUpdateFrequencyAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readUpdateFrequencyAsync() {
    return getUpdateFrequencyNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeUpdateFrequencyAsync(Double updateFrequency) {
    DataValue value = DataValue.valueOnly(new Variant(updateFrequency));
    return getUpdateFrequencyNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getUpdateFrequencyNode() throws UaException {
    try {
      return getUpdateFrequencyNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUpdateFrequencyNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "UpdateFrequency",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getActivityTimeout() throws UaException {
    PropertyTypeNode node = getActivityTimeoutNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setActivityTimeout(Double value) throws UaException {
    PropertyTypeNode node = getActivityTimeoutNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readActivityTimeout() throws UaException {
    try {
      return readActivityTimeoutAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeActivityTimeout(Double value) throws UaException {
    try {
      writeActivityTimeoutAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readActivityTimeoutAsync() {
    return getActivityTimeoutNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeActivityTimeoutAsync(Double activityTimeout) {
    DataValue value = DataValue.valueOnly(new Variant(activityTimeout));
    return getActivityTimeoutNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getActivityTimeoutNode() throws UaException {
    try {
      return getActivityTimeoutNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getActivityTimeoutNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ActivityTimeout",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public TrustListValidationOptions getDefaultValidationOptions() throws UaException {
    PropertyTypeNode node = getDefaultValidationOptionsNode();
    return (TrustListValidationOptions) node.getValue().getValue().getValue();
  }

  @Override
  public void setDefaultValidationOptions(TrustListValidationOptions value) throws UaException {
    PropertyTypeNode node = getDefaultValidationOptionsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public TrustListValidationOptions readDefaultValidationOptions() throws UaException {
    try {
      return readDefaultValidationOptionsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDefaultValidationOptions(TrustListValidationOptions value) throws UaException {
    try {
      writeDefaultValidationOptionsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends TrustListValidationOptions>
      readDefaultValidationOptionsAsync() {
    return getDefaultValidationOptionsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (TrustListValidationOptions) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeDefaultValidationOptionsAsync(
      TrustListValidationOptions defaultValidationOptions) {
    DataValue value = DataValue.valueOnly(new Variant(defaultValidationOptions));
    return getDefaultValidationOptionsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDefaultValidationOptionsNode() throws UaException {
    try {
      return getDefaultValidationOptionsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDefaultValidationOptionsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DefaultValidationOptions",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
