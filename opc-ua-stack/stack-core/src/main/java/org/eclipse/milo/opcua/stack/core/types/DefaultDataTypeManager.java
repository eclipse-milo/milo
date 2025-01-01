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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.jspecify.annotations.Nullable;

public class DefaultDataTypeManager implements DataTypeManager {

  /** K = NodeId of DataType V = DataTypeCodec */
  private final Map<NodeId, DataTypeCodec> codecsByDataTypeId = new ConcurrentHashMap<>();

  /** K = NodeId of DataType Encoding V = DataTypeCodec */
  private final Map<NodeId, DataTypeCodec> codecsByEncodingId = new ConcurrentHashMap<>();

  /** R = QualifiedName of DataType Encoding C = NodeId of DataType V = DataTypeCodec */
  private final Map<QualifiedName, Map<NodeId, DataTypeCodec>> codecsByEncodingName =
      new ConcurrentHashMap<>();

  /**
   * R = QualifiedName of DataType Encoding C = NodeId of DataType V = NodeId of DataType Encoding
   */
  private final Map<QualifiedName, Map<NodeId, NodeId>> encodingIdsByEncodingName =
      new ConcurrentHashMap<>();

  /** K = String of Namespace URI V = DataTypeDictionary */
  private final Map<String, DataTypeDictionary> dataTypeDictionaries = new ConcurrentHashMap<>();

  @Override
  public void registerType(
      NodeId dataTypeId,
      DataTypeCodec codec,
      @Nullable NodeId binaryEncodingId,
      @Nullable NodeId xmlEncodingId,
      @Nullable NodeId jsonEncodingId) {

    codecsByDataTypeId.put(dataTypeId, codec);

    if (binaryEncodingId != null && binaryEncodingId.isNotNull()) {
      putCodecForEncoding(DataTypeEncoding.BINARY_ENCODING_NAME, dataTypeId, codec);
      putEncodingIdForEncoding(DataTypeEncoding.BINARY_ENCODING_NAME, dataTypeId, binaryEncodingId);
      codecsByEncodingId.put(binaryEncodingId, codec);
    }
    if (xmlEncodingId != null && xmlEncodingId.isNotNull()) {
      putCodecForEncoding(DataTypeEncoding.XML_ENCODING_NAME, dataTypeId, codec);
      putEncodingIdForEncoding(DataTypeEncoding.XML_ENCODING_NAME, dataTypeId, xmlEncodingId);
      codecsByEncodingId.put(xmlEncodingId, codec);
    }
    if (jsonEncodingId != null && jsonEncodingId.isNotNull()) {
      putCodecForEncoding(DataTypeEncoding.JSON_ENCODING_NAME, dataTypeId, codec);
      putEncodingIdForEncoding(DataTypeEncoding.JSON_ENCODING_NAME, dataTypeId, jsonEncodingId);
      codecsByEncodingId.put(jsonEncodingId, codec);
    }
  }

  @Override
  public @Nullable DataTypeCodec getCodec(NodeId id) {
    return codecsByEncodingId.getOrDefault(id, codecsByDataTypeId.get(id));
  }

  @Override
  public @Nullable NodeId getBinaryEncodingId(NodeId dataTypeId) {
    Map<NodeId, NodeId> byDataTypeId =
        encodingIdsByEncodingName.get(DataTypeEncoding.BINARY_ENCODING_NAME);

    return byDataTypeId != null ? byDataTypeId.get(dataTypeId) : null;
  }

  @Override
  public @Nullable NodeId getXmlEncodingId(NodeId dataTypeId) {
    Map<NodeId, NodeId> byDataTypeId =
        encodingIdsByEncodingName.get(DataTypeEncoding.XML_ENCODING_NAME);

    return byDataTypeId != null ? byDataTypeId.get(dataTypeId) : null;
  }

  @Override
  public @Nullable NodeId getJsonEncodingId(NodeId dataTypeId) {
    Map<NodeId, NodeId> byDataTypeId =
        encodingIdsByEncodingName.get(DataTypeEncoding.JSON_ENCODING_NAME);

    return byDataTypeId != null ? byDataTypeId.get(dataTypeId) : null;
  }

  @Override
  public @Nullable DataTypeDictionary getTypeDictionary(String namespaceUri) {
    return dataTypeDictionaries.get(namespaceUri);
  }

  @Override
  public void registerTypeDictionary(DataTypeDictionary dictionary) {
    dataTypeDictionaries.put(dictionary.getNamespaceUri(), dictionary);
  }

  private void putCodecForEncoding(
      QualifiedName encodingName, NodeId dataTypeId, DataTypeCodec codec) {
    Map<NodeId, DataTypeCodec> byDataTypeId =
        codecsByEncodingName.computeIfAbsent(encodingName, k -> new ConcurrentHashMap<>());
    byDataTypeId.put(dataTypeId, codec);
  }

  private void putEncodingIdForEncoding(
      QualifiedName encodingName, NodeId dataTypeId, NodeId encodingId) {
    Map<NodeId, NodeId> byDataTypeId =
        encodingIdsByEncodingName.computeIfAbsent(encodingName, k -> new ConcurrentHashMap<>());
    byDataTypeId.put(dataTypeId, encodingId);
  }

  /**
   * Create a {@link DefaultDataTypeManager} and initialize it by registering all built-in
   * DataTypes.
   *
   * @param namespaceTable a {@link NamespaceTable}.
   * @return a {@link DataTypeManager} pre-initialized wth the built-in DataTypes.
   */
  public static DataTypeManager createAndInitialize(NamespaceTable namespaceTable) {
    DefaultDataTypeManager dataTypeManager = new DefaultDataTypeManager();

    new DataTypeInitializer().initialize(namespaceTable, dataTypeManager);

    return dataTypeManager;
  }
}
