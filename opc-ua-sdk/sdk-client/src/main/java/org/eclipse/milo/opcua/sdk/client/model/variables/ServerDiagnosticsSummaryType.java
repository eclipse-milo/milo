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
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.8">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.8</a>
 */
public interface ServerDiagnosticsSummaryType extends BaseDataVariableType {
  /**
   * Get the local value of the ServerViewCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ServerViewCount Node.
   * @throws UaException if an error occurs creating or getting the ServerViewCount Node.
   */
  UInteger getServerViewCount() throws UaException;

  /**
   * Set the local value of the ServerViewCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ServerViewCount Node.
   * @throws UaException if an error occurs creating or getting the ServerViewCount Node.
   */
  void setServerViewCount(UInteger value) throws UaException;

  /**
   * Read the value of the ServerViewCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readServerViewCount() throws UaException;

  /**
   * Write a new value for the ServerViewCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeServerViewCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readServerViewCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readServerViewCountAsync();

  /**
   * An asynchronous implementation of {@link #writeServerViewCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeServerViewCountAsync(UInteger value);

  /**
   * Get the ServerViewCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ServerViewCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getServerViewCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getServerViewCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getServerViewCountNodeAsync();

  /**
   * Get the local value of the CurrentSessionCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CurrentSessionCount Node.
   * @throws UaException if an error occurs creating or getting the CurrentSessionCount Node.
   */
  UInteger getCurrentSessionCount() throws UaException;

  /**
   * Set the local value of the CurrentSessionCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CurrentSessionCount Node.
   * @throws UaException if an error occurs creating or getting the CurrentSessionCount Node.
   */
  void setCurrentSessionCount(UInteger value) throws UaException;

  /**
   * Read the value of the CurrentSessionCount Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readCurrentSessionCount() throws UaException;

  /**
   * Write a new value for the CurrentSessionCount Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCurrentSessionCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCurrentSessionCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readCurrentSessionCountAsync();

  /**
   * An asynchronous implementation of {@link #writeCurrentSessionCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCurrentSessionCountAsync(UInteger value);

  /**
   * Get the CurrentSessionCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CurrentSessionCount {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getCurrentSessionCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCurrentSessionCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCurrentSessionCountNodeAsync();

  /**
   * Get the local value of the CumulatedSessionCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CumulatedSessionCount Node.
   * @throws UaException if an error occurs creating or getting the CumulatedSessionCount Node.
   */
  UInteger getCumulatedSessionCount() throws UaException;

  /**
   * Set the local value of the CumulatedSessionCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CumulatedSessionCount Node.
   * @throws UaException if an error occurs creating or getting the CumulatedSessionCount Node.
   */
  void setCumulatedSessionCount(UInteger value) throws UaException;

  /**
   * Read the value of the CumulatedSessionCount Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readCumulatedSessionCount() throws UaException;

  /**
   * Write a new value for the CumulatedSessionCount Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCumulatedSessionCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCumulatedSessionCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readCumulatedSessionCountAsync();

  /**
   * An asynchronous implementation of {@link #writeCumulatedSessionCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCumulatedSessionCountAsync(UInteger value);

  /**
   * Get the CumulatedSessionCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CumulatedSessionCount {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getCumulatedSessionCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCumulatedSessionCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCumulatedSessionCountNodeAsync();

  /**
   * Get the local value of the SecurityRejectedSessionCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SecurityRejectedSessionCount Node.
   * @throws UaException if an error occurs creating or getting the SecurityRejectedSessionCount
   *     Node.
   */
  UInteger getSecurityRejectedSessionCount() throws UaException;

  /**
   * Set the local value of the SecurityRejectedSessionCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SecurityRejectedSessionCount Node.
   * @throws UaException if an error occurs creating or getting the SecurityRejectedSessionCount
   *     Node.
   */
  void setSecurityRejectedSessionCount(UInteger value) throws UaException;

  /**
   * Read the value of the SecurityRejectedSessionCount Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readSecurityRejectedSessionCount() throws UaException;

  /**
   * Write a new value for the SecurityRejectedSessionCount Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSecurityRejectedSessionCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSecurityRejectedSessionCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readSecurityRejectedSessionCountAsync();

  /**
   * An asynchronous implementation of {@link #writeSecurityRejectedSessionCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSecurityRejectedSessionCountAsync(UInteger value);

  /**
   * Get the SecurityRejectedSessionCount {@link BaseDataVariableType} Node, or {@code null} if it
   * does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SecurityRejectedSessionCount {@link BaseDataVariableType} Node, or {@code null} if
   *     it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getSecurityRejectedSessionCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSecurityRejectedSessionCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSecurityRejectedSessionCountNodeAsync();

  /**
   * Get the local value of the RejectedSessionCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the RejectedSessionCount Node.
   * @throws UaException if an error occurs creating or getting the RejectedSessionCount Node.
   */
  UInteger getRejectedSessionCount() throws UaException;

