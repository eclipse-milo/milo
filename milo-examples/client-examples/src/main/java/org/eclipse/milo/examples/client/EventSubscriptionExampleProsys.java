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

public class EventSubscriptionExampleProsys extends EventSubscriptionExample {

  public static void main(String[] args) throws Exception {
    EventSubscriptionExampleProsys example = new EventSubscriptionExampleProsys();

    new ClientExampleRunner(example, false).run();
  }

  @Override
  public String getEndpointUrl() {
    return "opc.tcp://localhost:53530/OPCUA/SimulationServer";
  }
}
