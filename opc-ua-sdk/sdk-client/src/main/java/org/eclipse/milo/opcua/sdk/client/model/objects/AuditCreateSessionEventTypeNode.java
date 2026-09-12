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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
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
import org.jspecify.annotations.Nullable;

public class AuditCreateSessionEventTypeNode extends AuditSessionEventTypeNode
    implements AuditCreateSessionEventType {
  public AuditCreateSessionEventTypeNode(
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
  public static ClientViews createViews(AuditCreateSessionEventTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable String getSecureChannelId() throws UaException {
    PropertyTypeNode node = getSecureChannelIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SecureChannelId (declaration i=2072, owner i=2071)"
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
          "http://opcfoundation.org/UA/:SecureChannelId (declaration i=2072, owner i=2071)"
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
                        "http://opcfoundation.org/UA/:SecureChannelId (declaration i=2072, owner"
                            + " i=2071) on "
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
                        "http://opcfoundation.org/UA/:SecureChannelId (declaration i=2072, owner"
                            + " i=2071) on "
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
            "http://opcfoundation.org/UA/:SecureChannelId (declaration i=2072, owner i=2071)"));
  }

  @Override
  public @Nullable ByteString getClientCertificate() throws UaException {
    PropertyTypeNode node = getClientCertificateNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ClientCertificate (declaration i=2073, owner i=2071)"
              + " on "
              + getNodeId());
    }
    return (ByteString) node.getValue().getValue().getValue();
  }

  @Override
  public void setClientCertificate(@Nullable ByteString value) throws UaException {
    PropertyTypeNode node = getClientCertificateNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ClientCertificate (declaration i=2073, owner i=2071)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable ByteString readClientCertificate() throws UaException {
    return ClientMembers.await(readClientCertificateAsync(), false);
  }

  @Override
  public void writeClientCertificate(@Nullable ByteString value) throws UaException {
    try {
      StatusCode statusCode = writeClientCertificateAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ByteString> readClientCertificateAsync() {
    return getClientCertificateNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ClientCertificate (declaration i=2073, owner"
                            + " i=2071) on "
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
                return (ByteString) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeClientCertificateAsync(
      @Nullable ByteString clientCertificate) {
    return getClientCertificateNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ClientCertificate (declaration i=2073, owner"
                            + " i=2071) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(clientCertificate));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getClientCertificateNode() throws UaException {
    return ClientMembers.await(getClientCertificateNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getClientCertificateNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ClientCertificate",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ClientCertificate (declaration i=2073, owner i=2071)"));
  }

  @Override
  public @Nullable String getClientCertificateThumbprint() throws UaException {
    PropertyTypeNode node = getClientCertificateThumbprintNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ClientCertificateThumbprint (declaration i=2747, owner"
              + " i=2071) on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setClientCertificateThumbprint(@Nullable String value) throws UaException {
    PropertyTypeNode node = getClientCertificateThumbprintNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ClientCertificateThumbprint (declaration i=2747, owner"
              + " i=2071) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String readClientCertificateThumbprint() throws UaException {
    return ClientMembers.await(readClientCertificateThumbprintAsync(), false);
  }

  @Override
  public void writeClientCertificateThumbprint(@Nullable String value) throws UaException {
    try {
      StatusCode statusCode = writeClientCertificateThumbprintAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String> readClientCertificateThumbprintAsync() {
    return getClientCertificateThumbprintNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ClientCertificateThumbprint (declaration"
                            + " i=2747, owner i=2071) on "
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
  public CompletableFuture<StatusCode> writeClientCertificateThumbprintAsync(
      @Nullable String clientCertificateThumbprint) {
    return getClientCertificateThumbprintNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ClientCertificateThumbprint (declaration"
                            + " i=2747, owner i=2071) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(clientCertificateThumbprint));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getClientCertificateThumbprintNode() throws UaException {
    return ClientMembers.await(getClientCertificateThumbprintNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getClientCertificateThumbprintNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ClientCertificateThumbprint",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ClientCertificateThumbprint (declaration i=2747, owner"
                + " i=2071)"));
  }

  @Override
  public @Nullable Double getRevisedSessionTimeout() throws UaException {
    PropertyTypeNode node = getRevisedSessionTimeoutNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RevisedSessionTimeout (declaration i=2074, owner i=2071)"
              + " on "
              + getNodeId());
    }
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setRevisedSessionTimeout(@Nullable Double value) throws UaException {
    PropertyTypeNode node = getRevisedSessionTimeoutNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RevisedSessionTimeout (declaration i=2074, owner i=2071)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Double readRevisedSessionTimeout() throws UaException {
    return ClientMembers.await(readRevisedSessionTimeoutAsync(), false);
  }

  @Override
  public void writeRevisedSessionTimeout(@Nullable Double value) throws UaException {
    try {
      StatusCode statusCode = writeRevisedSessionTimeoutAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Double> readRevisedSessionTimeoutAsync() {
    return getRevisedSessionTimeoutNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:RevisedSessionTimeout (declaration i=2074,"
                            + " owner i=2071) on "
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
                return (Double) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeRevisedSessionTimeoutAsync(
      @Nullable Double revisedSessionTimeout) {
    return getRevisedSessionTimeoutNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:RevisedSessionTimeout (declaration i=2074,"
                            + " owner i=2071) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(revisedSessionTimeout));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getRevisedSessionTimeoutNode() throws UaException {
    return ClientMembers.await(getRevisedSessionTimeoutNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getRevisedSessionTimeoutNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RevisedSessionTimeout",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:RevisedSessionTimeout (declaration i=2074, owner"
                + " i=2071)"));
  }
}
