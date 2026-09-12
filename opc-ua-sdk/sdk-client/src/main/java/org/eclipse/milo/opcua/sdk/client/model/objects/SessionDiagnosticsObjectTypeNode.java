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
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionDiagnosticsVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionSecurityDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.SubscriptionDiagnosticsArrayTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionSecurityDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;
import org.jspecify.annotations.Nullable;

public class SessionDiagnosticsObjectTypeNode extends BaseObjectTypeNode
    implements SessionDiagnosticsObjectType {
  public SessionDiagnosticsObjectTypeNode(
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
  public static ClientViews createViews(SessionDiagnosticsObjectTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable NodeId @Nullable [] getCurrentRoleIds() throws UaException {
    PropertyTypeNode node = getCurrentRoleIdsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CurrentRoleIds (declaration i=19303, owner i=2029)"
              + " on "
              + getNodeId());
    }
    return (NodeId[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setCurrentRoleIds(@Nullable NodeId @Nullable [] value) throws UaException {
    PropertyTypeNode node = getCurrentRoleIdsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CurrentRoleIds (declaration i=19303, owner i=2029)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable NodeId @Nullable [] readCurrentRoleIds() throws UaException {
    return ClientMembers.await(readCurrentRoleIdsAsync(), false);
  }

  @Override
  public void writeCurrentRoleIds(@Nullable NodeId @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeCurrentRoleIdsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable NodeId @Nullable []> readCurrentRoleIdsAsync() {
    return getCurrentRoleIdsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CurrentRoleIds (declaration i=19303, owner"
                            + " i=2029) on "
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
                return (NodeId[]) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeCurrentRoleIdsAsync(
      @Nullable NodeId @Nullable [] currentRoleIds) {
    return getCurrentRoleIdsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CurrentRoleIds (declaration i=19303, owner"
                            + " i=2029) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(currentRoleIds));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getCurrentRoleIdsNode() throws UaException {
    return ClientMembers.await(getCurrentRoleIdsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getCurrentRoleIdsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CurrentRoleIds",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:CurrentRoleIds (declaration i=19303, owner i=2029)"));
  }

  @Override
  public @Nullable SessionDiagnosticsDataType getSessionDiagnostics() throws UaException {
    SessionDiagnosticsVariableTypeNode node = getSessionDiagnosticsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SessionDiagnostics (declaration i=2030, owner i=2029)"
              + " on "
              + getNodeId());
    }
    return (SessionDiagnosticsDataType)
        decodeValue(
            node.getValue().getValue().getValue(),
            SessionDiagnosticsDataType.class,
            ValueRanks.Scalar);
  }

  @Override
  public void setSessionDiagnostics(@Nullable SessionDiagnosticsDataType value) throws UaException {
    SessionDiagnosticsVariableTypeNode node = getSessionDiagnosticsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SessionDiagnostics (declaration i=2030, owner i=2029)"
              + " on "
              + getNodeId());
    }
    node.setValue(
        new Variant(encodeValue(value, SessionDiagnosticsDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable SessionDiagnosticsDataType readSessionDiagnostics() throws UaException {
    return ClientMembers.await(readSessionDiagnosticsAsync(), false);
  }

  @Override
  public void writeSessionDiagnostics(@Nullable SessionDiagnosticsDataType value)
      throws UaException {
    try {
      StatusCode statusCode = writeSessionDiagnosticsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable SessionDiagnosticsDataType>
      readSessionDiagnosticsAsync() {
    return getSessionDiagnosticsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SessionDiagnostics (declaration i=2030, owner"
                            + " i=2029) on "
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
                return (SessionDiagnosticsDataType)
                    decodeValue(
                        v.getValue().getValue(),
                        SessionDiagnosticsDataType.class,
                        ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeSessionDiagnosticsAsync(
      @Nullable SessionDiagnosticsDataType sessionDiagnostics) {
    return getSessionDiagnosticsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SessionDiagnostics (declaration i=2030, owner"
                            + " i=2029) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                sessionDiagnostics,
                                SessionDiagnosticsDataType.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public SessionDiagnosticsVariableTypeNode getSessionDiagnosticsNode() throws UaException {
    return ClientMembers.await(getSessionDiagnosticsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends SessionDiagnosticsVariableTypeNode>
      getSessionDiagnosticsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        SessionDiagnosticsVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SessionDiagnostics",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SessionDiagnostics (declaration i=2030, owner i=2029)"));
  }

  @Override
  public @Nullable SessionSecurityDiagnosticsDataType getSessionSecurityDiagnostics()
      throws UaException {
    SessionSecurityDiagnosticsTypeNode node = getSessionSecurityDiagnosticsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SessionSecurityDiagnostics (declaration i=2031, owner"
              + " i=2029) on "
              + getNodeId());
    }
    return (SessionSecurityDiagnosticsDataType)
        decodeValue(
            node.getValue().getValue().getValue(),
            SessionSecurityDiagnosticsDataType.class,
            ValueRanks.Scalar);
  }

  @Override
  public void setSessionSecurityDiagnostics(@Nullable SessionSecurityDiagnosticsDataType value)
      throws UaException {
    SessionSecurityDiagnosticsTypeNode node = getSessionSecurityDiagnosticsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SessionSecurityDiagnostics (declaration i=2031, owner"
              + " i=2029) on "
              + getNodeId());
    }
    node.setValue(
        new Variant(
            encodeValue(value, SessionSecurityDiagnosticsDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable SessionSecurityDiagnosticsDataType readSessionSecurityDiagnostics()
      throws UaException {
    return ClientMembers.await(readSessionSecurityDiagnosticsAsync(), false);
  }

  @Override
  public void writeSessionSecurityDiagnostics(@Nullable SessionSecurityDiagnosticsDataType value)
      throws UaException {
    try {
      StatusCode statusCode = writeSessionSecurityDiagnosticsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable SessionSecurityDiagnosticsDataType>
      readSessionSecurityDiagnosticsAsync() {
    return getSessionSecurityDiagnosticsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SessionSecurityDiagnostics (declaration"
                            + " i=2031, owner i=2029) on "
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
                return (SessionSecurityDiagnosticsDataType)
                    decodeValue(
                        v.getValue().getValue(),
                        SessionSecurityDiagnosticsDataType.class,
                        ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeSessionSecurityDiagnosticsAsync(
      @Nullable SessionSecurityDiagnosticsDataType sessionSecurityDiagnostics) {
    return getSessionSecurityDiagnosticsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SessionSecurityDiagnostics (declaration"
                            + " i=2031, owner i=2029) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                sessionSecurityDiagnostics,
                                SessionSecurityDiagnosticsDataType.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public SessionSecurityDiagnosticsTypeNode getSessionSecurityDiagnosticsNode() throws UaException {
    return ClientMembers.await(getSessionSecurityDiagnosticsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends SessionSecurityDiagnosticsTypeNode>
      getSessionSecurityDiagnosticsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        SessionSecurityDiagnosticsTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SessionSecurityDiagnostics",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SessionSecurityDiagnostics (declaration i=2031, owner"
                + " i=2029)"));
  }

  @Override
  public @Nullable SubscriptionDiagnosticsDataType @Nullable [] getSubscriptionDiagnosticsArray()
      throws UaException {
    SubscriptionDiagnosticsArrayTypeNode node = getSubscriptionDiagnosticsArrayNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SubscriptionDiagnosticsArray (declaration i=2032, owner"
              + " i=2029) on "
              + getNodeId());
    }
    return (SubscriptionDiagnosticsDataType[])
        decodeValue(
            node.getValue().getValue().getValue(),
            SubscriptionDiagnosticsDataType.class,
            ValueRanks.OneDimension);
  }

  @Override
  public void setSubscriptionDiagnosticsArray(
      @Nullable SubscriptionDiagnosticsDataType @Nullable [] value) throws UaException {
    SubscriptionDiagnosticsArrayTypeNode node = getSubscriptionDiagnosticsArrayNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SubscriptionDiagnosticsArray (declaration i=2032, owner"
              + " i=2029) on "
              + getNodeId());
    }
    node.setValue(
        new Variant(
            encodeValue(value, SubscriptionDiagnosticsDataType.class, ValueRanks.OneDimension)));
  }

  @Override
  public @Nullable SubscriptionDiagnosticsDataType @Nullable [] readSubscriptionDiagnosticsArray()
      throws UaException {
    return ClientMembers.await(readSubscriptionDiagnosticsArrayAsync(), false);
  }

  @Override
  public void writeSubscriptionDiagnosticsArray(
      @Nullable SubscriptionDiagnosticsDataType @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeSubscriptionDiagnosticsArrayAsync(value).get();
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
  public CompletableFuture<? extends @Nullable SubscriptionDiagnosticsDataType @Nullable []>
      readSubscriptionDiagnosticsArrayAsync() {
    return getSubscriptionDiagnosticsArrayNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SubscriptionDiagnosticsArray (declaration"
                            + " i=2032, owner i=2029) on "
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
                return (SubscriptionDiagnosticsDataType[])
                    decodeValue(
                        v.getValue().getValue(),
                        SubscriptionDiagnosticsDataType.class,
                        ValueRanks.OneDimension);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeSubscriptionDiagnosticsArrayAsync(
      @Nullable SubscriptionDiagnosticsDataType @Nullable [] subscriptionDiagnosticsArray) {
    return getSubscriptionDiagnosticsArrayNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SubscriptionDiagnosticsArray (declaration"
                            + " i=2032, owner i=2029) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                subscriptionDiagnosticsArray,
                                SubscriptionDiagnosticsDataType.class,
                                ValueRanks.OneDimension)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public SubscriptionDiagnosticsArrayTypeNode getSubscriptionDiagnosticsArrayNode()
      throws UaException {
    return ClientMembers.await(getSubscriptionDiagnosticsArrayNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends SubscriptionDiagnosticsArrayTypeNode>
      getSubscriptionDiagnosticsArrayNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        SubscriptionDiagnosticsArrayTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SubscriptionDiagnosticsArray",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SubscriptionDiagnosticsArray (declaration i=2032, owner"
                + " i=2029)"));
  }
}
