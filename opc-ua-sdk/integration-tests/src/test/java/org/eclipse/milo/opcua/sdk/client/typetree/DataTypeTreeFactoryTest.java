/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.typetree;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.DataTypeManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DataTypeTreeFactoryTest extends AbstractClientServerTest {

  @Test
  void defaultFactoryCreatesEagerTree() throws Exception {
    DataTypeTree tree = client.getDataTypeTree();

    assertNotNull(tree);
    assertNotNull(tree.getDataType(NodeIds.Int32));
    assertNotNull(tree.getDataType(NodeIds.String));
    assertNotNull(tree.getDataType(NodeIds.XVType));
  }

  @Test
  void setDataTypeTreeFactoryToLazy() throws Exception {
    client.setDataTypeTreeFactory(DataTypeTreeFactory.lazy());

    DataTypeTree tree = client.getDataTypeTree();

    assertNotNull(tree);
    assertInstanceOf(LazyClientDataTypeTree.class, tree);
    assertNotNull(tree.getDataType(NodeIds.Int32));
    assertNotNull(tree.getDataType(NodeIds.String));
    assertNotNull(tree.getDataType(NodeIds.XVType));
  }

  @Test
  void setDataTypeTreeFactoryResetsCache() throws Exception {
    client.setDataTypeTreeFactory(DataTypeTreeFactory.eager());

    DataTypeTree tree1 = client.getDataTypeTree();
    assertNotNull(tree1);

    client.setDataTypeTreeFactory(DataTypeTreeFactory.lazy());

    DataTypeTree tree2 = client.getDataTypeTree();
    assertNotNull(tree2);

    assertNotSame(tree1, tree2);
    assertInstanceOf(LazyClientDataTypeTree.class, tree2);
  }

  @Test
  void readDataTypeTreeResetsDerivedCaches() throws Exception {
    DataTypeTree treeBefore = client.getDataTypeTree();
    DataTypeManager managerBefore = client.getDynamicDataTypeManager();
    EncodingContext contextBefore = client.getDynamicEncodingContext();

    DataTypeTree treeAfter = client.readDataTypeTree();

    // The dynamic DataTypeManager and EncodingContext hold references to the tree they were
    // created against, so refreshing the tree must rebuild them too.
    assertNotSame(treeBefore, treeAfter);
    assertNotSame(managerBefore, client.getDynamicDataTypeManager());
    assertNotSame(contextBefore, client.getDynamicEncodingContext());
  }

  @Test
  void customFactoryIsUsed() throws Exception {
    var customTree = new LazyClientDataTypeTree(client);

    client.setDataTypeTreeFactory(c -> customTree);

    DataTypeTree tree = client.getDataTypeTree();

    assertNotNull(tree);
    assertInstanceOf(LazyClientDataTypeTree.class, tree);
  }
}
