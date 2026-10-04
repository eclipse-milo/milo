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

import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.access.AccessController.AccessResult;
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
   * <p>The server must keep this result current, with the result of {@code
   * AccessController.checkReadAccess} for {@link #getSession()}, so that access rights that change
   * after the item was created reach the client as Part 4 §5.13.2.1 requires. Whoever samples the
   * item, the SDK re-checks it when its Session's identity or endpoint changes. For every other
   * change, whatever samples the item may do so on its own cycle, or the server may keep it current
   * from a component of its own, on any schedule that bounds how stale a result can be, using
   * {@link org.eclipse.milo.opcua.sdk.server.access.AccessControlManager#invalidateReadAccess} to
   * learn when. An item whose result is never refreshed is stale, not unsafe: it keeps enforcing
   * the result it was created with.
   *
   * <p>A result that is not a decision, {@link AccessResult#NODE_UNKNOWN}, leaves the last result
   * in place, the same as a check that failed. An allowed item keeps reporting whatever its sampler
   * delivers, typically the AddressSpace's own status for a Node it no longer knows. A denied item
   * is not sampled, so it reports {@code Bad_NodeIdUnknown} once in place of the denial, as Part 4
   * §5.13.1.6 requires for a deleted Node, and keeps withholding values; the next decision is
   * reported as new.
   *
   * <p>This method is safe to call from any thread, concurrently with {@link #setValue(DataValue)},
   * and is idempotent: repeating a call with the same result has no further effect. Results apply
   * in the order the calls arrive, and the item does not know which Session or which check a result
   * came from. A refresher with more than one trigger must order its own checks per item so that
   * the result of an older check never lands after a newer one. To keep a result checked before an
   * identity or endpoint change from replacing the one the SDK applies for it, apply it with {@link
   * #setReadAccessResult(AccessResult, Session, long)} instead.
   *
   * <p>{@link MonitoredDataItem}, the item the SDK creates for every data MonitoredItem, is the
   * implementation that enforces this. The default implementation does nothing, so an
   * implementation of this interface outside the SDK is not required to take part.
   *
   * @param accessResult the result of the read access check.
   */
  default void setReadAccessResult(AccessResult accessResult) {}

  /**
   * Update this item with the result of a read access check made for {@code session} at access
   * epoch {@code accessEpoch}, unless the item has since moved to another Session or that Session's
   * identity or endpoint has since changed.
   *
   * <p>A refresher reads {@link #getSession()} and that Session's {@link Session#getAccessEpoch()}
   * before its check and passes both here with the result. An identity or endpoint change bumps the
   * epoch, then applies the answer for the Session as it is now. This method compares and updates
   * in one step, so a result checked for the Session as it was never replaces that answer.
   * Comparing first and then calling {@link #setReadAccessResult(AccessResult)} leaves a gap in
   * which it can.
   *
   * <p>{@link MonitoredDataItem} compares and updates under the lock the SDK applies its own
   * results under. The default implementation compares and then calls {@link
   * #setReadAccessResult(AccessResult)}, so an implementation outside the SDK that applies results
   * under a lock of its own overrides this method to compare under it too.
   *
   * @param accessResult the result of the read access check.
   * @param session the Session the check was made for, read from {@link #getSession()} before the
   *     check.
   * @param accessEpoch the access epoch of {@code session}, read before the check.
   * @return {@code true} if the result was applied, {@code false} if the item has moved to another
   *     Session or the Session's access epoch is no longer {@code accessEpoch}.
   */
  default boolean setReadAccessResult(
      AccessResult accessResult, Session session, long accessEpoch) {

    if (getSession() != session || session.getAccessEpoch() != accessEpoch) {
      return false;
    }

    setReadAccessResult(accessResult);

    return true;
  }

  /**
   * Get the result of the most recent read access check applied to this item.
   *
   * <p>A sampler can use this to leave out items the Session may not read from its own reads, and a
   * component that refreshes results can use it to see the state an item is enforcing without
   * tracking that state itself.
   *
   * <p>The default implementation always returns {@link AccessResult#ALLOWED}, matching the default
   * {@link #setReadAccessResult(AccessResult)}, which applies nothing.
   *
   * @return the {@link AccessResult} most recently passed to {@link
   *     #setReadAccessResult(AccessResult)}, or {@link AccessResult#ALLOWED} if none has been.
   */
  default AccessResult getReadAccessResult() {
    return AccessResult.ALLOWED;
  }
}
