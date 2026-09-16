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

import io.netty.channel.Channel;
import org.jspecify.annotations.Nullable;

/**
 * Optional client transport capability for exposing the current transport channel.
 *
 * <p>This capability is used by higher-level client lifecycle code when it needs to force a
 * transport-level reconnect, for example after Session keep-alive failures. Transports that do not
 * own a Netty channel directly should not implement it.
 */
public interface CurrentChannelProvider {

  /**
   * Get the transport's current channel, if one is available.
   *
   * <p>The returned channel remains owned by the transport. Callers may close it to force the
   * transport's normal reconnect path, but must not mutate the pipeline or assume the channel will
   * remain active after this method returns.
   *
   * @return the current channel, or {@code null} if no channel is currently available.
   */
  @Nullable Channel getCurrentChannel();
}
