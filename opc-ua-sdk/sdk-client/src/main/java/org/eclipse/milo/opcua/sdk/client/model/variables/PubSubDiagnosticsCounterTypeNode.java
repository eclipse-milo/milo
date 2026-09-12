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
import org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubDiagnosticsCounterClassification;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

public class PubSubDiagnosticsCounterTypeNode extends BaseDataVariableTypeNode
    implements PubSubDiagnosticsCounterType {
  public PubSubDiagnosticsCounterTypeNode(
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
  public @Nullable Boolean getActive() throws UaException {
    PropertyTypeNode node = getActiveNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Active (declaration i=19726, owner i=19725)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setActive(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getActiveNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Active (declaration i=19726, owner i=19725)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readActive() throws UaException {
    return ClientMembers.await(readActiveAsync(), false);
  }

  @Override
  public void writeActive(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeActiveAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readActiveAsync() {
    return getActiveNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Active (declaration i=19726, owner i=19725)"
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
  public CompletableFuture<StatusCode> writeActiveAsync(@Nullable Boolean active) {
    return getActiveNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Active (declaration i=19726, owner i=19725)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(active));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getActiveNode() throws UaException {
    return ClientMembers.await(getActiveNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getActiveNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Active",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:Active (declaration i=19726, owner i=19725)"));
  }

  @Override
  public @Nullable PubSubDiagnosticsCounterClassification getClassification() throws UaException {
    PropertyTypeNode node = getClassificationNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Classification (declaration i=19727, owner i=19725)"
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
              "Classification: ValueRank=-1 does not permit rank " + rank);
        }
        if (value != null && !((Object) value instanceof PubSubDiagnosticsCounterClassification)) {
          if (!(value instanceof Integer)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "Classification: expected"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubDiagnosticsCounterClassification"
                    + " or Int32, got "
                    + value);
          }
          if (PubSubDiagnosticsCounterClassification.from((Integer) value) == null) {
            throw new UaException(
                StatusCodes.Bad_OutOfRange,
                "Classification: unknown"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubDiagnosticsCounterClassification"
                    + " value "
                    + value);
          }
        }
        convertedValue =
            value == null || value instanceof PubSubDiagnosticsCounterClassification
                ? (PubSubDiagnosticsCounterClassification) value
                : PubSubDiagnosticsCounterClassification.from((Integer) value);
      }
    }
    return (PubSubDiagnosticsCounterClassification) convertedValue;
  }

  @Override
  public void setClassification(@Nullable PubSubDiagnosticsCounterClassification value)
      throws UaException {
    PropertyTypeNode node = getClassificationNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Classification (declaration i=19727, owner i=19725)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable PubSubDiagnosticsCounterClassification readClassification() throws UaException {
    return ClientMembers.await(readClassificationAsync(), false);
  }

  @Override
  public void writeClassification(@Nullable PubSubDiagnosticsCounterClassification value)
      throws UaException {
    try {
      StatusCode statusCode = writeClassificationAsync(value).get();
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
  public CompletableFuture<? extends @Nullable PubSubDiagnosticsCounterClassification>
      readClassificationAsync() {
    return getClassificationNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Classification (declaration i=19727, owner"
                            + " i=19725) on "
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
                          "Classification: ValueRank=-1 does not permit rank " + rank);
                    }
                    if (value != null
                        && !((Object) value instanceof PubSubDiagnosticsCounterClassification)) {
                      if (!(value instanceof Integer)) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_TypeMismatch,
                            "Classification: expected"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubDiagnosticsCounterClassification"
                                + " or Int32, got "
                                + value);
                      }
                      if (PubSubDiagnosticsCounterClassification.from((Integer) value) == null) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_OutOfRange,
                            "Classification: unknown"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubDiagnosticsCounterClassification"
                                + " value "
                                + value);
                      }
                    }
                    convertedValue =
                        value == null || value instanceof PubSubDiagnosticsCounterClassification
                            ? (PubSubDiagnosticsCounterClassification) value
                            : PubSubDiagnosticsCounterClassification.from((Integer) value);
                  }
                }
                return (PubSubDiagnosticsCounterClassification) convertedValue;
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeClassificationAsync(
      @Nullable PubSubDiagnosticsCounterClassification classification) {
    return getClassificationNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Classification (declaration i=19727, owner"
                            + " i=19725) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(classification));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getClassificationNode() throws UaException {
    return ClientMembers.await(getClassificationNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getClassificationNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Classification",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:Classification (declaration i=19727, owner i=19725)"));
  }

  @Override
  public @Nullable DiagnosticsLevel getDiagnosticsLevel() throws UaException {
    PropertyTypeNode node = getDiagnosticsLevelNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DiagnosticsLevel (declaration i=19728, owner i=19725)"
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
              "DiagnosticsLevel: ValueRank=-1 does not permit rank " + rank);
        }
        if (value != null && !((Object) value instanceof DiagnosticsLevel)) {
          if (!(value instanceof Integer)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "DiagnosticsLevel: expected"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel or"
                    + " Int32, got "
                    + value);
          }
          if (DiagnosticsLevel.from((Integer) value) == null) {
            throw new UaException(
                StatusCodes.Bad_OutOfRange,
                "DiagnosticsLevel: unknown"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel value "
                    + value);
          }
        }
        convertedValue =
            value == null || value instanceof DiagnosticsLevel
                ? (DiagnosticsLevel) value
                : DiagnosticsLevel.from((Integer) value);
      }
    }
    return (DiagnosticsLevel) convertedValue;
  }

  @Override
  public void setDiagnosticsLevel(@Nullable DiagnosticsLevel value) throws UaException {
    PropertyTypeNode node = getDiagnosticsLevelNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DiagnosticsLevel (declaration i=19728, owner i=19725)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable DiagnosticsLevel readDiagnosticsLevel() throws UaException {
    return ClientMembers.await(readDiagnosticsLevelAsync(), false);
  }

  @Override
  public void writeDiagnosticsLevel(@Nullable DiagnosticsLevel value) throws UaException {
    try {
      StatusCode statusCode = writeDiagnosticsLevelAsync(value).get();
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
  public CompletableFuture<? extends @Nullable DiagnosticsLevel> readDiagnosticsLevelAsync() {
    return getDiagnosticsLevelNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DiagnosticsLevel (declaration i=19728, owner"
                            + " i=19725) on "
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
                          "DiagnosticsLevel: ValueRank=-1 does not permit rank " + rank);
                    }
                    if (value != null && !((Object) value instanceof DiagnosticsLevel)) {
                      if (!(value instanceof Integer)) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_TypeMismatch,
                            "DiagnosticsLevel: expected"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel"
                                + " or Int32, got "
                                + value);
                      }
                      if (DiagnosticsLevel.from((Integer) value) == null) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_OutOfRange,
                            "DiagnosticsLevel: unknown"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel"
                                + " value "
                                + value);
                      }
                    }
                    convertedValue =
                        value == null || value instanceof DiagnosticsLevel
                            ? (DiagnosticsLevel) value
                            : DiagnosticsLevel.from((Integer) value);
                  }
                }
                return (DiagnosticsLevel) convertedValue;
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeDiagnosticsLevelAsync(
      @Nullable DiagnosticsLevel diagnosticsLevel) {
    return getDiagnosticsLevelNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DiagnosticsLevel (declaration i=19728, owner"
                            + " i=19725) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(diagnosticsLevel));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getDiagnosticsLevelNode() throws UaException {
    return ClientMembers.await(getDiagnosticsLevelNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDiagnosticsLevelNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DiagnosticsLevel",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DiagnosticsLevel (declaration i=19728, owner i=19725)"));
  }

  @Override
  public @Nullable DateTime getTimeFirstChange() throws UaException {
    PropertyTypeNode node = getTimeFirstChangeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TimeFirstChange (declaration i=19729, owner i=19725)"
              + " on "
              + getNodeId());
    }
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setTimeFirstChange(@Nullable DateTime value) throws UaException {
    PropertyTypeNode node = getTimeFirstChangeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TimeFirstChange (declaration i=19729, owner i=19725)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable DateTime readTimeFirstChange() throws UaException {
    return ClientMembers.await(readTimeFirstChangeAsync(), false);
  }

  @Override
  public void writeTimeFirstChange(@Nullable DateTime value) throws UaException {
    try {
      StatusCode statusCode = writeTimeFirstChangeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable DateTime> readTimeFirstChangeAsync() {
    return getTimeFirstChangeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:TimeFirstChange (declaration i=19729, owner"
                            + " i=19725) on "
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
  public CompletableFuture<StatusCode> writeTimeFirstChangeAsync(
      @Nullable DateTime timeFirstChange) {
    return getTimeFirstChangeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:TimeFirstChange (declaration i=19729, owner"
                            + " i=19725) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(timeFirstChange));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getTimeFirstChangeNode() throws UaException {
    return ClientMembers.await(getTimeFirstChangeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getTimeFirstChangeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "TimeFirstChange",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:TimeFirstChange (declaration i=19729, owner i=19725)"));
  }
}
