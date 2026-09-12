/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.AxisInformation;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.4/#5.3.4.6">https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.4/#5.3.4.6</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface NDimensionArrayItemType extends ArrayItemType {
  QualifiedProperty<AxisInformation[]> AXIS_DEFINITION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AxisDefinition",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12079"),
          1,
          AxisInformation[].class);

  /** Gets the existing node's local value. */
  @Nullable AxisInformation @Nullable [] getAxisDefinition();

  /** Sets the existing node's local value. */
  void setAxisDefinition(@Nullable AxisInformation @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getAxisDefinitionNode();
}
