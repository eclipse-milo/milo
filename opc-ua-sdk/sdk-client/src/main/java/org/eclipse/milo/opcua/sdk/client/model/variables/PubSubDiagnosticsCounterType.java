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
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubDiagnosticsCounterClassification;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.5">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.5</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface PubSubDiagnosticsCounterType extends BaseDataVariableType {
  QualifiedProperty<Boolean> ACTIVE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Active",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<PubSubDiagnosticsCounterClassification> CLASSIFICATION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Classification",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19730"),
          -1,
          PubSubDiagnosticsCounterClassification.class);

  QualifiedProperty<DiagnosticsLevel> DIAGNOSTICS_LEVEL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DiagnosticsLevel",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19723"),
          -1,
          DiagnosticsLevel.class);

  QualifiedProperty<DateTime> TIME_FIRST_CHANGE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "TimeFirstChange",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=13"),
          -1,
          DateTime.class);

  /** Gets the existing node's local value. */
  @Nullable Boolean getActive() throws UaException;

  /** Sets the existing node's local value. */
  void setActive(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readActive() throws UaException;

  /** Writes the value remotely. */
  void writeActive(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readActiveAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeActiveAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getActiveNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getActiveNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable PubSubDiagnosticsCounterClassification getClassification() throws UaException;

  /** Sets the existing node's local value. */
  void setClassification(@Nullable PubSubDiagnosticsCounterClassification value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable PubSubDiagnosticsCounterClassification readClassification() throws UaException;

  /** Writes the value remotely. */
  void writeClassification(@Nullable PubSubDiagnosticsCounterClassification value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable PubSubDiagnosticsCounterClassification>
      readClassificationAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeClassificationAsync(
      @Nullable PubSubDiagnosticsCounterClassification value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getClassificationNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getClassificationNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DiagnosticsLevel getDiagnosticsLevel() throws UaException;

  /** Sets the existing node's local value. */
  void setDiagnosticsLevel(@Nullable DiagnosticsLevel value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DiagnosticsLevel readDiagnosticsLevel() throws UaException;

  /** Writes the value remotely. */
  void writeDiagnosticsLevel(@Nullable DiagnosticsLevel value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DiagnosticsLevel> readDiagnosticsLevelAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDiagnosticsLevelAsync(@Nullable DiagnosticsLevel value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDiagnosticsLevelNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getDiagnosticsLevelNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DateTime getTimeFirstChange() throws UaException;

  /** Sets the existing node's local value. */
  void setTimeFirstChange(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readTimeFirstChange() throws UaException;

  /** Writes the value remotely. */
  void writeTimeFirstChange(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readTimeFirstChangeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeTimeFirstChangeAsync(@Nullable DateTime value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getTimeFirstChangeNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getTimeFirstChangeNodeAsync();
}
