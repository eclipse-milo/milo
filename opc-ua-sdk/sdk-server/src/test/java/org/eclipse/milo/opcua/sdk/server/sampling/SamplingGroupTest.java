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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.junit.jupiter.api.Test;

public class SamplingGroupTest {

  @Test
  public void testGetSamplingInterval() {
    SamplingGroup group = new SamplingGroup(List.of());
    assertEquals(0.0, group.getSamplingInterval());

    SampledItem item = createSampledItem(true);
    SamplingGroup group2 = new SamplingGroup(List.of(item));
    assertEquals(0.0, group2.getSamplingInterval()); // Temporary group has interval 0
  }

  @Test
  public void testConstructorWithItems() {
    SampledItem item1 = createSampledItem(true);
    SampledItem item2 = createSampledItem(true);

    SamplingGroup group = new SamplingGroup(List.of(item1, item2));

    assertFalse(group.isEmpty());
    assertEquals(2, group.getActiveItems().size());
  }

  @Test
  public void testAddItemsMarksDirtyAndUpdatesEmpty() {
    // Note: addItems/removeItems are intended for scheduled groups.
    // For temporary groups (no scheduler), activeItems is computed once at construction.
    SamplingGroup group = new SamplingGroup(List.of());

    SampledItem item1 = createSampledItem(true);

    group.addItems(List.of(item1));

    // Group is no longer empty
    assertFalse(group.isEmpty());
    // Dirty flag is set
    assertTrue(group.isDirty());
  }

  @Test
  public void testRemoveItemsFromGroup() {
    SampledItem item1 = createSampledItem(true);
    SampledItem item2 = createSampledItem(true);

    SamplingGroup group = new SamplingGroup(List.of(item1, item2));

    group.removeItems(List.of(item1));

    // activeItems is computed at construction, removal marks dirty
    // but doesn't immediately recompute for temporary groups
    assertTrue(group.isDirty());
  }

  @Test
  public void testIsEmpty() {
    SamplingGroup group = new SamplingGroup(List.of());
    assertTrue(group.isEmpty());

    SampledItem item = createSampledItem(true);
    group.addItems(List.of(item));
    assertFalse(group.isEmpty());

    group.removeItems(List.of(item));
    assertTrue(group.isEmpty());
  }

  @Test
  public void testIsDirtyInitially() {
    SamplingGroup group = new SamplingGroup(List.of());
    assertTrue(group.isDirty());
  }

  @Test
  public void testMarkDirty() {
    SamplingGroup group = new SamplingGroup(List.of());

    // Initial dirty state
    assertTrue(group.isDirty());

    // Mark dirty explicitly
    group.markDirty();
    assertTrue(group.isDirty());
  }

  @Test
  public void testGetActiveItemsFiltersDisabled() {
    SampledItem activeItem = createSampledItem(true);
    SampledItem disabledItem = createSampledItem(false);

    SamplingGroup group = new SamplingGroup(List.of(activeItem, disabledItem));

    Collection<SampledItem> activeItems = group.getActiveItems();

    assertEquals(1, activeItems.size());
    assertTrue(activeItems.contains(activeItem));
    assertFalse(activeItems.contains(disabledItem));
  }

  @Test
  public void testAddItemsMarksDirty() {
    SamplingGroup group = new SamplingGroup(List.of());

    // Items added marks dirty
    SampledItem item = createSampledItem(true);
    group.addItems(List.of(item));
    assertTrue(group.isDirty());
  }

  @Test
  public void testRemoveItemsMarksDirty() {
    SampledItem item = createSampledItem(true);
    SamplingGroup group = new SamplingGroup(List.of(item));

    group.removeItems(List.of(item));
    assertTrue(group.isDirty());
  }

  private SampledItem createSampledItem(boolean active) {
    DataItem mockDataItem = mock(DataItem.class);
    ReadValueId readValueId =
        new ReadValueId(
            new NodeId(1, "test"),
            UInteger.valueOf(AttributeId.Value.id()),
            null,
            QualifiedName.NULL_VALUE);
    when(mockDataItem.getReadValueId()).thenReturn(readValueId);
    when(mockDataItem.isSamplingEnabled()).thenReturn(active);
    when(mockDataItem.getSamplingInterval()).thenReturn(100.0);

    return new SampledItem(mockDataItem, 100.0);
  }
}
