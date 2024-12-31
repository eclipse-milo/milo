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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.18">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.18</a>
 */
public interface SelectionListType extends BaseDataVariableType {
  QualifiedProperty<Object[]> SELECTIONS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Selections",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24"),
          1,
          Object[].class);

  QualifiedProperty<LocalizedText[]> SELECTION_DESCRIPTIONS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SelectionDescriptions",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          1,
          LocalizedText[].class);

  QualifiedProperty<Boolean> RESTRICT_TO_LIST =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "RestrictToList",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  Object[] getSelections();

  void setSelections(Object[] value);

  PropertyType getSelectionsNode();

  LocalizedText[] getSelectionDescriptions();

  void setSelectionDescriptions(LocalizedText[] value);

  PropertyType getSelectionDescriptionsNode();

  Boolean getRestrictToList();

  void setRestrictToList(Boolean value);

  PropertyType getRestrictToListNode();
}
