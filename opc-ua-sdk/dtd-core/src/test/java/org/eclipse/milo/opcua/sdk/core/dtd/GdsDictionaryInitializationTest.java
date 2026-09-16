/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.milo.opcua.sdk.core.dtd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.gds.types.ApplicationRecordDataType;
import org.eclipse.milo.opcua.stack.core.types.DataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.DefaultDataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.junit.jupiter.api.Test;

class GdsDictionaryInitializationTest {
  private static final String GDS_URI = "http://opcfoundation.org/UA/GDS/";

  // Standard initialization must not hide the separate GDS namespace's legacy dictionary/codec.
  @Test
  void standardThenGdsRegistersIndependentDictionaryAndCodec() throws Exception {
    verifyInitialization(true);
  }

  // GDS-first registration must not install a partial standard dictionary and block its codecs.
  @Test
  void gdsThenStandardRegistersIndependentDictionaryAndCodec() throws Exception {
    verifyInitialization(false);
  }

  private void verifyInitialization(boolean standardFirst) throws Exception {
    var namespaces = new NamespaceTable();
    namespaces.add("urn:unrelated");
    namespaces.add(GDS_URI);
    var manager = new DefaultDataTypeManager();
    var standard = new BinaryDataTypeDictionaryInitializer();
    var gds = new org.eclipse.milo.opcua.sdk.core.dtd.gds.BinaryDataTypeDictionaryInitializer();
    if (standardFirst) standard.initialize(namespaces, manager);
    gds.initialize(namespaces, manager);
    if (!standardFirst) standard.initialize(namespaces, manager);
    // Repeated registration remains idempotent for both namespaces.
    gds.initialize(namespaces, manager);
    standard.initialize(namespaces, manager);

    BinaryDataTypeDictionary dictionary =
        (BinaryDataTypeDictionary) manager.getTypeDictionary(GDS_URI);
    assertNotNull(dictionary, "GDS needs its own dictionary regardless of initialization order");
    assertNotNull(dictionary.getType("ApplicationRecordDataType"));
    assertNotNull(dictionary.getTypeDescription("ApplicationRecordDataType"));
    assertNull(
        manager
            .getTypeDictionary("http://opcfoundation.org/UA/")
            .getType("1:ApplicationRecordDataType"));
    assertNotNull(
        manager.getCodec(ApplicationRecordDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaces)));

    var context =
        new DefaultEncodingContext() {
          @Override
          public NamespaceTable getNamespaceTable() {
            return namespaces;
          }

          @Override
          public DataTypeManager getDataTypeManager() {
            return manager;
          }
        };
    var record =
        new ApplicationRecordDataType(
            new NodeId(2, "app"),
            "urn:test:gds",
            ApplicationType.Client,
            new LocalizedText[] {LocalizedText.english("Test")},
            "urn:test:product",
            null,
            null);
    ExtensionObject encoded = ExtensionObject.encode(context, record);
    assertEquals(record, encoded.decode(context));
  }
}
