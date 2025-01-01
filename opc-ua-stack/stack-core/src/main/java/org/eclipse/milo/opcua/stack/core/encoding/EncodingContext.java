/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding;

import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.ServerTable;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.types.DataTypeManager;

public interface EncodingContext {

  /**
   * Get the {@link DataTypeManager}.
   *
   * @return the {@link DataTypeManager}.
   */
  DataTypeManager getDataTypeManager();

  /**
   * Get the {@link EncodingManager}.
   *
   * @return the {@link EncodingManager}.
   */
  EncodingManager getEncodingManager();

  /**
   * Get the {@link EncodingLimits}.
   *
   * @return the {@link EncodingLimits}.
   */
  EncodingLimits getEncodingLimits();

  /**
   * Get the {@link NamespaceTable}.
   *
   * @return the {@link NamespaceTable}.
   */
  NamespaceTable getNamespaceTable();

  /**
   * Get the {@link ServerTable}.
   *
   * @return the {@link ServerTable}.
   */
  ServerTable getServerTable();
}
