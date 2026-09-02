/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.dtd.gds;

import org.eclipse.milo.opcua.sdk.core.dtd.BinaryDataTypeCodec;
import org.eclipse.milo.opcua.sdk.core.dtd.BinaryDataTypeDictionary;
import org.eclipse.milo.opcua.sdk.core.dtd.DataTypeDictionaryInitializer;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.gds.types.ApplicationRecordDataType;
import org.eclipse.milo.opcua.stack.core.types.DataTypeDictionary;

public class BinaryDataTypeDictionaryInitializer extends DataTypeDictionaryInitializer {
  @Override
  protected void initializeStructs(
      NamespaceTable namespaceTable, DataTypeDictionary binaryDictionary) throws Exception {
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "1:ApplicationRecordDataType",
            ApplicationRecordDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ApplicationRecordDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ApplicationRecordDataType.Codec())));
  }
}
