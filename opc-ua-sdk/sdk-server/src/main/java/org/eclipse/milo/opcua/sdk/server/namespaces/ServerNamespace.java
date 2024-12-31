/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.namespaces;

import java.util.List;
import java.util.NoSuchElementException;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceComposite;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceFilter;
import org.eclipse.milo.opcua.sdk.server.Lifecycle;
import org.eclipse.milo.opcua.sdk.server.LifecycleManager;
import org.eclipse.milo.opcua.sdk.server.ManagedAddressSpaceFragmentWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.Namespace;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.SimpleAddressSpaceFilter;
import org.eclipse.milo.opcua.sdk.server.diagnostics.objects.ServerDiagnosticsObject;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.util.SubscriptionModel;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

public class ServerNamespace extends AddressSpaceComposite implements Lifecycle, Namespace {

  private final LifecycleManager lifecycleManager = new LifecycleManager();

  private final String namespaceUri;
  private final UShort namespaceIndex;

  public ServerNamespace(OpcUaServer server) {
    super(server);

    this.namespaceUri = server.getConfig().getApplicationUri();
    this.namespaceIndex = server.getNamespaceTable().add(namespaceUri);

    lifecycleManager.addLifecycle(
        new Lifecycle() {
          @Override
          public void startup() {
            server.getAddressSpaceManager().register(ServerNamespace.this);
          }

          @Override
          public void shutdown() {
            server.getAddressSpaceManager().unregister(ServerNamespace.this);
          }
        });

    DiagnosticsFragment diagnosticsFragment = new DiagnosticsFragment(server);
    lifecycleManager.addLifecycle(diagnosticsFragment);

    VendorServerInfoFragment vendorServerInfoFragment = new VendorServerInfoFragment(server);
    lifecycleManager.addLifecycle(vendorServerInfoFragment);
  }

  @Override
  public final void startup() {
    lifecycleManager.startup();
  }

  @Override
  public final void shutdown() {
    lifecycleManager.shutdown();
  }

  @Override
  public String getNamespaceUri() {
    return namespaceUri;
  }

  @Override
  public UShort getNamespaceIndex() {
    return namespaceIndex;
  }

  private class DiagnosticsFragment extends ManagedAddressSpaceFragmentWithLifecycle {

    private final AddressSpaceFilter filter =
        SimpleAddressSpaceFilter.create(getNodeManager()::containsNode);

    private final SubscriptionModel subscriptionModel;

    public DiagnosticsFragment(OpcUaServer server) {
      super(server, ServerNamespace.this);

      subscriptionModel = new SubscriptionModel(server, this);

      ServerDiagnosticsTypeNode serverDiagnosticsNode =
          (ServerDiagnosticsTypeNode)
              getServer()
                  .getAddressSpaceManager()
                  .getManagedNode(NodeIds.Server_ServerDiagnostics)
                  .orElseThrow(
                      () ->
                          new NoSuchElementException(
                              "NodeId: " + NodeIds.Server_ServerDiagnostics));

      serverDiagnosticsNode
          .getEnabledFlagNode()
          .setUserAccessLevel(AccessLevel.toValue(AccessLevel.READ_WRITE));

      ServerDiagnosticsObject serverDiagnosticsObject =
          new ServerDiagnosticsObject(serverDiagnosticsNode, getNodeManager());

      getLifecycleManager().addLifecycle(subscriptionModel);
      getLifecycleManager().addLifecycle(serverDiagnosticsObject);
    }

    @Override
    public AddressSpaceFilter getFilter() {
      return filter;
    }

    @Override
    public void onDataItemsCreated(List<DataItem> dataItems) {
      subscriptionModel.onDataItemsCreated(dataItems);
    }

    @Override
    public void onDataItemsModified(List<DataItem> dataItems) {
      subscriptionModel.onDataItemsModified(dataItems);
    }

    @Override
    public void onDataItemsDeleted(List<DataItem> dataItems) {
      subscriptionModel.onDataItemsDeleted(dataItems);
    }

    @Override
    public void onMonitoringModeChanged(List<MonitoredItem> monitoredItems) {
      subscriptionModel.onMonitoringModeChanged(monitoredItems);
    }
  }

  private class VendorServerInfoFragment extends ManagedAddressSpaceFragmentWithLifecycle {

    private final AddressSpaceFilter filter =
        SimpleAddressSpaceFilter.create(getNodeManager()::containsNode);

    private final SubscriptionModel subscriptionModel;

    public VendorServerInfoFragment(OpcUaServer server) {
      super(server, ServerNamespace.this);

      subscriptionModel = new SubscriptionModel(server, this);

      getLifecycleManager().addLifecycle(subscriptionModel);

      getLifecycleManager().addStartupTask(() -> VendorServerInfoNodes.add(getNodeContext()));
    }

    @Override
    public AddressSpaceFilter getFilter() {
      return filter;
    }

    @Override
    public void onDataItemsCreated(List<DataItem> dataItems) {
      subscriptionModel.onDataItemsCreated(dataItems);
    }

    @Override
    public void onDataItemsModified(List<DataItem> dataItems) {
      subscriptionModel.onDataItemsModified(dataItems);
    }

    @Override
    public void onDataItemsDeleted(List<DataItem> dataItems) {
      subscriptionModel.onDataItemsDeleted(dataItems);
    }

    @Override
    public void onMonitoringModeChanged(List<MonitoredItem> monitoredItems) {
      subscriptionModel.onMonitoringModeChanged(monitoredItems);
    }
  }
}
