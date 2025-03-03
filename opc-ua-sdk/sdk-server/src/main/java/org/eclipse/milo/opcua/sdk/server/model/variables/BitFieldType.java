/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.structured.BitFieldDefinition;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.29">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.29</a>
 */
public interface BitFieldType extends BaseDataVariableType {
  QualifiedProperty<BitFieldDefinition[]> BIT_FIELDS_DEFINITIONS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "BitFieldsDefinitions",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=32421"),
          1,
          BitFieldDefinition[].class);

  BitFieldDefinition[] getBitFieldsDefinitions();

  void setBitFieldsDefinitions(BitFieldDefinition[] value);

  PropertyType getBitFieldsDefinitionsNode();
}
