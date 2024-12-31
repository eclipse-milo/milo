/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.client;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MethodExample implements ClientExample {

  public static void main(String[] args) throws Exception {
    MethodExample example = new MethodExample();

    new ClientExampleRunner(example).run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
    client.connect();

    // call the sqrt(x) function
    sqrt(client, 16.0)
        .exceptionally(
            ex -> {
              logger.error("error invoking sqrt()", ex);
              return -1.0;
            })
        .thenAccept(
            v -> {
              logger.info("sqrt(16)={}", v);

              future.complete(client);
            });
  }

  private CompletableFuture<Double> sqrt(OpcUaClient client, Double input) {
    NodeId objectId = NodeId.parse("ns=2;s=HelloWorld");
    NodeId methodId = NodeId.parse("ns=2;s=HelloWorld/sqrt(x)");

    var request =
        new CallMethodRequest(objectId, methodId, new Variant[] {Variant.ofDouble(input)});

    CompletableFuture<CallMethodResult> future =
        client.callAsync(List.of(request)).thenApply(r -> requireNonNull(r.getResults())[0]);

    return future.thenCompose(
        result -> {
          StatusCode statusCode = result.getStatusCode();

          if (statusCode.isGood()) {
            Double value = (Double) requireNonNull(result.getOutputArguments())[0].getValue();
            return CompletableFuture.completedFuture(value);
          } else {
            StatusCode[] inputArgumentResults =
                requireNonNullElse(result.getInputArgumentResults(), new StatusCode[0]);

            for (int i = 0; i < inputArgumentResults.length; i++) {
              logger.error("inputArgumentResults[{}]={}", i, inputArgumentResults[i]);
            }

            CompletableFuture<Double> f = new CompletableFuture<>();
            f.completeExceptionally(new UaException(statusCode));
            return f;
          }
        });
  }
}
