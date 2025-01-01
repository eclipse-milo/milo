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

/**
 * A {@link ManagedAddressSpace} fragment that has a {@link Lifecycle} associated with it.
 *
 * <p>This fragment will be registered with the provided composite and its {@link UaNodeManager}
 * will be registered with the server on startup and unregistered on shutdown.
 *
 * <p>Subclasses can register additional startup/shutdown tasks with the {@link LifecycleManager}
 * obtained from {@link #getLifecycleManager()}.
 */
public abstract class ManagedAddressSpaceFragmentWithLifecycle extends ManagedAddressSpaceFragment
    implements Lifecycle {

  private final LifecycleManager lifecycleManager = new LifecycleManager();

  private final AddressSpaceComposite composite;

  /**
   * Create a managed fragment using the server's {@link AddressSpaceManager} as the composite.
   *
   * @param server the {@link OpcUaServer} instance.
   */
  public ManagedAddressSpaceFragmentWithLifecycle(OpcUaServer server) {
    this(server, server.getAddressSpaceManager());
  }

  /**
   * Create a managed fragment using {@code composite}.
   *
   * @param server the {@link OpcUaServer} instance.
   * @param composite the {@link AddressSpaceComposite} this fragment is part of.
   */
  public ManagedAddressSpaceFragmentWithLifecycle(
      OpcUaServer server, AddressSpaceComposite composite) {
    super(server);

    this.composite = composite;

    getLifecycleManager()
        .addLifecycle(
            new Lifecycle() {
              @Override
              public void startup() {
                registerWithComposite(composite);
                registerNodeManager(getNodeManager());
              }

              @Override
              public void shutdown() {
                composite.unregister(ManagedAddressSpaceFragmentWithLifecycle.this);
                unregisterNodeManager(getNodeManager());
              }
            });
  }

  /**
   * Create a managed fragment using the server's {@link AddressSpaceManager} as the composite.
   *
   * @param server the {@link OpcUaServer} instance.
   * @param nodeManager the {@link UaNodeManager} to manage nodes with.
   */
  public ManagedAddressSpaceFragmentWithLifecycle(OpcUaServer server, UaNodeManager nodeManager) {
    this(server, nodeManager, server.getAddressSpaceManager());
  }

  /**
   * Create a managed fragment using {@code composite}.
   *
   * @param server the {@link OpcUaServer} instance.
   * @param nodeManager the {@link UaNodeManager} to manage nodes with.
   * @param composite the {@link AddressSpaceComposite} this fragment is part of.
   */
  public ManagedAddressSpaceFragmentWithLifecycle(
      OpcUaServer server, UaNodeManager nodeManager, AddressSpaceComposite composite) {

    super(server, nodeManager);

    this.composite = composite;

    getLifecycleManager()
        .addLifecycle(
            new Lifecycle() {
              @Override
              public void startup() {
                registerWithComposite(composite);
                registerNodeManager(getNodeManager());
              }

              @Override
              public void shutdown() {
                composite.unregister(ManagedAddressSpaceFragmentWithLifecycle.this);
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
   * Get the {@link AddressSpaceComposite} this fragment registers with.
   *
   * @return the {@link AddressSpaceComposite} this fragment registers with.
   */
  public AddressSpaceComposite getComposite() {
    return composite;
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
   * Register this {@link AddressSpaceFragment} with {@code composite}.
   *
   * <p>Can be overridden if e.g. this fragment should use {@link
   * AddressSpaceComposite#registerFirst(AddressSpaceFragment)} instead of {@link
   * AddressSpaceComposite#register(AddressSpaceFragment)}.
   *
   * @param composite the {@link AddressSpaceComposite} to register with.
   */
  protected void registerWithComposite(AddressSpaceComposite composite) {
    composite.register(ManagedAddressSpaceFragmentWithLifecycle.this);
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
