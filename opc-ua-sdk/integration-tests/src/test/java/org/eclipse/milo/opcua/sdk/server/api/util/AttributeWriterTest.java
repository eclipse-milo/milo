/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.api.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AttributeWriterTest extends AbstractClientServerTest {

  @Test
  void writeNullAllowed() throws Exception {
    StatusCode statusCode =
        client
            .writeValues(
                List.of(new NodeId(2, "AllowNulls")),
                List.of(DataValue.valueOnly(Variant.NULL_VALUE)))
            .get(0);

    assertEquals(StatusCode.GOOD, statusCode);
  }

  @Test
  void writeNullDisallowed() throws Exception {
    StatusCode statusCode =
        client
            .writeValues(
                List.of(new NodeId(2, "DisallowNulls")),
                List.of(DataValue.valueOnly(Variant.NULL_VALUE)))
            .get(0);

    assertEquals(new StatusCode(StatusCodes.Bad_TypeMismatch), statusCode);
  }

  @Test
  void writeNullNotConfigured() throws Exception {
    // Default behavior when AllowNulls property is not configured is to reject null values.
    StatusCode statusCode =
        client
            .writeValues(
                List.of(new NodeId(2, "AllowNullsNotConfigured")),
                List.of(DataValue.valueOnly(Variant.NULL_VALUE)))
            .get(0);

    assertEquals(new StatusCode(StatusCodes.Bad_TypeMismatch), statusCode);
  }

  @Test
  void writeByteStringToUByteArray() throws Exception {
    StatusCode statusCode =
        client
            .writeValues(
                List.of(new NodeId(2, "UByteArray")),
                List.of(DataValue.valueOnly(new Variant(ByteString.of(new byte[] {1, 2, 3})))))
            .get(0);

    assertEquals(StatusCode.GOOD, statusCode);
  }

  @BeforeAll
  void configure() {
    testNamespace.configure(
        (context, nodeManager) -> {
          UaVariableNode allowNulls =
              UaVariableNode.build(
                  context,
                  b -> {
                    b.setNodeId(new NodeId(2, "AllowNulls"));
                    b.setBrowseName(new QualifiedName(2, "AllowNulls"));
                    b.setDisplayName(LocalizedText.english("AllowNulls"));
                    b.setDataType(NodeIds.String);
                    b.setAccessLevel(AccessLevel.READ_WRITE);
                    b.setUserAccessLevel(AccessLevel.READ_WRITE);
                    return b.buildAndAdd();
                  });

          allowNulls.setAllowNulls(true);

          UaVariableNode disallowNulls =
              UaVariableNode.build(
                  context,
                  b -> {
                    b.setNodeId(new NodeId(2, "DisallowNulls"));
                    b.setBrowseName(new QualifiedName(2, "DisallowNulls"));
                    b.setDisplayName(LocalizedText.english("DisallowNulls"));
                    b.setDataType(NodeIds.String);
                    b.setAccessLevel(AccessLevel.READ_WRITE);
                    b.setUserAccessLevel(AccessLevel.READ_WRITE);
                    return b.buildAndAdd();
                  });

          disallowNulls.setAllowNulls(false);

          UaVariableNode.build(
              context,
              b -> {
                b.setNodeId(new NodeId(2, "AllowNullsNotConfigured"));
                b.setBrowseName(new QualifiedName(2, "AllowNullsNotConfigured"));
                b.setDisplayName(LocalizedText.english("AllowNullsNotConfigured"));
                b.setDataType(NodeIds.String);
                b.setAccessLevel(AccessLevel.READ_WRITE);
                b.setUserAccessLevel(AccessLevel.READ_WRITE);
                return b.buildAndAdd();
              });

          UaVariableNode.build(
              context,
              b -> {
                b.setNodeId(new NodeId(2, "UByteArray"));
                b.setBrowseName(new QualifiedName(2, "UByteArray"));
                b.setDisplayName(LocalizedText.english("UByteArray"));
                b.setDataType(NodeIds.Byte);
                b.setArrayDimensions(new UInteger[] {UInteger.valueOf(0)});
                b.setAccessLevel(AccessLevel.READ_WRITE);
                b.setUserAccessLevel(AccessLevel.READ_WRITE);
                return b.buildAndAdd();
              });
        });
  }
}
