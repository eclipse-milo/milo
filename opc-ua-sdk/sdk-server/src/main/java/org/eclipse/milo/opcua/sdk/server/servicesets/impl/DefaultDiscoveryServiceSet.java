/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.servicesets.impl;

import static java.util.stream.Collectors.toList;
import static org.eclipse.milo.opcua.sdk.server.servicesets.AbstractServiceSet.createResponseHeader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.servicesets.DiscoveryServiceSet;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.FindServersOnNetworkRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.FindServersOnNetworkResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.FindServersRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.FindServersResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.GetEndpointsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.GetEndpointsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterServer2Request;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterServer2Response;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterServerRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterServerResponse;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultDiscoveryServiceSet implements DiscoveryServiceSet {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final OpcUaServer server;
  private final Supplier<List<ApplicationDescription>> additionalServers;

  public DefaultDiscoveryServiceSet(OpcUaServer server) {
    this(server, List::of);
  }

  /**
   * Create a DefaultDiscoveryServiceSet that includes additional servers in the results of the
   * FindServers service.
   *
   * <p>The supplier is invoked each time the FindServers service is invoked, and may return
   * different results over time. It must not include the ApplicationDescription of {@code server}
   * itself; that is always included.
   *
   * <p>This is intended for use by servers that know of other servers they should advertise, e.g.
   * the other members of a non-transparent redundant server set.
   *
   * @param server the {@link OpcUaServer} this service set belongs to.
   * @param additionalServers a supplier of additional {@link ApplicationDescription}s to include in
   *     the results of the FindServers service.
   */
  public DefaultDiscoveryServiceSet(
      OpcUaServer server, Supplier<List<ApplicationDescription>> additionalServers) {

    this.server = server;
    this.additionalServers = additionalServers;
  }

  @Override
  public GetEndpointsResponse onGetEndpoints(
      ServiceRequestContext context, GetEndpointsRequest request) {
    List<String> profileUris =
        request.getProfileUris() != null
            ? List.of(request.getProfileUris())
            : Collections.emptyList();

    List<EndpointDescription> allEndpoints =
        server.getApplicationContext().getEndpointDescriptions().stream()
            .filter(
                ed -> {
                  String endpointUrl = ed.getEndpointUrl();
                  return endpointUrl == null || !endpointUrl.endsWith("/discovery");
                })
            .filter(ed -> filterProfileUris(ed, profileUris))
            .distinct()
            .toList();

    ApplicationDescription filteredApplicationDescription =
        getFilteredApplicationDescription(request.getEndpointUrl());

    List<EndpointDescription> matchingEndpoints =
        allEndpoints.stream()
            .filter(endpoint -> filterEndpointUrls(endpoint, request.getEndpointUrl()))
            .map(
                endpoint -> replaceApplicationDescription(endpoint, filteredApplicationDescription))
            .distinct()
            .toList();

    return new GetEndpointsResponse(
        createResponseHeader(request),
        matchingEndpoints.isEmpty()
            ? allEndpoints.toArray(new EndpointDescription[0])
            : matchingEndpoints.toArray(new EndpointDescription[0]));
  }

  @Override
  public FindServersResponse onFindServers(
      ServiceRequestContext context, FindServersRequest request) {
    List<String> serverUris =
        request.getServerUris() != null
            ? List.of(request.getServerUris())
            : Collections.emptyList();

    List<ApplicationDescription> applicationDescriptions = new ArrayList<>();
    applicationDescriptions.add(getFilteredApplicationDescription(request.getEndpointUrl()));
    applicationDescriptions.addAll(additionalServers.get());

    applicationDescriptions =
        applicationDescriptions.stream().filter(ad -> filterServerUris(ad, serverUris)).toList();

    return new FindServersResponse(
        createResponseHeader(request),
        applicationDescriptions.toArray(new ApplicationDescription[0]));
  }

  @Override
  public FindServersOnNetworkResponse onFindServersOnNetwork(
      ServiceRequestContext context, FindServersOnNetworkRequest request) throws UaException {

    throw new UaException(StatusCodes.Bad_ServiceUnsupported);
  }

  @Override
  public RegisterServerResponse onRegisterServer(
      ServiceRequestContext context, RegisterServerRequest request) throws UaException {

    throw new UaException(StatusCodes.Bad_ServiceUnsupported);
  }

  @Override
  public RegisterServer2Response onRegisterServer2(
      ServiceRequestContext context, RegisterServer2Request request) throws UaException {

    throw new UaException(StatusCodes.Bad_ServiceUnsupported);
  }

  private boolean filterProfileUris(EndpointDescription endpoint, List<String> profileUris) {
    return profileUris.isEmpty() || profileUris.contains(endpoint.getTransportProfileUri());
  }

  private boolean filterEndpointUrls(EndpointDescription endpoint, String endpointUrl) {
    try {
      String requestedHost = EndpointUtil.getHost(endpointUrl);
      String endpointHost = EndpointUtil.getHost(endpoint.getEndpointUrl());

      return Objects.requireNonNullElse(requestedHost, "").equalsIgnoreCase(endpointHost);
    } catch (Throwable e) {
      logger.debug("Unable to create URI.", e);
      return false;
    }
  }

  private EndpointDescription replaceApplicationDescription(
      EndpointDescription endpoint, ApplicationDescription applicationDescription) {

    return new EndpointDescription(
        endpoint.getEndpointUrl(),
        applicationDescription,
        endpoint.getServerCertificate(),
        endpoint.getSecurityMode(),
        endpoint.getSecurityPolicyUri(),
        endpoint.getUserIdentityTokens(),
        endpoint.getTransportProfileUri(),
        endpoint.getSecurityLevel());
  }

  private ApplicationDescription getFilteredApplicationDescription(String endpointUrl) {
    List<String> allDiscoveryUrls =
        server.getApplicationContext().getEndpointDescriptions().stream()
            .map(EndpointDescription::getEndpointUrl)
            .filter(Objects::nonNull)
            .filter(url -> url.endsWith("/discovery"))
            .distinct()
            .collect(toList());

    if (allDiscoveryUrls.isEmpty()) {
      allDiscoveryUrls =
          server.getApplicationContext().getEndpointDescriptions().stream()
              .map(EndpointDescription::getEndpointUrl)
              .filter(Objects::nonNull)
              .distinct()
              .toList();
    }

    List<String> matchingDiscoveryUrls =
        allDiscoveryUrls.stream()
            .filter(
                discoveryUrl -> {
                  try {

                    String requestedHost = EndpointUtil.getHost(endpointUrl);
                    String discoveryHost = EndpointUtil.getHost(discoveryUrl);

                    logger.debug(
                        "requestedHost={}, discoveryHost={}", requestedHost, discoveryHost);

                    return Objects.requireNonNullElse(requestedHost, "")
                        .equalsIgnoreCase(discoveryHost);
                  } catch (Throwable e) {
                    logger.debug("Unable to create URI.", e);
                    return false;
                  }
                })
            .distinct()
            .collect(toList());

    logger.debug("Matching discovery URLs: {}", matchingDiscoveryUrls);

    return new ApplicationDescription(
        server.getConfig().getApplicationUri(),
        server.getConfig().getProductUri(),
        server.getConfig().getApplicationName(),
        ApplicationType.Server,
        null,
        null,
        matchingDiscoveryUrls.isEmpty()
            ? allDiscoveryUrls.toArray(new String[0])
            : matchingDiscoveryUrls.toArray(new String[0]));
  }

  private boolean filterServerUris(ApplicationDescription ad, List<String> serverUris) {
    return serverUris.isEmpty() || serverUris.contains(ad.getApplicationUri());
  }
}
