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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.jspecify.annotations.Nullable;

public class ApplicationConfigurationFileTypeNode extends ConfigurationFileTypeNode
    implements ApplicationConfigurationFileType {
  public ApplicationConfigurationFileTypeNode(
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
  public static ClientViews createViews(ApplicationConfigurationFileTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable String @Nullable [] getAvailableNetworks() throws UaException {
    PropertyTypeNode node = getAvailableNetworksNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AvailableNetworks (declaration i=15551, owner i=15550)"
              + " on "
              + getNodeId());
    }
    return (String[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setAvailableNetworks(@Nullable String @Nullable [] value) throws UaException {
    PropertyTypeNode node = getAvailableNetworksNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AvailableNetworks (declaration i=15551, owner i=15550)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String @Nullable [] readAvailableNetworks() throws UaException {
    return ClientMembers.await(readAvailableNetworksAsync(), false);
  }

  @Override
  public void writeAvailableNetworks(@Nullable String @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeAvailableNetworksAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String @Nullable []> readAvailableNetworksAsync() {
    return getAvailableNetworksNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:AvailableNetworks (declaration i=15551, owner"
                            + " i=15550) on "
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
  public CompletableFuture<StatusCode> writeAvailableNetworksAsync(
      @Nullable String @Nullable [] availableNetworks) {
    return getAvailableNetworksNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:AvailableNetworks (declaration i=15551, owner"
                            + " i=15550) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(availableNetworks));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getAvailableNetworksNode() throws UaException {
    return ClientMembers.await(getAvailableNetworksNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getAvailableNetworksNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "AvailableNetworks",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:AvailableNetworks (declaration i=15551, owner i=15550)"));
  }

  @Override
  public @Nullable String getAvailablePorts() throws UaException {
    PropertyTypeNode node = getAvailablePortsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AvailablePorts (declaration i=15552, owner i=15550)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setAvailablePorts(@Nullable String value) throws UaException {
    PropertyTypeNode node = getAvailablePortsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AvailablePorts (declaration i=15552, owner i=15550)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String readAvailablePorts() throws UaException {
    return ClientMembers.await(readAvailablePortsAsync(), false);
  }

  @Override
  public void writeAvailablePorts(@Nullable String value) throws UaException {
    try {
      StatusCode statusCode = writeAvailablePortsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String> readAvailablePortsAsync() {
    return getAvailablePortsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:AvailablePorts (declaration i=15552, owner"
                            + " i=15550) on "
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
  public CompletableFuture<StatusCode> writeAvailablePortsAsync(@Nullable String availablePorts) {
    return getAvailablePortsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:AvailablePorts (declaration i=15552, owner"
                            + " i=15550) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(availablePorts));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getAvailablePortsNode() throws UaException {
    return ClientMembers.await(getAvailablePortsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getAvailablePortsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "AvailablePorts",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:AvailablePorts (declaration i=15552, owner i=15550)"));
  }

  @Override
  public @Nullable UShort getMaxEndpoints() throws UaException {
    PropertyTypeNode node = getMaxEndpointsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxEndpoints (declaration i=19414, owner i=15550)"
              + " on "
              + getNodeId());
    }
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxEndpoints(@Nullable UShort value) throws UaException {
    PropertyTypeNode node = getMaxEndpointsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxEndpoints (declaration i=19414, owner i=15550)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UShort readMaxEndpoints() throws UaException {
    return ClientMembers.await(readMaxEndpointsAsync(), false);
  }

  @Override
  public void writeMaxEndpoints(@Nullable UShort value) throws UaException {
    try {
      StatusCode statusCode = writeMaxEndpointsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UShort> readMaxEndpointsAsync() {
    return getMaxEndpointsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxEndpoints (declaration i=19414, owner"
                            + " i=15550) on "
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
                return (UShort) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxEndpointsAsync(@Nullable UShort maxEndpoints) {
    return getMaxEndpointsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxEndpoints (declaration i=19414, owner"
                            + " i=15550) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxEndpoints));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getMaxEndpointsNode() throws UaException {
    return ClientMembers.await(getMaxEndpointsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxEndpointsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxEndpoints",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxEndpoints (declaration i=19414, owner i=15550)"));
  }

  @Override
  public @Nullable UShort getMaxCertificateGroups() throws UaException {
    PropertyTypeNode node = getMaxCertificateGroupsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxCertificateGroups (declaration i=19415, owner i=15550)"
              + " on "
              + getNodeId());
    }
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxCertificateGroups(@Nullable UShort value) throws UaException {
    PropertyTypeNode node = getMaxCertificateGroupsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxCertificateGroups (declaration i=19415, owner i=15550)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UShort readMaxCertificateGroups() throws UaException {
    return ClientMembers.await(readMaxCertificateGroupsAsync(), false);
  }

  @Override
  public void writeMaxCertificateGroups(@Nullable UShort value) throws UaException {
    try {
      StatusCode statusCode = writeMaxCertificateGroupsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UShort> readMaxCertificateGroupsAsync() {
    return getMaxCertificateGroupsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxCertificateGroups (declaration i=19415,"
                            + " owner i=15550) on "
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
                return (UShort) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxCertificateGroupsAsync(
      @Nullable UShort maxCertificateGroups) {
    return getMaxCertificateGroupsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxCertificateGroups (declaration i=19415,"
                            + " owner i=15550) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxCertificateGroups));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getMaxCertificateGroupsNode() throws UaException {
    return ClientMembers.await(getMaxCertificateGroupsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxCertificateGroupsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxCertificateGroups",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxCertificateGroups (declaration i=19415, owner"
                + " i=15550)"));
  }

  @Override
  public @Nullable String @Nullable [] getSecurityPolicyUris() throws UaException {
    PropertyTypeNode node = getSecurityPolicyUrisNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SecurityPolicyUris (declaration i=15553, owner i=15550)"
              + " on "
              + getNodeId());
    }
    return (String[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setSecurityPolicyUris(@Nullable String @Nullable [] value) throws UaException {
    PropertyTypeNode node = getSecurityPolicyUrisNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SecurityPolicyUris (declaration i=15553, owner i=15550)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String @Nullable [] readSecurityPolicyUris() throws UaException {
    return ClientMembers.await(readSecurityPolicyUrisAsync(), false);
  }

  @Override
  public void writeSecurityPolicyUris(@Nullable String @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeSecurityPolicyUrisAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String @Nullable []> readSecurityPolicyUrisAsync() {
    return getSecurityPolicyUrisNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SecurityPolicyUris (declaration i=15553,"
                            + " owner i=15550) on "
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
  public CompletableFuture<StatusCode> writeSecurityPolicyUrisAsync(
      @Nullable String @Nullable [] securityPolicyUris) {
    return getSecurityPolicyUrisNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SecurityPolicyUris (declaration i=15553,"
                            + " owner i=15550) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(securityPolicyUris));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getSecurityPolicyUrisNode() throws UaException {
    return ClientMembers.await(getSecurityPolicyUrisNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSecurityPolicyUrisNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SecurityPolicyUris",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SecurityPolicyUris (declaration i=15553, owner"
                + " i=15550)"));
  }

  @Override
  public @Nullable UserTokenPolicy @Nullable [] getUserTokenTypes() throws UaException {
    PropertyTypeNode node = getUserTokenTypesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UserTokenTypes (declaration i=15554, owner i=15550)"
              + " on "
              + getNodeId());
    }
    return (UserTokenPolicy[])
        decodeValue(
            node.getValue().getValue().getValue(), UserTokenPolicy.class, ValueRanks.OneDimension);
  }

  @Override
  public void setUserTokenTypes(@Nullable UserTokenPolicy @Nullable [] value) throws UaException {
    PropertyTypeNode node = getUserTokenTypesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UserTokenTypes (declaration i=15554, owner i=15550)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, UserTokenPolicy.class, ValueRanks.OneDimension)));
  }

  @Override
  public @Nullable UserTokenPolicy @Nullable [] readUserTokenTypes() throws UaException {
    return ClientMembers.await(readUserTokenTypesAsync(), false);
  }

  @Override
  public void writeUserTokenTypes(@Nullable UserTokenPolicy @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeUserTokenTypesAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UserTokenPolicy @Nullable []>
      readUserTokenTypesAsync() {
    return getUserTokenTypesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:UserTokenTypes (declaration i=15554, owner"
                            + " i=15550) on "
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
                return (UserTokenPolicy[])
                    decodeValue(
                        v.getValue().getValue(), UserTokenPolicy.class, ValueRanks.OneDimension);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeUserTokenTypesAsync(
      @Nullable UserTokenPolicy @Nullable [] userTokenTypes) {
    return getUserTokenTypesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:UserTokenTypes (declaration i=15554, owner"
                            + " i=15550) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                userTokenTypes, UserTokenPolicy.class, ValueRanks.OneDimension)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getUserTokenTypesNode() throws UaException {
    return ClientMembers.await(getUserTokenTypesNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUserTokenTypesNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "UserTokenTypes",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:UserTokenTypes (declaration i=15554, owner i=15550)"));
  }

  @Override
  public @Nullable NodeId @Nullable [] getCertificateTypes() throws UaException {
    PropertyTypeNode node = getCertificateTypesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CertificateTypes (declaration i=15555, owner i=15550)"
              + " on "
              + getNodeId());
    }
    return (NodeId[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setCertificateTypes(@Nullable NodeId @Nullable [] value) throws UaException {
    PropertyTypeNode node = getCertificateTypesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CertificateTypes (declaration i=15555, owner i=15550)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable NodeId @Nullable [] readCertificateTypes() throws UaException {
    return ClientMembers.await(readCertificateTypesAsync(), false);
  }

  @Override
  public void writeCertificateTypes(@Nullable NodeId @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeCertificateTypesAsync(value).get();
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
  public CompletableFuture<? extends @Nullable NodeId @Nullable []> readCertificateTypesAsync() {
    return getCertificateTypesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CertificateTypes (declaration i=15555, owner"
                            + " i=15550) on "
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
  public CompletableFuture<StatusCode> writeCertificateTypesAsync(
      @Nullable NodeId @Nullable [] certificateTypes) {
    return getCertificateTypesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CertificateTypes (declaration i=15555, owner"
                            + " i=15550) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(certificateTypes));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getCertificateTypesNode() throws UaException {
    return ClientMembers.await(getCertificateTypesNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getCertificateTypesNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CertificateTypes",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:CertificateTypes (declaration i=15555, owner i=15550)"));
  }

  @Override
  public @Nullable NodeId @Nullable [] getCertificateGroupPurposes() throws UaException {
    PropertyTypeNode node = getCertificateGroupPurposesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CertificateGroupPurposes (declaration i=19416, owner"
              + " i=15550) on "
              + getNodeId());
    }
    return (NodeId[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setCertificateGroupPurposes(@Nullable NodeId @Nullable [] value) throws UaException {
    PropertyTypeNode node = getCertificateGroupPurposesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CertificateGroupPurposes (declaration i=19416, owner"
              + " i=15550) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable NodeId @Nullable [] readCertificateGroupPurposes() throws UaException {
    return ClientMembers.await(readCertificateGroupPurposesAsync(), false);
  }

  @Override
  public void writeCertificateGroupPurposes(@Nullable NodeId @Nullable [] value)
      throws UaException {
    try {
      StatusCode statusCode = writeCertificateGroupPurposesAsync(value).get();
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
  public CompletableFuture<? extends @Nullable NodeId @Nullable []>
      readCertificateGroupPurposesAsync() {
    return getCertificateGroupPurposesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CertificateGroupPurposes (declaration"
                            + " i=19416, owner i=15550) on "
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
  public CompletableFuture<StatusCode> writeCertificateGroupPurposesAsync(
      @Nullable NodeId @Nullable [] certificateGroupPurposes) {
    return getCertificateGroupPurposesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CertificateGroupPurposes (declaration"
                            + " i=19416, owner i=15550) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(certificateGroupPurposes));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getCertificateGroupPurposesNode() throws UaException {
    return ClientMembers.await(getCertificateGroupPurposesNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getCertificateGroupPurposesNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CertificateGroupPurposes",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:CertificateGroupPurposes (declaration i=19416, owner"
                + " i=15550)"));
  }
}
