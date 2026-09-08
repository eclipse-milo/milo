/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.junit.jupiter.api.Test;

class AbstractClientServerTestLifecycleTest {

  // One failed cleanup must not leak later resources or conceal their failures.
  @Test
  void cleanupAttemptsEveryResourceAndPreservesFailures() throws Exception {
    var fixture = new AbstractClientServerTest() {};
    var client = mock(OpcUaClient.class);
    var namespace = mock(TestNamespace.class);
    var server = mock(OpcUaServer.class);
    fixture.client = client;
    fixture.testNamespace = namespace;
    fixture.server = server;
    var disconnectFailure = new IllegalStateException("disconnect failed");
    var namespaceFailure = new AssertionError("namespace shutdown failed");
    var serverFailure = new IllegalStateException("server shutdown failed");
    when(client.disconnectAsync()).thenReturn(CompletableFuture.failedFuture(disconnectFailure));
    doThrow(namespaceFailure).when(namespace).shutdown();
    when(server.shutdown()).thenReturn(CompletableFuture.failedFuture(serverFailure));

    var failure = assertThrows(ExecutionException.class, fixture::stopClientAndServer);

    assertSame(disconnectFailure, failure.getCause());
    assertEquals(2, failure.getSuppressed().length);
    assertSame(namespaceFailure, failure.getSuppressed()[0]);
    assertSame(serverFailure, failure.getSuppressed()[1].getCause());
    var order = inOrder(client, namespace, server);
    order.verify(client).disconnectAsync();
    order.verify(namespace).shutdown();
    order.verify(server).shutdown();
    fixture.stopClientAndServer();
    order.verifyNoMoreInteractions();
  }

  @Test
  void cleanupHandlesResourcesMissingAfterPartialSetup() throws Exception {
    var fixture = new AbstractClientServerTest() {};
    var server = mock(OpcUaServer.class);
    fixture.server = server;
    when(server.shutdown()).thenReturn(CompletableFuture.completedFuture(server));

    fixture.stopClientAndServer();

    verify(server).shutdown();
  }
}
