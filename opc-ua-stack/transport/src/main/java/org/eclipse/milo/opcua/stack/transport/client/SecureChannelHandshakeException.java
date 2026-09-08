/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client;

import org.eclipse.milo.opcua.stack.core.UaException;

/**
 * A failure while establishing a SecureChannel after transport negotiation has succeeded.
 *
 * <p>This identifies the failed handshake phase independently of the server's status code or
 * whether the server sent an error at all. The original cause and its status code are preserved. It
 * does not describe TCP connection failures, Hello/Acknowledge failures, or failures of an already
 * established channel.
 */
public final class SecureChannelHandshakeException extends UaException {

  /**
   * Creates a SecureChannel establishment failure with the original cause.
   *
   * @param cause the failure encountered while establishing the SecureChannel.
   */
  public SecureChannelHandshakeException(Throwable cause) {
    super(cause);
  }
}
