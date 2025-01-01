/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.util;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateResult;

public class PendingHistoryUpdate implements Pending<HistoryUpdateDetails, HistoryUpdateResult> {

  private final CompletableFuture<HistoryUpdateResult> future = new CompletableFuture<>();

  private final HistoryUpdateDetails details;

  public PendingHistoryUpdate(HistoryUpdateDetails details) {
    this.details = details;
  }

  @Override
  public HistoryUpdateDetails getInput() {
    return details;
  }

  @Override
  public CompletableFuture<HistoryUpdateResult> getFuture() {
    return future;
  }
}
