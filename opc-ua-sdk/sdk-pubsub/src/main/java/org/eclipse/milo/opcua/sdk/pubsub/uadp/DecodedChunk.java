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

import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.Nullable;

/**
 * The chunk payload of one Chunk NetworkMessage (OPC UA Part 14 §7.2.4.4.4, Tables 158/159): one
 * piece of a DataSetMessage that was split across multiple NetworkMessages.
 *
 * <p>Surfaced as {@link DecodedNetworkMessage#chunk()} — a component rather than a sealed sibling
 * because a chunk NetworkMessage still carries the full plaintext header and consumes a
 * NetworkMessage sequence number, so the per-reader NetworkMessage recency windows must observe it
 * like any other NetworkMessage. When {@code chunk} is non-null, {@code messages()} and {@code
 * metaData()} are empty; reassembly happens downstream, per connection, after all security
 * processing (each chunk is secured individually).
 *
 * <p>The chunk fields live inside the (possibly encrypted) payload region; {@link #chunkData()} is
 * a fresh copy made by the decoder — it never aliases the transport buffer or a decoder-local
 * decrypted copy, so it may safely outlive the decode call. The array is owned by this record and
 * is not defensively copied again; like arrays generally, it compares by reference in {@code
 * equals}.
 *
 * @param dataSetWriterId the DataSetWriterId from the chunk PayloadHeader (Table 158, a single
 *     UInt16 with no Count byte), or {@code null} when the PayloadHeader is absent.
 * @param messageSequenceNumber the sequence number of the chunked payload; all chunks of one
 *     payload carry the same value, and a newer value abandons any incomplete predecessor.
 * @param chunkOffset the byte offset of {@link #chunkData()} within the reassembled payload.
 * @param totalSize the total size in bytes of the reassembled payload.
 * @param chunkData this chunk's piece of the payload; owned by this record.
 */
public record DecodedChunk(
    @Nullable UShort dataSetWriterId,
    UShort messageSequenceNumber,
    UInteger chunkOffset,
    UInteger totalSize,
    byte[] chunkData) {}
