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

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.MoreObjects;
import io.netty.buffer.ByteBuf;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.jspecify.annotations.Nullable;

/**
 * UA-TCP {@code ReverseHello} payload sent by a server immediately after it opens a reverse
 * connection to a client listener.
 *
 * <p>The message is part of the unencrypted UACP handshake and only carries routing information:
 * the server application URI and the endpoint URL the client should use in its subsequent {@link
 * HelloMessage}. It does not authenticate the server; identity checks still belong to the normal
 * SecureChannel and Session negotiation that follows.
 *
 * <p>OPC UA Part 6 limits both {@code ReverseHello} string fields to 4096 encoded bytes. The
 * constructor and decoder enforce that per-field limit independently from the normal {@link
 * HelloMessage} endpoint URL limit.
 */
public class ReverseHelloMessage {

  // OPC UA Part 6 fixes each ReverseHello string field at a maximum of 4096 bytes.
  static final int MAX_STRING_LENGTH = 4096;

  private final @Nullable String serverUri;

  private final @Nullable String endpointUrl;

  /**
   * @param serverUri the application URI of the Server that opened the reverse connection. The
   *     encoded value shall be at most 4096 bytes.
   * @param endpointUrl the URL of the Endpoint that the Client sends in its Hello message. The
   *     encoded value shall be at most 4096 bytes.
   * @throws IllegalArgumentException if either encoded string is longer than 4096 bytes.
   */
  public ReverseHelloMessage(@Nullable String serverUri, @Nullable String endpointUrl) {
    checkArgument(
        serverUri == null || serverUri.getBytes(StandardCharsets.UTF_8).length <= MAX_STRING_LENGTH,
        "serverUri encoded length cannot be greater than 4096 bytes");

    checkArgument(
        endpointUrl == null
            || endpointUrl.getBytes(StandardCharsets.UTF_8).length <= MAX_STRING_LENGTH,
        "endpointUrl encoded length cannot be greater than 4096 bytes");

    this.serverUri = serverUri;
    this.endpointUrl = endpointUrl;
  }

  /**
   * Get the application URI advertised by the server that opened the reverse connection.
   *
   * @return the server application URI, or {@code null} if the sender encoded a null string.
   */
  @Nullable
  public String getServerUri() {
    return serverUri;
  }

  /**
   * Get the endpoint URL the client should use in its following {@code Hello} message.
   *
   * @return the endpoint URL, or {@code null} if the sender encoded a null string.
   */
  @Nullable
  public String getEndpointUrl() {
    return endpointUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ReverseHelloMessage that = (ReverseHelloMessage) o;

    return Objects.equals(serverUri, that.serverUri)
        && Objects.equals(endpointUrl, that.endpointUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serverUri, endpointUrl);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("serverUri", serverUri)
        .add("endpointUrl", endpointUrl)
        .toString();
  }

  /**
   * Encode the two UA Binary string fields that form a {@code ReverseHello} payload.
   *
   * @param message the payload value to encode.
   * @param buffer the buffer receiving {@code ServerUri} followed by {@code EndpointUrl}.
   */
  public static void encode(ReverseHelloMessage message, ByteBuf buffer) {
    encodeString(message.getServerUri(), buffer);
    encodeString(message.getEndpointUrl(), buffer);
  }

  /**
   * Decode a {@code ReverseHello} payload after its UA-TCP message header has been consumed.
   *
   * @param buffer the buffer positioned at the payload body.
   * @return the decoded reverse hello payload.
   * @throws UaException if a string length is invalid, exceeds the UACP limit, or extends past the
   *     remaining frame bytes.
   */
  public static ReverseHelloMessage decode(ByteBuf buffer) throws UaException {
    return new ReverseHelloMessage(
        decodeString(buffer, "server URI"), /*    ServerUri    */
        decodeString(buffer, "endpoint URL") /*    EndpointUrl  */);
  }

  private static void encodeString(@Nullable String s, ByteBuf buffer) {
    if (s == null) {
      buffer.writeIntLE(-1);
    } else {
      byte[] bs = s.getBytes(StandardCharsets.UTF_8);
      buffer.writeIntLE(bs.length);
      buffer.writeBytes(bs);
    }
  }

  private static @Nullable String decodeString(ByteBuf buffer, String fieldName)
      throws UaException {
    if (buffer.readableBytes() < Integer.BYTES) {
      throw new UaException(
          StatusCodes.Bad_DecodingError,
          String.format(
              "not enough bytes to read %s length (remaining=%s)",
              fieldName, buffer.readableBytes()));
    }

    int length = buffer.readIntLE();
    if (length < 0) {
      if (length == -1) {
        return null;
      } else {
        throw new UaException(
            StatusCodes.Bad_DecodingError, "invalid " + fieldName + " length: " + length);
      }
    } else {
      if (length > MAX_STRING_LENGTH) {
        throw new UaException(
            StatusCodes.Bad_EncodingLimitsExceeded,
            String.format("%s length exceeds 4096: %s", fieldName, length));
      }
      if (length > buffer.readableBytes()) {
        throw new UaException(
            StatusCodes.Bad_DecodingError,
            String.format(
                "%s length exceeds remaining frame bytes (length=%s, remaining=%s)",
                fieldName, length, buffer.readableBytes()));
      }
      byte[] bs = new byte[length];
      buffer.readBytes(bs);
      return new String(bs, StandardCharsets.UTF_8);
    }
  }
}
