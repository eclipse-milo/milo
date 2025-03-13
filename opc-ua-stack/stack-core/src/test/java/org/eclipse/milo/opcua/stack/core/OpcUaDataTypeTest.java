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

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class OpcUaDataTypeTest {

  @Test
  void fromBackingClass() {
    for (OpcUaDataType dataType : OpcUaDataType.values()) {
      Class<?> backingClass = dataType.getBackingClass();
      OpcUaDataType fromBackingClass = OpcUaDataType.fromBackingClass(backingClass);

      assertEquals(
          dataType,
          fromBackingClass,
          "%s should return %s".formatted(backingClass.getName(), dataType));
    }
  }

  @ParameterizedTest
  @MethodSource("extensionObjectSubtypeBackingClasses")
  void fromBackingClassExtensionObjects(Class<?> clazz) {
    assertEquals(
        OpcUaDataType.ExtensionObject,
        OpcUaDataType.fromBackingClass(clazz),
        "%s should return OpcUaDataType.ExtensionObject".formatted(clazz.getName()));
  }

  @ParameterizedTest
  @MethodSource("allBackingClasses")
  void isBuiltin(Class<?> clazz) {
    assertTrue(
        OpcUaDataType.isBuiltin(clazz),
        clazz.getSimpleName() + " should be recognized as a built-in type");
  }

  private static Stream<Class<?>> allBackingClasses() {
    return Stream.concat(standardBackingClasses(), extensionObjectSubtypeBackingClasses());
  }

  private static Stream<Class<?>> standardBackingClasses() {
    return Stream.of(OpcUaDataType.values()).map(OpcUaDataType::getBackingClass);
  }

  private static Stream<Class<?>> extensionObjectSubtypeBackingClasses() {
    return Stream.of(
        ExtensionObject.Binary.class, ExtensionObject.Xml.class, ExtensionObject.Json.class);
  }
}
