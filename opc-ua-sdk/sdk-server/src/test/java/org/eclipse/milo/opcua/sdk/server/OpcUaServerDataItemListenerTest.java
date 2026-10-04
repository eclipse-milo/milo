/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import org.eclipse.milo.opcua.sdk.server.identity.AnonymousIdentityValidator;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredDataItem;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.transport.server.OpcServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.OpcServerTransportFactory;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** How {@link OpcUaServer} delivers data item notifications to its {@link DataItemListener}s. */
class OpcUaServerDataItemListenerTest {

  private static final OpcServerTransportFactory NO_OP_TRANSPORTS =
      transportProfile ->
          new OpcServerTransport() {
            @Override
            public void bind(
                ServerApplicationContext applicationContext, InetSocketAddress bindAddress) {}

            @Override
            public void unbind() {}
          };

  private OpcUaServer server;

  @BeforeEach
  void setUp() {
    EndpointConfig endpoint =
        EndpointConfig.newBuilder()
            .setBindAddress("localhost")
            .setBindPort(4840)
            .setHostname("localhost")
            .setPath("/test")
            .setSecurityPolicy(SecurityPolicy.None)
            .setSecurityMode(MessageSecurityMode.None)
            .addTokenPolicy(OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS)
            .build();

    OpcUaServerConfig config =
        OpcUaServerConfig.builder()
            .setApplicationUri("urn:eclipse:milo:test:server")
            .setApplicationName(LocalizedText.english("test server"))
            .setProductUri("urn:eclipse:milo:test")
            .setCertificateManager(new DefaultCertificateManager())
            .setIdentityValidator(AnonymousIdentityValidator.INSTANCE)
            .setEndpoints(Set.of(endpoint))
            .build();

    server = new OpcUaServer(config, NO_OP_TRANSPORTS);
  }

  @AfterEach
  void tearDown() {
    server.getSessionManager().shutdown();
  }

  // A listener is best-effort observation: one that throws must not keep the others, or the
  // service call that is notifying them, from completing.
  @Test
  void aThrowingListenerDoesNotStopTheOthers() {
    var seen = new CopyOnWriteArrayList<String>();
    server.addDataItemListener(
        new DataItemListener() {
          @Override
          public void onDataItemsCreated(List<DataItem> dataItems) {
            seen.add("first");
            throw new IllegalStateException("listener failure");
          }
        });
    server.addDataItemListener(
        new DataItemListener() {
          @Override
          public void onDataItemsCreated(List<DataItem> dataItems) {
            seen.add("second");
          }
        });

    server.getDataItemListener().onDataItemsCreated(List.of(dataItem()));

    assertEquals(List.of("first", "second"), seen);
  }

  @Test
  void aRemovedListenerIsNotNotified() {
    var seen = new CopyOnWriteArrayList<String>();
    DataItemListener listener =
        new DataItemListener() {
          @Override
          public void onDataItemsDeleted(List<DataItem> dataItems) {
            seen.add("deleted");
          }
        };

    server.addDataItemListener(listener);
    server.getDataItemListener().onDataItemsDeleted(List.of(dataItem()));
    server.removeDataItemListener(listener);
    server.getDataItemListener().onDataItemsDeleted(List.of(dataItem()));

    assertEquals(List.of("deleted"), seen);
  }

  private MonitoredDataItem dataItem() {
    return new MonitoredDataItem(
        server,
        mock(Session.class),
        uint(1),
        uint(1),
        new ReadValueId(new NodeId(2, "v"), AttributeId.Value.uid(), null, null),
        MonitoringMode.Reporting,
        TimestampsToReturn.Both,
        uint(1),
        100.0,
        uint(1),
        true);
  }
}
