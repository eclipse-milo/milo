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
import org.jspecify.annotations.Nullable;

/**
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface DataTypeDictionaryType extends BaseDataVariableType {
  QualifiedProperty<String> DATA_TYPE_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataTypeVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> NAMESPACE_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NamespaceUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<Boolean> DEPRECATED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Deprecated",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  /** Gets the existing node's local value. */
  @Nullable String getDataTypeVersionProperty();

  /** Sets the existing node's local value. */
  void setDataTypeVersionProperty(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDataTypeVersionNode();

  /** Gets the existing node's local value. */
  @Nullable String getNamespaceUri();

  /** Sets the existing node's local value. */
  void setNamespaceUri(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getNamespaceUriNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getDeprecated();

  /** Sets the existing node's local value. */
  void setDeprecated(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDeprecatedNode();
}
