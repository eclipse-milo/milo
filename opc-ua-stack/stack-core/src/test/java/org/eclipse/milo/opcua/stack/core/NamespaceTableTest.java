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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.junit.jupiter.api.Test;

class NamespaceTableTest {

  @Test
  public void containsOpcUaNamespace() {
    NamespaceTable namespaceTable = new NamespaceTable();

    assertEquals(Namespaces.OPC_UA, namespaceTable.get(0));
  }
}
