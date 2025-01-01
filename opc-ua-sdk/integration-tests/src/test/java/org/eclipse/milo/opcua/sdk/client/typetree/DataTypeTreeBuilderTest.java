/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.typetree;

import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DataTypeTreeBuilderTest extends AbstractClientServerTest {

  @Test
  void build() throws Exception {
    DataTypeTreeBuilder.build(client);
  }

  @Test
  void buildAsync() throws Exception {
    DataTypeTreeBuilder.buildAsync(client).get();
  }
}
