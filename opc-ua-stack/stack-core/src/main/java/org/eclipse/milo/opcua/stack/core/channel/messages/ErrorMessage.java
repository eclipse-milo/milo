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

import com.google.common.base.Charsets;
import com.google.common.base.MoreObjects;
import io.netty.buffer.ByteBuf;
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
    buffer.writeIntLE((int) message.getError().getValue());
    encodeString(message.getReason(), buffer);
  }

  public static ErrorMessage decode(ByteBuf buffer) {
    return new ErrorMessage(buffer.readUnsignedIntLE(), decodeString(buffer));
  }

  private static void encodeString(String s, ByteBuf buffer) {
    if (s == null) {
      buffer.writeIntLE(-1);
    } else {
      buffer.writeIntLE(s.length());
      buffer.writeBytes(s.getBytes(Charsets.UTF_8));
    }
  }

  private static String decodeString(ByteBuf buffer) {
    int length = buffer.readIntLE();
    if (length == -1) {
      return null;
    } else {
      byte[] bs = new byte[length];
      buffer.readBytes(bs);
      return new String(bs, Charsets.UTF_8);
    }
  }
}
