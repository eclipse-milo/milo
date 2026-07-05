/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.internal;

import java.util.List;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;

/**
 * One buffered event awaiting publication by an event-mode {@link DataSetWriterRuntime}: the
 * event's field values in dataset (wire) order plus the instant it was pushed (Part 14 §6.2.6.2).
 *
 * <p>Immutable and thread-safe: producers ({@code PubSubServiceImpl#publishEvent}) build one
 * instance and fan it out to every event writer referencing the dataset, so the same instance is
 * shared across several writer queues; the drain (publish task thread) reads it without further
 * copying. {@link #fields()} is defensively copied at construction.
 *
 * @param fields the event field values, one per dataset event field, in wire order.
 * @param timestamp the instant the event was pushed (used as the DataSetMessage timestamp when the
 *     writer's mask includes one).
 * @param overflowMarker whether this entry is a synthesized {@code EventQueueOverflowEventType}
 *     event inserted at overflow time (Part 14 §6.2.6.2); the drain clears the writer's overflow
 *     latch when it dequeues a marker.
 */
record QueuedEvent(List<DataValue> fields, DateTime timestamp, boolean overflowMarker) {

  QueuedEvent {
    fields = List.copyOf(fields);
  }

  /** Create a normal buffered event. */
  static QueuedEvent of(List<DataValue> fields, DateTime timestamp) {
    return new QueuedEvent(fields, timestamp, false);
  }

  /** Create a synthesized event-queue-overflow marker entry (Part 14 §6.2.6.2). */
  static QueuedEvent overflowMarker(List<DataValue> fields, DateTime timestamp) {
    return new QueuedEvent(fields, timestamp, true);
  }
}
