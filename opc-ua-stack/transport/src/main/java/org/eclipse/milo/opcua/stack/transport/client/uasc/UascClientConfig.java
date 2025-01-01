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

import io.netty.util.HashedWheelTimer;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

public interface UascClientConfig {

  /**
   * Get the timeout, in milliseconds, that an "Acknowledge" message must arrive by in response to
   * the client "Hello" message.
   *
   * @return the timeout, in milliseconds, that an "Acknowledge" message must arrive by.
   */
  UInteger getAcknowledgeTimeout();

  /**
   * Get the requested secure channel lifetime. The server may revise this value.
   *
   * @return the requested secure channel lifetime.
   */
  UInteger getChannelLifetime();

  /**
   * Get the Netty {@link HashedWheelTimer} to use for scheduling transport layer timeouts.
   *
   * @return the Netty {@link HashedWheelTimer} to use for scheduling transport layer timeouts.
   */
  HashedWheelTimer getWheelTimer();
}
