/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.DiscoveryClient;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.server.conditions.ExclusiveLimitAlarm;
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
import org.eclipse.milo.opcua.sdk.server.sampling.ReadAccessPolicy;
import org.eclipse.milo.opcua.sdk.server.sampling.SamplingManagerConfig;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.EventTestSupport;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.DefaultServerCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

/** Executes the server guide fragments against a local server and checks client-visible results. */
public class WikiServerGuidesTest extends AbstractClientServerTest {

  private GuideNamespace namespace;

  @Override
  protected void configureTestNamespace(TestNamespace unused) {
    namespace = new GuideNamespace(server);
    namespace.startup();
  }

  @AfterAll
  void shutdownGuideNamespace() {
    if (namespace != null) namespace.shutdown();
  }

  // A published Variable must be reachable over the service API, not only in a local node map.
  @Test
  void addressSpaceVariableCanBeReadAndUnknownNodesReportFailure() throws Exception {
    DataValue value =
        client.readValue(0, TimestampsToReturn.Both, namespace.temperature.getNodeId());
    assertTrue(value.statusCode().isGood());
    assertEquals(21.5, value.value().value());
    assertEquals(
        StatusCodes.Bad_NodeIdUnknown,
        client
            .readValue(0, TimestampsToReturn.Neither, namespace.id("Missing"))
            .statusCode()
            .getValue());
  }

  // Client writes must cross the type checker and application filter before storage changes.
  @Test
  void dataAccessRejectsOutOfRangeAndWrongTypeWithoutChangingValue() throws Exception {
    UaVariableNode node = namespace.newVariable("Setpoint", 20.0);
    try {
      // wiki:data-filter:start
      node.getFilterChain()
          .addLast(
              new AttributeFilter() {
                @Override
                public void writeAttribute(
                    AttributeFilterContext ctx, AttributeId attributeId, Object value)
                    throws UaException {
                  if (attributeId == AttributeId.Value) {
                    double setpoint = (Double) ((DataValue) value).value().value();
                    if (!Double.isFinite(setpoint) || setpoint < 0.0 || setpoint > 100.0) {
                      throw new UaException(StatusCodes.Bad_OutOfRange);
                    }
                  }
                  ctx.setAttribute(attributeId, value);
                }
              });
      // wiki:data-filter:end
      assertTrue(
          client
              .writeValues(List.of(node.getNodeId()), List.of(new DataValue(new Variant(35.0))))
              .get(0)
              .isGood());
      assertEquals(
          StatusCodes.Bad_OutOfRange,
          client
              .writeValues(List.of(node.getNodeId()), List.of(new DataValue(new Variant(150.0))))
              .get(0)
              .getValue());
      assertEquals(
          StatusCodes.Bad_TypeMismatch,
          client
              .writeValues(List.of(node.getNodeId()), List.of(new DataValue(new Variant("wrong"))))
              .get(0)
              .getValue());
      assertEquals(
          35.0, client.readValue(0, TimestampsToReturn.Neither, node.getNodeId()).value().value());
    } finally {
      node.delete();
    }
  }

  // A cached sampling grant must be invalidated when the application's permission input changes.
  @Test
  void samplingReportsRevisedIntervalPermissionRevocationAndRecovery() throws Exception {
    UaVariableNode node = namespace.newVariable("Permission", 42.0);
    var subscription = new OpcUaSubscription(client);
    subscription.setPublishingInterval(100.0);
    subscription.create();
    try {
      var values = new LinkedBlockingQueue<DataValue>();
      OpcUaMonitoredItem item = OpcUaMonitoredItem.newDataItem(node.getNodeId());
      item.setSamplingInterval(120.0);
      item.setDataValueListener((i, value) -> values.add(value));
      subscription.addMonitoredItem(item);
      subscription.synchronizeMonitoredItems();
      assertEquals(StatusCode.GOOD, item.getCreateResult().orElseThrow());
      assertEquals(125.0, item.getRevisedSamplingInterval().orElseThrow());
      DataValue initial = values.poll(5, TimeUnit.SECONDS);
      assertNotNull(initial);
      assertEquals(42.0, initial.value().value());
      // wiki:access-revoke:start
      node.setUserAccessLevel(AccessLevel.toValue(AccessLevel.NONE));
      server.getAccessControlManager().invalidateReadAccess(node.getNodeId());
      // wiki:access-revoke:end
      DataValue denied = values.poll(5, TimeUnit.SECONDS);
      assertNotNull(denied);
      assertEquals(StatusCodes.Bad_UserAccessDenied, denied.statusCode().getValue());
      assertTrue(denied.value().isNull());
      assertEquals(
          StatusCodes.Bad_UserAccessDenied,
          client
              .readValue(0, TimestampsToReturn.Neither, node.getNodeId())
              .statusCode()
              .getValue());
      node.setUserAccessLevel(AccessLevel.toValue(AccessLevel.READ_WRITE));
      server.getAccessControlManager().invalidateReadAccess(node.getNodeId());
      DataValue restored = values.poll(5, TimeUnit.SECONDS);
      assertNotNull(restored);
      assertTrue(restored.statusCode().isGood());
      assertEquals(42.0, restored.value().value());
    } finally {
      subscription.delete();
      node.delete();
    }
  }

