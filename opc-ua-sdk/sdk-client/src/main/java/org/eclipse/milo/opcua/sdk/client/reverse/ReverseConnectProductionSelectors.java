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

  static ReverseConnectSelector matchDiscoveryRoutingHints(
      ReverseConnectCandidateSnapshot discoveryCandidate) {

    if (missingRoutingHint(discoveryCandidate)) {
      return candidate -> false;
    }

    return candidate ->
        Objects.equals(discoveryCandidate.serverUri(), candidate.serverUri())
            && Objects.equals(discoveryCandidate.endpointUrl(), candidate.endpointUrl());
  }

  static UaException missingRoutingHintsException() {
    return new UaException(
        StatusCodes.Bad_ConfigurationError,
        "default Reverse Connect production selector requires ReverseHello ServerUri or"
            + " EndpointUrl");
  }
}
