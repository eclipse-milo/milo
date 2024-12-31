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

import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableType;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.19/#5.8.19.3">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.19/#5.8.19.3</a>
 */
public interface ExclusiveLimitAlarmType extends LimitAlarmType {
  TwoStateVariableType getActiveStateNode();

  LocalizedText getActiveState();

  void setActiveState(LocalizedText value);

  ExclusiveLimitStateMachineType getLimitStateNode();
}
