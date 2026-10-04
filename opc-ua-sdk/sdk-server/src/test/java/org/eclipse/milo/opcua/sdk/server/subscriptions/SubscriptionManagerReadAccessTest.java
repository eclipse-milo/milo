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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.RevisedDataItemParameters;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.DataItemListener;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.access.AccessController;
import org.eclipse.milo.opcua.sdk.server.access.AccessController.AccessResult;
import org.eclipse.milo.opcua.sdk.server.items.BaseMonitoredItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredDataItem;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateResult;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** How CreateMonitoredItems treats a data item the Session is denied read access to. */
class SubscriptionManagerReadAccessTest {

  private static final UInteger SUBSCRIPTION_ID = uint(1);

  private final Map<UInteger, BaseMonitoredItem<?>> ownedMonitoredItems = new HashMap<>();

  private final OpcUaServer server = mock(OpcUaServer.class);
  private final AccessController accessController = mock(AccessController.class);
  private final AddressSpaceManager addressSpaceManager = mock(AddressSpaceManager.class);
  private final Session session = mock(Session.class);
  private final ServiceRequestContext context = mock(ServiceRequestContext.class);

  private final ReadValueId itemToMonitor =
      new ReadValueId(
          new NodeId(2, "variable"), AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE);

  private SubscriptionManager manager;

  @BeforeEach
  void setUp() throws Exception {
    OpcUaServerConfig config = mock(OpcUaServerConfig.class);
    when(config.getExecutor()).thenReturn(mock(ExecutorService.class));
    when(config.getLimits()).thenReturn(new OpcUaServerConfigLimits() {});

    when(server.getConfig()).thenReturn(config);
    when(server.getAccessController()).thenReturn(accessController);
    when(server.getAddressSpaceManager()).thenReturn(addressSpaceManager);
    when(server.getDataItemListener()).thenReturn(new DataItemListener() {});
    when(server.getStaticEncodingContext()).thenReturn(DefaultEncodingContext.INSTANCE);
    when(server.getMonitoredItemCount()).thenReturn(new AtomicLong());

    manager = new SubscriptionManager(session, server);

    Subscription subscription = mock(Subscription.class);
    when(subscription.getId()).thenReturn(SUBSCRIPTION_ID);
    when(subscription.getMonitoredItems()).thenReturn(ownedMonitoredItems);
    when(subscription.nextItemId()).thenReturn(1L);
    doAnswer(
            invocation -> {
              List<BaseMonitoredItem<?>> items = invocation.getArgument(0);
              items.forEach(item -> ownedMonitoredItems.put(item.getId(), item));
              return null;
            })
        .when(subscription)
        .addMonitoredItems(anyList());
    subscriptions(manager).put(SUBSCRIPTION_ID, subscription);

    // NodeClass, EventNotifier, DataType, MinimumSamplingInterval of an Int32 Variable.
    when(addressSpaceManager.read(any(), eq(0.0), eq(TimestampsToReturn.Neither), anyList()))
        .thenReturn(
            List.of(
                new DataValue(new Variant(NodeClass.Variable)),
                new DataValue(Variant.NULL_VALUE),
                new DataValue(new Variant(NodeIds.Int32)),
                new DataValue(new Variant(0.0))));
    when(addressSpaceManager.onCreateDataItem(eq(itemToMonitor), any(), any()))
        .thenReturn(new RevisedDataItemParameters(100.0, uint(1)));
  }

