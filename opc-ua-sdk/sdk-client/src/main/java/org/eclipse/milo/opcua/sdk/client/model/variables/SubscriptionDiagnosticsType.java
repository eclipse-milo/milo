/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.12">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.12</a>
 */
public interface SubscriptionDiagnosticsType extends BaseDataVariableType {
  /**
   * Get the local value of the SessionId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SessionId Node.
   * @throws UaException if an error occurs creating or getting the SessionId Node.
   */
  NodeId getSessionId() throws UaException;

  /**
   * Set the local value of the SessionId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SessionId Node.
   * @throws UaException if an error occurs creating or getting the SessionId Node.
   */
  void setSessionId(NodeId value) throws UaException;

  /**
   * Read the value of the SessionId Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link NodeId} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId readSessionId() throws UaException;

  /**
   * Write a new value for the SessionId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link NodeId} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSessionId(NodeId value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSessionId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId> readSessionIdAsync();

  /**
   * An asynchronous implementation of {@link #writeSessionId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSessionIdAsync(NodeId value);

  /**
   * Get the SessionId {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SessionId {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getSessionIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSessionIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSessionIdNodeAsync();

  /**
   * Get the local value of the SubscriptionId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SubscriptionId Node.
   * @throws UaException if an error occurs creating or getting the SubscriptionId Node.
   */
  UInteger getSubscriptionId() throws UaException;

  /**
   * Set the local value of the SubscriptionId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SubscriptionId Node.
   * @throws UaException if an error occurs creating or getting the SubscriptionId Node.
   */
  void setSubscriptionId(UInteger value) throws UaException;

  /**
   * Read the value of the SubscriptionId Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readSubscriptionId() throws UaException;

  /**
   * Write a new value for the SubscriptionId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSubscriptionId(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSubscriptionId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readSubscriptionIdAsync();

  /**
   * An asynchronous implementation of {@link #writeSubscriptionId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSubscriptionIdAsync(UInteger value);

  /**
   * Get the SubscriptionId {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SubscriptionId {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getSubscriptionIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSubscriptionIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSubscriptionIdNodeAsync();

  /**
   * Get the local value of the Priority Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Priority Node.
   * @throws UaException if an error occurs creating or getting the Priority Node.
   */
  UByte getPriority() throws UaException;

  /**
   * Set the local value of the Priority Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Priority Node.
   * @throws UaException if an error occurs creating or getting the Priority Node.
   */
  void setPriority(UByte value) throws UaException;

  /**
   * Read the value of the Priority Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link UByte} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UByte readPriority() throws UaException;

  /**
   * Write a new value for the Priority Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UByte} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePriority(UByte value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPriority}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UByte> readPriorityAsync();

  /**
   * An asynchronous implementation of {@link #writePriority}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePriorityAsync(UByte value);

  /**
   * Get the Priority {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Priority {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getPriorityNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPriorityNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getPriorityNodeAsync();

  /**
   * Get the local value of the PublishingInterval Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the PublishingInterval Node.
   * @throws UaException if an error occurs creating or getting the PublishingInterval Node.
   */
  Double getPublishingInterval() throws UaException;

  /**
   * Set the local value of the PublishingInterval Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the PublishingInterval Node.
   * @throws UaException if an error occurs creating or getting the PublishingInterval Node.
   */
  void setPublishingInterval(Double value) throws UaException;

  /**
   * Read the value of the PublishingInterval Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readPublishingInterval() throws UaException;

  /**
   * Write a new value for the PublishingInterval Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePublishingInterval(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPublishingInterval}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readPublishingIntervalAsync();

  /**
   * An asynchronous implementation of {@link #writePublishingInterval}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePublishingIntervalAsync(Double value);

  /**
   * Get the PublishingInterval {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PublishingInterval {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getPublishingIntervalNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPublishingIntervalNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getPublishingIntervalNodeAsync();

  /**
   * Get the local value of the MaxKeepAliveCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxKeepAliveCount Node.
   * @throws UaException if an error occurs creating or getting the MaxKeepAliveCount Node.
   */
  UInteger getMaxKeepAliveCount() throws UaException;

