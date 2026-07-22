/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.conditions;

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.model.objects.ShelvedStateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/** Scheduler failure and server-shutdown lifecycle coverage for AlarmCondition shelving. */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ConditionShelvingSchedulerTest extends AbstractClientServerTest {

  private static final double MAX_TIME_SHELVED_MILLIS = 60_000.0;

  private final ControlledScheduledExecutor scheduler = new ControlledScheduledExecutor();
  private final ControlledExecutor executor = new ControlledExecutor();

  private AlarmCondition alarm;
  private NodeId shelvingStateId;
  private NodeId timedShelveMethodId;
  private NodeId unshelveTimeId;

  @Override
  protected TestServer createTestServer() throws Exception {
    return TestServer.create(
        configBuilder -> configBuilder.setExecutor(executor).setScheduledExecutor(scheduler));
  }

  @Override
  protected void configureTestNamespace(TestNamespace namespace) {
    namespace.configure(
        (context, nodeManager) -> {
          var source =
              new UaObjectNode(
                  context,
                  newNodeId("ConditionShelvingSchedulerTest/Source"),
                  newQualifiedName("ConditionShelvingSchedulerTest/Source"),
                  LocalizedText.english("ConditionShelvingSchedulerTest/Source"),
                  LocalizedText.NULL_VALUE,
                  uint(0),
                  uint(0),
                  ubyte(0));

          source.addReference(
              new Reference(
                  source.getNodeId(),
                  NodeIds.HasTypeDefinition,
                  NodeIds.BaseObjectType.expanded(),
                  true));
          source.addReference(
              new Reference(
                  source.getNodeId(),
                  NodeIds.HasComponent,
                  NodeIds.ObjectsFolder.expanded(),
                  Reference.Direction.INVERSE));
          nodeManager.addNode(source);

          try {
            alarm =
                AlarmCondition.create(
                    context,
                    b ->
                        b.nodeId(newNodeId("ConditionShelvingSchedulerTest/Alarm"))
                            .browseName(newQualifiedName("SchedulerTestAlarm"))
                            .conditionSource(source)
                            .severity(ushort(558))
                            .withShelving(Duration.ofMillis((long) MAX_TIME_SHELVED_MILLIS)));
          } catch (UaException e) {
            throw new RuntimeException(e);
          }

          server.getConditionManager().register(alarm);

          ShelvedStateMachineTypeNode shelvingState = requireNonNull(alarm.getShelvingState());
          shelvingStateId = shelvingState.getNodeId();
          timedShelveMethodId =
              requireNonNull(shelvingState.getTimedShelveMethodNode()).getNodeId();
          unshelveTimeId = requireNonNull(shelvingState.getUnshelveTimeNode()).getNodeId();
        });
  }

  @BeforeEach
  void resetAlarmState() throws Exception {
    call(shelvingStateId, NodeIds.ShelvedStateMachineType_Unshelve);
    alarm.setActive(false);
    alarm.setAcked(true);
  }

  @Test
  @Order(1)
  void schedulerRejectionCompletesTransitionAndFallsBackToLazyExpiry() throws Exception {
    alarm.setActive(true);
    ByteString activeEventId = alarm.currentBranch().getLastEventId();

    scheduler.rejectNextSchedule();
    CallMethodResult shelve = call(shelvingStateId, timedShelveMethodId, new Variant(30_000.0));

    assertTrue(requireNonNull(shelve.getStatusCode()).isGood());
    assertEquals("TimedShelved", currentStateText());
    assertEquals(Boolean.TRUE, alarm.getNode().getSuppressedOrShelved());
    assertTrue(alarm.isRetained());
    assertNotEquals(activeEventId, alarm.currentBranch().getLastEventId());
    assertEquals(1, scheduler.getRejectedScheduleCount());

    ShelvingRuntime shelvingRuntime = requireNonNull(alarm.getShelvingRuntime());
    assertFalse(shelvingRuntime.hasExpiryTimerForTesting());
    shelvingRuntime.makeExpiryDueForTesting();

    assertEquals(0.0, readUnshelveTime());
    assertEquals("Unshelved", currentStateText());
    assertEquals(Boolean.FALSE, alarm.getNode().getSuppressedOrShelved());
  }

  @Test
  @Order(2)
  void schedulerCallbackDispatchesExpiryToRegularExecutor() throws Exception {
    alarm.setActive(true);

    scheduler.captureNextSchedule();
    CallMethodResult shelve = call(shelvingStateId, timedShelveMethodId, new Variant(30_000.0));
    assertTrue(requireNonNull(shelve.getStatusCode()).isGood());

    Runnable schedulerCommand = requireNonNull(scheduler.getCapturedCommand());

    executor.captureNextExecution();
    schedulerCommand.run();

    Runnable expiryWork = requireNonNull(executor.getCapturedExecution());
    assertEquals("TimedShelved", currentStateText());

    expiryWork.run();
    assertEquals("Unshelved", currentStateText());
    assertEquals(Boolean.FALSE, alarm.getNode().getSuppressedOrShelved());
  }

  @Test
  @Order(3)
  void serverShutdownCancelsShelvingTimerAndPreventsPostShutdownExpiry() throws Exception {
    alarm.setActive(true);

    scheduler.captureNextSchedule();
    CallMethodResult shelve = call(shelvingStateId, timedShelveMethodId, new Variant(30_000.0));
    assertTrue(requireNonNull(shelve.getStatusCode()).isGood());

    ScheduledFuture<?> expiryTimer = requireNonNull(scheduler.getCapturedSchedule());
    Runnable expiryCommand = requireNonNull(scheduler.getCapturedCommand());
    assertTrue(
        requireNonNull(alarm.getShelvingRuntime()).ownsExpiryTimerForTesting(expiryTimer),
        "the captured task must be this alarm's shelving timer");
    ByteString shelvedEventId = alarm.currentBranch().getLastEventId();

    server.shutdown().get(2, TimeUnit.SECONDS);

    assertTrue(expiryTimer.isCancelled(), "server shutdown must cancel the shelving timer");

    // Simulate an executor that delivers a canceled task late. The shutdown generation guard must
    // still prevent any state transition or event after teardown.
    executor.captureNextExecution();
    expiryCommand.run();
    requireNonNull(executor.getCapturedExecution()).run();
    assertEquals("TimedShelved", currentStateText());
    assertEquals(Boolean.TRUE, alarm.getNode().getSuppressedOrShelved());
    assertEquals(shelvedEventId, alarm.currentBranch().getLastEventId());
  }

  @AfterAll
  @Override
  public void stopClientAndServer() {
    try {
      super.stopClientAndServer();
    } finally {
      scheduler.shutdownNow();
      executor.shutdownNow();
    }
  }

  private CallMethodResult call(NodeId objectId, NodeId methodId, Variant... inputs)
      throws Exception {

    CallResponse response = client.call(List.of(new CallMethodRequest(objectId, methodId, inputs)));

    return requireNonNull(response.getResults())[0];
  }

  private @Nullable String currentStateText() {
    LocalizedText currentState = requireNonNull(alarm.getShelvingState()).getCurrentState();
    return currentState != null ? currentState.text() : null;
  }

  private double readUnshelveTime() throws Exception {
    DataValue value = client.readValue(0.0, TimestampsToReturn.Both, unshelveTimeId);
    assertTrue(requireNonNull(value.getStatusCode()).isGood());
    return (Double) requireNonNull(value.value().value());
  }

  private static final class ControlledScheduledExecutor extends ScheduledThreadPoolExecutor {

    private final AtomicBoolean rejectNextSchedule = new AtomicBoolean(false);
    private final AtomicBoolean captureNextSchedule = new AtomicBoolean(false);
    private final AtomicReference<ScheduledFuture<?>> capturedSchedule = new AtomicReference<>();
    private final AtomicReference<Runnable> capturedCommand = new AtomicReference<>();

    private int rejectedScheduleCount;

    ControlledScheduledExecutor() {
      super(1);
      setRemoveOnCancelPolicy(true);
    }

    void rejectNextSchedule() {
      rejectNextSchedule.set(true);
    }

    void captureNextSchedule() {
      capturedSchedule.set(null);
      capturedCommand.set(null);
      captureNextSchedule.set(true);
    }

    @Nullable ScheduledFuture<?> getCapturedSchedule() {
      return capturedSchedule.get();
    }

    @Nullable Runnable getCapturedCommand() {
      return capturedCommand.get();
    }

    synchronized int getRejectedScheduleCount() {
      return rejectedScheduleCount;
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
      if (rejectNextSchedule.compareAndSet(true, false)) {
        synchronized (this) {
          rejectedScheduleCount++;
        }
        throw new RejectedExecutionException("simulated shelving timer rejection");
      }

      ScheduledFuture<?> future = super.schedule(command, delay, unit);
      if (captureNextSchedule.compareAndSet(true, false)) {
        capturedCommand.set(command);
        capturedSchedule.set(future);
      }
      return future;
    }
  }

  private static final class ControlledExecutor extends ThreadPoolExecutor {

    private final AtomicBoolean captureNextExecution = new AtomicBoolean(false);
    private final AtomicReference<Runnable> capturedExecution = new AtomicReference<>();

    ControlledExecutor() {
      super(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>());
    }

    void captureNextExecution() {
      capturedExecution.set(null);
      captureNextExecution.set(true);
    }

    @Nullable Runnable getCapturedExecution() {
      return capturedExecution.get();
    }

    @Override
    public void execute(Runnable command) {
      if (captureNextExecution.compareAndSet(true, false)) {
        capturedExecution.set(command);
      } else {
        super.execute(command);
      }
    }
  }
}
