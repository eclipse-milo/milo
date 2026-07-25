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
 * Infrastructure for SDK integration tests.
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest} owns a client, server, and
 * namespace for one test class. {@link org.eclipse.milo.opcua.sdk.test.TestServer} reuses immutable
 * certificate material within a test JVM while keeping each server's runtime state isolated.
 *
 * <p>Servers obtain loopback ports from {@link org.eclipse.milo.opcua.sdk.test.TestPortAllocator}.
 * Allocations are coordinated between Milo test JVMs and retained until JVM exit, preventing
 * concurrent test fixtures from selecting the same port.
 */
package org.eclipse.milo.opcua.sdk.test;
