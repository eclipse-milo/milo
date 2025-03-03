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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.12">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.12</a>
 */
public interface AuditCertificateEventType extends AuditSecurityEventType {
  QualifiedProperty<ByteString> CERTIFICATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Certificate",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15"),
          -1,
          ByteString.class);

  ByteString getCertificate();

  void setCertificate(ByteString value);

  PropertyType getCertificateNode();
}
