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

import java.io.ByteArrayInputStream;
import java.util.Base64;
import org.eclipse.milo.opcua.sdk.core.dtd.BinaryDataTypeCodec;
import org.eclipse.milo.opcua.sdk.core.dtd.BinaryDataTypeDictionary;
import org.eclipse.milo.opcua.sdk.core.dtd.BsdParser;
import org.eclipse.milo.opcua.sdk.core.dtd.DataTypeDictionaryInitializer;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.gds.types.ApplicationRecordDataType;
import org.eclipse.milo.opcua.stack.core.types.DataTypeDictionary;
import org.eclipse.milo.opcua.stack.core.types.DataTypeManager;
import org.opcfoundation.opcua.binaryschema.TypeDictionary;

public class BinaryDataTypeDictionaryInitializer extends DataTypeDictionaryInitializer {
  @Override
  public void initialize(NamespaceTable namespaceTable, DataTypeManager dataTypeManager) {
    if (dataTypeManager.getTypeDictionary("http://opcfoundation.org/UA/GDS/") != null) {
      return;
    }
    try {
      TypeDictionary schema =
          BsdParser.parse(
              new ByteArrayInputStream(
                  Base64.getDecoder()
                      .decode(
                          String.join(
                              "",
                              "PG9wYzpUeXBlRGljdGlvbmFyeQ0KICB4bWxuczpvcGM9Imh0dHA6Ly9vcGNmb3VuZGF0aW9uLm9yZy9CaW5hcnlTY2hlbWEvIg0KICB4bWxuczp4c2k9Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvWE1MU2NoZW1hLWluc3RhbmNlIg0KICB4bWxuczp1YT0iaHR0cDovL29wY2ZvdW5kYXRpb24ub3JnL1VBLyINCiAgeG1sbnM6dG5zPSJodHRwOi8vb3BjZm91bmRhdGlvbi5vcmcvVUEvR0RTLyINCiAgRGVmYXVsdEJ5dGVPcmRlcj0iTGl0dGxlRW5kaWFuIg0KICBUYXJnZXROYW1lc3BhY2U9Imh0dHA6Ly9vcGNmb3VuZGF0aW9uLm9yZy9VQS9HRFMvIg0KPg0KICA8b3BjOkltcG9ydCBOYW1lc3BhY2U9Imh0dHA6Ly9vcGNmb3VuZGF0aW9uLm9yZy9VQS8iIExvY2F0aW9uPSJPcGMuVWEuQmluYXJ5U2NoZW1hLmJzZCIvPg0KDQogIDxvcGM6U3RydWN0dXJlZFR5cGUgTmFtZT0iQXBwbGljYXRpb25SZWNvcmREYXRhVHlwZSIgQmFzZVR5cGU9InVhOkV4dGVuc2lvbk9iamVjdCI+DQogICAgPG9wYzpGaWVsZCBOYW1lPSJBcHBsaWNhdGlvbklkIiBUeXBlTmFtZT0idWE6Tm9kZUlkIiAvPg0KICAgIDxvcGM6RmllbGQgTmFtZT0iQXBwbGljYXRpb25VcmkiIFR5cGVOYW1lPSJvcGM6U3RyaW5nIiAvPg0KICAgIDxvcGM6RmllbGQgTmFtZT0iQXBwbGljYXRpb25UeXBlIiBUeXBlTmFtZT0idWE6QXBwbGljYXRpb25UeXBlIiAvPg0KICAgIDxvcGM6RmllbGQgTmFtZT0iTm9PZkFwcGxpY2F0aW9uTmFtZXMiIFR5cGVOYW1lPSJvcGM6SW50MzIiIC8+DQogICAgPG9wYzpGaWVsZCBOYW1lPSJBcHBsaWNhdGlvbk5hbWVzIiBUeXBlTmFtZT0idWE6TG9jYWxpemVkVGV4dCIgTGVuZ3RoRmllbGQ9Ik5vT2ZBcHBsaWNhdGlvbk5hbWVzIiAvPg0KICAgIDxvcGM6RmllbGQgTmFtZT0iUHJvZHVjdFVyaSIgVHlwZU5hbWU9Im9wYzpTdHJpbmciIC8+DQogICAgPG9wYzpGaWVsZCBOYW1lPSJOb09mRGlzY292ZXJ5VXJscyIgVHlwZU5hbWU9Im9wYzpJbnQzMiIgLz4NCiAgICA8b3BjOkZpZWxkIE5hbWU9IkRpc2NvdmVyeVVybHMiIFR5cGVOYW1lPSJvcGM6U3RyaW5nIiBMZW5ndGhGaWVsZD0iTm9PZkRpc2NvdmVyeVVybHMiIC8+DQogICAgPG9wYzpGaWVsZCBOYW1lPSJOb09mU2VydmVyQ2FwYWJpbGl0aWVzIiBUeXBlTmFtZT0ib3BjOkludDMyIiAvPg0KICAgIDxvcGM6RmllbGQgTmFtZT0iU2VydmVyQ2FwYWJpbGl0aWVzIiBUeXBlTmFtZT0ib3BjOlN0cmluZyIgTGVuZ3RoRmllbGQ9Ik5vT2ZTZXJ2ZXJDYXBhYmlsaXRpZXMiIC8+DQogIDwvb3BjOlN0cnVjdHVyZWRUeXBlPg0KDQo8L29wYzpUeXBlRGljdGlvbmFyeT4="))));
      BinaryDataTypeDictionary dictionary = new BinaryDataTypeDictionary(schema);
      initializeStructs(namespaceTable, dictionary);
      dataTypeManager.registerTypeDictionary(dictionary);
      for (DataTypeDictionary.Type type : dictionary.getTypes()) {
        dataTypeManager.registerType(
            type.getDataTypeId(), type.getCodec(), type.getEncodingId(), null, null);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void initializeStructs(
      NamespaceTable namespaceTable, DataTypeDictionary binaryDictionary) throws Exception {
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ApplicationRecordDataType",
            ApplicationRecordDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ApplicationRecordDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ApplicationRecordDataType.Codec())));
  }
}
