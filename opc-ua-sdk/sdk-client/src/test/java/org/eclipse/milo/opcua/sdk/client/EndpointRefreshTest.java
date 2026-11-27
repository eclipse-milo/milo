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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.security.cert.CertPathBuilderException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaServiceFaultException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceFault;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.transport.client.SecureChannelHandshakeException;
import org.junit.jupiter.api.Nested;
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

  @Nested
  class Notifications {

    // Listeners observing a refresh must see current() already updated to the adopted endpoint.
    @Test
    void adoptedRefreshNotifiesListenersAfterUpdatingCurrent() {
      var initial = endpoint(1);
      var replacement = endpoint(2);
      var refresh =
          new EndpointRefresh(initial, () -> CompletableFuture.completedFuture(replacement));
      var observedPrevious = new AtomicReference<EndpointConfiguration>();
      var observedCurrent = new AtomicReference<EndpointConfiguration>();
      var observedDuringCallback = new AtomicReference<EndpointConfiguration>();
      var failures = new AtomicInteger();

      refresh.addListener(
          new EndpointRefreshListener() {
            @Override
            public void onEndpointRefreshed(
                EndpointConfiguration previous, EndpointConfiguration current) {
              observedPrevious.set(previous);
              observedCurrent.set(current);
              observedDuringCallback.set(refresh.current());
            }

            @Override
            public void onEndpointRefreshFailed(Throwable failure) {
              failures.incrementAndGet();
            }
          });

      refresh.start();
      refresh.onConnectFailure(CERTIFICATE_INVALID);

      assertEquals(initial, observedPrevious.get());
      assertEquals(replacement, observedCurrent.get());
      assertEquals(
          replacement,
          observedDuringCallback.get(),
          "current() must already return the adopted endpoint when onEndpointRefreshed runs");
      assertEquals(0, failures.get());
    }

    // A refresh that keeps the same selected endpoint should still update discoveryEndpoints
    // without reporting an endpoint change or a failure.
    @Test
    void unchangedSelectedEndpointUpdatesDiscoveryListWithoutNotifyingListeners() {
      EndpointConfiguration initial = endpoint(1);
      EndpointDescription other =
          candidate(
              "opc.tcp://other:4840",
              "urn:server",
              "tcp",
              SecurityPolicy.Basic256Sha256,
              MessageSecurityMode.SignAndEncrypt);
      EndpointConfiguration sameSelected =
          new EndpointConfiguration(initial.endpoint(), List.of(initial.endpoint(), other));
      var refresh =
          new EndpointRefresh(initial, () -> CompletableFuture.completedFuture(sameSelected));
      var refreshes = new AtomicInteger();
      var failures = new AtomicInteger();

      refresh.addListener(
          new EndpointRefreshListener() {
            @Override
            public void onEndpointRefreshed(
                EndpointConfiguration previous, EndpointConfiguration current) {
              refreshes.incrementAndGet();
            }

            @Override
            public void onEndpointRefreshFailed(Throwable failure) {
              failures.incrementAndGet();
            }
          });

      refresh.start();
      refresh.onConnectFailure(CERTIFICATE_INVALID);

      assertEquals(
          List.of(initial.endpoint(), other),
          refresh.current().discoveryEndpoints(),
          "discoveryEndpoints must still be updated");
      assertEquals(0, refreshes.get(), "unchanged selected endpoint must not fire refreshed");
      assertEquals(0, failures.get(), "unchanged selected endpoint must not fire failure");
    }

    // A synchronous throw from resolve() must notify listeners and retain the cached endpoint.
    @Test
    void synchronousResolverThrowNotifiesFailureAndRetainsCachedEndpoint() {
      var thrown = new IllegalStateException("resolver unavailable");
      var refresh =
          new EndpointRefresh(
              endpoint(1),
              () -> {
                throw thrown;
              });
      var observedFailure = new AtomicReference<Throwable>();

      refresh.addListener(
          new EndpointRefreshListener() {
            @Override
            public void onEndpointRefreshFailed(Throwable failure) {
              observedFailure.set(failure);
            }
          });

      refresh.start();
      refresh.onConnectFailure(CERTIFICATE_INVALID);

      assertSame(thrown, observedFailure.get());
      assertEquals(endpoint(1), refresh.current());
    }

    // CompletionException and ExecutionException wrappers must be unwrapped so callers preserve
    // UaException subtypes and StatusCodes without double-wrapping.
    @Test
    void asyncFailuresUnwrapWrappersAndPreserveUaExceptionSubtypes() {
      var time = new AtomicLong();
      var serviceFault =
          new UaServiceFaultException(
              new ServiceFault(
                  new ResponseHeader(
                      null,
                      null,
                      new StatusCode(StatusCodes.Bad_ServiceUnsupported),
                      null,
                      null,
                      null)));
      var nestedUa = new UaException(StatusCodes.Bad_CommunicationError, "broken pipe");
      var ioFailure = new IOException("discovery socket closed");
      var attempt = new AtomicInteger();
      var refresh =
          new EndpointRefresh(
              endpoint(1),
              () ->
                  switch (attempt.incrementAndGet()) {
                    case 1 -> CompletableFuture.failedFuture(new CompletionException(serviceFault));
                    case 2 ->
                        CompletableFuture.failedFuture(
                            new CompletionException(new ExecutionException(nestedUa)));
                    default -> CompletableFuture.failedFuture(new CompletionException(ioFailure));
                  },
              time::get);
      var observed = new CopyOnWriteArrayList<Throwable>();

      refresh.addListener(
          new EndpointRefreshListener() {
            @Override
            public void onEndpointRefreshFailed(Throwable failure) {
              observed.add(failure);
            }
          });

      refresh.start();
      refresh.onConnectFailure(CERTIFICATE_INVALID);
      time.set(TimeUnit.SECONDS.toNanos(30));
      refresh.onConnectFailure(CERTIFICATE_INVALID);
      time.set(TimeUnit.SECONDS.toNanos(90));
      refresh.onConnectFailure(CERTIFICATE_INVALID);

      assertEquals(3, observed.size());
      assertSame(serviceFault, observed.get(0));
      assertEquals(
          StatusCodes.Bad_ServiceUnsupported,
          UaException.extractStatusCode(observed.get(0)).orElseThrow().value());
      assertSame(nestedUa, observed.get(1));
      assertEquals(
          StatusCodes.Bad_CommunicationError,
          UaException.extractStatusCode(observed.get(1)).orElseThrow().value());
      assertSame(ioFailure, observed.get(2));
      assertEquals(endpoint(1), refresh.current());
    }

    // A buggy resolver returning a null future or completing with null must notify failure, retain
    // the cached endpoint, and leave subsequent refreshes retryable.
    @Test
    void nullFutureAndNullResultNotifyFailureAndRemainRetryable() {
      var time = new AtomicLong();
      var calls = new AtomicInteger();
      var refresh =
          new EndpointRefresh(
              endpoint(1),
              () ->
                  switch (calls.incrementAndGet()) {
                    case 1 -> null;
                    case 2 -> CompletableFuture.completedFuture(null);
                    default -> CompletableFuture.completedFuture(endpoint(2));
                  },
              time::get);
      var failures = new CopyOnWriteArrayList<Throwable>();
      var refreshed = new AtomicReference<EndpointConfiguration>();

      refresh.addListener(
          new EndpointRefreshListener() {
            @Override
            public void onEndpointRefreshed(
                EndpointConfiguration previous, EndpointConfiguration current) {
              refreshed.set(current);
            }

            @Override
            public void onEndpointRefreshFailed(Throwable failure) {
              failures.add(failure);
            }
          });

      refresh.start();
      refresh.onConnectFailure(CERTIFICATE_INVALID);
      assertEquals(endpoint(1), refresh.current());

      time.set(TimeUnit.SECONDS.toNanos(30));
      refresh.onConnectFailure(CERTIFICATE_INVALID);
      assertEquals(endpoint(1), refresh.current());

      time.set(TimeUnit.SECONDS.toNanos(90));
      refresh.onConnectFailure(CERTIFICATE_INVALID);

      assertEquals(2, failures.size());
      assertInstanceOf(NullPointerException.class, failures.get(0));
      assertInstanceOf(NullPointerException.class, failures.get(1));
      assertEquals(endpoint(2), refreshed.get());
      assertEquals(endpoint(2), refresh.current());
    }

    // Refresh deadline TimeoutException must be normalized to UaException(Bad_Timeout) so callers
    // can inspect every failure uniformly via UaException.extractStatusCode.
    @Test
    void deadlineTimeoutNormalizesToBadTimeout() throws Exception {
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
      var failureFuture = new CompletableFuture<Throwable>();

      refresh.addListener(
          new EndpointRefreshListener() {
            @Override
            public void onEndpointRefreshFailed(Throwable failure) {
              failureFuture.complete(failure);
            }
          });

      refresh.start();
      refresh.onConnectFailure(CERTIFICATE_INVALID);

      Throwable failure = failureFuture.get(2, TimeUnit.SECONDS);
      UaException uaException = assertInstanceOf(UaException.class, failure);
      assertEquals(
          StatusCodes.Bad_Timeout,
          UaException.extractStatusCode(uaException).orElseThrow().value());
      assertInstanceOf(TimeoutException.class, uaException.getCause());
      assertTrue(source.isCancelled());
      assertEquals(endpoint(1), refresh.current());
    }

    // Equivalence rejection must report Bad_ConfigurationError so applications can distinguish
    // permanent refusals from transient discovery errors.
    @Test
    void nonEquivalentReplacementNotifiesBadConfigurationError() {
      EndpointDescription nonEquivalent =
          candidate(
              endpoint(1).endpoint().getEndpointUrl(),
              "urn:server",
              "tcp",
              SecurityPolicy.Basic256Sha256,
              MessageSecurityMode.SignAndEncrypt,
              new UserTokenPolicy(
                  "username-none",
                  UserTokenType.UserName,
                  null,
                  null,
                  SecurityPolicy.None.getUri()));
      var refresh =
          new EndpointRefresh(
              endpoint(1),
              () ->
                  CompletableFuture.completedFuture(
                      new EndpointConfiguration(nonEquivalent, List.of(nonEquivalent))));
      var observedFailure = new AtomicReference<Throwable>();
      var refreshes = new AtomicInteger();

      refresh.addListener(
          new EndpointRefreshListener() {
            @Override
            public void onEndpointRefreshed(
                EndpointConfiguration previous, EndpointConfiguration current) {
              refreshes.incrementAndGet();
            }

            @Override
            public void onEndpointRefreshFailed(Throwable failure) {
              observedFailure.set(failure);
            }
          });

      refresh.start();
      refresh.onConnectFailure(CERTIFICATE_INVALID);

      UaException uaException = assertInstanceOf(UaException.class, observedFailure.get());
      assertEquals(
          StatusCodes.Bad_ConfigurationError,
          UaException.extractStatusCode(uaException).orElseThrow().value());
      assertEquals(0, refreshes.get());
      assertEquals(endpoint(1), refresh.current());
    }

    // Calling stop() before a refresh finishes must suppress both late success and late failure
    // callbacks, including when stop() races with a synchronous resolver failure.
    @Test
    void stopBeforeCompletionSuppressesLateCallbacks() throws Exception {
      var lateSuccess =
          new CompletableFuture<EndpointConfiguration>() {
            @Override
            public boolean cancel(boolean mayInterrupt) {
              return false;
            }
          };
      var lateFailure =
          new CompletableFuture<EndpointConfiguration>() {
            @Override
            public boolean cancel(boolean mayInterrupt) {
              return false;
            }
          };
      var resolveEntered = new CountDownLatch(1);
      var releaseResolve = new CountDownLatch(1);
      var calls = new AtomicInteger();
      var refresh =
          new EndpointRefresh(
              endpoint(1),
              () ->
                  switch (calls.incrementAndGet()) {
                    case 1 -> lateSuccess;
                    case 2 -> lateFailure;
                    default -> {
                      resolveEntered.countDown();
                      try {
                        releaseResolve.await(5, TimeUnit.SECONDS);
                      } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                      }
                      throw new IllegalStateException("late sync failure");
                    }
                  });
      var refreshes = new AtomicInteger();
      var failures = new AtomicInteger();

      refresh.addListener(
          new EndpointRefreshListener() {
            @Override
            public void onEndpointRefreshed(
                EndpointConfiguration previous, EndpointConfiguration current) {
              refreshes.incrementAndGet();
            }

            @Override
            public void onEndpointRefreshFailed(Throwable failure) {
              failures.incrementAndGet();
            }
          });

      // Late async success after stop()
      refresh.start();
      refresh.onConnectFailure(CERTIFICATE_INVALID);
      refresh.stop();
      lateSuccess.complete(endpoint(2));
      assertEquals(endpoint(1), refresh.current());

      // Late async failure after stop()
      refresh.start();
      refresh.onConnectFailure(CERTIFICATE_INVALID);
      refresh.stop();
      lateFailure.completeExceptionally(new UaException(StatusCodes.Bad_Timeout));

      // Concurrent stop() while resolve() is executing before throwing synchronously
      refresh.start();
      CompletableFuture<Void> racingFailure =
          CompletableFuture.runAsync(() -> refresh.onConnectFailure(CERTIFICATE_INVALID));
      assertTrue(resolveEntered.await(5, TimeUnit.SECONDS));
      refresh.stop();
      releaseResolve.countDown();
      racingFailure.get(5, TimeUnit.SECONDS);

      assertEquals(0, refreshes.get(), "late success after stop() must not notify");
      assertEquals(0, failures.get(), "late failures after stop() must not notify");
    }

    // A failing listener must not prevent subsequent listeners or later notifications from running.
    @Test
    void listenerExceptionsAreIsolated() {
      var time = new AtomicLong();
      var calls = new AtomicInteger();
      var refresh =
          new EndpointRefresh(
              endpoint(1),
              () ->
                  calls.incrementAndGet() == 1
                      ? CompletableFuture.failedFuture(new UaException(StatusCodes.Bad_Timeout))
                      : CompletableFuture.completedFuture(endpoint(2)),
              time::get);
      var events = new CopyOnWriteArrayList<String>();

      refresh.addListener(
          new EndpointRefreshListener() {
            @Override
            public void onEndpointRefreshed(
                EndpointConfiguration previous, EndpointConfiguration current) {
              throw new RuntimeException("boom refresh");
            }

            @Override
            public void onEndpointRefreshFailed(Throwable failure) {
              throw new RuntimeException("boom failure");
            }
          });
      refresh.addListener(
          new EndpointRefreshListener() {
            @Override
            public void onEndpointRefreshed(
                EndpointConfiguration previous, EndpointConfiguration current) {
              throw new AssertionError("error refresh");
            }

            @Override
            public void onEndpointRefreshFailed(Throwable failure) {
              throw new AssertionError("error failure");
            }
          });
      refresh.addListener(
          new EndpointRefreshListener() {
            @Override
            public void onEndpointRefreshed(
                EndpointConfiguration previous, EndpointConfiguration current) {
              events.add(
                  "refreshed:" + current.endpoint().getServerCertificate().bytesOrEmpty()[0]);
            }

            @Override
            public void onEndpointRefreshFailed(Throwable failure) {
              events.add("failed:" + UaException.extractStatusCode(failure).orElseThrow().value());
            }
          });

      refresh.start();
      refresh.onConnectFailure(CERTIFICATE_INVALID);
      time.set(TimeUnit.SECONDS.toNanos(30));
      refresh.onConnectFailure(CERTIFICATE_INVALID);

      assertEquals(List.of("failed:" + StatusCodes.Bad_Timeout, "refreshed:2"), events);
    }

    // Listener callbacks must execute serially in FIFO order even on a multi-threaded executor.
    @Test
    void notificationsDeliverSeriallyInFifoOrder() throws Exception {
      ExecutorService executor = Executors.newFixedThreadPool(4);
      try {
        var time = new AtomicLong();
        var calls = new AtomicInteger();
        EndpointResolver resolver =
            new EndpointResolver() {
              @Override
              public CompletableFuture<EndpointConfiguration> resolve() {
                return switch (calls.incrementAndGet()) {
                  case 1 -> CompletableFuture.completedFuture(endpoint(2));
                  case 2 ->
                      CompletableFuture.failedFuture(new UaException(StatusCodes.Bad_Timeout));
                  case 3 -> CompletableFuture.completedFuture(endpoint(3));
                  default -> CompletableFuture.completedFuture(endpoint(4));
                };
              }

              @Override
              public Duration getRefreshCooldown() {
                return Duration.ZERO;
              }
            };
        var refresh = new EndpointRefresh(endpoint(1), resolver, time::get, executor);

        var firstEntered = new CountDownLatch(1);
        var releaseFirst = new CountDownLatch(1);
        var allDelivered = new CountDownLatch(4);
        var concurrentCallbacks = new AtomicInteger();
        var maxConcurrentCallbacks = new AtomicInteger();
        var events = new CopyOnWriteArrayList<String>();

        refresh.addListener(
            new EndpointRefreshListener() {
              @Override
              public void onEndpointRefreshed(
                  EndpointConfiguration previous, EndpointConfiguration current) {
                int active = concurrentCallbacks.incrementAndGet();
                maxConcurrentCallbacks.accumulateAndGet(active, Math::max);
                try {
                  int prevCert = previous.endpoint().getServerCertificate().bytesOrEmpty()[0];
                  int currCert = current.endpoint().getServerCertificate().bytesOrEmpty()[0];
                  events.add("refreshed:" + prevCert + "->" + currCert);
                  if (currCert == 2) {
                    firstEntered.countDown();
                    releaseFirst.await(5, TimeUnit.SECONDS);
                  }
                } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                } finally {
                  concurrentCallbacks.decrementAndGet();
                  allDelivered.countDown();
                }
              }

              @Override
              public void onEndpointRefreshFailed(Throwable failure) {
                int active = concurrentCallbacks.incrementAndGet();
                maxConcurrentCallbacks.accumulateAndGet(active, Math::max);
                try {
                  events.add("failed");
                } finally {
                  concurrentCallbacks.decrementAndGet();
                  allDelivered.countDown();
                }
              }
            });

        refresh.start();
        refresh.onConnectFailure(CERTIFICATE_INVALID);
        assertTrue(firstEntered.await(5, TimeUnit.SECONDS));

        // Queue three more outcomes while the first callback is still running.
        refresh.onConnectFailure(CERTIFICATE_INVALID);
        refresh.onConnectFailure(CERTIFICATE_INVALID);
        refresh.onConnectFailure(CERTIFICATE_INVALID);
        releaseFirst.countDown();

        assertTrue(allDelivered.await(5, TimeUnit.SECONDS));
        assertEquals(
            1, maxConcurrentCallbacks.get(), "callbacks must run serially without overlap");
        assertEquals(
            List.of("refreshed:1->2", "failed", "refreshed:2->3", "refreshed:3->4"), events);
      } finally {
        executor.shutdownNow();
      }
    }

    // Removing a listener stops delivering subsequent refresh and failure events to it.
    @Test
    void removedListenerStopsReceivingNotifications() {
      var time = new AtomicLong();
      var calls = new AtomicInteger();
      var refresh =
          new EndpointRefresh(
              endpoint(1),
              () ->
                  switch (calls.incrementAndGet()) {
                    case 1 -> CompletableFuture.completedFuture(endpoint(2));
                    case 2 -> CompletableFuture.completedFuture(endpoint(3));
                    default ->
                        CompletableFuture.failedFuture(new UaException(StatusCodes.Bad_Timeout));
                  },
              time::get);
      var removedEvents = new CopyOnWriteArrayList<String>();
      var keptEvents = new CopyOnWriteArrayList<String>();

      EndpointRefreshListener removable =
          new EndpointRefreshListener() {
            @Override
            public void onEndpointRefreshed(
                EndpointConfiguration previous, EndpointConfiguration current) {
              removedEvents.add("refreshed");
            }

            @Override
            public void onEndpointRefreshFailed(Throwable failure) {
              removedEvents.add("failed");
            }
          };
      EndpointRefreshListener kept =
          new EndpointRefreshListener() {
            @Override
            public void onEndpointRefreshed(
                EndpointConfiguration previous, EndpointConfiguration current) {
              keptEvents.add("refreshed");
            }

            @Override
            public void onEndpointRefreshFailed(Throwable failure) {
              keptEvents.add("failed");
            }
          };

      refresh.addListener(removable);
      refresh.addListener(kept);

      refresh.start();
      refresh.onConnectFailure(CERTIFICATE_INVALID);
      refresh.removeListener(removable);

      time.set(TimeUnit.SECONDS.toNanos(30));
      refresh.onConnectFailure(CERTIFICATE_INVALID);
      time.set(TimeUnit.SECONDS.toNanos(90));
      refresh.onConnectFailure(CERTIFICATE_INVALID);

      assertEquals(List.of("refreshed"), removedEvents);
      assertEquals(List.of("refreshed", "refreshed", "failed"), keptEvents);
    }

    // A retry triggered by another failure (or an immediate stop()+start()) must not start a new
    // resolution while the previous attempt's future cancellation is still running outside the
    // monitor, preserving EndpointResolver's one-resolution-at-a-time guarantee.
    @Test
    void retryDoesNotOverlapPendingCancellationAfterTimeoutOrStop() throws Exception {
      var cancelEntered = new CountDownLatch(1);
      var releaseCancel = new CountDownLatch(1);
      var cancelFinished = new CountDownLatch(1);
      var calls = new AtomicInteger();

      CompletableFuture<EndpointConfiguration> first =
          new CompletableFuture<>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
              cancelEntered.countDown();
              try {
                releaseCancel.await(5, TimeUnit.SECONDS);
              } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
              }
              boolean cancelled = super.cancel(mayInterruptIfRunning);
              cancelFinished.countDown();
              return cancelled;
            }
          };

      EndpointResolver resolver =
          new EndpointResolver() {
            @Override
            public CompletableFuture<EndpointConfiguration> resolve() {
              return calls.incrementAndGet() == 1
                  ? first
                  : CompletableFuture.completedFuture(endpoint(2));
            }

            @Override
            public Duration getTimeout() {
              return Duration.ofMillis(20);
            }

            @Override
            public Duration getRefreshCooldown() {
              return Duration.ZERO;
            }
          };

      var refresh = new EndpointRefresh(endpoint(1), resolver);
      refresh.start();
      refresh.onConnectFailure(CERTIFICATE_INVALID);

      assertTrue(cancelEntered.await(5, TimeUnit.SECONDS), "timeout should begin cancelling first");

      // While cancel(true) is still in flight outside the monitor, a new failure must not overlap.
      refresh.onConnectFailure(CERTIFICATE_INVALID);
      assertEquals(1, calls.get(), "resolver.resolve() must not be called before cancel finishes");

      releaseCancel.countDown();
      assertTrue(cancelFinished.await(5, TimeUnit.SECONDS));

      // Wait briefly for the finally block to release pendingCancels, then verify a retry runs.
      long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
      while (calls.get() < 2 && System.nanoTime() < deadline) {
        refresh.onConnectFailure(CERTIFICATE_INVALID);
        Thread.yield();
      }
      assertEquals(2, calls.get(), "retry must proceed once cancellation completes");
      assertEquals(endpoint(2), refresh.current());
    }

    // Callbacks must run outside EndpointRefresh's monitor so calling stop() re-entrantly or from
    // another thread while a callback is active never deadlocks.
    @Test
    void reentrantStopFromFailureCallbackRunsOutsideLockWithoutDeadlock() throws Exception {
      // Inline executor: verifies stop() re-entrancy even if the executor falls back inline.
      var inlineLockHeld = new AtomicBoolean(true);
      var inlineRefresh =
          new EndpointRefresh(
              endpoint(1),
              () ->
                  CompletableFuture.failedFuture(
                      new UaException(StatusCodes.Bad_ConfigurationError)),
              System::nanoTime,
              Runnable::run);
      inlineRefresh.addListener(
          new EndpointRefreshListener() {
            @Override
            public void onEndpointRefreshFailed(Throwable failure) {
              inlineLockHeld.set(Thread.holdsLock(inlineRefresh));
              inlineRefresh.stop();
            }
          });
      inlineRefresh.start();
      inlineRefresh.onConnectFailure(CERTIFICATE_INVALID);
      assertFalse(inlineLockHeld.get(), "callback must not hold EndpointRefresh monitor");

      // Async executor: verifies another thread calling stop() is not blocked by an active
      // callback.
      ExecutorService executor = Executors.newSingleThreadExecutor();
      try {
        var callbackEntered = new CountDownLatch(1);
        var releaseCallback = new CountDownLatch(1);
        var asyncLockHeld = new AtomicBoolean(true);
        var asyncRefresh =
            new EndpointRefresh(
                endpoint(1),
                () ->
                    CompletableFuture.failedFuture(
                        new UaException(StatusCodes.Bad_ConfigurationError)),
                System::nanoTime,
                executor);
        asyncRefresh.addListener(
            new EndpointRefreshListener() {
              @Override
              public void onEndpointRefreshFailed(Throwable failure) {
                asyncLockHeld.set(Thread.holdsLock(asyncRefresh));
                callbackEntered.countDown();
                try {
                  releaseCallback.await(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                }
              }
            });

        asyncRefresh.start();
        asyncRefresh.onConnectFailure(CERTIFICATE_INVALID);
        assertTrue(callbackEntered.await(5, TimeUnit.SECONDS));

        // If the callback held EndpointRefresh's monitor, stop() here would block until timeout.
        CompletableFuture<Void> stopFuture = CompletableFuture.runAsync(asyncRefresh::stop);
        stopFuture.get(2, TimeUnit.SECONDS);
        releaseCallback.countDown();
        assertFalse(asyncLockHeld.get(), "async callback must not hold EndpointRefresh monitor");
      } finally {
        executor.shutdownNow();
      }
    }
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
