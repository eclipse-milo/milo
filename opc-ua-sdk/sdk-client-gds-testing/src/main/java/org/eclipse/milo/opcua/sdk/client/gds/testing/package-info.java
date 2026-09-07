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
 * Reusable server-side fixtures for testing applications built on the Milo GDS client.
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.client.gds.testing.FakeGdsNamespace} hosts an in-memory GDS
 * namespace on an {@link org.eclipse.milo.opcua.sdk.server.OpcUaServer}. Tests can control access,
 * registration, certificate issuance, and TrustList responses, then inspect recorded calls. The
 * fixture returns requested PEM or PFX private keys and applies the supplied password. Pending
 * requests retain their key format and password until issuance or reset. The fixture must be
 * started before the server and shut down during test teardown.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.client.gds.testing;

import org.jspecify.annotations.NullMarked;
