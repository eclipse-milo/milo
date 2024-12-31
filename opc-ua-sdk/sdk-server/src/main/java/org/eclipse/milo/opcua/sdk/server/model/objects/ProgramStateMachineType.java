/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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

  Boolean getCreatable();

  void setCreatable(Boolean value);

  PropertyType getCreatableNode();

  Boolean getDeletable();

  void setDeletable(Boolean value);

  PropertyType getDeletableNode();

  Boolean getAutoDelete();

  void setAutoDelete(Boolean value);

  PropertyType getAutoDeleteNode();

  Integer getRecycleCount();

  void setRecycleCount(Integer value);

  PropertyType getRecycleCountNode();

  UInteger getInstanceCount();

  void setInstanceCount(UInteger value);

  PropertyType getInstanceCountNode();

  UInteger getMaxInstanceCount();

  void setMaxInstanceCount(UInteger value);

  PropertyType getMaxInstanceCountNode();

  UInteger getMaxRecycleCount();

  void setMaxRecycleCount(UInteger value);

  PropertyType getMaxRecycleCountNode();

  FiniteStateVariableType getCurrentStateNode();

  LocalizedText getCurrentState();

  void setCurrentState(LocalizedText value);

  FiniteTransitionVariableType getLastTransitionNode();

  LocalizedText getLastTransition();

  void setLastTransition(LocalizedText value);

  ProgramDiagnostic2Type getProgramDiagnosticNode();

  ProgramDiagnostic2DataType getProgramDiagnostic();

  void setProgramDiagnostic(ProgramDiagnostic2DataType value);

  BaseObjectType getFinalResultDataNode();

  StateType getHaltedNode();

  StateType getReadyNode();

  StateType getRunningNode();

  StateType getSuspendedNode();

  TransitionType getHaltedToReadyNode();

  TransitionType getReadyToRunningNode();

  TransitionType getRunningToHaltedNode();

  TransitionType getRunningToReadyNode();

  TransitionType getRunningToSuspendedNode();

  TransitionType getSuspendedToRunningNode();

  TransitionType getSuspendedToHaltedNode();

  TransitionType getSuspendedToReadyNode();

  TransitionType getReadyToHaltedNode();
}
