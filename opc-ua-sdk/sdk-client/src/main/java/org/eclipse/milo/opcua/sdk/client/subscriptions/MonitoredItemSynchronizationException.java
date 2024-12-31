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

import java.util.List;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;

public class MonitoredItemSynchronizationException extends UaException {

  private final List<MonitoredItemServiceOperationResult> createResults;
  private final List<MonitoredItemServiceOperationResult> modifyResults;
  private final List<MonitoredItemServiceOperationResult> deleteResults;

  public MonitoredItemSynchronizationException(
      String message,
      List<MonitoredItemServiceOperationResult> createResults,
      List<MonitoredItemServiceOperationResult> modifyResults,
      List<MonitoredItemServiceOperationResult> deleteResults) {

    super(StatusCodes.Bad_UnexpectedError, message);

    this.createResults = createResults;
    this.modifyResults = modifyResults;
    this.deleteResults = deleteResults;
  }

  /**
   * Get the results for any create operations that were part of the synchronization.
   *
   * @return the results for any create operations that were part of the synchronization.
   */
  public List<MonitoredItemServiceOperationResult> getCreateResults() {
    return createResults;
  }

  /**
   * Get the results for any modify operations that were part of the synchronization.
   *
   * @return the results for any modify operations that were part of the synchronization.
   */
  public List<MonitoredItemServiceOperationResult> getModifyResults() {
    return modifyResults;
  }

  /**
   * Get the results for any delete operations that were part of the synchronization.
   *
   * @return the results for any delete operations that were part of the synchronization.
   */
  public List<MonitoredItemServiceOperationResult> getDeleteResults() {
    return deleteResults;
  }
}
