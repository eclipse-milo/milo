/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.13">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.13</a>
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

  String getInvalidHostname();

  void setInvalidHostname(String value);

  PropertyType getInvalidHostnameNode();

  String getInvalidUri();

  void setInvalidUri(String value);

  PropertyType getInvalidUriNode();
}
