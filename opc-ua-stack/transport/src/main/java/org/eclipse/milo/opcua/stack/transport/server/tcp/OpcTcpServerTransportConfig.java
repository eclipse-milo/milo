/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.tcp;

import io.netty.bootstrap.Bootstrap;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.stack.transport.server.OpcServerTransportConfig;
import org.eclipse.milo.opcua.stack.transport.server.uasc.UascServerConfig;

public interface OpcTcpServerTransportConfig extends OpcServerTransportConfig, UascServerConfig {

  /**
   * Get a {@link Consumer} that can customize the outbound {@link Bootstrap} used by internal
   * reverse-connect attempts.
   *
   * @return a {@link Consumer} that can customize the outbound {@link Bootstrap}.
   */
  default Consumer<Bootstrap> getReverseConnectBootstrapCustomizer() {
    return b -> {};
  }

  /**
   * Create a new {@link OpcTcpServerTransportConfigBuilder}.
   *
   * @return a new {@link OpcTcpServerTransportConfigBuilder}.
   */
  static OpcTcpServerTransportConfigBuilder newBuilder() {
    return new OpcTcpServerTransportConfigBuilder();
  }
}
