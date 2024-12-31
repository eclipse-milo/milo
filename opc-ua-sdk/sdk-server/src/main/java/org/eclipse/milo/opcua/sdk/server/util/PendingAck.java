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
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionAcknowledgement;

public class PendingAck implements Pending<SubscriptionAcknowledgement, StatusCode> {

  private final CompletableFuture<StatusCode> future = new CompletableFuture<>();

  private final SubscriptionAcknowledgement input;

  public PendingAck(SubscriptionAcknowledgement input) {
    this.input = input;
  }

  @Override
  public CompletableFuture<StatusCode> getFuture() {
    return future;
  }

  @Override
  public SubscriptionAcknowledgement getInput() {
    return input;
  }
}
