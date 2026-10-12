/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.wiki;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Runs the tutorial programs together and checks the thermostat results that the Wiki's client and
 * server pages state, so a change to {@link FirstServer} that breaks a page fails here.
 *
 * <p>All tests share one server and one client. Tests that depend on Setpoint set it first.
 */
@Timeout(30)
class FirstProgramsTest {

  private static OpcUaServer server;
  private static OpcUaClient client;
  private static String endpointUrl;

  private static NodeId thermostatId;
  private static NodeId temperatureId;
  private static NodeId setpointId;
  private static NodeId adjustSetpointId;

  @BeforeAll
  static void startServerAndConnect() throws Exception {
    int port = freeLoopbackPort();
    endpointUrl = "opc.tcp://127.0.0.1:" + port + "/wiki";

    server = FirstServer.create(port);
    server.startup().get(10, TimeUnit.SECONDS);

    client = OpcUaClient.create(endpointUrl);
    client.connect();

    UShort namespaceIndex = client.getNamespaceTable().getIndex(FirstServer.NAMESPACE_URI);
    requireNonNull(namespaceIndex, "server does not expose the tutorial namespace");

    thermostatId = new NodeId(namespaceIndex, "Thermostat");
    temperatureId = new NodeId(namespaceIndex, "Temperature");
    setpointId = new NodeId(namespaceIndex, "Setpoint");
    adjustSetpointId = new NodeId(namespaceIndex, "AdjustSetpoint");
  }

  @AfterAll
  static void disconnectAndStopServer() throws Exception {
    try {
      if (client != null) {
        client.disconnect();
      }
    } finally {
      try {
        if (server != null) {
          server.shutdown().get(10, TimeUnit.SECONDS);
        }
      } finally {
        Stack.releaseSharedResources();
      }
    }
  }

  // The first-client tutorial must read the value the first-server tutorial publishes.
  @Test
  void firstClientReadsFirstServerTemperature() throws Exception {
    assertEquals(21.5, FirstClient.readTemperature(endpointUrl));
  }

  // Clients find the thermostat by browsing forward hierarchical references from Objects.
  @Test
  void browsingObjectsFindsThermostatThroughOrganizes() throws UaException {
    List<ReferenceDescription> references = client.getAddressSpace().browse(NodeIds.ObjectsFolder);

    List<NodeId> organizedIds =
        references.stream()
            .filter(reference -> NodeIds.Organizes.equals(reference.getReferenceTypeId()))
            .flatMap(FirstProgramsTest::targetNodeId)
            .toList();

    assertTrue(organizedIds.contains(thermostatId), "Objects organizes Thermostat");
  }

  // Browsing Thermostat is how clients discover everything that belongs to it.
  @Test
  void browsingThermostatFindsItsThreeComponents() throws UaException {
    List<ReferenceDescription> references = client.getAddressSpace().browse(thermostatId);

    Set<NodeId> referenceTypeIds =
        references.stream()
            .map(ReferenceDescription::getReferenceTypeId)
            .collect(Collectors.toSet());
    Set<NodeId> targetIds =
        references.stream().flatMap(FirstProgramsTest::targetNodeId).collect(Collectors.toSet());

    assertEquals(Set.of(NodeIds.HasComponent), referenceTypeIds);
    assertEquals(Set.of(temperatureId, setpointId, adjustSetpointId), targetIds);
  }

  @Test
  void writingADoubleToSetpointChangesItsValue() throws UaException {
    StatusCode status = write(setpointId, new Variant(30.0));

    assertEquals(StatusCode.GOOD, status);
    assertEquals(30.0, readGoodValue(setpointId));
  }

  // Part 4 §5.10.4: a value that does not match the Variable's DataType fails per operation.
  @Test
  void writingAStringToSetpointReturnsBadTypeMismatch() throws UaException {
    StatusCode status = write(setpointId, new Variant("warm"));

    assertEquals(new StatusCode(StatusCodes.Bad_TypeMismatch), status);
  }

  // Temperature's AccessLevel lacks CurrentWrite, so the server refuses the write and keeps 21.5.
  @Test
  void writingTemperatureReturnsBadNotWritable() throws UaException {
    StatusCode status = write(temperatureId, new Variant(25.0));

    assertEquals(new StatusCode(StatusCodes.Bad_NotWritable), status);
    assertEquals(21.5, readGoodValue(temperatureId), "Temperature unchanged");
  }

