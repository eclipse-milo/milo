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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.DiscoveryClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.identity.IdentityProvider;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Production {@link SksClientOperations}: {@link DiscoveryClient} for GetEndpoints and an ephemeral
 * {@link OpcUaClient} per connect.
 *
 * <p>The client config is built from the resolved endpoint — never via the endpoint-selecting
 * {@code OpcUaClient.create(String, ...)} overload, which re-runs discovery, blocks, and disables
 * session endpoint validation. Session endpoint validation is always enabled here (the Part 4
 * §6.1.4 CreateSession-response vs GetEndpoints comparison defense).
 */
final class DefaultSksClientOperations implements SksClientOperations {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSksClientOperations.class);

  private final String applicationUri;
  private final KeyPair keyPair;
  private final X509Certificate[] certificateChain;
  private final CertificateValidator certificateValidator;
  private final Duration requestTimeout;

  DefaultSksClientOperations(
      String applicationUri,
      KeyPair keyPair,
      X509Certificate[] certificateChain,
      CertificateValidator certificateValidator,
      Duration requestTimeout) {

    this.applicationUri = applicationUri;
    this.keyPair = keyPair;
    this.certificateChain = certificateChain;
    this.certificateValidator = certificateValidator;
    this.requestTimeout = requestTimeout;
  }

  @Override
  public List<EndpointDescription> getEndpoints(String discoveryUrl) throws UaException {
    try {
      return DiscoveryClient.getEndpoints(discoveryUrl).get();
    } catch (ExecutionException e) {
      Throwable cause = e.getCause() != null ? e.getCause() : e;
      long statusCode =
          UaException.extract(cause)
              .map(ua -> ua.getStatusCode().value())
              .orElse(StatusCodes.Bad_CommunicationError);
      throw new UaException(
          statusCode, "GetEndpoints at %s failed: %s".formatted(discoveryUrl, cause), cause);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public SksSession connect(
      EndpointDescription endpoint,
      List<EndpointDescription> discoveredEndpoints,
      IdentityProvider identityProvider)
      throws UaException {

    OpcUaClientConfig config =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setDiscoveryEndpoints(discoveredEndpoints)
            .setSessionEndpointValidationEnabled(true)
            .setApplicationName(LocalizedText.english("Eclipse Milo SKS pull client"))
            .setApplicationUri(applicationUri)
            .setKeyPair(keyPair)
            .setCertificate(certificateChain[0])
            .setCertificateChain(certificateChain)
            .setCertificateValidator(certificateValidator)
            .setIdentityProvider(identityProvider)
            .setRequestTimeout(uint(requestTimeout.toMillis()))
            .build();

    OpcUaClient client = OpcUaClient.create(config);
    try {
      client.connect();
    } catch (Exception connectFailure) {
      // connect() puts the client into a persistent auto-reconnect state regardless of the
      // outcome; a client that isn't disconnected leaks a reconnect loop against the SKS.
      try {
        client.disconnect();
      } catch (Exception disconnectFailure) {
        connectFailure.addSuppressed(disconnectFailure);
      }
      if (connectFailure instanceof UaException uaException) {
        throw uaException;
      } else {
        throw new UaException(StatusCodes.Bad_CommunicationError, connectFailure);
      }
    }

    return new ClientSession(client);
  }

  private static final class ClientSession implements SksSession {

    private final OpcUaClient client;

    private ClientSession(OpcUaClient client) {
      this.client = client;
    }

    @Override
    public CallMethodResult call(CallMethodRequest request) throws UaException {
      CallResponse response = client.call(List.of(request));

      CallMethodResult[] results = response.getResults();
      if (results == null || results.length == 0) {
        throw new UaException(
            StatusCodes.Bad_UnexpectedError, "Call response contained no results");
      }
      return results[0];
    }

    @Override
    public void close() {
      try {
        client.disconnect();
      } catch (Exception e) {
        LOGGER.debug("Error disconnecting from SKS", e);
      }
    }
  }
}
