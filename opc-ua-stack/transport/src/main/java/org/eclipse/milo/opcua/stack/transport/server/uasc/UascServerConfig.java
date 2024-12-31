/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.uasc;

import java.util.concurrent.ExecutorService;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

public interface UascServerConfig {

  /**
   * Get the {@link ExecutorService} to use when dispatching inbound {@link UascServiceRequest}s
   * that arrive while on the Netty event loop thread.
   *
   * @return the {@link ExecutorService} to use when dispatching inbound {@link
   *     UascServiceRequest}s.
   */
  ExecutorService getExecutor();

  /**
   * Get the deadline, in milliseconds, that a "Hello" message must arrive by after the underlying
   * channel is activated.
   *
   * @return the "Hello" message deadline, in milliseconds.
   */
  UInteger getHelloDeadline();

  /**
   * Get the minimum allowed secure channel lifetime, in milliseconds. Requested lifetimes smaller
   * than this value will be revised to this value.
   *
   * @return the minimum allowed secure channel lifetime, in milliseconds.
   */
  UInteger getMinimumSecureChannelLifetime();

  /**
   * Get the maximum allowed secure channel lifetime, in milliseconds. Requested lifetimes larger
   * than this value will be revised to this value.
   *
   * @return the maximum allowed secure channel lifetime, in milliseconds.
   */
  UInteger getMaximumSecureChannelLifetime();
}
