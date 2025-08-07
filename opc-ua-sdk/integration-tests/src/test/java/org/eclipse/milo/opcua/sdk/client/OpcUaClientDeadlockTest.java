/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.junit.jupiter.api.Test;

public class OpcUaClientDeadlockTest extends AbstractClientServerTest {

  @Test
  void deadlockInAsyncCallback() throws Exception {
    CompletableFuture<List<DataValue>> future =
        client.readValuesAsync(
            0.0, TimestampsToReturn.Neither, List.of(NodeIds.Server_ServerStatus_CurrentTime));

    var blockingReadComplete = new CountDownLatch(1);

    future.whenComplete(
        (values, ex) -> {
          try {
            client.readValue(
                0.0, TimestampsToReturn.Neither, NodeIds.Server_ServerStatus_CurrentTime);

            blockingReadComplete.countDown();
          } catch (UaException e) {
            throw new RuntimeException(e);
          }
        });

    assertTrue(
        blockingReadComplete.await(1, TimeUnit.SECONDS),
        "timed out waiting for blocking read to complete");
  }
}