  // The documented Method handler must return outputs and preserve per-input failure information.
  @Test
  void methodValidatesInputAndReturnsPerArgumentStatus() throws Exception {
    UaMethodNode method =
        UaMethodNode.builder(namespace.context())
            .setNodeId(namespace.id("Sqrt"))
            .setBrowseName(namespace.name("Sqrt"))
            .setDisplayName(LocalizedText.english("Sqrt"))
            .build();
    namespace.manager().addNode(method);
    method.addReference(
        new Reference(
            method.getNodeId(),
            NodeIds.HasComponent,
            NodeIds.ObjectsFolder.expanded(),
            Reference.Direction.INVERSE));
    var handler = new SquareRoot(method);
    method.setInputArguments(handler.getInputArguments());
    method.setOutputArguments(handler.getOutputArguments());
    method.setInvocationHandler(handler);
    try {
      CallMethodResult result = call(NodeIds.ObjectsFolder, method.getNodeId(), new Variant(81.0));
      assertEquals(StatusCode.GOOD, result.getStatusCode());
      assertEquals(9.0, result.getOutputArguments()[0].value());
      CallMethodResult negative =
          call(NodeIds.ObjectsFolder, method.getNodeId(), new Variant(-1.0));
      assertEquals(StatusCodes.Bad_InvalidArgument, negative.getStatusCode().getValue());
      assertEquals(StatusCodes.Bad_OutOfRange, negative.getInputArgumentResults()[0].getValue());
      CallMethodResult wrongType =
          call(NodeIds.ObjectsFolder, method.getNodeId(), new Variant("81"));
      assertEquals(StatusCodes.Bad_InvalidArgument, wrongType.getStatusCode().getValue());
      assertEquals(StatusCodes.Bad_TypeMismatch, wrongType.getInputArgumentResults()[0].getValue());
    } finally {
      method.delete();
    }
  }

  // An instantiation result owns its additions and must reject a colliding identity before reuse.
  @Test
  void typeInstantiationPublishesAndDeletesOnlyItsOwnInstance() throws Exception {
    UaNodeManager nodeManager = namespace.manager();
    NodeId folderId = namespace.id("Devices");
    QualifiedName folderName = namespace.name("Devices");
    // wiki:instantiate:start
    InstantiationRequest<UaObjectNode> request =
        InstantiationRequest.of(UaObjectNode.class, NodeIds.FolderType)
            .nodeId(folderId)
            .browseName(folderName)
            .displayName(LocalizedText.english("Devices"))
            .parent(NodeIds.ObjectsFolder, NodeIds.Organizes)
            .target(nodeManager)
            .build();
    InstantiationResult<UaObjectNode> result = server.getNodeInstantiator().instantiate(request);
    // wiki:instantiate:end
    try {
      assertEquals(
          LocalizedText.english("Devices"),
          client.getAddressSpace().getNode(folderId).readDisplayName());
      assertThrows(UaException.class, () -> server.getNodeInstantiator().instantiate(request));
    } finally {
      result.deleteCreated();
    }
    assertTrue(nodeManager.getNode(folderId).isEmpty());
    assertTrue(nodeManager.getNode(namespace.temperature.getNodeId()).isPresent());
  }

  // Field selection is evaluated while firing, so clients must still receive fields after cleanup.
  @Test
  void eventFieldsReachClientBeforeTemporaryEventNodesAreDeleted() throws Exception {
    var subscription = new OpcUaSubscription(client);
    subscription.create();
    try {
      EventFilter filter =
          EventTestSupport.severityEventFilter(
              321,
              EventTestSupport.eventField(NodeIds.BaseEventType, "Message"),
              EventTestSupport.eventField(NodeIds.BaseEventType, "SourceNode"));
      List<Variant[]> events = EventTestSupport.monitorEvents(subscription, NodeIds.Server, filter);
      NodeId eventId = namespace.id(UUID.randomUUID().toString());
      NodeId sourceId = namespace.temperature.getNodeId();
      // wiki:event:start
      BaseEventTypeNode event =
          server.getEventInstantiator().createEvent(eventId, NodeIds.BaseEventType);
      try {
        event.setEventId(
            ByteString.of(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8)));
        event.setEventType(NodeIds.BaseEventType);
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
      // wiki:event:end
      Variant[] received =
          EventTestSupport.awaitEvent(
              events,
              "wiki event",
              fields -> "Temperature changed".equals(EventTestSupport.localizedTextOf(fields[0])));
      assertEquals(sourceId, received[1].value());
      assertFalse(server.getAddressSpaceManager().getManagedNode(eventId).isPresent());
    } finally {
      subscription.delete();
    }
  }

