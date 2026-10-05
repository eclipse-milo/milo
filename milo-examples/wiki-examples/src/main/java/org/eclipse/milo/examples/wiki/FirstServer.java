/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.wiki;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.InvalidArgumentException;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;

/** A loopback-only thermostat server for the Wiki tutorials. */
public final class FirstServer {

  public static final String NAMESPACE_URI = "urn:eclipse:milo:wiki";

  private FirstServer() {}

  /**
   * Create a server that owns its tutorial namespace.
   *
   * @param port the loopback TCP port to bind.
   * @return an unstarted server; its caller must shut it down, including after failed startup.
   */
  public static OpcUaServer create(int port) {
    // snippet:config:start
    var endpoint =
        EndpointConfig.newBuilder()
            .setBindAddress("127.0.0.1")
            .setHostname("127.0.0.1")
            .setBindPort(port)
            .setPath("/wiki")
            .setSecurityPolicy(SecurityPolicy.None)
            .setSecurityMode(MessageSecurityMode.None)
            .addTokenPolicy(OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS)
            .build();

    var config =
        OpcUaServerConfig.builder()
            .setApplicationUri("urn:eclipse:milo:wiki:server")
            .setApplicationName(LocalizedText.english("Wiki server"))
            .setProductUri("urn:eclipse:milo:wiki")
            .setCertificateManager(new DefaultCertificateManager())
            .setEndpoints(Set.of(endpoint))
            .build();

    var server =
        new OpcUaServer(
            config,
            profile -> new OpcTcpServerTransport(OpcTcpServerTransportConfig.newBuilder().build()));
    // snippet:config:end

    server.addLifecycleParticipant(new TutorialNamespace(server));
    return server;
  }

  public static void main(String[] args) throws Exception {
    int port = args.length == 0 ? 12686 : Integer.parseInt(args[0]);
    OpcUaServer server = create(port);
    try {
      server.startup().get(10, TimeUnit.SECONDS);
      System.out.println(
          "Listening on opc.tcp://127.0.0.1:" + port + "/wiki; press Enter to stop.");
      System.in.read();
    } finally {
      try {
        server.shutdown().get(10, TimeUnit.SECONDS);
      } finally {
        Stack.releaseSharedResources();
      }
    }
  }

  private static final class TutorialNamespace extends ManagedNamespaceWithLifecycle {
    TutorialNamespace(OpcUaServer server) {
      super(server, NAMESPACE_URI);

      // The Thermostat Object, organized under the Objects folder.
      // snippet:thermostat:start
      UaObjectNode thermostat =
          new UaObjectNode.UaObjectNodeBuilder(getNodeContext())
              .setNodeId(newNodeId("Thermostat"))
              .setBrowseName(newQualifiedName("Thermostat"))
              .setDisplayName(LocalizedText.english("Thermostat"))
              .setTypeDefinition(NodeIds.BaseObjectType)
              .build();
      getNodeManager().addNode(thermostat);

      thermostat.addReference(
          new Reference(
              thermostat.getNodeId(),
              NodeIds.Organizes,
              NodeIds.ObjectsFolder.expanded(),
              Reference.Direction.INVERSE));
      // snippet:thermostat:end

      // Temperature and Setpoint, components of Thermostat.
      // snippet:variables:start
      UaVariableNode temperature =
          new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
              .setNodeId(newNodeId("Temperature"))
              .setBrowseName(newQualifiedName("Temperature"))
              .setDisplayName(LocalizedText.english("Temperature"))
              .setDataType(NodeIds.Double)
              .setTypeDefinition(NodeIds.BaseDataVariableType)
              .setAccessLevel(AccessLevel.READ_ONLY)
              .setUserAccessLevel(AccessLevel.READ_ONLY)
              .setValue(new DataValue(new Variant(21.5)))
              .build();
      getNodeManager().addNode(temperature);
      thermostat.addComponent(temperature);

      UaVariableNode setpoint =
          new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
              .setNodeId(newNodeId("Setpoint"))
              .setBrowseName(newQualifiedName("Setpoint"))
              .setDisplayName(LocalizedText.english("Setpoint"))
              .setDataType(NodeIds.Double)
              .setTypeDefinition(NodeIds.BaseDataVariableType)
              .setAccessLevel(AccessLevel.READ_WRITE)
              .setUserAccessLevel(AccessLevel.READ_WRITE)
              .setValue(new DataValue(new Variant(22.0)))
              .build();
      getNodeManager().addNode(setpoint);
      thermostat.addComponent(setpoint);
      // snippet:variables:end

      // The AdjustSetpoint Method, a component of Thermostat.
      // snippet:method:start
      UaMethodNode adjustSetpoint =
          UaMethodNode.builder(getNodeContext())
              .setNodeId(newNodeId("AdjustSetpoint"))
              .setBrowseName(newQualifiedName("AdjustSetpoint"))
              .setDisplayName(LocalizedText.english("AdjustSetpoint"))
              .setDescription(LocalizedText.english("Add Delta to Setpoint and return the result."))
              .build();
      getNodeManager().addNode(adjustSetpoint);

      var handler = new AdjustSetpointHandler(adjustSetpoint, setpoint);
      adjustSetpoint.bindInvocationHandler(handler);
      thermostat.addComponent(adjustSetpoint);
      // snippet:method:end
    }
  }

  // snippet:adjust-setpoint:start
  /** Adds Delta to Setpoint when the result stays within 0.0 to 100.0. */
  private static final class AdjustSetpointHandler extends AbstractMethodInvocationHandler {

    private static final Argument DELTA =
        new Argument(
            "Delta",
            NodeIds.Double,
            ValueRanks.Scalar,
            null,
            LocalizedText.english("The amount to add to the setpoint."));

    private static final Argument NEW_SETPOINT =
        new Argument(
            "Setpoint",
            NodeIds.Double,
            ValueRanks.Scalar,
            null,
            LocalizedText.english("The setpoint after the adjustment."));

    private final UaVariableNode setpoint;

    AdjustSetpointHandler(UaMethodNode node, UaVariableNode setpoint) {
      super(node);
      this.setpoint = setpoint;
    }

    @Override
    public Argument[] getInputArguments() {
      return new Argument[] {DELTA};
    }

    @Override
    public Argument[] getOutputArguments() {
      return new Argument[] {NEW_SETPOINT};
    }

    @Override
    protected void validateInputArgumentValues(Variant[] inputs) throws InvalidArgumentException {
      // The SDK's type check lets a null value through, so test for a Double here.
      boolean finite = inputs[0].value() instanceof Double delta && Double.isFinite(delta);
      if (!finite) {
        StatusCode outOfRange = new StatusCode(StatusCodes.Bad_OutOfRange);
        throw new InvalidArgumentException(new StatusCode[] {outOfRange});
      }
    }

    @Override
    protected Variant[] invoke(InvocationContext context, Variant[] inputs) throws UaException {
      double delta = (Double) inputs[0].value();
      double current = (Double) setpoint.getValue().value().value();
      double adjusted = current + delta;

      boolean inRange = adjusted >= 0.0 && adjusted <= 100.0;
      if (!inRange) {
        throw new UaException(StatusCodes.Bad_OutOfRange);
      }

      setpoint.setValue(new DataValue(new Variant(adjusted)));

      return new Variant[] {new Variant(adjusted)};
    }
  }
  // snippet:adjust-setpoint:end
}