  /**
   * Set the local value of the MaxKeepAliveCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxKeepAliveCount Node.
   * @throws UaException if an error occurs creating or getting the MaxKeepAliveCount Node.
   */
  void setMaxKeepAliveCount(UInteger value) throws UaException;

  /**
   * Read the value of the MaxKeepAliveCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxKeepAliveCount() throws UaException;

  /**
   * Write a new value for the MaxKeepAliveCount Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxKeepAliveCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxKeepAliveCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxKeepAliveCountAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxKeepAliveCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxKeepAliveCountAsync(UInteger value);

  /**
   * Get the MaxKeepAliveCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxKeepAliveCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getMaxKeepAliveCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxKeepAliveCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMaxKeepAliveCountNodeAsync();

  /**
   * Get the local value of the MaxLifetimeCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxLifetimeCount Node.
   * @throws UaException if an error occurs creating or getting the MaxLifetimeCount Node.
   */
  UInteger getMaxLifetimeCount() throws UaException;

  /**
   * Set the local value of the MaxLifetimeCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxLifetimeCount Node.
   * @throws UaException if an error occurs creating or getting the MaxLifetimeCount Node.
   */
  void setMaxLifetimeCount(UInteger value) throws UaException;

  /**
   * Read the value of the MaxLifetimeCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxLifetimeCount() throws UaException;

  /**
   * Write a new value for the MaxLifetimeCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxLifetimeCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxLifetimeCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxLifetimeCountAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxLifetimeCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxLifetimeCountAsync(UInteger value);

  /**
   * Get the MaxLifetimeCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxLifetimeCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getMaxLifetimeCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxLifetimeCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMaxLifetimeCountNodeAsync();

  /**
   * Get the local value of the MaxNotificationsPerPublish Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxNotificationsPerPublish Node.
   * @throws UaException if an error occurs creating or getting the MaxNotificationsPerPublish Node.
   */
  UInteger getMaxNotificationsPerPublish() throws UaException;

  /**
   * Set the local value of the MaxNotificationsPerPublish Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxNotificationsPerPublish Node.
   * @throws UaException if an error occurs creating or getting the MaxNotificationsPerPublish Node.
   */
  void setMaxNotificationsPerPublish(UInteger value) throws UaException;

  /**
   * Read the value of the MaxNotificationsPerPublish Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxNotificationsPerPublish() throws UaException;

  /**
   * Write a new value for the MaxNotificationsPerPublish Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxNotificationsPerPublish(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxNotificationsPerPublish}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxNotificationsPerPublishAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxNotificationsPerPublish}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxNotificationsPerPublishAsync(UInteger value);

  /**
   * Get the MaxNotificationsPerPublish {@link BaseDataVariableType} Node, or {@code null} if it
   * does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxNotificationsPerPublish {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getMaxNotificationsPerPublishNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxNotificationsPerPublishNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMaxNotificationsPerPublishNodeAsync();

  /**
   * Get the local value of the PublishingEnabled Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the PublishingEnabled Node.
   * @throws UaException if an error occurs creating or getting the PublishingEnabled Node.
   */
  Boolean getPublishingEnabled() throws UaException;

  /**
   * Set the local value of the PublishingEnabled Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the PublishingEnabled Node.
   * @throws UaException if an error occurs creating or getting the PublishingEnabled Node.
   */
  void setPublishingEnabled(Boolean value) throws UaException;

  /**
   * Read the value of the PublishingEnabled Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readPublishingEnabled() throws UaException;

  /**
   * Write a new value for the PublishingEnabled Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePublishingEnabled(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPublishingEnabled}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readPublishingEnabledAsync();

  /**
   * An asynchronous implementation of {@link #writePublishingEnabled}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePublishingEnabledAsync(Boolean value);

  /**
   * Get the PublishingEnabled {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PublishingEnabled {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getPublishingEnabledNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPublishingEnabledNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getPublishingEnabledNodeAsync();

  /**
   * Get the local value of the ModifyCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ModifyCount Node.
   * @throws UaException if an error occurs creating or getting the ModifyCount Node.
   */
  UInteger getModifyCount() throws UaException;

  /**
   * Set the local value of the ModifyCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ModifyCount Node.
   * @throws UaException if an error occurs creating or getting the ModifyCount Node.
   */
  void setModifyCount(UInteger value) throws UaException;

