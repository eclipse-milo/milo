/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionSecurityDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionSecurityDiagnosticsDataType;

public class SessionsDiagnosticsSummaryTypeNode extends BaseObjectTypeNode
    implements SessionsDiagnosticsSummaryType {
  public SessionsDiagnosticsSummaryTypeNode(
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
  public SessionDiagnosticsDataType[] getSessionDiagnosticsArray() throws UaException {
    SessionDiagnosticsArrayTypeNode node = getSessionDiagnosticsArrayNode();
    return cast(node.getValue().getValue().getValue(), SessionDiagnosticsDataType[].class);
  }

  @Override
  public void setSessionDiagnosticsArray(SessionDiagnosticsDataType[] value) throws UaException {
    SessionDiagnosticsArrayTypeNode node = getSessionDiagnosticsArrayNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public SessionDiagnosticsDataType[] readSessionDiagnosticsArray() throws UaException {
    try {
      return readSessionDiagnosticsArrayAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSessionDiagnosticsArray(SessionDiagnosticsDataType[] value) throws UaException {
    try {
      writeSessionDiagnosticsArrayAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends SessionDiagnosticsDataType[]>
      readSessionDiagnosticsArrayAsync() {
    return getSessionDiagnosticsArrayNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), SessionDiagnosticsDataType[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeSessionDiagnosticsArrayAsync(
      SessionDiagnosticsDataType[] sessionDiagnosticsArray) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), sessionDiagnosticsArray);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getSessionDiagnosticsArrayNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public SessionDiagnosticsArrayTypeNode getSessionDiagnosticsArrayNode() throws UaException {
    try {
      return getSessionDiagnosticsArrayNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends SessionDiagnosticsArrayTypeNode>
      getSessionDiagnosticsArrayNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SessionDiagnosticsArray",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (SessionDiagnosticsArrayTypeNode) node);
  }

  @Override
  public SessionSecurityDiagnosticsDataType[] getSessionSecurityDiagnosticsArray()
      throws UaException {
    SessionSecurityDiagnosticsArrayTypeNode node = getSessionSecurityDiagnosticsArrayNode();
    return cast(node.getValue().getValue().getValue(), SessionSecurityDiagnosticsDataType[].class);
  }

  @Override
  public void setSessionSecurityDiagnosticsArray(SessionSecurityDiagnosticsDataType[] value)
      throws UaException {
    SessionSecurityDiagnosticsArrayTypeNode node = getSessionSecurityDiagnosticsArrayNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public SessionSecurityDiagnosticsDataType[] readSessionSecurityDiagnosticsArray()
      throws UaException {
    try {
      return readSessionSecurityDiagnosticsArrayAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSessionSecurityDiagnosticsArray(SessionSecurityDiagnosticsDataType[] value)
      throws UaException {
    try {
      writeSessionSecurityDiagnosticsArrayAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends SessionSecurityDiagnosticsDataType[]>
      readSessionSecurityDiagnosticsArrayAsync() {
    return getSessionSecurityDiagnosticsArrayNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), SessionSecurityDiagnosticsDataType[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeSessionSecurityDiagnosticsArrayAsync(
      SessionSecurityDiagnosticsDataType[] sessionSecurityDiagnosticsArray) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(
            client.getStaticEncodingContext(), sessionSecurityDiagnosticsArray);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getSessionSecurityDiagnosticsArrayNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public SessionSecurityDiagnosticsArrayTypeNode getSessionSecurityDiagnosticsArrayNode()
      throws UaException {
    try {
      return getSessionSecurityDiagnosticsArrayNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends SessionSecurityDiagnosticsArrayTypeNode>
      getSessionSecurityDiagnosticsArrayNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SessionSecurityDiagnosticsArray",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (SessionSecurityDiagnosticsArrayTypeNode) node);
  }
}
