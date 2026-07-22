/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.Lifecycle;
import org.eclipse.milo.opcua.sdk.server.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.conditions.ConditionBranch;
import org.eclipse.milo.opcua.sdk.server.conditions.ConditionMethodInterceptor;
import org.eclipse.milo.opcua.sdk.server.conditions.ExclusiveLevelAlarm;
import org.eclipse.milo.opcua.sdk.server.conditions.OffNormalAlarm;
import org.eclipse.milo.opcua.sdk.server.conditions.SystemOffNormalAlarm;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.util.SubscriptionModel;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A namespace demonstrating Part 9 Alarms &amp; Conditions: a simulated "Boiler" area whose
 * temperature drives an {@link ExclusiveLevelAlarm}, a motor whose state drives an {@link
 * OffNormalAlarm}, and a {@link SystemOffNormalAlarm} reporting simulated communication loss with
 * the underlying device (the Part 9 §5.14 underlying-system pattern, driven by application code).
 *
 * <p>Alarms are created with a builder call, registered with the server's ConditionManager so they
 * participate in ConditionRefresh and EventId-keyed dispatch, and become visible to clients through
 * one {@code registerEventNotifier(boiler)} call. Operator methods (Acknowledge, Confirm,
 * AddComment, Enable/Disable, shelving) work from any generic client with no method code here; the
 * interceptor on the temperature alarm shows where applications can observe or veto them.
 */
public class AlarmConditionsNamespace extends ManagedNamespaceWithLifecycle {

  public static final String NAMESPACE_URI = "urn:eclipse:milo:example:alarm-conditions";

  private static final String MOTOR_NORMAL_STATE = "Running";

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final SubscriptionModel subscriptionModel;

  private UaVariableNode temperatureNode;
  private UaVariableNode motorStateNode;

  private ExclusiveLevelAlarm temperatureAlarm;
  private OffNormalAlarm motorAlarm;
  private SystemOffNormalAlarm deviceCommunicationAlarm;

  private volatile ScheduledFuture<?> simulationFuture;

  // Simulation state, touched only by the simulation tick.
  private long tick = 0;
  private boolean communicationLost = false;
  private boolean motorRunning = true;

  AlarmConditionsNamespace(OpcUaServer server) {
    super(server, NAMESPACE_URI);

    subscriptionModel = new SubscriptionModel(server, this);

    getLifecycleManager().addLifecycle(subscriptionModel);

    getLifecycleManager().addStartupTask(this::createAndAddNodes);

    getLifecycleManager()
        .addLifecycle(
            new Lifecycle() {
              @Override
              public void startup() {
                simulationFuture =
                    getServer()
                        .getScheduledExecutorService()
                        .scheduleAtFixedRate(
                            AlarmConditionsNamespace.this::simulate, 1, 1, TimeUnit.SECONDS);
              }

              @Override
              public void shutdown() {
                ScheduledFuture<?> future = simulationFuture;
                if (future != null) {
                  future.cancel(false);
                }

                getServer().getConditionManager().unregister(temperatureAlarm);
                getServer().getConditionManager().unregister(motorAlarm);
                getServer().getConditionManager().unregister(deviceCommunicationAlarm);
              }
            });
  }