  /**
   * Set the local value of the RejectedSessionCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the RejectedSessionCount Node.
   * @throws UaException if an error occurs creating or getting the RejectedSessionCount Node.
   */
  void setRejectedSessionCount(UInteger value) throws UaException;

  /**
   * Read the value of the RejectedSessionCount Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readRejectedSessionCount() throws UaException;

  /**
   * Write a new value for the RejectedSessionCount Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeRejectedSessionCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readRejectedSessionCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readRejectedSessionCountAsync();

  /**
   * An asynchronous implementation of {@link #writeRejectedSessionCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeRejectedSessionCountAsync(UInteger value);

  /**
   * Get the RejectedSessionCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RejectedSessionCount {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getRejectedSessionCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRejectedSessionCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRejectedSessionCountNodeAsync();

  /**
   * Get the local value of the SessionTimeoutCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SessionTimeoutCount Node.
   * @throws UaException if an error occurs creating or getting the SessionTimeoutCount Node.
   */
  UInteger getSessionTimeoutCount() throws UaException;

  /**
   * Set the local value of the SessionTimeoutCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SessionTimeoutCount Node.
   * @throws UaException if an error occurs creating or getting the SessionTimeoutCount Node.
   */
  void setSessionTimeoutCount(UInteger value) throws UaException;

  /**
   * Read the value of the SessionTimeoutCount Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readSessionTimeoutCount() throws UaException;

  /**
   * Write a new value for the SessionTimeoutCount Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSessionTimeoutCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSessionTimeoutCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readSessionTimeoutCountAsync();

  /**
   * An asynchronous implementation of {@link #writeSessionTimeoutCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSessionTimeoutCountAsync(UInteger value);

  /**
   * Get the SessionTimeoutCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SessionTimeoutCount {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getSessionTimeoutCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSessionTimeoutCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSessionTimeoutCountNodeAsync();

  /**
   * Get the local value of the SessionAbortCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SessionAbortCount Node.
   * @throws UaException if an error occurs creating or getting the SessionAbortCount Node.
   */
  UInteger getSessionAbortCount() throws UaException;

  /**
   * Set the local value of the SessionAbortCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SessionAbortCount Node.
   * @throws UaException if an error occurs creating or getting the SessionAbortCount Node.
   */
  void setSessionAbortCount(UInteger value) throws UaException;

  /**
   * Read the value of the SessionAbortCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readSessionAbortCount() throws UaException;

  /**
   * Write a new value for the SessionAbortCount Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSessionAbortCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSessionAbortCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readSessionAbortCountAsync();

  /**
   * An asynchronous implementation of {@link #writeSessionAbortCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSessionAbortCountAsync(UInteger value);

  /**
   * Get the SessionAbortCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SessionAbortCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getSessionAbortCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSessionAbortCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSessionAbortCountNodeAsync();

  /**
   * Get the local value of the PublishingIntervalCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the PublishingIntervalCount Node.
   * @throws UaException if an error occurs creating or getting the PublishingIntervalCount Node.
   */
  UInteger getPublishingIntervalCount() throws UaException;

  /**
   * Set the local value of the PublishingIntervalCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the PublishingIntervalCount Node.
   * @throws UaException if an error occurs creating or getting the PublishingIntervalCount Node.
   */
  void setPublishingIntervalCount(UInteger value) throws UaException;

  /**
   * Read the value of the PublishingIntervalCount Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readPublishingIntervalCount() throws UaException;

  /**
   * Write a new value for the PublishingIntervalCount Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePublishingIntervalCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPublishingIntervalCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readPublishingIntervalCountAsync();

  /**
   * An asynchronous implementation of {@link #writePublishingIntervalCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePublishingIntervalCountAsync(UInteger value);

  /**
   * Get the PublishingIntervalCount {@link BaseDataVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PublishingIntervalCount {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getPublishingIntervalCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPublishingIntervalCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getPublishingIntervalCountNodeAsync();

  /**
   * Get the local value of the CurrentSubscriptionCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CurrentSubscriptionCount Node.
   * @throws UaException if an error occurs creating or getting the CurrentSubscriptionCount Node.
   */
  UInteger getCurrentSubscriptionCount() throws UaException;

  /**
   * Set the local value of the CurrentSubscriptionCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CurrentSubscriptionCount Node.
   * @throws UaException if an error occurs creating or getting the CurrentSubscriptionCount Node.
   */
  void setCurrentSubscriptionCount(UInteger value) throws UaException;

