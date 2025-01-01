/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util;

import com.google.common.collect.HashBiMap;
import java.util.Map;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.Nullable;

public class TypeUtil {

  private static final Map<Integer, Class<?>> BUILTIN_TYPES;
  private static final Map<Class<?>, Integer> BUILTIN_TYPES_INVERSE;

  private static final Map<Class<?>, Integer> PRIMITIVE_BUILTIN_TYPES;
  private static final Map<Integer, Class<?>> PRIMITIVE_BUILTIN_TYPES_INVERSE;

  static {
    HashBiMap<Integer, Class<?>> builtinTypes = HashBiMap.create();
    builtinTypes.put(1, Boolean.class);
    builtinTypes.put(2, Byte.class);
    builtinTypes.put(3, UByte.class);
    builtinTypes.put(4, Short.class);
    builtinTypes.put(5, UShort.class);
    builtinTypes.put(6, Integer.class);
    builtinTypes.put(7, UInteger.class);
    builtinTypes.put(8, Long.class);
    builtinTypes.put(9, ULong.class);
    builtinTypes.put(10, Float.class);
    builtinTypes.put(11, Double.class);
    builtinTypes.put(12, String.class);
    builtinTypes.put(13, DateTime.class);
    builtinTypes.put(14, UUID.class);
    builtinTypes.put(15, ByteString.class);
    builtinTypes.put(16, XmlElement.class);
    builtinTypes.put(17, NodeId.class);
    builtinTypes.put(18, ExpandedNodeId.class);
    builtinTypes.put(19, StatusCode.class);
    builtinTypes.put(20, QualifiedName.class);
    builtinTypes.put(21, LocalizedText.class);
    builtinTypes.put(22, ExtensionObject.class);
    builtinTypes.put(23, DataValue.class);
    builtinTypes.put(24, Variant.class);
    builtinTypes.put(25, DiagnosticInfo.class);

    BUILTIN_TYPES = Map.copyOf(builtinTypes);
    BUILTIN_TYPES_INVERSE = Map.copyOf(builtinTypes.inverse());

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
   * @param backingType the backing {@link Class} of the builtin type.
   * @return the id of the builtin type backed by {@code backingType}, or -1 if backingType is not
   *     builtin.
   */
  public static int getBuiltinTypeId(Class<?> backingType) {
    if (backingType.isPrimitive()) {
      return PRIMITIVE_BUILTIN_TYPES.getOrDefault(backingType, -1);
    } else {
      return BUILTIN_TYPES_INVERSE.getOrDefault(backingType, -1);
    }
  }

  /**
   * @param typeId the id of the builtin type.
   * @return true if the type is builtin.
   */
  public static boolean isBuiltin(NodeId typeId) {
    return BUILTIN_TYPES.containsKey(id(typeId));
  }

  /**
   * @param typeId the id of the builtin type.
   * @return true if the type is builtin.
   */
  public static boolean isBuiltin(ExpandedNodeId typeId) {
    return BUILTIN_TYPES.containsKey(id(typeId));
  }

  /**
   * @param id the id of the builtin type.
   * @return the {@link Class} backing the builtin type.
   */
  @Nullable
  public static Class<?> getBackingClass(int id) {
    return BUILTIN_TYPES.get(id);
  }

  /**
   * @param typeId the id of the builtin type.
   * @return the {@link Class} backing the builtin type.
   */
  @Nullable
  public static Class<?> getBackingClass(NodeId typeId) {
    return getBackingClass(id(typeId));
  }

  /**
   * @param id the id of the builtin type.
   * @return the {@link Class} backing the builtin type, favoring the primitive version, if
   *     applicable.
   */
  @Nullable
  public static Class<?> getPrimitiveBackingClass(int id) {
    return PRIMITIVE_BUILTIN_TYPES_INVERSE.getOrDefault(id, getBackingClass(id));
  }

  /**
   * @param typeId the id of the builtin type.
   * @return the {@link Class} backing the builtin type, favoring the primitive version, if
   *     applicable.
   */
  @Nullable
  public static Class<?> getPrimitiveBackingClass(NodeId typeId) {
    return getPrimitiveBackingClass(id(typeId));
  }

  private static int id(NodeId nodeId) {
    if (nodeId.getIdentifier() instanceof UInteger) {
      return ((UInteger) nodeId.getIdentifier()).intValue();
    } else {
      return -1;
    }
  }

  private static int id(ExpandedNodeId nodeId) {
    if (nodeId.getIdentifier() instanceof UInteger) {
      return ((UInteger) nodeId.getIdentifier()).intValue();
    } else {
      return -1;
    }
  }
}
