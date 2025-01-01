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
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;

public class PendingCall implements Pending<CallMethodRequest, CallMethodResult> {

  private final CompletableFuture<CallMethodResult> future = new CompletableFuture<>();

  private final CallMethodRequest request;

  public PendingCall(CallMethodRequest request) {
    this.request = request;
  }

  @Override
  public CompletableFuture<CallMethodResult> getFuture() {
    return future;
  }

  @Override
  public CallMethodRequest getInput() {
    return request;
  }
}
