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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.junit.jupiter.api.Test;

/**
 * The FileType handle state machine: the mode whitelist, the write-exclusive locking
 * shall-sentences, (Session, fileHandle) identity, per-handle positions and snapshots, the read
 * clamp and EOF contract, write extension and the size cap, SetPosition clamping, the 0x03
 * truncation rule, the OpenCount arithmetic and cap, and session eviction.
 */
class FileHandleManagerTest {

  private static final NodeId SESSION_A = new NodeId(1, "session-a");
  private static final NodeId SESSION_B = new NodeId(1, "session-b");

  private static final byte[] SNAPSHOT = {1, 2, 3, 4, 5, 6, 7, 8};

  private final FileHandleManager manager = new FileHandleManager();

  private UInteger open(NodeId sessionId, int mode) throws UaException {
    return manager.open(sessionId, mode, SNAPSHOT::clone);
  }

  @Test
  void onlyTheThreePart14ModesAreAccepted() throws Exception {
    var accepted = new HashSet<Integer>();

    for (int mode = 0; mode <= 0xFF; mode++) {
      try {
        UInteger handle = open(SESSION_A, mode);
        accepted.add(mode);
        manager.close(SESSION_A, handle);
      } catch (UaException e) {
        assertEquals(StatusCodes.Bad_InvalidArgument, e.getStatusCode().getValue(), "mode " + mode);
      }
    }

    // 0x00, bare Write 0x02, 0x07, Append combinations, and reserved bits are never legal
    assertEquals(Set.of(0x01, 0x03, 0x06), accepted);
  }

  @Test
  void writeOpenRequiresZeroExistingHandlesIncludingOwnReaders() throws Exception {
    UInteger reader = open(SESSION_A, 0x01);

    // the requesting session's own read handle blocks a write open (Bad_NotWritable)
    UaException e1 = assertThrows(UaException.class, () -> open(SESSION_A, 0x06));
    assertEquals(StatusCodes.Bad_NotWritable, e1.getStatusCode().getValue());

    UaException e2 = assertThrows(UaException.class, () -> open(SESSION_B, 0x03));
    assertEquals(StatusCodes.Bad_NotWritable, e2.getStatusCode().getValue());

    manager.close(SESSION_A, reader);

    UInteger writer = open(SESSION_A, 0x03);

    // read open fails only while a write handle exists (Bad_NotReadable)
    UaException e3 = assertThrows(UaException.class, () -> open(SESSION_B, 0x01));
    assertEquals(StatusCodes.Bad_NotReadable, e3.getStatusCode().getValue());

    manager.close(SESSION_A, writer);

    // parallel readers are fine
    UInteger r1 = open(SESSION_A, 0x01);
    UInteger r2 = open(SESSION_B, 0x01);
    assertNotEquals(r1, r2);
    assertEquals(2, manager.openCount());
  }

  @Test
  void handleIdentityIsSessionAndHandle() throws Exception {
    UInteger handle = open(SESSION_A, 0x01);

    // a foreign session presenting the same numeric handle: Bad_InvalidArgument on every method
    long code = StatusCodes.Bad_InvalidArgument;
    assertEquals(
        code,
        assertThrows(UaException.class, () -> manager.read(SESSION_B, handle, 1, 100))
            .getStatusCode()
            .getValue());
    assertEquals(
        code,
        assertThrows(
                UaException.class,
                () -> manager.write(SESSION_B, handle, ByteString.of(new byte[] {1})))
            .getStatusCode()
            .getValue());
    assertEquals(
        code,
        assertThrows(UaException.class, () -> manager.position(SESSION_B, handle))
            .getStatusCode()
            .getValue());
    assertEquals(
        code,
        assertThrows(UaException.class, () -> manager.setPosition(SESSION_B, handle, 0))
            .getStatusCode()
            .getValue());
    assertEquals(
        code,
        assertThrows(UaException.class, () -> manager.checkWriteHandle(SESSION_B, handle))
            .getStatusCode()
            .getValue());
    assertEquals(
        code,
        assertThrows(UaException.class, () -> manager.close(SESSION_B, handle))
            .getStatusCode()
            .getValue());

    // still valid for the owner
    assertEquals(0, manager.position(SESSION_A, handle));
    manager.close(SESSION_A, handle);
  }

