/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.sampling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.junit.jupiter.api.Test;

public class SampledItemTest {

  @Test
  public void testBucketExactMultiple() {
    // Exact multiples should stay the same
    assertEquals(100.0, SampledItem.bucket(100.0, 25.0));
    assertEquals(50.0, SampledItem.bucket(50.0, 25.0));
    assertEquals(25.0, SampledItem.bucket(25.0, 25.0));
    assertEquals(0.0, SampledItem.bucket(0.0, 25.0));
  }

  @Test
  public void testBucketRoundsUp() {
    // Non-multiples should round UP to next bucket
    assertEquals(125.0, SampledItem.bucket(101.0, 25.0));
    assertEquals(125.0, SampledItem.bucket(124.0, 25.0));
    assertEquals(50.0, SampledItem.bucket(26.0, 25.0));
    assertEquals(25.0, SampledItem.bucket(1.0, 25.0));
  }

  @Test
  public void testBucketZeroBucketSize() {
    // Zero bucket size should return original
    assertEquals(100.0, SampledItem.bucket(100.0, 0.0));
    assertEquals(101.0, SampledItem.bucket(101.0, 0.0));
  }

  @Test
  public void testBucketNegativeBucketSize() {
    // Negative bucket size should return original
    assertEquals(100.0, SampledItem.bucket(100.0, -25.0));
  }

  @Test
  public void testGetSamplingInterval() {
    DataItem mockDataItem = createMockDataItem();
    SampledItem item = new SampledItem(mockDataItem, 125.0);

    assertEquals(125.0, item.getSamplingInterval());
  }

  @Test
  public void testGetDataItem() {
    DataItem mockDataItem = createMockDataItem();
    SampledItem item = new SampledItem(mockDataItem, 100.0);

    assertEquals(mockDataItem, item.getDataItem());
  }

  @Test
  public void testGetNodeId() {
    DataItem mockDataItem = createMockDataItem();
    SampledItem item = new SampledItem(mockDataItem, 100.0);

    assertEquals(new NodeId(1, "test"), item.getNodeId());
  }

  @Test
  public void testGetAttributeId() {
    DataItem mockDataItem = createMockDataItem();
    SampledItem item = new SampledItem(mockDataItem, 100.0);

    assertEquals(AttributeId.Value, item.getAttributeId());
  }

  @Test
  public void testGetIndexRangeNull() {
    DataItem mockDataItem = createMockDataItem();
    SampledItem item = new SampledItem(mockDataItem, 100.0);

    assertNull(item.getIndexRange());
  }

  @Test
  public void testGetIndexRangeEmpty() {
    DataItem mockDataItem = createMockDataItem("");
    SampledItem item = new SampledItem(mockDataItem, 100.0);

    assertNull(item.getIndexRange());
  }

  @Test
  public void testGetIndexRangeSet() {
    DataItem mockDataItem = createMockDataItem("0:5");
    SampledItem item = new SampledItem(mockDataItem, 100.0);

    assertEquals("0:5", item.getIndexRange());
  }

  @Test
  public void testIsActiveWhenSamplingEnabled() {
    DataItem mockDataItem = createMockDataItem();
    when(mockDataItem.isSamplingEnabled()).thenReturn(true);

    SampledItem item = new SampledItem(mockDataItem, 100.0);

    assertTrue(item.isActive());
  }

  @Test
  public void testIsActiveWhenSamplingDisabled() {
    DataItem mockDataItem = createMockDataItem();
    when(mockDataItem.isSamplingEnabled()).thenReturn(false);

    SampledItem item = new SampledItem(mockDataItem, 100.0);

    assertFalse(item.isActive());
  }

  @Test
  public void testGetRequestedSamplingInterval() {
    DataItem mockDataItem = createMockDataItem();
    when(mockDataItem.getSamplingInterval()).thenReturn(123.0);

    SampledItem item = new SampledItem(mockDataItem, 125.0);

    assertEquals(123.0, item.getRequestedSamplingInterval());
  }

  private DataItem createMockDataItem() {
    return createMockDataItem(null);
  }

  private DataItem createMockDataItem(String indexRange) {
    DataItem mockDataItem = mock(DataItem.class);
    ReadValueId readValueId =
        new ReadValueId(
            new NodeId(1, "test"),
            UInteger.valueOf(AttributeId.Value.id()),
            indexRange,
            QualifiedName.NULL_VALUE);
    when(mockDataItem.getReadValueId()).thenReturn(readValueId);
    when(mockDataItem.isSamplingEnabled()).thenReturn(true);
    when(mockDataItem.getSamplingInterval()).thenReturn(100.0);
    return mockDataItem;
  }
}
