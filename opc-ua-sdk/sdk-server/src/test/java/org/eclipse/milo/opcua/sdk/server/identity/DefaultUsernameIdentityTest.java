/*
 * Copyright (c) 2023 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.identity;

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class DefaultUsernameIdentityTest {

    @Test
    void equalTo() {
        var id1 = new DefaultUsernameIdentity("foo");
        var id2 = new DefaultUsernameIdentity("foo");
        var id3 = new DefaultUsernameIdentity("bar");

        assertTrue(id1.equalTo(id2));
        assertFalse(id1.equalTo(id3));
    }

}