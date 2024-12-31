/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types;

import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

public interface DataTypeManager {

  void registerType(
      NodeId dataTypeId,
      DataTypeCodec codec,
      @Nullable NodeId binaryEncodingId,
      @Nullable NodeId xmlEncodingId,
      @Nullable NodeId jsonEncodingId);

  /**
   * Get the {@link DataTypeCodec} for the given {@link NodeId}.
   *
   * <p>The {@link NodeId} may be either the datatype or encoding id.
   *
   * @param id the {@link NodeId} of either the datatype or encoding.
   * @return the {@link DataTypeCodec} for the given {@link NodeId}.
   */
  @Nullable DataTypeCodec getCodec(NodeId id);

  @Nullable NodeId getBinaryEncodingId(NodeId dataTypeId);

  @Nullable NodeId getXmlEncodingId(NodeId dataTypeId);

  @Nullable NodeId getJsonEncodingId(NodeId dataTypeId);

  @Nullable DataTypeDictionary getTypeDictionary(String namespaceUri);

  void registerTypeDictionary(DataTypeDictionary dictionary);
}
