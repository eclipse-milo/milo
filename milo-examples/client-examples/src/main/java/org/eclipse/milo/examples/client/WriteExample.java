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
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WriteExample implements ClientExample {

  public static void main(String[] args) throws Exception {
    WriteExample example = new WriteExample();

    new ClientExampleRunner(example).run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
    client.connect();

    List<NodeId> nodeIds = List.of(new NodeId(2, "HelloWorld/ScalarTypes/Int32"));

    for (int i = 0; i < 10; i++) {
      Variant v = Variant.ofInt32(i);

      // don't write status or timestamps
      DataValue dv = new DataValue(v, null, null);

      List<StatusCode> statusCodes = client.writeValues(nodeIds, List.of(dv));

      StatusCode status = statusCodes.get(0);

      if (status.isGood()) {
        logger.info("Wrote '{}' to nodeId={}", v, nodeIds.get(0));
      }
    }

    future.complete(client);
  }
}
