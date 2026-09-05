/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.gds.model;

import org.eclipse.milo.opcua.sdk.client.ObjectTypeManager;
import org.eclipse.milo.opcua.sdk.client.gds.model.objects.AccessTokenIssuedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.gds.model.objects.AccessTokenRequestedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.gds.model.objects.ApplicationRegistrationChangedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.gds.model.objects.AuthorizationServiceTypeNode;
import org.eclipse.milo.opcua.sdk.client.gds.model.objects.AuthorizationServicesFolderTypeNode;
import org.eclipse.milo.opcua.sdk.client.gds.model.objects.CertificateDeliveredAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.gds.model.objects.CertificateDirectoryTypeNode;
import org.eclipse.milo.opcua.sdk.client.gds.model.objects.CertificateRequestedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.gds.model.objects.CertificateRevokedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.gds.model.objects.DirectoryTypeNode;
import org.eclipse.milo.opcua.sdk.client.gds.model.objects.KeyCredentialDeliveredAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.gds.model.objects.KeyCredentialManagementFolderTypeNode;
import org.eclipse.milo.opcua.sdk.client.gds.model.objects.KeyCredentialRequestedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.gds.model.objects.KeyCredentialRevokedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.gds.model.objects.KeyCredentialServiceTypeNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

public class ObjectTypeInitializer {
  public static void initialize(
      NamespaceTable namespaceTable, ObjectTypeManager objectTypeManager) {
    objectTypeManager.registerObjectType(
        NodeId.parse("ns=1;i=13").reindex(namespaceTable, "http://opcfoundation.org/UA/GDS/"),
        DirectoryTypeNode.class,
        DirectoryTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("ns=1;i=63").reindex(namespaceTable, "http://opcfoundation.org/UA/GDS/"),
        CertificateDirectoryTypeNode.class,
        CertificateDirectoryTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("ns=1;i=55").reindex(namespaceTable, "http://opcfoundation.org/UA/GDS/"),
        KeyCredentialManagementFolderTypeNode.class,
        KeyCredentialManagementFolderTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("ns=1;i=233").reindex(namespaceTable, "http://opcfoundation.org/UA/GDS/"),
        AuthorizationServicesFolderTypeNode.class,
        AuthorizationServicesFolderTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("ns=1;i=1039").reindex(namespaceTable, "http://opcfoundation.org/UA/GDS/"),
        KeyCredentialRequestedAuditEventTypeNode.class,
        KeyCredentialRequestedAuditEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("ns=1;i=1057").reindex(namespaceTable, "http://opcfoundation.org/UA/GDS/"),
        KeyCredentialDeliveredAuditEventTypeNode.class,
        KeyCredentialDeliveredAuditEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("ns=1;i=1075").reindex(namespaceTable, "http://opcfoundation.org/UA/GDS/"),
        KeyCredentialRevokedAuditEventTypeNode.class,
        KeyCredentialRevokedAuditEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("ns=1;i=26").reindex(namespaceTable, "http://opcfoundation.org/UA/GDS/"),
        ApplicationRegistrationChangedAuditEventTypeNode.class,
        ApplicationRegistrationChangedAuditEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("ns=1;i=91").reindex(namespaceTable, "http://opcfoundation.org/UA/GDS/"),
        CertificateRequestedAuditEventTypeNode.class,
        CertificateRequestedAuditEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("ns=1;i=109").reindex(namespaceTable, "http://opcfoundation.org/UA/GDS/"),
        CertificateDeliveredAuditEventTypeNode.class,
        CertificateDeliveredAuditEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("ns=1;i=27").reindex(namespaceTable, "http://opcfoundation.org/UA/GDS/"),
        CertificateRevokedAuditEventTypeNode.class,
        CertificateRevokedAuditEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("ns=1;i=111").reindex(namespaceTable, "http://opcfoundation.org/UA/GDS/"),
        AccessTokenRequestedAuditEventTypeNode.class,
        AccessTokenRequestedAuditEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("ns=1;i=975").reindex(namespaceTable, "http://opcfoundation.org/UA/GDS/"),
        AccessTokenIssuedAuditEventTypeNode.class,
        AccessTokenIssuedAuditEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("ns=1;i=1020").reindex(namespaceTable, "http://opcfoundation.org/UA/GDS/"),
        KeyCredentialServiceTypeNode.class,
        KeyCredentialServiceTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("ns=1;i=966").reindex(namespaceTable, "http://opcfoundation.org/UA/GDS/"),
        AuthorizationServiceTypeNode.class,
        AuthorizationServiceTypeNode::new);
  }
}
