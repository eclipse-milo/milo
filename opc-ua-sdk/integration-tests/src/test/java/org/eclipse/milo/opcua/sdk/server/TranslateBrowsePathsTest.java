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
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.ReadContext;
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

  private BrowsePathResult translate(String startingNode, String... targetNames)
      throws UaException {

    var elements = new RelativePathElement[targetNames.length];

    for (int i = 0; i < targetNames.length; i++) {
      elements[i] =
          new RelativePathElement(
              NodeIds.HierarchicalReferences, false, true, qualifiedName(targetNames[i]));
    }

    var browsePath = new BrowsePath(nodeId(startingNode), new RelativePath(elements));

    TranslateBrowsePathsToNodeIdsResponse response =
        client.translateBrowsePaths(List.of(browsePath));

    return requireNonNull(response.getResults())[0];
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
   * Routing       -> Renamed (stores "Stored", serves "Served")
   * </pre>
   */
  private static final class BrowsePathNamespace extends ManagedNamespaceWithLifecycle {

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

    @Override
    public List<DataValue> read(
        ReadContext context,
        Double maxAge,
        TimestampsToReturn timestamps,
        List<ReadValueId> readValueIds) {

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
