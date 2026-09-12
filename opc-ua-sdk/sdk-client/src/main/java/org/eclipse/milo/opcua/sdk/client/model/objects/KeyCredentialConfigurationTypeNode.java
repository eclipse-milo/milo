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

import com.digitalpetri.opcua.uanodeset.runtime.client.ClientDataTypes;
import com.digitalpetri.opcua.uanodeset.runtime.client.ClientMembers;
import com.digitalpetri.opcua.uanodeset.runtime.client.ClientViews;
import com.digitalpetri.opcua.uanodeset.runtime.members.MemberDeclaration;
import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallOptions;
import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallResult;
import com.digitalpetri.opcua.uanodeset.runtime.values.NumericValues;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.KeyCredentialConfigurationTypeGetEncryptingKeyOutputs;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

public class KeyCredentialConfigurationTypeNode extends BaseObjectTypeNode
    implements KeyCredentialConfigurationType {
  public KeyCredentialConfigurationTypeNode(
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
  public static ClientViews createViews(KeyCredentialConfigurationTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable String getResourceUri() throws UaException {
    PropertyTypeNode node = getResourceUriNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ResourceUri (declaration i=18069, owner i=18001)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setResourceUri(@Nullable String value) throws UaException {
    PropertyTypeNode node = getResourceUriNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ResourceUri (declaration i=18069, owner i=18001)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String readResourceUri() throws UaException {
    return ClientMembers.await(readResourceUriAsync(), false);
  }

  @Override
  public void writeResourceUri(@Nullable String value) throws UaException {
    try {
      StatusCode statusCode = writeResourceUriAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String> readResourceUriAsync() {
    return getResourceUriNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ResourceUri (declaration i=18069, owner"
                            + " i=18001) on "
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
  public CompletableFuture<StatusCode> writeResourceUriAsync(@Nullable String resourceUri) {
    return getResourceUriNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ResourceUri (declaration i=18069, owner"
                            + " i=18001) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(resourceUri));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getResourceUriNode() throws UaException {
    return ClientMembers.await(getResourceUriNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getResourceUriNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ResourceUri",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ResourceUri (declaration i=18069, owner i=18001)"));
  }

  @Override
  public @Nullable String getProfileUri() throws UaException {
    PropertyTypeNode node = getProfileUriNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ProfileUri (declaration i=18165, owner i=18001)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setProfileUri(@Nullable String value) throws UaException {
    PropertyTypeNode node = getProfileUriNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ProfileUri (declaration i=18165, owner i=18001)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String readProfileUri() throws UaException {
    return ClientMembers.await(readProfileUriAsync(), false);
  }

  @Override
  public void writeProfileUri(@Nullable String value) throws UaException {
    try {
      StatusCode statusCode = writeProfileUriAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String> readProfileUriAsync() {
    return getProfileUriNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ProfileUri (declaration i=18165, owner"
                            + " i=18001) on "
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
  public CompletableFuture<StatusCode> writeProfileUriAsync(@Nullable String profileUri) {
    return getProfileUriNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ProfileUri (declaration i=18165, owner"
                            + " i=18001) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(profileUri));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getProfileUriNode() throws UaException {
    return ClientMembers.await(getProfileUriNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getProfileUriNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ProfileUri",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ProfileUri (declaration i=18165, owner i=18001)"));
  }

  @Override
  public @Nullable String @Nullable [] getEndpointUrls() throws UaException {
    PropertyTypeNode node = getEndpointUrlsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EndpointUrls (declaration i=18004, owner i=18001)"
              + " on "
              + getNodeId());
    }
    return (String[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setEndpointUrls(@Nullable String @Nullable [] value) throws UaException {
    PropertyTypeNode node = getEndpointUrlsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EndpointUrls (declaration i=18004, owner i=18001)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String @Nullable [] readEndpointUrls() throws UaException {
    return ClientMembers.await(readEndpointUrlsAsync(), false);
  }

  @Override
  public void writeEndpointUrls(@Nullable String @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeEndpointUrlsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String @Nullable []> readEndpointUrlsAsync() {
    return getEndpointUrlsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EndpointUrls (declaration i=18004, owner"
                            + " i=18001) on "
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
  public CompletableFuture<StatusCode> writeEndpointUrlsAsync(
      @Nullable String @Nullable [] endpointUrls) {
    return getEndpointUrlsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EndpointUrls (declaration i=18004, owner"
                            + " i=18001) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(endpointUrls));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getEndpointUrlsNode() throws UaException {
    return ClientMembers.await(getEndpointUrlsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getEndpointUrlsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "EndpointUrls",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:EndpointUrls (declaration i=18004, owner i=18001)"));
  }

  @Override
  public @Nullable String getCredentialId() throws UaException {
    PropertyTypeNode node = getCredentialIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CredentialId (declaration i=18657, owner i=18001)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setCredentialId(@Nullable String value) throws UaException {
    PropertyTypeNode node = getCredentialIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CredentialId (declaration i=18657, owner i=18001)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String readCredentialId() throws UaException {
    return ClientMembers.await(readCredentialIdAsync(), false);
  }

  @Override
  public void writeCredentialId(@Nullable String value) throws UaException {
    try {
      StatusCode statusCode = writeCredentialIdAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String> readCredentialIdAsync() {
    return getCredentialIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CredentialId (declaration i=18657, owner"
                            + " i=18001) on "
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
  public CompletableFuture<StatusCode> writeCredentialIdAsync(@Nullable String credentialId) {
    return getCredentialIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CredentialId (declaration i=18657, owner"
                            + " i=18001) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(credentialId));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getCredentialIdNode() throws UaException {
    return ClientMembers.await(getCredentialIdNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getCredentialIdNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CredentialId",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:CredentialId (declaration i=18657, owner i=18001)"));
  }

  @Override
  public @Nullable StatusCode getServiceStatus() throws UaException {
    PropertyTypeNode node = getServiceStatusNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServiceStatus (declaration i=18005, owner i=18001)"
              + " on "
              + getNodeId());
    }
    return (StatusCode) node.getValue().getValue().getValue();
  }

  @Override
  public void setServiceStatus(@Nullable StatusCode value) throws UaException {
    PropertyTypeNode node = getServiceStatusNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServiceStatus (declaration i=18005, owner i=18001)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable StatusCode readServiceStatus() throws UaException {
    return ClientMembers.await(readServiceStatusAsync(), false);
  }

  @Override
  public void writeServiceStatus(@Nullable StatusCode value) throws UaException {
    try {
      StatusCode statusCode = writeServiceStatusAsync(value).get();
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
  public CompletableFuture<? extends @Nullable StatusCode> readServiceStatusAsync() {
    return getServiceStatusNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ServiceStatus (declaration i=18005, owner"
                            + " i=18001) on "
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
                return (StatusCode) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeServiceStatusAsync(@Nullable StatusCode serviceStatus) {
    return getServiceStatusNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ServiceStatus (declaration i=18005, owner"
                            + " i=18001) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(serviceStatus));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getServiceStatusNode() throws UaException {
    return ClientMembers.await(getServiceStatusNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getServiceStatusNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ServiceStatus",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:ServiceStatus (declaration i=18005, owner i=18001)"));
  }

  @NullMarked
  @Override
  public @Nullable UaMethodNode getGetEncryptingKeyMethodNode() throws UaException {
    return ClientMembers.await(getGetEncryptingKeyMethodNodeAsync(), false);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends @Nullable UaMethodNode> getGetEncryptingKeyMethodNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        UaMethodNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "GetEncryptingKey",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Method,
            true,
            "http://opcfoundation.org/UA/:GetEncryptingKey (declaration i=17534, owner i=18001)"));
  }

  @NullMarked
  @Override
  public KeyCredentialConfigurationTypeGetEncryptingKeyOutputs callGetEncryptingKey(
      @Nullable String credentialId, @Nullable String requestedSecurityPolicyUri)
      throws UaException {
    return callGetEncryptingKeyDetailed(credentialId, requestedSecurityPolicyUri).requireGood();
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends KeyCredentialConfigurationTypeGetEncryptingKeyOutputs>
      callGetEncryptingKeyAsync(
          @Nullable String credentialId, @Nullable String requestedSecurityPolicyUri) {
    CompletableFuture<KeyCredentialConfigurationTypeGetEncryptingKeyOutputs> result =
        new CompletableFuture<>();
    var call = callGetEncryptingKeyDetailedAsync(credentialId, requestedSecurityPolicyUri);
    result.whenComplete(
        (value, failure) -> {
          if (result.isCancelled()) {
            call.cancel(false);
          }
        });
    call.whenComplete(
        (value, failure) -> {
          if (failure != null) {
            result.completeExceptionally(failure);
          } else {
            try {
              result.complete(value.requireGood());
            } catch (UaException statusFailure) {
              result.completeExceptionally(statusFailure);
            }
          }
        });
    return result;
  }

  @NullMarked
  @Override
  public MethodCallResult<? extends KeyCredentialConfigurationTypeGetEncryptingKeyOutputs>
      callGetEncryptingKeyDetailed(
          @Nullable String credentialId, @Nullable String requestedSecurityPolicyUri)
          throws UaException {
    return callGetEncryptingKeyDetailed(
        MethodCallOptions.NONE, credentialId, requestedSecurityPolicyUri);
  }

  @NullMarked
  @Override
  public MethodCallResult<? extends KeyCredentialConfigurationTypeGetEncryptingKeyOutputs>
      callGetEncryptingKeyDetailed(
          MethodCallOptions options,
          @Nullable String credentialId,
          @Nullable String requestedSecurityPolicyUri)
          throws UaException {
    Objects.requireNonNull(options, "options");
    return ClientMembers.await(
        callGetEncryptingKeyDetailedAsync(options, credentialId, requestedSecurityPolicyUri), true);
  }

  @NullMarked
  @Override
  public CompletableFuture<
          ? extends
              MethodCallResult<? extends KeyCredentialConfigurationTypeGetEncryptingKeyOutputs>>
      callGetEncryptingKeyDetailedAsync(
          @Nullable String credentialId, @Nullable String requestedSecurityPolicyUri) {
    return callGetEncryptingKeyDetailedAsync(
        MethodCallOptions.NONE, credentialId, requestedSecurityPolicyUri);
  }

  @NullMarked
  @Override
  public CompletableFuture<
          ? extends
              MethodCallResult<? extends KeyCredentialConfigurationTypeGetEncryptingKeyOutputs>>
      callGetEncryptingKeyDetailedAsync(
          MethodCallOptions options,
          @Nullable String credentialId,
          @Nullable String requestedSecurityPolicyUri) {
    CompletableFuture<MethodCallResult<KeyCredentialConfigurationTypeGetEncryptingKeyOutputs>>
        result = new CompletableFuture<>();
    try {
      Objects.requireNonNull(options, "options");
      List<@Nullable Object> rawInputs = new ArrayList<>();
      rawInputs.add(credentialId);
      rawInputs.add(requestedSecurityPolicyUri);
      var lookup = getGetEncryptingKeyMethodNodeAsync();
      result.whenComplete(
          (value, failure) -> {
            if (result.isCancelled()) {
              lookup.cancel(false);
            }
          });
      CompletableFuture<MethodCallResult<KeyCredentialConfigurationTypeGetEncryptingKeyOutputs>>
          pipeline =
              lookup.thenCompose(
                  methodNode -> {
                    if (result.isCancelled()) {
                      return CompletableFuture.failedFuture(new CancellationException());
                    }
                    if (methodNode == null) {
                      return CompletableFuture.failedFuture(
                          new UaException(
                              StatusCodes.Bad_NotFound,
                              "Method node is required for invocation: GetEncryptingKey"));
                    }
                    var inputMetadata =
                        ClientDataTypes.read(
                            this.client,
                            List.<ExpandedNodeId>of(
                                    ExpandedNodeId.parse("i=12"), ExpandedNodeId.parse("i=12"))
                                .subList(0, rawInputs.size()),
                            rawInputs.toArray());
                    result.whenComplete(
                        (cancelledValue, cancelledFailure) -> {
                          if (result.isCancelled()) {
                            inputMetadata.cancel(false);
                          }
                        });
                    return inputMetadata
                        .handle(
                            (inputDataTypeTree, inputMetadataFailure) -> {
                              if (inputMetadataFailure != null) {
                                var checkedMetadataFailureinputMetadataFailure =
                                    UaException.extract(inputMetadataFailure);
                                if (checkedMetadataFailureinputMetadataFailure.isPresent()) {
                                  throw new CompletionException(
                                      checkedMetadataFailureinputMetadataFailure.orElseThrow());
                                }
                                Throwable metadataCauseinputMetadataFailure = inputMetadataFailure;
                                while (metadataCauseinputMetadataFailure != null) {
                                  if (metadataCauseinputMetadataFailure
                                      instanceof
                                      UaSerializationException metadataCodecinputMetadataFailure) {
                                    throw new CompletionException(
                                        new UaException(
                                            metadataCodecinputMetadataFailure
                                                        .getStatusCode()
                                                        .getValue()
                                                    == StatusCodes.Bad_OutOfRange
                                                ? StatusCodes.Bad_OutOfRange
                                                : StatusCodes.Bad_TypeMismatch,
                                            metadataCodecinputMetadataFailure));
                                  }
                                  metadataCauseinputMetadataFailure =
                                      metadataCauseinputMetadataFailure.getCause();
                                }
                                throw new CompletionException(
                                    UaException.extract(inputMetadataFailure)
                                        .orElseGet(() -> new UaException(inputMetadataFailure)));
                              }
                              return inputDataTypeTree;
                            })
                        .thenCompose(
                            dataTypeTree -> {
                              if (result.isCancelled()) {
                                return CompletableFuture.failedFuture(new CancellationException());
                              }
                              try {
                                List<Variant> inputArguments = new ArrayList<>();
                                if (rawInputs.size() > 0) {
                                  Variant encoded0;
                                  {
                                    @Nullable String convertedValue;
                                    {
                                      Object methodValue = rawInputs.get(0);
                                      try {
                                        if (methodValue instanceof Matrix
                                            && ((Matrix) methodValue).isNull()) {
                                          methodValue = null;
                                        }
                                        NamespaceTable namespaceTable =
                                            this.client.getNamespaceTable();
                                        DataTypeTree dataTypeTree_ = dataTypeTree;
                                        NodeId argumentDataTypeId =
                                            ExpandedNodeId.parse("i=12")
                                                .toNodeId(namespaceTable)
                                                .orElseThrow(
                                                    () ->
                                                        new UaException(
                                                            StatusCodes.Bad_NodeIdInvalid,
                                                            "Method argument DataType namespace is"
                                                                + " unavailable"));
                                        if (!OpcUaDataType.isBuiltin(argumentDataTypeId)
                                            && dataTypeTree_.getDataType(argumentDataTypeId)
                                                == null) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method argument CredentialId (effective property"
                                                  + " i=17535, DataType i=12) is unavailable in the"
                                                  + " effective type tree; resolved DataType: "
                                                  + argumentDataTypeId);
                                        }
                                        methodValue =
                                            NumericValues.normalize(
                                                methodValue, dataTypeTree_, argumentDataTypeId);
                                        if (methodValue != null) {
                                          Object shapeElements =
                                              methodValue instanceof Matrix
                                                  ? ((Matrix) methodValue).getElements()
                                                  : methodValue;
                                          int valueRank =
                                              methodValue instanceof Matrix
                                                  ? ((Matrix) methodValue).getValueRank()
                                                  : ArrayUtil.getValueRank(methodValue);
                                          boolean emptyArray =
                                              methodValue.getClass().isArray()
                                                  && ArrayUtil.getValueRank(methodValue) == 1
                                                  && Array.getLength(methodValue) == 0;
                                          if (!(valueRank == -1)) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "Method argument ValueRank mismatch");
                                          }
                                          if (methodValue instanceof Matrix) {
                                            int[] dimensions =
                                                ((Matrix) methodValue).getDimensions();
                                            if (dimensions.length < 2
                                                || !shapeElements.getClass().isArray()
                                                || ArrayUtil.getValueRank(shapeElements) != 1) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Malformed Method Matrix representation");
                                            }
                                            long elementCount = 1;
                                            for (int dimension : dimensions) {
                                              if (dimension < 0
                                                  || elementCount > Integer.MAX_VALUE) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Malformed Method Matrix dimensions");
                                              }
                                              elementCount *= dimension;
                                            }
                                            if (elementCount != Array.getLength(shapeElements)) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Method Matrix dimensions do not match its"
                                                      + " elements");
                                            }
                                            if (!(((Matrix) methodValue)
                                                .getDataType()
                                                .equals(Variant.of(shapeElements).getDataType()))) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Method Matrix DataType does not match its"
                                                      + " elements");
                                            }
                                          }
                                          Variant.of(shapeElements);
                                        }
                                        if (methodValue != null) {
                                          Object typedElements =
                                              methodValue instanceof Matrix
                                                  ? ((Matrix) methodValue).getElements()
                                                  : methodValue;
                                          if (NodeIds.Structure.equals(argumentDataTypeId)
                                              || dataTypeTree_.isStructType(argumentDataTypeId)) {
                                            var declaredType =
                                                dataTypeTree_.getType(argumentDataTypeId);
                                            if (typedElements.getClass().isArray()) {
                                              var structureCodec =
                                                  this.client
                                                      .getStaticEncodingContext()
                                                      .getDataTypeManager()
                                                      .getCodec(argumentDataTypeId);
                                              Class<?> structureClass =
                                                  structureCodec == null
                                                      ? UaStructuredType.class
                                                      : structureCodec.getType();
                                              Object decodedStructures =
                                                  Array.newInstance(
                                                      structureClass,
                                                      Array.getLength(typedElements));
                                              for (int structureIndex = 0;
                                                  structureIndex < Array.getLength(typedElements);
                                                  structureIndex++) {
                                                Object structure =
                                                    Array.get(typedElements, structureIndex);
                                                if (structure instanceof ExtensionObject) {
                                                  structure =
                                                      ((ExtensionObject) structure).isNull()
                                                          ? null
                                                          : ((ExtensionObject) structure)
                                                              .decode(
                                                                  this.client
                                                                      .getStaticEncodingContext());
                                                }
                                                if (structure != null) {
                                                  if (!(structure instanceof UaStructuredType)) {
                                                    throw new UaException(
                                                        StatusCodes.Bad_TypeMismatch,
                                                        "Method argument requires a Structure"
                                                            + " value");
                                                  }
                                                  if (NodeIds.Structure.equals(argumentDataTypeId)
                                                      || declaredType != null
                                                          && declaredType.isAbstract()) {
                                                    if (!dataTypeTree_.isSubtypeOf(
                                                        ((UaStructuredType) structure)
                                                            .getTypeId()
                                                            .toNodeId(namespaceTable)
                                                            .orElse(NodeId.NULL_VALUE),
                                                        argumentDataTypeId)) {
                                                      throw new UaException(
                                                          StatusCodes.Bad_TypeMismatch,
                                                          "Method Structure is not a subtype of the"
                                                              + " effective DataType");
                                                    }
                                                  } else {
                                                    if (!argumentDataTypeId.equals(
                                                        ((UaStructuredType) structure)
                                                            .getTypeId()
                                                            .toNodeId(namespaceTable)
                                                            .orElse(NodeId.NULL_VALUE))) {
                                                      throw new UaException(
                                                          StatusCodes.Bad_TypeMismatch,
                                                          "Method Structure does not match the"
                                                              + " effective DataType");
                                                    }
                                                  }
                                                }
                                                Array.set(
                                                    decodedStructures, structureIndex, structure);
                                              }
                                              methodValue =
                                                  methodValue instanceof Matrix
                                                      ? new Matrix(
                                                          decodedStructures,
                                                          ((Matrix) methodValue)
                                                              .getDimensions()
                                                              .clone())
                                                      : decodedStructures;
                                            } else {
                                              if (typedElements instanceof ExtensionObject) {
                                                typedElements =
                                                    ((ExtensionObject) typedElements).isNull()
                                                        ? null
                                                        : ((ExtensionObject) typedElements)
                                                            .decode(
                                                                this.client
                                                                    .getStaticEncodingContext());
                                              }
                                              if (typedElements != null) {
                                                if (!(typedElements instanceof UaStructuredType)) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method argument requires a Structure value");
                                                }
                                                if (NodeIds.Structure.equals(argumentDataTypeId)
                                                    || declaredType != null
                                                        && declaredType.isAbstract()) {
                                                  if (!dataTypeTree_.isSubtypeOf(
                                                      ((UaStructuredType) typedElements)
                                                          .getTypeId()
                                                          .toNodeId(namespaceTable)
                                                          .orElse(NodeId.NULL_VALUE),
                                                      argumentDataTypeId)) {
                                                    throw new UaException(
                                                        StatusCodes.Bad_TypeMismatch,
                                                        "Method Structure is not a subtype of the"
                                                            + " effective DataType");
                                                  }
                                                } else {
                                                  if (!argumentDataTypeId.equals(
                                                      ((UaStructuredType) typedElements)
                                                          .getTypeId()
                                                          .toNodeId(namespaceTable)
                                                          .orElse(NodeId.NULL_VALUE))) {
                                                    throw new UaException(
                                                        StatusCodes.Bad_TypeMismatch,
                                                        "Method Structure does not match the"
                                                            + " effective DataType");
                                                  }
                                                }
                                              }
                                              methodValue = typedElements;
                                            }
                                          } else {
                                            Variant.of(typedElements);
                                            NodeId assignableDataTypeId =
                                                dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                            == Number.class
                                                        && dataTypeTree_.isSubtypeOf(
                                                            argumentDataTypeId, NodeIds.Integer)
                                                    ? NodeIds.Integer
                                                    : argumentDataTypeId;
                                            if (dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                    != Variant.class
                                                && !dataTypeTree_.isAssignable(
                                                    assignableDataTypeId,
                                                    ArrayUtil.getBoxedType(typedElements))) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Method argument DataType mismatch");
                                            }
                                          }
                                        }
                                        convertedValue = (String) methodValue;
                                      } catch (UaSerializationException conversionFailure) {
                                        throw new UaException(
                                            conversionFailure.getStatusCode().getValue()
                                                    == StatusCodes.Bad_OutOfRange
                                                ? StatusCodes.Bad_OutOfRange
                                                : StatusCodes.Bad_TypeMismatch,
                                            conversionFailure);
                                      } catch (ClassCastException
                                          | IllegalArgumentException conversionFailure) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch, conversionFailure);
                                      }
                                    }
                                    try {
                                      Object wireValue = convertedValue;
                                      Object wireElements =
                                          wireValue instanceof Matrix
                                              ? ((Matrix) wireValue).getElements()
                                              : wireValue;
                                      NumericValues.requireEncodable(wireValue);
                                      wireValue =
                                          ExtensionObject.encodeValue(
                                              this.client.getStaticEncodingContext(), wireValue);
                                      encoded0 = Variant.of(wireValue);
                                    } catch (UaSerializationException encodingFailure) {
                                      throw new UaException(
                                          encodingFailure.getStatusCode().getValue()
                                                  == StatusCodes.Bad_OutOfRange
                                              ? StatusCodes.Bad_OutOfRange
                                              : StatusCodes.Bad_TypeMismatch,
                                          encodingFailure);
                                    } catch (ClassCastException
                                        | IllegalArgumentException encodingFailure) {
                                      throw new UaException(
                                          StatusCodes.Bad_TypeMismatch, encodingFailure);
                                    }
                                  }
                                  inputArguments.add(encoded0);
                                }
                                if (rawInputs.size() > 1) {
                                  Variant encoded1;
                                  {
                                    @Nullable String convertedValue;
                                    {
                                      Object methodValue = rawInputs.get(1);
                                      try {
                                        if (methodValue instanceof Matrix
                                            && ((Matrix) methodValue).isNull()) {
                                          methodValue = null;
                                        }
                                        NamespaceTable namespaceTable =
                                            this.client.getNamespaceTable();
                                        DataTypeTree dataTypeTree_ = dataTypeTree;
                                        NodeId argumentDataTypeId =
                                            ExpandedNodeId.parse("i=12")
                                                .toNodeId(namespaceTable)
                                                .orElseThrow(
                                                    () ->
                                                        new UaException(
                                                            StatusCodes.Bad_NodeIdInvalid,
                                                            "Method argument DataType namespace is"
                                                                + " unavailable"));
                                        if (!OpcUaDataType.isBuiltin(argumentDataTypeId)
                                            && dataTypeTree_.getDataType(argumentDataTypeId)
                                                == null) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method argument RequestedSecurityPolicyUri"
                                                  + " (effective property i=17535, DataType i=12)"
                                                  + " is unavailable in the effective type tree;"
                                                  + " resolved DataType: "
                                                  + argumentDataTypeId);
                                        }
                                        methodValue =
                                            NumericValues.normalize(
                                                methodValue, dataTypeTree_, argumentDataTypeId);
                                        if (methodValue != null) {
                                          Object shapeElements =
                                              methodValue instanceof Matrix
                                                  ? ((Matrix) methodValue).getElements()
                                                  : methodValue;
                                          int valueRank =
                                              methodValue instanceof Matrix
                                                  ? ((Matrix) methodValue).getValueRank()
                                                  : ArrayUtil.getValueRank(methodValue);
                                          boolean emptyArray =
                                              methodValue.getClass().isArray()
                                                  && ArrayUtil.getValueRank(methodValue) == 1
                                                  && Array.getLength(methodValue) == 0;
                                          if (!(valueRank == -1)) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "Method argument ValueRank mismatch");
                                          }
                                          if (methodValue instanceof Matrix) {
                                            int[] dimensions =
                                                ((Matrix) methodValue).getDimensions();
                                            if (dimensions.length < 2
                                                || !shapeElements.getClass().isArray()
                                                || ArrayUtil.getValueRank(shapeElements) != 1) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Malformed Method Matrix representation");
                                            }
                                            long elementCount = 1;
                                            for (int dimension : dimensions) {
                                              if (dimension < 0
                                                  || elementCount > Integer.MAX_VALUE) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Malformed Method Matrix dimensions");
                                              }
                                              elementCount *= dimension;
                                            }
                                            if (elementCount != Array.getLength(shapeElements)) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Method Matrix dimensions do not match its"
                                                      + " elements");
                                            }
                                            if (!(((Matrix) methodValue)
                                                .getDataType()
                                                .equals(Variant.of(shapeElements).getDataType()))) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Method Matrix DataType does not match its"
                                                      + " elements");
                                            }
                                          }
                                          Variant.of(shapeElements);
                                        }
                                        if (methodValue != null) {
                                          Object typedElements =
                                              methodValue instanceof Matrix
                                                  ? ((Matrix) methodValue).getElements()
                                                  : methodValue;
                                          if (NodeIds.Structure.equals(argumentDataTypeId)
                                              || dataTypeTree_.isStructType(argumentDataTypeId)) {
                                            var declaredType =
                                                dataTypeTree_.getType(argumentDataTypeId);
                                            if (typedElements.getClass().isArray()) {
                                              var structureCodec =
                                                  this.client
                                                      .getStaticEncodingContext()
                                                      .getDataTypeManager()
                                                      .getCodec(argumentDataTypeId);
                                              Class<?> structureClass =
                                                  structureCodec == null
                                                      ? UaStructuredType.class
                                                      : structureCodec.getType();
                                              Object decodedStructures =
                                                  Array.newInstance(
                                                      structureClass,
                                                      Array.getLength(typedElements));
                                              for (int structureIndex = 0;
                                                  structureIndex < Array.getLength(typedElements);
                                                  structureIndex++) {
                                                Object structure =
                                                    Array.get(typedElements, structureIndex);
                                                if (structure instanceof ExtensionObject) {
                                                  structure =
                                                      ((ExtensionObject) structure).isNull()
                                                          ? null
                                                          : ((ExtensionObject) structure)
                                                              .decode(
                                                                  this.client
                                                                      .getStaticEncodingContext());
                                                }
                                                if (structure != null) {
                                                  if (!(structure instanceof UaStructuredType)) {
                                                    throw new UaException(
                                                        StatusCodes.Bad_TypeMismatch,
                                                        "Method argument requires a Structure"
                                                            + " value");
                                                  }
                                                  if (NodeIds.Structure.equals(argumentDataTypeId)
                                                      || declaredType != null
                                                          && declaredType.isAbstract()) {
                                                    if (!dataTypeTree_.isSubtypeOf(
                                                        ((UaStructuredType) structure)
                                                            .getTypeId()
                                                            .toNodeId(namespaceTable)
                                                            .orElse(NodeId.NULL_VALUE),
                                                        argumentDataTypeId)) {
                                                      throw new UaException(
                                                          StatusCodes.Bad_TypeMismatch,
                                                          "Method Structure is not a subtype of the"
                                                              + " effective DataType");
                                                    }
                                                  } else {
                                                    if (!argumentDataTypeId.equals(
                                                        ((UaStructuredType) structure)
                                                            .getTypeId()
                                                            .toNodeId(namespaceTable)
                                                            .orElse(NodeId.NULL_VALUE))) {
                                                      throw new UaException(
                                                          StatusCodes.Bad_TypeMismatch,
                                                          "Method Structure does not match the"
                                                              + " effective DataType");
                                                    }
                                                  }
                                                }
                                                Array.set(
                                                    decodedStructures, structureIndex, structure);
                                              }
                                              methodValue =
                                                  methodValue instanceof Matrix
                                                      ? new Matrix(
                                                          decodedStructures,
                                                          ((Matrix) methodValue)
                                                              .getDimensions()
                                                              .clone())
                                                      : decodedStructures;
                                            } else {
                                              if (typedElements instanceof ExtensionObject) {
                                                typedElements =
                                                    ((ExtensionObject) typedElements).isNull()
                                                        ? null
                                                        : ((ExtensionObject) typedElements)
                                                            .decode(
                                                                this.client
                                                                    .getStaticEncodingContext());
                                              }
                                              if (typedElements != null) {
                                                if (!(typedElements instanceof UaStructuredType)) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method argument requires a Structure value");
                                                }
                                                if (NodeIds.Structure.equals(argumentDataTypeId)
                                                    || declaredType != null
                                                        && declaredType.isAbstract()) {
                                                  if (!dataTypeTree_.isSubtypeOf(
                                                      ((UaStructuredType) typedElements)
                                                          .getTypeId()
                                                          .toNodeId(namespaceTable)
                                                          .orElse(NodeId.NULL_VALUE),
                                                      argumentDataTypeId)) {
                                                    throw new UaException(
                                                        StatusCodes.Bad_TypeMismatch,
                                                        "Method Structure is not a subtype of the"
                                                            + " effective DataType");
                                                  }
                                                } else {
                                                  if (!argumentDataTypeId.equals(
                                                      ((UaStructuredType) typedElements)
                                                          .getTypeId()
                                                          .toNodeId(namespaceTable)
                                                          .orElse(NodeId.NULL_VALUE))) {
                                                    throw new UaException(
                                                        StatusCodes.Bad_TypeMismatch,
                                                        "Method Structure does not match the"
                                                            + " effective DataType");
                                                  }
                                                }
                                              }
                                              methodValue = typedElements;
                                            }
                                          } else {
                                            Variant.of(typedElements);
                                            NodeId assignableDataTypeId =
                                                dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                            == Number.class
                                                        && dataTypeTree_.isSubtypeOf(
                                                            argumentDataTypeId, NodeIds.Integer)
                                                    ? NodeIds.Integer
                                                    : argumentDataTypeId;
                                            if (dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                    != Variant.class
                                                && !dataTypeTree_.isAssignable(
                                                    assignableDataTypeId,
                                                    ArrayUtil.getBoxedType(typedElements))) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Method argument DataType mismatch");
                                            }
                                          }
                                        }
                                        convertedValue = (String) methodValue;
                                      } catch (UaSerializationException conversionFailure) {
                                        throw new UaException(
                                            conversionFailure.getStatusCode().getValue()
                                                    == StatusCodes.Bad_OutOfRange
                                                ? StatusCodes.Bad_OutOfRange
                                                : StatusCodes.Bad_TypeMismatch,
                                            conversionFailure);
                                      } catch (ClassCastException
                                          | IllegalArgumentException conversionFailure) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch, conversionFailure);
                                      }
                                    }
                                    try {
                                      Object wireValue = convertedValue;
                                      Object wireElements =
                                          wireValue instanceof Matrix
                                              ? ((Matrix) wireValue).getElements()
                                              : wireValue;
                                      NumericValues.requireEncodable(wireValue);
                                      wireValue =
                                          ExtensionObject.encodeValue(
                                              this.client.getStaticEncodingContext(), wireValue);
                                      encoded1 = Variant.of(wireValue);
                                    } catch (UaSerializationException encodingFailure) {
                                      throw new UaException(
                                          encodingFailure.getStatusCode().getValue()
                                                  == StatusCodes.Bad_OutOfRange
                                              ? StatusCodes.Bad_OutOfRange
                                              : StatusCodes.Bad_TypeMismatch,
                                          encodingFailure);
                                    } catch (ClassCastException
                                        | IllegalArgumentException encodingFailure) {
                                      throw new UaException(
                                          StatusCodes.Bad_TypeMismatch, encodingFailure);
                                    }
                                  }
                                  inputArguments.add(encoded1);
                                }
                                CallMethodRequest request =
                                    new CallMethodRequest(
                                        getNodeId(),
                                        methodNode.getNodeId(),
                                        inputArguments.toArray(new Variant[0]));
                                return this.client
                                    .getSessionAsync()
                                    .thenCompose(
                                        session -> {
                                          if (result.isCancelled()) {
                                            return CompletableFuture.failedFuture(
                                                new CancellationException());
                                          }
                                          RequestHeader originalHeader =
                                              this.client.newRequestHeader(
                                                  session.getAuthenticationToken());
                                          RequestHeader requestHeader =
                                              new RequestHeader(
                                                  originalHeader.getAuthenticationToken(),
                                                  originalHeader.getTimestamp(),
                                                  originalHeader.getRequestHandle(),
                                                  options.returnDiagnostics(),
                                                  originalHeader.getAuditEntryId(),
                                                  originalHeader.getTimeoutHint(),
                                                  originalHeader.getAdditionalHeader());
                                          var call =
                                              this.client.sendRequestAsync(
                                                  new CallRequest(
                                                      requestHeader,
                                                      new CallMethodRequest[] {request}));
                                          result.whenComplete(
                                              (value, failure) -> {
                                                if (result.isCancelled()) {
                                                  call.cancel(false);
                                                }
                                              });
                                          return call.thenCompose(
                                              message -> {
                                                if (result.isCancelled()) {
                                                  throw new CancellationException();
                                                }
                                                try {
                                                  if (!(message instanceof CallResponse response)
                                                      || response.getResponseHeader() == null
                                                      || response
                                                              .getResponseHeader()
                                                              .getServiceResult()
                                                          == null) {
                                                    throw new UaException(
                                                        StatusCodes.Bad_UnexpectedError,
                                                        "Malformed Call response header");
                                                  }
                                                  if (!response
                                                      .getResponseHeader()
                                                      .getServiceResult()
                                                      .isGood()) {
                                                    throw new UaException(
                                                        response
                                                            .getResponseHeader()
                                                            .getServiceResult());
                                                  }
                                                  if (response.getResults() == null
                                                      || response.getResults().length != 1
                                                      || response.getResults()[0] == null
                                                      || response.getResults()[0].getStatusCode()
                                                          == null) {
                                                    throw new UaException(
                                                        StatusCodes.Bad_UnexpectedError,
                                                        "Expected exactly one Call operation"
                                                            + " result");
                                                  }
                                                  CompletableFuture<DataTypeTree> outputMetadata =
                                                      response
                                                              .getResults()[0]
                                                              .getStatusCode()
                                                              .isBad()
                                                          ? CompletableFuture.completedFuture(
                                                              dataTypeTree)
                                                          : ClientDataTypes.read(
                                                              this.client,
                                                              List.<ExpandedNodeId>of(
                                                                  ExpandedNodeId.parse("i=15"),
                                                                  ExpandedNodeId.parse("i=12")),
                                                              (Object)
                                                                  response.getResults()[0]
                                                                      .getOutputArguments());
                                                  result.whenComplete(
                                                      (cancelledValue, cancelledFailure) -> {
                                                        if (result.isCancelled()) {
                                                          outputMetadata.cancel(false);
                                                        }
                                                      });
                                                  return outputMetadata.handle(
                                                      (outputDataTypeTree, metadataFailure) -> {
                                                        if (result.isCancelled()) {
                                                          throw new CancellationException();
                                                        }
                                                        try {
                                                          return MethodCallResult.decode(
                                                              request,
                                                              requestHeader,
                                                              response,
                                                              0,
                                                              outputArguments -> {
                                                                if (metadataFailure != null) {
                                                                  var
                                                                      checkedMetadataFailuremetadataFailure =
                                                                          UaException.extract(
                                                                              metadataFailure);
                                                                  if (checkedMetadataFailuremetadataFailure
                                                                      .isPresent()) {
                                                                    throw checkedMetadataFailuremetadataFailure
                                                                        .orElseThrow();
                                                                  }
                                                                  Throwable
                                                                      metadataCausemetadataFailure =
                                                                          metadataFailure;
                                                                  while (metadataCausemetadataFailure
                                                                      != null) {
                                                                    if (metadataCausemetadataFailure
                                                                        instanceof
                                                                        UaSerializationException
                                                                            metadataCodecmetadataFailure) {
                                                                      throw new UaException(
                                                                          metadataCodecmetadataFailure
                                                                                      .getStatusCode()
                                                                                      .getValue()
                                                                                  == StatusCodes
                                                                                      .Bad_OutOfRange
                                                                              ? StatusCodes
                                                                                  .Bad_OutOfRange
                                                                              : StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                          metadataCodecmetadataFailure);
                                                                    }
                                                                    metadataCausemetadataFailure =
                                                                        metadataCausemetadataFailure
                                                                            .getCause();
                                                                  }
                                                                  throw UaException.extract(
                                                                          metadataFailure)
                                                                      .orElseGet(
                                                                          () ->
                                                                              new UaException(
                                                                                  metadataFailure));
                                                                }
                                                                if ((outputArguments == null
                                                                        ? 0
                                                                        : outputArguments.length)
                                                                    != 2) {
                                                                  throw new UaException(
                                                                      StatusCodes.Bad_TypeMismatch,
                                                                      "Unexpected Method output"
                                                                          + " count");
                                                                }
                                                                if (outputArguments[0] == null) {
                                                                  throw new UaException(
                                                                      StatusCodes.Bad_TypeMismatch,
                                                                      "Null Method output Variant");
                                                                }
                                                                @Nullable ByteString decoded0;
                                                                {
                                                                  Object methodValue =
                                                                      outputArguments[0].getValue();
                                                                  try {
                                                                    if (methodValue
                                                                            instanceof Matrix
                                                                        && ((Matrix) methodValue)
                                                                            .isNull()) {
                                                                      methodValue = null;
                                                                    }
                                                                    NamespaceTable namespaceTable =
                                                                        this.client
                                                                            .getNamespaceTable();
                                                                    DataTypeTree dataTypeTree_ =
                                                                        outputDataTypeTree;
                                                                    NodeId argumentDataTypeId =
                                                                        ExpandedNodeId.parse("i=15")
                                                                            .toNodeId(
                                                                                namespaceTable)
                                                                            .orElseThrow(
                                                                                () ->
                                                                                    new UaException(
                                                                                        StatusCodes
                                                                                            .Bad_NodeIdInvalid,
                                                                                        "Method"
                                                                                            + " argument"
                                                                                            + " DataType"
                                                                                            + " namespace"
                                                                                            + " is unavailable"));
                                                                    if (!OpcUaDataType.isBuiltin(
                                                                            argumentDataTypeId)
                                                                        && dataTypeTree_
                                                                                .getDataType(
                                                                                    argumentDataTypeId)
                                                                            == null) {
                                                                      throw new UaException(
                                                                          StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                          "Method argument"
                                                                              + " PublicKey"
                                                                              + " (effective"
                                                                              + " property i=17536,"
                                                                              + " DataType i=15) is"
                                                                              + " unavailable in"
                                                                              + " the effective"
                                                                              + " type tree;"
                                                                              + " resolved"
                                                                              + " DataType: "
                                                                              + argumentDataTypeId);
                                                                    }
                                                                    methodValue =
                                                                        NumericValues.normalize(
                                                                            methodValue,
                                                                            dataTypeTree_,
                                                                            argumentDataTypeId);
                                                                    if (methodValue != null) {
                                                                      Object shapeElements =
                                                                          methodValue
                                                                                  instanceof Matrix
                                                                              ? ((Matrix)
                                                                                      methodValue)
                                                                                  .getElements()
                                                                              : methodValue;
                                                                      int valueRank =
                                                                          methodValue
                                                                                  instanceof Matrix
                                                                              ? ((Matrix)
                                                                                      methodValue)
                                                                                  .getValueRank()
                                                                              : ArrayUtil
                                                                                  .getValueRank(
                                                                                      methodValue);
                                                                      boolean emptyArray =
                                                                          methodValue
                                                                                  .getClass()
                                                                                  .isArray()
                                                                              && ArrayUtil
                                                                                      .getValueRank(
                                                                                          methodValue)
                                                                                  == 1
                                                                              && Array.getLength(
                                                                                      methodValue)
                                                                                  == 0;
                                                                      if (!(valueRank == -1)) {
                                                                        throw new UaException(
                                                                            StatusCodes
                                                                                .Bad_TypeMismatch,
                                                                            "Method argument"
                                                                                + " ValueRank"
                                                                                + " mismatch");
                                                                      }
                                                                      if (methodValue
                                                                          instanceof Matrix) {
                                                                        int[] dimensions =
                                                                            ((Matrix) methodValue)
                                                                                .getDimensions();
                                                                        if (dimensions.length < 2
                                                                            || !shapeElements
                                                                                .getClass()
                                                                                .isArray()
                                                                            || ArrayUtil
                                                                                    .getValueRank(
                                                                                        shapeElements)
                                                                                != 1) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Malformed Method"
                                                                                  + " Matrix"
                                                                                  + " representation");
                                                                        }
                                                                        long elementCount = 1;
                                                                        for (int dimension :
                                                                            dimensions) {
                                                                          if (dimension < 0
                                                                              || elementCount
                                                                                  > Integer
                                                                                      .MAX_VALUE) {
                                                                            throw new UaException(
                                                                                StatusCodes
                                                                                    .Bad_TypeMismatch,
                                                                                "Malformed Method"
                                                                                    + " Matrix"
                                                                                    + " dimensions");
                                                                          }
                                                                          elementCount *= dimension;
                                                                        }
                                                                        if (elementCount
                                                                            != Array.getLength(
                                                                                shapeElements)) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Method Matrix"
                                                                                  + " dimensions do"
                                                                                  + " not match its"
                                                                                  + " elements");
                                                                        }
                                                                        if (!(((Matrix) methodValue)
                                                                            .getDataType()
                                                                            .equals(
                                                                                Variant.of(
                                                                                        shapeElements)
                                                                                    .getDataType()))) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Method Matrix"
                                                                                  + " DataType does"
                                                                                  + " not match its"
                                                                                  + " elements");
                                                                        }
                                                                      }
                                                                      Variant.of(shapeElements);
                                                                    }
                                                                    if (methodValue != null) {
                                                                      Object typedElements =
                                                                          methodValue
                                                                                  instanceof Matrix
                                                                              ? ((Matrix)
                                                                                      methodValue)
                                                                                  .getElements()
                                                                              : methodValue;
                                                                      if (NodeIds.Structure.equals(
                                                                              argumentDataTypeId)
                                                                          || dataTypeTree_
                                                                              .isStructType(
                                                                                  argumentDataTypeId)) {
                                                                        var declaredType =
                                                                            dataTypeTree_.getType(
                                                                                argumentDataTypeId);
                                                                        if (typedElements
                                                                            .getClass()
                                                                            .isArray()) {
                                                                          var structureCodec =
                                                                              this.client
                                                                                  .getStaticEncodingContext()
                                                                                  .getDataTypeManager()
                                                                                  .getCodec(
                                                                                      argumentDataTypeId);
                                                                          Class<?> structureClass =
                                                                              structureCodec == null
                                                                                  ? UaStructuredType
                                                                                      .class
                                                                                  : structureCodec
                                                                                      .getType();
                                                                          Object decodedStructures =
                                                                              Array.newInstance(
                                                                                  structureClass,
                                                                                  Array.getLength(
                                                                                      typedElements));
                                                                          for (int structureIndex =
                                                                                  0;
                                                                              structureIndex
                                                                                  < Array.getLength(
                                                                                      typedElements);
                                                                              structureIndex++) {
                                                                            Object structure =
                                                                                Array.get(
                                                                                    typedElements,
                                                                                    structureIndex);
                                                                            if (structure
                                                                                instanceof
                                                                                ExtensionObject) {
                                                                              structure =
                                                                                  ((ExtensionObject)
                                                                                              structure)
                                                                                          .isNull()
                                                                                      ? null
                                                                                      : ((ExtensionObject)
                                                                                              structure)
                                                                                          .decode(
                                                                                              this
                                                                                                  .client
                                                                                                  .getStaticEncodingContext());
                                                                            }
                                                                            if (structure != null) {
                                                                              if (!(structure
                                                                                  instanceof
                                                                                  UaStructuredType)) {
                                                                                throw new UaException(
                                                                                    StatusCodes
                                                                                        .Bad_TypeMismatch,
                                                                                    "Method"
                                                                                        + " argument"
                                                                                        + " requires"
                                                                                        + " a Structure"
                                                                                        + " value");
                                                                              }
                                                                              if (NodeIds.Structure
                                                                                      .equals(
                                                                                          argumentDataTypeId)
                                                                                  || declaredType
                                                                                          != null
                                                                                      && declaredType
                                                                                          .isAbstract()) {
                                                                                if (!dataTypeTree_
                                                                                    .isSubtypeOf(
                                                                                        ((UaStructuredType)
                                                                                                structure)
                                                                                            .getTypeId()
                                                                                            .toNodeId(
                                                                                                namespaceTable)
                                                                                            .orElse(
                                                                                                NodeId
                                                                                                    .NULL_VALUE),
                                                                                        argumentDataTypeId)) {
                                                                                  throw new UaException(
                                                                                      StatusCodes
                                                                                          .Bad_TypeMismatch,
                                                                                      "Method"
                                                                                          + " Structure"
                                                                                          + " is not"
                                                                                          + " a subtype"
                                                                                          + " of the"
                                                                                          + " effective"
                                                                                          + " DataType");
                                                                                }
                                                                              } else {
                                                                                if (!argumentDataTypeId
                                                                                    .equals(
                                                                                        ((UaStructuredType)
                                                                                                structure)
                                                                                            .getTypeId()
                                                                                            .toNodeId(
                                                                                                namespaceTable)
                                                                                            .orElse(
                                                                                                NodeId
                                                                                                    .NULL_VALUE))) {
                                                                                  throw new UaException(
                                                                                      StatusCodes
                                                                                          .Bad_TypeMismatch,
                                                                                      "Method"
                                                                                          + " Structure"
                                                                                          + " does"
                                                                                          + " not match"
                                                                                          + " the effective"
                                                                                          + " DataType");
                                                                                }
                                                                              }
                                                                            }
                                                                            Array.set(
                                                                                decodedStructures,
                                                                                structureIndex,
                                                                                structure);
                                                                          }
                                                                          methodValue =
                                                                              methodValue
                                                                                      instanceof
                                                                                      Matrix
                                                                                  ? new Matrix(
                                                                                      decodedStructures,
                                                                                      ((Matrix)
                                                                                              methodValue)
                                                                                          .getDimensions()
                                                                                          .clone())
                                                                                  : decodedStructures;
                                                                        } else {
                                                                          if (typedElements
                                                                              instanceof
                                                                              ExtensionObject) {
                                                                            typedElements =
                                                                                ((ExtensionObject)
                                                                                            typedElements)
                                                                                        .isNull()
                                                                                    ? null
                                                                                    : ((ExtensionObject)
                                                                                            typedElements)
                                                                                        .decode(
                                                                                            this
                                                                                                .client
                                                                                                .getStaticEncodingContext());
                                                                          }
                                                                          if (typedElements
                                                                              != null) {
                                                                            if (!(typedElements
                                                                                instanceof
                                                                                UaStructuredType)) {
                                                                              throw new UaException(
                                                                                  StatusCodes
                                                                                      .Bad_TypeMismatch,
                                                                                  "Method argument"
                                                                                      + " requires"
                                                                                      + " a Structure"
                                                                                      + " value");
                                                                            }
                                                                            if (NodeIds.Structure
                                                                                    .equals(
                                                                                        argumentDataTypeId)
                                                                                || declaredType
                                                                                        != null
                                                                                    && declaredType
                                                                                        .isAbstract()) {
                                                                              if (!dataTypeTree_
                                                                                  .isSubtypeOf(
                                                                                      ((UaStructuredType)
                                                                                              typedElements)
                                                                                          .getTypeId()
                                                                                          .toNodeId(
                                                                                              namespaceTable)
                                                                                          .orElse(
                                                                                              NodeId
                                                                                                  .NULL_VALUE),
                                                                                      argumentDataTypeId)) {
                                                                                throw new UaException(
                                                                                    StatusCodes
                                                                                        .Bad_TypeMismatch,
                                                                                    "Method"
                                                                                        + " Structure"
                                                                                        + " is not"
                                                                                        + " a subtype"
                                                                                        + " of the"
                                                                                        + " effective"
                                                                                        + " DataType");
                                                                              }
                                                                            } else {
                                                                              if (!argumentDataTypeId
                                                                                  .equals(
                                                                                      ((UaStructuredType)
                                                                                              typedElements)
                                                                                          .getTypeId()
                                                                                          .toNodeId(
                                                                                              namespaceTable)
                                                                                          .orElse(
                                                                                              NodeId
                                                                                                  .NULL_VALUE))) {
                                                                                throw new UaException(
                                                                                    StatusCodes
                                                                                        .Bad_TypeMismatch,
                                                                                    "Method"
                                                                                        + " Structure"
                                                                                        + " does"
                                                                                        + " not match"
                                                                                        + " the effective"
                                                                                        + " DataType");
                                                                              }
                                                                            }
                                                                          }
                                                                          methodValue =
                                                                              typedElements;
                                                                        }
                                                                      } else {
                                                                        Variant.of(typedElements);
                                                                        NodeId
                                                                            assignableDataTypeId =
                                                                                dataTypeTree_
                                                                                                .getBackingClass(
                                                                                                    argumentDataTypeId)
                                                                                            == Number
                                                                                                .class
                                                                                        && dataTypeTree_
                                                                                            .isSubtypeOf(
                                                                                                argumentDataTypeId,
                                                                                                NodeIds
                                                                                                    .Integer)
                                                                                    ? NodeIds
                                                                                        .Integer
                                                                                    : argumentDataTypeId;
                                                                        if (dataTypeTree_
                                                                                    .getBackingClass(
                                                                                        argumentDataTypeId)
                                                                                != Variant.class
                                                                            && !dataTypeTree_
                                                                                .isAssignable(
                                                                                    assignableDataTypeId,
                                                                                    ArrayUtil
                                                                                        .getBoxedType(
                                                                                            typedElements))) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Method argument"
                                                                                  + " DataType"
                                                                                  + " mismatch");
                                                                        }
                                                                      }
                                                                    }
                                                                    decoded0 =
                                                                        (ByteString) methodValue;
                                                                  } catch (
                                                                      UaSerializationException
                                                                          conversionFailure) {
                                                                    throw new UaException(
                                                                        conversionFailure
                                                                                    .getStatusCode()
                                                                                    .getValue()
                                                                                == StatusCodes
                                                                                    .Bad_OutOfRange
                                                                            ? StatusCodes
                                                                                .Bad_OutOfRange
                                                                            : StatusCodes
                                                                                .Bad_TypeMismatch,
                                                                        conversionFailure);
                                                                  } catch (ClassCastException
                                                                      | IllegalArgumentException
                                                                          conversionFailure) {
                                                                    throw new UaException(
                                                                        StatusCodes
                                                                            .Bad_TypeMismatch,
                                                                        conversionFailure);
                                                                  }
                                                                }
                                                                if (outputArguments[1] == null) {
                                                                  throw new UaException(
                                                                      StatusCodes.Bad_TypeMismatch,
                                                                      "Null Method output Variant");
                                                                }
                                                                @Nullable String decoded1;
                                                                {
                                                                  Object methodValue =
                                                                      outputArguments[1].getValue();
                                                                  try {
                                                                    if (methodValue
                                                                            instanceof Matrix
                                                                        && ((Matrix) methodValue)
                                                                            .isNull()) {
                                                                      methodValue = null;
                                                                    }
                                                                    NamespaceTable namespaceTable =
                                                                        this.client
                                                                            .getNamespaceTable();
                                                                    DataTypeTree dataTypeTree_ =
                                                                        outputDataTypeTree;
                                                                    NodeId argumentDataTypeId =
                                                                        ExpandedNodeId.parse("i=12")
                                                                            .toNodeId(
                                                                                namespaceTable)
                                                                            .orElseThrow(
                                                                                () ->
                                                                                    new UaException(
                                                                                        StatusCodes
                                                                                            .Bad_NodeIdInvalid,
                                                                                        "Method"
                                                                                            + " argument"
                                                                                            + " DataType"
                                                                                            + " namespace"
                                                                                            + " is unavailable"));
                                                                    if (!OpcUaDataType.isBuiltin(
                                                                            argumentDataTypeId)
                                                                        && dataTypeTree_
                                                                                .getDataType(
                                                                                    argumentDataTypeId)
                                                                            == null) {
                                                                      throw new UaException(
                                                                          StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                          "Method argument"
                                                                              + " RevisedSecurityPolicyUri"
                                                                              + " (effective"
                                                                              + " property i=17536,"
                                                                              + " DataType i=12) is"
                                                                              + " unavailable in"
                                                                              + " the effective"
                                                                              + " type tree;"
                                                                              + " resolved"
                                                                              + " DataType: "
                                                                              + argumentDataTypeId);
                                                                    }
                                                                    methodValue =
                                                                        NumericValues.normalize(
                                                                            methodValue,
                                                                            dataTypeTree_,
                                                                            argumentDataTypeId);
                                                                    if (methodValue != null) {
                                                                      Object shapeElements =
                                                                          methodValue
                                                                                  instanceof Matrix
                                                                              ? ((Matrix)
                                                                                      methodValue)
                                                                                  .getElements()
                                                                              : methodValue;
                                                                      int valueRank =
                                                                          methodValue
                                                                                  instanceof Matrix
                                                                              ? ((Matrix)
                                                                                      methodValue)
                                                                                  .getValueRank()
                                                                              : ArrayUtil
                                                                                  .getValueRank(
                                                                                      methodValue);
                                                                      boolean emptyArray =
                                                                          methodValue
                                                                                  .getClass()
                                                                                  .isArray()
                                                                              && ArrayUtil
                                                                                      .getValueRank(
                                                                                          methodValue)
                                                                                  == 1
                                                                              && Array.getLength(
                                                                                      methodValue)
                                                                                  == 0;
                                                                      if (!(valueRank == -1)) {
                                                                        throw new UaException(
                                                                            StatusCodes
                                                                                .Bad_TypeMismatch,
                                                                            "Method argument"
                                                                                + " ValueRank"
                                                                                + " mismatch");
                                                                      }
                                                                      if (methodValue
                                                                          instanceof Matrix) {
                                                                        int[] dimensions =
                                                                            ((Matrix) methodValue)
                                                                                .getDimensions();
                                                                        if (dimensions.length < 2
                                                                            || !shapeElements
                                                                                .getClass()
                                                                                .isArray()
                                                                            || ArrayUtil
                                                                                    .getValueRank(
                                                                                        shapeElements)
                                                                                != 1) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Malformed Method"
                                                                                  + " Matrix"
                                                                                  + " representation");
                                                                        }
                                                                        long elementCount = 1;
                                                                        for (int dimension :
                                                                            dimensions) {
                                                                          if (dimension < 0
                                                                              || elementCount
                                                                                  > Integer
                                                                                      .MAX_VALUE) {
                                                                            throw new UaException(
                                                                                StatusCodes
                                                                                    .Bad_TypeMismatch,
                                                                                "Malformed Method"
                                                                                    + " Matrix"
                                                                                    + " dimensions");
                                                                          }
                                                                          elementCount *= dimension;
                                                                        }
                                                                        if (elementCount
                                                                            != Array.getLength(
                                                                                shapeElements)) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Method Matrix"
                                                                                  + " dimensions do"
                                                                                  + " not match its"
                                                                                  + " elements");
                                                                        }
                                                                        if (!(((Matrix) methodValue)
                                                                            .getDataType()
                                                                            .equals(
                                                                                Variant.of(
                                                                                        shapeElements)
                                                                                    .getDataType()))) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Method Matrix"
                                                                                  + " DataType does"
                                                                                  + " not match its"
                                                                                  + " elements");
                                                                        }
                                                                      }
                                                                      Variant.of(shapeElements);
                                                                    }
                                                                    if (methodValue != null) {
                                                                      Object typedElements =
                                                                          methodValue
                                                                                  instanceof Matrix
                                                                              ? ((Matrix)
                                                                                      methodValue)
                                                                                  .getElements()
                                                                              : methodValue;
                                                                      if (NodeIds.Structure.equals(
                                                                              argumentDataTypeId)
                                                                          || dataTypeTree_
                                                                              .isStructType(
                                                                                  argumentDataTypeId)) {
                                                                        var declaredType =
                                                                            dataTypeTree_.getType(
                                                                                argumentDataTypeId);
                                                                        if (typedElements
                                                                            .getClass()
                                                                            .isArray()) {
                                                                          var structureCodec =
                                                                              this.client
                                                                                  .getStaticEncodingContext()
                                                                                  .getDataTypeManager()
                                                                                  .getCodec(
                                                                                      argumentDataTypeId);
                                                                          Class<?> structureClass =
                                                                              structureCodec == null
                                                                                  ? UaStructuredType
                                                                                      .class
                                                                                  : structureCodec
                                                                                      .getType();
                                                                          Object decodedStructures =
                                                                              Array.newInstance(
                                                                                  structureClass,
                                                                                  Array.getLength(
                                                                                      typedElements));
                                                                          for (int structureIndex =
                                                                                  0;
                                                                              structureIndex
                                                                                  < Array.getLength(
                                                                                      typedElements);
                                                                              structureIndex++) {
                                                                            Object structure =
                                                                                Array.get(
                                                                                    typedElements,
                                                                                    structureIndex);
                                                                            if (structure
                                                                                instanceof
                                                                                ExtensionObject) {
                                                                              structure =
                                                                                  ((ExtensionObject)
                                                                                              structure)
                                                                                          .isNull()
                                                                                      ? null
                                                                                      : ((ExtensionObject)
                                                                                              structure)
                                                                                          .decode(
                                                                                              this
                                                                                                  .client
                                                                                                  .getStaticEncodingContext());
                                                                            }
                                                                            if (structure != null) {
                                                                              if (!(structure
                                                                                  instanceof
                                                                                  UaStructuredType)) {
                                                                                throw new UaException(
                                                                                    StatusCodes
                                                                                        .Bad_TypeMismatch,
                                                                                    "Method"
                                                                                        + " argument"
                                                                                        + " requires"
                                                                                        + " a Structure"
                                                                                        + " value");
                                                                              }
                                                                              if (NodeIds.Structure
                                                                                      .equals(
                                                                                          argumentDataTypeId)
                                                                                  || declaredType
                                                                                          != null
                                                                                      && declaredType
                                                                                          .isAbstract()) {
                                                                                if (!dataTypeTree_
                                                                                    .isSubtypeOf(
                                                                                        ((UaStructuredType)
                                                                                                structure)
                                                                                            .getTypeId()
                                                                                            .toNodeId(
                                                                                                namespaceTable)
                                                                                            .orElse(
                                                                                                NodeId
                                                                                                    .NULL_VALUE),
                                                                                        argumentDataTypeId)) {
                                                                                  throw new UaException(
                                                                                      StatusCodes
                                                                                          .Bad_TypeMismatch,
                                                                                      "Method"
                                                                                          + " Structure"
                                                                                          + " is not"
                                                                                          + " a subtype"
                                                                                          + " of the"
                                                                                          + " effective"
                                                                                          + " DataType");
                                                                                }
                                                                              } else {
                                                                                if (!argumentDataTypeId
                                                                                    .equals(
                                                                                        ((UaStructuredType)
                                                                                                structure)
                                                                                            .getTypeId()
                                                                                            .toNodeId(
                                                                                                namespaceTable)
                                                                                            .orElse(
                                                                                                NodeId
                                                                                                    .NULL_VALUE))) {
                                                                                  throw new UaException(
                                                                                      StatusCodes
                                                                                          .Bad_TypeMismatch,
                                                                                      "Method"
                                                                                          + " Structure"
                                                                                          + " does"
                                                                                          + " not match"
                                                                                          + " the effective"
                                                                                          + " DataType");
                                                                                }
                                                                              }
                                                                            }
                                                                            Array.set(
                                                                                decodedStructures,
                                                                                structureIndex,
                                                                                structure);
                                                                          }
                                                                          methodValue =
                                                                              methodValue
                                                                                      instanceof
                                                                                      Matrix
                                                                                  ? new Matrix(
                                                                                      decodedStructures,
                                                                                      ((Matrix)
                                                                                              methodValue)
                                                                                          .getDimensions()
                                                                                          .clone())
                                                                                  : decodedStructures;
                                                                        } else {
                                                                          if (typedElements
                                                                              instanceof
                                                                              ExtensionObject) {
                                                                            typedElements =
                                                                                ((ExtensionObject)
                                                                                            typedElements)
                                                                                        .isNull()
                                                                                    ? null
                                                                                    : ((ExtensionObject)
                                                                                            typedElements)
                                                                                        .decode(
                                                                                            this
                                                                                                .client
                                                                                                .getStaticEncodingContext());
                                                                          }
                                                                          if (typedElements
                                                                              != null) {
                                                                            if (!(typedElements
                                                                                instanceof
                                                                                UaStructuredType)) {
                                                                              throw new UaException(
                                                                                  StatusCodes
                                                                                      .Bad_TypeMismatch,
                                                                                  "Method argument"
                                                                                      + " requires"
                                                                                      + " a Structure"
                                                                                      + " value");
                                                                            }
                                                                            if (NodeIds.Structure
                                                                                    .equals(
                                                                                        argumentDataTypeId)
                                                                                || declaredType
                                                                                        != null
                                                                                    && declaredType
                                                                                        .isAbstract()) {
                                                                              if (!dataTypeTree_
                                                                                  .isSubtypeOf(
                                                                                      ((UaStructuredType)
                                                                                              typedElements)
                                                                                          .getTypeId()
                                                                                          .toNodeId(
                                                                                              namespaceTable)
                                                                                          .orElse(
                                                                                              NodeId
                                                                                                  .NULL_VALUE),
                                                                                      argumentDataTypeId)) {
                                                                                throw new UaException(
                                                                                    StatusCodes
                                                                                        .Bad_TypeMismatch,
                                                                                    "Method"
                                                                                        + " Structure"
                                                                                        + " is not"
                                                                                        + " a subtype"
                                                                                        + " of the"
                                                                                        + " effective"
                                                                                        + " DataType");
                                                                              }
                                                                            } else {
                                                                              if (!argumentDataTypeId
                                                                                  .equals(
                                                                                      ((UaStructuredType)
                                                                                              typedElements)
                                                                                          .getTypeId()
                                                                                          .toNodeId(
                                                                                              namespaceTable)
                                                                                          .orElse(
                                                                                              NodeId
                                                                                                  .NULL_VALUE))) {
                                                                                throw new UaException(
                                                                                    StatusCodes
                                                                                        .Bad_TypeMismatch,
                                                                                    "Method"
                                                                                        + " Structure"
                                                                                        + " does"
                                                                                        + " not match"
                                                                                        + " the effective"
                                                                                        + " DataType");
                                                                              }
                                                                            }
                                                                          }
                                                                          methodValue =
                                                                              typedElements;
                                                                        }
                                                                      } else {
                                                                        Variant.of(typedElements);
                                                                        NodeId
                                                                            assignableDataTypeId =
                                                                                dataTypeTree_
                                                                                                .getBackingClass(
                                                                                                    argumentDataTypeId)
                                                                                            == Number
                                                                                                .class
                                                                                        && dataTypeTree_
                                                                                            .isSubtypeOf(
                                                                                                argumentDataTypeId,
                                                                                                NodeIds
                                                                                                    .Integer)
                                                                                    ? NodeIds
                                                                                        .Integer
                                                                                    : argumentDataTypeId;
                                                                        if (dataTypeTree_
                                                                                    .getBackingClass(
                                                                                        argumentDataTypeId)
                                                                                != Variant.class
                                                                            && !dataTypeTree_
                                                                                .isAssignable(
                                                                                    assignableDataTypeId,
                                                                                    ArrayUtil
                                                                                        .getBoxedType(
                                                                                            typedElements))) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Method argument"
                                                                                  + " DataType"
                                                                                  + " mismatch");
                                                                        }
                                                                      }
                                                                    }
                                                                    decoded1 = (String) methodValue;
                                                                  } catch (
                                                                      UaSerializationException
                                                                          conversionFailure) {
                                                                    throw new UaException(
                                                                        conversionFailure
                                                                                    .getStatusCode()
                                                                                    .getValue()
                                                                                == StatusCodes
                                                                                    .Bad_OutOfRange
                                                                            ? StatusCodes
                                                                                .Bad_OutOfRange
                                                                            : StatusCodes
                                                                                .Bad_TypeMismatch,
                                                                        conversionFailure);
                                                                  } catch (ClassCastException
                                                                      | IllegalArgumentException
                                                                          conversionFailure) {
                                                                    throw new UaException(
                                                                        StatusCodes
                                                                            .Bad_TypeMismatch,
                                                                        conversionFailure);
                                                                  }
                                                                }
                                                                return KeyCredentialConfigurationTypeGetEncryptingKeyOutputs
                                                                    .of(decoded0, decoded1);
                                                              });
                                                        } catch (UaException failure) {
                                                          throw new CompletionException(failure);
                                                        }
                                                      });
                                                } catch (Exception failure) {
                                                  return CompletableFuture.failedFuture(failure);
                                                }
                                              });
                                        });
                              } catch (Exception failure) {
                                return CompletableFuture.failedFuture(failure);
                              }
                            });
                  });
      pipeline.whenComplete(
          (value, failure) -> {
            if (failure == null) {
              result.complete(value);
            } else {
              result.completeExceptionally(failure);
            }
          });
    } catch (RuntimeException failure) {
      result.completeExceptionally(failure);
    }
    return result;
  }

  @NullMarked
  @Override
  public @Nullable UaMethodNode getUpdateCredentialMethodNode() throws UaException {
    return ClientMembers.await(getUpdateCredentialMethodNodeAsync(), false);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends @Nullable UaMethodNode> getUpdateCredentialMethodNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        UaMethodNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "UpdateCredential",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Method,
            true,
            "http://opcfoundation.org/UA/:UpdateCredential (declaration i=18006, owner i=18001)"));
  }

  @NullMarked
  @Override
  public void callUpdateCredential(
      @Nullable String credentialId,
      @Nullable ByteString credentialSecret,
      @Nullable String certificateThumbprint,
      @Nullable String securityPolicyUri)
      throws UaException {
    callUpdateCredentialDetailed(
            credentialId, credentialSecret, certificateThumbprint, securityPolicyUri)
        .requireGood();
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends @Nullable Void> callUpdateCredentialAsync(
      @Nullable String credentialId,
      @Nullable ByteString credentialSecret,
      @Nullable String certificateThumbprint,
      @Nullable String securityPolicyUri) {
    CompletableFuture<@Nullable Void> result = new CompletableFuture<>();
    var call =
        callUpdateCredentialDetailedAsync(
            credentialId, credentialSecret, certificateThumbprint, securityPolicyUri);
    result.whenComplete(
        (value, failure) -> {
          if (result.isCancelled()) {
            call.cancel(false);
          }
        });
    call.whenComplete(
        (value, failure) -> {
          if (failure != null) {
            result.completeExceptionally(failure);
          } else {
            try {
              result.complete(value.requireGood());
            } catch (UaException statusFailure) {
              result.completeExceptionally(statusFailure);
            }
          }
        });
    return result;
  }

  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable Void> callUpdateCredentialDetailed(
      @Nullable String credentialId,
      @Nullable ByteString credentialSecret,
      @Nullable String certificateThumbprint,
      @Nullable String securityPolicyUri)
      throws UaException {
    return callUpdateCredentialDetailed(
        MethodCallOptions.NONE,
        credentialId,
        credentialSecret,
        certificateThumbprint,
        securityPolicyUri);
  }

  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable Void> callUpdateCredentialDetailed(
      MethodCallOptions options,
      @Nullable String credentialId,
      @Nullable ByteString credentialSecret,
      @Nullable String certificateThumbprint,
      @Nullable String securityPolicyUri)
      throws UaException {
    Objects.requireNonNull(options, "options");
    return ClientMembers.await(
        callUpdateCredentialDetailedAsync(
            options, credentialId, credentialSecret, certificateThumbprint, securityPolicyUri),
        true);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callUpdateCredentialDetailedAsync(
          @Nullable String credentialId,
          @Nullable ByteString credentialSecret,
          @Nullable String certificateThumbprint,
          @Nullable String securityPolicyUri) {
    return callUpdateCredentialDetailedAsync(
        MethodCallOptions.NONE,
        credentialId,
        credentialSecret,
        certificateThumbprint,
        securityPolicyUri);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callUpdateCredentialDetailedAsync(
          MethodCallOptions options,
          @Nullable String credentialId,
          @Nullable ByteString credentialSecret,
          @Nullable String certificateThumbprint,
          @Nullable String securityPolicyUri) {
    CompletableFuture<MethodCallResult<@Nullable Void>> result = new CompletableFuture<>();
    try {
      Objects.requireNonNull(options, "options");
      List<@Nullable Object> rawInputs = new ArrayList<>();
      rawInputs.add(credentialId);
      rawInputs.add(credentialSecret);
      rawInputs.add(certificateThumbprint);
      rawInputs.add(securityPolicyUri);
      var lookup = getUpdateCredentialMethodNodeAsync();
      result.whenComplete(
          (value, failure) -> {
            if (result.isCancelled()) {
              lookup.cancel(false);
            }
          });
      CompletableFuture<MethodCallResult<@Nullable Void>> pipeline =
          lookup.thenCompose(
              methodNode -> {
                if (result.isCancelled()) {
                  return CompletableFuture.failedFuture(new CancellationException());
                }
                if (methodNode == null) {
                  return CompletableFuture.failedFuture(
                      new UaException(
                          StatusCodes.Bad_NotFound,
                          "Method node is required for invocation: UpdateCredential"));
                }
                var inputMetadata =
                    ClientDataTypes.read(
                        this.client,
                        List.<ExpandedNodeId>of(
                                ExpandedNodeId.parse("i=12"),
                                ExpandedNodeId.parse("i=15"),
                                ExpandedNodeId.parse("i=12"),
                                ExpandedNodeId.parse("i=12"))
                            .subList(0, rawInputs.size()),
                        rawInputs.toArray());
                result.whenComplete(
                    (cancelledValue, cancelledFailure) -> {
                      if (result.isCancelled()) {
                        inputMetadata.cancel(false);
                      }
                    });
                return inputMetadata
                    .handle(
                        (inputDataTypeTree, inputMetadataFailure) -> {
                          if (inputMetadataFailure != null) {
                            var checkedMetadataFailureinputMetadataFailure =
                                UaException.extract(inputMetadataFailure);
                            if (checkedMetadataFailureinputMetadataFailure.isPresent()) {
                              throw new CompletionException(
                                  checkedMetadataFailureinputMetadataFailure.orElseThrow());
                            }
                            Throwable metadataCauseinputMetadataFailure = inputMetadataFailure;
                            while (metadataCauseinputMetadataFailure != null) {
                              if (metadataCauseinputMetadataFailure
                                  instanceof
                                  UaSerializationException metadataCodecinputMetadataFailure) {
                                throw new CompletionException(
                                    new UaException(
                                        metadataCodecinputMetadataFailure.getStatusCode().getValue()
                                                == StatusCodes.Bad_OutOfRange
                                            ? StatusCodes.Bad_OutOfRange
                                            : StatusCodes.Bad_TypeMismatch,
                                        metadataCodecinputMetadataFailure));
                              }
                              metadataCauseinputMetadataFailure =
                                  metadataCauseinputMetadataFailure.getCause();
                            }
                            throw new CompletionException(
                                UaException.extract(inputMetadataFailure)
                                    .orElseGet(() -> new UaException(inputMetadataFailure)));
                          }
                          return inputDataTypeTree;
                        })
                    .thenCompose(
                        dataTypeTree -> {
                          if (result.isCancelled()) {
                            return CompletableFuture.failedFuture(new CancellationException());
                          }
                          try {
                            List<Variant> inputArguments = new ArrayList<>();
                            if (rawInputs.size() > 0) {
                              Variant encoded0;
                              {
                                @Nullable String convertedValue;
                                {
                                  Object methodValue = rawInputs.get(0);
                                  try {
                                    if (methodValue instanceof Matrix
                                        && ((Matrix) methodValue).isNull()) {
                                      methodValue = null;
                                    }
                                    NamespaceTable namespaceTable = this.client.getNamespaceTable();
                                    DataTypeTree dataTypeTree_ = dataTypeTree;
                                    NodeId argumentDataTypeId =
                                        ExpandedNodeId.parse("i=12")
                                            .toNodeId(namespaceTable)
                                            .orElseThrow(
                                                () ->
                                                    new UaException(
                                                        StatusCodes.Bad_NodeIdInvalid,
                                                        "Method argument DataType namespace is"
                                                            + " unavailable"));
                                    if (!OpcUaDataType.isBuiltin(argumentDataTypeId)
                                        && dataTypeTree_.getDataType(argumentDataTypeId) == null) {
                                      throw new UaException(
                                          StatusCodes.Bad_TypeMismatch,
                                          "Method argument CredentialId (effective property"
                                              + " i=18007, DataType i=12) is unavailable in the"
                                              + " effective type tree; resolved DataType: "
                                              + argumentDataTypeId);
                                    }
                                    methodValue =
                                        NumericValues.normalize(
                                            methodValue, dataTypeTree_, argumentDataTypeId);
                                    if (methodValue != null) {
                                      Object shapeElements =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getElements()
                                              : methodValue;
                                      int valueRank =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getValueRank()
                                              : ArrayUtil.getValueRank(methodValue);
                                      boolean emptyArray =
                                          methodValue.getClass().isArray()
                                              && ArrayUtil.getValueRank(methodValue) == 1
                                              && Array.getLength(methodValue) == 0;
                                      if (!(valueRank == -1)) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "Method argument ValueRank mismatch");
                                      }
                                      if (methodValue instanceof Matrix) {
                                        int[] dimensions = ((Matrix) methodValue).getDimensions();
                                        if (dimensions.length < 2
                                            || !shapeElements.getClass().isArray()
                                            || ArrayUtil.getValueRank(shapeElements) != 1) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Malformed Method Matrix representation");
                                        }
                                        long elementCount = 1;
                                        for (int dimension : dimensions) {
                                          if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "Malformed Method Matrix dimensions");
                                          }
                                          elementCount *= dimension;
                                        }
                                        if (elementCount != Array.getLength(shapeElements)) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method Matrix dimensions do not match its elements");
                                        }
                                        if (!(((Matrix) methodValue)
                                            .getDataType()
                                            .equals(Variant.of(shapeElements).getDataType()))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method Matrix DataType does not match its elements");
                                        }
                                      }
                                      Variant.of(shapeElements);
                                    }
                                    if (methodValue != null) {
                                      Object typedElements =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getElements()
                                              : methodValue;
                                      if (NodeIds.Structure.equals(argumentDataTypeId)
                                          || dataTypeTree_.isStructType(argumentDataTypeId)) {
                                        var declaredType =
                                            dataTypeTree_.getType(argumentDataTypeId);
                                        if (typedElements.getClass().isArray()) {
                                          var structureCodec =
                                              this.client
                                                  .getStaticEncodingContext()
                                                  .getDataTypeManager()
                                                  .getCodec(argumentDataTypeId);
                                          Class<?> structureClass =
                                              structureCodec == null
                                                  ? UaStructuredType.class
                                                  : structureCodec.getType();
                                          Object decodedStructures =
                                              Array.newInstance(
                                                  structureClass, Array.getLength(typedElements));
                                          for (int structureIndex = 0;
                                              structureIndex < Array.getLength(typedElements);
                                              structureIndex++) {
                                            Object structure =
                                                Array.get(typedElements, structureIndex);
                                            if (structure instanceof ExtensionObject) {
                                              structure =
                                                  ((ExtensionObject) structure).isNull()
                                                      ? null
                                                      : ((ExtensionObject) structure)
                                                          .decode(
                                                              this.client
                                                                  .getStaticEncodingContext());
                                            }
                                            if (structure != null) {
                                              if (!(structure instanceof UaStructuredType)) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method argument requires a Structure value");
                                              }
                                              if (NodeIds.Structure.equals(argumentDataTypeId)
                                                  || declaredType != null
                                                      && declaredType.isAbstract()) {
                                                if (!dataTypeTree_.isSubtypeOf(
                                                    ((UaStructuredType) structure)
                                                        .getTypeId()
                                                        .toNodeId(namespaceTable)
                                                        .orElse(NodeId.NULL_VALUE),
                                                    argumentDataTypeId)) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method Structure is not a subtype of the"
                                                          + " effective DataType");
                                                }
                                              } else {
                                                if (!argumentDataTypeId.equals(
                                                    ((UaStructuredType) structure)
                                                        .getTypeId()
                                                        .toNodeId(namespaceTable)
                                                        .orElse(NodeId.NULL_VALUE))) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method Structure does not match the"
                                                          + " effective DataType");
                                                }
                                              }
                                            }
                                            Array.set(decodedStructures, structureIndex, structure);
                                          }
                                          methodValue =
                                              methodValue instanceof Matrix
                                                  ? new Matrix(
                                                      decodedStructures,
                                                      ((Matrix) methodValue)
                                                          .getDimensions()
                                                          .clone())
                                                  : decodedStructures;
                                        } else {
                                          if (typedElements instanceof ExtensionObject) {
                                            typedElements =
                                                ((ExtensionObject) typedElements).isNull()
                                                    ? null
                                                    : ((ExtensionObject) typedElements)
                                                        .decode(
                                                            this.client.getStaticEncodingContext());
                                          }
                                          if (typedElements != null) {
                                            if (!(typedElements instanceof UaStructuredType)) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Method argument requires a Structure value");
                                            }
                                            if (NodeIds.Structure.equals(argumentDataTypeId)
                                                || declaredType != null
                                                    && declaredType.isAbstract()) {
                                              if (!dataTypeTree_.isSubtypeOf(
                                                  ((UaStructuredType) typedElements)
                                                      .getTypeId()
                                                      .toNodeId(namespaceTable)
                                                      .orElse(NodeId.NULL_VALUE),
                                                  argumentDataTypeId)) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method Structure is not a subtype of the"
                                                        + " effective DataType");
                                              }
                                            } else {
                                              if (!argumentDataTypeId.equals(
                                                  ((UaStructuredType) typedElements)
                                                      .getTypeId()
                                                      .toNodeId(namespaceTable)
                                                      .orElse(NodeId.NULL_VALUE))) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method Structure does not match the effective"
                                                        + " DataType");
                                              }
                                            }
                                          }
                                          methodValue = typedElements;
                                        }
                                      } else {
                                        Variant.of(typedElements);
                                        NodeId assignableDataTypeId =
                                            dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                        == Number.class
                                                    && dataTypeTree_.isSubtypeOf(
                                                        argumentDataTypeId, NodeIds.Integer)
                                                ? NodeIds.Integer
                                                : argumentDataTypeId;
                                        if (dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                != Variant.class
                                            && !dataTypeTree_.isAssignable(
                                                assignableDataTypeId,
                                                ArrayUtil.getBoxedType(typedElements))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method argument DataType mismatch");
                                        }
                                      }
                                    }
                                    convertedValue = (String) methodValue;
                                  } catch (UaSerializationException conversionFailure) {
                                    throw new UaException(
                                        conversionFailure.getStatusCode().getValue()
                                                == StatusCodes.Bad_OutOfRange
                                            ? StatusCodes.Bad_OutOfRange
                                            : StatusCodes.Bad_TypeMismatch,
                                        conversionFailure);
                                  } catch (ClassCastException
                                      | IllegalArgumentException conversionFailure) {
                                    throw new UaException(
                                        StatusCodes.Bad_TypeMismatch, conversionFailure);
                                  }
                                }
                                try {
                                  Object wireValue = convertedValue;
                                  Object wireElements =
                                      wireValue instanceof Matrix
                                          ? ((Matrix) wireValue).getElements()
                                          : wireValue;
                                  NumericValues.requireEncodable(wireValue);
                                  wireValue =
                                      ExtensionObject.encodeValue(
                                          this.client.getStaticEncodingContext(), wireValue);
                                  encoded0 = Variant.of(wireValue);
                                } catch (UaSerializationException encodingFailure) {
                                  throw new UaException(
                                      encodingFailure.getStatusCode().getValue()
                                              == StatusCodes.Bad_OutOfRange
                                          ? StatusCodes.Bad_OutOfRange
                                          : StatusCodes.Bad_TypeMismatch,
                                      encodingFailure);
                                } catch (ClassCastException
                                    | IllegalArgumentException encodingFailure) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch, encodingFailure);
                                }
                              }
                              inputArguments.add(encoded0);
                            }
                            if (rawInputs.size() > 1) {
                              Variant encoded1;
                              {
                                @Nullable ByteString convertedValue;
                                {
                                  Object methodValue = rawInputs.get(1);
                                  try {
                                    if (methodValue instanceof Matrix
                                        && ((Matrix) methodValue).isNull()) {
                                      methodValue = null;
                                    }
                                    NamespaceTable namespaceTable = this.client.getNamespaceTable();
                                    DataTypeTree dataTypeTree_ = dataTypeTree;
                                    NodeId argumentDataTypeId =
                                        ExpandedNodeId.parse("i=15")
                                            .toNodeId(namespaceTable)
                                            .orElseThrow(
                                                () ->
                                                    new UaException(
                                                        StatusCodes.Bad_NodeIdInvalid,
                                                        "Method argument DataType namespace is"
                                                            + " unavailable"));
                                    if (!OpcUaDataType.isBuiltin(argumentDataTypeId)
                                        && dataTypeTree_.getDataType(argumentDataTypeId) == null) {
                                      throw new UaException(
                                          StatusCodes.Bad_TypeMismatch,
                                          "Method argument CredentialSecret (effective property"
                                              + " i=18007, DataType i=15) is unavailable in the"
                                              + " effective type tree; resolved DataType: "
                                              + argumentDataTypeId);
                                    }
                                    methodValue =
                                        NumericValues.normalize(
                                            methodValue, dataTypeTree_, argumentDataTypeId);
                                    if (methodValue != null) {
                                      Object shapeElements =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getElements()
                                              : methodValue;
                                      int valueRank =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getValueRank()
                                              : ArrayUtil.getValueRank(methodValue);
                                      boolean emptyArray =
                                          methodValue.getClass().isArray()
                                              && ArrayUtil.getValueRank(methodValue) == 1
                                              && Array.getLength(methodValue) == 0;
                                      if (!(valueRank == -1)) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "Method argument ValueRank mismatch");
                                      }
                                      if (methodValue instanceof Matrix) {
                                        int[] dimensions = ((Matrix) methodValue).getDimensions();
                                        if (dimensions.length < 2
                                            || !shapeElements.getClass().isArray()
                                            || ArrayUtil.getValueRank(shapeElements) != 1) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Malformed Method Matrix representation");
                                        }
                                        long elementCount = 1;
                                        for (int dimension : dimensions) {
                                          if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "Malformed Method Matrix dimensions");
                                          }
                                          elementCount *= dimension;
                                        }
                                        if (elementCount != Array.getLength(shapeElements)) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method Matrix dimensions do not match its elements");
                                        }
                                        if (!(((Matrix) methodValue)
                                            .getDataType()
                                            .equals(Variant.of(shapeElements).getDataType()))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method Matrix DataType does not match its elements");
                                        }
                                      }
                                      Variant.of(shapeElements);
                                    }
                                    if (methodValue != null) {
                                      Object typedElements =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getElements()
                                              : methodValue;
                                      if (NodeIds.Structure.equals(argumentDataTypeId)
                                          || dataTypeTree_.isStructType(argumentDataTypeId)) {
                                        var declaredType =
                                            dataTypeTree_.getType(argumentDataTypeId);
                                        if (typedElements.getClass().isArray()) {
                                          var structureCodec =
                                              this.client
                                                  .getStaticEncodingContext()
                                                  .getDataTypeManager()
                                                  .getCodec(argumentDataTypeId);
                                          Class<?> structureClass =
                                              structureCodec == null
                                                  ? UaStructuredType.class
                                                  : structureCodec.getType();
                                          Object decodedStructures =
                                              Array.newInstance(
                                                  structureClass, Array.getLength(typedElements));
                                          for (int structureIndex = 0;
                                              structureIndex < Array.getLength(typedElements);
                                              structureIndex++) {
                                            Object structure =
                                                Array.get(typedElements, structureIndex);
                                            if (structure instanceof ExtensionObject) {
                                              structure =
                                                  ((ExtensionObject) structure).isNull()
                                                      ? null
                                                      : ((ExtensionObject) structure)
                                                          .decode(
                                                              this.client
                                                                  .getStaticEncodingContext());
                                            }
                                            if (structure != null) {
                                              if (!(structure instanceof UaStructuredType)) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method argument requires a Structure value");
                                              }
                                              if (NodeIds.Structure.equals(argumentDataTypeId)
                                                  || declaredType != null
                                                      && declaredType.isAbstract()) {
                                                if (!dataTypeTree_.isSubtypeOf(
                                                    ((UaStructuredType) structure)
                                                        .getTypeId()
                                                        .toNodeId(namespaceTable)
                                                        .orElse(NodeId.NULL_VALUE),
                                                    argumentDataTypeId)) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method Structure is not a subtype of the"
                                                          + " effective DataType");
                                                }
                                              } else {
                                                if (!argumentDataTypeId.equals(
                                                    ((UaStructuredType) structure)
                                                        .getTypeId()
                                                        .toNodeId(namespaceTable)
                                                        .orElse(NodeId.NULL_VALUE))) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method Structure does not match the"
                                                          + " effective DataType");
                                                }
                                              }
                                            }
                                            Array.set(decodedStructures, structureIndex, structure);
                                          }
                                          methodValue =
                                              methodValue instanceof Matrix
                                                  ? new Matrix(
                                                      decodedStructures,
                                                      ((Matrix) methodValue)
                                                          .getDimensions()
                                                          .clone())
                                                  : decodedStructures;
                                        } else {
                                          if (typedElements instanceof ExtensionObject) {
                                            typedElements =
                                                ((ExtensionObject) typedElements).isNull()
                                                    ? null
                                                    : ((ExtensionObject) typedElements)
                                                        .decode(
                                                            this.client.getStaticEncodingContext());
                                          }
                                          if (typedElements != null) {
                                            if (!(typedElements instanceof UaStructuredType)) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Method argument requires a Structure value");
                                            }
                                            if (NodeIds.Structure.equals(argumentDataTypeId)
                                                || declaredType != null
                                                    && declaredType.isAbstract()) {
                                              if (!dataTypeTree_.isSubtypeOf(
                                                  ((UaStructuredType) typedElements)
                                                      .getTypeId()
                                                      .toNodeId(namespaceTable)
                                                      .orElse(NodeId.NULL_VALUE),
                                                  argumentDataTypeId)) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method Structure is not a subtype of the"
                                                        + " effective DataType");
                                              }
                                            } else {
                                              if (!argumentDataTypeId.equals(
                                                  ((UaStructuredType) typedElements)
                                                      .getTypeId()
                                                      .toNodeId(namespaceTable)
                                                      .orElse(NodeId.NULL_VALUE))) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method Structure does not match the effective"
                                                        + " DataType");
                                              }
                                            }
                                          }
                                          methodValue = typedElements;
                                        }
                                      } else {
                                        Variant.of(typedElements);
                                        NodeId assignableDataTypeId =
                                            dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                        == Number.class
                                                    && dataTypeTree_.isSubtypeOf(
                                                        argumentDataTypeId, NodeIds.Integer)
                                                ? NodeIds.Integer
                                                : argumentDataTypeId;
                                        if (dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                != Variant.class
                                            && !dataTypeTree_.isAssignable(
                                                assignableDataTypeId,
                                                ArrayUtil.getBoxedType(typedElements))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method argument DataType mismatch");
                                        }
                                      }
                                    }
                                    convertedValue = (ByteString) methodValue;
                                  } catch (UaSerializationException conversionFailure) {
                                    throw new UaException(
                                        conversionFailure.getStatusCode().getValue()
                                                == StatusCodes.Bad_OutOfRange
                                            ? StatusCodes.Bad_OutOfRange
                                            : StatusCodes.Bad_TypeMismatch,
                                        conversionFailure);
                                  } catch (ClassCastException
                                      | IllegalArgumentException conversionFailure) {
                                    throw new UaException(
                                        StatusCodes.Bad_TypeMismatch, conversionFailure);
                                  }
                                }
                                try {
                                  Object wireValue = convertedValue;
                                  Object wireElements =
                                      wireValue instanceof Matrix
                                          ? ((Matrix) wireValue).getElements()
                                          : wireValue;
                                  NumericValues.requireEncodable(wireValue);
                                  wireValue =
                                      ExtensionObject.encodeValue(
                                          this.client.getStaticEncodingContext(), wireValue);
                                  encoded1 = Variant.of(wireValue);
                                } catch (UaSerializationException encodingFailure) {
                                  throw new UaException(
                                      encodingFailure.getStatusCode().getValue()
                                              == StatusCodes.Bad_OutOfRange
                                          ? StatusCodes.Bad_OutOfRange
                                          : StatusCodes.Bad_TypeMismatch,
                                      encodingFailure);
                                } catch (ClassCastException
                                    | IllegalArgumentException encodingFailure) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch, encodingFailure);
                                }
                              }
                              inputArguments.add(encoded1);
                            }
                            if (rawInputs.size() > 2) {
                              Variant encoded2;
                              {
                                @Nullable String convertedValue;
                                {
                                  Object methodValue = rawInputs.get(2);
                                  try {
                                    if (methodValue instanceof Matrix
                                        && ((Matrix) methodValue).isNull()) {
                                      methodValue = null;
                                    }
                                    NamespaceTable namespaceTable = this.client.getNamespaceTable();
                                    DataTypeTree dataTypeTree_ = dataTypeTree;
                                    NodeId argumentDataTypeId =
                                        ExpandedNodeId.parse("i=12")
                                            .toNodeId(namespaceTable)
                                            .orElseThrow(
                                                () ->
                                                    new UaException(
                                                        StatusCodes.Bad_NodeIdInvalid,
                                                        "Method argument DataType namespace is"
                                                            + " unavailable"));
                                    if (!OpcUaDataType.isBuiltin(argumentDataTypeId)
                                        && dataTypeTree_.getDataType(argumentDataTypeId) == null) {
                                      throw new UaException(
                                          StatusCodes.Bad_TypeMismatch,
                                          "Method argument CertificateThumbprint (effective"
                                              + " property i=18007, DataType i=12) is unavailable"
                                              + " in the effective type tree; resolved DataType: "
                                              + argumentDataTypeId);
                                    }
                                    methodValue =
                                        NumericValues.normalize(
                                            methodValue, dataTypeTree_, argumentDataTypeId);
                                    if (methodValue != null) {
                                      Object shapeElements =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getElements()
                                              : methodValue;
                                      int valueRank =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getValueRank()
                                              : ArrayUtil.getValueRank(methodValue);
                                      boolean emptyArray =
                                          methodValue.getClass().isArray()
                                              && ArrayUtil.getValueRank(methodValue) == 1
                                              && Array.getLength(methodValue) == 0;
                                      if (!(valueRank == -1)) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "Method argument ValueRank mismatch");
                                      }
                                      if (methodValue instanceof Matrix) {
                                        int[] dimensions = ((Matrix) methodValue).getDimensions();
                                        if (dimensions.length < 2
                                            || !shapeElements.getClass().isArray()
                                            || ArrayUtil.getValueRank(shapeElements) != 1) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Malformed Method Matrix representation");
                                        }
                                        long elementCount = 1;
                                        for (int dimension : dimensions) {
                                          if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "Malformed Method Matrix dimensions");
                                          }
                                          elementCount *= dimension;
                                        }
                                        if (elementCount != Array.getLength(shapeElements)) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method Matrix dimensions do not match its elements");
                                        }
                                        if (!(((Matrix) methodValue)
                                            .getDataType()
                                            .equals(Variant.of(shapeElements).getDataType()))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method Matrix DataType does not match its elements");
                                        }
                                      }
                                      Variant.of(shapeElements);
                                    }
                                    if (methodValue != null) {
                                      Object typedElements =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getElements()
                                              : methodValue;
                                      if (NodeIds.Structure.equals(argumentDataTypeId)
                                          || dataTypeTree_.isStructType(argumentDataTypeId)) {
                                        var declaredType =
                                            dataTypeTree_.getType(argumentDataTypeId);
                                        if (typedElements.getClass().isArray()) {
                                          var structureCodec =
                                              this.client
                                                  .getStaticEncodingContext()
                                                  .getDataTypeManager()
                                                  .getCodec(argumentDataTypeId);
                                          Class<?> structureClass =
                                              structureCodec == null
                                                  ? UaStructuredType.class
                                                  : structureCodec.getType();
                                          Object decodedStructures =
                                              Array.newInstance(
                                                  structureClass, Array.getLength(typedElements));
                                          for (int structureIndex = 0;
                                              structureIndex < Array.getLength(typedElements);
                                              structureIndex++) {
                                            Object structure =
                                                Array.get(typedElements, structureIndex);
                                            if (structure instanceof ExtensionObject) {
                                              structure =
                                                  ((ExtensionObject) structure).isNull()
                                                      ? null
                                                      : ((ExtensionObject) structure)
                                                          .decode(
                                                              this.client
                                                                  .getStaticEncodingContext());
                                            }
                                            if (structure != null) {
                                              if (!(structure instanceof UaStructuredType)) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method argument requires a Structure value");
                                              }
                                              if (NodeIds.Structure.equals(argumentDataTypeId)
                                                  || declaredType != null
                                                      && declaredType.isAbstract()) {
                                                if (!dataTypeTree_.isSubtypeOf(
                                                    ((UaStructuredType) structure)
                                                        .getTypeId()
                                                        .toNodeId(namespaceTable)
                                                        .orElse(NodeId.NULL_VALUE),
                                                    argumentDataTypeId)) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method Structure is not a subtype of the"
                                                          + " effective DataType");
                                                }
                                              } else {
                                                if (!argumentDataTypeId.equals(
                                                    ((UaStructuredType) structure)
                                                        .getTypeId()
                                                        .toNodeId(namespaceTable)
                                                        .orElse(NodeId.NULL_VALUE))) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method Structure does not match the"
                                                          + " effective DataType");
                                                }
                                              }
                                            }
                                            Array.set(decodedStructures, structureIndex, structure);
                                          }
                                          methodValue =
                                              methodValue instanceof Matrix
                                                  ? new Matrix(
                                                      decodedStructures,
                                                      ((Matrix) methodValue)
                                                          .getDimensions()
                                                          .clone())
                                                  : decodedStructures;
                                        } else {
                                          if (typedElements instanceof ExtensionObject) {
                                            typedElements =
                                                ((ExtensionObject) typedElements).isNull()
                                                    ? null
                                                    : ((ExtensionObject) typedElements)
                                                        .decode(
                                                            this.client.getStaticEncodingContext());
                                          }
                                          if (typedElements != null) {
                                            if (!(typedElements instanceof UaStructuredType)) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Method argument requires a Structure value");
                                            }
                                            if (NodeIds.Structure.equals(argumentDataTypeId)
                                                || declaredType != null
                                                    && declaredType.isAbstract()) {
                                              if (!dataTypeTree_.isSubtypeOf(
                                                  ((UaStructuredType) typedElements)
                                                      .getTypeId()
                                                      .toNodeId(namespaceTable)
                                                      .orElse(NodeId.NULL_VALUE),
                                                  argumentDataTypeId)) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method Structure is not a subtype of the"
                                                        + " effective DataType");
                                              }
                                            } else {
                                              if (!argumentDataTypeId.equals(
                                                  ((UaStructuredType) typedElements)
                                                      .getTypeId()
                                                      .toNodeId(namespaceTable)
                                                      .orElse(NodeId.NULL_VALUE))) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method Structure does not match the effective"
                                                        + " DataType");
                                              }
                                            }
                                          }
                                          methodValue = typedElements;
                                        }
                                      } else {
                                        Variant.of(typedElements);
                                        NodeId assignableDataTypeId =
                                            dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                        == Number.class
                                                    && dataTypeTree_.isSubtypeOf(
                                                        argumentDataTypeId, NodeIds.Integer)
                                                ? NodeIds.Integer
                                                : argumentDataTypeId;
                                        if (dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                != Variant.class
                                            && !dataTypeTree_.isAssignable(
                                                assignableDataTypeId,
                                                ArrayUtil.getBoxedType(typedElements))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method argument DataType mismatch");
                                        }
                                      }
                                    }
                                    convertedValue = (String) methodValue;
                                  } catch (UaSerializationException conversionFailure) {
                                    throw new UaException(
                                        conversionFailure.getStatusCode().getValue()
                                                == StatusCodes.Bad_OutOfRange
                                            ? StatusCodes.Bad_OutOfRange
                                            : StatusCodes.Bad_TypeMismatch,
                                        conversionFailure);
                                  } catch (ClassCastException
                                      | IllegalArgumentException conversionFailure) {
                                    throw new UaException(
                                        StatusCodes.Bad_TypeMismatch, conversionFailure);
                                  }
                                }
                                try {
                                  Object wireValue = convertedValue;
                                  Object wireElements =
                                      wireValue instanceof Matrix
                                          ? ((Matrix) wireValue).getElements()
                                          : wireValue;
                                  NumericValues.requireEncodable(wireValue);
                                  wireValue =
                                      ExtensionObject.encodeValue(
                                          this.client.getStaticEncodingContext(), wireValue);
                                  encoded2 = Variant.of(wireValue);
                                } catch (UaSerializationException encodingFailure) {
                                  throw new UaException(
                                      encodingFailure.getStatusCode().getValue()
                                              == StatusCodes.Bad_OutOfRange
                                          ? StatusCodes.Bad_OutOfRange
                                          : StatusCodes.Bad_TypeMismatch,
                                      encodingFailure);
                                } catch (ClassCastException
                                    | IllegalArgumentException encodingFailure) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch, encodingFailure);
                                }
                              }
                              inputArguments.add(encoded2);
                            }
                            if (rawInputs.size() > 3) {
                              Variant encoded3;
                              {
                                @Nullable String convertedValue;
                                {
                                  Object methodValue = rawInputs.get(3);
                                  try {
                                    if (methodValue instanceof Matrix
                                        && ((Matrix) methodValue).isNull()) {
                                      methodValue = null;
                                    }
                                    NamespaceTable namespaceTable = this.client.getNamespaceTable();
                                    DataTypeTree dataTypeTree_ = dataTypeTree;
                                    NodeId argumentDataTypeId =
                                        ExpandedNodeId.parse("i=12")
                                            .toNodeId(namespaceTable)
                                            .orElseThrow(
                                                () ->
                                                    new UaException(
                                                        StatusCodes.Bad_NodeIdInvalid,
                                                        "Method argument DataType namespace is"
                                                            + " unavailable"));
                                    if (!OpcUaDataType.isBuiltin(argumentDataTypeId)
                                        && dataTypeTree_.getDataType(argumentDataTypeId) == null) {
                                      throw new UaException(
                                          StatusCodes.Bad_TypeMismatch,
                                          "Method argument SecurityPolicyUri (effective property"
                                              + " i=18007, DataType i=12) is unavailable in the"
                                              + " effective type tree; resolved DataType: "
                                              + argumentDataTypeId);
                                    }
                                    methodValue =
                                        NumericValues.normalize(
                                            methodValue, dataTypeTree_, argumentDataTypeId);
                                    if (methodValue != null) {
                                      Object shapeElements =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getElements()
                                              : methodValue;
                                      int valueRank =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getValueRank()
                                              : ArrayUtil.getValueRank(methodValue);
                                      boolean emptyArray =
                                          methodValue.getClass().isArray()
                                              && ArrayUtil.getValueRank(methodValue) == 1
                                              && Array.getLength(methodValue) == 0;
                                      if (!(valueRank == -1)) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "Method argument ValueRank mismatch");
                                      }
                                      if (methodValue instanceof Matrix) {
                                        int[] dimensions = ((Matrix) methodValue).getDimensions();
                                        if (dimensions.length < 2
                                            || !shapeElements.getClass().isArray()
                                            || ArrayUtil.getValueRank(shapeElements) != 1) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Malformed Method Matrix representation");
                                        }
                                        long elementCount = 1;
                                        for (int dimension : dimensions) {
                                          if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "Malformed Method Matrix dimensions");
                                          }
                                          elementCount *= dimension;
                                        }
                                        if (elementCount != Array.getLength(shapeElements)) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method Matrix dimensions do not match its elements");
                                        }
                                        if (!(((Matrix) methodValue)
                                            .getDataType()
                                            .equals(Variant.of(shapeElements).getDataType()))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method Matrix DataType does not match its elements");
                                        }
                                      }
                                      Variant.of(shapeElements);
                                    }
                                    if (methodValue != null) {
                                      Object typedElements =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getElements()
                                              : methodValue;
                                      if (NodeIds.Structure.equals(argumentDataTypeId)
                                          || dataTypeTree_.isStructType(argumentDataTypeId)) {
                                        var declaredType =
                                            dataTypeTree_.getType(argumentDataTypeId);
                                        if (typedElements.getClass().isArray()) {
                                          var structureCodec =
                                              this.client
                                                  .getStaticEncodingContext()
                                                  .getDataTypeManager()
                                                  .getCodec(argumentDataTypeId);
                                          Class<?> structureClass =
                                              structureCodec == null
                                                  ? UaStructuredType.class
                                                  : structureCodec.getType();
                                          Object decodedStructures =
                                              Array.newInstance(
                                                  structureClass, Array.getLength(typedElements));
                                          for (int structureIndex = 0;
                                              structureIndex < Array.getLength(typedElements);
                                              structureIndex++) {
                                            Object structure =
                                                Array.get(typedElements, structureIndex);
                                            if (structure instanceof ExtensionObject) {
                                              structure =
                                                  ((ExtensionObject) structure).isNull()
                                                      ? null
                                                      : ((ExtensionObject) structure)
                                                          .decode(
                                                              this.client
                                                                  .getStaticEncodingContext());
                                            }
                                            if (structure != null) {
                                              if (!(structure instanceof UaStructuredType)) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method argument requires a Structure value");
                                              }
                                              if (NodeIds.Structure.equals(argumentDataTypeId)
                                                  || declaredType != null
                                                      && declaredType.isAbstract()) {
                                                if (!dataTypeTree_.isSubtypeOf(
                                                    ((UaStructuredType) structure)
                                                        .getTypeId()
                                                        .toNodeId(namespaceTable)
                                                        .orElse(NodeId.NULL_VALUE),
                                                    argumentDataTypeId)) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method Structure is not a subtype of the"
                                                          + " effective DataType");
                                                }
                                              } else {
                                                if (!argumentDataTypeId.equals(
                                                    ((UaStructuredType) structure)
                                                        .getTypeId()
                                                        .toNodeId(namespaceTable)
                                                        .orElse(NodeId.NULL_VALUE))) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method Structure does not match the"
                                                          + " effective DataType");
                                                }
                                              }
                                            }
                                            Array.set(decodedStructures, structureIndex, structure);
                                          }
                                          methodValue =
                                              methodValue instanceof Matrix
                                                  ? new Matrix(
                                                      decodedStructures,
                                                      ((Matrix) methodValue)
                                                          .getDimensions()
                                                          .clone())
                                                  : decodedStructures;
                                        } else {
                                          if (typedElements instanceof ExtensionObject) {
                                            typedElements =
                                                ((ExtensionObject) typedElements).isNull()
                                                    ? null
                                                    : ((ExtensionObject) typedElements)
                                                        .decode(
                                                            this.client.getStaticEncodingContext());
                                          }
                                          if (typedElements != null) {
                                            if (!(typedElements instanceof UaStructuredType)) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Method argument requires a Structure value");
                                            }
                                            if (NodeIds.Structure.equals(argumentDataTypeId)
                                                || declaredType != null
                                                    && declaredType.isAbstract()) {
                                              if (!dataTypeTree_.isSubtypeOf(
                                                  ((UaStructuredType) typedElements)
                                                      .getTypeId()
                                                      .toNodeId(namespaceTable)
                                                      .orElse(NodeId.NULL_VALUE),
                                                  argumentDataTypeId)) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method Structure is not a subtype of the"
                                                        + " effective DataType");
                                              }
                                            } else {
                                              if (!argumentDataTypeId.equals(
                                                  ((UaStructuredType) typedElements)
                                                      .getTypeId()
                                                      .toNodeId(namespaceTable)
                                                      .orElse(NodeId.NULL_VALUE))) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method Structure does not match the effective"
                                                        + " DataType");
                                              }
                                            }
                                          }
                                          methodValue = typedElements;
                                        }
                                      } else {
                                        Variant.of(typedElements);
                                        NodeId assignableDataTypeId =
                                            dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                        == Number.class
                                                    && dataTypeTree_.isSubtypeOf(
                                                        argumentDataTypeId, NodeIds.Integer)
                                                ? NodeIds.Integer
                                                : argumentDataTypeId;
                                        if (dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                != Variant.class
                                            && !dataTypeTree_.isAssignable(
                                                assignableDataTypeId,
                                                ArrayUtil.getBoxedType(typedElements))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method argument DataType mismatch");
                                        }
                                      }
                                    }
                                    convertedValue = (String) methodValue;
                                  } catch (UaSerializationException conversionFailure) {
                                    throw new UaException(
                                        conversionFailure.getStatusCode().getValue()
                                                == StatusCodes.Bad_OutOfRange
                                            ? StatusCodes.Bad_OutOfRange
                                            : StatusCodes.Bad_TypeMismatch,
                                        conversionFailure);
                                  } catch (ClassCastException
                                      | IllegalArgumentException conversionFailure) {
                                    throw new UaException(
                                        StatusCodes.Bad_TypeMismatch, conversionFailure);
                                  }
                                }
                                try {
                                  Object wireValue = convertedValue;
                                  Object wireElements =
                                      wireValue instanceof Matrix
                                          ? ((Matrix) wireValue).getElements()
                                          : wireValue;
                                  NumericValues.requireEncodable(wireValue);
                                  wireValue =
                                      ExtensionObject.encodeValue(
                                          this.client.getStaticEncodingContext(), wireValue);
                                  encoded3 = Variant.of(wireValue);
                                } catch (UaSerializationException encodingFailure) {
                                  throw new UaException(
                                      encodingFailure.getStatusCode().getValue()
                                              == StatusCodes.Bad_OutOfRange
                                          ? StatusCodes.Bad_OutOfRange
                                          : StatusCodes.Bad_TypeMismatch,
                                      encodingFailure);
                                } catch (ClassCastException
                                    | IllegalArgumentException encodingFailure) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch, encodingFailure);
                                }
                              }
                              inputArguments.add(encoded3);
                            }
                            CallMethodRequest request =
                                new CallMethodRequest(
                                    getNodeId(),
                                    methodNode.getNodeId(),
                                    inputArguments.toArray(new Variant[0]));
                            return this.client
                                .getSessionAsync()
                                .thenCompose(
                                    session -> {
                                      if (result.isCancelled()) {
                                        return CompletableFuture.failedFuture(
                                            new CancellationException());
                                      }
                                      RequestHeader originalHeader =
                                          this.client.newRequestHeader(
                                              session.getAuthenticationToken());
                                      RequestHeader requestHeader =
                                          new RequestHeader(
                                              originalHeader.getAuthenticationToken(),
                                              originalHeader.getTimestamp(),
                                              originalHeader.getRequestHandle(),
                                              options.returnDiagnostics(),
                                              originalHeader.getAuditEntryId(),
                                              originalHeader.getTimeoutHint(),
                                              originalHeader.getAdditionalHeader());
                                      var call =
                                          this.client.sendRequestAsync(
                                              new CallRequest(
                                                  requestHeader,
                                                  new CallMethodRequest[] {request}));
                                      result.whenComplete(
                                          (value, failure) -> {
                                            if (result.isCancelled()) {
                                              call.cancel(false);
                                            }
                                          });
                                      return call.thenCompose(
                                          message -> {
                                            if (result.isCancelled()) {
                                              throw new CancellationException();
                                            }
                                            try {
                                              if (!(message instanceof CallResponse response)
                                                  || response.getResponseHeader() == null
                                                  || response.getResponseHeader().getServiceResult()
                                                      == null) {
                                                throw new UaException(
                                                    StatusCodes.Bad_UnexpectedError,
                                                    "Malformed Call response header");
                                              }
                                              if (!response
                                                  .getResponseHeader()
                                                  .getServiceResult()
                                                  .isGood()) {
                                                throw new UaException(
                                                    response
                                                        .getResponseHeader()
                                                        .getServiceResult());
                                              }
                                              if (response.getResults() == null
                                                  || response.getResults().length != 1
                                                  || response.getResults()[0] == null
                                                  || response.getResults()[0].getStatusCode()
                                                      == null) {
                                                throw new UaException(
                                                    StatusCodes.Bad_UnexpectedError,
                                                    "Expected exactly one Call operation result");
                                              }
                                              CompletableFuture<DataTypeTree> outputMetadata =
                                                  response.getResults()[0].getStatusCode().isBad()
                                                      ? CompletableFuture.completedFuture(
                                                          dataTypeTree)
                                                      : ClientDataTypes.read(
                                                          this.client,
                                                          List.<ExpandedNodeId>of(),
                                                          (Object)
                                                              response.getResults()[0]
                                                                  .getOutputArguments());
                                              result.whenComplete(
                                                  (cancelledValue, cancelledFailure) -> {
                                                    if (result.isCancelled()) {
                                                      outputMetadata.cancel(false);
                                                    }
                                                  });
                                              return outputMetadata.handle(
                                                  (outputDataTypeTree, metadataFailure) -> {
                                                    if (result.isCancelled()) {
                                                      throw new CancellationException();
                                                    }
                                                    try {
                                                      return MethodCallResult.decode(
                                                          request,
                                                          requestHeader,
                                                          response,
                                                          0,
                                                          outputArguments -> {
                                                            if (metadataFailure != null) {
                                                              var
                                                                  checkedMetadataFailuremetadataFailure =
                                                                      UaException.extract(
                                                                          metadataFailure);
                                                              if (checkedMetadataFailuremetadataFailure
                                                                  .isPresent()) {
                                                                throw checkedMetadataFailuremetadataFailure
                                                                    .orElseThrow();
                                                              }
                                                              Throwable
                                                                  metadataCausemetadataFailure =
                                                                      metadataFailure;
                                                              while (metadataCausemetadataFailure
                                                                  != null) {
                                                                if (metadataCausemetadataFailure
                                                                    instanceof
                                                                    UaSerializationException
                                                                        metadataCodecmetadataFailure) {
                                                                  throw new UaException(
                                                                      metadataCodecmetadataFailure
                                                                                  .getStatusCode()
                                                                                  .getValue()
                                                                              == StatusCodes
                                                                                  .Bad_OutOfRange
                                                                          ? StatusCodes
                                                                              .Bad_OutOfRange
                                                                          : StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                      metadataCodecmetadataFailure);
                                                                }
                                                                metadataCausemetadataFailure =
                                                                    metadataCausemetadataFailure
                                                                        .getCause();
                                                              }
                                                              throw UaException.extract(
                                                                      metadataFailure)
                                                                  .orElseGet(
                                                                      () ->
                                                                          new UaException(
                                                                              metadataFailure));
                                                            }
                                                            if ((outputArguments == null
                                                                    ? 0
                                                                    : outputArguments.length)
                                                                != 0) {
                                                              throw new UaException(
                                                                  StatusCodes.Bad_TypeMismatch,
                                                                  "Unexpected Method output count");
                                                            }
                                                            return null;
                                                          });
                                                    } catch (UaException failure) {
                                                      throw new CompletionException(failure);
                                                    }
                                                  });
                                            } catch (Exception failure) {
                                              return CompletableFuture.failedFuture(failure);
                                            }
                                          });
                                    });
                          } catch (Exception failure) {
                            return CompletableFuture.failedFuture(failure);
                          }
                        });
              });
      pipeline.whenComplete(
          (value, failure) -> {
            if (failure == null) {
              result.complete(value);
            } else {
              result.completeExceptionally(failure);
            }
          });
    } catch (RuntimeException failure) {
      result.completeExceptionally(failure);
    }
    return result;
  }

  @NullMarked
  @Override
  public @Nullable UaMethodNode getDeleteCredentialMethodNode() throws UaException {
    return ClientMembers.await(getDeleteCredentialMethodNodeAsync(), false);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends @Nullable UaMethodNode> getDeleteCredentialMethodNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        UaMethodNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DeleteCredential",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Method,
            true,
            "http://opcfoundation.org/UA/:DeleteCredential (declaration i=18008, owner i=18001)"));
  }

  @NullMarked
  @Override
  public void callDeleteCredential() throws UaException {
    callDeleteCredentialDetailed().requireGood();
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends @Nullable Void> callDeleteCredentialAsync() {
    CompletableFuture<@Nullable Void> result = new CompletableFuture<>();
    var call = callDeleteCredentialDetailedAsync();
    result.whenComplete(
        (value, failure) -> {
          if (result.isCancelled()) {
            call.cancel(false);
          }
        });
    call.whenComplete(
        (value, failure) -> {
          if (failure != null) {
            result.completeExceptionally(failure);
          } else {
            try {
              result.complete(value.requireGood());
            } catch (UaException statusFailure) {
              result.completeExceptionally(statusFailure);
            }
          }
        });
    return result;
  }

  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable Void> callDeleteCredentialDetailed()
      throws UaException {
    return callDeleteCredentialDetailed(MethodCallOptions.NONE);
  }

  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable Void> callDeleteCredentialDetailed(
      MethodCallOptions options) throws UaException {
    Objects.requireNonNull(options, "options");
    return ClientMembers.await(callDeleteCredentialDetailedAsync(options), true);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callDeleteCredentialDetailedAsync() {
    return callDeleteCredentialDetailedAsync(MethodCallOptions.NONE);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callDeleteCredentialDetailedAsync(MethodCallOptions options) {
    CompletableFuture<MethodCallResult<@Nullable Void>> result = new CompletableFuture<>();
    try {
      Objects.requireNonNull(options, "options");
      List<@Nullable Object> rawInputs = new ArrayList<>();
      var lookup = getDeleteCredentialMethodNodeAsync();
      result.whenComplete(
          (value, failure) -> {
            if (result.isCancelled()) {
              lookup.cancel(false);
            }
          });
      CompletableFuture<MethodCallResult<@Nullable Void>> pipeline =
          lookup.thenCompose(
              methodNode -> {
                if (result.isCancelled()) {
                  return CompletableFuture.failedFuture(new CancellationException());
                }
                if (methodNode == null) {
                  return CompletableFuture.failedFuture(
                      new UaException(
                          StatusCodes.Bad_NotFound,
                          "Method node is required for invocation: DeleteCredential"));
                }
                var inputMetadata =
                    ClientDataTypes.read(
                        this.client,
                        List.<ExpandedNodeId>of().subList(0, rawInputs.size()),
                        rawInputs.toArray());
                result.whenComplete(
                    (cancelledValue, cancelledFailure) -> {
                      if (result.isCancelled()) {
                        inputMetadata.cancel(false);
                      }
                    });
                return inputMetadata
                    .handle(
                        (inputDataTypeTree, inputMetadataFailure) -> {
                          if (inputMetadataFailure != null) {
                            var checkedMetadataFailureinputMetadataFailure =
                                UaException.extract(inputMetadataFailure);
                            if (checkedMetadataFailureinputMetadataFailure.isPresent()) {
                              throw new CompletionException(
                                  checkedMetadataFailureinputMetadataFailure.orElseThrow());
                            }
                            Throwable metadataCauseinputMetadataFailure = inputMetadataFailure;
                            while (metadataCauseinputMetadataFailure != null) {
                              if (metadataCauseinputMetadataFailure
                                  instanceof
                                  UaSerializationException metadataCodecinputMetadataFailure) {
                                throw new CompletionException(
                                    new UaException(
                                        metadataCodecinputMetadataFailure.getStatusCode().getValue()
                                                == StatusCodes.Bad_OutOfRange
                                            ? StatusCodes.Bad_OutOfRange
                                            : StatusCodes.Bad_TypeMismatch,
                                        metadataCodecinputMetadataFailure));
                              }
                              metadataCauseinputMetadataFailure =
                                  metadataCauseinputMetadataFailure.getCause();
                            }
                            throw new CompletionException(
                                UaException.extract(inputMetadataFailure)
                                    .orElseGet(() -> new UaException(inputMetadataFailure)));
                          }
                          return inputDataTypeTree;
                        })
                    .thenCompose(
                        dataTypeTree -> {
                          if (result.isCancelled()) {
                            return CompletableFuture.failedFuture(new CancellationException());
                          }
                          try {
                            List<Variant> inputArguments = new ArrayList<>();
                            CallMethodRequest request =
                                new CallMethodRequest(
                                    getNodeId(),
                                    methodNode.getNodeId(),
                                    inputArguments.toArray(new Variant[0]));
                            return this.client
                                .getSessionAsync()
                                .thenCompose(
                                    session -> {
                                      if (result.isCancelled()) {
                                        return CompletableFuture.failedFuture(
                                            new CancellationException());
                                      }
                                      RequestHeader originalHeader =
                                          this.client.newRequestHeader(
                                              session.getAuthenticationToken());
                                      RequestHeader requestHeader =
                                          new RequestHeader(
                                              originalHeader.getAuthenticationToken(),
                                              originalHeader.getTimestamp(),
                                              originalHeader.getRequestHandle(),
                                              options.returnDiagnostics(),
                                              originalHeader.getAuditEntryId(),
                                              originalHeader.getTimeoutHint(),
                                              originalHeader.getAdditionalHeader());
                                      var call =
                                          this.client.sendRequestAsync(
                                              new CallRequest(
                                                  requestHeader,
                                                  new CallMethodRequest[] {request}));
                                      result.whenComplete(
                                          (value, failure) -> {
                                            if (result.isCancelled()) {
                                              call.cancel(false);
                                            }
                                          });
                                      return call.thenCompose(
                                          message -> {
                                            if (result.isCancelled()) {
                                              throw new CancellationException();
                                            }
                                            try {
                                              if (!(message instanceof CallResponse response)
                                                  || response.getResponseHeader() == null
                                                  || response.getResponseHeader().getServiceResult()
                                                      == null) {
                                                throw new UaException(
                                                    StatusCodes.Bad_UnexpectedError,
                                                    "Malformed Call response header");
                                              }
                                              if (!response
                                                  .getResponseHeader()
                                                  .getServiceResult()
                                                  .isGood()) {
                                                throw new UaException(
                                                    response
                                                        .getResponseHeader()
                                                        .getServiceResult());
                                              }
                                              if (response.getResults() == null
                                                  || response.getResults().length != 1
                                                  || response.getResults()[0] == null
                                                  || response.getResults()[0].getStatusCode()
                                                      == null) {
                                                throw new UaException(
                                                    StatusCodes.Bad_UnexpectedError,
                                                    "Expected exactly one Call operation result");
                                              }
                                              CompletableFuture<DataTypeTree> outputMetadata =
                                                  response.getResults()[0].getStatusCode().isBad()
                                                      ? CompletableFuture.completedFuture(
                                                          dataTypeTree)
                                                      : ClientDataTypes.read(
                                                          this.client,
                                                          List.<ExpandedNodeId>of(),
                                                          (Object)
                                                              response.getResults()[0]
                                                                  .getOutputArguments());
                                              result.whenComplete(
                                                  (cancelledValue, cancelledFailure) -> {
                                                    if (result.isCancelled()) {
                                                      outputMetadata.cancel(false);
                                                    }
                                                  });
                                              return outputMetadata.handle(
                                                  (outputDataTypeTree, metadataFailure) -> {
                                                    if (result.isCancelled()) {
                                                      throw new CancellationException();
                                                    }
                                                    try {
                                                      return MethodCallResult.decode(
                                                          request,
                                                          requestHeader,
                                                          response,
                                                          0,
                                                          outputArguments -> {
                                                            if (metadataFailure != null) {
                                                              var
                                                                  checkedMetadataFailuremetadataFailure =
                                                                      UaException.extract(
                                                                          metadataFailure);
                                                              if (checkedMetadataFailuremetadataFailure
                                                                  .isPresent()) {
                                                                throw checkedMetadataFailuremetadataFailure
                                                                    .orElseThrow();
                                                              }
                                                              Throwable
                                                                  metadataCausemetadataFailure =
                                                                      metadataFailure;
                                                              while (metadataCausemetadataFailure
                                                                  != null) {
                                                                if (metadataCausemetadataFailure
                                                                    instanceof
                                                                    UaSerializationException
                                                                        metadataCodecmetadataFailure) {
                                                                  throw new UaException(
                                                                      metadataCodecmetadataFailure
                                                                                  .getStatusCode()
                                                                                  .getValue()
                                                                              == StatusCodes
                                                                                  .Bad_OutOfRange
                                                                          ? StatusCodes
                                                                              .Bad_OutOfRange
                                                                          : StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                      metadataCodecmetadataFailure);
                                                                }
                                                                metadataCausemetadataFailure =
                                                                    metadataCausemetadataFailure
                                                                        .getCause();
                                                              }
                                                              throw UaException.extract(
                                                                      metadataFailure)
                                                                  .orElseGet(
                                                                      () ->
                                                                          new UaException(
                                                                              metadataFailure));
                                                            }
                                                            if ((outputArguments == null
                                                                    ? 0
                                                                    : outputArguments.length)
                                                                != 0) {
                                                              throw new UaException(
                                                                  StatusCodes.Bad_TypeMismatch,
                                                                  "Unexpected Method output count");
                                                            }
                                                            return null;
                                                          });
                                                    } catch (UaException failure) {
                                                      throw new CompletionException(failure);
                                                    }
                                                  });
                                            } catch (Exception failure) {
                                              return CompletableFuture.failedFuture(failure);
                                            }
                                          });
                                    });
                          } catch (Exception failure) {
                            return CompletableFuture.failedFuture(failure);
                          }
                        });
              });
      pipeline.whenComplete(
          (value, failure) -> {
            if (failure == null) {
              result.complete(value);
            } else {
              result.completeExceptionally(failure);
            }
          });
    } catch (RuntimeException failure) {
      result.completeExceptionally(failure);
    }
    return result;
  }
}
