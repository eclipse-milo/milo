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
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.26">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.26</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AuditHistoryUpdateEventType extends AuditUpdateEventType {
  QualifiedProperty<NodeId> PARAMETER_DATA_TYPE_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ParameterDataTypeId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  /** Gets the existing node's local value. */
  @Nullable NodeId getParameterDataTypeId();

  /** Sets the existing node's local value. */
  void setParameterDataTypeId(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getParameterDataTypeIdNode();
}
