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

import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.10">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.10</a>
 */
public interface SamplingIntervalDiagnosticsType extends BaseDataVariableType {
  BaseDataVariableType getSamplingIntervalNode();

  Double getSamplingInterval();

  void setSamplingInterval(Double value);

  BaseDataVariableType getSampledMonitoredItemsCountNode();

  UInteger getSampledMonitoredItemsCount();

  void setSampledMonitoredItemsCount(UInteger value);

  BaseDataVariableType getMaxSampledMonitoredItemsCountNode();

  UInteger getMaxSampledMonitoredItemsCount();

  void setMaxSampledMonitoredItemsCount(UInteger value);

  BaseDataVariableType getDisabledMonitoredItemsSamplingCountNode();

  UInteger getDisabledMonitoredItemsSamplingCount();

  void setDisabledMonitoredItemsSamplingCount(UInteger value);
}
