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
 * White-box integration tests that must live in {@link
 * org.eclipse.milo.opcua.sdk.server.aliases.AliasManager}'s own package to reach its
 * package-private wire entry points against a running server.
 *
 * <p>Black-box alias integration tests, which exercise the manager purely through a client
 * connection, live in {@code org.eclipse.milo.opcua.sdk.test.aliases}. Add tests here only when the
 * scenario cannot be produced deterministically over the wire.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.server.aliases;

import org.jspecify.annotations.NullMarked;
