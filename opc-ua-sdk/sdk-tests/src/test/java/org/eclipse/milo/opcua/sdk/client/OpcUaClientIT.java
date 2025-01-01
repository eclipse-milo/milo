/// *
// * Copyright (c) 2016 Kevin Herron
// *
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the Eclipse Public License v1.0
// * and Eclipse Distribution License v1.0 which accompany this distribution.
// *
// * The Eclipse Public License is available at
// *   http://www.eclipse.org/legal/epl-v10.html
// * and the Eclipse Distribution License is available at
// *   http://www.eclipse.org/org/documents/edl-v10.html.
// */
//
// package org.eclipse.milo.opcua.sdk.client;
//
// import java.security.KeyStore;
// import java.security.PrivateKey;
// import java.security.Security;
// import java.security.cert.X509Certificate;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.EnumSet;
// import java.util.List;
// import java.util.Random;
// import java.util.concurrent.CompletableFuture;
// import java.util.concurrent.ExecutionException;
// import java.util.concurrent.ThreadLocalRandom;
// import java.util.concurrent.TimeUnit;
// import java.util.concurrent.atomic.AtomicBoolean;
// import java.util.concurrent.atomic.AtomicInteger;
// import java.util.concurrent.locks.Condition;
// import java.util.concurrent.locks.Lock;
// import java.util.concurrent.locks.ReentrantLock;
// import java.util.function.BiConsumer;
//
// import com.google.common.collect.ImmutableList;
// import org.bouncycastle.jce.provider.BouncyCastleProvider;
// import org.eclipse.milo.opcua.sdk.client.UaSession;
// import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
// import org.eclipse.milo.opcua.sdk.client.api.identity.UsernameProvider;
// import org.eclipse.milo.opcua.sdk.client.api.identity.X509IdentityProvider;
// import org.eclipse.milo.opcua.sdk.client.api.nodes.VariableNode;
// import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
// import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription;
// import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscriptionManager;
// import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
// import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
// import org.eclipse.milo.opcua.sdk.server.identity.CompositeValidator;
// import org.eclipse.milo.opcua.sdk.server.identity.UsernameIdentityValidator;
// import org.eclipse.milo.opcua.sdk.server.identity.X509IdentityValidator;
// import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
// import org.eclipse.milo.opcua.stack.core.AttributeId;
// import org.eclipse.milo.opcua.stack.core.Identifiers;
// import org.eclipse.milo.opcua.stack.core.Stack;
// import org.eclipse.milo.opcua.stack.core.StatusCodes;
// import org.eclipse.milo.opcua.stack.core.UaException;
// import org.eclipse.milo.opcua.stack.core.UaServiceFaultException;
// import org.eclipse.milo.opcua.stack.core.application.InsecureCertificateValidator;
// import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
// import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
// import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
// import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
// import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
// import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
// import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
// import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
// import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
// import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
// import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
// import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
// import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
// import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
// import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
// import org.eclipse.milo.opcua.stack.core.util.FutureUtils;
// import org.eclipse.milo.opcua.stack.server.tcp.SocketServers;
// import org.jooq.lambda.tuple.Tuple2;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.testng.annotations.AfterTest;
// import org.testng.annotations.BeforeTest;
// import org.testng.annotations.Test;
//
// import static com.google.common.collect.Lists.newArrayList;
// import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
// import static org.testng.Assert.assertEquals;
// import static org.testng.Assert.assertNotEquals;
// import static org.testng.Assert.assertNotNull;
// import static org.testng.Assert.assertTrue;
// import static org.testng.Assert.fail;
//
// public class OpcUaClientIT {
//
//    static {
//        // Required for SecurityPolicy.Aes256_Sha256_RsaPss
//        Security.addProvider(new BouncyCastleProvider());
//    }
//
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//
//    private OpcUaClient client;
//    private OpcUaServer server;
//
//    @BeforeTest
//    public void startClientAndServer() throws Exception {
//        logger.info("startClientAndServer()");
//
//        startServer(ThreadLocalRandom.current().nextInt(1025, 65536));
//        startClient();
//    }
//
//    @AfterTest
//    public void stopClientAndServer() throws ExecutionException, InterruptedException {
//        logger.info("stopClientAndServer()");
//
//        stopClient();
//        stopServer();
//    }
//
//    private void startClient() throws Exception {
//        int port = server.getConfig().getBindPort();
//
//        List<EndpointDescription> endpoints = DiscoveryClient
//            .getEndpoints(String.format("opc.tcp://localhost:%d/test-server", port)).get();
//
//        EndpointDescription endpoint = endpoints.stream()
//            .filter(e -> e.getUri().equals(SecurityPolicy.None.getUri()))
//            .findFirst().orElseThrow(() -> new Exception("no desired endpoints returned"));
//
//        KeyStoreLoader loader = new KeyStoreLoader().load();
//
//        OpcUaClientConfig clientConfig = OpcUaClientConfig.builder()
//            .setApplicationName(LocalizedText.english("Eclipse Milo Test Client"))
//            .setApplicationUri("urn:eclipse:milo:examples:client")
//            .setCertificate(loader.getClientCertificate())
//            .setKeyPair(loader.getClientKeyPair())
//            .setEndpoint(endpoint)
//            .setRequestTimeout(uint(60000))
//            .build();
//
//        client = OpcUaClient.create(clientConfig);
//
//        client.connect().get();
//    }
//
//    private void startServer(int port) throws Exception {
//        UsernameIdentityValidator usernameValidator = new UsernameIdentityValidator(
//            true, // allow anonymous access
//            challenge -> {
//                String user0 = "user";
//                String pass0 = "password";
//
//                char[] cs = new char[1000];
//                Arrays.fill(cs, 'a');
//                String user1 = new String(cs);
//                String pass1 = new String(cs);
//
//                boolean match0 = user0.equals(challenge.getUsername()) &&
//                    pass0.equals(challenge.getPassword());
//
//                boolean match1 = user1.equals(challenge.getUsername()) &&
//                    pass1.equals(challenge.getPassword());
//
//                return match0 || match1;
//            }
//        );
//
//        X509IdentityValidator x509IdentityValidator = new X509IdentityValidator(c -> true);
//
//        List<UserTokenPolicy> userTokenPolicies = newArrayList(
//            OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS,
//            OpcUaServerConfig.USER_TOKEN_POLICY_USERNAME,
//            OpcUaServerConfig.USER_TOKEN_POLICY_X509
//        );
//
//        KeyStoreLoader loader = new KeyStoreLoader().load();
//
//        TestCertificateManager certificateManager = new TestCertificateManager(
//            loader.getServerKeyPair(),
//            loader.getServerCertificate()
//        );
//
//        TestCertificateValidator certificateValidator = new TestCertificateValidator(
//            loader.getClientCertificate()
//        );
//
//        OpcUaServerConfig serverConfig = OpcUaServerConfig.builder()
//            .setApplicationName(LocalizedText.english("Eclipse Milo Test Server"))
//            .setApplicationUri("urn:eclipse:milo:examples:server")
//            .setBindAddresses(newArrayList("localhost"))
//            .setEndpointAddresses(newArrayList("localhost"))
//            .setBindPort(port)
//            .setCertificateManager(certificateManager)
//            .setCertificateValidator(certificateValidator)
//            .setSecurityPolicies(
//                EnumSet.of(
//                    SecurityPolicy.None,
//                    SecurityPolicy.Basic128Rsa15,
//                    SecurityPolicy.Basic256,
//                    SecurityPolicy.Basic256Sha256,
//                    SecurityPolicy.Aes128_Sha256_RsaOaep,
//                    SecurityPolicy.Aes256_Sha256_RsaPss))
//            .setProductUri("urn:digitalpetri:opcua:sdk")
//            .setServerName("test-server")
//            .setUserTokenPolicies(userTokenPolicies)
//            .setIdentityValidator(new CompositeValidator(usernameValidator,
// x509IdentityValidator))
//            .build();
//
//        server = new OpcUaServer(serverConfig);
//
//        // register a CttNamespace so we have some nodes to play with
//        server.getNamespaceManager().registerAndAdd(
//            TestNamespace.NAMESPACE_URI,
//            idx -> new TestNamespace(server, idx));
//
//        server.startup().get();
//    }
//
//    private void stopClient() {
//        try {
//            client.disconnect().get();
//        } catch (InterruptedException | ExecutionException e) {
//            logger.warn("Error disconnecting client.", e);
//        }
//    }
//
//    private void stopServer() throws InterruptedException, ExecutionException {
//        server.shutdown().get();
//        SocketServers.shutdownAll().get();
//    }
//
//    @Test
//    public void testRead() throws Exception {
//        logger.info("testRead()");
//
//        VariableNode currentTimeNode = client.getAddressSpace()
//            .createVariableNode(Identifiers.Server_ServerStatus_CurrentTime);
//
//        assertNotNull(currentTimeNode.getValue().get());
//    }
//
//    @Test
//    public void testWrite() throws Exception {
//        logger.info("testWrite()");
//
//        NodeId nodeId = new NodeId(2, "/Static/AllProfiles/Scalar/Int32");
//
//        VariableNode variableNode = client.getAddressSpace().createVariableNode(nodeId);
//
//        // read the existing value
//        Object valueBefore = variableNode.getValue().get();
//        assertNotNull(valueBefore);
//
//        // write a new random value
//        DataValue newValue = new DataValue(new Variant(new Random().nextInt()));
//        StatusCode writeStatus = variableNode.writeValue(newValue).get();
//        assertTrue(writeStatus.isGood());
//
//        // read the value again
//        Object valueAfter = variableNode.getValue().get();
//        assertNotNull(valueAfter);
//
//        assertNotEquals(valueBefore, valueAfter);
//    }
//
//    @Test
//    public void testSubscribe() throws Exception {
//        logger.info("testSubscribe()");
//
//        // create a subscription and a monitored item
//        UaSubscription subscription =
// client.getSubscriptionManager().createSubscription(1000.0).get();
//
//        List<MonitoredItemCreateRequest> requests = new ArrayList<>();
//
//        for (int i = 0; i < 10; i++) {
//            ReadValueId readValueId = new ReadValueId(
//                Identifiers.Server_ServerStatus_State,
//                AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE);
//
//            MonitoringParameters parameters = new MonitoringParameters(
//                uint(i),    // client handle
//                1000.0,     // sampling interval
//                null,       // no (default) filter
//                uint(10),   // queue size
//                true);      // discard oldest
//
//            MonitoredItemCreateRequest request = new MonitoredItemCreateRequest(
//                readValueId, MonitoringMode.Reporting, parameters);
//
//            requests.add(request);
//        }
//
//        @SuppressWarnings("unchecked")
//        CompletableFuture<DataValue>[] cfs = new CompletableFuture[10];
//
//        for (int i = 0; i < 10; i++) {
//            CompletableFuture<DataValue> f = new CompletableFuture<>();
//            f.thenAccept(value -> logger.info("received {}", value));
//            cfs[i] = f;
//        }
//
//        BiConsumer<UaMonitoredItem, Integer> callback =
//            (item, idx) -> item.setValueConsumer(cfs[idx]::complete);
//
//        List<UaMonitoredItem> items = subscription
//            .createMonitoredItems(TimestampsToReturn.Both, requests, callback).get();
//
//        assertTrue(items.stream().allMatch(item -> item.getStatusCode().isGood()));
//        assertNotNull(FutureUtils.sequence(cfs).get(5, TimeUnit.SECONDS));
//    }
//
//    @Test
//    public void testSubscribe_DataChangeNotification() throws Exception {
//        CompletableFuture<Void> future = new CompletableFuture<>();
//
//        // create a subscription and a monitored item
//        UaSubscription subscription =
// client.getSubscriptionManager().createSubscription(1000.0).get();
//
//        subscription.addNotificationListener(new UaSubscription.NotificationListener() {
//            @Override
//            public void onDataChangeNotification(UaSubscription subscription,
//                                                 ImmutableList<Tuple2<UaMonitoredItem, DataValue>>
// itemValues,
//                                                 DateTime publishTime) {
//
//                for (Tuple2<UaMonitoredItem, DataValue> itemValue : itemValues) {
//                    UaMonitoredItem item = itemValue.v1();
//                    DataValue value = itemValue.v2();
//
//                    logger.info("item={}, value={}", item.getReadValueId().getNodeId(), value);
//                }
//
//                future.complete(null);
//            }
//        });
//
//        ReadValueId readValueId = new ReadValueId(
//            Identifiers.Server_ServerStatus_State,
//            AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE);
//
//        MonitoringParameters parameters = new MonitoringParameters(
//            uint(1),    // client handle
//            1000.0,     // sampling interval
//            null,       // no (default) filter
//            uint(10),   // queue size
//            true);      // discard oldest
//
//        MonitoredItemCreateRequest request = new MonitoredItemCreateRequest(
//            readValueId, MonitoringMode.Reporting, parameters);
//
//        subscription.createMonitoredItems(TimestampsToReturn.Both, newArrayList(request)).get();
//
//        future.get(5, TimeUnit.SECONDS);
//    }
//
//    @Test(enabled = false)
//    public void testTransferSubscriptions() throws Exception {
//        logger.info("testTransferSubscriptions()");
//
//        // create a subscription and a monitored item
//        UaSubscription subscription =
// client.getSubscriptionManager().createSubscription(1000.0).get();
//
//        NodeId nodeId = new NodeId(2, "/Static/AllProfiles/Scalar/Int32");
//
//        ReadValueId readValueId = new ReadValueId(
//            nodeId, AttributeId.Value.uid(),
//            null, QualifiedName.NULL_VALUE);
//
//        MonitoringParameters parameters = new MonitoringParameters(
//            uint(1),    // client handle
//            100.0,      // sampling interval
//            null,       // no (default) filter
//            uint(10),   // queue size
//            true);      // discard oldest
//
//        MonitoredItemCreateRequest request = new MonitoredItemCreateRequest(
//            readValueId, MonitoringMode.Reporting, parameters);
//
//        List<UaMonitoredItem> items = subscription
//            .createMonitoredItems(TimestampsToReturn.Both, newArrayList(request)).get();
//
//        // do something with the value updates
//        UaMonitoredItem item = items.get(0);
//
//        AtomicInteger updateCount = new AtomicInteger(0);
//
//        item.setValueConsumer(v -> {
//            int count = updateCount.incrementAndGet();
//            logger.info("updateCount={}", count);
//        });
//
//        AtomicBoolean subscriptionTransferred = new AtomicBoolean(true);
//
//        client.getSubscriptionManager().addSubscriptionListener(new
// UaSubscriptionManager.SubscriptionListener() {
//            @Override
//            public void onKeepAlive(UaSubscription subscription, DateTime publishTime) {
//
//            }
//
//            @Override
//            public void onStatusChanged(UaSubscription subscription, StatusCode status) {
//
//            }
//
//            @Override
//            public void onPublishFailure(UaException exception) {
//
//            }
//
//            @Override
//            public void onNotificationDataLost(UaSubscription subscription) {
//
//            }
//
//            @Override
//            public void onSubscriptionTransferFailed(UaSubscription subscription, StatusCode
// statusCode) {
//                subscriptionTransferred.set(false);
//            }
//        });
//
//        logger.info("killing the session...");
//        UaSession uaSession = client.getSession().get();
//        server.getSessionManager().killSession(uaSession.getSessionId(), false);
//
//        logger.info("sleeping while waiting for an update");
//        Thread.sleep(5000);
//
//        // one update for the initial subscribe, another after transfer
//        assertEquals(2, updateCount.get());
//
//        assertTrue(subscriptionTransferred.get());
//
//        client.disconnect().get();
//    }
//
//    @Test
//    public void testUsernamePassword() throws Exception {
//        logger.info("testUsernamePassword()");
//
//        int port = server.getConfig().getBindPort();
//
//        List<EndpointDescription> endpoints = DiscoveryClient
//            .getEndpoints(String.format("opc.tcp://localhost:%d/test-server", port)).get();
//
//        EndpointDescription endpoint = endpoints.stream()
//            .filter(e -> e.getUri().equals(SecurityPolicy.None.getUri()))
//            .findFirst().orElseThrow(() -> new Exception("no desired endpoints returned"));
//
//        KeyStoreLoader loader = new KeyStoreLoader().load();
//
//        OpcUaClientConfig clientConfig = OpcUaClientConfig.builder()
//            .setApplicationName(LocalizedText.english("Eclipse Milo Test Client"))
//            .setApplicationUri("urn:eclipse:milo:test:client")
//            .setCertificate(loader.getClientCertificate())
//            .setKeyPair(loader.getClientKeyPair())
//            .setEndpoint(endpoint)
//            .setRequestTimeout(uint(60000))
//            .setIdentityProvider(new UsernameProvider("user", "password", new
// InsecureCertificateValidator()))
//            .build();
//
//        OpcUaClient client = OpcUaClient.create(clientConfig);
//
//        client.connect().get();
//    }
//
//    /**
//     * Test using a username and password long enough that the encryption requires multiple
// ciphertext blocks.
//     */
//    @Test
//    public void testUsernamePassword_MultiBlock() throws Exception {
//        logger.info("testUsernamePassword_MultiBlock()");
//
//        int port = server.getConfig().getBindPort();
//
//        List<EndpointDescription> endpoints = DiscoveryClient
//            .getEndpoints(String.format("opc.tcp://localhost:%d/test-server", port)).get();
//
//        EndpointDescription endpoint = endpoints.stream()
//            .filter(e -> e.getUri().equals(SecurityPolicy.None.getUri()))
//            .findFirst().orElseThrow(() -> new Exception("no desired endpoints returned"));
//
//        char[] cs = new char[1000];
//        Arrays.fill(cs, 'a');
//        String user = new String(cs);
//        String pass = new String(cs);
//
//
//        KeyStoreLoader loader = new KeyStoreLoader().load();
//
//        OpcUaClientConfig clientConfig = OpcUaClientConfig.builder()
//            .setApplicationName(LocalizedText.english("Eclipse Milo Test Client"))
//            .setApplicationUri("urn:eclipse:milo:test:client")
//            .setCertificate(loader.getClientCertificate())
//            .setKeyPair(loader.getClientKeyPair())
//            .setEndpoint(endpoint)
//            .setRequestTimeout(uint(60000))
//            .setIdentityProvider(new UsernameProvider(user, pass, new
// InsecureCertificateValidator()))
//            .build();
//
//        OpcUaClient client = OpcUaClient.create(clientConfig);
//
//        client.connect().get();
//    }
//
//    @Test
//    public void testUsernamePassword_WithSecurity() throws Exception {
//        logger.info("testUsernamePassword_WithSecurity()");
//
//        int port = server.getConfig().getBindPort();
//
//        List<EndpointDescription> endpoints = DiscoveryClient
//            .getEndpoints(String.format("opc.tcp://localhost:%d/test-server", port)).get();
//
//        EndpointDescription endpoint = endpoints.stream()
//            .filter(e -> e.getUri().equals(SecurityPolicy.Aes256_Sha256_RsaPss.getUri()))
//            .findFirst().orElseThrow(() -> new Exception("no desired endpoints returned"));
//
//        KeyStoreLoader loader = new KeyStoreLoader().load();
//
//        OpcUaClientConfig clientConfig = OpcUaClientConfig.builder()
//            .setApplicationName(LocalizedText.english("Eclipse Milo Test Client"))
//            .setApplicationUri("urn:eclipse:milo:examples:client")
//            .setCertificate(loader.getClientCertificate())
//            .setKeyPair(loader.getClientKeyPair())
//            .setEndpoint(endpoint)
//            .setRequestTimeout(uint(60000))
//            .setIdentityProvider(new UsernameProvider("user", "password", new
// InsecureCertificateValidator()))
//            .build();
//
//        OpcUaClient client = OpcUaClient.create(clientConfig);
//
//        client.connect().get();
//    }
//
//    @Test
//    public void testX509IdentityProvider_NoSecurity() throws Exception {
//        testX509IdentityProvider(SecurityPolicy.None);
//    }
//
//    @Test
//    public void testX509IdentityProvider_Basic128() throws Exception {
//        testX509IdentityProvider(SecurityPolicy.Basic128Rsa15);
//    }
//
//    private void testX509IdentityProvider(SecurityPolicy securityPolicy) throws Exception {
//        logger.info("testX509IdentityProvider({})", securityPolicy);
//
//        int port = server.getConfig().getBindPort();
//
//        List<EndpointDescription> endpoints = DiscoveryClient
//            .getEndpoints(String.format("opc.tcp://localhost:%d/test-server", port)).get();
//
//        EndpointDescription endpoint = endpoints.stream()
//            .filter(e -> e.getUri().equals(securityPolicy.getUri()))
//            .findFirst().orElseThrow(() -> new Exception("no desired endpoints returned"));
//
//        KeyStoreLoader loader = new KeyStoreLoader().load();
//
//        KeyStore keyStore = KeyStore.getInstance("PKCS12");
//        keyStore.load(getClass().getClassLoader().getResourceAsStream("test-identity.pfx"),
// "password".toCharArray());
//
//        X509Certificate identityCertificate = (X509Certificate)
// keyStore.getCertificate("identity");
//        PrivateKey identityPrivateKey = (PrivateKey) keyStore.getKey("identity",
// "password".toCharArray());
//
//        OpcUaClientConfig clientConfig = OpcUaClientConfig.builder()
//            .setApplicationName(LocalizedText.english("Eclipse Milo Test Client"))
//            .setApplicationUri("urn:eclipse:milo:examples:client")
//            .setEndpoint(endpoint)
//            .setKeyPair(loader.getClientKeyPair())
//            .setCertificate(loader.getClientCertificate())
//            .setRequestTimeout(uint(60000))
//            .setCertificate(loader.getClientCertificate())
//            .setKeyPair(loader.getClientKeyPair())
//            .setIdentityProvider(new X509IdentityProvider(identityCertificate,
// identityPrivateKey))
//            .build();
//
//        OpcUaClient client = OpcUaClient.create(clientConfig);
//
//        client.connect().get();
//    }
//
//    @Test
//    public void testConnectAndDisconnect() throws Exception {
//        logger.info("testConnectAndDisconnect()");
//
//        int port = server.getConfig().getBindPort();
//
//        List<EndpointDescription> endpoints = DiscoveryClient
//            .getEndpoints(String.format("opc.tcp://localhost:%d/test-server", port)).get();
//
//        EndpointDescription endpoint = endpoints.stream()
//            .filter(e -> e.getUri().equals(SecurityPolicy.None.getUri()))
//            .findFirst().orElseThrow(() -> new Exception("no desired endpoints returned"));
//
//        class ConnectDisconnect implements Runnable {
//            private final int threadNumber;
//
//            private ConnectDisconnect(int threadNumber) throws UaException {
//                this.threadNumber = threadNumber;
//            }
//
//            private OpcUaClientConfig clientConfig = OpcUaClientConfig.builder()
//                .setApplicationName(LocalizedText.english("Eclipse Milo Test Client"))
//                .setApplicationUri("urn:eclipse:milo:test:client")
//                .setEndpoint(endpoint)
//                .setRequestTimeout(uint(10000))
//                .build();
//
//            private OpcUaClient client = OpcUaClient.create(clientConfig);
//
//            @Override
//            public void run() {
//                for (int i = 0; i < 100; i++) {
//                    try {
//                        client.connect().get();
//
//                        client.readValues(
//                            0.0,
//                            TimestampsToReturn.Both,
//                            newArrayList(Identifiers.Server_ServerStatus_CurrentTime)
//                        ).get();
//
//                        client.disconnect().get();
//
//                        Thread.sleep(10);
//                    } catch (InterruptedException | ExecutionException e) {
//                        fail(e.getMessage(), e);
//                    }
//                }
//                logger.info("Thread {} done.", threadNumber);
//            }
//        }
//
//        Thread[] threads = new Thread[4];
//
//        for (int i = 0; i < threads.length; i++) {
//            threads[i] = new Thread(new ConnectDisconnect(i));
//            threads[i].start();
//        }
//
//        for (Thread thread : threads) {
//            thread.join();
//        }
//
//        client.connect().get();
//    }
//
//    @Test
//    public void testReactivate() throws Exception {
//        logger.info("testReactivate()");
//
//        int port = server.getConfig().getBindPort();
//
//        List<EndpointDescription> endpoints = DiscoveryClient
//            .getEndpoints(String.format("opc.tcp://localhost:%d/test-server", port)).get();
//
//        EndpointDescription endpoint = endpoints.stream()
//            .filter(e -> e.getUri().equals(SecurityPolicy.None.getUri()))
//            .findFirst().orElseThrow(() -> new Exception("no desired endpoints returned"));
//
//        OpcUaClientConfig clientConfig = OpcUaClientConfig.builder()
//            .setApplicationName(LocalizedText.english("Eclipse Milo Test Client"))
//            .setApplicationUri("urn:eclipse:milo:test:client")
//            .setEndpoint(endpoint)
//            .setRequestTimeout(uint(10000))
//            .build();
//
//        OpcUaClient client = OpcUaClient.create(clientConfig);
//
//        client.connect().get();
//
//        VariableNode currentTimeNode = client.getAddressSpace()
//            .createVariableNode(Identifiers.Server_ServerStatus_CurrentTime);
//
//        assertNotNull(currentTimeNode.getValue().get());
//
//        // Kill the session. Client can't and won't be notified of this.
//        logger.info("killing session...");
//        UaSession session = client.getSession().get();
//        server.getSessionManager().killSession(session.getSessionId(), true);
//
//        // Expect the next action to fail because the session is no longer valid.
//        try {
//            logger.info("reading, expecting failure...");
//            currentTimeNode.getValue().get();
//        } catch (Throwable t) {
//            StatusCode statusCode = UaServiceFaultException.extract(t)
//                .map(UaException::getStatusCode)
//                .orElse(StatusCode.BAD);
//
//            assertEquals(StatusCodes.Bad_SessionIdInvalid, statusCode.getValue());
//        }
//
//        Thread.sleep(1000);
//
//        // Force a reactivate and read.
//        logger.info("reconnecting...");
//        client.connect().get();
//
//        logger.info("reading, expecting success...");
//        assertNotNull(currentTimeNode.getValue().get());
//
//        client.disconnect().get();
//    }
//
//    /**
//     * Create a subscription, adds a monitored item, then waits for the value to arrive.
//     * Stop and start the server. This should invoke the onSubscriptionTransferFailed() callback
// because when the
//     * client reconnects the server would not be able to transfer a subscription after it lost all
// its state.
//     * Again, create a subscription, add a monitored item, then wait for the value to arrive.
//     *
//     * @throws Exception if anything goes wrong during the test.
//     */
//    @Test
//    public void testFailedSubscriptionTransfer() throws Exception {
//        CompletableFuture<Void> future = new CompletableFuture<>();
//
//        final Lock lock = new ReentrantLock();
//        final Condition notificationArrived = lock.newCondition();
//
//        UaSubscription.NotificationListener notificationListener = new
// UaSubscription.NotificationListener() {
//            @Override
//            public void onDataChangeNotification(UaSubscription subscription,
//                                                 ImmutableList<Tuple2<UaMonitoredItem, DataValue>>
// itemValues,
//                                                 DateTime publishTime) {
//
//                for (Tuple2<UaMonitoredItem, DataValue> itemValue : itemValues) {
//                    UaMonitoredItem item = itemValue.v1();
//                    DataValue value = itemValue.v2();
//
//                    logger.info("item={}, value={}", item.getReadValueId().getNodeId(), value);
//                }
//
//                try {
//                    lock.lock();
//                    notificationArrived.signalAll();
//                } finally {
//                    lock.unlock();
//                }
//            }
//        };
//
//        client.getSubscriptionManager().addSubscriptionListener(new
// UaSubscriptionManager.SubscriptionListener() {
//            @Override
//            public void onSubscriptionTransferFailed(UaSubscription subscription, StatusCode
// statusCode) {
//                Stack.sharedExecutor().execute(() -> {
//                    try {
//                        createItemAndWait(notificationListener, lock, notificationArrived);
//
//                        future.complete(null);
//                    } catch (InterruptedException | ExecutionException e) {
//                        logger.error("Error creating Subscription: {}", e.getMessage(), e);
//
//                        future.completeExceptionally(e);
//                    }
//                });
//            }
//        });
//
//
//        createItemAndWait(notificationListener, lock, notificationArrived);
//
//        stopServer();
//        startServer(server.getConfig().getBindPort());
//
//        future.get(30, TimeUnit.SECONDS);
//    }
//
//    private void createItemAndWait(
//        UaSubscription.NotificationListener notificationListener,
//        Lock lock,
//        Condition notificationArrived) throws InterruptedException, ExecutionException {
//
//        // create a subscription and a monitored item
//        UaSubscription subscription =
// client.getSubscriptionManager().createSubscription(1000.0).get();
//        subscription.addNotificationListener(notificationListener);
//
//        ReadValueId readValueId = new ReadValueId(
//            Identifiers.Server_ServerStatus_State,
//            AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE);
//
//        MonitoringParameters parameters = new MonitoringParameters(
//            uint(1),    // client handle
//            1000.0,     // sampling interval
//            null,       // no (default) filter
//            uint(10),   // queue size
//            true);      // discard oldest
//
//        MonitoredItemCreateRequest request = new MonitoredItemCreateRequest(
//            readValueId, MonitoringMode.Reporting, parameters);
//
//        lock.lock();
//        try {
//            subscription.createMonitoredItems(TimestampsToReturn.Both,
// newArrayList(request)).get();
//            notificationArrived.await(5, TimeUnit.SECONDS);
//        } finally {
//            lock.unlock();
//        }
//    }
//
// }
