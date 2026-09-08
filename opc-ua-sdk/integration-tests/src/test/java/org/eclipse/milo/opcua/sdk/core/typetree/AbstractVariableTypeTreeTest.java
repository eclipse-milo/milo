/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.typetree;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractVariableTypeTreeTest extends AbstractClientServerTest {

  private VariableTypeTree variableTypeTree;

  @BeforeAll
  public void buildVariableTypeTree() throws UaException {
    variableTypeTree = getVariableTypeTree();
  }

  protected abstract VariableTypeTree getVariableTypeTree() throws UaException;

  @Test
  public void testIsSubtype() {
    assertTrue(variableTypeTree.isSubtypeOf(NodeIds.PropertyType, NodeIds.BaseVariableType));
    assertTrue(variableTypeTree.isSubtypeOf(NodeIds.DataItemType, NodeIds.BaseDataVariableType));
    assertTrue(variableTypeTree.isSubtypeOf(NodeIds.AnalogItemType, NodeIds.DataItemType));

    assertFalse(variableTypeTree.isSubtypeOf(NodeIds.BaseVariableType, NodeIds.PropertyType));
    assertFalse(variableTypeTree.isSubtypeOf(NodeIds.PropertyType, NodeIds.DataItemType));
  }

  @Test
  public void testGetVariableType() {
    VariableType baseVariableType = variableTypeTree.getVariableType(NodeIds.BaseVariableType);
    assertNotNull(baseVariableType);

    VariableType propertyType = variableTypeTree.getVariableType(NodeIds.PropertyType);
    assertNotNull(propertyType);

    VariableType dataItemType = variableTypeTree.getVariableType(NodeIds.DataItemType);
    assertNotNull(dataItemType);
  }

  @Test
  void testIsAbstractAttribute() {
    VariableType baseVariableType = variableTypeTree.getVariableType(NodeIds.BaseVariableType);
    assertNotNull(baseVariableType);
    assertTrue(baseVariableType.isAbstract());

    VariableType baseDataVariableType =
        variableTypeTree.getVariableType(NodeIds.BaseDataVariableType);
    assertNotNull(baseDataVariableType);
    assertFalse(baseDataVariableType.isAbstract());

    VariableType propertyType = variableTypeTree.getVariableType(NodeIds.PropertyType);
    assertNotNull(propertyType);
    assertFalse(propertyType.isAbstract());
  }
}
