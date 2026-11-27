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
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.access.AccessController.AccessResult;
import org.eclipse.milo.opcua.sdk.server.access.DefaultAccessController;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilter;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilterContext;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.NamespaceReference;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.ServerReference;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.util.Lists;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * End-to-end Browse tests verifying that references to remote servers (OPC 10000-4 §5.9.2.2, §7.16,
 * §7.29) and missing or unreadable local targets (OPC 10000-3 §4.4.4) are handled accurately over
 * OPC TCP without dropping valid targets, bypassing local authorization, or failing binary
 * encoding.
 */
class BrowseRemoteAndUnreadableTargetsTest extends AbstractClientServerTest {

  private static final String REMOTE_SERVER_URI = "urn:example:remote:server";
  private static final String UNREGISTERED_SERVER_URI = "urn:example:unregistered:server";
  private static final String REMOTE_NS_URI = "urn:example:remote:ns";

  private static final String DENIED_LOCAL_ID = "BrowseRemote.DeniedLocalTarget";

  private StrictNullDenyingAccessController accessController;

  private NodeId folderId;
  private NodeId localTargetId;
  private NodeId deniedLocalTargetId;
  private NodeId missingLocalTargetId;
  private NodeId unreadableNodeClassTargetId;
  private NodeId localByIndexFolderId;
  private NodeId localByServerUriFolderId;
  private NodeId mixedUnregisteredServerFolderId;
  private NodeId invalidNamespaceFolderId;

  private ExpandedNodeId remoteAbsoluteTarget;
  private ExpandedNodeId remoteRelativeNs0Target;
  private ExpandedNodeId remoteServerUriTarget;

  @Override
  protected TestServer createTestServer() throws Exception {
    return TestServer.create(
        configBuilder ->
            configBuilder.setAccessControllerFactory(
                srv -> {
                  accessController = new StrictNullDenyingAccessController(srv);
                  return accessController;
                }));
  }

