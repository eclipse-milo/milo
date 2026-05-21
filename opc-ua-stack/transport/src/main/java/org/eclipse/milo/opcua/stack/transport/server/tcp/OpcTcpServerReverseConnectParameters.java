/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.tcp;

import static java.util.Objects.requireNonNull;

import java.net.InetSocketAddress;
import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.jspecify.annotations.Nullable;

/**
 * Input for one server-initiated reverse-connect attempt.
 *
 * <p>The parameters identify the client reverse listener to dial, the server endpoint URL and
 * server URI encoded into {@code ReverseHello}, and the context required when a successful attempt
 * rejoins the normal server UASC path. Factory methods accept either a listener URL or a resolved
 * socket address; when {@code serverUri} is omitted they resolve it from the endpoint descriptions
 * in the supplied {@link ServerApplicationContext}.
 *
 * @param applicationContext the server application context used by the normal UASC server path.
 * @param clientListenerUrl the client reverse-listener URL, when the caller supplied one.
 * @param clientListenerAddress the resolved client reverse-listener socket address.
 * @param endpointUrl the server endpoint URL advertised in {@code ReverseHello}.
 * @param serverUri the server application URI advertised in {@code ReverseHello}.
 * @param connectTimeoutMillis the TCP connect timeout, in milliseconds.
 * @param observer receives attempt state transitions.
 */
public record OpcTcpServerReverseConnectParameters(
    ServerApplicationContext applicationContext,
    @Nullable String clientListenerUrl,
    InetSocketAddress clientListenerAddress,
    String endpointUrl,
    String serverUri,
    int connectTimeoutMillis,
    OpcTcpServerReverseConnectAttemptObserver observer) {

  public OpcTcpServerReverseConnectParameters {
    requireNonNull(applicationContext, "applicationContext");
    requireNonNull(clientListenerAddress, "clientListenerAddress");
    requireNonNull(endpointUrl, "endpointUrl");
    requireNonNull(serverUri, "serverUri");
    requireNonNull(observer, "observer");

    if (serverUri.isBlank()) {
      throw new IllegalArgumentException("serverUri cannot be blank");
    }
    if (endpointUrl.isBlank()) {
      throw new IllegalArgumentException("endpointUrl cannot be blank");
    }
    if (connectTimeoutMillis <= 0) {
      throw new IllegalArgumentException("connectTimeoutMillis must be greater than 0");
    }
  }

  /**
   * Create parameters from a client listener URL.
   *
   * <p>The URL must use the {@code opc.tcp} scheme and include a host. The port is resolved with
   * {@link EndpointUtil#getPort(String)}, matching the rest of the UA-TCP URL handling in this
   * package.
   *
   * @param applicationContext the server application context.
   * @param clientListenerUrl the client reverse-listener URL.
   * @param endpointUrl the server endpoint URL advertised in {@code ReverseHello}.
   * @param serverUri the server application URI, or {@code null} to resolve from the endpoint list.
   * @param connectTimeoutMillis the TCP connect timeout, in milliseconds.
   * @param observer receives attempt state transitions.
   * @return reverse-connect parameters.
   */
  public static OpcTcpServerReverseConnectParameters fromUrl(
      ServerApplicationContext applicationContext,
      String clientListenerUrl,
      String endpointUrl,
      @Nullable String serverUri,
      int connectTimeoutMillis,
      OpcTcpServerReverseConnectAttemptObserver observer) {

    String scheme = EndpointUtil.getScheme(clientListenerUrl);
    if (!"opc.tcp".equals(scheme)) {
      throw new IllegalArgumentException(
          "clientListenerUrl must use opc.tcp: " + clientListenerUrl);
    }

    String host = EndpointUtil.getHost(clientListenerUrl);
    if (host == null || host.isBlank()) {
      throw new IllegalArgumentException("clientListenerUrl has no host: " + clientListenerUrl);
    }

    return fromAddress(
        applicationContext,
        clientListenerUrl,
        new InetSocketAddress(host, EndpointUtil.getPort(clientListenerUrl)),
        endpointUrl,
        serverUri,
        connectTimeoutMillis,
        observer);
  }

  /**
   * Create parameters from a resolved client listener address.
   *
   * <p>This form is useful when the caller has already resolved the listener address but still
   * wants the attempt to advertise a server endpoint URL and optionally derive the server URI from
   * the server application context.
   *
   * @param applicationContext the server application context.
   * @param clientListenerAddress the resolved client reverse-listener socket address.
   * @param endpointUrl the server endpoint URL advertised in {@code ReverseHello}.
   * @param serverUri the server application URI, or {@code null} to resolve from the endpoint list.
   * @param connectTimeoutMillis the TCP connect timeout, in milliseconds.
   * @param observer receives attempt state transitions.
   * @return reverse-connect parameters.
   */
  public static OpcTcpServerReverseConnectParameters fromAddress(
      ServerApplicationContext applicationContext,
      InetSocketAddress clientListenerAddress,
      String endpointUrl,
      @Nullable String serverUri,
      int connectTimeoutMillis,
      OpcTcpServerReverseConnectAttemptObserver observer) {

    return fromAddress(
        applicationContext,
        null,
        clientListenerAddress,
        endpointUrl,
        serverUri,
        connectTimeoutMillis,
        observer);
  }

  private static OpcTcpServerReverseConnectParameters fromAddress(
      ServerApplicationContext applicationContext,
      @Nullable String clientListenerUrl,
      InetSocketAddress clientListenerAddress,
      String endpointUrl,
      @Nullable String serverUri,
      int connectTimeoutMillis,
      OpcTcpServerReverseConnectAttemptObserver observer) {

    String resolvedServerUri =
        serverUri != null ? serverUri : resolveServerUri(applicationContext, endpointUrl);

    return new OpcTcpServerReverseConnectParameters(
        applicationContext,
        clientListenerUrl,
        clientListenerAddress,
        endpointUrl,
        resolvedServerUri,
        connectTimeoutMillis,
        observer);
  }

  private static String resolveServerUri(
      ServerApplicationContext applicationContext, String endpointUrl) {

    return applicationContext.getEndpointDescriptions().stream()
        .filter(
            endpoint ->
                Objects.equals(endpoint.getEndpointUrl(), endpointUrl)
                    || Objects.equals(
                        EndpointUtil.getPath(endpoint.getEndpointUrl()),
                        EndpointUtil.getPath(endpointUrl)))
        .filter(endpoint -> endpoint.getServer() != null)
        .map(endpoint -> endpoint.getServer().getApplicationUri())
        .filter(uri -> uri != null && !uri.isBlank())
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "serverUri could not be resolved for endpointUrl: " + endpointUrl));
  }
}
