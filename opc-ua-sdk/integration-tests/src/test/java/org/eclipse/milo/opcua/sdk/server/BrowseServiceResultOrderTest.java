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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.server.access.AccessController.AccessResult;
import org.eclipse.milo.opcua.sdk.server.access.DefaultAccessController;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataChangeTrigger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DeadbandType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DataChangeFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateResult;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ViewDescription;
import org.eclipse.milo.opcua.stack.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Part 4 §5.9.2.2: the Browse results match the size and order of nodesToBrowse.
 *
 * <p>The server resolves some operations before it browses the rest: an invalid BrowseDirection, an
 * unknown ReferenceTypeId, and a starting Node the AccessController denies Browse on. These tests
 * put such an operation ahead of browsed ones and check that every operation keeps its own result
 * at its own position.
 */
class BrowseServiceResultOrderTest extends AbstractClientServerTest {

  private static final NodeId UNKNOWN_REFERENCE_TYPE = new NodeId(0, 999999);

  /** The test Node this server's AccessController denies Browse on, for every Session. */
  private static final String BROWSE_DENIED_NODE = "TestInt32";

  @Override
  protected TestServer createTestServer() throws Exception {
    return TestServer.create(
        configBuilder ->
            configBuilder.setAccessControllerFactory(BrowseDenyingAccessController::new));
  }

  Stream<Arguments> earlyResolvedOperations() {
    return Stream.of(
        arguments(
            named(
                "unknown ReferenceTypeId",
                browseDescription(
                    NodeIds.RootFolder, BrowseDirection.Forward, UNKNOWN_REFERENCE_TYPE)),
            "Bad_ReferenceTypeIdInvalid \\[\\]"),
        arguments(
            named(
                "invalid BrowseDirection",
                browseDescription(
                    NodeIds.RootFolder, BrowseDirection.Invalid, NodeIds.HierarchicalReferences)),
            "Bad_BrowseDirectionInvalid \\[\\]"),
        // Browsed, this operation would return the inverse HasComponent reference to Objects.
        arguments(
            named(
                "Browse denied",
                browseDescription(
                    newNodeId(BROWSE_DENIED_NODE),
                    BrowseDirection.Inverse,
                    NodeIds.HierarchicalReferences)),
            "Good \\[\\]"));
  }

  @ParameterizedTest
  @MethodSource("earlyResolvedOperations")
  void earlyResolvedOperationBeforeBrowsedOperationKeepsBothResultsInPlace(
      BrowseDescription earlyResolved, String expectedSummary) throws UaException {

    List<BrowseResult> results =
        browse(
            List.of(
                earlyResolved,
                browseDescription(
                    NodeIds.ObjectsFolder,
                    BrowseDirection.Forward,
                    NodeIds.HierarchicalReferences)));

    assertLinesMatch(List.of(expectedSummary, "Good \\[.*\\bServer\\b.*\\]"), summarize(results));
  }

  @ParameterizedTest
  @MethodSource("earlyResolvedOperations")
  void earlyResolvedOperationBetweenBrowsedOperationsKeepsAllResultsInPlace(
      BrowseDescription earlyResolved, String expectedSummary) throws UaException {

    List<BrowseResult> results =
        browse(
            List.of(
                browseDescription(
                    NodeIds.RootFolder, BrowseDirection.Forward, NodeIds.HierarchicalReferences),
                earlyResolved,
                browseDescription(
                    NodeIds.ObjectsFolder,
                    BrowseDirection.Forward,
                    NodeIds.HierarchicalReferences)));

    assertLinesMatch(
        List.of("Good \\[.*\\bObjects\\b.*\\]", expectedSummary, "Good \\[.*\\bServer\\b.*\\]"),
        summarize(results));
  }

