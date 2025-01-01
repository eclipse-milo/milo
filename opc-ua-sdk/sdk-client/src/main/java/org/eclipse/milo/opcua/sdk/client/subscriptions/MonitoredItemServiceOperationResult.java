/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.subscriptions;

import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

public class MonitoredItemServiceOperationResult implements ServiceOperationResult<StatusCode> {

  private final OpcUaMonitoredItem monitoredItem;
  private final StatusCode serviceResult;
  private final @Nullable StatusCode operationResult;

  public MonitoredItemServiceOperationResult(
      OpcUaMonitoredItem monitoredItem,
      StatusCode serviceResult,
      @Nullable StatusCode operationResult) {

    this.monitoredItem = monitoredItem;
    this.serviceResult = serviceResult;
    this.operationResult = operationResult;
  }

  public OpcUaMonitoredItem monitoredItem() {
    return monitoredItem;
  }

  @Override
  public StatusCode serviceResult() {
    return serviceResult;
  }

  @Override
  public Optional<StatusCode> operationResult() {
    return Optional.ofNullable(operationResult);
  }

  /**
   * @return {@code true} if both the service and operation result are good.
   */
  public boolean isGood() {
    return serviceResult.isGood() && operationResult != null && operationResult.isGood();
  }
}
