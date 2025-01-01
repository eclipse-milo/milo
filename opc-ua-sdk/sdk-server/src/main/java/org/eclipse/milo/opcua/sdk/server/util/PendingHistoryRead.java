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
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResult;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadValueId;

public class PendingHistoryRead implements Pending<HistoryReadValueId, HistoryReadResult> {

  private final CompletableFuture<HistoryReadResult> future = new CompletableFuture<>();

  private final HistoryReadValueId id;

  public PendingHistoryRead(HistoryReadValueId id) {
    this.id = id;
  }

  @Override
  public HistoryReadValueId getInput() {
    return id;
  }

  @Override
  public CompletableFuture<HistoryReadResult> getFuture() {
    return future;
  }
}
