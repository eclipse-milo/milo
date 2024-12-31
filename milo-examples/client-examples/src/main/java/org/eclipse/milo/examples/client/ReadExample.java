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

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadExample implements ClientExample {

  public static void main(String[] args) throws Exception {
    ReadExample example = new ReadExample();

    new ClientExampleRunner(example, true).run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
    client.connect();

    // synchronous read request via VariableNode
    UaVariableNode node =
        client.getAddressSpace().getVariableNode(NodeIds.Server_ServerStatus_StartTime);
    DataValue value = node.readValue();

    logger.info("StartTime={}", value.getValue().getValue());

    // asynchronous read request
    readServerStateAndTime(client)
        .thenAccept(
            values -> {
              DataValue v0 = values.get(0);
              DataValue v1 = values.get(1);

              logger.info(
                  "State={}", ServerState.from((Integer) requireNonNull(v0.getValue().getValue())));
              logger.info("CurrentTime={}", v1.getValue().getValue());

              future.complete(client);
            });
  }

  private CompletableFuture<List<DataValue>> readServerStateAndTime(OpcUaClient client) {
    List<NodeId> nodeIds =
        List.of(NodeIds.Server_ServerStatus_State, NodeIds.Server_ServerStatus_CurrentTime);

    return client.readValuesAsync(0.0, TimestampsToReturn.Both, nodeIds);
  }
}
