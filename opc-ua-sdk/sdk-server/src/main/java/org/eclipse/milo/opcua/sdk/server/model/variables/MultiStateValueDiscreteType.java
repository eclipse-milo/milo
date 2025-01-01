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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumValueType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.3/#5.3.3.4">https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.3/#5.3.3.4</a>
 */
public interface MultiStateValueDiscreteType extends DiscreteItemType {
  QualifiedProperty<EnumValueType[]> ENUM_VALUES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EnumValues",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7594"),
          1,
          EnumValueType[].class);

  QualifiedProperty<LocalizedText> VALUE_AS_TEXT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ValueAsText",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          -1,
          LocalizedText.class);

  EnumValueType[] getEnumValues();

  void setEnumValues(EnumValueType[] value);

  PropertyType getEnumValuesNode();

  LocalizedText getValueAsText();

  void setValueAsText(LocalizedText value);

  PropertyType getValueAsTextNode();
}