  /**
   * Percent Deadband needs each Node's TypeDefinition, which the server finds with one Browse over
   * all the requested Nodes and pairs with them by position. A Browse-denied Node ahead of an
   * AnalogItemType Node must not take the AnalogItemType Node's TypeDefinition, or the AnalogItem
   * is rejected as if it had none.
   */
  @Test
  void percentDeadbandOnAnalogItemAfterBrowseDeniedNodeIsAccepted() throws UaException {
    var subscription = new OpcUaSubscription(client);
    subscription.create();

    try {
      UInteger subscriptionId = subscription.getSubscriptionId().orElseThrow();

      CreateMonitoredItemsResponse response =
          client.createMonitoredItems(
              subscriptionId,
              TimestampsToReturn.Both,
              List.of(
                  percentDeadbandRequest(newNodeId(BROWSE_DENIED_NODE), 1),
                  percentDeadbandRequest(newNodeId("TestAnalogValue"), 2)));

      List<MonitoredItemCreateResult> results = Lists.ofNullable(response.getResults());

      assertEquals(
          List.of("Bad_MonitoredItemFilterUnsupported", "Good"),
          results.stream().map(r -> statusName(r.getStatusCode())).toList(),
          "TestInt32 is not an AnalogItem; TestAnalogValue is");
    } finally {
      subscription.delete();
    }
  }

  private List<BrowseResult> browse(List<BrowseDescription> nodesToBrowse) throws UaException {
    BrowseResponse response =
        client.browse(
            new ViewDescription(NodeId.NULL_VALUE, DateTime.MIN_VALUE, uint(0)),
            uint(0),
            nodesToBrowse);

    return Lists.ofNullable(response.getResults());
  }

  private MonitoredItemCreateRequest percentDeadbandRequest(NodeId nodeId, int clientHandle) {
    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.Percent.getValue()), 10.0);

    return new MonitoredItemCreateRequest(
        new ReadValueId(nodeId, AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE),
        MonitoringMode.Reporting,
        new MonitoringParameters(
            uint(clientHandle),
            100.0,
            ExtensionObject.encode(client.getStaticEncodingContext(), filter),
            uint(1),
            true));
  }

  private static BrowseDescription browseDescription(
      NodeId nodeId, BrowseDirection direction, NodeId referenceTypeId) {

    return new BrowseDescription(
        nodeId,
        direction,
        referenceTypeId,
        true,
        uint(NodeClass.Unspecified.getValue()),
        uint(BrowseResultMask.All.getValue()));
  }

  /** Summarize each result as its status name and the BrowseNames of its references. */
  private static List<String> summarize(List<BrowseResult> results) {
    return results.stream().map(BrowseServiceResultOrderTest::summarize).toList();
  }

  private static String summarize(BrowseResult result) {
    ReferenceDescription[] references = result.getReferences();

    List<String> browseNames =
        references == null
            ? List.of()
            : Arrays.stream(references).map(r -> r.getBrowseName().name()).toList();

    return statusName(result.getStatusCode()) + " " + browseNames;
  }

  private static String statusName(StatusCode statusCode) {
    if (statusCode.value() == StatusCode.GOOD.value()) {
      return "Good";
    }

    return StatusCodes.lookup(statusCode.value())
        .map(nameAndDescription -> nameAndDescription[0])
        .orElse(String.format("0x%08X", statusCode.value()));
  }

  /** Denies Browse on {@link #BROWSE_DENIED_NODE} and otherwise defers to the default rules. */
  private static class BrowseDenyingAccessController extends DefaultAccessController {

    private final OpcUaServer server;

    BrowseDenyingAccessController(OpcUaServer server) {
      super(server);

      this.server = server;
    }

    @Override
    public Map<NodeId, AccessResult> checkBrowseAccess(Session session, List<NodeId> nodeIds) {
      var results = new HashMap<>(super.checkBrowseAccess(session, nodeIds));

      // The test namespace is registered after the server creates its AccessController.
      UShort namespaceIndex = server.getNamespaceTable().getIndex(TestNamespace.NAMESPACE_URI);

      if (namespaceIndex != null) {
        var deniedNodeId = new NodeId(namespaceIndex, BROWSE_DENIED_NODE);
        results.computeIfPresent(deniedNodeId, (nodeId, result) -> AccessResult.DENIED_USER_ACCESS);
      }

      return results;
    }
  }
}
