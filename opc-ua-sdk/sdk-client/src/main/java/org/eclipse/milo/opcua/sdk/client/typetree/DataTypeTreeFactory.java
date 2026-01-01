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

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.UaException;

/**
 * A factory that creates a {@link DataTypeTree} for a given {@link OpcUaClient}.
 *
 * <p>Implementations can create different types of DataTypeTrees, such as:
 *
 * <ul>
 *   <li>An eagerly built tree via {@link DataTypeTreeBuilder#build(OpcUaClient)}
 *   <li>A lazily-loaded tree via {@link LazyClientDataTypeTree}
 * </ul>
 *
 * @see DataTypeTreeBuilder
 * @see LazyClientDataTypeTree
 */
@FunctionalInterface
public interface DataTypeTreeFactory {

  /**
   * Creates a {@link DataTypeTree} for the given client.
   *
   * @param client the {@link OpcUaClient} to create a DataTypeTree for.
   * @return a new {@link DataTypeTree} instance.
   * @throws UaException if an error occurs while creating the tree.
   */
  DataTypeTree create(OpcUaClient client) throws UaException;

  /**
   * Returns the default factory that eagerly builds a {@link DataTypeTree} using {@link
   * DataTypeTreeBuilder#build(OpcUaClient)}.
   *
   * @return the default {@link DataTypeTreeFactory}.
   */
  static DataTypeTreeFactory eager() {
    return DataTypeTreeBuilder::build;
  }

  /**
   * Returns a factory that creates a {@link LazyClientDataTypeTree}.
   *
   * @return a {@link DataTypeTreeFactory} that creates lazy-loading trees.
   */
  static DataTypeTreeFactory lazy() {
    return LazyClientDataTypeTree::new;
  }
}