  @Override
  protected void configureTestNamespace(TestNamespace namespace) {
    int remoteServerIndex = server.getServerTable().add(REMOTE_SERVER_URI).intValue();
    assertEquals(1, remoteServerIndex, "precondition: remote server registered at index 1");

    String localServerUri = server.getConfig().getApplicationUri();
    assertEquals(
        uint(0),
        server.getServerTable().getIndex(localServerUri),
        "precondition: local server URI is registered at index 0");

    folderId = newNodeId("BrowseRemote.Folder");
    localTargetId = newNodeId("BrowseRemote.LocalTarget");
    deniedLocalTargetId = newNodeId(DENIED_LOCAL_ID);
    missingLocalTargetId = newNodeId("BrowseRemote.MissingLocalTarget");
    unreadableNodeClassTargetId = newNodeId("BrowseRemote.UnreadableNodeClassTarget");
    localByIndexFolderId = newNodeId("BrowseRemote.LocalByIndexFolder");
    localByServerUriFolderId = newNodeId("BrowseRemote.LocalByServerUriFolder");
    mixedUnregisteredServerFolderId = newNodeId("BrowseRemote.MixedUnregisteredServerFolder");
    invalidNamespaceFolderId = newNodeId("BrowseRemote.InvalidNamespaceFolder");

    remoteAbsoluteTarget =
        new ExpandedNodeId(
            ServerReference.of(remoteServerIndex),
            NamespaceReference.of(REMOTE_NS_URI),
            "RemoteAbsolute");

    remoteRelativeNs0Target =
        new ExpandedNodeId(
            ServerReference.of(remoteServerIndex),
            NamespaceReference.of(ushort(0)),
            NodeIds.ObjectsFolder.getIdentifier());

    remoteServerUriTarget =
        new ExpandedNodeId(
            ServerReference.of(REMOTE_SERVER_URI),
            NamespaceReference.of(REMOTE_NS_URI),
            "RemoteByServerUri");

    namespace.configure(
        (context, nodeManager) -> {
          var folder =
              new UaFolderNode(
                  context,
                  folderId,
                  newQualifiedName("BrowseRemoteFolder"),
                  LocalizedText.english("BrowseRemoteFolder"));
          nodeManager.addNode(folder);

          var localTarget =
              new UaFolderNode(
                  context,
                  localTargetId,
                  newQualifiedName("LocalTarget"),
                  LocalizedText.english("LocalTarget"));
          nodeManager.addNode(localTarget);

          var deniedLocalTarget =
              new UaFolderNode(
                  context,
                  deniedLocalTargetId,
                  newQualifiedName("DeniedLocalTarget"),
                  LocalizedText.english("DeniedLocalTarget"));
          nodeManager.addNode(deniedLocalTarget);

          var unreadableNodeClassTarget =
              new UaFolderNode(
                  context,
                  unreadableNodeClassTargetId,
                  newQualifiedName("UnreadableNodeClassTarget"),
                  LocalizedText.english("UnreadableNodeClassTarget"));
          unreadableNodeClassTarget
              .getFilterChain()
              .addFirst(
                  new AttributeFilter() {
                    @Override
                    public Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
                        throws UaException {
                      if (attributeId == AttributeId.NodeClass) {
                        throw new UaException(StatusCodes.Bad_NotReadable);
                      }
                      return ctx.readAttribute(attributeId);
                    }
                  });
          nodeManager.addNode(unreadableNodeClassTarget);

          folder.addReference(
              new Reference(folderId, NodeIds.Organizes, localTargetId.expanded(), true));
          folder.addReference(
              new Reference(folderId, NodeIds.Organizes, deniedLocalTargetId.expanded(), true));
          folder.addReference(
              new Reference(folderId, NodeIds.Organizes, remoteAbsoluteTarget, true));
          folder.addReference(
              new Reference(folderId, NodeIds.Organizes, remoteRelativeNs0Target, true));
          folder.addReference(
              new Reference(folderId, NodeIds.Organizes, remoteServerUriTarget, true));
          folder.addReference(
              new Reference(folderId, NodeIds.Organizes, missingLocalTargetId.expanded(), true));
          folder.addReference(
              new Reference(
                  folderId, NodeIds.Organizes, unreadableNodeClassTargetId.expanded(), true));

          // Two parallel folders referencing the exact same allowed and denied local nodes:
          // one via serverIndex == 0, the other via this server's own applicationUri.
          var localByIndexFolder =
              new UaFolderNode(
                  context,
                  localByIndexFolderId,
                  newQualifiedName("LocalByIndexFolder"),
                  LocalizedText.english("LocalByIndexFolder"));
          nodeManager.addNode(localByIndexFolder);
          localByIndexFolder.addReference(
              new Reference(
                  localByIndexFolderId, NodeIds.Organizes, localTargetId.expanded(), true));
          localByIndexFolder.addReference(
              new Reference(
                  localByIndexFolderId, NodeIds.Organizes, deniedLocalTargetId.expanded(), true));

          var localByServerUriFolder =
              new UaFolderNode(
                  context,
                  localByServerUriFolderId,
                  newQualifiedName("LocalByServerUriFolder"),
                  LocalizedText.english("LocalByServerUriFolder"));
          nodeManager.addNode(localByServerUriFolder);
          localByServerUriFolder.addReference(
              new Reference(
                  localByServerUriFolderId,
                  NodeIds.Organizes,
                  new ExpandedNodeId(
                      ServerReference.of(localServerUri),
                      NamespaceReference.of(TestNamespace.NAMESPACE_URI),
                      localTargetId.getIdentifier()),
                  true));
          localByServerUriFolder.addReference(
              new Reference(
                  localByServerUriFolderId,
                  NodeIds.Organizes,
                  new ExpandedNodeId(
                      ServerReference.of(localServerUri),
                      NamespaceReference.of(deniedLocalTargetId.getNamespaceIndex()),
                      deniedLocalTargetId.getIdentifier()),
                  true));

          // Mixed folder combining valid references with an unregistered remote ServerUri and
          // an unregistered remote ServerIndex.
          var mixedUnregisteredServerFolder =
              new UaFolderNode(
                  context,
                  mixedUnregisteredServerFolderId,
                  newQualifiedName("MixedUnregisteredServerFolder"),
                  LocalizedText.english("MixedUnregisteredServerFolder"));
          nodeManager.addNode(mixedUnregisteredServerFolder);
          mixedUnregisteredServerFolder.addReference(
              new Reference(
                  mixedUnregisteredServerFolderId,
                  NodeIds.Organizes,
                  localTargetId.expanded(),
                  true));
          mixedUnregisteredServerFolder.addReference(
              new Reference(
                  mixedUnregisteredServerFolderId, NodeIds.Organizes, remoteAbsoluteTarget, true));
          mixedUnregisteredServerFolder.addReference(
              new Reference(
                  mixedUnregisteredServerFolderId,
                  NodeIds.Organizes,
                  new ExpandedNodeId(
                      ServerReference.of(UNREGISTERED_SERVER_URI),
                      NamespaceReference.of(REMOTE_NS_URI),
                      "UnregisteredServerUriTarget"),
                  true));
          mixedUnregisteredServerFolder.addReference(
              new Reference(
                  mixedUnregisteredServerFolderId,
                  NodeIds.Organizes,
                  new ExpandedNodeId(
                      ServerReference.of(99),
                      NamespaceReference.of(REMOTE_NS_URI),
                      "UnregisteredServerIndexTarget"),
                  true));

          // Mixed folder combining a valid local target with targets whose namespaces cannot be
          // validly represented per OPC 10000-4 §7.29.
          var invalidNamespaceFolder =
              new UaFolderNode(
                  context,
                  invalidNamespaceFolderId,
                  newQualifiedName("InvalidNamespaceFolder"),
                  LocalizedText.english("InvalidNamespaceFolder"));
          nodeManager.addNode(invalidNamespaceFolder);
          invalidNamespaceFolder.addReference(
              new Reference(
                  invalidNamespaceFolderId, NodeIds.Organizes, localTargetId.expanded(), true));
          invalidNamespaceFolder.addReference(
              new Reference(
                  invalidNamespaceFolderId,
                  NodeIds.Organizes,
                  new ExpandedNodeId(
                      ServerReference.of(remoteServerIndex),
                      NamespaceReference.of(ushort(99)),
                      "RemoteUnmappedIndex"),
                  true));
          invalidNamespaceFolder.addReference(
              new Reference(
                  invalidNamespaceFolderId,
                  NodeIds.Organizes,
                  ExpandedNodeId.of("urn:example:unregistered:ns", "LocalUnregisteredNs"),
                  true));
          invalidNamespaceFolder.addReference(
              new Reference(
                  invalidNamespaceFolderId,
                  NodeIds.Organizes,
                  ExpandedNodeId.of(ushort(42), "Pump"),
                  true));
        });
  }

