/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.3">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.3</a>
 */
public interface ConditionVariableType extends BaseDataVariableType {
  QualifiedProperty<DateTime> SOURCE_TIMESTAMP =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SourceTimestamp",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  DateTime getSourceTimestamp();

  void setSourceTimestamp(DateTime value);

  PropertyType getSourceTimestampNode();
}
