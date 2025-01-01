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

import org.eclipse.milo.opcua.sdk.server.model.variables.StateVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.TransitionVariableType;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.2">https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.2</a>
 */
public interface StateMachineType extends BaseObjectType {
  StateVariableType getCurrentStateNode();

  LocalizedText getCurrentState();

  void setCurrentState(LocalizedText value);

  TransitionVariableType getLastTransitionNode();

  LocalizedText getLastTransition();

  void setLastTransition(LocalizedText value);
}