  @BeforeEach
  void clearRecordedChecks() {
    accessController.checkedNodeIds.clear();
  }

  /**
   * OPC 10000-4 §5.9.2.2 Table 34 & §7.29, OPC 10000-3 §4.4.4: with nodeClassMask == 0, Browse
   * returns local targets, remote targets (normalized to absolute namespace URI and never sent to
   * AccessController as NodeId.NULL_VALUE), missing local targets, and unreadable-NodeClass local
   * targets with non-null fallback attributes that encode cleanly over OPC TCP.
   */
  @Test
  void zeroNodeClassMaskReturnsLocalRemoteMissingAndUnreadableTargets() throws Exception {
    BrowseResult result = browse(folderId, 0L);

    assertTrue(result.getStatusCode().isGood());
    List<ReferenceDescription> refs = Lists.ofNullable(result.getReferences());
    Map<ExpandedNodeId, ReferenceDescription> byTarget =
        refs.stream().collect(Collectors.toMap(ReferenceDescription::getNodeId, r -> r));

    ExpandedNodeId expectedNormalizedNs0Remote =
        new ExpandedNodeId(
            ServerReference.of(1),
            NamespaceReference.of(Namespaces.OPC_UA),
            NodeIds.ObjectsFolder.getIdentifier());

    // ServerUri resolves to serverIndex 1 on the wire per OPC 10000-6 binary ExpandedNodeId rules.
    ExpandedNodeId expectedWireRemoteByUri =
        new ExpandedNodeId(
            ServerReference.of(1), NamespaceReference.of(REMOTE_NS_URI), "RemoteByServerUri");

    assertEquals(
        Set.of(
            localTargetId.expanded(),
            remoteAbsoluteTarget,
            expectedNormalizedNs0Remote,
            expectedWireRemoteByUri,
            missingLocalTargetId.expanded(),
            unreadableNodeClassTargetId.expanded()),
        byTarget.keySet(),
        "all targets except DeniedLocalTarget should be returned when nodeClassMask == 0");

    ReferenceDescription localRd = byTarget.get(localTargetId.expanded());
    assertEquals(NodeClass.Object, localRd.getNodeClass());
    assertEquals(newQualifiedName("LocalTarget"), localRd.getBrowseName());
    assertEquals(LocalizedText.english("LocalTarget"), localRd.getDisplayName());

    ReferenceDescription remoteRd = byTarget.get(remoteAbsoluteTarget);
    assertEquals(NodeClass.Unspecified, remoteRd.getNodeClass());
    assertEquals(QualifiedName.NULL_VALUE, remoteRd.getBrowseName());
    // Milo does not expose a remote-metadata hook on AddressSpace/Reference, so remote targets
    // currently fall back to LocalizedText.NULL_VALUE (known limitation vs OPC 10000-4 §7.29
    // Table 168 footnote 1, which calls for a remote DisplayName when an aggregating server can
    // supply one).
    assertEquals(LocalizedText.NULL_VALUE, remoteRd.getDisplayName());
    assertEquals(ExpandedNodeId.NULL_VALUE, remoteRd.getTypeDefinition());

    ReferenceDescription normalizedRemoteRd = byTarget.get(expectedNormalizedNs0Remote);
    assertNotNull(normalizedRemoteRd);
    assertTrue(
        normalizedRemoteRd.getNodeId().isAbsolute(),
        "BrowseUtil.normalize must convert relative remote targets to absolute namespace URI");
    assertEquals(Namespaces.OPC_UA, normalizedRemoteRd.getNodeId().getNamespaceUri());

    ReferenceDescription missingLocalRd = byTarget.get(missingLocalTargetId.expanded());
    assertEquals(NodeClass.Unspecified, missingLocalRd.getNodeClass());
    assertEquals(QualifiedName.NULL_VALUE, missingLocalRd.getBrowseName());
    assertEquals(LocalizedText.NULL_VALUE, missingLocalRd.getDisplayName());
    assertEquals(ExpandedNodeId.NULL_VALUE, missingLocalRd.getTypeDefinition());

    ReferenceDescription unreadableRd = byTarget.get(unreadableNodeClassTargetId.expanded());
    assertEquals(NodeClass.Unspecified, unreadableRd.getNodeClass());
    assertEquals(newQualifiedName("UnreadableNodeClassTarget"), unreadableRd.getBrowseName());
    assertEquals(LocalizedText.english("UnreadableNodeClassTarget"), unreadableRd.getDisplayName());

    assertFalse(
        accessController.checkedNodeIds.contains(NodeId.NULL_VALUE),
        "remote targets must never be mapped to NodeId.NULL_VALUE for checkBrowseAccess");
    assertTrue(
        accessController.checkedNodeIds.contains(deniedLocalTargetId),
        "control: local targets are still checked by AccessController");
  }

