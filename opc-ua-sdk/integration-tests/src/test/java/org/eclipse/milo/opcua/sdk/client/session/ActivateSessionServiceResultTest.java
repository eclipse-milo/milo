/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.session;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.junit.jupiter.api.Test;

public class ActivateSessionServiceResultTest extends AbstractClientServerTest {

  @Test
  void resultPresentAfterConnect() throws UaException {
    Optional<StatusCode> result = client.getSession().getLastActivateSessionServiceResult();

    assertTrue(result.isPresent());
    assertTrue(result.get().isGood());
  }
}
