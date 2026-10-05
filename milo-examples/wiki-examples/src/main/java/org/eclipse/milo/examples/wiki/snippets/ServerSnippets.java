/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.wiki.snippets;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.DiscoveryClient;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigBuilder;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.conditions.ExclusiveLimitAlarm;
import org.eclipse.milo.opcua.sdk.server.identity.IdentityValidator;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.InvalidArgumentException;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilter;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilterContext;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.InstantiationRequest;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.InstantiationResult;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.DefaultServerCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;

/**
 * Server samples from the Wiki's address space, service, event, security, and configuration pages.
 */
public final class ServerSnippets {

  private ServerSnippets() {}

  static UaVariableNode addVariable(
      UaNodeContext context,
      UaNodeManager nodeManager,
      NodeId variableId,
      QualifiedName variableName,
      String name,
      double value) {
    // snippet:variable:start
    UaVariableNode variable =
        UaVariableNode.builder(context)
            .setNodeId(variableId)
            .setBrowseName(variableName)
            .setDisplayName(LocalizedText.english(name))
            .setDataType(NodeIds.Double)
            .setTypeDefinition(NodeIds.BaseDataVariableType)
            .setAccessLevel(AccessLevel.READ_WRITE)
            .setUserAccessLevel(AccessLevel.READ_WRITE)
            .setValueRank(ValueRanks.Scalar)
            .setMinimumSamplingInterval(0.0)
            .setValue(new DataValue(new Variant(value)))
            .build();
    nodeManager.addNode(variable);
    variable.addReference(
        new Reference(
            variableId,
            NodeIds.Organizes,
            NodeIds.ObjectsFolder.expanded(),
            Reference.Direction.INVERSE));
    // snippet:variable:end
    return variable;
  }

