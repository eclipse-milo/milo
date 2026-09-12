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
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.11">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.11</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface TrustListOutOfDateAlarmType extends SystemOffNormalAlarmType {
  QualifiedProperty<NodeId> TRUST_LIST_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "TrustListId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<DateTime> LAST_UPDATE_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastUpdateTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<Double> UPDATE_FREQUENCY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UpdateFrequency",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  /** Gets the existing node's local value. */
  @Nullable NodeId getTrustListId() throws UaException;

  /** Sets the existing node's local value. */
  void setTrustListId(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId readTrustListId() throws UaException;

  /** Writes the value remotely. */
  void writeTrustListId(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId> readTrustListIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeTrustListIdAsync(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getTrustListIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getTrustListIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DateTime getLastUpdateTime() throws UaException;

  /** Sets the existing node's local value. */
  void setLastUpdateTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readLastUpdateTime() throws UaException;

  /** Writes the value remotely. */
  void writeLastUpdateTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readLastUpdateTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLastUpdateTimeAsync(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLastUpdateTimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getLastUpdateTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getUpdateFrequency() throws UaException;

  /** Sets the existing node's local value. */
  void setUpdateFrequency(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readUpdateFrequency() throws UaException;

  /** Writes the value remotely. */
  void writeUpdateFrequency(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readUpdateFrequencyAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeUpdateFrequencyAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getUpdateFrequencyNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getUpdateFrequencyNodeAsync();
}
