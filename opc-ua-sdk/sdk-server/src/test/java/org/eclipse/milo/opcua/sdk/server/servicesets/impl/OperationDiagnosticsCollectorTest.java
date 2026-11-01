/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.servicesets.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.WriteContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceComposite;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceFragment;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.SimpleAddressSpaceFilter;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.Diagnostic;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.junit.jupiter.api.Test;

class OperationDiagnosticsCollectorTest {

  private static final Diagnostic BLOCKED = Diagnostic.text("blocked");

  // DataValue(Variant) stamps the current time, so share one value to keep equal operations equal.
  private static final DataValue VALUE = new DataValue(new Variant(1));

  // ReadValueId and WriteValue compare by value. Keying by equality gave two equal operations in
  // one request a single slot, so a diagnostic for one was returned for both.
  @Test
  void equalOperationsInOneRequestKeepSeparateDiagnostics() {
    WriteValue first = writeValue("a");
    WriteValue second = writeValue("a");
    assertEquals(first, second, "control: the two operations are equal");

    var collector = new OperationDiagnosticsCollector<>(List.of(first, second));

    collector.report(second, BLOCKED);

    assertNull(collector.get(0));
    assertEquals(BLOCKED, collector.get(1));
  }

  // An address space must report the instance it received. An equal copy is not part of the
  // request and cannot be attributed to an operation.
  @Test
  void reportForAnObjectOutsideTheRequestIsIgnored() {
    WriteValue operation = writeValue("a");
    var collector = new OperationDiagnosticsCollector<>(List.of(operation));

    collector.report(writeValue("a"), BLOCKED);

    assertNull(collector.get(0));
  }

  @Test
  void laterReportReplacesAnEarlierOne() {
    WriteValue operation = writeValue("a");
    var collector = new OperationDiagnosticsCollector<>(List.of(operation));

    collector.report(operation, Diagnostic.text("first"));
    collector.report(operation, BLOCKED);

    assertEquals(BLOCKED, collector.get(0));
  }

  // Asking for diagnostics must not turn an address space's reporting bug into a failed operation;
  // without diagnostics the same call is a no-op.
  @Test
  @SuppressWarnings("DataFlowIssue")
  void nullReportsAreIgnored() {
    WriteValue operation = writeValue("a");
    var collector = new OperationDiagnosticsCollector<>(List.of(operation));

    collector.report(operation, null);
    collector.reportNested(operation, null);

    assertNull(collector.get(0));
    assertNull(collector.getNested(0));
  }

  // The same instance at two positions cannot be told apart by the address space, so a report
  // applies to both.
  @Test
  void sameInstanceAtTwoPositionsGetsTheDiagnosticAtBoth() {
    WriteValue operation = writeValue("a");
    var collector = new OperationDiagnosticsCollector<>(List.of(operation, operation));

    collector.report(operation, BLOCKED);

    assertEquals(BLOCKED, collector.get(0));
    assertEquals(BLOCKED, collector.get(1));
  }

  // The Milo 2.0 design runs address-space groups concurrently, so reports for distinct
  // operations can arrive from several threads at once.
  @Test
  void concurrentReportsForDistinctOperationsAreAllKept() throws Exception {
    int count = 1_000;
    var operations = new ArrayList<WriteValue>(count);
    for (int i = 0; i < count; i++) {
      operations.add(writeValue("node" + i));
    }

    var collector = new OperationDiagnosticsCollector<>(operations);
    var start = new CountDownLatch(1);
    ExecutorService executor = Executors.newFixedThreadPool(8);

    try {
      var futures = new ArrayList<Future<?>>();
      for (WriteValue operation : operations) {
        futures.add(
            executor.submit(
                () -> {
                  start.await();
                  collector.report(operation, Diagnostic.text(operation.getNodeId().toString()));
                  return null;
                }));
      }

      start.countDown();
      for (Future<?> future : futures) {
        future.get(10, TimeUnit.SECONDS);
      }
    } finally {
      executor.shutdownNow();
    }

    for (int i = 0; i < count; i++) {
      assertEquals(
          Diagnostic.text(operations.get(i).getNodeId().toString()), collector.get(i), "op " + i);
    }
  }

  // A composite regroups operations by fragment and hands each fragment a sublist. Because the
  // collector keys by identity and every child context shares it, a fragment's report lands at the
  // operation's position in the original request.
  @Test
  void reportsFromFragmentsBehindACompositeLandAtTheOriginalPositions() {
    OpcUaServer server = mock(OpcUaServer.class);

    var composite = new AddressSpaceComposite(server);
    composite.register(blockingFragment("left"));
    composite.register(blockingFragment("right"));

    List<WriteValue> request =
        List.of(writeValue("left"), writeValue("right"), writeValue("left"), writeValue("right"));
    var collector = new OperationDiagnosticsCollector<>(request);

    composite.write(new WriteContext(server, null, collector, null, null, null), request);

    for (int i = 0; i < request.size(); i++) {
      assertEquals(
          Diagnostic.text("blocked " + request.get(i).getNodeId().getIdentifier()),
          collector.get(i),
          "operation " + i);
    }
  }

  /** A fragment for {@code id} that rejects every write with a diagnostic naming the node. */
  private static AddressSpaceFragment blockingFragment(String id) {
    AddressSpaceFragment fragment = mock(AddressSpaceFragment.class);

    when(fragment.getFilter())
        .thenReturn(SimpleAddressSpaceFilter.create(nodeId -> nodeId.getIdentifier().equals(id)));

    when(fragment.write(any(), anyList()))
        .thenAnswer(
            invocation -> {
              WriteContext context = invocation.getArgument(0);
              List<WriteValue> group = invocation.getArgument(1);

              for (WriteValue writeValue : group) {
                context
                    .getOperationDiagnostics()
                    .report(writeValue, Diagnostic.text("blocked " + id));
              }

              return group.stream().map(writeValue -> StatusCode.BAD).toList();
            });

    return fragment;
  }

  private static WriteValue writeValue(String id) {
    return new WriteValue(new NodeId(1, id), AttributeId.Value.uid(), null, VALUE);
  }
}
