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
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.StatusResult;

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

  /**
   * Get the local value of the CreateSessionId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CreateSessionId Node.
   * @throws UaException if an error occurs creating or getting the CreateSessionId Node.
   */
  NodeId getCreateSessionId() throws UaException;

  /**
   * Set the local value of the CreateSessionId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CreateSessionId Node.
   * @throws UaException if an error occurs creating or getting the CreateSessionId Node.
   */
  void setCreateSessionId(NodeId value) throws UaException;

  /**
   * Read the value of the CreateSessionId Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link NodeId} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId readCreateSessionId() throws UaException;

  /**
   * Write a new value for the CreateSessionId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link NodeId} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCreateSessionId(NodeId value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCreateSessionId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId> readCreateSessionIdAsync();

  /**
   * An asynchronous implementation of {@link #writeCreateSessionId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCreateSessionIdAsync(NodeId value);

  /**
   * Get the CreateSessionId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CreateSessionId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getCreateSessionIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCreateSessionIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getCreateSessionIdNodeAsync();

  /**
   * Get the local value of the CreateClientName Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CreateClientName Node.
   * @throws UaException if an error occurs creating or getting the CreateClientName Node.
   */
  String getCreateClientName() throws UaException;

  /**
   * Set the local value of the CreateClientName Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CreateClientName Node.
   * @throws UaException if an error occurs creating or getting the CreateClientName Node.
   */
  void setCreateClientName(String value) throws UaException;

  /**
   * Read the value of the CreateClientName Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readCreateClientName() throws UaException;

  /**
   * Write a new value for the CreateClientName Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCreateClientName(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCreateClientName}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readCreateClientNameAsync();

  /**
   * An asynchronous implementation of {@link #writeCreateClientName}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCreateClientNameAsync(String value);

  /**
   * Get the CreateClientName {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CreateClientName {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getCreateClientNameNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCreateClientNameNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getCreateClientNameNodeAsync();

  /**
   * Get the local value of the InvocationCreationTime Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the InvocationCreationTime Node.
   * @throws UaException if an error occurs creating or getting the InvocationCreationTime Node.
   */
  DateTime getInvocationCreationTime() throws UaException;

  /**
   * Set the local value of the InvocationCreationTime Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the InvocationCreationTime Node.
   * @throws UaException if an error occurs creating or getting the InvocationCreationTime Node.
   */
  void setInvocationCreationTime(DateTime value) throws UaException;

  /**
   * Read the value of the InvocationCreationTime Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link DateTime} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DateTime readInvocationCreationTime() throws UaException;

  /**
   * Write a new value for the InvocationCreationTime Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link DateTime} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeInvocationCreationTime(DateTime value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readInvocationCreationTime}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DateTime> readInvocationCreationTimeAsync();

  /**
   * An asynchronous implementation of {@link #writeInvocationCreationTime}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeInvocationCreationTimeAsync(DateTime value);

  /**
   * Get the InvocationCreationTime {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the InvocationCreationTime {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getInvocationCreationTimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getInvocationCreationTimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getInvocationCreationTimeNodeAsync();

  /**
   * Get the local value of the LastTransitionTime Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LastTransitionTime Node.
   * @throws UaException if an error occurs creating or getting the LastTransitionTime Node.
   */
  DateTime getLastTransitionTime() throws UaException;

  /**
   * Set the local value of the LastTransitionTime Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LastTransitionTime Node.
   * @throws UaException if an error occurs creating or getting the LastTransitionTime Node.
   */
  void setLastTransitionTime(DateTime value) throws UaException;

  /**
   * Read the value of the LastTransitionTime Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link DateTime} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DateTime readLastTransitionTime() throws UaException;

  /**
   * Write a new value for the LastTransitionTime Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link DateTime} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLastTransitionTime(DateTime value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLastTransitionTime}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DateTime> readLastTransitionTimeAsync();

  /**
   * An asynchronous implementation of {@link #writeLastTransitionTime}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLastTransitionTimeAsync(DateTime value);

  /**
   * Get the LastTransitionTime {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LastTransitionTime {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getLastTransitionTimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLastTransitionTimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getLastTransitionTimeNodeAsync();

  /**
   * Get the local value of the LastMethodCall Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LastMethodCall Node.
   * @throws UaException if an error occurs creating or getting the LastMethodCall Node.
   */
  String getLastMethodCall() throws UaException;

  /**
   * Set the local value of the LastMethodCall Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LastMethodCall Node.
   * @throws UaException if an error occurs creating or getting the LastMethodCall Node.
   */
  void setLastMethodCall(String value) throws UaException;

  /**
   * Read the value of the LastMethodCall Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readLastMethodCall() throws UaException;

  /**
   * Write a new value for the LastMethodCall Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLastMethodCall(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLastMethodCall}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readLastMethodCallAsync();

  /**
   * An asynchronous implementation of {@link #writeLastMethodCall}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLastMethodCallAsync(String value);

  /**
   * Get the LastMethodCall {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LastMethodCall {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getLastMethodCallNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLastMethodCallNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getLastMethodCallNodeAsync();

  /**
   * Get the local value of the LastMethodSessionId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LastMethodSessionId Node.
   * @throws UaException if an error occurs creating or getting the LastMethodSessionId Node.
   */
  NodeId getLastMethodSessionId() throws UaException;

  /**
   * Set the local value of the LastMethodSessionId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LastMethodSessionId Node.
   * @throws UaException if an error occurs creating or getting the LastMethodSessionId Node.
   */
  void setLastMethodSessionId(NodeId value) throws UaException;

  /**
   * Read the value of the LastMethodSessionId Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link NodeId} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId readLastMethodSessionId() throws UaException;

  /**
   * Write a new value for the LastMethodSessionId Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link NodeId} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLastMethodSessionId(NodeId value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLastMethodSessionId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId> readLastMethodSessionIdAsync();

  /**
   * An asynchronous implementation of {@link #writeLastMethodSessionId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLastMethodSessionIdAsync(NodeId value);

  /**
   * Get the LastMethodSessionId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LastMethodSessionId {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getLastMethodSessionIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLastMethodSessionIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getLastMethodSessionIdNodeAsync();

  /**
   * Get the local value of the LastMethodInputArguments Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LastMethodInputArguments Node.
   * @throws UaException if an error occurs creating or getting the LastMethodInputArguments Node.
   */
  Object[] getLastMethodInputArguments() throws UaException;

  /**
   * Set the local value of the LastMethodInputArguments Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LastMethodInputArguments Node.
   * @throws UaException if an error occurs creating or getting the LastMethodInputArguments Node.
   */
  void setLastMethodInputArguments(Object[] value) throws UaException;

  /**
   * Read the value of the LastMethodInputArguments Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link Object[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Object[] readLastMethodInputArguments() throws UaException;

  /**
   * Write a new value for the LastMethodInputArguments Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link Object[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLastMethodInputArguments(Object[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLastMethodInputArguments}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Object[]> readLastMethodInputArgumentsAsync();

  /**
   * An asynchronous implementation of {@link #writeLastMethodInputArguments}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLastMethodInputArgumentsAsync(Object[] value);

  /**
   * Get the LastMethodInputArguments {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LastMethodInputArguments {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getLastMethodInputArgumentsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLastMethodInputArgumentsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getLastMethodInputArgumentsNodeAsync();

  /**
   * Get the local value of the LastMethodOutputArguments Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LastMethodOutputArguments Node.
   * @throws UaException if an error occurs creating or getting the LastMethodOutputArguments Node.
   */
  Object[] getLastMethodOutputArguments() throws UaException;

  /**
   * Set the local value of the LastMethodOutputArguments Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LastMethodOutputArguments Node.
   * @throws UaException if an error occurs creating or getting the LastMethodOutputArguments Node.
   */
  void setLastMethodOutputArguments(Object[] value) throws UaException;

  /**
   * Read the value of the LastMethodOutputArguments Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link Object[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Object[] readLastMethodOutputArguments() throws UaException;

  /**
   * Write a new value for the LastMethodOutputArguments Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link Object[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLastMethodOutputArguments(Object[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLastMethodOutputArguments}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Object[]> readLastMethodOutputArgumentsAsync();

  /**
   * An asynchronous implementation of {@link #writeLastMethodOutputArguments}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLastMethodOutputArgumentsAsync(Object[] value);

  /**
   * Get the LastMethodOutputArguments {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LastMethodOutputArguments {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getLastMethodOutputArgumentsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLastMethodOutputArgumentsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getLastMethodOutputArgumentsNodeAsync();

  /**
   * Get the local value of the LastMethodCallTime Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LastMethodCallTime Node.
   * @throws UaException if an error occurs creating or getting the LastMethodCallTime Node.
   */
  DateTime getLastMethodCallTime() throws UaException;

  /**
   * Set the local value of the LastMethodCallTime Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LastMethodCallTime Node.
   * @throws UaException if an error occurs creating or getting the LastMethodCallTime Node.
   */
  void setLastMethodCallTime(DateTime value) throws UaException;

  /**
   * Read the value of the LastMethodCallTime Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link DateTime} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DateTime readLastMethodCallTime() throws UaException;

  /**
   * Write a new value for the LastMethodCallTime Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link DateTime} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLastMethodCallTime(DateTime value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLastMethodCallTime}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DateTime> readLastMethodCallTimeAsync();

  /**
   * An asynchronous implementation of {@link #writeLastMethodCallTime}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLastMethodCallTimeAsync(DateTime value);

  /**
   * Get the LastMethodCallTime {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LastMethodCallTime {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getLastMethodCallTimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLastMethodCallTimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getLastMethodCallTimeNodeAsync();

  /**
   * Get the local value of the LastMethodReturnStatus Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LastMethodReturnStatus Node.
   * @throws UaException if an error occurs creating or getting the LastMethodReturnStatus Node.
   */
  StatusResult getLastMethodReturnStatus() throws UaException;

  /**
   * Set the local value of the LastMethodReturnStatus Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LastMethodReturnStatus Node.
   * @throws UaException if an error occurs creating or getting the LastMethodReturnStatus Node.
   */
  void setLastMethodReturnStatus(StatusResult value) throws UaException;

  /**
   * Read the value of the LastMethodReturnStatus Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link StatusResult} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  StatusResult readLastMethodReturnStatus() throws UaException;

  /**
   * Write a new value for the LastMethodReturnStatus Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link StatusResult} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLastMethodReturnStatus(StatusResult value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLastMethodReturnStatus}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends StatusResult> readLastMethodReturnStatusAsync();

  /**
   * An asynchronous implementation of {@link #writeLastMethodReturnStatus}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLastMethodReturnStatusAsync(StatusResult value);

  /**
   * Get the LastMethodReturnStatus {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LastMethodReturnStatus {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getLastMethodReturnStatusNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLastMethodReturnStatusNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getLastMethodReturnStatusNodeAsync();
}
