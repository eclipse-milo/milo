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
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

public class ServerStatusTypeNode extends BaseDataVariableTypeNode implements ServerStatusType {
  public ServerStatusTypeNode(
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
  public @Nullable DateTime getStartTime() throws UaException {
    BaseDataVariableTypeNode node = getStartTimeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StartTime (declaration i=2139, owner i=2138)"
              + " on "
              + getNodeId());
    }
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setStartTime(@Nullable DateTime value) throws UaException {
    BaseDataVariableTypeNode node = getStartTimeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StartTime (declaration i=2139, owner i=2138)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable DateTime readStartTime() throws UaException {
    return ClientMembers.await(readStartTimeAsync(), false);
  }

  @Override
  public void writeStartTime(@Nullable DateTime value) throws UaException {
    try {
      StatusCode statusCode = writeStartTimeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable DateTime> readStartTimeAsync() {
    return getStartTimeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:StartTime (declaration i=2139, owner i=2138)"
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
                return (DateTime) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeStartTimeAsync(@Nullable DateTime startTime) {
    return getStartTimeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:StartTime (declaration i=2139, owner i=2138)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(startTime));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getStartTimeNode() throws UaException {
    return ClientMembers.await(getStartTimeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getStartTimeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "StartTime",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:StartTime (declaration i=2139, owner i=2138)"));
  }

  @Override
  public @Nullable DateTime getCurrentTime() throws UaException {
    BaseDataVariableTypeNode node = getCurrentTimeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CurrentTime (declaration i=2140, owner i=2138)"
              + " on "
              + getNodeId());
    }
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setCurrentTime(@Nullable DateTime value) throws UaException {
    BaseDataVariableTypeNode node = getCurrentTimeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CurrentTime (declaration i=2140, owner i=2138)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable DateTime readCurrentTime() throws UaException {
    return ClientMembers.await(readCurrentTimeAsync(), false);
  }

  @Override
  public void writeCurrentTime(@Nullable DateTime value) throws UaException {
    try {
      StatusCode statusCode = writeCurrentTimeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable DateTime> readCurrentTimeAsync() {
    return getCurrentTimeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CurrentTime (declaration i=2140, owner"
                            + " i=2138) on "
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
  public CompletableFuture<StatusCode> writeCurrentTimeAsync(@Nullable DateTime currentTime) {
    return getCurrentTimeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CurrentTime (declaration i=2140, owner"
                            + " i=2138) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(currentTime));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getCurrentTimeNode() throws UaException {
    return ClientMembers.await(getCurrentTimeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getCurrentTimeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CurrentTime",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:CurrentTime (declaration i=2140, owner i=2138)"));
  }

  @Override
  public @Nullable ServerState getState() throws UaException {
    BaseDataVariableTypeNode node = getStateNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:State (declaration i=2141, owner i=2138)"
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
              StatusCodes.Bad_TypeMismatch, "State: ValueRank=-1 does not permit rank " + rank);
        }
        if (value != null && !((Object) value instanceof ServerState)) {
          if (!(value instanceof Integer)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "State: expected org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState or"
                    + " Int32, got "
                    + value);
          }
          if (ServerState.from((Integer) value) == null) {
            throw new UaException(
                StatusCodes.Bad_OutOfRange,
                "State: unknown org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState"
                    + " value "
                    + value);
          }
        }
        convertedValue =
            value == null || value instanceof ServerState
                ? (ServerState) value
                : ServerState.from((Integer) value);
      }
    }
    return (ServerState) convertedValue;
  }

  @Override
  public void setState(@Nullable ServerState value) throws UaException {
    BaseDataVariableTypeNode node = getStateNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:State (declaration i=2141, owner i=2138)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable ServerState readState() throws UaException {
    return ClientMembers.await(readStateAsync(), false);
  }

  @Override
  public void writeState(@Nullable ServerState value) throws UaException {
    try {
      StatusCode statusCode = writeStateAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServerState> readStateAsync() {
    return getStateNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:State (declaration i=2141, owner i=2138)"
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
                          "State: ValueRank=-1 does not permit rank " + rank);
                    }
                    if (value != null && !((Object) value instanceof ServerState)) {
                      if (!(value instanceof Integer)) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_TypeMismatch,
                            "State: expected"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState"
                                + " or Int32, got "
                                + value);
                      }
                      if (ServerState.from((Integer) value) == null) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_OutOfRange,
                            "State: unknown"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState"
                                + " value "
                                + value);
                      }
                    }
                    convertedValue =
                        value == null || value instanceof ServerState
                            ? (ServerState) value
                            : ServerState.from((Integer) value);
                  }
                }
                return (ServerState) convertedValue;
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeStateAsync(@Nullable ServerState state) {
    return getStateNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:State (declaration i=2141, owner i=2138)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(state));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getStateNode() throws UaException {
    return ClientMembers.await(getStateNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getStateNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "State",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:State (declaration i=2141, owner i=2138)"));
  }

  @Override
  public @Nullable BuildInfo getBuildInfo() throws UaException {
    BuildInfoTypeNode node = getBuildInfoNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:BuildInfo (declaration i=2142, owner i=2138)"
              + " on "
              + getNodeId());
    }
    return (BuildInfo)
        decodeValue(node.getValue().getValue().getValue(), BuildInfo.class, ValueRanks.Scalar);
  }

  @Override
  public void setBuildInfo(@Nullable BuildInfo value) throws UaException {
    BuildInfoTypeNode node = getBuildInfoNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:BuildInfo (declaration i=2142, owner i=2138)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, BuildInfo.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable BuildInfo readBuildInfo() throws UaException {
    return ClientMembers.await(readBuildInfoAsync(), false);
  }

  @Override
  public void writeBuildInfo(@Nullable BuildInfo value) throws UaException {
    try {
      StatusCode statusCode = writeBuildInfoAsync(value).get();
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
  public CompletableFuture<? extends @Nullable BuildInfo> readBuildInfoAsync() {
    return getBuildInfoNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:BuildInfo (declaration i=2142, owner i=2138)"
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
                return (BuildInfo)
                    decodeValue(v.getValue().getValue(), BuildInfo.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeBuildInfoAsync(@Nullable BuildInfo buildInfo) {
    return getBuildInfoNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:BuildInfo (declaration i=2142, owner i=2138)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(encodeValue(buildInfo, BuildInfo.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BuildInfoTypeNode getBuildInfoNode() throws UaException {
    return ClientMembers.await(getBuildInfoNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BuildInfoTypeNode> getBuildInfoNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BuildInfoTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "BuildInfo",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:BuildInfo (declaration i=2142, owner i=2138)"));
  }

  @Override
  public @Nullable UInteger getSecondsTillShutdown() throws UaException {
    BaseDataVariableTypeNode node = getSecondsTillShutdownNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SecondsTillShutdown (declaration i=2752, owner i=2138)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setSecondsTillShutdown(@Nullable UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getSecondsTillShutdownNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SecondsTillShutdown (declaration i=2752, owner i=2138)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readSecondsTillShutdown() throws UaException {
    return ClientMembers.await(readSecondsTillShutdownAsync(), false);
  }

  @Override
  public void writeSecondsTillShutdown(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeSecondsTillShutdownAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readSecondsTillShutdownAsync() {
    return getSecondsTillShutdownNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SecondsTillShutdown (declaration i=2752,"
                            + " owner i=2138) on "
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
  public CompletableFuture<StatusCode> writeSecondsTillShutdownAsync(
      @Nullable UInteger secondsTillShutdown) {
    return getSecondsTillShutdownNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SecondsTillShutdown (declaration i=2752,"
                            + " owner i=2138) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(secondsTillShutdown));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getSecondsTillShutdownNode() throws UaException {
    return ClientMembers.await(getSecondsTillShutdownNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getSecondsTillShutdownNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SecondsTillShutdown",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SecondsTillShutdown (declaration i=2752, owner i=2138)"));
  }

  @Override
  public @Nullable LocalizedText getShutdownReason() throws UaException {
    BaseDataVariableTypeNode node = getShutdownReasonNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ShutdownReason (declaration i=2753, owner i=2138)"
              + " on "
              + getNodeId());
    }
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setShutdownReason(@Nullable LocalizedText value) throws UaException {
    BaseDataVariableTypeNode node = getShutdownReasonNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ShutdownReason (declaration i=2753, owner i=2138)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable LocalizedText readShutdownReason() throws UaException {
    return ClientMembers.await(readShutdownReasonAsync(), false);
  }

  @Override
  public void writeShutdownReason(@Nullable LocalizedText value) throws UaException {
    try {
      StatusCode statusCode = writeShutdownReasonAsync(value).get();
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
  public CompletableFuture<? extends @Nullable LocalizedText> readShutdownReasonAsync() {
    return getShutdownReasonNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ShutdownReason (declaration i=2753, owner"
                            + " i=2138) on "
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
                return (LocalizedText) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeShutdownReasonAsync(
      @Nullable LocalizedText shutdownReason) {
    return getShutdownReasonNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ShutdownReason (declaration i=2753, owner"
                            + " i=2138) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(shutdownReason));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getShutdownReasonNode() throws UaException {
    return ClientMembers.await(getShutdownReasonNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getShutdownReasonNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ShutdownReason",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ShutdownReason (declaration i=2753, owner i=2138)"));
  }
}
