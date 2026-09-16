/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.test.aliases;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.model.objects.AliasNameCategoryType;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.jspecify.annotations.Nullable;

/**
 * Static helpers shared by the alias integration tests: client-side attribute reads and server-side
 * {@code LastChange} Property access and assertions.
 */
final class AliasTestSupport {

  private AliasTestSupport() {}

  /** Read a single attribute of a Node through the client. */
  static DataValue readAttribute(OpcUaClient client, NodeId nodeId, AttributeId attributeId)
      throws UaException {
    ReadResponse response =
        client.read(
            0.0,
            TimestampsToReturn.Neither,
            List.of(new ReadValueId(nodeId, attributeId.uid(), null, QualifiedName.NULL_VALUE)));

    return requireNonNull(response.getResults())[0];
  }

  /** Read the category's LastChange Property, asserting that it has one with a non-null value. */
  static UInteger requireLastChange(OpcUaServer server, NodeId categoryId) {
    UInteger value = readLastChange(server, categoryId);
    assertNotNull(value, () -> "no LastChange value on " + categoryId.toParseableString());
    return value;
  }

  static void assertStrictlyGreater(UInteger before, @Nullable UInteger after) {
    assertNotNull(after);
    assertTrue(
        after.longValue() > before.longValue(),
        () -> "expected LastChange > " + before + " but was " + after);
  }

  /**
   * The current value of the category's {@code LastChange} Property Node, or null if the category
   * has no such Property or it has no value yet.
   */
  static @Nullable UInteger readLastChange(OpcUaServer server, NodeId categoryId) {
    return server
        .getAddressSpaceManager()
        .getManagedNode(categoryId)
        .flatMap(categoryNode -> categoryNode.getPropertyNode(AliasNameCategoryType.LAST_CHANGE))
        .map(propertyNode -> (UInteger) propertyNode.getValue().value().value())
        .orElse(null);
  }
}
