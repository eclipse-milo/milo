/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.diagnostics;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.LongAdder;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceCounterDataType;

public class ServiceCounter {

  private final LongAdder totalCount = new LongAdder();
  private final LongAdder errorCount = new LongAdder();

  public void record(CompletionStage<?> completionStage) {
    completionStage.whenComplete(
        (r, ex) -> {
          totalCount.increment();

          if (ex != null) {
            errorCount.increment();
          }
        });
  }

  public void incrementTotalCount() {
    totalCount.increment();
  }

  public void incrementErrorCount() {
    errorCount.increment();
  }

  public ServiceCounterDataType getServiceCounter() {
    return new ServiceCounterDataType(uint(totalCount.sum()), uint(errorCount.sum()));
  }
}
