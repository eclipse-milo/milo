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

import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.10">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.10</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface SamplingIntervalDiagnosticsType extends BaseDataVariableType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSamplingIntervalNode();

  /** Gets the existing node's local value. */
  @Nullable Double getSamplingInterval();

  /** Sets the existing node's local value. */
  void setSamplingInterval(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSampledMonitoredItemsCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getSampledMonitoredItemsCount();

  /** Sets the existing node's local value. */
  void setSampledMonitoredItemsCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMaxSampledMonitoredItemsCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxSampledMonitoredItemsCount();

  /** Sets the existing node's local value. */
  void setMaxSampledMonitoredItemsCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getDisabledMonitoredItemsSamplingCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getDisabledMonitoredItemsSamplingCount();

  /** Sets the existing node's local value. */
  void setDisabledMonitoredItemsSamplingCount(@Nullable UInteger value);
}
