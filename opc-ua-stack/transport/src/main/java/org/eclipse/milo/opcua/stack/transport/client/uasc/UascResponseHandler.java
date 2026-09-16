/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client.uasc;

import io.netty.channel.Channel;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;

public interface UascResponseHandler {

  // response successfully received and decoded
  void handleResponse(long requestId, UaResponseMessageType responseMessage);

  // failed while sending request
  void handleSendFailure(long requestId, UaException exception);

  // failed while decoding response, aborted, decode exception, ServiceFault
  void handleReceiveFailure(long requestId, UaException exception);

  /**
   * A channel-level Error message arrived on {@code channel}.
   *
   * <p>Implementations must scope the resulting failure to requests carried by {@code channel}: a
   * transport can outlive any one channel, and requests not yet written may be waiting for its
   * replacement. See the {@link org.eclipse.milo.opcua.stack.transport.client} package
   * documentation for the channel and request lifecycle.
   *
   * @param channel the {@link Channel} the Error message arrived on.
   * @param exception the {@link UaException} carrying the Error message's status and reason.
   */
  void handleChannelError(Channel channel, UaException exception);

  /**
   * {@code channel} went inactive; fail the requests it was carrying.
   *
   * <p>The same scoping requirement described on {@link #handleChannelError} applies.
   *
   * @param channel the {@link Channel} that went inactive.
   */
  void handleChannelInactive(Channel channel);
}
