/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.11">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.11</a>
 */
public interface TrustListUpdatedAuditEventType extends AuditEventType {
  QualifiedProperty<NodeId> TRUST_LIST_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "TrustListId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  NodeId getTrustListId();

  void setTrustListId(NodeId value);

  PropertyType getTrustListIdNode();
}
