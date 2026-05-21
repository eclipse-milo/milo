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

import java.util.Objects;
import java.util.UUID;

/**
 * Synchronous predicate used by {@link ReverseConnectManager} to decide whether a selector wants to
 * claim a decoded reverse-connect candidate.
 */
@FunctionalInterface
public interface ReverseConnectSelector {

  /**
   * Return {@code true} when this selector should claim {@code candidate}.
   *
   * @param candidate the candidate being evaluated.
   * @return true if the candidate should be claimed.
   */
  boolean matches(ReverseConnectCandidateSnapshot candidate);

  /**
   * Match any decoded and verified candidate.
   *
   * @return a selector that matches all candidates.
   */
  static ReverseConnectSelector any() {
    return candidate -> true;
  }

  /**
   * Match one exact candidate by manager-assigned candidate id.
   *
   * @param candidateId the candidate id to match.
   * @return a selector that matches only the candidate with {@code candidateId}.
   */
  static ReverseConnectSelector byCandidateId(UUID candidateId) {
    requireNonNull(candidateId, "candidateId");

    return candidate -> candidateId.equals(candidate.id());
  }

  /**
   * Match candidates by the {@code ServerUri} carried in {@code ReverseHello}.
   *
   * @param serverUri the expected server application URI.
   * @return a selector that matches candidates with {@code serverUri}.
   */
  static ReverseConnectSelector byServerUri(String serverUri) {
    return candidate -> Objects.equals(serverUri, candidate.serverUri());
  }

  /**
   * Match candidates by the {@code EndpointUrl} carried in {@code ReverseHello}.
   *
   * @param endpointUrl the expected endpoint URL.
   * @return a selector that matches candidates with {@code endpointUrl}.
   */
  static ReverseConnectSelector byEndpointUrl(String endpointUrl) {
    return candidate -> Objects.equals(endpointUrl, candidate.endpointUrl());
  }

  /**
   * Match candidates by both {@code ServerUri} and {@code EndpointUrl}.
   *
   * @param serverUri the expected server application URI.
   * @param endpointUrl the expected endpoint URL.
   * @return a selector that matches only when both values match.
   */
  static ReverseConnectSelector byServerUriAndEndpointUrl(String serverUri, String endpointUrl) {
    return candidate ->
        Objects.equals(serverUri, candidate.serverUri())
            && Objects.equals(endpointUrl, candidate.endpointUrl());
  }
}
