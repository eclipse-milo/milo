/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.session;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.netty.channel.Channel;
import org.eclipse.milo.opcua.stack.transport.client.CurrentChannelProvider;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpMultiplexedReverseConnectTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpReverseConnectTransport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SessionFsmKeepAliveChannelCloseTest {

  @Test
  @DisplayName("closeTransportChannel closes channel for OpcTcpClientTransport")
  void closeTransportChannel_forwardConnect() {
    var transport = mock(OpcTcpClientTransport.class);
    var channel = mock(Channel.class);

    when(transport.getCurrentChannel()).thenReturn(channel);

    SessionFsmFactory.closeTransportChannel(transport);

    verify(channel).close();
  }

  @Test
  @DisplayName("closeTransportChannel closes channel for OpcTcpReverseConnectTransport")
  void closeTransportChannel_reverseConnect() {
    var transport = mock(OpcTcpReverseConnectTransport.class);
    var channel = mock(Channel.class);

    when(transport.getCurrentChannel()).thenReturn(channel);

    SessionFsmFactory.closeTransportChannel(transport);

    verify(channel).close();
  }

  @Test
  @DisplayName("closeTransportChannel closes channel for OpcTcpMultiplexedReverseConnectTransport")
  void closeTransportChannel_multiplexedReverseConnect() {
    var transport = mock(OpcTcpMultiplexedReverseConnectTransport.class);
    var channel = mock(Channel.class);

    when(transport.getCurrentChannel()).thenReturn(channel);

    SessionFsmFactory.closeTransportChannel(transport);

    verify(channel).close();
  }

  @Test
  @DisplayName("closeTransportChannel does not throw when current channel is null")
  void closeTransportChannel_currentChannelProvider_nullChannel() {
    var transport = mock(CurrentChannelProviderTransport.class);

    when(transport.getCurrentChannel()).thenReturn(null);

    // should not throw
    SessionFsmFactory.closeTransportChannel(transport);
  }

  @Test
  @DisplayName("closeTransportChannel is a no-op for unknown transport types")
  void closeTransportChannel_unknownTransport() {
    var transport = mock(OpcClientTransport.class);

    // should not throw; unknown transport types are silently ignored
    SessionFsmFactory.closeTransportChannel(transport);
  }

  private interface CurrentChannelProviderTransport
      extends OpcClientTransport, CurrentChannelProvider {}
}