  @Test
  void handleNumbersAreNeverReused() throws Exception {
    UInteger first = open(SESSION_A, 0x01);
    manager.close(SESSION_A, first);
    UInteger second = open(SESSION_A, 0x01);
    assertNotEquals(first, second);
  }

  @Test
  void readClampsAndSignalsEofWithEmptyByteString() throws Exception {
    UInteger handle = open(SESSION_A, 0x01);

    // clamp to maxLength
    assertArrayEquals(new byte[] {1, 2, 3}, manager.read(SESSION_A, handle, 100, 3).bytesOrEmpty());
    // clamp to requested length
    assertArrayEquals(new byte[] {4, 5}, manager.read(SESSION_A, handle, 2, 100).bytesOrEmpty());
    // clamp to remaining
    assertArrayEquals(
        new byte[] {6, 7, 8}, manager.read(SESSION_A, handle, 100, 100).bytesOrEmpty());
    // EOF: Good + empty ByteString
    assertEquals(0, manager.read(SESSION_A, handle, 100, 100).length());

    // non-positive length: Bad_InvalidArgument
    UaException e = assertThrows(UaException.class, () -> manager.read(SESSION_A, handle, 0, 100));
    assertEquals(StatusCodes.Bad_InvalidArgument, e.getStatusCode().getValue());
  }

  @Test
  void modeStateRulesGovernReadAndWrite() throws Exception {
    UInteger writeOnly = open(SESSION_A, 0x06);
    UaException e1 =
        assertThrows(UaException.class, () -> manager.read(SESSION_A, writeOnly, 1, 100));
    assertEquals(StatusCodes.Bad_InvalidState, e1.getStatusCode().getValue());
    manager.close(SESSION_A, writeOnly);

    UInteger readOnly = open(SESSION_A, 0x01);
    UaException e2 =
        assertThrows(
            UaException.class,
            () -> manager.write(SESSION_A, readOnly, ByteString.of(new byte[] {1})));
    assertEquals(StatusCodes.Bad_InvalidState, e2.getStatusCode().getValue());
  }

  @Test
  void emptyWriteIsAGoodNoOpAndWritesExtendPastTheEnd() throws Exception {
    UInteger handle = open(SESSION_A, 0x06);

    manager.write(SESSION_A, handle, null);
    manager.write(SESSION_A, handle, ByteString.of(new byte[0]));
    assertEquals(0, manager.position(SESSION_A, handle));

    manager.write(SESSION_A, handle, ByteString.of(new byte[] {10, 11, 12}));
    assertEquals(3, manager.position(SESSION_A, handle));

    // overwrite in place + extend past the end
    manager.setPosition(SESSION_A, handle, 2);
    manager.write(SESSION_A, handle, ByteString.of(new byte[] {20, 21, 22}));
    assertEquals(5, manager.position(SESSION_A, handle));

    byte[] content = manager.closeWriteHandle(SESSION_A, handle);
    assertArrayEquals(new byte[] {10, 11, 20, 21, 22}, content);
  }

  @Test
  void writesGrowingTheBufferPastTheSizeCapAreRejected() throws Exception {
    UInteger handle = open(SESSION_A, 0x06);

    // filling to exactly the cap is accepted
    manager.write(SESSION_A, handle, ByteString.of(new byte[FileHandleManager.MAX_FILE_SIZE]));
    assertEquals(FileHandleManager.MAX_FILE_SIZE, manager.position(SESSION_A, handle));

    // one byte more would grow the buffer past the cap: Bad_ResourceUnavailable, state untouched
    UaException e =
        assertThrows(
            UaException.class,
            () -> manager.write(SESSION_A, handle, ByteString.of(new byte[] {1})));
    assertEquals(StatusCodes.Bad_ResourceUnavailable, e.getStatusCode().getValue());
    assertEquals(FileHandleManager.MAX_FILE_SIZE, manager.position(SESSION_A, handle));
  }

  @Test
  void setPositionClampsToEofAndPositionsArePerHandle() throws Exception {
    UInteger h1 = open(SESSION_A, 0x01);
    UInteger h2 = open(SESSION_A, 0x01);

    manager.setPosition(SESSION_A, h1, 1000);
    assertEquals(SNAPSHOT.length, manager.position(SESSION_A, h1));
    assertEquals(0, manager.position(SESSION_A, h2));

    // UInt64 positions >= 2^63 arrive as negative raw bits and clamp to EOF, not to 0
    manager.setPosition(SESSION_A, h1, 0xFFFFFFFFFFFFFFFFL);
    assertEquals(SNAPSHOT.length, manager.position(SESSION_A, h1));

    manager.setPosition(SESSION_A, h1, 4);
    assertArrayEquals(
        new byte[] {5, 6, 7, 8}, manager.read(SESSION_A, h1, 100, 100).bytesOrEmpty());
    assertArrayEquals(new byte[] {1, 2}, manager.read(SESSION_A, h2, 2, 100).bytesOrEmpty());
  }

