/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.types;

import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.sdk.server.typetree.ReferenceTypeTreeBuilder;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReferenceTypeTreeBuilderTest {

  private ReferenceTypeTree referenceTypeTree;

  @BeforeAll
  public void buildReferenceTypeTree() throws Exception {
    referenceTypeTree = ReferenceTypeTreeBuilder.build(TestServer.create());
  }

  @Test
  void traverseFromRoot() {
    referenceTypeTree
        .getRoot()
        .traverseWithDepth(
            (dataType, depth) -> {
              for (int i = 0; i < depth; i++) {
                System.out.print("\t");
              }
              System.out.println(dataType.getBrowseName().toParseableString());
            });
  }
}
