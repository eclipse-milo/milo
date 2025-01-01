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
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionDiagnosticsVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionSecurityDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.SubscriptionDiagnosticsArrayTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;

public class SessionDiagnosticsObjectTypeNode extends BaseObjectTypeNode
    implements SessionDiagnosticsObjectType {
  public SessionDiagnosticsObjectTypeNode(
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
  public SessionDiagnosticsDataType getSessionDiagnostics() throws UaException {
    SessionDiagnosticsVariableTypeNode node = getSessionDiagnosticsNode();
    return cast(node.getValue().getValue().getValue(), SessionDiagnosticsDataType.class);
  }

  @Override
  public void setSessionDiagnostics(SessionDiagnosticsDataType value) throws UaException {
    SessionDiagnosticsVariableTypeNode node = getSessionDiagnosticsNode();
    ExtensionObject encoded = ExtensionObject.encode(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public SessionDiagnosticsDataType readSessionDiagnostics() throws UaException {
    try {
      return readSessionDiagnosticsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSessionDiagnostics(SessionDiagnosticsDataType value) throws UaException {
    try {
      writeSessionDiagnosticsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends SessionDiagnosticsDataType> readSessionDiagnosticsAsync() {
    return getSessionDiagnosticsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), SessionDiagnosticsDataType.class));
  }

  @Override
  public CompletableFuture<StatusCode> writeSessionDiagnosticsAsync(
      SessionDiagnosticsDataType sessionDiagnostics) {
    ExtensionObject encoded =
        ExtensionObject.encode(client.getStaticEncodingContext(), sessionDiagnostics);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getSessionDiagnosticsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public SessionDiagnosticsVariableTypeNode getSessionDiagnosticsNode() throws UaException {
    try {
      return getSessionDiagnosticsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends SessionDiagnosticsVariableTypeNode>
      getSessionDiagnosticsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SessionDiagnostics",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (SessionDiagnosticsVariableTypeNode) node);
  }

  @Override
  public SessionSecurityDiagnosticsDataType getSessionSecurityDiagnostics() throws UaException {
    SessionSecurityDiagnosticsTypeNode node = getSessionSecurityDiagnosticsNode();
    return cast(node.getValue().getValue().getValue(), SessionSecurityDiagnosticsDataType.class);
  }

  @Override
  public void setSessionSecurityDiagnostics(SessionSecurityDiagnosticsDataType value)
      throws UaException {
    SessionSecurityDiagnosticsTypeNode node = getSessionSecurityDiagnosticsNode();
    ExtensionObject encoded = ExtensionObject.encode(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public SessionSecurityDiagnosticsDataType readSessionSecurityDiagnostics() throws UaException {
    try {
      return readSessionSecurityDiagnosticsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSessionSecurityDiagnostics(SessionSecurityDiagnosticsDataType value)
      throws UaException {
    try {
      writeSessionSecurityDiagnosticsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends SessionSecurityDiagnosticsDataType>
      readSessionSecurityDiagnosticsAsync() {
    return getSessionSecurityDiagnosticsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), SessionSecurityDiagnosticsDataType.class));
  }

  @Override
  public CompletableFuture<StatusCode> writeSessionSecurityDiagnosticsAsync(
      SessionSecurityDiagnosticsDataType sessionSecurityDiagnostics) {
    ExtensionObject encoded =
        ExtensionObject.encode(client.getStaticEncodingContext(), sessionSecurityDiagnostics);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getSessionSecurityDiagnosticsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public SessionSecurityDiagnosticsTypeNode getSessionSecurityDiagnosticsNode() throws UaException {
    try {
      return getSessionSecurityDiagnosticsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends SessionSecurityDiagnosticsTypeNode>
      getSessionSecurityDiagnosticsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SessionSecurityDiagnostics",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (SessionSecurityDiagnosticsTypeNode) node);
  }

  @Override
  public SubscriptionDiagnosticsDataType[] getSubscriptionDiagnosticsArray() throws UaException {
    SubscriptionDiagnosticsArrayTypeNode node = getSubscriptionDiagnosticsArrayNode();
    return cast(node.getValue().getValue().getValue(), SubscriptionDiagnosticsDataType[].class);
  }

  @Override
  public void setSubscriptionDiagnosticsArray(SubscriptionDiagnosticsDataType[] value)
      throws UaException {
    SubscriptionDiagnosticsArrayTypeNode node = getSubscriptionDiagnosticsArrayNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public SubscriptionDiagnosticsDataType[] readSubscriptionDiagnosticsArray() throws UaException {
    try {
      return readSubscriptionDiagnosticsArrayAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSubscriptionDiagnosticsArray(SubscriptionDiagnosticsDataType[] value)
      throws UaException {
    try {
      writeSubscriptionDiagnosticsArrayAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends SubscriptionDiagnosticsDataType[]>
      readSubscriptionDiagnosticsArrayAsync() {
    return getSubscriptionDiagnosticsArrayNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), SubscriptionDiagnosticsDataType[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeSubscriptionDiagnosticsArrayAsync(
      SubscriptionDiagnosticsDataType[] subscriptionDiagnosticsArray) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(
            client.getStaticEncodingContext(), subscriptionDiagnosticsArray);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getSubscriptionDiagnosticsArrayNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public SubscriptionDiagnosticsArrayTypeNode getSubscriptionDiagnosticsArrayNode()
      throws UaException {
    try {
      return getSubscriptionDiagnosticsArrayNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends SubscriptionDiagnosticsArrayTypeNode>
      getSubscriptionDiagnosticsArrayNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SubscriptionDiagnosticsArray",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (SubscriptionDiagnosticsArrayTypeNode) node);
  }
}
