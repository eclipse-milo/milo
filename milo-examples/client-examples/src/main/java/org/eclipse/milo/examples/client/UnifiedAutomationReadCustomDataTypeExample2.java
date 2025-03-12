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

import com.google.gson.JsonObject;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient.DefaultDataTypeManagerInitializer;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.sdk.core.types.json.JsonCodecFactory;
import org.eclipse.milo.sdk.core.types.json.JsonStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An example that shows reading the value of a node whose DataType is a custom structure type.
 *
 * <p>Requires the Unified Automation CPP Demo server be running and the endpoint URL be pointing to
 * it.
 */
public class UnifiedAutomationReadCustomDataTypeExample2 implements ClientExample {

  public static void main(String[] args) throws Exception {
    var example = new UnifiedAutomationReadCustomDataTypeExample2();

    new ClientExampleRunner(example, false).run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
    client.setDataTypeManagerInitializer(
        new DefaultDataTypeManagerInitializer(JsonCodecFactory::create));

    client.connect();

    readWriteReadPerson(client);
    readWriteReadWorkOrder(client);
    readWriteCarExtras(client);
    readWorkOrderArray(client);

    future.complete(client);
  }

  private void readWriteReadPerson(OpcUaClient client) throws Exception {
    NodeId nodeId = NodeId.parse("ns=3;s=Person1");

    JsonStruct struct = readScalarValue(client, nodeId);
    JsonObject value = struct.getJsonObject();
    logger.info("Person1: {}", value);

    Random r = new Random();
    value.addProperty("Name", "Fat Boy" + r.nextInt(100));
    value.addProperty("Gender", r.nextInt(2));

    StatusCode status = writeValue(client, nodeId, new JsonStruct(struct.getDataType(), value));
    System.out.println("write status: " + status);

    value = readScalarValue(client, nodeId).getJsonObject();
    logger.info("Person1': {}", value);
  }

  private void readWriteReadWorkOrder(OpcUaClient client) throws Exception {
    NodeId nodeId = NodeId.parse("ns=3;s=Demo.Static.Scalar.WorkOrder");

    JsonStruct struct = readScalarValue(client, nodeId);
    JsonObject value = struct.getJsonObject();
    logger.info("WorkOrder: {}", value);

    value.addProperty("ID", UUID.randomUUID().toString());

    StatusCode status = writeValue(client, nodeId, new JsonStruct(struct.getDataType(), value));
    System.out.println("write status: " + status);

    value = readScalarValue(client, nodeId).getJsonObject();
    logger.info("WorkOrder': {}", value);
  }

  private void readWriteCarExtras(OpcUaClient client) throws Exception {
    NodeId nodeId = NodeId.parse("ns=3;s=Demo.Static.Scalar.CarExtras");

    JsonStruct struct = readScalarValue(client, nodeId);
    JsonObject value = struct.getJsonObject();
    logger.info("CarExtras: {}", value);

    String b64 = value.get("Value").getAsString();
    byte[] decoded = Base64.getDecoder().decode(b64);
    decoded[0] = (byte) ~decoded[0];
    value.addProperty("Value", Base64.getEncoder().encodeToString(decoded));

    StatusCode status = writeValue(client, nodeId, new JsonStruct(struct.getDataType(), value));
    System.out.println("write status: " + status);

    value = readScalarValue(client, nodeId).getJsonObject();
    logger.info("CarExtras': {}", value);
  }

  private void readWorkOrderArray(OpcUaClient client) throws Exception {
    NodeId nodeId = NodeId.parse("ns=3;s=Demo.Static.Arrays.WorkOrder");

    JsonStruct[] structs = readArrayValue(client, nodeId);
    JsonObject[] values =
        Arrays.stream(structs).map(JsonStruct::getJsonObject).toArray(JsonObject[]::new);

    logger.info("WorkOrderArray:");
    for (int i = 0; i < values.length; i++) {
      logger.info("  WorkOrder[{}]: {}", i, values[i]);
    }
  }

  private static JsonStruct readScalarValue(OpcUaClient client, NodeId nodeId) throws Exception {
    DataValue dataValue =
        client.readValues(0.0, TimestampsToReturn.Neither, List.of(nodeId)).get(0);

    ExtensionObject xo = (ExtensionObject) dataValue.value().value();
    assert xo != null;

    return (JsonStruct) xo.decode(client.getDynamicEncodingContext());
  }

  private static JsonStruct[] readArrayValue(OpcUaClient client, NodeId nodeId) throws Exception {
    DataValue dataValue =
        client.readValues(0.0, TimestampsToReturn.Neither, List.of(nodeId)).get(0);

    ExtensionObject[] xos = (ExtensionObject[]) dataValue.value().value();
    assert xos != null;

    EncodingContext ctx = client.getDynamicEncodingContext();

    return Arrays.stream(xos).map(xo -> (JsonStruct) xo.decode(ctx)).toArray(JsonStruct[]::new);
  }

  private static StatusCode writeValue(OpcUaClient client, NodeId nodeId, JsonStruct value)
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
