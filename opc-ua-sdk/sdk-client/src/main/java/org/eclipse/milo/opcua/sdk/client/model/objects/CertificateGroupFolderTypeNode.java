/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import com.digitalpetri.opcua.uanodeset.runtime.client.ClientMembers;
import com.digitalpetri.opcua.uanodeset.runtime.client.ClientViews;
import com.digitalpetri.opcua.uanodeset.runtime.members.MemberDeclaration;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

public class CertificateGroupFolderTypeNode extends FolderTypeNode
    implements CertificateGroupFolderType {
  public CertificateGroupFolderTypeNode(
      OpcUaClient client,
      NodeId nodeId,
      NodeClass nodeClass,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions,
      UByte eventNotifier) {
    super(
        client,
        nodeId,
        nodeClass,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions,
        eventNotifier);
  }

  /**
   * Creates an independently owned view context retaining this exact existing node and its client.
   * Cached attributes remain shared even after SDK cache eviction. Close the returned context when
   * its views are no longer needed.
   */
  public static ClientViews createViews(CertificateGroupFolderTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public CertificateGroupTypeNode getDefaultApplicationGroupNode() throws UaException {
    return ClientMembers.await(getDefaultApplicationGroupNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends CertificateGroupTypeNode>
      getDefaultApplicationGroupNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        CertificateGroupTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DefaultApplicationGroup",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:DefaultApplicationGroup (declaration i=13814, owner"
                + " i=13813)"));
  }

  @Override
  public @Nullable CertificateGroupTypeNode getDefaultHttpsGroupNode() throws UaException {
    return ClientMembers.await(getDefaultHttpsGroupNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable CertificateGroupTypeNode>
      getDefaultHttpsGroupNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        CertificateGroupTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DefaultHttpsGroup",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            true,
            "http://opcfoundation.org/UA/:DefaultHttpsGroup (declaration i=13848, owner i=13813)"));
  }

  @Override
  public @Nullable CertificateGroupTypeNode getDefaultUserTokenGroupNode() throws UaException {
    return ClientMembers.await(getDefaultUserTokenGroupNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable CertificateGroupTypeNode>
      getDefaultUserTokenGroupNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        CertificateGroupTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DefaultUserTokenGroup",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            true,
            "http://opcfoundation.org/UA/:DefaultUserTokenGroup (declaration i=13882, owner"
                + " i=13813)"));
  }
}
