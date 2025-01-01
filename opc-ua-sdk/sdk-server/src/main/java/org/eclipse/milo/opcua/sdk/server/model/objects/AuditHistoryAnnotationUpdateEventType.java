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
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.6.4">https://reference.opcfoundation.org/v105/Core/docs/Part11/5.6.4</a>
 */
public interface AuditHistoryAnnotationUpdateEventType extends AuditHistoryUpdateEventType {
  QualifiedProperty<PerformUpdateType> PERFORM_INSERT_REPLACE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PerformInsertReplace",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11293"),
          -1,
          PerformUpdateType.class);

  QualifiedProperty<DataValue[]> NEW_VALUES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NewValues",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23"),
          1,
          DataValue[].class);

  QualifiedProperty<DataValue[]> OLD_VALUES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OldValues",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23"),
          1,
          DataValue[].class);

  PerformUpdateType getPerformInsertReplace();

  void setPerformInsertReplace(PerformUpdateType value);

  PropertyType getPerformInsertReplaceNode();

  DataValue[] getNewValues();

  void setNewValues(DataValue[] value);

  PropertyType getNewValuesNode();

  DataValue[] getOldValues();

  void setOldValues(DataValue[] value);

  PropertyType getOldValuesNode();
}
