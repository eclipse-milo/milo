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

import com.google.common.base.MoreObjects;
import io.netty.buffer.ByteBuf;
import java.nio.charset.StandardCharsets;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.jspecify.annotations.Nullable;

public class HelloMessage {

  static final int MAX_ENDPOINT_URL_LENGTH = 4096;

  private final long protocolVersion;

  private final long receiveBufferSize;

  private final long sendBufferSize;

  private final long maxMessageSize;

  private final long maxChunkCount;

  private final String endpointUrl;

  /**
   * @param protocolVersion the latest version of the OPC UA TCP protocol supported by the Client.
   * @param receiveBufferSize the largest MessageChunk that the sender (Client) can receive. This
   *     value shall be greater than or equal to 8192 bytes.
   * @param sendBufferSize the largest MessageChunk that the sender (Client) will send. This value
   *     shall be greater than or equal to 8192 bytes.
   * @param maxMessageSize the maximum size for any response Message. The Message size is calculated
   *     using the unencrypted Message body. A value of zero indicates that the Client has no limit.
   * @param maxChunkCount the maximum number of chunks in any response Message. A value of zero
   *     indicates that the Client has no limit.
   * @param endpointUrl the URL of the Endpoint which the Client wished to connect to. The encoded
   *     value shall be less than 4096 bytes.
   */
  public HelloMessage(
      long protocolVersion,
      long receiveBufferSize,
      long sendBufferSize,
      long maxMessageSize,
      long maxChunkCount,
      @Nullable String endpointUrl) {

    checkArgument(receiveBufferSize >= 8192, "receiverBufferSize must be at least 8192 bytes");
    checkArgument(sendBufferSize >= 8192, "sendBufferSize must be at least 8192 bytes");
    checkArgument(
        endpointUrl == null || endpointUrl.length() <= MAX_ENDPOINT_URL_LENGTH,
        "endpointUrl length cannot be greater than 4096 bytes");

    this.protocolVersion = protocolVersion;
    this.receiveBufferSize = receiveBufferSize;
    this.sendBufferSize = sendBufferSize;
    this.maxMessageSize = maxMessageSize;
    this.maxChunkCount = maxChunkCount;
    this.endpointUrl = endpointUrl;
  }

  public long getProtocolVersion() {
    return protocolVersion;
  }

  public long getReceiveBufferSize() {
    return receiveBufferSize;
  }

  public long getSendBufferSize() {
    return sendBufferSize;
  }

  public long getMaxMessageSize() {
    return maxMessageSize;
  }

  public long getMaxChunkCount() {
    return maxChunkCount;
  }

  @Nullable
  public String getEndpointUrl() {
    return endpointUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    HelloMessage that = (HelloMessage) o;

    return maxChunkCount == that.maxChunkCount
        && maxMessageSize == that.maxMessageSize
        && protocolVersion == that.protocolVersion
        && receiveBufferSize == that.receiveBufferSize
        && sendBufferSize == that.sendBufferSize
        && endpointUrl.equals(that.endpointUrl);
  }

  @Override
  public int hashCode() {
    int result = Long.hashCode(protocolVersion);
    result = 31 * result + Long.hashCode(receiveBufferSize);
    result = 31 * result + Long.hashCode(sendBufferSize);
    result = 31 * result + Long.hashCode(maxMessageSize);
    result = 31 * result + Long.hashCode(maxChunkCount);
    result = 31 * result + endpointUrl.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("protocolVersion", protocolVersion)
        .add("receiverBufferSize", receiveBufferSize)
        .add("sendBufferSize", sendBufferSize)
        .add("maxMessageSize", maxMessageSize)
        .add("maxChunkCount", maxChunkCount)
        .add("endpointUrl", endpointUrl)
        .toString();
  }

  public static void encode(HelloMessage message, ByteBuf buffer) {
    buffer.writeIntLE((int) message.getProtocolVersion());
    buffer.writeIntLE((int) message.getReceiveBufferSize());
    buffer.writeIntLE((int) message.getSendBufferSize());
    buffer.writeIntLE((int) message.getMaxMessageSize());
    buffer.writeIntLE((int) message.getMaxChunkCount());
    encodeString(message.getEndpointUrl(), buffer);
  }

  public static HelloMessage decode(ByteBuf buffer) throws UaException {
    return new HelloMessage(
        buffer.readUnsignedIntLE(), /*    ProtocolVersion    */
        buffer.readUnsignedIntLE(), /*    ReceiveBufferSize  */
        buffer.readUnsignedIntLE(), /*    SendBufferSize     */
        buffer.readUnsignedIntLE(), /*    MaxMessageSize     */
        buffer.readUnsignedIntLE(), /*    MaxChunkCount      */
        decodeString(buffer) /*    EndpointUrl        */);
  }

  private static void encodeString(String s, ByteBuf buffer) {
    if (s == null) {
      buffer.writeIntLE(-1);
    } else {
      buffer.writeIntLE(s.length());
      buffer.writeBytes(s.getBytes(StandardCharsets.UTF_8));
    }
  }

  private static String decodeString(ByteBuf buffer) throws UaException {
    int length = buffer.readIntLE();
    if (length < 0) {
      if (length == -1) {
        return null;
      } else {
        throw new UaException(
            StatusCodes.Bad_DecodingError, "invalid endpoint URL length: " + length);
      }
    } else {
      if (length > MAX_ENDPOINT_URL_LENGTH) {
        throw new UaException(
            StatusCodes.Bad_EncodingLimitsExceeded, "endpoint URL length exceeds 4096: " + length);
      }
      byte[] bs = new byte[length];
      buffer.readBytes(bs);
      return new String(bs, StandardCharsets.UTF_8);
    }
  }
}
