/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class AttributeIdTest {

  @Test
  public void testFrom() {
    for (AttributeId attributeId : AttributeId.values()) {
      int id = attributeId.id();

      assertEquals(AttributeId.from(id).orElseThrow(), attributeId);
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

    assertEquals(attributes.get(0), AttributeId.NodeId);
    assertEquals(attributes.get(1), AttributeId.NodeClass);
    assertEquals(attributes.get(2), AttributeId.BrowseName);
    assertEquals(attributes.get(3), AttributeId.DisplayName);
    assertEquals(attributes.get(4), AttributeId.Description);
    assertEquals(attributes.get(5), AttributeId.WriteMask);
    assertEquals(attributes.get(6), AttributeId.UserWriteMask);
    assertEquals(attributes.get(7), AttributeId.RolePermissions);
    assertEquals(attributes.get(8), AttributeId.UserRolePermissions);
    assertEquals(attributes.get(9), AttributeId.AccessRestrictions);

    assertEquals(attributes.get(10), AttributeId.IsAbstract);
    assertEquals(attributes.get(11), AttributeId.DataTypeDefinition);
  }

  @Test
  public void testVariableAttributes() {
    var attributes = new ArrayList<>(AttributeId.VARIABLE_ATTRIBUTES);

    assertEquals(attributes.get(0), AttributeId.NodeId);
    assertEquals(attributes.get(1), AttributeId.NodeClass);
    assertEquals(attributes.get(2), AttributeId.BrowseName);
    assertEquals(attributes.get(3), AttributeId.DisplayName);
    assertEquals(attributes.get(4), AttributeId.Description);
    assertEquals(attributes.get(5), AttributeId.WriteMask);
    assertEquals(attributes.get(6), AttributeId.UserWriteMask);
    assertEquals(attributes.get(7), AttributeId.RolePermissions);
    assertEquals(attributes.get(8), AttributeId.UserRolePermissions);
    assertEquals(attributes.get(9), AttributeId.AccessRestrictions);

    assertEquals(attributes.get(10), AttributeId.Value);
    assertEquals(attributes.get(11), AttributeId.DataType);
    assertEquals(attributes.get(12), AttributeId.ValueRank);
    assertEquals(attributes.get(13), AttributeId.ArrayDimensions);
    assertEquals(attributes.get(14), AttributeId.AccessLevel);
    assertEquals(attributes.get(15), AttributeId.UserAccessLevel);
    assertEquals(attributes.get(16), AttributeId.MinimumSamplingInterval);
    assertEquals(attributes.get(17), AttributeId.Historizing);
    assertEquals(attributes.get(18), AttributeId.AccessLevelEx);
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
