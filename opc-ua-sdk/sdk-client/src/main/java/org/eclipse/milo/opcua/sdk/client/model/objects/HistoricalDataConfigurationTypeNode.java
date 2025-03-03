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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ExceptionDeviationFormat;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class HistoricalDataConfigurationTypeNode extends BaseObjectTypeNode
    implements HistoricalDataConfigurationType {
  public HistoricalDataConfigurationTypeNode(
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
  public Boolean getStepped() throws UaException {
    PropertyTypeNode node = getSteppedNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setStepped(Boolean value) throws UaException {
    PropertyTypeNode node = getSteppedNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readStepped() throws UaException {
    try {
      return readSteppedAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeStepped(Boolean value) throws UaException {
    try {
      writeSteppedAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readSteppedAsync() {
    return getSteppedNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSteppedAsync(Boolean stepped) {
    DataValue value = DataValue.valueOnly(new Variant(stepped));
    return getSteppedNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getSteppedNode() throws UaException {
    try {
      return getSteppedNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSteppedNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Stepped", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getDefinition() throws UaException {
    PropertyTypeNode node = getDefinitionNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setDefinition(String value) throws UaException {
    PropertyTypeNode node = getDefinitionNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readDefinition() throws UaException {
    try {
      return readDefinitionAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDefinition(String value) throws UaException {
    try {
      writeDefinitionAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readDefinitionAsync() {
    return getDefinitionNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeDefinitionAsync(String definition) {
    DataValue value = DataValue.valueOnly(new Variant(definition));
    return getDefinitionNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDefinitionNode() throws UaException {
    try {
      return getDefinitionNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDefinitionNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Definition", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getMaxTimeInterval() throws UaException {
    PropertyTypeNode node = getMaxTimeIntervalNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxTimeInterval(Double value) throws UaException {
    PropertyTypeNode node = getMaxTimeIntervalNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readMaxTimeInterval() throws UaException {
    try {
      return readMaxTimeIntervalAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxTimeInterval(Double value) throws UaException {
    try {
      writeMaxTimeIntervalAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readMaxTimeIntervalAsync() {
    return getMaxTimeIntervalNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxTimeIntervalAsync(Double maxTimeInterval) {
    DataValue value = DataValue.valueOnly(new Variant(maxTimeInterval));
    return getMaxTimeIntervalNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxTimeIntervalNode() throws UaException {
    try {
      return getMaxTimeIntervalNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxTimeIntervalNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxTimeInterval",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getMinTimeInterval() throws UaException {
    PropertyTypeNode node = getMinTimeIntervalNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setMinTimeInterval(Double value) throws UaException {
    PropertyTypeNode node = getMinTimeIntervalNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readMinTimeInterval() throws UaException {
    try {
      return readMinTimeIntervalAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMinTimeInterval(Double value) throws UaException {
    try {
      writeMinTimeIntervalAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readMinTimeIntervalAsync() {
    return getMinTimeIntervalNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMinTimeIntervalAsync(Double minTimeInterval) {
    DataValue value = DataValue.valueOnly(new Variant(minTimeInterval));
    return getMinTimeIntervalNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMinTimeIntervalNode() throws UaException {
    try {
      return getMinTimeIntervalNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMinTimeIntervalNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MinTimeInterval",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getExceptionDeviation() throws UaException {
    PropertyTypeNode node = getExceptionDeviationNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setExceptionDeviation(Double value) throws UaException {
    PropertyTypeNode node = getExceptionDeviationNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readExceptionDeviation() throws UaException {
    try {
      return readExceptionDeviationAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeExceptionDeviation(Double value) throws UaException {
    try {
      writeExceptionDeviationAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readExceptionDeviationAsync() {
    return getExceptionDeviationNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeExceptionDeviationAsync(Double exceptionDeviation) {
    DataValue value = DataValue.valueOnly(new Variant(exceptionDeviation));
    return getExceptionDeviationNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getExceptionDeviationNode() throws UaException {
    try {
      return getExceptionDeviationNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getExceptionDeviationNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ExceptionDeviation",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public ExceptionDeviationFormat getExceptionDeviationFormat() throws UaException {
    PropertyTypeNode node = getExceptionDeviationFormatNode();
    Object value = node.getValue().getValue().getValue();

    if (value instanceof Integer) {
      return ExceptionDeviationFormat.from((Integer) value);
    } else if (value instanceof ExceptionDeviationFormat) {
      return (ExceptionDeviationFormat) value;
    } else {
      return null;
    }
  }

  @Override
  public void setExceptionDeviationFormat(ExceptionDeviationFormat value) throws UaException {
    PropertyTypeNode node = getExceptionDeviationFormatNode();
    node.setValue(new Variant(value));
  }

  @Override
  public ExceptionDeviationFormat readExceptionDeviationFormat() throws UaException {
    try {
      return readExceptionDeviationFormatAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeExceptionDeviationFormat(ExceptionDeviationFormat value) throws UaException {
    try {
      writeExceptionDeviationFormatAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends ExceptionDeviationFormat> readExceptionDeviationFormatAsync() {
    return getExceptionDeviationFormatNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(
            v -> {
              Object value = v.getValue().getValue();
              if (value instanceof Integer) {
                return ExceptionDeviationFormat.from((Integer) value);
              } else {
                return null;
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeExceptionDeviationFormatAsync(
      ExceptionDeviationFormat exceptionDeviationFormat) {
    DataValue value = DataValue.valueOnly(new Variant(exceptionDeviationFormat));
    return getExceptionDeviationFormatNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getExceptionDeviationFormatNode() throws UaException {
    try {
      return getExceptionDeviationFormatNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getExceptionDeviationFormatNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ExceptionDeviationFormat",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public DateTime getStartOfArchive() throws UaException {
    PropertyTypeNode node = getStartOfArchiveNode();
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setStartOfArchive(DateTime value) throws UaException {
    PropertyTypeNode node = getStartOfArchiveNode();
    node.setValue(new Variant(value));
  }

  @Override
  public DateTime readStartOfArchive() throws UaException {
    try {
      return readStartOfArchiveAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeStartOfArchive(DateTime value) throws UaException {
    try {
      writeStartOfArchiveAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends DateTime> readStartOfArchiveAsync() {
    return getStartOfArchiveNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (DateTime) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeStartOfArchiveAsync(DateTime startOfArchive) {
    DataValue value = DataValue.valueOnly(new Variant(startOfArchive));
    return getStartOfArchiveNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getStartOfArchiveNode() throws UaException {
    try {
      return getStartOfArchiveNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getStartOfArchiveNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "StartOfArchive",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public DateTime getStartOfOnlineArchive() throws UaException {
    PropertyTypeNode node = getStartOfOnlineArchiveNode();
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setStartOfOnlineArchive(DateTime value) throws UaException {
    PropertyTypeNode node = getStartOfOnlineArchiveNode();
    node.setValue(new Variant(value));
  }

  @Override
  public DateTime readStartOfOnlineArchive() throws UaException {
    try {
      return readStartOfOnlineArchiveAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeStartOfOnlineArchive(DateTime value) throws UaException {
    try {
      writeStartOfOnlineArchiveAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends DateTime> readStartOfOnlineArchiveAsync() {
    return getStartOfOnlineArchiveNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (DateTime) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeStartOfOnlineArchiveAsync(
      DateTime startOfOnlineArchive) {
    DataValue value = DataValue.valueOnly(new Variant(startOfOnlineArchive));
    return getStartOfOnlineArchiveNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getStartOfOnlineArchiveNode() throws UaException {
    try {
      return getStartOfOnlineArchiveNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getStartOfOnlineArchiveNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "StartOfOnlineArchive",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getServerTimestampSupported() throws UaException {
    PropertyTypeNode node = getServerTimestampSupportedNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setServerTimestampSupported(Boolean value) throws UaException {
    PropertyTypeNode node = getServerTimestampSupportedNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readServerTimestampSupported() throws UaException {
    try {
      return readServerTimestampSupportedAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeServerTimestampSupported(Boolean value) throws UaException {
    try {
      writeServerTimestampSupportedAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readServerTimestampSupportedAsync() {
    return getServerTimestampSupportedNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeServerTimestampSupportedAsync(
      Boolean serverTimestampSupported) {
    DataValue value = DataValue.valueOnly(new Variant(serverTimestampSupported));
    return getServerTimestampSupportedNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getServerTimestampSupportedNode() throws UaException {
    try {
      return getServerTimestampSupportedNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getServerTimestampSupportedNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ServerTimestampSupported",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getMaxTimeStoredValues() throws UaException {
    PropertyTypeNode node = getMaxTimeStoredValuesNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxTimeStoredValues(Double value) throws UaException {
    PropertyTypeNode node = getMaxTimeStoredValuesNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readMaxTimeStoredValues() throws UaException {
    try {
      return readMaxTimeStoredValuesAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxTimeStoredValues(Double value) throws UaException {
    try {
      writeMaxTimeStoredValuesAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readMaxTimeStoredValuesAsync() {
    return getMaxTimeStoredValuesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxTimeStoredValuesAsync(Double maxTimeStoredValues) {
    DataValue value = DataValue.valueOnly(new Variant(maxTimeStoredValues));
    return getMaxTimeStoredValuesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxTimeStoredValuesNode() throws UaException {
    try {
      return getMaxTimeStoredValuesNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxTimeStoredValuesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxTimeStoredValues",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxCountStoredValues() throws UaException {
    PropertyTypeNode node = getMaxCountStoredValuesNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxCountStoredValues(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxCountStoredValuesNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxCountStoredValues() throws UaException {
    try {
      return readMaxCountStoredValuesAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxCountStoredValues(UInteger value) throws UaException {
    try {
      writeMaxCountStoredValuesAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxCountStoredValuesAsync() {
    return getMaxCountStoredValuesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxCountStoredValuesAsync(
      UInteger maxCountStoredValues) {
    DataValue value = DataValue.valueOnly(new Variant(maxCountStoredValues));
    return getMaxCountStoredValuesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxCountStoredValuesNode() throws UaException {
    try {
      return getMaxCountStoredValuesNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxCountStoredValuesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxCountStoredValues",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public AggregateConfigurationTypeNode getAggregateConfigurationNode() throws UaException {
    try {
      return getAggregateConfigurationNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends AggregateConfigurationTypeNode>
      getAggregateConfigurationNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "AggregateConfiguration",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (AggregateConfigurationTypeNode) node);
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
}
