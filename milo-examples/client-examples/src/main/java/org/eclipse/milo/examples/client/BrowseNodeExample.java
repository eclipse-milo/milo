/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.client;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowseNodeExample implements ClientExample {

  public static void main(String[] args) throws Exception {
    BrowseNodeExample example = new BrowseNodeExample();

    new ClientExampleRunner(example).run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
    client.connect();

    // start browsing at root folder
    browseNode("", client, NodeIds.RootFolder);

    future.complete(client);
  }

  private void browseNode(String indent, OpcUaClient client, NodeId browseRoot) {
    try {
      List<? extends UaNode> nodes = client.getAddressSpace().browseNodes(browseRoot);

      for (UaNode node : nodes) {
        logger.info("{} Node={}", indent, node.getBrowseName().name());

        // recursively browse to children
        browseNode(indent + "  ", client, node.getNodeId());
      }
    } catch (UaException e) {
      logger.error("Browsing nodeId={} failed: {}", browseRoot, e.getMessage(), e);
    }
  }
}
