/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.channels.UnresolvedAddressException;
import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaServiceFaultException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceFault;
import org.eclipse.milo.opcua.stack.transport.client.SecureChannelHandshakeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class DiscoveryFallbackTest {

  // Appending /discovery must normalize trailing slashes and preserve any query or fragment
  // without treating path segments that merely end in "discovery" as already targeting /discovery.
  @ParameterizedTest
  @CsvSource({
    "opc.tcp://host:4840, opc.tcp://host:4840/discovery",
    "opc.tcp://host:4840/, opc.tcp://host:4840/discovery",
    "opc.tcp://host:4840///, opc.tcp://host:4840/discovery",
    "opc.tcp://host:4840/foo, opc.tcp://host:4840/foo/discovery",
    "opc.tcp://host:4840/foo/, opc.tcp://host:4840/foo/discovery",
    "opc.tcp://host:4840/foo/notdiscovery, opc.tcp://host:4840/foo/notdiscovery/discovery",
    "opc.tcp://host:4840/foo?a=1#frag, opc.tcp://host:4840/foo/discovery?a=1#frag",
    "opc.tcp://host:4840/foo/?a=1#frag, opc.tcp://host:4840/foo/discovery?a=1#frag",
    "opc.tcp://host:4840?a=1, opc.tcp://host:4840/discovery?a=1",
    "opc.tcp://host:4840/foo?next=/discovery, opc.tcp://host:4840/foo/discovery?next=/discovery",
    "opc.tcp://host:4840/DISCOVERY, opc.tcp://host:4840/DISCOVERY/discovery",
    "opc.tcp://[::1]:4840/foo?a=1, opc.tcp://[::1]:4840/foo/discovery?a=1"
  })
  void appendsDiscoveryPathPreservingQueryAndFragment(String endpointUrl, String expected) {
    assertEquals(Optional.of(expected), DiscoveryFallback.fallbackDiscoveryUrl(endpointUrl));
  }

  // URLs whose path already ends with /discovery (ignoring trailing slashes, query, or fragment)
  // must not produce a second /discovery segment.
  @ParameterizedTest
  @ValueSource(
      strings = {
        "",
        "opc.tcp://host:4840/discovery",
        "opc.tcp://host:4840/discovery/",
        "opc.tcp://host:4840/discovery//",
        "opc.tcp://host:4840/foo/discovery",
        "opc.tcp://host:4840/foo/discovery/",
        "opc.tcp://host:4840/foo/discovery?a=1#frag",
        "opc.tcp://[::1]:4840/foo/discovery/"
      })
  void returnsEmptyWhenUrlAlreadyTargetsDiscovery(String discoveryUrl) {
    assertEquals(Optional.empty(), DiscoveryFallback.fallbackDiscoveryUrl(discoveryUrl));
  }

  @Test
  void returnsEmptyForNullUrl() {
    assertEquals(Optional.empty(), DiscoveryFallback.fallbackDiscoveryUrl(null));
  }

  // Only failures that a separate /discovery endpoint can resolve should trigger fallback.
  @ParameterizedTest(name = "{0}")
  @MethodSource("retryableFailures")
  void retriesDiscoveryForQualifyingFailures(String label, Throwable failure) {
    assertTrue(
        DiscoveryFallback.shouldFallback(failure),
        () -> label + " should qualify for /discovery fallback");
  }

  static Stream<Arguments> retryableFailures() {
    return Stream.of(
        Arguments.of(
            "Bad_TcpEndpointUrlInvalid",
            new UaException(StatusCodes.Bad_TcpEndpointUrlInvalid, "invalid endpoint url")),
        Arguments.of(
            "Bad_SecurityPolicyRejected", new UaException(StatusCodes.Bad_SecurityPolicyRejected)),
        Arguments.of(
            "Bad_SecurityModeRejected", new UaException(StatusCodes.Bad_SecurityModeRejected)),
        Arguments.of(
            "Bad_SecurityModeInsufficient",
            new UaException(StatusCodes.Bad_SecurityModeInsufficient)),
        Arguments.of(
            "Bad_SecurityChecksFailed", new UaException(StatusCodes.Bad_SecurityChecksFailed)),
        Arguments.of(
            "GetEndpoints ServiceFault Bad_SecurityModeInsufficient",
            serviceFault(StatusCodes.Bad_SecurityModeInsufficient)),
        Arguments.of(
            "OPN handshake Bad_SecurityPolicyRejected",
            new SecureChannelHandshakeException(
                new UaException(StatusCodes.Bad_SecurityPolicyRejected))),
        Arguments.of(
            "OPN handshake Bad_ConnectionClosed",
            new SecureChannelHandshakeException(
                new UaException(StatusCodes.Bad_ConnectionClosed, "connection closed"))),
        Arguments.of(
            "OPN handshake SocketException",
            new SecureChannelHandshakeException(new SocketException("connection reset"))));
  }

  // Selection, transport, DNS, timeout, capacity, shutdown, and non-security GetEndpoints service
  // faults cannot be resolved by retrying against /discovery.
  @ParameterizedTest(name = "{0}")
  @MethodSource("nonRetryableFailures")
  void skipsDiscoveryForNonQualifyingFailures(String label, Throwable failure) {
    assertFalse(
        DiscoveryFallback.shouldFallback(failure),
        () -> label + " must not trigger /discovery fallback");
  }

  static Stream<Arguments> nonRetryableFailures() {
    return Stream.of(
        Arguments.of("null failure", null),
        Arguments.of(
            "Bad_ConfigurationError",
            new UaException(StatusCodes.Bad_ConfigurationError, "no endpoint selected")),
        Arguments.of(
            "GetEndpoints Bad_ServiceUnsupported",
            serviceFault(StatusCodes.Bad_ServiceUnsupported)),
        Arguments.of("GetEndpoints Bad_InternalError", serviceFault(StatusCodes.Bad_InternalError)),
        Arguments.of(
            "TCP connection refused",
            new UaException(
                StatusCodes.Bad_ConnectionRejected, new ConnectException("Connection refused"))),
        Arguments.of(
            "acknowledge Bad_Timeout",
            new UaException(StatusCodes.Bad_Timeout, "timed out waiting for acknowledge")),
        Arguments.of("TimeoutException", new TimeoutException("timed out")),
        Arguments.of("CancellationException", new CancellationException()),
        Arguments.of("UnknownHostException", new UnknownHostException("does-not-exist.invalid")),
        Arguments.of("UnresolvedAddressException", new UnresolvedAddressException()),
        Arguments.of(
            "unsupported scheme",
            new UaException(StatusCodes.Bad_InternalError, "unsupported protocol: foo")),
        Arguments.of(
            "OPN Bad_Timeout",
            new SecureChannelHandshakeException(new UaException(StatusCodes.Bad_Timeout))),
        Arguments.of(
            "OPN Bad_RequestTimeout",
            new SecureChannelHandshakeException(serviceFault(StatusCodes.Bad_RequestTimeout))),
        Arguments.of(
            "OPN TimeoutException",
            new SecureChannelHandshakeException(new TimeoutException("timed out"))),
        Arguments.of(
            "OPN ReadTimeoutException",
            new SecureChannelHandshakeException(ReadTimeoutException.INSTANCE)),
        Arguments.of(
            "OPN SocketTimeoutException",
            new SecureChannelHandshakeException(new SocketTimeoutException("read timed out"))),
        Arguments.of(
            "OPN ConnectTimeoutException",
            new SecureChannelHandshakeException(new ConnectTimeoutException("connect timed out"))),
        Arguments.of(
            "OPN CancellationException",
            new SecureChannelHandshakeException(new CancellationException())),
        Arguments.of(
            "OPN Bad_MaxConnectionsReached",
            new SecureChannelHandshakeException(
                serviceFault(StatusCodes.Bad_MaxConnectionsReached))),
        Arguments.of(
            "OPN Bad_TooManySessions",
            new SecureChannelHandshakeException(serviceFault(StatusCodes.Bad_TooManySessions))),
        Arguments.of(
            "OPN Bad_ResourceUnavailable",
            new SecureChannelHandshakeException(serviceFault(StatusCodes.Bad_ResourceUnavailable))),
        Arguments.of(
            "OPN Bad_ServerHalted",
            new SecureChannelHandshakeException(serviceFault(StatusCodes.Bad_ServerHalted))),
        Arguments.of(
            "OPN Bad_Shutdown",
            new SecureChannelHandshakeException(serviceFault(StatusCodes.Bad_Shutdown))),
        Arguments.of(
            "OPN Bad_OutOfMemory",
            new SecureChannelHandshakeException(serviceFault(StatusCodes.Bad_OutOfMemory))),
        Arguments.of(
            "OPN Bad_RequestCancelledByClient",
            new SecureChannelHandshakeException(
                serviceFault(StatusCodes.Bad_RequestCancelledByClient))),
        Arguments.of(
            "OPN Bad_RequestCancelledByRequest",
            new SecureChannelHandshakeException(
                serviceFault(StatusCodes.Bad_RequestCancelledByRequest))),
        Arguments.of(
            "OPN Bad_ServerTooBusy",
            new SecureChannelHandshakeException(serviceFault(StatusCodes.Bad_ServerTooBusy))),
        Arguments.of(
            "OPN Bad_TcpServerTooBusy",
            new SecureChannelHandshakeException(new UaException(StatusCodes.Bad_TcpServerTooBusy))),
        Arguments.of(
            "OPN Bad_TcpNotEnoughResources",
            new SecureChannelHandshakeException(
                new UaException(StatusCodes.Bad_TcpNotEnoughResources))));
  }

  // Cause-chain walking must terminate cleanly if an exception chain contains a cycle.
  @Test
  void handlesCyclicCauseChainSafely() {
    var first = new RuntimeException("first");
    var second = new RuntimeException("second", first);
    first.initCause(second);

    assertFalse(DiscoveryFallback.shouldFallback(first));
  }

  // toUaException must preserve UaException subclasses directly, map TimeoutException to
  // Bad_Timeout, and wrap non-UaException causes without double-wrapping.
  @Test
  void toUaExceptionPreservesSubclassesAndMapsTimeout() {
    var plain = new UaException(StatusCodes.Bad_TcpEndpointUrlInvalid, "bad url");
    assertSame(plain, DiscoveryFallback.toUaException(plain));

    UaServiceFaultException fault = serviceFault(StatusCodes.Bad_ServiceUnsupported);
    assertSame(
        fault,
        DiscoveryFallback.toUaException(new ExecutionException(new CompletionException(fault))));

    var handshake =
        new SecureChannelHandshakeException(new UaException(StatusCodes.Bad_ConnectionClosed));
    assertSame(handshake, DiscoveryFallback.toUaException(handshake));

    var timeout = new TimeoutException("deadline exceeded");
    UaException mappedTimeout = DiscoveryFallback.toUaException(timeout);
    assertEquals(StatusCodes.Bad_Timeout, mappedTimeout.getStatusCode().value());
    assertSame(timeout, mappedTimeout.getCause());

    var dns = new UnknownHostException("does-not-exist.invalid");
    UaException wrappedDns = DiscoveryFallback.toUaException(dns);
    assertSame(dns, wrappedDns.getCause());
  }

  private static UaServiceFaultException serviceFault(long statusCode) {
    var header =
        new ResponseHeader(DateTime.now(), uint(1), new StatusCode(statusCode), null, null, null);
    return new UaServiceFaultException(new ServiceFault(header));
  }
}