  /**
   * OPC 10000-4 §5.9.2.2 Table 34: with a non-zero nodeClassMask (Object), matching local targets
   * and unknown-NodeClass remote targets are returned, while local targets with unknown NodeClass
   * (missing or unreadable) are excluded.
   */
  @Test
  void objectNodeClassMaskKeepsRemoteTargetsAndExcludesUnknownLocalTargets() throws Exception {
    BrowseResult result = browse(folderId, NodeClass.Object.getValue());

    assertTrue(result.getStatusCode().isGood());
    Set<ExpandedNodeId> targets =
        Lists.ofNullable(result.getReferences()).stream()
            .map(ReferenceDescription::getNodeId)
            .collect(Collectors.toSet());

    ExpandedNodeId expectedNormalizedNs0Remote =
        new ExpandedNodeId(
            ServerReference.of(1),
            NamespaceReference.of(Namespaces.OPC_UA),
            NodeIds.ObjectsFolder.getIdentifier());

    ExpandedNodeId expectedWireRemoteByUri =
        new ExpandedNodeId(
            ServerReference.of(1), NamespaceReference.of(REMOTE_NS_URI), "RemoteByServerUri");

    assertEquals(
        Set.of(
            localTargetId.expanded(),
            remoteAbsoluteTarget,
            expectedNormalizedNs0Remote,
            expectedWireRemoteByUri),
        targets);
  }

