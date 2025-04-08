/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static java.util.Objects.requireNonNullElse;
import static org.eclipse.milo.opcua.sdk.client.session.SessionFsm.SessionInitializer;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.client.model.ObjectTypeInitializer;
import org.eclipse.milo.opcua.sdk.client.model.VariableTypeInitializer;
import org.eclipse.milo.opcua.sdk.client.session.SessionFsm;
import org.eclipse.milo.opcua.sdk.client.session.SessionFsmFactory;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.client.subscriptions.PublishingManager;
import org.eclipse.milo.opcua.sdk.client.typetree.DataTypeTreeBuilder;
import org.eclipse.milo.opcua.sdk.core.types.codec.DynamicCodecFactory;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.*;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
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
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.types.structured.ViewDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.eclipse.milo.opcua.stack.core.util.ExecutionQueue;
import org.eclipse.milo.opcua.stack.core.util.Lazy;
import org.eclipse.milo.opcua.stack.core.util.Lists;
import org.eclipse.milo.opcua.stack.core.util.LongSequence;
import org.eclipse.milo.opcua.stack.core.util.ManifestUtil;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.eclipse.milo.opcua.stack.core.util.Tree;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpcUaClient {

  public static final String SDK_VERSION = ManifestUtil.read("X-SDK-Version").orElse("dev");

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
   *
   * <p>If the server is not configured with an endpoint with no security or authentication you must
   * use {@link #create(String, Function, Consumer, Consumer)} to select an endpoint and configure
   * any certificates or identity provider that the selected endpoint would require.
   *
   * @param endpointUrl the endpoint URL of the server to connect to and get endpoints from.
   * @return an {@link OpcUaClient} configured to connect to the server identified by {@code
   *     endpointUrl}.
   * @throws UaException if the endpoints could not be retrieved or the client could not be created.
   */
  public static OpcUaClient create(String endpointUrl) throws UaException {
    // select the first EndpointDescription with no security and anonymous authentication
    Predicate<EndpointDescription> predicate =
        e ->
            SecurityPolicy.None.getUri().equals(e.getSecurityPolicyUri())
                && Stream.of(requireNonNullElse(e.getUserIdentityTokens(), new UserTokenPolicy[0]))
                    .anyMatch(p -> p.getTokenType() == UserTokenType.Anonymous);

    return create(
        endpointUrl,
        endpoints -> endpoints.stream().filter(predicate).findFirst(),
        b -> {},
        b -> {});
  }

  /**
   * Create and configure an {@link OpcUaClient} by selecting an {@link EndpointDescription} from a
   * list of endpoints retrieved via the GetEndpoints service from the server at {@code endpointUrl}
   * and building an {@link OpcUaClientConfig} using that endpoint.
   *
   * @param endpointUrl the endpoint URL of the server to connect to and retrieve endpoints from.
   * @param selectEndpoint a function that selects the {@link EndpointDescription} to connect to
   *     from the list of endpoints from the server.
   * @param configureTransport a Consumer that receives an {@link
   *     OpcTcpClientTransportConfigBuilder} that can be used to configure the transport.
   * @param configureClient a Consumer that receives an {@link OpcUaClientConfigBuilder} that can be
   *     used to configure the client.
   * @return a configured {@link OpcUaClient}.
   * @throws UaException if the endpoints could not be retrieved or the client could not be created.
   */
  public static OpcUaClient create(
      String endpointUrl,
      Function<List<EndpointDescription>, Optional<EndpointDescription>> selectEndpoint,
      Consumer<OpcTcpClientTransportConfigBuilder> configureTransport,
      Consumer<OpcUaClientConfigBuilder> configureClient)
      throws UaException {

    try {
      List<EndpointDescription> endpoints =
          DiscoveryClient.getEndpoints(endpointUrl, configureTransport).get();

      EndpointDescription endpoint =
          selectEndpoint
              .apply(endpoints)
              .orElseThrow(
                  () ->
                      new UaException(StatusCodes.Bad_ConfigurationError, "no endpoint selected"));

      OpcTcpClientTransportConfigBuilder transportConfigBuilder =
          OpcTcpClientTransportConfig.newBuilder();
      configureTransport.accept(transportConfigBuilder);

      OpcUaClientConfigBuilder clientConfigBuilder =
          OpcUaClientConfig.builder().setEndpoint(endpoint);
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
        throw UaException.extract(e).orElseGet(() -> new UaException(e));
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

  private final Lazy<OperationLimits> operationLimits = new Lazy<>();

  private final ObjectTypeManager objectTypeManager = new ObjectTypeManager();

  private final VariableTypeManager variableTypeManager = new VariableTypeManager();

  private DataTypeManagerInitializer dataTypeManagerInitializer =
      new DefaultDataTypeManagerInitializer();

  private final EncodingManager encodingManager = DefaultEncodingManager.createAndInitialize();

  private final DataTypeManager staticDataTypeManager =
      DefaultDataTypeManager.createAndInitialize(namespaceTable);
  private final EncodingContext staticEncodingContext;

  private final Lazy<DataTypeManager> dynamicDataTypeManager = new Lazy<>();
  private final Lazy<EncodingContext> dynamicEncodingContext = new Lazy<>();

  private final Lazy<DataTypeTree> dataTypeTree = new Lazy<>();

  private final PublishingManager publishingManager;
  private final Map<UInteger, OpcUaSubscription> subscriptions = new ConcurrentHashMap<>();

  private final SessionFsm sessionFsm;

  private final ClientApplicationContext applicationContext;

  private final OpcUaClientConfig config;

  private final OpcClientTransport transport;

  public OpcUaClient(OpcUaClientConfig config, OpcClientTransport transport) {
    this.config = config;
    this.transport = transport;

    staticEncodingContext =
        new EncodingContext() {
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

    applicationContext =
        new ClientApplicationContext() {
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

    sessionFsm.addInitializer(
        (client, session) -> {
          logger.debug("SessionInitializer: NamespaceTable and ServerTable");
          RequestHeader requestHeader = newRequestHeader(session.getAuthenticationToken());

          ReadRequest readRequest =
              new ReadRequest(
                  requestHeader,
                  0.0,
                  TimestampsToReturn.Neither,
                  new ReadValueId[] {
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
                  });

          return client
              .sendRequestAsync(readRequest)
              .thenApply(ReadResponse.class::cast)
              .thenApply(response -> Objects.requireNonNull(response.getResults()))
              .thenApply(
                  results -> {
                    String[] namespaceArray = (String[]) results[0].value().value();
                    String[] serverArray = (String[]) results[1].value().value();
                    if (namespaceArray != null) {
                      updateNamespaceTable(namespaceArray);
                    }
                    if (serverArray != null) {
                      updateServerTable(serverArray);
                    }
                    return Unit.VALUE;
                  })
              .exceptionally(
                  ex -> {
                    logger.warn("SessionInitializer: NamespaceTable", ex);
                    return Unit.VALUE;
                  });
        });

    faultNotificationQueue = new ExecutionQueue(transport.getConfig().getExecutor());

    addressSpace = new AddressSpace(this);
    publishingManager = new PublishingManager(this);

    ObjectTypeInitializer.initialize(namespaceTable, objectTypeManager);
    VariableTypeInitializer.initialize(namespaceTable, variableTypeManager);
  }

  /**
   * Connect the underlying transport and open a session.
   *
   * <p>Regardless of the success or failure of the initial connect operation, the client is now in
   * a state where it will strive to remain connected and keep a session open, automatically
   * reconnecting as necessary, and creating or transferring sessions as necessary.
   *
   * @return this {@link OpcUaClient}
   * @throws UaException if an error occurs.
   */
  public OpcUaClient connect() throws UaException {
    try {
      return connectAsync().get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Connect the underlying transport and open a session.
   *
   * <p>Regardless of the success or failure of the initial connect operation, the client is now in
   * a state where it will strive to remain connected and keep a session open, automatically
   * reconnecting as necessary, and creating or transferring sessions as necessary.
   *
   * @return a {@link CompletableFuture} that completes successfully with this {@link OpcUaClient},
   *     or completes exceptionally if an error occurs.
   */
  public CompletableFuture<OpcUaClient> connectAsync() {
    return transport
        .connect(applicationContext)
        .handle((u, ex) -> sessionFsm.openSession())
        .thenCompose(Function.identity())
        .thenApply(s -> OpcUaClient.this);
  }

  /**
   * Close the session, if it's open, and disconnect the underlying transport.
   *
   * @return this {@link OpcUaClient}.
   * @throws UaException if an unexpected error occurs. Errors closing the session or the
   *     disconnecting the transport are swallowed.
   */
  public OpcUaClient disconnect() throws UaException {
    try {
      return disconnectAsync().get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Close the session, if it's open, and disconnect the underlying transport.
   *
   * @return a {@link CompletableFuture} that completes successfully with this {@link OpcUaClient},
   *     or completes exceptionally if an unexpected error occurs. Errors closing the session or the
   *     disconnecting the transport are swallowed.
   */
  public CompletableFuture<OpcUaClient> disconnectAsync() {
    return sessionFsm
        .closeSession()
        .exceptionally(ex -> Unit.VALUE)
        .thenCompose(u -> transport.disconnect().thenApply(c -> OpcUaClient.this))
        .exceptionally(ex -> OpcUaClient.this);
  }

  /**
   * Get the {@link OpcUaSession}, if it's available.
   *
   * @return the {@link OpcUaSession}.
   * @throws UaException if an error occurs getting the session.
   */
  public OpcUaSession getSession() throws UaException {
    try {
      return getSessionAsync().get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Get the {@link OpcUaSession}, if it's available.
   *
   * @return a {@link CompletableFuture} that completes successfully with the {@link OpcUaSession},
   *     or completes exceptionally if an error occurs getting the session.
   */
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

  public void addSubscription(OpcUaSubscription subscription) {
    subscription.getSubscriptionId().ifPresent(id -> subscriptions.put(id, subscription));
  }

  public void removeSubscription(OpcUaSubscription subscription) {
    subscription.getSubscriptionId().ifPresent(subscriptions::remove);
  }

  public List<OpcUaSubscription> getSubscriptions() {
    return List.copyOf(subscriptions.values());
  }

  public PublishingManager getPublishingManager() {
    return publishingManager;
  }

  /**
   * Get the client's "static" {@link DataTypeManager}.
   *
   * <p>This {@link DataTypeManager} is for static codecs that serialize classes that exist at
   * compile time, e.g. structures from namespace 0 and or code-generated structures.
   *
   * @return the client's static {@link DataTypeManager}.
   */
  public DataTypeManager getStaticDataTypeManager() {
    return staticDataTypeManager;
  }

  /**
   * Get the client's "dynamic" {@link DataTypeManager}.
   *
   * <p>This {@link DataTypeManager} is for dynamic codecs that were created by reading datatype
   * information from the server at runtime.
   *
   * @return the client's dynamic {@link DataTypeManager}.
   * @see #setDataTypeManagerInitializer(DataTypeManagerInitializer)
   */
  public DataTypeManager getDynamicDataTypeManager() throws UaException {
    return dynamicDataTypeManager.getOrThrow(
        () -> {
          DataTypeManager dataTypeManager =
              DefaultDataTypeManager.createAndInitialize(getNamespaceTable());
          dataTypeManagerInitializer.initialize(
              getNamespaceTable(), getDataTypeTree(), dataTypeManager);
          return dataTypeManager;
        });
  }

  /**
   * Get a "static" {@link EncodingContext} instance.
   *
   * <p>This {@link EncodingContext} instance returns the client's static {@link DataTypeManager}.
   *
   * @return a "static" {@link EncodingContext} instance.
   * @see #getStaticDataTypeManager()
   */
  public EncodingContext getStaticEncodingContext() {
    return staticEncodingContext;
  }

  /**
   * Get a "dynamic" {@link EncodingContext}.
   *
   * <p>This {@link EncodingContext} instance returns the client's dynamic {@link DataTypeManager}.
   *
   * @return a "dynamic" {@link EncodingContext}.
   * @see #getDynamicDataTypeManager()
   */
  public EncodingContext getDynamicEncodingContext() throws UaException {
    return dynamicEncodingContext.getOrThrow(
        () -> {
          final DataTypeManager dataTypeManager = getDynamicDataTypeManager();

          return new EncodingContext() {
            @Override
            public DataTypeManager getDataTypeManager() {
              return dataTypeManager;
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
        });
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
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Read the server's NamespaceTable (NamespaceArray) and update the local copy.
   *
   * <p>This call completes asynchronously.
   *
   * @return a {@link CompletableFuture} that completes successfully with the updated {@link
   *     NamespaceTable} or completes exceptionally if a service- or operation-level error occurs.
   */
  public CompletableFuture<NamespaceTable> readNamespaceTableAsync() {
    return getSessionAsync()
        .thenCompose(
            session -> {
              RequestHeader requestHeader = newRequestHeader(session.getAuthenticationToken());

              ReadRequest readRequest =
                  new ReadRequest(
                      requestHeader,
                      0.0,
                      TimestampsToReturn.Neither,
                      new ReadValueId[] {
                        new ReadValueId(
                            NodeIds.Server_NamespaceArray,
                            AttributeId.Value.uid(),
                            null,
                            QualifiedName.NULL_VALUE)
                      });

              CompletableFuture<String[]> namespaceArray =
                  sendRequestAsync(readRequest)
                      .thenApply(ReadResponse.class::cast)
                      .thenApply(response -> Objects.requireNonNull(response.getResults()))
                      .thenApply(results -> (String[]) results[0].value().value());

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
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Read the server's ServerTable (ServerArray) and update the local copy.
   *
   * <p>This call completes asynchronously.
   *
   * @return a {@link CompletableFuture} that completes successfully with the updated {@link
   *     ServerTable} or completes exceptionally if a service- or operation-level error occurs.
   */
  public CompletableFuture<ServerTable> readServerTableAsync() {
    return getSessionAsync()
        .thenCompose(
            session -> {
              RequestHeader requestHeader = newRequestHeader(session.getAuthenticationToken());

              ReadRequest readRequest =
                  new ReadRequest(
                      requestHeader,
                      0.0,
                      TimestampsToReturn.Neither,
                      new ReadValueId[] {
                        new ReadValueId(
                            NodeIds.Server_ServerArray,
                            AttributeId.Value.uid(),
                            null,
                            QualifiedName.NULL_VALUE)
                      });

              CompletableFuture<String[]> serverArray =
                  sendRequestAsync(readRequest)
                      .thenApply(ReadResponse.class::cast)
                      .thenApply(response -> Objects.requireNonNull(response.getResults()))
                      .thenApply(results -> (String[]) results[0].value().value());

              return serverArray
                  .thenAccept(this::updateServerTable)
                  .thenApply(v -> getServerTable());
            });
  }

  private void updateNamespaceTable(String[] namespaceArray) {
    if (namespaceArray.length > UShort.MAX_VALUE) {
      logger.warn(
          "NamespaceTable returned by " + "server contains " + namespaceArray.length + " entries");
    }

    NamespaceTable namespaceTable = getNamespaceTable();

    namespaceTable.update(
        uriTable -> {
          uriTable.clear();

          namespaceTable.add(Namespaces.OPC_UA);
          for (String uri : namespaceArray) {
            namespaceTable.add(uri);
          }
        });
  }

  private void updateServerTable(String[] serverArray) {
    if (serverArray.length > UShort.MAX_VALUE) {
      logger.warn(
          "ServerTable returned by " + "server contains " + serverArray.length + " entries");
    }

    ServerTable serverTable = getServerTable();

    serverTable.update(
        map -> {
          map.clear();

          for (String uri : serverArray) {
            serverTable.add(uri);
          }
        });
  }

  /**
   * Get the {@link DataTypeTree}, reading it from the server if necessary.
   *
   * @return the {@link DataTypeTree}.
   * @throws UaException if an error occurs while reading the DataTypes.
   */
  public DataTypeTree getDataTypeTree() throws UaException {
    try {
      return dataTypeTree.getOrThrow(() -> DataTypeTreeBuilder.build(this));
    } catch (Exception e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Read the {@link DataTypeTree} from the server and update the local copy.
   *
   * @return the updated {@link DataTypeTree}.
   * @throws UaException if an error occurs while reading the DataTypes.
   */
  public DataTypeTree readDataTypeTree() throws UaException {
    dataTypeTree.reset();

    return getDataTypeTree();
  }

  /**
   * Read the {@link DataTypeTree} from the server and update the local copy.
   *
   * @return a {@link CompletionStage} that completes successfully with the updated {@link
   *     DataTypeTree}, or completes exceptionally if an error occurs while reading the DataTypes.
   */
  public CompletionStage<DataTypeTree> readDataTypeTreeAsync() {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            return readDataTypeTree();
          } catch (UaException e) {
            throw new CompletionException(e);
          }
        },
        transport.getConfig().getExecutor());
  }

  /**
   * Get the local copy of the server's {@link OperationLimits}, or read them from the server if
   * they have not been read.
   *
   * @return the server's {@link OperationLimits}.
   * @throws UaException if an error occurs reading the operation limits.
   */
  public OperationLimits getOperationLimits() throws UaException {
    return operationLimits.getOrThrow(() -> OperationLimits.read(this));
  }

  /**
   * Read the server's OperationLimits and update the local copy.
   *
   * @return the server's {@link OperationLimits}.
   * @throws UaException if an error occurs reading the operation limits.
   */
  public OperationLimits readOperationLimits() throws UaException {
    operationLimits.reset();

    return getOperationLimits();
  }

  /**
   * Create a new {@link RequestHeader} with a null authentication token.
   *
   * <p>A unique request handle will be automatically assigned to the header.
   *
   * @return a new {@link RequestHeader} with a null authentication token.
   */
  public RequestHeader newRequestHeader() {
    return newRequestHeader(NodeId.NULL_VALUE);
  }

  /**
   * Create a new {@link RequestHeader} with {@code authToken}.
   *
   * <p>A unique request handle will be automatically assigned to the header.
   *
   * @param authToken the authentication token to create the header with.
   * @return a new {@link RequestHeader} created with {@code authToken}.
   */
  public RequestHeader newRequestHeader(NodeId authToken) {
    return newRequestHeader(authToken, config.getRequestTimeout());
  }

  /**
   * Create a new {@link RequestHeader} with {@code authToken} and {@code requestTimeout}.
   *
   * <p>A unique request handle will be automatically assigned to the header.
   *
   * @param authToken the authentication token to create the header with.
   * @param requestTimeout the timeout hint to create the header with.
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
        null);
  }

  /**
   * Set the {@link DataTypeManagerInitializer} that called to register codecs with the client's
   * dynamic {@link DataTypeManager}.
   *
   * <p>This resets the client's dynamic {@link DataTypeManager} and {@link EncodingContext}. They
   * will be built or rebuilt the next time they are accessed.
   *
   * @param dataTypeManagerInitializer the {@link DataTypeManagerInitializer} to set.
   * @see #getDynamicDataTypeManager()
   */
  public void setDataTypeManagerInitializer(DataTypeManagerInitializer dataTypeManagerInitializer) {
    this.dataTypeManagerInitializer = dataTypeManagerInitializer;

    dynamicDataTypeManager.reset();
    dynamicEncodingContext.reset();
  }

  // region Attribute Services

  /**
   * Read one or more attributes of one or more Nodes.
   *
   * @param maxAge the requested max age of the value, in milliseconds. If maxAge is set to 0, the
   *     Server shall attempt to read a new value from the data source. If maxAge is set to the max
   *     Int32 value or greater, the Server shall attempt to get a cached value. Negative values are
   *     invalid for maxAge.
   * @param timestampsToReturn the requested {@link TimestampsToReturn}.
   * @param readValueIds the {@link ReadValueId}s identifying the nodes and attributes to read.
   * @return the {@link ReadResponse}.
   * @throws UaException if an error occurs.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.10.2">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.10.2</a>
   */
  public ReadResponse read(
      double maxAge, TimestampsToReturn timestampsToReturn, List<ReadValueId> readValueIds)
      throws UaException {

    try {
      return readAsync(maxAge, timestampsToReturn, readValueIds).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Read the Value attribute of a Node.
   *
   * @param maxAge the requested max age of the value, in milliseconds. If maxAge is set to 0, the
   *     Server shall attempt to read a new value from the data source. If maxAge is set to the max
   *     Int32 value or greater, the Server shall attempt to get a cached value. Negative values are
   *     invalid for maxAge.
   * @param timestampsToReturn the requested {@link TimestampsToReturn}.
   * @param nodeId the {@link NodeId}s identifying the Node to read.
   * @return the {@link DataValue}.
   * @throws UaException if an error occurs.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.10.2">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.10.2</a>
   */
  public DataValue readValue(double maxAge, TimestampsToReturn timestampsToReturn, NodeId nodeId)
      throws UaException {

    return readValues(maxAge, timestampsToReturn, List.of(nodeId)).get(0);
  }

  /**
   * Read the Value attribute of one or more Nodes.
   *
   * @param maxAge the requested max age of the value, in milliseconds. If maxAge is set to 0, the
   *     Server shall attempt to read a new value from the data source. If maxAge is set to the max
   *     Int32 value or greater, the Server shall attempt to get a cached value. Negative values are
   *     invalid for maxAge.
   * @param timestampsToReturn the requested {@link TimestampsToReturn}.
   * @param nodeIds the {@link NodeId}s identifying the Nodes to read.
   * @return a List of {@link DataValue}s.
   * @throws UaException if an error occurs.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.10.2">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.10.2</a>
   */
  public List<DataValue> readValues(
      double maxAge, TimestampsToReturn timestampsToReturn, List<NodeId> nodeIds)
      throws UaException {

    try {
      return readValuesAsync(maxAge, timestampsToReturn, nodeIds).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Read one or more attributes of one or more Nodes.
   *
   * @param maxAge the requested max age of the value, in milliseconds. If maxAge is set to 0, the
   *     Server shall attempt to read a new value from the data source. If maxAge is set to the max
   *     Int32 value or greater, the Server shall attempt to get a cached value. Negative values are
   *     invalid for maxAge.
   * @param timestampsToReturn the requested {@link TimestampsToReturn}.
   * @param readValueIds the {@link ReadValueId}s identifying the nodes and attributes to read.
   * @return a {@link CompletableFuture} that completes successfully with the {@link ReadResponse},
   *     or completes exceptionally if an error occurs.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.10.2">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.10.2</a>
   */
  public CompletableFuture<ReadResponse> readAsync(
      double maxAge, TimestampsToReturn timestampsToReturn, List<ReadValueId> readValueIds) {

    return getSessionAsync()
        .thenCompose(
            session -> {
              var request =
                  new ReadRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      maxAge,
                      timestampsToReturn,
                      readValueIds.toArray(new ReadValueId[0]));

              return sendRequestAsync(request).thenApply(ReadResponse.class::cast);
            });
  }

  /**
   * Read the Value attribute of one or more Nodes.
   *
   * @param maxAge the requested max age of the value, in milliseconds. If maxAge is set to 0, the
   *     Server shall attempt to read a new value from the data source. If maxAge is set to the max
   *     Int32 value or greater, the Server shall attempt to get a cached value. Negative values are
   *     invalid for maxAge.
   * @param timestampsToReturn the requested {@link TimestampsToReturn}.
   * @param nodeIds the {@link NodeId}s identifying the Nodes to read.
   * @return a {@link CompletableFuture} that completes successfully with a list of {@link
   *     DataValue}s, or completes exceptionally if an error occurs.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.10.2">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.10.2</a>
   */
  public CompletableFuture<List<DataValue>> readValuesAsync(
      double maxAge, TimestampsToReturn timestampsToReturn, List<NodeId> nodeIds) {

    List<ReadValueId> readValueIds =
        nodeIds.stream()
            .map(
                nodeId ->
                    new ReadValueId(
                        nodeId, AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE))
            .collect(Collectors.toList());

    return readAsync(maxAge, timestampsToReturn, readValueIds)
        .thenApply(r -> Lists.ofNullable(r.getResults()));
  }

  /**
   * Write attribute values to one or more Nodes.
   *
   * @param writeValues a List of {@link WriteValue}s describing the Nodes and attribute values to
   *     write.
   * @return the {@link WriteResponse}.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.10.4">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.10.4</a>
   */
  public WriteResponse write(List<WriteValue> writeValues) throws UaException {
    try {
      return writeAsync(writeValues).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Write the Value attribute of one or more Nodes.
   *
   * @param nodeIds the {@link NodeId}s identifying the Nodes to write.
   * @param values the {@link DataValue}s to write.
   * @return a List of {@link StatusCode}s describing the result of each write operation.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.10.4">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.10.4</a>
   */
  public List<StatusCode> writeValues(List<NodeId> nodeIds, List<DataValue> values)
      throws UaException {
    try {
      return writeValuesAsync(nodeIds, values).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Write attribute values to one or more Nodes.
   *
   * @param writeValues a List of {@link WriteValue}s describing the Nodes and attribute values to
   *     write
   * @return a {@link CompletableFuture} that completes successfully with the {@link WriteResponse},
   *     or completes exceptionally if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.10.4">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.10.4</a>
   */
  public CompletableFuture<WriteResponse> writeAsync(List<WriteValue> writeValues) {
    return getSessionAsync()
        .thenCompose(
            session -> {
              WriteRequest request =
                  new WriteRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      writeValues.toArray(new WriteValue[0]));

              return sendRequestAsync(request).thenApply(WriteResponse.class::cast);
            });
  }

  /**
   * Write the Value attribute of one or more Nodes.
   *
   * @param nodeIds the {@link NodeId}s identifying the Nodes to write.
   * @param values the {@link DataValue}s to write.
   * @return a {@link CompletableFuture} that completes successfully with a List of {@link
   *     StatusCode}s describing the result of each write operation, or completes exceptionally if
   *     there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.10.4">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.10.4</a>
   */
  public CompletableFuture<List<StatusCode>> writeValuesAsync(
      List<NodeId> nodeIds, List<DataValue> values) {
    var writeValues = new ArrayList<WriteValue>(nodeIds.size());

    for (int i = 0; i < nodeIds.size(); i++) {
      NodeId nodeId = nodeIds.get(i);
      DataValue value = values.get(i);
      writeValues.add(new WriteValue(nodeId, AttributeId.Value.uid(), null, value));
    }

    return writeAsync(writeValues).thenApply(response -> Lists.ofNullable(response.getResults()));
  }

  /**
   * Read historical values or Events of one or more Nodes.
   *
   * @param historyReadDetails defines the types of history read to be performed.
   * @param timestampsToReturn specifies the timestamps to be returned for each requested value
   *     attribute.
   * @param releaseContinuationPoints if {@code true}, passed continuationPoints shall be reset to
   *     free resources in the Server. if {@code false}, passed continuationPoints shall be used to
   *     get the next set of historical information.
   * @param nodesToRead the list of items upon which the historical retrieval is to be performed.
   * @return the {@link HistoryReadResponse}.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.10.3">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.10.3</a>
   */
  public HistoryReadResponse historyRead(
      HistoryReadDetails historyReadDetails,
      TimestampsToReturn timestampsToReturn,
      boolean releaseContinuationPoints,
      List<HistoryReadValueId> nodesToRead)
      throws UaException {

    try {
      var future =
          historyReadAsync(
              historyReadDetails, timestampsToReturn, releaseContinuationPoints, nodesToRead);

      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Read historical values or Events of one or more Nodes.
   *
   * @param historyReadDetails defines the types of history read to be performed.
   * @param timestampsToReturn specifies the timestamps to be returned for each requested value
   *     attribute.
   * @param releaseContinuationPoints if {@code true}, passed continuationPoints shall be reset to
   *     free resources in the Server. if {@code false}, passed continuationPoints shall be used to
   *     get the next set of historical information.
   * @param nodesToRead the list of items upon which the historical retrieval is to be performed.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     HistoryReadResponse}, or completes exceptionally if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.10.3">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.10.3</a>
   */
  public CompletableFuture<HistoryReadResponse> historyReadAsync(
      HistoryReadDetails historyReadDetails,
      TimestampsToReturn timestampsToReturn,
      boolean releaseContinuationPoints,
      List<HistoryReadValueId> nodesToRead) {

    return getSessionAsync()
        .thenCompose(
            session -> {
              HistoryReadRequest request =
                  new HistoryReadRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      ExtensionObject.encode(getStaticEncodingContext(), historyReadDetails),
                      timestampsToReturn,
                      releaseContinuationPoints,
                      nodesToRead.toArray(new HistoryReadValueId[0]));

              return sendRequestAsync(request).thenApply(HistoryReadResponse.class::cast);
            });
  }

  /**
   * Update historical values or Events of one or more Nodes.
   *
   * @param historyUpdateDetails the {@link HistoryUpdateDetails} for this update.
   * @return the {@link HistoryUpdateResponse}.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.10.5">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.10.5</a>
   */
  public HistoryUpdateResponse historyUpdate(List<HistoryUpdateDetails> historyUpdateDetails)
      throws UaException {
    try {
      return historyUpdateAsync(historyUpdateDetails).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Update historical values or Events of one or more Nodes.
   *
   * @param historyUpdateDetails the {@link HistoryUpdateDetails} for this update.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     HistoryUpdateResponse}, or completes exceptionally if there is an error invoking the
   *     service.
   */
  public CompletableFuture<HistoryUpdateResponse> historyUpdateAsync(
      List<HistoryUpdateDetails> historyUpdateDetails) {

    return getSessionAsync()
        .thenCompose(
            session -> {
              ExtensionObject[] details =
                  historyUpdateDetails.stream()
                      .map(hud -> ExtensionObject.encode(getStaticEncodingContext(), hud))
                      .toArray(ExtensionObject[]::new);

              HistoryUpdateRequest request =
                  new HistoryUpdateRequest(
                      newRequestHeader(session.getAuthenticationToken()), details);

              return sendRequestAsync(request).thenApply(HistoryUpdateResponse.class::cast);
            });
  }

  // endregion

  // region Method Services

  /**
   * Call (invoke) one or more methods.
   *
   * @param requests the {@link CallMethodRequest}s identifying the object/method to call and the
   *     input arguments.
   * @return the {@link CallResponse}.
   * @throws UaException if an error occurs.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.11.2">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.11.2</a>
   */
  public CallResponse call(List<CallMethodRequest> requests) throws UaException {
    try {
      return callAsync(requests).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Call (invoke) one or more methods.
   *
   * @param requests the {@link CallMethodRequest}s identifying the object/method to call and the
   *     input arguments.
   * @return a {@link CompletableFuture} that completes successfully with the {@link CallResponse},
   *     or completes exceptionally if an error occurs.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.11.2">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.11.2</a>
   */
  public CompletableFuture<CallResponse> callAsync(List<CallMethodRequest> requests) {
    return getSessionAsync()
        .thenCompose(
            session -> {
              CallRequest request =
                  new CallRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      requests.toArray(new CallMethodRequest[0]));

              return sendRequestAsync(request).thenApply(CallResponse.class::cast);
            });
  }

  // endregion

  // region MonitoredItem Services

  /**
   * Create and add one or more Monitored Items to a Subscription.
   *
   * @param subscriptionId the server-assigned identifier for the subscription that will report
   *     notifications for the Monitored Items.
   * @param timestampsToReturn the {@link TimestampsToReturn}.
   * @param itemsToCreate a List of {@link MonitoredItemCreateRequest} that describe the Monitored
   *     Items to be created and assigned to the specified subscription.
   * @return the {@link CreateMonitoredItemsResponse}.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.12.2">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.12.2</a>
   */
  public CreateMonitoredItemsResponse createMonitoredItems(
      UInteger subscriptionId,
      TimestampsToReturn timestampsToReturn,
      List<MonitoredItemCreateRequest> itemsToCreate)
      throws UaException {

    try {
      return createMonitoredItemsAsync(subscriptionId, timestampsToReturn, itemsToCreate).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Create and add one or more Monitored Items to a Subscription.
   *
   * @param subscriptionId the server-assigned identifier for the subscription that will report
   *     notifications for the Monitored Items.
   * @param timestampsToReturn the {@link TimestampsToReturn}.
   * @param itemsToCreate a List of {@link MonitoredItemCreateRequest} that describe the Monitored
   *     Items to be created and assigned to the specified subscription.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     CreateMonitoredItemsResponse}, or completes exceptionally if there is an error invoking the
   *     service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.12.2">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.12.2</a>
   */
  public CompletableFuture<CreateMonitoredItemsResponse> createMonitoredItemsAsync(
      UInteger subscriptionId,
      TimestampsToReturn timestampsToReturn,
      List<MonitoredItemCreateRequest> itemsToCreate) {

    return getSessionAsync()
        .thenCompose(
            session -> {
              CreateMonitoredItemsRequest request =
                  new CreateMonitoredItemsRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      subscriptionId,
                      timestampsToReturn,
                      itemsToCreate.toArray(new MonitoredItemCreateRequest[0]));

              return sendRequestAsync(request).thenApply(CreateMonitoredItemsResponse.class::cast);
            });
  }

  /**
   * Modify Monitored Items of a Subscription.
   *
   * @param subscriptionId the server-assigned identifier for the subscription that owns the items
   *     to modify.
   * @param timestampsToReturn the {@link TimestampsToReturn}.
   * @param itemsToModify a List of {@link MonitoredItemModifyRequest} that describe Monitored Items
   *     to modify.
   * @return the {@link ModifyMonitoredItemsResponse}.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.12.3">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.12.3</a>
   */
  public ModifyMonitoredItemsResponse modifyMonitoredItems(
      UInteger subscriptionId,
      TimestampsToReturn timestampsToReturn,
      List<MonitoredItemModifyRequest> itemsToModify)
      throws UaException {

    try {
      return modifyMonitoredItemsAsync(subscriptionId, timestampsToReturn, itemsToModify).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Modify Monitored Items of a Subscription.
   *
   * @param subscriptionId the server-assigned identifier for the subscription that owns the items
   *     to modify.
   * @param timestampsToReturn the {@link TimestampsToReturn}.
   * @param itemsToModify a List of {@link MonitoredItemModifyRequest} that describe Monitored Items
   *     to modify.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     ModifyMonitoredItemsResponse}, or completes exceptionally if there is an error invoking the
   *     service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.12.3">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.12.3</a>
   */
  public CompletableFuture<ModifyMonitoredItemsResponse> modifyMonitoredItemsAsync(
      UInteger subscriptionId,
      TimestampsToReturn timestampsToReturn,
      List<MonitoredItemModifyRequest> itemsToModify) {

    return getSessionAsync()
        .thenCompose(
            session -> {
              ModifyMonitoredItemsRequest request =
                  new ModifyMonitoredItemsRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      subscriptionId,
                      timestampsToReturn,
                      itemsToModify.toArray(new MonitoredItemModifyRequest[0]));

              return sendRequestAsync(request).thenApply(ModifyMonitoredItemsResponse.class::cast);
            });
  }

  /**
   * Delete one or more Monitored Items of a Subscription.
   *
   * @param subscriptionId the server-assigned identifier for the subscription that owns the
   *     Monitored Items to delete.
   * @param monitoredItemIds a List of server-assigned identifiers for the Monitored Items to
   *     delete.
   * @return the {@link DeleteMonitoredItemsResponse}.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.12.6">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.12.6</a>
   */
  public DeleteMonitoredItemsResponse deleteMonitoredItems(
      UInteger subscriptionId, List<UInteger> monitoredItemIds) throws UaException {

    try {
      return deleteMonitoredItemsAsync(subscriptionId, monitoredItemIds).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Delete one or more Monitored Items of a Subscription.
   *
   * @param subscriptionId the server-assigned identifier for the subscription that owns the
   *     Monitored Items to delete.
   * @param monitoredItemIds a List of server-assigned identifiers for the Monitored Items to
   *     delete.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     DeleteMonitoredItemsResponse}, or completes exceptionally if there is an error invoking the
   *     service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.12.6">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.12.6</a>
   */
  public CompletableFuture<DeleteMonitoredItemsResponse> deleteMonitoredItemsAsync(
      UInteger subscriptionId, List<UInteger> monitoredItemIds) {

    return getSessionAsync()
        .thenCompose(
            session -> {
              DeleteMonitoredItemsRequest request =
                  new DeleteMonitoredItemsRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      subscriptionId,
                      monitoredItemIds.toArray(new UInteger[0]));

              return sendRequestAsync(request).thenApply(DeleteMonitoredItemsResponse.class::cast);
            });
  }

  /**
   * Set the Monitoring Mode for one or more Monitored Items of a Subscription.
   *
   * @param subscriptionId the server-assigned identifier for the subscription that owns the
   *     Monitored Items.
   * @param monitoringMode the {@link MonitoringMode} to be set for the Monitored Items.
   * @param monitoredItemIds a List of server-assigned identifiers for the Monitored Items that will
   *     have their {@link MonitoringMode} set.
   * @return the {@link SetMonitoringModeResponse}.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.12.4">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.12.4</a>
   */
  public SetMonitoringModeResponse setMonitoringMode(
      UInteger subscriptionId, MonitoringMode monitoringMode, List<UInteger> monitoredItemIds)
      throws UaException {

    try {
      return setMonitoringModeAsync(subscriptionId, monitoringMode, monitoredItemIds).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Set the Monitoring Mode for one or more Monitored Items of a Subscription.
   *
   * @param subscriptionId the server-assigned identifier for the subscription that owns the
   *     Monitored Items.
   * @param monitoringMode the {@link MonitoringMode} to be set for the Monitored Items.
   * @param monitoredItemIds a List of server-assigned identifiers for the Monitored Items that will
   *     have their {@link MonitoringMode} set.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     SetMonitoringModeResponse}, or completes exceptionally if there is an error invoking the
   *     service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.12.4">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.12.4</a>
   */
  public CompletableFuture<SetMonitoringModeResponse> setMonitoringModeAsync(
      UInteger subscriptionId, MonitoringMode monitoringMode, List<UInteger> monitoredItemIds) {

    return getSessionAsync()
        .thenCompose(
            session -> {
              SetMonitoringModeRequest request =
                  new SetMonitoringModeRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      subscriptionId,
                      monitoringMode,
                      monitoredItemIds.toArray(new UInteger[0]));

              return sendRequestAsync(request).thenApply(SetMonitoringModeResponse.class::cast);
            });
  }

  /**
   * Create and delete triggering links for a triggering item.
   *
   * @param subscriptionId the server-assigned identifier for the subscription that owns the
   *     triggering item and items to report.
   * @param triggeringItemId the server-assigned identifier for the Monitored Item to use as the
   *     triggering item.
   * @param linksToAdd a List of server-assigned identifiers for the Monitored Items that are to be
   *     added as triggering links.
   * @param linksToRemove a List of server-assigned identifiers for the Monitored Items that are to
   *     be removed as triggering links.
   * @return the {@link SetTriggeringResponse}.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.12.5">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.12.5</a>
   */
  public SetTriggeringResponse setTriggering(
      UInteger subscriptionId,
      UInteger triggeringItemId,
      List<UInteger> linksToAdd,
      List<UInteger> linksToRemove)
      throws UaException {

    try {
      return setTriggeringAsync(subscriptionId, triggeringItemId, linksToAdd, linksToRemove).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Create and delete triggering links for a triggering item.
   *
   * @param subscriptionId the server-assigned identifier for the subscription that owns the
   *     triggering item and items to report.
   * @param triggeringItemId the server-assigned identifier for the Monitored Item to use as the
   *     triggering item.
   * @param linksToAdd a List of server-assigned identifiers for the Monitored Items that are to be
   *     added as triggering links.
   * @param linksToRemove a List of server-assigned identifiers for the Monitored Items that are to
   *     be removed as triggering links.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     SetTriggeringResponse}, or completes exceptionally if there is an error invoking the
   *     service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.12.5">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.12.5</a>
   */
  public CompletableFuture<SetTriggeringResponse> setTriggeringAsync(
      UInteger subscriptionId,
      UInteger triggeringItemId,
      List<UInteger> linksToAdd,
      List<UInteger> linksToRemove) {

    return getSessionAsync()
        .thenCompose(
            session -> {
              SetTriggeringRequest request =
                  new SetTriggeringRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      subscriptionId,
                      triggeringItemId,
                      linksToAdd.toArray(new UInteger[0]),
                      linksToRemove.toArray(new UInteger[0]));

              return sendRequestAsync(request).thenApply(SetTriggeringResponse.class::cast);
            });
  }

  // endregion

  // region NodeManagement Services

  /**
   * Add one or more Nodes to the AddressSpace hierarchy.
   *
   * @param nodesToAdd a List of {@link AddNodesItem} describing the Nodes to be added.
   * @return the {@link AddNodesResponse}.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.7.2">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.7.2</a>
   */
  public AddNodesResponse addNodes(List<AddNodesItem> nodesToAdd) throws UaException {
    try {
      return addNodesAsync(nodesToAdd).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Add one or more Nodes to the AddressSpace hierarchy.
   *
   * @param nodesToAdd a List of {@link AddNodesItem} describing the Nodes to be added.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     AddNodesResponse}, or completes exceptionally if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.7.2">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.7.2</a>
   */
  public CompletableFuture<AddNodesResponse> addNodesAsync(List<AddNodesItem> nodesToAdd) {
    return getSessionAsync()
        .thenCompose(
            session -> {
              AddNodesRequest request =
                  new AddNodesRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      nodesToAdd.toArray(new AddNodesItem[0]));

              return sendRequestAsync(request).thenApply(AddNodesResponse.class::cast);
            });
  }

  /**
   * Add one or more References to one or more Nodes.
   *
   * @param referencesToAdd a List of {@link AddReferencesItem}s describing the References to add.
   * @return the {@link AddReferencesResponse}.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.7.3">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.7.3</a>
   */
  public AddReferencesResponse addReferences(List<AddReferencesItem> referencesToAdd)
      throws UaException {
    try {
      return addReferencesAsync(referencesToAdd).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Add one or more References to one or more Nodes.
   *
   * @param referencesToAdd a List of {@link AddReferencesItem}s describing the References to add.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     AddReferencesResponse}, or completes exceptionally if there is an error invoking the
   *     service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.7.3">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.7.3</a>
   */
  public CompletableFuture<AddReferencesResponse> addReferencesAsync(
      List<AddReferencesItem> referencesToAdd) {
    return getSessionAsync()
        .thenCompose(
            session -> {
              AddReferencesRequest request =
                  new AddReferencesRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      referencesToAdd.toArray(new AddReferencesItem[0]));

              return sendRequestAsync(request).thenApply(AddReferencesResponse.class::cast);
            });
  }

  /**
   * Delete one or more Nodes from the AddressSpace hierarchy.
   *
   * @param nodesToDelete a List of {@link DeleteNodesItem}s describing the Nodes to delete.
   * @return the {@link DeleteNodesResponse}.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.7.4">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.7.4</a>
   */
  public DeleteNodesResponse deleteNodes(List<DeleteNodesItem> nodesToDelete) throws UaException {
    try {
      return deleteNodesAsync(nodesToDelete).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Delete one or more Nodes from the AddressSpace hierarchy.
   *
   * @param nodesToDelete a List of {@link DeleteNodesItem}s describing the Nodes to delete.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     DeleteNodesResponse}, or completes exceptionally if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.7.4">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.7.4</a>
   */
  public CompletableFuture<DeleteNodesResponse> deleteNodesAsync(
      List<DeleteNodesItem> nodesToDelete) {
    return getSessionAsync()
        .thenCompose(
            session -> {
              DeleteNodesRequest request =
                  new DeleteNodesRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      nodesToDelete.toArray(new DeleteNodesItem[0]));

              return sendRequestAsync(request).thenApply(DeleteNodesResponse.class::cast);
            });
  }

  /**
   * Delete one or more References from one or more Nodes.
   *
   * @param referencesToDelete a List of {@link DeleteReferencesItem} describing the References to
   *     delete.
   * @return the {@link DeleteReferencesResponse}.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.7.5">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.7.5</a>
   */
  public DeleteReferencesResponse deleteReferences(List<DeleteReferencesItem> referencesToDelete)
      throws UaException {

    try {
      return deleteReferencesAsync(referencesToDelete).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Delete one or more References from one or more Nodes.
   *
   * @param referencesToDelete a List of {@link DeleteReferencesItem} describing the References to
   *     delete.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     DeleteReferencesResponse}, or completes exceptionally if there is an error invoking the
   *     service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.7.5">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.7.5</a>
   */
  public CompletableFuture<DeleteReferencesResponse> deleteReferencesAsync(
      List<DeleteReferencesItem> referencesToDelete) {

    return getSessionAsync()
        .thenCompose(
            session -> {
              DeleteReferencesRequest request =
                  new DeleteReferencesRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      referencesToDelete.toArray(new DeleteReferencesItem[0]));

              return sendRequestAsync(request).thenApply(DeleteReferencesResponse.class::cast);
            });
  }

  // endregion

  // region Query Services

  /**
   * Issue a query request to the server.
   *
   * @param view specifies a View and temporal context to a server.
   * @param nodeTypes a List of {@link NodeTypeDescription} that describe that type of Nodes to
   *     search for.
   * @param filter a {@link ContentFilter} that limits the resulting Nodes to those matching the
   *     filter criteria.
   * @param maxDataSetsToReturn the maximum number of {@link QueryDataSet}s to return.
   * @param maxReferencesToReturn the maximum number of References to return for each {@link
   *     QueryDataSet}.
   * @return the {@link QueryFirstResponse}.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.9.3">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.9.3</a>
   */
  public QueryFirstResponse queryFirst(
      ViewDescription view,
      List<NodeTypeDescription> nodeTypes,
      ContentFilter filter,
      UInteger maxDataSetsToReturn,
      UInteger maxReferencesToReturn)
      throws UaException {

    try {
      return queryFirstAsync(view, nodeTypes, filter, maxDataSetsToReturn, maxReferencesToReturn)
          .get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Issue a query request to the server.
   *
   * @param view specifies a View and temporal context to a server.
   * @param nodeTypes a List of {@link NodeTypeDescription} that describe that type of Nodes to
   *     search for.
   * @param filter a {@link ContentFilter} that limits the resulting Nodes to those matching the
   *     filter criteria.
   * @param maxDataSetsToReturn the maximum number of {@link QueryDataSet}s to return.
   * @param maxReferencesToReturn the maximum number of References to return for each {@link
   *     QueryDataSet}.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     QueryFirstResponse}, or completes exceptionally if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.9.3">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.9.3</a>
   */
  public CompletableFuture<QueryFirstResponse> queryFirstAsync(
      ViewDescription view,
      List<NodeTypeDescription> nodeTypes,
      ContentFilter filter,
      UInteger maxDataSetsToReturn,
      UInteger maxReferencesToReturn) {

    return getSessionAsync()
        .thenCompose(
            session -> {
              QueryFirstRequest request =
                  new QueryFirstRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      view,
                      nodeTypes.toArray(new NodeTypeDescription[0]),
                      filter,
                      maxDataSetsToReturn,
                      maxReferencesToReturn);

              return sendRequestAsync(request).thenApply(QueryFirstResponse.class::cast);
            });
  }

  /**
   * Request the next set of QueryFirst or QueryNext response information that was too large to be
   * sent in a single response.
   *
   * @param releaseContinuationPoint if {@code true}, passed continuation points shall be reset to
   *     free resources in the server. If {@code false}, passed continuation Points shall be used to
   *     get the next set of browse information.
   * @param continuationPoint a server-defined opaque value that represents the continuation point.
   * @return the {@link QueryNextResponse}.
   * @throws UaException if there is an error invoking the service.
   * @see #queryFirst(ViewDescription, List, ContentFilter, UInteger, UInteger)
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.9.4">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.9.4</a>
   */
  public QueryNextResponse queryNext(boolean releaseContinuationPoint, ByteString continuationPoint)
      throws UaException {

    try {
      return queryNextAsync(releaseContinuationPoint, continuationPoint).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Request the next set of QueryFirst or QueryNext response information that was too large to be
   * sent in a single response.
   *
   * @param releaseContinuationPoint if {@code true}, passed continuation points shall be reset to
   *     free resources in the server. If {@code false}, passed continuation Points shall be used to
   *     get the next set of browse information.
   * @param continuationPoint a server-defined opaque value that represents the continuation point.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     QueryNextResponse}, or completes exceptionally if there is an error invoking the service.
   * @see #queryFirstAsync(ViewDescription, List, ContentFilter, UInteger, UInteger)
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.9.4">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.9.4</a>
   */
  public CompletableFuture<QueryNextResponse> queryNextAsync(
      boolean releaseContinuationPoint, ByteString continuationPoint) {

    return getSessionAsync()
        .thenCompose(
            session -> {
              QueryNextRequest request =
                  new QueryNextRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      releaseContinuationPoint,
                      continuationPoint);

              return sendRequestAsync(request).thenApply(QueryNextResponse.class::cast);
            });
  }

  // endregion

  // region Subscription Services

  /**
   * Create a new Subscription belonging to this client's Session.
   *
   * @param requestedPublishingInterval this interval defines the cyclic rate that the subscription
   *     is being requested to return notifications to the client. This interval is expressed in
   *     milliseconds.
   * @param requestedLifetimeCount the requested lifetime count. The lifetime count shall be a
   *     minimum of three times the keep keep-alive count.
   * @param requestedMaxKeepAliveCount the requested maximum keep-alive count. When the publishing
   *     timer has expired this number of times without requiring any notification to be sent, the
   *     subscription sends a keep-alive message to the client.
   * @param maxNotificationsPerPublish the maximum number of notifications that the client wishes to
   *     receive in a single publish response. A value of zero indicates that there is no limit.
   * @param publishingEnabled if {@code true}, publishing is enabled for this subscription.
   * @param priority indicates the relative priority of the subscription.
   * @return the {@link CreateSubscriptionResponse}.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.13.2">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.13.2</a>
   */
  public CreateSubscriptionResponse createSubscription(
      double requestedPublishingInterval,
      UInteger requestedLifetimeCount,
      UInteger requestedMaxKeepAliveCount,
      UInteger maxNotificationsPerPublish,
      boolean publishingEnabled,
      UByte priority)
      throws UaException {

    try {
      CompletableFuture<CreateSubscriptionResponse> future =
          createSubscriptionAsync(
              requestedPublishingInterval,
              requestedLifetimeCount,
              requestedMaxKeepAliveCount,
              maxNotificationsPerPublish,
              publishingEnabled,
              priority);

      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Create a new Subscription belonging to this client's Session.
   *
   * @param requestedPublishingInterval this interval defines the cyclic rate that the subscription
   *     is being requested to return notifications to the client. This interval is expressed in
   *     milliseconds.
   * @param requestedLifetimeCount the requested lifetime count. The lifetime count shall be a
   *     minimum of three times the keep keep-alive count.
   * @param requestedMaxKeepAliveCount the requested maximum keep-alive count. When the publishing
   *     timer has expired this number of times without requiring any notification to be sent, the
   *     subscription sends a keep-alive message to the client.
   * @param maxNotificationsPerPublish the maximum number of notifications that the client wishes to
   *     receive in a single publish response. A value of zero indicates that there is no limit.
   * @param publishingEnabled if {@code true}, publishing is enabled for this subscription.
   * @param priority indicates the relative priority of the subscription.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     CreateSubscriptionResponse}, or completes exceptionally if there is an error invoking the
   *     service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.13.2">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.13.2</a>
   */
  public CompletableFuture<CreateSubscriptionResponse> createSubscriptionAsync(
      double requestedPublishingInterval,
      UInteger requestedLifetimeCount,
      UInteger requestedMaxKeepAliveCount,
      UInteger maxNotificationsPerPublish,
      boolean publishingEnabled,
      UByte priority) {

    return getSessionAsync()
        .thenCompose(
            session -> {
              CreateSubscriptionRequest request =
                  new CreateSubscriptionRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      requestedPublishingInterval,
                      requestedLifetimeCount,
                      requestedMaxKeepAliveCount,
                      maxNotificationsPerPublish,
                      publishingEnabled,
                      priority);

              return sendRequestAsync(request).thenApply(CreateSubscriptionResponse.class::cast);
            });
  }

  /**
   * Modify an existing Subscription belonging to this client's Session.
   *
   * @param subscriptionId the server-assigned identifier for the Subscription.
   * @param requestedPublishingInterval this interval defines the cyclic rate that the subscription
   *     is being requested to return notifications to the client. This interval is expressed in
   *     milliseconds.
   * @param requestedLifetimeCount the requested lifetime count. The lifetime count shall be a
   *     minimum of three times the keep keep-alive count.
   * @param requestedMaxKeepAliveCount the requested maximum keep-alive count. When the publishing
   *     timer has expired this number of times without requiring any notification to be sent, the
   *     subscription sends a keep-alive message to the client.
   * @param maxNotificationsPerPublish the maximum number of notifications that the client wishes to
   *     receive in a single publish response. A value of zero indicates that there is no limit.
   * @param priority indicates the relative priority of the subscription.
   * @return the {@link ModifySubscriptionResponse}.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.13.3">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.13.3</a>
   */
  public ModifySubscriptionResponse modifySubscription(
      UInteger subscriptionId,
      double requestedPublishingInterval,
      UInteger requestedLifetimeCount,
      UInteger requestedMaxKeepAliveCount,
      UInteger maxNotificationsPerPublish,
      UByte priority)
      throws UaException {

    try {
      CompletableFuture<ModifySubscriptionResponse> future =
          modifySubscriptionAsync(
              subscriptionId,
              requestedPublishingInterval,
              requestedLifetimeCount,
              requestedMaxKeepAliveCount,
              maxNotificationsPerPublish,
              priority);

      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Modify an existing Subscription belonging to this client's Session.
   *
   * @param subscriptionId the server-assigned identifier for the Subscription.
   * @param requestedPublishingInterval this interval defines the cyclic rate that the subscription
   *     is being requested to return notifications to the client. This interval is expressed in
   *     milliseconds.
   * @param requestedLifetimeCount the requested lifetime count. The lifetime count shall be a
   *     minimum of three times the keep keep-alive count.
   * @param requestedMaxKeepAliveCount the requested maximum keep-alive count. When the publishing
   *     timer has expired this number of times without requiring any notification to be sent, the
   *     subscription sends a keep-alive message to the client.
   * @param maxNotificationsPerPublish the maximum number of notifications that the client wishes to
   *     receive in a single publish response. A value of zero indicates that there is no limit.
   * @param priority indicates the relative priority of the subscription.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     ModifySubscriptionResponse}, or completes exceptionally if there is an error invoking the
   *     service.
   */
  public CompletableFuture<ModifySubscriptionResponse> modifySubscriptionAsync(
      UInteger subscriptionId,
      double requestedPublishingInterval,
      UInteger requestedLifetimeCount,
      UInteger requestedMaxKeepAliveCount,
      UInteger maxNotificationsPerPublish,
      UByte priority) {

    return getSessionAsync()
        .thenCompose(
            session -> {
              ModifySubscriptionRequest request =
                  new ModifySubscriptionRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      subscriptionId,
                      requestedPublishingInterval,
                      requestedLifetimeCount,
                      requestedMaxKeepAliveCount,
                      maxNotificationsPerPublish,
                      priority);

              return sendRequestAsync(request).thenApply(ModifySubscriptionResponse.class::cast);
            });
  }

  /**
   * Delete one or more Subscriptions belonging to this client's Session.
   *
   * @param subscriptionIds the server-assigned identifiers of the Subscriptions to delete.
   * @return the {@link DeleteSubscriptionsResponse}.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.13.8">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.13.8</a>
   */
  public DeleteSubscriptionsResponse deleteSubscriptions(List<UInteger> subscriptionIds)
      throws UaException {
    try {
      CompletableFuture<DeleteSubscriptionsResponse> future =
          deleteSubscriptionsAsync(subscriptionIds);

      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Delete one or more Subscriptions belonging to this client's Session.
   *
   * @param subscriptionIds the server-assigned identifiers of the Subscriptions to delete.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     DeleteSubscriptionsResponse}, or completes exceptionally if there is an error invoking the
   *     service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.13.8">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.13.8</a>
   */
  public CompletableFuture<DeleteSubscriptionsResponse> deleteSubscriptionsAsync(
      List<UInteger> subscriptionIds) {
    return getSessionAsync()
        .thenCompose(
            session -> {
              DeleteSubscriptionsRequest request =
                  new DeleteSubscriptionsRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      subscriptionIds.toArray(new UInteger[0]));

              return sendRequestAsync(request).thenApply(DeleteSubscriptionsResponse.class::cast);
            });
  }

  /**
   * Transfer a Subscription and its MonitoredItems from one Session to another (this) Session.
   *
   * @param subscriptionIds a List of server-assigned identifiers to be transferred to this Session.
   * @param sendInitialValues {@code true} if the first Publish response(s) after the transfer shall
   *     contain the current values of all MonitoredItems in the Subscription where the Monitoring
   *     Mode is set to {@link MonitoringMode#Reporting}.
   * @return the {@link TransferSubscriptionsResponse}.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.13.7">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.13.7</a>
   */
  public TransferSubscriptionsResponse transferSubscriptions(
      List<UInteger> subscriptionIds, boolean sendInitialValues) throws UaException {

    try {
      CompletableFuture<TransferSubscriptionsResponse> future =
          transferSubscriptionsAsync(subscriptionIds, sendInitialValues);

      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Transfer a Subscription and its MonitoredItems from one Session to another (this) Session.
   *
   * @param subscriptionIds a List of server-assigned identifiers to be transferred to this Session.
   * @param sendInitialValues {@code true} if the first Publish response(s) after the transfer shall
   *     contain the current values of all MonitoredItems in the Subscription where the Monitoring
   *     Mode is set to {@link MonitoringMode#Reporting}.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     TransferSubscriptionsResponse}, or completes exceptionally if there is an error invoking
   *     the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.13.7">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.13.7</a>
   */
  public CompletableFuture<TransferSubscriptionsResponse> transferSubscriptionsAsync(
      List<UInteger> subscriptionIds, boolean sendInitialValues) {

    return getSessionAsync()
        .thenCompose(
            session -> {
              TransferSubscriptionsRequest request =
                  new TransferSubscriptionsRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      subscriptionIds.toArray(new UInteger[0]),
                      sendInitialValues);

              return sendRequestAsync(request).thenApply(TransferSubscriptionsResponse.class::cast);
            });
  }

  /**
   * Enable or disable the sending of notifications on one or more subscriptions.
   *
   * @param publishingEnabled {@code true} if publishing is enabled.
   * @param subscriptionIds a List of server-assigned identifiers for subscriptions.
   * @return the {@link SetPublishingModeResponse}.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.13.4">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.13.4</a>
   */
  public SetPublishingModeResponse setPublishingMode(
      boolean publishingEnabled, List<UInteger> subscriptionIds) throws UaException {

    try {
      CompletableFuture<SetPublishingModeResponse> future =
          setPublishingModeAsync(publishingEnabled, subscriptionIds);

      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Enable or disable the sending of notifications on one or more subscriptions.
   *
   * @param publishingEnabled {@code true} if publishing is enabled.
   * @param subscriptionIds a List of server-assigned identifiers for subscriptions.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     SetPublishingModeResponse}, or completes exceptionally if there is an error invoking the
   *     service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.13.4">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.13.4</a>
   */
  public CompletableFuture<SetPublishingModeResponse> setPublishingModeAsync(
      boolean publishingEnabled, List<UInteger> subscriptionIds) {

    return getSessionAsync()
        .thenCompose(
            session -> {
              SetPublishingModeRequest request =
                  new SetPublishingModeRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      publishingEnabled,
                      subscriptionIds.toArray(new UInteger[0]));

              return sendRequestAsync(request).thenApply(SetPublishingModeResponse.class::cast);
            });
  }

  /**
   * Send a {@link PublishRequest} to the server, acknowledging the receipt of previously-received
   * messages and requesting the server return a NotificationMessage or keep-alive message.
   *
   * @param subscriptionAcknowledgements a List of {@link SubscriptionAcknowledgement}s for one or
   *     more subscriptions.
   * @return the {@link PublishResponse}.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.13.5">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.13.5</a>
   */
  public PublishResponse publish(List<SubscriptionAcknowledgement> subscriptionAcknowledgements)
      throws UaException {

    try {
      CompletableFuture<PublishResponse> future = publishAsync(subscriptionAcknowledgements);

      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Send a {@link PublishRequest} to the server, acknowledging the receipt of previously-received
   * messages and requesting the server return a NotificationMessage or keep-alive message.
   *
   * @param subscriptionAcknowledgements a List of {@link SubscriptionAcknowledgement}s for one or
   *     more subscriptions.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     PublishResponse}, or completes exceptionally if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.13.5">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.13.5</a>
   */
  public CompletableFuture<PublishResponse> publishAsync(
      List<SubscriptionAcknowledgement> subscriptionAcknowledgements) {

    return getSessionAsync()
        .thenCompose(
            session -> {
              PublishRequest request =
                  new PublishRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      subscriptionAcknowledgements.toArray(new SubscriptionAcknowledgement[0]));

              return sendRequestAsync(request).thenApply(PublishResponse.class::cast);
            });
  }

  /**
   * Send a {@link RepublishRequest} to the server, requesting the server republish a
   * NotificationMessage from its retransmission queue.
   *
   * @param subscriptionId the server-assigned identifier for the Subscription.
   * @param retransmitSequenceNumber the sequence number of the NotificationMessage to be
   *     republished.
   * @return the {@link RepublishResponse}.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.13.6">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.13.6</a>
   */
  public RepublishResponse republish(UInteger subscriptionId, UInteger retransmitSequenceNumber)
      throws UaException {

    try {
      CompletableFuture<RepublishResponse> future =
          republishAsync(subscriptionId, retransmitSequenceNumber);

      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Send a {@link RepublishRequest} to the server, requesting the server republish a
   * NotificationMessage from its retransmission queue.
   *
   * @param subscriptionId the server-assigned identifier for the Subscription.
   * @param retransmitSequenceNumber the sequence number of the NotificationMessage to be
   *     republished.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     RepublishResponse}, or completes exceptionally if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.13.6">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.13.6</a>
   */
  public CompletableFuture<RepublishResponse> republishAsync(
      UInteger subscriptionId, UInteger retransmitSequenceNumber) {

    return getSessionAsync()
        .thenCompose(
            session -> {
              RepublishRequest request =
                  new RepublishRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      subscriptionId,
                      retransmitSequenceNumber);

              return sendRequestAsync(request).thenApply(RepublishResponse.class::cast);
            });
  }

  // endregion

  // region View Services

  /**
   * Browse references of a single Node.
   *
   * @param nodeToBrowse the Node to browse.
   * @return the {@link BrowseResult}.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.2">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.2</a>
   */
  public BrowseResult browse(BrowseDescription nodeToBrowse) throws UaException {
    try {
      return browseAsync(nodeToBrowse).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Browse references of one or more Nodes.
   *
   * @param nodesToBrowse the Nodes to browse.
   * @return the {@link BrowseResult}s.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.2">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.2</a>
   */
  public List<BrowseResult> browse(List<BrowseDescription> nodesToBrowse) throws UaException {
    try {
      return browseAsync(nodesToBrowse).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Browse references of one or more Nodes.
   *
   * @param viewDescription a description of the view to browse. An empty {@link ViewDescription}
   *     indicates a view of the entire address space.
   * @param maxReferencesPerNode the maximum number of references to return for each starting Node
   *     specified in the request.
   * @param nodesToBrowse a List of Nodes to browse.
   * @return the {@link BrowseResponse}.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.2">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.2</a>
   */
  public BrowseResponse browse(
      ViewDescription viewDescription,
      UInteger maxReferencesPerNode,
      List<BrowseDescription> nodesToBrowse)
      throws UaException {

    try {
      return browseAsync(viewDescription, maxReferencesPerNode, nodesToBrowse).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Browse references of one or more Nodes.
   *
   * @param viewDescription a description of the view to browse. An empty {@link ViewDescription}
   *     indicates a view of the entire address space.
   * @param maxReferencesPerNode the maximum number of references to return for each starting Node
   *     specified in the request.
   * @param nodesToBrowse a List of Nodes to browse.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     BrowseResponse}, or completes exceptionally if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.2">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.2</a>
   */
  public CompletableFuture<BrowseResponse> browseAsync(
      ViewDescription viewDescription,
      UInteger maxReferencesPerNode,
      List<BrowseDescription> nodesToBrowse) {

    return getSessionAsync()
        .thenCompose(
            session -> {
              BrowseRequest request =
                  new BrowseRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      viewDescription,
                      maxReferencesPerNode,
                      nodesToBrowse.toArray(new BrowseDescription[0]));

              return sendRequestAsync(request).thenApply(BrowseResponse.class::cast);
            });
  }

  /**
   * Browse a single node, with no view and no max references specified.
   *
   * @param nodeToBrowse the node to browse.
   * @return a {@link CompletableFuture} containing the {@link BrowseResult}.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.2">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.2</a>
   */
  public CompletableFuture<BrowseResult> browseAsync(BrowseDescription nodeToBrowse) {
    return browseAsync(List.of(nodeToBrowse)).thenApply(rs -> rs.get(0));
  }

  /**
   * Browse a list of nodes, with no view and no max references specified.
   *
   * @param nodesToBrowse the nodes to browse.
   * @return a {@link CompletableFuture} containing the {@link BrowseResult}s.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.2">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.2</a>
   */
  public CompletableFuture<List<BrowseResult>> browseAsync(List<BrowseDescription> nodesToBrowse) {
    var viewDescription = new ViewDescription(NodeId.NULL_VALUE, DateTime.MIN_VALUE, uint(0));

    return browseAsync(viewDescription, uint(0), nodesToBrowse)
        .thenApply(r -> Lists.ofNullable(r.getResults()));
  }

  /**
   * Request the next set of Browse or BrowseNext information that was too large to be sent in a
   * single response.
   *
   * @param releaseContinuationPoints if {@code true}, the continuation points are released to free
   *     resources in the server. If {@code false}, they are used to get the next set of browse
   *     information.
   * @param continuationPoints server-defined opaque values that represent continuation points.
   * @return the {@link BrowseNextResponse}.
   * @throws UaException if there is an error invoking the service.
   * @see #browse(ViewDescription, UInteger, List)
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.3">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.3</a>
   */
  public BrowseNextResponse browseNext(
      boolean releaseContinuationPoints, List<ByteString> continuationPoints) throws UaException {

    try {
      return browseNextAsync(releaseContinuationPoints, continuationPoints).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Request the next set of Browse or BrowseNext information that was too large to be sent in a
   * single response.
   *
   * @param releaseContinuationPoints if {@code true}, the continuation points are released to free
   *     resources in the server. If {@code false}, they are used to get the next set of browse
   *     information.
   * @param continuationPoints server-defined opaque values that represent continuation points.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     BrowseNextResponse}, or completes exceptionally if there is an error invoking the service.
   * @see #browseAsync(ViewDescription, UInteger, List)
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.3">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.3</a>
   */
  public CompletableFuture<BrowseNextResponse> browseNextAsync(
      boolean releaseContinuationPoints, List<ByteString> continuationPoints) {

    return getSessionAsync()
        .thenCompose(
            session -> {
              BrowseNextRequest request =
                  new BrowseNextRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      releaseContinuationPoints,
                      continuationPoints.toArray(new ByteString[0]));

              return sendRequestAsync(request).thenApply(BrowseNextResponse.class::cast);
            });
  }

  /**
   * Translate one or more {@link BrowsePath}s into {@link NodeId}s.
   *
   * @param browsePaths a List of {@link BrowsePath}s to translate.
   * @return the {@link TranslateBrowsePathsToNodeIdsResponse}.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.4">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.4</a>
   */
  public TranslateBrowsePathsToNodeIdsResponse translateBrowsePaths(List<BrowsePath> browsePaths)
      throws UaException {
    try {
      return translateBrowsePathsAsync(browsePaths).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Translate one or more {@link BrowsePath}s into {@link NodeId}s.
   *
   * @param browsePaths a List of {@link BrowsePath}s to translate.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     TranslateBrowsePathsToNodeIdsResponse}, or completes exceptionally if there is an error
   *     invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.4">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.4</a>
   */
  public CompletableFuture<TranslateBrowsePathsToNodeIdsResponse> translateBrowsePathsAsync(
      List<BrowsePath> browsePaths) {

    return getSessionAsync()
        .thenCompose(
            session -> {
              TranslateBrowsePathsToNodeIdsRequest request =
                  new TranslateBrowsePathsToNodeIdsRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      browsePaths.toArray(new BrowsePath[0]));

              return sendRequestAsync(request)
                  .thenApply(TranslateBrowsePathsToNodeIdsResponse.class::cast);
            });
  }

  /**
   * Register {@link NodeId}s that the client intends to access repeatedly (e.g. read, write, call),
   * allowing servers to set up anything needed so that the access operations are more efficient.
   *
   * @param nodesToRegister the {@link NodeId}s of the Nodes to register.
   * @return the {@link RegisterNodesResponse}.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.5">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.5</a>
   */
  public RegisterNodesResponse registerNodes(List<NodeId> nodesToRegister) throws UaException {
    try {
      return registerNodesAsync(nodesToRegister).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Register Nodes that the client intends to access repeatedly (e.g. read, write, call), allowing
   * servers to set up anything needed so that the access operations are more efficient.
   *
   * @param nodesToRegister the {@link NodeId}s of the Nodes to register.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     RegisterNodesResponse}, or completes exceptionally if there is an error invoking the
   *     service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.5">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.5</a>
   */
  public CompletableFuture<RegisterNodesResponse> registerNodesAsync(List<NodeId> nodesToRegister) {
    return getSessionAsync()
        .thenCompose(
            session -> {
              RegisterNodesRequest request =
                  new RegisterNodesRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      nodesToRegister.toArray(new NodeId[0]));

              return sendRequestAsync(request).thenApply(RegisterNodesResponse.class::cast);
            });
  }

  /**
   * Unregister Nodes that were previously registered with a Register Nodes service call.
   *
   * @param nodesToUnregister the {@link NodeId}s returned by the previous Register Nodes service
   *     call.
   * @return the {@link UnregisterNodesResponse}.
   * @throws UaException if there is an error invoking the service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.6">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.6</a>
   */
  public UnregisterNodesResponse unregisterNodes(List<NodeId> nodesToUnregister)
      throws UaException {
    try {
      return unregisterNodesAsync(nodesToUnregister).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Unregister Nodes that were previously registered with a Register Nodes service call.
   *
   * @param nodesToUnregister the {@link NodeId}s returned by the previous Register Nodes service
   *     call.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     UnregisterNodesResponse}, or completes exceptionally if there is an error invoking the
   *     service.
   * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.6">
   *     https://reference.opcfoundation.org/Core/Part4/v105/docs/5.8.6</a>
   */
  public CompletableFuture<UnregisterNodesResponse> unregisterNodesAsync(
      List<NodeId> nodesToUnregister) {
    return getSessionAsync()
        .thenCompose(
            session -> {
              UnregisterNodesRequest request =
                  new UnregisterNodesRequest(
                      newRequestHeader(session.getAuthenticationToken()),
                      nodesToUnregister.toArray(new NodeId[0]));

              return sendRequestAsync(request).thenApply(UnregisterNodesResponse.class::cast);
            });
  }

  // endregion

  /**
   * Send a service request and wait for the service response.
   *
   * @param request the service request to send.
   * @return the service response.
   * @throws UaException if there is an error invoking the service.
   */
  public UaResponseMessageType sendRequest(UaRequestMessageType request) throws UaException {
    try {
      return sendRequestAsync(request).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Send a service request asynchronously.
   *
   * @param request the service request to send.
   * @return a {@link CompletableFuture} that completes successfully with the service response, or
   *     completes exceptionally if there is an error invoking the service.
   */
  public CompletableFuture<UaResponseMessageType> sendRequestAsync(UaRequestMessageType request) {
    return transport.sendRequestMessage(request).whenComplete(this::notifyFaultListeners);
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
          () -> faultListeners.forEach(h -> h.onServiceFault(serviceFault)));
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

  public interface DataTypeManagerInitializer {

    /**
     * Register codecs for custom data types.
     *
     * @param namespaceTable the Server's {@link NamespaceTable}.
     * @param dataTypeTree the Client's {@link DataTypeTree}.
     * @param dataTypeManager the {@link DataTypeManager} to register codecs with.
     */
    void initialize(
        NamespaceTable namespaceTable, DataTypeTree dataTypeTree, DataTypeManager dataTypeManager)
        throws UaException;
  }

  public static class DefaultDataTypeManagerInitializer implements DataTypeManagerInitializer {

    private final CodecFactory codecFactory;

    /**
     * Create a new {@link DefaultDataTypeManagerInitializer} that uses {@link DynamicCodecFactory}.
     */
    public DefaultDataTypeManagerInitializer() {
      this(DynamicCodecFactory::create);
    }

    /**
     * Create a new {@link DefaultDataTypeManagerInitializer} using the provided {@link
     * CodecFactory}.
     *
     * @param codecFactory the {@link CodecFactory} to use when creating {@link DataTypeCodec}s.
     */
    public DefaultDataTypeManagerInitializer(CodecFactory codecFactory) {
      this.codecFactory = codecFactory;
    }

    @Override
    public void initialize(
        NamespaceTable namespaceTable, DataTypeTree dataTypeTree, DataTypeManager dataTypeManager)
        throws UaException {

      Tree<DataType> structureNode = dataTypeTree.getTreeNode(NodeIds.Structure);

      if (structureNode != null) {
        structureNode.traverse(
            dataType -> {
              if (dataType.getDataTypeDefinition() != null) {
                LoggerFactory.getLogger(getClass())
                    .debug(
                        "Registering type: name={}, dataTypeId={}",
                        dataType.getBrowseName(),
                        dataType.getNodeId());

                String namespaceUri = namespaceTable.get(dataType.getNodeId().getNamespaceIndex());

                if (namespaceUri == null) {
                  throw new UaRuntimeException(
                      StatusCodes.Bad_UnexpectedError,
                      "DataType namespace not registered: "
                          + dataType.getNodeId().toParseableString());
                }

                dataTypeManager.registerType(
                    dataType.getNodeId(),
                    codecFactory.create(dataType, dataTypeTree),
                    dataType.getBinaryEncodingId(),
                    dataType.getXmlEncodingId(),
                    dataType.getJsonEncodingId());
              }
            });
      } else {
        throw new UaException(
            StatusCodes.Bad_UnexpectedError,
            "tree for NodeIds.Structure not found; is the server DataType hierarchy sane?");
      }
    }
  }
}
