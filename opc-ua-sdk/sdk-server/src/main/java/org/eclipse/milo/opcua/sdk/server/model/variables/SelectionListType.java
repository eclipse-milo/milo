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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.18">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.18</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
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

  /** Gets the existing node's local value. */
  @Nullable Object @Nullable [] getSelections();

  /** Sets the existing node's local value. */
  void setSelections(@Nullable Object @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSelectionsNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText @Nullable [] getSelectionDescriptions();

  /** Sets the existing node's local value. */
  void setSelectionDescriptions(@Nullable LocalizedText @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSelectionDescriptionsNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getRestrictToList();

  /** Sets the existing node's local value. */
  void setRestrictToList(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getRestrictToListNode();
}
