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
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

public class ApplicationConfigurationTypeNode extends ServerConfigurationTypeNode
    implements ApplicationConfigurationType {
  public ApplicationConfigurationTypeNode(
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
  public static ClientViews createViews(ApplicationConfigurationTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable String getApplicationUri() throws UaException {
    PropertyTypeNode node = getApplicationUriNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ApplicationUri (declaration i=26850, owner i=25731)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setApplicationUri(@Nullable String value) throws UaException {
    PropertyTypeNode node = getApplicationUriNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ApplicationUri (declaration i=26850, owner i=25731)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String readApplicationUri() throws UaException {
    return ClientMembers.await(readApplicationUriAsync(), false);
  }

  @Override
  public void writeApplicationUri(@Nullable String value) throws UaException {
    try {
      StatusCode statusCode = writeApplicationUriAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String> readApplicationUriAsync() {
    return getApplicationUriNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ApplicationUri (declaration i=26850, owner"
                            + " i=25731) on "
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
  public CompletableFuture<StatusCode> writeApplicationUriAsync(@Nullable String applicationUri) {
    return getApplicationUriNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ApplicationUri (declaration i=26850, owner"
                            + " i=25731) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(applicationUri));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getApplicationUriNode() throws UaException {
    return ClientMembers.await(getApplicationUriNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getApplicationUriNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ApplicationUri",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ApplicationUri (declaration i=26850, owner i=25731)"));
  }

  @Override
  public @Nullable String getProductUri() throws UaException {
    PropertyTypeNode node = getProductUriNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ProductUri (declaration i=26851, owner i=25731)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setProductUri(@Nullable String value) throws UaException {
    PropertyTypeNode node = getProductUriNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ProductUri (declaration i=26851, owner i=25731)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String readProductUri() throws UaException {
    return ClientMembers.await(readProductUriAsync(), false);
  }

  @Override
  public void writeProductUri(@Nullable String value) throws UaException {
    try {
      StatusCode statusCode = writeProductUriAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String> readProductUriAsync() {
    return getProductUriNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ProductUri (declaration i=26851, owner"
                            + " i=25731) on "
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
  public CompletableFuture<StatusCode> writeProductUriAsync(@Nullable String productUri) {
    return getProductUriNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ProductUri (declaration i=26851, owner"
                            + " i=25731) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(productUri));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getProductUriNode() throws UaException {
    return ClientMembers.await(getProductUriNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getProductUriNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ProductUri",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ProductUri (declaration i=26851, owner i=25731)"));
  }

  @Override
  public @Nullable ApplicationType getApplicationType() throws UaException {
    PropertyTypeNode node = getApplicationTypeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ApplicationType (declaration i=26852, owner i=25731)"
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
              "ApplicationType: ValueRank=-1 does not permit rank " + rank);
        }
        if (value != null && !((Object) value instanceof ApplicationType)) {
          if (!(value instanceof Integer)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "ApplicationType: expected"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType or"
                    + " Int32, got "
                    + value);
          }
          if (ApplicationType.from((Integer) value) == null) {
            throw new UaException(
                StatusCodes.Bad_OutOfRange,
                "ApplicationType: unknown"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType value "
                    + value);
          }
        }
        convertedValue =
            value == null || value instanceof ApplicationType
                ? (ApplicationType) value
                : ApplicationType.from((Integer) value);
      }
    }
    return (ApplicationType) convertedValue;
  }

  @Override
  public void setApplicationType(@Nullable ApplicationType value) throws UaException {
    PropertyTypeNode node = getApplicationTypeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ApplicationType (declaration i=26852, owner i=25731)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable ApplicationType readApplicationType() throws UaException {
    return ClientMembers.await(readApplicationTypeAsync(), false);
  }

  @Override
  public void writeApplicationType(@Nullable ApplicationType value) throws UaException {
    try {
      StatusCode statusCode = writeApplicationTypeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ApplicationType> readApplicationTypeAsync() {
    return getApplicationTypeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ApplicationType (declaration i=26852, owner"
                            + " i=25731) on "
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
                          "ApplicationType: ValueRank=-1 does not permit rank " + rank);
                    }
                    if (value != null && !((Object) value instanceof ApplicationType)) {
                      if (!(value instanceof Integer)) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_TypeMismatch,
                            "ApplicationType: expected"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType"
                                + " or Int32, got "
                                + value);
                      }
                      if (ApplicationType.from((Integer) value) == null) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_OutOfRange,
                            "ApplicationType: unknown"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType"
                                + " value "
                                + value);
                      }
                    }
                    convertedValue =
                        value == null || value instanceof ApplicationType
                            ? (ApplicationType) value
                            : ApplicationType.from((Integer) value);
                  }
                }
                return (ApplicationType) convertedValue;
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeApplicationTypeAsync(
      @Nullable ApplicationType applicationType) {
    return getApplicationTypeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ApplicationType (declaration i=26852, owner"
                            + " i=25731) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(applicationType));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getApplicationTypeNode() throws UaException {
    return ClientMembers.await(getApplicationTypeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getApplicationTypeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ApplicationType",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ApplicationType (declaration i=26852, owner i=25731)"));
  }

  @Override
  public @Nullable Boolean getEnabled() throws UaException {
    PropertyTypeNode node = getEnabledNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Enabled (declaration i=26849, owner i=25731)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setEnabled(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getEnabledNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Enabled (declaration i=26849, owner i=25731)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readEnabled() throws UaException {
    return ClientMembers.await(readEnabledAsync(), false);
  }

  @Override
  public void writeEnabled(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeEnabledAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readEnabledAsync() {
    return getEnabledNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Enabled (declaration i=26849, owner i=25731)"
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
                return (Boolean) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeEnabledAsync(@Nullable Boolean enabled) {
    return getEnabledNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Enabled (declaration i=26849, owner i=25731)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(enabled));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getEnabledNode() throws UaException {
    return ClientMembers.await(getEnabledNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getEnabledNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Enabled",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:Enabled (declaration i=26849, owner i=25731)"));
  }

  @Override
  public @Nullable Boolean getIsNonUaApplication() throws UaException {
    PropertyTypeNode node = getIsNonUaApplicationNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:IsNonUaApplication (declaration i=23741, owner i=25731)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setIsNonUaApplication(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getIsNonUaApplicationNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:IsNonUaApplication (declaration i=23741, owner i=25731)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readIsNonUaApplication() throws UaException {
    return ClientMembers.await(readIsNonUaApplicationAsync(), false);
  }

  @Override
  public void writeIsNonUaApplication(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeIsNonUaApplicationAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readIsNonUaApplicationAsync() {
    return getIsNonUaApplicationNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:IsNonUaApplication (declaration i=23741,"
                            + " owner i=25731) on "
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
                return (Boolean) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeIsNonUaApplicationAsync(
      @Nullable Boolean isNonUaApplication) {
    return getIsNonUaApplicationNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:IsNonUaApplication (declaration i=23741,"
                            + " owner i=25731) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(isNonUaApplication));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getIsNonUaApplicationNode() throws UaException {
    return ClientMembers.await(getIsNonUaApplicationNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getIsNonUaApplicationNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "IsNonUaApplication",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:IsNonUaApplication (declaration i=23741, owner"
                + " i=25731)"));
  }

  @Override
  public @Nullable KeyCredentialConfigurationFolderTypeNode getKeyCredentialsNode()
      throws UaException {
    return ClientMembers.await(getKeyCredentialsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable KeyCredentialConfigurationFolderTypeNode>
      getKeyCredentialsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        KeyCredentialConfigurationFolderTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "KeyCredentials",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            true,
            "http://opcfoundation.org/UA/:KeyCredentials (declaration i=19423, owner i=25731)"));
  }

  @Override
  public @Nullable AuthorizationServicesConfigurationFolderTypeNode getAuthorizationServicesNode()
      throws UaException {
    return ClientMembers.await(getAuthorizationServicesNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable AuthorizationServicesConfigurationFolderTypeNode>
      getAuthorizationServicesNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        AuthorizationServicesConfigurationFolderTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "AuthorizationServices",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            true,
            "http://opcfoundation.org/UA/:AuthorizationServices (declaration i=19427, owner"
                + " i=25731)"));
  }
}
