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
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;
import org.jspecify.annotations.Nullable;

public class SubscriptionDiagnosticsArrayTypeNode extends BaseDataVariableTypeNode
    implements SubscriptionDiagnosticsArrayType {
  public SubscriptionDiagnosticsArrayTypeNode(
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
  public @Nullable SubscriptionDiagnosticsDataType getSubscriptionDiagnostics() throws UaException {
    SubscriptionDiagnosticsTypeNode node = getSubscriptionDiagnosticsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SubscriptionDiagnostics (declaration i=12784, owner i=2171)"
              + " on "
              + getNodeId());
    }
    return (SubscriptionDiagnosticsDataType)
        decodeValue(
            node.getValue().getValue().getValue(),
            SubscriptionDiagnosticsDataType.class,
            ValueRanks.Scalar);
  }

  @Override
  public void setSubscriptionDiagnostics(@Nullable SubscriptionDiagnosticsDataType value)
      throws UaException {
    SubscriptionDiagnosticsTypeNode node = getSubscriptionDiagnosticsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SubscriptionDiagnostics (declaration i=12784, owner i=2171)"
              + " on "
              + getNodeId());
    }
    node.setValue(
        new Variant(encodeValue(value, SubscriptionDiagnosticsDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable SubscriptionDiagnosticsDataType readSubscriptionDiagnostics()
      throws UaException {
    return ClientMembers.await(readSubscriptionDiagnosticsAsync(), false);
  }

  @Override
  public void writeSubscriptionDiagnostics(@Nullable SubscriptionDiagnosticsDataType value)
      throws UaException {
    try {
      StatusCode statusCode = writeSubscriptionDiagnosticsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable SubscriptionDiagnosticsDataType>
      readSubscriptionDiagnosticsAsync() {
    return getSubscriptionDiagnosticsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SubscriptionDiagnostics (declaration i=12784,"
                            + " owner i=2171) on "
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
                return (SubscriptionDiagnosticsDataType)
                    decodeValue(
                        v.getValue().getValue(),
                        SubscriptionDiagnosticsDataType.class,
                        ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeSubscriptionDiagnosticsAsync(
      @Nullable SubscriptionDiagnosticsDataType subscriptionDiagnostics) {
    return getSubscriptionDiagnosticsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SubscriptionDiagnostics (declaration i=12784,"
                            + " owner i=2171) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                subscriptionDiagnostics,
                                SubscriptionDiagnosticsDataType.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public SubscriptionDiagnosticsTypeNode getSubscriptionDiagnosticsNode() throws UaException {
    return ClientMembers.await(getSubscriptionDiagnosticsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends SubscriptionDiagnosticsTypeNode>
      getSubscriptionDiagnosticsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        SubscriptionDiagnosticsTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SubscriptionDiagnostics",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SubscriptionDiagnostics (declaration i=12784, owner"
                + " i=2171)"));
  }
}
