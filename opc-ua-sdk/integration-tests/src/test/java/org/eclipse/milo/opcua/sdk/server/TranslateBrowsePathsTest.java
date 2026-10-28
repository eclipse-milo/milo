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

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.BrowseContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.ReadContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.ReferenceResult;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePath;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePathResult;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePathTarget;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePath;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePathElement;
import org.eclipse.milo.opcua.stack.core.types.structured.TranslateBrowsePathsToNodeIdsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ViewDescription;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Part 4 §5.9.4: TranslateBrowsePathsToNodeIds matches each RelativePath element against the
 * BrowseNames of the Nodes it reaches.
 *
 * <p>The Server reads those BrowseNames through the address space Read path. These tests use their
 * own namespace so they control the order of each Node's references and how the namespace answers a
 * Read: one Node's Read throws, and another Node's BrowseName as Read differs from the one it
 * stores.
 */
class TranslateBrowsePathsTest extends AbstractClientServerTest {

  private static final String NAMESPACE_URI = "urn:eclipse:milo:test:translate-browse-paths";

  /** A child whose Read throws, ahead of the matching child. */
  private static final String THROWING_READ = "ThrowingRead";

  private static final String BROKEN_CHILD = THROWING_READ + ".Broken";

  /** A child whose stored BrowseName differs from the BrowseName the namespace serves. */
  private static final String RENAMED_CHILD = "Routing.Renamed";

  private static final String STORED_NAME = "Stored";
  private static final String SERVED_NAME = "Served";

  private BrowsePathNamespace namespace;

  @Override
  protected void configureTestNamespace(TestNamespace ignored) {
    namespace = new BrowsePathNamespace(server);
    namespace.startup();
  }

  @AfterAll
  void stopNamespace() {
    if (namespace != null) {
      namespace.shutdown();
    }
  }

  Stream<Arguments> unreadableChildren() {
    return Stream.of(
        arguments(named("reference to a Node that does not exist", "Dangling")),
        arguments(named("reference to a Node on another Server", "Remote")),
        arguments(named("child whose Read throws", THROWING_READ)));
  }

  /**
   * A child whose BrowseName can't be read just doesn't match. It must not stop the path from
   * resolving through a sibling, and it must not fail the whole request.
   */
  @ParameterizedTest
  @MethodSource("unreadableChildren")
  void unreadableChildAheadOfMatchDoesNotStopPathFromResolving(String startingNode)
      throws UaException {

    BrowsePathResult result = translate(startingNode, "Device", "Item");

    assertEquals(StatusCode.GOOD, result.getStatusCode());
    assertEquals(List.of(expandedNodeId(startingNode + ".Device.Item")), targetIds(result));
  }

  // Both siblings have a child named "Leaf", so a BrowseName paired with the wrong sibling would
  // resolve to Alpha's Leaf instead of Beta's.
  @Test
  void intermediateElementFollowsTheSiblingWhoseBrowseNameMatches() throws UaException {
    BrowsePathResult result = translate("Siblings", "Beta", "Leaf");

    assertEquals(StatusCode.GOOD, result.getStatusCode());
    assertEquals(List.of(expandedNodeId("Siblings.Beta.Leaf")), targetIds(result));
  }

  /** Part 4 §5.9.4.1: when several targets share a BrowseName, the Server returns all of them. */
  @Test
  void lastElementReturnsEveryTargetWithMatchingBrowseName() throws UaException {
    BrowsePathResult result = translate("Twins", "Twin");

    assertEquals(StatusCode.GOOD, result.getStatusCode());
    assertEquals(
        Set.of(expandedNodeId("Twins.First"), expandedNodeId("Twins.Second")),
        Set.copyOf(targetIds(result)));
  }

  /**
   * Part 4 §5.9.4.1: "If a Node has multiple targets with the same BrowseName, the Server shall
   * return a list of NodeIds." The first "Twin" has no "Leaf", so following only the first match at
   * an intermediate element would end in Bad_NoMatch.
   */
  @Test
  void pathResolvesThroughALaterIntermediateMatch() throws UaException {
    BrowsePathResult result = translate("DeadEnd", "Twin", "Leaf");

    assertEquals(StatusCode.GOOD, result.getStatusCode());
    assertEquals(List.of(expandedNodeId("DeadEnd.Second.Leaf")), targetIds(result));
  }

