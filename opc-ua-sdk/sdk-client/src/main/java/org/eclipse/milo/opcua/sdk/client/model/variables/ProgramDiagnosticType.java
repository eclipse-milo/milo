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
import org.eclipse.milo.opcua.stack.core.types.structured.StatusResult;
import org.jspecify.annotations.Nullable;

/**
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ProgramDiagnosticType extends BaseDataVariableType {
  QualifiedProperty<NodeId> CREATE_SESSION_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CreateSessionId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<String> CREATE_CLIENT_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CreateClientName",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<DateTime> INVOCATION_CREATION_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InvocationCreationTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<DateTime> LAST_TRANSITION_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastTransitionTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<String> LAST_METHOD_CALL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastMethodCall",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<NodeId> LAST_METHOD_SESSION_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastMethodSessionId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<Object[]> LAST_METHOD_INPUT_ARGUMENTS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastMethodInputArguments",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24"),
          1,
          Object[].class);

  QualifiedProperty<Object[]> LAST_METHOD_OUTPUT_ARGUMENTS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastMethodOutputArguments",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24"),
          1,
          Object[].class);

  QualifiedProperty<DateTime> LAST_METHOD_CALL_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastMethodCallTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<StatusResult> LAST_METHOD_RETURN_STATUS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastMethodReturnStatus",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=299"),
          -1,
          StatusResult.class);

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
  PropertyType getCreateSessionIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getCreateSessionIdNodeAsync();

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
  PropertyType getCreateClientNameNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getCreateClientNameNodeAsync();

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
  PropertyType getInvocationCreationTimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getInvocationCreationTimeNodeAsync();

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
  PropertyType getLastMethodCallNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getLastMethodCallNodeAsync();

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
  PropertyType getLastMethodSessionIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getLastMethodSessionIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Object @Nullable [] getLastMethodInputArguments() throws UaException;

  /** Sets the existing node's local value. */
  void setLastMethodInputArguments(@Nullable Object @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Object @Nullable [] readLastMethodInputArguments() throws UaException;

  /** Writes the value remotely. */
  void writeLastMethodInputArguments(@Nullable Object @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Object @Nullable []> readLastMethodInputArgumentsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLastMethodInputArgumentsAsync(
      @Nullable Object @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLastMethodInputArgumentsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getLastMethodInputArgumentsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Object @Nullable [] getLastMethodOutputArguments() throws UaException;

  /** Sets the existing node's local value. */
  void setLastMethodOutputArguments(@Nullable Object @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Object @Nullable [] readLastMethodOutputArguments() throws UaException;

  /** Writes the value remotely. */
  void writeLastMethodOutputArguments(@Nullable Object @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Object @Nullable []> readLastMethodOutputArgumentsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLastMethodOutputArgumentsAsync(
      @Nullable Object @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLastMethodOutputArgumentsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getLastMethodOutputArgumentsNodeAsync();

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
  PropertyType getLastMethodCallTimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getLastMethodCallTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable StatusResult getLastMethodReturnStatus() throws UaException;

  /** Sets the existing node's local value. */
  void setLastMethodReturnStatus(@Nullable StatusResult value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable StatusResult readLastMethodReturnStatus() throws UaException;

  /** Writes the value remotely. */
  void writeLastMethodReturnStatus(@Nullable StatusResult value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable StatusResult> readLastMethodReturnStatusAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLastMethodReturnStatusAsync(@Nullable StatusResult value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLastMethodReturnStatusNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getLastMethodReturnStatusNodeAsync();
}
