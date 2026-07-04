/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.server;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * The FileType handle state machine of the {@code PubSubConfiguration} file object (Part 20 §4.2
 * with the Part 14 §9.1.3.7.1 overlay).
 *
 * <p>Deliberately package-private and PubSub-only: not designed for reuse as a generic sdk-server
 * FileType utility; promote later if TrustList/GDS work wants one.
 *
 * <p>Contract highlights:
 *
 * <ul>
 *   <li><b>Modes:</b> only Read ({@code 0x01}), Read+Write ({@code 0x03}), and Write+EraseExisting
 *       ({@code 0x06}) are legal; every other mode — including {@code 0x00}, bare Write {@code
 *       0x02}, {@code 0x07}, any Append combination, EraseExisting without Write, and reserved bits
 *       — is rejected with {@code Bad_InvalidArgument}.
 *   <li><b>Locking:</b> opening for write requires ZERO existing handles of any kind, including the
 *       requesting session's own read handles ({@code Bad_NotWritable}); opening for read fails
 *       only while a write handle exists ({@code Bad_NotReadable}). Milo emits the Part 20
 *       shall-sentence codes and tolerates (never emits) peers' {@code Bad_InvalidState}.
 *   <li><b>Identity:</b> a handle is {@code (Session, fileHandle)}; a handle presented by a foreign
 *       session is {@code Bad_InvalidArgument} even on numeric collision. Handle numbers come from
 *       one monotonically increasing counter and are never reused within the manager's lifetime.
 *   <li><b>Snapshots:</b> the readable stream is materialized AT Open. A {@code 0x03} handle's
 *       write buffer is seeded with a copy of the snapshot and reads on it read the WRITE BUFFER
 *       (the handle's coherent read-modify-write view), not the open snapshot; a {@code 0x06}
 *       buffer starts empty (EraseExisting erases the file buffer only, never the configuration).
 *   <li><b>Read:</b> non-positive length is {@code Bad_InvalidArgument}; responses are clamped to
 *       {@code min(length, maxLength, remaining)}; EOF is Good + an empty ByteString; a handle
 *       without the Read bit answers {@code Bad_InvalidState}.
 *   <li><b>Write:</b> a handle without the Write bit answers {@code Bad_InvalidState}; an empty or
 *       null ByteString is a Good no-op; mid-file writes overwrite in place; writing past the end
 *       extends the buffer.
 *   <li><b>Positions:</b> mode-independent; SetPosition past EOF clamps to EOF, never errors.
 *   <li><b>CloseAndUpdate truncation:</b> {@link #closeWriteHandle} returns the buffer truncated at
 *       the final write position for {@code 0x03} handles (killing stale tail bytes of a shorter
 *       in-place rewrite); an untouched {@code 0x03} buffer (no write issued) is returned as the
 *       unmodified snapshot. {@code 0x06} is the recommended full-rewrite mode.
 *   <li><b>OpenCount cap:</b> more than {@value #MAX_OPEN_HANDLES} concurrent handles (the ns0
 *       OpenCount property is UInt16) answers {@code Bad_ResourceUnavailable} — a spec-silent
 *       corner decided here.
 *   <li><b>Size cap:</b> a write that would grow a buffer past {@value #MAX_FILE_SIZE} bytes
 *       answers {@code Bad_ResourceUnavailable} — the sibling spec-silent resource guard to the
 *       OpenCount cap, bounding heap growth from write loops (any plausible configuration file is
 *       orders of magnitude smaller).
 * </ul>
 *
 * <p>Thread safety: none of its own — every call is made by {@link PubSubConfigurationFace} while
 * holding the face lock, which serializes the eight method handlers and session eviction.
 */
final class FileHandleManager {

  /** Read ({@code 0x01}). */
  static final int MODE_READ = 0x01;

  /** Read + Write ({@code 0x03}). */
  static final int MODE_READ_WRITE = 0x03;

  /** Write + EraseExisting ({@code 0x06}). */
  static final int MODE_WRITE_ERASE = 0x06;

  /** The largest number of concurrently open handles; OpenCount is served as UInt16. */
  static final int MAX_OPEN_HANDLES = 65535;

  /** The largest buffer a write may grow to; larger writes answer Bad_ResourceUnavailable. */
  static final int MAX_FILE_SIZE = 64 * 1024 * 1024;

  private final Map<HandleKey, HandleState> handles = new LinkedHashMap<>();

  /** Monotonically increasing; handle numbers are never reused within this manager's lifetime. */
  private long nextFileHandle = 1;

  /**
   * Open a handle.
   *
   * @param sessionId the owning session's id.
   * @param mode the requested mode byte.
   * @param snapshot supplies the current file content; invoked only after the mode and locking
   *     checks passed, and only for modes that need content ({@code 0x01} and {@code 0x03}).
   * @return the new file handle.
   * @throws UaException {@code Bad_InvalidArgument} for an illegal mode, {@code Bad_NotWritable} /
   *     {@code Bad_NotReadable} on lock conflicts, {@code Bad_ResourceUnavailable} at the handle
   *     cap.
   */
  UInteger open(NodeId sessionId, int mode, Supplier<byte[]> snapshot) throws UaException {
    if (mode != MODE_READ && mode != MODE_READ_WRITE && mode != MODE_WRITE_ERASE) {
      throw new UaException(StatusCodes.Bad_InvalidArgument, "mode setting is invalid: " + mode);
    }

    if ((mode & 0x02) != 0) {
      // write-open requires zero existing handles of any kind, including the requesting
      // session's own read handles (P20 4.2.2 shall-sentence)
      if (!handles.isEmpty()) {
        throw new UaException(StatusCodes.Bad_NotWritable, "the file is already opened");
      }
    } else {
      // read-open fails only against an existing writer; parallel readers are fine
      if (handles.values().stream().anyMatch(HandleState::isWriteMode)) {
        throw new UaException(
            StatusCodes.Bad_NotReadable, "the file is already opened for writing");
      }
    }

    if (handles.size() >= MAX_OPEN_HANDLES) {
      throw new UaException(
          StatusCodes.Bad_ResourceUnavailable, "too many open file handles: " + handles.size());
    }

    byte[] data =
        switch (mode) {
          case MODE_READ -> snapshot.get();
          // the write buffer of a 0x03 handle is seeded with a COPY of the snapshot
          case MODE_READ_WRITE -> snapshot.get().clone();
          // EraseExisting: the buffer starts empty; the configuration itself is untouched
          default -> new byte[0];
        };

    UInteger fileHandle = UInteger.valueOf(nextFileHandle++);
    handles.put(new HandleKey(sessionId, fileHandle), new HandleState(mode, data));

    return fileHandle;
  }

  /**
   * Close a handle, discarding buffered writes (the abort path; only CloseAndUpdate applies).
   *
   * @throws UaException {@code Bad_InvalidArgument} for an unknown or foreign handle.
   */
  void close(NodeId sessionId, UInteger fileHandle) throws UaException {
    HandleKey key = new HandleKey(sessionId, fileHandle);
    if (handles.remove(key) == null) {
      throw new UaException(StatusCodes.Bad_InvalidArgument, "invalid file handle in call");
    }
  }

  /**
   * Read up to {@code min(length, maxLength, remaining)} bytes at the handle's position, advancing
   * it. EOF is Good + an empty ByteString. A {@code 0x03} handle reads its write buffer.
   *
   * @throws UaException {@code Bad_InvalidArgument} for an unknown/foreign handle or a non-positive
   *     length, {@code Bad_InvalidState} for a handle without the Read bit.
   */
  ByteString read(NodeId sessionId, UInteger fileHandle, int length, long maxLength)
      throws UaException {

    HandleState state = getHandle(sessionId, fileHandle);

    if (!state.isReadable()) {
      throw new UaException(StatusCodes.Bad_InvalidState, "file was not opened for read access");
    }
    if (length <= 0) {
      throw new UaException(StatusCodes.Bad_InvalidArgument, "non-positive length: " + length);
    }

    long remaining = state.size - state.position;
    int count = (int) Math.min(Math.min(length, maxLength), remaining);
    if (count <= 0) {
      return ByteString.of(new byte[0]);
    }

    byte[] bytes =
        Arrays.copyOfRange(state.data, (int) state.position, (int) state.position + count);
    state.position += count;

    return ByteString.of(bytes);
  }

  /**
   * Write {@code data} at the handle's position, overwriting in place and extending past the end;
   * an empty or null ByteString is a Good no-op with no effect on position or content.
   *
   * @throws UaException {@code Bad_InvalidArgument} for an unknown/foreign handle, {@code
   *     Bad_InvalidState} for a handle without the Write bit, {@code Bad_ResourceUnavailable} for a
   *     write that would grow the buffer past {@value #MAX_FILE_SIZE} bytes.
   */
  void write(NodeId sessionId, UInteger fileHandle, @Nullable ByteString data) throws UaException {
    HandleState state = getHandle(sessionId, fileHandle);

    if (!state.isWriteMode()) {
      throw new UaException(StatusCodes.Bad_InvalidState, "file was not opened for write access");
    }

    byte[] bytes = data != null ? data.bytesOrEmpty() : new byte[0];
    if (bytes.length == 0) {
      return;
    }

    long end = state.position + bytes.length;
    if (end > state.data.length) {
      if (end > MAX_FILE_SIZE) {
        throw new UaException(
            StatusCodes.Bad_ResourceUnavailable,
            "write would grow the file past " + MAX_FILE_SIZE + " bytes");
      }
      state.data =
          Arrays.copyOf(
              state.data, (int) Math.min(Math.max(end, state.data.length * 2L), MAX_FILE_SIZE));
    }
    System.arraycopy(bytes, 0, state.data, (int) state.position, bytes.length);

    state.position = end;
    state.size = Math.max(state.size, (int) end);
    state.finalWritePosition = Math.max(state.finalWritePosition, end);
  }

  /**
   * Get the handle's position (mode-independent).
   *
   * @throws UaException {@code Bad_InvalidArgument} for an unknown or foreign handle.
   */
  long position(NodeId sessionId, UInteger fileHandle) throws UaException {
    return getHandle(sessionId, fileHandle).position;
  }

  /**
   * Set the handle's position (mode-independent), clamping past-EOF values to EOF.
   *
   * @param position the requested position as raw UInt64 bits; positions &ge; 2^63 arrive negative
   *     as a signed long and clamp to EOF like any other past-EOF value (Part 20 §4.2.7).
   * @throws UaException {@code Bad_InvalidArgument} for an unknown or foreign handle.
   */
  void setPosition(NodeId sessionId, UInteger fileHandle, long position) throws UaException {
    HandleState state = getHandle(sessionId, fileHandle);
    state.position = Long.compareUnsigned(position, state.size) > 0 ? state.size : position;
  }

  /**
   * Check the handle prefix of a CloseAndUpdate call without closing it: the handle must exist,
   * belong to {@code sessionId}, and carry the Write bit.
   *
   * @throws UaException {@code Bad_InvalidArgument} for an unknown/foreign handle, {@code
   *     Bad_InvalidState} for a handle without the Write bit.
   */
  void checkWriteHandle(NodeId sessionId, UInteger fileHandle) throws UaException {
    HandleState state = getHandle(sessionId, fileHandle);
    if (!state.isWriteMode()) {
      throw new UaException(StatusCodes.Bad_InvalidState, "file was not opened for write access");
    }
  }

  /**
   * Close a write handle and return its buffer for CloseAndUpdate: {@code 0x03} buffers are
   * truncated at the final write position (an untouched {@code 0x03} buffer is the unmodified
   * snapshot); {@code 0x06} buffers are returned whole. The caller must have called {@link
   * #checkWriteHandle} first — closure happens whenever the handle/mode checks passed, whether or
   * not changes are subsequently applied.
   */
  byte[] closeWriteHandle(NodeId sessionId, UInteger fileHandle) {
    HandleState state = handles.remove(new HandleKey(sessionId, fileHandle));
    if (state == null || !state.isWriteMode()) {
      throw new IllegalStateException("checkWriteHandle was not called first");
    }

    if (state.mode == MODE_READ_WRITE && state.finalWritePosition > 0) {
      return Arrays.copyOf(state.data, (int) state.finalWritePosition);
    }
    return Arrays.copyOf(state.data, state.size);
  }

  /**
   * Evict every handle of {@code sessionId} on session close, releasing the write lock if held.
   *
   * @return the number of handles evicted.
   */
  int evictSession(NodeId sessionId) {
    int before = handles.size();
    handles.keySet().removeIf(key -> key.sessionId().equals(sessionId));
    return before - handles.size();
  }

  /** Evict every handle (face shutdown). */
  void evictAll() {
    handles.clear();
  }

  /** The number of currently valid handles: the ns0 OpenCount value. */
  int openCount() {
    return handles.size();
  }

  private HandleState getHandle(NodeId sessionId, UInteger fileHandle) throws UaException {
    HandleState state = handles.get(new HandleKey(sessionId, fileHandle));
    if (state == null) {
      throw new UaException(StatusCodes.Bad_InvalidArgument, "invalid file handle in call");
    }
    return state;
  }

  /** Handle identity: {@code (Session, fileHandle)} (P20 4.2.2). */
  private record HandleKey(NodeId sessionId, UInteger fileHandle) {}

  private static final class HandleState {

    private final int mode;

    /**
     * The handle's content: the read snapshot ({@code 0x01}) or the mutable write buffer ({@code
     * 0x03}/{@code 0x06}); {@code data.length} may exceed {@link #size} (growth headroom).
     */
    private byte[] data;

    /** The logical content length. */
    private int size;

    private long position = 0;

    /** The furthest position any write has reached; drives the {@code 0x03} truncation rule. */
    private long finalWritePosition = 0;

    private HandleState(int mode, byte[] data) {
      this.mode = mode;
      this.data = data;
      this.size = data.length;
    }

    private boolean isReadable() {
      return (mode & 0x01) != 0;
    }

    private boolean isWriteMode() {
      return (mode & 0x02) != 0;
    }
  }
}
