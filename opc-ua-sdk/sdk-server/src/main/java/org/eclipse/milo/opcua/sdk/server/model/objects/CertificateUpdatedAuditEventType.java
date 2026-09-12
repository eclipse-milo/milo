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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.27">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.27</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface CertificateUpdatedAuditEventType extends AuditUpdateMethodEventType {
  QualifiedProperty<NodeId> CERTIFICATE_GROUP =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CertificateGroup",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<NodeId> CERTIFICATE_TYPE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CertificateType",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  /** Gets the existing node's local value. */
  @Nullable NodeId getCertificateGroup();

  /** Sets the existing node's local value. */
  void setCertificateGroup(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getCertificateGroupNode();

  /** Gets the existing node's local value. */
  @Nullable NodeId getCertificateType();

  /** Sets the existing node's local value. */
  void setCertificateType(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getCertificateTypeNode();
}
