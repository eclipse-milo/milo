/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.subscriptions;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.ScriptableSubscriptionServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.DataChangeNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionAcknowledgement;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

/**
 * What {@code PublishingManager} does with SubscriptionAcknowledgements once it has queued them.
 *
 * <p>An acknowledgement is the client's permission for the Server to forget a NotificationMessage:
 * Part 4 §5.14.5.2 says of subscriptionAcknowledgements that "the Server may delete the Message
 * with this sequence number from its retransmission queue", and §5.14.7.1 that "the Client should
 * acknowledge all Messages in this list for which it will not request retransmission". So an
 * acknowledgement must be sent for every NotificationMessage the client actually has, and must be
 * sent exactly once it can be — never for a message the client does not have, and never
 * <i>abandoned</i>, because an acknowledgement that is dropped leaves the Server holding a message
 * forever (until its retransmission queue evicts it) and leaves availableSequenceNumbers growing.
 *
 * <p>{@code sendPublishRequest} drains the queued acknowledgements into the outgoing PublishRequest
 * and clears them. That hands ownership of them to a single network request, and the two nested
 * classes below cover the two ways the Server's answer to that request can contradict the client's
 * assumption that they arrived:
 *
 * <ol>
 *   <li>the request fails outright, so the acknowledgements it carried never took effect;
 *   <li>the request succeeds but the response's per-acknowledgement results say the Server rejected
 *       one of them (Part 4 §5.14.5.2: "List of results for the acknowledgements... The size and
 *       order of the list matches the size and order of the subscriptionAcknowledgements request
 *       parameter").
 * </ol>
 */
public class PublishAcknowledgementTest {

  /** How long to wait for something that must happen. */
  private static final long AWAIT_TIMEOUT_MILLIS = 10_000;

  /**
   * How long to wait for a client that has lost an acknowledgement to send it again, and for a
   * client that has been told an acknowledgement failed to say so. A client that re-queues a lost
   * acknowledgement puts it on the very next PublishRequest, which is sent within milliseconds of
   * the failed one completing, so this is generous by three orders of magnitude.
   */
  private static final long RECOVERY_WINDOW_MILLIS = 5_000;

  /**
   * Long enough that nothing times out on its own, so a parked Publish request stays parked and any
   * failure observed below is scripted rather than incidental.
   */
  private static final long REQUEST_TIMEOUT_MILLIS = 60_000;

  /**
   * The client keeps one more PublishRequest in flight than it has Subscriptions, so a single
   * Subscription means two.
   */
  private static final int PIPELINE_DEPTH = 2;

  /** Distinctive text used to prove the log capture is observing this JVM's SLF4J output. */
  private static final String CAPTURE_PROBE = "publish-acknowledgement-log-capture-probe";

  /**
   * A PublishRequest can fail — a timeout, a dropped connection, a transient ServiceFault — and the
   * acknowledgements it was carrying fail with it. They were removed from the client's queue when
   * the request was built, so unless they are put back the client has silently decided never to
   * acknowledge those NotificationMessages: the Server keeps them in its retransmission queue until
   * it evicts them, and re-advertises them in availableSequenceNumbers on every subsequent
   * PublishResponse.
   *
   * <p>The failure modelled here is the strong one — the request is failed <i>before</i> the Server
   * records what it carried, so the acknowledgement is genuinely lost in flight and the Server
   * never saw it. That is the case where re-sending is unambiguously required; the weaker case (the
   * Server processed the acknowledgement and the response was lost on the way back) merely costs a
   * duplicate acknowledgement, which the Server answers with a per-acknowledgement result rather
   * than a fault.
   */
  @Nested
  class AcknowledgementLostWithItsPublishRequest {

    @Test
    void acknowledgementIsSentAgainAfterThePublishRequestCarryingItFails() throws Exception {
      try (var fixture = new Fixture()) {
        fixture.loseNextPublishRequestCarryingAcknowledgement(1);

        fixture.sendDataChange(1, uint(1));

        assertTrue(fixture.awaitDelivered(1), "NotificationMessage 1 was never delivered");
        assertTrue(
            fixture.awaitAcknowledgementLost(),
            "no PublishRequest carrying the acknowledgement for sequence 1 ever reached the"
                + " Server, so the scenario under test did not happen");

        // Keep the Publish pipeline turning so a re-queued acknowledgement has requests to ride on
        // whether it is restored when the failure is handled or only when the next response is.
        fixture.sendDataChange(2, uint(1), uint(2));
        assertTrue(fixture.awaitDelivered(2), "NotificationMessage 2 was never delivered");

        assertTrue(
            fixture.awaitAcknowledged(1, RECOVERY_WINDOW_MILLIS),
            "NotificationMessage 1 was received and delivered, but the only acknowledgement for it"
                + " went out on a PublishRequest that failed and was never sent again: the Server"
                + " is left holding a message the client already has, and re-advertising it in"
                + " availableSequenceNumbers, until its retransmission queue evicts it");
      }
    }

