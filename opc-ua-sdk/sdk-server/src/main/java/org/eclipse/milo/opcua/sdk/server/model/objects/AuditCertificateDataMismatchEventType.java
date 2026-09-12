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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.13">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.13</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AuditCertificateDataMismatchEventType extends AuditCertificateEventType {
  QualifiedProperty<String> INVALID_HOSTNAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InvalidHostname",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> INVALID_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InvalidUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /** Gets the existing node's local value. */
  @Nullable String getInvalidHostname();

  /** Sets the existing node's local value. */
  void setInvalidHostname(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getInvalidHostnameNode();

  /** Gets the existing node's local value. */
  @Nullable String getInvalidUri();

  /** Sets the existing node's local value. */
  void setInvalidUri(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getInvalidUriNode();
}