  /**
   * Read the value of the ModifyCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readModifyCount() throws UaException;

  /**
   * Write a new value for the ModifyCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeModifyCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readModifyCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readModifyCountAsync();

  /**
   * An asynchronous implementation of {@link #writeModifyCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeModifyCountAsync(UInteger value);

  /**
   * Get the ModifyCount {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ModifyCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getModifyCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getModifyCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getModifyCountNodeAsync();

  /**
   * Get the local value of the EnableCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the EnableCount Node.
   * @throws UaException if an error occurs creating or getting the EnableCount Node.
   */
  UInteger getEnableCount() throws UaException;

  /**
   * Set the local value of the EnableCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the EnableCount Node.
   * @throws UaException if an error occurs creating or getting the EnableCount Node.
   */
  void setEnableCount(UInteger value) throws UaException;

  /**
   * Read the value of the EnableCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readEnableCount() throws UaException;

  /**
   * Write a new value for the EnableCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeEnableCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readEnableCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readEnableCountAsync();

  /**
   * An asynchronous implementation of {@link #writeEnableCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeEnableCountAsync(UInteger value);

  /**
   * Get the EnableCount {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the EnableCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getEnableCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getEnableCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getEnableCountNodeAsync();

  /**
   * Get the local value of the DisableCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DisableCount Node.
   * @throws UaException if an error occurs creating or getting the DisableCount Node.
   */
  UInteger getDisableCount() throws UaException;

  /**
   * Set the local value of the DisableCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DisableCount Node.
   * @throws UaException if an error occurs creating or getting the DisableCount Node.
   */
  void setDisableCount(UInteger value) throws UaException;

  /**
   * Read the value of the DisableCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readDisableCount() throws UaException;

  /**
   * Write a new value for the DisableCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDisableCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDisableCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readDisableCountAsync();

  /**
   * An asynchronous implementation of {@link #writeDisableCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDisableCountAsync(UInteger value);

  /**
   * Get the DisableCount {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DisableCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getDisableCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDisableCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getDisableCountNodeAsync();

  /**
   * Get the local value of the RepublishRequestCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the RepublishRequestCount Node.
   * @throws UaException if an error occurs creating or getting the RepublishRequestCount Node.
   */
  UInteger getRepublishRequestCount() throws UaException;

  /**
   * Set the local value of the RepublishRequestCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the RepublishRequestCount Node.
   * @throws UaException if an error occurs creating or getting the RepublishRequestCount Node.
   */
  void setRepublishRequestCount(UInteger value) throws UaException;

  /**
   * Read the value of the RepublishRequestCount Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readRepublishRequestCount() throws UaException;

  /**
   * Write a new value for the RepublishRequestCount Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeRepublishRequestCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readRepublishRequestCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readRepublishRequestCountAsync();

  /**
   * An asynchronous implementation of {@link #writeRepublishRequestCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeRepublishRequestCountAsync(UInteger value);

  /**
   * Get the RepublishRequestCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RepublishRequestCount {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getRepublishRequestCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRepublishRequestCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRepublishRequestCountNodeAsync();

  /**
   * Get the local value of the RepublishMessageRequestCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the RepublishMessageRequestCount Node.
   * @throws UaException if an error occurs creating or getting the RepublishMessageRequestCount
   *     Node.
   */
  UInteger getRepublishMessageRequestCount() throws UaException;

  /**
   * Set the local value of the RepublishMessageRequestCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the RepublishMessageRequestCount Node.
   * @throws UaException if an error occurs creating or getting the RepublishMessageRequestCount
   *     Node.
   */
  void setRepublishMessageRequestCount(UInteger value) throws UaException;

