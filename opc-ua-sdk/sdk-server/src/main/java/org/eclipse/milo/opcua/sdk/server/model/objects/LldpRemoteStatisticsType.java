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

import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.4">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.4</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface LldpRemoteStatisticsType extends BaseObjectType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getLastChangeTimeNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getLastChangeTime();

  /** Sets the existing node's local value. */
  void setLastChangeTime(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getRemoteInsertsNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getRemoteInserts();

  /** Sets the existing node's local value. */
  void setRemoteInserts(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getRemoteDeletesNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getRemoteDeletes();

  /** Sets the existing node's local value. */
  void setRemoteDeletes(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getRemoteDropsNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getRemoteDrops();

  /** Sets the existing node's local value. */
  void setRemoteDrops(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getRemoteAgeoutsNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getRemoteAgeouts();

  /** Sets the existing node's local value. */
  void setRemoteAgeouts(@Nullable UInteger value);
}
