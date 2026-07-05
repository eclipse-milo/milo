/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub;

import java.util.List;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.jspecify.annotations.Nullable;

/**
 * A remote publisher status message received by a local PubSub connection.
 *
 * <p>This event is notification-only: it reports the remote publisher's PubSubConnection status and
 * the local readers whose PublisherId filter correlates with that status. It does not change the
 * local {@link PubSubService#state(PubSubHandle)} of the receiving connection, reader group, or
 * DataSetReader.
 *
 * @param connection the local PubSubConnection that received the status message.
 * @param mappingName the message mapping level of the status topic, e.g. {@code "json"}.
 * @param topic the broker topic the status was received on, or {@code null} for topic-less
 *     transports.
 * @param messageId the MessageId from the status message.
 * @param publisherId the remote PublisherId.
 * @param status the remote PubSubConnection status.
 * @param cyclic whether the message was a cyclic status report.
 * @param timestamp the cyclic report timestamp, or {@code null} for non-cyclic status.
 * @param nextReportTime when the next cyclic status report was expected, or {@code null} for
 *     non-cyclic status.
 * @param timeout {@code true} when this event was synthesized because the previous cyclic report's
 *     {@code NextReportTime} elapsed without a replacement.
 * @param readers local DataSetReaders whose mapping and PublisherId filter correlate with the
 *     remote publisher.
 */
public record PublisherStatusReceivedEvent(
    PubSubHandle connection,
    String mappingName,
    @Nullable String topic,
    String messageId,
    PublisherId publisherId,
    PubSubState status,
    boolean cyclic,
    @Nullable DateTime timestamp,
    @Nullable DateTime nextReportTime,
    boolean timeout,
    List<PubSubHandle> readers) {

  /**
   * Create a new {@link PublisherStatusReceivedEvent}.
   *
   * @param connection the local PubSubConnection that received the status message.
   * @param mappingName the message mapping level of the status topic.
   * @param topic the broker topic the status was received on, or {@code null}.
   * @param messageId the status MessageId.
   * @param publisherId the remote PublisherId.
   * @param status the remote PubSubConnection status.
   * @param cyclic whether the message was cyclic.
   * @param timestamp the cyclic report timestamp, or {@code null}.
   * @param nextReportTime the expected next cyclic report time, or {@code null}.
   * @param timeout whether this event is a synthetic timeout.
   * @param readers correlated local DataSetReaders.
   */
  public PublisherStatusReceivedEvent {
    readers = List.copyOf(readers);
  }
}
