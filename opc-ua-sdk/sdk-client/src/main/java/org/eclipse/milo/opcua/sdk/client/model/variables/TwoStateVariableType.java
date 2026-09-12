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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface TwoStateVariableType extends StateVariableType {
  QualifiedProperty<Boolean> ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Id",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<DateTime> TRANSITION_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "TransitionTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<DateTime> EFFECTIVE_TRANSITION_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EffectiveTransitionTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<LocalizedText> TRUE_STATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "TrueState",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          -1,
          LocalizedText.class);

  QualifiedProperty<LocalizedText> FALSE_STATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "FalseState",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          -1,
          LocalizedText.class);

  /** Gets the existing node's local value. */
  @Nullable Boolean getId() throws UaException;

  /** Sets the existing node's local value. */
  void setId(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readId() throws UaException;

  /** Writes the value remotely. */
  void writeId(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeIdAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DateTime getTransitionTime() throws UaException;

  /** Sets the existing node's local value. */
  void setTransitionTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readTransitionTime() throws UaException;

  /** Writes the value remotely. */
  void writeTransitionTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readTransitionTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeTransitionTimeAsync(@Nullable DateTime value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getTransitionTimeNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getTransitionTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DateTime getEffectiveTransitionTime() throws UaException;

  /** Sets the existing node's local value. */
  void setEffectiveTransitionTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readEffectiveTransitionTime() throws UaException;

  /** Writes the value remotely. */
  void writeEffectiveTransitionTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readEffectiveTransitionTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEffectiveTransitionTimeAsync(@Nullable DateTime value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getEffectiveTransitionTimeNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getEffectiveTransitionTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getTrueState() throws UaException;

  /** Sets the existing node's local value. */
  void setTrueState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readTrueState() throws UaException;

  /** Writes the value remotely. */
  void writeTrueState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readTrueStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeTrueStateAsync(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getTrueStateNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getTrueStateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getFalseState() throws UaException;

  /** Sets the existing node's local value. */
  void setFalseState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readFalseState() throws UaException;

  /** Writes the value remotely. */
  void writeFalseState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readFalseStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeFalseStateAsync(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getFalseStateNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getFalseStateNodeAsync();
}
