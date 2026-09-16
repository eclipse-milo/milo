/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import org.eclipse.milo.opcua.sdk.server.servicesets.AttributeServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.DiscoveryServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.MethodServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.MonitoredItemServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.NodeManagementServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.QueryServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.SessionServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.SubscriptionServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.ViewServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultAttributeServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultDiscoveryServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultMethodServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultMonitoredItemServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultNodeManagementServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultQueryServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultSessionServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultSubscriptionServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultViewServiceSet;

/**
 * Factories for the service set implementations an {@link OpcUaServer} uses.
 *
 * <p>Each factory method is invoked once while the server is constructed, and the returned instance
 * is registered on every applicable endpoint path: the {@link DiscoveryServiceSet} on all paths,
 * the other service sets on all paths not suffixed with "/discovery".
 *
 * <p>All methods have defaults that return the standard implementations, so an implementation only
 * overrides the service sets it replaces:
 *
 * <pre>{@code
 * var server = new OpcUaServer(config, transportFactory, new ServiceSets() {
 *     @Override
 *     public DiscoveryServiceSet createDiscoveryServiceSet(OpcUaServer server) {
 *         return new DefaultDiscoveryServiceSet(server, additionalServersSupplier);
 *     }
 * });
 * }</pre>
 */
public interface ServiceSets {

  default AttributeServiceSet createAttributeServiceSet(OpcUaServer server) {
    return new DefaultAttributeServiceSet(server);
  }

  default DiscoveryServiceSet createDiscoveryServiceSet(OpcUaServer server) {
    return new DefaultDiscoveryServiceSet(server);
  }

  default MethodServiceSet createMethodServiceSet(OpcUaServer server) {
    return new DefaultMethodServiceSet(server);
  }

  default MonitoredItemServiceSet createMonitoredItemServiceSet(OpcUaServer server) {
    return new DefaultMonitoredItemServiceSet(server);
  }

  default NodeManagementServiceSet createNodeManagementServiceSet(OpcUaServer server) {
    return new DefaultNodeManagementServiceSet(server);
  }

  default QueryServiceSet createQueryServiceSet(OpcUaServer server) {
    return new DefaultQueryServiceSet();
  }

  default SessionServiceSet createSessionServiceSet(OpcUaServer server) {
    return new DefaultSessionServiceSet(server);
  }

  default SubscriptionServiceSet createSubscriptionServiceSet(OpcUaServer server) {
    return new DefaultSubscriptionServiceSet(server);
  }

  default ViewServiceSet createViewServiceSet(OpcUaServer server) {
    return new DefaultViewServiceSet(server);
  }
}
