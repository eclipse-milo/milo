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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.11">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.11</a>
 */
public interface AuditCancelEventType extends AuditSessionEventType {
  QualifiedProperty<UInteger> REQUEST_HANDLE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "RequestHandle",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  UInteger getRequestHandle();

  void setRequestHandle(UInteger value);

  PropertyType getRequestHandleNode();
}
