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
import org.eclipse.milo.opcua.sdk.server.subscriptions.Subscription;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;

public interface MonitoredItem {

  /**
   * @return the server-side id of this item.
   */
  UInteger getId();

  /**
   * @return the most recent {@link Session} that owned the subscription this item belongs to.
   */
  Session getSession();

  /**
   * @return the id of the {@link Subscription} this item belongs to.
   */
  UInteger getSubscriptionId();

  /**
   * @return the {@link ReadValueId} being monitored.
   */
  ReadValueId getReadValueId();

  /**
   * @return the {@link TimestampsToReturn}.
   */
  TimestampsToReturn getTimestampsToReturn();

  /**
   * @return {@code true} if this item should be sampled.
   */
  boolean isSamplingEnabled();
}
