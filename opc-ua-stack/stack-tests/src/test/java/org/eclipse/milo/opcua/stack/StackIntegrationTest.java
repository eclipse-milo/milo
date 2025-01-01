/// *
// * Copyright (c) 2022 the Eclipse Milo Authors
// *
// * This program and the accompanying materials are made
// * available under the terms of the Eclipse Public License 2.0
// * which is available at https://www.eclipse.org/legal/epl-2.0/
// *
// * SPDX-License-Identifier: EPL-2.0
// */
//
// package org.eclipse.milo.opcua.stack;
//
// import java.security.Security;
// import java.util.ArrayList;
// import java.util.LinkedHashSet;
// import java.util.List;
// import java.util.Random;
// import java.util.Set;
//
// import org.bouncycastle.jce.provider.BouncyCastleProvider;
// import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
// import org.eclipse.milo.opcua.stack.client.UaStackClient;
// import org.eclipse.milo.opcua.stack.client.UaStackClientConfig;
// import org.eclipse.milo.opcua.stack.client.UaStackClientConfigBuilder;
// import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
// import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
// import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
// import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
// import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
// import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
// import org.eclipse.milo.opcua.stack.server.EndpointConfiguration;
// import org.eclipse.milo.opcua.stack.server.UaStackServer;
// import org.eclipse.milo.opcua.stack.server.UaStackServerConfig;
// import org.eclipse.milo.opcua.stack.server.UaStackServerConfigBuilder;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.testng.annotations.AfterSuite;
// import org.testng.annotations.BeforeSuite;
// import org.testng.annotations.Test;
//
// import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
//
// public abstract class StackIntegrationTest extends SecurityFixture {
//
//    static {
//        Security.addProvider(new BouncyCastleProvider());
//    }
//
//    private static final UserTokenPolicy USER_TOKEN_POLICY_ANONYMOUS = new UserTokenPolicy(
//        "anonymous",
//        UserTokenType.Anonymous,
//        null,
//        null,
//        null
//    );
//
//    protected final Logger logger = LoggerFactory.getLogger(getClass());
//
//    private final int tcpBindPort = new Random().nextInt(64000) + 1000;
//    protected UaStackClient stackClient;
//    protected UaStackServer stackServer;
//
//    @BeforeSuite
//    public void setUpClientServer() throws Exception {
//        super.setUp();
//
//        int tcpBindPort = getTcpBindPort();
//
//        List<String> bindAddresses = new ArrayList<>();
//        bindAddresses.add("localhost");
//
//        List<String> hostnames = new ArrayList<>();
//        hostnames.add("localhost");
//
//        Set<EndpointConfiguration> endpointConfigurations = new LinkedHashSet<>();
//
//        for (String bindAddress : bindAddresses) {
//            for (String hostname : hostnames) {
//                EndpointConfiguration.Builder base = EndpointConfiguration.newBuilder()
//                    .setBindAddress(bindAddress)
//                    .setHostname(hostname)
//                    .setPath("/test")
//                    .setCertificate(serverCertificate)
//                    .addTokenPolicies(USER_TOKEN_POLICY_ANONYMOUS);
//
//                // TCP Transport Endpoints
//                endpointConfigurations.add(
//                    base.copy()
//                        .setBindPort(tcpBindPort)
//                        .setSecurityPolicy(SecurityPolicy.None)
//                        .setSecurityMode(MessageSecurityMode.None)
//                        .setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
//                        .build()
//                );
//
//                endpointConfigurations.add(
//                    base.copy()
//                        .setBindPort(tcpBindPort)
//                        .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
//                        .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
//                        .setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
//                        .build()
//                );
//            }
//        }
//
//        UaStackServerConfig serverConfig = configureServer(
//            UaStackServerConfig.builder()
//                .setEndpoints(endpointConfigurations)
//                .setCertificateManager(serverCertificateManager)
//                .setCertificateValidator(serverCertificateValidator)
//        ).build();
//
//        stackServer = new UaStackServer(serverConfig);
//        stackServer.startup().get();
//
//        String discoveryUrl = getDiscoveryUrl();
//
//        EndpointDescription endpoint = selectEndpoint(
//            DiscoveryClient.getEndpoints(discoveryUrl)
//                .thenApply(endpoints -> {
//                    endpoints.forEach(e ->
//                        logger.info("discovered endpoint: {}", e.getEndpointUrl()));
//
//                    return endpoints;
//                })
//                .get()
//        );
//
//        UaStackClientConfig clientConfig = configureClient(
//            UaStackClientConfig.builder()
//                .setEndpoint(endpoint)
//                .setKeyPair(clientKeyPair)
//                .setCertificate(clientCertificate)
//                .setRequestTimeout(uint(5000))
//        ).build();
//
//        stackClient = UaStackClient.create(clientConfig);
//        stackClient.connect().get();
//    }
//
//    @AfterSuite
//    public void tearDownClientServer() throws Exception {
//        stackClient.disconnect().get();
//        stackServer.shutdown().get();
//    }
//
//    protected EndpointDescription selectEndpoint(List<EndpointDescription> endpoints) {
//        return endpoints.get(0);
//    }
//
//    protected UaStackClientConfigBuilder configureClient(UaStackClientConfigBuilder builder) {
//        return builder;
//    }
//
//    protected UaStackServerConfigBuilder configureServer(UaStackServerConfigBuilder builder) {
//        return builder;
//    }
//
//    protected int getTcpBindPort() {
//        return tcpBindPort;
//    }
//
//    protected String getDiscoveryUrl() {
//        return String.format("opc.tcp://localhost:%d/test", getTcpBindPort());
//    }
//
//    public static class TestTcpStackIntegrationTest extends StackIntegrationTest {
//
//        @Test
//        public void test() {
//        }
//
//    }
//
// }
