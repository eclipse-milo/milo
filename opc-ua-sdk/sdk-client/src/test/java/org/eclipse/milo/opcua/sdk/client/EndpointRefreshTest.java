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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.ConnectException;
import java.net.SocketException;
import java.security.cert.CertPathBuilderException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.transport.client.SecureChannelHandshakeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class EndpointRefreshTest {

  private static final UaException CERTIFICATE_INVALID =
      new SecureChannelHandshakeException(new UaException(StatusCodes.Bad_CertificateInvalid));

  // A failed OPN can mean stale discovery even when the peer provides no useful security status.
  @ParameterizedTest
  @MethodSource("handshakeFailures")
  void refreshesAfterHandshakeFailureRegardlessOfCause(Throwable cause) {
    var refresh =
        new EndpointRefresh(endpoint(1), () -> CompletableFuture.completedFuture(endpoint(2)));
    refresh.start();
    refresh.onConnectFailure(new CompletionException(new SecureChannelHandshakeException(cause)));
    assertEquals(endpoint(2), refresh.current());
  }

  static Stream<Throwable> handshakeFailures() {
    return Stream.of(
        new UaException(StatusCodes.Bad_CertificateInvalid),
        new UaException(StatusCodes.Bad_SecurityChecksFailed),
        new UaException(StatusCodes.Bad_UnexpectedError),
        new UaException(StatusCodes.Bad_Timeout),
        new UaException(StatusCodes.Bad_ConnectionClosed),
        new SocketException("connection reset"),
        new UaException(
            StatusCodes.Bad_SecurityChecksFailed, new CertPathBuilderException("untrusted")));
  }

  // The same status before OPN, including a HEL rejection, must not trigger discovery.
  @ParameterizedTest
  @ValueSource(
      longs = {
        StatusCodes.Bad_CertificateInvalid, StatusCodes.Bad_SecurityChecksFailed,
        StatusCodes.Bad_Timeout, StatusCodes.Bad_ConnectionRejected,
        StatusCodes.Bad_UserAccessDenied, StatusCodes.Bad_IdentityTokenRejected
      })
  void ignoresFailuresOutsideSecureChannelEstablishment(long code) {
    assertFalse(EndpointRefresh.isRefreshTrigger(new CompletionException(new UaException(code))));
  }

  @Test
  void ignoresTcpConnectionFailure() {
    assertFalse(EndpointRefresh.isRefreshTrigger(new ConnectException()));
  }

  @Test
  void cancellationDuringHandshakeDoesNotRefresh() {
    assertFalse(
        EndpointRefresh.isRefreshTrigger(
            new SecureChannelHandshakeException(new CancellationException())));
  }

  // A qualifying failure refreshes at once; the replacement is visible to the next attempt.
  @Test
  void qualifyingFailureReplacesEndpoint() throws Exception {
    var resolved = new CompletableFuture<EndpointConfiguration>();
    var refresh = new EndpointRefresh(endpoint(1), () -> resolved);
    refresh.start();
    refresh.onConnectFailure(CERTIFICATE_INVALID);
    assertEquals(endpoint(1), refresh.current(), "cached endpoint stays until discovery completes");
    resolved.complete(endpoint(2));
    assertEquals(endpoint(2), refresh.current());
  }

  // An unsecured endpoint has no certificate binding, and unrelated failures are not a reason to
  // rediscover.
  @Test
  void unsecuredEndpointAndUnrelatedFailuresDoNotRefresh() {
    var calls = new AtomicInteger();
    EndpointResolver counting =
        () -> CompletableFuture.completedFuture(endpoint(calls.incrementAndGet() + 1));
    var secured = new EndpointRefresh(endpoint(1), counting);
    secured.start();
    secured.onConnectFailure(new UaException(StatusCodes.Bad_ConnectionRejected));
    var unsecured = new EndpointRefresh(endpoint(1, MessageSecurityMode.None), counting);
    unsecured.start();
    unsecured.onConnectFailure(CERTIFICATE_INVALID);
    assertEquals(0, calls.get());
  }

  // Failures reported while a refresh is in flight must share it rather than start another.
  @Test
  void concurrentFailuresShareOneResolution() throws Exception {
    var source = new CompletableFuture<EndpointConfiguration>();
    var calls = new AtomicInteger();
    var refresh =
        new EndpointRefresh(
            endpoint(1),
            () -> {
              calls.incrementAndGet();
              return source;
            });
    refresh.start();
    CompletableFuture<?>[] reporters = new CompletableFuture[16];
    for (int i = 0; i < reporters.length; i++) {
      reporters[i] =
          CompletableFuture.runAsync(() -> refresh.onConnectFailure(CERTIFICATE_INVALID));
    }
    CompletableFuture.allOf(reporters).get(5, TimeUnit.SECONDS);
    assertEquals(1, calls.get());
    source.complete(endpoint(2));
    assertEquals(endpoint(2), refresh.current());
  }

  // Part 4 6.7: a temporary GetEndpoints failure cannot strand later reconnects, but repeated
  // failures must not flood discovery.
  @Test
  void retriesOutageWithBoundedFrequency() {
    var calls = new AtomicInteger();
    var time = new AtomicLong();
    var refresh =
        new EndpointRefresh(
            endpoint(1),
            () -> {
              int attempt = calls.incrementAndGet();
              if (attempt == 1)
                return CompletableFuture.failedFuture(new UaException(StatusCodes.Bad_Timeout));
              return CompletableFuture.completedFuture(endpoint(attempt == 2 ? 1 : 2));
            },
            time::get);
    refresh.start();
    refresh.onConnectFailure(CERTIFICATE_INVALID);
    assertEquals(endpoint(1), refresh.current());
    for (int i = 0; i < 100; i++) refresh.onConnectFailure(CERTIFICATE_INVALID);
    assertEquals(1, calls.get(), "no refresh inside the 30 s cooldown");
    time.set(TimeUnit.SECONDS.toNanos(30));
    refresh.onConnectFailure(CERTIFICATE_INVALID);
    assertEquals(2, calls.get());
    time.set(TimeUnit.SECONDS.toNanos(89));
    refresh.onConnectFailure(CERTIFICATE_INVALID);
    assertEquals(2, calls.get(), "later refreshes wait out the doubled 60 s cooldown");
    time.set(TimeUnit.SECONDS.toNanos(90));
    refresh.onConnectFailure(CERTIFICATE_INVALID);
    assertEquals(3, calls.get());
    assertEquals(endpoint(2), refresh.current());
  }

  // The cooldown belongs to the resolver, so a deployment can trade discovery load for recovery
  // latency without a client-wide setting.
  @Test
  void resolverCooldownScalesRefreshSchedule() {
    var calls = new AtomicInteger();
    var time = new AtomicLong();
    EndpointResolver resolver =
        new EndpointResolver() {
          @Override
          public CompletableFuture<EndpointConfiguration> resolve() {
            return CompletableFuture.completedFuture(endpoint(calls.incrementAndGet() + 1));
          }

          @Override
          public Duration getRefreshCooldown() {
            return Duration.ofSeconds(2);
          }
        };
    var refresh = new EndpointRefresh(endpoint(1), resolver, time::get);
    refresh.start();
    refresh.onConnectFailure(CERTIFICATE_INVALID);
    assertEquals(1, calls.get());
    time.set(TimeUnit.SECONDS.toNanos(1));
    refresh.onConnectFailure(CERTIFICATE_INVALID);
    assertEquals(1, calls.get(), "second refresh must wait out the 2 s cooldown");
    time.set(TimeUnit.SECONDS.toNanos(2));
    refresh.onConnectFailure(CERTIFICATE_INVALID);
    assertEquals(2, calls.get());
    time.set(TimeUnit.SECONDS.toNanos(5));
    refresh.onConnectFailure(CERTIFICATE_INVALID);
    assertEquals(2, calls.get(), "third refresh must wait out the doubled 4 s cooldown");
    time.set(TimeUnit.SECONDS.toNanos(6));
    refresh.onConnectFailure(CERTIFICATE_INVALID);
    assertEquals(3, calls.get());
  }

  // A stopped client must neither publish a late result nor keep the resolver running.
  @Test
  void stopCancelsRefreshAndDiscardsLateCompletion() {
    var source =
        new CompletableFuture<EndpointConfiguration>() {
          @Override
          public boolean cancel(boolean mayInterrupt) {
            return false;
          }
        };
    var cancelled = new CompletableFuture<EndpointConfiguration>();
    var calls = new AtomicInteger();
    var refresh =
        new EndpointRefresh(endpoint(1), () -> calls.incrementAndGet() == 1 ? source : cancelled);
    refresh.start();
    refresh.onConnectFailure(CERTIFICATE_INVALID);
    refresh.stop();
    source.complete(endpoint(2));
    assertEquals(endpoint(1), refresh.current());
    refresh.onConnectFailure(CERTIFICATE_INVALID);
    assertEquals(1, calls.get(), "no refresh after stop");
    refresh.start();
    refresh.onConnectFailure(CERTIFICATE_INVALID);
    refresh.stop();
    assertTrue(cancelled.isCancelled());
  }

  // Security failure is a reason to rediscover, never authorization to select a weaker endpoint.
  @ParameterizedTest(name = "{0}")
  @MethodSource("nonEquivalentCandidates")
  void rejectsNonEquivalentReplacement(String difference, EndpointDescription candidate) {
    assertThrows(
        UaException.class,
        () ->
            EndpointRefresh.validateEquivalent(
                endpoint(1), new EndpointConfiguration(candidate, List.of(candidate))),
        () -> "a replacement with a different " + difference + " must be rejected");
  }

  static Stream<Arguments> nonEquivalentCandidates() {
    String url = endpoint(1).endpoint().getEndpointUrl();
    SecurityPolicy policy = SecurityPolicy.Basic256Sha256;
    MessageSecurityMode mode = MessageSecurityMode.SignAndEncrypt;
    return Stream.of(
        Arguments.of(
            "endpoint URL", candidate("opc.tcp://other:4840", "urn:server", "tcp", policy, mode)),
        Arguments.of("server URI", candidate(url, "urn:other", "tcp", policy, mode)),
        Arguments.of(
            "transport", candidate(url, "urn:server", "different-transport", policy, mode)),
        Arguments.of(
            "security policy", candidate(url, "urn:server", "tcp", SecurityPolicy.None, mode)),
        Arguments.of(
            "security mode", candidate(url, "urn:server", "tcp", policy, MessageSecurityMode.None)),
        // GetEndpoints is unauthenticated; a refreshed None username policy on a Sign channel would
        // send the password in the clear on the next ActivateSession.
        Arguments.of(
            "user token policies",
            candidate(
                url,
                "urn:server",
                "tcp",
                policy,
                mode,
                new UserTokenPolicy(
                    "username-none",
                    UserTokenType.UserName,
                    null,
                    null,
                    SecurityPolicy.None.getUri()))));
  }

  // A refresh reports the selected endpoint; the discovery list around it may differ harmlessly.
  @Test
  void acceptsReplacementWhoseDiscoveryListChangedAroundTheSameEndpoint() throws Exception {
    EndpointDescription selected = endpoint(1).endpoint();
    EndpointDescription other =
        candidate(
            "opc.tcp://other:4840",
            "urn:server",
            "tcp",
            SecurityPolicy.Basic256Sha256,
            MessageSecurityMode.SignAndEncrypt);
    EndpointRefresh.validateEquivalent(
        endpoint(1), new EndpointConfiguration(selected, List.of(selected, other)));
  }

  // The transport reconnects without calling start(), so a cooldown doubled by an earlier outage
  // would otherwise delay recovery from a second rotation for the client's whole lifetime.
  @Test
  void successfulConnectionResetsCooldown() {
    var calls = new AtomicInteger();
    var time = new AtomicLong();
    var refresh =
        new EndpointRefresh(
            endpoint(1),
            () -> CompletableFuture.completedFuture(endpoint(calls.incrementAndGet() + 1)),
            time::get);
    refresh.start();
    refresh.onConnectFailure(CERTIFICATE_INVALID);
    time.set(TimeUnit.SECONDS.toNanos(30));
    refresh.onConnectFailure(CERTIFICATE_INVALID);
    assertEquals(2, calls.get());
    time.set(TimeUnit.SECONDS.toNanos(31));
    refresh.onConnectFailure(CERTIFICATE_INVALID);
    assertEquals(2, calls.get(), "baseline: the doubled 60 s cooldown still applies");

    refresh.onConnected();
    refresh.onConnectFailure(CERTIFICATE_INVALID);
    assertEquals(3, calls.get(), "first failure after a working connection refreshes at once");
    time.set(TimeUnit.SECONDS.toNanos(60));
    refresh.onConnectFailure(CERTIFICATE_INVALID);
    assertEquals(3, calls.get(), "the cooldown restarts at its base 30 s, not zero");
    time.set(TimeUnit.SECONDS.toNanos(61));
    refresh.onConnectFailure(CERTIFICATE_INVALID);
    assertEquals(4, calls.get());
  }

  // A stopped refresh must not be reactivated by a late channel-ready notification.
  @Test
  void connectedAfterStopDoesNotRefresh() {
    var calls = new AtomicInteger();
    var refresh =
        new EndpointRefresh(
            endpoint(1),
            () -> CompletableFuture.completedFuture(endpoint(calls.incrementAndGet() + 1)));
    refresh.start();
    refresh.stop();
    refresh.onConnected();
    refresh.onConnectFailure(CERTIFICATE_INVALID);
    assertEquals(0, calls.get());
  }

  // A no-match selection or a resolver that throws retains the old endpoint and stays retryable.
  @Test
  void noMatchAndSynchronousResolverFailureRemainRetryable() {
    var time = new AtomicLong();
    var calls = new AtomicInteger();
    var refresh =
        new EndpointRefresh(
            endpoint(1),
            () -> {
              if (calls.incrementAndGet() == 1)
                throw new IllegalStateException("resolver unavailable");
              if (calls.get() == 2)
                return CompletableFuture.failedFuture(
                    new UaException(StatusCodes.Bad_ConfigurationError));
              return CompletableFuture.completedFuture(endpoint(2));
            },
            time::get);
    refresh.start();
    refresh.onConnectFailure(CERTIFICATE_INVALID);
    time.set(TimeUnit.SECONDS.toNanos(30));
    refresh.onConnectFailure(CERTIFICATE_INVALID);
    assertEquals(endpoint(1), refresh.current());
    time.set(TimeUnit.SECONDS.toNanos(90));
    refresh.onConnectFailure(CERTIFICATE_INVALID);
    assertEquals(endpoint(2), refresh.current());
  }

  // A custom resolver that never finishes must be cancelled and must not block later refreshes.
  @Test
  void timeoutCancelsResolverAndPreservesCachedEndpoint() throws Exception {
    var source = new CompletableFuture<EndpointConfiguration>();
    EndpointResolver resolver =
        new EndpointResolver() {
          @Override
          public CompletableFuture<EndpointConfiguration> resolve() {
            return source;
          }

          @Override
          public Duration getTimeout() {
            return Duration.ofMillis(50);
          }
        };
    var refresh = new EndpointRefresh(endpoint(1), resolver);
    refresh.start();
    refresh.onConnectFailure(CERTIFICATE_INVALID);
    source.copy().exceptionally(e -> null).get(2, TimeUnit.SECONDS);
    assertTrue(source.isCancelled());
    assertEquals(endpoint(1), refresh.current());
  }

  private static EndpointDescription candidate(
      String url,
      String uri,
      String transport,
      SecurityPolicy policy,
      MessageSecurityMode mode,
      UserTokenPolicy... userIdentityTokens) {
    return new EndpointDescription(
        url,
        server(uri),
        ByteString.of(new byte[] {2}),
        mode,
        policy.getUri(),
        userIdentityTokens.length == 0 ? null : userIdentityTokens,
        transport,
        null);
  }

  static EndpointConfiguration endpoint(int certificate) {
    return endpoint(certificate, MessageSecurityMode.SignAndEncrypt);
  }

  static EndpointConfiguration endpoint(int certificate, MessageSecurityMode mode) {
    var endpoint =
        new EndpointDescription(
            "opc.tcp://localhost:4840",
            server("urn:server"),
            ByteString.of(new byte[] {(byte) certificate}),
            mode,
            mode == MessageSecurityMode.None
                ? SecurityPolicy.None.getUri()
                : SecurityPolicy.Basic256Sha256.getUri(),
            null,
            "tcp",
            null);
    return new EndpointConfiguration(endpoint, List.of(endpoint));
  }

  private static ApplicationDescription server(String uri) {
    return new ApplicationDescription(
        uri, "product", LocalizedText.english("server"), ApplicationType.Server, null, null, null);
  }
}
