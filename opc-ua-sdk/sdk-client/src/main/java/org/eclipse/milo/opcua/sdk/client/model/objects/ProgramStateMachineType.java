/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.FiniteStateVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.FiniteTransitionVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.ProgramDiagnostic2Type;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ProgramDiagnostic2DataType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.1</a>
 */
public interface ProgramStateMachineType extends FiniteStateMachineType {
  QualifiedProperty<Boolean> CREATABLE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Creatable",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> DELETABLE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Deletable",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> AUTO_DELETE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AutoDelete",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Integer> RECYCLE_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "RecycleCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=6"),
          -1,
          Integer.class);

  QualifiedProperty<UInteger> INSTANCE_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InstanceCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_INSTANCE_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxInstanceCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_RECYCLE_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxRecycleCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  /**
   * Get the local value of the Creatable Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Creatable Node.
   * @throws UaException if an error occurs creating or getting the Creatable Node.
   */
  Boolean getCreatable() throws UaException;

  /**
   * Set the local value of the Creatable Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Creatable Node.
   * @throws UaException if an error occurs creating or getting the Creatable Node.
   */
  void setCreatable(Boolean value) throws UaException;

  /**
   * Read the value of the Creatable Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readCreatable() throws UaException;

  /**
   * Write a new value for the Creatable Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCreatable(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCreatable}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readCreatableAsync();

  /**
   * An asynchronous implementation of {@link #writeCreatable}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCreatableAsync(Boolean value);

  /**
   * Get the Creatable {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Creatable {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getCreatableNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCreatableNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getCreatableNodeAsync();

  /**
   * Get the local value of the Deletable Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Deletable Node.
   * @throws UaException if an error occurs creating or getting the Deletable Node.
   */
  Boolean getDeletable() throws UaException;

  /**
   * Set the local value of the Deletable Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Deletable Node.
   * @throws UaException if an error occurs creating or getting the Deletable Node.
   */
  void setDeletable(Boolean value) throws UaException;

  /**
   * Read the value of the Deletable Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readDeletable() throws UaException;

  /**
   * Write a new value for the Deletable Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDeletable(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDeletable}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readDeletableAsync();

  /**
   * An asynchronous implementation of {@link #writeDeletable}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDeletableAsync(Boolean value);

  /**
   * Get the Deletable {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Deletable {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDeletableNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDeletableNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDeletableNodeAsync();

  /**
   * Get the local value of the AutoDelete Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the AutoDelete Node.
   * @throws UaException if an error occurs creating or getting the AutoDelete Node.
   */
  Boolean getAutoDelete() throws UaException;

  /**
   * Set the local value of the AutoDelete Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the AutoDelete Node.
   * @throws UaException if an error occurs creating or getting the AutoDelete Node.
   */
  void setAutoDelete(Boolean value) throws UaException;

  /**
   * Read the value of the AutoDelete Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readAutoDelete() throws UaException;

  /**
   * Write a new value for the AutoDelete Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeAutoDelete(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readAutoDelete}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readAutoDeleteAsync();

  /**
   * An asynchronous implementation of {@link #writeAutoDelete}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeAutoDeleteAsync(Boolean value);

  /**
   * Get the AutoDelete {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AutoDelete {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getAutoDeleteNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAutoDeleteNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getAutoDeleteNodeAsync();

  /**
   * Get the local value of the RecycleCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the RecycleCount Node.
   * @throws UaException if an error occurs creating or getting the RecycleCount Node.
   */
  Integer getRecycleCount() throws UaException;

  /**
   * Set the local value of the RecycleCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the RecycleCount Node.
   * @throws UaException if an error occurs creating or getting the RecycleCount Node.
   */
  void setRecycleCount(Integer value) throws UaException;

  /**
   * Read the value of the RecycleCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Integer} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Integer readRecycleCount() throws UaException;

  /**
   * Write a new value for the RecycleCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Integer} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeRecycleCount(Integer value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readRecycleCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Integer> readRecycleCountAsync();

  /**
   * An asynchronous implementation of {@link #writeRecycleCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeRecycleCountAsync(Integer value);

  /**
   * Get the RecycleCount {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RecycleCount {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getRecycleCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRecycleCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getRecycleCountNodeAsync();

  /**
   * Get the local value of the InstanceCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the InstanceCount Node.
   * @throws UaException if an error occurs creating or getting the InstanceCount Node.
   */
  UInteger getInstanceCount() throws UaException;

  /**
   * Set the local value of the InstanceCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the InstanceCount Node.
   * @throws UaException if an error occurs creating or getting the InstanceCount Node.
   */
  void setInstanceCount(UInteger value) throws UaException;

  /**
   * Read the value of the InstanceCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readInstanceCount() throws UaException;

  /**
   * Write a new value for the InstanceCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeInstanceCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readInstanceCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readInstanceCountAsync();

  /**
   * An asynchronous implementation of {@link #writeInstanceCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeInstanceCountAsync(UInteger value);

  /**
   * Get the InstanceCount {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the InstanceCount {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getInstanceCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getInstanceCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getInstanceCountNodeAsync();

  /**
   * Get the local value of the MaxInstanceCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxInstanceCount Node.
   * @throws UaException if an error occurs creating or getting the MaxInstanceCount Node.
   */
  UInteger getMaxInstanceCount() throws UaException;

  /**
   * Set the local value of the MaxInstanceCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxInstanceCount Node.
   * @throws UaException if an error occurs creating or getting the MaxInstanceCount Node.
   */
  void setMaxInstanceCount(UInteger value) throws UaException;

  /**
   * Read the value of the MaxInstanceCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxInstanceCount() throws UaException;

  /**
   * Write a new value for the MaxInstanceCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxInstanceCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxInstanceCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxInstanceCountAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxInstanceCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxInstanceCountAsync(UInteger value);

  /**
   * Get the MaxInstanceCount {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxInstanceCount {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxInstanceCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxInstanceCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxInstanceCountNodeAsync();

  /**
   * Get the local value of the MaxRecycleCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxRecycleCount Node.
   * @throws UaException if an error occurs creating or getting the MaxRecycleCount Node.
   */
  UInteger getMaxRecycleCount() throws UaException;

  /**
   * Set the local value of the MaxRecycleCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxRecycleCount Node.
   * @throws UaException if an error occurs creating or getting the MaxRecycleCount Node.
   */
  void setMaxRecycleCount(UInteger value) throws UaException;

  /**
   * Read the value of the MaxRecycleCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxRecycleCount() throws UaException;

  /**
   * Write a new value for the MaxRecycleCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxRecycleCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxRecycleCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxRecycleCountAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxRecycleCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxRecycleCountAsync(UInteger value);

  /**
   * Get the MaxRecycleCount {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxRecycleCount {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxRecycleCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxRecycleCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxRecycleCountNodeAsync();

  /**
   * Get the local value of the CurrentState Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CurrentState Node.
   * @throws UaException if an error occurs creating or getting the CurrentState Node.
   */
  LocalizedText getCurrentState() throws UaException;

  /**
   * Set the local value of the CurrentState Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CurrentState Node.
   * @throws UaException if an error occurs creating or getting the CurrentState Node.
   */
  void setCurrentState(LocalizedText value) throws UaException;

  /**
   * Read the value of the CurrentState Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readCurrentState() throws UaException;

  /**
   * Write a new value for the CurrentState Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCurrentState(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCurrentState}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readCurrentStateAsync();

  /**
   * An asynchronous implementation of {@link #writeCurrentState}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCurrentStateAsync(LocalizedText value);

  /**
   * Get the CurrentState {@link FiniteStateVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CurrentState {@link FiniteStateVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  FiniteStateVariableType getCurrentStateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCurrentStateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the FiniteStateVariableType Node
   *     or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends FiniteStateVariableType> getCurrentStateNodeAsync();

  /**
   * Get the local value of the LastTransition Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LastTransition Node.
   * @throws UaException if an error occurs creating or getting the LastTransition Node.
   */
  LocalizedText getLastTransition() throws UaException;

  /**
   * Set the local value of the LastTransition Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LastTransition Node.
   * @throws UaException if an error occurs creating or getting the LastTransition Node.
   */
  void setLastTransition(LocalizedText value) throws UaException;

  /**
   * Read the value of the LastTransition Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readLastTransition() throws UaException;

  /**
   * Write a new value for the LastTransition Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLastTransition(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLastTransition}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readLastTransitionAsync();

  /**
   * An asynchronous implementation of {@link #writeLastTransition}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLastTransitionAsync(LocalizedText value);

  /**
   * Get the LastTransition {@link FiniteTransitionVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LastTransition {@link FiniteTransitionVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  FiniteTransitionVariableType getLastTransitionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLastTransitionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the FiniteTransitionVariableType
   *     Node or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends FiniteTransitionVariableType> getLastTransitionNodeAsync();

  /**
   * Get the local value of the ProgramDiagnostic Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ProgramDiagnostic Node.
   * @throws UaException if an error occurs creating or getting the ProgramDiagnostic Node.
   */
  ProgramDiagnostic2DataType getProgramDiagnostic() throws UaException;

  /**
   * Set the local value of the ProgramDiagnostic Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ProgramDiagnostic Node.
   * @throws UaException if an error occurs creating or getting the ProgramDiagnostic Node.
   */
  void setProgramDiagnostic(ProgramDiagnostic2DataType value) throws UaException;

  /**
   * Read the value of the ProgramDiagnostic Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ProgramDiagnostic2DataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ProgramDiagnostic2DataType readProgramDiagnostic() throws UaException;

  /**
   * Write a new value for the ProgramDiagnostic Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link ProgramDiagnostic2DataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeProgramDiagnostic(ProgramDiagnostic2DataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readProgramDiagnostic}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ProgramDiagnostic2DataType> readProgramDiagnosticAsync();

  /**
   * An asynchronous implementation of {@link #writeProgramDiagnostic}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeProgramDiagnosticAsync(ProgramDiagnostic2DataType value);

  /**
   * Get the ProgramDiagnostic {@link ProgramDiagnostic2Type} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ProgramDiagnostic {@link ProgramDiagnostic2Type} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  ProgramDiagnostic2Type getProgramDiagnosticNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getProgramDiagnosticNode()}.
   *
   * @return a CompletableFuture that completes successfully with the ProgramDiagnostic2Type Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends ProgramDiagnostic2Type> getProgramDiagnosticNodeAsync();

  /**
   * Get the FinalResultData {@link BaseObjectType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the FinalResultData {@link BaseObjectType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseObjectType getFinalResultDataNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getFinalResultDataNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseObjectType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseObjectType> getFinalResultDataNodeAsync();

  /**
   * Get the Halted {@link StateType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Halted {@link StateType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  StateType getHaltedNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getHaltedNode()}.
   *
   * @return a CompletableFuture that completes successfully with the StateType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends StateType> getHaltedNodeAsync();

  /**
   * Get the Ready {@link StateType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Ready {@link StateType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  StateType getReadyNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getReadyNode()}.
   *
   * @return a CompletableFuture that completes successfully with the StateType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends StateType> getReadyNodeAsync();

  /**
   * Get the Running {@link StateType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Running {@link StateType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  StateType getRunningNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRunningNode()}.
   *
   * @return a CompletableFuture that completes successfully with the StateType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends StateType> getRunningNodeAsync();

  /**
   * Get the Suspended {@link StateType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Suspended {@link StateType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  StateType getSuspendedNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSuspendedNode()}.
   *
   * @return a CompletableFuture that completes successfully with the StateType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends StateType> getSuspendedNodeAsync();

  /**
   * Get the HaltedToReady {@link TransitionType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the HaltedToReady {@link TransitionType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getHaltedToReadyNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getHaltedToReadyNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getHaltedToReadyNodeAsync();

  /**
   * Get the ReadyToRunning {@link TransitionType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ReadyToRunning {@link TransitionType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getReadyToRunningNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getReadyToRunningNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getReadyToRunningNodeAsync();

  /**
   * Get the RunningToHalted {@link TransitionType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RunningToHalted {@link TransitionType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getRunningToHaltedNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRunningToHaltedNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getRunningToHaltedNodeAsync();

  /**
   * Get the RunningToReady {@link TransitionType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RunningToReady {@link TransitionType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getRunningToReadyNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRunningToReadyNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getRunningToReadyNodeAsync();

  /**
   * Get the RunningToSuspended {@link TransitionType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RunningToSuspended {@link TransitionType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getRunningToSuspendedNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRunningToSuspendedNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getRunningToSuspendedNodeAsync();

  /**
   * Get the SuspendedToRunning {@link TransitionType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SuspendedToRunning {@link TransitionType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getSuspendedToRunningNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSuspendedToRunningNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getSuspendedToRunningNodeAsync();

  /**
   * Get the SuspendedToHalted {@link TransitionType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SuspendedToHalted {@link TransitionType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getSuspendedToHaltedNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSuspendedToHaltedNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getSuspendedToHaltedNodeAsync();

  /**
   * Get the SuspendedToReady {@link TransitionType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SuspendedToReady {@link TransitionType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getSuspendedToReadyNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSuspendedToReadyNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getSuspendedToReadyNodeAsync();

  /**
   * Get the ReadyToHalted {@link TransitionType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ReadyToHalted {@link TransitionType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getReadyToHaltedNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getReadyToHaltedNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getReadyToHaltedNodeAsync();
}
