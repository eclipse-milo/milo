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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Per-DataSetReader sequence-number tracking (Part 14 §7.2.3): one {@link SequenceNumberWindow} per
 * wire-derived stream, kept separately for the NetworkMessage sequence stream — keyed by
 * (PublisherId, WriterGroupId) — and the DataSetMessage sequence stream — keyed by (PublisherId,
 * DataSetWriterId). Keys use the wire values only (PublisherIds in canonical string form so
 * differing wire types of the same id share a stream); {@code null} components — headers absent on
 * the wire — participate in the key, so a wildcard reader interleaving multiple publisher/writer
 * streams tracks each stream independently, while streams indistinguishable on the wire are
 * indistinguishable to the window too.
 *
 * <p>NetworkMessage sequence numbers are UADP UInt16 (§7.2.4.4.2 Table 154; the JSON mapping has no
 * NetworkMessage sequence number), so the NetworkMessage windows always use N=16. The
 * DataSetMessage window width comes from the reader's mapping: 16 for the UADP mapping, 32 for the
 * JSON mapping (§7.2.5.4.1 Table 185) and for unknown custom mappings, whose decoded slot spans
 * UInt32.
 *
 * <p>Record discard (§7.2.3): "Subscribers shall discard the records they keep for sequence numbers
 * if they do not receive messages for two times the message receive timeout to deal with Publishers
 * or brokers that are out of service and were not able to continue from the last used
 * SequenceNumber." The check is lazy, per stream, on access: a record whose last <em>received</em>
 * observation — an accepted message or a keep-alive, both received messages in §7.2.3's sense — is
 * older than the discard period is reset before use, so the next message re-seeds the stream. A
 * consistent keep-alive refreshes the discard clock without moving the window position, so a
 * keep-alive-only period of any length keeps the record alive; an INCONSISTENT keep-alive — one
 * whose carried next-expected value classifies STALE or INVALID against the current window —
 * reseeds the window to {@code carried - 1} (with a WARN naming the reader and the stream, once per
 * occurrence): the keep-alive "provides the next expected sequence number for the DataSetWriter"
 * (§7.2.4.5.8), so the publisher is authoritative about its own counter and a restarted publisher
 * that emits keep-alives recovers its stream immediately, in both timeout modes. The trade-off is a
 * reordered stale keep-alive causing a transient backward reseed that self-heals within a few
 * messages (the next data message classifies invalid and drops, then the next current keep-alive
 * reseeds forward); the WARN makes it visible. Rejected (stale/invalid) data observations
 * deliberately do not refresh the discard clock — a publisher that restarts its numbering without a
 * silent gap and without keep-alives would otherwise keep its own stream wedged forever; with the
 * accept-clock it recovers after the discard period.
 *
 * <p>Restart recovery when the timeout is disabled: when the reader's messageReceiveTimeout is 0
 * there is no time-based discard ({@code discardNanos == 0}), and §7.2.3's recovery rule is
 * undefined — a restarted publisher's stream that carries no keep-alives would stay wedged forever.
 * As a documented Milo extension, after {@value #RESTART_REJECTION_THRESHOLD} consecutive
 * STALE/INVALID classifications on one stream with no intervening accepted message or keep-alive
 * observation, the stream's record is discarded (with a WARN naming the reader and the stream) so
 * the next message accepts and re-seeds the window. The heuristic is INERT whenever a time-based
 * discard is armed ({@code discardNanos > 0}): there the pure spec semantics stand — rejects keep
 * dropping for as long as the record lives, and only the two-times-timeout silence (or an
 * inconsistent keep-alive) discards or reseeds it — so the replay-rejection posture a later
 * security phase builds on this window (signature verification followed by the same recency check)
 * is not weakened on deployments with a configured timeout.
 *
 * <p>In both modes the maps are bounded at {@value #MAX_TRACKED_STREAMS} streams each, evicting the
 * least recently used stream (with a once-per-tracker WARN), purely as protection against rogue
 * traffic fabricating stream identities.
 *
 * <p>Threading: dispatch is serialized per connection, so all observations happen on one thread at
 * a time; instances are reset by replacement through a volatile field on {@link
 * DataSetReaderRuntime} (engine threads), and {@link #lastAcceptedDataSetMessageSequenceNumber()}
 * is volatile for cross-thread reads.
 */
final class ReaderSequenceTracker {

  /** Bound on tracked streams per window kind; the least recently used stream is evicted. */
  static final int MAX_TRACKED_STREAMS = 4096;

  /**
   * Consecutive STALE/INVALID classifications on one stream — with no intervening accept or
   * keep-alive — after which the stream's record is discarded so a restarted publisher recovers.
   * Applies only when no time-based discard is armed ({@code discardNanos == 0}).
   */
  static final int RESTART_REJECTION_THRESHOLD = 16;

  /** NetworkMessage sequence numbers are UInt16 in the UADP mapping (§7.2.4.4.2 Table 154). */
  private static final int NETWORK_MESSAGE_BIT_WIDTH = 16;

  private static final Logger LOGGER = LoggerFactory.getLogger(ReaderSequenceTracker.class);

  /** Access-ordered so the eldest entry is the least recently used stream. */
  private final Map<StreamKey, SequenceNumberWindow> networkMessageWindows =
      new LinkedHashMap<>(16, 0.75f, true);

  private final Map<StreamKey, SequenceNumberWindow> dataSetMessageWindows =
      new LinkedHashMap<>(16, 0.75f, true);

  private final String readerPath;
  private final int dataSetMessageBitWidth;
  private final long discardNanos;

  private boolean evictionWarned = false;

  private volatile @Nullable UInteger lastAcceptedDataSetMessageSequenceNumber;

  /**
   * @param readerPath the reader's component path, for logging.
   * @param dataSetMessageBitWidth the wire width N of the reader's DataSetMessage sequence numbers:
   *     16 for the UADP mapping, 32 otherwise.
   * @param discardNanos the §7.2.3 record-discard period (two times the messageReceiveTimeout), or
   *     0 to disable time-based discard.
   */
  ReaderSequenceTracker(String readerPath, int dataSetMessageBitWidth, long discardNanos) {
    this.readerPath = readerPath;
    this.dataSetMessageBitWidth = dataSetMessageBitWidth;
    this.discardNanos = discardNanos;
  }

  /**
   * Classify a NetworkMessage sequence number against its (PublisherId, WriterGroupId) stream
   * window without advancing it. A rejected classification counts toward the restart-recovery
   * heuristic (see class Javadoc).
   */
  SequenceNumberWindow.Classification classifyNetworkMessage(
      @Nullable PublisherId publisherId,
      @Nullable UShort writerGroupId,
      UShort sequenceNumber,
      long nowNanos) {

    return classify(
        networkMessageWindows,
        key(publisherId, writerGroupId),
        NETWORK_MESSAGE_BIT_WIDTH,
        sequenceNumber.longValue(),
        nowNanos,
        "NetworkMessage");
  }

  /**
   * Advance the (PublisherId, WriterGroupId) stream window to an accepted sequence number. Called
   * for every NEW NetworkMessage, including those carrying only keep-alive DataSetMessages: the
   * next-expected rule (§7.2.4.5.8) applies to the DataSetMessage sequence number only, while the
   * publisher consumes a NetworkMessage sequence number for every NetworkMessage it sends (§7.2.3).
   */
  void acceptNetworkMessage(
      @Nullable PublisherId publisherId,
      @Nullable UShort writerGroupId,
      UShort sequenceNumber,
      long nowNanos) {

    window(
            networkMessageWindows,
            key(publisherId, writerGroupId),
            NETWORK_MESSAGE_BIT_WIDTH,
            nowNanos)
        .accept(sequenceNumber.longValue(), nowNanos);
  }

  /**
   * Classify a DataSetMessage sequence number against its (PublisherId, DataSetWriterId) stream
   * window without advancing it. A rejected classification counts toward the restart-recovery
   * heuristic (see class Javadoc).
   */
  SequenceNumberWindow.Classification classifyDataSetMessage(
      @Nullable PublisherId publisherId,
      @Nullable UShort dataSetWriterId,
      UInteger sequenceNumber,
      long nowNanos) {

    return classify(
        dataSetMessageWindows,
        key(publisherId, dataSetWriterId),
        dataSetMessageBitWidth,
        sequenceNumber.longValue(),
        nowNanos,
        "DataSetMessage");
  }

  /**
   * Classify {@code received} against the stream's window, recording rejections and applying the
   * restart-recovery discard when no time-based discard is armed: after {@value
   * #RESTART_REJECTION_THRESHOLD} consecutive rejections the record is forgotten (WARN, once per
   * occurrence), so the message after the one that tripped the threshold accepts and re-seeds.
   */
  private SequenceNumberWindow.Classification classify(
      Map<StreamKey, SequenceNumberWindow> windows,
      StreamKey key,
      int bitWidth,
      long received,
      long nowNanos,
      String windowKind) {

    SequenceNumberWindow window = window(windows, key, bitWidth, nowNanos);
    SequenceNumberWindow.Classification classification = window.classify(received);

    if (classification != SequenceNumberWindow.Classification.NEW
        && window.recordRejection() >= RESTART_REJECTION_THRESHOLD
        && discardNanos == 0) {
      // with messageReceiveTimeout disabled there is no §7.2.3 time-based discard, and a
      // publisher that restarted its numbering would stay wedged forever; discard the record so
      // the next message accepts and re-seeds (documented Milo extension — inert when a
      // time-based discard is armed, where the spec semantics stand; see class Javadoc)
      window.reset();
      LOGGER.warn(
          "reader {} discarding the {} sequence-number record of stream {} after {} consecutive"
              + " stale/invalid messages with no messageReceiveTimeout configured; the next"
              + " message re-seeds the stream (restarted publisher?)",
          readerPath,
          windowKind,
          key,
          RESTART_REJECTION_THRESHOLD);
    }

    return classification;
  }

  /** Advance the (PublisherId, DataSetWriterId) stream window to an accepted sequence number. */
  void acceptDataSetMessage(
      @Nullable PublisherId publisherId,
      @Nullable UShort dataSetWriterId,
      UInteger sequenceNumber,
      long nowNanos) {

    window(
            dataSetMessageWindows,
            key(publisherId, dataSetWriterId),
            dataSetMessageBitWidth,
            nowNanos)
        .accept(sequenceNumber.longValue(), nowNanos);

    lastAcceptedDataSetMessageSequenceNumber = sequenceNumber;
  }

  /**
   * Observe a keep-alive carrying the next expected DataSetMessage sequence number (§7.2.4.5.8,
   * §7.2.5.4.1): seeds an unseeded (PublisherId, DataSetWriterId) stream window to {@code
   * nextExpected - 1}; never advances a seeded window on a consistent carried value and never
   * counts as stale, but reseeds the window to {@code nextExpected - 1} (WARN, once per occurrence)
   * when the carried value classifies STALE or INVALID — the publisher is authoritative about its
   * own counter, so an inconsistent keep-alive means the window is wrong (see class Javadoc).
   * Always refreshes the stream's §7.2.3 record-discard clock: a keep-alive is a received message.
   */
  void observeKeepAlive(
      @Nullable PublisherId publisherId,
      @Nullable UShort dataSetWriterId,
      UInteger nextExpected,
      long nowNanos) {

    StreamKey key = key(publisherId, dataSetWriterId);

    boolean reseeded =
        window(dataSetMessageWindows, key, dataSetMessageBitWidth, nowNanos)
            .observeKeepAlive(nextExpected.longValue(), nowNanos);

    if (reseeded) {
      LOGGER.warn(
          "reader {} reseeding the DataSetMessage sequence-number window of stream {} to {} - 1:"
              + " a keep-alive carried a next expected sequence number inconsistent with the"
              + " current window, and the publisher is authoritative about its own counter"
              + " (restarted publisher, or a reordered stale keep-alive that the next current"
              + " keep-alive heals)",
          readerPath,
          key,
          nextExpected);
    }
  }

  /**
   * The sequence number of the last DataSetMessage accepted by this tracker, or {@code null} if
   * none was accepted since the last reset. The natural source for a future Part 14 Table 331
   * {@code MessageSequenceNumber} LiveValue.
   */
  @Nullable UInteger lastAcceptedDataSetMessageSequenceNumber() {
    return lastAcceptedDataSetMessageSequenceNumber;
  }

  private SequenceNumberWindow window(
      Map<StreamKey, SequenceNumberWindow> windows, StreamKey key, int bitWidth, long nowNanos) {

    SequenceNumberWindow window = windows.get(key);
    if (window != null) {
      // §7.2.3 record discard, checked lazily: no received message (accepted or keep-alive) on
      // this stream for two times the message receive timeout — forget the record so the next
      // message re-seeds the stream
      if (discardNanos > 0 && nowNanos - window.lastSeenNanos() > discardNanos) {
        window.reset();
      }
      return window;
    }

    if (windows.size() >= MAX_TRACKED_STREAMS) {
      Iterator<Map.Entry<StreamKey, SequenceNumberWindow>> iterator = windows.entrySet().iterator();
      iterator.next();
      iterator.remove();

      if (!evictionWarned) {
        evictionWarned = true;
        LOGGER.warn(
            "reader {} tracks more than {} sequence-number streams;"
                + " evicting the least recently used (rogue or misconfigured traffic?)",
            readerPath,
            MAX_TRACKED_STREAMS);
      }
    }

    window = new SequenceNumberWindow(bitWidth, nowNanos);
    windows.put(key, window);
    return window;
  }

  private static StreamKey key(@Nullable PublisherId publisherId, @Nullable UShort id) {
    return new StreamKey(publisherId != null ? publisherId.toCanonicalString() : null, id);
  }

  /**
   * A wire-derived stream identity. {@code null} components were absent on the wire and participate
   * in the key; PublisherIds compare by canonical string form, mirroring the matching chain's
   * cross-type comparison (the JSON mapping carries every PublisherId as a String).
   */
  private record StreamKey(@Nullable String publisherId, @Nullable UShort id) {}
}
