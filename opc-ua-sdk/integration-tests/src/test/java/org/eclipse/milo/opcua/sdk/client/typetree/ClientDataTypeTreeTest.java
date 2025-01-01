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

import org.eclipse.milo.opcua.sdk.core.typetree.AbstractDataTypeTreeTest;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.UaException;

public class ClientDataTypeTreeTest extends AbstractDataTypeTreeTest {

  @Override
  protected DataTypeTree getDataTypeTree() throws UaException {
    return client.getDataTypeTree();
  }
}
