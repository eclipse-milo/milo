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
 * <p>Read access control for data items is split. CreateMonitoredItems fails an item only for
 * denials that are not about read access, such as an AccessRestriction the channel does not
 * satisfy. Part 4 §5.13.2.1 requires an item the Session may not read to be created anyway and to
 * report {@code Bad_UserAccessDenied} or {@code Bad_NotReadable} in the Publish response, including
 * when access rights change later. A MonitoredDataItem therefore carries the result of the most
 * recent read access check, exposes it through {@link
 * org.eclipse.milo.opcua.sdk.server.items.DataItem#getReadAccessResult}, and replaces the values it
 * is given with the denial status while access is denied; this package seeds that result at create
 * time. Keeping it current is the server's responsibility, and the server chooses where that
 * happens: the sampling framework in {@link org.eclipse.milo.opcua.sdk.server.sampling} refreshes
 * it before every sample, an AddressSpace that samples on its own can do the same through {@link
 * org.eclipse.milo.opcua.sdk.server.items.DataItem#setReadAccessResult}, or a component of the
 * server's own can refresh every item it knows of on configuration, identity, or transfer events,
 * on any schedule that bounds how stale a result can be. That method is safe to call from any
 * thread alongside the sampler's {@code setValue} and is idempotent, and results apply in call
 * order, so a refresher with several triggers must order its checks per item. An item whose result
 * is never refreshed is stale, not unsafe: it keeps enforcing the result it was created with.
 *
 * <p>A component that refreshes on its own schedule can learn of every data item on the server from
 * a {@link org.eclipse.milo.opcua.sdk.server.DataItemListener}, and of the events that change a
 * result between refreshes: {@link
 * org.eclipse.milo.opcua.sdk.server.DataItemListener#onDataItemsTransferred} when items move to
 * another Session, {@link
 * org.eclipse.milo.opcua.sdk.server.SessionListener#onSessionIdentityChanged} and {@link
 * org.eclipse.milo.opcua.sdk.server.SessionListener#onSessionEndpointChanged} when a Session's user
 * or security changes, a {@link org.eclipse.milo.opcua.sdk.server.access.ReadAccessListener} for
 * every {@link org.eclipse.milo.opcua.sdk.server.access.AccessControlManager#invalidateReadAccess},
 * and {@link org.eclipse.milo.opcua.sdk.server.Session#isClosed} to skip a Session that has closed
 * since it was last seen.
 */
package org.eclipse.milo.opcua.sdk.server.subscriptions;
