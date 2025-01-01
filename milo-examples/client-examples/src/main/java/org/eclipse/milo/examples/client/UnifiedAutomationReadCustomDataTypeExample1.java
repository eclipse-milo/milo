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

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.core.types.DynamicEnum;
import org.eclipse.milo.opcua.sdk.core.types.DynamicOptionSet;
import org.eclipse.milo.opcua.sdk.core.types.DynamicStruct;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An example that shows reading the value of a node whose DataType is a custom structure type.
 *
 * <p>Requires the Unified Automation CPP Demo server be running and the endpoint URL be pointing to
 * it.
 */
public class UnifiedAutomationReadCustomDataTypeExample1 implements ClientExample {

  public static void main(String[] args) throws Exception {
    var example = new UnifiedAutomationReadCustomDataTypeExample1();

    new ClientExampleRunner(example, false).run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
    client.connect();

    readWriteReadPerson(client);
    readWriteReadWorkOrder(client);
    readWriteCarExtras(client);
    readWorkOrderArray(client);

    future.complete(client);
  }

  private void readWriteReadPerson(OpcUaClient client) throws Exception {
    NodeId nodeId = NodeId.parse("ns=3;s=Person1");

    DynamicStruct value = readScalarValue(client, nodeId);
    logger.info("Person1: {}", value);

    Random r = new Random();
    DynamicEnum gender = (DynamicEnum) value.getMembers().get("Gender");
    value.getMembers().put("Name", "Fat Boy" + r.nextInt(100));
    value.getMembers().put("Gender", new DynamicEnum(gender.getDataType(), r.nextInt(2)));

    StatusCode status = writeValue(client, nodeId, value);
    System.out.println("write status: " + status);

    value = readScalarValue(client, nodeId);
    logger.info("Person1': {}", value);
  }

  private void readWriteReadWorkOrder(OpcUaClient client) throws Exception {
    NodeId nodeId = NodeId.parse("ns=3;s=Demo.Static.Scalar.WorkOrder");

    DynamicStruct value = readScalarValue(client, nodeId);
    logger.info("WorkOrder: {}", value);

    value.getMembers().put("ID", UUID.randomUUID());

    StatusCode status = writeValue(client, nodeId, value);
    System.out.println("write status: " + status);

    value = readScalarValue(client, nodeId);
    logger.info("WorkOrder': {}", value);
  }

  private void readWriteCarExtras(OpcUaClient client) throws Exception {
    NodeId nodeId = NodeId.parse("ns=3;s=Demo.Static.Scalar.CarExtras");

    DynamicOptionSet value = (DynamicOptionSet) readScalarValue(client, nodeId);
    logger.info("CarExtras: {}", value);

    byte b = requireNonNull(value.getValue().bytes())[0];
    value.setValue(ByteString.of(new byte[] {(byte) ~b}));

    StatusCode status = writeValue(client, nodeId, value);
    System.out.println("write status: " + status);

    value = (DynamicOptionSet) readScalarValue(client, nodeId);
    logger.info("CarExtras': {}", value);
  }

  private void readWorkOrderArray(OpcUaClient client) throws Exception {
    NodeId nodeId = NodeId.parse("ns=3;s=Demo.Static.Arrays.WorkOrder");

    DynamicStruct[] value = readArrayValue(client, nodeId);

    logger.info("WorkOrderArray:");
    for (int i = 0; i < value.length; i++) {
      logger.info("  WorkOrder[{}]: {}", i, value[i]);
    }
  }

  private static DynamicStruct readScalarValue(OpcUaClient client, NodeId nodeId) throws Exception {
    DataValue dataValue =
        client.readValues(0.0, TimestampsToReturn.Neither, List.of(nodeId)).get(0);

    ExtensionObject xo = (ExtensionObject) dataValue.getValue().getValue();
    assert xo != null;

    return (DynamicStruct) xo.decode(client.getDynamicEncodingContext());
  }

  private static DynamicStruct[] readArrayValue(OpcUaClient client, NodeId nodeId)
      throws Exception {
    DataValue dataValue =
        client.readValues(0.0, TimestampsToReturn.Neither, List.of(nodeId)).get(0);

    ExtensionObject[] xos = (ExtensionObject[]) dataValue.getValue().getValue();
    assert xos != null;

    EncodingContext ctx = client.getDynamicEncodingContext();

    return Arrays.stream(xos)
        .map(xo -> (DynamicStruct) xo.decode(ctx))
        .toArray(DynamicStruct[]::new);
  }

  private static StatusCode writeValue(OpcUaClient client, NodeId nodeId, DynamicStruct value)
      throws Exception {
    ExtensionObject xo =
        ExtensionObject.encodeDefaultBinary(
            client.getDynamicEncodingContext(), value, value.getDataType().getBinaryEncodingId());

    return client
        .writeValues(List.of(nodeId), List.of(DataValue.valueOnly(new Variant(xo))))
        .get(0);
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