  // Both "Twin" Nodes have a "Leaf"; following only the first would lose the second Leaf.
  @Test
  void pathReturnsTargetsReachedThroughEveryIntermediateMatch() throws UaException {
    BrowsePathResult result = translate("Both", "Twin", "Leaf");

    assertEquals(StatusCode.GOOD, result.getStatusCode());
    assertEquals(
        Set.of(expandedNodeId("Both.First.Leaf"), expandedNodeId("Both.Second.Leaf")),
        Set.copyOf(targetIds(result)));
  }

  // Both "Twin" Nodes lead to the same "Leaf". The path must keep going from it, and "End" is
  // still one target even though both branches reach it.
  @Test
  void targetReachedThroughSeveralIntermediateMatchesIsReturnedOnce() throws UaException {
    BrowsePathResult result = translate("Converging", "Twin", "Leaf", "End");

    assertEquals(StatusCode.GOOD, result.getStatusCode());
    assertEquals(List.of(expandedNodeId("Converging.Leaf.End")), targetIds(result));
  }

  // Following every match must still report Bad_NoMatch when none of them reaches the end.
  @Test
  void pathThatNoIntermediateMatchCompletesReturnsBadNoMatch() throws UaException {
    BrowsePathResult result = translate("DeadEnd", "Twin", "Missing");

    assertEquals(new StatusCode(StatusCodes.Bad_NoMatch), result.getStatusCode());
    assertEquals(List.of(), targetIds(result));
  }

  /**
   * The BrowseName a Read returns decides the match, not the BrowseName the Node stores. Read and
   * Browse report the served name, so TranslateBrowsePaths must agree with them.
   */
  @Test
  void browseNameServedByReadDecidesTheMatch() throws UaException {
    BrowsePathResult served = translate("Routing", SERVED_NAME);
    BrowsePathResult stored = translate("Routing", STORED_NAME);

    assertEquals(StatusCode.GOOD, served.getStatusCode());
    assertEquals(List.of(expandedNodeId(RENAMED_CHILD)), targetIds(served));

    assertEquals(
        new StatusCode(StatusCodes.Bad_NoMatch),
        stored.getStatusCode(),
        "the stored BrowseName is never served, so it must not match");
  }

  /**
   * Paths in one request reuse what an earlier path browsed and read, but only for the same Node
   * and the same reference filters. Each path must still get its own result.
   */
  @Test
  void pathsInOneRequestThatShareNodesEachGetTheirOwnResult() throws UaException {
    List<BrowsePathResult> results =
        translateAll(
            hierarchicalPath("Siblings", "Alpha", "Leaf"),
            hierarchicalPath("Siblings", "Beta", "Leaf"),
            hierarchicalPath("Siblings", "Alpha", "Leaf"),
            hierarchicalPath("Siblings", "Alpha"),
            browsePath("Siblings", NodeIds.HasComponent, false, "Alpha", "Leaf"));

    assertEquals(List.of(expandedNodeId("Siblings.Alpha.Leaf")), targetIds(results.get(0)));
    assertEquals(List.of(expandedNodeId("Siblings.Beta.Leaf")), targetIds(results.get(1)));
    assertEquals(List.of(expandedNodeId("Siblings.Alpha.Leaf")), targetIds(results.get(2)));
    assertEquals(List.of(expandedNodeId("Siblings.Alpha")), targetIds(results.get(3)));

    assertEquals(
        new StatusCode(StatusCodes.Bad_NoMatch),
        results.get(4).getStatusCode(),
        "Alpha is the target of an Organizes reference, not a HasComponent reference");
  }

  /**
   * Paths in one request that share a prefix browse each Node once, and read the BrowseNames of a
   * Node's targets in one Read. A later request browses and reads again, so it never sees what an
   * earlier request found.
   */
  @Test
  void sharedPrefixIsBrowsedAndReadOncePerRequest() throws UaException {
    BrowsePath[] paths = {
      hierarchicalPath("Siblings", "Alpha", "Leaf"), hierarchicalPath("Siblings", "Beta", "Leaf")
    };

    List<String> expectedBrowses = List.of("Siblings", "Siblings.Alpha", "Siblings.Beta");

    List<List<String>> expectedReads =
        List.of(
            List.of("Siblings.Alpha", "Siblings.Beta"),
            List.of("Siblings.Alpha.Leaf"),
            List.of("Siblings.Beta.Leaf"));

    namespace.clearCalls();
    translateAll(paths);

    assertEquals(expectedBrowses, namespace.browsedNodes());
    assertEquals(expectedReads, namespace.readBatches());

    namespace.clearCalls();
    translateAll(paths);

    assertEquals(expectedBrowses, namespace.browsedNodes(), "a later request must browse again");
    assertEquals(expectedReads, namespace.readBatches(), "a later request must read again");
  }

