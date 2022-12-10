/*
 * Copyright (c) 2022 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.services.impl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.services.DiscoveryServiceSet;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
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

import static java.util.stream.Collectors.toList;
import static org.eclipse.milo.opcua.sdk.server.services.AbstractServiceSet.createResponseHeader;
import static org.eclipse.milo.opcua.stack.core.util.ConversionUtil.a;
import static org.eclipse.milo.opcua.stack.core.util.FutureUtils.failedUaFuture;

public class DefaultDiscoveryServiceSet implements DiscoveryServiceSet {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final OpcUaServer server;

    public DefaultDiscoveryServiceSet(OpcUaServer server) {
        this.server = server;
    }

    @Override
    public CompletableFuture<GetEndpointsResponse> onGetEndpoints(ServiceRequestContext context, GetEndpointsRequest request) {
        List<String> profileUris = request.getProfileUris() != null ?
            List.of(request.getProfileUris()) :
            Collections.emptyList();

        List<EndpointDescription> allEndpoints = server.getApplicationContext()
            .getEndpointDescriptions()
            .stream()
            .filter(ed -> !ed.getEndpointUrl().endsWith("/discovery"))
            .filter(ed -> filterProfileUris(ed, profileUris))
            .distinct()
            .collect(Collectors.toList());

        ApplicationDescription filteredApplicationDescription =
            getFilteredApplicationDescription(request.getEndpointUrl());

        List<EndpointDescription> matchingEndpoints = allEndpoints.stream()
            .filter(endpoint -> filterEndpointUrls(endpoint, request.getEndpointUrl()))
            .map(endpoint ->
                replaceApplicationDescription(
                    endpoint,
                    filteredApplicationDescription
                )
            )
            .distinct()
            .collect(toList());

        var response = new GetEndpointsResponse(
            createResponseHeader(request),
            matchingEndpoints.isEmpty() ?
                allEndpoints.toArray(new EndpointDescription[0]) :
                matchingEndpoints.toArray(new EndpointDescription[0])
        );

        return CompletableFuture.completedFuture(response);
    }

    @Override
    public CompletableFuture<FindServersResponse> onFindServers(ServiceRequestContext context, FindServersRequest request) {
        List<String> serverUris = request.getServerUris() != null ?
            List.of(request.getServerUris()) :
            Collections.emptyList();

        List<ApplicationDescription> applicationDescriptions =
            List.of(getFilteredApplicationDescription(request.getEndpointUrl()));

        applicationDescriptions = applicationDescriptions.stream()
            .filter(ad -> filterServerUris(ad, serverUris))
            .collect(toList());

        var response = new FindServersResponse(
            createResponseHeader(request),
            a(applicationDescriptions, ApplicationDescription.class)
        );

        return CompletableFuture.completedFuture(response);
    }

    @Override
    public CompletableFuture<FindServersOnNetworkResponse> onFindServersOnNetwork(ServiceRequestContext context, FindServersOnNetworkRequest request) {
        return failedUaFuture(StatusCodes.Bad_ServiceUnsupported);
    }

    @Override
    public CompletableFuture<RegisterServerResponse> onRegisterServer(ServiceRequestContext context, RegisterServerRequest request) {
        return failedUaFuture(StatusCodes.Bad_ServiceUnsupported);
    }

    @Override
    public CompletableFuture<RegisterServer2Response> onRegisterServer2(ServiceRequestContext context, RegisterServer2Request request) {
        return failedUaFuture(StatusCodes.Bad_ServiceUnsupported);
    }

    private boolean filterProfileUris(EndpointDescription endpoint, List<String> profileUris) {
        return profileUris.size() == 0 || profileUris.contains(endpoint.getTransportProfileUri());
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
        EndpointDescription endpoint,
        ApplicationDescription applicationDescription) {

        return new EndpointDescription(
            endpoint.getEndpointUrl(),
            applicationDescription,
            endpoint.getServerCertificate(),
            endpoint.getSecurityMode(),
            endpoint.getSecurityPolicyUri(),
            endpoint.getUserIdentityTokens(),
            endpoint.getTransportProfileUri(),
            endpoint.getSecurityLevel()
        );
    }

    private ApplicationDescription getFilteredApplicationDescription(String endpointUrl) {
        List<String> allDiscoveryUrls = server.getConfig().getEndpoints()
            .stream()
            .map(EndpointConfig::getEndpointUrl)
            .filter(url -> url.endsWith("/discovery"))
            .distinct()
            .collect(toList());

        if (allDiscoveryUrls.isEmpty()) {
            allDiscoveryUrls = server.getConfig().getEndpoints()
                .stream()
                .map(EndpointConfig::getEndpointUrl)
                .distinct()
                .collect(toList());
        }

        List<String> matchingDiscoveryUrls = allDiscoveryUrls.stream()
            .filter(discoveryUrl -> {
                try {

                    String requestedHost = EndpointUtil.getHost(endpointUrl);
                    String discoveryHost = EndpointUtil.getHost(discoveryUrl);

                    logger.debug("requestedHost={}, discoveryHost={}", requestedHost, discoveryHost);

                    return Objects.requireNonNullElse(requestedHost, "").equalsIgnoreCase(discoveryHost);
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
            matchingDiscoveryUrls.isEmpty() ?
                allDiscoveryUrls.toArray(new String[0]) :
                matchingDiscoveryUrls.toArray(new String[0])
        );
    }

    private boolean filterServerUris(ApplicationDescription ad, List<String> serverUris) {
        return serverUris.size() == 0 || serverUris.contains(ad.getApplicationUri());
    }

}