  // Clearing the process value must retain an unacknowledged alarm until its EventId is
  // acknowledged.
  @Test
  void limitAlarmRetainsClearedStateUntilAcknowledged() throws Exception {
    UaNodeContext context = namespace.context();
    UaObjectNode source =
        server
            .getAddressSpaceManager()
            .getManagedNode(NodeIds.Server)
            .map(UaObjectNode.class::cast)
            .orElseThrow();
    NodeId alarmId = namespace.id("HighTemperature");
    QualifiedName alarmName = namespace.name("HighTemperature");
    // wiki:alarm:start
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
    // wiki:alarm:end
    try {
      assertTrue(alarm.isActive());
      assertFalse(alarm.isAcked());
      assertTrue(alarm.isRetained());
      alarm.evaluate(70.0);
      assertFalse(alarm.isActive());
      assertTrue(alarm.isRetained());
      CallMethodResult invalid =
          call(
              alarm.getConditionId(),
              NodeIds.AcknowledgeableConditionType_Acknowledge,
              new Variant(ByteString.of(new byte[] {1})),
              new Variant(LocalizedText.NULL_VALUE));
      assertEquals(StatusCodes.Bad_EventIdUnknown, invalid.getStatusCode().getValue());
      CallMethodResult acknowledged =
          call(
              alarm.getConditionId(),
              NodeIds.AcknowledgeableConditionType_Acknowledge,
              new Variant(alarm.currentBranch().getLastEventId()),
              new Variant(LocalizedText.NULL_VALUE));
      assertEquals(StatusCode.GOOD, acknowledged.getStatusCode());
      assertTrue(alarm.isAcked());
      assertFalse(alarm.isRetained());
    } finally {
      server.getConditionManager().unregister(alarm);
      alarm.getNode().delete();
    }
  }

  // Trust is an explicit decision: a rejected certificate is quarantined, not automatically
  // trusted.
  @Test
  void certificateIsRejectedUntilExplicitlyTrusted() throws Exception {
    X509Certificate peerCertificate = testServer.getClientCertificate();
    // wiki:trust:start
    var trustList = new MemoryTrustListManager();
    var rejected = new MemoryCertificateQuarantine();
    var validator = new DefaultServerCertificateValidator(trustList, rejected);
    // wiki:trust:end
    UaException failure =
        assertThrows(
            UaException.class,
            () -> validator.validateCertificateChain(List.of(peerCertificate), null, null));
    assertEquals(StatusCodes.Bad_SecurityChecksFailed, failure.getStatusCode().getValue());
    assertTrue(rejected.getRejectedCertificates().contains(peerCertificate));
    trustList.addTrustedCertificate(peerCertificate);
    validator.validateCertificateChain(List.of(peerCertificate), null, null);
    trustList.removeTrustedCertificate(CertificateUtil.thumbprint(peerCertificate));
    assertThrows(
        UaException.class,
        () -> validator.validateCertificateChain(List.of(peerCertificate), null, null));
  }

  // Endpoint discovery is independently useful and does not create or authenticate a Session.
  @Test
  void discoveryReturnsBoundServerEndpoints() throws Exception {
    String discoveryUrl = server.getBoundEndpoints().get(0).getEndpointUrl();
    // wiki:discovery:start
    List<EndpointDescription> endpoints =
        DiscoveryClient.getEndpoints(discoveryUrl).get(10, TimeUnit.SECONDS);
    // wiki:discovery:end
    assertFalse(endpoints.isEmpty());
    assertTrue(
        endpoints.stream()
            .allMatch(
                endpoint ->
                    endpoint
                        .getServer()
                        .getApplicationUri()
                        .equals(server.getConfig().getApplicationUri())));
  }

  private CallMethodResult call(NodeId objectId, NodeId methodId, Variant... inputs)
      throws Exception {
    return client.call(List.of(new CallMethodRequest(objectId, methodId, inputs))).getResults()[0];
  }

  // wiki:method:start
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

  // wiki:method:end

  private static final class GuideNamespace extends ManagedNamespaceWithLifecycle {
    private UaVariableNode temperature;

    GuideNamespace(OpcUaServer server) {
      super(server, "urn:eclipse:milo:wiki:server");
      getLifecycleManager().addStartupTask(() -> temperature = newVariable("Temperature", 21.5));
    }

    UaVariableNode newVariable(String name, double value) {
      UaNodeContext context = getNodeContext();
      UaNodeManager nodeManager = getNodeManager();
      NodeId variableId = newNodeId(name);
      QualifiedName variableName = newQualifiedName(name);
      // wiki:variable:start
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
      // wiki:variable:end
      return variable;
    }

    // wiki:sampling:start
    @Override
    protected SamplingManagerConfig samplingManagerConfig() {
      return SamplingManagerConfig.defaults()
          .withMinimumIntervalMillis(100)
          .withReadAccessPolicy(ReadAccessPolicy.cached());
    }

    // wiki:sampling:end

    NodeId id(String name) {
      return newNodeId(name);
    }

    QualifiedName name(String name) {
      return newQualifiedName(name);
    }

    UaNodeContext context() {
      return getNodeContext();
    }

    UaNodeManager manager() {
      return getNodeManager();
    }
  }
}
