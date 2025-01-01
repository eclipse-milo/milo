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

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.objects.ServerTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.ServerStatusTypeNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerStatusDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadNodeExample implements ClientExample {

  public static void main(String[] args) throws Exception {
    ReadNodeExample example = new ReadNodeExample();

    new ClientExampleRunner(example).run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
    client.connect();

    // Get a typed reference to the Server object: ServerNode
    ServerTypeNode serverNode =
        (ServerTypeNode) client.getAddressSpace().getObjectNode(NodeIds.Server, NodeIds.ServerType);

    // Read properties of the Server object...
    String[] serverArray = serverNode.getServerArray();
    String[] namespaceArray = serverNode.getNamespaceArray();

    logger.info("ServerArray={}", Arrays.toString(serverArray));
    logger.info("NamespaceArray={}", Arrays.toString(namespaceArray));

    // Read the value of attribute the ServerStatus variable component
    ServerStatusDataType serverStatus = serverNode.getServerStatus();

    logger.info("ServerStatus={}", serverStatus);

    // Get a typed reference to the ServerStatus variable
    // component and read value attributes individually
    ServerStatusTypeNode serverStatusNode = serverNode.getServerStatusNode();
    BuildInfo buildInfo = serverStatusNode.getBuildInfo();
    DateTime startTime = serverStatusNode.getStartTime();
    DateTime currentTime = serverStatusNode.getCurrentTime();
    ServerState state = serverStatusNode.getState();

    logger.info("ServerStatus.BuildInfo={}", buildInfo);
    logger.info("ServerStatus.StartTime={}", startTime);
    logger.info("ServerStatus.CurrentTime={}", currentTime);
    logger.info("ServerStatus.State={}", state);

    future.complete(client);
  }
}
