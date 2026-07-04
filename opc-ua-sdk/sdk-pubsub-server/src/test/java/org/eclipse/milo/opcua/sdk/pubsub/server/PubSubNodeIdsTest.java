/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.junit.jupiter.api.Test;

/**
 * The deterministic NodeId scheme: {@link PubSubNodeIds} must reproduce the exact string ids the
 * info model fragment has always minted (golden strings), and the prefix-strip must round-trip name
 * paths — including names containing {@code '/'} — while rejecting non-fragment ids.
 */
class PubSubNodeIdsTest {

  private static final UShort NS = ushort(1);

  @Test
  void componentNodeIdMatchesTheFragmentScheme() {
    assertEquals(new NodeId(NS, "PubSub/conn"), PubSubNodeIds.componentNodeId(NS, "conn"));
    assertEquals(
        new NodeId(NS, "PubSub/conn/group"), PubSubNodeIds.componentNodeId(NS, "conn/group"));
    assertEquals(
        new NodeId(NS, "PubSub/conn/group/writer"),
        PubSubNodeIds.componentNodeId(NS, "conn/group/writer"));
  }

  @Test
  void publishedDataSetNodeIdMatchesTheFragmentScheme() {
    assertEquals(
        new NodeId(NS, "PubSub/PublishedDataSets/ds"),
        PubSubNodeIds.publishedDataSetNodeId(NS, "ds"));
  }

  @Test
  void childNodeIdAppendsASegmentInTheParentNamespace() {
    NodeId component = PubSubNodeIds.componentNodeId(NS, "conn");
    NodeId status = PubSubNodeIds.childNodeId(component, "Status");
    NodeId enable = PubSubNodeIds.childNodeId(status, "Enable");
    NodeId disable = PubSubNodeIds.childNodeId(status, "Disable");

    assertEquals(new NodeId(NS, "PubSub/conn/Status"), status);
    assertEquals(new NodeId(NS, "PubSub/conn/Status/Enable"), enable);
    assertEquals(new NodeId(NS, "PubSub/conn/Status/Disable"), disable);
  }

  @Test
  void namePathOfRoundTripsComponentIds() {
    assertEquals(
        "conn/group/writer",
        PubSubNodeIds.namePathOf(PubSubNodeIds.componentNodeId(NS, "conn/group/writer"), NS));

    assertEquals(
        "PublishedDataSets/ds",
        PubSubNodeIds.namePathOf(PubSubNodeIds.publishedDataSetNodeId(NS, "ds"), NS));

    // names may contain '/': the joined path round-trips verbatim (it is intentionally
    // not segment-splittable — consumers carry name components, they never parse paths)
    assertEquals(
        "we/ird/name",
        PubSubNodeIds.namePathOf(PubSubNodeIds.componentNodeId(NS, "we/ird/name"), NS));
  }

  @Test
  void namePathOfRejectsNonFragmentIds() {
    // wrong namespace index
    assertNull(PubSubNodeIds.namePathOf(new NodeId(ushort(2), "PubSub/conn"), NS));

    // non-String identifier
    assertNull(PubSubNodeIds.namePathOf(new NodeId(NS, uint(42)), NS));

    // no "PubSub/" prefix
    assertNull(PubSubNodeIds.namePathOf(new NodeId(NS, "NotPubSub/conn"), NS));
    assertNull(PubSubNodeIds.namePathOf(new NodeId(NS, "PubSubX/conn"), NS));

    // the bare prefix carries no name path
    assertNull(PubSubNodeIds.namePathOf(new NodeId(NS, "PubSub"), NS));
  }
}
