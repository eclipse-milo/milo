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
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
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
 * <p>A non-null {@link #failure()} reports that decoding could not complete: the message was
 * truncated or malformed past the failure point, or carried an unsupported chunked payload. The
 * tolerant-decode contract is preserved — everything decoded before the failure point is still
 * present in {@link #messages()} and {@link #metaData()} — but the failure is observable so callers
 * can count it. Input that is merely foreign or tolerated-and-skipped (e.g. a non-UADP version
 * nibble, reserved flag values) does not report a failure.
 *
 * @param publisherId the publisher id, or {@code null} if not present.
 * @param writerGroupId the WriterGroupId, or {@code null} if not present.
 * @param groupVersion the GroupVersion, or {@code null} if not present.
 * @param networkMessageNumber the NetworkMessageNumber, or {@code null} if not present.
 * @param sequenceNumber the NetworkMessage SequenceNumber, or {@code null} if not present.
 * @param timestamp the NetworkMessage timestamp, or {@code null} if not present.
 * @param messages the decoded DataSetMessages, in payload order; possibly empty.
 * @param metaData the DataSetMetaData announcements carried by the message; possibly empty.
 * @param failure the decode failure, or {@code null} if decoding completed.
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
    List<DecodedMetaData> metaData,
    @Nullable Failure failure)
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
   * @param failure the decode failure, or {@code null} if decoding completed.
   */
  public DecodedNetworkMessage {
    messages = List.copyOf(messages);
    metaData = List.copyOf(metaData);
  }

  /**
   * Create a {@link DecodedNetworkMessage} without a decode failure.
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

    return of(
        publisherId,
        writerGroupId,
        groupVersion,
        networkMessageNumber,
        sequenceNumber,
        timestamp,
        messages,
        metaData,
        null);
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
   * @param failure the decode failure, or {@code null} if decoding completed.
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
      List<DecodedMetaData> metaData,
      @Nullable Failure failure) {

    return new DecodedNetworkMessage(
        publisherId,
        writerGroupId,
        groupVersion,
        networkMessageNumber,
        sequenceNumber,
        timestamp,
        messages,
        metaData,
        failure);
  }

  /**
   * A decode failure: why a NetworkMessage could not be fully decoded.
   *
   * <p>{@code Bad_DecodingError} reports truncated or malformed input; {@code Bad_NotSupported}
   * reports a chunked NetworkMessage (ExtendedFlags2 bit 0, Part 14 §7.2.4.4.4), whose reassembly
   * is not implemented in this version.
   *
   * @param statusCode the status code classifying the failure.
   * @param message a human-readable description of the failure.
   * @param cause the exception that ended decoding, or {@code null} if the failure was detected
   *     without an exception. Compared by reference in {@link #equals(Object)}.
   */
  public record Failure(StatusCode statusCode, String message, @Nullable Throwable cause) {}
}
