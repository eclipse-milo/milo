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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
import org.eclipse.milo.opcua.sdk.core.NumericRange;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

  @Test
  void testReadDataTypeDictionaryBytes() throws Exception {
    int[] fragmentSizes = new int[] {1, 2, 1024, 2048, 4096, 8192};
    int[] dictionarySizes = new int[] {1, 2, 1024, 1025, 2048, 2049, 4096, 4097, 8192, 8193};

    for (int fragmentSize : fragmentSizes) {
      for (int dictionarySize : dictionarySizes) {
        byte[] dictionary = new byte[dictionarySize];
        for (int i = 0; i < dictionarySize; i++) {
          byte b = (byte) i;
          if (b == 0) b++; // no null bytes because they get trimmed
          dictionary[i] = b;
        }

        System.out.printf("fragmentSize=%d dictionarySize=%d\n", fragmentSize, dictionarySize);

        testReadDataTypeDictionaryBytes(ByteString.of(dictionary), fragmentSize);
      }
    }
  }

  @Test
  public void readBuiltinDataTypeDictionaryBytes() throws ExecutionException, InterruptedException {
    CompletableFuture<ByteString> future =
        dictionaryReader.readDataTypeDictionaryBytes(
            NodeIds.OpcUa_BinarySchema, Integer.MAX_VALUE // not used for built-in dictionary
            );

    assertNotNull(future.get());
  }

  private void testReadDataTypeDictionaryBytes(ByteString dictionary, int fragmentSize)
      throws Exception {
    Mockito.when(transport.sendRequestMessage(ArgumentMatchers.any(ReadRequest.class)))
        .then(
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
            });

    ByteString typeDictionaryBs =
        dictionaryReader.readDataTypeDictionaryBytes(NodeId.NULL_VALUE, fragmentSize).get();

    Assertions.assertEquals(dictionary, typeDictionaryBs);
  }
}
