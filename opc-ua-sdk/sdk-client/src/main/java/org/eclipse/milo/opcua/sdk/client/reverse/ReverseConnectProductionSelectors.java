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

import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;

final class ReverseConnectProductionSelectors {

  private ReverseConnectProductionSelectors() {}

  static boolean missingRoutingHint(ReverseConnectCandidateSnapshot candidate) {
    return candidate.serverUri() == null && candidate.endpointUrl() == null;
  }

  /**
   * Build a production selector that loosely matches the discovery candidate's routing hints.
   *
   * <p>The returned selector treats a {@code null} {@code ServerUri} or {@code EndpointUrl} on the
   * discovery candidate as "no constraint": a production candidate that supplies a different
   * non-null value for the unset hint will still match. For example, a discovery candidate {@code
   * (serverUri=null, endpointUrl="opc.tcp://A:4840/x")} will match a production candidate {@code
   * (serverUri="urn:any", endpointUrl="opc.tcp://A:4840/x")}. Identity is verified later by the
   * production SecureChannel handshake regardless of this loose match.
   *
   * <p>Applications that need strict identity matching (for example, a load balancer fronting
   * multiple application instances with the same endpoint URL) should configure {@link
   * ReverseConnectAcceptor.Builder#setProductionSelector} with a tighter selector that requires
   * both hints to be present and equal.
   */
  static ReverseConnectSelector matchDiscoveryRoutingHints(
      ReverseConnectCandidateSnapshot discoveryCandidate) {

    if (missingRoutingHint(discoveryCandidate)) {
      return candidate -> false;
    }

    return candidate ->
        (discoveryCandidate.serverUri() == null
                || Objects.equals(discoveryCandidate.serverUri(), candidate.serverUri()))
            && (discoveryCandidate.endpointUrl() == null
                || Objects.equals(discoveryCandidate.endpointUrl(), candidate.endpointUrl()));
  }

  static UaException missingRoutingHintsException() {
    return new UaException(
        StatusCodes.Bad_ConfigurationError,
        "default Reverse Connect production selector requires ReverseHello ServerUri or"
            + " EndpointUrl");
  }
}
