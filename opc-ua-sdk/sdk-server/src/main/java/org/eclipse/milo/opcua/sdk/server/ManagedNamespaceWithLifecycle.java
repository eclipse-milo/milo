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

public abstract class ManagedNamespaceWithLifecycle extends ManagedNamespace implements Lifecycle {

  private final LifecycleManager lifecycleManager = new LifecycleManager();

  public ManagedNamespaceWithLifecycle(OpcUaServer server, String namespaceUri) {
    super(server, namespaceUri);

    getLifecycleManager()
        .addLifecycle(
            new Lifecycle() {
              @Override
              public void startup() {
                registerAddressSpace(ManagedNamespaceWithLifecycle.this);
                registerNodeManager(getNodeManager());
              }

              @Override
              public void shutdown() {
                unregisterAddressSpace(ManagedNamespaceWithLifecycle.this);
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
   * Register this {@link ManagedAddressSpace} with its managing entity.
   *
   * <p>The default implementation registers it with the Server's {@link AddressSpaceManager}.
   *
   * <p>ManagedAddressSpaces that belong to a {@link AddressSpaceComposite} other than Server's
   * AddressSpaceManager should override this to register with that composite.
   *
   * @param addressSpace the {@link AddressSpace} to register.
   */
  protected void registerAddressSpace(AddressSpaceFragment addressSpace) {
    getServer().getAddressSpaceManager().register(addressSpace);
  }

  /**
   * Unregister this {@link ManagedAddressSpace} with its managing entity.
   *
   * <p>The default implementation unregisters it with the Server's {@link AddressSpaceManager}.
   *
   * <p>ManagedAddressSpaces that belong to a {@link AddressSpaceComposite} other than Server's
   * AddressSpaceManager should override this to unregister with that composite.
   *
   * @param addressSpace the {@link AddressSpace} to unregister.
   */
  protected void unregisterAddressSpace(AddressSpaceFragment addressSpace) {
    getServer().getAddressSpaceManager().unregister(addressSpace);
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
