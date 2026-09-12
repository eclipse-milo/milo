/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.variables;

import com.digitalpetri.opcua.uanodeset.runtime.client.ClientMembers;
import com.digitalpetri.opcua.uanodeset.runtime.members.MemberDeclaration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
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
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.NumberRange;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

public class BaseAnalogTypeNode extends DataItemTypeNode implements BaseAnalogType {
  public BaseAnalogTypeNode(
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
  public @Nullable Range getInstrumentRange() throws UaException {
    PropertyTypeNode node = getInstrumentRangeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InstrumentRange (declaration i=17567, owner i=15318)"
              + " on "
              + getNodeId());
    }
    return (Range)
        decodeValue(node.getValue().getValue().getValue(), Range.class, ValueRanks.Scalar);
  }

  @Override
  public void setInstrumentRange(@Nullable Range value) throws UaException {
    PropertyTypeNode node = getInstrumentRangeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InstrumentRange (declaration i=17567, owner i=15318)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, Range.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable Range readInstrumentRange() throws UaException {
    return ClientMembers.await(readInstrumentRangeAsync(), false);
  }

  @Override
  public void writeInstrumentRange(@Nullable Range value) throws UaException {
    try {
      StatusCode statusCode = writeInstrumentRangeAsync(value).get();
      if (statusCode != null && !statusCode.isGood()) {
        throw new UaException(statusCode);
      }
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends @Nullable Range> readInstrumentRangeAsync() {
    return getInstrumentRangeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:InstrumentRange (declaration i=17567, owner"
                            + " i=15318) on "
                            + getNodeId()));
              }
              return node.readAttributeAsync(AttributeId.Value);
            })
        .thenApply(
            v -> {
              if (!v.getStatusCode().isGood()) {
                throw new CompletionException(new UaException(v.getStatusCode()));
              }
              try {
                return (Range) decodeValue(v.getValue().getValue(), Range.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeInstrumentRangeAsync(@Nullable Range instrumentRange) {
    return getInstrumentRangeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:InstrumentRange (declaration i=17567, owner"
                            + " i=15318) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(encodeValue(instrumentRange, Range.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getInstrumentRangeNode() throws UaException {
    return ClientMembers.await(getInstrumentRangeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getInstrumentRangeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "InstrumentRange",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:InstrumentRange (declaration i=17567, owner i=15318)"));
  }

  @Override
  public @Nullable NumberRange getInstrumentNumberRange() throws UaException {
    PropertyTypeNode node = getInstrumentNumberRangeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InstrumentNumberRange (declaration i=23904, owner i=15318)"
              + " on "
              + getNodeId());
    }
    return (NumberRange)
        decodeValue(node.getValue().getValue().getValue(), NumberRange.class, ValueRanks.Scalar);
  }

  @Override
  public void setInstrumentNumberRange(@Nullable NumberRange value) throws UaException {
    PropertyTypeNode node = getInstrumentNumberRangeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InstrumentNumberRange (declaration i=23904, owner i=15318)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, NumberRange.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable NumberRange readInstrumentNumberRange() throws UaException {
    return ClientMembers.await(readInstrumentNumberRangeAsync(), false);
  }

  @Override
  public void writeInstrumentNumberRange(@Nullable NumberRange value) throws UaException {
    try {
      StatusCode statusCode = writeInstrumentNumberRangeAsync(value).get();
      if (statusCode != null && !statusCode.isGood()) {
        throw new UaException(statusCode);
      }
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends @Nullable NumberRange> readInstrumentNumberRangeAsync() {
    return getInstrumentNumberRangeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:InstrumentNumberRange (declaration i=23904,"
                            + " owner i=15318) on "
                            + getNodeId()));
              }
              return node.readAttributeAsync(AttributeId.Value);
            })
        .thenApply(
            v -> {
              if (!v.getStatusCode().isGood()) {
                throw new CompletionException(new UaException(v.getStatusCode()));
              }
              try {
                return (NumberRange)
                    decodeValue(v.getValue().getValue(), NumberRange.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeInstrumentNumberRangeAsync(
      @Nullable NumberRange instrumentNumberRange) {
    return getInstrumentNumberRangeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:InstrumentNumberRange (declaration i=23904,"
                            + " owner i=15318) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                instrumentNumberRange, NumberRange.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getInstrumentNumberRangeNode() throws UaException {
    return ClientMembers.await(getInstrumentNumberRangeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getInstrumentNumberRangeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "InstrumentNumberRange",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:InstrumentNumberRange (declaration i=23904, owner"
                + " i=15318)"));
  }

  @Override
  public @Nullable Range getEuRange() throws UaException {
    PropertyTypeNode node = getEuRangeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EURange (declaration i=17568, owner i=15318)"
              + " on "
              + getNodeId());
    }
    return (Range)
        decodeValue(node.getValue().getValue().getValue(), Range.class, ValueRanks.Scalar);
  }

  @Override
  public void setEuRange(@Nullable Range value) throws UaException {
    PropertyTypeNode node = getEuRangeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EURange (declaration i=17568, owner i=15318)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, Range.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable Range readEuRange() throws UaException {
    return ClientMembers.await(readEuRangeAsync(), false);
  }

  @Override
  public void writeEuRange(@Nullable Range value) throws UaException {
    try {
      StatusCode statusCode = writeEuRangeAsync(value).get();
      if (statusCode != null && !statusCode.isGood()) {
        throw new UaException(statusCode);
      }
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends @Nullable Range> readEuRangeAsync() {
    return getEuRangeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EURange (declaration i=17568, owner i=15318)"
                            + " on "
                            + getNodeId()));
              }
              return node.readAttributeAsync(AttributeId.Value);
            })
        .thenApply(
            v -> {
              if (!v.getStatusCode().isGood()) {
                throw new CompletionException(new UaException(v.getStatusCode()));
              }
              try {
                return (Range) decodeValue(v.getValue().getValue(), Range.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeEuRangeAsync(@Nullable Range euRange) {
    return getEuRangeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EURange (declaration i=17568, owner i=15318)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(encodeValue(euRange, Range.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getEuRangeNode() throws UaException {
    return ClientMembers.await(getEuRangeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getEuRangeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "EURange",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:EURange (declaration i=17568, owner i=15318)"));
  }

  @Override
  public @Nullable NumberRange getEuNumberRange() throws UaException {
    PropertyTypeNode node = getEuNumberRangeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EUNumberRange (declaration i=23905, owner i=15318)"
              + " on "
              + getNodeId());
    }
    return (NumberRange)
        decodeValue(node.getValue().getValue().getValue(), NumberRange.class, ValueRanks.Scalar);
  }

  @Override
  public void setEuNumberRange(@Nullable NumberRange value) throws UaException {
    PropertyTypeNode node = getEuNumberRangeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EUNumberRange (declaration i=23905, owner i=15318)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, NumberRange.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable NumberRange readEuNumberRange() throws UaException {
    return ClientMembers.await(readEuNumberRangeAsync(), false);
  }

  @Override
  public void writeEuNumberRange(@Nullable NumberRange value) throws UaException {
    try {
      StatusCode statusCode = writeEuNumberRangeAsync(value).get();
      if (statusCode != null && !statusCode.isGood()) {
        throw new UaException(statusCode);
      }
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends @Nullable NumberRange> readEuNumberRangeAsync() {
    return getEuNumberRangeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EUNumberRange (declaration i=23905, owner"
                            + " i=15318) on "
                            + getNodeId()));
              }
              return node.readAttributeAsync(AttributeId.Value);
            })
        .thenApply(
            v -> {
              if (!v.getStatusCode().isGood()) {
                throw new CompletionException(new UaException(v.getStatusCode()));
              }
              try {
                return (NumberRange)
                    decodeValue(v.getValue().getValue(), NumberRange.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeEuNumberRangeAsync(
      @Nullable NumberRange euNumberRange) {
    return getEuNumberRangeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EUNumberRange (declaration i=23905, owner"
                            + " i=15318) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(euNumberRange, NumberRange.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getEuNumberRangeNode() throws UaException {
    return ClientMembers.await(getEuNumberRangeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getEuNumberRangeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "EUNumberRange",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:EUNumberRange (declaration i=23905, owner i=15318)"));
  }

  @Override
  public @Nullable EUInformation getEngineeringUnitsProperty() throws UaException {
    PropertyTypeNode node = getEngineeringUnitsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=17569, owner i=15318)"
              + " on "
              + getNodeId());
    }
    return (EUInformation)
        decodeValue(node.getValue().getValue().getValue(), EUInformation.class, ValueRanks.Scalar);
  }

  @Override
  public void setEngineeringUnitsProperty(@Nullable EUInformation value) throws UaException {
    PropertyTypeNode node = getEngineeringUnitsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=17569, owner i=15318)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, EUInformation.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable EUInformation readEngineeringUnitsProperty() throws UaException {
    return ClientMembers.await(readEngineeringUnitsPropertyAsync(), false);
  }

  @Override
  public void writeEngineeringUnitsProperty(@Nullable EUInformation value) throws UaException {
    try {
      StatusCode statusCode = writeEngineeringUnitsPropertyAsync(value).get();
      if (statusCode != null && !statusCode.isGood()) {
        throw new UaException(statusCode);
      }
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends @Nullable EUInformation> readEngineeringUnitsPropertyAsync() {
    return getEngineeringUnitsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=17569, owner"
                            + " i=15318) on "
                            + getNodeId()));
              }
              return node.readAttributeAsync(AttributeId.Value);
            })
        .thenApply(
            v -> {
              if (!v.getStatusCode().isGood()) {
                throw new CompletionException(new UaException(v.getStatusCode()));
              }
              try {
                return (EUInformation)
                    decodeValue(v.getValue().getValue(), EUInformation.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeEngineeringUnitsPropertyAsync(
      @Nullable EUInformation engineeringUnits) {
    return getEngineeringUnitsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=17569, owner"
                            + " i=15318) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(engineeringUnits, EUInformation.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getEngineeringUnitsNode() throws UaException {
    return ClientMembers.await(getEngineeringUnitsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getEngineeringUnitsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "EngineeringUnits",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=17569, owner i=15318)"));
  }
}