  /**
   * Read the value of the RepublishMessageRequestCount Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readRepublishMessageRequestCount() throws UaException;

  /**
   * Write a new value for the RepublishMessageRequestCount Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeRepublishMessageRequestCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readRepublishMessageRequestCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readRepublishMessageRequestCountAsync();

  /**
   * An asynchronous implementation of {@link #writeRepublishMessageRequestCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeRepublishMessageRequestCountAsync(UInteger value);

  /**
   * Get the RepublishMessageRequestCount {@link BaseDataVariableType} Node, or {@code null} if it
   * does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RepublishMessageRequestCount {@link BaseDataVariableType} Node, or {@code null} if
   *     it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getRepublishMessageRequestCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRepublishMessageRequestCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRepublishMessageRequestCountNodeAsync();

  /**
   * Get the local value of the RepublishMessageCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the RepublishMessageCount Node.
   * @throws UaException if an error occurs creating or getting the RepublishMessageCount Node.
   */
  UInteger getRepublishMessageCount() throws UaException;

  /**
   * Set the local value of the RepublishMessageCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the RepublishMessageCount Node.
   * @throws UaException if an error occurs creating or getting the RepublishMessageCount Node.
   */
  void setRepublishMessageCount(UInteger value) throws UaException;

  /**
   * Read the value of the RepublishMessageCount Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readRepublishMessageCount() throws UaException;

  /**
   * Write a new value for the RepublishMessageCount Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeRepublishMessageCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readRepublishMessageCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readRepublishMessageCountAsync();

  /**
   * An asynchronous implementation of {@link #writeRepublishMessageCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeRepublishMessageCountAsync(UInteger value);

  /**
   * Get the RepublishMessageCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RepublishMessageCount {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getRepublishMessageCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRepublishMessageCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRepublishMessageCountNodeAsync();

  /**
   * Get the local value of the TransferRequestCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the TransferRequestCount Node.
   * @throws UaException if an error occurs creating or getting the TransferRequestCount Node.
   */
  UInteger getTransferRequestCount() throws UaException;

  /**
   * Set the local value of the TransferRequestCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the TransferRequestCount Node.
   * @throws UaException if an error occurs creating or getting the TransferRequestCount Node.
   */
  void setTransferRequestCount(UInteger value) throws UaException;

  /**
   * Read the value of the TransferRequestCount Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readTransferRequestCount() throws UaException;

  /**
   * Write a new value for the TransferRequestCount Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeTransferRequestCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readTransferRequestCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readTransferRequestCountAsync();

  /**
   * An asynchronous implementation of {@link #writeTransferRequestCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeTransferRequestCountAsync(UInteger value);

  /**
   * Get the TransferRequestCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TransferRequestCount {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getTransferRequestCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTransferRequestCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getTransferRequestCountNodeAsync();

  /**
   * Get the local value of the TransferredToAltClientCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the TransferredToAltClientCount Node.
   * @throws UaException if an error occurs creating or getting the TransferredToAltClientCount
   *     Node.
   */
  UInteger getTransferredToAltClientCount() throws UaException;

  /**
   * Set the local value of the TransferredToAltClientCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the TransferredToAltClientCount Node.
   * @throws UaException if an error occurs creating or getting the TransferredToAltClientCount
   *     Node.
   */
  void setTransferredToAltClientCount(UInteger value) throws UaException;

  /**
   * Read the value of the TransferredToAltClientCount Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readTransferredToAltClientCount() throws UaException;

  /**
   * Write a new value for the TransferredToAltClientCount Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeTransferredToAltClientCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readTransferredToAltClientCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readTransferredToAltClientCountAsync();

  /**
   * An asynchronous implementation of {@link #writeTransferredToAltClientCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeTransferredToAltClientCountAsync(UInteger value);

  /**
   * Get the TransferredToAltClientCount {@link BaseDataVariableType} Node, or {@code null} if it
   * does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TransferredToAltClientCount {@link BaseDataVariableType} Node, or {@code null} if
   *     it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getTransferredToAltClientCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTransferredToAltClientCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getTransferredToAltClientCountNodeAsync();

  /**
   * Get the local value of the TransferredToSameClientCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the TransferredToSameClientCount Node.
   * @throws UaException if an error occurs creating or getting the TransferredToSameClientCount
   *     Node.
   */
  UInteger getTransferredToSameClientCount() throws UaException;

  /**
   * Set the local value of the TransferredToSameClientCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the TransferredToSameClientCount Node.
   * @throws UaException if an error occurs creating or getting the TransferredToSameClientCount
   *     Node.
   */
  void setTransferredToSameClientCount(UInteger value) throws UaException;

