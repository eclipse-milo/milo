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
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceListEntryDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ReferenceDescriptionVariableType extends BaseDataVariableType {
  QualifiedProperty<ReferenceListEntryDataType[]> REFERENCE_REFINEMENT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ReferenceRefinement",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=32660"),
          1,
          ReferenceListEntryDataType[].class);

  /** Gets the existing node's local value. */
  @Nullable ReferenceListEntryDataType @Nullable [] getReferenceRefinement();

  /** Sets the existing node's local value. */
  void setReferenceRefinement(@Nullable ReferenceListEntryDataType @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getReferenceRefinementNode();
}
