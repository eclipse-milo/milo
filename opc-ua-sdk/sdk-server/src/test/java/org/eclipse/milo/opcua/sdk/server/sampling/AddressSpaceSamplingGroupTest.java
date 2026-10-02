/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.sampling;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.server.AddressSpace;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.ReadContext;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredDataItem;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** How {@link AddressSpaceSamplingGroup} turns a sample into AddressSpace reads. */
class AddressSpaceSamplingGroupTest {

  private final OpcUaServer server = mock(OpcUaServer.class);
  private final AddressSpace addressSpace = mock(AddressSpace.class);
  private final Session session = mock(Session.class);
  private final Session otherSession = mock(Session.class);

  private int maxNodesPerRead = 10_000;
  private AddressSpaceSamplingGroup group;

  @BeforeEach
  void setUp() {
    OpcUaServerConfig config = mock(OpcUaServerConfig.class);
    when(config.getLimits())
        .thenReturn(
            new OpcUaServerConfigLimits() {
              @Override
              public UInteger getMaxNodesPerRead() {
                return uint(maxNodesPerRead);
              }
            });
    when(server.getConfig()).thenReturn(config);

    group = new AddressSpaceSamplingGroup(server, addressSpace, 100);
  }

  // One Read per Session, under that Session, so the AddressSpace can apply per-Session filters;
  // each value carries only the timestamps its item asked for.
  @Test
  void readsEachSessionOnceAndDeliversValuesWithTheRequestedTimestamps() throws Exception {
    MonitoredDataItem a = SamplingTestItems.item(server, session, "a");
    MonitoredDataItem b = SamplingTestItems.item(server, session, "b");
    MonitoredDataItem c = SamplingTestItems.item(server, otherSession, "c");
    b.modify(
        TimestampsToReturn.Source,
        uint(1),
        100.0,
        MonitoredDataItem.DEFAULT_FILTER,
        uint(10),
        true);

    var sourceTime = new DateTime();
    var serverTime = new DateTime();
    when(addressSpace.read(any(), eq(0d), eq(TimestampsToReturn.Both), anyList()))
        .thenAnswer(
            invocation -> {
              List<?> ids = invocation.getArgument(3);
              return ids.stream()
                  .map(
                      id ->
                          new DataValue(
                              new Variant(ids.indexOf(id)),
                              StatusCode.GOOD,
                              sourceTime,
                              serverTime))
                  .toList();
            });

    CompletionStage<Void> stage = group.sample(List.of(a, b, c));
    stage.toCompletableFuture().get(5, TimeUnit.SECONDS);

    verify(addressSpace)
        .read(
            argThat(
                (ReadContext ctx) -> ctx != null && ctx.getSession().equals(Optional.of(session))),
            eq(0d),
            eq(TimestampsToReturn.Both),
            eq(List.of(a.getReadValueId(), b.getReadValueId())));
    verify(addressSpace)
        .read(
            argThat(
                (ReadContext ctx) ->
                    ctx != null && ctx.getSession().equals(Optional.of(otherSession))),
            eq(0d),
            eq(TimestampsToReturn.Both),
            eq(List.of(c.getReadValueId())));
    assertEquals(2, group.getRequestCount());

    DataValue delivered = SamplingTestItems.drain(b).get(0);
    assertEquals(1, delivered.value().value());
    assertNotNull(delivered.sourceTime(), "Source timestamps were asked for");
    assertNull(delivered.serverTime(), "Server timestamps were not");
  }

  // A Read bigger than MaxNodesPerRead is what the server tells clients it refuses; the sampler
  // keeps under it too.
  @Test
  void splitsReadsAtMaxNodesPerRead() throws Exception {
    maxNodesPerRead = 2;
    MonitoredDataItem a = SamplingTestItems.item(server, session, "a");
    MonitoredDataItem b = SamplingTestItems.item(server, session, "b");
    MonitoredDataItem c = SamplingTestItems.item(server, session, "c");
    when(addressSpace.read(any(), eq(0d), eq(TimestampsToReturn.Both), anyList()))
        .thenAnswer(
            invocation ->
                ((List<?>) invocation.getArgument(3))
                    .stream().map(id -> new DataValue(new Variant(1))).toList());

    group.sample(List.of(a, b, c)).toCompletableFuture().get(5, TimeUnit.SECONDS);

    verify(addressSpace)
        .read(any(), eq(0d), any(), eq(List.of(a.getReadValueId(), b.getReadValueId())));
    verify(addressSpace).read(any(), eq(0d), any(), eq(List.of(c.getReadValueId())));
    assertEquals(2, group.getRequestCount());
  }

  // One Session's Read failing is not a reason to leave another Session's items unread.
  @Test
  void aFailingReadForOneSessionDoesNotStopTheOthers() throws Exception {
    MonitoredDataItem a = SamplingTestItems.item(server, session, "a");
    MonitoredDataItem c = SamplingTestItems.item(server, otherSession, "c");
    when(addressSpace.read(
            argThat(
                (ReadContext ctx) -> ctx != null && ctx.getSession().equals(Optional.of(session))),
            eq(0d),
            any(),
            anyList()))
        .thenThrow(new IllegalStateException("device unreachable"));
    when(addressSpace.read(
            argThat(
                (ReadContext ctx) ->
                    ctx != null && ctx.getSession().equals(Optional.of(otherSession))),
            eq(0d),
            any(),
            anyList()))
        .thenReturn(List.of(new DataValue(new Variant(7))));

    CompletionStage<Void> stage = group.sample(List.of(a, c));
    stage.toCompletableFuture().get(5, TimeUnit.SECONDS);

    verify(addressSpace, times(2)).read(any(), eq(0d), any(), anyList());
    assertTrue(SamplingTestItems.drain(a).isEmpty());
    assertEquals(7, SamplingTestItems.drain(c).get(0).value().value());
  }
}
