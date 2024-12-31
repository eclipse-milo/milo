/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.eclipse.milo.opcua.sdk.client.model.variables.ServerStatusTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.junit.jupiter.api.Test;

public class ClientModelTest extends AbstractClientServerTest {

  @Test
  public void serverStatusTypeNode() throws UaException {
    // Just a basic sanity check that some generated model methods works as expected

    UaNode node = client.getAddressSpace().getNode(NodeIds.Server_ServerStatus);

    ServerStatusTypeNode serverStatus = (ServerStatusTypeNode) node;

    assertNotNull(serverStatus.getStartTime());
    assertNotNull(serverStatus.getCurrentTime());
    assertNotNull(serverStatus.getState());
    assertNotNull(serverStatus.getBuildInfo());
    assertNotNull(serverStatus.getSecondsTillShutdown());
    assertNotNull(serverStatus.getShutdownReason());

    assertNotNull(serverStatus.getStartTimeNode());
    assertNotNull(serverStatus.getCurrentTimeNode());
    assertNotNull(serverStatus.getStateNode());
    assertNotNull(serverStatus.getBuildInfoNode());
    assertNotNull(serverStatus.getSecondsTillShutdownNode());
    assertNotNull(serverStatus.getShutdownReasonNode());

    assertNotNull(serverStatus.readStartTime());
    assertNotNull(serverStatus.readCurrentTime());
    assertNotNull(serverStatus.readState());
    assertNotNull(serverStatus.readBuildInfo());
    assertNotNull(serverStatus.readSecondsTillShutdown());
    assertNotNull(serverStatus.readShutdownReason());
  }
}
