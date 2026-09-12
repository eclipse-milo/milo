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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.3">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.3</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
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

  QualifiedProperty<String> CLIENT_APPLICATION_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ClientApplicationUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /** Gets the existing node's local value. */
  @Nullable DateTime getActionTimeStamp();

  /** Sets the existing node's local value. */
  void setActionTimeStamp(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getActionTimeStampNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getStatus();

  /** Sets the existing node's local value. */
  void setStatus(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getStatusNode();

  /** Gets the existing node's local value. */
  @Nullable String getServerId();

  /** Sets the existing node's local value. */
  void setServerId(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getServerIdNode();

  /** Gets the existing node's local value. */
  @Nullable String getClientAuditEntryId();

  /** Sets the existing node's local value. */
  void setClientAuditEntryId(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getClientAuditEntryIdNode();

  /** Gets the existing node's local value. */
  @Nullable String getClientUserId();

  /** Sets the existing node's local value. */
  void setClientUserId(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getClientUserIdNode();

  /** Gets the existing node's local value. */
  @Nullable String getClientApplicationUri();

  /** Sets the existing node's local value. */
  void setClientApplicationUri(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getClientApplicationUriNode();
}
