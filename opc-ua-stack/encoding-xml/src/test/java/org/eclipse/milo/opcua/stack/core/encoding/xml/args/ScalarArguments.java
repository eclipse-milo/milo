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

import java.util.UUID;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.junit.jupiter.params.provider.Arguments;

@SuppressWarnings("unused")
public class ScalarArguments {

  public static Stream<Arguments> nodeIdArguments() {
    return Stream.of(
        Arguments.of(
            new NodeId(0, 123),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Identifier>ns=0;i=123</uax:Identifier>
            </Test>
            """),
        Arguments.of(
            new NodeId(1, "Hello"),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Identifier>ns=1;s=Hello</uax:Identifier>
            </Test>
            """),
        Arguments.of(
            new NodeId(2, UUID.fromString("12345678-1234-1234-1234-123456789012")),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Identifier>ns=2;g=12345678-1234-1234-1234-123456789012</uax:Identifier>
            </Test>
            """),
        Arguments.of(
            new NodeId(3, ByteString.of(new byte[] {1, 2, 3, 4})),
            """
            <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
              <uax:Identifier>ns=3;b=AQIDBA==</uax:Identifier>
            </Test>
            """),
        Arguments.of(null, ""));
  }
}
