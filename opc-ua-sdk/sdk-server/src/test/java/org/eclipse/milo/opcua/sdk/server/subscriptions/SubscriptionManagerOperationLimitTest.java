/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.subscriptions;

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.access.AccessController;
import org.eclipse.milo.opcua.sdk.server.access.AccessController.AccessResult;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifyMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifyMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateResult;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemModifyRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemModifyResult;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.SetMonitoringModeRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.SetMonitoringModeResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.SetTriggeringRequest;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Part 5 §6.3.11: MaxMonitoredItemsPerCall bounds the operation array of every monitored item
 * service. A request over the limit must fail as a whole with Bad_TooManyOperations, and a request
 * exactly at the limit must still be processed.
 *
 * <p>The Subscription in this fixture owns no monitored items, so a request that passes the limit
 * check is recognizable by the per-operation results it produces for unknown item ids.
 */
class SubscriptionManagerOperationLimitTest {

  private static final int MAX_MONITORED_ITEMS_PER_CALL = 2;

  private static final UInteger SUBSCRIPTION_ID = uint(1);

  private static final StatusCode MONITORED_ITEM_ID_INVALID =
      new StatusCode(StatusCodes.Bad_MonitoredItemIdInvalid);

  private final OpcUaServer server = mock(OpcUaServer.class);
  private final AccessController accessController = mock(AccessController.class);
  private final AddressSpaceManager addressSpaceManager = mock(AddressSpaceManager.class);
  private final Session session = mock(Session.class);
  private final ServiceRequestContext context = mock(ServiceRequestContext.class);

  private final ReadValueId itemToMonitor =
      new ReadValueId(
          new NodeId(2, "event-source"),
          AttributeId.EventNotifier.uid(),
          null,
          QualifiedName.NULL_VALUE);

  private SubscriptionManager manager;

  @BeforeEach
  void setUp() throws Exception {
    OpcUaServerConfigLimits limits =
        new OpcUaServerConfigLimits() {
          @Override
          public UInteger getMaxMonitoredItemsPerCall() {
            return uint(MAX_MONITORED_ITEMS_PER_CALL);
          }
        };

    OpcUaServerConfig config = mock(OpcUaServerConfig.class);
    when(config.getExecutor()).thenReturn(mock(ExecutorService.class));
    when(config.getLimits()).thenReturn(limits);

    when(server.getConfig()).thenReturn(config);
    when(server.getAccessController()).thenReturn(accessController);
    when(server.getAddressSpaceManager()).thenReturn(addressSpaceManager);

    manager = new SubscriptionManager(session, server);

    Subscription subscription = mock(Subscription.class);
    when(subscription.getId()).thenReturn(SUBSCRIPTION_ID);
    when(subscription.getMonitoredItems()).thenReturn(new HashMap<>());
    subscriptions(manager).put(SUBSCRIPTION_ID, subscription);

    // Denying an event item fails it before any item is created, which keeps the at-limit
    // CreateMonitoredItems request observable without a full AddressSpace.
    when(accessController.checkReadAccess(eq(session), anyList()))
        .thenReturn(Map.of(itemToMonitor, AccessResult.DENIED_USER_ACCESS));
  }

  @Test
  void createMonitoredItemsOverLimitFailsBeforeAnyItemIsProcessed() {
    var exception =
        assertThrows(
            UaException.class,
            () ->
                manager.createMonitoredItems(
                    context, createRequest(MAX_MONITORED_ITEMS_PER_CALL + 1)));

    assertEquals(StatusCodes.Bad_TooManyOperations, exception.getStatusCode().value());
    verify(accessController, never()).checkReadAccess(any(), anyList());
  }

  @Test
  void createMonitoredItemsAtLimitIsProcessed() throws Exception {
    CreateMonitoredItemsResponse response =
        manager.createMonitoredItems(context, createRequest(MAX_MONITORED_ITEMS_PER_CALL));

    MonitoredItemCreateResult[] results = requireNonNull(response.getResults());
    assertEquals(MAX_MONITORED_ITEMS_PER_CALL, results.length);
    for (MonitoredItemCreateResult result : results) {
      assertEquals(new StatusCode(StatusCodes.Bad_UserAccessDenied), result.getStatusCode());
    }
  }

  @Test
  void modifyMonitoredItemsOverLimitFails() {
    var exception =
        assertThrows(
            UaException.class,
            () ->
                manager.modifyMonitoredItems(
                    context, modifyRequest(MAX_MONITORED_ITEMS_PER_CALL + 1)));

    assertEquals(StatusCodes.Bad_TooManyOperations, exception.getStatusCode().value());
  }

  @Test
  void modifyMonitoredItemsAtLimitIsProcessed() throws Exception {
    ModifyMonitoredItemsResponse response =
        manager.modifyMonitoredItems(context, modifyRequest(MAX_MONITORED_ITEMS_PER_CALL));

    MonitoredItemModifyResult[] results = requireNonNull(response.getResults());
    assertEquals(MAX_MONITORED_ITEMS_PER_CALL, results.length);
    for (MonitoredItemModifyResult result : results) {
      assertEquals(MONITORED_ITEM_ID_INVALID, result.getStatusCode());
    }
  }