  @Test
  void adjustSetpointAddsDeltaAndReturnsTheNewSetpoint() throws UaException {
    setSetpoint(22.0);

    CallMethodResult result = callAdjustSetpoint(new Variant(1.5));

    assertEquals(StatusCode.GOOD, result.getStatusCode());
    assertArrayEquals(new Variant[] {new Variant(23.5)}, result.getOutputArguments());
    assertEquals(23.5, readGoodValue(setpointId));
  }

  // A rejected adjustment must leave Setpoint where it was, never outside 0.0 to 100.0.
  @ParameterizedTest
  @ValueSource(doubles = {78.5, -22.5})
  void adjustSetpointOutsideZeroToOneHundredFailsAndKeepsSetpoint(double delta) throws UaException {
    setSetpoint(22.0);

    CallMethodResult result = callAdjustSetpoint(new Variant(delta));

    assertEquals(new StatusCode(StatusCodes.Bad_OutOfRange), result.getStatusCode());
    assertEquals(22.0, readGoodValue(setpointId), "Setpoint unchanged");
  }

  // Part 4 §5.11.2: Bad_InvalidArgument with a per-input result tells the client which input was
  // wrong. The SDK's type check lets a null value through, so the handler must reject it as well.
  @ParameterizedTest
  @MethodSource("nonFiniteOrNullDeltas")
  void adjustSetpointRejectsNonFiniteOrNullDeltaAsOutOfRange(Variant delta) throws UaException {
    CallMethodResult result = callAdjustSetpoint(delta);

    assertEquals(new StatusCode(StatusCodes.Bad_InvalidArgument), result.getStatusCode());
    assertArrayEquals(
        new StatusCode[] {new StatusCode(StatusCodes.Bad_OutOfRange)},
        result.getInputArgumentResults());
  }

  static Stream<Variant> nonFiniteOrNullDeltas() {
    return Stream.of(
        new Variant(Double.NaN),
        new Variant(Double.POSITIVE_INFINITY),
        new Variant(Double.NEGATIVE_INFINITY),
        Variant.NULL_VALUE);
  }

  // The SDK checks input types against AdjustSetpoint's declared arguments before the handler runs.
  @Test
  void adjustSetpointRejectsStringDeltaAsTypeMismatch() throws UaException {
    CallMethodResult result = callAdjustSetpoint(new Variant("1.5"));

    assertEquals(new StatusCode(StatusCodes.Bad_InvalidArgument), result.getStatusCode());
    assertArrayEquals(
        new StatusCode[] {new StatusCode(StatusCodes.Bad_TypeMismatch)},
        result.getInputArgumentResults());
  }

  private static int freeLoopbackPort() throws Exception {
    try (var socket = new ServerSocket(0, 1, InetAddress.getByName("127.0.0.1"))) {
      return socket.getLocalPort();
    }
  }

  /** The reference's target as a local NodeId, or nothing if it is on another server. */
  private static Stream<NodeId> targetNodeId(ReferenceDescription reference) {
    return reference.getNodeId().toNodeId(client.getNamespaceTable()).stream();
  }

  private static Object readGoodValue(NodeId nodeId) throws UaException {
    DataValue value = client.readValue(0.0, TimestampsToReturn.Neither, nodeId);

    assertEquals(StatusCode.GOOD, value.statusCode(), "read status");

    return value.value().value();
  }

  private static StatusCode write(NodeId nodeId, Variant value) throws UaException {
    DataValue dataValue = DataValue.valueOnly(value);

    List<StatusCode> results = client.writeValues(List.of(nodeId), List.of(dataValue));

    return results.get(0);
  }

  private static void setSetpoint(double value) throws UaException {
    StatusCode status = write(setpointId, new Variant(value));

    assertEquals(StatusCode.GOOD, status, "set Setpoint before the call");
  }

  private static CallMethodResult callAdjustSetpoint(Variant delta) throws UaException {
    var request = new CallMethodRequest(thermostatId, adjustSetpointId, new Variant[] {delta});

    CallResponse response = client.call(List.of(request));

    return requireNonNull(response.getResults())[0];
  }
}
