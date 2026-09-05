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
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

  // RFC 3986 section 3.1 makes scheme names case-insensitive, and Part 6 endpoint URLs inherit
  // that. Consumers already normalize the scheme themselves (DiscoveryClient lowercases it, the
  // HTTPS and WebSocket transports use equalsIgnoreCase), so parsing must not reject a mixed-case
  // scheme before they get the chance.
  @ParameterizedTest
  @ValueSource(strings = {"opc.tcp", "OPC.TCP", "Opc.Tcp", "opc.wss", "OPC.WSS", "https", "HTTPS"})
  public void testMixedCaseScheme(String scheme) {
    String endpointUrl = scheme + "://localhost:4840/foo";

    assertEquals(scheme, EndpointUtil.getScheme(endpointUrl));
    assertEquals("localhost", EndpointUtil.getHost(endpointUrl));
    assertEquals(4840, EndpointUtil.getPort(endpointUrl));
    assertEquals("/foo", EndpointUtil.getPath(endpointUrl));
  }

  // A server matches an incoming Hello against its configured endpoints by comparing paths
  // (UascServerHelloHandler, UascServerAsymmetricHandler). An unmatched URL silently yields "/",
  // which matches the wrong endpoint rather than failing loudly.
  @Test
  public void testGetPath_MixedCaseScheme() {
    assertEquals("/foo", EndpointUtil.getPath("OPC.TCP://localhost:4840/foo"));
    assertEquals("/foo", EndpointUtil.getPath("OPC.TCP://localhost:4840/foo/"));
    assertEquals("/foo/bar", EndpointUtil.getPath("Opc.Tcp://localhost:4840/foo/bar"));
    assertEquals("/", EndpointUtil.getPath("OPC.TCP://localhost:4840"));
  }

  // Servers behind NAT rewrite the hostname of the endpoints they advertise. A URL the parser
  // rejects is returned unchanged, leaving the client with an unreachable address.
  @Test
  public void testUpdateUrlWithMixedCaseScheme() {
    assertEquals("OPC.TCP://localhost2:4840", updateUrl("OPC.TCP://localhost:4840", "localhost2"));

    assertEquals(
        "OPC.TCP://localhost2:4840/foo", updateUrl("OPC.TCP://localhost:4840/foo", "localhost2"));

    assertEquals(
        "Opc.Tcp://localhost2:12685", updateUrl("Opc.Tcp://localhost:4840", "localhost2", 12685));

    assertEquals("HTTPS://localhost2/foo", updateUrl("HTTPS://localhost/foo", "localhost2"));
  }

  @Test
  public void testMixedCaseSchemeWithIpv6Host() {
    String endpointUrl = "OPC.TCP://[::1]:4840/foo";

    assertEquals("[::1]", EndpointUtil.getHost(endpointUrl));
    assertEquals(4840, EndpointUtil.getPort(endpointUrl));
    assertEquals("/foo", EndpointUtil.getPath(endpointUrl));
    assertEquals("OPC.TCP://[2001:db8::1]:4840/foo", updateUrl(endpointUrl, "[2001:db8::1]"));
  }

  // An unrecognized scheme must still be rejected; case-insensitivity is not a license to parse
  // anything.
  @Test
  public void testUnknownSchemeIsStillRejected() {
    assertNull(EndpointUtil.getScheme("ftp://localhost:4840/foo"));
    assertNull(EndpointUtil.getScheme("FTP://localhost:4840/foo"));
    assertNull(EndpointUtil.getHost("FTP://localhost:4840/foo"));
    assertEquals("/", EndpointUtil.getPath("FTP://localhost:4840/foo"));
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

  @Test
  public void testUpdateUrlWithIpv6Hostname() {
    String input1 = "opc.tcp://[::1]:4840";
    String input2 = "opc.tcp://[::1]:4840/foo";
    String input3 = "opc.tcp://[::1]";
    String input4 = "opc.tcp://[::1]/foo";
    String newHost = "[2001:db8::1]";

    assertEquals("opc.tcp://[2001:db8::1]:4840", updateUrl(input1, newHost));
    assertEquals("opc.tcp://[2001:db8::1]:4840/foo", updateUrl(input2, newHost));
    assertEquals("opc.tcp://[2001:db8::1]", updateUrl(input3, newHost));
    assertEquals("opc.tcp://[2001:db8::1]/foo", updateUrl(input4, newHost));
  }

  @Test
  public void testUpdateUrlWithIpv6HostnameAndNewPort() {
    String input1 = "opc.tcp://[::1]:4840";
    String input2 = "opc.tcp://[::1]:4840/foo";
    String input3 = "opc.tcp://[::1]";
    String input4 = "opc.tcp://[::1]/foo";

    String newHost = "[2001:db8::1]";
    int newPort = 12685;

    // change both host and port
    assertEquals("opc.tcp://[2001:db8::1]:12685", updateUrl(input1, newHost, newPort));
    assertEquals("opc.tcp://[2001:db8::1]:12685/foo", updateUrl(input2, newHost, newPort));
    assertEquals("opc.tcp://[2001:db8::1]:12685", updateUrl(input3, newHost, newPort));
    assertEquals("opc.tcp://[2001:db8::1]:12685", updateUrl("opc.tcp://[::1]:0", newHost, newPort));
    assertEquals("opc.tcp://[2001:db8::1]:12685/foo", updateUrl(input4, newHost, newPort));

    // change only port, keep host
    assertEquals("opc.tcp://[::1]:12685", updateUrl(input1, null, newPort));
    assertEquals("opc.tcp://[::1]:12685/foo", updateUrl(input2, null, newPort));
    assertEquals("opc.tcp://[::1]:12685", updateUrl(input3, null, newPort));
    assertEquals("opc.tcp://[::1]:12685/foo", updateUrl(input4, null, newPort));
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
