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
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.7">https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.7</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AuditHistoryAtTimeDeleteEventType extends AuditHistoryDeleteEventType {
  QualifiedProperty<DateTime[]> REQ_TIMES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ReqTimes",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          1,
          DateTime[].class);

  QualifiedProperty<DataValue[]> OLD_VALUES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OldValues",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23"),
          1,
          DataValue[].class);

  /** Gets the existing node's local value. */
  @Nullable DateTime @Nullable [] getReqTimes() throws UaException;

  /** Sets the existing node's local value. */
  void setReqTimes(@Nullable DateTime @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime @Nullable [] readReqTimes() throws UaException;

  /** Writes the value remotely. */
  void writeReqTimes(@Nullable DateTime @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime @Nullable []> readReqTimesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeReqTimesAsync(@Nullable DateTime @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getReqTimesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getReqTimesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DataValue @Nullable [] getOldValues() throws UaException;

  /** Sets the existing node's local value. */
  void setOldValues(@Nullable DataValue @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DataValue @Nullable [] readOldValues() throws UaException;

  /** Writes the value remotely. */
  void writeOldValues(@Nullable DataValue @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DataValue @Nullable []> readOldValuesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeOldValuesAsync(@Nullable DataValue @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getOldValuesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getOldValuesNodeAsync();
}
