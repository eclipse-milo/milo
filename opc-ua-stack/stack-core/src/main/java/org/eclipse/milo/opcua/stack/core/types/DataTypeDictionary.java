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

import java.util.List;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

public interface DataTypeDictionary {

  String getNamespaceUri();

  @Nullable DataTypeCodec getCodec(String description);

  void registerType(Type type);

  @Nullable Type getType(String description);

  List<Type> getTypes();

  interface Type {
    String getDescription();

    NodeId getDataTypeId();

    NodeId getEncodingId();

    DataTypeCodec getCodec();
  }
}
