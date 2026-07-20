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

/**
 * The states of the ExclusiveLimitStateMachine (Part 9 §5.8.19.2), doubling as the identifier of
 * one of a limit alarm's four configurable limits.
 *
 * <p>An {@link ExclusiveLimitAlarm} is in at most one of these states while active; a {@link
 * NonExclusiveLimitAlarm} tracks an independent TwoStateVariable per configured limit.
 */
public enum ExclusiveLimitState {
  HIGH_HIGH("HighHigh", true, NodeIds.ExclusiveLimitStateMachineType_HighHigh),

  HIGH("High", true, NodeIds.ExclusiveLimitStateMachineType_High),

  LOW("Low", false, NodeIds.ExclusiveLimitStateMachineType_Low),

  LOW_LOW("LowLow", false, NodeIds.ExclusiveLimitStateMachineType_LowLow);

  /**
   * The limits in excursion-severity order, most severe first: both outer limits (HighHigh, LowLow)
   * before both inner limits (High, Low). A first-match scan in this order yields the most severe
   * violated limit. This is severity-rank order, <i>not</i> limit-value order (HighHigh &gt; High
   * &gt; Low &gt; LowLow) and deliberately <b>not</b> grouped by side ({@code HighHigh, High,
   * LowLow, Low}).
   *
   * <p>The interleaving is load-bearing for {@code NonExclusiveLimitAlarm.mostSevereViolated},
   * which scans deadband-derived states: a large deadband can latch one side's limit violated while
   * the other side newly violates, so an inner limit (High) and the opposite outer limit (LowLow)
   * can both read violated at once, and the more severe (LowLow) must win. Grouping by side would
   * return the inner High instead. {@code LimitAlarm.enteredLimit} scans plain thresholds, where
   * the two sides are mutually exclusive, so this order is equally correct there.
   */
  static final ExclusiveLimitState[] BY_EXCURSION = {HIGH_HIGH, LOW_LOW, HIGH, LOW};

  private final String stateName;
  private final boolean highSide;
  private final NodeId stateId;

  ExclusiveLimitState(String stateName, boolean highSide, NodeId stateId) {
    this.stateName = stateName;
    this.highSide = highSide;
    this.stateId = stateId;
  }

  String stateName() {
    return stateName;
  }

  /** High-side limits are violated by values at or above them, low-side at or below. */
  boolean isHighSide() {
    return highSide;
  }

  NodeId stateId() {
    return stateId;
  }

  /** Rank within a side: HighHigh/LowLow outrank High/Low; escalation compares within one side. */
  int severityRank() {
    return this == HIGH_HIGH || this == LOW_LOW ? 2 : 1;
  }
}
