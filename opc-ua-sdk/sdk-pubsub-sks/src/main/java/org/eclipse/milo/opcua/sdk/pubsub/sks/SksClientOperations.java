/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.sks;

import java.util.List;
import org.eclipse.milo.opcua.sdk.client.identity.IdentityProvider;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;

/**
 * Seam wrapping the blocking client operations used by a fetch: discovery, connect, and Call.
 *
 * <p>Exists so the resolution/failover logic in {@link SksSecurityKeyProvider} is testable with a
 * scripted stub; the production implementation is {@link DefaultSksClientOperations}. All methods
 * block and are only invoked on the provider-owned executor thread.
 */
interface SksClientOperations {

  /**
   * Call the GetEndpoints service at {@code discoveryUrl}.
   *
   * @param discoveryUrl the discovery URL to call GetEndpoints at.
   * @return the endpoints returned by the server.
   * @throws UaException if the call fails.
   */
  List<EndpointDescription> getEndpoints(String discoveryUrl) throws UaException;

  /**
   * Connect to {@code endpoint} and open a session.
   *
   * <p>Implementations must guarantee that a failed connect attempt leaves no background connection
   * or reconnect machinery running.
   *
   * @param endpoint the endpoint to connect to.
   * @param discoveredEndpoints the full endpoint list {@code endpoint} was selected from, used for
   *     session endpoint validation.
   * @param identityProvider the identity to activate the session with.
   * @return a connected {@link SksSession}.
   * @throws UaException if connecting fails.
   */
  SksSession connect(
      EndpointDescription endpoint,
      List<EndpointDescription> discoveredEndpoints,
      IdentityProvider identityProvider)
      throws UaException;

  /** A connected session against an SKS; {@link #close()} must always disconnect. */
  interface SksSession extends AutoCloseable {

    /**
     * Invoke a method via the Call service.
     *
     * @param request the {@link CallMethodRequest} to invoke.
     * @return the corresponding {@link CallMethodResult}.
     * @throws UaException if the service call fails.
     */
    CallMethodResult call(CallMethodRequest request) throws UaException;

    /** Disconnect. Never throws; disconnect failures are logged and swallowed. */
    @Override
    void close();
  }
}
