/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.gds;

import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.gds.types.ApplicationRecordDataType;
import org.eclipse.milo.opcua.stack.core.types.DataTypeManager;

public class DataTypeInitializer {
  public static void initialize(NamespaceTable namespaceTable, DataTypeManager dataTypeManager) {
    try {
      registerStructCodecs(namespaceTable, dataTypeManager);
    } catch (Exception e) {
      throw new RuntimeException("DataType initialization failed", e);
    }
  }

  private static void registerStructCodecs(
      NamespaceTable namespaceTable, DataTypeManager dataTypeManager) throws Exception {
    dataTypeManager.registerType(
        ApplicationRecordDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
        new ApplicationRecordDataType.Codec(),
        ApplicationRecordDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
        ApplicationRecordDataType.XML_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
        ApplicationRecordDataType.JSON_ENCODING_ID.toNodeIdOrThrow(namespaceTable));
  }
}
