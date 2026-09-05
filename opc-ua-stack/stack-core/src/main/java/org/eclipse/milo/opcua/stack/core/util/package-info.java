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
 * Shared protocol utilities and asynchronous coordination primitives used by transports and SDKs.
 *
 * <p>These utilities support endpoint and value handling, cryptographic operations, and task
 * coordination. The calling transport or SDK retains authority over protocol validation and
 * resource lifecycles; utility objects do not own its channels, sessions, or executor shutdown.
 *
 * <p>{@link org.eclipse.milo.opcua.stack.core.util.ExecutionQueue} serializes callbacks by default
 * and can permit an explicit number of concurrent workers. It retains queued tasks through executor
 * rejection by running the rejected worker on the submitting thread. Pausing stops new task
 * execution after currently running callbacks finish; resuming makes queued tasks eligible again.
 * Callbacks run outside the queue lock, and normal asynchronous workers yield between small batches
 * so other users of the executor can make progress.
 */
package org.eclipse.milo.opcua.stack.core.util;
