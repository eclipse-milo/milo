/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.8">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.8</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ServerDiagnosticsSummaryType extends BaseDataVariableType {
  /** Gets the existing node's local value. */
  @Nullable UInteger getServerViewCount() throws UaException;

  /** Sets the existing node's local value. */
  void setServerViewCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readServerViewCount() throws UaException;

  /** Writes the value remotely. */
  void writeServerViewCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readServerViewCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeServerViewCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getServerViewCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getServerViewCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getCurrentSessionCount() throws UaException;

  /** Sets the existing node's local value. */
  void setCurrentSessionCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readCurrentSessionCount() throws UaException;

  /** Writes the value remotely. */
  void writeCurrentSessionCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readCurrentSessionCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCurrentSessionCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCurrentSessionCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCurrentSessionCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getCumulatedSessionCount() throws UaException;

  /** Sets the existing node's local value. */
  void setCumulatedSessionCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readCumulatedSessionCount() throws UaException;

  /** Writes the value remotely. */
  void writeCumulatedSessionCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readCumulatedSessionCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCumulatedSessionCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCumulatedSessionCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCumulatedSessionCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getSecurityRejectedSessionCount() throws UaException;

  /** Sets the existing node's local value. */
  void setSecurityRejectedSessionCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readSecurityRejectedSessionCount() throws UaException;

  /** Writes the value remotely. */
  void writeSecurityRejectedSessionCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readSecurityRejectedSessionCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSecurityRejectedSessionCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSecurityRejectedSessionCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSecurityRejectedSessionCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getRejectedSessionCount() throws UaException;

  /** Sets the existing node's local value. */
  void setRejectedSessionCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readRejectedSessionCount() throws UaException;

  /** Writes the value remotely. */
  void writeRejectedSessionCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readRejectedSessionCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeRejectedSessionCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getRejectedSessionCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRejectedSessionCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getSessionTimeoutCount() throws UaException;

  /** Sets the existing node's local value. */
  void setSessionTimeoutCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readSessionTimeoutCount() throws UaException;

  /** Writes the value remotely. */
  void writeSessionTimeoutCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readSessionTimeoutCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSessionTimeoutCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSessionTimeoutCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSessionTimeoutCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getSessionAbortCount() throws UaException;

  /** Sets the existing node's local value. */
  void setSessionAbortCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readSessionAbortCount() throws UaException;

  /** Writes the value remotely. */
  void writeSessionAbortCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readSessionAbortCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSessionAbortCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSessionAbortCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSessionAbortCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getPublishingIntervalCount() throws UaException;

  /** Sets the existing node's local value. */
  void setPublishingIntervalCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readPublishingIntervalCount() throws UaException;

  /** Writes the value remotely. */
  void writePublishingIntervalCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readPublishingIntervalCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePublishingIntervalCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getPublishingIntervalCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getPublishingIntervalCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getCurrentSubscriptionCount() throws UaException;

  /** Sets the existing node's local value. */
  void setCurrentSubscriptionCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readCurrentSubscriptionCount() throws UaException;

  /** Writes the value remotely. */
  void writeCurrentSubscriptionCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readCurrentSubscriptionCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCurrentSubscriptionCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCurrentSubscriptionCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCurrentSubscriptionCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getCumulatedSubscriptionCount() throws UaException;

  /** Sets the existing node's local value. */
  void setCumulatedSubscriptionCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readCumulatedSubscriptionCount() throws UaException;

  /** Writes the value remotely. */
  void writeCumulatedSubscriptionCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readCumulatedSubscriptionCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCumulatedSubscriptionCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCumulatedSubscriptionCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCumulatedSubscriptionCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getSecurityRejectedRequestsCount() throws UaException;

  /** Sets the existing node's local value. */
  void setSecurityRejectedRequestsCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readSecurityRejectedRequestsCount() throws UaException;

  /** Writes the value remotely. */
  void writeSecurityRejectedRequestsCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readSecurityRejectedRequestsCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSecurityRejectedRequestsCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSecurityRejectedRequestsCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSecurityRejectedRequestsCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getRejectedRequestsCount() throws UaException;

  /** Sets the existing node's local value. */
  void setRejectedRequestsCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readRejectedRequestsCount() throws UaException;

  /** Writes the value remotely. */
  void writeRejectedRequestsCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readRejectedRequestsCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeRejectedRequestsCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getRejectedRequestsCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRejectedRequestsCountNodeAsync();
}
