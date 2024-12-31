/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server;

import java.net.InetSocketAddress;

public interface OpcServerTransport {

  /**
   * Bind a {@link ServerApplicationContext} to the given bind address and port.
   *
   * @param applicationContext the {@link ServerApplicationContext} to bind.
   * @param bindAddress the local address to bind to.
   * @throws Exception if an error occurs binding to the address/port combination.
   */
  void bind(ServerApplicationContext applicationContext, InetSocketAddress bindAddress)
      throws Exception;

  /**
   * Unbind this transport (close the server channel).
   *
   * @throws Exception if an error occurs unbinding this transport.
   */
  void unbind() throws Exception;
}
