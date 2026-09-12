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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
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

  /** Gets the existing node's local value. */
  @Nullable Boolean getCreatable() throws UaException;

  /** Sets the existing node's local value. */
  void setCreatable(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readCreatable() throws UaException;

  /** Writes the value remotely. */
  void writeCreatable(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readCreatableAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCreatableAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getCreatableNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getCreatableNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getDeletable() throws UaException;

  /** Sets the existing node's local value. */
  void setDeletable(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readDeletable() throws UaException;

  /** Writes the value remotely. */
  void writeDeletable(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readDeletableAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDeletableAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDeletableNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getDeletableNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getAutoDelete() throws UaException;

  /** Sets the existing node's local value. */
  void setAutoDelete(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readAutoDelete() throws UaException;

  /** Writes the value remotely. */
  void writeAutoDelete(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readAutoDeleteAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAutoDeleteAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getAutoDeleteNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getAutoDeleteNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Integer getRecycleCount() throws UaException;

  /** Sets the existing node's local value. */
  void setRecycleCount(@Nullable Integer value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Integer readRecycleCount() throws UaException;

  /** Writes the value remotely. */
  void writeRecycleCount(@Nullable Integer value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Integer> readRecycleCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeRecycleCountAsync(@Nullable Integer value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getRecycleCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getRecycleCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getInstanceCount() throws UaException;

  /** Sets the existing node's local value. */
  void setInstanceCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readInstanceCount() throws UaException;

  /** Writes the value remotely. */
  void writeInstanceCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readInstanceCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeInstanceCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getInstanceCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getInstanceCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxInstanceCount() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxInstanceCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxInstanceCount() throws UaException;

  /** Writes the value remotely. */
  void writeMaxInstanceCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxInstanceCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxInstanceCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxInstanceCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getMaxInstanceCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxRecycleCount() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxRecycleCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxRecycleCount() throws UaException;

  /** Writes the value remotely. */
  void writeMaxRecycleCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxRecycleCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxRecycleCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxRecycleCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getMaxRecycleCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getCurrentState() throws UaException;

  /** Sets the existing node's local value. */
  void setCurrentState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readCurrentState() throws UaException;

  /** Writes the value remotely. */
  void writeCurrentState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readCurrentStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCurrentStateAsync(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  FiniteStateVariableType getCurrentStateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends FiniteStateVariableType> getCurrentStateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getLastTransition() throws UaException;

  /** Sets the existing node's local value. */
  void setLastTransition(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readLastTransition() throws UaException;

  /** Writes the value remotely. */
  void writeLastTransition(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readLastTransitionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLastTransitionAsync(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  FiniteTransitionVariableType getLastTransitionNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends FiniteTransitionVariableType> getLastTransitionNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ProgramDiagnostic2DataType getProgramDiagnostic() throws UaException;

  /** Sets the existing node's local value. */
  void setProgramDiagnostic(@Nullable ProgramDiagnostic2DataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ProgramDiagnostic2DataType readProgramDiagnostic() throws UaException;

  /** Writes the value remotely. */
  void writeProgramDiagnostic(@Nullable ProgramDiagnostic2DataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ProgramDiagnostic2DataType> readProgramDiagnosticAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeProgramDiagnosticAsync(
      @Nullable ProgramDiagnostic2DataType value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable ProgramDiagnostic2Type getProgramDiagnosticNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable ProgramDiagnostic2Type> getProgramDiagnosticNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part10/A.2.6/#A.2.6.2
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseObjectType getFinalResultDataNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part10/A.2.6/#A.2.6.2
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable BaseObjectType> getFinalResultDataNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateType getHaltedNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends StateType> getHaltedNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateType getReadyNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends StateType> getReadyNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateType getRunningNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends StateType> getRunningNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateType getSuspendedNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends StateType> getSuspendedNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getHaltedToReadyNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getHaltedToReadyNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getReadyToRunningNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getReadyToRunningNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getRunningToHaltedNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getRunningToHaltedNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getRunningToReadyNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getRunningToReadyNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getRunningToSuspendedNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getRunningToSuspendedNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getSuspendedToRunningNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getSuspendedToRunningNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getSuspendedToHaltedNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getSuspendedToHaltedNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getSuspendedToReadyNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getSuspendedToReadyNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getReadyToHaltedNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getReadyToHaltedNodeAsync();
}
