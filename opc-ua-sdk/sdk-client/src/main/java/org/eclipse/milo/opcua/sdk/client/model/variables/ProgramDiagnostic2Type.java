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
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.9">https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.9</a>
 */
public interface ProgramDiagnostic2Type extends BaseDataVariableType {
  QualifiedProperty<DateTime> LAST_TRANSITION_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastTransitionTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

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
   * Get the CreateSessionId {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CreateSessionId {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getCreateSessionIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCreateSessionIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCreateSessionIdNodeAsync();

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
   * Get the CreateClientName {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CreateClientName {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getCreateClientNameNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCreateClientNameNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCreateClientNameNodeAsync();

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
   * Get the InvocationCreationTime {@link BaseDataVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the InvocationCreationTime {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getInvocationCreationTimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getInvocationCreationTimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getInvocationCreationTimeNodeAsync();

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
   * Get the LastMethodCall {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LastMethodCall {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getLastMethodCallNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLastMethodCallNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getLastMethodCallNodeAsync();

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
   * Get the LastMethodSessionId {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LastMethodSessionId {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getLastMethodSessionIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLastMethodSessionIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getLastMethodSessionIdNodeAsync();

  /**
   * Get the local value of the LastMethodInputArguments Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LastMethodInputArguments Node.
   * @throws UaException if an error occurs creating or getting the LastMethodInputArguments Node.
   */
  Argument[] getLastMethodInputArguments() throws UaException;

  /**
   * Set the local value of the LastMethodInputArguments Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LastMethodInputArguments Node.
   * @throws UaException if an error occurs creating or getting the LastMethodInputArguments Node.
   */
  void setLastMethodInputArguments(Argument[] value) throws UaException;

  /**
   * Read the value of the LastMethodInputArguments Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link Argument[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Argument[] readLastMethodInputArguments() throws UaException;

  /**
   * Write a new value for the LastMethodInputArguments Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link Argument[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLastMethodInputArguments(Argument[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLastMethodInputArguments}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Argument[]> readLastMethodInputArgumentsAsync();

  /**
   * An asynchronous implementation of {@link #writeLastMethodInputArguments}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLastMethodInputArgumentsAsync(Argument[] value);

  /**
   * Get the LastMethodInputArguments {@link BaseDataVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LastMethodInputArguments {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getLastMethodInputArgumentsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLastMethodInputArgumentsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getLastMethodInputArgumentsNodeAsync();

  /**
   * Get the local value of the LastMethodOutputArguments Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LastMethodOutputArguments Node.
   * @throws UaException if an error occurs creating or getting the LastMethodOutputArguments Node.
   */
  Argument[] getLastMethodOutputArguments() throws UaException;

  /**
   * Set the local value of the LastMethodOutputArguments Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LastMethodOutputArguments Node.
   * @throws UaException if an error occurs creating or getting the LastMethodOutputArguments Node.
   */
  void setLastMethodOutputArguments(Argument[] value) throws UaException;

  /**
   * Read the value of the LastMethodOutputArguments Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link Argument[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Argument[] readLastMethodOutputArguments() throws UaException;

  /**
   * Write a new value for the LastMethodOutputArguments Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link Argument[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLastMethodOutputArguments(Argument[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLastMethodOutputArguments}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Argument[]> readLastMethodOutputArgumentsAsync();

  /**
   * An asynchronous implementation of {@link #writeLastMethodOutputArguments}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLastMethodOutputArgumentsAsync(Argument[] value);

  /**
   * Get the LastMethodOutputArguments {@link BaseDataVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LastMethodOutputArguments {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getLastMethodOutputArgumentsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLastMethodOutputArgumentsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getLastMethodOutputArgumentsNodeAsync();

  /**
   * Get the local value of the LastMethodInputValues Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LastMethodInputValues Node.
   * @throws UaException if an error occurs creating or getting the LastMethodInputValues Node.
   */
  Object[] getLastMethodInputValues() throws UaException;

  /**
   * Set the local value of the LastMethodInputValues Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LastMethodInputValues Node.
   * @throws UaException if an error occurs creating or getting the LastMethodInputValues Node.
   */
  void setLastMethodInputValues(Object[] value) throws UaException;

  /**
   * Read the value of the LastMethodInputValues Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link Object[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Object[] readLastMethodInputValues() throws UaException;

  /**
   * Write a new value for the LastMethodInputValues Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link Object[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLastMethodInputValues(Object[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLastMethodInputValues}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Object[]> readLastMethodInputValuesAsync();

  /**
   * An asynchronous implementation of {@link #writeLastMethodInputValues}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLastMethodInputValuesAsync(Object[] value);

  /**
   * Get the LastMethodInputValues {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LastMethodInputValues {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getLastMethodInputValuesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLastMethodInputValuesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getLastMethodInputValuesNodeAsync();

  /**
   * Get the local value of the LastMethodOutputValues Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LastMethodOutputValues Node.
   * @throws UaException if an error occurs creating or getting the LastMethodOutputValues Node.
   */
  Object[] getLastMethodOutputValues() throws UaException;

  /**
   * Set the local value of the LastMethodOutputValues Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LastMethodOutputValues Node.
   * @throws UaException if an error occurs creating or getting the LastMethodOutputValues Node.
   */
  void setLastMethodOutputValues(Object[] value) throws UaException;

  /**
   * Read the value of the LastMethodOutputValues Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link Object[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Object[] readLastMethodOutputValues() throws UaException;

  /**
   * Write a new value for the LastMethodOutputValues Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link Object[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLastMethodOutputValues(Object[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLastMethodOutputValues}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Object[]> readLastMethodOutputValuesAsync();

  /**
   * An asynchronous implementation of {@link #writeLastMethodOutputValues}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLastMethodOutputValuesAsync(Object[] value);

  /**
   * Get the LastMethodOutputValues {@link BaseDataVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LastMethodOutputValues {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getLastMethodOutputValuesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLastMethodOutputValuesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getLastMethodOutputValuesNodeAsync();

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
   * Get the LastMethodCallTime {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LastMethodCallTime {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getLastMethodCallTimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLastMethodCallTimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getLastMethodCallTimeNodeAsync();

  /**
   * Get the local value of the LastMethodReturnStatus Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LastMethodReturnStatus Node.
   * @throws UaException if an error occurs creating or getting the LastMethodReturnStatus Node.
   */
  StatusCode getLastMethodReturnStatus() throws UaException;

  /**
   * Set the local value of the LastMethodReturnStatus Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LastMethodReturnStatus Node.
   * @throws UaException if an error occurs creating or getting the LastMethodReturnStatus Node.
   */
  void setLastMethodReturnStatus(StatusCode value) throws UaException;

  /**
   * Read the value of the LastMethodReturnStatus Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link StatusCode} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  StatusCode readLastMethodReturnStatus() throws UaException;

  /**
   * Write a new value for the LastMethodReturnStatus Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link StatusCode} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLastMethodReturnStatus(StatusCode value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLastMethodReturnStatus}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends StatusCode> readLastMethodReturnStatusAsync();

  /**
   * An asynchronous implementation of {@link #writeLastMethodReturnStatus}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLastMethodReturnStatusAsync(StatusCode value);

  /**
   * Get the LastMethodReturnStatus {@link BaseDataVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LastMethodReturnStatus {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getLastMethodReturnStatusNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLastMethodReturnStatusNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getLastMethodReturnStatusNodeAsync();
}