  static void validateSetpointWrites(UaVariableNode node) {
    // snippet:data-filter:start
    node.getFilterChain()
        .addLast(
            new AttributeFilter() {
              @Override
              public void writeAttribute(
                  AttributeFilterContext ctx, AttributeId attributeId, Object value)
                  throws UaException {
                if (attributeId == AttributeId.Value) {
                  DataValue dataValue = (DataValue) value;
                  if (!(dataValue.value().value() instanceof Double setpoint)) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch);
                  }
                  if (!Double.isFinite(setpoint) || setpoint < 0.0 || setpoint > 100.0) {
                    throw new UaException(StatusCodes.Bad_OutOfRange);
                  }
                }

                ctx.writeAttribute(attributeId, value);
              }
            });
    // snippet:data-filter:end
  }

  // snippet:method:start
  static final class SquareRoot extends AbstractMethodInvocationHandler {
    SquareRoot(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return new Argument[] {
        new Argument(
            "x",
            NodeIds.Double,
            ValueRanks.Scalar,
            null,
            LocalizedText.english("Nonnegative finite input"))
      };
    }

    @Override
    public Argument[] getOutputArguments() {
      return new Argument[] {
        new Argument(
            "root", NodeIds.Double, ValueRanks.Scalar, null, LocalizedText.english("Square root"))
      };
    }

    @Override
    protected void validateInputArgumentValues(Variant[] inputs) throws InvalidArgumentException {
      Double x = (Double) inputs[0].value();
      if (x == null || !Double.isFinite(x) || x < 0.0) {
        throw new InvalidArgumentException(
            new StatusCode[] {new StatusCode(StatusCodes.Bad_OutOfRange)});
      }
    }

    @Override
    protected Variant[] invoke(InvocationContext context, Variant[] inputs) {
      return new Variant[] {new Variant(Math.sqrt((Double) inputs[0].value()))};
    }
  }

  // snippet:method:end

  static void fireTemperatureEvent(OpcUaServer server, NodeId eventId, NodeId sourceId)
      throws UaException {
    // snippet:event:start
    BaseEventTypeNode event =
        server.getEventInstantiator().createEvent(eventId, NodeIds.BaseEventType);
    try {
      event.setEventId(
          ByteString.of(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8)));
      event.setSourceNode(sourceId);
      event.setSourceName("Temperature");
      event.setTime(DateTime.now());
      event.setReceiveTime(DateTime.now());
      event.setMessage(LocalizedText.english("Temperature changed"));
      event.setSeverity(ushort(321));
      server.getEventNotifier().fire(event);
    } finally {
      event.delete();
    }
    // snippet:event:end
  }

  static ExclusiveLimitAlarm registerHighTemperatureAlarm(
      OpcUaServer server,
      UaNodeContext context,
      NodeId alarmId,
      QualifiedName alarmName,
      UaObjectNode source)
      throws UaException {
    // snippet:alarm:start
    ExclusiveLimitAlarm alarm =
        ExclusiveLimitAlarm.create(
            context,
            builder ->
                builder
                    .nodeId(alarmId)
                    .browseName(alarmName)
                    .conditionSource(source)
                    .highLimit(80.0));
    server.getConditionManager().register(alarm);
    alarm.evaluate(85.0);
    // snippet:alarm:end
    return alarm;
  }

  static List<EndpointDescription> discoverEndpoints(String discoveryUrl) throws Exception {
    // snippet:discovery:start
    List<EndpointDescription> endpoints =
        DiscoveryClient.getEndpoints(discoveryUrl).get(10, TimeUnit.SECONDS);
    // snippet:discovery:end
    return endpoints;
  }

  static DefaultServerCertificateValidator newValidator() {
    // snippet:trust:start
    var trustList = new MemoryTrustListManager();
    var rejected = new MemoryCertificateQuarantine();
    var validator = new DefaultServerCertificateValidator(trustList, rejected);
    // snippet:trust:end
    return validator;
  }

  static InstantiationResult<UaObjectNode> addDevicesFolder(
      OpcUaServer server, UaNodeManager nodeManager, NodeId folderId, QualifiedName folderName)
      throws UaException {
    // snippet:instantiate:start
    InstantiationRequest<UaObjectNode> request =
        InstantiationRequest.of(UaObjectNode.class, NodeIds.FolderType)
            .nodeId(folderId)
            .browseName(folderName)
            .displayName(LocalizedText.english("Devices"))
            .parent(NodeIds.ObjectsFolder, NodeIds.Organizes)
            .target(nodeManager)
            .build();
    InstantiationResult<UaObjectNode> result = server.getNodeInstantiator().instantiate(request);
    // snippet:instantiate:end
    return result;
  }

  static void revokeReadAccess(OpcUaServer server, UaVariableNode node) {
    // snippet:access-revoke:start
    node.setUserAccessLevel(AccessLevel.toValue(AccessLevel.NONE));
    server.getAccessControlManager().invalidateReadAccess(node.getNodeId());
    // snippet:access-revoke:end
  }

  static void configureServer(
      OpcUaServerConfigBuilder builder,
      int port,
      X509Certificate certificate,
      String applicationUri,
      CertificateManager certificateManager,
      IdentityValidator usernameValidator) {
    // snippet:server-config:start
    EndpointConfig endpoint =
        EndpointConfig.newBuilder()
            .setBindAddress("127.0.0.1")
            .setHostname("localhost")
            .setBindPort(port)
            .setPath("/wiki")
            .setCertificate(certificate)
            .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
            .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
            .addTokenPolicy(OpcUaServerConfig.USER_TOKEN_POLICY_USERNAME)
            .build();
    builder
        .setApplicationUri(applicationUri)
        .setCertificateManager(certificateManager)
        .setEndpoints(Set.of(endpoint))
        .setIdentityValidator(usernameValidator)
        .setLimits(
            new OpcUaServerConfigLimits() {
              @Override
              public UInteger getMaxNodesPerRead() {
                return uint(2);
              }
            });
    // snippet:server-config:end
  }

  static void runWithNamespace(OpcUaServer server, ManagedNamespaceWithLifecycle namespace)
      throws Exception {
    // snippet:lifecycle:start
    server.addLifecycleParticipant(namespace);
    try {
      server.startup().get(10, TimeUnit.SECONDS);
      if (server.getBoundEndpoints().isEmpty()) {
        throw new IllegalStateException("server has no bound endpoint");
      }
      // Application work runs here while the server owns the namespace lifecycle.
    } finally {
      server.shutdown().get(10, TimeUnit.SECONDS);
    }
    // snippet:lifecycle:end
  }
}
