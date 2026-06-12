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
 * PubSub engine internals: the {@code PubSubService} implementation, per-component runtimes and the
 * Part 14 state machine, publish scheduling and keep-alive, reader matching and dispatch, the
 * metadata cache, diagnostics collection, and the reconfigure diff. Everything in this package is
 * implementation detail and not part of the public API; it may change without notice.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.pubsub.internal;

import org.jspecify.annotations.NullMarked;
