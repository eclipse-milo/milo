/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.client.ijt;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.examples.client.ClientExample;
import org.eclipse.milo.examples.client.ClientExampleRunner;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.core.types.DynamicStructType;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;

/**
 * Reads and decodes the IJT sample server's dynamic result structure.
 *
 * <p>The example requires <a
 * href="https://github.com/umati/UA-for-Industrial-Joining-Technologies">the IJT sample server</a>
 * and uses {@link SecurityPolicy#None}, matching that sample server's default endpoint. It is kept
 * in a vendor-specific package because the node id and data type are part of the IJT companion
 * specification sample model rather than the local Milo example server.
 */
public class IJTReadCustomDataTypeExample implements ClientExample {

  public static void main(String[] args) throws Exception {
    var example = new IJTReadCustomDataTypeExample();

    new ClientExampleRunner(example, false).run();
  }

  @Override
  public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
    client.connect();

    NodeId nodeId = NodeId.parse("ns=1;s=TighteningSystem/ResultManagement/Results/Result");
    DataValue value = client.readValue(0, TimestampsToReturn.Both, nodeId);

    if (value.getValue().getValue() instanceof ExtensionObject xo) {
      Object decoded = xo.decode(client.getDynamicEncodingContext());

      if (decoded instanceof DynamicStructType struct) {
        System.out.println(struct.toStringPretty());
      }
    }

    future.complete(client);
  }

  @Override
  public String getEndpointUrl() {
    return "opc.tcp://localhost:40451";
  }

  @Override
  public SecurityPolicy getSecurityPolicy() {
    return SecurityPolicy.None;
  }
}
