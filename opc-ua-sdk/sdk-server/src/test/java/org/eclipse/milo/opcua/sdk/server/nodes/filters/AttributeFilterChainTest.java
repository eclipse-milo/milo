/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes.filters;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.testng.annotations.Test;

public class AttributeFilterChainTest {

  private final UaNodeManager nodeManager = new UaNodeManager();

  private final UaNodeContext context =
      new UaNodeContext() {
        @Override
        public OpcUaServer getServer() {
          return null;
        }

        @Override
        public NodeManager<UaNode> getNodeManager() {
          return nodeManager;
        }

        @Override
        public NamespaceTable getNamespaceTable() {
          return new NamespaceTable();
        }
      };

  @Test
  public void testAddFirst() {
    AttributeFilterChain chain = new AttributeFilterChain();

    chain.addFirst(
        AttributeFilters.getValue(ctx -> new DataValue(new Variant("A"))),
        AttributeFilters.getValue(ctx -> new DataValue(new Variant("B"))));

    DataValue value = (DataValue) chain.getAttribute(null, AttributeId.Value);
    assertEquals(value.getValue().getValue(), "B");
  }

  @Test
  public void testAddLast() {
    AttributeFilterChain chain = new AttributeFilterChain();

    chain.addLast(
        AttributeFilters.getValue(ctx -> (DataValue) ctx.getAttribute(AttributeId.Value)),
        AttributeFilters.getValue(ctx -> new DataValue(new Variant("Last"))));

    DataValue value = (DataValue) chain.getAttribute(null, AttributeId.Value);
    assertEquals(value.getValue().getValue(), "Last");
  }

  @Test
  public void testEmptyChain() {
    UaVariableNode node =
        new UaVariableNode.UaVariableNodeBuilder(context)
            .setNodeId(NodeId.NULL_VALUE)
            .setAccessLevel(AccessLevel.READ_WRITE)
            .setBrowseName(QualifiedName.NULL_VALUE)
            .setDisplayName(LocalizedText.NULL_VALUE)
            .setDataType(NodeIds.String)
            .setTypeDefinition(NodeIds.BaseDataVariableType)
            .build();

    node.setValue(new DataValue(new Variant("foo")));

    DataValue value = (DataValue) node.getFilterChain().getAttribute(node, AttributeId.Value);
    assertEquals(value.getValue().getValue(), "foo");
  }

  @Test
  public void testObservable() {
    final AtomicBoolean observed = new AtomicBoolean(false);

    UaVariableNode node =
        new UaVariableNode.UaVariableNodeBuilder(context)
            .setNodeId(NodeId.NULL_VALUE)
            .setAccessLevel(AccessLevel.READ_WRITE)
            .setBrowseName(QualifiedName.NULL_VALUE)
            .setDisplayName(LocalizedText.NULL_VALUE)
            .setDataType(NodeIds.String)
            .setTypeDefinition(NodeIds.BaseDataVariableType)
            .build();

    node.addAttributeObserver(
        (node1, attributeId, value) -> {
          observed.set(true);
        });

    node.getFilterChain()
        .addLast(
            AttributeFilters.getValue(
                ctx -> {
                  ctx.setObservable(true);

                  return new DataValue(new Variant("foo"));
                }));

    assertEquals(node.getValue().getValue().getValue(), "foo");

    assertTrue(observed.get());
  }
}
