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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.MoreObjects;
import io.netty.buffer.ByteBuf;
import java.nio.charset.StandardCharsets;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;

/**
 * Represents a ReverseHello message as defined in OPC UA Part 6, Section 7.1.2.6.
 *
 * <p>A ReverseHello is sent by a server to a client over a TCP connection that the server
 * initiated. It carries the server's ApplicationUri and an endpoint URL so the client can identify
 * the server and begin the normal Hello/Acknowledge handshake on the same socket.
 */
public class ReverseHelloMessage {

  static final int MAX_URI_LENGTH = 4096;

  private final String serverUri;

  private final String endpointUrl;

  /**
   * @param serverUri the URI of the Server which is requesting a connection. The encoded value
   *     shall be less than 4096 bytes.
   * @param endpointUrl the URL of the Endpoint which the Client can use to access the Server. The
   *     encoded value shall be less than 4096 bytes.
   */
  public ReverseHelloMessage(String serverUri, String endpointUrl) {
    checkNotNull(serverUri, "serverUri must not be null");
    checkNotNull(endpointUrl, "endpointUrl must not be null");
    checkArgument(
        serverUri.getBytes(StandardCharsets.UTF_8).length <= MAX_URI_LENGTH,
        "serverUri length cannot be greater than %s bytes",
        MAX_URI_LENGTH);
    checkArgument(
        endpointUrl.getBytes(StandardCharsets.UTF_8).length <= MAX_URI_LENGTH,
        "endpointUrl length cannot be greater than %s bytes",
        MAX_URI_LENGTH);

    this.serverUri = serverUri;
    this.endpointUrl = endpointUrl;
  }

  /**
   * @return the URI of the server which is requesting a connection.
   */
  public String getServerUri() {
    return serverUri;
  }

  /**
   * @return the URL of the endpoint which the client can use to access the server.
   */
  public String getEndpointUrl() {
    return endpointUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ReverseHelloMessage that = (ReverseHelloMessage) o;

    return serverUri.equals(that.serverUri) && endpointUrl.equals(that.endpointUrl);
  }

  @Override
  public int hashCode() {
    int result = serverUri.hashCode();
    result = 31 * result + endpointUrl.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("serverUri", serverUri)
        .add("endpointUrl", endpointUrl)
        .toString();
  }

  /**
   * Encode a {@link ReverseHelloMessage} to a {@link ByteBuf}.
   *
   * @param message the message to encode.
   * @param buffer the buffer to write to.
   */
  public static void encode(ReverseHelloMessage message, ByteBuf buffer) {
    encodeString(message.getServerUri(), buffer);
    encodeString(message.getEndpointUrl(), buffer);
  }

  /**
   * Decode a {@link ReverseHelloMessage} from a {@link ByteBuf}.
   *
   * @param buffer the buffer to read from.
   * @return the decoded {@link ReverseHelloMessage}.
   * @throws UaException if the message cannot be decoded.
   */
  public static ReverseHelloMessage decode(ByteBuf buffer) throws UaException {
    String serverUri = decodeString(buffer, "serverUri");
    String endpointUrl = decodeString(buffer, "endpointUrl");
    return new ReverseHelloMessage(serverUri, endpointUrl);
  }

  private static void encodeString(String s, ByteBuf buffer) {
    byte[] bs = s.getBytes(StandardCharsets.UTF_8);
    buffer.writeIntLE(bs.length);
    buffer.writeBytes(bs);
  }

  private static String decodeString(ByteBuf buffer, String fieldName) throws UaException {
    int length = buffer.readIntLE();
    if (length < 0) {
      throw new UaException(
          StatusCodes.Bad_DecodingError, "invalid " + fieldName + " length: " + length);
    }
    if (length > MAX_URI_LENGTH) {
      throw new UaException(
          StatusCodes.Bad_EncodingLimitsExceeded,
          fieldName + " length exceeds " + MAX_URI_LENGTH + ": " + length);
    }
    byte[] bs = new byte[length];
    buffer.readBytes(bs);
    return new String(bs, StandardCharsets.UTF_8);
  }
}
