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
 * Shared support for transport tests that cross the JVM/network boundary.
 *
 * <p>Tests should let the operating system choose listener ports when the bound address is
 * observable. When an API requires the port before binding, {@link
 * org.eclipse.milo.opcua.stack.transport.TestPortAllocator} coordinates allocation across Milo test
 * JVMs and keeps each assigned port exclusive to one JVM for its lifetime.
 */
package org.eclipse.milo.opcua.stack.transport;
