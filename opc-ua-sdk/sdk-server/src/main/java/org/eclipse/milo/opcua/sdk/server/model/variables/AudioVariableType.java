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
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.19">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.19</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AudioVariableType extends BaseDataVariableType {
  QualifiedProperty<String> LIST_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ListId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> AGENCY_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AgencyId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> VERSION_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "VersionId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /** Gets the existing node's local value. */
  @Nullable String getListId();

  /** Sets the existing node's local value. */
  void setListId(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getListIdNode();

  /** Gets the existing node's local value. */
  @Nullable String getAgencyId();

  /** Sets the existing node's local value. */
  void setAgencyId(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getAgencyIdNode();

  /** Gets the existing node's local value. */
  @Nullable String getVersionId();

  /** Sets the existing node's local value. */
  void setVersionId(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getVersionIdNode();
}
