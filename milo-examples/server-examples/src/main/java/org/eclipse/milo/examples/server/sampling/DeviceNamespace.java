/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.server.sampling;

import org.eclipse.milo.examples.server.RestrictedAccessFilter;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.Lifecycle;
import org.eclipse.milo.opcua.sdk.server.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.access.ReadAccessListener;
import org.eclipse.milo.opcua.sdk.server.access.ReadAccessScope;
import org.eclipse.milo.opcua.sdk.server.identity.Identity;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilters;
import org.eclipse.milo.opcua.sdk.server.sampling.ReadAccessPolicy;
import org.eclipse.milo.opcua.sdk.server.sampling.SamplingGroupFactory;
import org.eclipse.milo.opcua.sdk.server.sampling.SamplingManagerConfig;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A namespace whose Variables are the registers of a {@link SimulatedDevice}, sampled by a {@link
 * DeviceSamplingGroup} with one device request per interval.
 *
 * <p>Under the {@code Device} folder are one Double Variable per register and a Boolean {@code
 * Locked} Variable. While the device is locked, every user but {@code admin} loses CurrentRead on
 * the registers. The namespace samples on the cached read access policy, so writing {@code Locked}
 * invalidates the namespace's cached answers; a non-admin client subscribed to a register then sees
 * {@code Bad_UserAccessDenied} on the next cycle, and its values again once unlocked.
 *
 * <p>The device polls no faster than 100 ms, so the namespace sets that as the minimum interval on
 * its {@link SamplingManagerConfig}; {@code ManagedAddressSpace} revises every requested sampling
 * interval up to the one the manager samples at, so a client is told the rate it will get.
 */
public final class DeviceNamespace extends ManagedNamespaceWithLifecycle {

  public static final String NAMESPACE_URI = "urn:eclipse:milo:device-example";

  private static final String FOLDER = "Device";
  private static final double FASTEST_POLL_MILLIS = 100.0;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final ReadAccessListener invalidationLogger =
      scope -> logger.info("Read access answers may have changed: {}", scope);

  private final OpcUaServer server;
  private final SimulatedDevice device;

  public DeviceNamespace(OpcUaServer server) {
    super(server, NAMESPACE_URI);

    this.server = server;

    device = new SimulatedDevice(server.getScheduledExecutorService());

    getLifecycleManager().addStartupTask(this::createAndAddNodes);

    getLifecycleManager()
        .addLifecycle(
            new Lifecycle() {
              @Override
              public void startup() {
                server.getAccessControlManager().addReadAccessListener(invalidationLogger);
              }

              @Override
              public void shutdown() {
                server.getAccessControlManager().removeReadAccessListener(invalidationLogger);
              }
            });
  }

  @Override
  protected SamplingGroupFactory samplingGroupFactory() {
    return (server, intervalMillis) ->
        new DeviceSamplingGroup(server, intervalMillis, device, this, this::registerOf);
  }

  @Override
  protected SamplingManagerConfig samplingManagerConfig() {
    return SamplingManagerConfig.defaults()
        .withMinimumIntervalMillis((long) FASTEST_POLL_MILLIS)
        .withReadAccessPolicy(ReadAccessPolicy.cached());
  }

  /** The register a Node stands for, or {@code null} if it is not a register Node. */
  private @Nullable String registerOf(NodeId nodeId) {
    if (nodeId.getIdentifier() instanceof String identifier
        && identifier.startsWith(FOLDER + "/")) {

      String register = identifier.substring(FOLDER.length() + 1);

      return SimulatedDevice.REGISTERS.contains(register) ? register : null;
    }

    return null;
  }

  private void createAndAddNodes() {
    var folder =
        new UaFolderNode(
            getNodeContext(),
            newNodeId(FOLDER),
            newQualifiedName(FOLDER),
            LocalizedText.english(FOLDER));

    getNodeManager().addNode(folder);

    folder.addReference(
        new Reference(
            folder.getNodeId(), NodeIds.Organizes, NodeIds.ObjectsFolder.expanded(), false));

    for (String register : SimulatedDevice.REGISTERS) {
      UaVariableNode node =
          new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
              .setNodeId(newNodeId(FOLDER + "/" + register))
              .setBrowseName(newQualifiedName(register))
              .setDisplayName(LocalizedText.english(register))
              .setDataType(NodeIds.Double)
              .setTypeDefinition(NodeIds.BaseDataVariableType)
              .setAccessLevel(AccessLevel.READ_ONLY)
              .setUserAccessLevel(AccessLevel.READ_ONLY)
              .setMinimumSamplingInterval(FASTEST_POLL_MILLIS)
              .build();

      // The effective UserAccessLevel depends on who is asking and on the lock. The filter is
      // consulted on every access check, so it is the cache that holds the answer until the lock
      // changes and the namespace invalidates.
      node.getFilterChain()
          .addLast(
              new RestrictedAccessFilter(
                  identity ->
                      device.isLocked() && !isAdmin(identity)
                          ? AccessLevel.NONE
                          : AccessLevel.READ_ONLY));

      getNodeManager().addNode(node);
      folder.addOrganizes(node);
    }

    UaVariableNode locked =
        new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
            .setNodeId(newNodeId(FOLDER + "/Locked"))
            .setBrowseName(newQualifiedName("Locked"))
            .setDisplayName(LocalizedText.english("Locked"))
            .setDataType(NodeIds.Boolean)
            .setTypeDefinition(NodeIds.BaseDataVariableType)
            .setAccessLevel(AccessLevel.READ_WRITE)
            .setUserAccessLevel(AccessLevel.READ_WRITE)
            .setValue(new DataValue(new Variant(false)))
            .build();

    // Commit the change, then invalidate: a check that runs in between must see the new lock.
    locked
        .getFilterChain()
        .addLast(
            AttributeFilters.getSetValue(
                ctx -> new DataValue(new Variant(device.isLocked())),
                (ctx, value) -> {
                  device.setLocked(Boolean.TRUE.equals(value.value().value()));
                  ctx.setAttribute(AttributeId.Value, value);

                  server
                      .getAccessControlManager()
                      .invalidateReadAccess(ReadAccessScope.namespace(getNamespaceIndex()));
                }));

    getNodeManager().addNode(locked);
    folder.addOrganizes(locked);
  }

  private static boolean isAdmin(Identity identity) {
    return identity instanceof Identity.UsernameIdentity username
        && "admin".equals(username.getUsername());
  }
}
