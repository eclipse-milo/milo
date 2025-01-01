/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.channel.messages;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

public class AcknowledgeMessage {

  private final long protocolVersion;

  private final long receiveBufferSize;

  private final long sendBufferSize;

  private final long maxMessageSize;

  private final long maxChunkCount;

  /**
   * @param protocolVersion The latest version of the OPC UA TCP protocol supported by the Server.
   * @param receiveBufferSize The largest MessageChunk that the sender can receive. This value shall
   *     not be larger than what the Client requested in the Hello Message. This value shall be
   *     greater than or equal to 8192 bytes.
   * @param sendBufferSize The largest MessageChunk that the sender will send. This value shall not
   *     be larger than what the Client requested in the Hello Message. This value shall be greater
   *     than or equal to 8192 bytes.
   * @param maxMessageSize The maximum size for any request Message. The maximum size for any
   *     request Message. The Client shall abort the Message with a Bad_RequestTooLarge StatusCode
   *     if a request Message exceeds this value. The Message size is calculated using the
   *     unencrypted Message body. A value of zero indicates that the Server has no limit.
   * @param maxChunkCount The maximum number of chunks in any request Message. A value of zero
   *     indicates that the Server has no limit.
   */
  public AcknowledgeMessage(
      long protocolVersion,
      long receiveBufferSize,
      long sendBufferSize,
      long maxMessageSize,
      long maxChunkCount) {

    Preconditions.checkArgument(
        receiveBufferSize >= 8192, "receiverBufferSize must be greater than or equal to 8192");

    Preconditions.checkArgument(
        sendBufferSize >= 8192, "sendBufferSize must be greater than or equal to 8192");

    this.protocolVersion = protocolVersion;
    this.receiveBufferSize = receiveBufferSize;
    this.sendBufferSize = sendBufferSize;
    this.maxMessageSize = maxMessageSize;
    this.maxChunkCount = maxChunkCount;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AcknowledgeMessage that = (AcknowledgeMessage) o;

    return maxChunkCount == that.maxChunkCount
        && maxMessageSize == that.maxMessageSize
        && protocolVersion == that.protocolVersion
        && receiveBufferSize == that.receiveBufferSize
        && sendBufferSize == that.sendBufferSize;
  }

  @Override
  public int hashCode() {
    int result = (int) (protocolVersion ^ (protocolVersion >>> 32));
    result = 31 * result + (int) (receiveBufferSize ^ (receiveBufferSize >>> 32));
    result = 31 * result + (int) (sendBufferSize ^ (sendBufferSize >>> 32));
    result = 31 * result + (int) (maxMessageSize ^ (maxMessageSize >>> 32));
    result = 31 * result + (int) (maxChunkCount ^ (maxChunkCount >>> 32));
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
        .toString();
  }

  public static void encode(AcknowledgeMessage message, ByteBuf buffer) {
    buffer.writeIntLE((int) message.getProtocolVersion());
    buffer.writeIntLE((int) message.getReceiveBufferSize());
    buffer.writeIntLE((int) message.getSendBufferSize());
    buffer.writeIntLE((int) message.getMaxMessageSize());
    buffer.writeIntLE((int) message.getMaxChunkCount());
  }

  public static AcknowledgeMessage decode(ByteBuf buffer) {
    return new AcknowledgeMessage(
        buffer.readUnsignedIntLE(), /*    ProtocolVersion     */
        buffer.readUnsignedIntLE(), /*    ReceiveBufferSize   */
        buffer.readUnsignedIntLE(), /*    SendBufferSize      */
        buffer.readUnsignedIntLE(), /*    MaxMessageSize      */
        buffer.readUnsignedIntLE() /*    MaxChunkCount       */);
  }
}
