/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Static diagnostics Variables beneath Server.ServerDiagnostics stay in the address space while the
 * EnabledFlag is false, and their access levels keep CurrentRead. A Value read must therefore not
 * fail with Bad_NotReadable, which Part 4 §7.38.2 defines as "the access level does not allow
 * reading or subscribing to the Node". It reports Bad_OutOfService, "the source of the data is not
 * operational" (Part 8 §7.3.2), until diagnostics are turned on.
 */
class StaticDiagnosticsVariablesEnabledFlagTest extends AbstractClientServerTest {

  private static final List<AttributeId> ACCESS_LEVEL_ATTRIBUTES =
      List.of(AttributeId.AccessLevel, AttributeId.AccessLevelEx, AttributeId.UserAccessLevel);

  private ServerDiagnosticsTypeNode diagnosticsNode;

  @BeforeEach
  void getDiagnosticsNode() {
    diagnosticsNode =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow();

    assertEquals(Boolean.FALSE, diagnosticsNode.getEnabledFlag(), "diagnostics start disabled");
  }

  @AfterEach
  void disableDiagnostics() {
    diagnosticsNode.setEnabledFlag(false);
  }

  /**
   * Every static diagnostics Variable a client can read over SecurityPolicy None. The
   * SessionSecurityDiagnosticsArray requires encryption and is covered separately.
   */
  static Stream<Arguments> staticDiagnosticsVariables() {
    return Stream.of(
        Arguments.of(
            "ServerDiagnosticsSummary", NodeIds.Server_ServerDiagnostics_ServerDiagnosticsSummary),
        Arguments.of(
            "ServerViewCount",
            NodeIds.Server_ServerDiagnostics_ServerDiagnosticsSummary_ServerViewCount),
        Arguments.of(
            "CurrentSessionCount",
            NodeIds.Server_ServerDiagnostics_ServerDiagnosticsSummary_CurrentSessionCount),
        Arguments.of(
            "CumulatedSessionCount",
            NodeIds.Server_ServerDiagnostics_ServerDiagnosticsSummary_CumulatedSessionCount),
        Arguments.of(
            "SecurityRejectedSessionCount",
            NodeIds.Server_ServerDiagnostics_ServerDiagnosticsSummary_SecurityRejectedSessionCount),
        Arguments.of(
            "RejectedSessionCount",
            NodeIds.Server_ServerDiagnostics_ServerDiagnosticsSummary_RejectedSessionCount),
        Arguments.of(
            "SessionTimeoutCount",
            NodeIds.Server_ServerDiagnostics_ServerDiagnosticsSummary_SessionTimeoutCount),
        Arguments.of(
            "SessionAbortCount",
            NodeIds.Server_ServerDiagnostics_ServerDiagnosticsSummary_SessionAbortCount),
        Arguments.of(
            "PublishingIntervalCount",
            NodeIds.Server_ServerDiagnostics_ServerDiagnosticsSummary_PublishingIntervalCount),
        Arguments.of(
            "CurrentSubscriptionCount",
            NodeIds.Server_ServerDiagnostics_ServerDiagnosticsSummary_CurrentSubscriptionCount),
        Arguments.of(
            "CumulatedSubscriptionCount",
            NodeIds.Server_ServerDiagnostics_ServerDiagnosticsSummary_CumulatedSubscriptionCount),
        Arguments.of(
            "SecurityRejectedRequestsCount",
            NodeIds
                .Server_ServerDiagnostics_ServerDiagnosticsSummary_SecurityRejectedRequestsCount),
        Arguments.of(
            "RejectedRequestsCount",
            NodeIds.Server_ServerDiagnostics_ServerDiagnosticsSummary_RejectedRequestsCount),
        Arguments.of(
            "SessionDiagnosticsArray",
            NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionDiagnosticsArray),
        Arguments.of(
            "SubscriptionDiagnosticsArray",
            NodeIds.Server_ServerDiagnostics_SubscriptionDiagnosticsArray));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("staticDiagnosticsVariables")
  void valueReadsOutOfServiceWhileDiagnosticsAreDisabled(String name, NodeId nodeId)
      throws Exception {

    DataValue[] values = readValueAndAccessLevels(nodeId);

    assertEquals(
        new StatusCode(StatusCodes.Bad_OutOfService), values[0].statusCode(), "Value status");
    assertNull(values[0].value().value(), "a Bad Value carries no value");

    // The access levels stay true: reading is allowed, the data source is off.
    assertTrue(
        AccessLevel.fromValue((UByte) values[1].value().value()).contains(AccessLevel.CurrentRead),
        "AccessLevel includes CurrentRead");
    assertTrue(
        new AccessLevelExType((UInteger) values[2].value().value()).getCurrentRead(),
        "AccessLevelEx includes CurrentRead");
    assertTrue(
        AccessLevel.fromValue((UByte) values[3].value().value()).contains(AccessLevel.CurrentRead),
        "UserAccessLevel includes CurrentRead");
  }

  // Only the Value follows the flag. The access levels a client checks before reading or
  // subscribing are the same in both states.
  @ParameterizedTest(name = "{0}")
  @MethodSource("staticDiagnosticsVariables")
  void valueReadsGoodOnceDiagnosticsAreEnabled(String name, NodeId nodeId) throws Exception {
    DataValue[] disabled = readValueAndAccessLevels(nodeId);

    diagnosticsNode.setEnabledFlag(true);

    DataValue[] enabled = readValueAndAccessLevels(nodeId);

    assertEquals(StatusCode.GOOD, enabled[0].statusCode(), "Value status");
    assertNotNull(enabled[0].value().value(), "a Good Value carries a value");
    assertEquals(
        accessLevels(disabled),
        accessLevels(enabled),
        "access levels do not change with EnabledFlag");
  }

  // The test client can't read this array: its AccessRestrictions require an encrypted channel and,
  // by default, a SecurityAdmin role. It uses the same Value filter as the other static Variables,
  // so read it through the node's filter chain on the server.
  @Test
  void sessionSecurityDiagnosticsArrayValueTracksEnabledFlag() throws Exception {
    SessionSecurityDiagnosticsArrayTypeNode node =
        diagnosticsNode
            .getSessionsDiagnosticsSummaryNode()
            .getSessionSecurityDiagnosticsArrayNode();

    List<Object> disabledAccessLevels = readAccessLevels(node);
    DataValue disabled = (DataValue) readAttribute(node, AttributeId.Value);

    assertEquals(
        new StatusCode(StatusCodes.Bad_OutOfService),
        disabled.statusCode(),
        "Value status while disabled");
    assertNull(disabled.value().value(), "a Bad Value carries no value");

    diagnosticsNode.setEnabledFlag(true);

    DataValue enabled = (DataValue) readAttribute(node, AttributeId.Value);

    assertEquals(StatusCode.GOOD, enabled.statusCode(), "Value status once enabled");
    assertNotNull(enabled.value().value(), "a Good Value carries a value");
    assertEquals(
        disabledAccessLevels,
        readAccessLevels(node),
        "access levels do not change with EnabledFlag");
  }

  // CreateMonitoredItems checks the access levels, which allow reading while diagnostics are off,
  // so the item is created and its first notification carries the Value's status. The same item
  // must start reporting values once diagnostics are turned on, without the client recreating it.
  @Test
  void monitoredItemCreatedWhileDisabledReportsValuesOnceEnabled() throws Exception {
    var subscription = new OpcUaSubscription(client);
    subscription.setPublishingInterval(50.0);
    subscription.create();

    try {
      BlockingQueue<DataValue> values = new LinkedBlockingQueue<>();

      var item =
          OpcUaMonitoredItem.newDataItem(
              NodeIds.Server_ServerDiagnostics_ServerDiagnosticsSummary_CurrentSessionCount);
      item.setSamplingInterval(50.0);
      item.setDataValueListener((i, value) -> values.add(value));

      subscription.addMonitoredItem(item);
      subscription.createMonitoredItems();

      assertEquals(StatusCode.GOOD, item.getCreateResult().orElseThrow(), "create result");

      DataValue first = values.poll(5, TimeUnit.SECONDS);
      assertNotNull(first, "no notification while disabled");
      assertEquals(
          new StatusCode(StatusCodes.Bad_OutOfService),
          first.statusCode(),
          "notification status while disabled");

      diagnosticsNode.setEnabledFlag(true);

      DataValue next = values.poll(5, TimeUnit.SECONDS);
      assertNotNull(next, "no notification after enabling diagnostics");
      assertEquals(StatusCode.GOOD, next.statusCode(), "notification status once enabled");
      assertTrue(
          ((UInteger) next.value().value()).longValue() >= 1,
          "CurrentSessionCount counts the test client's Session");
    } finally {
      subscription.delete();
    }
  }

  /**
   * Reads the Value followed by the {@link #ACCESS_LEVEL_ATTRIBUTES} through the test client,
   * asserting the access-level reads succeed.
   */
  private DataValue[] readValueAndAccessLevels(NodeId nodeId) throws Exception {
    List<ReadValueId> readValueIds =
        Stream.concat(Stream.of(AttributeId.Value), ACCESS_LEVEL_ATTRIBUTES.stream())
            .map(attributeId -> new ReadValueId(nodeId, attributeId.uid(), null, null))
            .toList();

    DataValue[] values = client.read(0.0, TimestampsToReturn.Neither, readValueIds).getResults();

    for (int i = 1; i < values.length; i++) {
      assertEquals(
          StatusCode.GOOD,
          values[i].statusCode(),
          readValueIds.get(i).getAttributeId() + " read status");
    }

    return values;
  }

  /** The access-level values from a {@link #readValueAndAccessLevels} result. */
  private static List<Object> accessLevels(DataValue[] values) {
    return Stream.of(values).skip(1).map(value -> value.value().value()).toList();
  }

  private static List<Object> readAccessLevels(UaVariableNode node) throws UaException {
    List<Object> values = new ArrayList<>();
    for (AttributeId attributeId : ACCESS_LEVEL_ATTRIBUTES) {
      values.add(readAttribute(node, attributeId));
    }
    return values;
  }

  /** Reads an attribute through the node's filter chain without a Session. */
  private static Object readAttribute(UaVariableNode node, AttributeId attributeId)
      throws UaException {

    return node.getFilterChain().readAttribute(null, node, attributeId);
  }
}
