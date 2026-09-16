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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.LongStream;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
import org.eclipse.milo.opcua.sdk.client.UaSession;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.NotificationMessage;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RepublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RepublishResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.util.TaskQueue;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.junit.jupiter.api.Test;

/** Session-activation ownership of reconnect recovery state in {@link PublishingManager}. */
public class PublishingManagerSessionIsolationTest {

  private static final long AWAIT_TIMEOUT_SECONDS = 5L;

  /**
   * A recovery from activation A can remain queued when replacement Session B transfers the same
   * Subscription. A must not consume B's advertised sequence numbers; otherwise B falls back to a
   * blind drain and can lose messages the TransferResult proved were recoverable.
   */
  @Test
  void staleRecoveryDoesNotConsumeReplacementSessionsTransferResult() throws Exception {
    Fixture fixture = new Fixture(1, 1);
    UaSession staleSession = fixture.newSession();
    UaSession replacementSession = fixture.newSession();

    fixture.setLastSequenceNumber(6L);
    fixture.setActivation(1L);
    fixture.setResponder(request -> response(request.getRetransmitSequenceNumber().longValue()));

    fixture.manager.notifySubscriptionTransferred(
        replacementSession, fixture.subscriptionId, new UInteger[] {uint(7)});

    // Models another activation callback being counted before this Session's callback runs. The
    // TransferResult belongs to the Session itself; predicting "current + 1" at notification time
    // would stamp it with activation 2 and make activation 3 ignore it.
    fixture.setActivation(3L);
    fixture.republishUntilUnavailable(staleSession, 1L);
    fixture.republishUntilUnavailable(replacementSession, 3L);

    assertEquals(
        List.of(7L),
        fixture.republishRequests,
        "the stale recovery consumed the replacement Session's TransferResult, so the replacement"
            + " performed a blind extra Republish instead of following its advertised set");
  }

  /**
   * Milo can permit more than 64 outstanding PublishRequests. Part 4 §6.7 recovery must cover every
   * response that pipeline could have lost and still make the extra request that receives
   * Bad_MessageNotAvailable; otherwise Publish resumes before the retransmission queue is drained.
   */
  @Test
  void blindRecoveryCoversConfiguredPipelineDepthBeyond64AndTerminationProbe() throws Exception {
    int pipelineDepth = 65;
    Fixture fixture = new Fixture(pipelineDepth - 1, pipelineDepth);
    UaSession session = fixture.newSession();

    fixture.capturePermittedPipelineDepth();

    // The recovery bound belongs to the pre-disconnect pipeline. Removing all but the Subscription
    // under test after the outage must not shrink it to the new two-request natural target.
    fixture.retainOnlyPrimarySubscription();
    fixture.setActivation(1L);
    fixture.setResponder(
        request -> {
          long sequenceNumber = request.getRetransmitSequenceNumber().longValue();

          return sequenceNumber <= pipelineDepth
              ? response(sequenceNumber)
              : CompletableFuture.failedFuture(
                  new UaException(StatusCodes.Bad_MessageNotAvailable));
        });

    fixture.republishUntilUnavailable(session, 1L);

    List<Long> expected = LongStream.rangeClosed(1L, pipelineDepth + 1L).boxed().toList();

    assertEquals(
        expected,
        fixture.republishRequests,
        "a blind reconnect drain must request all 65 NotificationMessages a configured 65-deep"
            + " Publish pipeline could have lost, then request sequence 66 to receive the"
            + " Bad_MessageNotAvailable termination response");
  }

  /**
   * Bad_TooManyPublishRequests describes the Session that rejected the request. A delayed failure
   * from activation A must not install a ceiling after activation B has reset its independent
   * Publish pipeline state.
   */
  @Test
  void oldActivationCannotClampReplacementSessionsPublishCeiling() throws Exception {
    Fixture fixture = new Fixture(1, 2);
    fixture.setActivation(2L);

    fixture.handlePublishFailure(
        new UaException(StatusCodes.Bad_TooManyPublishRequests), new AtomicLong(1L), 1L);

    fixture.startPublishing(2L);
    assertEquals(
        2,
        fixture.publishResponses.size(),
        "A stale refusal must leave the replacement Session's two-request pipeline open");
  }

