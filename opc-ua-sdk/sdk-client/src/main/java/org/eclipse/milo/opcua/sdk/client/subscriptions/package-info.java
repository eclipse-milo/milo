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
 * Maintains client-side Subscriptions, their desired MonitoredItems, and ordered notification
 * delivery. {@link org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription} owns the
 * desired configuration and server-assigned state. The publishing manager maintains the Session's
 * Publish pipeline and routes responses to registered Subscriptions.
 *
 * <p>A reset starts a new incarnation of a Subscription object. Service responses captured for an
 * earlier incarnation cannot update its replacement, and pending Publish responses retain their
 * registration identities when server-assigned ids are reused. Publish requests may also serve a
 * Subscription first registered after the request was sent. Registration changes publish an
 * immutable registry snapshot; retaining it for each Publish request needs no registry lock or map
 * copy. Per-subscription processing and delivery queues preserve the order received from the
 * transport.
 */
package org.eclipse.milo.opcua.sdk.client.subscriptions;
