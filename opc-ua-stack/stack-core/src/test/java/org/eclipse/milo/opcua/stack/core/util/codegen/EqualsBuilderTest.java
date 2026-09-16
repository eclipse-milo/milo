/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util.codegen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class EqualsBuilderTest {

  static Stream<Arguments> unequalValues() {
    return Stream.of(
        Arguments.of("int[] vs long[]", new int[] {1}, new long[] {1}),
        Arguments.of("boolean[] vs byte[]", new boolean[] {true}, new byte[] {1}),
        Arguments.of("float[] vs double[]", new float[] {1}, new double[] {1}),
        Arguments.of("int[] vs Integer[]", new int[] {1}, new Integer[] {1}),
        Arguments.of("int[] vs Object[]", new int[] {1}, new Object[] {1}),
        Arguments.of("int[] vs Integer", new int[] {1}, 1),
        Arguments.of("String[] vs String", new String[] {"x"}, "x"),
        Arguments.of("int[][] vs long[][]", new int[][] {{1}}, new long[][] {{1}}),
        Arguments.of(
            "nested int[] vs long[]",
            new Object[] {"x", new int[] {1}},
            new Object[] {"x", new long[] {1}}),
        Arguments.of("nested int[] vs Integer", new Object[] {new int[] {1}}, new Object[] {1}),
        Arguments.of("String[] vs Object[] content", new String[] {"x"}, new Object[] {"y"}));
  }

  static Stream<Arguments> compatibleValues() {
    return Stream.of(
        Arguments.of("int[] vs int[]", new int[] {1}, new int[] {1}),
        Arguments.of("String[] vs Object[]", new String[] {"x"}, new Object[] {"x"}),
        Arguments.of("int[][] vs Object[]", new int[][] {{1}}, new Object[] {new int[] {1}}),
        Arguments.of(
            "nested int[] vs int[]",
            new Object[] {null, new int[] {1}},
            new Object[] {null, new int[] {1}}));
  }

  // Generated equals() methods pass fields of type Object here. Operands whose
  // types cannot be compared element by element must compare unequal instead of
  // throwing ClassCastException, regardless of which side is the array. The last
  // case keeps compatible reference arrays with different content unequal.
  @ParameterizedTest(name = "{0}")
  @MethodSource("unequalValues")
  void mismatchedValuesCompareUnequalInBothOrders(String name, Object a, Object b) {
    assertEquals(false, new EqualsBuilder().append(a, b).build(), "a, b");
    assertEquals(false, new EqualsBuilder().append(b, a).build(), "b, a");
  }

  // Control cases: the compatibility check must not reject arrays that differ
  // only in their declared reference component type, or nested arrays.
  @ParameterizedTest(name = "{0}")
  @MethodSource("compatibleValues")
  void compatibleArraysCompareByContentInBothOrders(String name, Object a, Object b) {
    assertEquals(true, new EqualsBuilder().append(a, b).build(), "a, b");
    assertEquals(true, new EqualsBuilder().append(b, a).build(), "b, a");
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("compatibleValues")
  void nullAndArrayCompareUnequalInBothOrders(String name, Object a, Object ignored) {
    assertEquals(false, new EqualsBuilder().append(a, null).build(), "a, null");
    assertEquals(false, new EqualsBuilder().append(null, a).build(), "null, a");
  }
}
