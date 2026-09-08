/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.dtd;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
import org.eclipse.milo.opcua.sdk.core.NumericRange;
import org.eclipse.milo.opcua.sdk.core.dtd.BsdParser;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

class BinaryDataTypeDictionaryReaderTest {

  private final OpcTcpClientTransport transport = Mockito.mock(OpcTcpClientTransport.class);
  private final OpcTcpClientTransportConfig transportConfig =
      Mockito.mock(OpcTcpClientTransportConfig.class);

  private final OpcUaClient client = Mockito.mock(OpcUaClient.class);
  private final OpcUaClientConfig clientConfig = Mockito.mock(OpcUaClientConfig.class);

  private final OpcUaSession session = Mockito.mock(OpcUaSession.class);

  private final BinaryDataTypeDictionaryReader dictionaryReader =
      new BinaryDataTypeDictionaryReader(client);

  @BeforeEach
  void setUp() {
    Mockito.when(client.getConfig()).thenReturn(clientConfig);
    Mockito.when(client.getConfig().getRequestTimeout()).thenReturn(uint(5000));
    Mockito.when(client.getTransport()).thenReturn(transport);
    Mockito.when(client.getTransport().getConfig()).thenReturn(transportConfig);
    Mockito.when(client.getTransport().getConfig().getExecutor())
        .thenReturn(Stack.sharedExecutor());
    Mockito.when(client.getSessionAsync()).thenReturn(completedFuture(session));
  }

  void assemblesFragments(int fragmentSize, int dictionarySize) throws Exception {
    byte[] dictionary = new byte[dictionarySize];
    for (int i = 0; i < dictionarySize; i++) dictionary[i] = (byte) ('A' + i % 26);
    testReadDataTypeDictionaryBytes(ByteString.of(dictionary), fragmentSize);
  }

  @TestFactory
  Stream<DynamicTest> fragmentBoundaries() {
    return Stream.of(
            new Boundary("one byte exact boundary", 1, 3),
            new Boundary("two byte exact boundary", 2, 6),
            new Boundary("two byte final short fragment", 2, 7),
            new Boundary("short first fragment", 1024, 1023),
            new Boundary("one exact fragment", 1024, 1024),
            new Boundary("one byte past boundary", 1024, 1025),
            new Boundary("multiple exact fragments", 2048, 6144),
            new Boundary("large payload with final short fragment", 4096, 65537))
        .map(
            c ->
                DynamicTest.dynamicTest(
                    c.name(), () -> assemblesFragments(c.fragmentSize(), c.dictionarySize())));
  }

  // Servers may terminate with empty data or a status, or ignore IndexRange altogether.
  void handlesServerResponses(List<DataValue> responses, String expected) throws Exception {
    var ranges = new ArrayList<String>();
    Mockito.doAnswer(
            invocation -> {
              ReadRequest request = invocation.getArgument(0);
              int index = ranges.size();
              ranges.add(Objects.requireNonNull(request.getNodesToRead())[0].getIndexRange());
              assertTrue(index < responses.size(), "reader requested an unexpected extra fragment");
              return completedFuture(
                  new ReadResponse(null, new DataValue[] {responses.get(index)}, null));
            })
        .when(transport)
        .sendRequestMessage(ArgumentMatchers.any(ReadRequest.class));

    ByteString actual =
        dictionaryReader.readDataTypeDictionaryBytes(NodeId.NULL_VALUE, 4).get(5, TimeUnit.SECONDS);
    assertEquals(ByteString.of(expected.getBytes(StandardCharsets.UTF_8)), actual);
    assertEquals(responses.size(), ranges.size());
    for (int i = 0; i < ranges.size(); i++) {
      assertEquals((i * 4) + ":" + (i * 4 + 3), ranges.get(i));
    }
  }

