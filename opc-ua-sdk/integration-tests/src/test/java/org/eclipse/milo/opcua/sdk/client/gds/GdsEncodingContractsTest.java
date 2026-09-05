/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.milo.opcua.sdk.client.gds;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.gds.model.objects.AuthorizationServiceType;
import org.eclipse.milo.opcua.sdk.client.gds.model.objects.AuthorizationServiceTypeNode;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.junit.jupiter.api.Test;

class GdsEncodingContractsTest extends AbstractGdsClientTest {

  // A successful Write service can carry Bad_UserAccessDenied for its one operation. Generated
  // blocking
  // writers promise UaException for that outcome while async writers expose the exact StatusCode.
  @Test
  void generatedBlockingWriterReportsOperationFailureAndStillAcceptsGoodWrites() throws Exception {
    NodeId id = newNodeId("ReadOnlyAuthorizationService");
    UaVariableNode[] property = new UaVariableNode[1];
    testNamespace.configure(
        (context, nodes) -> {
          UaObjectNode object =
              UaObjectNode.builder(context)
                  .setNodeId(id)
                  .setBrowseName(newQualifiedName("AuthorizationService"))
                  .setDisplayName(LocalizedText.english("AuthorizationService"))
                  .build();
          nodes.addNode(object);
          object.setProperty(AuthorizationServiceType.SERVICE_URI, "urn:original");
          property[0] =
              (UaVariableNode)
                  object.getPropertyNode(AuthorizationServiceType.SERVICE_URI).orElseThrow();
          property[0].setAccessLevel(AccessLevel.toValue(AccessLevel.READ_ONLY));
          property[0].setUserAccessLevel(AccessLevel.toValue(AccessLevel.READ_ONLY));
        });
    var node =
        new AuthorizationServiceTypeNode(
            client,
            id,
            NodeClass.Object,
            newQualifiedName("AuthorizationService"),
            LocalizedText.english("AuthorizationService"),
            LocalizedText.NULL_VALUE,
            uint(0),
            uint(0),
            null,
            null,
            null,
            ubyte(0));
    assertEquals("urn:original", node.readServiceUri());
    assertEquals(
        StatusCodes.Bad_UserAccessDenied,
        node.writeServiceUriAsync("urn:rejected").get(10, TimeUnit.SECONDS).value());
    UaException error = assertThrows(UaException.class, () -> node.writeServiceUri("urn:rejected"));
    assertEquals(StatusCodes.Bad_UserAccessDenied, error.getStatusCode().value());
    assertEquals("urn:original", node.readServiceUri());
    property[0].setAccessLevel(AccessLevel.toValue(AccessLevel.READ_WRITE));
    property[0].setUserAccessLevel(AccessLevel.toValue(AccessLevel.READ_WRITE));
    node.writeServiceUri("urn:accepted");
    assertEquals("urn:accepted", node.readServiceUri());
  }
}
