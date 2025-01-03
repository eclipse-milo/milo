/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util;

import static org.eclipse.milo.opcua.stack.core.util.EndpointUtil.updateUrl;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class EndpointUtilTest {

  @Test
  public void testGetPath() {
    assertEquals("/foo", EndpointUtil.getPath("opc.tcp://localhost:4840/foo"));
    assertEquals("/foo", EndpointUtil.getPath("opc.tcp://localhost:4840/foo/"));
    assertEquals("/foo", EndpointUtil.getPath("opc.tcp://invalid_host:4840/foo"));
    assertEquals("/foo", EndpointUtil.getPath("opc.tcp://invalid_host:4840/foo/"));
  }

  @Test
  public void testGetPath_EmptyAndSlash() {
    assertEquals("/", EndpointUtil.getPath("opc.tcp://localhost:4840"));
    assertEquals("/", EndpointUtil.getPath("opc.tcp://localhost:4840/"));
    assertEquals("/", EndpointUtil.getPath("opc.tcp://invalid_host:4840"));
    assertEquals("/", EndpointUtil.getPath("opc.tcp://invalid_host:4840/"));
  }

  @Test
  public void testGetPath_Invalid() {
    assertEquals(
        "/no spaces allowed", EndpointUtil.getPath("opc.tcp://localhost:4840/no spaces allowed"));
  }

  @Test
  public void testReplaceUrlHostname() {
    testReplaceUrlHostnameWithScheme("opc.tcp");
    testReplaceUrlHostnameWithScheme("http");
    testReplaceUrlHostnameWithScheme("https");
  }

  @Test
  public void testReplaceUrlPort() {
    testReplaceUrlPortWithScheme("opc.tcp");
    testReplaceUrlPortWithScheme("http");
    testReplaceUrlPortWithScheme("https");
  }

  @Test
  public void testIpv6() {
    String withPath = "opc.tcp://[fe80::9289:e377:bacb:f608%enp0s31f6]:4840/foo";
    String withoutPath = "opc.tcp://[fe80::9289:e377:bacb:f608%enp0s31f6]:4840";

    assertEquals("opc.tcp", EndpointUtil.getScheme(withPath));
    assertEquals("opc.tcp", EndpointUtil.getScheme(withoutPath));

    assertEquals("[fe80::9289:e377:bacb:f608%enp0s31f6]", EndpointUtil.getHost(withPath));
    assertEquals("[fe80::9289:e377:bacb:f608%enp0s31f6]", EndpointUtil.getHost(withoutPath));

    assertEquals(4840, EndpointUtil.getPort(withPath));
    assertEquals(4840, EndpointUtil.getPort(withoutPath));

    assertEquals("/foo", EndpointUtil.getPath(withPath));
    assertEquals("/", EndpointUtil.getPath(withoutPath));
  }

  private void testReplaceUrlHostnameWithScheme(String scheme) {
    assertEquals(
        scheme + "://localhost2:4840", updateUrl(scheme + "://localhost:4840", "localhost2"));

    assertEquals(
        scheme + "://localhost2:4840/", updateUrl(scheme + "://localhost:4840/", "localhost2"));

    assertEquals(
        scheme + "://localhost2:4840/foo",
        updateUrl(scheme + "://localhost:4840/foo", "localhost2"));

    assertEquals(
        scheme + "://localhost2:4840/foo/bar",
        updateUrl(scheme + "://localhost:4840/foo/bar", "localhost2"));

    assertEquals(scheme + "://localhost2", updateUrl(scheme + "://localhost", "localhost2"));

    assertEquals(scheme + "://localhost2/", updateUrl(scheme + "://localhost/", "localhost2"));

    assertEquals(
        scheme + "://localhost2/foo", updateUrl(scheme + "://localhost/foo", "localhost2"));

    assertEquals(
        scheme + "://localhost2/foo/bar", updateUrl(scheme + "://localhost/foo/bar", "localhost2"));

    assertEquals(scheme + "://example2.com", updateUrl(scheme + "://example.com", "example2.com"));

    assertEquals(
        scheme + "://example2.com/", updateUrl(scheme + "://example.com/", "example2.com"));

    assertEquals(
        scheme + "://example2.com/foo", updateUrl(scheme + "://example.com/foo", "example2.com"));

    assertEquals(
        scheme + "://example2.com/foo/bar",
        updateUrl(scheme + "://example.com/foo/bar", "example2.com"));

    assertEquals(scheme + "://192.168.0.1", updateUrl(scheme + "://127.0.0.1", "192.168.0.1"));

    assertEquals(scheme + "://192.168.0.1/", updateUrl(scheme + "://127.0.0.1/", "192.168.0.1"));

    assertEquals(
        scheme + "://192.168.0.1/foo", updateUrl(scheme + "://127.0.0.1/foo", "192.168.0.1"));

    assertEquals(
        scheme + "://192.168.0.1/foo/bar",
        updateUrl(scheme + "://127.0.0.1/foo/bar", "192.168.0.1"));

    assertEquals(
        scheme + "://192.168.0.1:4840", updateUrl(scheme + "://127.0.0.1:4840", "192.168.0.1"));

    assertEquals(
        scheme + "://192.168.0.1:4840/", updateUrl(scheme + "://127.0.0.1:4840/", "192.168.0.1"));

    assertEquals(
        scheme + "://192.168.0.1:4840/foo",
        updateUrl(scheme + "://127.0.0.1:4840/foo", "192.168.0.1"));

    assertEquals(
        scheme + "://192.168.0.1:4840/foo/bar",
        updateUrl(scheme + "://127.0.0.1:4840/foo/bar", "192.168.0.1"));
  }

  private void testReplaceUrlPortWithScheme(String scheme) {
    assertEquals(
        scheme + "://localhost:12685", updateUrl(scheme + "://localhost:4840", "localhost", 12685));

    assertEquals(
        scheme + "://localhost:12685", updateUrl(scheme + "://localhost:4840", null, 12685));

    assertEquals(scheme + "://localhost:4840", updateUrl(scheme + "://localhost:4840", null, -1));
  }
}
