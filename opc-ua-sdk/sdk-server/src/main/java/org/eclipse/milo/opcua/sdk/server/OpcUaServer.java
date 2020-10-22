/*
 * Copyright (c) 2019 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import org.eclipse.milo.opcua.sdk.core.ServerTable;
import org.eclipse.milo.opcua.sdk.server.api.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.diagnostics.ServerDiagnosticsSummary;
import org.eclipse.milo.opcua.sdk.server.model.ObjectTypeInitializer;
import org.eclipse.milo.opcua.sdk.server.model.VariableTypeInitializer;
import org.eclipse.milo.opcua.sdk.server.namespaces.OpcUaNamespace;
import org.eclipse.milo.opcua.sdk.server.namespaces.ServerNamespace;
import org.eclipse.milo.opcua.sdk.server.nodes.factories.EventFactory;
import org.eclipse.milo.opcua.sdk.server.subscriptions.Subscription;
import org.eclipse.milo.opcua.stack.core.BuiltinReferenceType;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.ReferenceType;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.serialization.SerializationContext;
import org.eclipse.milo.opcua.stack.core.types.DataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.eclipse.milo.opcua.stack.core.util.ManifestUtil;
import org.eclipse.milo.opcua.stack.server.UaStackServer;
import org.eclipse.milo.opcua.stack.server.services.AttributeHistoryServiceSet;
import org.eclipse.milo.opcua.stack.server.services.AttributeServiceSet;
import org.eclipse.milo.opcua.stack.server.services.MethodServiceSet;
import org.eclipse.milo.opcua.stack.server.services.MonitoredItemServiceSet;
import org.eclipse.milo.opcua.stack.server.services.NodeManagementServiceSet;
import org.eclipse.milo.opcua.stack.server.services.SessionServiceSet;
import org.eclipse.milo.opcua.stack.server.services.SubscriptionServiceSet;
import org.eclipse.milo.opcua.stack.server.services.ViewServiceSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpcUaServer {

    public static final String SDK_VERSION =
        ManifestUtil.read("X-SDK-Version").orElse("dev");

    static {
        Logger logger = LoggerFactory.getLogger(OpcUaServer.class);
        logger.info("Eclipse Milo OPC UA Stack version: {}", Stack.VERSION);
        logger.info("Eclipse Milo OPC UA Server SDK version: {}", SDK_VERSION);
    }

    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Stack.sharedScheduledExecutor();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<NodeId, ReferenceType> referenceTypes = Maps.newConcurrentMap();

    private final Map<UInteger, Subscription> subscriptions = Maps.newConcurrentMap();

    private final ServerTable serverTable = createServerTable();

    private final AddressSpaceManager addressSpaceManager = createAddressSpaceManager();
    private final SessionManager sessionManager = createSessionManager();
    private final ObjectTypeManager objectTypeManager = createObjectTypeManager();
    private final VariableTypeManager variableTypeManager = createVariableTypeManager();

    private final ServerDiagnosticsSummary diagnosticsSummary = createServerDiagnosticsSummary();

    private final EventBus eventBus = new EventBus("server");
    private final EventFactory eventFactory = createEventFactory();

    private final UaStackServer stackServer;

    private final OpcUaNamespace opcUaNamespace;
    private final ServerNamespace serverNamespace;

    private final OpcUaServerConfig config;

    public OpcUaServer(OpcUaServerConfig config) {
        this.config = config;

        stackServer = new UaStackServer(config);

        Stream<String> paths = stackServer.getConfig().getEndpoints()
            .stream()
            .map(e -> EndpointUtil.getPath(e.getEndpointUrl()))
            .distinct();

        paths.filter(path -> !path.endsWith("/discovery")).forEach(path -> {
            stackServer.addServiceSet(path, (AttributeServiceSet) sessionManager);
            stackServer.addServiceSet(path, (AttributeHistoryServiceSet) sessionManager);
            stackServer.addServiceSet(path, (MethodServiceSet) sessionManager);
            stackServer.addServiceSet(path, (MonitoredItemServiceSet) sessionManager);
            stackServer.addServiceSet(path, (NodeManagementServiceSet) sessionManager);
            stackServer.addServiceSet(path, (SessionServiceSet) sessionManager);
            stackServer.addServiceSet(path, (SubscriptionServiceSet) sessionManager);
            stackServer.addServiceSet(path, (ViewServiceSet) sessionManager);
        });

        ObjectTypeInitializer.initialize(stackServer.getNamespaceTable(), objectTypeManager);

        VariableTypeInitializer.initialize(stackServer.getNamespaceTable(), variableTypeManager);

        opcUaNamespace = new OpcUaNamespace(this);
        opcUaNamespace.startup();

        serverNamespace = new ServerNamespace(this);
        serverNamespace.startup();

        serverTable.addUri(stackServer.getConfig().getApplicationUri());

        for (ReferenceType referenceType : BuiltinReferenceType.values()) {
            referenceTypes.put(referenceType.getNodeId(), referenceType);
        }
    }

    protected ServerTable createServerTable() {
        return new ServerTable();
    }

    protected AddressSpaceManager createAddressSpaceManager() {
        return new AddressSpaceManager(this);
    }

    protected SessionManager createSessionManager() {
        return new SessionManager(this);
    }

    protected ObjectTypeManager createObjectTypeManager() {
        return new ObjectTypeManager();
    }

    protected VariableTypeManager createVariableTypeManager() {
        return new VariableTypeManager();
    }

    protected ServerDiagnosticsSummary createServerDiagnosticsSummary() {
        return new ServerDiagnosticsSummary(this);
    }

    protected EventFactory createEventFactory() {
        return new EventFactory(this);
    }

    public OpcUaServerConfig getConfig() {
        return config;
    }

    public CompletableFuture<OpcUaServer> startup() {
        eventFactory.startup();

        return stackServer.startup()
            .thenApply(s -> OpcUaServer.this);
    }

    public CompletableFuture<OpcUaServer> shutdown() {
        serverNamespace.shutdown();
        opcUaNamespace.shutdown();

        eventFactory.shutdown();

        subscriptions.values()
            .forEach(Subscription::deleteSubscription);

        return stackServer.shutdown()
            .thenApply(s -> OpcUaServer.this);
    }

    public UaStackServer getStackServer() {
        return stackServer;
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

    public ServerTable getServerTable() {
        return serverTable;
    }

    public DataTypeManager getDataTypeManager() {
        return stackServer.getDataTypeManager();
    }

    public NamespaceTable getNamespaceTable() {
        return stackServer.getNamespaceTable();
    }

    public SerializationContext getSerializationContext() {
        return stackServer.getSerializationContext();
    }

    public ServerDiagnosticsSummary getDiagnosticsSummary() {
        return diagnosticsSummary;
    }

    /**
     * Get the Server-wide {@link EventBus}.
     * <p>
     * Events posted to the EventBus are delivered synchronously to registered subscribers.
     *
     * @return the Server-wide {@link EventBus}.
     */
    public EventBus getEventBus() {
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

    public ObjectTypeManager getObjectTypeManager() {
        return objectTypeManager;
    }

    public VariableTypeManager getVariableTypeManager() {
        return variableTypeManager;
    }

    public Map<UInteger, Subscription> getSubscriptions() {
        return subscriptions;
    }

    public Optional<KeyPair> getKeyPair(ByteString thumbprint) {
        return stackServer.getConfig().getCertificateManager().getKeyPair(thumbprint);
    }

    public Optional<X509Certificate> getCertificate(ByteString thumbprint) {
        return stackServer.getConfig().getCertificateManager().getCertificate(thumbprint);
    }

    public Optional<X509Certificate[]> getCertificateChain(ByteString thumbprint) {
        return stackServer.getConfig().getCertificateManager().getCertificateChain(thumbprint);
    }

    public ExecutorService getExecutorService() {
        return stackServer.getConfig().getExecutor();
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return SCHEDULED_EXECUTOR_SERVICE;
    }

    public ImmutableList<EndpointDescription> getEndpointDescriptions() {
        return stackServer.getEndpointDescriptions();
    }

    public Map<NodeId, ReferenceType> getReferenceTypes() {
        return referenceTypes;
    }

}