  private void createAndAddNodes() {
    // The "Boiler" area: registering it as an event notifier sets its SubscribeToEvents bit and
    // wires it into the Server's notifier hierarchy, so clients can create event monitored items
    // on the Boiler node itself and see only this area's alarms (or on the Server node to see
    // everything).
    UaObjectNode boiler = addObjectNode("Boiler", NodeIds.ObjectsFolder, NodeIds.Organizes);

    registerEventNotifier(boiler);

    temperatureNode =
        addVariableNode(boiler, "Boiler/Temperature", NodeIds.Double, new Variant(75.0));

    // The motor hangs off the boiler as an event source, so its alarm is in scope for items
    // monitoring the Boiler area.
    UaObjectNode motor = addObjectNode("Boiler/Motor", boiler.getNodeId(), NodeIds.HasComponent);

    motor.addReference(
        new Reference(
            boiler.getNodeId(), NodeIds.HasEventSource, motor.getNodeId().expanded(), true));

    motorStateNode =
        addVariableNode(
            motor, "Boiler/Motor/State", NodeIds.String, new Variant(MOTOR_NORMAL_STATE));

    UaVariableNode motorNormalStateNode =
        addVariableNode(
            motor, "Boiler/Motor/NormalState", NodeIds.String, new Variant(MOTOR_NORMAL_STATE));

    try {
      createAlarms(boiler, motor, motorNormalStateNode);
    } catch (UaException e) {
      throw new RuntimeException("failed to create example alarms", e);
    }
  }

  private void createAlarms(UaObjectNode boiler, UaObjectNode motor, UaVariableNode motorNormal)
      throws UaException {

    // A level alarm on the boiler temperature, configured entirely through the builder. Limits
    // carry per-limit severities, leaving the High state requires retreating 1.5 degrees below
    // the limit (deadband), and the optional ConfirmedState and ShelvingState components are
    // opted in — their methods wire themselves.
    temperatureAlarm =
        ExclusiveLevelAlarm.create(
            getNodeContext(),
            b ->
                b.nodeId(newNodeId("Boiler/TemperatureAlarm"))
                    .browseName(newQualifiedName("TemperatureAlarm"))
                    .conditionSource(boiler)
                    .inputNode(temperatureNode.getNodeId())
                    .conditionClass(
                        NodeIds.ProcessConditionClassType,
                        LocalizedText.english("ProcessConditionClass"))
                    .highLimit(90.0, ushort(600))
                    .highHighLimit(95.0, ushort(800))
                    .highDeadband(1.5)
                    .withConfirm()
                    .withShelving(Duration.ofHours(8)));

    // Interceptor example: observe (or veto, by throwing a UaException such as
    // Bad_UserAccessDenied) operator actions before the SDK applies its default semantics.
    temperatureAlarm.setInterceptor(
        new ConditionMethodInterceptor() {
          @Override
          @NullMarked
          public Outcome onAcknowledge(
              InvocationContext context, ConditionBranch branch, @Nullable LocalizedText comment) {

            String user = context.getSession().map(Session::getClientUserId).orElse("anonymous");
            logger.info("TemperatureAlarm acknowledged by '{}', comment={}", user, comment);

            return Outcome.PROCEED;
          }
        });

    // An off-normal alarm: active while the motor state differs from the normal-state value. The
    // SDK samples nothing; the simulation passes both values to evaluate().
    motorAlarm =
        OffNormalAlarm.create(
            getNodeContext(),
            b ->
                b.nodeId(newNodeId("Boiler/Motor/OffNormalAlarm"))
                    .browseName(newQualifiedName("MotorOffNormalAlarm"))
                    .conditionSource(motor)
                    .inputNode(motorStateNode.getNodeId())
                    .normalState(motorNormal.getNodeId())
                    .severity(ushort(400)));

    // Part 9 §5.14 underlying-system pattern, as application code: when communication with the
    // underlying device is lost, a SystemOffNormalAlarm activates and the conditions fed by that
    // device report Quality Bad_CommunicationError (see simulate()).
    deviceCommunicationAlarm =
        SystemOffNormalAlarm.create(
            getNodeContext(),
            b ->
                b.nodeId(newNodeId("Boiler/DeviceCommunicationAlarm"))
                    .browseName(newQualifiedName("DeviceCommunicationAlarm"))
                    .conditionSource(boiler)
                    .conditionClass(
                        NodeIds.SystemConditionClassType,
                        LocalizedText.english("SystemConditionClass"))
                    .severity(ushort(900)));

    // Registration is what makes a Condition participate in ConditionRefresh replay and
    // cross-Condition EventId-keyed dispatch.
    getServer().getConditionManager().register(temperatureAlarm);
    getServer().getConditionManager().register(motorAlarm);
    getServer().getConditionManager().register(deviceCommunicationAlarm);
  }

