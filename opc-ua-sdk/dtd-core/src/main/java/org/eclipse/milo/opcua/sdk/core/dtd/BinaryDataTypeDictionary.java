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

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.types.DataTypeDictionary;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;
import org.opcfoundation.opcua.binaryschema.TypeDescription;
import org.opcfoundation.opcua.binaryschema.TypeDictionary;

public class BinaryDataTypeDictionary implements DataTypeDictionary {

  private final Map<String, TypeDescription> typeDescriptions = new ConcurrentHashMap<>();
  private final Map<String, Type> types = new ConcurrentHashMap<>();

  private final TypeDictionary typeDictionary;

  public BinaryDataTypeDictionary(TypeDictionary typeDictionary) {
    this.typeDictionary = typeDictionary;

    typeDictionary
        .getOpaqueTypeOrEnumeratedTypeOrStructuredType()
        .forEach(
            typeDescription -> typeDescriptions.put(typeDescription.getName(), typeDescription));
  }

  @Override
  public String getNamespaceUri() {
    return typeDictionary.getTargetNamespace();
  }

  @Override
  public @Nullable DataTypeCodec getCodec(String description) {
    Type type = types.get(description);

    return type != null ? type.getCodec() : null;
  }

  @Override
  public void registerType(Type type) {
    String description = type.getDescription();

    types.put(description, type);
  }

  @Override
  public @Nullable Type getType(String description) {
    return types.get(description);
  }

  @Override
  public List<Type> getTypes() {
    return List.copyOf(types.values());
  }

  public @Nullable TypeDescription getTypeDescription(String description) {
    return typeDescriptions.get(description);
  }

  public void addTypeDescription(TypeDescription typeDescription) {
    typeDescriptions.put(typeDescription.getName(), typeDescription);
  }

  public static class BinaryType implements DataTypeDictionary.Type {

    public final String description;
    public final NodeId dataTypeId;
    public final NodeId encodingId;
    public final BinaryDataTypeCodec codec;

    public BinaryType(
        String description, NodeId dataTypeId, NodeId encodingId, BinaryDataTypeCodec codec) {
      this.description = description;
      this.dataTypeId = dataTypeId;
      this.encodingId = encodingId;
      this.codec = codec;
    }

    @Override
    public String getDescription() {
      return description;
    }

    @Override
    public NodeId getDataTypeId() {
      return dataTypeId;
    }

    @Override
    public NodeId getEncodingId() {
      return encodingId;
    }

    @Override
    public DataTypeCodec getCodec() {
      return codec;
    }
  }
}
