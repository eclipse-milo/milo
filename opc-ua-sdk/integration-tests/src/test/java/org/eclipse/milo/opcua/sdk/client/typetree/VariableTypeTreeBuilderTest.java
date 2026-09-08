/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.typetree;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VariableTypeTreeBuilderTest extends AbstractClientServerTest {

  @Test
  void buildAsync() throws Exception {
    var tree = VariableTypeTreeBuilder.buildAsync(client).get(10, TimeUnit.SECONDS);
    assertTrue(tree.isSubtypeOf(NodeIds.BaseDataVariableType, NodeIds.BaseVariableType));
  }
}
