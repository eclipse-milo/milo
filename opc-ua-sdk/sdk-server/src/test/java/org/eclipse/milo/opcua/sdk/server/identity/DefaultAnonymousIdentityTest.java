/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.identity;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class DefaultAnonymousIdentityTest {

  @Test
  void equalTo() {
    DefaultAnonymousIdentity id1 = new DefaultAnonymousIdentity();
    DefaultAnonymousIdentity id2 = new DefaultAnonymousIdentity();

    assertTrue(id1.equalTo(id2));
  }
}
