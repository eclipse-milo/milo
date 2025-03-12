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

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.dtd.LegacyDataTypeManagerInitializer;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An example that shows reading the value of a node whose DataType is a custom structure type.
 *
 * <p>Requires the Unified Automation CPP Demo server be running and the endpoint URL be pointing to
 * it.
 */
public class UnifiedAutomationReadCustomDataTypeExampleLegacy implements ClientExample {

  public static void main(String[] args) throws Exception {
    var example = new UnifiedAutomationReadCustomDataTypeExampleLegacy();

    new ClientExampleRunner(example, false).run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
    client.connect();

    // Decoding a struct with custom DataType requires a DataTypeManager
    // that has the codec registered with it.
    // LegacyDataTypeManagerInitializer will read any DataTypeDictionary nodes present in the
    // server then dynamically generate and register codecs for custom structures.
    client.setDataTypeManagerInitializer(new LegacyDataTypeManagerInitializer(client));

    readPerson(client);
    readWorkOrder(client);

    future.complete(client);
  }

  private void readPerson(OpcUaClient client) throws UaException {
    DataValue dataValue =
        client
            .readValues(0.0, TimestampsToReturn.Neither, List.of(NodeId.parse("ns=3;s=Person1")))
            .get(0);

    ExtensionObject xo = (ExtensionObject) dataValue.value().value();
    assert xo != null;

    Object value = xo.decode(client.getDynamicEncodingContext());

    logger.info("value: {}", value);
  }

  private void readWorkOrder(OpcUaClient client) throws UaException {
    DataValue dataValue =
        client
            .readValues(
                0.0,
                TimestampsToReturn.Neither,
                List.of(NodeId.parse("ns=3;s=Demo.Static.Scalar.WorkOrder")))
            .get(0);

    ExtensionObject xo = (ExtensionObject) dataValue.value().value();
    assert xo != null;

    Object value = xo.decode(client.getDynamicEncodingContext());

    logger.info("value: {}", value);
  }

  @Override
  public String getEndpointUrl() {
    // Change this if UaCPPServer is running somewhere other than localhost.
    return "opc.tcp://localhost:48010";
  }

  @Override
  public SecurityPolicy getSecurityPolicy() {
    return SecurityPolicy.None;
  }
}
