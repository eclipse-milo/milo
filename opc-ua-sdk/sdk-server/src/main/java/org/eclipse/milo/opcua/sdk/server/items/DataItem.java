/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.items;

import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

public interface DataItem extends MonitoredItem {

  /**
   * Set the latest sampled value.
   *
   * @param value the latest sampled value.
   */
  void setValue(DataValue value);

  /**
   * Apply a new {@link StatusCode} to the last value that passed the filter and then set the
   * derived value.
   *
   * @param quality the {@link StatusCode} to apply.
   */
  void setQuality(StatusCode quality);

  /**
   * @return the rate to sample this item at.
   */
  double getSamplingInterval();
}
