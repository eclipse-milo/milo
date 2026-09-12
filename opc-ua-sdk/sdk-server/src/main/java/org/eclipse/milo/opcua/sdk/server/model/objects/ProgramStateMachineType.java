/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteStateVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteTransitionVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.ProgramDiagnostic2Type;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
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
  @Nullable Boolean getCreatable();

  /** Sets the existing node's local value. */
  void setCreatable(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getCreatableNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getDeletable();

  /** Sets the existing node's local value. */
  void setDeletable(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDeletableNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getAutoDelete();

  /** Sets the existing node's local value. */
  void setAutoDelete(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getAutoDeleteNode();

  /** Gets the existing node's local value. */
  @Nullable Integer getRecycleCount();

  /** Sets the existing node's local value. */
  void setRecycleCount(@Nullable Integer value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getRecycleCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getInstanceCount();

  /** Sets the existing node's local value. */
  void setInstanceCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getInstanceCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxInstanceCount();

  /** Sets the existing node's local value. */
  void setMaxInstanceCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxInstanceCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxRecycleCount();

  /** Sets the existing node's local value. */
  void setMaxRecycleCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxRecycleCountNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  FiniteStateVariableType getCurrentStateNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getCurrentState();

  /** Sets the existing node's local value. */
  void setCurrentState(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  FiniteTransitionVariableType getLastTransitionNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getLastTransition();

  /** Sets the existing node's local value. */
  void setLastTransition(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable ProgramDiagnostic2Type getProgramDiagnosticNode();

  /** Gets the existing node's local value. */
  @Nullable ProgramDiagnostic2DataType getProgramDiagnostic();

  /** Sets the existing node's local value. */
  void setProgramDiagnostic(@Nullable ProgramDiagnostic2DataType value);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part10/A.2.6/#A.2.6.2
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseObjectType getFinalResultDataNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateType getHaltedNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateType getReadyNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateType getRunningNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateType getSuspendedNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getHaltedToReadyNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getReadyToRunningNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getRunningToHaltedNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getRunningToReadyNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getRunningToSuspendedNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getSuspendedToRunningNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getSuspendedToHaltedNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getSuspendedToReadyNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getReadyToHaltedNode();
}
