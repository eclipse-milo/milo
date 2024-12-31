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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import org.eclipse.milo.opcua.sdk.server.servicesets.AttributeServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.DiscoveryServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.MethodServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.MonitoredItemServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.NodeManagementServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.Service;
import org.eclipse.milo.opcua.sdk.server.servicesets.SessionServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.SubscriptionServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.ViewServiceSet;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.AddReferencesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseNextRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CancelRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CloseSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteNodesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteSubscriptionsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.FindServersOnNetworkRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.FindServersRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.GetEndpointsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifyMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifySubscriptionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterNodesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterServer2Request;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterServerRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RepublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.SetMonitoringModeRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.SetPublishingModeRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.SetTriggeringRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferSubscriptionsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.TranslateBrowsePathsToNodeIdsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.UnregisterNodesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteRequest;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.jspecify.annotations.Nullable;

public abstract class AbstractServiceHandler {

  private final ServiceHandlerTable serviceHandlerTable = new ServiceHandlerTable();

  public void addServiceSet(String path, AttributeServiceSet serviceSet) {
    addServiceHandler(
        path,
        Service.ATTRIBUTE_READ,
        (context, request) -> serviceSet.onRead(context, (ReadRequest) request));
    addServiceHandler(
        path,
        Service.ATTRIBUTE_HISTORY_READ,
        (context, request) -> serviceSet.onHistoryRead(context, (HistoryReadRequest) request));
    addServiceHandler(
        path,
        Service.ATTRIBUTE_WRITE,
        (context, request) -> serviceSet.onWrite(context, (WriteRequest) request));
    addServiceHandler(
        path,
        Service.ATTRIBUTE_HISTORY_UPDATE,
        (context, request) -> serviceSet.onHistoryUpdate(context, (HistoryUpdateRequest) request));
  }

  public void addServiceSet(String path, DiscoveryServiceSet serviceSet) {
    addServiceHandler(
        path,
        Service.DISCOVERY_FIND_SERVERS,
        (context, request) -> serviceSet.onFindServers(context, (FindServersRequest) request));
    addServiceHandler(
        path,
        Service.DISCOVERY_FIND_SERVERS_ON_NETWORK,
        (context, request) ->
            serviceSet.onFindServersOnNetwork(context, (FindServersOnNetworkRequest) request));
    addServiceHandler(
        path,
        Service.DISCOVERY_GET_ENDPOINTS,
        (context, request) -> serviceSet.onGetEndpoints(context, (GetEndpointsRequest) request));
    addServiceHandler(
        path,
        Service.DISCOVERY_REGISTER_SERVER,
        (context, request) ->
            serviceSet.onRegisterServer(context, (RegisterServerRequest) request));
    addServiceHandler(
        path,
        Service.DISCOVERY_REGISTER_SERVER_2,
        (context, request) ->
            serviceSet.onRegisterServer2(context, (RegisterServer2Request) request));
  }

  public void addServiceSet(String path, MethodServiceSet serviceSet) {
    addServiceHandler(
        path,
        Service.METHOD_CALL,
        (context, request) -> serviceSet.onCall(context, (CallRequest) request));
  }

  public void addServiceSet(String path, MonitoredItemServiceSet serviceSet) {
    addServiceHandler(
        path,
        Service.MONITORED_ITEM_CREATE_MONITORED_ITEMS,
        (context, request) ->
            serviceSet.onCreateMonitoredItems(context, (CreateMonitoredItemsRequest) request));
    addServiceHandler(
        path,
        Service.MONITORED_ITEM_MODIFY_MONITORED_ITEMS,
        (context, request) ->
            serviceSet.onModifyMonitoredItems(context, (ModifyMonitoredItemsRequest) request));
    addServiceHandler(
        path,
        Service.MONITORED_ITEM_DELETE_MONITORED_ITEMS,
        (context, request) ->
            serviceSet.onDeleteMonitoredItems(context, (DeleteMonitoredItemsRequest) request));
    addServiceHandler(
        path,
        Service.MONITORED_ITEM_SET_MONITORING_MODE,
        (context, request) ->
            serviceSet.onSetMonitoringMode(context, (SetMonitoringModeRequest) request));
    addServiceHandler(
        path,
        Service.MONITORED_ITEM_SET_TRIGGERING,
        (context, request) -> serviceSet.onSetTriggering(context, (SetTriggeringRequest) request));
  }

  public void addServiceSet(String path, NodeManagementServiceSet serviceSet) {
    addServiceHandler(
        path,
        Service.NODE_MANAGEMENT_ADD_NODES,
        (context, request) -> serviceSet.onAddNodes(context, (AddNodesRequest) request));
    addServiceHandler(
        path,
        Service.NODE_MANAGEMENT_DELETE_NODES,
        (context, request) -> serviceSet.onDeleteNodes(context, (DeleteNodesRequest) request));
    addServiceHandler(
        path,
        Service.NODE_MANAGEMENT_ADD_REFERENCES,
        (context, request) -> serviceSet.onAddReferences(context, (AddReferencesRequest) request));
    addServiceHandler(
        path,
        Service.NODE_MANAGEMENT_DELETE_REFERENCES,
        (context, request) ->
            serviceSet.onDeleteReferences(context, (DeleteReferencesRequest) request));
  }