  /** A delayed successful response cannot pay down a replacement Session's learned ceiling. */
  @Test
  void oldActivationCannotAdvanceReplacementSessionsCeilingCooldown() throws Exception {
    Fixture fixture = new Fixture(1, 2);
    fixture.setActivation(2L);

    fixture.handlePublishFailure(
        new UaException(StatusCodes.Bad_TooManyPublishRequests), new AtomicLong(2L), 2L);

    fixture.releasePendingPublish(new AtomicLong(1L), 1L);

    fixture.startPublishing(2L);
    assertEquals(1, fixture.publishResponses.size(), "The current refusal must limit the pipeline");

    // Recovery probes cost eight successful round trips. A stale success must not bring the
    // first probe forward to the seventh response from the replacement Session.
    for (int i = 0; i < 7; i++) {
      fixture.completePublish(i);
      assertEquals(i + 2, fixture.publishResponses.size(), "A stale success advanced the probe");
    }
    fixture.completePublish(7);
    assertEquals(
        10, fixture.publishResponses.size(), "The eighth current success must allow a probe");
  }

  private static CompletableFuture<UaResponseMessageType> response(long sequenceNumber) {
    var notificationMessage =
        new NotificationMessage(uint(sequenceNumber), DateTime.now(), new ExtensionObject[0]);

    return CompletableFuture.completedFuture(
        new RepublishResponse(mock(ResponseHeader.class), notificationMessage));
  }

  /** A minimal, synchronously executed PublishingManager fixture for activation-boundary tests. */
  private static final class Fixture {

    private final List<Long> republishRequests = new ArrayList<>();
    private final List<CompletableFuture<UaResponseMessageType>> publishResponses =
        new ArrayList<>();

    private final AtomicReference<
            Function<RepublishRequest, CompletableFuture<UaResponseMessageType>>>
        responder = new AtomicReference<>();

    private final OpcUaClient client = mock(OpcUaClient.class);
    private final PublishingManager manager;
    private final Class<?> subscriptionDetailsClass;
    private final Object primaryDetails;
    private final UInteger subscriptionId = uint(1);

    Fixture(int subscriptionCount, int maxPendingPublishRequests) throws Exception {
      OpcUaClientConfig config = mock(OpcUaClientConfig.class);
      when(config.getMaxPendingPublishRequests()).thenReturn(uint(maxPendingPublishRequests));
      when(config.getRequestTimeout()).thenReturn(uint(5000));
      when(client.getConfig()).thenReturn(config);
      OpcClientTransport transport = mock(OpcClientTransport.class);
      OpcClientTransportConfig transportConfig = mock(OpcClientTransportConfig.class);
      when(client.getTransport()).thenReturn(transport);
      when(transport.getConfig()).thenReturn(transportConfig);
      ExecutorService executor = mock(ExecutorService.class);
      doAnswer(
              invocation -> {
                invocation.<Runnable>getArgument(0).run();
                return null;
              })
          .when(executor)
          .execute(any(Runnable.class));
      when(transportConfig.getExecutor()).thenReturn(executor);
      RequestHeader publishHeader = mock(RequestHeader.class);
      when(publishHeader.getRequestHandle()).thenReturn(uint(1));
      when(client.newRequestHeader(any(NodeId.class), any(UInteger.class)))
          .thenReturn(publishHeader);
      when(client.newRequestHeader(any(NodeId.class))).thenReturn(mock(RequestHeader.class));
      when(client.getSessionAsync()).thenReturn(new CompletableFuture<>());
      when(client.sendRequestAsync(any()))
          .thenAnswer(
              invocation -> {
                if (invocation.getArgument(0) instanceof PublishRequest) {
                  var future = new CompletableFuture<UaResponseMessageType>();
                  publishResponses.add(future);
                  return future;
                }
                RepublishRequest request = invocation.getArgument(0);
                republishRequests.add(request.getRetransmitSequenceNumber().longValue());

                return responder.get().apply(request);
              });

      manager = new PublishingManager(client);

      subscriptionDetailsClass =
          Class.forName(PublishingManager.class.getName() + "$SubscriptionDetails");

      var details = new HashMap<UInteger, Object>();

      primaryDetails = newSubscriptionDetails(subscriptionId);

      for (int i = 0; i < subscriptionCount; i++) {
        UInteger id = uint(i + 1L);
        details.put(id, i == 0 ? primaryDetails : newSubscriptionDetails(id));
      }
      setSubscriptionDetails(Map.copyOf(details));
    }

    OpcUaSession newSession() {
      OpcUaSession session = mock(OpcUaSession.class);
      when(session.getAuthenticationToken()).thenReturn(NodeId.NULL_VALUE);
      when(session.getSessionId()).thenReturn(new NodeId(1, System.identityHashCode(session)));

      return session;
    }

