/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.identity;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.slf4j.LoggerFactory;

/**
 * Part 4 (7.40.4): a username token whose SecurityPolicy is {@code None} carries the password as
 * plain UTF-8, which only a {@code SignAndEncrypt} SecureChannel or a TLS transport keeps private.
 * {@link UsernameProvider} sends it as the token policy asks, and logs a WARN when nothing else
 * protects it, so an operator can see that a password crosses the network in the clear.
 *
 * <p>The WARN is the provider's only signal, so these tests read what slf4j-simple, this module's
 * SLF4J binding, writes to {@link System#err}.
 */
class UsernameProviderUnencryptedPasswordWarningTest {

  private static final String PASSWORD = "unencrypted-password-canary";

  private static final ByteString SERVER_NONCE = ByteString.of(new byte[32]);

  private static final String CAPTURE_PROBE = "username-provider-log-capture-probe";

  // Over a None or Sign SecureChannel on opc.tcp, anyone on the path can read the password.
  @ParameterizedTest
  @EnumSource(
      value = MessageSecurityMode.class,
      names = {"None", "Sign"})
  void warnsWhenSecureChannelDoesNotEncryptPassword(MessageSecurityMode securityMode)
      throws Exception {

    EndpointDescription endpoint =
        opcTcpEndpoint(securityMode, Stack.TCP_UASC_UABINARY_TRANSPORT_URI);

    String log = stderrOf(() -> activate(new UsernameProvider("user1", PASSWORD), endpoint));

    assertEquals(1, warningCount(log, endpoint));
  }

  // A SignAndEncrypt SecureChannel, or a transport that runs over TLS, keeps the password private
  // in transit, so a WARN there would be a false alarm.
  @ParameterizedTest
  @MethodSource("endpointsThatEncryptThePassword")
  void doesNotWarnWhenChannelOrTransportEncryptsPassword(EndpointDescription endpoint)
      throws Exception {

    String log = stderrOf(() -> activate(new UsernameProvider("user1", PASSWORD), endpoint));

    assertEquals(0, warningCount(log, endpoint));
  }

  // Session retries and reactivations call the provider again; repeating the WARN on each would
  // bury it.
  @Test
  void warnsOncePerProviderAcrossActivations() throws Exception {
    EndpointDescription endpoint =
        opcTcpEndpoint(MessageSecurityMode.None, Stack.TCP_UASC_UABINARY_TRANSPORT_URI);

    var provider = new UsernameProvider("user1", PASSWORD);

    String log =
        stderrOf(
            () -> {
              activate(provider, endpoint);
              activate(provider, endpoint);
            });

    assertEquals(1, warningCount(log, endpoint));
  }

  // The WARN reports that a password travels in the clear; it must not put the password itself
  // into the log.
  @Test
  void warningDoesNotContainPassword() throws Exception {
    EndpointDescription endpoint =
        opcTcpEndpoint(MessageSecurityMode.None, Stack.TCP_UASC_UABINARY_TRANSPORT_URI);

    String log = stderrOf(() -> activate(new UsernameProvider("user1", PASSWORD), endpoint));

    assertEquals(1, warningCount(log, endpoint), "precondition: the WARN was logged");
    assertFalse(log.contains(PASSWORD), "the log contains the password");
  }

  // An opc.tcp endpoint built by hand may name no transport profile. Its None SecureChannel still
  // carries the password in the clear, so the provider must not take it for a TLS transport.
  @ParameterizedTest
  @NullAndEmptySource
  void warnsForOpcTcpEndpointWithoutTransportProfile(String transportProfileUri) throws Exception {
    EndpointDescription endpoint = opcTcpEndpoint(MessageSecurityMode.None, transportProfileUri);

    String log = stderrOf(() -> activate(new UsernameProvider("user1", PASSWORD), endpoint));

    assertEquals(1, warningCount(log, endpoint));
  }

  private static Stream<Named<EndpointDescription>> endpointsThatEncryptThePassword() {
    return Stream.of(
        Named.of(
            "SignAndEncrypt SecureChannel",
            opcTcpEndpoint(
                MessageSecurityMode.SignAndEncrypt, Stack.TCP_UASC_UABINARY_TRANSPORT_URI)),
        Named.of(
            "HTTPS transport",
            endpoint(
                "https://localhost:12686/milo",
                SecurityPolicy.None,
                MessageSecurityMode.None,
                Stack.HTTPS_UABINARY_TRANSPORT_URI)));
  }

  private static void activate(UsernameProvider provider, EndpointDescription endpoint)
      throws Exception {

    provider.getIdentityToken(new IdentityProviderContext(endpoint, SERVER_NONCE));
  }

  /**
   * Count the provider's unencrypted-password WARNs for {@code endpoint} in {@code log}.
   *
   * <p>The WARN names the endpoint URL, so a WARN another test's provider logs for a different
   * endpoint is not counted.
   */
  private static long warningCount(String log, EndpointDescription endpoint) {
    String warning = "Sending password unencrypted to " + endpoint.getEndpointUrl();

    return Pattern.compile(Pattern.quote(warning)).matcher(log).results().count();
  }

  /**
   * Run {@code action} and return what it wrote to {@link System#err}.
   *
   * <p>slf4j-simple looks up {@link System#err} on every write, so replacing it captures what the
   * provider logs on this thread. A probe logged through the provider's logger proves the capture
   * sees that logger's WARNs; without it, a test that expects no WARN could pass vacuously.
   */
  private static String stderrOf(ThrowingRunnable action) throws Exception {
    PrintStream original = System.err;
    var captured = new ByteArrayOutputStream();

    System.setErr(new PrintStream(captured, true, StandardCharsets.UTF_8));
    try {
      action.run();

      LoggerFactory.getLogger(UsernameProvider.class).warn(CAPTURE_PROBE);
    } finally {
      System.setErr(original);
    }

    String log = captured.toString(StandardCharsets.UTF_8);

    assertTrue(
        log.contains(CAPTURE_PROBE),
        "control: UsernameProvider's WARNs do not reach the captured System.err");

    return log;
  }

  private static EndpointDescription opcTcpEndpoint(
      MessageSecurityMode securityMode, String transportProfileUri) {

    SecurityPolicy securityPolicy =
        securityMode == MessageSecurityMode.None
            ? SecurityPolicy.None
            : SecurityPolicy.Basic256Sha256;

    return endpoint(
        "opc.tcp://localhost:12686/milo", securityPolicy, securityMode, transportProfileUri);
  }

  /**
   * Create an endpoint whose only username token policy has SecurityPolicy {@code None}, so the
   * provider sends the password as plain UTF-8.
   */
  private static EndpointDescription endpoint(
      String endpointUrl,
      SecurityPolicy securityPolicy,
      MessageSecurityMode securityMode,
      String transportProfileUri) {

    var server =
        new ApplicationDescription(
            "urn:eclipse:milo:test:server",
            "urn:eclipse:milo:test",
            null,
            ApplicationType.Server,
            null,
            null,
            null);

    var tokenPolicy =
        new UserTokenPolicy(
            "username", UserTokenType.UserName, null, null, SecurityPolicy.None.getUri());

    return new EndpointDescription(
        endpointUrl,
        server,
        ByteString.NULL_VALUE,
        securityMode,
        securityPolicy.getUri(),
        new UserTokenPolicy[] {tokenPolicy},
        transportProfileUri,
        ubyte(0));
  }

  @FunctionalInterface
  private interface ThrowingRunnable {
    void run() throws Exception;
  }
}
