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
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceListEntryDataType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part23/5.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part23/5.3.1</a>
 */
public interface ReferenceDescriptionVariableType extends BaseDataVariableType {
  QualifiedProperty<ReferenceListEntryDataType[]> REFERENCE_REFINEMENT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ReferenceRefinement",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=32660"),
          1,
          ReferenceListEntryDataType[].class);

  ReferenceListEntryDataType[] getReferenceRefinement();

  void setReferenceRefinement(ReferenceListEntryDataType[] value);

  PropertyType getReferenceRefinementNode();
}
