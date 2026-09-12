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
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.StatusResult;
import org.jspecify.annotations.Nullable;

public class ProgramDiagnosticTypeNode extends BaseDataVariableTypeNode
    implements ProgramDiagnosticType {
  public ProgramDiagnosticTypeNode(
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
  public @Nullable NodeId getCreateSessionId() throws UaException {
    PropertyTypeNode node = getCreateSessionIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CreateSessionId (declaration i=2381, owner i=2380)"
              + " on "
              + getNodeId());
    }
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setCreateSessionId(@Nullable NodeId value) throws UaException {
    PropertyTypeNode node = getCreateSessionIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CreateSessionId (declaration i=2381, owner i=2380)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable NodeId readCreateSessionId() throws UaException {
    return ClientMembers.await(readCreateSessionIdAsync(), false);
  }

  @Override
  public void writeCreateSessionId(@Nullable NodeId value) throws UaException {
    try {
      StatusCode statusCode = writeCreateSessionIdAsync(value).get();
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
  public CompletableFuture<? extends @Nullable NodeId> readCreateSessionIdAsync() {
    return getCreateSessionIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CreateSessionId (declaration i=2381, owner"
                            + " i=2380) on "
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
  public CompletableFuture<StatusCode> writeCreateSessionIdAsync(@Nullable NodeId createSessionId) {
    return getCreateSessionIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CreateSessionId (declaration i=2381, owner"
                            + " i=2380) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(createSessionId));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getCreateSessionIdNode() throws UaException {
    return ClientMembers.await(getCreateSessionIdNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getCreateSessionIdNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CreateSessionId",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:CreateSessionId (declaration i=2381, owner i=2380)"));
  }

  @Override
  public @Nullable String getCreateClientName() throws UaException {
    PropertyTypeNode node = getCreateClientNameNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CreateClientName (declaration i=2382, owner i=2380)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setCreateClientName(@Nullable String value) throws UaException {
    PropertyTypeNode node = getCreateClientNameNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CreateClientName (declaration i=2382, owner i=2380)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String readCreateClientName() throws UaException {
    return ClientMembers.await(readCreateClientNameAsync(), false);
  }

  @Override
  public void writeCreateClientName(@Nullable String value) throws UaException {
    try {
      StatusCode statusCode = writeCreateClientNameAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String> readCreateClientNameAsync() {
    return getCreateClientNameNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CreateClientName (declaration i=2382, owner"
                            + " i=2380) on "
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
  public CompletableFuture<StatusCode> writeCreateClientNameAsync(
      @Nullable String createClientName) {
    return getCreateClientNameNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CreateClientName (declaration i=2382, owner"
                            + " i=2380) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(createClientName));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getCreateClientNameNode() throws UaException {
    return ClientMembers.await(getCreateClientNameNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getCreateClientNameNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CreateClientName",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:CreateClientName (declaration i=2382, owner i=2380)"));
  }

  @Override
  public @Nullable DateTime getInvocationCreationTime() throws UaException {
    PropertyTypeNode node = getInvocationCreationTimeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InvocationCreationTime (declaration i=2383, owner i=2380)"
              + " on "
              + getNodeId());
    }
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setInvocationCreationTime(@Nullable DateTime value) throws UaException {
    PropertyTypeNode node = getInvocationCreationTimeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InvocationCreationTime (declaration i=2383, owner i=2380)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable DateTime readInvocationCreationTime() throws UaException {
    return ClientMembers.await(readInvocationCreationTimeAsync(), false);
  }

  @Override
  public void writeInvocationCreationTime(@Nullable DateTime value) throws UaException {
    try {
      StatusCode statusCode = writeInvocationCreationTimeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable DateTime> readInvocationCreationTimeAsync() {
    return getInvocationCreationTimeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:InvocationCreationTime (declaration i=2383,"
                            + " owner i=2380) on "
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
  public CompletableFuture<StatusCode> writeInvocationCreationTimeAsync(
      @Nullable DateTime invocationCreationTime) {
    return getInvocationCreationTimeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:InvocationCreationTime (declaration i=2383,"
                            + " owner i=2380) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(invocationCreationTime));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getInvocationCreationTimeNode() throws UaException {
    return ClientMembers.await(getInvocationCreationTimeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getInvocationCreationTimeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "InvocationCreationTime",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:InvocationCreationTime (declaration i=2383, owner"
                + " i=2380)"));
  }

  @Override
  public @Nullable DateTime getLastTransitionTime() throws UaException {
    PropertyTypeNode node = getLastTransitionTimeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LastTransitionTime (declaration i=2384, owner i=2380)"
              + " on "
              + getNodeId());
    }
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastTransitionTime(@Nullable DateTime value) throws UaException {
    PropertyTypeNode node = getLastTransitionTimeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LastTransitionTime (declaration i=2384, owner i=2380)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable DateTime readLastTransitionTime() throws UaException {
    return ClientMembers.await(readLastTransitionTimeAsync(), false);
  }

  @Override
  public void writeLastTransitionTime(@Nullable DateTime value) throws UaException {
    try {
      StatusCode statusCode = writeLastTransitionTimeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable DateTime> readLastTransitionTimeAsync() {
    return getLastTransitionTimeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LastTransitionTime (declaration i=2384, owner"
                            + " i=2380) on "
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
  public CompletableFuture<StatusCode> writeLastTransitionTimeAsync(
      @Nullable DateTime lastTransitionTime) {
    return getLastTransitionTimeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LastTransitionTime (declaration i=2384, owner"
                            + " i=2380) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(lastTransitionTime));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getLastTransitionTimeNode() throws UaException {
    return ClientMembers.await(getLastTransitionTimeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLastTransitionTimeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "LastTransitionTime",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:LastTransitionTime (declaration i=2384, owner i=2380)"));
  }

  @Override
  public @Nullable String getLastMethodCall() throws UaException {
    PropertyTypeNode node = getLastMethodCallNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LastMethodCall (declaration i=2385, owner i=2380)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastMethodCall(@Nullable String value) throws UaException {
    PropertyTypeNode node = getLastMethodCallNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LastMethodCall (declaration i=2385, owner i=2380)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String readLastMethodCall() throws UaException {
    return ClientMembers.await(readLastMethodCallAsync(), false);
  }

  @Override
  public void writeLastMethodCall(@Nullable String value) throws UaException {
    try {
      StatusCode statusCode = writeLastMethodCallAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String> readLastMethodCallAsync() {
    return getLastMethodCallNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LastMethodCall (declaration i=2385, owner"
                            + " i=2380) on "
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
  public CompletableFuture<StatusCode> writeLastMethodCallAsync(@Nullable String lastMethodCall) {
    return getLastMethodCallNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LastMethodCall (declaration i=2385, owner"
                            + " i=2380) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(lastMethodCall));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getLastMethodCallNode() throws UaException {
    return ClientMembers.await(getLastMethodCallNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLastMethodCallNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "LastMethodCall",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:LastMethodCall (declaration i=2385, owner i=2380)"));
  }

  @Override
  public @Nullable NodeId getLastMethodSessionId() throws UaException {
    PropertyTypeNode node = getLastMethodSessionIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LastMethodSessionId (declaration i=2386, owner i=2380)"
              + " on "
              + getNodeId());
    }
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastMethodSessionId(@Nullable NodeId value) throws UaException {
    PropertyTypeNode node = getLastMethodSessionIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LastMethodSessionId (declaration i=2386, owner i=2380)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable NodeId readLastMethodSessionId() throws UaException {
    return ClientMembers.await(readLastMethodSessionIdAsync(), false);
  }

  @Override
  public void writeLastMethodSessionId(@Nullable NodeId value) throws UaException {
    try {
      StatusCode statusCode = writeLastMethodSessionIdAsync(value).get();
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
  public CompletableFuture<? extends @Nullable NodeId> readLastMethodSessionIdAsync() {
    return getLastMethodSessionIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LastMethodSessionId (declaration i=2386,"
                            + " owner i=2380) on "
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
  public CompletableFuture<StatusCode> writeLastMethodSessionIdAsync(
      @Nullable NodeId lastMethodSessionId) {
    return getLastMethodSessionIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LastMethodSessionId (declaration i=2386,"
                            + " owner i=2380) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(lastMethodSessionId));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getLastMethodSessionIdNode() throws UaException {
    return ClientMembers.await(getLastMethodSessionIdNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLastMethodSessionIdNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "LastMethodSessionId",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:LastMethodSessionId (declaration i=2386, owner i=2380)"));
  }

  @Override
  public @Nullable Object @Nullable [] getLastMethodInputArguments() throws UaException {
    PropertyTypeNode node = getLastMethodInputArgumentsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LastMethodInputArguments (declaration i=2387, owner i=2380)"
              + " on "
              + getNodeId());
    }
    return (Object[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastMethodInputArguments(@Nullable Object @Nullable [] value) throws UaException {
    PropertyTypeNode node = getLastMethodInputArgumentsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LastMethodInputArguments (declaration i=2387, owner i=2380)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Object @Nullable [] readLastMethodInputArguments() throws UaException {
    return ClientMembers.await(readLastMethodInputArgumentsAsync(), false);
  }

  @Override
  public void writeLastMethodInputArguments(@Nullable Object @Nullable [] value)
      throws UaException {
    try {
      StatusCode statusCode = writeLastMethodInputArgumentsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Object @Nullable []>
      readLastMethodInputArgumentsAsync() {
    return getLastMethodInputArgumentsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LastMethodInputArguments (declaration i=2387,"
                            + " owner i=2380) on "
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
                return (Object[]) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeLastMethodInputArgumentsAsync(
      @Nullable Object @Nullable [] lastMethodInputArguments) {
    return getLastMethodInputArgumentsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LastMethodInputArguments (declaration i=2387,"
                            + " owner i=2380) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(lastMethodInputArguments));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getLastMethodInputArgumentsNode() throws UaException {
    return ClientMembers.await(getLastMethodInputArgumentsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLastMethodInputArgumentsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "LastMethodInputArguments",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:LastMethodInputArguments (declaration i=2387, owner"
                + " i=2380)"));
  }

  @Override
  public @Nullable Object @Nullable [] getLastMethodOutputArguments() throws UaException {
    PropertyTypeNode node = getLastMethodOutputArgumentsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LastMethodOutputArguments (declaration i=2388, owner"
              + " i=2380) on "
              + getNodeId());
    }
    return (Object[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastMethodOutputArguments(@Nullable Object @Nullable [] value) throws UaException {
    PropertyTypeNode node = getLastMethodOutputArgumentsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LastMethodOutputArguments (declaration i=2388, owner"
              + " i=2380) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Object @Nullable [] readLastMethodOutputArguments() throws UaException {
    return ClientMembers.await(readLastMethodOutputArgumentsAsync(), false);
  }

  @Override
  public void writeLastMethodOutputArguments(@Nullable Object @Nullable [] value)
      throws UaException {
    try {
      StatusCode statusCode = writeLastMethodOutputArgumentsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Object @Nullable []>
      readLastMethodOutputArgumentsAsync() {
    return getLastMethodOutputArgumentsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LastMethodOutputArguments (declaration"
                            + " i=2388, owner i=2380) on "
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
                return (Object[]) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeLastMethodOutputArgumentsAsync(
      @Nullable Object @Nullable [] lastMethodOutputArguments) {
    return getLastMethodOutputArgumentsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LastMethodOutputArguments (declaration"
                            + " i=2388, owner i=2380) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(lastMethodOutputArguments));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getLastMethodOutputArgumentsNode() throws UaException {
    return ClientMembers.await(getLastMethodOutputArgumentsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLastMethodOutputArgumentsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "LastMethodOutputArguments",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:LastMethodOutputArguments (declaration i=2388, owner"
                + " i=2380)"));
  }

  @Override
  public @Nullable DateTime getLastMethodCallTime() throws UaException {
    PropertyTypeNode node = getLastMethodCallTimeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LastMethodCallTime (declaration i=2389, owner i=2380)"
              + " on "
              + getNodeId());
    }
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastMethodCallTime(@Nullable DateTime value) throws UaException {
    PropertyTypeNode node = getLastMethodCallTimeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LastMethodCallTime (declaration i=2389, owner i=2380)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable DateTime readLastMethodCallTime() throws UaException {
    return ClientMembers.await(readLastMethodCallTimeAsync(), false);
  }

  @Override
  public void writeLastMethodCallTime(@Nullable DateTime value) throws UaException {
    try {
      StatusCode statusCode = writeLastMethodCallTimeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable DateTime> readLastMethodCallTimeAsync() {
    return getLastMethodCallTimeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LastMethodCallTime (declaration i=2389, owner"
                            + " i=2380) on "
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
  public CompletableFuture<StatusCode> writeLastMethodCallTimeAsync(
      @Nullable DateTime lastMethodCallTime) {
    return getLastMethodCallTimeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LastMethodCallTime (declaration i=2389, owner"
                            + " i=2380) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(lastMethodCallTime));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getLastMethodCallTimeNode() throws UaException {
    return ClientMembers.await(getLastMethodCallTimeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLastMethodCallTimeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "LastMethodCallTime",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:LastMethodCallTime (declaration i=2389, owner i=2380)"));
  }

  @Override
  public @Nullable StatusResult getLastMethodReturnStatus() throws UaException {
    PropertyTypeNode node = getLastMethodReturnStatusNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LastMethodReturnStatus (declaration i=2390, owner i=2380)"
              + " on "
              + getNodeId());
    }
    return (StatusResult)
        decodeValue(node.getValue().getValue().getValue(), StatusResult.class, ValueRanks.Scalar);
  }

  @Override
  public void setLastMethodReturnStatus(@Nullable StatusResult value) throws UaException {
    PropertyTypeNode node = getLastMethodReturnStatusNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LastMethodReturnStatus (declaration i=2390, owner i=2380)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, StatusResult.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable StatusResult readLastMethodReturnStatus() throws UaException {
    return ClientMembers.await(readLastMethodReturnStatusAsync(), false);
  }

  @Override
  public void writeLastMethodReturnStatus(@Nullable StatusResult value) throws UaException {
    try {
      StatusCode statusCode = writeLastMethodReturnStatusAsync(value).get();
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
  public CompletableFuture<? extends @Nullable StatusResult> readLastMethodReturnStatusAsync() {
    return getLastMethodReturnStatusNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LastMethodReturnStatus (declaration i=2390,"
                            + " owner i=2380) on "
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
                return (StatusResult)
                    decodeValue(v.getValue().getValue(), StatusResult.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeLastMethodReturnStatusAsync(
      @Nullable StatusResult lastMethodReturnStatus) {
    return getLastMethodReturnStatusNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LastMethodReturnStatus (declaration i=2390,"
                            + " owner i=2380) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                lastMethodReturnStatus, StatusResult.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getLastMethodReturnStatusNode() throws UaException {
    return ClientMembers.await(getLastMethodReturnStatusNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLastMethodReturnStatusNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "LastMethodReturnStatus",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:LastMethodReturnStatus (declaration i=2390, owner"
                + " i=2380)"));
  }
}
