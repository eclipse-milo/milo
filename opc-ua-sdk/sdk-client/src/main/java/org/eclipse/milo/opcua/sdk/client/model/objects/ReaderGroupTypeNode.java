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

public class ReaderGroupTypeNode extends PubSubGroupTypeNode implements ReaderGroupType {
  public ReaderGroupTypeNode(
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
  public PubSubDiagnosticsReaderGroupTypeNode getDiagnosticsNode() throws UaException {
    try {
      return getDiagnosticsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PubSubDiagnosticsReaderGroupTypeNode>
      getDiagnosticsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "Diagnostics",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (PubSubDiagnosticsReaderGroupTypeNode) node);
  }

  @Override
  public ReaderGroupTransportTypeNode getTransportSettingsNode() throws UaException {
    try {
      return getTransportSettingsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends ReaderGroupTransportTypeNode> getTransportSettingsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "TransportSettings",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (ReaderGroupTransportTypeNode) node);
  }

  @Override
  public ReaderGroupMessageTypeNode getMessageSettingsNode() throws UaException {
    try {
      return getMessageSettingsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends ReaderGroupMessageTypeNode> getMessageSettingsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MessageSettings",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (ReaderGroupMessageTypeNode) node);
  }
}
