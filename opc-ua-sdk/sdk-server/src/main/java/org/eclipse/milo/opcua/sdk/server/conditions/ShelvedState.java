/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.conditions;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/** The states of an alarm's ShelvedStateMachine (Part 9 §5.8.11). */
public enum ShelvedState {
  UNSHELVED("Unshelved", NodeIds.ShelvedStateMachineType_Unshelved),

  TIMED_SHELVED("TimedShelved", NodeIds.ShelvedStateMachineType_TimedShelved),

  ONE_SHOT_SHELVED("OneShotShelved", NodeIds.ShelvedStateMachineType_OneShotShelved);

  private final String stateName;
  private final NodeId stateId;

  ShelvedState(String stateName, NodeId stateId) {
    this.stateName = stateName;
    this.stateId = stateId;
  }

  String stateName() {
    return stateName;
  }

  NodeId stateId() {
    return stateId;
  }
}