  /**
   * Part 4 §5.13.2.1: the create succeeds and the denial goes to the Publish response. The denial
   * is queued on the item at create time, before the AddressSpace has sampled it.
   */
  @Test
  void readAccessDenialCreatesTheItemWithTheDenialQueued() throws Exception {
    when(accessController.checkReadAccess(eq(session), anyList()))
        .thenReturn(Map.of(itemToMonitor, AccessResult.DENIED_USER_ACCESS));

    CreateMonitoredItemsResponse response =
        manager.createMonitoredItems(context, createRequest(createItem()));

    MonitoredItemCreateResult[] results = requireNonNull(response.getResults());
    assertEquals(1, results.length);
    assertEquals(StatusCode.GOOD, results[0].getStatusCode());

    assertEquals(1, ownedMonitoredItems.size());
    MonitoredDataItem item =
        assertInstanceOf(MonitoredDataItem.class, ownedMonitoredItems.values().iterator().next());

    List<DataValue> queued = drain(item);
    assertEquals(1, queued.size());
    assertEquals(StatusCodes.Bad_UserAccessDenied, queued.get(0).statusCode().getValue());
    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        item.getReadAccessResult(),
        "the create-time result is the result the item reports");
  }

  // Only read-access denials are deferred; an AccessRestriction the channel does not satisfy
  // still fails the item in CreateMonitoredItems.
  @Test
  void securityModeDenialStillFailsTheItem() throws Exception {
    when(accessController.checkReadAccess(eq(session), anyList()))
        .thenReturn(Map.of(itemToMonitor, AccessResult.DENIED_SECURITY_MODE));

    CreateMonitoredItemsResponse response =
        manager.createMonitoredItems(context, createRequest(createItem()));

    MonitoredItemCreateResult[] results = requireNonNull(response.getResults());
    assertEquals(1, results.length);
    assertEquals(
        new StatusCode(StatusCodes.Bad_SecurityModeInsufficient), results[0].getStatusCode());

    assertTrue(ownedMonitoredItems.isEmpty());
  }

  // An event item has no sampled value to carry a denial, so a read-access denial from the
  // AccessController still fails it in CreateMonitoredItems.
  @Test
  void readAccessDenialStillFailsAnEventItem() throws Exception {
    var eventItemToMonitor =
        new ReadValueId(
            new NodeId(2, "object"),
            AttributeId.EventNotifier.uid(),
            null,
            QualifiedName.NULL_VALUE);

    when(accessController.checkReadAccess(eq(session), anyList()))
        .thenReturn(Map.of(eventItemToMonitor, AccessResult.DENIED_USER_ACCESS));

    var request =
        new MonitoredItemCreateRequest(
            eventItemToMonitor,
            MonitoringMode.Reporting,
            new MonitoringParameters(uint(1), 0.0, null, uint(1), true));

    CreateMonitoredItemsResponse response =
        manager.createMonitoredItems(context, createRequest(request));

    MonitoredItemCreateResult[] results = requireNonNull(response.getResults());
    assertEquals(1, results.length);
    assertEquals(new StatusCode(StatusCodes.Bad_UserAccessDenied), results[0].getStatusCode());

    assertTrue(ownedMonitoredItems.isEmpty());
  }

  // A custom AccessController that answers nothing for an item must not fail the create. The item
  // is created allowed, as before the create-time check was seeded, and is refreshed from there.
  @Test
  void aMissingAccessResultCreatesTheItemAllowed() throws Exception {
    when(accessController.checkReadAccess(eq(session), anyList())).thenReturn(Map.of());

    CreateMonitoredItemsResponse response =
        manager.createMonitoredItems(context, createRequest(createItem()));

    assertEquals(StatusCode.GOOD, requireNonNull(response.getResults())[0].getStatusCode());
    MonitoredDataItem item =
        assertInstanceOf(MonitoredDataItem.class, ownedMonitoredItems.values().iterator().next());
    assertEquals(AccessResult.ALLOWED, item.getReadAccessResult());
  }

  /**
   * Part 4 §5.13.2.1: an identity or endpoint change can revoke read access to every item of the
   * Session at once. The re-check reaches every data item, whoever samples it, and a new denial is
   * queued without waiting for a sample.
   */
  @Test
  void refreshReadAccessAppliesTheCurrentAnswerToEveryDataItem() throws Exception {
    MonitoredDataItem item = createAllowedItem();
    when(accessController.checkReadAccess(eq(session), anyList()))
        .thenReturn(Map.of(itemToMonitor, AccessResult.DENIED_USER_ACCESS));

    manager.refreshReadAccess();

    assertEquals(AccessResult.DENIED_USER_ACCESS, item.getReadAccessResult());
    assertEquals(
        List.of(StatusCodes.Bad_UserAccessDenied),
        drain(item).stream().map(value -> value.statusCode().getValue()).toList());
  }

  // Values queued before an identity or endpoint change were sampled and checked for the previous
  // user or channel. An item the Session may no longer read drops them, and the client gets the
  // denial in their place, as it does after a transfer to a Session that may not read the item.
  @Test
  void refreshReadAccessWithANewDenialDropsTheValuesQueuedBeforeTheChange() throws Exception {
    MonitoredDataItem item = createAllowedItemWithQueuedValues(1, 2);
    when(accessController.checkReadAccess(eq(session), anyList()))
        .thenReturn(Map.of(itemToMonitor, AccessResult.DENIED_USER_ACCESS));

    manager.refreshReadAccess();

    assertEquals(
        List.of(StatusCodes.Bad_UserAccessDenied),
        drain(item).stream().map(value -> value.statusCode().getValue()).toList());
  }

  // The control for the test above: a client reactivating on a new channel keeps the values it
  // may still read. Only a new denial drops them.
  @Test
  void refreshReadAccessThatStillAllowsKeepsTheQueuedValues() throws Exception {
    MonitoredDataItem item = createAllowedItemWithQueuedValues(1, 2);

    manager.refreshReadAccess();

    assertEquals(List.of(1, 2), drain(item).stream().map(value -> value.value().value()).toList());
  }

  // ActivateSession calls the refresh after committing the change, so a check that throws must
  // neither fail the service nor change any item.
  @Test
  void refreshReadAccessWhoseCheckThrowsChangesNothing() throws Exception {
    MonitoredDataItem item = createAllowedItem();
    when(accessController.checkReadAccess(eq(session), anyList()))
        .thenThrow(new IllegalStateException("attribute filter failed"));

    assertDoesNotThrow(() -> manager.refreshReadAccess());

    assertEquals(AccessResult.ALLOWED, item.getReadAccessResult());
    assertTrue(drain(item).isEmpty());
  }

  // Two identity changes in quick succession each run a refresh. One whose check straddles the
  // second change answers for the previous user, so it applies nothing and leaves the item to the
  // refresh of the later change.
  @Test
  void refreshReadAccessOvertakenByAnotherChangeAppliesNothing() throws Exception {
    MonitoredDataItem item = createAllowedItem();
    var accessEpoch = new AtomicLong();
    when(session.getAccessEpoch()).thenAnswer(invocation -> accessEpoch.get());
    when(accessController.checkReadAccess(eq(session), anyList()))
        .thenAnswer(
            invocation -> {
              accessEpoch.incrementAndGet();
              return Map.of(itemToMonitor, AccessResult.DENIED_USER_ACCESS);
            });

    manager.refreshReadAccess();

    assertEquals(AccessResult.ALLOWED, item.getReadAccessResult());
  }

  // Two ActivateSession requests can overlap. When the later one changes the identity and applies
  // its answer while this refresh is still applying its own, this refresh's answer is for the
  // previous identity, so it must neither replace the later answer nor drop the value queued under
  // it.
  @Test
  void refreshReadAccessOvertakenWhileApplyingKeepsTheLaterAnswerAndItsValues() throws Exception {
    MonitoredDataItem item = createAllowedItemWithQueuedValues();
    var accessEpoch = new AtomicLong();
    when(session.getAccessEpoch()).thenAnswer(invocation -> accessEpoch.get());

    // Looking up this refresh's answer stands in for the moment the later ActivateSession commits
    // its identity and applies its own answer, under which a value is then queued.
    var staleAnswers =
        new HashMap<ReadValueId, AccessResult>(
            Map.of(itemToMonitor, AccessResult.DENIED_USER_ACCESS)) {
          @Override
          public AccessResult get(Object key) {
            accessEpoch.incrementAndGet();
            item.setReadAccessResultAfterSessionChange(AccessResult.ALLOWED);
            item.setValue(new DataValue(new Variant(42)));
            return super.get(key);
          }
        };
    when(accessController.checkReadAccess(eq(session), anyList())).thenReturn(staleAnswers);

    manager.refreshReadAccess();

    assertEquals(AccessResult.ALLOWED, item.getReadAccessResult());
    assertEquals(List.of(42), drain(item).stream().map(value -> value.value().value()).toList());
  }

  private MonitoredDataItem createAllowedItem() throws Exception {
    when(accessController.checkReadAccess(eq(session), anyList()))
        .thenReturn(Map.of(itemToMonitor, AccessResult.ALLOWED));
    manager.createMonitoredItems(context, createRequest(createItem()));

    return assertInstanceOf(
        MonitoredDataItem.class, ownedMonitoredItems.values().iterator().next());
  }

  private MonitoredDataItem createAllowedItemWithQueuedValues(int... values) throws Exception {
    when(addressSpaceManager.onCreateDataItem(eq(itemToMonitor), any(), any()))
        .thenReturn(new RevisedDataItemParameters(100.0, uint(10)));
    MonitoredDataItem item = createAllowedItem();

    for (int value : values) {
      item.setValue(new DataValue(new Variant(value)));
    }

    return item;
  }

  private static List<DataValue> drain(MonitoredDataItem item) {
    var notifications = new ArrayList<UaStructuredType>();
    item.getNotifications(notifications, Integer.MAX_VALUE);

    return notifications.stream().map(n -> ((MonitoredItemNotification) n).getValue()).toList();
  }

  private MonitoredItemCreateRequest createItem() {
    return new MonitoredItemCreateRequest(
        itemToMonitor,
        MonitoringMode.Reporting,
        new MonitoringParameters(uint(1), 100.0, null, uint(1), true));
  }

  private static CreateMonitoredItemsRequest createRequest(
      MonitoredItemCreateRequest... itemsToCreate) {

    RequestHeader header =
        new RequestHeader(NodeId.NULL_VALUE, DateTime.now(), uint(1), uint(0), null, uint(0), null);

    return new CreateMonitoredItemsRequest(
        header, SUBSCRIPTION_ID, TimestampsToReturn.Both, itemsToCreate);
  }

  @SuppressWarnings("unchecked")
  private static Map<UInteger, Subscription> subscriptions(SubscriptionManager manager)
      throws ReflectiveOperationException {

    Field field = SubscriptionManager.class.getDeclaredField("subscriptions");
    field.setAccessible(true);
    return (Map<UInteger, Subscription>) field.get(manager);
  }
}
