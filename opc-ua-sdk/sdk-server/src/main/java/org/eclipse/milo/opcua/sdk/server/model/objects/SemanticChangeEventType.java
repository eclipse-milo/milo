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
import org.eclipse.milo.opcua.stack.core.types.structured.SemanticChangeStructureDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.33">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.33</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface SemanticChangeEventType extends BaseEventType {
  QualifiedProperty<SemanticChangeStructureDataType[]> CHANGES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Changes",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=897"),
          1,
          SemanticChangeStructureDataType[].class);

  /** Gets the existing node's local value. */
  @Nullable SemanticChangeStructureDataType @Nullable [] getChanges();

  /** Sets the existing node's local value. */
  void setChanges(@Nullable SemanticChangeStructureDataType @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getChangesNode();
}
