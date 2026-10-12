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
 * Runnable programs for the Wiki's first client and server tutorials.
 *
 * <p>The server binds only to IPv4 loopback and exposes a small thermostat (one Object with a
 * read-only Temperature, a writable Setpoint, and an AdjustSetpoint Method) over an anonymous,
 * unencrypted endpoint. It is a local learning program, not a deployment security configuration.
 * The client selects that endpoint explicitly, reads Temperature, and checks both operation status
 * and value type.
 *
 * <p>The server owns its namespace lifecycle. The standalone entry points close their client or
 * server before releasing Milo's shared resources. Applications hosting other Milo components must
 * defer that process-wide release until all components have stopped.
 */
@org.jspecify.annotations.NullMarked
package org.eclipse.milo.examples.wiki;
