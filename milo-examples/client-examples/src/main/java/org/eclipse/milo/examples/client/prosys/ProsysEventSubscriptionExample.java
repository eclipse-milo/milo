/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
