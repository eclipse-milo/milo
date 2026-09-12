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
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnFailureCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnListenerStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnTalkerStatus;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

public class IIeeeBaseTsnStatusStreamTypeNode extends BaseInterfaceTypeNode
    implements IIeeeBaseTsnStatusStreamType {
  public IIeeeBaseTsnStatusStreamTypeNode(
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
  public static ClientViews createViews(IIeeeBaseTsnStatusStreamTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable TsnTalkerStatus getTalkerStatus() throws UaException {
    BaseDataVariableTypeNode node = getTalkerStatusNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TalkerStatus (declaration i=24184, owner i=24183)"
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
              "TalkerStatus: ValueRank=-1 does not permit rank " + rank);
        }
        if (value != null && !((Object) value instanceof TsnTalkerStatus)) {
          if (!(value instanceof Integer)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "TalkerStatus: expected"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnTalkerStatus or"
                    + " Int32, got "
                    + value);
          }
          if (TsnTalkerStatus.from((Integer) value) == null) {
            throw new UaException(
                StatusCodes.Bad_OutOfRange,
                "TalkerStatus: unknown"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnTalkerStatus value "
                    + value);
          }
        }
        convertedValue =
            value == null || value instanceof TsnTalkerStatus
                ? (TsnTalkerStatus) value
                : TsnTalkerStatus.from((Integer) value);
      }
    }
    return (TsnTalkerStatus) convertedValue;
  }

  @Override
  public void setTalkerStatus(@Nullable TsnTalkerStatus value) throws UaException {
    BaseDataVariableTypeNode node = getTalkerStatusNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TalkerStatus (declaration i=24184, owner i=24183)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable TsnTalkerStatus readTalkerStatus() throws UaException {
    return ClientMembers.await(readTalkerStatusAsync(), false);
  }

  @Override
  public void writeTalkerStatus(@Nullable TsnTalkerStatus value) throws UaException {
    try {
      StatusCode statusCode = writeTalkerStatusAsync(value).get();
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
  public CompletableFuture<? extends @Nullable TsnTalkerStatus> readTalkerStatusAsync() {
    return getTalkerStatusNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:TalkerStatus (declaration i=24184, owner"
                            + " i=24183) on "
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
                          "TalkerStatus: ValueRank=-1 does not permit rank " + rank);
                    }
                    if (value != null && !((Object) value instanceof TsnTalkerStatus)) {
                      if (!(value instanceof Integer)) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_TypeMismatch,
                            "TalkerStatus: expected"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnTalkerStatus"
                                + " or Int32, got "
                                + value);
                      }
                      if (TsnTalkerStatus.from((Integer) value) == null) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_OutOfRange,
                            "TalkerStatus: unknown"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnTalkerStatus"
                                + " value "
                                + value);
                      }
                    }
                    convertedValue =
                        value == null || value instanceof TsnTalkerStatus
                            ? (TsnTalkerStatus) value
                            : TsnTalkerStatus.from((Integer) value);
                  }
                }
                return (TsnTalkerStatus) convertedValue;
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeTalkerStatusAsync(
      @Nullable TsnTalkerStatus talkerStatus) {
    return getTalkerStatusNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:TalkerStatus (declaration i=24184, owner"
                            + " i=24183) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(talkerStatus));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getTalkerStatusNode() throws UaException {
    return ClientMembers.await(getTalkerStatusNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable BaseDataVariableTypeNode>
      getTalkerStatusNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "TalkerStatus",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:TalkerStatus (declaration i=24184, owner i=24183)"));
  }

  @Override
  public @Nullable TsnListenerStatus getListenerStatus() throws UaException {
    BaseDataVariableTypeNode node = getListenerStatusNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ListenerStatus (declaration i=24185, owner i=24183)"
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
              "ListenerStatus: ValueRank=-1 does not permit rank " + rank);
        }
        if (value != null && !((Object) value instanceof TsnListenerStatus)) {
          if (!(value instanceof Integer)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "ListenerStatus: expected"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnListenerStatus or"
                    + " Int32, got "
                    + value);
          }
          if (TsnListenerStatus.from((Integer) value) == null) {
            throw new UaException(
                StatusCodes.Bad_OutOfRange,
                "ListenerStatus: unknown"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnListenerStatus value "
                    + value);
          }
        }
        convertedValue =
            value == null || value instanceof TsnListenerStatus
                ? (TsnListenerStatus) value
                : TsnListenerStatus.from((Integer) value);
      }
    }
    return (TsnListenerStatus) convertedValue;
  }

  @Override
  public void setListenerStatus(@Nullable TsnListenerStatus value) throws UaException {
    BaseDataVariableTypeNode node = getListenerStatusNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ListenerStatus (declaration i=24185, owner i=24183)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable TsnListenerStatus readListenerStatus() throws UaException {
    return ClientMembers.await(readListenerStatusAsync(), false);
  }

  @Override
  public void writeListenerStatus(@Nullable TsnListenerStatus value) throws UaException {
    try {
      StatusCode statusCode = writeListenerStatusAsync(value).get();
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
  public CompletableFuture<? extends @Nullable TsnListenerStatus> readListenerStatusAsync() {
    return getListenerStatusNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ListenerStatus (declaration i=24185, owner"
                            + " i=24183) on "
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
                          "ListenerStatus: ValueRank=-1 does not permit rank " + rank);
                    }
                    if (value != null && !((Object) value instanceof TsnListenerStatus)) {
                      if (!(value instanceof Integer)) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_TypeMismatch,
                            "ListenerStatus: expected"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnListenerStatus"
                                + " or Int32, got "
                                + value);
                      }
                      if (TsnListenerStatus.from((Integer) value) == null) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_OutOfRange,
                            "ListenerStatus: unknown"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnListenerStatus"
                                + " value "
                                + value);
                      }
                    }
                    convertedValue =
                        value == null || value instanceof TsnListenerStatus
                            ? (TsnListenerStatus) value
                            : TsnListenerStatus.from((Integer) value);
                  }
                }
                return (TsnListenerStatus) convertedValue;
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeListenerStatusAsync(
      @Nullable TsnListenerStatus listenerStatus) {
    return getListenerStatusNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ListenerStatus (declaration i=24185, owner"
                            + " i=24183) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(listenerStatus));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getListenerStatusNode() throws UaException {
    return ClientMembers.await(getListenerStatusNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable BaseDataVariableTypeNode>
      getListenerStatusNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ListenerStatus",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:ListenerStatus (declaration i=24185, owner i=24183)"));
  }

  @Override
  public @Nullable TsnFailureCode getFailureCode() throws UaException {
    BaseDataVariableTypeNode node = getFailureCodeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:FailureCode (declaration i=24186, owner i=24183)"
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
              "FailureCode: ValueRank=-1 does not permit rank " + rank);
        }
        if (value != null && !((Object) value instanceof TsnFailureCode)) {
          if (!(value instanceof Integer)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "FailureCode: expected"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnFailureCode or Int32,"
                    + " got "
                    + value);
          }
          if (TsnFailureCode.from((Integer) value) == null) {
            throw new UaException(
                StatusCodes.Bad_OutOfRange,
                "FailureCode: unknown"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnFailureCode value "
                    + value);
          }
        }
        convertedValue =
            value == null || value instanceof TsnFailureCode
                ? (TsnFailureCode) value
                : TsnFailureCode.from((Integer) value);
      }
    }
    return (TsnFailureCode) convertedValue;
  }

  @Override
  public void setFailureCode(@Nullable TsnFailureCode value) throws UaException {
    BaseDataVariableTypeNode node = getFailureCodeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:FailureCode (declaration i=24186, owner i=24183)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable TsnFailureCode readFailureCode() throws UaException {
    return ClientMembers.await(readFailureCodeAsync(), false);
  }

  @Override
  public void writeFailureCode(@Nullable TsnFailureCode value) throws UaException {
    try {
      StatusCode statusCode = writeFailureCodeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable TsnFailureCode> readFailureCodeAsync() {
    return getFailureCodeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:FailureCode (declaration i=24186, owner"
                            + " i=24183) on "
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
                          "FailureCode: ValueRank=-1 does not permit rank " + rank);
                    }
                    if (value != null && !((Object) value instanceof TsnFailureCode)) {
                      if (!(value instanceof Integer)) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_TypeMismatch,
                            "FailureCode: expected"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnFailureCode"
                                + " or Int32, got "
                                + value);
                      }
                      if (TsnFailureCode.from((Integer) value) == null) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_OutOfRange,
                            "FailureCode: unknown"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnFailureCode"
                                + " value "
                                + value);
                      }
                    }
                    convertedValue =
                        value == null || value instanceof TsnFailureCode
                            ? (TsnFailureCode) value
                            : TsnFailureCode.from((Integer) value);
                  }
                }
                return (TsnFailureCode) convertedValue;
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeFailureCodeAsync(@Nullable TsnFailureCode failureCode) {
    return getFailureCodeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:FailureCode (declaration i=24186, owner"
                            + " i=24183) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(failureCode));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getFailureCodeNode() throws UaException {
    return ClientMembers.await(getFailureCodeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getFailureCodeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "FailureCode",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:FailureCode (declaration i=24186, owner i=24183)"));
  }

  @Override
  public @Nullable Object getFailureSystemIdentifier() throws UaException {
    BaseDataVariableTypeNode node = getFailureSystemIdentifierNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:FailureSystemIdentifier (declaration i=24187, owner"
              + " i=24183) on "
              + getNodeId());
    }
    return (Object) node.getValue().getValue().getValue();
  }

  @Override
  public void setFailureSystemIdentifier(@Nullable Object value) throws UaException {
    BaseDataVariableTypeNode node = getFailureSystemIdentifierNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:FailureSystemIdentifier (declaration i=24187, owner"
              + " i=24183) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Object readFailureSystemIdentifier() throws UaException {
    return ClientMembers.await(readFailureSystemIdentifierAsync(), false);
  }

  @Override
  public void writeFailureSystemIdentifier(@Nullable Object value) throws UaException {
    try {
      StatusCode statusCode = writeFailureSystemIdentifierAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Object> readFailureSystemIdentifierAsync() {
    return getFailureSystemIdentifierNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:FailureSystemIdentifier (declaration i=24187,"
                            + " owner i=24183) on "
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
                return (Object) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeFailureSystemIdentifierAsync(
      @Nullable Object failureSystemIdentifier) {
    return getFailureSystemIdentifierNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:FailureSystemIdentifier (declaration i=24187,"
                            + " owner i=24183) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(failureSystemIdentifier));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getFailureSystemIdentifierNode() throws UaException {
    return ClientMembers.await(getFailureSystemIdentifierNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getFailureSystemIdentifierNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "FailureSystemIdentifier",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:FailureSystemIdentifier (declaration i=24187, owner"
                + " i=24183)"));
  }
}