    void capturePermittedPipelineDepth() throws Exception {
      Method method = PublishingManager.class.getDeclaredMethod("maybeSendPublishRequests");
      method.setAccessible(true);
      method.invoke(manager);
    }

    void retainOnlyPrimarySubscription() throws Exception {
      setSubscriptionDetails(Map.of(subscriptionId, primaryDetails));
    }

    void setResponder(
        Function<RepublishRequest, CompletableFuture<UaResponseMessageType>> responder) {

      this.responder.set(responder);
    }

    @SuppressWarnings("unchecked")
    void setActivation(long activation) throws Exception {
      Field field = PublishingManager.class.getDeclaredField("sessionActivations");
      field.setAccessible(true);

      ((AtomicLong) field.get(manager)).set(activation);

      Field ceilingField = PublishingManager.class.getDeclaredField("pendingPublishCeiling");
      ceilingField.setAccessible(true);

      AtomicReference<Object> ceiling = (AtomicReference<Object>) ceilingField.get(manager);
      Method none = ceiling.get().getClass().getDeclaredMethod("none", long.class);
      none.setAccessible(true);
      ceiling.set(none.invoke(null, activation));
    }

    void setLastSequenceNumber(long sequenceNumber) throws Exception {
      Field field = subscriptionDetailsClass.getDeclaredField("lastSequenceNumber");
      field.setAccessible(true);
      field.setLong(primaryDetails, sequenceNumber);
    }

    void republishUntilUnavailable(UaSession session, long activation) throws Exception {
      Method method =
          PublishingManager.class.getDeclaredMethod(
              "republishUntilUnavailable", UaSession.class, subscriptionDetailsClass, long.class);
      method.setAccessible(true);

      CompletableFuture<?> future =
          (CompletableFuture<?>) method.invoke(manager, session, primaryDetails, activation);
      future.get(AWAIT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    void handlePublishFailure(Throwable failure, AtomicLong pendingCount, long activation)
        throws Exception {

      Method method =
          PublishingManager.class.getDeclaredMethod(
              "handlePublishFailure",
              Throwable.class,
              UInteger.class,
              AtomicLong.class,
              List.class,
              long.class,
              long.class);
      method.setAccessible(true);
      method.invoke(manager, failure, uint(1), pendingCount, List.of(), 0L, activation);
    }

    void startPublishing(long activation) throws Exception {
      OpcUaSession session = newSession();
      when(client.getSessionAsync()).thenReturn(CompletableFuture.completedFuture(session));
      Method method =
          PublishingManager.class.getDeclaredMethod(
              "resumePublishing", long.class, UaSession.class);
      method.setAccessible(true);
      method.invoke(manager, activation, session);
    }

    void completePublish(int index) {
      // An unregistered Subscription's response still releases its Session's permit and refills
      // the pipeline, without involving notification delivery in these activation tests.
      publishResponses
          .get(index)
          .complete(
              new PublishResponse(
                  mock(ResponseHeader.class),
                  uint(99),
                  new UInteger[0],
                  false,
                  new NotificationMessage(uint(1), DateTime.now(), new ExtensionObject[0]),
                  new StatusCode[0],
                  new DiagnosticInfo[0]));
    }

    void releasePendingPublish(AtomicLong pendingCount, long activation) throws Exception {
      Method method =
          PublishingManager.class.getDeclaredMethod(
              "releasePendingPublish", AtomicLong.class, long.class);
      method.setAccessible(true);
      method.invoke(manager, pendingCount, activation);
    }

    private Object newSubscriptionDetails(UInteger id) throws Exception {
      OpcUaSubscription subscription = mock(OpcUaSubscription.class);
      when(subscription.getIncarnation()).thenReturn(1L);
      when(subscription.getDeliveryQueue()).thenReturn(new TaskQueue(Runnable::run));

      Constructor<?> constructor =
          subscriptionDetailsClass.getDeclaredConstructor(
              OpcUaSubscription.class, UInteger.class, java.util.concurrent.Executor.class);
      constructor.setAccessible(true);

      return constructor.newInstance(
          subscription, id, (java.util.concurrent.Executor) Runnable::run);
    }

    private void setSubscriptionDetails(Map<UInteger, Object> details) throws Exception {

      Field field = PublishingManager.class.getDeclaredField("subscriptionDetails");
      field.setAccessible(true);

      field.set(manager, details);
    }
  }
}
