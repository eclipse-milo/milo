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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.3/#5.3.3.3">https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.3/#5.3.3.3</a>
 */
public interface MultiStateDiscreteType extends DiscreteItemType {
  QualifiedProperty<LocalizedText[]> ENUM_STRINGS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EnumStrings",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          1,
          LocalizedText[].class);

  LocalizedText[] getEnumStrings();

  void setEnumStrings(LocalizedText[] value);

  PropertyType getEnumStringsNode();
}