  @TestFactory
  Stream<DynamicTest> responseCases() {
    DataValue prefix = bytes("<x/>");
    return Stream.of(
            new ResponseCase(
                "trims trailing NUL and whitespace", List.of(prefix, bytes("\0 \n")), "<x/>"),
            new ResponseCase(
                "server ignores IndexRange", List.of(bytes("<dictionary/>")), "<dictionary/>"),
            new ResponseCase(
                "null fragment retains prefix",
                List.of(prefix, new DataValue(Variant.NULL_VALUE)),
                "<x/>"),
            new ResponseCase("empty fragment retains prefix", List.of(prefix, bytes("")), "<x/>"),
            new ResponseCase(
                "null ByteString retains prefix",
                List.of(prefix, new DataValue(new Variant(ByteString.NULL_VALUE))),
                "<x/>"),
            new ResponseCase(
                "exact fragment ends with no data",
                List.of(prefix, new DataValue(new StatusCode(StatusCodes.Bad_IndexRangeNoData))),
                "<x/>"),
            new ResponseCase(
                "other bad status retains prefix",
                List.of(prefix, new DataValue(new StatusCode(StatusCodes.Bad_UnexpectedError))),
                "<x/>"),
            new ResponseCase(
                "initial bad status returns empty bytes",
                List.of(new DataValue(new StatusCode(StatusCodes.Bad_NodeIdUnknown))),
                ""))
        .map(
            c ->
                DynamicTest.dynamicTest(
                    c.name(), () -> handlesServerResponses(c.responses(), c.expected())));
  }

  private record Boundary(String name, int fragmentSize, int dictionarySize) {}

  private record ResponseCase(String name, List<DataValue> responses, String expected) {}

  private static DataValue bytes(String text) {
    return new DataValue(new Variant(ByteString.of(text.getBytes(StandardCharsets.UTF_8))));
  }

  @Test
  void readBuiltinDataTypeDictionaryBytes() throws Exception {
    ByteString bytes =
        dictionaryReader
            .readDataTypeDictionaryBytes(NodeIds.OpcUa_BinarySchema, Integer.MAX_VALUE)
            .get(5, TimeUnit.SECONDS);
    var dictionary = BsdParser.parse(new ByteArrayInputStream(bytes.bytesOrEmpty()));
    assertEquals("http://opcfoundation.org/UA/", dictionary.getTargetNamespace());
    assertTrue(
        dictionary.getOpaqueTypeOrEnumeratedTypeOrStructuredType().stream()
            .anyMatch(type -> type.getName().equals("Argument")));
    Mockito.verify(transport, Mockito.never())
        .sendRequestMessage(ArgumentMatchers.any(ReadRequest.class));
  }

  private void testReadDataTypeDictionaryBytes(ByteString dictionary, int fragmentSize)
      throws Exception {
    Mockito.doAnswer(
            invocationOnMock -> {
              ReadRequest readRequest = invocationOnMock.getArgument(0);

              List<ReadValueId> readValueIds =
                  Arrays.stream(Objects.requireNonNull(readRequest.getNodesToRead())).toList();

              ReadValueId readValueId = readValueIds.get(0);

              NumericRange numericRange = NumericRange.parse(readValueId.getIndexRange());

              try {
                Object fragment =
                    NumericRange.readFromValueAtRange(new Variant(dictionary), numericRange);

                return completedFuture(
                    new ReadResponse(
                        null, new DataValue[] {new DataValue(new Variant(fragment))}, null));
              } catch (UaException e) {
                return completedFuture(
                    new ReadResponse(
                        null, new DataValue[] {new DataValue(e.getStatusCode())}, null));
              }
            })
        .when(transport)
        .sendRequestMessage(ArgumentMatchers.any(ReadRequest.class));

    ByteString typeDictionaryBs =
        dictionaryReader
            .readDataTypeDictionaryBytes(NodeId.NULL_VALUE, fragmentSize)
            .get(5, TimeUnit.SECONDS);

    Assertions.assertEquals(dictionary, typeDictionaryBs);
  }
}
