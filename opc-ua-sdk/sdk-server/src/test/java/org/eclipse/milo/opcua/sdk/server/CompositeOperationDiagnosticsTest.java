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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.AddNodesContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.AddReferencesContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.CallContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.DeleteNodesContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.DeleteReferencesContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.HistoryReadContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.HistoryUpdateContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.ReadContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.WriteContext;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.AddReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteRawModifiedDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRawModifiedDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Composites hand each child address space the parent context's {@link OperationDiagnostics}, so
 * every layer reports into the one collector of the request. A child given a fresh collector would
 * report into one the service never reads.
 */
class CompositeOperationDiagnosticsTest {

  private static final NodeId NODE_ID = new NodeId(1, "node");

  private final OpcUaServer server = mock(OpcUaServer.class);

  @ParameterizedTest(name = "{0}, {2}")
  @MethodSource("cases")
  void childContextSharesTheParentOperationDiagnostics(
      String compositeName,
      BiFunction<OpcUaServer, AddressSpaceFragment, AddressSpace> composite,
      String serviceName,
      ServiceCall serviceCall) {

    when(server.getNamespaceTable()).thenReturn(new NamespaceTable());

    var contexts = new ArrayList<ServiceOperationContext<?>>();
    AddressSpace addressSpace = composite.apply(server, recordingFragment(contexts));

    OperationDiagnostics<?> parent = serviceCall.call(addressSpace, server);

    assertEquals(1, contexts.size(), "control: the fragment received the operation");
    assertSame(parent, contexts.get(0).getOperationDiagnostics());
  }

  /** Calls one service on an address space and returns the diagnostics its context carried. */
  @FunctionalInterface
  interface ServiceCall {
    OperationDiagnostics<?> call(AddressSpace addressSpace, OpcUaServer server);
  }

