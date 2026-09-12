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
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Annotation;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

public class AuditHistoryAnnotationUpdateEventTypeNode extends AuditHistoryUpdateEventTypeNode
    implements AuditHistoryAnnotationUpdateEventType {
  public AuditHistoryAnnotationUpdateEventTypeNode(
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
  public static ClientViews createViews(AuditHistoryAnnotationUpdateEventTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable PerformUpdateType getPerformInsertReplace() throws UaException {
    PropertyTypeNode node = getPerformInsertReplaceNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PerformInsertReplace (declaration i=19293, owner i=19095)"
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
              "PerformInsertReplace: ValueRank=-1 does not permit rank " + rank);
        }
        if (value != null && !((Object) value instanceof PerformUpdateType)) {
          if (!(value instanceof Integer)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "PerformInsertReplace: expected"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType or"
                    + " Int32, got "
                    + value);
          }
          if (PerformUpdateType.from((Integer) value) == null) {
            throw new UaException(
                StatusCodes.Bad_OutOfRange,
                "PerformInsertReplace: unknown"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType value "
                    + value);
          }
        }
        convertedValue =
            value == null || value instanceof PerformUpdateType
                ? (PerformUpdateType) value
                : PerformUpdateType.from((Integer) value);
      }
    }
    return (PerformUpdateType) convertedValue;
  }

  @Override
  public void setPerformInsertReplace(@Nullable PerformUpdateType value) throws UaException {
    PropertyTypeNode node = getPerformInsertReplaceNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PerformInsertReplace (declaration i=19293, owner i=19095)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable PerformUpdateType readPerformInsertReplace() throws UaException {
    return ClientMembers.await(readPerformInsertReplaceAsync(), false);
  }

  @Override
  public void writePerformInsertReplace(@Nullable PerformUpdateType value) throws UaException {
    try {
      StatusCode statusCode = writePerformInsertReplaceAsync(value).get();
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
  public CompletableFuture<? extends @Nullable PerformUpdateType> readPerformInsertReplaceAsync() {
    return getPerformInsertReplaceNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:PerformInsertReplace (declaration i=19293,"
                            + " owner i=19095) on "
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
                          "PerformInsertReplace: ValueRank=-1 does not permit rank " + rank);
                    }
                    if (value != null && !((Object) value instanceof PerformUpdateType)) {
                      if (!(value instanceof Integer)) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_TypeMismatch,
                            "PerformInsertReplace: expected"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType"
                                + " or Int32, got "
                                + value);
                      }
                      if (PerformUpdateType.from((Integer) value) == null) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_OutOfRange,
                            "PerformInsertReplace: unknown"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType"
                                + " value "
                                + value);
                      }
                    }
                    convertedValue =
                        value == null || value instanceof PerformUpdateType
                            ? (PerformUpdateType) value
                            : PerformUpdateType.from((Integer) value);
                  }
                }
                return (PerformUpdateType) convertedValue;
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writePerformInsertReplaceAsync(
      @Nullable PerformUpdateType performInsertReplace) {
    return getPerformInsertReplaceNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:PerformInsertReplace (declaration i=19293,"
                            + " owner i=19095) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(performInsertReplace));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getPerformInsertReplaceNode() throws UaException {
    return ClientMembers.await(getPerformInsertReplaceNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPerformInsertReplaceNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "PerformInsertReplace",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:PerformInsertReplace (declaration i=19293, owner"
                + " i=19095)"));
  }

  @Override
  public @Nullable Annotation @Nullable [] getNewValues() throws UaException {
    PropertyTypeNode node = getNewValuesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NewValues (declaration i=19294, owner i=19095)"
              + " on "
              + getNodeId());
    }
    return (Annotation[])
        decodeValue(
            node.getValue().getValue().getValue(), Annotation.class, ValueRanks.OneDimension);
  }

  @Override
  public void setNewValues(@Nullable Annotation @Nullable [] value) throws UaException {
    PropertyTypeNode node = getNewValuesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NewValues (declaration i=19294, owner i=19095)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, Annotation.class, ValueRanks.OneDimension)));
  }

  @Override
  public @Nullable Annotation @Nullable [] readNewValues() throws UaException {
    return ClientMembers.await(readNewValuesAsync(), false);
  }

  @Override
  public void writeNewValues(@Nullable Annotation @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeNewValuesAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Annotation @Nullable []> readNewValuesAsync() {
    return getNewValuesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:NewValues (declaration i=19294, owner"
                            + " i=19095) on "
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
                return (Annotation[])
                    decodeValue(v.getValue().getValue(), Annotation.class, ValueRanks.OneDimension);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeNewValuesAsync(
      @Nullable Annotation @Nullable [] newValues) {
    return getNewValuesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:NewValues (declaration i=19294, owner"
                            + " i=19095) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(newValues, Annotation.class, ValueRanks.OneDimension)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getNewValuesNode() throws UaException {
    return ClientMembers.await(getNewValuesNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getNewValuesNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "NewValues",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:NewValues (declaration i=19294, owner i=19095)"));
  }

  @Override
  public @Nullable Annotation @Nullable [] getOldValues() throws UaException {
    PropertyTypeNode node = getOldValuesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:OldValues (declaration i=19295, owner i=19095)"
              + " on "
              + getNodeId());
    }
    return (Annotation[])
        decodeValue(
            node.getValue().getValue().getValue(), Annotation.class, ValueRanks.OneDimension);
  }

  @Override
  public void setOldValues(@Nullable Annotation @Nullable [] value) throws UaException {
    PropertyTypeNode node = getOldValuesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:OldValues (declaration i=19295, owner i=19095)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, Annotation.class, ValueRanks.OneDimension)));
  }

  @Override
  public @Nullable Annotation @Nullable [] readOldValues() throws UaException {
    return ClientMembers.await(readOldValuesAsync(), false);
  }

  @Override
  public void writeOldValues(@Nullable Annotation @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeOldValuesAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Annotation @Nullable []> readOldValuesAsync() {
    return getOldValuesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:OldValues (declaration i=19295, owner"
                            + " i=19095) on "
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
                return (Annotation[])
                    decodeValue(v.getValue().getValue(), Annotation.class, ValueRanks.OneDimension);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeOldValuesAsync(
      @Nullable Annotation @Nullable [] oldValues) {
    return getOldValuesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:OldValues (declaration i=19295, owner"
                            + " i=19095) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(oldValues, Annotation.class, ValueRanks.OneDimension)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getOldValuesNode() throws UaException {
    return ClientMembers.await(getOldValuesNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getOldValuesNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "OldValues",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:OldValues (declaration i=19295, owner i=19095)"));
  }
}
