/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.reverse;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;

/**
 * Common endpoint-selection policies for discovery-first Reverse Connect clients.
 *
 * <p>The policies intentionally work on discovered {@link EndpointDescription}s rather than
 * treating {@code ReverseHello} as identity. {@code ReverseHello} can narrow the endpoint URL to
 * prefer, but the selected endpoint still goes through the normal client security and Session
 * validation path when the production client connects.
 *
 * <p>The default discovery-first helpers use {@link #isNoSecurityAndAnonymous(EndpointDescription)}
 * because their default client configuration uses the anonymous identity provider. Applications
 * that configure a different identity provider or security policy should choose a matching
 * predicate.
 */
public final class ReverseConnectEndpointSelectors {

  private ReverseConnectEndpointSelectors() {}

  /**
   * Return true for endpoints that use no message security.
   *
   * @param endpoint the endpoint to test.
   * @return true if the endpoint is {@link SecurityPolicy#None} and {@link
   *     MessageSecurityMode#None}.
   */
  public static boolean isNoSecurity(EndpointDescription endpoint) {
    requireNonNull(endpoint, "endpoint");

    return SecurityPolicy.None.getUri().equals(endpoint.getSecurityPolicyUri())
        && endpoint.getSecurityMode() == MessageSecurityMode.None;
  }

  /**
   * Return true for endpoints that allow anonymous Session activation.
   *
   * @param endpoint the endpoint to test.
   * @return true if the endpoint advertises an anonymous user token policy.
   */
  public static boolean allowsAnonymous(EndpointDescription endpoint) {
    requireNonNull(endpoint, "endpoint");

    return Stream.of(requireNonNullElse(endpoint.getUserIdentityTokens(), new UserTokenPolicy[0]))
        .filter(Objects::nonNull)
        .anyMatch(policy -> policy.getTokenType() == UserTokenType.Anonymous);
  }

  /**
   * Return true for no-security endpoints that allow anonymous Session activation.
   *
   * <p>This predicate matches the default discovery-first client configuration, which uses
   * no-security transport and the default anonymous identity provider.
   *
   * @param endpoint the endpoint to test.
   * @return true if the endpoint is no-security and advertises an anonymous user token policy.
   */
  public static boolean isNoSecurityAndAnonymous(EndpointDescription endpoint) {
    return isNoSecurity(endpoint) && allowsAnonymous(endpoint);
  }

  /**
   * Select the first endpoint matching {@code predicate}.
   *
   * @param predicate the endpoint predicate.
   * @return an endpoint selector.
   */
  public static ReverseConnectEndpointSelector first(Predicate<EndpointDescription> predicate) {
    requireNonNull(predicate, "predicate");

    return (candidate, endpoints) -> {
      requireNonNull(candidate, "candidate");
      requireNonNull(endpoints, "endpoints");

      return endpoints.stream().filter(predicate).findFirst();
    };
  }

  /**
   * Select the first no-security endpoint.
   *
   * <p>This selector does not check user-token policy. Use {@link #noSecurityAnonymous()} for the
   * default anonymous client configuration.
   *
   * @return an endpoint selector.
   */
  public static ReverseConnectEndpointSelector noSecurity() {
    return first(ReverseConnectEndpointSelectors::isNoSecurity);
  }

  /**
   * Select the first no-security endpoint that allows anonymous Session activation.
   *
   * @return an endpoint selector.
   */
  public static ReverseConnectEndpointSelector noSecurityAnonymous() {
    return first(ReverseConnectEndpointSelectors::isNoSecurityAndAnonymous);
  }

  /**
   * Select the endpoint whose URL matches the discovery connection's {@code ReverseHello}.
   *
   * <p>The {@code ReverseHello} endpoint URL is a routing hint only. The selected endpoint still
   * has to pass the normal client endpoint, SecureChannel, and Session validation path.
   *
   * @return an endpoint selector.
   */
  public static ReverseConnectEndpointSelector matchReverseHelloEndpointUrl() {
    return matchReverseHelloThen(endpoint -> true);
  }

  /**
   * Select the endpoint whose URL matches {@code ReverseHello} and satisfies {@code predicate}.
   *
   * <p>The {@code ReverseHello} URL narrows the discovered endpoint list but does not authenticate
   * the server.
   *
   * @param predicate the additional endpoint predicate.
   * @return an endpoint selector.
   */
  public static ReverseConnectEndpointSelector matchReverseHelloThen(
      Predicate<EndpointDescription> predicate) {

    requireNonNull(predicate, "predicate");

    return (candidate, endpoints) -> {
      requireNonNull(candidate, "candidate");
      requireNonNull(endpoints, "endpoints");

      return Optional.ofNullable(candidate.endpointUrl())
          .flatMap(
              endpointUrl ->
                  endpoints.stream()
                      .filter(endpoint -> Objects.equals(endpointUrl, endpoint.getEndpointUrl()))
                      .filter(predicate)
                      .findFirst());
    };
  }

  /**
   * Prefer a {@code ReverseHello} endpoint URL match, then fall back to the first endpoint matching
   * {@code predicate}.
   *
   * <p>This is useful when the server's {@code ReverseHello} identifies the same endpoint URL that
   * was later returned by {@code GetEndpoints}, while still allowing servers that advertise a
   * different but compatible URL to connect.
   *
   * @param predicate the endpoint predicate.
   * @return an endpoint selector.
   */
  public static ReverseConnectEndpointSelector preferReverseHelloEndpointUrl(
      Predicate<EndpointDescription> predicate) {

    return matchReverseHelloThen(predicate).orElse(first(predicate));
  }
}
