/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.servicesets.impl.helpers;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.ReferenceResult;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.access.AccessController;
import org.eclipse.milo.opcua.sdk.server.access.AccessController.AccessResult;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.ServerTable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.NamespaceReference;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.ServerReference;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.ViewDescription;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/** Unit tests for {@link BrowseHelper} target filtering, attribute fallback, and access checks. */
class BrowseHelperTest {

  private static final String LOCAL_SERVER_URI = "urn:example:local:server";
  private static final String REMOTE_SERVER_URI = "urn:example:remote:server";
  private static final String LOCAL_NS_URI = "urn:example:local";
  private static final String REMOTE_NS_URI = "urn:example:remote";

  private static final ExpandedNodeId LOCAL_TARGET = new NodeId(1, "Local").expanded();
  private static final ExpandedNodeId LOCAL_SERVER_URI_TARGET =
      new ExpandedNodeId(
          ServerReference.of(LOCAL_SERVER_URI), NamespaceReference.of(LOCAL_NS_URI), "LocalByUri");
  private static final ExpandedNodeId REMOTE_SERVER_INDEX_TARGET =
      new ExpandedNodeId(ServerReference.of(1), NamespaceReference.of(REMOTE_NS_URI), "R1");
  private static final ExpandedNodeId REMOTE_SERVER_URI_TARGET =
      new ExpandedNodeId(
          ServerReference.of(REMOTE_SERVER_URI), NamespaceReference.of(REMOTE_NS_URI), "R2");

  @Nested
  class FilterNodeClass {

    /**
     * OPC 10000-4 §5.9.2.2 Table 34 & §7.16: nodeClassMask == 0 returns all NodeClasses; when
     * NodeClass is unknown for a remote Node, nodeClassMask is ignored; when NodeClass is unknown
     * for a local Node (including one addressed by this server's own URI), it matches only
     * nodeClassMask == 0 because Unspecified has no mask bit.
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("filterCases")
    void filterNodeClassFollowsPart4Table34(
        String label,
        ExpandedNodeId targetNodeId,
        NodeClass nodeClass,
        long mask,
        boolean expected) {

      ServerTable serverTable = defaultServerTable();
      BrowseDescription description = browseDescription(new NodeId(1, "Source"), mask);

      assertEquals(
          expected,
          BrowseHelper.filterNodeClass(description, targetNodeId, serverTable, nodeClass),
          label);
    }

    static Stream<Arguments> filterCases() {
      long objectMask = NodeClass.Object.getValue();
      long variableMask = NodeClass.Variable.getValue();
      long objectOrVariableMask = objectMask | variableMask;

      return Stream.of(
          arguments("local Object with mask 0", LOCAL_TARGET, NodeClass.Object, 0L, true),
          arguments(
              "local Object with Object mask", LOCAL_TARGET, NodeClass.Object, objectMask, true),
          arguments(
              "local Object with Variable mask",
              LOCAL_TARGET,
              NodeClass.Object,
              variableMask,
              false),
          arguments(
              "local Object with Object|Variable mask",
              LOCAL_TARGET,
              NodeClass.Object,
              objectOrVariableMask,
              true),
          arguments("local Unspecified with mask 0", LOCAL_TARGET, NodeClass.Unspecified, 0L, true),
          arguments(
              "local Unspecified with Object mask",
              LOCAL_TARGET,
              NodeClass.Unspecified,
              objectMask,
              false),
          arguments(
              "local Unspecified with Variable mask",
              LOCAL_TARGET,
              NodeClass.Unspecified,
              variableMask,
              false),
          arguments("local null NodeClass with mask 0", LOCAL_TARGET, null, 0L, true),
          arguments("local null NodeClass with Object mask", LOCAL_TARGET, null, objectMask, false),
          arguments(
              "local own-ServerUri Unspecified with mask 0",
              LOCAL_SERVER_URI_TARGET,
              NodeClass.Unspecified,
              0L,
              true),
          arguments(
              "local own-ServerUri Unspecified excluded by Object mask",
              LOCAL_SERVER_URI_TARGET,
              NodeClass.Unspecified,
              objectMask,
              false),
          arguments(
              "remote serverIndex Unspecified with mask 0",
              REMOTE_SERVER_INDEX_TARGET,
              NodeClass.Unspecified,
              0L,
              true),
          arguments(
              "remote serverIndex Unspecified ignores Object mask",
              REMOTE_SERVER_INDEX_TARGET,
              NodeClass.Unspecified,
              objectMask,
              true),
          arguments(
              "remote serverIndex Unspecified ignores Variable mask",
              REMOTE_SERVER_INDEX_TARGET,
              NodeClass.Unspecified,
              variableMask,
              true),
          arguments(
              "remote serverIndex null NodeClass ignores Object mask",
              REMOTE_SERVER_INDEX_TARGET,
              null,
              objectMask,
              true),
          arguments(
              "remote serverUri Unspecified ignores Method mask",
              REMOTE_SERVER_URI_TARGET,
              NodeClass.Unspecified,
              NodeClass.Method.getValue(),
              true));
    }
  }

  @Nested
  class RemoteAndUnreadableTargets {

    /**
     * Non-local targets must never be converted to NodeId.NULL_VALUE for local attribute reads or
     * AccessController.checkBrowseAccess() checks.
     */
    @Test
    void remoteOnlyTargetsSkipLocalAttributeReadAndTargetAccessCheck() {
      NodeId sourceId = new NodeId(1, "Source");
      NamespaceTable namespaceTable = defaultNamespaceTable();
      ServerTable serverTable = defaultServerTable();

      AddressSpaceManager addressSpaceManager = mock(AddressSpaceManager.class);
      when(addressSpaceManager.browse(any(), any(), eq(List.of(sourceId))))
          .thenReturn(
              List.of(
                  ReferenceResult.of(
                      List.of(
                          new Reference(
                              sourceId, NodeIds.Organizes, REMOTE_SERVER_INDEX_TARGET, true)))));

      AccessController accessController = mock(AccessController.class);
      when(accessController.checkBrowseAccess(any(), eq(List.of(sourceId))))
          .thenReturn(Map.of(sourceId, AccessResult.ALLOWED));

      OpcUaServer server =
          mockServer(serverTable, namespaceTable, addressSpaceManager, accessController);
      Session session = mock(Session.class);

      BrowseRequest request =
          browseRequest(browseDescription(sourceId, NodeClass.Object.getValue()));

      List<BrowseResult> results = BrowseHelper.browse(server, () -> Optional.of(session), request);

      assertEquals(1, results.size());
      BrowseResult result = results.get(0);
      assertTrue(result.getStatusCode().isGood());
      assertEquals(1, result.getReferences().length);

      ReferenceDescription rd = result.getReferences()[0];
      assertEquals(REMOTE_SERVER_INDEX_TARGET, rd.getNodeId());
      assertEquals(QualifiedName.NULL_VALUE, rd.getBrowseName());
      assertEquals(LocalizedText.NULL_VALUE, rd.getDisplayName());
      assertEquals(NodeClass.Unspecified, rd.getNodeClass());
      assertEquals(ExpandedNodeId.NULL_VALUE, rd.getTypeDefinition());

      // No local targets existed, so neither local attribute reads nor target browse-access checks
      // should have been invoked.
      verify(addressSpaceManager, never()).read(any(), anyDouble(), any(), anyList());
      verify(accessController).checkBrowseAccess(session, List.of(sourceId));
      verify(accessController, never()).checkBrowseAccess(session, List.of(NodeId.NULL_VALUE));
    }

