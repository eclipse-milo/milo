/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.dtd;

import java.util.List;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.core.dtd.generic.StructCodec;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.DataTypeDictionary;
import org.eclipse.milo.opcua.stack.core.types.DataTypeManager;

public class LegacyDataTypeManagerInitializer implements OpcUaClient.DataTypeManagerInitializer {

  private final OpcUaClient client;
  private final BinaryCodecFactory codecFactory;

  public LegacyDataTypeManagerInitializer(OpcUaClient client) {
    this(client, StructCodec::new);
  }

  public LegacyDataTypeManagerInitializer(OpcUaClient client, BinaryCodecFactory codecFactory) {
    this.client = client;
    this.codecFactory = codecFactory;
  }

  @Override
  public void initialize(
      NamespaceTable namespaceTable, DataTypeTree dataTypeTree, DataTypeManager dataTypeManager)
      throws UaException {

    List<DataTypeDictionary> dataTypeDictionaries =
        new BinaryDataTypeDictionaryReader(client).readDataTypeDictionaries(codecFactory);

    for (DataTypeDictionary dictionary : dataTypeDictionaries) {
      dataTypeManager.registerTypeDictionary(dictionary);

      dictionary
          .getTypes()
          .forEach(
              type ->
                  dataTypeManager.registerType(
                      type.getDataTypeId(), type.getCodec(), type.getEncodingId(), null, null));
    }
  }
}
