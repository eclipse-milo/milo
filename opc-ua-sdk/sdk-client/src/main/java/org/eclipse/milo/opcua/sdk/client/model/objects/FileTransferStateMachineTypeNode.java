/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class FileTransferStateMachineTypeNode extends FiniteStateMachineTypeNode
    implements FileTransferStateMachineType {
  public FileTransferStateMachineTypeNode(
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

  @Override
  public InitialStateTypeNode getIdleNode() throws UaException {
    try {
      return getIdleNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends InitialStateTypeNode> getIdleNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Idle", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (InitialStateTypeNode) node);
  }

  @Override
  public StateTypeNode getReadPrepareNode() throws UaException {
    try {
      return getReadPrepareNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends StateTypeNode> getReadPrepareNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ReadPrepare",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (StateTypeNode) node);
  }

  @Override
  public StateTypeNode getReadTransferNode() throws UaException {
    try {
      return getReadTransferNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends StateTypeNode> getReadTransferNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ReadTransfer",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (StateTypeNode) node);
  }

  @Override
  public StateTypeNode getApplyWriteNode() throws UaException {
    try {
      return getApplyWriteNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends StateTypeNode> getApplyWriteNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "ApplyWrite", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (StateTypeNode) node);
  }

  @Override
  public StateTypeNode getErrorNode() throws UaException {
    try {
      return getErrorNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends StateTypeNode> getErrorNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Error", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (StateTypeNode) node);
  }

  @Override
  public TransitionTypeNode getIdleToReadPrepareNode() throws UaException {
    try {
      return getIdleToReadPrepareNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getIdleToReadPrepareNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "IdleToReadPrepare",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TransitionTypeNode) node);
  }

  @Override
  public TransitionTypeNode getReadPrepareToReadTransferNode() throws UaException {
    try {
      return getReadPrepareToReadTransferNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getReadPrepareToReadTransferNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ReadPrepareToReadTransfer",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TransitionTypeNode) node);
  }

  @Override
  public TransitionTypeNode getReadTransferToIdleNode() throws UaException {
    try {
      return getReadTransferToIdleNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getReadTransferToIdleNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ReadTransferToIdle",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TransitionTypeNode) node);
  }

  @Override
  public TransitionTypeNode getIdleToApplyWriteNode() throws UaException {
    try {
      return getIdleToApplyWriteNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getIdleToApplyWriteNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "IdleToApplyWrite",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TransitionTypeNode) node);
  }

  @Override
  public TransitionTypeNode getApplyWriteToIdleNode() throws UaException {
    try {
      return getApplyWriteToIdleNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getApplyWriteToIdleNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ApplyWriteToIdle",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TransitionTypeNode) node);
  }

  @Override
  public TransitionTypeNode getReadPrepareToErrorNode() throws UaException {
    try {
      return getReadPrepareToErrorNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getReadPrepareToErrorNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ReadPrepareToError",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TransitionTypeNode) node);
  }

  @Override
  public TransitionTypeNode getReadTransferToErrorNode() throws UaException {
    try {
      return getReadTransferToErrorNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getReadTransferToErrorNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ReadTransferToError",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TransitionTypeNode) node);
  }

  @Override
  public TransitionTypeNode getApplyWriteToErrorNode() throws UaException {
    try {
      return getApplyWriteToErrorNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getApplyWriteToErrorNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ApplyWriteToError",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TransitionTypeNode) node);
  }

  @Override
  public TransitionTypeNode getErrorToIdleNode() throws UaException {
    try {
      return getErrorToIdleNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getErrorToIdleNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ErrorToIdle",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TransitionTypeNode) node);
  }
}
