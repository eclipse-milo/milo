/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

public abstract class ManagedAddressSpaceWithLifecycle extends ManagedAddressSpace
    implements Lifecycle {

  private final LifecycleManager lifecycleManager = new LifecycleManager();

  public ManagedAddressSpaceWithLifecycle(OpcUaServer server) {
    super(server);

    getLifecycleManager()
        .addLifecycle(
            new Lifecycle() {
              @Override
              public void startup() {
                registerNodeManager(getNodeManager());
              }

              @Override
              public void shutdown() {
                unregisterNodeManager(getNodeManager());
              }
            });
  }

  public ManagedAddressSpaceWithLifecycle(OpcUaServer server, UaNodeManager nodeManager) {
    super(server, nodeManager);

    getLifecycleManager()
        .addLifecycle(
            new Lifecycle() {
              @Override
              public void startup() {
                registerNodeManager(getNodeManager());
              }

              @Override
              public void shutdown() {
                unregisterNodeManager(getNodeManager());
              }
            });
  }

  @Override
  public final void startup() {
    lifecycleManager.startup();
  }

  @Override
  public final void shutdown() {
    lifecycleManager.shutdown();
  }

  /**
   * Get the {@link LifecycleManager} for this {@link ManagedAddressSpaceFragmentWithLifecycle}.
   *
   * @return the {@link LifecycleManager} for this {@link ManagedAddressSpaceFragmentWithLifecycle}.
   */
  protected LifecycleManager getLifecycleManager() {
    return lifecycleManager;
  }

  /**
   * Register this AddressSpace's {@link UaNodeManager} with its managing entity.
   *
   * <p>The default implementation registers it with the server's {@link AddressSpaceManager}.
   *
   * @param nodeManager the {@link UaNodeManager} to register.
   */
  protected void registerNodeManager(UaNodeManager nodeManager) {
    getServer().getAddressSpaceManager().register(nodeManager);
  }

  /**
   * Unregister this AddressSpace's {@link UaNodeManager} with its managing entity.
   *
   * <p>The default implementation unregisters it with the server's {@link AddressSpaceManager}.
   *
   * @param nodeManager the {@link UaNodeManager} to unregister.
   */
  protected void unregisterNodeManager(UaNodeManager nodeManager) {
    getServer().getAddressSpaceManager().unregister(nodeManager);
  }
}
