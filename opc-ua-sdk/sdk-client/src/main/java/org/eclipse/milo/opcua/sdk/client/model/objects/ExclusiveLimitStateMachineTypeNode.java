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

public class ExclusiveLimitStateMachineTypeNode extends FiniteStateMachineTypeNode
    implements ExclusiveLimitStateMachineType {
  public ExclusiveLimitStateMachineTypeNode(
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
  public StateTypeNode getHighHighNode() throws UaException {
    try {
      return getHighHighNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends StateTypeNode> getHighHighNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "HighHigh", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (StateTypeNode) node);
  }

  @Override
  public StateTypeNode getHighNode() throws UaException {
    try {
      return getHighNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends StateTypeNode> getHighNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "High", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (StateTypeNode) node);
  }

  @Override
  public StateTypeNode getLowNode() throws UaException {
    try {
      return getLowNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends StateTypeNode> getLowNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Low", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (StateTypeNode) node);
  }

  @Override
  public StateTypeNode getLowLowNode() throws UaException {
    try {
      return getLowLowNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends StateTypeNode> getLowLowNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "LowLow", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (StateTypeNode) node);
  }

  @Override
  public TransitionTypeNode getLowLowToLowNode() throws UaException {
    try {
      return getLowLowToLowNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getLowLowToLowNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LowLowToLow",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TransitionTypeNode) node);
  }

  @Override
  public TransitionTypeNode getLowToLowLowNode() throws UaException {
    try {
      return getLowToLowLowNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getLowToLowLowNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LowToLowLow",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TransitionTypeNode) node);
  }

  @Override
  public TransitionTypeNode getHighHighToHighNode() throws UaException {
    try {
      return getHighHighToHighNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getHighHighToHighNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "HighHighToHigh",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TransitionTypeNode) node);
  }

  @Override
  public TransitionTypeNode getHighToHighHighNode() throws UaException {
    try {
      return getHighToHighHighNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getHighToHighHighNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "HighToHighHigh",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TransitionTypeNode) node);
  }
}
