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
 * <p>Array lifecycle components create typed child graphs through the node instantiator and store
 * them in their supplied diagnostics node manager. They observe the diagnostics enabled flag and
 * retire children as their owning Sessions or Subscriptions leave. Each array allocates child
 * identifiers independently of its current element count, so removing an earlier entry does not
 * reuse the identifier of a surviving child. These identifiers describe node identity, not an
 * element's current position in the array Value.
 *
 * <p>Security diagnostics also carry the standard array's access metadata into each child graph. In
 * restricted access mode, their attribute filters derive caller-visible access from the current
 * Session's roles.
 */
package org.eclipse.milo.opcua.sdk.server.diagnostics.variables;
