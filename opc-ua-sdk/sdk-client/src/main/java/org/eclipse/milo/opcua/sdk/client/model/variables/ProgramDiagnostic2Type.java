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
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.9">https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.9</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ProgramDiagnostic2Type extends BaseDataVariableType {
  QualifiedProperty<DateTime> LAST_TRANSITION_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastTransitionTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  /** Gets the existing node's local value. */
  @Nullable DateTime getLastTransitionTime() throws UaException;

  /** Sets the existing node's local value. */
  void setLastTransitionTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readLastTransitionTime() throws UaException;

  /** Writes the value remotely. */
  void writeLastTransitionTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readLastTransitionTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLastTransitionTimeAsync(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLastTransitionTimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getLastTransitionTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable NodeId getCreateSessionId() throws UaException;

  /** Sets the existing node's local value. */
  void setCreateSessionId(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId readCreateSessionId() throws UaException;

  /** Writes the value remotely. */
  void writeCreateSessionId(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId> readCreateSessionIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCreateSessionIdAsync(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCreateSessionIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCreateSessionIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getCreateClientName() throws UaException;

  /** Sets the existing node's local value. */
  void setCreateClientName(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readCreateClientName() throws UaException;

  /** Writes the value remotely. */
  void writeCreateClientName(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readCreateClientNameAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCreateClientNameAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCreateClientNameNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCreateClientNameNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DateTime getInvocationCreationTime() throws UaException;

  /** Sets the existing node's local value. */
  void setInvocationCreationTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readInvocationCreationTime() throws UaException;

  /** Writes the value remotely. */
  void writeInvocationCreationTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readInvocationCreationTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeInvocationCreationTimeAsync(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getInvocationCreationTimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getInvocationCreationTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getLastMethodCall() throws UaException;

  /** Sets the existing node's local value. */
  void setLastMethodCall(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readLastMethodCall() throws UaException;

  /** Writes the value remotely. */
  void writeLastMethodCall(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readLastMethodCallAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLastMethodCallAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getLastMethodCallNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getLastMethodCallNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable NodeId getLastMethodSessionId() throws UaException;

  /** Sets the existing node's local value. */
  void setLastMethodSessionId(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId readLastMethodSessionId() throws UaException;

  /** Writes the value remotely. */
  void writeLastMethodSessionId(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId> readLastMethodSessionIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLastMethodSessionIdAsync(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getLastMethodSessionIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getLastMethodSessionIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Argument @Nullable [] getLastMethodInputArguments() throws UaException;

  /** Sets the existing node's local value. */
  void setLastMethodInputArguments(@Nullable Argument @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Argument @Nullable [] readLastMethodInputArguments() throws UaException;

  /** Writes the value remotely. */
  void writeLastMethodInputArguments(@Nullable Argument @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Argument @Nullable []> readLastMethodInputArgumentsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLastMethodInputArgumentsAsync(
      @Nullable Argument @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getLastMethodInputArgumentsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getLastMethodInputArgumentsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Argument @Nullable [] getLastMethodOutputArguments() throws UaException;

  /** Sets the existing node's local value. */
  void setLastMethodOutputArguments(@Nullable Argument @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Argument @Nullable [] readLastMethodOutputArguments() throws UaException;

  /** Writes the value remotely. */
  void writeLastMethodOutputArguments(@Nullable Argument @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Argument @Nullable []> readLastMethodOutputArgumentsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLastMethodOutputArgumentsAsync(
      @Nullable Argument @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getLastMethodOutputArgumentsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getLastMethodOutputArgumentsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Object @Nullable [] getLastMethodInputValues() throws UaException;

  /** Sets the existing node's local value. */
  void setLastMethodInputValues(@Nullable Object @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Object @Nullable [] readLastMethodInputValues() throws UaException;

  /** Writes the value remotely. */
  void writeLastMethodInputValues(@Nullable Object @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Object @Nullable []> readLastMethodInputValuesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLastMethodInputValuesAsync(
      @Nullable Object @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getLastMethodInputValuesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getLastMethodInputValuesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Object @Nullable [] getLastMethodOutputValues() throws UaException;

  /** Sets the existing node's local value. */
  void setLastMethodOutputValues(@Nullable Object @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Object @Nullable [] readLastMethodOutputValues() throws UaException;

  /** Writes the value remotely. */
  void writeLastMethodOutputValues(@Nullable Object @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Object @Nullable []> readLastMethodOutputValuesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLastMethodOutputValuesAsync(
      @Nullable Object @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getLastMethodOutputValuesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getLastMethodOutputValuesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DateTime getLastMethodCallTime() throws UaException;

  /** Sets the existing node's local value. */
  void setLastMethodCallTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readLastMethodCallTime() throws UaException;

  /** Writes the value remotely. */
  void writeLastMethodCallTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readLastMethodCallTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLastMethodCallTimeAsync(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getLastMethodCallTimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getLastMethodCallTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable StatusCode getLastMethodReturnStatus() throws UaException;

  /** Sets the existing node's local value. */
  void setLastMethodReturnStatus(@Nullable StatusCode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable StatusCode readLastMethodReturnStatus() throws UaException;

  /** Writes the value remotely. */
  void writeLastMethodReturnStatus(@Nullable StatusCode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable StatusCode> readLastMethodReturnStatusAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLastMethodReturnStatusAsync(@Nullable StatusCode value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getLastMethodReturnStatusNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getLastMethodReturnStatusNodeAsync();
}