    /**
     * OPC 10000-4 §7.16: a local target addressed by this server's own ServerUri must still undergo
     * local attribute reads, NodeClass filtering, and AccessController.checkBrowseAccess().
     */
    @Test
    void localTargetAddressedByOwnServerUriIsCheckedByAccessController() {
      NodeId sourceId = new NodeId(1, "Source");
      NodeId deniedLocalId = new NodeId(1, "LocalByUri");
      NamespaceTable namespaceTable = defaultNamespaceTable();
      ServerTable serverTable = defaultServerTable();

      AddressSpaceManager addressSpaceManager = mock(AddressSpaceManager.class);
      when(addressSpaceManager.browse(any(), any(), eq(List.of(sourceId))))
          .thenReturn(
              List.of(
                  ReferenceResult.of(
                      List.of(
                          new Reference(
                              sourceId, NodeIds.Organizes, LOCAL_SERVER_URI_TARGET, true)))));
      when(addressSpaceManager.getManagedReferences(eq(deniedLocalId), any()))
          .thenReturn(
              List.of(
                  new Reference(
                      deniedLocalId,
                      NodeIds.HasTypeDefinition,
                      NodeIds.FolderType.expanded(),
                      true)));
      when(addressSpaceManager.read(any(), anyDouble(), any(), anyList()))
          .thenAnswer(
              invocation -> {
                List<ReadValueId> readIds = invocation.getArgument(3);
                assertEquals(3, readIds.size());
                assertEquals(deniedLocalId, readIds.get(0).getNodeId());
                return List.of(
                    new DataValue(new Variant(new QualifiedName(1, "LocalByUri"))),
                    new DataValue(new Variant(LocalizedText.english("LocalByUri"))),
                    new DataValue(new Variant(NodeClass.Object)));
              });

      AccessController accessController = mock(AccessController.class);
      when(accessController.checkBrowseAccess(any(), eq(List.of(sourceId))))
          .thenReturn(Map.of(sourceId, AccessResult.ALLOWED));
      when(accessController.checkBrowseAccess(any(), eq(List.of(deniedLocalId))))
          .thenReturn(Map.of(deniedLocalId, AccessResult.DENIED_USER_ACCESS));

      OpcUaServer server =
          mockServer(serverTable, namespaceTable, addressSpaceManager, accessController);
      Session session = mock(Session.class);

      BrowseResult result =
          BrowseHelper.browse(
                  server,
                  () -> Optional.of(session),
                  browseRequest(browseDescription(sourceId, 0L)))
              .get(0);

      assertTrue(result.getStatusCode().isGood());
      assertEquals(0, result.getReferences().length, "denied local target must be filtered out");
      verify(accessController).checkBrowseAccess(session, List.of(deniedLocalId));
    }

