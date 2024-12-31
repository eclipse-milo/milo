/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import org.testng.annotations.Test;

public class AttributeIdTest {

  @Test
  public void testFrom() {
    for (AttributeId attributeId : AttributeId.values()) {
      int id = attributeId.id();

      assertEquals(attributeId, AttributeId.from(id).orElseThrow());
    }

    assertFalse(AttributeId.from(-1).isPresent());
    assertFalse(AttributeId.from(0).isPresent());
    assertFalse(AttributeId.from(28).isPresent());
  }

  @Test
  public void testIsValid() {
    for (AttributeId attributeId : AttributeId.values()) {
      int id = attributeId.id();

      assertTrue(AttributeId.isValid(id));
    }

    assertFalse(AttributeId.isValid(-1));
    assertFalse(AttributeId.isValid(0));
    assertFalse(AttributeId.isValid(28));
  }

  @Test
  public void testDataTypeAttributes() {
    var attributes = new ArrayList<>(AttributeId.DATA_TYPE_ATTRIBUTES);

    assertEquals(AttributeId.NodeId, attributes.get(0));
    assertEquals(AttributeId.NodeClass, attributes.get(1));
    assertEquals(AttributeId.BrowseName, attributes.get(2));
    assertEquals(AttributeId.DisplayName, attributes.get(3));
    assertEquals(AttributeId.Description, attributes.get(4));
    assertEquals(AttributeId.WriteMask, attributes.get(5));
    assertEquals(AttributeId.UserWriteMask, attributes.get(6));
    assertEquals(AttributeId.RolePermissions, attributes.get(7));
    assertEquals(AttributeId.UserRolePermissions, attributes.get(8));
    assertEquals(AttributeId.AccessRestrictions, attributes.get(9));

    assertEquals(AttributeId.IsAbstract, attributes.get(10));
    assertEquals(AttributeId.DataTypeDefinition, attributes.get(11));
  }

  @Test
  public void testVariableAttributes() {
    var attributes = new ArrayList<>(AttributeId.VARIABLE_ATTRIBUTES);

    assertEquals(AttributeId.NodeId, attributes.get(0));
    assertEquals(AttributeId.NodeClass, attributes.get(1));
    assertEquals(AttributeId.BrowseName, attributes.get(2));
    assertEquals(AttributeId.DisplayName, attributes.get(3));
    assertEquals(AttributeId.Description, attributes.get(4));
    assertEquals(AttributeId.WriteMask, attributes.get(5));
    assertEquals(AttributeId.UserWriteMask, attributes.get(6));
    assertEquals(AttributeId.RolePermissions, attributes.get(7));
    assertEquals(AttributeId.UserRolePermissions, attributes.get(8));
    assertEquals(AttributeId.AccessRestrictions, attributes.get(9));

    assertEquals(AttributeId.Value, attributes.get(10));
    assertEquals(AttributeId.DataType, attributes.get(11));
    assertEquals(AttributeId.ValueRank, attributes.get(12));
    assertEquals(AttributeId.ArrayDimensions, attributes.get(13));
    assertEquals(AttributeId.AccessLevel, attributes.get(14));
    assertEquals(AttributeId.UserAccessLevel, attributes.get(15));
    assertEquals(AttributeId.MinimumSamplingInterval, attributes.get(16));
    assertEquals(AttributeId.Historizing, attributes.get(17));
    assertEquals(AttributeId.AccessLevelEx, attributes.get(18));
  }

  @Test
  public void testVariableTypeAttributes() {
    var attributes = new ArrayList<>(AttributeId.VARIABLE_TYPE_ATTRIBUTES);

    assertEquals(AttributeId.NodeId, attributes.get(0));
    assertEquals(AttributeId.NodeClass, attributes.get(1));
    assertEquals(AttributeId.BrowseName, attributes.get(2));
    assertEquals(AttributeId.DisplayName, attributes.get(3));
    assertEquals(AttributeId.Description, attributes.get(4));
    assertEquals(AttributeId.WriteMask, attributes.get(5));
    assertEquals(AttributeId.UserWriteMask, attributes.get(6));
    assertEquals(AttributeId.RolePermissions, attributes.get(7));
    assertEquals(AttributeId.UserRolePermissions, attributes.get(8));
    assertEquals(AttributeId.AccessRestrictions, attributes.get(9));

    assertEquals(AttributeId.Value, attributes.get(10));
    assertEquals(AttributeId.DataType, attributes.get(11));
    assertEquals(AttributeId.ValueRank, attributes.get(12));
    assertEquals(AttributeId.ArrayDimensions, attributes.get(13));
    assertEquals(AttributeId.IsAbstract, attributes.get(14));
  }
}
