/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.xml.args;

import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

@SuppressWarnings("unused")
public class ArrayArguments {

  public static Stream<Arguments> booleanArrayArguments() {
    return Stream.of(
        Arguments.of(
            new Boolean[] {false, true, false, true},
            """
              <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
                <uax:Boolean>false</uax:Boolean>
                <uax:Boolean>true</uax:Boolean>
                <uax:Boolean>false</uax:Boolean>
                <uax:Boolean>true</uax:Boolean>
              </Test>
              """),
        Arguments.of(
            new Boolean[] {},
            """
              <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              </Test>
              """),
        Arguments.of(
            null,
            """
              <Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
              """));
  }
}
