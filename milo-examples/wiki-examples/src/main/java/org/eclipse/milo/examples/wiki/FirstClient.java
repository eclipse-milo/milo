/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.wiki;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;

/** Reads the first-server tutorial's Temperature Variable and checks its status and Java type. */
public final class FirstClient {
  private FirstClient() {}

  /**
   * Connect to the loopback tutorial endpoint and read Temperature.
   *
   * @param endpointUrl the discovery URL of a tutorial server.
   * @return the Good Double value returned by the server.
   * @throws Exception if connecting or reading fails, the namespace is missing, or the value has an
   *     unexpected status or type.
   */
  public static double readTemperature(String endpointUrl) throws Exception {
    OpcUaClient client =
        OpcUaClient.create(
            endpointUrl,
            endpoints ->
                endpoints.stream()
                    .filter(e -> SecurityPolicy.None.getUri().equals(e.getSecurityPolicyUri()))
                    .filter(e -> e.getSecurityMode() == MessageSecurityMode.None)
                    .filter(e -> endpointUrl.equals(e.getEndpointUrl()))
                    .findFirst(),
            transport -> {},
            builder ->
                builder
                    .setApplicationUri("urn:eclipse:milo:wiki:client")
                    .setIdentityProvider(new AnonymousProvider()));
    try {
      client.connect();
      UShort namespaceIndex = client.getNamespaceTable().getIndex("urn:eclipse:milo:wiki");
      if (namespaceIndex == null) {
        throw new IllegalStateException("Server does not expose the tutorial namespace");
      }
      NodeId nodeId = new NodeId(namespaceIndex, "Temperature");
      DataValue value = client.readValue(0.0, TimestampsToReturn.Both, nodeId);
      if (value.getStatusCode() == null || !value.getStatusCode().isGood()) {
        throw new IllegalStateException("Read failed: " + value.getStatusCode());
      }
      if (!(value.getValue().getValue() instanceof Double temperature)) {
        throw new IllegalStateException("Expected a Double, received " + value.getValue());
      }
      return temperature;
    } finally {
      client.disconnect();
    }
  }

  public static void main(String[] args) throws Exception {
    String endpointUrl = args.length == 0 ? "opc.tcp://127.0.0.1:12686/wiki" : args[0];
    try {
      System.out.println("Temperature: " + readTemperature(endpointUrl));
    } finally {
      Stack.releaseSharedResources();
    }
  }
}
