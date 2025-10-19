/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.test;

import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfigBuilder;
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
  protected TestServer testServer;
  protected TestNamespace testNamespace;

  @BeforeAll
  public void startClientAndServer() throws Exception {
    testServer = TestServer.create();
    server = testServer.getServer();

    testNamespace = new TestNamespace(server);
    testNamespace.startup();

    server.startup().get();

    configureTestNamespace(testNamespace);

    client = TestClient.create(server, this::customizeClientConfig);

    client.connect();
  }

  @AfterAll
  public void stopClientAndServer() {
    try {
      client.disconnectAsync().get(2, TimeUnit.SECONDS);
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
    try {
      // let the session listener callbacks run
      Thread.sleep(500);
      testNamespace.shutdown();
      server.shutdown().get(2, TimeUnit.SECONDS);
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  /**
   * Customize the configuration of the OPC UA client before it connects. This method can be
   * overridden by subclasses to modify the default client configuration.
   *
   * @param configBuilder the {@link OpcUaClientConfigBuilder} to modify.
   */
  protected void customizeClientConfig(OpcUaClientConfigBuilder configBuilder) {}

  /**
   * Configure the test namespace with test-specific nodes.
   *
   * <p>This method is called after the server has started but before the client connects, allowing
   * subclasses to add nodes that will be available to the client.
   *
   * @param namespace the {@link TestNamespace} to configure.
   */
  protected void configureTestNamespace(TestNamespace namespace) {}

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
