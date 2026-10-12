/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.gds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.junit.jupiter.api.Test;

public class GdsClientWithoutGdsNamespaceTest extends AbstractClientServerTest {

  // Pointing a GDS client at an ordinary server must fail up front with a clear reason, and must
  // not disturb the client, which may still be wanted for other work.
  @Test
  void createFailsWithBadNotFoundWhenTheServerDoesNotHostTheGdsNamespace() throws Exception {
    UaException e = assertThrows(UaException.class, () -> GdsClient.create(client));

    assertEquals(StatusCodes.Bad_NotFound, e.getStatusCode().value());
    assertTrue(e.getMessage().contains(GdsClient.NAMESPACE_URI), e.getMessage());
    assertTrue(client.readNamespaceTable().toArray().length > 0, "client still usable");
  }
}
