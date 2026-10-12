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
 * Connects standard diagnostics Variables to live server, Session, and Subscription state.
 * Attribute filters obtain current diagnostic values when a Variable is read; the runtime objects
 * remain the source of those values.
 *
 * <p>Each SubscriptionDiagnosticsArray component, the server-wide one and one per Session Object,
 * creates typed child graphs through the node instantiator and stores them in its supplied
 * diagnostics node manager. It observes the diagnostics enabled flag and retires children as their
 * Subscriptions leave. It allocates child identifiers independently of its current element count,
 * so removing an earlier entry does not reuse the identifier of a surviving child. These
 * identifiers describe node identity, not an element's current position in the array Value.
 *
 * <p>The two Session array components only publish the array Values. Their elements are the
 * Variables of the per-Session diagnostics Objects, which the {@code
 * org.eclipse.milo.opcua.sdk.server.diagnostics.objects} package creates and references from the
 * arrays.
 *
 * <p>Each Session's security diagnostics Variables carry the standard security array's access
 * metadata. In restricted access mode, their attribute filters derive caller-visible access from
 * the current Session's roles.
 */
package org.eclipse.milo.opcua.sdk.server.diagnostics.variables;
