/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.dtd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.model.variables.DataTypeDictionaryTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.junit.jupiter.api.Test;

class BinaryDataTypeDictionaryManagerTest {

  // Generated setters require existing children; startup must materialize NamespaceUri itself.
  @Test
  void startupCreatesNamespaceUriPropertyAndShutdownRemovesIt() throws Exception {
    var server =
        new OpcUaServer(
            OpcUaServerConfig.builder()
                .setCertificateManager(new DefaultCertificateManager())
                .build(),
            profile -> null);
    var nodeManager = new UaNodeManager();
    String namespaceUri = "urn:eclipse:milo:test:dictionary";
    UShort namespaceIndex = server.getNamespaceTable().add(namespaceUri);
    server.getAddressSpaceManager().register(nodeManager);
    var context =
        new UaNodeContext() {
          @Override
          public OpcUaServer getServer() {
            return server;
          }

          @Override
          public NodeManager<UaNode> getNodeManager() {
            return nodeManager;
          }
        };
    var manager = new BinaryDataTypeDictionaryManager(context, namespaceUri);
    boolean started = false;
    try {
      manager.startup();
      started = true;

      DataTypeDictionaryTypeNode dictionary =
          assertInstanceOf(
              DataTypeDictionaryTypeNode.class,
              nodeManager.get(new NodeId(namespaceIndex, namespaceUri)));
      assertNotNull(dictionary.getNamespaceUriNode());
      assertEquals(
          new QualifiedName(0, "NamespaceUri"), dictionary.getNamespaceUriNode().getBrowseName());
      assertEquals(namespaceUri, dictionary.getNamespaceUri());

      manager.shutdown();
      started = false;
      assertTrue(nodeManager.getNodes().isEmpty(), "dictionary children must be removed too");
    } finally {
      try {
        if (started) manager.shutdown();
      } finally {
        server.shutdown().get(10, TimeUnit.SECONDS);
      }
    }
  }
}
