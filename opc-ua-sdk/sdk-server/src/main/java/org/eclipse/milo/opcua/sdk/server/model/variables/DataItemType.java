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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.1</a>
 */
public interface DataItemType extends BaseDataVariableType {
  QualifiedProperty<String> DEFINITION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Definition",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<Double> VALUE_PRECISION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ValuePrecision",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  String getDefinition();

  void setDefinition(String value);

  PropertyType getDefinitionNode();

  Double getValuePrecision();

  void setValuePrecision(Double value);

  PropertyType getValuePrecisionNode();
}