  private BrowsePathResult translate(String startingNode, String... targetNames)
      throws UaException {

    return translateAll(hierarchicalPath(startingNode, targetNames)).get(0);
  }

  private List<BrowsePathResult> translateAll(BrowsePath... browsePaths) throws UaException {
    TranslateBrowsePathsToNodeIdsResponse response =
        client.translateBrowsePaths(List.of(browsePaths));

    return List.of(requireNonNull(response.getResults()));
  }

  private BrowsePath hierarchicalPath(String startingNode, String... targetNames) {
    return browsePath(startingNode, NodeIds.HierarchicalReferences, true, targetNames);
  }

  private BrowsePath browsePath(
      String startingNode, NodeId referenceTypeId, boolean includeSubtypes, String... targetNames) {

    var elements = new RelativePathElement[targetNames.length];

    for (int i = 0; i < targetNames.length; i++) {
      elements[i] =
          new RelativePathElement(
              referenceTypeId, false, includeSubtypes, qualifiedName(targetNames[i]));
    }

    return new BrowsePath(nodeId(startingNode), new RelativePath(elements));
  }

  private static List<ExpandedNodeId> targetIds(BrowsePathResult result) {
    BrowsePathTarget[] targets = requireNonNull(result.getTargets());

    return Stream.of(targets).map(BrowsePathTarget::getTargetId).toList();
  }

  private NodeId nodeId(String id) {
    return new NodeId(namespace.getNamespaceIndex(), id);
  }

  private ExpandedNodeId expandedNodeId(String id) {
    return nodeId(id).expanded();
  }

  private QualifiedName qualifiedName(String name) {
    return new QualifiedName(namespace.getNamespaceIndex(), name);
  }

  /**
   * Each starting Node is a Folder whose children are linked by Organizes references, added in the
   * order shown:
   *
   * <pre>
   * Dangling      -> (missing Node), Device -> Item
   * Remote        -> (Node on Server 1), Device -> Item
   * ThrowingRead  -> Broken, Device -> Item
   * Siblings      -> Alpha -> Leaf, Beta -> Leaf
   * Twins         -> First "Twin", Second "Twin"
   * DeadEnd       -> First "Twin", Second "Twin" -> Leaf
   * Both          -> First "Twin" -> Leaf, Second "Twin" -> Leaf
   * Converging    -> First "Twin" -> Leaf -> End, Second "Twin" -> (the same Leaf)
   * Routing       -> Renamed (stores "Stored", serves "Served")
   * </pre>
   *
   * <p>It records the Nodes passed to each Browse and Read it handles, by NodeId identifier.
   */
  private static final class BrowsePathNamespace extends ManagedNamespaceWithLifecycle {

    private final List<String> browsedNodes = new CopyOnWriteArrayList<>();
    private final List<List<String>> readBatches = new CopyOnWriteArrayList<>();

    BrowsePathNamespace(OpcUaServer server) {
      super(server, NAMESPACE_URI);

      getLifecycleManager().addStartupTask(this::addNodes);
    }

