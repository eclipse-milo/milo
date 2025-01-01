/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.session;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.junit.jupiter.api.Test;

public class SessionFsmTest {

  @Test
  public void testCloseSessionWhileInactive() throws Exception {
    OpcUaClientConfig clientConfig =
        OpcUaClientConfig.builder()
            .setEndpoint(
                new EndpointDescription(
                    "opc.tcp://localhost:12685",
                    null,
                    null,
                    MessageSecurityMode.None,
                    SecurityPolicy.None.getUri(),
                    null,
                    TransportProfile.TCP_UASC_UABINARY.getUri(),
                    null))
            .setApplicationName(LocalizedText.english("Eclipse Milo Test Client"))
            .setApplicationUri("urn:eclipse:milo:examples:client")
            .build();

    OpcUaClient client = OpcUaClient.create(clientConfig);

    SessionFsm sessionFsm = SessionFsmFactory.newSessionFsm(client);

    assertNotNull(sessionFsm.closeSession().get());
  }
}
