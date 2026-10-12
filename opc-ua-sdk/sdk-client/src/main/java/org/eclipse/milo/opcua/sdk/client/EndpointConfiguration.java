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

import java.util.List;
import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;

/**
 * A selected connection endpoint and the discovery response used to validate CreateSession.
 *
 * <p>The selected endpoint may have an application-supplied host override. Keep the discovery list
 * as returned by the server so Session endpoint validation compares the advertised endpoints.
 *
 * @param endpoint the selected endpoint, including any host override.
 * @param discoveryEndpoints the unmodified GetEndpoints result.
 */
public record EndpointConfiguration(
    EndpointDescription endpoint, List<EndpointDescription> discoveryEndpoints) {
  public EndpointConfiguration {
    Objects.requireNonNull(endpoint, "endpoint");
    discoveryEndpoints = List.copyOf(discoveryEndpoints);
  }
}
