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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.client.subscriptions.MonitoredItemSynchronizationException;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode.UaVariableNodeBuilder;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Read access to a data MonitoredItem is enforced while the item is sampled, not only when it is
 * created. The harness client connects anonymously over SecurityPolicy None.
 */
public class MonitoredItemReadAccessTest extends AbstractClientServerTest {

  private static final int INITIAL_VALUE = 42;

  private UaVariableNode revocable;
  private OpcUaSubscription subscription;

  @Override
  protected void configureTestNamespace(TestNamespace namespace) {
    namespace.configure(
        (context, nodeManager) -> {
          // Readable Variable that this user may not read.
          addInt32Variable(
              context,
              "NotUserReadable",
              b -> {
                b.setAccessLevel(AccessLevel.READ_WRITE);
                b.setUserAccessLevel(AccessLevel.WRITE_ONLY);
              });

          // Variable that nobody may read.
          addInt32Variable(
              context,
              "WriteOnly",
              b -> {
                b.setAccessLevel(AccessLevel.WRITE_ONLY);
                b.setUserAccessLevel(AccessLevel.WRITE_ONLY);
              });

          // Readable when the item is created; read access is changed by the test.
          revocable =
              addInt32Variable(
                  context,
                  "Revocable",
                  b -> {
                    b.setAccessLevel(AccessLevel.READ_WRITE);
                    b.setUserAccessLevel(AccessLevel.READ_WRITE);
                  });

          // Readable, but only over an encrypted channel.
          addInt32Variable(
              context,
              "EncryptionRequired",
              b -> {
                b.setAccessLevel(AccessLevel.READ_WRITE);
                b.setUserAccessLevel(AccessLevel.READ_WRITE);
                b.setAccessRestrictions(
                    AccessRestrictionType.of(AccessRestrictionType.Field.EncryptionRequired));
              });
        });
  }

  @BeforeEach
  void createSubscription() throws UaException {
    subscription = new OpcUaSubscription(client);
    subscription.setPublishingInterval(100.0);
    subscription.create();
  }

  @AfterEach
  void deleteSubscription() throws UaException {
    subscription.delete();

    revocable.setUserAccessLevel(AccessLevel.toValue(AccessLevel.READ_WRITE));
    revocable.setValue(new DataValue(new Variant(INITIAL_VALUE)));
  }

  /**
   * Part 4 §5.13.2.1: when the user is denied read access to the item, CreateMonitoredItems
   * succeeds and Bad_UserAccessDenied is delivered in the Publish response instead.
   */
  @Test
  void itemOnVariableTheUserCannotReadIsCreatedAndReportsBadUserAccessDenied() throws Exception {
    var values = new LinkedBlockingQueue<DataValue>();

    OpcUaMonitoredItem item = monitor("NotUserReadable", values);

    assertEquals(StatusCode.GOOD, item.getCreateResult().orElseThrow());

    DataValue first = values.poll(5, TimeUnit.SECONDS);
    assertNotNull(first, "the denial must be delivered as a notification");
    assertEquals(StatusCodes.Bad_UserAccessDenied, first.statusCode().getValue());
  }

  /**
   * Part 4 §7.38.2 defines Bad_NotReadable as "the access level does not allow reading or
   * subscribing to the Node", so a Variable whose AccessLevel lacks CurrentRead reports that code
   * rather than Bad_UserAccessDenied.
   */
  @Test
  void itemOnVariableNobodyCanReadIsCreatedAndReportsBadNotReadable() throws Exception {
    var values = new LinkedBlockingQueue<DataValue>();

    OpcUaMonitoredItem item = monitor("WriteOnly", values);

    assertEquals(StatusCode.GOOD, item.getCreateResult().orElseThrow());

    DataValue first = values.poll(5, TimeUnit.SECONDS);
    assertNotNull(first, "the denial must be delivered as a notification");
    assertEquals(StatusCodes.Bad_NotReadable, first.statusCode().getValue());
  }

  /**
   * Part 4 §5.13.2.1: access rights that change after CreateMonitoredItems are reflected in the
   * Publish response, and data flows again once read access is restored.
   */
  @Test
  void revokingAndRestoringReadAccessIsReportedThroughTheSubscription() throws Exception {
    var values = new LinkedBlockingQueue<DataValue>();

    OpcUaMonitoredItem item = monitor("Revocable", values);

    assertEquals(StatusCode.GOOD, item.getCreateResult().orElseThrow());

    DataValue initial = values.poll(5, TimeUnit.SECONDS);
    assertNotNull(initial);
    assertEquals(StatusCode.GOOD, initial.statusCode());
    assertEquals(INITIAL_VALUE, initial.value().value());

    revocable.setUserAccessLevel(AccessLevel.toValue(AccessLevel.WRITE_ONLY));

    DataValue denied = values.poll(5, TimeUnit.SECONDS);
    assertNotNull(denied, "removing read access must produce a notification");
    assertEquals(StatusCodes.Bad_UserAccessDenied, denied.statusCode().getValue());

    revocable.setValue(new DataValue(new Variant(INITIAL_VALUE + 1)));

    assertNull(
        values.poll(1, TimeUnit.SECONDS),
        "a value change must not be reported while read access is denied");

    revocable.setUserAccessLevel(AccessLevel.toValue(AccessLevel.READ_WRITE));

    DataValue restored = values.poll(5, TimeUnit.SECONDS);
    assertNotNull(restored, "restoring read access must resume data");
    assertEquals(StatusCode.GOOD, restored.statusCode());
    assertEquals(INITIAL_VALUE + 1, restored.value().value());
  }

  /**
   * Only read-access denials are deferred to the Publish response. An AccessRestrictions denial is
   * about the channel, not the user, and still fails the item in CreateMonitoredItems.
   */
  @Test
  void accessRestrictionDenialStillFailsTheCreateRequest() {
    OpcUaMonitoredItem item = OpcUaMonitoredItem.newDataItem(newNodeId("EncryptionRequired"));
    subscription.addMonitoredItem(item);

    assertThrows(
        MonitoredItemSynchronizationException.class, subscription::synchronizeMonitoredItems);

    assertEquals(
        StatusCodes.Bad_SecurityModeInsufficient, item.getCreateResult().orElseThrow().getValue());
  }

  private OpcUaMonitoredItem monitor(String name, Queue<DataValue> values) throws Exception {
    OpcUaMonitoredItem item = OpcUaMonitoredItem.newDataItem(newNodeId(name));
    item.setSamplingInterval(100.0);
    item.setDataValueListener((i, v) -> values.add(v));

    subscription.addMonitoredItem(item);
    subscription.synchronizeMonitoredItems();

    return item;
  }

  private UaVariableNode addInt32Variable(
      UaNodeContext context, String name, Consumer<UaVariableNodeBuilder> customizer) {

    return UaVariableNode.build(
        context,
        b -> {
          b.setNodeId(newNodeId(name));
          b.setBrowseName(newQualifiedName(name));
          b.setDisplayName(LocalizedText.english(name));
          b.setDataType(NodeIds.Int32);
          b.setValue(new DataValue(new Variant(INITIAL_VALUE)));
          customizer.accept(b);
          return b.buildAndAdd();
        });
  }
}