    private void addNodes() {
      UaFolderNode dangling = addFolder("Dangling", "Dangling");
      dangling.addReference(
          new Reference(
              dangling.getNodeId(), NodeIds.Organizes, newNodeId("Missing").expanded(), true));
      addDeviceWithItem(dangling);

      UaFolderNode remote = addFolder("Remote", "Remote");
      remote.addReference(
          new Reference(
              remote.getNodeId(),
              NodeIds.Organizes,
              ExpandedNodeId.parse("svr=1;ns=2;s=Elsewhere"),
              true));
      addDeviceWithItem(remote);

      UaFolderNode throwingRead = addFolder(THROWING_READ, THROWING_READ);
      throwingRead.addOrganizes(addFolder(BROKEN_CHILD, "Broken"));
      addDeviceWithItem(throwingRead);

      UaFolderNode siblings = addFolder("Siblings", "Siblings");
      for (String name : List.of("Alpha", "Beta")) {
        UaFolderNode sibling = addFolder("Siblings." + name, name);
        siblings.addOrganizes(sibling);
        sibling.addOrganizes(addFolder("Siblings." + name + ".Leaf", "Leaf"));
      }

      UaFolderNode twins = addFolder("Twins", "Twins");
      twins.addOrganizes(addFolder("Twins.First", "Twin"));
      twins.addOrganizes(addFolder("Twins.Second", "Twin"));

      UaFolderNode deadEnd = addFolder("DeadEnd", "DeadEnd");
      deadEnd.addOrganizes(addFolder("DeadEnd.First", "Twin"));
      UaFolderNode deadEndSecond = addFolder("DeadEnd.Second", "Twin");
      deadEnd.addOrganizes(deadEndSecond);
      deadEndSecond.addOrganizes(addFolder("DeadEnd.Second.Leaf", "Leaf"));

      UaFolderNode both = addFolder("Both", "Both");
      for (String id : List.of("Both.First", "Both.Second")) {
        UaFolderNode twin = addFolder(id, "Twin");
        both.addOrganizes(twin);
        twin.addOrganizes(addFolder(id + ".Leaf", "Leaf"));
      }

      UaFolderNode converging = addFolder("Converging", "Converging");
      UaFolderNode convergingLeaf = addFolder("Converging.Leaf", "Leaf");
      convergingLeaf.addOrganizes(addFolder("Converging.Leaf.End", "End"));
      for (String id : List.of("Converging.First", "Converging.Second")) {
        UaFolderNode twin = addFolder(id, "Twin");
        converging.addOrganizes(twin);
        twin.addOrganizes(convergingLeaf);
      }

      UaFolderNode routing = addFolder("Routing", "Routing");
      routing.addOrganizes(addFolder(RENAMED_CHILD, STORED_NAME));
    }

    private void addDeviceWithItem(UaFolderNode parent) {
      String deviceId = parent.getNodeId().getIdentifier() + ".Device";

      UaFolderNode device = addFolder(deviceId, "Device");
      parent.addOrganizes(device);
      device.addOrganizes(addFolder(deviceId + ".Item", "Item"));
    }

    private UaFolderNode addFolder(String id, String browseName) {
      var folder =
          new UaFolderNode(
              getNodeContext(),
              newNodeId(id),
              newQualifiedName(browseName),
              LocalizedText.english(browseName));

      getNodeManager().addNode(folder);

      return folder;
    }

    /** The Nodes browsed since the last {@link #clearCalls()}, in call order. */
    List<String> browsedNodes() {
      return List.copyOf(browsedNodes);
    }

    /** The Nodes passed to each Read since the last {@link #clearCalls()}, in call order. */
    List<List<String>> readBatches() {
      return List.copyOf(readBatches);
    }

    void clearCalls() {
      browsedNodes.clear();
      readBatches.clear();
    }

    @Override
    public List<ReferenceResult> browse(
        BrowseContext context, ViewDescription view, List<NodeId> nodeIds) {

      nodeIds.forEach(nodeId -> browsedNodes.add(nodeId.getIdentifier().toString()));

      return super.browse(context, view, nodeIds);
    }

    @Override
    public List<DataValue> read(
        ReadContext context,
        Double maxAge,
        TimestampsToReturn timestamps,
        List<ReadValueId> readValueIds) {

      readBatches.add(
          readValueIds.stream().map(id -> id.getNodeId().getIdentifier().toString()).toList());

      NodeId brokenId = newNodeId(BROKEN_CHILD);
      NodeId renamedId = newNodeId(RENAMED_CHILD);

      for (ReadValueId readValueId : readValueIds) {
        if (readValueId.getNodeId().equals(brokenId)) {
          throw new IllegalStateException("read failed: " + brokenId);
        }
      }

      var values = new ArrayList<>(super.read(context, maxAge, timestamps, readValueIds));

      for (int i = 0; i < readValueIds.size(); i++) {
        ReadValueId readValueId = readValueIds.get(i);

        boolean renamedBrowseName =
            readValueId.getNodeId().equals(renamedId)
                && readValueId.getAttributeId().equals(AttributeId.BrowseName.uid());

        if (renamedBrowseName) {
          values.set(i, new DataValue(new Variant(newQualifiedName(SERVED_NAME))));
        }
      }

      return values;
    }
  }
}
