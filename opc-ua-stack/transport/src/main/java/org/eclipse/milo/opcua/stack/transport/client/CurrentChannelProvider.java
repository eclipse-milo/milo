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
 * Interface for transport implementations that can expose the currently active channel.
 *
 * <p>This lets higher-level lifecycle code close the present channel without depending on the
 * transport implementation type.
 */
public interface CurrentChannelProvider {

  /**
   * Get the currently active channel.
   *
   * @return the currently active channel, or {@code null} if no channel is available.
   */
  @Nullable Channel getCurrentChannel();
}
