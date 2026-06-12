/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.uadp;

import java.util.List;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.Nullable;

/**
 * A decoded NetworkMessage: the header values present on the wire plus the decoded DataSetMessages
 * and any discovery metadata announcements it carried.
 *
 * <p>Header components are {@code null} when the corresponding header was not present on the wire;
 * the reader matching chain treats absent values as wildcards.
 *
 * @param publisherId the publisher id, or {@code null} if not present.
 * @param writerGroupId the WriterGroupId, or {@code null} if not present.
 * @param groupVersion the GroupVersion, or {@code null} if not present.
 * @param networkMessageNumber the NetworkMessageNumber, or {@code null} if not present.
 * @param sequenceNumber the NetworkMessage SequenceNumber, or {@code null} if not present.
 * @param timestamp the NetworkMessage timestamp, or {@code null} if not present.
 * @param messages the decoded DataSetMessages, in payload order; possibly empty.
 * @param metaData the DataSetMetaData announcements carried by the message; possibly empty.
 * @apiNote Create instances via {@link #of(PublisherId, UShort, UInteger, UShort, UShort, DateTime,
 *     List, List)} rather than the canonical constructor; the factory methods are stable while the
 *     canonical constructor is not.
 */
public record DecodedNetworkMessage(
    @Nullable PublisherId publisherId,
    @Nullable UShort writerGroupId,
    @Nullable UInteger groupVersion,
    @Nullable UShort networkMessageNumber,
    @Nullable UShort sequenceNumber,
    @Nullable DateTime timestamp,
    List<DecodedDataSetMessage> messages,
    List<DecodedMetaData> metaData)
    implements UadpDecodedMessage {

  /**
   * Create a new {@link DecodedNetworkMessage}.
   *
   * @param publisherId the publisher id, or {@code null} if not present.
   * @param writerGroupId the WriterGroupId, or {@code null} if not present.
   * @param groupVersion the GroupVersion, or {@code null} if not present.
   * @param networkMessageNumber the NetworkMessageNumber, or {@code null} if not present.
   * @param sequenceNumber the NetworkMessage SequenceNumber, or {@code null} if not present.
   * @param timestamp the NetworkMessage timestamp, or {@code null} if not present.
   * @param messages the decoded DataSetMessages, in payload order; possibly empty.
   * @param metaData the DataSetMetaData announcements carried by the message; possibly empty.
   */
  public DecodedNetworkMessage {
    messages = List.copyOf(messages);
    metaData = List.copyOf(metaData);
  }

  /**
   * Create a {@link DecodedNetworkMessage}.
   *
   * @param publisherId the publisher id, or {@code null} if not present.
   * @param writerGroupId the WriterGroupId, or {@code null} if not present.
   * @param groupVersion the GroupVersion, or {@code null} if not present.
   * @param networkMessageNumber the NetworkMessageNumber, or {@code null} if not present.
   * @param sequenceNumber the NetworkMessage SequenceNumber, or {@code null} if not present.
   * @param timestamp the NetworkMessage timestamp, or {@code null} if not present.
   * @param messages the decoded DataSetMessages, in payload order; possibly empty.
   * @param metaData the DataSetMetaData announcements carried by the message; possibly empty.
   * @return a new {@link DecodedNetworkMessage}.
   */
  public static DecodedNetworkMessage of(
      @Nullable PublisherId publisherId,
      @Nullable UShort writerGroupId,
      @Nullable UInteger groupVersion,
      @Nullable UShort networkMessageNumber,
      @Nullable UShort sequenceNumber,
      @Nullable DateTime timestamp,
      List<DecodedDataSetMessage> messages,
      List<DecodedMetaData> metaData) {

    return new DecodedNetworkMessage(
        publisherId,
        writerGroupId,
        groupVersion,
        networkMessageNumber,
        sequenceNumber,
        timestamp,
        messages,
        metaData);
  }
}