  @Test
  void readWriteHandleReadsTheWriteBufferNotTheSnapshot() throws Exception {
    UInteger handle = open(SESSION_A, 0x03);

    manager.write(SESSION_A, handle, ByteString.of(new byte[] {99}));
    manager.setPosition(SESSION_A, handle, 0);

    // the handle's coherent read-modify-write view: reads see the buffered write
    assertArrayEquals(
        new byte[] {99, 2, 3, 4, 5, 6, 7, 8},
        manager.read(SESSION_A, handle, 100, 100).bytesOrEmpty());
  }

  @Test
  void closeAndUpdateTruncatesReadWriteBuffersAtTheFinalWritePosition() throws Exception {
    // a shorter in-place rewrite: stale tail bytes are killed
    UInteger handle = open(SESSION_A, 0x03);
    manager.write(SESSION_A, handle, ByteString.of(new byte[] {50, 51, 52}));
    manager.checkWriteHandle(SESSION_A, handle);
    assertArrayEquals(new byte[] {50, 51, 52}, manager.closeWriteHandle(SESSION_A, handle));

    // an untouched 0x03 buffer decodes as the unmodified snapshot
    UInteger untouched = open(SESSION_A, 0x03);
    manager.checkWriteHandle(SESSION_A, untouched);
    assertArrayEquals(SNAPSHOT, manager.closeWriteHandle(SESSION_A, untouched));

    // the truncation point is the FURTHEST write, not the final position
    UInteger rewound = open(SESSION_A, 0x03);
    manager.write(SESSION_A, rewound, ByteString.of(new byte[] {60, 61, 62, 63}));
    manager.setPosition(SESSION_A, rewound, 1);
    manager.write(SESSION_A, rewound, ByteString.of(new byte[] {70}));
    assertArrayEquals(new byte[] {60, 70, 62, 63}, manager.closeWriteHandle(SESSION_A, rewound));
  }

  @Test
  void checkWriteHandleRejectsReadHandlesWithInvalidState() throws Exception {
    UInteger reader = open(SESSION_A, 0x01);
    UaException e =
        assertThrows(UaException.class, () -> manager.checkWriteHandle(SESSION_A, reader));
    assertEquals(StatusCodes.Bad_InvalidState, e.getStatusCode().getValue());
    // the handle survives the failed check
    assertEquals(1, manager.openCount());
  }

  @Test
  void openCountIsLiveAndCapped() throws Exception {
    var handles = new HashSet<UInteger>();
    for (int i = 0; i < FileHandleManager.MAX_OPEN_HANDLES; i++) {
      handles.add(manager.open(SESSION_A, 0x01, () -> new byte[0]));
    }
    assertEquals(FileHandleManager.MAX_OPEN_HANDLES, manager.openCount());

    // OpenCount is served as UInt16: the 65536th open answers Bad_ResourceUnavailable
    UaException e = assertThrows(UaException.class, () -> open(SESSION_A, 0x01));
    assertEquals(StatusCodes.Bad_ResourceUnavailable, e.getStatusCode().getValue());

    manager.close(SESSION_A, handles.iterator().next());
    assertEquals(FileHandleManager.MAX_OPEN_HANDLES - 1, manager.openCount());
  }

  @Test
  void sessionEvictionReleasesHandlesAndTheWriteLock() throws Exception {
    open(SESSION_A, 0x01);
    open(SESSION_A, 0x01);
    open(SESSION_B, 0x01);

    assertEquals(2, manager.evictSession(SESSION_A));
    assertEquals(1, manager.openCount());

    // eviction of a write handle releases the lock
    manager.evictSession(SESSION_B);
    UInteger writer = open(SESSION_A, 0x06);
    assertEquals(1, manager.evictSession(SESSION_A));
    open(SESSION_B, 0x03);
    assertEquals(1, manager.openCount());
  }
}
