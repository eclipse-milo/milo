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
import java.util.ArrayList;
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
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.sdk.core.typetree.ObjectTypeTree;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.sdk.core.typetree.VariableTypeTree;
import org.eclipse.milo.opcua.sdk.server.conditions.ConditionManager;
import org.eclipse.milo.opcua.sdk.server.conditions.DefaultConditionManager;
import org.eclipse.milo.opcua.sdk.server.diagnostics.ServerDiagnosticsSummary;
import org.eclipse.milo.opcua.sdk.server.events.EventNotifierScope;
import org.eclipse.milo.opcua.sdk.server.events.TransientEvent;
import org.eclipse.milo.opcua.sdk.server.items.EventItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.model.ObjectTypeInitializer;
import org.eclipse.milo.opcua.sdk.server.model.VariableTypeInitializer;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.namespaces.OpcUaNamespace;
import org.eclipse.milo.opcua.sdk.server.namespaces.ServerNamespace;
import org.eclipse.milo.opcua.sdk.server.nodes.factories.EventFactory;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.EventInstantiator;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.NodeInstantiator;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.TypeModelCache;
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
import org.eclipse.milo.opcua.stack.core.NodeIds;
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
import org.eclipse.milo.opcua.stack.core.security.CertificateGroup;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.eclipse.milo.opcua.stack.core.util.FutureUtils;
import org.eclipse.milo.opcua.stack.core.util.Lazy;
import org.eclipse.milo.opcua.stack.core.util.LongSequence;
import org.eclipse.milo.opcua.stack.core.util.ManifestUtil;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;
import org.eclipse.milo.opcua.stack.transport.server.EndpointSelectionKey;
import org.eclipse.milo.opcua.stack.transport.server.OpcServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.OpcServerTransportFactory;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hosts the address space, services, and transports for an OPC UA server application.
 *
 * <p>The standard address space is initialized during construction. Application components that
 * must start after that initialization but before the server becomes externally visible can be
 * registered with {@link #addLifecycleParticipant(Lifecycle)} before calling {@link #startup()}.
 * The server then owns their lifecycle through startup rollback or terminal shutdown.
 */
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

  private final TypeModelCache typeModelCache = new TypeModelCache(this);
  private final NodeInstantiator nodeInstantiator = new NodeInstantiator(this);

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
  private final Lazy<EndpointSelectionIndex> endpointSelectionIndex = new Lazy<>();

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

  // lifecycleState, startupFuture, and shutdownFuture are guarded by lifecycleLock.
  // lifecycleParticipants is mutated under the lock and only while state is NEW, so the startup
  // thread may iterate it unlocked once the state leaves NEW and registration is closed.
  // startedParticipants is confined to the thread executing startup; teardown reads it only after
  // startupFuture completes, which orders the accesses.
  private final Object lifecycleLock = new Object();
  private final List<Lifecycle> lifecycleParticipants = new ArrayList<>();
  private final List<Lifecycle> startedParticipants = new ArrayList<>();

  private ServerLifecycleState lifecycleState = ServerLifecycleState.NEW;
  private @Nullable CompletableFuture<OpcUaServer> startupFuture;
  private @Nullable CompletableFuture<OpcUaServer> shutdownFuture;

  private final EventBus eventBus = new EventBus("server");
  private final EventFactory eventFactory = new EventFactory(this);
  private final EventInstantiator eventInstantiator = new EventInstantiator(this);
  private final EventNotifier eventNotifier = new ServerEventNotifier(this);
  private final ConditionManager conditionManager = new DefaultConditionManager(this);

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

  /**
   * Start this server and make its configured endpoints externally visible.
   *
   * <p>Registered lifecycle participants are started synchronously in registration order after the
   * standard address space and event facilities are available, but before the first passive
   * transport bind or reverse-connect attempt. If startup fails, each participant whose {@link
   * Lifecycle#startup()} call completed normally is shut down in reverse order before this future
   * fails.
   *
   * <p>Startup is single-flight. Concurrent or subsequent calls return the same future. Once
   * startup begins, the server is terminal: a failed or shut-down server cannot be started again.
   *
   * <p>A {@link #shutdown()} requested before endpoints are bound abandons startup and fails this
   * future; a later request lets startup complete and then tears the server down.
   *
   * @return a future completed with this server when startup succeeds.
   */
  public CompletableFuture<OpcUaServer> startup() {
    CompletableFuture<OpcUaServer> future;

    synchronized (lifecycleLock) {
      if (startupFuture != null) {
        return startupFuture;
      }
      if (lifecycleState != ServerLifecycleState.NEW) {
        startupFuture =
            CompletableFuture.failedFuture(
                new IllegalStateException("cannot start server when state=" + lifecycleState));
        return startupFuture;
      }

      lifecycleState = ServerLifecycleState.STARTING;
      startupFuture = future = new CompletableFuture<>();
    }

    try {
      startupInternal();

      synchronized (lifecycleLock) {
        // A shutdown requested late in startup leaves state at SHUTTING_DOWN; startup still
        // succeeded, and the shutdown chained on this future performs the teardown.
        if (lifecycleState == ServerLifecycleState.STARTING) {
          lifecycleState = ServerLifecycleState.RUNNING;
        }
      }

      future.complete(this);
    } catch (Throwable t) {
      rollbackStartup(t);

      synchronized (lifecycleLock) {
        if (lifecycleState == ServerLifecycleState.STARTING) {
          lifecycleState = ServerLifecycleState.STARTUP_FAILED;
        }
      }

      future.completeExceptionally(t);
    }

    return future;
  }

  private void startupInternal() throws UaException {
    // Reject ambiguous endpoint configurations before binding anything: two Session-capable
    // endpoints mapping to the same wire-observable selection key cannot be told apart at
    // OpenSecureChannel time, so runtime selection would depend on collection ordering.
    getEndpointSelectionIndex().validate();

    eventFactory.startup();
    eventInstantiator.startup();

    // Participants run application code that may request shutdown re-entrantly. Honor such a
    // request between participants, and once more before the server becomes externally visible.
    for (Lifecycle participant : lifecycleParticipants) {
      abortStartupIfShutdownRequested();
      participant.startup();
      startedParticipants.add(participant);
    }
    abortStartupIfShutdownRequested();

    bindEndpoints();

    // Startup validates targets internally; it runs after binding so the reverse-connect transport
    // lookup finds the bound transport without having to create one as a side effect.
    reverseConnectTargetManager.startup();

    if (boundEndpoints.isEmpty() && !reverseConnectTargetManager.hasSchedulableTargets()) {
      throw new UaException(StatusCodes.Bad_ConfigurationError, "No endpoints bound");
    }
  }

  private void bindEndpoints() {
    List<ResolvedEndpoint> endpoints =
        getResolvedEndpoints().stream()
            .sorted(Comparator.comparing(e -> e.endpointConfig().getTransportProfile()))
            .toList();

    for (ResolvedEndpoint resolvedEndpoint : endpoints) {
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
    }
  }

  private void abortStartupIfShutdownRequested() {
    synchronized (lifecycleLock) {
      if (lifecycleState != ServerLifecycleState.STARTING) {
        throw new IllegalStateException("server shutdown requested during startup");
      }
    }
  }

  /**
   * Register a server-owned lifecycle participant.
   *
   * <p>The participant is started during {@link #startup()} after core server facilities and the
   * standard address space are ready, but before an endpoint can accept connections. If its startup
   * completes normally, it is shut down exactly once during startup rollback or terminal server
   * shutdown, in reverse registration order and while the standard address space still exists.
   *
   * <p>Registration transfers exclusive lifecycle ownership to this server: the caller must not
   * invoke the participant's lifecycle methods unless it first removes the participant. The same
   * instance cannot be registered more than once. Concurrent registrations are ordered by the order
   * in which they acquire the server's lifecycle lock.
   *
   * <p>Participant callbacks run synchronously as part of a server startup or shutdown. A
   * participant may initiate a server shutdown from its startup callback, but it must not block
   * waiting for the enclosing server lifecycle operation to complete.
   *
   * @param participant the lifecycle participant whose startup and shutdown this server will own.
   * @throws IllegalArgumentException if the same participant instance is already registered.
   * @throws IllegalStateException if startup or shutdown has begun.
   */
  public void addLifecycleParticipant(Lifecycle participant) {
    Objects.requireNonNull(participant, "participant");

    synchronized (lifecycleLock) {
      requireLifecycleRegistrationOpen();
      if (lifecycleParticipants.stream().anyMatch(p -> p == participant)) {
        throw new IllegalArgumentException("lifecycle participant is already registered");
      }
      lifecycleParticipants.add(participant);
    }
  }

  /**
   * Remove a lifecycle participant before the server startup begins.
   *
   * <p>A successful removal returns lifecycle ownership to the caller; this server will not invoke
   * the participant. Removal is matched by object identity, not {@link Object#equals(Object)}.
   *
   * @param participant the lifecycle participant instance to remove.
   * @return {@code true} if the participant was registered and removed.
   * @throws IllegalStateException if startup or shutdown has begun.
   */
  public boolean removeLifecycleParticipant(Lifecycle participant) {
    Objects.requireNonNull(participant, "participant");

    synchronized (lifecycleLock) {
      requireLifecycleRegistrationOpen();
      return lifecycleParticipants.removeIf(p -> p == participant);
    }
  }

  private void requireLifecycleRegistrationOpen() {
    if (lifecycleState != ServerLifecycleState.NEW) {
      throw new IllegalStateException(
          "lifecycle participants cannot be changed when server state=" + lifecycleState);
    }
  }

  /**
   * Stop accepting new sessions and tear down the server runtime.
   *
   * <p>This method is the synchronization point for server shutdown. The first caller performs the
   * shutdown sequence: reject new sessions, stop reverse-connect activity, unbind passive
   * transports, drain session listener work, close sessions, stop successfully started lifecycle
   * participants in reverse registration order, then tear down the remaining namespaces,
   * diagnostics, events, and subscriptions. Concurrent callers receive the same {@link
   * CompletableFuture} so terminal lifecycle code is only run once.
   *
   * <p>Every started participant is given one shutdown attempt even if another participant fails.
   * The first shutdown failure completes the returned future exceptionally; later cleanup failures
   * are attached to it as suppressed exceptions. Calling shutdown before startup does not start or
   * stop registered participants.
   *
   * <p>If shutdown is requested from a session listener callback, this method never returns a
   * future whose completion requires that callback to return first. When the teardown runs on
   * another thread — chained behind an in-flight startup, or already driven by an earlier caller —
   * the callback receives an already-completed future; when the callback itself is the first caller
   * after startup has completed, the teardown runs inline without waiting for listener quiescence.
   *
   * @return a future completed when the server shutdown sequence has finished.
   */
  public CompletableFuture<OpcUaServer> shutdown() {
    sessionManager.beginShutdown();

    CompletableFuture<OpcUaServer> newShutdownFuture;
    CompletableFuture<OpcUaServer> startupToAwait;

    synchronized (lifecycleLock) {
      if (shutdownFuture != null) {
        if (sessionManager.isSessionListenerCallback() && !shutdownFuture.isDone()) {
          // The active shutdown is waiting for this callback to return; joining it here would
          // deadlock the listener queue.
          return CompletableFuture.completedFuture(this);
        }
        return shutdownFuture;
      }

      shutdownFuture = newShutdownFuture = new CompletableFuture<>();
      startupToAwait = startupFuture;
      lifecycleState = ServerLifecycleState.SHUTTING_DOWN;
    }

    if (startupToAwait != null && !startupToAwait.isDone()) {
      startupToAwait.whenComplete((server, failure) -> performShutdown(newShutdownFuture));

      if (sessionManager.isSessionListenerCallback()) {
        // The deferred teardown runs on the startup thread and waits for listener quiescence;
        // handing this callback the real future would let it block the very drain it must return
        // from.
        return CompletableFuture.completedFuture(this);
      }
    } else {
      performShutdown(newShutdownFuture);
    }

    return newShutdownFuture;
  }

  private void performShutdown(CompletableFuture<OpcUaServer> future) {
    Throwable failure = shutdownInternal();

    synchronized (lifecycleLock) {
      lifecycleState = ServerLifecycleState.SHUTDOWN;
    }

    if (failure == null) {
      future.complete(this);
    } else {
      future.completeExceptionally(failure);
    }
  }

  private @Nullable Throwable shutdownInternal() {
    var failures = new FailureAccumulator();

    failures.run(reverseConnectTargetManager::shutdown);
    unbindTransports(failures);
    failures.run(sessionManager::shutdown);

    // Participants may depend on the standard namespaces and event facilities, so stop them after
    // external visibility and sessions are quiesced but before any core address-space teardown.
    stopStartedParticipants(failures);

    failures.run(conditionManager::shutdown);
    failures.run(serverNamespace::shutdown);
    failures.run(opcUaNamespace::shutdown);
    stopEventServices(failures);

    for (Subscription subscription : subscriptions.values()) {
      failures.run(subscription::deleteSubscription);
    }

    return failures.failure();
  }

  /**
   * Undo the externally visible effects of a failed startup.
   *
   * <p>Cleanup failures are attached to {@code startupFailure} as suppressed exceptions.
   */
  private void rollbackStartup(Throwable startupFailure) {
    sessionManager.beginShutdown();

    var failures = new FailureAccumulator(startupFailure);
    failures.run(reverseConnectTargetManager::shutdown);
    unbindTransports(failures);
    failures.run(sessionManager::shutdown);
    stopStartedParticipants(failures);
    stopEventServices(failures);
  }

  private void stopStartedParticipants(FailureAccumulator failures) {
    List<Lifecycle> participants = new ArrayList<>(startedParticipants);
    startedParticipants.clear();

    Collections.reverse(participants);
    participants.forEach(participant -> failures.run(participant::shutdown));
  }

  private void stopEventServices(FailureAccumulator failures) {
    // AbstractLifecycle rejects shutdown when never started, so guard the path where shutdown is
    // requested before startup ever ran. Repeat shutdown of a started lifecycle is a no-op.
    if (eventInstantiator.isRunning()) {
      failures.run(eventInstantiator::shutdown);
    }
    if (eventFactory.isRunning()) {
      failures.run(eventFactory::shutdown);
    }
  }

  private void unbindTransports(FailureAccumulator failures) {
    for (OpcServerTransport transport : transports.values()) {
      try {
        transport.unbind();
      } catch (Throwable t) {
        // Unbind failures never fail a normal shutdown; log them, and when an earlier failure is
        // primary keep them attached as suppressed diagnostic context.
        logger.warn("Error unbinding transport", t);
        failures.suppress(t);
      }
    }

    transports.clear();
    boundEndpoints.clear();
  }

  /**
   * Accumulates failures from a best-effort teardown sequence: the first failure becomes the
   * primary, and later failures are attached to it as suppressed exceptions.
   */
  private static final class FailureAccumulator {

    private @Nullable Throwable failure;

    FailureAccumulator() {}

    FailureAccumulator(@Nullable Throwable failure) {
      this.failure = failure;
    }

    /** Run {@code step}, capturing anything it throws. */
    void run(Runnable step) {
      try {
        step.run();
      } catch (Throwable t) {
        if (failure == null) {
          failure = t;
        } else {
          suppress(t);
        }
      }
    }

    /** Attach {@code t} to the primary failure, if one exists, without promoting it. */
    void suppress(Throwable t) {
      if (failure != null && failure != t) {
        failure.addSuppressed(t);
      }
    }

    @Nullable Throwable failure() {
      return failure;
    }
  }

  private enum ServerLifecycleState {
    NEW,
    STARTING,
    RUNNING,
    STARTUP_FAILED,
    SHUTTING_DOWN,
    SHUTDOWN
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
   * @deprecated use {@link #getEventInstantiator()}, which validates the expected Java class at
   *     plan time instead of casting after creation. See {@code
   *     docs/features/node-instantiation-migration.md}.
   */
  @Deprecated
  public EventFactory getEventFactory() {
    return eventFactory;
  }

  /**
   * Get the shared {@link EventInstantiator}, used to create transient Event instances.
   *
   * @return the shared {@link EventInstantiator}.
   */
  public EventInstantiator getEventInstantiator() {
    return eventInstantiator;
  }

  /**
   * Get the Server's {@link EventNotifier}.
   *
   * @return the Server's {@link EventNotifier}.
   */
  public EventNotifier getEventNotifier() {
    return eventNotifier;
  }

  /**
   * Get the Server's {@link ConditionManager}.
   *
   * @return the Server's {@link ConditionManager}.
   */
  public ConditionManager getConditionManager() {
    return conditionManager;
  }

  /**
   * Create a new {@link TransientEvent} of the type identified by {@code typeDefinitionId}.
   *
   * <p>The Event Node is created with an auto-generated NodeId by the server's {@link
   * EventInstantiator}, and is deleted when the {@link TransientEvent} is closed. The EventType,
   * EventId, Time, and ReceiveTime fields are pre-populated with sensible defaults that may be
   * overwritten; all other fields must be populated by the caller before firing.
   *
   * @param typeDefinitionId the {@link NodeId} of the ObjectTypeNode representing the Event type
   *     definition.
   * @return a new {@link TransientEvent}.
   * @throws UaException if an error occurs creating the Event instance.
   */
  public TransientEvent newEvent(NodeId typeDefinitionId) throws UaException {
    BaseEventTypeNode eventNode =
        eventInstantiator.createEvent(new NodeId(1, UUID.randomUUID()), typeDefinitionId);

    eventNode.setEventId(NonceUtil.generateNonce(16));
    eventNode.setTime(DateTime.now());
    eventNode.setReceiveTime(DateTime.NULL_VALUE);

    return new TransientEvent(this, eventNode);
  }

  public ObjectTypeManager getObjectTypeManager() {
    return objectTypeManager;
  }

  public VariableTypeManager getVariableTypeManager() {
    return variableTypeManager;
  }

  /**
   * Get the Server's {@link TypeModelCache}, holding compiled {@link
   * org.eclipse.milo.opcua.sdk.server.nodes.instantiation.TypeInstantiationModel}s of this Server's
   * TypeDefinitions.
   *
   * @return the Server's {@link TypeModelCache}.
   */
  public TypeModelCache getTypeModelCache() {
    return typeModelCache;
  }

  /**
   * Get the Server's {@link NodeInstantiator}, the facade for describing, planning, and applying
   * TypeDefinition instantiations.
   *
   * @return the Server's {@link NodeInstantiator}.
   */
  public NodeInstantiator getNodeInstantiator() {
    return nodeInstantiator;
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
   * list; server instances are terminal and cannot be restarted.
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
   *
   * <p>When the endpoint set is next resolved, it is validated for selection-key collisions the
   * same way {@link #startup()} validates the initial configuration. A collision cannot fail an
   * already-running server, so it is logged as an error instead; the colliding endpoints are not
   * selectable until the configuration is corrected and the cache reset again.
   */
  public void resetEndpointDescriptionCache() {
    resolvedEndpoints.reset();
    endpointSelectionIndex.reset();
  }

  private EndpointSelectionIndex getEndpointSelectionIndex() {
    return endpointSelectionIndex.get(
        () -> {
          var index = EndpointSelectionIndex.build(getResolvedEndpoints());
          try {
            index.validate();
          } catch (UaException e) {
            logger.error("Colliding endpoints will not be selectable: {}", e.getMessage());
          }
          return index;
        });
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
      // A fixed certificate without a selection request is advertised verbatim; everything else
      // selects a managed identity.
      boolean managed = endpoint.getEndpointCertificateConfig().isPresent() || certificate == null;

      if (endpoint.getSecurityPolicy() != SecurityPolicy.None) {
        securityProviderResolver.resolve(profile);

        if (managed) {
          certificateIdentity = resolveCertificateIdentity(endpoint, profile, certificate);
          certificate = certificateIdentity.certificate();
        } else if (profile.secureChannelEnhancements()) {
          // The legacy fixed-certificate API (setCertificate) advertises the certificate verbatim,
          // bypassing the CertificateIdentitySelector compatibility checks. Enhanced policies (e.g.
          // ECC) require a matching certificate family, so an RSA fixed certificate paired with an
          // ECC policy can never complete a handshake. Omit such endpoints from advertisement
          // rather than advertise an unusable endpoint.
          CertificateCompatibility.checkCompatible(profile, certificate);
        }
      } else if (managed) {
        certificateIdentity =
            resolveUserTokenCertificateIdentity(endpoint, certificate).orElse(null);

        if (certificateIdentity != null) {
          certificate = certificateIdentity.certificate();
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

    NodeId certificateGroupId = effectiveCertificateGroupId(endpoint);
    CertificateGroup certificateGroup =
        certificateManager
            .getCertificateGroup(certificateGroupId)
            .orElseThrow(
                () ->
                    new EndpointResolutionException(
                        "certificate group not registered: "
                            + certificateGroupId.toParseableString()));

    NodeId certificateTypeId =
        endpoint
            .getEndpointCertificateConfig()
            .flatMap(EndpointCertificateConfig::getCertificateTypeId)
            .orElse(null);

    CertificateIdentitySelectionContext context =
        CertificateIdentitySelectionContext.forEndpointAdvertisement(
            List.of(certificateGroup), profile, certificateTypeId, certificate);

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

  /**
   * Select the certificate a {@link SecurityPolicy#None} endpoint advertises so clients can encrypt
   * legacy UserName and IssuedToken secrets.
   *
   * <p>Returns empty when no token policy needs one. An implicit DefaultApplicationGroup request
   * that cannot be satisfied also returns empty, after a WARN, so the endpoint stays advertised
   * without a certificate and anonymous access keeps working. An explicit {@link
   * EndpointCertificateConfig} that cannot be satisfied still omits the endpoint.
   */
  private Optional<CertificateIdentity> resolveUserTokenCertificateIdentity(
      EndpointConfig endpoint, @Nullable X509Certificate certificate)
      throws UaException, EndpointResolutionException {

    List<SecurityPolicyProfile> tokenProfiles =
        getEncryptedUserTokenSecurityPolicyProfiles(endpoint);

    if (tokenProfiles.isEmpty()) {
      return Optional.empty();
    }

    String failureReason = "no compatible certificate identity found";

    for (SecurityPolicyProfile tokenProfile : tokenProfiles) {
      try {
        securityProviderResolver.resolve(tokenProfile);

        return Optional.of(resolveCertificateIdentity(endpoint, tokenProfile, certificate));
      } catch (UaException | EndpointResolutionException e) {
        failureReason = Objects.toString(e.getMessage(), e.getClass().getSimpleName());
      }
    }

    if (endpoint.getEndpointCertificateConfig().isPresent()) {
      throw new EndpointResolutionException(failureReason);
    }

    logger.warn(
        "Advertising endpoint without a certificate; encrypted user tokens will be rejected:"
            + " endpointUrl={}, reason={}",
        endpoint.getEndpointUrl(),
        failureReason);

    return Optional.empty();
  }

  private static List<SecurityPolicyProfile> getEncryptedUserTokenSecurityPolicyProfiles(
      EndpointConfig endpoint) {

    List<SecurityPolicyProfile> profiles = new ArrayList<>();

    for (UserTokenPolicy tokenPolicy : endpoint.getTokenPolicies()) {
      UserTokenType tokenType = tokenPolicy.getTokenType();

      if (tokenType == UserTokenType.UserName || tokenType == UserTokenType.IssuedToken) {
        SecurityPolicy securityPolicy;
        try {
          securityPolicy =
              SecurityPolicy.fromUri(endpoint.getEffectiveTokenSecurityPolicyUri(tokenPolicy));
        } catch (UaException e) {
          // An unknown token security policy cannot use this server's certificate, so it does not
          // decide whether the endpoint advertises one.
          continue;
        }

        SecurityPolicyProfile profile = SecurityPolicyProfiles.get(securityPolicy);

        if (securityPolicy != SecurityPolicy.None
            && !profile.usesEnhancedUserTokenSecret()
            && !profiles.contains(profile)) {
          profiles.add(profile);
        }
      }
    }

    return profiles;
  }

  private static NodeId effectiveCertificateGroupId(EndpointConfig endpoint) {
    return endpoint
        .getEndpointCertificateConfig()
        .map(EndpointCertificateConfig::getCertificateGroupId)
        .orElse(NodeIds.ServerConfiguration_CertificateGroups_DefaultApplicationGroup);
  }

  private void logOmittedEndpoint(EndpointConfig endpoint, String reason) {
    String certificateGroup = effectiveCertificateGroupId(endpoint).toParseableString();
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
    public Optional<EndpointDescription> selectEndpoint(
        EndpointSelectionKey key, @Nullable String requestedEndpointUrl) {

      return getEndpointSelectionIndex()
          .select(key, requestedEndpointUrl)
          .map(ResolvedEndpoint::endpointDescription);
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
        // An unsecured channel is discovery-only unless an explicit SecurityPolicy.None endpoint
        // currently exists for this transport and path. The current endpoint descriptions are
        // re-checked on every request, rather than trusting a selection captured at
        // OpenSecureChannel time, so that removing the None endpoint and resetting the endpoint
        // description cache locks down already-open unsecured channels.
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

  /**
   * An {@link EventNotifier} that scopes delivery to {@link EventItem}s by the notifier hierarchy.
   *
   * <p>Delivery rules for a fired Event with SourceNode S, evaluated per registered listener:
   *
   * <ul>
   *   <li>listeners that are not {@link EventItem}s always receive the Event.
   *   <li>an item monitoring the Server Object (i=2253) always receives the Event (Part 5 root
   *       notifier rule).
   *   <li>an item monitoring node M receives the Event iff S = M or S is reachable from M via
   *       forward HasEventSource/HasNotifier References (subtypes honored). Reachability is
   *       resolved at fire time by walking inverse References up from S, so the hierarchy must be
   *       wired with Reference pairs (both directions), as {@code
   *       ManagedNamespace.registerEventNotifier} does.
   *   <li>an Event whose SourceNode is null or outside any wired hierarchy reaches only
   *       Server-object items.
   * </ul>
   */
  private static class ServerEventNotifier implements EventNotifier {

    private final Set<EventListener> eventListeners =
        Collections.synchronizedSet(new LinkedHashSet<>());

    private final OpcUaServer server;

    private ServerEventNotifier(OpcUaServer server) {
      this.server = server;
    }

    @Override
    public void fire(BaseEventTypeNode event) {
      List<EventListener> toNotify;
      synchronized (eventListeners) {
        toNotify = List.copyOf(eventListeners);
      }

      if (toNotify.isEmpty()) {
        return;
      }

      // The notifier-scope walk is only consulted for non-Server MonitoredItems; when every
      // listener monitors the Server Object (or is a non-MonitoredItem subscriber) the BFS would
      // be discarded, so skip it entirely in that common case.
      boolean scopeNeeded =
          toNotify.stream()
              .anyMatch(
                  l ->
                      l instanceof MonitoredItem item
                          && !NodeIds.Server.equals(item.getReadValueId().getNodeId()));

      Set<NodeId> notifierScope =
          scopeNeeded ? EventNotifierScope.resolve(server, event) : Set.of();

      for (EventListener eventListener : toNotify) {
        if (isInScope(eventListener, notifierScope)) {
          eventListener.onEvent(event);
        }
      }
    }

    private boolean isInScope(EventListener eventListener, Set<NodeId> notifierScope) {
      if (eventListener instanceof MonitoredItem item) {
        return EventNotifierScope.contains(item, notifierScope);
      } else {
        return true;
      }
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
