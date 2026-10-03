/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import java.util.List;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;

/**
 * Server-wide listener for the lifecycle of data MonitoredItems.
 *
 * <p>The SDK notifies these listeners when data items are created, deleted, transferred, or have
 * their MonitoringMode changed: synchronously, on the thread that makes the change, and before the
 * owning {@link AddressSpace} hears about the same items through its own callbacks. That is a
 * service request thread for the Subscription and MonitoredItem services, the thread that closes a
 * Session for the items it deletes, and the Subscription's publishing timer for the items of a
 * Subscription whose lifetime expired, which runs while holding that Subscription's monitor. Every
 * data item on the server passes through here, whichever AddressSpace owns it, so a component that
 * keeps per-item state current, such as one that refreshes read access results, needs no hook on
 * individual AddressSpaces. Register with {@link OpcUaServer#addDataItemListener}.
 *
 * <p>Event items are not reported. Listeners should return quickly and must not throw; an exception
 * is logged and affects neither the service call nor the other listeners.
 */
public interface DataItemListener {

  /**
   * Data items have been created by CreateMonitoredItems.
   *
   * <p>Each item already carries the read access result of its create-time check; see {@link
   * DataItem#setReadAccessResult}.
   *
   * @param dataItems the {@link DataItem}s that were created.
   */
  default void onDataItemsCreated(List<DataItem> dataItems) {}

  /**
   * Data items have been deleted, by DeleteMonitoredItems, DeleteSubscriptions, the expiry of their
   * Subscription, or the close of their Session.
   *
   * @param dataItems the {@link DataItem}s that were deleted.
   */
  default void onDataItemsDeleted(List<DataItem> dataItems) {}

  /**
   * Data items have had their MonitoringMode changed by SetMonitoringMode.
   *
   * <p>The new mode is already visible through {@link DataItem#isSamplingEnabled()}.
   *
   * @param dataItems the {@link DataItem}s whose MonitoringMode changed.
   */
  default void onMonitoringModeChanged(List<DataItem> dataItems) {}

  /**
   * Data items have been transferred to another Session by TransferSubscriptions (Part 4 §5.14.7).
   *
   * <p>Each item's {@link DataItem#getSession()} already returns {@code newSession}, and each item
   * already carries the result of a read access check for {@code newSession}, unless that check
   * failed.
   *
   * @param dataItems the {@link DataItem}s that were transferred.
   * @param oldSession the Session the items belonged to before the transfer.
   * @param newSession the Session the items belong to now.
   */
  default void onDataItemsTransferred(
      List<DataItem> dataItems, Session oldSession, Session newSession) {}
}