  /**
   * Read the value of the TransferredToSameClientCount Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readTransferredToSameClientCount() throws UaException;

  /**
   * Write a new value for the TransferredToSameClientCount Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeTransferredToSameClientCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readTransferredToSameClientCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readTransferredToSameClientCountAsync();

  /**
   * An asynchronous implementation of {@link #writeTransferredToSameClientCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeTransferredToSameClientCountAsync(UInteger value);

  /**
   * Get the TransferredToSameClientCount {@link BaseDataVariableType} Node, or {@code null} if it
   * does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TransferredToSameClientCount {@link BaseDataVariableType} Node, or {@code null} if
   *     it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getTransferredToSameClientCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTransferredToSameClientCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getTransferredToSameClientCountNodeAsync();

  /**
   * Get the local value of the PublishRequestCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the PublishRequestCount Node.
   * @throws UaException if an error occurs creating or getting the PublishRequestCount Node.
   */
  UInteger getPublishRequestCount() throws UaException;

  /**
   * Set the local value of the PublishRequestCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the PublishRequestCount Node.
   * @throws UaException if an error occurs creating or getting the PublishRequestCount Node.
   */
  void setPublishRequestCount(UInteger value) throws UaException;

  /**
   * Read the value of the PublishRequestCount Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readPublishRequestCount() throws UaException;

  /**
   * Write a new value for the PublishRequestCount Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePublishRequestCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPublishRequestCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readPublishRequestCountAsync();

  /**
   * An asynchronous implementation of {@link #writePublishRequestCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePublishRequestCountAsync(UInteger value);

  /**
   * Get the PublishRequestCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PublishRequestCount {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getPublishRequestCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPublishRequestCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getPublishRequestCountNodeAsync();

  /**
   * Get the local value of the DataChangeNotificationsCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DataChangeNotificationsCount Node.
   * @throws UaException if an error occurs creating or getting the DataChangeNotificationsCount
   *     Node.
   */
  UInteger getDataChangeNotificationsCount() throws UaException;

  /**
   * Set the local value of the DataChangeNotificationsCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DataChangeNotificationsCount Node.
   * @throws UaException if an error occurs creating or getting the DataChangeNotificationsCount
   *     Node.
   */
  void setDataChangeNotificationsCount(UInteger value) throws UaException;

  /**
   * Read the value of the DataChangeNotificationsCount Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readDataChangeNotificationsCount() throws UaException;

  /**
   * Write a new value for the DataChangeNotificationsCount Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDataChangeNotificationsCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDataChangeNotificationsCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readDataChangeNotificationsCountAsync();

  /**
   * An asynchronous implementation of {@link #writeDataChangeNotificationsCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDataChangeNotificationsCountAsync(UInteger value);

  /**
   * Get the DataChangeNotificationsCount {@link BaseDataVariableType} Node, or {@code null} if it
   * does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DataChangeNotificationsCount {@link BaseDataVariableType} Node, or {@code null} if
   *     it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getDataChangeNotificationsCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDataChangeNotificationsCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getDataChangeNotificationsCountNodeAsync();

  /**
   * Get the local value of the EventNotificationsCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the EventNotificationsCount Node.
   * @throws UaException if an error occurs creating or getting the EventNotificationsCount Node.
   */
  UInteger getEventNotificationsCount() throws UaException;

  /**
   * Set the local value of the EventNotificationsCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the EventNotificationsCount Node.
   * @throws UaException if an error occurs creating or getting the EventNotificationsCount Node.
   */
  void setEventNotificationsCount(UInteger value) throws UaException;

  /**
   * Read the value of the EventNotificationsCount Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readEventNotificationsCount() throws UaException;

  /**
   * Write a new value for the EventNotificationsCount Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeEventNotificationsCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readEventNotificationsCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readEventNotificationsCountAsync();

  /**
   * An asynchronous implementation of {@link #writeEventNotificationsCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeEventNotificationsCountAsync(UInteger value);

  /**
   * Get the EventNotificationsCount {@link BaseDataVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the EventNotificationsCount {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getEventNotificationsCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getEventNotificationsCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getEventNotificationsCountNodeAsync();

  /**
   * Get the local value of the NotificationsCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the NotificationsCount Node.
   * @throws UaException if an error occurs creating or getting the NotificationsCount Node.
   */
  UInteger getNotificationsCount() throws UaException;

