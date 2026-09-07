/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.gds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.client.gds.GdsClient;
import org.eclipse.milo.opcua.sdk.server.ObjectTypeManager;
import org.eclipse.milo.opcua.sdk.server.gds.model.ObjectTypeInitializer;
import org.eclipse.milo.opcua.sdk.server.gds.model.objects.AccessTokenIssuedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.gds.model.objects.AccessTokenRequestedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.gds.model.objects.ApplicationRegistrationChangedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.gds.model.objects.AuthorizationServiceTypeNode;
import org.eclipse.milo.opcua.sdk.server.gds.model.objects.AuthorizationServicesFolderTypeNode;
import org.eclipse.milo.opcua.sdk.server.gds.model.objects.CertificateDeliveredAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.gds.model.objects.CertificateDirectoryTypeNode;
import org.eclipse.milo.opcua.sdk.server.gds.model.objects.CertificateRequestedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.gds.model.objects.CertificateRevokedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.gds.model.objects.DirectoryTypeNode;
import org.eclipse.milo.opcua.sdk.server.gds.model.objects.KeyCredentialDeliveredAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.gds.model.objects.KeyCredentialManagementFolderTypeNode;
import org.eclipse.milo.opcua.sdk.server.gds.model.objects.KeyCredentialRequestedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.gds.model.objects.KeyCredentialRevokedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.gds.model.objects.KeyCredentialServiceTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.gds.GdsNodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The generated server model registers through a {@link NamespaceTable} because the GDS namespace
 * index is assigned by whichever server hosts it; each registration must land on the table's actual
 * index for the GDS URI, not on the {@code ns=1} the generator wrote.
 */
public class GdsServerModelTest {

  private static final String GDS_URI = GdsClient.NAMESPACE_URI;

  static Stream<Arguments> objectTypes() {
    return Stream.of(
        Arguments.of(GdsNodeIds.DirectoryType, DirectoryTypeNode.class),
        Arguments.of(GdsNodeIds.CertificateDirectoryType, CertificateDirectoryTypeNode.class),
        Arguments.of(
            GdsNodeIds.KeyCredentialManagementFolderType,
            KeyCredentialManagementFolderTypeNode.class),
        Arguments.of(GdsNodeIds.KeyCredentialServiceType, KeyCredentialServiceTypeNode.class),
        Arguments.of(
            GdsNodeIds.AuthorizationServicesFolderType, AuthorizationServicesFolderTypeNode.class),
        Arguments.of(GdsNodeIds.AuthorizationServiceType, AuthorizationServiceTypeNode.class),
        Arguments.of(
            GdsNodeIds.ApplicationRegistrationChangedAuditEventType,
            ApplicationRegistrationChangedAuditEventTypeNode.class),
        Arguments.of(
            GdsNodeIds.CertificateRequestedAuditEventType,
            CertificateRequestedAuditEventTypeNode.class),
        Arguments.of(
            GdsNodeIds.CertificateDeliveredAuditEventType,
            CertificateDeliveredAuditEventTypeNode.class),
        Arguments.of(
            GdsNodeIds.CertificateRevokedAuditEventType,
            CertificateRevokedAuditEventTypeNode.class),
        Arguments.of(
            GdsNodeIds.KeyCredentialRequestedAuditEventType,
            KeyCredentialRequestedAuditEventTypeNode.class),
        Arguments.of(
            GdsNodeIds.KeyCredentialDeliveredAuditEventType,
            KeyCredentialDeliveredAuditEventTypeNode.class),
        Arguments.of(
            GdsNodeIds.KeyCredentialRevokedAuditEventType,
            KeyCredentialRevokedAuditEventTypeNode.class),
        Arguments.of(
            GdsNodeIds.AccessTokenRequestedAuditEventType,
            AccessTokenRequestedAuditEventTypeNode.class),
        Arguments.of(
            GdsNodeIds.AccessTokenIssuedAuditEventType, AccessTokenIssuedAuditEventTypeNode.class));
  }

  @ParameterizedTest(name = "{1}")
  @MethodSource("objectTypes")
  void objectTypeInitializerRegistersEachGdsTypeOnTheTablesIndexForTheGdsUri(
      ExpandedNodeId typeId, Class<? extends UaObjectNode> nodeClass) throws Exception {

    var namespaceTable = new NamespaceTable("urn:eclipse:milo:test:some-other-namespace", GDS_URI);
    var objectTypeManager = new ObjectTypeManager();

    ObjectTypeInitializer.initialize(namespaceTable, objectTypeManager);

    NodeId localTypeId = typeId.toNodeIdOrThrow(namespaceTable);
    assertTrue(localTypeId.getNamespaceIndex().intValue() > 1, "GDS is not at ns=1 here");
    assertEquals(
        nodeClass,
        objectTypeManager
            .getRegisteredType(localTypeId)
            .orElseThrow(() -> new AssertionError("not registered: " + typeId))
            .nodeClass());
  }
}
