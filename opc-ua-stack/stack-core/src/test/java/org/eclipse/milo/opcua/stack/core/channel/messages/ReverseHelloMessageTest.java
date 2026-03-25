/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.channel.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.junit.jupiter.api.Test;

class ReverseHelloMessageTest {

  @Test
  void roundTripEncodeDecode() throws UaException {
    var original = new ReverseHelloMessage("urn:example:server", "opc.tcp://localhost:4840");

    ByteBuf encoded = TcpMessageEncoder.encode(original);
    ReverseHelloMessage decoded = TcpMessageDecoder.decodeReverseHello(encoded);

    assertEquals(original.getServerUri(), decoded.getServerUri());
    assertEquals(original.getEndpointUrl(), decoded.getEndpointUrl());
    assertEquals(original, decoded);

    encoded.release();
  }

  @Test
  void maxLengthStringsSucceed() throws UaException {
    String maxString = "x".repeat(ReverseHelloMessage.MAX_URI_LENGTH);
    var message = new ReverseHelloMessage(maxString, maxString);

    ByteBuf encoded = TcpMessageEncoder.encode(message);
    ReverseHelloMessage decoded = TcpMessageDecoder.decodeReverseHello(encoded);

    assertEquals(maxString, decoded.getServerUri());
    assertEquals(maxString, decoded.getEndpointUrl());

    encoded.release();
  }

  @Test
  void overLengthServerUriThrows() {
    ByteBuf buffer = Unpooled.buffer();
    int overLength = ReverseHelloMessage.MAX_URI_LENGTH + 1;
    buffer.writeIntLE(overLength);
    buffer.writeBytes(new byte[overLength]);
    buffer.writeIntLE(0);

    assertThrows(UaException.class, () -> ReverseHelloMessage.decode(buffer));

    buffer.release();
  }

  @Test
  void overLengthEndpointUrlThrows() {
    ByteBuf buffer = Unpooled.buffer();
    // valid serverUri
    buffer.writeIntLE(3);
    buffer.writeBytes(new byte[] {'u', 'r', 'n'});
    // over-length endpointUrl
    int overLength = ReverseHelloMessage.MAX_URI_LENGTH + 1;
    buffer.writeIntLE(overLength);
    buffer.writeBytes(new byte[overLength]);

    assertThrows(UaException.class, () -> ReverseHelloMessage.decode(buffer));

    buffer.release();
  }

  @Test
  void emptyStringsRoundTrip() throws UaException {
    var original = new ReverseHelloMessage("", "");

    ByteBuf encoded = TcpMessageEncoder.encode(original);
    ReverseHelloMessage decoded = TcpMessageDecoder.decodeReverseHello(encoded);

    assertEquals("", decoded.getServerUri());
    assertEquals("", decoded.getEndpointUrl());

    encoded.release();
  }

  @Test
  void messageTypeReverseHelloRoundTrip() throws UaException {
    int mediumInt = MessageType.toMediumInt(MessageType.ReverseHello);
    MessageType decoded = MessageType.fromMediumInt(mediumInt);

    assertEquals(MessageType.ReverseHello, decoded);
  }

  @Test
  void constructorRejectsOverLengthServerUri() {
    String overLength = "x".repeat(ReverseHelloMessage.MAX_URI_LENGTH + 1);
    assertThrows(
        IllegalArgumentException.class,
        () -> new ReverseHelloMessage(overLength, "opc.tcp://localhost:4840"));
  }

  @Test
  void constructorRejectsOverLengthEndpointUrl() {
    String overLength = "x".repeat(ReverseHelloMessage.MAX_URI_LENGTH + 1);
    assertThrows(
        IllegalArgumentException.class,
        () -> new ReverseHelloMessage("urn:example:server", overLength));
  }

  @Test
  void constructorRejectsNullServerUri() {
    assertThrows(
        NullPointerException.class,
        () -> new ReverseHelloMessage(null, "opc.tcp://localhost:4840"));
  }

  @Test
  void constructorRejectsNullEndpointUrl() {
    assertThrows(
        NullPointerException.class, () -> new ReverseHelloMessage("urn:example:server", null));
  }

  @Test
  void decodeFailsWhenUriTooLarge() throws UaException {
    for (int i = ReverseHelloMessage.MAX_URI_LENGTH - 1;
        i <= ReverseHelloMessage.MAX_URI_LENGTH + 1;
        i++) {
      ByteBuf buffer = Unpooled.buffer();
      buffer.writeIntLE(i);
      buffer.writeBytes(new byte[i]);
      // endpointUrl = empty string
      buffer.writeIntLE(0);

      if (i <= ReverseHelloMessage.MAX_URI_LENGTH) {
        assertNotNull(ReverseHelloMessage.decode(buffer));
      } else {
        assertThrows(UaException.class, () -> ReverseHelloMessage.decode(buffer));
      }
      buffer.release();
    }
  }
}
