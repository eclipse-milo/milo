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

import static java.util.Objects.requireNonNullElse;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowseExample implements ClientExample {

  public static void main(String[] args) throws Exception {
    BrowseExample example = new BrowseExample();

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
    BrowseDescription browse =
        new BrowseDescription(
            browseRoot,
            BrowseDirection.Forward,
            NodeIds.References,
            true,
            uint(NodeClass.Object.getValue() | NodeClass.Variable.getValue()),
            uint(BrowseResultMask.All.getValue()));

    try {
      BrowseResult browseResult = client.browse(browse);

      ReferenceDescription[] references =
          requireNonNullElse(browseResult.getReferences(), new ReferenceDescription[0]);

      for (ReferenceDescription rd : references) {
        logger.info("{} Node={}", indent, rd.getBrowseName().getName());

        // recursively browse to children
        rd.getNodeId()
            .toNodeId(client.getNamespaceTable())
            .ifPresent(nodeId -> browseNode(indent + "  ", client, nodeId));
      }
    } catch (UaException e) {
      logger.error("Browsing nodeId={} failed: {}", browseRoot, e.getMessage(), e);
    }
  }
}
