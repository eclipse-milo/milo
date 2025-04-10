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

import java.time.Instant;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.params.provider.Arguments;

@SuppressWarnings("unused")
public class StructArguments {

  public static Stream<Arguments> structArguments() {
    return Stream.of(
        // XVType
        Arguments.of(
            "XVType",
            new XVType(1.0, 2.0f),
            """
            <XVType xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:ua="http://opcfoundation.org/UA/">
              <uax:X>1.0</uax:X>
              <uax:Value>2.0</uax:Value>
            </XVType>
            """),

        // Argument
        Arguments.of(
            "Argument",
            new Argument(
                "ArgumentName",
                NodeId.parse("ns=0;i=1"),
                -1,
                null,
                LocalizedText.english("Description")),
            """
            <Argument xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:ua="http://opcfoundation.org/UA/">
              <uax:Name>ArgumentName</uax:Name>
              <uax:DataType>
                <uax:Identifier>i=1</uax:Identifier>
              </uax:DataType>
              <uax:ValueRank>-1</uax:ValueRank>
              <uax:ArrayDimensions xsi:nil="true"></uax:ArrayDimensions>
              <uax:Description>
                <uax:Locale>en</uax:Locale>
                <uax:Text>Description</uax:Text>
              </uax:Description>
            </Argument>
            """),

        // BuildInfo
        Arguments.of(
            "BuildInfo",
            new BuildInfo(
                "urn:example:product",
                "Example Manufacturer",
                "Example Product",
                "1.0.0",
                "12345",
                new DateTime(Instant.parse("2023-01-01T00:00:00Z"))),
            """
            <BuildInfo xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:ua="http://opcfoundation.org/UA/">
              <uax:ProductUri>urn:example:product</uax:ProductUri>
              <uax:ManufacturerName>Example Manufacturer</uax:ManufacturerName>
              <uax:ProductName>Example Product</uax:ProductName>
              <uax:SoftwareVersion>1.0.0</uax:SoftwareVersion>
              <uax:BuildNumber>12345</uax:BuildNumber>
              <uax:BuildDate>2023-01-01T00:00:00Z</uax:BuildDate>
            </BuildInfo>
            """));
  }
}