  /**
   * Set the local value of the NotificationsCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the NotificationsCount Node.
   * @throws UaException if an error occurs creating or getting the NotificationsCount Node.
   */
  void setNotificationsCount(UInteger value) throws UaException;

  /**
   * Read the value of the NotificationsCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readNotificationsCount() throws UaException;

  /**
   * Write a new value for the NotificationsCount Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeNotificationsCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readNotificationsCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readNotificationsCountAsync();

  /**
   * An asynchronous implementation of {@link #writeNotificationsCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeNotificationsCountAsync(UInteger value);

  /**
   * Get the NotificationsCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the NotificationsCount {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getNotificationsCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getNotificationsCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getNotificationsCountNodeAsync();

  /**
   * Get the local value of the LatePublishRequestCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LatePublishRequestCount Node.
   * @throws UaException if an error occurs creating or getting the LatePublishRequestCount Node.
   */
  UInteger getLatePublishRequestCount() throws UaException;

  /**
   * Set the local value of the LatePublishRequestCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LatePublishRequestCount Node.
   * @throws UaException if an error occurs creating or getting the LatePublishRequestCount Node.
   */
  void setLatePublishRequestCount(UInteger value) throws UaException;

  /**
   * Read the value of the LatePublishRequestCount Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readLatePublishRequestCount() throws UaException;

  /**
   * Write a new value for the LatePublishRequestCount Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLatePublishRequestCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLatePublishRequestCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readLatePublishRequestCountAsync();

  /**
   * An asynchronous implementation of {@link #writeLatePublishRequestCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLatePublishRequestCountAsync(UInteger value);

  /**
   * Get the LatePublishRequestCount {@link BaseDataVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LatePublishRequestCount {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getLatePublishRequestCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLatePublishRequestCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getLatePublishRequestCountNodeAsync();

  /**
   * Get the local value of the CurrentKeepAliveCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CurrentKeepAliveCount Node.
   * @throws UaException if an error occurs creating or getting the CurrentKeepAliveCount Node.
   */
  UInteger getCurrentKeepAliveCount() throws UaException;

  /**
   * Set the local value of the CurrentKeepAliveCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CurrentKeepAliveCount Node.
   * @throws UaException if an error occurs creating or getting the CurrentKeepAliveCount Node.
   */
  void setCurrentKeepAliveCount(UInteger value) throws UaException;

  /**
   * Read the value of the CurrentKeepAliveCount Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readCurrentKeepAliveCount() throws UaException;

  /**
   * Write a new value for the CurrentKeepAliveCount Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCurrentKeepAliveCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCurrentKeepAliveCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readCurrentKeepAliveCountAsync();

  /**
   * An asynchronous implementation of {@link #writeCurrentKeepAliveCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCurrentKeepAliveCountAsync(UInteger value);

  /**
   * Get the CurrentKeepAliveCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CurrentKeepAliveCount {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getCurrentKeepAliveCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCurrentKeepAliveCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCurrentKeepAliveCountNodeAsync();

  /**
   * Get the local value of the CurrentLifetimeCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CurrentLifetimeCount Node.
   * @throws UaException if an error occurs creating or getting the CurrentLifetimeCount Node.
   */
  UInteger getCurrentLifetimeCount() throws UaException;

  /**
   * Set the local value of the CurrentLifetimeCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CurrentLifetimeCount Node.
   * @throws UaException if an error occurs creating or getting the CurrentLifetimeCount Node.
   */
  void setCurrentLifetimeCount(UInteger value) throws UaException;

  /**
   * Read the value of the CurrentLifetimeCount Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readCurrentLifetimeCount() throws UaException;

  /**
   * Write a new value for the CurrentLifetimeCount Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCurrentLifetimeCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCurrentLifetimeCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readCurrentLifetimeCountAsync();

  /**
   * An asynchronous implementation of {@link #writeCurrentLifetimeCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCurrentLifetimeCountAsync(UInteger value);

  /**
   * Get the CurrentLifetimeCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CurrentLifetimeCount {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getCurrentLifetimeCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCurrentLifetimeCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCurrentLifetimeCountNodeAsync();

  /**
   * Get the local value of the UnacknowledgedMessageCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the UnacknowledgedMessageCount Node.
   * @throws UaException if an error occurs creating or getting the UnacknowledgedMessageCount Node.
   */
  UInteger getUnacknowledgedMessageCount() throws UaException;

