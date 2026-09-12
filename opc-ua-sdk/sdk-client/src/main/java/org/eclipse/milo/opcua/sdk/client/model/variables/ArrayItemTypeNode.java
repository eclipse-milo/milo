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
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.AxisScaleEnumeration;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

public class ArrayItemTypeNode extends DataItemTypeNode implements ArrayItemType {
  public ArrayItemTypeNode(
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
          "http://opcfoundation.org/UA/:InstrumentRange (declaration i=12024, owner i=12021)"
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
          "http://opcfoundation.org/UA/:InstrumentRange (declaration i=12024, owner i=12021)"
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
                        "http://opcfoundation.org/UA/:InstrumentRange (declaration i=12024, owner"
                            + " i=12021) on "
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
                        "http://opcfoundation.org/UA/:InstrumentRange (declaration i=12024, owner"
                            + " i=12021) on "
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
            "http://opcfoundation.org/UA/:InstrumentRange (declaration i=12024, owner i=12021)"));
  }

  @Override
  public @Nullable Range getEuRange() throws UaException {
    PropertyTypeNode node = getEuRangeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EURange (declaration i=12025, owner i=12021)"
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
          "http://opcfoundation.org/UA/:EURange (declaration i=12025, owner i=12021)"
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
                        "http://opcfoundation.org/UA/:EURange (declaration i=12025, owner i=12021)"
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
                        "http://opcfoundation.org/UA/:EURange (declaration i=12025, owner i=12021)"
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
  public PropertyTypeNode getEuRangeNode() throws UaException {
    return ClientMembers.await(getEuRangeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getEuRangeNodeAsync() {
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
            false,
            "http://opcfoundation.org/UA/:EURange (declaration i=12025, owner i=12021)"));
  }

  @Override
  public @Nullable EUInformation getEngineeringUnitsProperty() throws UaException {
    PropertyTypeNode node = getEngineeringUnitsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=12026, owner i=12021)"
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
          "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=12026, owner i=12021)"
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
                        "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=12026, owner"
                            + " i=12021) on "
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
                        "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=12026, owner"
                            + " i=12021) on "
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
  public PropertyTypeNode getEngineeringUnitsNode() throws UaException {
    return ClientMembers.await(getEngineeringUnitsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getEngineeringUnitsNodeAsync() {
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
            false,
            "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=12026, owner i=12021)"));
  }

  @Override
  public @Nullable LocalizedText getTitle() throws UaException {
    PropertyTypeNode node = getTitleNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Title (declaration i=12027, owner i=12021)"
              + " on "
              + getNodeId());
    }
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setTitle(@Nullable LocalizedText value) throws UaException {
    PropertyTypeNode node = getTitleNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Title (declaration i=12027, owner i=12021)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable LocalizedText readTitle() throws UaException {
    return ClientMembers.await(readTitleAsync(), false);
  }

  @Override
  public void writeTitle(@Nullable LocalizedText value) throws UaException {
    try {
      StatusCode statusCode = writeTitleAsync(value).get();
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
  public CompletableFuture<? extends @Nullable LocalizedText> readTitleAsync() {
    return getTitleNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Title (declaration i=12027, owner i=12021)"
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
                return (LocalizedText) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeTitleAsync(@Nullable LocalizedText title) {
    return getTitleNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Title (declaration i=12027, owner i=12021)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(title));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getTitleNode() throws UaException {
    return ClientMembers.await(getTitleNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getTitleNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Title",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:Title (declaration i=12027, owner i=12021)"));
  }

  @Override
  public @Nullable AxisScaleEnumeration getAxisScaleType() throws UaException {
    PropertyTypeNode node = getAxisScaleTypeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AxisScaleType (declaration i=12028, owner i=12021)"
              + " on "
              + getNodeId());
    }
    Object value = node.getValue().getValue().getValue();
    Object convertedValue;
    {
      if (value == null || value instanceof Matrix && ((Matrix) value).isNull()) {
        convertedValue = null;
      } else {
        Object elements = value instanceof Matrix ? ((Matrix) value).getElements() : value;
        int rank =
            value instanceof Matrix
                ? ((Matrix) value).getValueRank()
                : ArrayUtil.getValueRank(value);
        boolean permitted = rank == -1;
        if (!permitted) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "AxisScaleType: ValueRank=-1 does not permit rank " + rank);
        }
        if (value != null && !((Object) value instanceof AxisScaleEnumeration)) {
          if (!(value instanceof Integer)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "AxisScaleType: expected"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.AxisScaleEnumeration or"
                    + " Int32, got "
                    + value);
          }
          if (AxisScaleEnumeration.from((Integer) value) == null) {
            throw new UaException(
                StatusCodes.Bad_OutOfRange,
                "AxisScaleType: unknown"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.AxisScaleEnumeration"
                    + " value "
                    + value);
          }
        }
        convertedValue =
            value == null || value instanceof AxisScaleEnumeration
                ? (AxisScaleEnumeration) value
                : AxisScaleEnumeration.from((Integer) value);
      }
    }
    return (AxisScaleEnumeration) convertedValue;
  }

  @Override
  public void setAxisScaleType(@Nullable AxisScaleEnumeration value) throws UaException {
    PropertyTypeNode node = getAxisScaleTypeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AxisScaleType (declaration i=12028, owner i=12021)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable AxisScaleEnumeration readAxisScaleType() throws UaException {
    return ClientMembers.await(readAxisScaleTypeAsync(), false);
  }

  @Override
  public void writeAxisScaleType(@Nullable AxisScaleEnumeration value) throws UaException {
    try {
      StatusCode statusCode = writeAxisScaleTypeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable AxisScaleEnumeration> readAxisScaleTypeAsync() {
    return getAxisScaleTypeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:AxisScaleType (declaration i=12028, owner"
                            + " i=12021) on "
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
                Object value = v.getValue().getValue();
                Object convertedValue;
                {
                  if (value == null || value instanceof Matrix && ((Matrix) value).isNull()) {
                    convertedValue = null;
                  } else {
                    Object elements =
                        value instanceof Matrix ? ((Matrix) value).getElements() : value;
                    int rank =
                        value instanceof Matrix
                            ? ((Matrix) value).getValueRank()
                            : ArrayUtil.getValueRank(value);
                    boolean permitted = rank == -1;
                    if (!permitted) {
                      throw new UaRuntimeException(
                          StatusCodes.Bad_TypeMismatch,
                          "AxisScaleType: ValueRank=-1 does not permit rank " + rank);
                    }
                    if (value != null && !((Object) value instanceof AxisScaleEnumeration)) {
                      if (!(value instanceof Integer)) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_TypeMismatch,
                            "AxisScaleType: expected"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.AxisScaleEnumeration"
                                + " or Int32, got "
                                + value);
                      }
                      if (AxisScaleEnumeration.from((Integer) value) == null) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_OutOfRange,
                            "AxisScaleType: unknown"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.AxisScaleEnumeration"
                                + " value "
                                + value);
                      }
                    }
                    convertedValue =
                        value == null || value instanceof AxisScaleEnumeration
                            ? (AxisScaleEnumeration) value
                            : AxisScaleEnumeration.from((Integer) value);
                  }
                }
                return (AxisScaleEnumeration) convertedValue;
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeAxisScaleTypeAsync(
      @Nullable AxisScaleEnumeration axisScaleType) {
    return getAxisScaleTypeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:AxisScaleType (declaration i=12028, owner"
                            + " i=12021) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(axisScaleType));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getAxisScaleTypeNode() throws UaException {
    return ClientMembers.await(getAxisScaleTypeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getAxisScaleTypeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "AxisScaleType",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:AxisScaleType (declaration i=12028, owner i=12021)"));
  }
}
