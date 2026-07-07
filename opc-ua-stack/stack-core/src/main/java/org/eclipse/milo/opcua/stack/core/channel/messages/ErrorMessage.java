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

import com.google.common.base.MoreObjects;
import io.netty.buffer.ByteBuf;
import java.nio.charset.StandardCharsets;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

public class ErrorMessage {

  private final StatusCode error;
  private final String reason;

  public ErrorMessage(long error, String reason) {
    this.error = new StatusCode(error);
    this.reason = reason;
  }

  public StatusCode getError() {
    return error;
  }

  public String getReason() {
    return reason;
  }

  @Override
  public String toString() {

    return MoreObjects.toStringHelper(this).add("error", error).add("reason", reason).toString();
  }

  public static void encode(ErrorMessage message, ByteBuf buffer) {
    buffer.writeIntLE((int) message.getError().value());
    encodeString(message.getReason(), buffer);
  }

  public static ErrorMessage decode(ByteBuf buffer) throws UaException {
    return new ErrorMessage(buffer.readUnsignedIntLE(), decodeString(buffer));
  }

  private static void encodeString(String s, ByteBuf buffer) {
    if (s == null) {
      buffer.writeIntLE(-1);
    } else {
      byte[] bs = s.getBytes(StandardCharsets.UTF_8);
      buffer.writeIntLE(bs.length);
      buffer.writeBytes(bs);
    }
  }

  private static String decodeString(ByteBuf buffer) throws UaException {
    int length = buffer.readIntLE();
    if (length < 0) {
      if (length == -1) {
        return null;
      } else {
        throw new UaException(
            StatusCodes.Bad_DecodingError, "invalid error reason length: " + length);
      }
    } else {
      if (length > EncodingLimits.DEFAULT_MAX_MESSAGE_SIZE) {
        throw new UaException(
            StatusCodes.Bad_EncodingLimitsExceeded,
            String.format(
                "error reason length exceeds max message size (length=%s, max=%s)",
                length, EncodingLimits.DEFAULT_MAX_MESSAGE_SIZE));
      }
      if (length > buffer.readableBytes()) {
        throw new UaException(
            StatusCodes.Bad_DecodingError,
            String.format(
                "error reason length exceeds remaining frame bytes (length=%s, remaining=%s)",
                length, buffer.readableBytes()));
      }
      byte[] bs = new byte[length];
      buffer.readBytes(bs);
      return new String(bs, StandardCharsets.UTF_8);
    }
  }
}