  static Stream<Arguments> cases() {
    return composites()
        .flatMap(
            composite ->
                services()
                    .map(
                        service ->
                            Arguments.of(
                                composite.get()[0],
                                composite.get()[1],
                                service.get()[0],
                                service.get()[1])));
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
                return Optional.of(fragment);
              }
            };

    return Stream.of(
        Arguments.of("AddressSpaceComposite", composite),
        Arguments.of("SimpleAddressSpaceComposite", simpleComposite));
  }

  @SuppressWarnings("unchecked")
  static Stream<Arguments> services() {
    ServiceCall read =
        (addressSpace, server) -> {
          OperationDiagnostics<ReadValueId> diagnostics = mock(OperationDiagnostics.class);
          var context = new ReadContext(server, null, diagnostics, null, uint(0), null);
          var item =
              new ReadValueId(NODE_ID, AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE);

          addressSpace.read(context, 0.0, TimestampsToReturn.Both, List.of(item));
          return diagnostics;
        };

    ServiceCall write =
        (addressSpace, server) -> {
          OperationDiagnostics<WriteValue> diagnostics = mock(OperationDiagnostics.class);
          var context = new WriteContext(server, null, diagnostics, null, uint(0), null);
          var item =
              new WriteValue(NODE_ID, AttributeId.Value.uid(), null, new DataValue(new Variant(1)));

          addressSpace.write(context, List.of(item));
          return diagnostics;
        };

    ServiceCall historyRead =
        (addressSpace, server) -> {
          OperationDiagnostics<HistoryReadValueId> diagnostics = mock(OperationDiagnostics.class);
          var context = new HistoryReadContext(server, null, diagnostics, null, uint(0), null);
          var details =
              new ReadRawModifiedDetails(false, DateTime.MIN_VALUE, DateTime.now(), uint(0), true);
          var item = new HistoryReadValueId(NODE_ID, null, QualifiedName.NULL_VALUE, null);

          addressSpace.historyRead(context, details, TimestampsToReturn.Both, List.of(item));
          return diagnostics;
        };

    ServiceCall historyUpdate =
        (addressSpace, server) -> {
          HistoryUpdateOperationDiagnostics diagnostics =
              mock(HistoryUpdateOperationDiagnostics.class);
          var context = new HistoryUpdateContext(server, null, diagnostics, null, uint(0), null);
          var item =
              new DeleteRawModifiedDetails(NODE_ID, false, DateTime.MIN_VALUE, DateTime.now());

          addressSpace.historyUpdate(context, List.of(item));
          return diagnostics;
        };

    ServiceCall call =
        (addressSpace, server) -> {
          CallOperationDiagnostics diagnostics = mock(CallOperationDiagnostics.class);
          var context = new CallContext(server, null, diagnostics, null, uint(0), null);
          var item = new CallMethodRequest(NODE_ID, new NodeId(1, "method"), new Variant[0]);

          addressSpace.call(context, List.of(item));
          return diagnostics;
        };

    ServiceCall addNodes =
        (addressSpace, server) -> {
          OperationDiagnostics<AddNodesItem> diagnostics = mock(OperationDiagnostics.class);
          var context = new AddNodesContext(server, null, diagnostics, null, uint(0), null);
          var item =
              new AddNodesItem(
                  ExpandedNodeId.NULL_VALUE,
                  NodeIds.HasComponent,
                  NODE_ID.expanded(),
                  new QualifiedName(1, "added"),
                  NodeClass.Object,
                  null,
                  NodeIds.BaseObjectType.expanded());

          addressSpace.addNodes(context, List.of(item));
          return diagnostics;
        };

    ServiceCall deleteNodes =
        (addressSpace, server) -> {
          OperationDiagnostics<DeleteNodesItem> diagnostics = mock(OperationDiagnostics.class);
          var context = new DeleteNodesContext(server, null, diagnostics, null, uint(0), null);

          addressSpace.deleteNodes(context, List.of(new DeleteNodesItem(NODE_ID, true)));
          return diagnostics;
        };

    ServiceCall addReferences =
        (addressSpace, server) -> {
          OperationDiagnostics<AddReferencesItem> diagnostics = mock(OperationDiagnostics.class);
          var context = new AddReferencesContext(server, null, diagnostics, null, uint(0), null);
          var item =
              new AddReferencesItem(
                  NODE_ID, NodeIds.Organizes, true, null, NODE_ID.expanded(), NodeClass.Object);

          addressSpace.addReferences(context, List.of(item));
          return diagnostics;
        };

    ServiceCall deleteReferences =
        (addressSpace, server) -> {
          OperationDiagnostics<DeleteReferencesItem> diagnostics = mock(OperationDiagnostics.class);
          var context = new DeleteReferencesContext(server, null, diagnostics, null, uint(0), null);
          var item =
              new DeleteReferencesItem(NODE_ID, NodeIds.Organizes, true, NODE_ID.expanded(), true);

          addressSpace.deleteReferences(context, List.of(item));
          return diagnostics;
        };

    return Stream.of(
        Arguments.of("Read", read),
        Arguments.of("Write", write),
        Arguments.of("HistoryRead", historyRead),
        Arguments.of("HistoryUpdate", historyUpdate),
        Arguments.of("Call", call),
        Arguments.of("AddNodes", addNodes),
        Arguments.of("DeleteNodes", deleteNodes),
        Arguments.of("AddReferences", addReferences),
        Arguments.of("DeleteReferences", deleteReferences));
  }

  /** A fragment that serves every node, records each context it receives, and returns nulls. */
  private static AddressSpaceFragment recordingFragment(List<ServiceOperationContext<?>> contexts) {

    return mock(
        AddressSpaceFragment.class,
        invocation -> {
          if (invocation.getMethod().getName().equals("getFilter")) {
            return SimpleAddressSpaceFilter.create(nodeId -> true);
          }

          Object[] arguments = invocation.getArguments();

          if (arguments.length > 0 && arguments[0] instanceof ServiceOperationContext<?> context) {
            contexts.add(context);

            List<?> group = (List<?>) arguments[arguments.length - 1];
            return Collections.nCopies(group.size(), null);
          }

          return null;
        });
  }
}
