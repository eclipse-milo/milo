/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.client;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.dtd.LegacyDataTypeManagerInitializer;
import org.eclipse.milo.opcua.sdk.client.typetree.DataTypeManagerFactory;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;

/**
 * Demonstrates how to configure an {@link OpcUaClient} to use the legacy (OPC UA <= 1.03)
 * DataTypeDictionary mechanism for discovering custom data types and creating runtime codecs for
 * them. The DataTypeDictionary is not just deprecated in Milo, it's also deprecated in the OPC UA
 * specification.
 *
 * <p>This was the default (and only supported mechanism) in Milo 0.6.x and earlier. Milo 1.x now
 * uses the DataTypeDefinition attribute instead via {@link
 * DataTypeManagerFactory.DefaultInitializer}.
 *
 * <p>Requires a server that still supports DataTypeDictionary, such as the Unified Automation C++
 * demo server, which this example is configured for by default.
 */
public class LegacyDataTypeDictionaryExample implements ClientExample {

  public static void main(String[] args) throws Exception {
    var example = new LegacyDataTypeDictionaryExample();

    new ClientExampleRunner(example, false).run();
  }

  @Override
  public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
    client.setDynamicDataTypeManagerFactory(
        DataTypeManagerFactory.eager(new LegacyDataTypeManagerInitializer(client)));

    client.connect();

    DataValue dataValue =
        client.readValue(
            0.0, TimestampsToReturn.Both, NodeId.parse("ns=3;s=Demo.WorkOrder.WorkOrderVariable"));

    if (dataValue.getValue().getValue() instanceof ExtensionObject xo) {
      Object decoded = xo.decode(client.getDynamicEncodingContext());

      System.out.println("Decoded: " + decoded);
    }

    future.complete(client);
  }

  @Override
  public String getEndpointUrl() {
    return "opc.tcp://10.211.55.3:48010";
  }

  @Override
  public SecurityPolicy getSecurityPolicy() {
    return SecurityPolicy.None;
  }
}
