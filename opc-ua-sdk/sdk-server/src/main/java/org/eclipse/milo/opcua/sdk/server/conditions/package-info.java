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
 * <p>Applications that define a custom Condition ObjectType or a custom Java behavior use {@link
 * org.eclipse.milo.opcua.sdk.server.conditions.Condition#createInstance} to create the typed node
 * tree and behavior together. {@link
 * org.eclipse.milo.opcua.sdk.server.conditions.Condition#attachInstance} and {@link
 * org.eclipse.milo.opcua.sdk.server.conditions.Condition#adoptInstance} provide the corresponding
 * lifecycles for existing custom instances. These extension entry points own instance construction,
 * method discovery, and handler installation; they do not add custom state-transition semantics or
 * concrete behavior-specific builder validation.
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
 * <p>The default manager resolves shared shelving Methods through either the ConditionId or the
 * nested ShelvingState ObjectId. Unregistering retires handlers installed on copied Methods only
 * while they still belong to that behavior, preserving later application or replacement handlers.
 * Calls that already obtained a handler may finish against the retired behavior.
 *
 * <p>Deferred shelving expiry is resolved before event emission or refresh copying. Field reads
 * within those operations cannot trigger another expiry transition, so one event retains one
 * EventId and one shelving state. Ordinary UnshelveTime reads still apply the lazy expiry fallback
 * when a timer prompt was lost or delayed.
 *
 * <h2>Limit-alarm data flow</h2>
 *
 * <p>Limit-alarm behavior separates domain evaluation from Condition state application. The {@code
 * evaluate(double)} methods implement Milo's standard scalar limit and deadband policy. The
 * reduced-state APIs on {@link org.eclipse.milo.opcua.sdk.server.conditions.ExclusiveLimitAlarm}
 * and {@link org.eclipse.milo.opcua.sdk.server.conditions.NonExclusiveLimitAlarm} instead accept a
 * trusted outcome already computed by the application. Milo applies either outcome through the
 * Condition mutation boundary, which owns acknowledgement cycles, transition timing, shelving,
 * Retain, snapshots, and event generation.
 *
 * <p>Custom limit-alarm subtypes remain responsible for array or compound reduction and, for
 * non-exclusive alarms, selection of the primary state used for event presentation. Attaching or
 * adopting stock behavior preserves the generated subtype's NodeIds, HasTypeDefinition, EventType,
 * and subtype-specific members. Creation and adoption add the standard optional {@code
 * ActiveState/EffectiveTransitionTime} member for limit alarms; attaching a complete instance
 * preserves its existing optional-member shape.
 *
 * <p>Subtype-specific state cannot currently be written atomically with the private standard limit
 * transition. Custom fields that must share the same transition and event boundary therefore need a
 * separately designed behavior-extension seam rather than direct node writes around a reduced-state
 * API call.
 *
 * <p>The {@link org.eclipse.milo.opcua.sdk.server.conditions.DefaultConditionManager} releases the
 * runtime resources of each registered {@link
 * org.eclipse.milo.opcua.sdk.server.conditions.Condition} when the Condition is unregistered,
 * displaced by a registration under the same ConditionId, or the server shuts down.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.server.conditions;

import org.jspecify.annotations.NullMarked;