  /**
   * Read the value of the CurrentSubscriptionCount Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readCurrentSubscriptionCount() throws UaException;

  /**
   * Write a new value for the CurrentSubscriptionCount Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCurrentSubscriptionCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCurrentSubscriptionCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readCurrentSubscriptionCountAsync();

  /**
   * An asynchronous implementation of {@link #writeCurrentSubscriptionCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCurrentSubscriptionCountAsync(UInteger value);

  /**
   * Get the CurrentSubscriptionCount {@link BaseDataVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CurrentSubscriptionCount {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getCurrentSubscriptionCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCurrentSubscriptionCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCurrentSubscriptionCountNodeAsync();

  /**
   * Get the local value of the CumulatedSubscriptionCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CumulatedSubscriptionCount Node.
   * @throws UaException if an error occurs creating or getting the CumulatedSubscriptionCount Node.
   */
  UInteger getCumulatedSubscriptionCount() throws UaException;

  /**
   * Set the local value of the CumulatedSubscriptionCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CumulatedSubscriptionCount Node.
   * @throws UaException if an error occurs creating or getting the CumulatedSubscriptionCount Node.
   */
  void setCumulatedSubscriptionCount(UInteger value) throws UaException;

  /**
   * Read the value of the CumulatedSubscriptionCount Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readCumulatedSubscriptionCount() throws UaException;

  /**
   * Write a new value for the CumulatedSubscriptionCount Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCumulatedSubscriptionCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCumulatedSubscriptionCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readCumulatedSubscriptionCountAsync();

  /**
   * An asynchronous implementation of {@link #writeCumulatedSubscriptionCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCumulatedSubscriptionCountAsync(UInteger value);

  /**
   * Get the CumulatedSubscriptionCount {@link BaseDataVariableType} Node, or {@code null} if it
   * does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CumulatedSubscriptionCount {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getCumulatedSubscriptionCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCumulatedSubscriptionCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCumulatedSubscriptionCountNodeAsync();

  /**
   * Get the local value of the SecurityRejectedRequestsCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SecurityRejectedRequestsCount Node.
   * @throws UaException if an error occurs creating or getting the SecurityRejectedRequestsCount
   *     Node.
   */
  UInteger getSecurityRejectedRequestsCount() throws UaException;

  /**
   * Set the local value of the SecurityRejectedRequestsCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SecurityRejectedRequestsCount Node.
   * @throws UaException if an error occurs creating or getting the SecurityRejectedRequestsCount
   *     Node.
   */
  void setSecurityRejectedRequestsCount(UInteger value) throws UaException;

  /**
   * Read the value of the SecurityRejectedRequestsCount Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readSecurityRejectedRequestsCount() throws UaException;

  /**
   * Write a new value for the SecurityRejectedRequestsCount Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSecurityRejectedRequestsCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSecurityRejectedRequestsCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readSecurityRejectedRequestsCountAsync();

  /**
   * An asynchronous implementation of {@link #writeSecurityRejectedRequestsCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSecurityRejectedRequestsCountAsync(UInteger value);

  /**
   * Get the SecurityRejectedRequestsCount {@link BaseDataVariableType} Node, or {@code null} if it
   * does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SecurityRejectedRequestsCount {@link BaseDataVariableType} Node, or {@code null} if
   *     it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getSecurityRejectedRequestsCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSecurityRejectedRequestsCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSecurityRejectedRequestsCountNodeAsync();

  /**
   * Get the local value of the RejectedRequestsCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the RejectedRequestsCount Node.
   * @throws UaException if an error occurs creating or getting the RejectedRequestsCount Node.
   */
  UInteger getRejectedRequestsCount() throws UaException;

  /**
   * Set the local value of the RejectedRequestsCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the RejectedRequestsCount Node.
   * @throws UaException if an error occurs creating or getting the RejectedRequestsCount Node.
   */
  void setRejectedRequestsCount(UInteger value) throws UaException;

  /**
   * Read the value of the RejectedRequestsCount Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readRejectedRequestsCount() throws UaException;

  /**
   * Write a new value for the RejectedRequestsCount Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeRejectedRequestsCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readRejectedRequestsCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readRejectedRequestsCountAsync();

  /**
   * An asynchronous implementation of {@link #writeRejectedRequestsCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeRejectedRequestsCountAsync(UInteger value);

  /**
   * Get the RejectedRequestsCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RejectedRequestsCount {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getRejectedRequestsCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRejectedRequestsCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRejectedRequestsCountNodeAsync();
}