    /**
     * Unrepresentable targets (unregistered remote ServerUri/ServerIndex, remote relative namespace
     * index outside NamespaceTable, or local unregistered namespace URI) are skipped while valid
     * sibling references remain intact.
     */
    @Test
    void unrepresentableTargetsAreSkippedWhileValidReferencesRemain() {
      NodeId sourceId = new NodeId(1, "Source");
      NamespaceTable namespaceTable = defaultNamespaceTable();
      ServerTable serverTable = defaultServerTable();

      ExpandedNodeId unregisteredServerUri =
          new ExpandedNodeId(
              ServerReference.of("urn:example:unregistered:server"),
              NamespaceReference.of(REMOTE_NS_URI),
              "BadServerUri");
      ExpandedNodeId unregisteredServerIndex =
          new ExpandedNodeId(
              ServerReference.of(99), NamespaceReference.of(REMOTE_NS_URI), "BadServerIndex");
      ExpandedNodeId unmappedRemoteNsIndex =
          new ExpandedNodeId(
              ServerReference.of(1), NamespaceReference.of(ushort(99)), "BadRemoteNsIndex");
      ExpandedNodeId unregisteredLocalNsUri =
          ExpandedNodeId.of("urn:example:unregistered:ns", "BadLocalNsUri");
      ExpandedNodeId unregisteredLocalNsIndex = ExpandedNodeId.of(ushort(42), "BadLocalNsIndex");

      AddressSpaceManager addressSpaceManager = mock(AddressSpaceManager.class);
      when(addressSpaceManager.browse(any(), any(), eq(List.of(sourceId))))
          .thenReturn(
              List.of(
                  ReferenceResult.of(
                      List.of(
                          new Reference(
                              sourceId, NodeIds.Organizes, REMOTE_SERVER_URI_TARGET, true),
                          new Reference(sourceId, NodeIds.Organizes, unregisteredServerUri, true),
                          new Reference(sourceId, NodeIds.Organizes, unregisteredServerIndex, true),
                          new Reference(sourceId, NodeIds.Organizes, unmappedRemoteNsIndex, true),
                          new Reference(sourceId, NodeIds.Organizes, unregisteredLocalNsUri, true),
                          new Reference(
                              sourceId, NodeIds.Organizes, unregisteredLocalNsIndex, true)))));

      AccessController accessController = mock(AccessController.class);
      when(accessController.checkBrowseAccess(any(), eq(List.of(sourceId))))
          .thenReturn(Map.of(sourceId, AccessResult.ALLOWED));

      OpcUaServer server =
          mockServer(serverTable, namespaceTable, addressSpaceManager, accessController);
      Session session = mock(Session.class);

      BrowseResult result =
          BrowseHelper.browse(
                  server,
                  () -> Optional.of(session),
                  browseRequest(browseDescription(sourceId, 0L)))
              .get(0);

      assertTrue(result.getStatusCode().isGood());
      assertEquals(1, result.getReferences().length);
      assertEquals(
          new ExpandedNodeId(ServerReference.of(1), NamespaceReference.of(REMOTE_NS_URI), "R2"),
          result.getReferences()[0].getNodeId());
    }

