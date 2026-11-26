/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes.filters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
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
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.junit.jupiter.api.Test;

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
    assertEquals("B", value.value().value());
  }

  @Test
  public void testAddLast() {
    AttributeFilterChain chain = new AttributeFilterChain();

    chain.addLast(
        AttributeFilters.getValue(ctx -> (DataValue) ctx.getAttribute(AttributeId.Value)),
        AttributeFilters.getValue(ctx -> new DataValue(new Variant("Last"))));

    DataValue value = (DataValue) chain.getAttribute(null, AttributeId.Value);
    assertEquals("Last", value.value().value());
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
    assertEquals("foo", value.value().value());
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

    assertEquals("foo", node.getValue().value().value());

    assertTrue(observed.get());
  }

  @Test
  public void testRemoveFromEmptyChain() {
    AttributeFilterChain chain = new AttributeFilterChain();
    AttributeFilter filter = AttributeFilters.getValue(ctx -> new DataValue(new Variant("A")));

    // Should not throw an exception when removing from empty chain
    chain.remove(filter);
    assertEquals(0, chain.getFilters().size());
  }

  @Test
  public void testRemoveSingleFilter() {
    AttributeFilterChain chain = new AttributeFilterChain();
    AttributeFilter filter = AttributeFilters.getValue(ctx -> new DataValue(new Variant("A")));

    chain.addLast(filter);
    assertEquals(1, chain.getFilters().size());

    chain.remove(filter);
    assertEquals(0, chain.getFilters().size());
  }

  @Test
  public void testRemoveFirstFilter() {
    AttributeFilterChain chain = new AttributeFilterChain();
    AttributeFilter filter1 = AttributeFilters.getValue(ctx -> new DataValue(new Variant("A")));
    AttributeFilter filter2 = AttributeFilters.getValue(ctx -> new DataValue(new Variant("B")));
    AttributeFilter filter3 = AttributeFilters.getValue(ctx -> new DataValue(new Variant("C")));

    chain.addLast(filter1, filter2, filter3);
    assertEquals(3, chain.getFilters().size());

    chain.remove(filter1);
    assertEquals(2, chain.getFilters().size());

    // Verify the chain still works correctly after removal
    DataValue value = (DataValue) chain.getAttribute(null, AttributeId.Value);
    assertEquals("B", value.value().value());
  }

  @Test
  public void testRemoveLastFilter() {
    AttributeFilterChain chain = new AttributeFilterChain();
    AttributeFilter filter1 = AttributeFilters.getValue(ctx -> new DataValue(new Variant("A")));
    AttributeFilter filter2 = AttributeFilters.getValue(ctx -> new DataValue(new Variant("B")));
    AttributeFilter filter3 = AttributeFilters.getValue(ctx -> new DataValue(new Variant("C")));

    chain.addLast(filter1, filter2, filter3);
    assertEquals(3, chain.getFilters().size());

    chain.remove(filter3);
    assertEquals(2, chain.getFilters().size());

    // Verify the chain still works correctly after removal
    DataValue value = (DataValue) chain.getAttribute(null, AttributeId.Value);
    assertEquals("A", value.value().value());
  }

  @Test
  public void testRemoveMiddleFilter() {
    AttributeFilterChain chain = new AttributeFilterChain();
    AttributeFilter filter1 = AttributeFilters.getValue(ctx -> new DataValue(new Variant("A")));
    AttributeFilter filter2 = AttributeFilters.getValue(ctx -> new DataValue(new Variant("B")));
    AttributeFilter filter3 = AttributeFilters.getValue(ctx -> new DataValue(new Variant("C")));

    chain.addLast(filter1, filter2, filter3);
    assertEquals(3, chain.getFilters().size());

    chain.remove(filter2);
    assertEquals(2, chain.getFilters().size());

    // Verify the chain still works correctly after removal
    DataValue value = (DataValue) chain.getAttribute(null, AttributeId.Value);
    assertEquals("A", value.value().value());
  }

  @Test
  public void testRemoveNonExistentFilter() {
    AttributeFilterChain chain = new AttributeFilterChain();
    AttributeFilter filter1 = AttributeFilters.getValue(ctx -> new DataValue(new Variant("A")));
    AttributeFilter filter2 = AttributeFilters.getValue(ctx -> new DataValue(new Variant("B")));
    AttributeFilter nonExistentFilter =
        AttributeFilters.getValue(ctx -> new DataValue(new Variant("C")));

    chain.addLast(filter1, filter2);
    assertEquals(2, chain.getFilters().size());

    // Should not throw an exception when removing non-existent filter
    chain.remove(nonExistentFilter);
    assertEquals(2, chain.getFilters().size());
  }

  @Test
  public void testRemoveAndAddAgain() {
    AttributeFilterChain chain = new AttributeFilterChain();
    AttributeFilter filter1 = AttributeFilters.getValue(ctx -> new DataValue(new Variant("A")));
    AttributeFilter filter2 = AttributeFilters.getValue(ctx -> new DataValue(new Variant("B")));

    chain.addLast(filter1, filter2);
    assertEquals(2, chain.getFilters().size());

    chain.remove(filter1);
    assertEquals(1, chain.getFilters().size());

    chain.addFirst(filter1);
    assertEquals(2, chain.getFilters().size());

    // Verify the chain still works correctly
    DataValue value = (DataValue) chain.getAttribute(null, AttributeId.Value);
    assertEquals("A", value.value().value());
  }

  @Test
  public void testRemoveNull() {
    AttributeFilterChain chain = new AttributeFilterChain();

    // Should throw NullPointerException for consistency with addFirst/addLast
    assertThrows(NullPointerException.class, () -> chain.remove(null));
  }

  // AttributeFilters.getValue, setValue, and getSetValue must delegate non-intercepted attributes
  // via ctx.readAttribute/ctx.writeAttribute rather than falling back to getAttribute/setAttribute,
  // so downstream readAttribute and writeAttribute hooks remain reachable.
  @Test
  public void testAttributeFiltersForwardReadAndWriteAttribute() throws UaException {
    var downstreamReadCalled = new AtomicBoolean(false);
    var downstreamWriteCalled = new AtomicBoolean(false);
    var downstream =
        new AttributeFilter() {
          @Override
          public Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
              throws UaException {
            downstreamReadCalled.set(true);
            if (attributeId == AttributeId.Description) {
              return LocalizedText.english("from-readAttribute");
            }
            if (attributeId == AttributeId.Value) {
              return new DataValue(new Variant("downstream-value"));
            }
            return ctx.readAttribute(attributeId);
          }

          @Override
          public void writeAttribute(
              AttributeFilterContext ctx, AttributeId attributeId, Object value)
              throws UaException {
            downstreamWriteCalled.set(true);
            throw new UaException(StatusCodes.Bad_OutOfRange);
          }
        };

    // 1. AttributeFilters.getValue: intercepts Value read, delegates non-Value readAttribute and
    // ALL writeAttribute calls (both Value and non-Value) downstream.
    var getValueChain = new AttributeFilterChain();
    getValueChain.addLast(
        AttributeFilters.getValue(ctx -> new DataValue(new Variant("supplied-value"))), downstream);

    assertEquals(
        "supplied-value",
        ((DataValue) getValueChain.readAttribute(null, null, AttributeId.Value)).value().value());
    assertEquals(
        LocalizedText.english("from-readAttribute"),
        getValueChain.readAttribute(null, null, AttributeId.Description));
    assertTrue(downstreamReadCalled.get());

    assertEquals(
        StatusCodes.Bad_OutOfRange,
        assertThrows(
                UaException.class,
                () ->
                    getValueChain.writeAttribute(
                        null, null, AttributeId.Description, LocalizedText.english("x")))
            .getStatusCode()
            .getValue());
    assertTrue(downstreamWriteCalled.get());

    downstreamWriteCalled.set(false);
    assertEquals(
        StatusCodes.Bad_OutOfRange,
        assertThrows(
                UaException.class,
                () ->
                    getValueChain.writeAttribute(
                        null, null, AttributeId.Value, new DataValue(new Variant("x"))))
            .getStatusCode()
            .getValue());
    assertTrue(downstreamWriteCalled.get());

    // 2. AttributeFilters.setValue: intercepts Value write, delegates ALL readAttribute calls
    // (both Value and non-Value) and non-Value writeAttribute calls downstream.
    var setValueTarget = new AtomicReference<DataValue>();
    var setValueChain = new AttributeFilterChain();
    setValueChain.addLast(
        AttributeFilters.setValue((ctx, dv) -> setValueTarget.set(dv)), downstream);

    downstreamReadCalled.set(false);
    assertEquals(
        "downstream-value",
        ((DataValue) setValueChain.readAttribute(null, null, AttributeId.Value)).value().value());
    assertEquals(
        LocalizedText.english("from-readAttribute"),
        setValueChain.readAttribute(null, null, AttributeId.Description));
    assertTrue(downstreamReadCalled.get());

    setValueChain.writeAttribute(
        null, null, AttributeId.Value, new DataValue(new Variant("written")));
    assertEquals("written", setValueTarget.get().value().value());

    downstreamWriteCalled.set(false);
    assertEquals(
        StatusCodes.Bad_OutOfRange,
        assertThrows(
                UaException.class,
                () ->
                    setValueChain.writeAttribute(
                        null, null, AttributeId.Description, LocalizedText.english("x")))
            .getStatusCode()
            .getValue());
    assertTrue(downstreamWriteCalled.get());

    // 3. AttributeFilters.getSetValue: intercepts Value read/write, delegates non-Value
    // readAttribute and writeAttribute downstream.
    var getSetTarget = new AtomicReference<DataValue>();
    var getSetChain = new AttributeFilterChain();
    getSetChain.addLast(
        AttributeFilters.getSetValue(
            ctx -> new DataValue(new Variant("getSet-value")), (ctx, dv) -> getSetTarget.set(dv)),
        downstream);

    assertEquals(
        "getSet-value",
        ((DataValue) getSetChain.readAttribute(null, null, AttributeId.Value)).value().value());
    assertEquals(
        LocalizedText.english("from-readAttribute"),
        getSetChain.readAttribute(null, null, AttributeId.Description));

    getSetChain.writeAttribute(
        null, null, AttributeId.Value, new DataValue(new Variant("getSet-write")));
    assertEquals("getSet-write", getSetTarget.get().value().value());

    downstreamWriteCalled.set(false);
    assertEquals(
        StatusCodes.Bad_OutOfRange,
        assertThrows(
                UaException.class,
                () ->
                    getSetChain.writeAttribute(
                        null, null, AttributeId.Description, LocalizedText.english("x")))
            .getStatusCode()
            .getValue());
    assertTrue(downstreamWriteCalled.get());
  }
}
