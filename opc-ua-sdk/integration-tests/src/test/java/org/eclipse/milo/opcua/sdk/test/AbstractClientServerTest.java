/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.test;

import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractClientServerTest {

  protected OpcUaClient client;
  protected OpcUaServer server;
  protected TestNamespace testNamespace;

  @BeforeAll
  public void startClientAndServer() throws Exception {
    server = TestServer.create();

    testNamespace = new TestNamespace(server);
    testNamespace.startup();

    server.startup().get();

    client = TestClient.create(server);

    client.connect();
  }

  @AfterAll
  public void stopClientAndServer() throws ExecutionException, InterruptedException {
    client.disconnectAsync().get();

    testNamespace.shutdown();
    server.shutdown().get();
  }

  /**
   * Create a new {@link NodeId} in the {@link TestNamespace}.
   *
   * @param id the identifier to use.
   * @return a new {@link NodeId} in the {@link TestNamespace}.
   */
  protected NodeId newNodeId(String id) {
    return new NodeId(testNamespace.getNamespaceIndex(), id);
  }

  /**
   * Create a new {@link QualifiedName} in the {@link TestNamespace}.
   *
   * @param name the name to use.
   * @return a new {@link QualifiedName} in the {@link TestNamespace}.
   */
  protected QualifiedName newQualifiedName(String name) {
    return new QualifiedName(testNamespace.getNamespaceIndex(), name);
  }
}
