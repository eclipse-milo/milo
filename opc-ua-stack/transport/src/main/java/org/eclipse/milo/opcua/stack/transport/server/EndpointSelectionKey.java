/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server;

import static java.util.Objects.requireNonNullElse;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.DigestUtil;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.jspecify.annotations.Nullable;

/**
 * The wire-observable identity of a server endpoint at OpenSecureChannel time.
 *
 * <p>OPC UA does not transmit an EndpointDescription identifier during OpenSecureChannel (Part 6,
 * 7.1.2.3: the Hello message carries only an endpoint URL), so a server must derive which
 * configured endpoint a new SecureChannel belongs to from the inputs the client does send. This key
 * captures exactly those inputs:
 *
 * <ul>
 *   <li>the transport profile the connection arrived on
 *   <li>the normalized path of the endpoint URL from the Hello message (host and port are excluded
 *       because clients may validly substitute them, e.g. connecting by IP address)
 *   <li>the SecurityPolicy from the AsymmetricSecurityHeader
 *   <li>the MessageSecurityMode from the OpenSecureChannel request
 *   <li>the SHA-1 thumbprint of the endpoint application certificate (the receiver thumbprint from
 *       the AsymmetricSecurityHeader); absent for {@link SecurityPolicy#None}, where no certificate
 *       is observable on the wire
 * </ul>
 *
 * <p>The intended invariant is that each key identifies exactly one effective Session endpoint.
 * {@link UserTokenPolicy}s are deliberately not part of the key: they are properties of the
 * selected endpoint, not selectors. Endpoints that share a key must agree on every
 * Session-sensitive property (see {@link #isSessionEquivalent(EndpointDescription,
 * EndpointDescription)}); they may differ only in the substitutable host/port portion of their
 * endpoint URL.
 *
 * @param transportProfileUri the URI of the {@link TransportProfile} the connection uses.
 * @param path the normalized endpoint URL path, as returned by {@link EndpointUtil#getPath}.
 * @param securityPolicyUri the URI of the channel {@link SecurityPolicy}.
 * @param securityMode the {@link MessageSecurityMode} requested for the channel.
 * @param certificateThumbprint the SHA-1 thumbprint of the endpoint application certificate, or
 *     {@link ByteString#NULL_VALUE} for {@link SecurityPolicy#None}.
 */
