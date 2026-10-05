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

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executor;
import org.eclipse.milo.opcua.sdk.client.AddressSpace;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.methods.UaMethod;
import org.eclipse.milo.opcua.sdk.client.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.client.subscriptions.EventFilterBuilder;
import org.eclipse.milo.opcua.sdk.client.subscriptions.MonitoredItemSynchronizationException;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.DefaultClientCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePath;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePathResult;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePathTarget;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePath;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePathElement;
import org.eclipse.milo.opcua.stack.core.types.structured.TranslateBrowsePathsToNodeIdsResponse;
import org.eclipse.milo.opcua.stack.core.util.validation.ValidationCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Client samples from the Wiki's client guide pages. */
public final class ClientSnippets {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClientSnippets.class);

  private ClientSnippets() {}

  // snippet:connect:start
  static OpcUaClient connect(String endpointUrl) throws UaException {
    OpcUaClient client =
        OpcUaClient.create(
            endpointUrl,
            endpoints -> selectEndpoint(endpoints),
            transport -> {},
            config ->
                config
                    .setApplicationName(LocalizedText.english("Thermostat client"))
                    .setApplicationUri("urn:eclipse:milo:wiki:client")
                    .setIdentityProvider(new AnonymousProvider())
                    .setRequestTimeout(uint(5_000)));

    try {
      client.connect();
    } catch (UaException e) {
      // A failed connect keeps retrying in the background until you disconnect.
      try {
        client.disconnect();
      } catch (UaException disconnectFailure) {
        e.addSuppressed(disconnectFailure);
      }
      throw e;
    }

    return client;
  }

  static Optional<EndpointDescription> selectEndpoint(List<EndpointDescription> endpoints) {
    return endpoints.stream()
        .filter(e -> SecurityPolicy.None.getUri().equals(e.getSecurityPolicyUri()))
        .filter(e -> e.getSecurityMode() == MessageSecurityMode.None)
        .findFirst();
  }

  // snippet:connect:end

  // snippet:disconnect:start
  static void runClient(String endpointUrl) throws Exception {
    try {
      OpcUaClient client = connect(endpointUrl);
      try {
        System.out.println("Connected. Press Enter to disconnect.");
        System.in.read();
      } finally {
        client.disconnect();
      }
    } finally {
      Stack.releaseSharedResources();
    }
  }

  // snippet:disconnect:end

  // snippet:tutorial-namespace:start
  static UShort tutorialNamespaceIndex(OpcUaClient client) {
    NamespaceTable namespaceTable = client.getNamespaceTable();
    UShort namespaceIndex = namespaceTable.getIndex("urn:eclipse:milo:wiki");
    if (namespaceIndex == null) {
      throw new IllegalStateException("Server does not expose the tutorial namespace");
    }
    return namespaceIndex;
  }

  // snippet:tutorial-namespace:end

  // snippet:browse:start
  static Optional<NodeId> findThermostat(OpcUaClient client, UShort namespaceIndex)
      throws UaException {
    var thermostatName = new QualifiedName(namespaceIndex, "Thermostat");
    List<ReferenceDescription> references = client.getAddressSpace().browse(NodeIds.ObjectsFolder);

    for (ReferenceDescription reference : references) {
      if (thermostatName.equals(reference.getBrowseName())) {
        ExpandedNodeId targetId = reference.getNodeId();
        return targetId.toNodeId(client.getNamespaceTable());
      }
    }

    return Optional.empty();
  }

  // snippet:browse:end

  // snippet:browse-components:start
  static void printComponents(OpcUaClient client, NodeId thermostatId) throws UaException {
    List<ReferenceDescription> references = client.getAddressSpace().browse(thermostatId);

    for (ReferenceDescription reference : references) {
      String name = reference.getBrowseName().name();
      NodeClass nodeClass = reference.getNodeClass();
      System.out.println(name + ": " + nodeClass);
    }
  }

  // snippet:browse-components:end

  // snippet:translate:start
  static NodeId resolveSetpoint(OpcUaClient client, UShort namespaceIndex) throws UaException {
    var thermostatName = new QualifiedName(namespaceIndex, "Thermostat");
    var setpointName = new QualifiedName(namespaceIndex, "Setpoint");

    RelativePathElement[] elements = {
      new RelativePathElement(NodeIds.Organizes, false, true, thermostatName),
      new RelativePathElement(NodeIds.HasComponent, false, true, setpointName)
    };
    var browsePath = new BrowsePath(NodeIds.ObjectsFolder, new RelativePath(elements));

    TranslateBrowsePathsToNodeIdsResponse response =
        client.translateBrowsePaths(List.of(browsePath));
    BrowsePathResult result = requireNonNull(response.getResults())[0];
    if (!result.getStatusCode().isGood()) {
      throw new UaException(result.getStatusCode());
    }

    BrowsePathTarget target = requireNonNull(result.getTargets())[0];
    if (!target.getRemainingPathIndex().equals(UInteger.MAX)) {
      throw new UaException(StatusCodes.Bad_NoMatch, "Setpoint path resolved only partly");
    }

    ExpandedNodeId setpointId = target.getTargetId();
    return setpointId.toNodeIdOrThrow(client.getNamespaceTable());
  }

  // snippet:translate:end

  // snippet:browse-options:start
  static List<ReferenceDescription> browseVariables(OpcUaClient client, NodeId thermostatId)
      throws UaException {
    AddressSpace.BrowseOptions options =
        AddressSpace.BrowseOptions.builder()
            .setReferenceType(NodeIds.HasComponent)
            .setIncludeSubtypes(true)
            .setNodeClassMask(EnumSet.of(NodeClass.Variable))
            .build();

    return client.getAddressSpace().browse(thermostatId, options);
  }

  // snippet:browse-options:end

  // snippet:read:start
  static double readTemperature(OpcUaClient client, NodeId temperatureId) throws UaException {
    DataValue value = client.readValue(0.0, TimestampsToReturn.Both, temperatureId);
    if (!value.statusCode().isGood()) {
      throw new UaException(value.statusCode());
    }

    Object body = value.value().value();
    if (!(body instanceof Double temperature)) {
      throw new IllegalStateException("Expected a Double, got " + body);
    }
    return temperature;
  }

  // snippet:read:end

  // snippet:read-values:start
  static void printThermostat(OpcUaClient client, NodeId temperatureId, NodeId setpointId)
      throws UaException {
    List<NodeId> nodeIds = List.of(temperatureId, setpointId);
    List<DataValue> values = client.readValues(0.0, TimestampsToReturn.Both, nodeIds);

    for (int i = 0; i < nodeIds.size(); i++) {
      NodeId nodeId = nodeIds.get(i);
      DataValue value = values.get(i);

      if (value.statusCode().isGood()) {
        System.out.println(nodeId.getIdentifier() + " = " + value.value().value());
      } else {
        System.out.println(nodeId.getIdentifier() + " failed: " + value.statusCode());
      }
    }
  }

  // snippet:read-values:end

  // snippet:write:start
  static void writeSetpoint(OpcUaClient client, NodeId setpointId, double setpoint)
      throws UaException {
    DataValue dataValue = DataValue.valueOnly(Variant.ofDouble(setpoint));
    List<StatusCode> results = client.writeValues(List.of(setpointId), List.of(dataValue));

    StatusCode status = results.get(0);
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  // snippet:write:end

  // snippet:write-batch:start
  static Map<NodeId, StatusCode> writeBatch(
      OpcUaClient client, List<NodeId> nodeIds, List<DataValue> values) throws UaException {
    if (nodeIds.size() != values.size()) {
      throw new IllegalArgumentException("Each NodeId needs exactly one value");
    }
    if (new HashSet<>(nodeIds).size() != nodeIds.size()) {
      throw new IllegalArgumentException("Each NodeId may appear only once");
    }

    List<StatusCode> results = client.writeValues(nodeIds, values);

    Map<NodeId, StatusCode> failures = new LinkedHashMap<>();
    for (int i = 0; i < nodeIds.size(); i++) {
      StatusCode status = results.get(i);
      if (!status.isGood()) {
        failures.put(nodeIds.get(i), status);
      }
    }
    return failures;
  }

  // snippet:write-batch:end

  // snippet:data_subscription:start
  static OpcUaSubscription watchSetpoint(OpcUaClient client, NodeId setpointId) throws UaException {
    var subscription = new OpcUaSubscription(client, 500.0);

    OpcUaMonitoredItem item = OpcUaMonitoredItem.newDataItem(setpointId);
    item.setSamplingInterval(100.0);
    item.setDataValueListener(
        (monitoredItem, value) -> {
          StatusCode status = value.statusCode();
          if (status.isGood()) {
            Object setpoint = value.value().value();
            System.out.println("Setpoint: " + setpoint);
          } else {
            System.out.println("Setpoint unavailable: " + status);
          }
        });
    subscription.addMonitoredItem(item);

    subscription.create();
    try {
      subscription.synchronizeMonitoredItems();
    } catch (MonitoredItemSynchronizationException e) {
      subscription.delete();
      throw e;
    }

    return subscription;
  }

  // snippet:data_subscription:end

  // snippet:event_subscription:start
  static OpcUaMonitoredItem watchServerEvents(OpcUaSubscription subscription) throws UaException {
    EventFilter filter =
        new EventFilterBuilder()
            .select(NodeIds.BaseEventType, new QualifiedName(0, "EventId"))
            .select(NodeIds.BaseEventType, new QualifiedName(0, "Message"))
            .build();

    OpcUaMonitoredItem item = OpcUaMonitoredItem.newEventItem(NodeIds.Server, filter);
    item.setEventValueListener(
        (monitoredItem, fields) -> {
          // Fields arrive in select-clause order: EventId, then Message.
          Object message = fields[1].value();
          if (message instanceof LocalizedText text) {
            System.out.println("Event: " + text.text());
          }
        });

    subscription.addMonitoredItem(item);
    subscription.synchronizeMonitoredItems();

    return item;
  }

  // snippet:event_subscription:end

  // snippet:delete-subscription:start
  static void stopWatching(OpcUaClient client, OpcUaSubscription subscription) throws UaException {
    try {
      subscription.delete();
    } finally {
      client.disconnect();
    }
  }

  // snippet:delete-subscription:end

  // snippet:method:start
  static double adjustSetpoint(
      OpcUaClient client, NodeId thermostatId, NodeId adjustSetpointId, double delta)
      throws UaException {
    Variant[] inputs = {new Variant(delta)};
    var request = new CallMethodRequest(thermostatId, adjustSetpointId, inputs);

    CallResponse response = client.call(List.of(request));
    CallMethodResult result = requireNonNull(response.getResults())[0];
    if (!result.getStatusCode().isGood()) {
      throw new UaException(result.getStatusCode());
    }

    Variant[] outputs = requireNonNull(result.getOutputArguments());
    Object newSetpoint = outputs[0].value();
    if (!(newSetpoint instanceof Double setpoint)) {
      throw new IllegalStateException("Expected a Double, got " + newSetpoint);
    }
    return setpoint;
  }

  // snippet:method:end

  // snippet:method-wrapper:start
  static double adjustSetpointWithWrapper(OpcUaClient client, NodeId thermostatId, double delta)
      throws UaException {
    UaObjectNode thermostat = client.getAddressSpace().getObjectNode(thermostatId);
    UaMethod adjustSetpoint = thermostat.getMethod("AdjustSetpoint");

    Variant[] inputs = {new Variant(delta)};
    Variant[] outputs = adjustSetpoint.call(inputs);

    Object newSetpoint = outputs[0].value();
    if (!(newSetpoint instanceof Double setpoint)) {
      throw new IllegalStateException("Expected a Double, got " + newSetpoint);
    }
    return setpoint;
  }

  // snippet:method-wrapper:end

  // snippet:node-wrappers:start
  static void updateSetpoint(
      OpcUaClient client, NodeId thermostatId, NodeId setpointId, double newSetpoint)
      throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();
    UaObjectNode thermostat = addressSpace.getObjectNode(thermostatId);
    UaVariableNode setpoint = addressSpace.getVariableNode(setpointId);

    // The lookup read DisplayName, so this getter answers from the cache.
    LocalizedText name = thermostat.getDisplayName();

    // readValue() reads from the server and caches the result.
    DataValue current = setpoint.readValue();
    if (!current.statusCode().isGood()) {
      throw new UaException(current.statusCode());
    }
    System.out.println(name.text() + " setpoint: " + current.value().value());

    // writeValue() writes to the server, then updates the cached Value.
    setpoint.writeValue(new Variant(newSetpoint));
  }

  // snippet:node-wrappers:end

  // snippet:node:start
  static void synchronizeSetpoint(OpcUaClient client, NodeId setpointId, double newSetpoint)
      throws UaException {
    UaVariableNode setpoint = client.getAddressSpace().getVariableNode(setpointId);
    Set<AttributeId> valueOnly = EnumSet.of(AttributeId.Value);

    // Stage the value in the local cache, then write it to the server.
    setpoint.setValue(new Variant(newSetpoint));
    List<StatusCode> writeResults = setpoint.synchronize(valueOnly);
    StatusCode writeStatus = writeResults.get(0);
    if (!writeStatus.isGood()) {
      throw new UaException(writeStatus);
    }

    // Read the value back so the cache holds what the server stored.
    List<DataValue> readResults = setpoint.refresh(valueOnly);
    DataValue refreshed = readResults.get(0);
    if (!refreshed.statusCode().isGood()) {
      throw new UaException(refreshed.statusCode());
    }
  }

  // snippet:node:end

  // snippet:recover-subscription:start
  static void recreateWhenLost(OpcUaSubscription subscription, Executor recoveryExecutor) {
    subscription.setSubscriptionListener(
        new OpcUaSubscription.SubscriptionListener() {
          @Override
          public void onTransferFailed(OpcUaSubscription lostSubscription, StatusCode status) {
            recoveryExecutor.execute(() -> recreate(lostSubscription));
          }

          @Override
          public void onStatusChanged(OpcUaSubscription changedSubscription, StatusCode status) {
            if (status.value() == StatusCodes.Bad_Timeout) {
              recoveryExecutor.execute(() -> recreate(changedSubscription));
            }
          }

          @Override
          public void onNotificationDataLost(OpcUaSubscription affectedSubscription) {
            LOGGER.warn("Notifications were lost. Reconcile from current values.");
          }
        });
  }

  static void recreate(OpcUaSubscription subscription) {
    try {
      // The client kept the MonitoredItems, so synchronizing creates them on the server again.
      subscription.create();
      subscription.synchronizeMonitoredItems();
    } catch (UaException e) {
      LOGGER.warn("Recreating the subscription failed", e);
    }
  }

  // snippet:recover-subscription:end

  // snippet:partition:start
  static List<DataValue> readInBatches(OpcUaClient client, List<NodeId> nodeIds, int maxBatchSize)
      throws UaException {
    if (maxBatchSize < 1) {
      throw new IllegalArgumentException("maxBatchSize must be positive");
    }

    // A server limit of 0 means the server advertises no limit.
    long serverLimit =
        client.getOperationLimits().maxNodesPerRead().map(UInteger::longValue).orElse(0L);

    int batchSize = maxBatchSize;
    if (serverLimit > 0 && serverLimit < batchSize) {
      batchSize = (int) serverLimit;
    }

    List<DataValue> values = new ArrayList<>();
    for (int start = 0; start < nodeIds.size(); start += batchSize) {
      int end = Math.min(start + batchSize, nodeIds.size());
      List<NodeId> batch = nodeIds.subList(start, end);
      values.addAll(client.readValues(0.0, TimestampsToReturn.Both, batch));
    }
    return values;
  }

  // snippet:partition:end

  // snippet:partition-thermostat:start
  static List<DataValue> readThermostat(OpcUaClient client) throws UaException {
    UShort namespaceIndex = client.getNamespaceTable().getIndex("urn:eclipse:milo:wiki");
    if (namespaceIndex == null) {
      throw new IllegalStateException("Server does not expose the tutorial namespace");
    }

    NodeId temperatureId = new NodeId(namespaceIndex, "Temperature");
    NodeId setpointId = new NodeId(namespaceIndex, "Setpoint");
    List<NodeId> nodeIds = List.of(temperatureId, setpointId);

    return readInBatches(client, nodeIds, 500);
  }

  // snippet:partition-thermostat:end

  // snippet:security:start
  static OpcUaClient secureClient(
      String discoveryUrl,
      KeyPair keyPair,
      X509Certificate[] certificateChain,
      X509Certificate trustedServer)
      throws UaException {
    var trust = new MemoryTrustListManager();
    trust.addTrustedCertificate(trustedServer);
    var quarantine = new MemoryCertificateQuarantine();
    var validator =
        new DefaultClientCertificateValidator(
            trust, ValidationCheck.ALL_OPTIONAL_CHECKS, quarantine);
    return OpcUaClient.create(
        discoveryUrl,
        endpoints ->
            endpoints.stream()
                .filter(
                    e -> SecurityPolicy.Basic256Sha256.getUri().equals(e.getSecurityPolicyUri()))
                .filter(e -> e.getSecurityMode() == MessageSecurityMode.SignAndEncrypt)
                .findFirst(),
        transport -> {},
        config ->
            config
                .setCertificateIdentity(keyPair, certificateChain)
                .setCertificateValidator(validator)
                .setSessionEndpointValidationEnabled(true)
                .setIdentityProvider(new AnonymousProvider()));
  }
  // snippet:security:end
}
