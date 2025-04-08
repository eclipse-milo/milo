/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;

public interface CodecFactory {

  /**
   * Create a {@link DataTypeCodec} instance for {@code dataType}.
   *
   * @param dataType the {@link DataType} to create the codec for.
   * @param dataTypeTree the {@link DataTypeTree}.
   * @return a {@link DataTypeCodec} for {@code dataType}.
   */
  DataTypeCodec create(DataType dataType, DataTypeTree dataTypeTree);
}
