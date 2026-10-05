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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.AddNodesContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.ReadContext;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * A composite falls back to an empty fragment only for NodeIds that no registered fragment serves.
 *
 * <p>The empty fragment is a {@link ManagedAddressSpace}, and building one creates a NodeFactory
 * that reads {@link OpcUaServer#getObjectTypeManager()}. These tests use that call on a mock server
 * to observe whether an empty fragment was built.
 */
class AddressSpaceCompositeRoutingTest {

  private static final NodeId KNOWN_NODE_ID = new NodeId(1, "known");
  private static final NodeId UNKNOWN_NODE_ID = new NodeId(2, "unknown");

  private final OpcUaServer server = mock(OpcUaServer.class);

  // Every operation routed through a composite looks up its fragment, so building a discarded
  // empty fragment on each lookup made per-node cost grow with every request (issue 2098).
  @ParameterizedTest(name = "{0}")
  @MethodSource("composites")
  void readOfAMatchedNodeDoesNotBuildAnEmptyFragment(
      String name, BiFunction<OpcUaServer, AddressSpaceFragment, AddressSpace> factory) {

    AddressSpace composite = factory.apply(server, knownNodeFragment());

    List<DataValue> values = read(composite, KNOWN_NODE_ID);

    assertEquals(new Variant(42), values.get(0).value());
    verify(server, never()).getObjectTypeManager();
  }

  // Control for the test above: the empty fragment still answers unmatched NodeIds, and building
  // it is observable through the server mock.
  @ParameterizedTest(name = "{0}")
  @MethodSource("composites")
  void readOfAnUnmatchedNodeIsAnsweredByAnEmptyFragment(
      String name, BiFunction<OpcUaServer, AddressSpaceFragment, AddressSpace> factory) {

    AddressSpace composite = factory.apply(server, knownNodeFragment());

    List<DataValue> values = read(composite, UNKNOWN_NODE_ID);

    assertEquals(StatusCodes.Bad_NodeIdUnknown, values.get(0).statusCode().value());
    verify(server).getObjectTypeManager();
  }

  // AddNodes routes on the requested NodeId, or on the parent NodeId when none is requested, and
  // SimpleAddressSpaceComposite has a separate empty-fragment fallback for each route.
  @ParameterizedTest(name = "{0}, {2}")
  @MethodSource("addNodesRoutes")
  void addNodesOfAMatchedNodeDoesNotBuildAnEmptyFragment(
      String name,
      BiFunction<OpcUaServer, AddressSpaceFragment, AddressSpace> factory,
      String route,
      AddNodesItem item) {

    when(server.getNamespaceTable()).thenReturn(new NamespaceTable());
    AddressSpace composite = factory.apply(server, knownNodeFragment());

    List<AddNodesResult> results =
        composite.addNodes(new AddNodesContext(server, null), List.of(item));

    assertEquals(StatusCode.GOOD, results.get(0).getStatusCode());
    verify(server, never()).getObjectTypeManager();
  }

  static Stream<Arguments> addNodesRoutes() {
    AddNodesItem byRequestedNodeId =
        addNodesItem(ExpandedNodeId.NULL_VALUE, KNOWN_NODE_ID.expanded());
    AddNodesItem byParentNodeId = addNodesItem(KNOWN_NODE_ID.expanded(), ExpandedNodeId.NULL_VALUE);

    return composites()
        .flatMap(
            composite -> {
              Object name = composite.get()[0];
              Object factory = composite.get()[1];

              return Stream.of(
                  Arguments.of(name, factory, "requested NodeId", byRequestedNodeId),
                  Arguments.of(name, factory, "parent NodeId", byParentNodeId));
            });
  }

  static Stream<Arguments> composites() {
    BiFunction<OpcUaServer, AddressSpaceFragment, AddressSpace> composite =
        (server, fragment) -> {
          var addressSpaceComposite = new AddressSpaceComposite(server);
          addressSpaceComposite.register(fragment);
          return addressSpaceComposite;
        };

    BiFunction<OpcUaServer, AddressSpaceFragment, AddressSpace> simpleComposite =
        (server, fragment) ->
            new SimpleAddressSpaceComposite(server) {
              @Override
              protected List<AddressSpaceFragment> getAddressSpaces() {
                return List.of(fragment);
              }

              @Override
              protected Optional<AddressSpaceFragment> getAddressSpace(NodeId nodeId) {
                return KNOWN_NODE_ID.equals(nodeId) ? Optional.of(fragment) : Optional.empty();
              }
            };

    return Stream.of(
        Arguments.of("AddressSpaceComposite", composite),
        Arguments.of("SimpleAddressSpaceComposite", simpleComposite));
  }

  /**
   * A fragment that serves {@link #KNOWN_NODE_ID}, reads 42 from it, and accepts nodes added to it.
   */
  private static AddressSpaceFragment knownNodeFragment() {
    AddressSpaceFragment fragment = mock(AddressSpaceFragment.class);
    when(fragment.getFilter()).thenReturn(SimpleAddressSpaceFilter.create(KNOWN_NODE_ID::equals));
    when(fragment.read(any(), any(), any(), anyList()))
        .thenReturn(List.of(new DataValue(new Variant(42))));
    when(fragment.addNodes(any(), anyList()))
        .thenReturn(List.of(new AddNodesResult(StatusCode.GOOD, KNOWN_NODE_ID)));
    return fragment;
  }

  private static AddNodesItem addNodesItem(
      ExpandedNodeId parentNodeId, ExpandedNodeId requestedNewNodeId) {

    return new AddNodesItem(
        parentNodeId,
        NodeIds.HasComponent,
        requestedNewNodeId,
        new QualifiedName(1, "added"),
        NodeClass.Object,
        null,
        NodeIds.BaseObjectType.expanded());
  }

  private List<DataValue> read(AddressSpace composite, NodeId nodeId) {
    var readValueId =
        new ReadValueId(nodeId, AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE);

    return composite.read(
        new ReadContext(server, null), 0.0, TimestampsToReturn.Both, List.of(readValueId));
  }
}