    /**
     * The control that keeps the assertion above from passing vacuously: the identical script with
     * nothing failed. If the client did not acknowledge sequence 1 here either, the test above
     * would be measuring the fixture rather than the loss.
     */
    @Test
    void acknowledgementIsSentWhenThePublishRequestCarryingItSucceeds() throws Exception {
      try (var fixture = new Fixture()) {
        fixture.sendDataChange(1, uint(1));

        assertTrue(fixture.awaitDelivered(1), "NotificationMessage 1 was never delivered");

        fixture.sendDataChange(2, uint(1), uint(2));
        assertTrue(fixture.awaitDelivered(2), "NotificationMessage 2 was never delivered");

        assertTrue(
            fixture.awaitAcknowledged(1, RECOVERY_WINDOW_MILLIS),
            "control: with no induced failure the acknowledgement for sequence 1 must reach the"
                + " Server");
      }
    }
  }

  /**
   * Part 4 §5.14.5.2 gives the PublishResponse a results array: "List of results for the
   * acknowledgements. The size and order of the list matches the size and order of the
   * subscriptionAcknowledgements request parameter." It is how a Server reports
   * Bad_SequenceNumberUnknown or Bad_SubscriptionIdInvalid for an individual acknowledgement while
   * the Publish call itself succeeds.
   *
   * <p>{@code PublishingManager} never reads it, so an acknowledgement the Server refused is
   * indistinguishable from one it accepted. §5.14.5.2 also says of availableSequenceNumbers that
   * "this information is for diagnostic purpose and Clients should log differences to the expected
   * sequence numbers" — the same expectation applies to a refused acknowledgement, and today
   * nothing at all is emitted.
   */
  @Nested
  class AcknowledgementResults {

    /**
     * There is no listener, status, or other API surface on which a refused acknowledgement could
     * be observed, so this asserts on what the client logs. It therefore requires the diagnostic to
     * be logged at a level enabled by this module's {@code simplelogger.properties} (INFO or
     * above); WARN is the appropriate level for a Server refusing a client's acknowledgement.
     */
    @Test
    void acknowledgementRefusedByTheServerIsReported() throws Exception {
      try (var fixture = new Fixture()) {
        fixture.sendDataChange(1, uint(1));

        assertTrue(fixture.awaitDelivered(1), "NotificationMessage 1 was never delivered");
        assertTrue(
            fixture.awaitAcknowledged(1, AWAIT_TIMEOUT_MILLIS),
            "the client never acknowledged sequence 1, so there is no acknowledgement for the"
                + " Server to refuse");

        try (var stderr = new StderrCapture()) {
          // Control for the capture itself: unless a WARN emitted through this JVM's SLF4J binding
          // is visible here, the absence asserted below would prove nothing at all.
          LoggerFactory.getLogger(PublishAcknowledgementTest.class).warn(CAPTURE_PROBE);

          assertTrue(
              awaitTrue(() -> stderr.text().contains(CAPTURE_PROBE), RECOVERY_WINDOW_MILLIS),
              "control: WARN records are not reaching the captured System.err, so this test cannot"
                  + " observe anything the client does or does not log");

          fixture.refuseAcknowledgements(StatusCodes.Bad_SequenceNumberUnknown);

          assertTrue(
              fixture.awaitDelivered(2),
              "the PublishResponse refusing the acknowledgement was never processed");

          assertTrue(
              awaitTrue(
                  () -> stderr.text().contains("Bad_SequenceNumberUnknown"),
                  RECOVERY_WINDOW_MILLIS),
              "the Server refused the acknowledgement for sequence 1 with"
                  + " Bad_SequenceNumberUnknown in the PublishResponse results array, and the"
                  + " client reported nothing: the results array is never read, so no"
                  + " acknowledgement failure is detectable by an application or an operator");
        }
      }
    }
  }

  // region fixture

  /**
   * A running Server whose Publish and Republish responses are scripted, a connected client, and a
   * Subscription with one client-side MonitoredItem whose values are recorded as they are
   * delivered.
   *
   * <p>Construction stops before any NotificationMessage has been sent, so a test can arrange the
   * fate of the first acknowledgement before the notification that produces it.
   */
  private static final class Fixture implements AutoCloseable {

    /** Every value handed to {@code onDataReceived}, in delivery order. */
    private final List<Integer> deliveredValues = Collections.synchronizedList(new ArrayList<>());

