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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.3">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.3</a>
 */
public interface AuditEventType extends BaseEventType {
  QualifiedProperty<DateTime> ACTION_TIME_STAMP =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ActionTimeStamp",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<Boolean> STATUS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Status",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<String> SERVER_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ServerId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> CLIENT_AUDIT_ENTRY_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ClientAuditEntryId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> CLIENT_USER_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ClientUserId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  DateTime getActionTimeStamp();

  void setActionTimeStamp(DateTime value);

  PropertyType getActionTimeStampNode();

  Boolean getStatus();

  void setStatus(Boolean value);

  PropertyType getStatusNode();

  String getServerId();

  void setServerId(String value);

  PropertyType getServerIdNode();

  String getClientAuditEntryId();

  void setClientAuditEntryId(String value);

  PropertyType getClientAuditEntryIdNode();

  String getClientUserId();

  void setClientUserId(String value);

  PropertyType getClientUserIdNode();
}
