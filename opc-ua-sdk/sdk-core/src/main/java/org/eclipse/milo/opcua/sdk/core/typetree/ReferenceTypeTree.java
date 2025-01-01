/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.typetree;

import org.eclipse.milo.opcua.stack.core.util.Tree;

/** A tree-based representation of a ReferenceType hierarchy. */
public class ReferenceTypeTree extends TypeTree<ReferenceType> {

  public ReferenceTypeTree(Tree<ReferenceType> tree) {
    super(tree);
  }
}
