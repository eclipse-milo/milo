/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util;

import static org.eclipse.milo.opcua.stack.core.util.EndpointUtil.updateUrl;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class EndpointUtilTest {

  @Test
  public void testGetPath() {
    assertEquals(EndpointUtil.getPath("opc.tcp://localhost:4840/foo"), "/foo");
    assertEquals(EndpointUtil.getPath("opc.tcp://localhost:4840/foo/"), "/foo");
    assertEquals(EndpointUtil.getPath("opc.tcp://invalid_host:4840/foo"), "/foo");
    assertEquals(EndpointUtil.getPath("opc.tcp://invalid_host:4840/foo/"), "/foo");
  }

  @Test
  public void testGetPath_EmptyAndSlash() {
    assertEquals(EndpointUtil.getPath("opc.tcp://localhost:4840"), "/");
    assertEquals(EndpointUtil.getPath("opc.tcp://localhost:4840/"), "/");
    assertEquals(EndpointUtil.getPath("opc.tcp://invalid_host:4840"), "/");
    assertEquals(EndpointUtil.getPath("opc.tcp://invalid_host:4840/"), "/");
  }

  @Test
  public void testGetPath_Invalid() {
    assertEquals(
        EndpointUtil.getPath("opc.tcp://localhost:4840/no spaces allowed"), "/no spaces allowed");
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

    assertEquals(EndpointUtil.getScheme(withPath), "opc.tcp");
    assertEquals(EndpointUtil.getScheme(withoutPath), "opc.tcp");

    assertEquals(EndpointUtil.getHost(withPath), "[fe80::9289:e377:bacb:f608%enp0s31f6]");
    assertEquals(EndpointUtil.getHost(withoutPath), "[fe80::9289:e377:bacb:f608%enp0s31f6]");

    assertEquals(EndpointUtil.getPort(withPath), 4840);
    assertEquals(EndpointUtil.getPort(withoutPath), 4840);

    assertEquals(EndpointUtil.getPath(withPath), "/foo");
    assertEquals(EndpointUtil.getPath(withoutPath), "/");
  }

  private void testReplaceUrlHostnameWithScheme(String scheme) {
    assertEquals(
        updateUrl(scheme + "://localhost:4840", "localhost2"), scheme + "://localhost2:4840");

    assertEquals(
        updateUrl(scheme + "://localhost:4840/", "localhost2"), scheme + "://localhost2:4840/");

    assertEquals(
        updateUrl(scheme + "://localhost:4840/foo", "localhost2"),
        scheme + "://localhost2:4840/foo");

    assertEquals(
        updateUrl(scheme + "://localhost:4840/foo/bar", "localhost2"),
        scheme + "://localhost2:4840/foo/bar");

    assertEquals(updateUrl(scheme + "://localhost", "localhost2"), scheme + "://localhost2");

    assertEquals(updateUrl(scheme + "://localhost/", "localhost2"), scheme + "://localhost2/");

    assertEquals(
        updateUrl(scheme + "://localhost/foo", "localhost2"), scheme + "://localhost2/foo");

    assertEquals(
        updateUrl(scheme + "://localhost/foo/bar", "localhost2"), scheme + "://localhost2/foo/bar");

    assertEquals(updateUrl(scheme + "://example.com", "example2.com"), scheme + "://example2.com");

    assertEquals(
        updateUrl(scheme + "://example.com/", "example2.com"), scheme + "://example2.com/");

    assertEquals(
        updateUrl(scheme + "://example.com/foo", "example2.com"), scheme + "://example2.com/foo");

    assertEquals(
        updateUrl(scheme + "://example.com/foo/bar", "example2.com"),
        scheme + "://example2.com/foo/bar");

    assertEquals(updateUrl(scheme + "://127.0.0.1", "192.168.0.1"), scheme + "://192.168.0.1");

    assertEquals(updateUrl(scheme + "://127.0.0.1/", "192.168.0.1"), scheme + "://192.168.0.1/");

    assertEquals(
        updateUrl(scheme + "://127.0.0.1/foo", "192.168.0.1"), scheme + "://192.168.0.1/foo");

    assertEquals(
        updateUrl(scheme + "://127.0.0.1/foo/bar", "192.168.0.1"),
        scheme + "://192.168.0.1/foo/bar");

    assertEquals(
        updateUrl(scheme + "://127.0.0.1:4840", "192.168.0.1"), scheme + "://192.168.0.1:4840");

    assertEquals(
        updateUrl(scheme + "://127.0.0.1:4840/", "192.168.0.1"), scheme + "://192.168.0.1:4840/");

    assertEquals(
        updateUrl(scheme + "://127.0.0.1:4840/foo", "192.168.0.1"),
        scheme + "://192.168.0.1:4840/foo");

    assertEquals(
        updateUrl(scheme + "://127.0.0.1:4840/foo/bar", "192.168.0.1"),
        scheme + "://192.168.0.1:4840/foo/bar");
  }

  private void testReplaceUrlPortWithScheme(String scheme) {
    assertEquals(
        updateUrl(scheme + "://localhost:4840", "localhost", 12685), scheme + "://localhost:12685");

    assertEquals(
        updateUrl(scheme + "://localhost:4840", null, 12685), scheme + "://localhost:12685");

    assertEquals(updateUrl(scheme + "://localhost:4840", null, -1), scheme + "://localhost:4840");
  }
}
