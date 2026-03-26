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

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.identity.IdentityProvider;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigBuilder;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;

public interface ClientExample {

  /**
   * Returns the endpoint URL the client connects to. The port from this URL is also used as the
   * bind port for the embedded ExampleServer when run via {@link ClientExampleRunner}.
   *
   * @return the endpoint URL.
   */
  default String getEndpointUrl() {
    return "opc.tcp://localhost:12686/milo";
  }

  default Predicate<EndpointDescription> endpointFilter() {
    return e -> getSecurityPolicy().getUri().equals(e.getSecurityPolicyUri());
  }

  default SecurityPolicy getSecurityPolicy() {
    return SecurityPolicy.Basic256Sha256;
  }

  default IdentityProvider getIdentityProvider() {
    return new AnonymousProvider();
  }

  /**
   * Override to customize the client's configuration before it is built.
   *
   * @param builder the client config builder, pre-populated with defaults.
   */
  default void configureClient(OpcUaClientConfigBuilder builder) {}

  /**
   * Override to customize the ExampleServer's configuration before it is built.
   *
   * @param builder the server config builder, pre-populated with defaults.
   */
  default void configureServer(OpcUaServerConfigBuilder builder) {}

  void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception;
}
