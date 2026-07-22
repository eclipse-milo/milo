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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import java.net.InetSocketAddress;
import java.security.KeyPair;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.sdk.core.typetree.ObjectTypeTree;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.sdk.core.typetree.VariableTypeTree;
import org.eclipse.milo.opcua.sdk.server.diagnostics.ServerDiagnosticsSummary;
import org.eclipse.milo.opcua.sdk.server.model.ObjectTypeInitializer;
import org.eclipse.milo.opcua.sdk.server.model.VariableTypeInitializer;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.namespaces.OpcUaNamespace;
import org.eclipse.milo.opcua.sdk.server.namespaces.ServerNamespace;
import org.eclipse.milo.opcua.sdk.server.nodes.factories.EventFactory;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTarget;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTargetHandle;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTargetListener;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTargetManager;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTargetSnapshot;
import org.eclipse.milo.opcua.sdk.server.servicesets.AttributeServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.DiscoveryServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.MethodServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.MonitoredItemServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.NodeManagementServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.QueryServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.Service;
import org.eclipse.milo.opcua.sdk.server.servicesets.SessionServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.SubscriptionServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.ViewServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.AccessController;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultAccessController;
import org.eclipse.milo.opcua.sdk.server.subscriptions.Subscription;
import org.eclipse.milo.opcua.sdk.server.typetree.DataTypeTreeBuilder;
import org.eclipse.milo.opcua.sdk.server.typetree.ObjectTypeTreeBuilder;
import org.eclipse.milo.opcua.sdk.server.typetree.ReferenceTypeTreeBuilder;
import org.eclipse.milo.opcua.sdk.server.typetree.VariableTypeTreeBuilder;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.ServerTable;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.channel.SecurityKeysListener;
import org.eclipse.milo.opcua.stack.core.channel.messages.ErrorMessage;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingManager;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingManager;
import org.eclipse.milo.opcua.stack.core.security.CertificateCompatibility;
import org.eclipse.milo.opcua.stack.core.security.CertificateIdentity;
import org.eclipse.milo.opcua.stack.core.security.CertificateIdentitySelectionContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateIdentitySelector;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateIdentitySelector;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfiles;
import org.eclipse.milo.opcua.stack.core.security.SecurityProviderResolver;
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
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
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

  private final Map<UInteger, Subscription> subscriptions = new ConcurrentHashMap<>();
  private final AtomicLong monitoredItemCount = new AtomicLong(0L);

  private final NamespaceTable namespaceTable = new NamespaceTable();
  private final ServerTable serverTable = new ServerTable();

  private final AddressSpaceManager addressSpaceManager = new AddressSpaceManager(this);
  private final SessionManager sessionManager;

  private final EncodingManager encodingManager = DefaultEncodingManager.createAndInitialize();

  private final ObjectTypeManager objectTypeManager = new ObjectTypeManager();
  private final VariableTypeManager variableTypeManager = new VariableTypeManager();

  private final Lazy<DataTypeTree> dataTypeTree = new Lazy<>();
  private final Lazy<ObjectTypeTree> objectTypeTree = new Lazy<>();
  private final Lazy<ReferenceTypeTree> referenceTypeTree = new Lazy<>();
  private final Lazy<VariableTypeTree> variableTypeTree = new Lazy<>();

  private final DataTypeManager staticDataTypeManager =
      DefaultDataTypeManager.createAndInitialize(namespaceTable);

  private final DataTypeManager dynamicDataTypeManager =
      DefaultDataTypeManager.createAndInitialize(namespaceTable);

  private final Set<NodeId> registeredViews = Sets.newConcurrentHashSet();

  private final ServerDiagnosticsSummary diagnosticsSummary = new ServerDiagnosticsSummary(this);

  private final Lazy<List<ResolvedEndpoint>> resolvedEndpoints = new Lazy<>();

  private final List<EndpointConfig> boundEndpoints = new CopyOnWriteArrayList<>();
  private final CertificateIdentitySelector endpointCertificateIdentitySelector =
      DefaultCertificateIdentitySelector.create();
  private final SecurityProviderResolver securityProviderResolver =
      SecurityProviderResolver.create();

  /**
   * SecureChannel id sequence, starting at a random value in [1..{@link Integer#MAX_VALUE}], and
   * wrapping back to 1 after {@link UInteger#MAX_VALUE}.
   */
  private final LongSequence secureChannelIds =
      new LongSequence(1L, UInteger.MAX_VALUE, new Random().nextInt(Integer.MAX_VALUE - 1) + 1);

  private final AtomicLong secureChannelTokenIds = new AtomicLong();

  private final Map<TransportProfile, OpcServerTransport> transports = new ConcurrentHashMap<>();

  /**
   * Shared shutdown result for the terminal server shutdown.
   *
   * <p>Shutdown tears down diagnostics and namespace state that cannot be safely torn down twice,
   * so concurrent callers must observe the same operation instead of each running teardown logic.
   */
  private final AtomicReference<CompletableFuture<OpcUaServer>> shutdownFuture =
      new AtomicReference<>();

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
  private final ReverseConnectTargetManager reverseConnectTargetManager;

  public OpcUaServer(OpcUaServerConfig config, OpcServerTransportFactory transportFactory) {
    this(config, transportFactory, new ServiceSets() {});
  }

  /**
   * Create an OpcUaServer using the service set implementations supplied by {@code serviceSets}.
   *
   * @param config the {@link OpcUaServerConfig}.
   * @param transportFactory the {@link OpcServerTransportFactory}.
   * @param serviceSets the {@link ServiceSets} supplying the service set implementations this
   *     server uses.
   */
  public OpcUaServer(
      OpcUaServerConfig config,
      OpcServerTransportFactory transportFactory,
      ServiceSets serviceSets) {

    this.config = config;
    this.transportFactory = transportFactory;

    applicationContext = new ServerApplicationContextImpl();
    reverseConnectTargetManager =
        new ReverseConnectTargetManager(
            applicationContext,
            applicationContext::getEndpointDescriptions,
            // Use a non-mutating lookup so target validation does not eagerly install transports
            // into the server's cache. Transports are populated by getOrCreateTransport during
            // endpoint binding, before the reverse-connect manager starts scheduling attempts.
            transports::get,
            config.getApplicationUri(),
            config.getExecutor(),
            config.getScheduledExecutorService(),
            config.getReverseConnectTargets());

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

    DiscoveryServiceSet discoveryServiceSet = serviceSets.createDiscoveryServiceSet(this);
    AttributeServiceSet attributeServiceSet = serviceSets.createAttributeServiceSet(this);
    MethodServiceSet methodServiceSet = serviceSets.createMethodServiceSet(this);
    MonitoredItemServiceSet monitoredItemServiceSet =
        serviceSets.createMonitoredItemServiceSet(this);
    NodeManagementServiceSet nodeManagementServiceSet =
        serviceSets.createNodeManagementServiceSet(this);
    QueryServiceSet queryServiceSet = serviceSets.createQueryServiceSet(this);
    SessionServiceSet sessionServiceSet = serviceSets.createSessionServiceSet(this);
    SubscriptionServiceSet subscriptionServiceSet = serviceSets.createSubscriptionServiceSet(this);
    ViewServiceSet viewServiceSet = serviceSets.createViewServiceSet(this);

    paths.forEach(
        path -> {
          addServiceSet(path, discoveryServiceSet);

          if (!path.endsWith("/discovery")) {
            addServiceSet(path, attributeServiceSet);
            addServiceSet(path, methodServiceSet);
            addServiceSet(path, monitoredItemServiceSet);
            addServiceSet(path, nodeManagementServiceSet);
            addServiceSet(path, queryServiceSet);
            addServiceSet(path, sessionServiceSet);
            addServiceSet(path, subscriptionServiceSet);
            addServiceSet(path, viewServiceSet);
          }
        });

    ObjectTypeInitializer.initialize(namespaceTable, objectTypeManager);

    VariableTypeInitializer.initialize(namespaceTable, variableTypeManager);

    serverTable.add(config.getApplicationUri());

    sessionManager = new SessionManager(this, config.getExecutor());

    opcUaNamespace = new OpcUaNamespace(this);
    opcUaNamespace.startup();

    serverNamespace = new ServerNamespace(this);
    serverNamespace.startup();

    accessController = new DefaultAccessController(this);
  }

  public CompletableFuture<OpcUaServer> startup() {
    eventFactory.startup();

    getResolvedEndpoints().stream()
        .sorted(Comparator.comparing(e -> e.endpointConfig().getTransportProfile()))
        .forEach(
            resolvedEndpoint -> {
              EndpointConfig endpoint = resolvedEndpoint.endpointConfig();

              logger.info(
                  "Binding endpoint {} to {}:{} [{}/{}]",
                  endpoint.getEndpointUrl(),
                  endpoint.getBindAddress(),
                  endpoint.getBindPort(),
                  endpoint.getSecurityPolicy(),
                  endpoint.getSecurityMode());

              TransportProfile transportProfile = endpoint.getTransportProfile();

              OpcServerTransport transport = getOrCreateTransport(transportProfile);

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

    try {
      // Validate after binding so the reverse-connect transport lookup finds the bound transport
      // without having to create one as a side effect.
      reverseConnectTargetManager.validateTargets();
    } catch (Throwable t) {
      rollbackStartup();
      return CompletableFuture.failedFuture(t);
    }

    try {
      reverseConnectTargetManager.startup();
    } catch (Throwable t) {
      rollbackStartup();
      return CompletableFuture.failedFuture(t);
    }

    if (boundEndpoints.isEmpty() && !reverseConnectTargetManager.hasSchedulableTargets()) {
      rollbackStartup();
      return CompletableFuture.failedFuture(
          new UaException(StatusCodes.Bad_ConfigurationError, "No endpoints bound"));
    } else {
      return CompletableFuture.completedFuture(this);
    }
  }

  /**
   * Stop accepting new sessions and tear down the server runtime.
   *
   * <p>This method is the synchronization point for server shutdown. The first caller performs the
   * shutdown sequence: reject new sessions, unbind transports, drain session listener work, close
   * sessions, then shut down namespaces, diagnostics, events, and subscriptions. Concurrent callers
   * receive the same {@link CompletableFuture} so namespace and diagnostics lifecycle code is only
   * run once.
   *
   * <p>If shutdown is requested from a session listener callback, the shutdown path avoids waiting
   * on the callback that is currently executing. When another caller is already waiting for
   * listener quiescence, this method returns a completed future for that callback and the outer
   * shutdown caller continues the real teardown after the callback returns.
   *
   * @return a future completed when the server shutdown sequence has finished.
   */
  public CompletableFuture<OpcUaServer> shutdown() {
    sessionManager.beginShutdown();

    CompletableFuture<OpcUaServer> newShutdownFuture = new CompletableFuture<>();
    if (!shutdownFuture.compareAndSet(null, newShutdownFuture)) {
      CompletableFuture<OpcUaServer> existingShutdownFuture = shutdownFuture.get();
      if (sessionManager.isSessionListenerCallback() && !existingShutdownFuture.isDone()) {
        // The active shutdown is waiting for this callback to return; joining it here would
        // deadlock the listener queue.
        return CompletableFuture.completedFuture(this);
      } else {
        return existingShutdownFuture;
      }
    }

    try {
      shutdownInternal();
      newShutdownFuture.complete(this);
    } catch (Exception e) {
      newShutdownFuture.completeExceptionally(e);
    }

    return newShutdownFuture;
  }

  private void shutdownInternal() {
    reverseConnectTargetManager.shutdown();

    unbindTransports();

    sessionManager.shutdown();

    serverNamespace.shutdown();
    opcUaNamespace.shutdown();

    eventFactory.shutdown();

    subscriptions.values().forEach(Subscription::deleteSubscription);
  }

  private void rollbackStartup() {
    reverseConnectTargetManager.shutdown();
    unbindTransports();
    eventFactory.shutdown();
  }

  private void unbindTransports() {
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
    boundEndpoints.clear();
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
   * Get the Server's {@link ObjectTypeTree}.
   *
   * @return the Server's {@link ObjectTypeTree}.
   */
  public ObjectTypeTree getObjectTypeTree() {
    return objectTypeTree.get(() -> ObjectTypeTreeBuilder.build(this));
  }

  /**
   * Re-build and return the Server's {@link ObjectTypeTree}.
   *
   * @return the re-built {@link ObjectTypeTree}.
   */
  public ObjectTypeTree updateObjectTypeTree() {
    objectTypeTree.reset();

    return getObjectTypeTree();
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

  /**
   * Get the Server's {@link VariableTypeTree}.
   *
   * @return the Server's {@link VariableTypeTree}.
   */
  public VariableTypeTree getVariableTypeTree() {
    return variableTypeTree.get(() -> VariableTypeTreeBuilder.build(this));
  }

  /**
   * Re-build and return the Server's {@link VariableTypeTree}.
   *
   * @return the re-built {@link VariableTypeTree}.
   */
  public VariableTypeTree updateVariableTypeTree() {
    variableTypeTree.reset();

    return getVariableTypeTree();
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
   * Add a server-managed Reverse Connect target at runtime.
   *
   * <p>If this server is already running and the target is enabled and not paused, the target is
   * validated against the configured {@code opc.tcp} endpoints and scheduled immediately. The
   * returned handle can pause, resume, trigger, remove, or inspect the target after it has been
   * registered.
   *
   * <pre>{@code
   * ReverseConnectTarget target =
   *     ReverseConnectTarget.builder()
   *         .setClientListenerUrl("opc.tcp://client.example.com:48060")
   *         .setEndpointUrl("opc.tcp://server.example.com:12686/milo")
   *         .setRegistrationPeriod(uint(1_000))
   *         .setConnectTimeout(uint(5_000))
   *         .build();
   *
   * ReverseConnectTargetHandle handle = server.addReverseConnectTarget(target);
   * try {
   *   handle.trigger().get();
   * } finally {
   *   handle.remove().get();
   * }
   * }</pre>
   *
   * @param target the immutable target configuration to register.
   * @return a runtime handle for the registered target.
   * @throws IllegalArgumentException if another target with the same id is already registered or
   *     the running server cannot use the target endpoint.
   * @throws IllegalStateException if the server's reverse target manager has already shut down.
   */
  public ReverseConnectTargetHandle addReverseConnectTarget(ReverseConnectTarget target) {
    return reverseConnectTargetManager.addTarget(target);
  }

  /**
   * Replace an existing server-managed Reverse Connect target.
   *
   * <p>The replacement keeps the same target id, cancels scheduled work and any in-flight attempt
   * owned by the previous target configuration, and applies the new enabled/paused state for future
   * scheduling. Active reverse-opened channels already handed to the server path remain open.
   *
   * @param target the replacement target configuration.
   * @return a completed future containing the updated target snapshot, or a failed future if the
   *     target id is not registered, the running server cannot use the replacement endpoint, or the
   *     server's reverse target manager has already shut down.
   */
  public CompletableFuture<ReverseConnectTargetSnapshot> updateReverseConnectTarget(
      ReverseConnectTarget target) {

    return reverseConnectTargetManager.update(target);
  }

  /**
   * Remove a server-managed Reverse Connect target and close resources it owns.
   *
   * <p>Removal cancels any scheduled attempt, closes any in-flight attempt, and closes any active
   * reverse-opened channels associated with the target.
   *
   * @param targetId the target id to remove.
   * @return a completed future containing the final snapshot for the removed target, or a failed
   *     future if the target id is not registered.
   */
  public CompletableFuture<ReverseConnectTargetSnapshot> removeReverseConnectTarget(UUID targetId) {
    return reverseConnectTargetManager.remove(targetId);
  }

  /**
   * Get immutable runtime snapshots for all server-managed Reverse Connect targets.
   *
   * @return a snapshot list ordered by target registration order.
   */
  public List<ReverseConnectTargetSnapshot> getReverseConnectTargetSnapshots() {
    return reverseConnectTargetManager.snapshots();
  }

  /**
   * Get the immutable runtime snapshot for a server-managed Reverse Connect target.
   *
   * @param targetId the target id to inspect.
   * @return the target snapshot, or {@link Optional#empty()} if the target is not registered.
   */
  public Optional<ReverseConnectTargetSnapshot> getReverseConnectTargetSnapshot(UUID targetId) {
    return reverseConnectTargetManager.snapshot(targetId);
  }

  /**
   * Register a listener for server-managed Reverse Connect target lifecycle events.
   *
   * <p>Listener callbacks are dispatched on this server's configured executor.
   *
   * @param listener the listener to register.
   */
  public void addReverseConnectTargetListener(ReverseConnectTargetListener listener) {
    reverseConnectTargetManager.addListener(listener);
  }

  /**
   * Remove a previously registered Reverse Connect target listener.
   *
   * @param listener the listener to remove.
   */
  public void removeReverseConnectTargetListener(ReverseConnectTargetListener listener) {
    reverseConnectTargetManager.removeListener(listener);
  }

  /**
   * Get the {@link EndpointConfig}s that were successfully bound during {@link #startup()}.
   *
   * <p>The returned list is populated during {@link #startup()} and cleared by {@link #shutdown()}
   * (and on a failed startup that rolls back). Callers querying after shutdown observe an empty
   * list. A subsequent successful {@link #startup()} repopulates the list.
   *
   * @return the {@link EndpointConfig}s that are currently bound, or an empty list when the server
   *     is not running.
   */
  public List<EndpointConfig> getBoundEndpoints() {
    return List.copyOf(boundEndpoints);
  }

  private OpcServerTransport getOrCreateTransport(TransportProfile transportProfile) {
    return transports.computeIfAbsent(transportProfile, transportFactory::create);
  }

  /**
   * Reset the endpoint descriptions cache.
   *
   * <p>If any of the EndpointConfig returned by {@link OpcUaServerConfig#getEndpoints()} has
   * changed, e.g., because the certificate has changed, the cached EndpointDescriptions need to be
   * reset.
   *
   * <p><b>Limitation:</b> resetting the cache re-resolves which endpoints are advertised, but
   * socket binding is performed once during {@link #startup()} and is not re-attempted here. An
   * endpoint that was unsatisfiable at startup (and therefore never bound) may become advertised
   * after a reset (e.g. following post-startup certificate provisioning), in which case its URL is
   * advertised without a listening socket behind it. Conversely, an endpoint bound at startup
   * remains bound even if it no longer resolves. Re-binding after a reset is not currently
   * supported; a server restart is required to change the set of bound sockets.
   */
  public void resetEndpointDescriptionCache() {
    resolvedEndpoints.reset();
  }

  private List<ResolvedEndpoint> getResolvedEndpoints() {
    return resolvedEndpoints.get(
        () -> {
          // Resolve (validate + select certificate) each configured endpoint first. Endpoints that
          // cannot be satisfied are omitted here so that the ApplicationDescription advertised in
          // every EndpointDescription, and the discovery URLs returned by GetEndpoints, are derived
          // from the same set of endpoints that actually resolved -- never from raw config that
          // includes unsatisfiable endpoints.
          Set<EndpointConfig> configs = config.getEndpoints();

          List<ResolvedCertificate> resolved =
              configs.stream().map(this::resolveCertificate).flatMap(Optional::stream).toList();

          int omittedCount = configs.size() - resolved.size();
          if (omittedCount > 0) {
            logger.warn(
                "Omitted {} of {} configured endpoint(s) from advertisement; see preceding "
                    + "per-endpoint warnings for the specific reasons.",
                omittedCount,
                configs.size());
          }

          List<String> resolvedEndpointUrls =
              resolved.stream().map(r -> r.endpointConfig().getEndpointUrl()).distinct().toList();

          ApplicationDescription applicationDescription =
              buildApplicationDescription(resolvedEndpointUrls);
          UserTokenPolicyIds userTokenPolicyIds =
              UserTokenPolicyIds.assign(
                  resolved.stream().map(ResolvedCertificate::endpointConfig).toList());

          return resolved.stream()
              .map(r -> buildResolvedEndpoint(r, applicationDescription, userTokenPolicyIds))
              .toList();
        });
  }

  /**
   * Validate an {@link EndpointConfig} and select the certificate to advertise for it.
   *
   * <p>This is the first phase of endpoint resolution: it performs all validation that may cause an
   * endpoint to be omitted from advertisement, without yet constructing an {@link
   * EndpointDescription}. Separating this phase lets the shared {@link ApplicationDescription} (and
   * therefore its {@code discoveryUrls}) be derived solely from endpoints that successfully
   * resolved.
   *
   * @param endpoint the configured endpoint to resolve.
   * @return a {@link ResolvedCertificate} if the endpoint can be advertised, otherwise an empty
   *     {@link Optional}.
   */
  private Optional<ResolvedCertificate> resolveCertificate(EndpointConfig endpoint) {
    SecurityPolicyProfile profile = SecurityPolicyProfiles.get(endpoint.getSecurityPolicy());

    try {
      CertificateIdentity certificateIdentity = null;
      X509Certificate certificate = endpoint.getCertificate();

      if (endpoint.getSecurityPolicy() != SecurityPolicy.None) {
        securityProviderResolver.resolve(profile);

        if (endpoint.getEndpointCertificateConfig().isPresent()) {
          certificateIdentity = resolveCertificateIdentity(endpoint, profile, certificate);
          certificate = certificateIdentity.certificate();
        } else if (certificate != null && profile.secureChannelEnhancements()) {
          // The legacy fixed-certificate API (setCertificate) advertises the certificate verbatim,
          // bypassing the CertificateIdentitySelector compatibility checks. Enhanced policies (e.g.
          // ECC) require a matching certificate family, so an RSA fixed certificate paired with an
          // ECC policy can never complete a handshake. Omit such endpoints from advertisement
          // rather than advertise an unusable endpoint.
          CertificateCompatibility.checkCompatible(profile, certificate);
        }
      }

      return Optional.of(new ResolvedCertificate(endpoint, certificateIdentity, certificate));
    } catch (UaException | EndpointResolutionException e) {
      logOmittedEndpoint(endpoint, e.getMessage());
      return Optional.empty();
    }
  }

  private ResolvedEndpoint buildResolvedEndpoint(
      ResolvedCertificate resolved,
      ApplicationDescription applicationDescription,
      UserTokenPolicyIds userTokenPolicyIds) {

    EndpointConfig endpoint = resolved.endpointConfig();

    return new ResolvedEndpoint(
        endpoint,
        resolved.certificateIdentity(),
        new EndpointDescription(
            endpoint.getEndpointUrl(),
            applicationDescription,
            certificateByteString(resolved.certificate()),
            endpoint.getSecurityMode(),
            endpoint.getSecurityPolicy().getUri(),
            userTokenPolicyIds.policiesFor(endpoint),
            endpoint.getTransportProfile().getUri(),
            ubyte(getSecurityLevel(endpoint.getSecurityPolicy(), endpoint.getSecurityMode()))));
  }

  /**
   * An {@link EndpointConfig} that resolved successfully, paired with the certificate to advertise.
   */
  private record ResolvedCertificate(
      EndpointConfig endpointConfig,
      @Nullable CertificateIdentity certificateIdentity,
      @Nullable X509Certificate certificate) {}

  private CertificateIdentity resolveCertificateIdentity(
      EndpointConfig endpoint, SecurityPolicyProfile profile, @Nullable X509Certificate certificate)
      throws UaException, EndpointResolutionException {

    CertificateManager certificateManager = config.getCertificateManager();

    if (certificateManager == null) {
      throw new EndpointResolutionException("no CertificateManager configured");
    }

    EndpointCertificateConfig certificateConfig =
        endpoint.getEndpointCertificateConfig().orElse(null);

    NodeId certificateGroupId =
        certificateConfig != null ? certificateConfig.getCertificateGroupId() : null;
    NodeId certificateTypeId =
        certificateConfig != null ? certificateConfig.getCertificateTypeId().orElse(null) : null;

    CertificateIdentitySelectionContext context =
        CertificateIdentitySelectionContext.forEndpointAdvertisement(
            certificateManager, profile, certificateGroupId, certificateTypeId, certificate);

    CertificateIdentity selectedIdentity =
        endpointCertificateIdentitySelector
            .select(context)
            .orElseThrow(
                () -> new EndpointResolutionException("no compatible certificate identity found"));

    if (certificate != null
        && !CertificateUtil.thumbprint(certificate).equals(selectedIdentity.thumbprint())) {
      throw new EndpointResolutionException(
          "explicit endpoint certificate is not available as a compatible local identity");
    }

    return selectedIdentity;
  }

  private void logOmittedEndpoint(EndpointConfig endpoint, String reason) {
    String certificateGroup =
        endpoint
            .getEndpointCertificateConfig()
            .map(config -> config.getCertificateGroupId().toParseableString())
            .orElse("<any>");
    String certificateType =
        endpoint
            .getEndpointCertificateConfig()
            .flatMap(EndpointCertificateConfig::getCertificateTypeId)
            .map(NodeId::toParseableString)
            .orElse("<policy-preferred>");

    logger.warn(
        "Omitting endpoint advertisement: endpointUrl={}, securityPolicyUri={}, securityMode={},"
            + " certificateGroup={}, certificateType={}, reason={}",
        endpoint.getEndpointUrl(),
        endpoint.getSecurityPolicy().getUri(),
        endpoint.getSecurityMode(),
        certificateGroup,
        certificateType,
        reason);
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

  /**
   * Build the {@link ApplicationDescription} embedded in every advertised {@link
   * EndpointDescription}.
   *
   * <p>The {@code discoveryUrls} are derived from {@code resolvedEndpointUrls} -- the URLs of
   * endpoints that successfully resolved -- rather than from raw {@link
   * OpcUaServerConfig#getEndpoints()}. This keeps the embedded ApplicationDescription consistent
   * with the discovery URLs advertised by {@code DefaultDiscoveryServiceSet}, which is likewise
   * derived from the resolved {@link EndpointDescription}s, so unsatisfiable/omitted endpoints do
   * not leak phantom discovery URLs into either path. As with the discovery service, {@code
   * /discovery} URLs are preferred when present and all resolved URLs are used otherwise.
   *
   * @param resolvedEndpointUrls the distinct endpoint URLs of endpoints that resolved successfully.
   * @return the {@link ApplicationDescription} to embed in resolved {@link EndpointDescription}s.
   */
  private ApplicationDescription buildApplicationDescription(List<String> resolvedEndpointUrls) {
    List<String> discoveryUrls =
        resolvedEndpointUrls.stream().filter(url -> url.endsWith("/discovery")).distinct().toList();

    if (discoveryUrls.isEmpty()) {
      discoveryUrls = resolvedEndpointUrls.stream().distinct().toList();
    }

    return new ApplicationDescription(
        config.getApplicationUri(),
        config.getProductUri(),
        config.getApplicationName(),
        ApplicationType.Server,
        null,
        null,
        discoveryUrls.toArray(new String[0]));
  }

  private short getSecurityLevel(SecurityPolicy securityPolicy, MessageSecurityMode securityMode) {
    return securityPolicy.getProfile().getSecurityLevel(securityMode);
  }

  private static final class EndpointResolutionException extends Exception {

    private EndpointResolutionException(String message) {
      super(message);
    }
  }

  private class ServerApplicationContextImpl implements ServerApplicationContext {

    @Override
    public List<EndpointDescription> getEndpointDescriptions() {
      return getResolvedEndpoints().stream().map(ResolvedEndpoint::endpointDescription).toList();
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
    public @Nullable SecurityKeysListener getSecurityKeysListener() {
      return config.getSecurityKeysListener().orElse(null);
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
                          logServiceRequestCompleted(path, requestMessage, service, context);
                        }
                      });

          FutureUtils.complete(future).with(response);
        } else {
          try {
            UaResponseMessageType response = serviceHandler.handle(context, requestMessage);

            logServiceRequestCompleted(path, requestMessage, service, context);

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

    private void logServiceRequestCompleted(
        String path,
        UaRequestMessageType requestMessage,
        Service service,
        ServiceRequestContext context) {

      if (logger.isTraceEnabled()) {
        logger.trace(
            "Service request completed: path={} handle={} service={} remote={}",
            path,
            requestMessage.getRequestHeader().getRequestHandle(),
            service,
            context.getChannel().remoteAddress());
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
  }

  private static class ServerEventNotifier implements EventNotifier {

    private final Set<EventListener> eventListeners =
        Collections.synchronizedSet(new LinkedHashSet<>());

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