  /**
   * Set the local value of the UnacknowledgedMessageCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the UnacknowledgedMessageCount Node.
   * @throws UaException if an error occurs creating or getting the UnacknowledgedMessageCount Node.
   */
  void setUnacknowledgedMessageCount(UInteger value) throws UaException;

  /**
   * Read the value of the UnacknowledgedMessageCount Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readUnacknowledgedMessageCount() throws UaException;

  /**
   * Write a new value for the UnacknowledgedMessageCount Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeUnacknowledgedMessageCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readUnacknowledgedMessageCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readUnacknowledgedMessageCountAsync();

  /**
   * An asynchronous implementation of {@link #writeUnacknowledgedMessageCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeUnacknowledgedMessageCountAsync(UInteger value);

  /**
   * Get the UnacknowledgedMessageCount {@link BaseDataVariableType} Node, or {@code null} if it
   * does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the UnacknowledgedMessageCount {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getUnacknowledgedMessageCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getUnacknowledgedMessageCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getUnacknowledgedMessageCountNodeAsync();

  /**
   * Get the local value of the DiscardedMessageCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DiscardedMessageCount Node.
   * @throws UaException if an error occurs creating or getting the DiscardedMessageCount Node.
   */
  UInteger getDiscardedMessageCount() throws UaException;

  /**
   * Set the local value of the DiscardedMessageCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DiscardedMessageCount Node.
   * @throws UaException if an error occurs creating or getting the DiscardedMessageCount Node.
   */
  void setDiscardedMessageCount(UInteger value) throws UaException;

  /**
   * Read the value of the DiscardedMessageCount Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readDiscardedMessageCount() throws UaException;

  /**
   * Write a new value for the DiscardedMessageCount Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDiscardedMessageCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDiscardedMessageCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readDiscardedMessageCountAsync();

  /**
   * An asynchronous implementation of {@link #writeDiscardedMessageCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDiscardedMessageCountAsync(UInteger value);

  /**
   * Get the DiscardedMessageCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DiscardedMessageCount {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getDiscardedMessageCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDiscardedMessageCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getDiscardedMessageCountNodeAsync();

  /**
   * Get the local value of the MonitoredItemCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MonitoredItemCount Node.
   * @throws UaException if an error occurs creating or getting the MonitoredItemCount Node.
   */
  UInteger getMonitoredItemCount() throws UaException;

  /**
   * Set the local value of the MonitoredItemCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MonitoredItemCount Node.
   * @throws UaException if an error occurs creating or getting the MonitoredItemCount Node.
   */
  void setMonitoredItemCount(UInteger value) throws UaException;

  /**
   * Read the value of the MonitoredItemCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMonitoredItemCount() throws UaException;

  /**
   * Write a new value for the MonitoredItemCount Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMonitoredItemCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMonitoredItemCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMonitoredItemCountAsync();

  /**
   * An asynchronous implementation of {@link #writeMonitoredItemCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMonitoredItemCountAsync(UInteger value);

  /**
   * Get the MonitoredItemCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MonitoredItemCount {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getMonitoredItemCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMonitoredItemCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMonitoredItemCountNodeAsync();

  /**
   * Get the local value of the DisabledMonitoredItemCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DisabledMonitoredItemCount Node.
   * @throws UaException if an error occurs creating or getting the DisabledMonitoredItemCount Node.
   */
  UInteger getDisabledMonitoredItemCount() throws UaException;

  /**
   * Set the local value of the DisabledMonitoredItemCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DisabledMonitoredItemCount Node.
   * @throws UaException if an error occurs creating or getting the DisabledMonitoredItemCount Node.
   */
  void setDisabledMonitoredItemCount(UInteger value) throws UaException;

