/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.client.prosys;

import org.eclipse.milo.examples.client.ClientExampleRunner;
import org.eclipse.milo.examples.client.EventSubscriptionExample;

/**
 * Runs the standard event-subscription example against Prosys OPC UA Simulation Server.
 *
 * <p>The class lives in the Prosys package because it depends on the Simulation Server's default
 * endpoint URL and address-space behavior. The subscription logic remains inherited from {@link
 * EventSubscriptionExample}; this wrapper only selects the external server and disables the local
 * embedded example server.
 */
public class ProsysEventSubscriptionExample extends EventSubscriptionExample {

  public static void main(String[] args) throws Exception {
    ProsysEventSubscriptionExample example = new ProsysEventSubscriptionExample();

    new ClientExampleRunner(example, false).run();
  }

  @Override
  public String getEndpointUrl() {
    return "opc.tcp://localhost:53530/OPCUA/SimulationServer";
  }
}
