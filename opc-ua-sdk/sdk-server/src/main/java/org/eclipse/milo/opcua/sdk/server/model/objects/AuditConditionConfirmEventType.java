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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.10.7">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.10.7</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AuditConditionConfirmEventType extends AuditConditionEventType {
  QualifiedProperty<ByteString> CONDITION_EVENT_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConditionEventId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15"),
          -1,
          ByteString.class);

  QualifiedProperty<LocalizedText> COMMENT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Comment",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          -1,
          LocalizedText.class);

  /** Gets the existing node's local value. */
  @Nullable ByteString getConditionEventId();

  /** Sets the existing node's local value. */
  void setConditionEventId(@Nullable ByteString value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getConditionEventIdNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getComment();

  /** Sets the existing node's local value. */
  void setComment(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getCommentNode();
}
