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
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.AddressSpace;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.client.subscriptions.EventFilterBuilder;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.DefaultClientCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.util.validation.ValidationCheck;

/** Client samples from the Wiki's connecting, service, node, subscription, and security pages. */
public final class ClientSnippets {

  private ClientSnippets() {}

  // snippet:connect:start
  static DataValue connectAndRead(String discoveryUrl) throws Exception {
    OpcUaClient client =
        OpcUaClient.create(
            discoveryUrl,
            endpoints ->
                endpoints.stream()
                    .filter(e -> SecurityPolicy.None.getUri().equals(e.getSecurityPolicyUri()))
                    .filter(e -> e.getSecurityMode() == MessageSecurityMode.None)
                    .findFirst(),
            transport -> {},
            config ->
                config.setIdentityProvider(new AnonymousProvider()).setRequestTimeout(uint(5_000)));
    try {
      client.connectAsync().get(10, TimeUnit.SECONDS);
      DataValue value =
          client.readValue(0.0, TimestampsToReturn.Both, NodeIds.Server_ServerStatus_CurrentTime);
      if (!value.statusCode().isGood()) {
        throw new UaException(value.statusCode());
      }
      return value;
    } finally {
      client.disconnectAsync().get(10, TimeUnit.SECONDS);
    }
  }

  // snippet:connect:end

  // snippet:read:start
  static int readInt32(OpcUaClient client, NodeId nodeId) throws UaException {
    DataValue value = client.readValue(0.0, TimestampsToReturn.Both, nodeId);
    if (!value.statusCode().isGood()) {
      throw new UaException(value.statusCode());
    }

    Object body = value.value().value();
    if (!(body instanceof Integer integer)) {
      throw new IllegalArgumentException("Expected an Int32, got " + body);
    }
    return integer;
  }

  // snippet:read:end

  // snippet:write:start
  static void writeInt32(OpcUaClient client, NodeId nodeId, int value) throws UaException {
    DataValue dataValue = DataValue.valueOnly(new Variant(value));
    List<StatusCode> results = client.writeValues(List.of(nodeId), List.of(dataValue));

    StatusCode status = results.get(0);
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  // snippet:write:end

  // snippet:browse:start
  static List<ReferenceDescription> browseProperties(OpcUaClient client) throws UaException {
    AddressSpace.BrowseOptions options =
        AddressSpace.BrowseOptions.builder()
            .setReferenceType(NodeIds.HasProperty)
            .setIncludeSubtypes(true)
            .setMaxReferencesPerNode(uint(1))
            .build();
    return client.getAddressSpace().getNode(NodeIds.Server).browse(options);
  }

  // snippet:browse:end

  // snippet:node:start
  static void synchronizeValue(OpcUaClient client, NodeId nodeId, int value) throws UaException {
    UaVariableNode node = client.getAddressSpace().getVariableNode(nodeId);

    // Stage the value in the local cache, then write it to the server.
    node.setValue(new Variant(value));
    StatusCode writeStatus = node.synchronize(EnumSet.of(AttributeId.Value)).get(0);
    if (!writeStatus.isGood()) {
      throw new UaException(writeStatus);
    }

    // Read the value back so the cache holds what the server stored.
    DataValue refreshed = node.refresh(EnumSet.of(AttributeId.Value)).get(0);
    if (!refreshed.statusCode().isGood()) {
      throw new UaException(refreshed.statusCode());
    }
  }

  // snippet:node:end

  // snippet:method:start
  static double callSquareRoot(OpcUaClient client, NodeId objectId, NodeId methodId, double input)
      throws UaException {
    Variant[] inputs = {new Variant(input)};
    var request = new CallMethodRequest(objectId, methodId, inputs);

    CallResponse response = client.call(List.of(request));
    CallMethodResult result = requireNonNull(response.getResults())[0];
    if (!result.getStatusCode().isGood()) {
      throw new UaException(result.getStatusCode());
    }

    Variant[] outputs = requireNonNull(result.getOutputArguments());
    return (Double) outputs[0].value();
  }

  // snippet:method:end

  // snippet:data_subscription:start
  static DataValue firstNotification(OpcUaClient client, NodeId nodeId) throws Exception {
    var subscription = new OpcUaSubscription(client);
    subscription.setPublishingInterval(100.0);
    var firstValue = new CompletableFuture<DataValue>();
    var item = OpcUaMonitoredItem.newDataItem(nodeId);
    item.setSamplingInterval(100.0);
    item.setDataValueListener((ignored, value) -> firstValue.complete(value));
    subscription.addMonitoredItem(item);
    try {
      subscription.create();
      subscription.synchronizeMonitoredItems();
      DataValue value = firstValue.get(10, TimeUnit.SECONDS);
      if (!value.statusCode().isGood()) {
        throw new UaException(value.statusCode());
      }
      return value;
    } finally {
      subscription.delete();
    }
  }

  // snippet:data_subscription:end

  // snippet:event_subscription:start
  static Variant[] firstEvent(OpcUaClient client, NodeId notifierId) throws Exception {
    var subscription = new OpcUaSubscription(client);
    subscription.setPublishingInterval(100.0);
    EventFilter filter =
        new EventFilterBuilder()
            .select(NodeIds.BaseEventType, new QualifiedName(0, "EventId"))
            .select(NodeIds.BaseEventType, new QualifiedName(0, "Message"))
            .build();
    var item = OpcUaMonitoredItem.newEventItem(notifierId, filter);
    var firstFields = new CompletableFuture<Variant[]>();
    item.setEventValueListener((ignored, fields) -> firstFields.complete(fields));
    subscription.addMonitoredItem(item);
    try {
      subscription.create();
      subscription.synchronizeMonitoredItems();
      return firstFields.get(10, TimeUnit.SECONDS);
    } finally {
      subscription.delete();
    }
  }

  // snippet:event_subscription:end

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
