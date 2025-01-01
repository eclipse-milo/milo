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
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;

public class PendingWrite implements Pending<WriteValue, StatusCode> {

  private final CompletableFuture<StatusCode> future = new CompletableFuture<>();

  private final WriteValue writeValue;

  public PendingWrite(WriteValue writeValue) {
    this.writeValue = writeValue;
  }

  @Override
  public CompletableFuture<StatusCode> getFuture() {
    return future;
  }

  @Override
  public WriteValue getInput() {
    return writeValue;
  }
}
