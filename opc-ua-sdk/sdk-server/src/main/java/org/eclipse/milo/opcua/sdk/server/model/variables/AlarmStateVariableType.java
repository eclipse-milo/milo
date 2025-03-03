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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/8.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/8.2</a>
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

  UShort getHighestActiveSeverity();

  void setHighestActiveSeverity(UShort value);

  PropertyType getHighestActiveSeverityNode();

  UShort getHighestUnackSeverity();

  void setHighestUnackSeverity(UShort value);

  PropertyType getHighestUnackSeverityNode();

  UInteger getActiveCount();

  void setActiveCount(UInteger value);

  PropertyType getActiveCountNode();

  UInteger getUnacknowledgedCount();

  void setUnacknowledgedCount(UInteger value);

  PropertyType getUnacknowledgedCountNode();

  UInteger getUnconfirmedCount();

  void setUnconfirmedCount(UInteger value);

  PropertyType getUnconfirmedCountNode();

  ContentFilter getFilter();

  void setFilter(ContentFilter value);

  PropertyType getFilterNode();
}
