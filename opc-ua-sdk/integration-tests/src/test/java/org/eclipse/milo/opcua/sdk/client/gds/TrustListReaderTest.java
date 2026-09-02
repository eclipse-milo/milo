/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.gds;

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.eclipse.milo.opcua.sdk.client.gds.FakeGdsNamespace.TrustListFile;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.OpenFileMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TrustListMasks;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.TrustListDataType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class TrustListReaderTest extends AbstractGdsClientTest {

  /** A body of a few hundred bytes so chunk sizes above and below it are both interesting. */
  private static final TrustListDataType TRUST_LIST =
      new TrustListDataType(
          uint(TrustListMasks.All.getValue()),
          new ByteString[] {ByteString.of(new byte[200]), ByteString.of(new byte[50])},
          new ByteString[] {ByteString.of(new byte[30])},
          new ByteString[] {ByteString.of(new byte[70])},
          new ByteString[0]);

  private TrustListFile file;
  private NodeId trustListId;

  @BeforeEach
  void serveTrustList() {
    file = gds.getApplicationGroupTrustList();
    file.setTrustList(TRUST_LIST);
    trustListId = gds.defaultApplicationGroupTrustListId();
  }

  // The FileType read loop must reassemble the body regardless of how many Read calls it takes:
  // one byte at a time, the reference client's 4096, or a single read larger than the file.
  @ParameterizedTest(name = "chunkSize={0}")
  @ValueSource(ints = {1, 4096, 1_000_000})
  void readsTheWholeBodyInChunksThenClosesTheFile(int chunkSize) throws Exception {
    TrustListDataType read = TrustListReader.read(client, trustListId, chunkSize);

    List<String> calls = file.calls();
    assertEquals(TRUST_LIST, read);
    assertEquals("Open", calls.get(0));
    assertEquals("Close", calls.get(calls.size() - 1));
    assertEquals(1, calls.stream().filter("Close"::equals).count(), "closed exactly once");
    assertTrue(
        calls.subList(1, calls.size() - 1).stream().allMatch(("Read(" + chunkSize + ")")::equals),
        "every Read requested chunkSize bytes: " + calls);
    assertEquals(0, file.openHandles(), "no handle left open");
  }

  // Part 5 C.2.1: MaxByteStringLength is the server's per-call limit; honouring it avoids a
  // Bad_ResponseTooLarge from a server with a small limit.
  @Test
  void readUsesMaxByteStringLengthAsTheChunkSizeWhenTheFileExposesIt() throws Exception {
    TrustListDataType read = TrustListReader.read(client, trustListId);

    assertEquals(TRUST_LIST, read);
    assertTrue(file.calls().contains("Read(100)"), file.calls().toString());
  }

  @Test
  void readFallsBackToTheDefaultChunkSizeWhenMaxByteStringLengthIsAbsent() throws Exception {
    TrustListFile userTokenFile = gds.getUserTokenGroupTrustList();
    userTokenFile.setTrustList(TRUST_LIST);

    TrustListDataType read = TrustListReader.read(client, gds.defaultUserTokenGroupTrustListId());

    assertEquals(TRUST_LIST, read);
    assertTrue(
        userTokenFile.calls().contains("Read(" + TrustListReader.DEFAULT_CHUNK_SIZE + ")"),
        userTokenFile.calls().toString());
  }

  // A lost handle counts against the server's OpenCount until it times out; Close must run even
  // when a Read fails, and the Read's error is what the caller sees.
  @Test
  void closeIsStillCalledWhenAReadFailsMidway() {
    file.failReadAfter(1);

    UaException e =
        assertThrows(UaException.class, () -> TrustListReader.read(client, trustListId, 100));

    assertEquals(StatusCodes.Bad_UnexpectedError, e.getStatusCode().value());
    assertEquals(List.of("Open", "Read(100)", "Read(100)", "Close"), file.calls());
    assertEquals(0, file.openHandles());
  }

  @Test
  void malformedBodyFailsWithBadDecodingErrorAfterClosingTheFile() {
    file.setBody(new byte[] {1, 2, 3});

    UaException e =
        assertThrows(UaException.class, () -> TrustListReader.read(client, trustListId));

    assertEquals(StatusCodes.Bad_DecodingError, e.getStatusCode().value());
    assertEquals("Close", file.calls().get(file.calls().size() - 1));
  }

  // Part 4 §5.12.2.2 lets a caller name a Method by its declaration on the ObjectType that
  // defines it. The reference GDS client and TrustListReader both call FileType_Open on
  // TrustListType instances, so the server must resolve a declaration inherited from a supertype.
  @Test
  void serverResolvesFileTypeDeclarationIdsOnATrustListTypeInstance() throws Exception {
    CallMethodResult open =
        call(
            NodeIds.FileType_Open,
            new Variant[] {Variant.ofByte(ubyte(OpenFileMode.Read.getValue()))});
    CallMethodResult unrelatedMethod =
        call(NodeIds.FileType_GetPosition, new Variant[] {Variant.ofUInt32(uint(1))});

    assertTrue(open.getStatusCode().isGood(), "FileType_Open: " + open);
    assertEquals(StatusCodes.Bad_MethodInvalid, unrelatedMethod.getStatusCode().value());

    Variant[] outputs = requireNonNull(open.getOutputArguments());
    UInteger handle = requireNonNull((UInteger) outputs[0].value());
    CallMethodResult close = call(NodeIds.FileType_Close, new Variant[] {Variant.ofUInt32(handle)});

    assertTrue(close.getStatusCode().isGood(), "FileType_Close: " + close);
    assertEquals(0, file.openHandles());
  }

  private CallMethodResult call(NodeId methodId, Variant[] inputs) throws UaException {
    CallMethodResult[] results =
        client.call(List.of(new CallMethodRequest(trustListId, methodId, inputs))).getResults();

    return requireNonNull(results)[0];
  }
}
