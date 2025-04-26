/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static java.util.stream.Collectors.toList;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import java.net.InetSocketAddress;
import java.security.KeyPair;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.sdk.server.diagnostics.ServerDiagnosticsSummary;
import org.eclipse.milo.opcua.sdk.server.model.ObjectTypeInitializer;
import org.eclipse.milo.opcua.sdk.server.model.VariableTypeInitializer;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.namespaces.OpcUaNamespace;
import org.eclipse.milo.opcua.sdk.server.namespaces.ServerNamespace;
import org.eclipse.milo.opcua.sdk.server.nodes.factories.EventFactory;
import org.eclipse.milo.opcua.sdk.server.servicesets.Service;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.AccessController;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultAccessController;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultAttributeServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultDiscoveryServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultMethodServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultMonitoredItemServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultNodeManagementServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultSessionServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultSubscriptionServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultViewServiceSet;
import org.eclipse.milo.opcua.sdk.server.subscriptions.Subscription;
import org.eclipse.milo.opcua.sdk.server.typetree.DataTypeTreeBuilder;
import org.eclipse.milo.opcua.sdk.server.typetree.ReferenceTypeTreeBuilder;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.ServerTable;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.channel.messages.ErrorMessage;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingManager;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingManager;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.DataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.DefaultDataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.eclipse.milo.opcua.stack.core.util.FutureUtils;
import org.eclipse.milo.opcua.stack.core.util.Lazy;
import org.eclipse.milo.opcua.stack.core.util.LongSequence;
import org.eclipse.milo.opcua.stack.core.util.ManifestUtil;
import org.eclipse.milo.opcua.stack.transport.server.OpcServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.OpcServerTransportFactory;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpcUaServer extends AbstractServiceHandler {

  public static final String SDK_VERSION = ManifestUtil.read("X-SDK-Version").orElse("dev");

  static {
    Logger logger = LoggerFactory.getLogger(OpcUaServer.class);
    logger.info("Java version: {}", System.getProperty("java.version"));
    logger.info("Eclipse Milo OPC UA Stack version: {}", Stack.VERSION);
    logger.info("Eclipse Milo OPC UA Server SDK version: {}", SDK_VERSION);
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final Lazy<ApplicationDescription> applicationDescription = new Lazy<>();

  private final Map<UInteger, Subscription> subscriptions = new ConcurrentHashMap<>();
  private final AtomicLong monitoredItemCount = new AtomicLong(0L);

  private final NamespaceTable namespaceTable = new NamespaceTable();
  private final ServerTable serverTable = new ServerTable();

  private final AddressSpaceManager addressSpaceManager = new AddressSpaceManager(this);
  private final SessionManager sessionManager = new SessionManager(this);

  private final EncodingManager encodingManager = DefaultEncodingManager.createAndInitialize();

  private final ObjectTypeManager objectTypeManager = new ObjectTypeManager();
  private final VariableTypeManager variableTypeManager = new VariableTypeManager();

  private final Lazy<DataTypeTree> dataTypeTree = new Lazy<>();
  private final Lazy<ReferenceTypeTree> referenceTypeTree = new Lazy<>();

  private final DataTypeManager staticDataTypeManager =
      DefaultDataTypeManager.createAndInitialize(namespaceTable);

  private final DataTypeManager dynamicDataTypeManager =
      DefaultDataTypeManager.createAndInitialize(namespaceTable);

  private final Set<NodeId> registeredViews = Sets.newConcurrentHashSet();

  private final ServerDiagnosticsSummary diagnosticsSummary = new ServerDiagnosticsSummary(this);

  private final List<EndpointConfig> boundEndpoints = new CopyOnWriteArrayList<>();

  /**
   * SecureChannel id sequence, starting at a random value in [1..{@link Integer#MAX_VALUE}], and
   * wrapping back to 1 after {@link UInteger#MAX_VALUE}.
   */
  private final LongSequence secureChannelIds =
      new LongSequence(1L, UInteger.MAX_VALUE, new Random().nextInt(Integer.MAX_VALUE - 1) + 1);

  private final AtomicLong secureChannelTokenIds = new AtomicLong();

  private final Map<TransportProfile, OpcServerTransport> transports = new ConcurrentHashMap<>();

  private final EventBus eventBus = new EventBus("server");
  private final EventFactory eventFactory = new EventFactory(this);
  private final EventNotifier eventNotifier = new ServerEventNotifier();

  private final EncodingContext staticEncodingContext;
  private final EncodingContext dynamicEncodingContext;

  private final OpcUaNamespace opcUaNamespace;
  private final ServerNamespace serverNamespace;

  private final AccessController accessController;

  private final OpcUaServerConfig config;
  private final OpcServerTransportFactory transportFactory;
  private final ServerApplicationContext applicationContext;

  public OpcUaServer(OpcUaServerConfig config, OpcServerTransportFactory transportFactory) {
    this.config = config;
    this.transportFactory = transportFactory;

    applicationContext = new ServerApplicationContextImpl();

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

    dynamicEncodingContext =
        new EncodingContext() {
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

    Stream<String> paths =
        config.getEndpoints().stream()
            .map(e -> EndpointUtil.getPath(e.getEndpointUrl()))
            .distinct();

    paths.forEach(
        path -> {
          addServiceSet(path, new DefaultDiscoveryServiceSet(OpcUaServer.this));

          if (!path.endsWith("/discovery")) {
            addServiceSet(path, new DefaultAttributeServiceSet(OpcUaServer.this));
            addServiceSet(path, new DefaultMethodServiceSet(OpcUaServer.this));
            addServiceSet(path, new DefaultMonitoredItemServiceSet(OpcUaServer.this));
            addServiceSet(path, new DefaultNodeManagementServiceSet(OpcUaServer.this));
            addServiceSet(path, new DefaultSessionServiceSet(OpcUaServer.this));
            addServiceSet(path, new DefaultSubscriptionServiceSet(OpcUaServer.this));
            addServiceSet(path, new DefaultViewServiceSet(OpcUaServer.this));
          }
        });

    ObjectTypeInitializer.initialize(namespaceTable, objectTypeManager);

    VariableTypeInitializer.initialize(namespaceTable, variableTypeManager);

    serverTable.add(config.getApplicationUri());

    opcUaNamespace = new OpcUaNamespace(this);
    opcUaNamespace.startup();

    serverNamespace = new ServerNamespace(this);
    serverNamespace.startup();

    accessController = new DefaultAccessController(this);
  }

  public CompletableFuture<OpcUaServer> startup() {
    eventFactory.startup();

    config.getEndpoints().stream()
        .sorted(Comparator.comparing(EndpointConfig::getTransportProfile))
        .forEach(
            endpoint -> {
              logger.info(
                  "Binding endpoint {} to {}:{} [{}/{}]",
                  endpoint.getEndpointUrl(),
                  endpoint.getBindAddress(),
                  endpoint.getBindPort(),
                  endpoint.getSecurityPolicy(),
                  endpoint.getSecurityMode());

              TransportProfile transportProfile = endpoint.getTransportProfile();

              OpcServerTransport transport =
                  transports.computeIfAbsent(transportProfile, transportFactory::create);

              if (transport != null) {
                try {
                  var bindAddress =
                      new InetSocketAddress(endpoint.getBindAddress(), endpoint.getBindPort());
                  transport.bind(applicationContext, bindAddress);

                  transports.put(transportProfile, transport);

                  boundEndpoints.add(endpoint);
                } catch (Exception e) {
                  logger.warn(
                      "Failed to bind endpoint {} to {}:{} [{}/{}]",
                      endpoint.getEndpointUrl(),
                      endpoint.getBindAddress(),
                      endpoint.getBindPort(),
                      endpoint.getSecurityPolicy(),
                      endpoint.getSecurityMode(),
                      e);
                }
              } else {
                logger.warn("No OpcServerTransport for TransportProfile: {}", transportProfile);
              }
            });

    if (boundEndpoints.isEmpty()) {
      return CompletableFuture.failedFuture(
          new UaException(StatusCodes.Bad_ConfigurationError, "No endpoints bound"));
    } else {
      return CompletableFuture.completedFuture(this);
    }
  }

  public CompletableFuture<OpcUaServer> shutdown() {
    transports
        .values()
        .forEach(
            transport -> {
              try {
                transport.unbind();
              } catch (Exception e) {
                logger.warn("Error unbinding transport", e);
              }
            });
    transports.clear();

    serverNamespace.shutdown();
    opcUaNamespace.shutdown();

    eventFactory.shutdown();

    subscriptions.values().forEach(Subscription::deleteSubscription);

    return CompletableFuture.completedFuture(this);
  }

  public OpcUaServerConfig getConfig() {
    return config;
  }

  public AccessController getAccessController() {
    return accessController;
  }

  public ServerApplicationContext getApplicationContext() {
    return applicationContext;
  }

  public AddressSpaceManager getAddressSpaceManager() {
    return addressSpaceManager;
  }

  public SessionManager getSessionManager() {
    return sessionManager;
  }

  public OpcUaNamespace getOpcUaNamespace() {
    return opcUaNamespace;
  }

  public ServerNamespace getServerNamespace() {
    return serverNamespace;
  }

  public EncodingManager getEncodingManager() {
    return encodingManager;
  }

  public DataTypeManager getStaticDataTypeManager() {
    return staticDataTypeManager;
  }

  public DataTypeManager getDynamicDataTypeManager() {
    return dynamicDataTypeManager;
  }

  public EncodingContext getStaticEncodingContext() {
    return staticEncodingContext;
  }

  public EncodingContext getDynamicEncodingContext() {
    return dynamicEncodingContext;
  }

  public NamespaceTable getNamespaceTable() {
    return namespaceTable;
  }

  public ServerTable getServerTable() {
    return serverTable;
  }

  public ServerDiagnosticsSummary getDiagnosticsSummary() {
    return diagnosticsSummary;
  }

  /**
   * Get an internal EventBus used to decouple communication between internal components of the
   * Server implementation.
   *
   * <p>This EventBus is not intended for use by user implementations.
   *
   * @return an internal EventBus used to decouple communication between internal components of the
   *     Server implementation.
   */
  public EventBus getInternalEventBus() {
    return eventBus;
  }

  /**
   * Get the shared {@link EventFactory}.
   *
   * @return the shared {@link EventFactory}.
   */
  public EventFactory getEventFactory() {
    return eventFactory;
  }

  /**
   * Get the Server's {@link EventNotifier}.
   *
   * @return the Server's {@link EventNotifier}.
   */
  public EventNotifier getEventNotifier() {
    return eventNotifier;
  }

  public ObjectTypeManager getObjectTypeManager() {
    return objectTypeManager;
  }

  public VariableTypeManager getVariableTypeManager() {
    return variableTypeManager;
  }

  /**
   * Get the Server's {@link DataTypeTree}.
   *
   * @return the Server's {@link DataTypeTree}.
   */
  public DataTypeTree getDataTypeTree() {
    return dataTypeTree.get(() -> DataTypeTreeBuilder.build(this));
  }

  /**
   * Re-build and return the Server's {@link DataTypeTree}.
   *
   * @return the re-built {@link DataTypeTree}.
   */
  public DataTypeTree updateDataTypeTree() {
    dataTypeTree.reset();

    return getDataTypeTree();
  }

  /**
   * Get the Server's {@link ReferenceTypeTree}.
   *
   * @return the Server's {@link ReferenceTypeTree}.
   */
  public ReferenceTypeTree getReferenceTypeTree() {
    return referenceTypeTree.get(() -> ReferenceTypeTreeBuilder.build(this));
  }

  /**
   * Re-build and return the Server's {@link ReferenceTypeTree}.
   *
   * @return the re-built {@link ReferenceTypeTree}.
   */
  public ReferenceTypeTree updateReferenceTypeTree() {
    referenceTypeTree.reset();

    return getReferenceTypeTree();
  }

  public Set<NodeId> getRegisteredViews() {
    return registeredViews;
  }

  public Map<UInteger, Subscription> getSubscriptions() {
    return subscriptions;
  }

  public AtomicLong getMonitoredItemCount() {
    return monitoredItemCount;
  }

  public Optional<KeyPair> getKeyPair(ByteString thumbprint) {
    return config.getCertificateManager().getKeyPair(thumbprint);
  }

  public Optional<X509Certificate> getCertificate(ByteString thumbprint) {
    return config.getCertificateManager().getCertificate(thumbprint);
  }

  public Optional<X509Certificate[]> getCertificateChain(ByteString thumbprint) {
    return config.getCertificateManager().getCertificateChain(thumbprint);
  }

  public ExecutorService getExecutorService() {
    return config.getExecutor();
  }

  public ScheduledExecutorService getScheduledExecutorService() {
    return config.getScheduledExecutorService();
  }

  public Optional<RoleMapper> getRoleMapper() {
    return config.getRoleMapper();
  }

  /**
   * Get the {@link EndpointConfig}s that were successfully bound during {@link #startup()}.
   *
   * @return the {@link EndpointConfig}s that were successfully bound during {@link #startup()}.
   */
  public List<EndpointConfig> getBoundEndpoints() {
    return List.copyOf(boundEndpoints);
  }

  private class ServerApplicationContextImpl implements ServerApplicationContext {

    @Override
    public List<EndpointDescription> getEndpointDescriptions() {
      return config.getEndpoints().stream().map(this::transformEndpoint).toList();
    }

    @Override
    public EncodingContext getEncodingContext() {
      return staticEncodingContext;
    }

    @Override
    public CertificateManager getCertificateManager() {
      return config.getCertificateManager();
    }

    @Override
    public Long getNextSecureChannelId() {
      return secureChannelIds.getAndIncrement();
    }

    @Override
    public Long getNextSecureChannelTokenId() {
      return secureChannelTokenIds.getAndIncrement();
    }

    @Override
    public CompletableFuture<UaResponseMessageType> handleServiceRequest(
        ServiceRequestContext context, UaRequestMessageType requestMessage) {

      var future = new CompletableFuture<UaResponseMessageType>();

      getExecutorService().execute(() -> handleServiceRequest(context, requestMessage, future));

      return future;
    }

    private void handleServiceRequest(
        ServiceRequestContext context,
        UaRequestMessageType requestMessage,
        CompletableFuture<UaResponseMessageType> future) {

      String path = EndpointUtil.getPath(context.getEndpointUrl());

      if (context.getSecureChannel().getSecurityPolicy() == SecurityPolicy.None) {
        if (getEndpointDescriptions().stream()
            .filter(e -> EndpointUtil.getPath(e.getEndpointUrl()).equals(path))
            .filter(
                e ->
                    Objects.equals(
                        e.getTransportProfileUri(), context.getTransportProfile().getUri()))
            .noneMatch(
                e -> Objects.equals(e.getSecurityPolicyUri(), SecurityPolicy.None.getUri()))) {

          if (!isDiscoveryService(requestMessage)) {
            var errorMessage =
                new ErrorMessage(
                    StatusCodes.Bad_SecurityPolicyRejected,
                    StatusCodes.lookup(StatusCodes.Bad_SecurityPolicyRejected)
                        .map(ss -> ss[1])
                        .orElse(""));

            context.getChannel().pipeline().fireUserEventTriggered(errorMessage);

            future.completeExceptionally(new UaException(StatusCodes.Bad_SecurityPolicyRejected));
            return;
          }
        }
      }

      Service service = Service.from(requestMessage.getTypeId());
      ServiceHandler serviceHandler = service != null ? getServiceHandler(path, service) : null;

      if (serviceHandler != null) {
        if (logger.isTraceEnabled()) {
          logger.trace(
              "Service request received: path={} handle={} service={} remote={}",
              path,
              requestMessage.getRequestHeader().getRequestHandle(),
              service,
              context.getChannel().remoteAddress());
        }

        if (serviceHandler instanceof AsyncServiceHandler asyncServiceHandler) {
          CompletableFuture<UaResponseMessageType> response =
              asyncServiceHandler
                  .handleAsync(context, requestMessage)
                  .whenComplete(
                      (r, ex) -> {
                        if (ex != null) {
                          logger.debug(
                              "Service request completed exceptionally: path={} handle={}"
                                  + " service={} remote={}",
                              path,
                              requestMessage.getRequestHeader().getRequestHandle(),
                              service,
                              context.getChannel().remoteAddress(),
                              ex);
                        } else {
                          if (logger.isTraceEnabled()) {
                            logger.trace(
                                "Service request completed: path={} handle={} service={} remote={}",
                                path,
                                requestMessage.getRequestHeader().getRequestHandle(),
                                service,
                                context.getChannel().remoteAddress());
                          }
                        }
                      });

          FutureUtils.complete(future).with(response);
        } else {
          try {
            UaResponseMessageType response = serviceHandler.handle(context, requestMessage);

            if (logger.isTraceEnabled()) {
              logger.trace(
                  "Service request completed: path={} handle={} service={} remote={}",
                  path,
                  requestMessage.getRequestHeader().getRequestHandle(),
                  service,
                  context.getChannel().remoteAddress());
            }

            future.complete(response);
          } catch (UaException e) {
            logger.debug(
                "Service request completed exceptionally: path={} handle={} service={} remote={}",
                path,
                requestMessage.getRequestHeader().getRequestHandle(),
                service,
                context.getChannel().remoteAddress(),
                e);

            future.completeExceptionally(e);
          }
        }
      } else {
        logger.warn("No ServiceHandler registered for path={} service={}", path, service);

        future.completeExceptionally(new UaException(StatusCodes.Bad_NotImplemented));
      }
    }

    /**
     * Return {@code true} if {@code requestMessage} is one of the Discovery service requests:
     *
     * <ul>
     *   <li>FindServersRequest
     *   <li>GetEndpointsRequest
     *   <li>RegisterServerRequest
     *   <li>FindServersOnNetworkRequest
     *   <li>RegisterServer2Request
     * </ul>
     *
     * @param requestMessage the {@link UaRequestMessageType} to check.
     * @return {@code true} if {@code requestMessage} is one of the Discovery service requests.
     */
    private boolean isDiscoveryService(UaRequestMessageType requestMessage) {
      Service service = Service.from(requestMessage.getTypeId());

      if (service != null) {
        return switch (service) {
          case DISCOVERY_FIND_SERVERS,
              DISCOVERY_GET_ENDPOINTS,
              DISCOVERY_REGISTER_SERVER,
              DISCOVERY_FIND_SERVERS_ON_NETWORK,
              DISCOVERY_REGISTER_SERVER_2 ->
              true;
          default -> false;
        };
      }

      return false;
    }

    private EndpointDescription transformEndpoint(EndpointConfig endpoint) {
      return new EndpointDescription(
          endpoint.getEndpointUrl(),
          getApplicationDescription(),
          certificateByteString(endpoint.getCertificate()),
          endpoint.getSecurityMode(),
          endpoint.getSecurityPolicy().getUri(),
          endpoint.getTokenPolicies().toArray(new UserTokenPolicy[0]),
          endpoint.getTransportProfile().getUri(),
          ubyte(getSecurityLevel(endpoint.getSecurityPolicy(), endpoint.getSecurityMode())));
    }

    private ByteString certificateByteString(@Nullable X509Certificate certificate) {
      if (certificate != null) {
        try {
          return ByteString.of(certificate.getEncoded());
        } catch (CertificateEncodingException e) {
          logger.error("Error decoding certificate.", e);
          return ByteString.NULL_VALUE;
        }
      } else {
        return ByteString.NULL_VALUE;
      }
    }

    private ApplicationDescription getApplicationDescription() {
      return applicationDescription.get(
          () -> {
            List<String> discoveryUrls =
                config.getEndpoints().stream()
                    .map(EndpointConfig::getEndpointUrl)
                    .filter(url -> url.endsWith("/discovery"))
                    .distinct()
                    .collect(toList());

            if (discoveryUrls.isEmpty()) {
              discoveryUrls =
                  config.getEndpoints().stream()
                      .map(EndpointConfig::getEndpointUrl)
                      .distinct()
                      .toList();
            }

            return new ApplicationDescription(
                config.getApplicationUri(),
                config.getProductUri(),
                config.getApplicationName(),
                ApplicationType.Server,
                null,
                null,
                discoveryUrls.toArray(new String[0]));
          });
    }

    private short getSecurityLevel(
        SecurityPolicy securityPolicy, MessageSecurityMode securityMode) {
      short securityLevel = 0;

      switch (securityPolicy) {
        case Aes256_Sha256_RsaPss:
        case Basic256Sha256:
          securityLevel |= 0x08;
          break;
        case Aes128_Sha256_RsaOaep:
          securityLevel |= 0x04;
          break;
        case Basic256:
        case Basic128Rsa15:
          securityLevel |= 0x01;
          break;
        case None:
        default:
          break;
      }

      switch (securityMode) {
        case SignAndEncrypt:
          securityLevel |= 0x80;
          break;
        case Sign:
          securityLevel |= 0x40;
          break;
        default:
          securityLevel |= 0x20;
          break;
      }

      return securityLevel;
    }
  }

  private static class ServerEventNotifier implements EventNotifier {

    private final List<EventListener> eventListeners =
        Collections.synchronizedList(new ArrayList<>());

    @Override
    public void fire(BaseEventTypeNode event) {
      List<EventListener> toNotify;
      synchronized (eventListeners) {
        toNotify = List.copyOf(eventListeners);
      }

      toNotify.forEach(eventListener -> eventListener.onEvent(event));
    }

    @Override
    public void register(EventListener eventListener) {
      eventListeners.add(eventListener);
    }

    @Override
    public void unregister(EventListener eventListener) {
      eventListeners.remove(eventListener);
    }
  }
}