public record EndpointSelectionKey(
    String transportProfileUri,
    String path,
    String securityPolicyUri,
    MessageSecurityMode securityMode,
    ByteString certificateThumbprint) {

  public EndpointSelectionKey {
    // Canonicalize so that a null and an empty thumbprint are the same key. ByteString#equals
    // already treats them as equal but ByteString#hashCode does not, which would break map lookups.
    if (certificateThumbprint == null || certificateThumbprint.isNullOrEmpty()) {
      certificateThumbprint = ByteString.NULL_VALUE;
    }
  }

  /**
   * Create the key identifying the endpoint a new SecureChannel selects, from the inputs available
   * to the server during OpenSecureChannel.
   *
   * @param transportProfile the {@link TransportProfile} the connection arrived on.
   * @param endpointUrl the endpoint URL from the Hello message.
   * @param securityPolicy the {@link SecurityPolicy} from the AsymmetricSecurityHeader.
   * @param securityMode the {@link MessageSecurityMode} from the OpenSecureChannel request.
   * @param certificateThumbprint the receiver certificate thumbprint from the
   *     AsymmetricSecurityHeader; ignored for {@link SecurityPolicy#None}.
   * @return the {@link EndpointSelectionKey} for the channel.
   */
  public static EndpointSelectionKey of(
      TransportProfile transportProfile,
      @Nullable String endpointUrl,
      SecurityPolicy securityPolicy,
      MessageSecurityMode securityMode,
      @Nullable ByteString certificateThumbprint) {

    return new EndpointSelectionKey(
        transportProfile.getUri(),
        EndpointUtil.getPath(endpointUrl),
        securityPolicy.getUri(),
        securityMode,
        securityPolicy == SecurityPolicy.None ? ByteString.NULL_VALUE : certificateThumbprint);
  }

  /**
   * Create the key under which {@code endpoint} is selectable.
   *
   * @param endpoint the {@link EndpointDescription} to derive a key for.
   * @return the {@link EndpointSelectionKey} under which {@code endpoint} is selectable.
   */
  public static EndpointSelectionKey of(EndpointDescription endpoint) {
    String securityPolicyUri = endpoint.getSecurityPolicyUri();

    ByteString certificateThumbprint = ByteString.NULL_VALUE;
    if (!Objects.equals(securityPolicyUri, SecurityPolicy.None.getUri())) {
      ByteString serverCertificate =
          requireNonNullElse(endpoint.getServerCertificate(), ByteString.NULL_VALUE);
      certificateThumbprint = ByteString.of(DigestUtil.sha1(serverCertificate.bytesOrEmpty()));
    }

    return new EndpointSelectionKey(
        requireNonNullElse(endpoint.getTransportProfileUri(), ""),
        EndpointUtil.getPath(endpoint.getEndpointUrl()),
        requireNonNullElse(securityPolicyUri, ""),
        endpoint.getSecurityMode(),
        certificateThumbprint);
  }

  /**
   * Select the unique endpoint identified by {@code key} from {@code endpoints}.
   *
   * <p>Multiple endpoints may share a key only when they are {@link
   * #isSessionEquivalent(EndpointDescription, EndpointDescription) Session-equivalent}, i.e. they
   * are host/port substitution aliases of the same effective endpoint. In that case the alias whose
   * URL best matches {@code requestedEndpointUrl} is preferred (host and port match, then host
   * match), falling back to the first alias; every alias carries identical Session-sensitive state,
   * so the choice affects only the advertised URL.
   *
   * <p>If endpoints sharing the key are not Session-equivalent, no endpoint is returned: the key is
   * ambiguous and selecting one arbitrarily would make security-sensitive Session state depend on
   * collection ordering.
   *
   * @param endpoints the candidate {@link EndpointDescription}s.
   * @param key the {@link EndpointSelectionKey} to resolve.
   * @param requestedEndpointUrl the endpoint URL requested by the client, used to prefer among
   *     host/port substitution aliases; may be null.
   * @return the unique endpoint identified by {@code key}, or empty if there is none or the key is
   *     ambiguous.
   */
  public static Optional<EndpointDescription> selectUnique(
      List<EndpointDescription> endpoints,
      EndpointSelectionKey key,
      @Nullable String requestedEndpointUrl) {

    List<EndpointDescription> candidates =
        endpoints.stream().filter(e -> key.equals(EndpointSelectionKey.of(e))).toList();

    if (candidates.isEmpty()) {
      return Optional.empty();
    }

    EndpointDescription first = candidates.get(0);
    for (EndpointDescription candidate : candidates) {
      if (!isSessionEquivalent(first, candidate)) {
        return Optional.empty();
      }
    }

    return Optional.of(
        preferRequestedUrl(candidates, requestedEndpointUrl, EndpointDescription::getEndpointUrl));
  }

  /**
   * Return whether two endpoints sharing a selection key are interchangeable for Session purposes.
   *
   * <p>Endpoints sharing a key already agree on transport profile, path, SecurityPolicy,
   * MessageSecurityMode, and (for secured policies) certificate thumbprint. What remains
   * Session-sensitive is the advertised user token policy set (compared as a set, ignoring
   * declaration order) and the advertised certificate itself, which for {@link SecurityPolicy#None}
   * endpoints is not covered by the key.
   *
   * @param a an {@link EndpointDescription}.
   * @param b an {@link EndpointDescription} sharing {@code a}'s selection key.
   * @return {@code true} if the endpoints differ only in the substitutable host/port portion of
   *     their endpoint URL.
   */
  public static boolean isSessionEquivalent(EndpointDescription a, EndpointDescription b) {
    return userTokenPolicies(a).equals(userTokenPolicies(b))
        && Arrays.equals(
            requireNonNullElse(a.getServerCertificate(), ByteString.NULL_VALUE).bytesOrEmpty(),
            requireNonNullElse(b.getServerCertificate(), ByteString.NULL_VALUE).bytesOrEmpty());
  }

  private static Set<UserTokenPolicy> userTokenPolicies(EndpointDescription endpoint) {
    UserTokenPolicy[] tokens = endpoint.getUserIdentityTokens();
    return tokens == null ? Set.of() : new HashSet<>(Arrays.asList(tokens));
  }

  /**
   * Select from {@code candidates} the element whose endpoint URL best matches {@code
   * requestedEndpointUrl}: an exact host and port match is preferred, then a host-only match,
   * falling back to the first candidate.
   *
   * <p>Intended for choosing among host/port substitution aliases that share a selection key, so
   * the endpoint URL advertised to a client reflects the URL it actually requested.
   *
   * @param candidates the candidate elements; must not be empty.
   * @param requestedEndpointUrl the endpoint URL requested by the client; may be null.
   * @param endpointUrl extracts a candidate's endpoint URL; may return null.
   * @param <T> the candidate type.
   * @return the candidate whose endpoint URL best matches {@code requestedEndpointUrl}, or the
   *     first candidate if none match.
   */
  public static <T> T preferRequestedUrl(
      List<T> candidates,
      @Nullable String requestedEndpointUrl,
      Function<T, @Nullable String> endpointUrl) {

    if (requestedEndpointUrl != null) {
      String requestedHost = EndpointUtil.getHost(requestedEndpointUrl);
      int requestedPort = EndpointUtil.getPort(requestedEndpointUrl);

      if (requestedHost != null) {
        T hostMatch = null;

        for (T candidate : candidates) {
          String url = requireNonNullElse(endpointUrl.apply(candidate), "");

          if (requestedHost.equalsIgnoreCase(EndpointUtil.getHost(url))) {
            if (requestedPort == EndpointUtil.getPort(url)) {
              return candidate;
            }
            if (hostMatch == null) {
              hostMatch = candidate;
            }
          }
        }

        if (hostMatch != null) {
          return hostMatch;
        }
      }
    }

    return candidates.get(0);
  }
}