    /**
     * When a local target has some readable attributes and some Bad/unreadable attributes, readable
     * values are kept and unreadable ones default to non-null placeholders (never null NodeClass).
     */
    @Test
    void partiallyUnreadableLocalTargetUsesNonNullDefaultsForMissingAttributes() {
      NodeId sourceId = new NodeId(1, "Source");
      NodeId targetId = new NodeId(1, "PartialTarget");
      NamespaceTable namespaceTable = defaultNamespaceTable();
      ServerTable serverTable = defaultServerTable();

      AddressSpaceManager addressSpaceManager = mock(AddressSpaceManager.class);
      when(addressSpaceManager.browse(any(), any(), eq(List.of(sourceId))))
          .thenReturn(
              List.of(
                  ReferenceResult.of(
                      List.of(
                          new Reference(sourceId, NodeIds.Organizes, targetId.expanded(), true)))));
      when(addressSpaceManager.read(any(), anyDouble(), any(), anyList()))
          .thenAnswer(
              invocation -> {
                List<ReadValueId> readIds = invocation.getArgument(3);
                assertEquals(3, readIds.size());
                assertEquals(targetId, readIds.get(0).getNodeId());
                return List.of(
                    new DataValue(new Variant(new QualifiedName(1, "PartialTarget"))),
                    new DataValue(new StatusCode(StatusCodes.Bad_NotReadable)),
                    new DataValue(new StatusCode(StatusCodes.Bad_NotReadable)));
              });

      AccessController accessController = mock(AccessController.class);
      when(accessController.checkBrowseAccess(any(), anyList()))
          .thenReturn(Map.of(sourceId, AccessResult.ALLOWED, targetId, AccessResult.ALLOWED));

      OpcUaServer server =
          mockServer(serverTable, namespaceTable, addressSpaceManager, accessController);
      Session session = mock(Session.class);

      // Mask 0 includes the unreadable-NodeClass local target with non-null defaults.
      BrowseResult allMaskResult =
          BrowseHelper.browse(
                  server,
                  () -> Optional.of(session),
                  browseRequest(browseDescription(sourceId, 0L)))
              .get(0);

      assertTrue(allMaskResult.getStatusCode().isGood());
      assertEquals(1, allMaskResult.getReferences().length);
      ReferenceDescription rd = allMaskResult.getReferences()[0];
      assertEquals(new QualifiedName(1, "PartialTarget"), rd.getBrowseName());
      assertEquals(LocalizedText.NULL_VALUE, rd.getDisplayName());
      assertEquals(NodeClass.Unspecified, rd.getNodeClass());

      // A specific mask excludes the local target whose NodeClass could not be read.
      BrowseResult objectMaskResult =
          BrowseHelper.browse(
                  server,
                  () -> Optional.of(session),
                  browseRequest(browseDescription(sourceId, NodeClass.Object.getValue())))
              .get(0);

      assertTrue(objectMaskResult.getStatusCode().isGood());
      assertEquals(0, objectMaskResult.getReferences().length);
    }

    private OpcUaServer mockServer(
        ServerTable serverTable,
        NamespaceTable namespaceTable,
        AddressSpaceManager addressSpaceManager,
        AccessController accessController) {

      ReferenceTypeTree referenceTypeTree = mock(ReferenceTypeTree.class);
      when(referenceTypeTree.containsType(NodeIds.Organizes)).thenReturn(true);

      OpcUaServerConfigLimits limits =
          new OpcUaServerConfigLimits() {
            @Override
            public UShort getMaxBrowseContinuationPoints() {
              return ushort(10);
            }
          };

      OpcUaServerConfig config = mock(OpcUaServerConfig.class);
      when(config.getLimits()).thenReturn(limits);

      OpcUaServer server = mock(OpcUaServer.class);
      when(server.getConfig()).thenReturn(config);
      when(server.getServerTable()).thenReturn(serverTable);
      when(server.getNamespaceTable()).thenReturn(namespaceTable);
      when(server.getReferenceTypeTree()).thenReturn(referenceTypeTree);
      when(server.getAddressSpaceManager()).thenReturn(addressSpaceManager);
      when(server.getAccessController()).thenReturn(accessController);
      return server;
    }
  }

  private static ServerTable defaultServerTable() {
    var serverTable = new ServerTable();
    serverTable.add(LOCAL_SERVER_URI);
    serverTable.add(REMOTE_SERVER_URI);
    return serverTable;
  }

  private static NamespaceTable defaultNamespaceTable() {
    var namespaceTable = new NamespaceTable();
    namespaceTable.add(LOCAL_NS_URI);
    return namespaceTable;
  }

  private static BrowseDescription browseDescription(NodeId nodeId, long nodeClassMask) {
    return new BrowseDescription(
        nodeId,
        BrowseDirection.Forward,
        NodeIds.Organizes,
        true,
        uint(nodeClassMask),
        uint(BrowseResultMask.All.getValue()));
  }

  private static BrowseRequest browseRequest(BrowseDescription... descriptions) {
    return new BrowseRequest(
        new RequestHeader(NodeId.NULL_VALUE, DateTime.now(), uint(1), uint(0), null, uint(0), null),
        new ViewDescription(NodeId.NULL_VALUE, DateTime.NULL_VALUE, uint(0)),
        uint(0),
        descriptions);
  }
}
