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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.25">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.25</a>
 */
public interface AuditWriteUpdateEventType extends AuditUpdateEventType {
  QualifiedProperty<UInteger> ATTRIBUTE_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AttributeId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<String> INDEX_RANGE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "IndexRange",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=291"),
          -1,
          String.class);

  QualifiedProperty<Object> OLD_VALUE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OldValue",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24"),
          -1,
          Object.class);

  QualifiedProperty<Object> NEW_VALUE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NewValue",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24"),
          -1,
          Object.class);

  UInteger getAttributeId();

  void setAttributeId(UInteger value);

  PropertyType getAttributeIdNode();

  String getIndexRange();

  void setIndexRange(String value);

  PropertyType getIndexRangeNode();

  Object getOldValue();

  void setOldValue(Object value);

  PropertyType getOldValueNode();

  Object getNewValue();

  void setNewValue(Object value);

  PropertyType getNewValueNode();
}
