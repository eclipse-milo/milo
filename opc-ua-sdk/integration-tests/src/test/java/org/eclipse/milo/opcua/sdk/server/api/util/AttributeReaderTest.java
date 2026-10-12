/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AttributeReaderTest extends AbstractClientServerTest {

  private static final NodeId NULL_MATRIX = new NodeId(2, "AttributeReaderTest.NullMatrix");

  // Part 4 §7.38.2: Bad_IndexRangeNoData is the result when no data exists within the range, and a
  // null Matrix stored by server code holds no data.
  @Test
  void indexRangeReadOfStoredNullMatrixReturnsIndexRangeNoData() throws Exception {
    DataValue value =
        client
            .readAsync(
                0.0,
                TimestampsToReturn.Neither,
                List.of(
                    new ReadValueId(
                        NULL_MATRIX, AttributeId.Value.uid(), "0:1,0:1", QualifiedName.NULL_VALUE)))
            .get(5, TimeUnit.SECONDS)
            .getResults()[0];

    assertEquals(new StatusCode(StatusCodes.Bad_IndexRangeNoData), value.statusCode());
  }

  @BeforeAll
  void configure() {
    testNamespace.configure(
        (context, nodeManager) -> {
          UaVariableNode.build(
              context,
              b -> {
                b.setNodeId(NULL_MATRIX);
                b.setBrowseName(new QualifiedName(2, "NullMatrix"));
                b.setDisplayName(LocalizedText.english("NullMatrix"));
                b.setDataType(NodeIds.Int32);
                b.setValueRank(2);
                b.setAccessLevel(AccessLevel.READ_ONLY);
                b.setUserAccessLevel(AccessLevel.READ_ONLY);
                b.setValue(new DataValue(new Variant(Matrix.ofNull())));
                return b.buildAndAdd();
              });
        });
  }
}