  public void addServiceSet(String path, SessionServiceSet serviceSet) {
    addServiceHandler(
        path,
        Service.SESSION_CREATE_SESSION,
        (context, request) -> serviceSet.onCreateSession(context, (CreateSessionRequest) request));
    addServiceHandler(
        path,
        Service.SESSION_ACTIVATE_SESSION,
        (context, request) ->
            serviceSet.onActivateSession(context, (ActivateSessionRequest) request));
    addServiceHandler(
        path,
        Service.SESSION_CLOSE_SESSION,
        (context, request) -> serviceSet.onCloseSession(context, (CloseSessionRequest) request));
    addServiceHandler(
        path,
        Service.SESSION_CANCEL,
        (context, request) -> serviceSet.onCancel(context, (CancelRequest) request));
  }

  public void addServiceSet(String path, SubscriptionServiceSet serviceSet) {
    addServiceHandler(
        path,
        Service.SUBSCRIPTION_CREATE_SUBSCRIPTION,
        (context, request) ->
            serviceSet.onCreateSubscription(context, (CreateSubscriptionRequest) request));
    addServiceHandler(
        path,
        Service.SUBSCRIPTION_MODIFY_SUBSCRIPTION,
        (context, request) ->
            serviceSet.onModifySubscription(context, (ModifySubscriptionRequest) request));
    addServiceHandler(
        path,
        Service.SUBSCRIPTION_DELETE_SUBSCRIPTIONS,
        (context, request) ->
            serviceSet.onDeleteSubscriptions(context, (DeleteSubscriptionsRequest) request));
    addServiceHandler(
        path,
        Service.SUBSCRIPTION_TRANSFER_SUBSCRIPTIONS,
        (context, request) ->
            serviceSet.onTransferSubscriptions(context, (TransferSubscriptionsRequest) request));
    addServiceHandler(
        path,
        Service.SUBSCRIPTION_SET_PUBLISHING_MODE,
        (context, request) ->
            serviceSet.onSetPublishingMode(context, (SetPublishingModeRequest) request));
    addServiceHandler(
        path,
        Service.SUBSCRIPTION_PUBLISH,
        (AsyncServiceHandler)
            (context, requestMessage) -> {
              CompletableFuture<PublishResponse> future =
                  serviceSet.onPublish(context, (PublishRequest) requestMessage);

              return future.thenApply(Function.identity());
            });
    addServiceHandler(
        path,
        Service.SUBSCRIPTION_REPUBLISH,
        (context, request) -> serviceSet.onRepublish(context, (RepublishRequest) request));
  }

  public void addServiceSet(String path, ViewServiceSet serviceSet) {
    addServiceHandler(
        path,
        Service.VIEW_BROWSE,
        (context, request) -> serviceSet.onBrowse(context, (BrowseRequest) request));
    addServiceHandler(
        path,
        Service.VIEW_BROWSE_NEXT,
        (context, request) -> serviceSet.onBrowseNext(context, (BrowseNextRequest) request));
    addServiceHandler(
        path,
        Service.VIEW_TRANSLATE_BROWSE_PATHS,
        (context, request) ->
            serviceSet.onTranslateBrowsePaths(
                context, (TranslateBrowsePathsToNodeIdsRequest) request));
    addServiceHandler(
        path,
        Service.VIEW_REGISTER_NODES,
        (context, request) -> serviceSet.onRegisterNodes(context, (RegisterNodesRequest) request));
    addServiceHandler(
        path,
        Service.VIEW_UNREGISTER_NODES,
        (context, request) ->
            serviceSet.onUnregisterNodes(context, (UnregisterNodesRequest) request));
  }

  protected void addServiceHandler(String path, Service service, ServiceHandler serviceHandler) {
    serviceHandlerTable.put(path, service, serviceHandler);
  }

  protected @Nullable ServiceHandler getServiceHandler(String path, Service service) {
    return serviceHandlerTable.get(path, service);
  }

  protected interface ServiceHandler {

    /**
     * Handle a service request, returning the corresponding service response.
     *
     * @param context the {@link ServiceRequestContext}.
     * @param requestMessage the {@link UaRequestMessageType} to handle.
     * @return the {@link UaResponseMessageType} service response.
     */
    UaResponseMessageType handle(ServiceRequestContext context, UaRequestMessageType requestMessage)
        throws UaException;
  }

  protected interface AsyncServiceHandler extends ServiceHandler {

    @Override
    default UaResponseMessageType handle(
        ServiceRequestContext context, UaRequestMessageType requestMessage) throws UaException {

      try {
        return handleAsync(context, requestMessage).get();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new UaException(StatusCodes.Bad_UnexpectedError, e);
      } catch (ExecutionException e) {
        throw UaException.extract(e).orElse(new UaException(e));
      }
    }

    /**
     * Handle a service request asynchronously, returning a {@link CompletableFuture} that completes
     * with the corresponding service response.
     *
     * @param context the {@link ServiceRequestContext}.
     * @param requestMessage the {@link UaRequestMessageType} to handle.
     * @return a {@link CompletableFuture} that completes with the corresponding service response.
     */
    CompletableFuture<UaResponseMessageType> handleAsync(
        ServiceRequestContext context, UaRequestMessageType requestMessage);
  }

  private static class ServiceHandlerTable {

    private final ConcurrentMap<String, ConcurrentMap<Service, ServiceHandler>> table =
        new ConcurrentHashMap<>();

    public @Nullable ServiceHandler get(String path, Service service) {
      ConcurrentMap<Service, ServiceHandler> handlers = table.get(path);
      if (handlers != null) {
        return handlers.get(service);
      } else {
        return null;
      }
    }

    public void put(String path, Service service, ServiceHandler handler) {
      ConcurrentMap<Service, ServiceHandler> handlers =
          table.computeIfAbsent(path, k -> new ConcurrentHashMap<>());

      handlers.put(service, handler);
    }
  }
}
