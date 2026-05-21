/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.channel.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.charset.StandardCharsets;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ReverseHelloMessageTest {

  private static final String SERVER_URI = "urn:eclipse:milo:reverse-server";

  private static final String ENDPOINT_URL = "opc.tcp://localhost:4840/milo";

  @Test
  public void testTcpMessageRoundTrip() throws UaException {
    ReverseHelloMessage message = new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL);
    ByteBuf buffer = TcpMessageEncoder.encode(message);

    try {
      assertEquals(message, TcpMessageDecoder.decodeReverseHello(buffer));
    } finally {
      buffer.release();
    }
  }

  @Test
  public void testTcpMessageHeaderAndLength() throws UaException {
    ReverseHelloMessage message = new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL);
    ByteBuf buffer = TcpMessageEncoder.encode(message);

    try {
      int expectedLength =
          8
              + Integer.BYTES
              + SERVER_URI.getBytes(StandardCharsets.UTF_8).length
              + Integer.BYTES
              + ENDPOINT_URL.getBytes(StandardCharsets.UTF_8).length;

      assertEquals((byte) 'R', buffer.getByte(0));
      assertEquals((byte) 'H', buffer.getByte(1));
      assertEquals((byte) 'E', buffer.getByte(2));
      assertEquals((byte) 'F', buffer.getByte(3));
      assertEquals(expectedLength, buffer.getUnsignedIntLE(4));
      assertEquals(expectedLength, buffer.readableBytes());
    } finally {
      buffer.release();
    }
  }

  @Test
  public void testDecodeFailsWhenServerUriLengthIsNegative() {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeIntLE(-2);

    assertUaException(StatusCodes.Bad_DecodingError, () -> ReverseHelloMessage.decode(buffer));

    buffer.release();
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 2, 3})
  public void testDecodeFailsWhenServerUriLengthIsTruncated(int byteCount) {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeZero(byteCount);

    assertUaException(StatusCodes.Bad_DecodingError, () -> ReverseHelloMessage.decode(buffer));

    buffer.release();
  }

  @Test
  public void testDecodeFailsWhenEndpointUrlLengthIsNegative() {
    ByteBuf buffer = Unpooled.buffer();
    writeString(SERVER_URI, buffer);
    buffer.writeIntLE(-2);

    assertUaException(StatusCodes.Bad_DecodingError, () -> ReverseHelloMessage.decode(buffer));

    buffer.release();
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 2, 3})
  public void testDecodeFailsWhenEndpointUrlLengthIsTruncated(int byteCount) {
    ByteBuf buffer = Unpooled.buffer();
    writeString(SERVER_URI, buffer);
    buffer.writeZero(byteCount);

    assertUaException(StatusCodes.Bad_DecodingError, () -> ReverseHelloMessage.decode(buffer));

    buffer.release();
  }

  @Test
  public void testDecodeFailsWhenServerUriTooLarge() {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeIntLE(ReverseHelloMessage.MAX_STRING_LENGTH + 1);

    assertThrows(UaException.class, () -> ReverseHelloMessage.decode(buffer));

    buffer.release();
  }

  @Test
  public void testDecodeFailsWhenEndpointUrlTooLarge() {
    ByteBuf buffer = Unpooled.buffer();
    writeString(SERVER_URI, buffer);
    buffer.writeIntLE(ReverseHelloMessage.MAX_STRING_LENGTH + 1);

    assertThrows(UaException.class, () -> ReverseHelloMessage.decode(buffer));

    buffer.release();
  }

  @Test
  public void testDecodeFailsWhenServerUriLengthExceedsReadableBytes() {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeIntLE(SERVER_URI.getBytes(StandardCharsets.UTF_8).length);
    buffer.writeBytes(
        SERVER_URI.substring(0, SERVER_URI.length() - 1).getBytes(StandardCharsets.UTF_8));

    assertUaException(StatusCodes.Bad_DecodingError, () -> ReverseHelloMessage.decode(buffer));

    buffer.release();
  }

  @Test
  public void testDecodeFailsWhenEndpointUrlLengthExceedsReadableBytes() {
    ByteBuf buffer = Unpooled.buffer();
    writeString(SERVER_URI, buffer);
    buffer.writeIntLE(ENDPOINT_URL.getBytes(StandardCharsets.UTF_8).length);
    buffer.writeBytes(
        ENDPOINT_URL.substring(0, ENDPOINT_URL.length() - 1).getBytes(StandardCharsets.UTF_8));

    assertUaException(StatusCodes.Bad_DecodingError, () -> ReverseHelloMessage.decode(buffer));

    buffer.release();
  }

  @Test
  public void testTcpDecodeFailsWhenMessageTypeIsNotReverseHello() {
    ByteBuf buffer = newTcpMessage(MessageType.Hello, 'F');

    assertUaException(
        StatusCodes.Bad_TcpMessageTypeInvalid, () -> TcpMessageDecoder.decodeReverseHello(buffer));

    buffer.release();
  }

  @Test
  public void testTcpDecodeFailsWhenChunkTypeIsNotFinal() {
    ByteBuf buffer = newTcpMessage(MessageType.ReverseHello, 'C');

    assertUaException(
        StatusCodes.Bad_TcpMessageTypeInvalid, () -> TcpMessageDecoder.decodeReverseHello(buffer));

    buffer.release();
  }

  @Test
  public void testEncodeRejectsOverLimitServerUri() {
    String serverUri = "a".repeat(ReverseHelloMessage.MAX_STRING_LENGTH + 1);

    assertThrows(
        IllegalArgumentException.class, () -> new ReverseHelloMessage(serverUri, ENDPOINT_URL));
  }

  @Test
  public void testEncodeRejectsOverLimitEndpointUrl() {
    String endpointUrl = "a".repeat(ReverseHelloMessage.MAX_STRING_LENGTH + 1);

    assertThrows(
        IllegalArgumentException.class, () -> new ReverseHelloMessage(SERVER_URI, endpointUrl));
  }

  @Test
  public void testNullFieldsRoundTrip() throws UaException {
    ReverseHelloMessage message = new ReverseHelloMessage(null, null);
    ByteBuf buffer = TcpMessageEncoder.encode(message);

    try {
      assertEquals(message, TcpMessageDecoder.decodeReverseHello(buffer));
    } finally {
      buffer.release();
    }
  }

  private static void writeString(String s, ByteBuf buffer) {
    byte[] bs = s.getBytes(StandardCharsets.UTF_8);
    buffer.writeIntLE(bs.length);
    buffer.writeBytes(bs);
  }

  private static ByteBuf newTcpMessage(MessageType messageType, char chunkType) {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeMediumLE(MessageType.toMediumInt(messageType));
    buffer.writeByte(chunkType);
    buffer.writeIntLE(8);
    return buffer;
  }

  private static void assertUaException(long statusCode, ThrowingRunnable runnable) {
    UaException exception = assertThrows(UaException.class, runnable);

    assertEquals(statusCode, exception.getStatusCode().value());
  }

  private interface ThrowingRunnable extends org.junit.jupiter.api.function.Executable {}
}
