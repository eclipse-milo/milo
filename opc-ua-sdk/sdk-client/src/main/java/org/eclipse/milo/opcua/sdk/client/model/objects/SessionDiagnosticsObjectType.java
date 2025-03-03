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
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionDiagnosticsVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionSecurityDiagnosticsType;
import org.eclipse.milo.opcua.sdk.client.model.variables.SubscriptionDiagnosticsArrayType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionSecurityDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.5">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.5</a>
 */
public interface SessionDiagnosticsObjectType extends BaseObjectType {
  QualifiedProperty<NodeId[]> CURRENT_ROLE_IDS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CurrentRoleIds",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          1,
          NodeId[].class);

  /**
   * Get the local value of the CurrentRoleIds Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CurrentRoleIds Node.
   * @throws UaException if an error occurs creating or getting the CurrentRoleIds Node.
   */
  NodeId[] getCurrentRoleIds() throws UaException;

  /**
   * Set the local value of the CurrentRoleIds Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CurrentRoleIds Node.
   * @throws UaException if an error occurs creating or getting the CurrentRoleIds Node.
   */
  void setCurrentRoleIds(NodeId[] value) throws UaException;

  /**
   * Read the value of the CurrentRoleIds Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link NodeId[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId[] readCurrentRoleIds() throws UaException;

  /**
   * Write a new value for the CurrentRoleIds Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link NodeId[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCurrentRoleIds(NodeId[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCurrentRoleIds}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId[]> readCurrentRoleIdsAsync();

  /**
   * An asynchronous implementation of {@link #writeCurrentRoleIds}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCurrentRoleIdsAsync(NodeId[] value);

  /**
   * Get the CurrentRoleIds {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CurrentRoleIds {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getCurrentRoleIdsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCurrentRoleIdsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getCurrentRoleIdsNodeAsync();

  /**
   * Get the local value of the SessionDiagnostics Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SessionDiagnostics Node.
   * @throws UaException if an error occurs creating or getting the SessionDiagnostics Node.
   */
  SessionDiagnosticsDataType getSessionDiagnostics() throws UaException;

  /**
   * Set the local value of the SessionDiagnostics Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SessionDiagnostics Node.
   * @throws UaException if an error occurs creating or getting the SessionDiagnostics Node.
   */
  void setSessionDiagnostics(SessionDiagnosticsDataType value) throws UaException;

  /**
   * Read the value of the SessionDiagnostics Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link SessionDiagnosticsDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  SessionDiagnosticsDataType readSessionDiagnostics() throws UaException;

  /**
   * Write a new value for the SessionDiagnostics Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link SessionDiagnosticsDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSessionDiagnostics(SessionDiagnosticsDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSessionDiagnostics}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends SessionDiagnosticsDataType> readSessionDiagnosticsAsync();

  /**
   * An asynchronous implementation of {@link #writeSessionDiagnostics}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSessionDiagnosticsAsync(SessionDiagnosticsDataType value);

  /**
   * Get the SessionDiagnostics {@link SessionDiagnosticsVariableType} Node, or {@code null} if it
   * does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SessionDiagnostics {@link SessionDiagnosticsVariableType} Node, or {@code null} if
   *     it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  SessionDiagnosticsVariableType getSessionDiagnosticsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSessionDiagnosticsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the SessionDiagnosticsVariableType
   *     Node or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends SessionDiagnosticsVariableType> getSessionDiagnosticsNodeAsync();

  /**
   * Get the local value of the SessionSecurityDiagnostics Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SessionSecurityDiagnostics Node.
   * @throws UaException if an error occurs creating or getting the SessionSecurityDiagnostics Node.
   */
  SessionSecurityDiagnosticsDataType getSessionSecurityDiagnostics() throws UaException;

  /**
   * Set the local value of the SessionSecurityDiagnostics Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SessionSecurityDiagnostics Node.
   * @throws UaException if an error occurs creating or getting the SessionSecurityDiagnostics Node.
   */
  void setSessionSecurityDiagnostics(SessionSecurityDiagnosticsDataType value) throws UaException;

  /**
   * Read the value of the SessionSecurityDiagnostics Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link SessionSecurityDiagnosticsDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  SessionSecurityDiagnosticsDataType readSessionSecurityDiagnostics() throws UaException;

  /**
   * Write a new value for the SessionSecurityDiagnostics Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link SessionSecurityDiagnosticsDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSessionSecurityDiagnostics(SessionSecurityDiagnosticsDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSessionSecurityDiagnostics}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends SessionSecurityDiagnosticsDataType>
      readSessionSecurityDiagnosticsAsync();

  /**
   * An asynchronous implementation of {@link #writeSessionSecurityDiagnostics}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSessionSecurityDiagnosticsAsync(
      SessionSecurityDiagnosticsDataType value);

  /**
   * Get the SessionSecurityDiagnostics {@link SessionSecurityDiagnosticsType} Node, or {@code null}
   * if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SessionSecurityDiagnostics {@link SessionSecurityDiagnosticsType} Node, or {@code
   *     null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  SessionSecurityDiagnosticsType getSessionSecurityDiagnosticsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSessionSecurityDiagnosticsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the SessionSecurityDiagnosticsType
   *     Node or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends SessionSecurityDiagnosticsType>
      getSessionSecurityDiagnosticsNodeAsync();

  /**
   * Get the local value of the SubscriptionDiagnosticsArray Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SubscriptionDiagnosticsArray Node.
   * @throws UaException if an error occurs creating or getting the SubscriptionDiagnosticsArray
   *     Node.
   */
  SubscriptionDiagnosticsDataType[] getSubscriptionDiagnosticsArray() throws UaException;

  /**
   * Set the local value of the SubscriptionDiagnosticsArray Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SubscriptionDiagnosticsArray Node.
   * @throws UaException if an error occurs creating or getting the SubscriptionDiagnosticsArray
   *     Node.
   */
  void setSubscriptionDiagnosticsArray(SubscriptionDiagnosticsDataType[] value) throws UaException;

  /**
   * Read the value of the SubscriptionDiagnosticsArray Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link SubscriptionDiagnosticsDataType[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  SubscriptionDiagnosticsDataType[] readSubscriptionDiagnosticsArray() throws UaException;

  /**
   * Write a new value for the SubscriptionDiagnosticsArray Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link SubscriptionDiagnosticsDataType[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSubscriptionDiagnosticsArray(SubscriptionDiagnosticsDataType[] value)
      throws UaException;

  /**
   * An asynchronous implementation of {@link #readSubscriptionDiagnosticsArray}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends SubscriptionDiagnosticsDataType[]>
      readSubscriptionDiagnosticsArrayAsync();

  /**
   * An asynchronous implementation of {@link #writeSubscriptionDiagnosticsArray}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSubscriptionDiagnosticsArrayAsync(
      SubscriptionDiagnosticsDataType[] value);

  /**
   * Get the SubscriptionDiagnosticsArray {@link SubscriptionDiagnosticsArrayType} Node, or {@code
   * null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SubscriptionDiagnosticsArray {@link SubscriptionDiagnosticsArrayType} Node, or
   *     {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  SubscriptionDiagnosticsArrayType getSubscriptionDiagnosticsArrayNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSubscriptionDiagnosticsArrayNode()}.
   *
   * @return a CompletableFuture that completes successfully with the
   *     SubscriptionDiagnosticsArrayType Node or completes exceptionally if an error occurs
   *     creating or getting the Node.
   */
  CompletableFuture<? extends SubscriptionDiagnosticsArrayType>
      getSubscriptionDiagnosticsArrayNodeAsync();
}
