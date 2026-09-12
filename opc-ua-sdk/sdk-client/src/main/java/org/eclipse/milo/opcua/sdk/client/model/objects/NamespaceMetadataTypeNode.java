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
import java.lang.reflect.Array;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.IdType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

public class NamespaceMetadataTypeNode extends BaseObjectTypeNode implements NamespaceMetadataType {
  public NamespaceMetadataTypeNode(
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
  public static ClientViews createViews(NamespaceMetadataTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable String getNamespaceUri() throws UaException {
    PropertyTypeNode node = getNamespaceUriNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NamespaceUri (declaration i=11617, owner i=11616)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setNamespaceUri(@Nullable String value) throws UaException {
    PropertyTypeNode node = getNamespaceUriNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NamespaceUri (declaration i=11617, owner i=11616)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String readNamespaceUri() throws UaException {
    return ClientMembers.await(readNamespaceUriAsync(), false);
  }

  @Override
  public void writeNamespaceUri(@Nullable String value) throws UaException {
    try {
      StatusCode statusCode = writeNamespaceUriAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String> readNamespaceUriAsync() {
    return getNamespaceUriNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:NamespaceUri (declaration i=11617, owner"
                            + " i=11616) on "
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
  public CompletableFuture<StatusCode> writeNamespaceUriAsync(@Nullable String namespaceUri) {
    return getNamespaceUriNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:NamespaceUri (declaration i=11617, owner"
                            + " i=11616) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(namespaceUri));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getNamespaceUriNode() throws UaException {
    return ClientMembers.await(getNamespaceUriNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getNamespaceUriNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "NamespaceUri",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:NamespaceUri (declaration i=11617, owner i=11616)"));
  }

  @Override
  public @Nullable String getNamespaceVersion() throws UaException {
    PropertyTypeNode node = getNamespaceVersionNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NamespaceVersion (declaration i=11618, owner i=11616)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setNamespaceVersion(@Nullable String value) throws UaException {
    PropertyTypeNode node = getNamespaceVersionNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NamespaceVersion (declaration i=11618, owner i=11616)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String readNamespaceVersion() throws UaException {
    return ClientMembers.await(readNamespaceVersionAsync(), false);
  }

  @Override
  public void writeNamespaceVersion(@Nullable String value) throws UaException {
    try {
      StatusCode statusCode = writeNamespaceVersionAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String> readNamespaceVersionAsync() {
    return getNamespaceVersionNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:NamespaceVersion (declaration i=11618, owner"
                            + " i=11616) on "
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
  public CompletableFuture<StatusCode> writeNamespaceVersionAsync(
      @Nullable String namespaceVersion) {
    return getNamespaceVersionNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:NamespaceVersion (declaration i=11618, owner"
                            + " i=11616) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(namespaceVersion));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getNamespaceVersionNode() throws UaException {
    return ClientMembers.await(getNamespaceVersionNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getNamespaceVersionNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "NamespaceVersion",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:NamespaceVersion (declaration i=11618, owner i=11616)"));
  }

  @Override
  public @Nullable DateTime getNamespacePublicationDate() throws UaException {
    PropertyTypeNode node = getNamespacePublicationDateNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NamespacePublicationDate (declaration i=11619, owner"
              + " i=11616) on "
              + getNodeId());
    }
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setNamespacePublicationDate(@Nullable DateTime value) throws UaException {
    PropertyTypeNode node = getNamespacePublicationDateNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NamespacePublicationDate (declaration i=11619, owner"
              + " i=11616) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable DateTime readNamespacePublicationDate() throws UaException {
    return ClientMembers.await(readNamespacePublicationDateAsync(), false);
  }

  @Override
  public void writeNamespacePublicationDate(@Nullable DateTime value) throws UaException {
    try {
      StatusCode statusCode = writeNamespacePublicationDateAsync(value).get();
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
  public CompletableFuture<? extends @Nullable DateTime> readNamespacePublicationDateAsync() {
    return getNamespacePublicationDateNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:NamespacePublicationDate (declaration"
                            + " i=11619, owner i=11616) on "
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
  public CompletableFuture<StatusCode> writeNamespacePublicationDateAsync(
      @Nullable DateTime namespacePublicationDate) {
    return getNamespacePublicationDateNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:NamespacePublicationDate (declaration"
                            + " i=11619, owner i=11616) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(namespacePublicationDate));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getNamespacePublicationDateNode() throws UaException {
    return ClientMembers.await(getNamespacePublicationDateNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getNamespacePublicationDateNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "NamespacePublicationDate",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:NamespacePublicationDate (declaration i=11619, owner"
                + " i=11616)"));
  }

  @Override
  public @Nullable Boolean getIsNamespaceSubset() throws UaException {
    PropertyTypeNode node = getIsNamespaceSubsetNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:IsNamespaceSubset (declaration i=11620, owner i=11616)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setIsNamespaceSubset(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getIsNamespaceSubsetNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:IsNamespaceSubset (declaration i=11620, owner i=11616)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readIsNamespaceSubset() throws UaException {
    return ClientMembers.await(readIsNamespaceSubsetAsync(), false);
  }

  @Override
  public void writeIsNamespaceSubset(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeIsNamespaceSubsetAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readIsNamespaceSubsetAsync() {
    return getIsNamespaceSubsetNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:IsNamespaceSubset (declaration i=11620, owner"
                            + " i=11616) on "
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
  public CompletableFuture<StatusCode> writeIsNamespaceSubsetAsync(
      @Nullable Boolean isNamespaceSubset) {
    return getIsNamespaceSubsetNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:IsNamespaceSubset (declaration i=11620, owner"
                            + " i=11616) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(isNamespaceSubset));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getIsNamespaceSubsetNode() throws UaException {
    return ClientMembers.await(getIsNamespaceSubsetNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getIsNamespaceSubsetNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "IsNamespaceSubset",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:IsNamespaceSubset (declaration i=11620, owner i=11616)"));
  }

  @Override
  public @Nullable IdType @Nullable [] getStaticNodeIdTypes() throws UaException {
    PropertyTypeNode node = getStaticNodeIdTypesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StaticNodeIdTypes (declaration i=11621, owner i=11616)"
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
        boolean permitted = rank == 1 || (rank == 1 && Array.getLength(elements) == 0);
        if (!permitted) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "StaticNodeIdTypes: ValueRank=1 does not permit rank " + rank);
        }
        IdType[] converted = new IdType[Array.getLength(elements)];
        if (elements != null && elements.getClass().isArray()) {
          for (int i = 0; i < Array.getLength(elements); i++) {
            Object element = Array.get(elements, i);
            if (element != null && !((Object) element instanceof IdType)) {
              if (!(element instanceof Integer)) {
                throw new UaException(
                    StatusCodes.Bad_TypeMismatch,
                    "StaticNodeIdTypes: expected"
                        + " org.eclipse.milo.opcua.stack.core.types.enumerated.IdType or Int32, got"
                        + " "
                        + element);
              }
              if (IdType.from((Integer) element) == null) {
                throw new UaException(
                    StatusCodes.Bad_OutOfRange,
                    "StaticNodeIdTypes: unknown"
                        + " org.eclipse.milo.opcua.stack.core.types.enumerated.IdType value "
                        + element);
              }
            }
            converted[i] =
                element == null || element instanceof IdType
                    ? (IdType) element
                    : IdType.from((Integer) element);
          }
        } else {
          if (elements != null && !((Object) elements instanceof IdType)) {
            if (!(elements instanceof Integer)) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "StaticNodeIdTypes: expected"
                      + " org.eclipse.milo.opcua.stack.core.types.enumerated.IdType or Int32, got "
                      + elements);
            }
            if (IdType.from((Integer) elements) == null) {
              throw new UaException(
                  StatusCodes.Bad_OutOfRange,
                  "StaticNodeIdTypes: unknown"
                      + " org.eclipse.milo.opcua.stack.core.types.enumerated.IdType value "
                      + elements);
            }
          }
        }
        convertedValue = converted;
      }
    }
    return (IdType[]) convertedValue;
  }

  @Override
  public void setStaticNodeIdTypes(@Nullable IdType @Nullable [] value) throws UaException {
    PropertyTypeNode node = getStaticNodeIdTypesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StaticNodeIdTypes (declaration i=11621, owner i=11616)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable IdType @Nullable [] readStaticNodeIdTypes() throws UaException {
    return ClientMembers.await(readStaticNodeIdTypesAsync(), false);
  }

  @Override
  public void writeStaticNodeIdTypes(@Nullable IdType @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeStaticNodeIdTypesAsync(value).get();
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
  public CompletableFuture<? extends @Nullable IdType @Nullable []> readStaticNodeIdTypesAsync() {
    return getStaticNodeIdTypesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:StaticNodeIdTypes (declaration i=11621, owner"
                            + " i=11616) on "
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
                    boolean permitted = rank == 1 || (rank == 1 && Array.getLength(elements) == 0);
                    if (!permitted) {
                      throw new UaRuntimeException(
                          StatusCodes.Bad_TypeMismatch,
                          "StaticNodeIdTypes: ValueRank=1 does not permit rank " + rank);
                    }
                    IdType[] converted = new IdType[Array.getLength(elements)];
                    if (elements != null && elements.getClass().isArray()) {
                      for (int i = 0; i < Array.getLength(elements); i++) {
                        Object element = Array.get(elements, i);
                        if (element != null && !((Object) element instanceof IdType)) {
                          if (!(element instanceof Integer)) {
                            throw new UaRuntimeException(
                                StatusCodes.Bad_TypeMismatch,
                                "StaticNodeIdTypes: expected"
                                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.IdType"
                                    + " or Int32, got "
                                    + element);
                          }
                          if (IdType.from((Integer) element) == null) {
                            throw new UaRuntimeException(
                                StatusCodes.Bad_OutOfRange,
                                "StaticNodeIdTypes: unknown"
                                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.IdType"
                                    + " value "
                                    + element);
                          }
                        }
                        converted[i] =
                            element == null || element instanceof IdType
                                ? (IdType) element
                                : IdType.from((Integer) element);
                      }
                    } else {
                      if (elements != null && !((Object) elements instanceof IdType)) {
                        if (!(elements instanceof Integer)) {
                          throw new UaRuntimeException(
                              StatusCodes.Bad_TypeMismatch,
                              "StaticNodeIdTypes: expected"
                                  + " org.eclipse.milo.opcua.stack.core.types.enumerated.IdType or"
                                  + " Int32, got "
                                  + elements);
                        }
                        if (IdType.from((Integer) elements) == null) {
                          throw new UaRuntimeException(
                              StatusCodes.Bad_OutOfRange,
                              "StaticNodeIdTypes: unknown"
                                  + " org.eclipse.milo.opcua.stack.core.types.enumerated.IdType"
                                  + " value "
                                  + elements);
                        }
                      }
                    }
                    convertedValue = converted;
                  }
                }
                return (IdType[]) convertedValue;
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeStaticNodeIdTypesAsync(
      @Nullable IdType @Nullable [] staticNodeIdTypes) {
    return getStaticNodeIdTypesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:StaticNodeIdTypes (declaration i=11621, owner"
                            + " i=11616) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(staticNodeIdTypes));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getStaticNodeIdTypesNode() throws UaException {
    return ClientMembers.await(getStaticNodeIdTypesNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getStaticNodeIdTypesNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "StaticNodeIdTypes",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:StaticNodeIdTypes (declaration i=11621, owner i=11616)"));
  }

  @Override
  public @Nullable String @Nullable [] getStaticNumericNodeIdRange() throws UaException {
    PropertyTypeNode node = getStaticNumericNodeIdRangeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StaticNumericNodeIdRange (declaration i=11622, owner"
              + " i=11616) on "
              + getNodeId());
    }
    return (String[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setStaticNumericNodeIdRange(@Nullable String @Nullable [] value) throws UaException {
    PropertyTypeNode node = getStaticNumericNodeIdRangeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StaticNumericNodeIdRange (declaration i=11622, owner"
              + " i=11616) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String @Nullable [] readStaticNumericNodeIdRange() throws UaException {
    return ClientMembers.await(readStaticNumericNodeIdRangeAsync(), false);
  }

  @Override
  public void writeStaticNumericNodeIdRange(@Nullable String @Nullable [] value)
      throws UaException {
    try {
      StatusCode statusCode = writeStaticNumericNodeIdRangeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String @Nullable []>
      readStaticNumericNodeIdRangeAsync() {
    return getStaticNumericNodeIdRangeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:StaticNumericNodeIdRange (declaration"
                            + " i=11622, owner i=11616) on "
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
  public CompletableFuture<StatusCode> writeStaticNumericNodeIdRangeAsync(
      @Nullable String @Nullable [] staticNumericNodeIdRange) {
    return getStaticNumericNodeIdRangeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:StaticNumericNodeIdRange (declaration"
                            + " i=11622, owner i=11616) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(staticNumericNodeIdRange));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getStaticNumericNodeIdRangeNode() throws UaException {
    return ClientMembers.await(getStaticNumericNodeIdRangeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getStaticNumericNodeIdRangeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "StaticNumericNodeIdRange",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:StaticNumericNodeIdRange (declaration i=11622, owner"
                + " i=11616)"));
  }

  @Override
  public @Nullable String getStaticStringNodeIdPattern() throws UaException {
    PropertyTypeNode node = getStaticStringNodeIdPatternNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StaticStringNodeIdPattern (declaration i=11623, owner"
              + " i=11616) on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setStaticStringNodeIdPattern(@Nullable String value) throws UaException {
    PropertyTypeNode node = getStaticStringNodeIdPatternNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StaticStringNodeIdPattern (declaration i=11623, owner"
              + " i=11616) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String readStaticStringNodeIdPattern() throws UaException {
    return ClientMembers.await(readStaticStringNodeIdPatternAsync(), false);
  }

  @Override
  public void writeStaticStringNodeIdPattern(@Nullable String value) throws UaException {
    try {
      StatusCode statusCode = writeStaticStringNodeIdPatternAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String> readStaticStringNodeIdPatternAsync() {
    return getStaticStringNodeIdPatternNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:StaticStringNodeIdPattern (declaration"
                            + " i=11623, owner i=11616) on "
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
  public CompletableFuture<StatusCode> writeStaticStringNodeIdPatternAsync(
      @Nullable String staticStringNodeIdPattern) {
    return getStaticStringNodeIdPatternNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:StaticStringNodeIdPattern (declaration"
                            + " i=11623, owner i=11616) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(staticStringNodeIdPattern));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getStaticStringNodeIdPatternNode() throws UaException {
    return ClientMembers.await(getStaticStringNodeIdPatternNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getStaticStringNodeIdPatternNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "StaticStringNodeIdPattern",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:StaticStringNodeIdPattern (declaration i=11623, owner"
                + " i=11616)"));
  }

  @Override
  public @Nullable RolePermissionType @Nullable [] getDefaultRolePermissions() throws UaException {
    PropertyTypeNode node = getDefaultRolePermissionsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DefaultRolePermissions (declaration i=16137, owner i=11616)"
              + " on "
              + getNodeId());
    }
    return (RolePermissionType[])
        decodeValue(
            node.getValue().getValue().getValue(),
            RolePermissionType.class,
            ValueRanks.OneDimension);
  }

  @Override
  public void setDefaultRolePermissions(@Nullable RolePermissionType @Nullable [] value)
      throws UaException {
    PropertyTypeNode node = getDefaultRolePermissionsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DefaultRolePermissions (declaration i=16137, owner i=11616)"
              + " on "
              + getNodeId());
    }
    node.setValue(
        new Variant(encodeValue(value, RolePermissionType.class, ValueRanks.OneDimension)));
  }

  @Override
  public @Nullable RolePermissionType @Nullable [] readDefaultRolePermissions() throws UaException {
    return ClientMembers.await(readDefaultRolePermissionsAsync(), false);
  }

  @Override
  public void writeDefaultRolePermissions(@Nullable RolePermissionType @Nullable [] value)
      throws UaException {
    try {
      StatusCode statusCode = writeDefaultRolePermissionsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable RolePermissionType @Nullable []>
      readDefaultRolePermissionsAsync() {
    return getDefaultRolePermissionsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DefaultRolePermissions (declaration i=16137,"
                            + " owner i=11616) on "
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
                return (RolePermissionType[])
                    decodeValue(
                        v.getValue().getValue(), RolePermissionType.class, ValueRanks.OneDimension);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeDefaultRolePermissionsAsync(
      @Nullable RolePermissionType @Nullable [] defaultRolePermissions) {
    return getDefaultRolePermissionsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DefaultRolePermissions (declaration i=16137,"
                            + " owner i=11616) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                defaultRolePermissions,
                                RolePermissionType.class,
                                ValueRanks.OneDimension)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getDefaultRolePermissionsNode() throws UaException {
    return ClientMembers.await(getDefaultRolePermissionsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getDefaultRolePermissionsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DefaultRolePermissions",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:DefaultRolePermissions (declaration i=16137, owner"
                + " i=11616)"));
  }

  @Override
  public @Nullable RolePermissionType @Nullable [] getDefaultUserRolePermissions()
      throws UaException {
    PropertyTypeNode node = getDefaultUserRolePermissionsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DefaultUserRolePermissions (declaration i=16138, owner"
              + " i=11616) on "
              + getNodeId());
    }
    return (RolePermissionType[])
        decodeValue(
            node.getValue().getValue().getValue(),
            RolePermissionType.class,
            ValueRanks.OneDimension);
  }

  @Override
  public void setDefaultUserRolePermissions(@Nullable RolePermissionType @Nullable [] value)
      throws UaException {
    PropertyTypeNode node = getDefaultUserRolePermissionsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DefaultUserRolePermissions (declaration i=16138, owner"
              + " i=11616) on "
              + getNodeId());
    }
    node.setValue(
        new Variant(encodeValue(value, RolePermissionType.class, ValueRanks.OneDimension)));
  }

  @Override
  public @Nullable RolePermissionType @Nullable [] readDefaultUserRolePermissions()
      throws UaException {
    return ClientMembers.await(readDefaultUserRolePermissionsAsync(), false);
  }

  @Override
  public void writeDefaultUserRolePermissions(@Nullable RolePermissionType @Nullable [] value)
      throws UaException {
    try {
      StatusCode statusCode = writeDefaultUserRolePermissionsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable RolePermissionType @Nullable []>
      readDefaultUserRolePermissionsAsync() {
    return getDefaultUserRolePermissionsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DefaultUserRolePermissions (declaration"
                            + " i=16138, owner i=11616) on "
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
                return (RolePermissionType[])
                    decodeValue(
                        v.getValue().getValue(), RolePermissionType.class, ValueRanks.OneDimension);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeDefaultUserRolePermissionsAsync(
      @Nullable RolePermissionType @Nullable [] defaultUserRolePermissions) {
    return getDefaultUserRolePermissionsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DefaultUserRolePermissions (declaration"
                            + " i=16138, owner i=11616) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                defaultUserRolePermissions,
                                RolePermissionType.class,
                                ValueRanks.OneDimension)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getDefaultUserRolePermissionsNode() throws UaException {
    return ClientMembers.await(getDefaultUserRolePermissionsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getDefaultUserRolePermissionsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DefaultUserRolePermissions",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:DefaultUserRolePermissions (declaration i=16138, owner"
                + " i=11616)"));
  }

  @Override
  public @Nullable AccessRestrictionType getDefaultAccessRestrictions() throws UaException {
    PropertyTypeNode node = getDefaultAccessRestrictionsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DefaultAccessRestrictions (declaration i=16139, owner"
              + " i=11616) on "
              + getNodeId());
    }
    return (AccessRestrictionType) node.getValue().getValue().getValue();
  }

  @Override
  public void setDefaultAccessRestrictions(@Nullable AccessRestrictionType value)
      throws UaException {
    PropertyTypeNode node = getDefaultAccessRestrictionsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DefaultAccessRestrictions (declaration i=16139, owner"
              + " i=11616) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable AccessRestrictionType readDefaultAccessRestrictions() throws UaException {
    return ClientMembers.await(readDefaultAccessRestrictionsAsync(), false);
  }

  @Override
  public void writeDefaultAccessRestrictions(@Nullable AccessRestrictionType value)
      throws UaException {
    try {
      StatusCode statusCode = writeDefaultAccessRestrictionsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable AccessRestrictionType>
      readDefaultAccessRestrictionsAsync() {
    return getDefaultAccessRestrictionsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DefaultAccessRestrictions (declaration"
                            + " i=16139, owner i=11616) on "
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
                return (AccessRestrictionType) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeDefaultAccessRestrictionsAsync(
      @Nullable AccessRestrictionType defaultAccessRestrictions) {
    return getDefaultAccessRestrictionsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DefaultAccessRestrictions (declaration"
                            + " i=16139, owner i=11616) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(defaultAccessRestrictions));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getDefaultAccessRestrictionsNode() throws UaException {
    return ClientMembers.await(getDefaultAccessRestrictionsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getDefaultAccessRestrictionsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DefaultAccessRestrictions",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:DefaultAccessRestrictions (declaration i=16139, owner"
                + " i=11616)"));
  }

  @Override
  public @Nullable UInteger getConfigurationVersion() throws UaException {
    PropertyTypeNode node = getConfigurationVersionNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ConfigurationVersion (declaration i=25267, owner i=11616)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setConfigurationVersion(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getConfigurationVersionNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ConfigurationVersion (declaration i=25267, owner i=11616)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readConfigurationVersion() throws UaException {
    return ClientMembers.await(readConfigurationVersionAsync(), false);
  }

  @Override
  public void writeConfigurationVersion(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeConfigurationVersionAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readConfigurationVersionAsync() {
    return getConfigurationVersionNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ConfigurationVersion (declaration i=25267,"
                            + " owner i=11616) on "
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
  public CompletableFuture<StatusCode> writeConfigurationVersionAsync(
      @Nullable UInteger configurationVersion) {
    return getConfigurationVersionNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ConfigurationVersion (declaration i=25267,"
                            + " owner i=11616) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(configurationVersion));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getConfigurationVersionNode() throws UaException {
    return ClientMembers.await(getConfigurationVersionNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getConfigurationVersionNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ConfigurationVersion",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:ConfigurationVersion (declaration i=25267, owner"
                + " i=11616)"));
  }

  @Override
  public @Nullable String getModelVersion() throws UaException {
    PropertyTypeNode node = getModelVersionNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ModelVersion (declaration i=32419, owner i=11616)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setModelVersion(@Nullable String value) throws UaException {
    PropertyTypeNode node = getModelVersionNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ModelVersion (declaration i=32419, owner i=11616)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String readModelVersion() throws UaException {
    return ClientMembers.await(readModelVersionAsync(), false);
  }

  @Override
  public void writeModelVersion(@Nullable String value) throws UaException {
    try {
      StatusCode statusCode = writeModelVersionAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String> readModelVersionAsync() {
    return getModelVersionNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ModelVersion (declaration i=32419, owner"
                            + " i=11616) on "
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
  public CompletableFuture<StatusCode> writeModelVersionAsync(@Nullable String modelVersion) {
    return getModelVersionNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ModelVersion (declaration i=32419, owner"
                            + " i=11616) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(modelVersion));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getModelVersionNode() throws UaException {
    return ClientMembers.await(getModelVersionNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getModelVersionNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ModelVersion",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:ModelVersion (declaration i=32419, owner i=11616)"));
  }

  @Override
  public @Nullable AddressSpaceFileTypeNode getNamespaceFileNode() throws UaException {
    return ClientMembers.await(getNamespaceFileNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable AddressSpaceFileTypeNode>
      getNamespaceFileNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        AddressSpaceFileTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "NamespaceFile",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            true,
            "http://opcfoundation.org/UA/:NamespaceFile (declaration i=11624, owner i=11616)"));
  }
}
