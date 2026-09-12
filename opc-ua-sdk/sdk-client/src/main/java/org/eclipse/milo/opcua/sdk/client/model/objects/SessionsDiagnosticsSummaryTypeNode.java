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
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionSecurityDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
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
import org.jspecify.annotations.Nullable;

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

  /**
   * Creates an independently owned view context retaining this exact existing node and its client.
   * Cached attributes remain shared even after SDK cache eviction. Close the returned context when
   * its views are no longer needed.
   */
  public static ClientViews createViews(SessionsDiagnosticsSummaryTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable SessionDiagnosticsDataType @Nullable [] getSessionDiagnosticsArray()
      throws UaException {
    SessionDiagnosticsArrayTypeNode node = getSessionDiagnosticsArrayNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SessionDiagnosticsArray (declaration i=2027, owner i=2026)"
              + " on "
              + getNodeId());
    }
    return (SessionDiagnosticsDataType[])
        decodeValue(
            node.getValue().getValue().getValue(),
            SessionDiagnosticsDataType.class,
            ValueRanks.OneDimension);
  }

  @Override
  public void setSessionDiagnosticsArray(@Nullable SessionDiagnosticsDataType @Nullable [] value)
      throws UaException {
    SessionDiagnosticsArrayTypeNode node = getSessionDiagnosticsArrayNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SessionDiagnosticsArray (declaration i=2027, owner i=2026)"
              + " on "
              + getNodeId());
    }
    node.setValue(
        new Variant(encodeValue(value, SessionDiagnosticsDataType.class, ValueRanks.OneDimension)));
  }

  @Override
  public @Nullable SessionDiagnosticsDataType @Nullable [] readSessionDiagnosticsArray()
      throws UaException {
    return ClientMembers.await(readSessionDiagnosticsArrayAsync(), false);
  }

  @Override
  public void writeSessionDiagnosticsArray(@Nullable SessionDiagnosticsDataType @Nullable [] value)
      throws UaException {
    try {
      StatusCode statusCode = writeSessionDiagnosticsArrayAsync(value).get();
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
  public CompletableFuture<? extends @Nullable SessionDiagnosticsDataType @Nullable []>
      readSessionDiagnosticsArrayAsync() {
    return getSessionDiagnosticsArrayNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SessionDiagnosticsArray (declaration i=2027,"
                            + " owner i=2026) on "
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
                return (SessionDiagnosticsDataType[])
                    decodeValue(
                        v.getValue().getValue(),
                        SessionDiagnosticsDataType.class,
                        ValueRanks.OneDimension);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeSessionDiagnosticsArrayAsync(
      @Nullable SessionDiagnosticsDataType @Nullable [] sessionDiagnosticsArray) {
    return getSessionDiagnosticsArrayNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SessionDiagnosticsArray (declaration i=2027,"
                            + " owner i=2026) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                sessionDiagnosticsArray,
                                SessionDiagnosticsDataType.class,
                                ValueRanks.OneDimension)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public SessionDiagnosticsArrayTypeNode getSessionDiagnosticsArrayNode() throws UaException {
    return ClientMembers.await(getSessionDiagnosticsArrayNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends SessionDiagnosticsArrayTypeNode>
      getSessionDiagnosticsArrayNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        SessionDiagnosticsArrayTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SessionDiagnosticsArray",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SessionDiagnosticsArray (declaration i=2027, owner"
                + " i=2026)"));
  }

  @Override
  public @Nullable SessionSecurityDiagnosticsDataType @Nullable []
      getSessionSecurityDiagnosticsArray() throws UaException {
    SessionSecurityDiagnosticsArrayTypeNode node = getSessionSecurityDiagnosticsArrayNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SessionSecurityDiagnosticsArray (declaration i=2028, owner"
              + " i=2026) on "
              + getNodeId());
    }
    return (SessionSecurityDiagnosticsDataType[])
        decodeValue(
            node.getValue().getValue().getValue(),
            SessionSecurityDiagnosticsDataType.class,
            ValueRanks.OneDimension);
  }

  @Override
  public void setSessionSecurityDiagnosticsArray(
      @Nullable SessionSecurityDiagnosticsDataType @Nullable [] value) throws UaException {
    SessionSecurityDiagnosticsArrayTypeNode node = getSessionSecurityDiagnosticsArrayNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SessionSecurityDiagnosticsArray (declaration i=2028, owner"
              + " i=2026) on "
              + getNodeId());
    }
    node.setValue(
        new Variant(
            encodeValue(value, SessionSecurityDiagnosticsDataType.class, ValueRanks.OneDimension)));
  }

  @Override
  public @Nullable SessionSecurityDiagnosticsDataType @Nullable []
      readSessionSecurityDiagnosticsArray() throws UaException {
    return ClientMembers.await(readSessionSecurityDiagnosticsArrayAsync(), false);
  }

  @Override
  public void writeSessionSecurityDiagnosticsArray(
      @Nullable SessionSecurityDiagnosticsDataType @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeSessionSecurityDiagnosticsArrayAsync(value).get();
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
  public CompletableFuture<? extends @Nullable SessionSecurityDiagnosticsDataType @Nullable []>
      readSessionSecurityDiagnosticsArrayAsync() {
    return getSessionSecurityDiagnosticsArrayNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SessionSecurityDiagnosticsArray (declaration"
                            + " i=2028, owner i=2026) on "
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
                return (SessionSecurityDiagnosticsDataType[])
                    decodeValue(
                        v.getValue().getValue(),
                        SessionSecurityDiagnosticsDataType.class,
                        ValueRanks.OneDimension);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeSessionSecurityDiagnosticsArrayAsync(
      @Nullable SessionSecurityDiagnosticsDataType @Nullable [] sessionSecurityDiagnosticsArray) {
    return getSessionSecurityDiagnosticsArrayNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SessionSecurityDiagnosticsArray (declaration"
                            + " i=2028, owner i=2026) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                sessionSecurityDiagnosticsArray,
                                SessionSecurityDiagnosticsDataType.class,
                                ValueRanks.OneDimension)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public SessionSecurityDiagnosticsArrayTypeNode getSessionSecurityDiagnosticsArrayNode()
      throws UaException {
    return ClientMembers.await(getSessionSecurityDiagnosticsArrayNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends SessionSecurityDiagnosticsArrayTypeNode>
      getSessionSecurityDiagnosticsArrayNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        SessionSecurityDiagnosticsArrayTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SessionSecurityDiagnosticsArray",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SessionSecurityDiagnosticsArray (declaration i=2028,"
                + " owner i=2026)"));
  }
}
