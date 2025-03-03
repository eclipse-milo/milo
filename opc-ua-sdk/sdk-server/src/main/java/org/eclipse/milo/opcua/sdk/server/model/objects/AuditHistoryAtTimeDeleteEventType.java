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
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.7">https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.7</a>
 */
public interface AuditHistoryAtTimeDeleteEventType extends AuditHistoryDeleteEventType {
  QualifiedProperty<DateTime[]> REQ_TIMES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ReqTimes",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          1,
          DateTime[].class);

  QualifiedProperty<DataValue[]> OLD_VALUES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OldValues",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23"),
          1,
          DataValue[].class);

  DateTime[] getReqTimes();

  void setReqTimes(DateTime[] value);

  PropertyType getReqTimesNode();

  DataValue[] getOldValues();

  void setOldValues(DataValue[] value);

  PropertyType getOldValuesNode();
}
