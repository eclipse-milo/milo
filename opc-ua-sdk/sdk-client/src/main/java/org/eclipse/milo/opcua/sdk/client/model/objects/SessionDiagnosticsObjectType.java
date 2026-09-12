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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.5">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.5</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface SessionDiagnosticsObjectType extends BaseObjectType {
  QualifiedProperty<NodeId[]> CURRENT_ROLE_IDS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CurrentRoleIds",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          1,
          NodeId[].class);

  /** Gets the existing node's local value. */
  @Nullable NodeId @Nullable [] getCurrentRoleIds() throws UaException;

  /** Sets the existing node's local value. */
  void setCurrentRoleIds(@Nullable NodeId @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId @Nullable [] readCurrentRoleIds() throws UaException;

  /** Writes the value remotely. */
  void writeCurrentRoleIds(@Nullable NodeId @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId @Nullable []> readCurrentRoleIdsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCurrentRoleIdsAsync(@Nullable NodeId @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getCurrentRoleIdsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getCurrentRoleIdsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable SessionDiagnosticsDataType getSessionDiagnostics() throws UaException;

  /** Sets the existing node's local value. */
  void setSessionDiagnostics(@Nullable SessionDiagnosticsDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable SessionDiagnosticsDataType readSessionDiagnostics() throws UaException;

  /** Writes the value remotely. */
  void writeSessionDiagnostics(@Nullable SessionDiagnosticsDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable SessionDiagnosticsDataType> readSessionDiagnosticsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSessionDiagnosticsAsync(
      @Nullable SessionDiagnosticsDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  SessionDiagnosticsVariableType getSessionDiagnosticsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends SessionDiagnosticsVariableType> getSessionDiagnosticsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable SessionSecurityDiagnosticsDataType getSessionSecurityDiagnostics() throws UaException;

  /** Sets the existing node's local value. */
  void setSessionSecurityDiagnostics(@Nullable SessionSecurityDiagnosticsDataType value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable SessionSecurityDiagnosticsDataType readSessionSecurityDiagnostics() throws UaException;

  /** Writes the value remotely. */
  void writeSessionSecurityDiagnostics(@Nullable SessionSecurityDiagnosticsDataType value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable SessionSecurityDiagnosticsDataType>
      readSessionSecurityDiagnosticsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSessionSecurityDiagnosticsAsync(
      @Nullable SessionSecurityDiagnosticsDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  SessionSecurityDiagnosticsType getSessionSecurityDiagnosticsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends SessionSecurityDiagnosticsType>
      getSessionSecurityDiagnosticsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable SubscriptionDiagnosticsDataType @Nullable [] getSubscriptionDiagnosticsArray()
      throws UaException;

  /** Sets the existing node's local value. */
  void setSubscriptionDiagnosticsArray(@Nullable SubscriptionDiagnosticsDataType @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable SubscriptionDiagnosticsDataType @Nullable [] readSubscriptionDiagnosticsArray()
      throws UaException;

  /** Writes the value remotely. */
  void writeSubscriptionDiagnosticsArray(
      @Nullable SubscriptionDiagnosticsDataType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable SubscriptionDiagnosticsDataType @Nullable []>
      readSubscriptionDiagnosticsArrayAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSubscriptionDiagnosticsArrayAsync(
      @Nullable SubscriptionDiagnosticsDataType @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  SubscriptionDiagnosticsArrayType getSubscriptionDiagnosticsArrayNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends SubscriptionDiagnosticsArrayType>
      getSubscriptionDiagnosticsArrayNodeAsync();
}
