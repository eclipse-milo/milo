/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.4">https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.4</a>
 */
public interface TransitionVariableType extends BaseDataVariableType {
  QualifiedProperty<Object> ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Id",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24"),
          -1,
          Object.class);

  QualifiedProperty<QualifiedName> NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Name",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20"),
          -1,
          QualifiedName.class);

  QualifiedProperty<UInteger> NUMBER =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Number",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

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

  /**
   * Get the local value of the Id Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Id Node.
   * @throws UaException if an error occurs creating or getting the Id Node.
   */
  Object getId() throws UaException;

  /**
   * Set the local value of the Id Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Id Node.
   * @throws UaException if an error occurs creating or getting the Id Node.
   */
  void setId(Object value) throws UaException;

  /**
   * Read the value of the Id Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link Object} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Object readId() throws UaException;

  /**
   * Write a new value for the Id Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link Object} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeId(Object value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<?> readIdAsync();

  /**
   * An asynchronous implementation of {@link #writeId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeIdAsync(Object value);

  /**
   * Get the Id {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Id {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getIdNodeAsync();

  /**
   * Get the local value of the Name Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Name Node.
   * @throws UaException if an error occurs creating or getting the Name Node.
   */
  QualifiedName getName() throws UaException;

  /**
   * Set the local value of the Name Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Name Node.
   * @throws UaException if an error occurs creating or getting the Name Node.
   */
  void setName(QualifiedName value) throws UaException;

  /**
   * Read the value of the Name Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link QualifiedName} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  QualifiedName readName() throws UaException;

  /**
   * Write a new value for the Name Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link QualifiedName} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeName(QualifiedName value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readName}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends QualifiedName> readNameAsync();

  /**
   * An asynchronous implementation of {@link #writeName}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeNameAsync(QualifiedName value);

  /**
   * Get the Name {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Name {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getNameNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getNameNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getNameNodeAsync();

  /**
   * Get the local value of the Number Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Number Node.
   * @throws UaException if an error occurs creating or getting the Number Node.
   */
  UInteger getNumber() throws UaException;

  /**
   * Set the local value of the Number Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Number Node.
   * @throws UaException if an error occurs creating or getting the Number Node.
   */
  void setNumber(UInteger value) throws UaException;

  /**
   * Read the value of the Number Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readNumber() throws UaException;

  /**
   * Write a new value for the Number Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeNumber(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readNumber}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readNumberAsync();

  /**
   * An asynchronous implementation of {@link #writeNumber}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeNumberAsync(UInteger value);

  /**
   * Get the Number {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Number {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getNumberNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getNumberNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getNumberNodeAsync();

  /**
   * Get the local value of the TransitionTime Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the TransitionTime Node.
   * @throws UaException if an error occurs creating or getting the TransitionTime Node.
   */
  DateTime getTransitionTime() throws UaException;

  /**
   * Set the local value of the TransitionTime Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the TransitionTime Node.
   * @throws UaException if an error occurs creating or getting the TransitionTime Node.
   */
  void setTransitionTime(DateTime value) throws UaException;

  /**
   * Read the value of the TransitionTime Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link DateTime} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DateTime readTransitionTime() throws UaException;

  /**
   * Write a new value for the TransitionTime Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link DateTime} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeTransitionTime(DateTime value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readTransitionTime}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DateTime> readTransitionTimeAsync();

  /**
   * An asynchronous implementation of {@link #writeTransitionTime}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeTransitionTimeAsync(DateTime value);

  /**
   * Get the TransitionTime {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TransitionTime {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getTransitionTimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTransitionTimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getTransitionTimeNodeAsync();

  /**
   * Get the local value of the EffectiveTransitionTime Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the EffectiveTransitionTime Node.
   * @throws UaException if an error occurs creating or getting the EffectiveTransitionTime Node.
   */
  DateTime getEffectiveTransitionTime() throws UaException;

  /**
   * Set the local value of the EffectiveTransitionTime Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the EffectiveTransitionTime Node.
   * @throws UaException if an error occurs creating or getting the EffectiveTransitionTime Node.
   */
  void setEffectiveTransitionTime(DateTime value) throws UaException;

  /**
   * Read the value of the EffectiveTransitionTime Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link DateTime} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DateTime readEffectiveTransitionTime() throws UaException;

  /**
   * Write a new value for the EffectiveTransitionTime Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link DateTime} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeEffectiveTransitionTime(DateTime value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readEffectiveTransitionTime}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DateTime> readEffectiveTransitionTimeAsync();

  /**
   * An asynchronous implementation of {@link #writeEffectiveTransitionTime}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeEffectiveTransitionTimeAsync(DateTime value);

  /**
   * Get the EffectiveTransitionTime {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the EffectiveTransitionTime {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getEffectiveTransitionTimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getEffectiveTransitionTimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getEffectiveTransitionTimeNodeAsync();
}