  /**
   * OPC 10000-4 §5.9.2.2 Table 34: even when nodeClassMask matches no local target (Variable),
   * remote targets with unknown NodeClass ignore nodeClassMask and are still returned.
   */
  @Test
  void variableNodeClassMaskStillReturnsRemoteTargetsOnly() throws Exception {
    BrowseResult result = browse(folderId, NodeClass.Variable.getValue());

    assertTrue(result.getStatusCode().isGood());
    Set<ExpandedNodeId> targets =
        Lists.ofNullable(result.getReferences()).stream()
            .map(ReferenceDescription::getNodeId)
            .collect(Collectors.toSet());

    ExpandedNodeId expectedNormalizedNs0Remote =
        new ExpandedNodeId(
            ServerReference.of(1),
            NamespaceReference.of(Namespaces.OPC_UA),
            NodeIds.ObjectsFolder.getIdentifier());

    ExpandedNodeId expectedWireRemoteByUri =
        new ExpandedNodeId(
            ServerReference.of(1), NamespaceReference.of(REMOTE_NS_URI), "RemoteByServerUri");

    assertEquals(
        Set.of(remoteAbsoluteTarget, expectedNormalizedNs0Remote, expectedWireRemoteByUri),
        targets);
  }

  /**
   * OPC 10000-4 §7.16: a local target addressed by this server's own applicationUri (ServerTable
   * index 0) must receive the exact same AccessController authorization, local attribute reads, and
   * nodeClassMask filtering as the same target addressed by serverIndex == 0.
   */
  @Test
  void localTargetAddressedByOwnServerUriMatchesServerIndexZeroAuthAndFiltering() throws Exception {
    for (long mask : new long[] {0L, NodeClass.Object.getValue(), NodeClass.Variable.getValue()}) {
      accessController.checkedNodeIds.clear();
      BrowseResult byIndex = browse(localByIndexFolderId, mask);
      assertTrue(byIndex.getStatusCode().isGood());

      accessController.checkedNodeIds.clear();
      BrowseResult byServerUri = browse(localByServerUriFolderId, mask);
      assertTrue(byServerUri.getStatusCode().isGood());

      List<ReferenceDescription> byIndexRefs = Lists.ofNullable(byIndex.getReferences());
      List<ReferenceDescription> byServerUriRefs = Lists.ofNullable(byServerUri.getReferences());

      assertEquals(
          byIndexRefs,
          byServerUriRefs,
          "own-ServerUri local targets must produce identical Browse results for mask=" + mask);

      if (mask == NodeClass.Variable.getValue()) {
        assertTrue(
            byServerUriRefs.isEmpty(),
            "own-ServerUri Object target must not bypass Variable nodeClassMask");
      } else {
        assertEquals(1, byServerUriRefs.size());
        assertEquals(localTargetId.expanded(), byServerUriRefs.get(0).getNodeId());
        assertEquals(NodeClass.Object, byServerUriRefs.get(0).getNodeClass());
        assertEquals(newQualifiedName("LocalTarget"), byServerUriRefs.get(0).getBrowseName());
        assertTrue(
            accessController.checkedNodeIds.contains(deniedLocalTargetId),
            "denied own-ServerUri local target must be checked by AccessController");
      }
    }
  }

