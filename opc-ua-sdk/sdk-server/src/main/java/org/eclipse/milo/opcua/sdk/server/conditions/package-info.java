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
 * Server-side OPC UA Condition state, method dispatch, refresh, and persistence support.
 *
 * <p>The {@link org.eclipse.milo.opcua.sdk.server.conditions.DefaultConditionManager} releases the
 * runtime resources of each registered {@link
 * org.eclipse.milo.opcua.sdk.server.conditions.Condition} when the Condition is unregistered,
 * displaced by a registration under the same ConditionId, or the server shuts down.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.server.conditions;

import org.jspecify.annotations.NullMarked;
