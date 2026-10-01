/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

/**
 * Server-side Subscriptions and MonitoredItems (OPC UA Part 4 §5.13 and §5.12).
 *
 * <p>Each Session owns a {@link
 * org.eclipse.milo.opcua.sdk.server.subscriptions.SubscriptionManager} that implements the
 * Subscription and MonitoredItem service sets for that Session. It creates {@link
 * org.eclipse.milo.opcua.sdk.server.subscriptions.Subscription}s, validates and creates the
 * MonitoredItems that belong to them, and answers Publish requests from the shared {@link
 * org.eclipse.milo.opcua.sdk.server.subscriptions.PublishQueue}. A Subscription can outlive its
 * Session through TransferSubscriptions, in which case it moves to the new Session's manager.
 *
 * <h2>Data flow</h2>
 *
 * <p>CreateMonitoredItems reads the attributes it needs from the {@link
 * org.eclipse.milo.opcua.sdk.server.AddressSpace}, validates the request against them, and creates
 * a {@link org.eclipse.milo.opcua.sdk.server.items.MonitoredDataItem} or {@link
 * org.eclipse.milo.opcua.sdk.server.items.MonitoredEventItem}. Data items are handed to the owning
 * AddressSpace through {@code onDataItemsCreated}, and from then on the AddressSpace samples them
 * and pushes values with {@link org.eclipse.milo.opcua.sdk.server.items.DataItem#setValue}. Event
 * items are registered with the server's event notifier. Each item filters what it is given and
 * keeps a bounded queue of notifications. The Subscription's publishing timer drains those queues
 * into NotificationMessages and completes queued Publish requests with them.
 *
 * <h2>Ownership</h2>
 *
 * <p>The AddressSpace owns sampling: when and how a value is read. This package owns everything the
 * specification makes the server responsible for regardless of the data source: request validation,
 * quotas, monitoring mode, filters and deadbands, queue overflow, sequence numbers, keep-alives,
 * and lifetime.
 *
 * <p>Read access control for data items is also owned here, so an AddressSpace does not need to
 * check it. CreateMonitoredItems fails an item only for denials that are not about read access,
 * such as an AccessRestriction the channel does not satisfy. Part 4 §5.13.2.1 requires an item the
 * Session may not read to be created anyway and to report {@code Bad_UserAccessDenied} or {@code
 * Bad_NotReadable} in the Publish response, including when access rights change later. A
 * MonitoredDataItem therefore carries the result of the most recent read access check and replaces
 * the values it is given with the denial status while access is denied. The check is seeded at
 * create time and refreshed by the Subscription on every publishing interval, outside the
 * subscription lock. The cost is one {@code AccessController.checkReadAccess} call per Subscription
 * per publishing interval.
 */
package org.eclipse.milo.opcua.sdk.server.subscriptions;
