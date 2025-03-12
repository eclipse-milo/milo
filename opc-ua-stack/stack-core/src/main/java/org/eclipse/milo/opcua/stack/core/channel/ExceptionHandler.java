/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.channel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.channel.messages.ErrorMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageEncoder;

public class ExceptionHandler {

  public static ErrorMessage sendErrorMessage(ChannelHandlerContext ctx, Throwable cause)
      throws Exception {
    String message = cause.getMessage();
    long statusCode = StatusCodes.Bad_UnexpectedError;

    if (cause instanceof UaException) {
      UaException ex = (UaException) cause;
      message = ex.getMessage();
      statusCode = ex.getStatusCode().value();
    } else {
      Throwable innerCause = cause.getCause();

      if (innerCause instanceof UaException) {
        UaException ex = (UaException) innerCause;
        message = ex.getMessage();
        statusCode = ex.getStatusCode().value();
      } else if (innerCause instanceof UaRuntimeException) {
        UaRuntimeException ex = (UaRuntimeException) innerCause;
        message = ex.getMessage();
        statusCode = ex.getStatusCode().value();
      }
    }

    ErrorMessage error = new ErrorMessage(statusCode, message);
    ByteBuf messageBuffer = TcpMessageEncoder.encode(error);

    ctx.writeAndFlush(messageBuffer).addListener(future -> ctx.close());

    // Wait 2 seconds before closing the channel without regard for the write result.
    // Clients are supposed to close the channel upon receiving an Error message, but
    // an adversarial client may not behave as expected and may not even be reading
    // responses from their socket.
    ctx.executor().schedule(() -> ctx.close(), 2, TimeUnit.SECONDS);

    return error;
  }
}
