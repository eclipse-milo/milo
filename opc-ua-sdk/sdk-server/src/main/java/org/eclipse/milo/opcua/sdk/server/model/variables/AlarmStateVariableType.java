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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/8.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/8.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AlarmStateVariableType extends BaseDataVariableType {
  QualifiedProperty<UShort> HIGHEST_ACTIVE_SEVERITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "HighestActiveSeverity",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<UShort> HIGHEST_UNACK_SEVERITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "HighestUnackSeverity",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<UInteger> ACTIVE_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ActiveCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> UNACKNOWLEDGED_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UnacknowledgedCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> UNCONFIRMED_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UnconfirmedCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<ContentFilter> FILTER =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Filter",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=586"),
          -1,
          ContentFilter.class);

  /** Gets the existing node's local value. */
  @Nullable UShort getHighestActiveSeverity();

  /** Sets the existing node's local value. */
  void setHighestActiveSeverity(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getHighestActiveSeverityNode();

  /** Gets the existing node's local value. */
  @Nullable UShort getHighestUnackSeverity();

  /** Sets the existing node's local value. */
  void setHighestUnackSeverity(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getHighestUnackSeverityNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getActiveCount();

  /** Sets the existing node's local value. */
  void setActiveCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getActiveCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getUnacknowledgedCount();

  /** Sets the existing node's local value. */
  void setUnacknowledgedCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getUnacknowledgedCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getUnconfirmedCount();

  /** Sets the existing node's local value. */
  void setUnconfirmedCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getUnconfirmedCountNode();

  /** Gets the existing node's local value. */
  @Nullable ContentFilter getFilter();

  /** Sets the existing node's local value. */
  void setFilter(@Nullable ContentFilter value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getFilterNode();
}