    private final OpcUaServer server;
    private final OpcUaClient client;
    private final LossyPublishServiceSet scriptable;
    private final UInteger subscriptionId;
    private final UInteger clientHandle;

    Fixture() throws Exception {
      TestServer testServer = TestServer.create();
      server = testServer.getServer();

      scriptable = new LossyPublishServiceSet(server);
      for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
        server.addServiceSet(endpoint.getPath(), scriptable);
      }

      server.startup().get();

      client =
          TestClient.create(
              server,
              cfg ->
                  cfg
                      // Long request timeout so parked Publish requests do not time out.
                      .setRequestTimeout(uint(REQUEST_TIMEOUT_MILLIS))
                      // No Session keep-alive traffic: the only requests in flight during a test
                      // are the ones it scripts.
                      .setKeepAliveInterval(uint(REQUEST_TIMEOUT_MILLIS)));
      client.connect();

      var subscription = new OpcUaSubscription(client);
      subscription.setSubscriptionListener(
          new OpcUaSubscription.SubscriptionListener() {
            @Override
            public void onDataReceived(
                OpcUaSubscription subscription,
                List<OpcUaMonitoredItem> items,
                List<DataValue> values) {

              values.forEach(value -> deliveredValues.add((Integer) value.getValue().getValue()));
            }
          });
      subscription.create();

      subscriptionId = subscription.getSubscriptionId().orElseThrow();

      // Client-side only: addMonitoredItem() assigns the ClientHandle the notification fan-out
      // looks values up by, which is all a scripted notification needs.
      var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      subscription.addMonitoredItem(item);
      clientHandle = item.getClientHandle().orElseThrow();

      assertTrue(
          awaitTrue(
              () -> scriptable.getParkedRequestCount() >= PIPELINE_DEPTH, AWAIT_TIMEOUT_MILLIS),
          "the client did not fill its Publish pipeline");
    }

    /**
     * Answer a parked Publish request with a data-change NotificationMessage carrying {@code
     * sequenceNumber} as its sequence number and as the value of its MonitoredItem.
     */
    void sendDataChange(long sequenceNumber, UInteger... available) {
      scriptable.enqueueDataChange(
          subscriptionId, sequenceNumber, notifications((int) sequenceNumber), available);
    }

    /**
     * Arrange for the next PublishRequest carrying an acknowledgement of {@code sequenceNumber} to
     * fail before the Server records or answers it.
     */
    void loseNextPublishRequestCarryingAcknowledgement(long sequenceNumber) {
      scriptable.loseNextRequestCarrying(subscriptionId, uint(sequenceNumber));
    }

    boolean awaitAcknowledgementLost() throws Exception {
      return scriptable.awaitRequestLost(AWAIT_TIMEOUT_MILLIS);
    }

    /**
     * Answer every PublishRequest still parked, refusing each acknowledgement it carries with
     * {@code statusCode} in the positionally matching entry of the response's results array.
     *
     * <p>The request carrying an acknowledgement is answered with a data-change NotificationMessage
     * so that its delivery is an observable barrier proving the response was processed; the others
     * get a keep-alive that leaves the sequence accounting where it was.
     */
    void refuseAcknowledgements(long statusCode) {
      for (int i = 0; i < PIPELINE_DEPTH; i++) {
        scriptable.enqueue(request -> refusingResponse(request, statusCode));
      }
    }

    private CompletableFuture<PublishResponse> refusingResponse(
        PublishRequest request, long statusCode) {

      SubscriptionAcknowledgement[] acks = request.getSubscriptionAcknowledgements();
      int ackCount = acks != null ? acks.length : 0;

      var results = new StatusCode[ackCount];
      Arrays.fill(results, StatusCode.of(statusCode));

      // Part 4 §5.14.1.1: a keep-alive carries "the sequence number of the next NotificationMessage
      // that is to be sent", so a keep-alive numbered 2 accounts for nothing beyond sequence 1.
      ExtensionObject[] notificationData = ackCount > 0 ? encodedNotifications(2) : null;
      UInteger[] available = ackCount > 0 ? new UInteger[] {uint(2)} : new UInteger[0];

      return CompletableFuture.completedFuture(
          scriptable.buildPublishResponse(
              request, subscriptionId, 2, notificationData, available, false, results));
    }

    boolean awaitDelivered(int value) throws Exception {
      return awaitTrue(() -> deliveredValues.contains(value), AWAIT_TIMEOUT_MILLIS);
    }

    boolean awaitAcknowledged(long sequenceNumber, long timeoutMillis) throws Exception {
      return awaitTrue(
          () ->
              scriptable.getReceivedAcknowledgements().stream()
                  .anyMatch(
                      ack ->
                          subscriptionId.equals(ack.getSubscriptionId())
                              && ack.getSequenceNumber().longValue() == sequenceNumber),
          timeoutMillis);
    }

