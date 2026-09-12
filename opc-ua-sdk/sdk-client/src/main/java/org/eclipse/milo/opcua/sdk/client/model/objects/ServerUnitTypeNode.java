/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import com.digitalpetri.opcua.uanodeset.runtime.client.ClientMembers;
import com.digitalpetri.opcua.uanodeset.runtime.client.ClientViews;
import com.digitalpetri.opcua.uanodeset.runtime.members.MemberDeclaration;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.ConversionLimitEnum;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

public class ServerUnitTypeNode extends UnitTypeNode implements ServerUnitType {
  public ServerUnitTypeNode(
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

  /**
   * Creates an independently owned view context retaining this exact existing node and its client.
   * Cached attributes remain shared even after SDK cache eviction. Close the returned context when
   * its views are no longer needed.
   */
  public static ClientViews createViews(ServerUnitTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable ConversionLimitEnum getConversionLimit() throws UaException {
    PropertyTypeNode node = getConversionLimitNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ConversionLimit (declaration i=32461, owner i=32447)"
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
              "ConversionLimit: ValueRank=-1 does not permit rank " + rank);
        }
        if (value != null && !((Object) value instanceof ConversionLimitEnum)) {
          if (!(value instanceof Integer)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "ConversionLimit: expected"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.ConversionLimitEnum or"
                    + " Int32, got "
                    + value);
          }
          if (ConversionLimitEnum.from((Integer) value) == null) {
            throw new UaException(
                StatusCodes.Bad_OutOfRange,
                "ConversionLimit: unknown"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.ConversionLimitEnum"
                    + " value "
                    + value);
          }
        }
        convertedValue =
            value == null || value instanceof ConversionLimitEnum
                ? (ConversionLimitEnum) value
                : ConversionLimitEnum.from((Integer) value);
      }
    }
    return (ConversionLimitEnum) convertedValue;
  }

  @Override
  public void setConversionLimit(@Nullable ConversionLimitEnum value) throws UaException {
    PropertyTypeNode node = getConversionLimitNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ConversionLimit (declaration i=32461, owner i=32447)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable ConversionLimitEnum readConversionLimit() throws UaException {
    return ClientMembers.await(readConversionLimitAsync(), false);
  }

  @Override
  public void writeConversionLimit(@Nullable ConversionLimitEnum value) throws UaException {
    try {
      StatusCode statusCode = writeConversionLimitAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ConversionLimitEnum> readConversionLimitAsync() {
    return getConversionLimitNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ConversionLimit (declaration i=32461, owner"
                            + " i=32447) on "
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
                          "ConversionLimit: ValueRank=-1 does not permit rank " + rank);
                    }
                    if (value != null && !((Object) value instanceof ConversionLimitEnum)) {
                      if (!(value instanceof Integer)) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_TypeMismatch,
                            "ConversionLimit: expected"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.ConversionLimitEnum"
                                + " or Int32, got "
                                + value);
                      }
                      if (ConversionLimitEnum.from((Integer) value) == null) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_OutOfRange,
                            "ConversionLimit: unknown"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.ConversionLimitEnum"
                                + " value "
                                + value);
                      }
                    }
                    convertedValue =
                        value == null || value instanceof ConversionLimitEnum
                            ? (ConversionLimitEnum) value
                            : ConversionLimitEnum.from((Integer) value);
                  }
                }
                return (ConversionLimitEnum) convertedValue;
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeConversionLimitAsync(
      @Nullable ConversionLimitEnum conversionLimit) {
    return getConversionLimitNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ConversionLimit (declaration i=32461, owner"
                            + " i=32447) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(conversionLimit));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getConversionLimitNode() throws UaException {
    return ClientMembers.await(getConversionLimitNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getConversionLimitNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ConversionLimit",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ConversionLimit (declaration i=32461, owner i=32447)"));
  }

  @Override
  public @Nullable BaseObjectTypeNode getAlternativeUnitsNode() throws UaException {
    return ClientMembers.await(getAlternativeUnitsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable BaseObjectTypeNode> getAlternativeUnitsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseObjectTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "AlternativeUnits",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            true,
            "http://opcfoundation.org/UA/:AlternativeUnits (declaration i=32452, owner i=32447)"));
  }

  @Override
  public @Nullable UnitTypeNode getCoherentUnitNode() throws UaException {
    return ClientMembers.await(getCoherentUnitNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable UnitTypeNode> getCoherentUnitNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        UnitTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CoherentUnit",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            true,
            "http://opcfoundation.org/UA/:CoherentUnit (declaration i=32462, owner i=32447)"));
  }
}
