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
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceCounterDataType;
import org.jspecify.annotations.Nullable;

public class SessionDiagnosticsVariableTypeNode extends BaseDataVariableTypeNode
    implements SessionDiagnosticsVariableType {
  public SessionDiagnosticsVariableTypeNode(
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
  public @Nullable NodeId getSessionId() throws UaException {
    BaseDataVariableTypeNode node = getSessionIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SessionId (declaration i=2198, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setSessionId(@Nullable NodeId value) throws UaException {
    BaseDataVariableTypeNode node = getSessionIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SessionId (declaration i=2198, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable NodeId readSessionId() throws UaException {
    return ClientMembers.await(readSessionIdAsync(), false);
  }

  @Override
  public void writeSessionId(@Nullable NodeId value) throws UaException {
    try {
      StatusCode statusCode = writeSessionIdAsync(value).get();
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
  public CompletableFuture<? extends @Nullable NodeId> readSessionIdAsync() {
    return getSessionIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SessionId (declaration i=2198, owner i=2197)"
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
                return (NodeId) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeSessionIdAsync(@Nullable NodeId sessionId) {
    return getSessionIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SessionId (declaration i=2198, owner i=2197)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(sessionId));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getSessionIdNode() throws UaException {
    return ClientMembers.await(getSessionIdNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getSessionIdNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SessionId",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SessionId (declaration i=2198, owner i=2197)"));
  }

  @Override
  public @Nullable String getSessionName() throws UaException {
    BaseDataVariableTypeNode node = getSessionNameNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SessionName (declaration i=2199, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setSessionName(@Nullable String value) throws UaException {
    BaseDataVariableTypeNode node = getSessionNameNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SessionName (declaration i=2199, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String readSessionName() throws UaException {
    return ClientMembers.await(readSessionNameAsync(), false);
  }

  @Override
  public void writeSessionName(@Nullable String value) throws UaException {
    try {
      StatusCode statusCode = writeSessionNameAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String> readSessionNameAsync() {
    return getSessionNameNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SessionName (declaration i=2199, owner"
                            + " i=2197) on "
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
  public CompletableFuture<StatusCode> writeSessionNameAsync(@Nullable String sessionName) {
    return getSessionNameNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SessionName (declaration i=2199, owner"
                            + " i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(sessionName));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getSessionNameNode() throws UaException {
    return ClientMembers.await(getSessionNameNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getSessionNameNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SessionName",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SessionName (declaration i=2199, owner i=2197)"));
  }

  @Override
  public @Nullable ApplicationDescription getClientDescription() throws UaException {
    BaseDataVariableTypeNode node = getClientDescriptionNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ClientDescription (declaration i=2200, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ApplicationDescription)
        decodeValue(
            node.getValue().getValue().getValue(), ApplicationDescription.class, ValueRanks.Scalar);
  }

  @Override
  public void setClientDescription(@Nullable ApplicationDescription value) throws UaException {
    BaseDataVariableTypeNode node = getClientDescriptionNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ClientDescription (declaration i=2200, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ApplicationDescription.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ApplicationDescription readClientDescription() throws UaException {
    return ClientMembers.await(readClientDescriptionAsync(), false);
  }

  @Override
  public void writeClientDescription(@Nullable ApplicationDescription value) throws UaException {
    try {
      StatusCode statusCode = writeClientDescriptionAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ApplicationDescription>
      readClientDescriptionAsync() {
    return getClientDescriptionNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ClientDescription (declaration i=2200, owner"
                            + " i=2197) on "
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
                return (ApplicationDescription)
                    decodeValue(
                        v.getValue().getValue(), ApplicationDescription.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeClientDescriptionAsync(
      @Nullable ApplicationDescription clientDescription) {
    return getClientDescriptionNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ClientDescription (declaration i=2200, owner"
                            + " i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                clientDescription,
                                ApplicationDescription.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getClientDescriptionNode() throws UaException {
    return ClientMembers.await(getClientDescriptionNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getClientDescriptionNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ClientDescription",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ClientDescription (declaration i=2200, owner i=2197)"));
  }

  @Override
  public @Nullable String getServerUri() throws UaException {
    BaseDataVariableTypeNode node = getServerUriNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServerUri (declaration i=2201, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setServerUri(@Nullable String value) throws UaException {
    BaseDataVariableTypeNode node = getServerUriNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServerUri (declaration i=2201, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String readServerUri() throws UaException {
    return ClientMembers.await(readServerUriAsync(), false);
  }

  @Override
  public void writeServerUri(@Nullable String value) throws UaException {
    try {
      StatusCode statusCode = writeServerUriAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String> readServerUriAsync() {
    return getServerUriNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ServerUri (declaration i=2201, owner i=2197)"
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
                return (String) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeServerUriAsync(@Nullable String serverUri) {
    return getServerUriNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ServerUri (declaration i=2201, owner i=2197)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(serverUri));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getServerUriNode() throws UaException {
    return ClientMembers.await(getServerUriNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getServerUriNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ServerUri",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ServerUri (declaration i=2201, owner i=2197)"));
  }

  @Override
  public @Nullable String getEndpointUrl() throws UaException {
    BaseDataVariableTypeNode node = getEndpointUrlNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EndpointUrl (declaration i=2202, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setEndpointUrl(@Nullable String value) throws UaException {
    BaseDataVariableTypeNode node = getEndpointUrlNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EndpointUrl (declaration i=2202, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String readEndpointUrl() throws UaException {
    return ClientMembers.await(readEndpointUrlAsync(), false);
  }

  @Override
  public void writeEndpointUrl(@Nullable String value) throws UaException {
    try {
      StatusCode statusCode = writeEndpointUrlAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String> readEndpointUrlAsync() {
    return getEndpointUrlNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EndpointUrl (declaration i=2202, owner"
                            + " i=2197) on "
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
  public CompletableFuture<StatusCode> writeEndpointUrlAsync(@Nullable String endpointUrl) {
    return getEndpointUrlNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EndpointUrl (declaration i=2202, owner"
                            + " i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(endpointUrl));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getEndpointUrlNode() throws UaException {
    return ClientMembers.await(getEndpointUrlNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getEndpointUrlNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "EndpointUrl",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:EndpointUrl (declaration i=2202, owner i=2197)"));
  }

  @Override
  public @Nullable String @Nullable [] getLocaleIds() throws UaException {
    BaseDataVariableTypeNode node = getLocaleIdsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LocaleIds (declaration i=2203, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (String[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setLocaleIds(@Nullable String @Nullable [] value) throws UaException {
    BaseDataVariableTypeNode node = getLocaleIdsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LocaleIds (declaration i=2203, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String @Nullable [] readLocaleIds() throws UaException {
    return ClientMembers.await(readLocaleIdsAsync(), false);
  }

  @Override
  public void writeLocaleIds(@Nullable String @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeLocaleIdsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String @Nullable []> readLocaleIdsAsync() {
    return getLocaleIdsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LocaleIds (declaration i=2203, owner i=2197)"
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
                return (String[]) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeLocaleIdsAsync(
      @Nullable String @Nullable [] localeIds) {
    return getLocaleIdsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LocaleIds (declaration i=2203, owner i=2197)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(localeIds));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getLocaleIdsNode() throws UaException {
    return ClientMembers.await(getLocaleIdsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getLocaleIdsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "LocaleIds",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:LocaleIds (declaration i=2203, owner i=2197)"));
  }

  @Override
  public @Nullable Double getActualSessionTimeout() throws UaException {
    BaseDataVariableTypeNode node = getActualSessionTimeoutNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ActualSessionTimeout (declaration i=2204, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setActualSessionTimeout(@Nullable Double value) throws UaException {
    BaseDataVariableTypeNode node = getActualSessionTimeoutNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ActualSessionTimeout (declaration i=2204, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Double readActualSessionTimeout() throws UaException {
    return ClientMembers.await(readActualSessionTimeoutAsync(), false);
  }

  @Override
  public void writeActualSessionTimeout(@Nullable Double value) throws UaException {
    try {
      StatusCode statusCode = writeActualSessionTimeoutAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Double> readActualSessionTimeoutAsync() {
    return getActualSessionTimeoutNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ActualSessionTimeout (declaration i=2204,"
                            + " owner i=2197) on "
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
  public CompletableFuture<StatusCode> writeActualSessionTimeoutAsync(
      @Nullable Double actualSessionTimeout) {
    return getActualSessionTimeoutNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ActualSessionTimeout (declaration i=2204,"
                            + " owner i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(actualSessionTimeout));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getActualSessionTimeoutNode() throws UaException {
    return ClientMembers.await(getActualSessionTimeoutNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getActualSessionTimeoutNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ActualSessionTimeout",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ActualSessionTimeout (declaration i=2204, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable UInteger getMaxResponseMessageSize() throws UaException {
    BaseDataVariableTypeNode node = getMaxResponseMessageSizeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxResponseMessageSize (declaration i=3050, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxResponseMessageSize(@Nullable UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getMaxResponseMessageSizeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxResponseMessageSize (declaration i=3050, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxResponseMessageSize() throws UaException {
    return ClientMembers.await(readMaxResponseMessageSizeAsync(), false);
  }

  @Override
  public void writeMaxResponseMessageSize(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxResponseMessageSizeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxResponseMessageSizeAsync() {
    return getMaxResponseMessageSizeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxResponseMessageSize (declaration i=3050,"
                            + " owner i=2197) on "
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
                return (UInteger) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxResponseMessageSizeAsync(
      @Nullable UInteger maxResponseMessageSize) {
    return getMaxResponseMessageSizeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxResponseMessageSize (declaration i=3050,"
                            + " owner i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxResponseMessageSize));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getMaxResponseMessageSizeNode() throws UaException {
    return ClientMembers.await(getMaxResponseMessageSizeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getMaxResponseMessageSizeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxResponseMessageSize",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxResponseMessageSize (declaration i=3050, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable DateTime getClientConnectionTime() throws UaException {
    BaseDataVariableTypeNode node = getClientConnectionTimeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ClientConnectionTime (declaration i=2205, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setClientConnectionTime(@Nullable DateTime value) throws UaException {
    BaseDataVariableTypeNode node = getClientConnectionTimeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ClientConnectionTime (declaration i=2205, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable DateTime readClientConnectionTime() throws UaException {
    return ClientMembers.await(readClientConnectionTimeAsync(), false);
  }

  @Override
  public void writeClientConnectionTime(@Nullable DateTime value) throws UaException {
    try {
      StatusCode statusCode = writeClientConnectionTimeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable DateTime> readClientConnectionTimeAsync() {
    return getClientConnectionTimeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ClientConnectionTime (declaration i=2205,"
                            + " owner i=2197) on "
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
                return (DateTime) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeClientConnectionTimeAsync(
      @Nullable DateTime clientConnectionTime) {
    return getClientConnectionTimeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ClientConnectionTime (declaration i=2205,"
                            + " owner i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(clientConnectionTime));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getClientConnectionTimeNode() throws UaException {
    return ClientMembers.await(getClientConnectionTimeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getClientConnectionTimeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ClientConnectionTime",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ClientConnectionTime (declaration i=2205, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable DateTime getClientLastContactTime() throws UaException {
    BaseDataVariableTypeNode node = getClientLastContactTimeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ClientLastContactTime (declaration i=2206, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setClientLastContactTime(@Nullable DateTime value) throws UaException {
    BaseDataVariableTypeNode node = getClientLastContactTimeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ClientLastContactTime (declaration i=2206, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable DateTime readClientLastContactTime() throws UaException {
    return ClientMembers.await(readClientLastContactTimeAsync(), false);
  }

  @Override
  public void writeClientLastContactTime(@Nullable DateTime value) throws UaException {
    try {
      StatusCode statusCode = writeClientLastContactTimeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable DateTime> readClientLastContactTimeAsync() {
    return getClientLastContactTimeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ClientLastContactTime (declaration i=2206,"
                            + " owner i=2197) on "
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
                return (DateTime) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeClientLastContactTimeAsync(
      @Nullable DateTime clientLastContactTime) {
    return getClientLastContactTimeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ClientLastContactTime (declaration i=2206,"
                            + " owner i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(clientLastContactTime));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getClientLastContactTimeNode() throws UaException {
    return ClientMembers.await(getClientLastContactTimeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getClientLastContactTimeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ClientLastContactTime",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ClientLastContactTime (declaration i=2206, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable UInteger getCurrentSubscriptionsCount() throws UaException {
    BaseDataVariableTypeNode node = getCurrentSubscriptionsCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CurrentSubscriptionsCount (declaration i=2207, owner"
              + " i=2197) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setCurrentSubscriptionsCount(@Nullable UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getCurrentSubscriptionsCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CurrentSubscriptionsCount (declaration i=2207, owner"
              + " i=2197) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readCurrentSubscriptionsCount() throws UaException {
    return ClientMembers.await(readCurrentSubscriptionsCountAsync(), false);
  }

  @Override
  public void writeCurrentSubscriptionsCount(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeCurrentSubscriptionsCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readCurrentSubscriptionsCountAsync() {
    return getCurrentSubscriptionsCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CurrentSubscriptionsCount (declaration"
                            + " i=2207, owner i=2197) on "
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
                return (UInteger) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeCurrentSubscriptionsCountAsync(
      @Nullable UInteger currentSubscriptionsCount) {
    return getCurrentSubscriptionsCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CurrentSubscriptionsCount (declaration"
                            + " i=2207, owner i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(currentSubscriptionsCount));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getCurrentSubscriptionsCountNode() throws UaException {
    return ClientMembers.await(getCurrentSubscriptionsCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getCurrentSubscriptionsCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CurrentSubscriptionsCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:CurrentSubscriptionsCount (declaration i=2207, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable UInteger getCurrentMonitoredItemsCount() throws UaException {
    BaseDataVariableTypeNode node = getCurrentMonitoredItemsCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CurrentMonitoredItemsCount (declaration i=2208, owner"
              + " i=2197) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setCurrentMonitoredItemsCount(@Nullable UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getCurrentMonitoredItemsCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CurrentMonitoredItemsCount (declaration i=2208, owner"
              + " i=2197) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readCurrentMonitoredItemsCount() throws UaException {
    return ClientMembers.await(readCurrentMonitoredItemsCountAsync(), false);
  }

  @Override
  public void writeCurrentMonitoredItemsCount(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeCurrentMonitoredItemsCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readCurrentMonitoredItemsCountAsync() {
    return getCurrentMonitoredItemsCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CurrentMonitoredItemsCount (declaration"
                            + " i=2208, owner i=2197) on "
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
                return (UInteger) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeCurrentMonitoredItemsCountAsync(
      @Nullable UInteger currentMonitoredItemsCount) {
    return getCurrentMonitoredItemsCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CurrentMonitoredItemsCount (declaration"
                            + " i=2208, owner i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(currentMonitoredItemsCount));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getCurrentMonitoredItemsCountNode() throws UaException {
    return ClientMembers.await(getCurrentMonitoredItemsCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getCurrentMonitoredItemsCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CurrentMonitoredItemsCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:CurrentMonitoredItemsCount (declaration i=2208, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable UInteger getCurrentPublishRequestsInQueue() throws UaException {
    BaseDataVariableTypeNode node = getCurrentPublishRequestsInQueueNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CurrentPublishRequestsInQueue (declaration i=2209, owner"
              + " i=2197) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setCurrentPublishRequestsInQueue(@Nullable UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getCurrentPublishRequestsInQueueNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CurrentPublishRequestsInQueue (declaration i=2209, owner"
              + " i=2197) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readCurrentPublishRequestsInQueue() throws UaException {
    return ClientMembers.await(readCurrentPublishRequestsInQueueAsync(), false);
  }

  @Override
  public void writeCurrentPublishRequestsInQueue(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeCurrentPublishRequestsInQueueAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readCurrentPublishRequestsInQueueAsync() {
    return getCurrentPublishRequestsInQueueNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CurrentPublishRequestsInQueue (declaration"
                            + " i=2209, owner i=2197) on "
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
                return (UInteger) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeCurrentPublishRequestsInQueueAsync(
      @Nullable UInteger currentPublishRequestsInQueue) {
    return getCurrentPublishRequestsInQueueNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CurrentPublishRequestsInQueue (declaration"
                            + " i=2209, owner i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(currentPublishRequestsInQueue));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getCurrentPublishRequestsInQueueNode() throws UaException {
    return ClientMembers.await(getCurrentPublishRequestsInQueueNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getCurrentPublishRequestsInQueueNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CurrentPublishRequestsInQueue",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:CurrentPublishRequestsInQueue (declaration i=2209, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getTotalRequestCount() throws UaException {
    BaseDataVariableTypeNode node = getTotalRequestCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TotalRequestCount (declaration i=8900, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setTotalRequestCount(@Nullable ServiceCounterDataType value) throws UaException {
    BaseDataVariableTypeNode node = getTotalRequestCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TotalRequestCount (declaration i=8900, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readTotalRequestCount() throws UaException {
    return ClientMembers.await(readTotalRequestCountAsync(), false);
  }

  @Override
  public void writeTotalRequestCount(@Nullable ServiceCounterDataType value) throws UaException {
    try {
      StatusCode statusCode = writeTotalRequestCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readTotalRequestCountAsync() {
    return getTotalRequestCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:TotalRequestCount (declaration i=8900, owner"
                            + " i=2197) on "
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeTotalRequestCountAsync(
      @Nullable ServiceCounterDataType totalRequestCount) {
    return getTotalRequestCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:TotalRequestCount (declaration i=8900, owner"
                            + " i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                totalRequestCount,
                                ServiceCounterDataType.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getTotalRequestCountNode() throws UaException {
    return ClientMembers.await(getTotalRequestCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getTotalRequestCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "TotalRequestCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:TotalRequestCount (declaration i=8900, owner i=2197)"));
  }

  @Override
  public @Nullable UInteger getUnauthorizedRequestCount() throws UaException {
    BaseDataVariableTypeNode node = getUnauthorizedRequestCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UnauthorizedRequestCount (declaration i=11892, owner"
              + " i=2197) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setUnauthorizedRequestCount(@Nullable UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getUnauthorizedRequestCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UnauthorizedRequestCount (declaration i=11892, owner"
              + " i=2197) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readUnauthorizedRequestCount() throws UaException {
    return ClientMembers.await(readUnauthorizedRequestCountAsync(), false);
  }

  @Override
  public void writeUnauthorizedRequestCount(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeUnauthorizedRequestCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readUnauthorizedRequestCountAsync() {
    return getUnauthorizedRequestCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:UnauthorizedRequestCount (declaration"
                            + " i=11892, owner i=2197) on "
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
                return (UInteger) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeUnauthorizedRequestCountAsync(
      @Nullable UInteger unauthorizedRequestCount) {
    return getUnauthorizedRequestCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:UnauthorizedRequestCount (declaration"
                            + " i=11892, owner i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(unauthorizedRequestCount));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getUnauthorizedRequestCountNode() throws UaException {
    return ClientMembers.await(getUnauthorizedRequestCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getUnauthorizedRequestCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "UnauthorizedRequestCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:UnauthorizedRequestCount (declaration i=11892, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getReadCount() throws UaException {
    BaseDataVariableTypeNode node = getReadCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ReadCount (declaration i=2217, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setReadCount(@Nullable ServiceCounterDataType value) throws UaException {
    BaseDataVariableTypeNode node = getReadCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ReadCount (declaration i=2217, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readReadCount() throws UaException {
    return ClientMembers.await(readReadCountAsync(), false);
  }

  @Override
  public void writeReadCount(@Nullable ServiceCounterDataType value) throws UaException {
    try {
      StatusCode statusCode = writeReadCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType> readReadCountAsync() {
    return getReadCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ReadCount (declaration i=2217, owner i=2197)"
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeReadCountAsync(
      @Nullable ServiceCounterDataType readCount) {
    return getReadCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ReadCount (declaration i=2217, owner i=2197)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                readCount, ServiceCounterDataType.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getReadCountNode() throws UaException {
    return ClientMembers.await(getReadCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getReadCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ReadCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ReadCount (declaration i=2217, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getHistoryReadCount() throws UaException {
    BaseDataVariableTypeNode node = getHistoryReadCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:HistoryReadCount (declaration i=2218, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setHistoryReadCount(@Nullable ServiceCounterDataType value) throws UaException {
    BaseDataVariableTypeNode node = getHistoryReadCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:HistoryReadCount (declaration i=2218, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readHistoryReadCount() throws UaException {
    return ClientMembers.await(readHistoryReadCountAsync(), false);
  }

  @Override
  public void writeHistoryReadCount(@Nullable ServiceCounterDataType value) throws UaException {
    try {
      StatusCode statusCode = writeHistoryReadCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType> readHistoryReadCountAsync() {
    return getHistoryReadCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:HistoryReadCount (declaration i=2218, owner"
                            + " i=2197) on "
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeHistoryReadCountAsync(
      @Nullable ServiceCounterDataType historyReadCount) {
    return getHistoryReadCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:HistoryReadCount (declaration i=2218, owner"
                            + " i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                historyReadCount,
                                ServiceCounterDataType.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getHistoryReadCountNode() throws UaException {
    return ClientMembers.await(getHistoryReadCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getHistoryReadCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "HistoryReadCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:HistoryReadCount (declaration i=2218, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getWriteCount() throws UaException {
    BaseDataVariableTypeNode node = getWriteCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:WriteCount (declaration i=2219, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setWriteCount(@Nullable ServiceCounterDataType value) throws UaException {
    BaseDataVariableTypeNode node = getWriteCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:WriteCount (declaration i=2219, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readWriteCount() throws UaException {
    return ClientMembers.await(readWriteCountAsync(), false);
  }

  @Override
  public void writeWriteCount(@Nullable ServiceCounterDataType value) throws UaException {
    try {
      StatusCode statusCode = writeWriteCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType> readWriteCountAsync() {
    return getWriteCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:WriteCount (declaration i=2219, owner i=2197)"
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeWriteCountAsync(
      @Nullable ServiceCounterDataType writeCount) {
    return getWriteCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:WriteCount (declaration i=2219, owner i=2197)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                writeCount, ServiceCounterDataType.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getWriteCountNode() throws UaException {
    return ClientMembers.await(getWriteCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getWriteCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "WriteCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:WriteCount (declaration i=2219, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getHistoryUpdateCount() throws UaException {
    BaseDataVariableTypeNode node = getHistoryUpdateCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:HistoryUpdateCount (declaration i=2220, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setHistoryUpdateCount(@Nullable ServiceCounterDataType value) throws UaException {
    BaseDataVariableTypeNode node = getHistoryUpdateCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:HistoryUpdateCount (declaration i=2220, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readHistoryUpdateCount() throws UaException {
    return ClientMembers.await(readHistoryUpdateCountAsync(), false);
  }

  @Override
  public void writeHistoryUpdateCount(@Nullable ServiceCounterDataType value) throws UaException {
    try {
      StatusCode statusCode = writeHistoryUpdateCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readHistoryUpdateCountAsync() {
    return getHistoryUpdateCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:HistoryUpdateCount (declaration i=2220, owner"
                            + " i=2197) on "
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeHistoryUpdateCountAsync(
      @Nullable ServiceCounterDataType historyUpdateCount) {
    return getHistoryUpdateCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:HistoryUpdateCount (declaration i=2220, owner"
                            + " i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                historyUpdateCount,
                                ServiceCounterDataType.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getHistoryUpdateCountNode() throws UaException {
    return ClientMembers.await(getHistoryUpdateCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getHistoryUpdateCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "HistoryUpdateCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:HistoryUpdateCount (declaration i=2220, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getCallCount() throws UaException {
    BaseDataVariableTypeNode node = getCallCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CallCount (declaration i=2221, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setCallCount(@Nullable ServiceCounterDataType value) throws UaException {
    BaseDataVariableTypeNode node = getCallCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CallCount (declaration i=2221, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readCallCount() throws UaException {
    return ClientMembers.await(readCallCountAsync(), false);
  }

  @Override
  public void writeCallCount(@Nullable ServiceCounterDataType value) throws UaException {
    try {
      StatusCode statusCode = writeCallCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType> readCallCountAsync() {
    return getCallCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CallCount (declaration i=2221, owner i=2197)"
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeCallCountAsync(
      @Nullable ServiceCounterDataType callCount) {
    return getCallCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CallCount (declaration i=2221, owner i=2197)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                callCount, ServiceCounterDataType.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getCallCountNode() throws UaException {
    return ClientMembers.await(getCallCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getCallCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CallCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:CallCount (declaration i=2221, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getCreateMonitoredItemsCount() throws UaException {
    BaseDataVariableTypeNode node = getCreateMonitoredItemsCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CreateMonitoredItemsCount (declaration i=2222, owner"
              + " i=2197) on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setCreateMonitoredItemsCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    BaseDataVariableTypeNode node = getCreateMonitoredItemsCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CreateMonitoredItemsCount (declaration i=2222, owner"
              + " i=2197) on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readCreateMonitoredItemsCount() throws UaException {
    return ClientMembers.await(readCreateMonitoredItemsCountAsync(), false);
  }

  @Override
  public void writeCreateMonitoredItemsCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    try {
      StatusCode statusCode = writeCreateMonitoredItemsCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readCreateMonitoredItemsCountAsync() {
    return getCreateMonitoredItemsCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CreateMonitoredItemsCount (declaration"
                            + " i=2222, owner i=2197) on "
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeCreateMonitoredItemsCountAsync(
      @Nullable ServiceCounterDataType createMonitoredItemsCount) {
    return getCreateMonitoredItemsCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CreateMonitoredItemsCount (declaration"
                            + " i=2222, owner i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                createMonitoredItemsCount,
                                ServiceCounterDataType.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getCreateMonitoredItemsCountNode() throws UaException {
    return ClientMembers.await(getCreateMonitoredItemsCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getCreateMonitoredItemsCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CreateMonitoredItemsCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:CreateMonitoredItemsCount (declaration i=2222, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getModifyMonitoredItemsCount() throws UaException {
    BaseDataVariableTypeNode node = getModifyMonitoredItemsCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ModifyMonitoredItemsCount (declaration i=2223, owner"
              + " i=2197) on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setModifyMonitoredItemsCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    BaseDataVariableTypeNode node = getModifyMonitoredItemsCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ModifyMonitoredItemsCount (declaration i=2223, owner"
              + " i=2197) on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readModifyMonitoredItemsCount() throws UaException {
    return ClientMembers.await(readModifyMonitoredItemsCountAsync(), false);
  }

  @Override
  public void writeModifyMonitoredItemsCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    try {
      StatusCode statusCode = writeModifyMonitoredItemsCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readModifyMonitoredItemsCountAsync() {
    return getModifyMonitoredItemsCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ModifyMonitoredItemsCount (declaration"
                            + " i=2223, owner i=2197) on "
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeModifyMonitoredItemsCountAsync(
      @Nullable ServiceCounterDataType modifyMonitoredItemsCount) {
    return getModifyMonitoredItemsCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ModifyMonitoredItemsCount (declaration"
                            + " i=2223, owner i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                modifyMonitoredItemsCount,
                                ServiceCounterDataType.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getModifyMonitoredItemsCountNode() throws UaException {
    return ClientMembers.await(getModifyMonitoredItemsCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getModifyMonitoredItemsCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ModifyMonitoredItemsCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ModifyMonitoredItemsCount (declaration i=2223, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getSetMonitoringModeCount() throws UaException {
    BaseDataVariableTypeNode node = getSetMonitoringModeCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SetMonitoringModeCount (declaration i=2224, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setSetMonitoringModeCount(@Nullable ServiceCounterDataType value) throws UaException {
    BaseDataVariableTypeNode node = getSetMonitoringModeCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SetMonitoringModeCount (declaration i=2224, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readSetMonitoringModeCount() throws UaException {
    return ClientMembers.await(readSetMonitoringModeCountAsync(), false);
  }

  @Override
  public void writeSetMonitoringModeCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    try {
      StatusCode statusCode = writeSetMonitoringModeCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readSetMonitoringModeCountAsync() {
    return getSetMonitoringModeCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SetMonitoringModeCount (declaration i=2224,"
                            + " owner i=2197) on "
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeSetMonitoringModeCountAsync(
      @Nullable ServiceCounterDataType setMonitoringModeCount) {
    return getSetMonitoringModeCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SetMonitoringModeCount (declaration i=2224,"
                            + " owner i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                setMonitoringModeCount,
                                ServiceCounterDataType.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getSetMonitoringModeCountNode() throws UaException {
    return ClientMembers.await(getSetMonitoringModeCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getSetMonitoringModeCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SetMonitoringModeCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SetMonitoringModeCount (declaration i=2224, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getSetTriggeringCount() throws UaException {
    BaseDataVariableTypeNode node = getSetTriggeringCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SetTriggeringCount (declaration i=2225, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setSetTriggeringCount(@Nullable ServiceCounterDataType value) throws UaException {
    BaseDataVariableTypeNode node = getSetTriggeringCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SetTriggeringCount (declaration i=2225, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readSetTriggeringCount() throws UaException {
    return ClientMembers.await(readSetTriggeringCountAsync(), false);
  }

  @Override
  public void writeSetTriggeringCount(@Nullable ServiceCounterDataType value) throws UaException {
    try {
      StatusCode statusCode = writeSetTriggeringCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readSetTriggeringCountAsync() {
    return getSetTriggeringCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SetTriggeringCount (declaration i=2225, owner"
                            + " i=2197) on "
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeSetTriggeringCountAsync(
      @Nullable ServiceCounterDataType setTriggeringCount) {
    return getSetTriggeringCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SetTriggeringCount (declaration i=2225, owner"
                            + " i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                setTriggeringCount,
                                ServiceCounterDataType.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getSetTriggeringCountNode() throws UaException {
    return ClientMembers.await(getSetTriggeringCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getSetTriggeringCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SetTriggeringCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SetTriggeringCount (declaration i=2225, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getDeleteMonitoredItemsCount() throws UaException {
    BaseDataVariableTypeNode node = getDeleteMonitoredItemsCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteMonitoredItemsCount (declaration i=2226, owner"
              + " i=2197) on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setDeleteMonitoredItemsCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    BaseDataVariableTypeNode node = getDeleteMonitoredItemsCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteMonitoredItemsCount (declaration i=2226, owner"
              + " i=2197) on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readDeleteMonitoredItemsCount() throws UaException {
    return ClientMembers.await(readDeleteMonitoredItemsCountAsync(), false);
  }

  @Override
  public void writeDeleteMonitoredItemsCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    try {
      StatusCode statusCode = writeDeleteMonitoredItemsCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readDeleteMonitoredItemsCountAsync() {
    return getDeleteMonitoredItemsCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DeleteMonitoredItemsCount (declaration"
                            + " i=2226, owner i=2197) on "
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeDeleteMonitoredItemsCountAsync(
      @Nullable ServiceCounterDataType deleteMonitoredItemsCount) {
    return getDeleteMonitoredItemsCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DeleteMonitoredItemsCount (declaration"
                            + " i=2226, owner i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                deleteMonitoredItemsCount,
                                ServiceCounterDataType.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getDeleteMonitoredItemsCountNode() throws UaException {
    return ClientMembers.await(getDeleteMonitoredItemsCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getDeleteMonitoredItemsCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DeleteMonitoredItemsCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DeleteMonitoredItemsCount (declaration i=2226, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getCreateSubscriptionCount() throws UaException {
    BaseDataVariableTypeNode node = getCreateSubscriptionCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CreateSubscriptionCount (declaration i=2227, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setCreateSubscriptionCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    BaseDataVariableTypeNode node = getCreateSubscriptionCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CreateSubscriptionCount (declaration i=2227, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readCreateSubscriptionCount() throws UaException {
    return ClientMembers.await(readCreateSubscriptionCountAsync(), false);
  }

  @Override
  public void writeCreateSubscriptionCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    try {
      StatusCode statusCode = writeCreateSubscriptionCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readCreateSubscriptionCountAsync() {
    return getCreateSubscriptionCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CreateSubscriptionCount (declaration i=2227,"
                            + " owner i=2197) on "
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeCreateSubscriptionCountAsync(
      @Nullable ServiceCounterDataType createSubscriptionCount) {
    return getCreateSubscriptionCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CreateSubscriptionCount (declaration i=2227,"
                            + " owner i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                createSubscriptionCount,
                                ServiceCounterDataType.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getCreateSubscriptionCountNode() throws UaException {
    return ClientMembers.await(getCreateSubscriptionCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getCreateSubscriptionCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CreateSubscriptionCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:CreateSubscriptionCount (declaration i=2227, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getModifySubscriptionCount() throws UaException {
    BaseDataVariableTypeNode node = getModifySubscriptionCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ModifySubscriptionCount (declaration i=2228, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setModifySubscriptionCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    BaseDataVariableTypeNode node = getModifySubscriptionCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ModifySubscriptionCount (declaration i=2228, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readModifySubscriptionCount() throws UaException {
    return ClientMembers.await(readModifySubscriptionCountAsync(), false);
  }

  @Override
  public void writeModifySubscriptionCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    try {
      StatusCode statusCode = writeModifySubscriptionCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readModifySubscriptionCountAsync() {
    return getModifySubscriptionCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ModifySubscriptionCount (declaration i=2228,"
                            + " owner i=2197) on "
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeModifySubscriptionCountAsync(
      @Nullable ServiceCounterDataType modifySubscriptionCount) {
    return getModifySubscriptionCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ModifySubscriptionCount (declaration i=2228,"
                            + " owner i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                modifySubscriptionCount,
                                ServiceCounterDataType.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getModifySubscriptionCountNode() throws UaException {
    return ClientMembers.await(getModifySubscriptionCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getModifySubscriptionCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ModifySubscriptionCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ModifySubscriptionCount (declaration i=2228, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getSetPublishingModeCount() throws UaException {
    BaseDataVariableTypeNode node = getSetPublishingModeCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SetPublishingModeCount (declaration i=2229, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setSetPublishingModeCount(@Nullable ServiceCounterDataType value) throws UaException {
    BaseDataVariableTypeNode node = getSetPublishingModeCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SetPublishingModeCount (declaration i=2229, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readSetPublishingModeCount() throws UaException {
    return ClientMembers.await(readSetPublishingModeCountAsync(), false);
  }

  @Override
  public void writeSetPublishingModeCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    try {
      StatusCode statusCode = writeSetPublishingModeCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readSetPublishingModeCountAsync() {
    return getSetPublishingModeCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SetPublishingModeCount (declaration i=2229,"
                            + " owner i=2197) on "
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeSetPublishingModeCountAsync(
      @Nullable ServiceCounterDataType setPublishingModeCount) {
    return getSetPublishingModeCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SetPublishingModeCount (declaration i=2229,"
                            + " owner i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                setPublishingModeCount,
                                ServiceCounterDataType.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getSetPublishingModeCountNode() throws UaException {
    return ClientMembers.await(getSetPublishingModeCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getSetPublishingModeCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SetPublishingModeCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SetPublishingModeCount (declaration i=2229, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getPublishCount() throws UaException {
    BaseDataVariableTypeNode node = getPublishCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PublishCount (declaration i=2230, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setPublishCount(@Nullable ServiceCounterDataType value) throws UaException {
    BaseDataVariableTypeNode node = getPublishCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PublishCount (declaration i=2230, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readPublishCount() throws UaException {
    return ClientMembers.await(readPublishCountAsync(), false);
  }

  @Override
  public void writePublishCount(@Nullable ServiceCounterDataType value) throws UaException {
    try {
      StatusCode statusCode = writePublishCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType> readPublishCountAsync() {
    return getPublishCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:PublishCount (declaration i=2230, owner"
                            + " i=2197) on "
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writePublishCountAsync(
      @Nullable ServiceCounterDataType publishCount) {
    return getPublishCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:PublishCount (declaration i=2230, owner"
                            + " i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                publishCount, ServiceCounterDataType.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getPublishCountNode() throws UaException {
    return ClientMembers.await(getPublishCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getPublishCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "PublishCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:PublishCount (declaration i=2230, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getRepublishCount() throws UaException {
    BaseDataVariableTypeNode node = getRepublishCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RepublishCount (declaration i=2231, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setRepublishCount(@Nullable ServiceCounterDataType value) throws UaException {
    BaseDataVariableTypeNode node = getRepublishCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RepublishCount (declaration i=2231, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readRepublishCount() throws UaException {
    return ClientMembers.await(readRepublishCountAsync(), false);
  }

  @Override
  public void writeRepublishCount(@Nullable ServiceCounterDataType value) throws UaException {
    try {
      StatusCode statusCode = writeRepublishCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType> readRepublishCountAsync() {
    return getRepublishCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:RepublishCount (declaration i=2231, owner"
                            + " i=2197) on "
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeRepublishCountAsync(
      @Nullable ServiceCounterDataType republishCount) {
    return getRepublishCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:RepublishCount (declaration i=2231, owner"
                            + " i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                republishCount, ServiceCounterDataType.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getRepublishCountNode() throws UaException {
    return ClientMembers.await(getRepublishCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getRepublishCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RepublishCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:RepublishCount (declaration i=2231, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getTransferSubscriptionsCount() throws UaException {
    BaseDataVariableTypeNode node = getTransferSubscriptionsCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TransferSubscriptionsCount (declaration i=2232, owner"
              + " i=2197) on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setTransferSubscriptionsCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    BaseDataVariableTypeNode node = getTransferSubscriptionsCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TransferSubscriptionsCount (declaration i=2232, owner"
              + " i=2197) on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readTransferSubscriptionsCount() throws UaException {
    return ClientMembers.await(readTransferSubscriptionsCountAsync(), false);
  }

  @Override
  public void writeTransferSubscriptionsCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    try {
      StatusCode statusCode = writeTransferSubscriptionsCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readTransferSubscriptionsCountAsync() {
    return getTransferSubscriptionsCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:TransferSubscriptionsCount (declaration"
                            + " i=2232, owner i=2197) on "
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeTransferSubscriptionsCountAsync(
      @Nullable ServiceCounterDataType transferSubscriptionsCount) {
    return getTransferSubscriptionsCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:TransferSubscriptionsCount (declaration"
                            + " i=2232, owner i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                transferSubscriptionsCount,
                                ServiceCounterDataType.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getTransferSubscriptionsCountNode() throws UaException {
    return ClientMembers.await(getTransferSubscriptionsCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getTransferSubscriptionsCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "TransferSubscriptionsCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:TransferSubscriptionsCount (declaration i=2232, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getDeleteSubscriptionsCount() throws UaException {
    BaseDataVariableTypeNode node = getDeleteSubscriptionsCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteSubscriptionsCount (declaration i=2233, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setDeleteSubscriptionsCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    BaseDataVariableTypeNode node = getDeleteSubscriptionsCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteSubscriptionsCount (declaration i=2233, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readDeleteSubscriptionsCount() throws UaException {
    return ClientMembers.await(readDeleteSubscriptionsCountAsync(), false);
  }

  @Override
  public void writeDeleteSubscriptionsCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    try {
      StatusCode statusCode = writeDeleteSubscriptionsCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readDeleteSubscriptionsCountAsync() {
    return getDeleteSubscriptionsCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DeleteSubscriptionsCount (declaration i=2233,"
                            + " owner i=2197) on "
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeDeleteSubscriptionsCountAsync(
      @Nullable ServiceCounterDataType deleteSubscriptionsCount) {
    return getDeleteSubscriptionsCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DeleteSubscriptionsCount (declaration i=2233,"
                            + " owner i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                deleteSubscriptionsCount,
                                ServiceCounterDataType.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getDeleteSubscriptionsCountNode() throws UaException {
    return ClientMembers.await(getDeleteSubscriptionsCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getDeleteSubscriptionsCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DeleteSubscriptionsCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DeleteSubscriptionsCount (declaration i=2233, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getAddNodesCount() throws UaException {
    BaseDataVariableTypeNode node = getAddNodesCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AddNodesCount (declaration i=2234, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setAddNodesCount(@Nullable ServiceCounterDataType value) throws UaException {
    BaseDataVariableTypeNode node = getAddNodesCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AddNodesCount (declaration i=2234, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readAddNodesCount() throws UaException {
    return ClientMembers.await(readAddNodesCountAsync(), false);
  }

  @Override
  public void writeAddNodesCount(@Nullable ServiceCounterDataType value) throws UaException {
    try {
      StatusCode statusCode = writeAddNodesCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType> readAddNodesCountAsync() {
    return getAddNodesCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:AddNodesCount (declaration i=2234, owner"
                            + " i=2197) on "
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeAddNodesCountAsync(
      @Nullable ServiceCounterDataType addNodesCount) {
    return getAddNodesCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:AddNodesCount (declaration i=2234, owner"
                            + " i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                addNodesCount, ServiceCounterDataType.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getAddNodesCountNode() throws UaException {
    return ClientMembers.await(getAddNodesCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getAddNodesCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "AddNodesCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:AddNodesCount (declaration i=2234, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getAddReferencesCount() throws UaException {
    BaseDataVariableTypeNode node = getAddReferencesCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AddReferencesCount (declaration i=2235, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setAddReferencesCount(@Nullable ServiceCounterDataType value) throws UaException {
    BaseDataVariableTypeNode node = getAddReferencesCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AddReferencesCount (declaration i=2235, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readAddReferencesCount() throws UaException {
    return ClientMembers.await(readAddReferencesCountAsync(), false);
  }

  @Override
  public void writeAddReferencesCount(@Nullable ServiceCounterDataType value) throws UaException {
    try {
      StatusCode statusCode = writeAddReferencesCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readAddReferencesCountAsync() {
    return getAddReferencesCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:AddReferencesCount (declaration i=2235, owner"
                            + " i=2197) on "
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeAddReferencesCountAsync(
      @Nullable ServiceCounterDataType addReferencesCount) {
    return getAddReferencesCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:AddReferencesCount (declaration i=2235, owner"
                            + " i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                addReferencesCount,
                                ServiceCounterDataType.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getAddReferencesCountNode() throws UaException {
    return ClientMembers.await(getAddReferencesCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getAddReferencesCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "AddReferencesCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:AddReferencesCount (declaration i=2235, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getDeleteNodesCount() throws UaException {
    BaseDataVariableTypeNode node = getDeleteNodesCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteNodesCount (declaration i=2236, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setDeleteNodesCount(@Nullable ServiceCounterDataType value) throws UaException {
    BaseDataVariableTypeNode node = getDeleteNodesCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteNodesCount (declaration i=2236, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readDeleteNodesCount() throws UaException {
    return ClientMembers.await(readDeleteNodesCountAsync(), false);
  }

  @Override
  public void writeDeleteNodesCount(@Nullable ServiceCounterDataType value) throws UaException {
    try {
      StatusCode statusCode = writeDeleteNodesCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType> readDeleteNodesCountAsync() {
    return getDeleteNodesCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DeleteNodesCount (declaration i=2236, owner"
                            + " i=2197) on "
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeDeleteNodesCountAsync(
      @Nullable ServiceCounterDataType deleteNodesCount) {
    return getDeleteNodesCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DeleteNodesCount (declaration i=2236, owner"
                            + " i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                deleteNodesCount,
                                ServiceCounterDataType.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getDeleteNodesCountNode() throws UaException {
    return ClientMembers.await(getDeleteNodesCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getDeleteNodesCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DeleteNodesCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DeleteNodesCount (declaration i=2236, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getDeleteReferencesCount() throws UaException {
    BaseDataVariableTypeNode node = getDeleteReferencesCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteReferencesCount (declaration i=2237, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setDeleteReferencesCount(@Nullable ServiceCounterDataType value) throws UaException {
    BaseDataVariableTypeNode node = getDeleteReferencesCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteReferencesCount (declaration i=2237, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readDeleteReferencesCount() throws UaException {
    return ClientMembers.await(readDeleteReferencesCountAsync(), false);
  }

  @Override
  public void writeDeleteReferencesCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    try {
      StatusCode statusCode = writeDeleteReferencesCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readDeleteReferencesCountAsync() {
    return getDeleteReferencesCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DeleteReferencesCount (declaration i=2237,"
                            + " owner i=2197) on "
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeDeleteReferencesCountAsync(
      @Nullable ServiceCounterDataType deleteReferencesCount) {
    return getDeleteReferencesCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DeleteReferencesCount (declaration i=2237,"
                            + " owner i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                deleteReferencesCount,
                                ServiceCounterDataType.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getDeleteReferencesCountNode() throws UaException {
    return ClientMembers.await(getDeleteReferencesCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getDeleteReferencesCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DeleteReferencesCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DeleteReferencesCount (declaration i=2237, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getBrowseCount() throws UaException {
    BaseDataVariableTypeNode node = getBrowseCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:BrowseCount (declaration i=2238, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setBrowseCount(@Nullable ServiceCounterDataType value) throws UaException {
    BaseDataVariableTypeNode node = getBrowseCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:BrowseCount (declaration i=2238, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readBrowseCount() throws UaException {
    return ClientMembers.await(readBrowseCountAsync(), false);
  }

  @Override
  public void writeBrowseCount(@Nullable ServiceCounterDataType value) throws UaException {
    try {
      StatusCode statusCode = writeBrowseCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType> readBrowseCountAsync() {
    return getBrowseCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:BrowseCount (declaration i=2238, owner"
                            + " i=2197) on "
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeBrowseCountAsync(
      @Nullable ServiceCounterDataType browseCount) {
    return getBrowseCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:BrowseCount (declaration i=2238, owner"
                            + " i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                browseCount, ServiceCounterDataType.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getBrowseCountNode() throws UaException {
    return ClientMembers.await(getBrowseCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getBrowseCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "BrowseCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:BrowseCount (declaration i=2238, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getBrowseNextCount() throws UaException {
    BaseDataVariableTypeNode node = getBrowseNextCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:BrowseNextCount (declaration i=2239, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setBrowseNextCount(@Nullable ServiceCounterDataType value) throws UaException {
    BaseDataVariableTypeNode node = getBrowseNextCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:BrowseNextCount (declaration i=2239, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readBrowseNextCount() throws UaException {
    return ClientMembers.await(readBrowseNextCountAsync(), false);
  }

  @Override
  public void writeBrowseNextCount(@Nullable ServiceCounterDataType value) throws UaException {
    try {
      StatusCode statusCode = writeBrowseNextCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType> readBrowseNextCountAsync() {
    return getBrowseNextCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:BrowseNextCount (declaration i=2239, owner"
                            + " i=2197) on "
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeBrowseNextCountAsync(
      @Nullable ServiceCounterDataType browseNextCount) {
    return getBrowseNextCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:BrowseNextCount (declaration i=2239, owner"
                            + " i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                browseNextCount, ServiceCounterDataType.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getBrowseNextCountNode() throws UaException {
    return ClientMembers.await(getBrowseNextCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getBrowseNextCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "BrowseNextCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:BrowseNextCount (declaration i=2239, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getTranslateBrowsePathsToNodeIdsCount()
      throws UaException {
    BaseDataVariableTypeNode node = getTranslateBrowsePathsToNodeIdsCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TranslateBrowsePathsToNodeIdsCount (declaration i=2240,"
              + " owner i=2197) on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setTranslateBrowsePathsToNodeIdsCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    BaseDataVariableTypeNode node = getTranslateBrowsePathsToNodeIdsCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TranslateBrowsePathsToNodeIdsCount (declaration i=2240,"
              + " owner i=2197) on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readTranslateBrowsePathsToNodeIdsCount()
      throws UaException {
    return ClientMembers.await(readTranslateBrowsePathsToNodeIdsCountAsync(), false);
  }

  @Override
  public void writeTranslateBrowsePathsToNodeIdsCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    try {
      StatusCode statusCode = writeTranslateBrowsePathsToNodeIdsCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readTranslateBrowsePathsToNodeIdsCountAsync() {
    return getTranslateBrowsePathsToNodeIdsCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:TranslateBrowsePathsToNodeIdsCount"
                            + " (declaration i=2240, owner i=2197) on "
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeTranslateBrowsePathsToNodeIdsCountAsync(
      @Nullable ServiceCounterDataType translateBrowsePathsToNodeIdsCount) {
    return getTranslateBrowsePathsToNodeIdsCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:TranslateBrowsePathsToNodeIdsCount"
                            + " (declaration i=2240, owner i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                translateBrowsePathsToNodeIdsCount,
                                ServiceCounterDataType.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getTranslateBrowsePathsToNodeIdsCountNode() throws UaException {
    return ClientMembers.await(getTranslateBrowsePathsToNodeIdsCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getTranslateBrowsePathsToNodeIdsCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "TranslateBrowsePathsToNodeIdsCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:TranslateBrowsePathsToNodeIdsCount (declaration i=2240,"
                + " owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getQueryFirstCount() throws UaException {
    BaseDataVariableTypeNode node = getQueryFirstCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:QueryFirstCount (declaration i=2241, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setQueryFirstCount(@Nullable ServiceCounterDataType value) throws UaException {
    BaseDataVariableTypeNode node = getQueryFirstCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:QueryFirstCount (declaration i=2241, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readQueryFirstCount() throws UaException {
    return ClientMembers.await(readQueryFirstCountAsync(), false);
  }

  @Override
  public void writeQueryFirstCount(@Nullable ServiceCounterDataType value) throws UaException {
    try {
      StatusCode statusCode = writeQueryFirstCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType> readQueryFirstCountAsync() {
    return getQueryFirstCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:QueryFirstCount (declaration i=2241, owner"
                            + " i=2197) on "
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeQueryFirstCountAsync(
      @Nullable ServiceCounterDataType queryFirstCount) {
    return getQueryFirstCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:QueryFirstCount (declaration i=2241, owner"
                            + " i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                queryFirstCount, ServiceCounterDataType.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getQueryFirstCountNode() throws UaException {
    return ClientMembers.await(getQueryFirstCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getQueryFirstCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "QueryFirstCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:QueryFirstCount (declaration i=2241, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getQueryNextCount() throws UaException {
    BaseDataVariableTypeNode node = getQueryNextCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:QueryNextCount (declaration i=2242, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setQueryNextCount(@Nullable ServiceCounterDataType value) throws UaException {
    BaseDataVariableTypeNode node = getQueryNextCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:QueryNextCount (declaration i=2242, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readQueryNextCount() throws UaException {
    return ClientMembers.await(readQueryNextCountAsync(), false);
  }

  @Override
  public void writeQueryNextCount(@Nullable ServiceCounterDataType value) throws UaException {
    try {
      StatusCode statusCode = writeQueryNextCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType> readQueryNextCountAsync() {
    return getQueryNextCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:QueryNextCount (declaration i=2242, owner"
                            + " i=2197) on "
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeQueryNextCountAsync(
      @Nullable ServiceCounterDataType queryNextCount) {
    return getQueryNextCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:QueryNextCount (declaration i=2242, owner"
                            + " i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                queryNextCount, ServiceCounterDataType.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getQueryNextCountNode() throws UaException {
    return ClientMembers.await(getQueryNextCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getQueryNextCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "QueryNextCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:QueryNextCount (declaration i=2242, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getRegisterNodesCount() throws UaException {
    BaseDataVariableTypeNode node = getRegisterNodesCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RegisterNodesCount (declaration i=2730, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setRegisterNodesCount(@Nullable ServiceCounterDataType value) throws UaException {
    BaseDataVariableTypeNode node = getRegisterNodesCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RegisterNodesCount (declaration i=2730, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readRegisterNodesCount() throws UaException {
    return ClientMembers.await(readRegisterNodesCountAsync(), false);
  }

  @Override
  public void writeRegisterNodesCount(@Nullable ServiceCounterDataType value) throws UaException {
    try {
      StatusCode statusCode = writeRegisterNodesCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readRegisterNodesCountAsync() {
    return getRegisterNodesCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:RegisterNodesCount (declaration i=2730, owner"
                            + " i=2197) on "
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeRegisterNodesCountAsync(
      @Nullable ServiceCounterDataType registerNodesCount) {
    return getRegisterNodesCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:RegisterNodesCount (declaration i=2730, owner"
                            + " i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                registerNodesCount,
                                ServiceCounterDataType.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getRegisterNodesCountNode() throws UaException {
    return ClientMembers.await(getRegisterNodesCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getRegisterNodesCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RegisterNodesCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:RegisterNodesCount (declaration i=2730, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getUnregisterNodesCount() throws UaException {
    BaseDataVariableTypeNode node = getUnregisterNodesCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UnregisterNodesCount (declaration i=2731, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setUnregisterNodesCount(@Nullable ServiceCounterDataType value) throws UaException {
    BaseDataVariableTypeNode node = getUnregisterNodesCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UnregisterNodesCount (declaration i=2731, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServiceCounterDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServiceCounterDataType readUnregisterNodesCount() throws UaException {
    return ClientMembers.await(readUnregisterNodesCountAsync(), false);
  }

  @Override
  public void writeUnregisterNodesCount(@Nullable ServiceCounterDataType value) throws UaException {
    try {
      StatusCode statusCode = writeUnregisterNodesCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readUnregisterNodesCountAsync() {
    return getUnregisterNodesCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:UnregisterNodesCount (declaration i=2731,"
                            + " owner i=2197) on "
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
                return (ServiceCounterDataType)
                    decodeValue(
                        v.getValue().getValue(), ServiceCounterDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeUnregisterNodesCountAsync(
      @Nullable ServiceCounterDataType unregisterNodesCount) {
    return getUnregisterNodesCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:UnregisterNodesCount (declaration i=2731,"
                            + " owner i=2197) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                unregisterNodesCount,
                                ServiceCounterDataType.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getUnregisterNodesCountNode() throws UaException {
    return ClientMembers.await(getUnregisterNodesCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getUnregisterNodesCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "UnregisterNodesCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:UnregisterNodesCount (declaration i=2731, owner"
                + " i=2197)"));
  }
}