    /** A DataChangeNotification carrying {@code value} for this Fixture's MonitoredItem. */
    private List<MonitoredItemNotification> notifications(int value) {
      return List.of(
          new MonitoredItemNotification(clientHandle, new DataValue(Variant.ofInt32(value))));
    }

    private ExtensionObject[] encodedNotifications(int value) {
      return new ExtensionObject[] {
        scriptable.encode(
            new DataChangeNotification(
                notifications(value).toArray(MonitoredItemNotification[]::new), null))
      };
    }

    @Override
    public void close() throws Exception {
      scriptable.failParkedRequests(StatusCodes.Bad_NoSubscription);
      try {
        client.disconnectAsync().get(5, TimeUnit.SECONDS);
      } finally {
        server.shutdown().get(10, TimeUnit.SECONDS);
      }
    }
  }

  /**
   * A {@link ScriptableSubscriptionServiceSet} that can drop one PublishRequest on the floor.
   *
   * <p>The armed request is failed before {@code super.onPublish} records its acknowledgements, so
   * from the Server's point of view the request — and everything it was carrying — never arrived.
   * That is what makes {@code getReceivedAcknowledgements()} a sound record of what the client
   * actually managed to acknowledge.
   */
  private static final class LossyPublishServiceSet extends ScriptableSubscriptionServiceSet {

    private final AtomicReference<SubscriptionAcknowledgement> target = new AtomicReference<>();
    private final CountDownLatch lost = new CountDownLatch(1);

    LossyPublishServiceSet(OpcUaServer server) {
      super(server);
    }

    void loseNextRequestCarrying(UInteger subscriptionId, UInteger sequenceNumber) {
      target.set(new SubscriptionAcknowledgement(subscriptionId, sequenceNumber));
    }

    boolean awaitRequestLost(long timeoutMillis) throws InterruptedException {
      return lost.await(timeoutMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public CompletableFuture<PublishResponse> onPublish(
        ServiceRequestContext context, PublishRequest request) {

      SubscriptionAcknowledgement armed = target.get();

      if (armed != null && carries(request, armed) && target.compareAndSet(armed, null)) {
        lost.countDown();

        return CompletableFuture.failedFuture(new UaException(StatusCodes.Bad_Timeout));
      }

      return super.onPublish(context, request);
    }

    private static boolean carries(PublishRequest request, SubscriptionAcknowledgement armed) {
      SubscriptionAcknowledgement[] acks = request.getSubscriptionAcknowledgements();

      if (acks == null) {
        return false;
      }

      return Arrays.stream(acks)
          .anyMatch(
              ack ->
                  armed.getSubscriptionId().equals(ack.getSubscriptionId())
                      && armed.getSequenceNumber().equals(ack.getSequenceNumber()));
    }
  }

  /**
   * Captures everything written to {@link System#err} while installed, passing it through to the
   * original stream so the output a failing test needs is not swallowed.
   *
   * <p>slf4j-simple, the SLF4J binding on this module's test classpath, is configured with its
   * default uncached {@code System.err} target and resolves {@code System.err} on every write, so
   * installing this stream is enough to observe log records emitted afterwards.
   */
  private static final class StderrCapture implements AutoCloseable {

    private final ByteArrayOutputStream captured = new ByteArrayOutputStream();
    private final PrintStream original = System.err;
    private final PrintStream installed;

    StderrCapture() {
      installed = new PrintStream(new Tee(), true, StandardCharsets.UTF_8);

      System.setErr(installed);
    }

    String text() {
      synchronized (captured) {
        return captured.toString(StandardCharsets.UTF_8);
      }
    }

    @Override
    public void close() {
      System.setErr(original);
      installed.flush();
    }

    private final class Tee extends OutputStream {

      @Override
      public void write(int b) {
        synchronized (captured) {
          captured.write(b);
        }
        original.write(b);
      }

      @Override
      public void write(byte[] b, int off, int len) {
        synchronized (captured) {
          captured.write(b, off, len);
        }
        original.write(b, off, len);
      }
    }
  }

  /** Polls {@code condition} until it holds or {@code timeoutMillis} elapses. */
  private static boolean awaitTrue(ThrowingBooleanSupplier condition, long timeoutMillis)
      throws Exception {

    long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMillis);

    while (System.nanoTime() < deadline) {
      if (condition.get()) {
        return true;
      }
      Thread.sleep(25);
    }

    return condition.get();
  }

  @FunctionalInterface
  private interface ThrowingBooleanSupplier {
    boolean get() throws Exception;
  }

  // endregion
}
