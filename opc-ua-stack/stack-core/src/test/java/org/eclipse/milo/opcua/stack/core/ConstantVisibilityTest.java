/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ConstantVisibilityTest {

  // Scala callers must import the declaring classes to access inherited Java static constants.
  @ParameterizedTest
  @MethodSource("constantClasses")
  void constantDeclaringClassesArePublic(Class<?> type) {
    assertTrue(Modifier.isPublic(type.getModifiers()), () -> type.getName() + " must be public");
  }

  static Stream<Class<?>> constantClasses() {
    return Stream.concat(
        Stream.<Class<?>>iterate(NodeIds.class, type -> type != Object.class, Class::getSuperclass),
        Stream.<Class<?>>iterate(
            StatusCodes.class, type -> type != Object.class, Class::getSuperclass));
  }
}
