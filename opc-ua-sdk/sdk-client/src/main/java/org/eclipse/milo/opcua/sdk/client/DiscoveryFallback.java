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

import io.netty.channel.ConnectTimeoutException;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaExceptionStatus;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.transport.client.SecureChannelHandshakeException;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfigBuilder;
import org.jspecify.annotations.Nullable;

/**
 * Resolves initial discovery endpoints for {@link OpcUaClient#create} with a single guarded
 * fallback to {@code /discovery}.
 *
 * <p>Only failures that a separate {@code /discovery} endpoint can plausibly resolve (invalid
 * endpoint URL, security rejection, or a non-timeout/non-capacity SecureChannel handshake failure)
 * trigger the fallback attempt. When both attempts fail during discovery, the first failure is
 * thrown with the fallback failure attached as suppressed.
 */
final class DiscoveryFallback {

  private static final Set<Long> RETRYABLE_STATUS_CODES =
      Set.of(
          StatusCodes.Bad_TcpEndpointUrlInvalid,
          StatusCodes.Bad_SecurityPolicyRejected,
          StatusCodes.Bad_SecurityModeRejected,
          StatusCodes.Bad_SecurityModeInsufficient,
          StatusCodes.Bad_SecurityChecksFailed);

  private static final Set<Long> NON_RETRYABLE_STATUS_CODES =
      Set.of(
          StatusCodes.Bad_Timeout,
          StatusCodes.Bad_RequestTimeout,
          StatusCodes.Bad_ServerTooBusy,
          StatusCodes.Bad_TcpServerTooBusy,
          StatusCodes.Bad_TcpNotEnoughResources,
          StatusCodes.Bad_MaxConnectionsReached,
          StatusCodes.Bad_TooManySessions,
          StatusCodes.Bad_ResourceUnavailable,
          StatusCodes.Bad_ServerHalted,
          StatusCodes.Bad_Shutdown,
          StatusCodes.Bad_OutOfMemory,
          StatusCodes.Bad_RequestCancelledByClient,
          StatusCodes.Bad_RequestCancelledByRequest,
          StatusCodes.Bad_RequestInterrupted);

  private DiscoveryFallback() {}

  record Result(String discoveryUrl, EndpointConfiguration configuration) {}

