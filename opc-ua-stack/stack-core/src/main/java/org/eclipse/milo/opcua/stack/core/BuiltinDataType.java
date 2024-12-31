/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
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
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

public enum BuiltinDataType {
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

  BuiltinDataType(int typeId, Class<?> backingClass) {
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

  private static final BiMap<Integer, Class<?>> BackingClassesById;
  private static final BiMap<NodeId, Class<?>> BackingClassesByNodeId;
  private static final BiMap<NodeId, BuiltinDataType> DataTypesByNodeId;

  static {
    ImmutableBiMap.Builder<Integer, Class<?>> builder = ImmutableBiMap.builder();
    ImmutableBiMap.Builder<NodeId, Class<?>> builder2 = ImmutableBiMap.builder();
    ImmutableBiMap.Builder<NodeId, BuiltinDataType> builder3 = ImmutableBiMap.builder();

    for (BuiltinDataType dataType : values()) {
      builder.put(dataType.getTypeId(), dataType.getBackingClass());
      builder2.put(dataType.getNodeId(), dataType.getBackingClass());
      builder3.put(dataType.getNodeId(), dataType);
    }

    BackingClassesById = builder.build();
    BackingClassesByNodeId = builder2.build();
    DataTypesByNodeId = builder3.build();
  }

  /**
   * @param backingClass the backing {@link Class} of the builtin type.
   * @return the id of the builtin type backed by {@code backingClass}.
   */
  public static int getBuiltinTypeId(Class<?> backingClass) {
    return BackingClassesById.inverse().get(maybeBoxPrimitive(backingClass));
  }

  /**
   * @param typeId the id of the builtin type.
   * @return the {@link Class} backing the builtin type.
   */
  @Nullable
  public static Class<?> getBackingClass(int typeId) {
    return BackingClassesById.get(typeId);
  }

  @Nullable
  public static Class<?> getBackingClass(NodeId typeId) {
    return BackingClassesByNodeId.get(typeId);
  }

  @Nullable
  public static Class<?> getBackingClass(ExpandedNodeId typeId) {
    if (typeId.getNamespaceIndex().intValue() == 0 && typeId.getType() == IdType.Numeric) {
      Number id = (Number) typeId.getIdentifier();
      return BackingClassesById.get(id.intValue());
    }

    return null;
  }

  public static @Nullable BuiltinDataType fromTypeId(int typeId) {
    // TODO turn this into a lookup
    for (BuiltinDataType builtinDataType : values()) {
      if (builtinDataType.typeId == typeId) {
        return builtinDataType;
      }
    }
    return null;
  }

  @Nullable
  public static BuiltinDataType fromBackingClass(Class<?> backingClass) {
    NodeId nodeId = BackingClassesByNodeId.inverse().get(maybeBoxPrimitive(backingClass));

    return nodeId != null ? DataTypesByNodeId.get(nodeId) : null;
  }

  @Nullable
  public static BuiltinDataType fromNodeId(NodeId nodeId) {
    return DataTypesByNodeId.get(nodeId);
  }

  @Nullable
  public static BuiltinDataType fromNodeId(ExpandedNodeId nodeId) {
    if (nodeId.getIdentifier() instanceof UInteger
        && (Namespaces.OPC_UA.equals(nodeId.getNamespaceUri())
            || nodeId.getNamespaceIndex().intValue() == 0)) {

      return fromNodeId(new NodeId(0, (UInteger) nodeId.getIdentifier()));
    } else {
      return null;
    }
  }

  public static boolean isBuiltin(int typeId) {
    return BackingClassesById.containsKey(typeId);
  }

  public static boolean isBuiltin(NodeId typeId) {
    return BackingClassesByNodeId.containsKey(typeId);
  }

  public static boolean isBuiltin(ExpandedNodeId typeId) {
    return BuiltinDataType.fromNodeId(typeId) != null;
  }

  public static boolean isBuiltin(Class<?> clazz) {
    return BackingClassesById.inverse().containsKey(maybeBoxPrimitive(clazz));
  }

  private static Class<?> maybeBoxPrimitive(Class<?> clazz) {
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
    }

    return clazz;
  }
}
