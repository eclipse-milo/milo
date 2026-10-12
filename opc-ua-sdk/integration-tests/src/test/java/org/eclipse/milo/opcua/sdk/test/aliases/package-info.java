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
 * Client/server integration tests for OPC UA Part 17 Alias Names support ({@link
 * org.eclipse.milo.opcua.sdk.server.aliases.AliasManager} and collaborators).
 *
 * <h2>Test model</h2>
 *
 * <p>Tests run against the shared per-class client and server owned by {@link
 * org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest}. An {@code AliasManager} has a one-shot
 * lifecycle (no restart after shutdown), so tests never share a manager: each test constructs a
 * fresh instance against the running server, starts it if the scenario calls for it, and shuts it
 * down (or discards it, for failed-startup scenarios) before returning.
 *
 * <p>Because a manager shutdown restores exactly the no-manager baseline that the standard
 * namespace establishes at server startup — {@code FindAlias} Methods unbound and non-executable —
 * tests that assert the "no manager installed" state and tests that install a manager can coexist
 * in one server without ordering constraints, provided every test cleans up the Nodes it created.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.test.aliases;

import org.jspecify.annotations.NullMarked;
