/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;
import org.eclipse.milo.opcua.stack.core.types.structured.UserIdentityToken;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.10">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.10</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AuditActivateSessionEventType extends AuditSessionEventType {
  QualifiedProperty<SignedSoftwareCertificate[]> CLIENT_SOFTWARE_CERTIFICATES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ClientSoftwareCertificates",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=344"),
          1,
          SignedSoftwareCertificate[].class);

  QualifiedProperty<UserIdentityToken> USER_IDENTITY_TOKEN =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UserIdentityToken",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=316"),
          -1,
          UserIdentityToken.class);

  QualifiedProperty<String> SECURE_CHANNEL_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecureChannelId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<NodeId[]> CURRENT_ROLE_IDS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CurrentRoleIds",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          1,
          NodeId[].class);

  /** Gets the existing node's local value. */
  @Nullable SignedSoftwareCertificate @Nullable [] getClientSoftwareCertificates();

  /** Sets the existing node's local value. */
  void setClientSoftwareCertificates(@Nullable SignedSoftwareCertificate @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getClientSoftwareCertificatesNode();

  /** Gets the existing node's local value. */
  @Nullable UserIdentityToken getUserIdentityToken();

  /** Sets the existing node's local value. */
  void setUserIdentityToken(@Nullable UserIdentityToken value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getUserIdentityTokenNode();

  /** Gets the existing node's local value. */
  @Nullable String getSecureChannelId();

  /** Sets the existing node's local value. */
  void setSecureChannelId(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSecureChannelIdNode();

  /** Gets the existing node's local value. */
  @Nullable NodeId @Nullable [] getCurrentRoleIds();

  /** Sets the existing node's local value. */
  void setCurrentRoleIds(@Nullable NodeId @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getCurrentRoleIdsNode();
}
