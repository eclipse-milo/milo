/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.items;

import org.eclipse.milo.opcua.sdk.server.servicesets.impl.AccessController.AccessResult;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

public interface DataItem extends MonitoredItem {

  /**
   * Set the latest sampled value.
   *
   * @param value the latest sampled value.
   */
  void setValue(DataValue value);

  /**
   * Apply a new {@link StatusCode} to the last value that passed the filter and then set the
   * derived value.
   *
   * @param quality the {@link StatusCode} to apply.
   */
  void setQuality(StatusCode quality);

  /**
   * @return the rate to sample this item at.
   */
  double getSamplingInterval();

  /**
   * Update this item with the result of the most recent check of its Session's read access.
   *
   * <p>While access is denied, every value passed to {@link #setValue(DataValue)} is replaced by a
   * {@link DataValue} carrying the denial status, so the client receives the denial in a Publish
   * response rather than a value it may not read. A transition to denied queues the denial right
   * away, unless the item is {@link
   * org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode#Disabled}, in which case
   * nothing is queued until the first call after monitoring resumes. Once access is allowed again,
   * the next value passed to {@link #setValue(DataValue)} is always reported.
   *
   * <p>Whatever samples this item is expected to call this with the result of {@code
   * AccessController.checkReadAccess} for {@link #getSession()} on every sampling cycle, so that
   * access rights that change after the item was created reach the client as Part 4 §5.13.2.1
   * requires. {@link org.eclipse.milo.opcua.sdk.server.util.SubscriptionModel} does this.
   *
   * @param accessResult the result of the read access check.
   */
  void setReadAccessResult(AccessResult accessResult);

  /**
   * Get the result of the most recent read access check applied to this item.
   *
   * <p>A sampler can use this to leave out items the Session may not read from its own reads, and a
   * component that refreshes results can use it to see the state an item is enforcing without
   * tracking that state itself.
   *
   * @return the {@link AccessResult} most recently passed to {@link
   *     #setReadAccessResult(AccessResult)}, or {@link AccessResult#ALLOWED} if none has been.
   */
  AccessResult getReadAccessResult();
}