  static Result resolve(
      String endpointUrl,
      Function<List<EndpointDescription>, Optional<EndpointDescription>> selectEndpoint,
      Consumer<OpcTcpClientTransportConfigBuilder> configureTransport,
      Duration discoveryTimeout)
      throws UaException {

    var initialSelectionStarted = new AtomicBoolean(false);
    try {
      EndpointConfiguration config =
          resolveOnce(
              endpointUrl,
              selectEndpoint,
              configureTransport,
              discoveryTimeout,
              initialSelectionStarted);
      return new Result(endpointUrl, config);
    } catch (ExecutionException first) {
      Throwable firstCause = unwrap(first);
      if (initialSelectionStarted.get()) {
        throw propagateSelectionFailure(firstCause);
      }

      Optional<String> fallbackUrl = fallbackDiscoveryUrl(endpointUrl);
      if (fallbackUrl.isEmpty() || !shouldFallback(firstCause)) {
        throw toUaException(firstCause);
      }

      String discoveryUrl = fallbackUrl.get();
      var fallbackSelectionStarted = new AtomicBoolean(false);
      try {
        EndpointConfiguration config =
            resolveOnce(
                discoveryUrl,
                selectEndpoint,
                configureTransport,
                discoveryTimeout,
                fallbackSelectionStarted);
        return new Result(discoveryUrl, config);
      } catch (ExecutionException second) {
        Throwable secondary = unwrap(second);
        if (fallbackSelectionStarted.get()) {
          throw propagateSelectionFailure(secondary);
        }
        UaException primary = toUaException(firstCause);
        if (secondary != null && secondary != primary) {
          primary.addSuppressed(secondary);
        }
        throw primary;
      } catch (RuntimeException second) {
        UaException primary = toUaException(firstCause);
        primary.addSuppressed(second);
        throw primary;
      } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
        UaException exception = new UaException(StatusCodes.Bad_UnexpectedError, ie);
        if (firstCause != null) {
          exception.addSuppressed(firstCause);
        }
        throw exception;
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  private static EndpointConfiguration resolveOnce(
      String url,
      Function<List<EndpointDescription>, Optional<EndpointDescription>> selectEndpoint,
      Consumer<OpcTcpClientTransportConfigBuilder> configureTransport,
      Duration discoveryTimeout,
      AtomicBoolean selectionStarted)
      throws ExecutionException, InterruptedException {

    Function<List<EndpointDescription>, Optional<EndpointDescription>> trackedSelect =
        endpoints -> {
          selectionStarted.set(true);
          return selectEndpoint.apply(endpoints);
        };

    CompletableFuture<EndpointConfiguration> future =
        EndpointResolver.create(url, trackedSelect, configureTransport, discoveryTimeout).resolve();

    try {
      return future.get();
    } catch (InterruptedException | RuntimeException e) {
      future.cancel(true);
      throw e;
    }
  }

  private static UaException propagateSelectionFailure(Throwable cause) {
    if (cause instanceof RuntimeException runtimeException) {
      throw runtimeException;
    }
    if (cause instanceof Error error) {
      throw error;
    }
    return toUaException(cause);
  }

  static Optional<String> fallbackDiscoveryUrl(@Nullable String endpointUrl) {
    if (endpointUrl == null || endpointUrl.isEmpty()) {
      return Optional.empty();
    }

    int suffixStart = queryOrFragmentIndex(endpointUrl);
    String beforeSuffix = suffixStart >= 0 ? endpointUrl.substring(0, suffixStart) : endpointUrl;
    String suffix = suffixStart >= 0 ? endpointUrl.substring(suffixStart) : "";

    int schemeEnd = beforeSuffix.indexOf("://");
    int authorityStart = schemeEnd >= 0 ? schemeEnd + 3 : 0;
    int pathStart = beforeSuffix.indexOf('/', authorityStart);

    String base;
    if (pathStart < 0) {
      base = beforeSuffix;
    } else {
      String authority = beforeSuffix.substring(0, pathStart);
      String path = stripTrailingSlashes(beforeSuffix.substring(pathStart));
      if (path.endsWith("/discovery")) {
        return Optional.empty();
      }
      base = authority + path;
    }

    return Optional.of(base + "/discovery" + suffix);
  }

  private static int queryOrFragmentIndex(String url) {
    int queryIndex = url.indexOf('?');
    int fragmentIndex = url.indexOf('#');
    if (queryIndex < 0) {
      return fragmentIndex;
    }
    if (fragmentIndex < 0) {
      return queryIndex;
    }
    return Math.min(queryIndex, fragmentIndex);
  }

  private static String stripTrailingSlashes(String path) {
    int end = path.length();
    while (end > 0 && path.charAt(end - 1) == '/') {
      end--;
    }
    return path.substring(0, end);
  }

  static boolean shouldFallback(@Nullable Throwable failure) {
    if (failure == null) {
      return false;
    }

    boolean handshakeFailure = false;
    boolean retryableStatus = false;
    Set<Throwable> visited = Collections.newSetFromMap(new IdentityHashMap<>());

    for (Throwable cause = failure; cause != null && visited.add(cause); cause = cause.getCause()) {
      if (cause instanceof CancellationException
          || cause instanceof TimeoutException
          || cause instanceof io.netty.handler.timeout.TimeoutException
          || cause instanceof SocketTimeoutException
          || cause instanceof ConnectTimeoutException) {
        return false;
      }

      if (cause instanceof SecureChannelHandshakeException) {
        handshakeFailure = true;
      }

      if (cause instanceof UaExceptionStatus statusCarrier) {
        long statusCode = statusCarrier.getStatusCode().value();
        if (NON_RETRYABLE_STATUS_CODES.contains(statusCode)) {
          return false;
        }
        if (RETRYABLE_STATUS_CODES.contains(statusCode)) {
          retryableStatus = true;
        }
      }
    }

    return retryableStatus || handshakeFailure;
  }

  static UaException toUaException(Throwable cause) {
    Throwable unwrapped = unwrap(cause);
    if (unwrapped instanceof UaException uaException) {
      return uaException;
    }
    if (unwrapped instanceof TimeoutException timeoutException) {
      return new UaException(StatusCodes.Bad_Timeout, timeoutException);
    }
    return new UaException(unwrapped);
  }

  private static Throwable unwrap(Throwable failure) {
    Throwable current = failure;
    Set<Throwable> visited = Collections.newSetFromMap(new IdentityHashMap<>());
    while ((current instanceof ExecutionException || current instanceof CompletionException)
        && current.getCause() != null
        && visited.add(current)) {
      current = current.getCause();
    }
    return current;
  }
}
