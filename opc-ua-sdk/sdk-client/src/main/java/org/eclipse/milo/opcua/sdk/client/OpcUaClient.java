/*
 * Copyright (c) 2022 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.milo.opcua.sdk.client.model.ObjectTypeInitializer;
import org.eclipse.milo.opcua.sdk.client.model.VariableTypeInitializer;
import org.eclipse.milo.opcua.sdk.client.session.SessionFsm;
import org.eclipse.milo.opcua.sdk.client.session.SessionFsmFactory;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscriptionManager;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.ServerTable;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaServiceFaultException;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingManager;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingManager;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.DataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.DefaultDataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.AddReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.AddReferencesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.AddReferencesResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseNextRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseNextResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePath;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteNodesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteNodesResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteSubscriptionsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteSubscriptionsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifyMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifyMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifySubscriptionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifySubscriptionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemModifyRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.NodeTypeDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.QueryDataSet;
import org.eclipse.milo.opcua.stack.core.types.structured.QueryFirstRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.QueryFirstResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.QueryNextRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.QueryNextResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterNodesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterNodesResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RepublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RepublishResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceFault;
import org.eclipse.milo.opcua.stack.core.types.structured.SetMonitoringModeRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.SetMonitoringModeResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.SetPublishingModeRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.SetPublishingModeResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.SetTriggeringRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.SetTriggeringResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionAcknowledgement;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferSubscriptionsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferSubscriptionsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.TranslateBrowsePathsToNodeIdsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.TranslateBrowsePathsToNodeIdsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.UnregisterNodesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.UnregisterNodesResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ViewDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.eclipse.milo.opcua.stack.core.util.ExecutionQueue;
import org.eclipse.milo.opcua.stack.core.util.LongSequence;
import org.eclipse.milo.opcua.stack.core.util.ManifestUtil;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.eclipse.milo.opcua.sdk.client.session.SessionFsm.SessionInitializer;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

public class OpcUaClient {

    public static final String SDK_VERSION =
        ManifestUtil.read("X-SDK-Version").orElse("dev");

    static {
        Logger logger = LoggerFactory.getLogger(OpcUaClient.class);
        logger.info("Java version: " + System.getProperty("java.version"));
        logger.info("Eclipse Milo OPC UA Stack version: {}", Stack.VERSION);
        logger.info("Eclipse Milo OPC UA Client SDK version: {}", SDK_VERSION);
    }

    /**
     * Create an {@link OpcUaClient} configured with {@code config}.
     *
     * @param config the {@link OpcUaClientConfig}.
     * @return an {@link OpcUaClient} configured with {@code config}.
     * @throws UaException if the client could not be created (e.g. transport/encoding not supported).
     */
    public static OpcUaClient create(OpcUaClientConfig config) throws UaException {
        OpcTcpClientTransportConfig transportConfig = OpcTcpClientTransportConfig.newBuilder().build();
        var transport = new OpcTcpClientTransport(transportConfig);

        return new OpcUaClient(config, transport);
    }

    /**
     * Select the first endpoint with no security that allows anonymous connections and create an
     * {@link OpcUaClient} with the default configuration.
     * <p>
     * If the server is not configured with an endpoint with no security or authentication you
     * must use {@link #create(String, Function, Consumer, Consumer)} to select an endpoint and configure
     * any certificates or identity provider that the selected endpoint would require.
     *
     * @param endpointUrl the endpoint URL of the server to connect to and get endpoints from.
     * @return an {@link OpcUaClient} configured to connect to the server identified by
     * {@code endpointUrl}.
     * @throws UaException if the endpoints could not be retrieved or the client could not be
     *                     created.
     */
    public static OpcUaClient create(String endpointUrl) throws UaException {
        // select the first EndpointDescription with no security and anonymous authentication
        Predicate<EndpointDescription> predicate = e ->
            SecurityPolicy.None.getUri().equals(e.getSecurityPolicyUri()) &&
                Arrays.stream(e.getUserIdentityTokens())
                    .anyMatch(p -> p.getTokenType() == UserTokenType.Anonymous);

        return create(
            endpointUrl,
            endpoints -> endpoints.stream()
                .filter(predicate)
                .findFirst(),
            b -> {},
            b -> {}
        );
    }

    /**
     * Create and configure an {@link OpcUaClient} by selecting an {@link EndpointDescription} from
     * a list of endpoints retrieved via the GetEndpoints service from the server at {@code endpointUrl}
     * and building an {@link OpcUaClientConfig} using that endpoint.
     *
     * @param endpointUrl        the endpoint URL of the server to connect to and retrieve endpoints from.
     * @param selectEndpoint     a function that selects the {@link EndpointDescription} to connect
     *                           to from the list of endpoints from the server.
     * @param configureTransport a Consumer that receives an {@link OpcTcpClientTransportConfigBuilder}
     *                           that can be used to configure the transport.
     * @param configureClient    a Consumer that receives an {@link OpcUaClientConfigBuilder} that
     *                           can be used  to configure the client.
     * @return a configured {@link OpcUaClient}.
     * @throws UaException if the endpoints could not be retrieved or the client could not be created.
     */
    public static OpcUaClient create(
        String endpointUrl,
        Function<List<EndpointDescription>, Optional<EndpointDescription>> selectEndpoint,
        Consumer<OpcTcpClientTransportConfigBuilder> configureTransport,
        Consumer<OpcUaClientConfigBuilder> configureClient
    ) throws UaException {

        try {
            List<EndpointDescription> endpoints =
                DiscoveryClient.getEndpoints(endpointUrl).get();

            EndpointDescription endpoint = selectEndpoint.apply(endpoints).orElseThrow(() ->
                new UaException(
                    StatusCodes.Bad_ConfigurationError,
                    "no endpoint selected"
                )
            );

            OpcTcpClientTransportConfigBuilder transportConfigBuilder = OpcTcpClientTransportConfig.newBuilder();
            configureTransport.accept(transportConfigBuilder);

            OpcUaClientConfigBuilder clientConfigBuilder = OpcUaClientConfig.builder().setEndpoint(endpoint);
            configureClient.accept(clientConfigBuilder);
            OpcUaClientConfig clientConfig = clientConfigBuilder.build();

            var transport = new OpcTcpClientTransport(transportConfigBuilder.build());

            return new OpcUaClient(clientConfig, transport);
        } catch (InterruptedException | ExecutionException e) {
            if (!endpointUrl.endsWith("/discovery")) {
                StringBuilder discoveryUrl = new StringBuilder(endpointUrl);
                if (!endpointUrl.endsWith("/")) {
                    discoveryUrl.append("/");
                }
                discoveryUrl.append("discovery");

                return create(discoveryUrl.toString(), selectEndpoint, configureTransport, configureClient);
            } else {
                throw UaException.extract(e)
                    .orElseGet(() -> new UaException(e));
            }
        }
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final LongSequence requestHandles = new LongSequence(0, UInteger.MAX_VALUE);

    private final List<ServiceFaultListener> faultListeners = new CopyOnWriteArrayList<>();
    private final ExecutionQueue faultNotificationQueue;

    private final AddressSpace addressSpace;

    private final NamespaceTable namespaceTable = new NamespaceTable();
    private final ServerTable serverTable = new ServerTable();

    private final ObjectTypeManager objectTypeManager = new ObjectTypeManager();

    private final VariableTypeManager variableTypeManager = new VariableTypeManager();

    private final EncodingManager encodingManager =
        DefaultEncodingManager.createAndInitialize();

    private final DataTypeManager staticDataTypeManager =
        DefaultDataTypeManager.createAndInitialize(namespaceTable);

    private final DataTypeManager dynamicDataTypeManager =
        DefaultDataTypeManager.createAndInitialize(namespaceTable);

    private final EncodingContext staticEncodingContext;
    private final EncodingContext dynamicEncodingContext;

    private final OpcUaSubscriptionManager subscriptionManager;

    private final SessionFsm sessionFsm;

    private final ClientApplicationContext applicationContext;

    private final OpcUaClientConfig config;

    private final OpcClientTransport transport;


    public OpcUaClient(OpcUaClientConfig config, OpcClientTransport transport) {
        this.config = config;
        this.transport = transport;

        staticEncodingContext = new EncodingContext() {
            @Override
            public DataTypeManager getDataTypeManager() {
                return staticDataTypeManager;
            }

            @Override
            public EncodingManager getEncodingManager() {
                return encodingManager;
            }

            @Override
            public EncodingLimits getEncodingLimits() {
                return config.getEncodingLimits();
            }

            @Override
            public NamespaceTable getNamespaceTable() {
                return namespaceTable;
            }

            @Override
            public ServerTable getServerTable() {
                return serverTable;
            }
        };

        dynamicEncodingContext = new EncodingContext() {
            @Override
            public DataTypeManager getDataTypeManager() {
                return dynamicDataTypeManager;
            }

            @Override
            public EncodingManager getEncodingManager() {
                return encodingManager;
            }

            @Override
            public EncodingLimits getEncodingLimits() {
                return config.getEncodingLimits();
            }

            @Override
            public NamespaceTable getNamespaceTable() {
                return namespaceTable;
            }

            @Override
            public ServerTable getServerTable() {
                return serverTable;
            }
        };

        applicationContext = new ClientApplicationContext() {
            @Override
            public EndpointDescription getEndpoint() {
                return config.getEndpoint();
            }

            @Override
            public Optional<KeyPair> getKeyPair() {
                return config.getKeyPair();
            }

            @Override
            public Optional<X509Certificate> getCertificate() {
                return config.getCertificate();
            }

            @Override
            public Optional<X509Certificate[]> getCertificateChain() {
                return config.getCertificateChain();
            }

            @Override
            public CertificateValidator getCertificateValidator() {
                return config.getCertificateValidator();
            }

            @Override
            public EncodingContext getEncodingContext() {
                return staticEncodingContext;
            }

            @Override
            public UInteger getRequestTimeout() {
                return config.getRequestTimeout();
            }
        };

        sessionFsm = SessionFsmFactory.newSessionFsm(this);

        sessionFsm.addInitializer((client, session) -> {
            logger.debug("SessionInitializer: NamespaceTable and ServerTable");
            RequestHeader requestHeader = newRequestHeader(session.getAuthenticationToken());

            ReadRequest readRequest = new ReadRequest(
                requestHeader,
                0.0,
                TimestampsToReturn.Neither,
                new ReadValueId[]{
                    new ReadValueId(
                        NodeIds.Server_NamespaceArray,
                        AttributeId.Value.uid(),
                        null,
                        QualifiedName.NULL_VALUE),
                    new ReadValueId(
                        NodeIds.Server_ServerArray,
                        AttributeId.Value.uid(),
                        null,
                        QualifiedName.NULL_VALUE)
                }
            );

            return client.sendRequestAsync(readRequest)
                .thenApply(ReadResponse.class::cast)
                .thenApply(response -> Objects.requireNonNull(response.getResults()))
                .thenApply(results -> {
                    String[] namespaceArray = (String[]) results[0].getValue().getValue();
                    String[] serverArray = (String[]) results[1].getValue().getValue();
                    if (namespaceArray != null) {
                        updateNamespaceTable(namespaceArray);
                    }
                    if (serverArray != null) {
                        updateServerTable(serverArray);
                    }
                    return Unit.VALUE;
                })
                .exceptionally(ex -> {
                    logger.warn("SessionInitializer: NamespaceTable", ex);
                    return Unit.VALUE;
                });
        });


        faultNotificationQueue = new ExecutionQueue(transport.getConfig().getExecutor());

        addressSpace = new AddressSpace(this);
        subscriptionManager = new OpcUaSubscriptionManager(this);

        ObjectTypeInitializer.initialize(namespaceTable, objectTypeManager);
        VariableTypeInitializer.initialize(namespaceTable, variableTypeManager);
    }

    public OpcUaClient connect() throws UaException {
        try {
            return connectAsync().get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<OpcUaClient> connectAsync() {
        return transport.connect(applicationContext)
            .thenCompose(c -> sessionFsm.openSession())
            .thenApply(s -> OpcUaClient.this);
    }

    public OpcUaClient disconnect() throws UaException {
        try {
            return disconnectAsync().get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<OpcUaClient> disconnectAsync() {
        return sessionFsm
            .closeSession()
            .exceptionally(ex -> Unit.VALUE)
            .thenCompose(u ->
                transport
                    .disconnect()
                    .thenApply(c -> OpcUaClient.this))
            .exceptionally(ex -> OpcUaClient.this);
    }

    public OpcUaSession getSession() throws UaException {
        try {
            return getSessionAsync().get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<OpcUaSession> getSessionAsync() {
        return sessionFsm.getSession();
    }

    public OpcUaClientConfig getConfig() {
        return config;
    }

    public OpcClientTransport getTransport() {
        return transport;
    }

    public AddressSpace getAddressSpace() {
        return addressSpace;
    }

    public ObjectTypeManager getObjectTypeManager() {
        return objectTypeManager;
    }

    public VariableTypeManager getVariableTypeManager() {
        return variableTypeManager;
    }

    public OpcUaSubscriptionManager getSubscriptionManager() {
        return subscriptionManager;
    }

    /**
     * Get the client's "static" {@link DataTypeManager}.
     * <p>
     * This {@link DataTypeManager} is for static codecs that serialize classes that exist at
     * compile time, e.g. structures from namespace 0 and or code-generated structures.
     *
     * @return the client's static {@link DataTypeManager}.
     */
    public DataTypeManager getStaticDataTypeManager() {
        return staticDataTypeManager;
    }

    /**
     * Get the client's "dynamic" {@link DataTypeManager}.
     * <p>
     * This {@link DataTypeManager} is for dynamic codecs that were created by reading the server's
     * DataType Dictionary at runtime and serializes generic representations of structures used by
     * instances of a BsdParser implementation.
     *
     * @return the client's dynamic {@link DataTypeManager}.
     */
    public DataTypeManager getDynamicDataTypeManager() {
        return dynamicDataTypeManager;
    }

    /**
     * Get a "static" {@link EncodingContext} instance.
     * <p>
     * This {@link EncodingContext} instance returns the client's static {@link DataTypeManager}.
     *
     * @return a "static" {@link EncodingContext} instance.
     * @see #getStaticDataTypeManager()
     */
    public EncodingContext getStaticEncodingContext() {
        return staticEncodingContext;
    }

    /**
     * Get a "dynamic" {@link EncodingContext}.
     * <p>
     * This {@link EncodingContext} instance returns the client's dynamic {@link DataTypeManager}.
     *
     * @return a "dynamic" {@link EncodingContext}.
     * @see #getDynamicDataTypeManager()
     */
    public EncodingContext getDynamicEncodingContext() {
        return dynamicEncodingContext;
    }

    /**
     * Get the local copy of the server's NamespaceTable (NamespaceArray).
     *
     * @return the local copy of the server's NamespaceTable (NamespaceArray).
     */
    public NamespaceTable getNamespaceTable() {
        return namespaceTable;
    }

    /**
     * Read the server's NamespaceTable (NamespaceArray) and update the local copy.
     *
     * @return the updated {@link NamespaceTable}.
     * @throws UaException if an operation- or service-level error occurs.
     */
    public NamespaceTable readNamespaceTable() throws UaException {
        try {
            return readNamespaceTableAsync().get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    /**
     * Read the server's NamespaceTable (NamespaceArray) and update the local copy.
     * <p>
     * This call completes asynchronously.
     *
     * @return a {@link CompletableFuture} that completes successfully with the updated
     * {@link NamespaceTable} or completes exceptionally if a service- or operation-level error
     * occurs.
     */
    public CompletableFuture<NamespaceTable> readNamespaceTableAsync() {
        return getSessionAsync().thenCompose(session -> {
            RequestHeader requestHeader = newRequestHeader(session.getAuthenticationToken());

            ReadRequest readRequest = new ReadRequest(
                requestHeader,
                0.0,
                TimestampsToReturn.Neither,
                new ReadValueId[]{
                    new ReadValueId(
                        NodeIds.Server_NamespaceArray,
                        AttributeId.Value.uid(),
                        null,
                        QualifiedName.NULL_VALUE)
                }
            );

            CompletableFuture<String[]> namespaceArray = sendRequestAsync(readRequest)
                .thenApply(ReadResponse.class::cast)
                .thenApply(response -> Objects.requireNonNull(response.getResults()))
                .thenApply(results -> (String[]) results[0].getValue().getValue());

            return namespaceArray
                .thenAccept(this::updateNamespaceTable)
                .thenApply(v -> getNamespaceTable());
        });
    }

    /**
     * Get the local copy of the server's ServerTable (ServerArray).
     *
     * @return the local copy of the server's ServerTable (ServerArray).
     */
    public ServerTable getServerTable() {
        return serverTable;
    }

    /**
     * Read the server's ServerTable (ServerArray) and update the local copy.
     *
     * @return the updated {@link ServerTable}.
     * @throws UaException if an operation- or service-level error occurs.
     */
    public ServerTable readServerTable() throws UaException {
        try {
            return readServerTableAsync().get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    /**
     * Read the server's ServerTable (ServerArray) and update the local copy.
     * <p>
     * This call completes asynchronously.
     *
     * @return a {@link CompletableFuture} that completes successfully with the updated
     * {@link ServerTable} or completes exceptionally if a service- or operation-level error
     * occurs.
     */
    public CompletableFuture<ServerTable> readServerTableAsync() {
        return getSessionAsync().thenCompose(session -> {
            RequestHeader requestHeader = newRequestHeader(session.getAuthenticationToken());

            ReadRequest readRequest = new ReadRequest(
                requestHeader,
                0.0,
                TimestampsToReturn.Neither,
                new ReadValueId[]{
                    new ReadValueId(
                        NodeIds.Server_ServerArray,
                        AttributeId.Value.uid(),
                        null,
                        QualifiedName.NULL_VALUE)
                }
            );

            CompletableFuture<String[]> serverArray = sendRequestAsync(readRequest)
                .thenApply(ReadResponse.class::cast)
                .thenApply(response -> Objects.requireNonNull(response.getResults()))
                .thenApply(results -> (String[]) results[0].getValue().getValue());

            return serverArray
                .thenAccept(this::updateServerTable)
                .thenApply(v -> getServerTable());
        });
    }

    private void updateNamespaceTable(String[] namespaceArray) {
        if (namespaceArray.length > UShort.MAX_VALUE) {
            logger.warn("NamespaceTable returned by " +
                "server contains " + namespaceArray.length + " entries");
        }

        NamespaceTable namespaceTable = getNamespaceTable();

        namespaceTable.update(uriTable -> {
            uriTable.clear();

            namespaceTable.add(Namespaces.OPC_UA);
            for (String uri : namespaceArray) {
                namespaceTable.add(uri);
            }
        });
    }

    private void updateServerTable(String[] serverArray) {
        if (serverArray.length > UShort.MAX_VALUE) {
            logger.warn("ServerTable returned by " +
                "server contains " + serverArray.length + " entries");
        }

        ServerTable serverTable = getServerTable();

        serverTable.update(map -> {
            map.clear();

            for (String uri : serverArray) {
                serverTable.add(uri);
            }
        });
    }

    /**
     * Create a new {@link RequestHeader} with a null authentication token.
     * <p>
     * A unique request handle will be automatically assigned to the header.
     *
     * @return a new {@link RequestHeader} with a null authentication token.
     */
    public RequestHeader newRequestHeader() {
        return newRequestHeader(NodeId.NULL_VALUE);
    }

    /**
     * Create a new {@link RequestHeader} with {@code authToken}.
     * <p>
     * A unique request handle will be automatically assigned to the header.
     *
     * @param authToken the authentication token to create the header with.
     * @return a new {@link RequestHeader} created with {@code authToken}.
     */
    public RequestHeader newRequestHeader(NodeId authToken) {
        return newRequestHeader(authToken, config.getRequestTimeout());
    }

    /**
     * Create a new {@link RequestHeader} with {@code authToken} and {@code requestTimeout}.
     * <p>
     * A unique request handle will be automatically assigned to the header.
     *
     * @param authToken      the authentication token to create the header with.
     * @param requestTimeout the timeout hint to create the header with.f
     * @return a new {@link RequestHeader} created with {@code authToken} and {@code requestTimeout}.
     */
    public RequestHeader newRequestHeader(NodeId authToken, UInteger requestTimeout) {
        return new RequestHeader(
            authToken,
            DateTime.now(),
            uint(requestHandles.getAndIncrement()),
            uint(0),
            null,
            requestTimeout,
            null
        );
    }

    //region Attribute Services

    public ReadResponse read(
        double maxAge,
        TimestampsToReturn timestampsToReturn,
        List<ReadValueId> readValueIds
    ) throws UaException {

        try {
            return readAsync(maxAge, timestampsToReturn, readValueIds).get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public List<DataValue> readValues(
        double maxAge,
        TimestampsToReturn timestampsToReturn,
        List<NodeId> nodeIds
    ) throws UaException {

        try {
            return readValuesAsync(maxAge, timestampsToReturn, nodeIds).get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<ReadResponse> readAsync(
        double maxAge,
        TimestampsToReturn timestampsToReturn,
        List<ReadValueId> readValueIds
    ) {

        return getSessionAsync().thenCompose(session -> {
            var request = new ReadRequest(
                newRequestHeader(session.getAuthenticationToken()),
                maxAge,
                timestampsToReturn,
                readValueIds.toArray(new ReadValueId[0])
            );

            return sendRequestAsync(request)
                .thenApply(ReadResponse.class::cast);
        });
    }

    public CompletableFuture<List<DataValue>> readValuesAsync(
        double maxAge,
        TimestampsToReturn timestampsToReturn,
        List<NodeId> nodeIds
    ) {

        List<ReadValueId> readValueIds = nodeIds.stream()
            .map(nodeId -> new ReadValueId(nodeId, AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE))
            .collect(Collectors.toList());

        return readAsync(maxAge, timestampsToReturn, readValueIds).thenApply(r -> List.of(r.getResults()));
    }

    public WriteResponse write(List<WriteValue> writeValues) throws UaException {
        try {
            return writeAsync(writeValues).get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public List<StatusCode> writeValues(List<NodeId> nodeIds, List<DataValue> values) throws UaException {
        try {
            return writeValuesAsync(nodeIds, values).get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<WriteResponse> writeAsync(List<WriteValue> writeValues) {
        return getSessionAsync().thenCompose(session -> {
            WriteRequest request = new WriteRequest(
                newRequestHeader(session.getAuthenticationToken()),
                writeValues.toArray(new WriteValue[0])
            );

            return sendRequestAsync(request)
                .thenApply(WriteResponse.class::cast);
        });
    }

    public CompletableFuture<List<StatusCode>> writeValuesAsync(List<NodeId> nodeIds, List<DataValue> values) {
        if (nodeIds.size() != values.size()) {
            CompletableFuture<List<StatusCode>> failed = new CompletableFuture<>();
            failed.completeExceptionally(new IllegalArgumentException("nodeIds.size() != values.size()"));
            return failed;
        } else {
            var writeValues = new ArrayList<WriteValue>(nodeIds.size());

            for (int i = 0; i < nodeIds.size(); i++) {
                NodeId nodeId = nodeIds.get(i);
                DataValue value = values.get(i);
                writeValues.add(new WriteValue(nodeId, AttributeId.Value.uid(), null, value));
            }

            return writeAsync(writeValues)
                .thenApply(response -> List.of(response.getResults()));
        }
    }

    public HistoryReadResponse historyRead(
        HistoryReadDetails historyReadDetails,
        TimestampsToReturn timestampsToReturn,
        boolean releaseContinuationPoints,
        List<HistoryReadValueId> nodesToRead
    ) throws UaException {

        try {
            var future = historyReadAsync(
                historyReadDetails,
                timestampsToReturn,
                releaseContinuationPoints,
                nodesToRead
            );

            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<HistoryReadResponse> historyReadAsync(
        HistoryReadDetails historyReadDetails,
        TimestampsToReturn timestampsToReturn,
        boolean releaseContinuationPoints,
        List<HistoryReadValueId> nodesToRead
    ) {

        return getSessionAsync().thenCompose(session -> {
            HistoryReadRequest request = new HistoryReadRequest(
                newRequestHeader(session.getAuthenticationToken()),
                ExtensionObject.encode(getStaticEncodingContext(), historyReadDetails),
                timestampsToReturn,
                releaseContinuationPoints,
                nodesToRead.toArray(new HistoryReadValueId[0])
            );

            return sendRequestAsync(request)
                .thenApply(HistoryReadResponse.class::cast);
        });
    }

    public HistoryUpdateResponse historyUpdate(List<HistoryUpdateDetails> historyUpdateDetails) throws UaException {
        try {
            return historyUpdateAsync(historyUpdateDetails).get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<HistoryUpdateResponse> historyUpdateAsync(
        List<HistoryUpdateDetails> historyUpdateDetails
    ) {

        return getSessionAsync().thenCompose(session -> {
            ExtensionObject[] details = historyUpdateDetails.stream()
                .map(hud -> ExtensionObject.encode(getStaticEncodingContext(), hud))
                .toArray(ExtensionObject[]::new);

            HistoryUpdateRequest request = new HistoryUpdateRequest(
                newRequestHeader(session.getAuthenticationToken()),
                details
            );

            return sendRequestAsync(request)
                .thenApply(HistoryUpdateResponse.class::cast);
        });
    }

    //endregion

    //region Method Services

    public CallResponse call(List<CallMethodRequest> requests) throws UaException {
        try {
            return callAsync(requests).get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<CallResponse> callAsync(List<CallMethodRequest> methodsToCall) {
        return getSessionAsync().thenCompose(session -> {
            CallRequest request = new CallRequest(
                newRequestHeader(session.getAuthenticationToken()),
                methodsToCall.toArray(new CallMethodRequest[0])
            );

            return sendRequestAsync(request)
                .thenApply(CallResponse.class::cast);
        });
    }

    //endregion

    //region MonitoredItem Services

    public CreateMonitoredItemsResponse createMonitoredItems(
        UInteger subscriptionId,
        TimestampsToReturn timestampsToReturn,
        List<MonitoredItemCreateRequest> itemsToCreate
    ) throws UaException {

        try {
            return createMonitoredItemsAsync(subscriptionId, timestampsToReturn, itemsToCreate).get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<CreateMonitoredItemsResponse> createMonitoredItemsAsync(
        UInteger subscriptionId,
        TimestampsToReturn timestampsToReturn,
        List<MonitoredItemCreateRequest> itemsToCreate
    ) {

        return getSessionAsync().thenCompose(session -> {
            CreateMonitoredItemsRequest request = new CreateMonitoredItemsRequest(
                newRequestHeader(session.getAuthenticationToken()),
                subscriptionId,
                timestampsToReturn,
                itemsToCreate.toArray(new MonitoredItemCreateRequest[0])
            );

            return sendRequestAsync(request)
                .thenApply(CreateMonitoredItemsResponse.class::cast);
        });
    }

    public ModifyMonitoredItemsResponse modifyMonitoredItems(
        UInteger subscriptionId,
        TimestampsToReturn timestampsToReturn,
        List<MonitoredItemModifyRequest> itemsToModify
    ) throws UaException {

        try {
            return modifyMonitoredItemsAsync(subscriptionId, timestampsToReturn, itemsToModify).get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<ModifyMonitoredItemsResponse> modifyMonitoredItemsAsync(
        UInteger subscriptionId,
        TimestampsToReturn timestampsToReturn,
        List<MonitoredItemModifyRequest> itemsToModify
    ) {

        return getSessionAsync().thenCompose(session -> {
            ModifyMonitoredItemsRequest request = new ModifyMonitoredItemsRequest(
                newRequestHeader(session.getAuthenticationToken()),
                subscriptionId,
                timestampsToReturn,
                itemsToModify.toArray(new MonitoredItemModifyRequest[0])
            );

            return sendRequestAsync(request)
                .thenApply(ModifyMonitoredItemsResponse.class::cast);
        });
    }

    public DeleteMonitoredItemsResponse deleteMonitoredItems(
        UInteger subscriptionId,
        List<UInteger> monitoredItemIds
    ) throws UaException {

        try {
            return deleteMonitoredItemsAsync(subscriptionId, monitoredItemIds).get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<DeleteMonitoredItemsResponse> deleteMonitoredItemsAsync(
        UInteger subscriptionId,
        List<UInteger> monitoredItemIds
    ) {

        return getSessionAsync().thenCompose(session -> {
            DeleteMonitoredItemsRequest request = new DeleteMonitoredItemsRequest(
                newRequestHeader(session.getAuthenticationToken()),
                subscriptionId,
                monitoredItemIds.toArray(new UInteger[0])
            );

            return sendRequestAsync(request)
                .thenApply(DeleteMonitoredItemsResponse.class::cast);
        });
    }

    public SetMonitoringModeResponse setMonitoringMode(
        UInteger subscriptionId,
        MonitoringMode monitoringMode,
        List<UInteger> monitoredItemIds
    ) throws UaException {

        try {
            return setMonitoringModeAsync(subscriptionId, monitoringMode, monitoredItemIds).get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<SetMonitoringModeResponse> setMonitoringModeAsync(
        UInteger subscriptionId,
        MonitoringMode monitoringMode,
        List<UInteger> monitoredItemIds
    ) {

        return getSessionAsync().thenCompose(session -> {
            SetMonitoringModeRequest request = new SetMonitoringModeRequest(
                newRequestHeader(session.getAuthenticationToken()),
                subscriptionId,
                monitoringMode,
                monitoredItemIds.toArray(new UInteger[0])
            );

            return sendRequestAsync(request)
                .thenApply(SetMonitoringModeResponse.class::cast);
        });
    }

    public SetTriggeringResponse setTriggering(
        UInteger subscriptionId,
        UInteger triggeringItemId,
        List<UInteger> linksToAdd,
        List<UInteger> linksToRemove
    ) throws UaException {

        try {
            return setTriggeringAsync(subscriptionId, triggeringItemId, linksToAdd, linksToRemove).get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<SetTriggeringResponse> setTriggeringAsync(
        UInteger subscriptionId,
        UInteger triggeringItemId,
        List<UInteger> linksToAdd,
        List<UInteger> linksToRemove
    ) {

        return getSessionAsync().thenCompose(session -> {
            SetTriggeringRequest request = new SetTriggeringRequest(
                newRequestHeader(session.getAuthenticationToken()),
                subscriptionId,
                triggeringItemId,
                linksToAdd.toArray(new UInteger[0]),
                linksToRemove.toArray(new UInteger[0])
            );

            return sendRequestAsync(request)
                .thenApply(SetTriggeringResponse.class::cast);
        });
    }

    //endregion

    //region NodeManagement Services

    public AddNodesResponse addNodes(List<AddNodesItem> nodesToAdd) throws UaException {
        try {
            return addNodesAsync(nodesToAdd).get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<AddNodesResponse> addNodesAsync(List<AddNodesItem> nodesToAdd) {
        return getSessionAsync().thenCompose(session -> {
            AddNodesRequest request = new AddNodesRequest(
                newRequestHeader(session.getAuthenticationToken()),
                nodesToAdd.toArray(new AddNodesItem[0])
            );

            return sendRequestAsync(request)
                .thenApply(AddNodesResponse.class::cast);
        });
    }

    public AddReferencesResponse addReferences(List<AddReferencesItem> referencesToAdd) throws UaException {
        try {
            return addReferencesAsync(referencesToAdd).get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<AddReferencesResponse> addReferencesAsync(List<AddReferencesItem> referencesToAdd) {
        return getSessionAsync().thenCompose(session -> {
            AddReferencesRequest request = new AddReferencesRequest(
                newRequestHeader(session.getAuthenticationToken()),
                referencesToAdd.toArray(new AddReferencesItem[0])
            );

            return sendRequestAsync(request)
                .thenApply(AddReferencesResponse.class::cast);
        });
    }

    public DeleteNodesResponse deleteNodes(List<DeleteNodesItem> nodesToDelete) throws UaException {
        try {
            return deleteNodesAsync(nodesToDelete).get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<DeleteNodesResponse> deleteNodesAsync(List<DeleteNodesItem> nodesToDelete) {
        return getSessionAsync().thenCompose(session -> {
            DeleteNodesRequest request = new DeleteNodesRequest(
                newRequestHeader(session.getAuthenticationToken()),
                nodesToDelete.toArray(new DeleteNodesItem[0])
            );

            return sendRequestAsync(request)
                .thenApply(DeleteNodesResponse.class::cast);
        });
    }

    public DeleteReferencesResponse deleteReferences(
        List<DeleteReferencesItem> referencesToDelete
    ) throws UaException {

        try {
            return deleteReferencesAsync(referencesToDelete).get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<DeleteReferencesResponse> deleteReferencesAsync(
        List<DeleteReferencesItem> referencesToDelete
    ) {

        return getSessionAsync().thenCompose(session -> {
            DeleteReferencesRequest request = new DeleteReferencesRequest(
                newRequestHeader(session.getAuthenticationToken()),
                referencesToDelete.toArray(new DeleteReferencesItem[0])
            );

            return sendRequestAsync(request)
                .thenApply(DeleteReferencesResponse.class::cast);
        });
    }

    //endregion

    //region Query Services

    public QueryFirstResponse queryFirst(
        ViewDescription view,
        List<NodeTypeDescription> nodeTypes,
        ContentFilter filter,
        UInteger maxDataSetsToReturn,
        UInteger maxReferencesToReturn
    ) throws UaException {

        try {
            return queryFirstAsync(view, nodeTypes, filter, maxDataSetsToReturn, maxReferencesToReturn).get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    /**
     * This service is used to issue a query to the server.
     *
     * @param view                  specifies a View and temporal context to a server.
     * @param nodeTypes             the {@link NodeTypeDescription}.
     * @param filter                the {@link ContentFilter}. Resulting Nodes shall be limited to
     *                              the Nodes matching the criteria defined by the filter.
     * @param maxDataSetsToReturn   the number of {@link QueryDataSet}s that the client wants the
     *                              server to return in the response and on each subsequent
     *                              continuation call response. The server is allowed to further
     *                              limit the response, but shall not exceed this limit. A value
     *                              of 0 indicates that the client is imposing no limitation.
     * @param maxReferencesToReturn the number of References that the client wants the server to
     *                              return in the response for each {@link QueryDataSet} and on
     *                              each subsequent continuation call response. The server is
     *                              allowed to further limit the response, but shall not exceed
     *                              this limit. A value of 0 indicates that the client is imposing
     *                              no limitation.
     * @return a {@link CompletableFuture} containing the {@link QueryFirstResponse}.
     */
    public CompletableFuture<QueryFirstResponse> queryFirstAsync(
        ViewDescription view,
        List<NodeTypeDescription> nodeTypes,
        ContentFilter filter,
        UInteger maxDataSetsToReturn,
        UInteger maxReferencesToReturn
    ) {

        return getSessionAsync().thenCompose(session -> {
            QueryFirstRequest request = new QueryFirstRequest(
                newRequestHeader(session.getAuthenticationToken()),
                view,
                nodeTypes.toArray(new NodeTypeDescription[0]),
                filter,
                maxDataSetsToReturn,
                maxReferencesToReturn
            );

            return sendRequestAsync(request)
                .thenApply(QueryFirstResponse.class::cast);
        });
    }

    public QueryNextResponse queryNext(
        boolean releaseContinuationPoint,
        ByteString continuationPoint
    ) throws UaException {

        try {
            return queryNextAsync(releaseContinuationPoint, continuationPoint).get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    /**
     * This Service is used to request the next set of QueryFirst or QueryNext response
     * information that is too large to be sent in a single response.
     *
     * @param releaseContinuationPoint if {@code true}, passed continuationPoints shall be reset to
     *                                 free resources in the server. If {@code false}, passed
     *                                 continuationPoints shall be used to get the next set of browse
     *                                 information.
     * @param continuationPoint        a server-defined opaque value that represents the continuation
     *                                 point.
     * @return a {@link CompletableFuture} containing the {@link QueryNextResponse}.
     */
    public CompletableFuture<QueryNextResponse> queryNextAsync(
        boolean releaseContinuationPoint,
        ByteString continuationPoint
    ) {

        return getSessionAsync().thenCompose(session -> {
            QueryNextRequest request = new QueryNextRequest(
                newRequestHeader(session.getAuthenticationToken()),
                releaseContinuationPoint,
                continuationPoint
            );

            return sendRequestAsync(request)
                .thenApply(QueryNextResponse.class::cast);
        });
    }


    //endregion

    //region Subscription Services

    /**
     * Create a new Subscription.
     * <p>
     * This method is a convenience method for {@link #createSubscriptionAsync(double, UInteger, UInteger,
     * UInteger, boolean, UByte)} that blocks until the response is received.
     *
     * @param requestedPublishingInterval the requested publishing interval.
     * @param requestedLifetimeCount      the requested lifetime count.
     * @param requestedMaxKeepAliveCount  the requested max keep alive count.
     * @param maxNotificationsPerPublish  the max notifications per publish.
     * @param publishingEnabled           {@code true} if publishing is enabled.
     * @param priority                    the priority.
     * @return the {@link CreateSubscriptionResponse}.
     * @throws UaException if a service- or operation-level error occurs.
     */
    public CreateSubscriptionResponse createSubscription(
        double requestedPublishingInterval,
        UInteger requestedLifetimeCount,
        UInteger requestedMaxKeepAliveCount,
        UInteger maxNotificationsPerPublish,
        boolean publishingEnabled,
        UByte priority
    ) throws UaException {

        try {
            CompletableFuture<CreateSubscriptionResponse> future = createSubscriptionAsync(
                requestedPublishingInterval,
                requestedLifetimeCount,
                requestedMaxKeepAliveCount,
                maxNotificationsPerPublish,
                publishingEnabled,
                priority
            );

            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<CreateSubscriptionResponse> createSubscriptionAsync(
        double requestedPublishingInterval,
        UInteger requestedLifetimeCount,
        UInteger requestedMaxKeepAliveCount,
        UInteger maxNotificationsPerPublish,
        boolean publishingEnabled,
        UByte priority
    ) {

        return getSessionAsync().thenCompose(session -> {
            CreateSubscriptionRequest request = new CreateSubscriptionRequest(
                newRequestHeader(session.getAuthenticationToken()),
                requestedPublishingInterval,
                requestedLifetimeCount,
                requestedMaxKeepAliveCount,
                maxNotificationsPerPublish,
                publishingEnabled,
                priority
            );

            return sendRequestAsync(request)
                .thenApply(CreateSubscriptionResponse.class::cast);
        });
    }

    public ModifySubscriptionResponse modifySubscription(
        UInteger subscriptionId,
        double requestedPublishingInterval,
        UInteger requestedLifetimeCount,
        UInteger requestedMaxKeepAliveCount,
        UInteger maxNotificationsPerPublish,
        UByte priority
    ) throws UaException {

        try {
            CompletableFuture<ModifySubscriptionResponse> future = modifySubscriptionAsync(
                subscriptionId,
                requestedPublishingInterval,
                requestedLifetimeCount,
                requestedMaxKeepAliveCount,
                maxNotificationsPerPublish,
                priority
            );

            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<ModifySubscriptionResponse> modifySubscriptionAsync(
        UInteger subscriptionId,
        double requestedPublishingInterval,
        UInteger requestedLifetimeCount,
        UInteger requestedMaxKeepAliveCount,
        UInteger maxNotificationsPerPublish,
        UByte priority
    ) {

        return getSessionAsync().thenCompose(session -> {
            ModifySubscriptionRequest request = new ModifySubscriptionRequest(
                newRequestHeader(session.getAuthenticationToken()),
                subscriptionId,
                requestedPublishingInterval,
                requestedLifetimeCount,
                requestedMaxKeepAliveCount,
                maxNotificationsPerPublish,
                priority
            );

            return sendRequestAsync(request)
                .thenApply(ModifySubscriptionResponse.class::cast);
        });
    }

    public DeleteSubscriptionsResponse deleteSubscriptions(List<UInteger> subscriptionIds) throws UaException {
        try {
            CompletableFuture<DeleteSubscriptionsResponse> future = deleteSubscriptionsAsync(subscriptionIds);

            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<DeleteSubscriptionsResponse> deleteSubscriptionsAsync(List<UInteger> subscriptionIds) {
        return getSessionAsync().thenCompose(session -> {
            DeleteSubscriptionsRequest request = new DeleteSubscriptionsRequest(
                newRequestHeader(session.getAuthenticationToken()),
                subscriptionIds.toArray(new UInteger[0])
            );

            return sendRequestAsync(request)
                .thenApply(DeleteSubscriptionsResponse.class::cast);
        });
    }

    public TransferSubscriptionsResponse transferSubscriptions(
        List<UInteger> subscriptionIds,
        boolean sendInitialValues
    ) throws UaException {

        try {
            CompletableFuture<TransferSubscriptionsResponse> future = transferSubscriptionsAsync(
                subscriptionIds,
                sendInitialValues
            );

            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<TransferSubscriptionsResponse> transferSubscriptionsAsync(
        List<UInteger> subscriptionIds,
        boolean sendInitialValues
    ) {

        return getSessionAsync().thenCompose(session -> {
            TransferSubscriptionsRequest request = new TransferSubscriptionsRequest(
                newRequestHeader(session.getAuthenticationToken()),
                subscriptionIds.toArray(new UInteger[0]),
                sendInitialValues
            );

            return sendRequestAsync(request)
                .thenApply(TransferSubscriptionsResponse.class::cast);
        });
    }

    public SetPublishingModeResponse setPublishingMode(
        boolean publishingEnabled,
        List<UInteger> subscriptionIds
    ) throws UaException {

        try {
            CompletableFuture<SetPublishingModeResponse> future = setPublishingModeAsync(
                publishingEnabled,
                subscriptionIds
            );

            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<SetPublishingModeResponse> setPublishingModeAsync(
        boolean publishingEnabled,
        List<UInteger> subscriptionIds
    ) {

        return getSessionAsync().thenCompose(session -> {
            SetPublishingModeRequest request = new SetPublishingModeRequest(
                newRequestHeader(session.getAuthenticationToken()),
                publishingEnabled,
                subscriptionIds.toArray(new UInteger[0])
            );

            return sendRequestAsync(request)
                .thenApply(SetPublishingModeResponse.class::cast);
        });
    }

    public PublishResponse publish(
        List<SubscriptionAcknowledgement> subscriptionAcknowledgements
    ) throws UaException {

        try {
            CompletableFuture<PublishResponse> future = publishAsync(subscriptionAcknowledgements);

            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<PublishResponse> publishAsync(
        List<SubscriptionAcknowledgement> subscriptionAcknowledgements
    ) {

        return getSessionAsync().thenCompose(session -> {
            PublishRequest request = new PublishRequest(
                newRequestHeader(session.getAuthenticationToken()),
                subscriptionAcknowledgements.toArray(new SubscriptionAcknowledgement[0])
            );

            return sendRequestAsync(request)
                .thenApply(PublishResponse.class::cast);
        });
    }

    public RepublishResponse republish(
        UInteger subscriptionId,
        UInteger retransmitSequenceNumber
    ) throws UaException {

        try {
            CompletableFuture<RepublishResponse> future = republishAsync(
                subscriptionId,
                retransmitSequenceNumber
            );

            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<RepublishResponse> republishAsync(
        UInteger subscriptionId,
        UInteger retransmitSequenceNumber
    ) {

        return getSessionAsync().thenCompose(session -> {
            RepublishRequest request = new RepublishRequest(
                newRequestHeader(session.getAuthenticationToken()),
                subscriptionId,
                retransmitSequenceNumber
            );

            return sendRequestAsync(request)
                .thenApply(RepublishResponse.class::cast);
        });
    }

    //endregion

    //region View Services

    public BrowseResponse browse(
        ViewDescription viewDescription,
        UInteger maxReferencesPerNode,
        List<BrowseDescription> nodesToBrowse
    ) throws UaException {

        try {
            return browseAsync(viewDescription, maxReferencesPerNode, nodesToBrowse).get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<BrowseResponse> browseAsync(
        ViewDescription viewDescription,
        UInteger maxReferencesPerNode,
        List<BrowseDescription> nodesToBrowse
    ) {

        return getSessionAsync().thenCompose(session -> {
            BrowseRequest request = new BrowseRequest(
                newRequestHeader(session.getAuthenticationToken()),
                viewDescription,
                maxReferencesPerNode,
                nodesToBrowse.toArray(new BrowseDescription[0])
            );

            return sendRequestAsync(request)
                .thenApply(BrowseResponse.class::cast);
        });
    }

    public BrowseResult browse(BrowseDescription nodeToBrowse) throws UaException {
        try {
            return browseAsync(nodeToBrowse).get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    /**
     * Browse a single node, with no view and no max references specified.
     *
     * @param nodeToBrowse the node to browse.
     * @return a {@link CompletableFuture} containing the {@link BrowseResult}.
     */
    public CompletableFuture<BrowseResult> browseAsync(BrowseDescription nodeToBrowse) {
        return browseAsync(List.of(nodeToBrowse)).thenApply(rs -> rs.get(0));
    }

    public List<BrowseResult> browse(List<BrowseDescription> nodesToBrowse) throws UaException {
        try {
            return browseAsync(nodesToBrowse).get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    /**
     * Browse a list of nodes, with no view and no max references specified.
     *
     * @param nodesToBrowse the nodes to browse.
     * @return a {@link CompletableFuture} containing the {@link BrowseResult}s.
     */
    public CompletableFuture<List<BrowseResult>> browseAsync(List<BrowseDescription> nodesToBrowse) {
        var viewDescription = new ViewDescription(NodeId.NULL_VALUE, DateTime.MIN_VALUE, uint(0));

        return browseAsync(viewDescription, uint(0), nodesToBrowse)
            .thenApply(r -> List.of(r.getResults()));
    }

    public BrowseNextResponse browseNext(
        boolean releaseContinuationPoints,
        List<ByteString> continuationPoints
    ) throws UaException {

        try {
            return browseNextAsync(releaseContinuationPoints, continuationPoints).get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<BrowseNextResponse> browseNextAsync(
        boolean releaseContinuationPoints,
        List<ByteString> continuationPoints
    ) {

        return getSessionAsync().thenCompose(session -> {
            BrowseNextRequest request = new BrowseNextRequest(
                newRequestHeader(session.getAuthenticationToken()),
                releaseContinuationPoints,
                continuationPoints.toArray(new ByteString[0])
            );

            return sendRequestAsync(request)
                .thenApply(BrowseNextResponse.class::cast);
        });
    }

    public TranslateBrowsePathsToNodeIdsResponse translateBrowsePaths(List<BrowsePath> browsePaths) throws UaException {
        try {
            return translateBrowsePathsAsync(browsePaths).get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<TranslateBrowsePathsToNodeIdsResponse> translateBrowsePathsAsync(
        List<BrowsePath> browsePaths
    ) {

        return getSessionAsync().thenCompose(session -> {
            TranslateBrowsePathsToNodeIdsRequest request = new TranslateBrowsePathsToNodeIdsRequest(
                newRequestHeader(session.getAuthenticationToken()),
                browsePaths.toArray(new BrowsePath[0])
            );

            return sendRequestAsync(request)
                .thenApply(TranslateBrowsePathsToNodeIdsResponse.class::cast);
        });
    }

    public RegisterNodesResponse registerNodes(List<NodeId> nodesToRegister) throws UaException {
        try {
            return registerNodesAsync(nodesToRegister).get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<RegisterNodesResponse> registerNodesAsync(List<NodeId> nodesToRegister) {
        return getSessionAsync().thenCompose(session -> {
            RegisterNodesRequest request = new RegisterNodesRequest(
                newRequestHeader(session.getAuthenticationToken()),
                nodesToRegister.toArray(new NodeId[0])
            );

            return sendRequestAsync(request)
                .thenApply(RegisterNodesResponse.class::cast);
        });
    }

    public UnregisterNodesResponse unregisterNodes(List<NodeId> nodesToUnregister) throws UaException {
        try {
            return unregisterNodesAsync(nodesToUnregister).get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<UnregisterNodesResponse> unregisterNodesAsync(List<NodeId> nodesToUnregister) {
        return getSessionAsync().thenCompose(session -> {
            UnregisterNodesRequest request = new UnregisterNodesRequest(
                newRequestHeader(session.getAuthenticationToken()),
                nodesToUnregister.toArray(new NodeId[0])
            );

            return sendRequestAsync(request)
                .thenApply(UnregisterNodesResponse.class::cast);
        });
    }

    //endregion

    public UaResponseMessageType sendRequest(UaRequestMessageType request) throws UaException {
        try {
            return sendRequestAsync(request).get();
        } catch (InterruptedException | ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    public CompletableFuture<UaResponseMessageType> sendRequestAsync(UaRequestMessageType request) {
        return transport.sendRequestMessage(request)
            .whenComplete(this::notifyFaultListeners);
    }

    private void notifyFaultListeners(UaResponseMessageType response, Throwable ex) {
        if (ex == null || faultListeners.isEmpty()) {
            return;
        }

        UaServiceFaultException faultException = null;

        if (ex instanceof UaServiceFaultException) {
            faultException = (UaServiceFaultException) ex;
        } else if (ex.getCause() instanceof UaServiceFaultException) {
            faultException = (UaServiceFaultException) ex.getCause();
        }

        if (faultException != null) {
            ServiceFault serviceFault = faultException.getServiceFault();

            logger.debug("Notifying {} ServiceFaultListeners", faultListeners.size());

            faultNotificationQueue.submit(
                () ->
                    faultListeners.forEach(h -> h.onServiceFault(serviceFault))
            );
        }
    }

    public void addFaultListener(ServiceFaultListener faultListener) {
        faultListeners.add(faultListener);
        logger.debug("Added ServiceFaultListener: {}", faultListener);
    }

    public void removeFaultListener(ServiceFaultListener faultListener) {
        faultListeners.remove(faultListener);
        logger.debug("Removed ServiceFaultListener: {}", faultListener);
    }

    public void addSessionActivityListener(SessionActivityListener listener) {
        sessionFsm.addActivityListener(listener);
        logger.debug("Added SessionActivityListener: {}", listener);
    }

    public void removeSessionActivityListener(SessionActivityListener listener) {
        sessionFsm.removeActivityListener(listener);
        logger.debug("Removed SessionActivityListener: {}", listener);
    }

    public void addSessionInitializer(SessionInitializer initializer) {
        sessionFsm.addInitializer(initializer);
        logger.debug("Added SessionInitializer: {}", initializer);
    }

    public void removeSessionInitializer(SessionInitializer initializer) {
        sessionFsm.removeInitializer(initializer);
        logger.debug("Removed SessionInitializer: {}", initializer);
    }

}
