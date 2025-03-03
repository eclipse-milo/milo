/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableType;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.20">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.20</a>
 */
public interface NonExclusiveLimitAlarmType extends LimitAlarmType {
  TwoStateVariableType getActiveStateNode();

  LocalizedText getActiveState();

  void setActiveState(LocalizedText value);

  TwoStateVariableType getHighHighStateNode();

  LocalizedText getHighHighState();

  void setHighHighState(LocalizedText value);

  TwoStateVariableType getHighStateNode();

  LocalizedText getHighState();

  void setHighState(LocalizedText value);

  TwoStateVariableType getLowStateNode();

  LocalizedText getLowState();

  void setLowState(LocalizedText value);

  TwoStateVariableType getLowLowStateNode();

  LocalizedText getLowLowState();

  void setLowLowState(LocalizedText value);
}
