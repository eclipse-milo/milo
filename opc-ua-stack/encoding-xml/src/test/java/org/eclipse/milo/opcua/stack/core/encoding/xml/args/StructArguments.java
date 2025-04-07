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
              <ua:X>1.0</ua:X>
              <ua:Value>2.0</ua:Value>
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
              <ua:Name>ArgumentName</ua:Name>
              <ua:DataType>
                <uax:Identifier>i=1</uax:Identifier>
              </ua:DataType>
              <ua:ValueRank>-1</ua:ValueRank>
              <ua:ArrayDimensions xsi:nil="true"></ua:ArrayDimensions>
              <ua:Description>
                <uax:Locale>en</uax:Locale>
                <uax:Text>Description</uax:Text>
              </ua:Description>
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
              <ua:ProductUri>urn:example:product</ua:ProductUri>
              <ua:ManufacturerName>Example Manufacturer</ua:ManufacturerName>
              <ua:ProductName>Example Product</ua:ProductName>
              <ua:SoftwareVersion>1.0.0</ua:SoftwareVersion>
              <ua:BuildNumber>12345</ua:BuildNumber>
              <ua:BuildDate>2023-01-01T00:00:00Z</ua:BuildDate>
            </BuildInfo>
            """));
  }
}
