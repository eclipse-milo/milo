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
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;
import org.eclipse.milo.opcua.stack.core.types.structured.UserIdentityToken;
import org.jspecify.annotations.Nullable;

public class AuditActivateSessionEventTypeNode extends AuditSessionEventTypeNode
    implements AuditActivateSessionEventType {
  public AuditActivateSessionEventTypeNode(
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
  public static ClientViews createViews(AuditActivateSessionEventTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable SignedSoftwareCertificate @Nullable [] getClientSoftwareCertificates()
      throws UaException {
    PropertyTypeNode node = getClientSoftwareCertificatesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ClientSoftwareCertificates (declaration i=2076, owner"
              + " i=2075) on "
              + getNodeId());
    }
    return (SignedSoftwareCertificate[])
        decodeValue(
            node.getValue().getValue().getValue(),
            SignedSoftwareCertificate.class,
            ValueRanks.OneDimension);
  }

  @Override
  public void setClientSoftwareCertificates(@Nullable SignedSoftwareCertificate @Nullable [] value)
      throws UaException {
    PropertyTypeNode node = getClientSoftwareCertificatesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ClientSoftwareCertificates (declaration i=2076, owner"
              + " i=2075) on "
              + getNodeId());
    }
    node.setValue(
        new Variant(encodeValue(value, SignedSoftwareCertificate.class, ValueRanks.OneDimension)));
  }

  @Override
  public @Nullable SignedSoftwareCertificate @Nullable [] readClientSoftwareCertificates()
      throws UaException {
    return ClientMembers.await(readClientSoftwareCertificatesAsync(), false);
  }

  @Override
  public void writeClientSoftwareCertificates(
      @Nullable SignedSoftwareCertificate @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeClientSoftwareCertificatesAsync(value).get();
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
  public CompletableFuture<? extends @Nullable SignedSoftwareCertificate @Nullable []>
      readClientSoftwareCertificatesAsync() {
    return getClientSoftwareCertificatesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ClientSoftwareCertificates (declaration"
                            + " i=2076, owner i=2075) on "
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
                return (SignedSoftwareCertificate[])
                    decodeValue(
                        v.getValue().getValue(),
                        SignedSoftwareCertificate.class,
                        ValueRanks.OneDimension);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeClientSoftwareCertificatesAsync(
      @Nullable SignedSoftwareCertificate @Nullable [] clientSoftwareCertificates) {
    return getClientSoftwareCertificatesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ClientSoftwareCertificates (declaration"
                            + " i=2076, owner i=2075) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                clientSoftwareCertificates,
                                SignedSoftwareCertificate.class,
                                ValueRanks.OneDimension)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getClientSoftwareCertificatesNode() throws UaException {
    return ClientMembers.await(getClientSoftwareCertificatesNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getClientSoftwareCertificatesNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ClientSoftwareCertificates",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ClientSoftwareCertificates (declaration i=2076, owner"
                + " i=2075)"));
  }

  @Override
  public @Nullable UserIdentityToken getUserIdentityToken() throws UaException {
    PropertyTypeNode node = getUserIdentityTokenNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UserIdentityToken (declaration i=2077, owner i=2075)"
              + " on "
              + getNodeId());
    }
    return (UserIdentityToken)
        decodeValue(
            node.getValue().getValue().getValue(), UserIdentityToken.class, ValueRanks.Scalar);
  }

  @Override
  public void setUserIdentityToken(@Nullable UserIdentityToken value) throws UaException {
    PropertyTypeNode node = getUserIdentityTokenNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UserIdentityToken (declaration i=2077, owner i=2075)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, UserIdentityToken.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable UserIdentityToken readUserIdentityToken() throws UaException {
    return ClientMembers.await(readUserIdentityTokenAsync(), false);
  }

  @Override
  public void writeUserIdentityToken(@Nullable UserIdentityToken value) throws UaException {
    try {
      StatusCode statusCode = writeUserIdentityTokenAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UserIdentityToken> readUserIdentityTokenAsync() {
    return getUserIdentityTokenNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:UserIdentityToken (declaration i=2077, owner"
                            + " i=2075) on "
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
                return (UserIdentityToken)
                    decodeValue(
                        v.getValue().getValue(), UserIdentityToken.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeUserIdentityTokenAsync(
      @Nullable UserIdentityToken userIdentityToken) {
    return getUserIdentityTokenNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:UserIdentityToken (declaration i=2077, owner"
                            + " i=2075) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                userIdentityToken, UserIdentityToken.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getUserIdentityTokenNode() throws UaException {
    return ClientMembers.await(getUserIdentityTokenNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUserIdentityTokenNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "UserIdentityToken",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:UserIdentityToken (declaration i=2077, owner i=2075)"));
  }

  @Override
  public @Nullable String getSecureChannelId() throws UaException {
    PropertyTypeNode node = getSecureChannelIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SecureChannelId (declaration i=11485, owner i=2075)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setSecureChannelId(@Nullable String value) throws UaException {
    PropertyTypeNode node = getSecureChannelIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SecureChannelId (declaration i=11485, owner i=2075)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String readSecureChannelId() throws UaException {
    return ClientMembers.await(readSecureChannelIdAsync(), false);
  }

  @Override
  public void writeSecureChannelId(@Nullable String value) throws UaException {
    try {
      StatusCode statusCode = writeSecureChannelIdAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String> readSecureChannelIdAsync() {
    return getSecureChannelIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SecureChannelId (declaration i=11485, owner"
                            + " i=2075) on "
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
                return (String) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeSecureChannelIdAsync(@Nullable String secureChannelId) {
    return getSecureChannelIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SecureChannelId (declaration i=11485, owner"
                            + " i=2075) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(secureChannelId));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getSecureChannelIdNode() throws UaException {
    return ClientMembers.await(getSecureChannelIdNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSecureChannelIdNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SecureChannelId",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SecureChannelId (declaration i=11485, owner i=2075)"));
  }

  @Override
  public @Nullable NodeId @Nullable [] getCurrentRoleIds() throws UaException {
    PropertyTypeNode node = getCurrentRoleIdsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CurrentRoleIds (declaration i=19304, owner i=2075)"
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
          "http://opcfoundation.org/UA/:CurrentRoleIds (declaration i=19304, owner i=2075)"
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
                        "http://opcfoundation.org/UA/:CurrentRoleIds (declaration i=19304, owner"
                            + " i=2075) on "
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
                        "http://opcfoundation.org/UA/:CurrentRoleIds (declaration i=19304, owner"
                            + " i=2075) on "
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
            "http://opcfoundation.org/UA/:CurrentRoleIds (declaration i=19304, owner i=2075)"));
  }
}
