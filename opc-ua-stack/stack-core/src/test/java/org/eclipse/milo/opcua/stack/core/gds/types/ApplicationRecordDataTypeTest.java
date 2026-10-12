/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.gds.types;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.ServerTable;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingManager;
import org.eclipse.milo.opcua.stack.core.encoding.OpcUaEncodingManager;
import org.eclipse.milo.opcua.stack.core.gds.DataTypeInitializer;
import org.eclipse.milo.opcua.stack.core.gds.GdsNodeIds;
import org.eclipse.milo.opcua.stack.core.types.DataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.DefaultDataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ApplicationRecordDataTypeTest {

  private static final String GDS_URI = "http://opcfoundation.org/UA/GDS/";

  static Stream<Arguments> records() {
    return Stream.of(
        Arguments.of(
            "all optional fields null",
            new ApplicationRecordDataType(
                NodeId.NULL_VALUE, null, ApplicationType.Client, null, null, null, null)),
        Arguments.of(
            "all fields populated",
            new ApplicationRecordDataType(
                new NodeId(2, "app-1"),
                "urn:eclipse:milo:test",
                ApplicationType.ClientAndServer,
                new LocalizedText[] {
                  LocalizedText.english("Test"), new LocalizedText("de", "Test")
                },
                "urn:eclipse:milo:product",
                new String[] {
                  "opc.tcp://localhost:4840/test", "opc.tcp://localhost:4840/discovery"
                },
                new String[] {"NA", "DA"})));
  }

  // Part 12 §6.5.5 defines ApplicationRecordDataType with GDS-namespace encoding ids, so the codec
  // can only be found once DataTypeInitializer has resolved them through a NamespaceTable that
  // contains the GDS URI. This proves that registration plus the generated codec round-trip a
  // record through an ExtensionObject the way a Directory method result carries it.
  @ParameterizedTest(name = "{0}")
  @MethodSource("records")
  void binaryEncodingRoundTripsThroughExtensionObject(String name, ApplicationRecordDataType record)
      throws Exception {

    EncodingContext context = gdsEncodingContext();

    ExtensionObject encoded = ExtensionObject.encode(context, record);
    Object decoded = encoded.decode(context);

    assertEquals(
        GdsNodeIds.ApplicationRecordDataType_Encoding_DefaultBinary.toNodeIdOrThrow(
            context.getNamespaceTable()),
        encoded.getEncodingOrTypeId(),
        "encoded with the GDS-namespace binary encoding id");
    assertEquals(record, decoded);
  }

  private static EncodingContext gdsEncodingContext() {
    var namespaceTable = new NamespaceTable();
    namespaceTable.add("urn:some:other:namespace");
    namespaceTable.add(GDS_URI);
    assertEquals(ushort(2), namespaceTable.getIndex(GDS_URI), "GDS is not at index 1");

    DataTypeManager dataTypeManager = DefaultDataTypeManager.createAndInitialize(namespaceTable);
    DataTypeInitializer.initialize(namespaceTable, dataTypeManager);

    return new EncodingContext() {
      @Override
      public DataTypeManager getDataTypeManager() {
        return dataTypeManager;
      }

      @Override
      public EncodingManager getEncodingManager() {
        return OpcUaEncodingManager.getInstance();
      }

      @Override
      public EncodingLimits getEncodingLimits() {
        return EncodingLimits.DEFAULT;
      }

      @Override
      public NamespaceTable getNamespaceTable() {
        return namespaceTable;
      }

      @Override
      public ServerTable getServerTable() {
        return new ServerTable();
      }
    };
  }
}