  /**
   * One simulation tick per second: a temperature sine wave that periodically violates the High and
   * HighHigh limits, a motor that toggles off-normal, and a periodic communication outage
   * demonstrating the §5.14 underlying-system pattern.
   */
  private void simulate() {
    try {
      long t = tick++;

      // Drop communication with the simulated device for the last 15 seconds of every 3 minutes.
      boolean lost = t % 180 >= 165;
      if (lost != communicationLost) {
        communicationLost = lost;

        if (lost) {
          deviceCommunicationAlarm.setActive(true);
          temperatureAlarm.setQuality(StatusCode.of(StatusCodes.Bad_CommunicationError));
          motorAlarm.setQuality(StatusCode.of(StatusCodes.Bad_CommunicationError));
        } else {
          temperatureAlarm.setQuality(StatusCode.GOOD);
          motorAlarm.setQuality(StatusCode.GOOD);
          deviceCommunicationAlarm.setActive(false);
        }
      }

      if (communicationLost) {
        // No fresh samples arrive while communication is down; alarm state holds.
        return;
      }

      // 50..100 over a 2-minute period: crosses High (90) and HighHigh (95) every cycle.
      double temperature = 75.0 + 25.0 * Math.sin(2.0 * Math.PI * t / 120.0);
      temperatureNode.setValue(new DataValue(new Variant(temperature)));
      temperatureAlarm.evaluate(temperature);

      if (t > 0 && t % 45 == 0) {
        motorRunning = !motorRunning;
      }
      String motorState = motorRunning ? MOTOR_NORMAL_STATE : "Stopped";
      motorStateNode.setValue(new DataValue(new Variant(motorState)));
      motorAlarm.evaluate(motorState, MOTOR_NORMAL_STATE);
    } catch (Throwable e) {
      logger.error("simulation tick failed: {}", e.getMessage(), e);
    }
  }

  private UaObjectNode addObjectNode(String name, NodeId parentNodeId, NodeId referenceTypeId) {
    var node =
        new UaObjectNode(
            getNodeContext(),
            newNodeId(name),
            newQualifiedName(name.substring(name.lastIndexOf('/') + 1)),
            LocalizedText.english(name.substring(name.lastIndexOf('/') + 1)),
            LocalizedText.NULL_VALUE,
            uint(0),
            uint(0),
            ubyte(0));

    node.addReference(
        new Reference(
            node.getNodeId(), NodeIds.HasTypeDefinition, NodeIds.BaseObjectType.expanded(), true));

    node.addReference(
        new Reference(
            node.getNodeId(),
            referenceTypeId,
            parentNodeId.expanded(),
            Reference.Direction.INVERSE));

    getNodeManager().addNode(node);

    return node;
  }

  private UaVariableNode addVariableNode(
      UaObjectNode parent, String name, NodeId dataTypeId, Variant initialValue) {

    String browseName = name.substring(name.lastIndexOf('/') + 1);

    UaVariableNode node =
        new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
            .setNodeId(newNodeId(name))
            .setAccessLevel(AccessLevel.READ_ONLY)
            .setUserAccessLevel(AccessLevel.READ_ONLY)
            .setBrowseName(newQualifiedName(browseName))
            .setDisplayName(LocalizedText.english(browseName))
            .setDataType(dataTypeId)
            .setTypeDefinition(NodeIds.BaseDataVariableType)
            .build();

    node.setValue(new DataValue(initialValue));

    getNodeManager().addNode(node);

    node.addReference(
        new Reference(
            node.getNodeId(),
            NodeIds.HasComponent,
            parent.getNodeId().expanded(),
            Reference.Direction.INVERSE));

    return node;
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