  @Test
  void deleteMonitoredItemsOverLimitFails() {
    var request =
        new DeleteMonitoredItemsRequest(
            header(), SUBSCRIPTION_ID, itemIds(MAX_MONITORED_ITEMS_PER_CALL + 1));

    assertServiceFault(
        StatusCodes.Bad_TooManyOperations, manager.deleteMonitoredItems(context, request));
  }

  @Test
  void deleteMonitoredItemsAtLimitIsProcessed() throws Exception {
    var request =
        new DeleteMonitoredItemsRequest(
            header(), SUBSCRIPTION_ID, itemIds(MAX_MONITORED_ITEMS_PER_CALL));

    DeleteMonitoredItemsResponse response = manager.deleteMonitoredItems(context, request).get();

    assertArrayEquals(
        new StatusCode[] {MONITORED_ITEM_ID_INVALID, MONITORED_ITEM_ID_INVALID},
        response.getResults());
  }

  @Test
  void setMonitoringModeOverLimitFails() {
    var request =
        new SetMonitoringModeRequest(
            header(),
            SUBSCRIPTION_ID,
            MonitoringMode.Disabled,
            itemIds(MAX_MONITORED_ITEMS_PER_CALL + 1));

    assertServiceFault(
        StatusCodes.Bad_TooManyOperations, manager.setMonitoringMode(context, request));
  }

  @Test
  void setMonitoringModeAtLimitIsProcessed() throws Exception {
    var request =
        new SetMonitoringModeRequest(
            header(),
            SUBSCRIPTION_ID,
            MonitoringMode.Disabled,
            itemIds(MAX_MONITORED_ITEMS_PER_CALL));

    SetMonitoringModeResponse response = manager.setMonitoringMode(context, request).get();

    assertArrayEquals(
        new StatusCode[] {MONITORED_ITEM_ID_INVALID, MONITORED_ITEM_ID_INVALID},
        response.getResults());
  }

  // The SetTriggering limit applies to linksToAdd and linksToRemove together: here each array is
  // within the limit on its own, and only their sum exceeds it.
  @Test
  void setTriggeringOverLimitByCombinedLinksFails() {
    var request =
        new SetTriggeringRequest(header(), SUBSCRIPTION_ID, uint(1), itemIds(2), itemIds(1));

    assertServiceFault(StatusCodes.Bad_TooManyOperations, manager.setTriggering(context, request));
  }

  // A request at the limit gets past the limit check and fails on its unknown triggering item
  // instead.
  @Test
  void setTriggeringAtLimitByCombinedLinksIsProcessed() {
    var request =
        new SetTriggeringRequest(header(), SUBSCRIPTION_ID, uint(1), itemIds(1), itemIds(1));

    assertServiceFault(
        StatusCodes.Bad_MonitoredItemIdInvalid, manager.setTriggering(context, request));
  }

  private static void assertServiceFault(long expectedStatusCode, CompletableFuture<?> future) {
    var exception = assertThrows(ExecutionException.class, future::get);
    var cause = assertInstanceOf(UaException.class, exception.getCause());

    assertEquals(expectedStatusCode, cause.getStatusCode().value());
  }

  private CreateMonitoredItemsRequest createRequest(int itemCount) {
    var item =
        new MonitoredItemCreateRequest(
            itemToMonitor,
            MonitoringMode.Reporting,
            new MonitoringParameters(uint(1), 0.0, null, uint(1), true));

    var itemsToCreate =
        IntStream.range(0, itemCount)
            .mapToObj(i -> item)
            .toArray(MonitoredItemCreateRequest[]::new);

    return new CreateMonitoredItemsRequest(
        header(), SUBSCRIPTION_ID, TimestampsToReturn.Both, itemsToCreate);
  }

  private static ModifyMonitoredItemsRequest modifyRequest(int itemCount) {
    var itemsToModify =
        IntStream.rangeClosed(1, itemCount)
            .mapToObj(
                i ->
                    new MonitoredItemModifyRequest(
                        uint(i), new MonitoringParameters(uint(i), 0.0, null, uint(1), true)))
            .toArray(MonitoredItemModifyRequest[]::new);

    return new ModifyMonitoredItemsRequest(
        header(), SUBSCRIPTION_ID, TimestampsToReturn.Both, itemsToModify);
  }

  private static UInteger[] itemIds(int count) {
    return IntStream.rangeClosed(1, count).mapToObj(i -> uint(i)).toArray(UInteger[]::new);
  }

  private static RequestHeader header() {
    return new RequestHeader(
        NodeId.NULL_VALUE, DateTime.now(), uint(1), uint(0), null, uint(0), null);
  }

  @SuppressWarnings("unchecked")
  private static Map<UInteger, Subscription> subscriptions(SubscriptionManager manager)
      throws ReflectiveOperationException {

    Field field = SubscriptionManager.class.getDeclaredField("subscriptions");
    field.setAccessible(true);
    return (Map<UInteger, Subscription>) field.get(manager);
  }
}
