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
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;

public class PendingRead implements Pending<ReadValueId, DataValue> {

  private final CompletableFuture<DataValue> future = new CompletableFuture<>();

  private final ReadValueId id;

  public PendingRead(ReadValueId id) {
    this.id = id;
  }

  @Override
  public ReadValueId getInput() {
    return id;
  }

  @Override
  public CompletableFuture<DataValue> getFuture() {
    return future;
  }
}
