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

import org.eclipse.milo.opcua.sdk.core.typetree.AbstractVariableTypeTreeTest;
import org.eclipse.milo.opcua.sdk.core.typetree.VariableTypeTree;
import org.eclipse.milo.opcua.stack.core.UaException;

public class ClientVariableTypeTreeTest extends AbstractVariableTypeTreeTest {

  @Override
  protected VariableTypeTree getVariableTypeTree() throws UaException {
    return VariableTypeTreeBuilder.build(client);
  }
}
