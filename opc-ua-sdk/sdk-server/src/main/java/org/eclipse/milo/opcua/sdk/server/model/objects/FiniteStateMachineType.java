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

import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteStateVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteTransitionVariableType;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.5">https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.5</a>
 */
public interface FiniteStateMachineType extends StateMachineType {
  FiniteStateVariableType getCurrentStateNode();

  LocalizedText getCurrentState();

  void setCurrentState(LocalizedText value);

  FiniteTransitionVariableType getLastTransitionNode();

  LocalizedText getLastTransition();

  void setLastTransition(LocalizedText value);

  BaseDataVariableType getAvailableStatesNode();

  NodeId[] getAvailableStates();

  void setAvailableStates(NodeId[] value);

  BaseDataVariableType getAvailableTransitionsNode();

  NodeId[] getAvailableTransitions();

  void setAvailableTransitions(NodeId[] value);
}