  /**
   * Read the value of the DisabledMonitoredItemCount Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readDisabledMonitoredItemCount() throws UaException;

  /**
   * Write a new value for the DisabledMonitoredItemCount Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDisabledMonitoredItemCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDisabledMonitoredItemCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readDisabledMonitoredItemCountAsync();

  /**
   * An asynchronous implementation of {@link #writeDisabledMonitoredItemCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDisabledMonitoredItemCountAsync(UInteger value);

  /**
   * Get the DisabledMonitoredItemCount {@link BaseDataVariableType} Node, or {@code null} if it
   * does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DisabledMonitoredItemCount {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getDisabledMonitoredItemCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDisabledMonitoredItemCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getDisabledMonitoredItemCountNodeAsync();

  /**
   * Get the local value of the MonitoringQueueOverflowCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MonitoringQueueOverflowCount Node.
   * @throws UaException if an error occurs creating or getting the MonitoringQueueOverflowCount
   *     Node.
   */
  UInteger getMonitoringQueueOverflowCount() throws UaException;

  /**
   * Set the local value of the MonitoringQueueOverflowCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MonitoringQueueOverflowCount Node.
   * @throws UaException if an error occurs creating or getting the MonitoringQueueOverflowCount
   *     Node.
   */
  void setMonitoringQueueOverflowCount(UInteger value) throws UaException;

  /**
   * Read the value of the MonitoringQueueOverflowCount Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMonitoringQueueOverflowCount() throws UaException;

  /**
   * Write a new value for the MonitoringQueueOverflowCount Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMonitoringQueueOverflowCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMonitoringQueueOverflowCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMonitoringQueueOverflowCountAsync();

  /**
   * An asynchronous implementation of {@link #writeMonitoringQueueOverflowCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMonitoringQueueOverflowCountAsync(UInteger value);

  /**
   * Get the MonitoringQueueOverflowCount {@link BaseDataVariableType} Node, or {@code null} if it
   * does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MonitoringQueueOverflowCount {@link BaseDataVariableType} Node, or {@code null} if
   *     it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getMonitoringQueueOverflowCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMonitoringQueueOverflowCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMonitoringQueueOverflowCountNodeAsync();

  /**
   * Get the local value of the NextSequenceNumber Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the NextSequenceNumber Node.
   * @throws UaException if an error occurs creating or getting the NextSequenceNumber Node.
   */
  UInteger getNextSequenceNumber() throws UaException;

  /**
   * Set the local value of the NextSequenceNumber Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the NextSequenceNumber Node.
   * @throws UaException if an error occurs creating or getting the NextSequenceNumber Node.
   */
  void setNextSequenceNumber(UInteger value) throws UaException;

  /**
   * Read the value of the NextSequenceNumber Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readNextSequenceNumber() throws UaException;

  /**
   * Write a new value for the NextSequenceNumber Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeNextSequenceNumber(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readNextSequenceNumber}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readNextSequenceNumberAsync();

  /**
   * An asynchronous implementation of {@link #writeNextSequenceNumber}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeNextSequenceNumberAsync(UInteger value);

  /**
   * Get the NextSequenceNumber {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the NextSequenceNumber {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getNextSequenceNumberNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getNextSequenceNumberNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getNextSequenceNumberNodeAsync();

  /**
   * Get the local value of the EventQueueOverflowCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the EventQueueOverflowCount Node.
   * @throws UaException if an error occurs creating or getting the EventQueueOverflowCount Node.
   */
  UInteger getEventQueueOverflowCount() throws UaException;

  /**
   * Set the local value of the EventQueueOverflowCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the EventQueueOverflowCount Node.
   * @throws UaException if an error occurs creating or getting the EventQueueOverflowCount Node.
   */
  void setEventQueueOverflowCount(UInteger value) throws UaException;

  /**
   * Read the value of the EventQueueOverflowCount Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readEventQueueOverflowCount() throws UaException;

  /**
   * Write a new value for the EventQueueOverflowCount Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeEventQueueOverflowCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readEventQueueOverflowCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readEventQueueOverflowCountAsync();

  /**
   * An asynchronous implementation of {@link #writeEventQueueOverflowCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeEventQueueOverflowCountAsync(UInteger value);

  /**
   * Get the EventQueueOverflowCount {@link BaseDataVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the EventQueueOverflowCount {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getEventQueueOverflowCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getEventQueueOverflowCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getEventQueueOverflowCountNodeAsync();
}
