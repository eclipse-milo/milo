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
// import java.security.cert.X509Certificate;
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.LinkedHashSet;
// import java.util.List;
// import java.util.Set;
// import java.util.UUID;
// import java.util.concurrent.CompletableFuture;
//
// import org.bouncycastle.jce.provider.BouncyCastleProvider;
// import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
// import org.eclipse.milo.opcua.stack.client.UaStackClient;
// import org.eclipse.milo.opcua.stack.client.UaStackClientConfig;
// import org.eclipse.milo.opcua.stack.core.AttributeId;
// import org.eclipse.milo.opcua.stack.core.Stack;
// import org.eclipse.milo.opcua.stack.core.StatusCodes;
// import org.eclipse.milo.opcua.stack.core.UaException;
// import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
// import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
// import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
// import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
// import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
// import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
// import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
// import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
// import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
// import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
// import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
// import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
// import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
// import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
// import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
// import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
// import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
// import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
// import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
// import org.eclipse.milo.opcua.stack.core.types.structured.ReadRequest;
// import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
// import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
// import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
// import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
// import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
// import org.eclipse.milo.opcua.stack.core.util.FutureUtils;
// import org.eclipse.milo.opcua.stack.server.EndpointConfiguration;
// import org.eclipse.milo.opcua.stack.server.UaStackServer;
// import org.eclipse.milo.opcua.stack.server.UaStackServerConfig;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.testng.annotations.AfterSuite;
// import org.testng.annotations.BeforeSuite;
// import org.testng.annotations.DataProvider;
// import org.testng.annotations.Test;
//
// import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
// import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
// import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
// import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
// import static org.eclipse.milo.opcua.stack.core.util.ConversionUtil.a;
// import static org.eclipse.milo.opcua.stack.core.util.ConversionUtil.l;
// import static org.testng.Assert.assertEquals;
// import static org.testng.Assert.assertThrows;
// import static org.testng.Assert.fail;
//
// public class ClientServerTest extends SecurityFixture {
//
//    static {
//        Security.addProvider(new BouncyCastleProvider());
//
//        Stack.ConnectionLimits.RATE_LIMIT_ENABLED = false;
//    }
//
//    private static final UInteger DEFAULT_TIMEOUT_HINT = uint(60000);
//
//    @DataProvider
//    public Object[][] getVariants() {
//        return new Object[][]{
//            {new Variant(true)},
//            {new Variant((byte) 1)},
//            {new Variant(ubyte(1))},
//            {new Variant((short) 1)},
//            {new Variant(ushort(1))},
//            {new Variant(1)},
//            {new Variant(uint(1))},
//            {new Variant(1L)},
//            {new Variant(ulong(1L))},
//            {new Variant(3.14f)},
//            {new Variant(6.12d)},
//            {new Variant("hello, world")},
//            {new Variant(DateTime.now())},
//            {new Variant(UUID.randomUUID())},
//            {new Variant(ByteString.of(new byte[]{1, 2, 3, 4}))},
//            {new Variant(new XmlElement("<tag>hello</tag>"))},
//            {new Variant(new NodeId(0, 42))},
//            {new Variant(new ExpandedNodeId(ushort(1), "uri", uint(42), uint(1)))},
//            {new Variant(StatusCode.GOOD)},
//            {new Variant(new QualifiedName(0, "QualifiedName"))},
//            {new Variant(LocalizedText.english("LocalizedText"))},
//            {new Variant(ExtensionObject.encode(
//                new TestEncodingContext(),
//                new ReadValueId(NodeId.NULL_VALUE, uint(1), null, new QualifiedName(0,
// "DataEncoding"))
//            ))},
//        };
//    }
//
//    private Logger logger = LoggerFactory.getLogger(getClass());
//
//    private EndpointDescription[] endpoints;
//
//    private UaStackServer server;
//
//    @BeforeSuite
//    public void setUpClientServer() throws Exception {
//        super.setUp();
//
//        UaStackServerConfig config = UaStackServerConfig.builder()
//            .setCertificateManager(serverCertificateManager)
//            .setCertificateValidator(serverCertificateValidator)
//            .setEndpoints(createEndpointConfigurations(serverCertificate))
//            .build();
//
//        server = new UaStackServer(config);
//
//        setReadRequestHandler(new Variant(42));
//
//        server.startup().get();
//
//        endpoints = DiscoveryClient.getEndpoints("opc.tcp://localhost:12685/test")
//            .get()
//            .toArray(new EndpointDescription[0]);
//    }
//
//    @AfterSuite
//    public void tearDownClientServer() throws Exception {
//        server.shutdown().get();
//    }
//
//    private void setReadRequestHandler(Variant variant) {
//        server.addServiceHandler("/test", ReadRequest.TYPE_ID, service -> {
//            ReadRequest request = (ReadRequest) service.getRequest();
//
//            ResponseHeader header = new ResponseHeader(
//                DateTime.now(),
//                request.getRequestHeader().getRequestHandle(),
//                StatusCode.GOOD,
//                null,
//                null,
//                null
//            );
//
//            List<ReadValueId> nodesToRead = List.of(request.getNodesToRead());
//            List<DataValue> results = Collections.nCopies(nodesToRead.size(), new
// DataValue(variant));
//
//            ReadResponse response = new ReadResponse(header, a(results, DataValue.class), null);
//
//            service.setResponse(response);
//        });
//    }
//
//    @Test(dataProvider = "getVariants")
//    public void testClientServerRoundTrip_TestStack_NoSecurity(Variant input) throws Exception {
//        EndpointDescription endpoint = endpoints[0];
//
//        logger.info("SecurityPolicy={}, MessageSecurityMode={}, input={}",
//            SecurityPolicy.fromUri(endpoint.getSecurityPolicyUri()), endpoint.getSecurityMode(),
// input);
//
//        UaStackClient client = createClient(endpoint);
//
//        connectAndTest(input, client);
//    }
//
//    @Test(dataProvider = "getVariants")
//    public void testClientServerRoundTrip_TestStack_Basic128Rsa15_Sign(Variant input) throws
// Exception {
//        EndpointDescription endpoint = endpoints[1];
//
//        logger.info("SecurityPolicy={}, MessageSecurityMode={}, input={}",
//            SecurityPolicy.fromUri(endpoint.getSecurityPolicyUri()), endpoint.getSecurityMode(),
// input);
//
//        UaStackClient client = createClient(endpoint);
//
//        connectAndTest(input, client);
//    }
//
//    @Test(dataProvider = "getVariants")
//    public void testClientServerRoundTrip_TestStack_Basic256_Sign(Variant input) throws Exception
// {
//        EndpointDescription endpoint = endpoints[2];
//
//        logger.info("SecurityPolicy={}, MessageSecurityMode={}, input={}",
//            SecurityPolicy.fromUri(endpoint.getSecurityPolicyUri()), endpoint.getSecurityMode(),
// input);
//
//        UaStackClient client = createClient(endpoint);
//
//        connectAndTest(input, client);
//    }
//
//    @Test(dataProvider = "getVariants")
//    public void testClientServerRoundTrip_TestStack_Basic256Sha256_Sign(Variant input) throws
// Exception {
//        EndpointDescription endpoint = endpoints[3];
//
//        logger.info("SecurityPolicy={}, MessageSecurityMode={}, input={}",
//            SecurityPolicy.fromUri(endpoint.getSecurityPolicyUri()), endpoint.getSecurityMode(),
// input);
//
//        UaStackClient client = createClient(endpoint);
//
//        connectAndTest(input, client);
//    }
//
//    @Test(dataProvider = "getVariants")
//    public void testClientServerRoundTrip_TestStack_Basic128Rsa15_SignAndEncrypt(Variant input)
// throws Exception {
//        EndpointDescription endpoint = endpoints[4];
//
//        logger.info("SecurityPolicy={}, MessageSecurityMode={}, input={}",
//            SecurityPolicy.fromUri(endpoint.getSecurityPolicyUri()), endpoint.getSecurityMode(),
// input);
//
//        UaStackClient client = createClient(endpoint);
//
//        connectAndTest(input, client);
//    }
//
//    @Test(dataProvider = "getVariants")
//    public void testClientServerRoundTrip_TestStack_Basic256_SignAndEncrypt(Variant input) throws
// Exception {
//        EndpointDescription endpoint = endpoints[5];
//
//        logger.info("SecurityPolicy={}, MessageSecurityMode={}, input={}",
//            SecurityPolicy.fromUri(endpoint.getSecurityPolicyUri()), endpoint.getSecurityMode(),
// input);
//
//        UaStackClient client = createClient(endpoint);
//
//        connectAndTest(input, client);
//    }
//
//    @Test(dataProvider = "getVariants")
//    public void testClientServerRoundTrip_TestStack_Basic256Sha256_SignAndEncrypt(Variant input)
// throws Exception {
//        EndpointDescription endpoint = endpoints[6];
//
//        logger.info("SecurityPolicy={}, MessageSecurityMode={}, input={}",
//            SecurityPolicy.fromUri(endpoint.getSecurityPolicyUri()), endpoint.getSecurityMode(),
// input);
//
//        UaStackClient client = createClient(endpoint);
//
//        connectAndTest(input, client);
//    }
//
//    @Test
//    public void testClientStateMachine() throws Exception {
//        EndpointDescription endpoint = endpoints[0];
//
//        Variant input = new Variant(42);
//        logger.info("SecurityPolicy={}, MessageSecurityMode={}, input={}",
//            SecurityPolicy.fromUri(endpoint.getSecurityPolicyUri()), endpoint.getSecurityMode(),
// input);
//
//        UaStackClient client = createClient(endpoint);
//
//        for (int i = 0; i < 1000; i++) {
//            client.connect().get();
//
//            RequestHeader header = new RequestHeader(
//                NodeId.NULL_VALUE,
//                DateTime.now(),
//                uint(i),
//                uint(0),
//                null,
//                DEFAULT_TIMEOUT_HINT,
//                null
//            );
//
//            ReadRequest request = new ReadRequest(
//                header,
//                0.0,
//                TimestampsToReturn.Neither,
//                new ReadValueId[]{
//                    new ReadValueId(
//                        NodeId.NULL_VALUE,
//                        AttributeId.Value.uid(),
//                        null,
//                        null)
//                }
//            );
//
//            logger.debug("sending request: {}", request);
//            UaResponseMessageType response = client.sendRequest(request).get();
//            logger.debug("got response: {}", response);
//
//            client.disconnect().get();
//        }
//    }
//
//    @Test
//    public void testClientDisconnect() throws Exception {
//        EndpointDescription endpoint = endpoints[0];
//        Variant input = new Variant(42);
//
//        logger.info("SecurityPolicy={}, MessageSecurityMode={}, input={}",
//            SecurityPolicy.fromUri(endpoint.getSecurityPolicyUri()), endpoint.getSecurityMode(),
// input);
//
//        UaStackClient client = createClient(endpoint);
//
//        client.connect().get();
//
//        RequestHeader header = new RequestHeader(
//            NodeId.NULL_VALUE,
//            DateTime.now(),
//            uint(0),
//            uint(0),
//            null,
//            DEFAULT_TIMEOUT_HINT,
//            null
//        );
//
//        ReadRequest request = new ReadRequest(
//            header,
//            0.0,
//            TimestampsToReturn.Neither,
//            new ReadValueId[]{
//                new ReadValueId(
//                    NodeId.NULL_VALUE,
//                    AttributeId.Value.uid(),
//                    null,
//                    null)
//            }
//        );
//
//        logger.info("sending request: {}", request);
//        UaResponseMessageType response0 = client.sendRequest(request).get();
//        logger.info("got response: {}", response0);
//
//        client.disconnect().get();
//
//        assertThrows(() -> client.sendRequest(request).get());
//    }
//
//    @Test
//    public void testClientReconnect() throws Exception {
//        EndpointDescription endpoint = endpoints[0];
//        Variant input = new Variant(42);
//
//        logger.info("SecurityPolicy={}, MessageSecurityMode={}, input={}",
//            SecurityPolicy.fromUri(endpoint.getSecurityPolicyUri()), endpoint.getSecurityMode(),
// input);
//
//        UaStackClient client = createClient(endpoint);
//
//        client.connect().get();
//
//        RequestHeader header = new RequestHeader(
//            NodeId.NULL_VALUE,
//            DateTime.now(),
//            uint(0),
//            uint(0),
//            null,
//            DEFAULT_TIMEOUT_HINT,
//            null
//        );
//
//        ReadRequest request = new ReadRequest(
//            header,
//            0.0,
//            TimestampsToReturn.Neither,
//            new ReadValueId[]{
//                new ReadValueId(
//                    NodeId.NULL_VALUE,
//                    AttributeId.Value.uid(),
//                    null,
//                    null)
//            }
//        );
//
//        logger.info("sending request: {}", request);
//        UaResponseMessageType response0 = client.sendRequest(request).get();
//        logger.info("got response: {}", response0);
//
//        logger.info("initiating a reconnect by closing channel in server...");
//        server.getConnectedChannels().forEach(c -> {
//            try {
//                c.close().await();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//
//        logger.info("sending request: {}", request);
//        try {
//            UaResponseMessageType response1 = client.sendRequest(request).get();
//            logger.info("got response: {}", response1);
//        } catch (Exception e) {
//            // try again because close() above is a race condition
//            UaResponseMessageType response1 = client.sendRequest(request).get();
//            logger.info("got response: {}", response1);
//        }
//
//        client.disconnect().get();
//    }
//
//    @Test
//    public void testClientTimeout() throws Exception {
//        EndpointDescription endpoint = endpoints[0];
//
//        logger.info("SecurityPolicy={}, MessageSecurityMode={}",
//            SecurityPolicy.fromUri(endpoint.getSecurityPolicyUri()), endpoint.getSecurityMode());
//
//        UaStackClientConfig config = UaStackClientConfig.builder()
//            .setEndpoint(endpoint)
//            .setKeyPair(clientKeyPair)
//            .setCertificate(clientCertificate)
//            .build();
//
//        UaStackClient client = UaStackClient.create(config);
//        client.connect().get();
//
//        server.addServiceHandler("/test", ReadRequest.TYPE_ID, service -> {
//            // intentionally do nothing so the request can timeout
//            logger.info("received {}; ignoring...", service.getRequest());
//        });
//
//        RequestHeader header = new RequestHeader(
//            NodeId.NULL_VALUE,
//            DateTime.now(),
//            uint(0),
//            uint(0),
//            null,
//            uint(1000),
//            null
//        );
//
//        ReadRequest request = new ReadRequest(
//            header,
//            0.0,
//            TimestampsToReturn.Neither,
//            new ReadValueId[]{
//                new ReadValueId(
//                    NodeId.NULL_VALUE,
//                    AttributeId.Value.uid(),
//                    null,
//                    null)
//            }
//        );
//
//        try {
//            client.sendRequest(request).get();
//
//            fail("expected response to timeout");
//        } catch (Throwable t) {
//            StatusCode statusCode = UaException
//                .extractStatusCode(t)
//                .orElse(StatusCode.BAD);
//
//            assertEquals(statusCode.getValue(), StatusCodes.Bad_Timeout);
//        }
//    }
//
//    private UaStackClient createClient(EndpointDescription endpoint) throws UaException {
//        UaStackClientConfig config = UaStackClientConfig.builder()
//            .setEndpoint(endpoint)
//            .setKeyPair(clientKeyPair)
//            .setCertificate(clientCertificate)
//            .build();
//
//        return UaStackClient.create(config);
//    }
//
//    private void connectAndTest(Variant input, UaStackClient client) throws InterruptedException,
// java.util.concurrent.ExecutionException {
//        setReadRequestHandler(input);
//
//        client.connect().get();
//
//        var responses = new ArrayList<CompletableFuture<ReadResponse>>();
//
//        for (int i = 0; i < 100; i++) {
//            RequestHeader header = new RequestHeader(
//                NodeId.NULL_VALUE,
//                DateTime.now(),
//                uint(i),
//                uint(0),
//                null,
//                uint(10000),
//                null
//            );
//
//            ReadRequest request = new ReadRequest(
//                header,
//                0.0,
//                TimestampsToReturn.Neither,
//                new ReadValueId[]{
//                    new ReadValueId(
//                        NodeId.NULL_VALUE,
//                        AttributeId.Value.uid(),
//                        null,
//                        null)
//                }
//            );
//
//            responses.add(
//                client.sendRequest(request)
//                    .thenApply(ReadResponse.class::cast));
//        }
//
//
//        CompletableFuture.allOf(responses.toArray(new CompletableFuture[0])).get();
//
//        FutureUtils.sequence(responses).get().forEach(response -> {
//            Variant value = List.of(response.getResults()).get(0).getValue();
//
//            assertEquals(value, input);
//        });
//
//        client.disconnect().get();
//    }
//
//    private Set<EndpointConfiguration> createEndpointConfigurations(X509Certificate certificate) {
//        Set<EndpointConfiguration> endpointConfigurations = new LinkedHashSet<>();
//
//        List<String> bindAddresses = new ArrayList<>();
//        bindAddresses.add("localhost");
//
//        Set<String> hostnames = new LinkedHashSet<>();
//        hostnames.add("localhost");
//
//        for (String bindAddress : bindAddresses) {
//            for (String hostname : hostnames) {
//                EndpointConfiguration.Builder builder = EndpointConfiguration.newBuilder()
//                    .setBindAddress(bindAddress)
//                    .setHostname(hostname)
//                    .setPath("/test")
//                    .setCertificate(certificate)
//                    .addTokenPolicies(
//                        USER_TOKEN_POLICY_ANONYMOUS);
//
//
//                /* No Security */
//                EndpointConfiguration.Builder noSecurityBuilder = builder.copy()
//                    .setSecurityPolicy(SecurityPolicy.None)
//                    .setSecurityMode(MessageSecurityMode.None);
//
//                endpointConfigurations.add(buildTcpEndpoint(noSecurityBuilder));
//
//                /* Basic128Rsa15 */
//                endpointConfigurations.add(buildTcpEndpoint(
//                    builder.copy()
//                        .setSecurityPolicy(SecurityPolicy.Basic128Rsa15)
//                        .setSecurityMode(MessageSecurityMode.Sign))
//                );
//
//                endpointConfigurations.add(buildTcpEndpoint(
//                    builder.copy()
//                        .setSecurityPolicy(SecurityPolicy.Basic128Rsa15)
//                        .setSecurityMode(MessageSecurityMode.SignAndEncrypt))
//                );
//
//                /* Basic256 */
//                endpointConfigurations.add(buildTcpEndpoint(
//                    builder.copy()
//                        .setSecurityPolicy(SecurityPolicy.Basic256)
//                        .setSecurityMode(MessageSecurityMode.Sign))
//                );
//
//                endpointConfigurations.add(buildTcpEndpoint(
//                    builder.copy()
//                        .setSecurityPolicy(SecurityPolicy.Basic256)
//                        .setSecurityMode(MessageSecurityMode.SignAndEncrypt))
//                );
//
//                /* Basic256Sha256 */
//                endpointConfigurations.add(buildTcpEndpoint(
//                    builder.copy()
//                        .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
//                        .setSecurityMode(MessageSecurityMode.Sign))
//                );
//
//                endpointConfigurations.add(buildTcpEndpoint(
//                    builder.copy()
//                        .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
//                        .setSecurityMode(MessageSecurityMode.SignAndEncrypt))
//                );
//
//                /*
//                 * It's good practice to provide a discovery-specific endpoint with no security.
//                 * It's required practice if all regular endpoints have security configured.
//                 *
//                 * Usage of the  "/discovery" suffix is defined by OPC UA Part 6:
//                 *
//                 * Each OPC UA Server Application implements the Discovery Service Set. If the OPC
// UA Server requires a
//                 * different address for this Endpoint it shall create the address by appending
// the path "/discovery" to
//                 * its base address.
//                 */
//
//                EndpointConfiguration.Builder discoveryBuilder = builder.copy()
//                    .setPath("/example/discovery")
//                    .setSecurityPolicy(SecurityPolicy.None)
//                    .setSecurityMode(MessageSecurityMode.None);
//
//                endpointConfigurations.add(buildTcpEndpoint(discoveryBuilder));
//            }
//        }
//
//        return endpointConfigurations;
//    }
//
//    private static EndpointConfiguration buildTcpEndpoint(EndpointConfiguration.Builder base) {
//        return base.copy()
//            .setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
//            .setBindPort(12685)
//            .build();
//    }
//
//    /**
//     * A {@link UserTokenPolicy} for anonymous access.
//     */
//    private static final UserTokenPolicy USER_TOKEN_POLICY_ANONYMOUS = new UserTokenPolicy(
//        "anonymous",
//        UserTokenType.Anonymous,
//        null,
//        null,
//        null
//    );
//
// }
