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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.6">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.6</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ServerStatusType extends BaseDataVariableType {
  /** Gets the existing node's local value. */
  @Nullable DateTime getStartTime() throws UaException;

  /** Sets the existing node's local value. */
  void setStartTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readStartTime() throws UaException;

  /** Writes the value remotely. */
  void writeStartTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readStartTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeStartTimeAsync(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getStartTimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getStartTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DateTime getCurrentTime() throws UaException;

  /** Sets the existing node's local value. */
  void setCurrentTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readCurrentTime() throws UaException;

  /** Writes the value remotely. */
  void writeCurrentTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readCurrentTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCurrentTimeAsync(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCurrentTimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCurrentTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServerState getState() throws UaException;

  /** Sets the existing node's local value. */
  void setState(@Nullable ServerState value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServerState readState() throws UaException;

  /** Writes the value remotely. */
  void writeState(@Nullable ServerState value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServerState> readStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeStateAsync(@Nullable ServerState value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getStateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getStateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable BuildInfo getBuildInfo() throws UaException;

  /** Sets the existing node's local value. */
  void setBuildInfo(@Nullable BuildInfo value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable BuildInfo readBuildInfo() throws UaException;

  /** Writes the value remotely. */
  void writeBuildInfo(@Nullable BuildInfo value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable BuildInfo> readBuildInfoAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeBuildInfoAsync(@Nullable BuildInfo value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BuildInfoType getBuildInfoNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BuildInfoType> getBuildInfoNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getSecondsTillShutdown() throws UaException;

  /** Sets the existing node's local value. */
  void setSecondsTillShutdown(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readSecondsTillShutdown() throws UaException;

  /** Writes the value remotely. */
  void writeSecondsTillShutdown(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readSecondsTillShutdownAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSecondsTillShutdownAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSecondsTillShutdownNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSecondsTillShutdownNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getShutdownReason() throws UaException;

  /** Sets the existing node's local value. */
  void setShutdownReason(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readShutdownReason() throws UaException;

  /** Writes the value remotely. */
  void writeShutdownReason(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readShutdownReasonAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeShutdownReasonAsync(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getShutdownReasonNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getShutdownReasonNodeAsync();
}
