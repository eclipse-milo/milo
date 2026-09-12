/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.UaException;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.12">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.12</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface PubSubDiagnosticsDataSetReaderType extends PubSubDiagnosticsType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseObjectType getCountersNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseObjectType> getCountersNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseObjectType getLiveValuesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseObjectType> getLiveValuesNodeAsync();
}
