/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client.uasc;

import io.netty.handler.codec.DecoderException;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaExceptionStatus;

/** Maps the exceptions the client UASC handlers catch to the failure their handshake reports. */
final class HandshakeFailures {

  private HandshakeFailures() {}

  /**
   * Get the failure to complete the handshake future with for an exception a handler caught.
   *
   * <p>Netty wraps an exception thrown while decoding in a {@link DecoderException}. A reply that
   * cannot be decoded fails the handshake with a {@link UaException}: the one the decoder threw,
   * one with the status a {@link UaExceptionStatus} such as {@code UaSerializationException}
   * carries, or one with {@code Bad_DecodingError} for any other decoding failure. Other exceptions
   * are returned as they are.
   *
   * @param caught the exception passed to {@code exceptionCaught}.
   * @return the failure to complete the handshake future with.
   */
  static Throwable of(Throwable caught) {
    if (!(caught instanceof DecoderException)) {
      return caught;
    }

    Throwable decodingFailure = caught.getCause() != null ? caught.getCause() : caught;

    if (decodingFailure instanceof UaException) {
      return decodingFailure;
    } else if (UaExceptionStatus.extract(decodingFailure).isPresent()) {
      return new UaException(decodingFailure);
    } else {
      return new UaException(StatusCodes.Bad_DecodingError, decodingFailure);
    }
  }
}
