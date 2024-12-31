/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.util;

import static org.testng.Assert.assertNotEquals;

import java.util.Set;
import org.testng.annotations.Test;

public class HostnameUtilTest {

  @Test
  public void testGetHostnames_ExcludeLocal() throws Exception {
    Set<String> hostnames = HostnameUtil.getHostnames("0.0.0.0", false);

    hostnames.forEach(
        hostname -> {
          assertNotEquals(hostname, "127.0.0.1");
          assertNotEquals(hostname, "localhost");
          System.out.println(hostname);
        });
  }
}
