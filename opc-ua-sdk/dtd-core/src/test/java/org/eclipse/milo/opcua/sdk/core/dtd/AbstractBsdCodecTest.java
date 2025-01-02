/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import jakarta.xml.bind.JAXBException;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.ServerTable;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingManager;
import org.eclipse.milo.opcua.stack.core.encoding.OpcUaEncodingManager;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.types.DataTypeDictionary;
import org.eclipse.milo.opcua.stack.core.types.DataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.OpcUaDataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.opcfoundation.opcua.binaryschema.StructuredType;
import org.opcfoundation.opcua.binaryschema.TypeDictionary;

public abstract class AbstractBsdCodecTest {

  private static final String BSD_CODEC_TEST_NAMESPACE = "https://github.com/eclipse/milo";

  private final DataTypeManager dataTypeManager = OpcUaDataTypeManager.getInstance();
  private final EncodingManager encodingManager = OpcUaEncodingManager.getInstance();
  private final NamespaceTable namespaceTable = new NamespaceTable();
  private final ServerTable serverTable = new ServerTable();

  private final EncodingContext context =
      new EncodingContext() {

        @Override
        public DataTypeManager getDataTypeManager() {
          return dataTypeManager;
        }

        @Override
        public EncodingManager getEncodingManager() {
          return encodingManager;
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
          return serverTable;
        }
      };

  public AbstractBsdCodecTest() throws JAXBException {
    new BinaryDataTypeDictionaryInitializer().initialize(namespaceTable, dataTypeManager);

    TypeDictionary typeDictionary =
        BsdParser.parse(
            getClass().getClassLoader().getResourceAsStream("dictionaries/BsdParserTest.bsd.xml"));

    var binaryDictionary = new BinaryDataTypeDictionary(typeDictionary);

    typeDictionary
        .getOpaqueTypeOrEnumeratedTypeOrStructuredType()
        .forEach(
            typeDescription -> {
              if (typeDescription instanceof StructuredType) {
                StructuredType structuredType = (StructuredType) typeDescription;

                BinaryDataTypeCodec codec = createCodec(structuredType);

                binaryDictionary.registerType(
                    new BinaryDataTypeDictionary.BinaryType(
                        structuredType.getName(), NodeId.NULL_VALUE, NodeId.NULL_VALUE, codec));
              }
            });

    dataTypeManager.registerTypeDictionary(binaryDictionary);

    binaryDictionary
        .getTypes()
        .forEach(
            type ->
                dataTypeManager.registerType(
                    type.getDataTypeId(), type.getCodec(), type.getEncodingId(), null, null));
  }

  protected abstract BinaryDataTypeCodec createCodec(StructuredType structuredType);

  protected BinaryDataTypeCodec getCodec(String name) {
    DataTypeDictionary dictionary = dataTypeManager.getTypeDictionary(BSD_CODEC_TEST_NAMESPACE);
    assertNotNull(dictionary);
    DataTypeCodec codec = dictionary.getCodec(name);
    assertNotNull(codec);
    return (BinaryDataTypeCodec) codec;
  }

  protected void assertRoundTrip(String type, Object originalValue, BinaryDataTypeCodec codec) {
    System.out.printf("--- assertRoundTrip Type: %s ---\n", type);

    System.out.println("originalValue:\t" + originalValue);
    ByteBuf buffer = Unpooled.buffer();
    codec.encode(context, new OpcUaBinaryEncoder(context).setBuffer(buffer), originalValue);

    ByteBuf encodedValue = buffer.copy();
    System.out.println("encodedValue:\t" + ByteBufUtil.hexDump(encodedValue));

    Object decodedValue = codec.decode(context, new OpcUaBinaryDecoder(context).setBuffer(buffer));
    assertEquals(originalValue, decodedValue);
    System.out.println("decodedValue:\t" + decodedValue);
  }

  /**
   * A weaker version of {@link #assertRoundTrip(String, Object, BinaryDataTypeCodec)} for values
   * that don't implement equals and hashcode or values that contain members not implementing equals
   * and hashcode.
   *
   * <p>Relies on toString() values to be implemented at all levels instead... not great, but since
   * the built-in structs don't implement equals/hashcode it's what we have.
   */
  protected void assertRoundTripUsingToString(
      String type, Object originalValue, BinaryDataTypeCodec codec) {

    System.out.printf("--- assertRoundTrip Type: %s ---\n", type);

    System.out.println("originalValue:\t" + originalValue);
    ByteBuf buffer = Unpooled.buffer();
    codec.encode(context, new OpcUaBinaryEncoder(context).setBuffer(buffer), originalValue);

    ByteBuf encodedValue = buffer.copy();
    System.out.println("encodedValue:\t" + ByteBufUtil.hexDump(encodedValue));

    Object decodedValue = codec.decode(context, new OpcUaBinaryDecoder(context).setBuffer(buffer));
    assertEquals(originalValue.toString(), decodedValue.toString());
    System.out.println("decodedValue:\t" + decodedValue);
  }
}
