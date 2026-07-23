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
 * <p>Concrete behavior classes offer three instance lifecycles:
 *
 * <ul>
 *   <li>{@code create(context, builder -> ...)} creates and initializes a new typed instance.
 *   <li>{@code attach(node, options -> ...)} activates behavior on a structurally complete,
 *       generated typed instance already present in the address space.
 *   <li>{@code adopt(context, nodeId, builder -> ...)} completes a partial generated instance in
 *       place and then activates its behavior, preserving existing identities and stored
 *       configuration unless the builder explicitly overrides a value.
 * </ul>
 *
 * <p>An {@code adopt} factory accepts generated subtypes of its declared node type. This selects
 * the behavior level, not a replacement address-space type: the existing HasTypeDefinition,
 * EventType, NodeIds, and subtype-specific fields remain unchanged. Adopting an
 * ExclusiveLimitAlarmType through {@link
 * org.eclipse.milo.opcua.sdk.server.conditions.AlarmCondition#adopt} is therefore a supported
 * recovery path when a loaded NodeSet can provide generic alarm state but lacks a usable numeric
 * limit configuration.
 *
 * <p>All three return behavior without registering it. Applications must pass the result to their
 * {@link org.eclipse.milo.opcua.sdk.server.conditions.ConditionManager} before clients invoke
 * Condition methods or request ConditionRefresh. Unregistering or replacing a behavior releases its
 * runtime resources.
 *
 * <p>Condition Methods may be instance copies or shared type/instance nodes. Behavior mutates only
 * Methods proven to be exclusive instance copies; shared Methods are dispatched per registered
 * Condition through the ConditionManager, leaving the shared node and any application-installed
 * handler unchanged.
 *
 * <p>The {@link org.eclipse.milo.opcua.sdk.server.conditions.DefaultConditionManager} releases the
 * runtime resources of each registered {@link
 * org.eclipse.milo.opcua.sdk.server.conditions.Condition} when the Condition is unregistered,
 * displaced by a registration under the same ConditionId, or the server shuts down.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.server.conditions;

import org.jspecify.annotations.NullMarked;
