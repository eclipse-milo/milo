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
public abstract class AbstractObjectTypeTreeTest extends AbstractClientServerTest {

  private ObjectTypeTree objectTypeTree;

  @BeforeAll
  public void buildObjectTypeTree() throws UaException {
    objectTypeTree = getObjectTypeTree();
  }

  protected abstract ObjectTypeTree getObjectTypeTree() throws UaException;

  @Test
  public void testIsSubtype() {
    assertTrue(objectTypeTree.isSubtypeOf(NodeIds.FolderType, NodeIds.BaseObjectType));
    assertTrue(objectTypeTree.isSubtypeOf(NodeIds.ServerType, NodeIds.BaseObjectType));
    assertTrue(objectTypeTree.isSubtypeOf(NodeIds.FileType, NodeIds.BaseObjectType));

    assertFalse(objectTypeTree.isSubtypeOf(NodeIds.BaseObjectType, NodeIds.FolderType));
    assertFalse(objectTypeTree.isSubtypeOf(NodeIds.FolderType, NodeIds.ServerType));
  }

  @Test
  public void testGetObjectType() {
    ObjectType baseObjectType = objectTypeTree.getObjectType(NodeIds.BaseObjectType);
    assertNotNull(baseObjectType);

    ObjectType folderType = objectTypeTree.getObjectType(NodeIds.FolderType);
    assertNotNull(folderType);

    ObjectType serverType = objectTypeTree.getObjectType(NodeIds.ServerType);
    assertNotNull(serverType);
  }

  @Test
  void testIsAbstractAttribute() {
    ObjectType baseObjectType = objectTypeTree.getObjectType(NodeIds.BaseObjectType);
    assertNotNull(baseObjectType);
    assertFalse(baseObjectType.isAbstract());

    ObjectType folderType = objectTypeTree.getObjectType(NodeIds.FolderType);
    assertNotNull(folderType);
    assertFalse(folderType.isAbstract());
  }
}