  /**
   * An unregistered remote ServerUri would cause OpcUaBinaryEncoder to throw Bad_EncodingError and
   * abort the entire BrowseResponse. Unrepresentable server targets must be skipped while valid
   * sibling references are still returned over OPC TCP.
   */
  @Test
  void mixedBrowseSkipsUnregisteredRemoteServerTargetsWithoutAbortingResponse() throws Exception {
    BrowseResult result = browse(mixedUnregisteredServerFolderId, 0L);

    assertTrue(result.getStatusCode().isGood());
    Set<ExpandedNodeId> targets =
        Lists.ofNullable(result.getReferences()).stream()
            .map(ReferenceDescription::getNodeId)
            .collect(Collectors.toSet());

    assertEquals(
        Set.of(localTargetId.expanded(), remoteAbsoluteTarget),
        targets,
        "valid local and remote targets must survive unregistered remote server references");
  }

  /**
   * OPC 10000-4 §7.29: targets whose namespaces cannot be validly normalized or do not exist in
   * NamespaceArray (a remote target with an unmapped numeric namespace index, a local target with
   * an unregistered namespace URI, or a local target with an unregistered numeric namespace index
   * such as ns=42) must be skipped rather than emitted malformed or bypassing local access checks.
   */
  @Test
  void invalidNamespaceTargetsAreSkippedWithoutFailingBrowseOrBypassingAccessControl()
      throws Exception {

    BrowseResult result = browse(invalidNamespaceFolderId, 0L);

    assertTrue(result.getStatusCode().isGood());
    List<ReferenceDescription> refs = Lists.ofNullable(result.getReferences());
    assertEquals(1, refs.size());
    assertEquals(localTargetId.expanded(), refs.get(0).getNodeId());
    assertEquals(NodeClass.Object, refs.get(0).getNodeClass());
    assertFalse(accessController.checkedNodeIds.contains(NodeId.NULL_VALUE));
  }

  private BrowseResult browse(NodeId nodeId, long nodeClassMask) throws UaException {
    return client.browse(
        new BrowseDescription(
            nodeId,
            BrowseDirection.Forward,
            NodeIds.Organizes,
            true,
            uint(nodeClassMask),
            uint(BrowseResultMask.All.getValue())));
  }

  /**
   * Strict AccessController that denies {@link NodeId#NULL_VALUE} and {@link #DENIED_LOCAL_ID},
   * recording every NodeId passed to {@link #checkBrowseAccess}.
   */
  private static final class StrictNullDenyingAccessController extends DefaultAccessController {

    private final OpcUaServer server;
    private final List<NodeId> checkedNodeIds = new CopyOnWriteArrayList<>();

    StrictNullDenyingAccessController(OpcUaServer server) {
      super(server);
      this.server = server;
    }

    @Override
    public Map<NodeId, AccessResult> checkBrowseAccess(Session session, List<NodeId> nodeIds) {
      checkedNodeIds.addAll(nodeIds);

      var results = new HashMap<>(super.checkBrowseAccess(session, nodeIds));

      // Deny NodeId.NULL_VALUE to verify BrowseHelper skips checkBrowseAccess for non-local
      // targets rather than mapping them to NULL_VALUE.
      results.computeIfPresent(NodeId.NULL_VALUE, (id, r) -> AccessResult.DENIED_USER_ACCESS);

      UShort namespaceIndex = server.getNamespaceTable().getIndex(TestNamespace.NAMESPACE_URI);
      if (namespaceIndex != null) {
        NodeId deniedId = new NodeId(namespaceIndex, DENIED_LOCAL_ID);
        results.computeIfPresent(deniedId, (id, r) -> AccessResult.DENIED_USER_ACCESS);
      }

      return results;
    }
  }
}
