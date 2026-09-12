/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.11">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.11</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface TrustListOutOfDateAlarmType extends SystemOffNormalAlarmType {
  QualifiedProperty<NodeId> TRUST_LIST_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "TrustListId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<DateTime> LAST_UPDATE_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastUpdateTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<Double> UPDATE_FREQUENCY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UpdateFrequency",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  /** Gets the existing node's local value. */
  @Nullable NodeId getTrustListId();

  /** Sets the existing node's local value. */
  void setTrustListId(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getTrustListIdNode();

  /** Gets the existing node's local value. */
  @Nullable DateTime getLastUpdateTime();

  /** Sets the existing node's local value. */
  void setLastUpdateTime(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLastUpdateTimeNode();

  /** Gets the existing node's local value. */
  @Nullable Double getUpdateFrequency();

  /** Sets the existing node's local value. */
  void setUpdateFrequency(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getUpdateFrequencyNode();
}
