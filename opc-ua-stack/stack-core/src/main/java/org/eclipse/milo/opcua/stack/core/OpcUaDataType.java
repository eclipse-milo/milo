/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;
import java.util.Map;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.IdType;
import org.jspecify.annotations.Nullable;

public enum OpcUaDataType {
  Boolean(1, Boolean.class),
  SByte(2, Byte.class),
  Byte(3, UByte.class),
  Int16(4, Short.class),
  UInt16(5, UShort.class),
  Int32(6, Integer.class),
  UInt32(7, UInteger.class),
  Int64(8, Long.class),
  UInt64(9, ULong.class),
  Float(10, Float.class),
  Double(11, Double.class),
  String(12, String.class),
  DateTime(13, DateTime.class),
  Guid(14, UUID.class),
  ByteString(15, ByteString.class),
  XmlElement(16, XmlElement.class),
  NodeId(17, NodeId.class),
  ExpandedNodeId(18, org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.class),
  StatusCode(19, StatusCode.class),
  QualifiedName(20, QualifiedName.class),
  LocalizedText(21, org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText.class),
  ExtensionObject(22, ExtensionObject.class),
  DataValue(23, org.eclipse.milo.opcua.stack.core.types.builtin.DataValue.class),
  Variant(24, Variant.class),
  DiagnosticInfo(25, org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo.class);

  private final int typeId;
  private final Class<?> backingClass;

  OpcUaDataType(int typeId, Class<?> backingClass) {
    this.typeId = typeId;
    this.backingClass = backingClass;
  }

  public NodeId getNodeId() {
    return new NodeId(0, typeId);
  }

  public int getTypeId() {
    return typeId;
  }

  public Class<?> getBackingClass() {
    return backingClass;
  }

  private static final BiMap<Integer, Class<?>> BACKING_CLASSES_BY_ID;
  private static final BiMap<NodeId, Class<?>> BACKING_CLASSES_BY_NODE_ID;
  private static final BiMap<NodeId, OpcUaDataType> DATA_TYPES_BY_NODE_ID;
  private static final Map<Class<?>, Integer> PRIMITIVE_BUILTIN_TYPES;
  private static final Map<Integer, Class<?>> PRIMITIVE_BUILTIN_TYPES_INVERSE;

  static {
    ImmutableBiMap.Builder<Integer, Class<?>> builder = ImmutableBiMap.builder();
    ImmutableBiMap.Builder<NodeId, Class<?>> builder2 = ImmutableBiMap.builder();
    ImmutableBiMap.Builder<NodeId, OpcUaDataType> builder3 = ImmutableBiMap.builder();

    for (OpcUaDataType dataType : values()) {
      builder.put(dataType.getTypeId(), dataType.getBackingClass());
      builder2.put(dataType.getNodeId(), dataType.getBackingClass());
      builder3.put(dataType.getNodeId(), dataType);
    }

    BACKING_CLASSES_BY_ID = builder.build();
    BACKING_CLASSES_BY_NODE_ID = builder2.build();
    DATA_TYPES_BY_NODE_ID = builder3.build();

    HashBiMap<Class<?>, Integer> primitiveBuiltinTypes = HashBiMap.create();
    primitiveBuiltinTypes.put(boolean.class, 1);
    primitiveBuiltinTypes.put(byte.class, 2);
    primitiveBuiltinTypes.put(short.class, 4);
    primitiveBuiltinTypes.put(int.class, 6);
    primitiveBuiltinTypes.put(long.class, 8);
    primitiveBuiltinTypes.put(float.class, 10);
    primitiveBuiltinTypes.put(double.class, 11);

    PRIMITIVE_BUILTIN_TYPES = Map.copyOf(primitiveBuiltinTypes);
    PRIMITIVE_BUILTIN_TYPES_INVERSE = Map.copyOf(primitiveBuiltinTypes.inverse());
  }

  /**
   * @param backingClass the backing {@link Class} of the builtin type.
   * @return the id of the builtin type backed by {@code backingClass}.
   */
  public static int getBuiltinTypeId(Class<?> backingClass) {
    return BACKING_CLASSES_BY_ID.inverse().get(maybeTransformClass(backingClass));
  }

  /**
   * @param typeId the id of the builtin type.
   * @return the {@link Class} backing the builtin type.
   */
  public static @Nullable Class<?> getBackingClass(int typeId) {
    return BACKING_CLASSES_BY_ID.get(typeId);
  }

  public static @Nullable Class<?> getBackingClass(NodeId typeId) {
    return BACKING_CLASSES_BY_NODE_ID.get(typeId);
  }

  public static @Nullable Class<?> getBackingClass(ExpandedNodeId typeId) {
    if (typeId.isOpcUaNamespace() && typeId.getType() == IdType.Numeric) {
      Number id = (Number) typeId.getIdentifier();
      return BACKING_CLASSES_BY_ID.get(id.intValue());
    }

    return null;
  }

  public static @Nullable Class<?> getPrimitiveBackingClass(int typeId) {
    return PRIMITIVE_BUILTIN_TYPES_INVERSE.getOrDefault(typeId, getBackingClass(typeId));
  }

  public static @Nullable OpcUaDataType fromTypeId(int typeId) {
    // TODO turn this into a lookup
    for (OpcUaDataType dataType : values()) {
      if (dataType.typeId == typeId) {
        return dataType;
      }
    }
    return null;
  }

  @Nullable
  public static OpcUaDataType fromBackingClass(Class<?> backingClass) {
    NodeId nodeId = BACKING_CLASSES_BY_NODE_ID.inverse().get(maybeTransformClass(backingClass));

    return nodeId != null ? DATA_TYPES_BY_NODE_ID.get(nodeId) : null;
  }

  @Nullable
  public static OpcUaDataType fromNodeId(NodeId nodeId) {
    return DATA_TYPES_BY_NODE_ID.get(nodeId);
  }

  @Nullable
  public static OpcUaDataType fromNodeId(ExpandedNodeId nodeId) {
    if (nodeId.isOpcUaNamespace() && nodeId.getIdentifier() instanceof UInteger id) {
      return fromNodeId(new NodeId(0, id));
    } else {
      return null;
    }
  }

  public static boolean isBuiltin(int typeId) {
    return BACKING_CLASSES_BY_ID.containsKey(typeId);
  }

  public static boolean isBuiltin(NodeId typeId) {
    return BACKING_CLASSES_BY_NODE_ID.containsKey(typeId);
  }

  public static boolean isBuiltin(ExpandedNodeId typeId) {
    return OpcUaDataType.fromNodeId(typeId) != null;
  }

  public static boolean isBuiltin(Class<?> clazz) {
    return BACKING_CLASSES_BY_ID.inverse().containsKey(maybeTransformClass(clazz));
  }

  private static Class<?> maybeTransformClass(Class<?> clazz) {
    if (clazz.isPrimitive()) {
      if (clazz == boolean.class) {
        return Boolean.class;
      } else if (clazz == byte.class) {
        return Byte.class;
      } else if (clazz == short.class) {
        return Short.class;
      } else if (clazz == int.class) {
        return Integer.class;
      } else if (clazz == long.class) {
        return Long.class;
      } else if (clazz == float.class) {
        return Float.class;
      } else if (clazz == double.class) {
        return Double.class;
      }
    } else {
      if (org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject.class.isAssignableFrom(
          clazz)) {
        return ExtensionObject.class;
      }
    }

    return clazz;
  }
}
