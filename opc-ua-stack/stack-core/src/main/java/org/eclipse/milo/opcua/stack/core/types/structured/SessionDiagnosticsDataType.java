/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.11">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.11</a>
 */
public class SessionDiagnosticsDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=865");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=867");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=866");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15368");

  private final NodeId sessionId;

  private final @Nullable String sessionName;

  private final ApplicationDescription clientDescription;

  private final @Nullable String serverUri;

  private final @Nullable String endpointUrl;

  private final String @Nullable [] localeIds;

  private final Double actualSessionTimeout;

  private final UInteger maxResponseMessageSize;

  private final DateTime clientConnectionTime;

  private final DateTime clientLastContactTime;

  private final UInteger currentSubscriptionsCount;

  private final UInteger currentMonitoredItemsCount;

  private final UInteger currentPublishRequestsInQueue;

  private final ServiceCounterDataType totalRequestCount;

  private final UInteger unauthorizedRequestCount;

  private final ServiceCounterDataType readCount;

  private final ServiceCounterDataType historyReadCount;

  private final ServiceCounterDataType writeCount;

  private final ServiceCounterDataType historyUpdateCount;

  private final ServiceCounterDataType callCount;

  private final ServiceCounterDataType createMonitoredItemsCount;

  private final ServiceCounterDataType modifyMonitoredItemsCount;

  private final ServiceCounterDataType setMonitoringModeCount;

  private final ServiceCounterDataType setTriggeringCount;

  private final ServiceCounterDataType deleteMonitoredItemsCount;

  private final ServiceCounterDataType createSubscriptionCount;

  private final ServiceCounterDataType modifySubscriptionCount;

  private final ServiceCounterDataType setPublishingModeCount;

  private final ServiceCounterDataType publishCount;

  private final ServiceCounterDataType republishCount;

  private final ServiceCounterDataType transferSubscriptionsCount;

  private final ServiceCounterDataType deleteSubscriptionsCount;

  private final ServiceCounterDataType addNodesCount;

  private final ServiceCounterDataType addReferencesCount;

  private final ServiceCounterDataType deleteNodesCount;

  private final ServiceCounterDataType deleteReferencesCount;

  private final ServiceCounterDataType browseCount;

  private final ServiceCounterDataType browseNextCount;

  private final ServiceCounterDataType translateBrowsePathsToNodeIdsCount;

  private final ServiceCounterDataType queryFirstCount;

  private final ServiceCounterDataType queryNextCount;

  private final ServiceCounterDataType registerNodesCount;

  private final ServiceCounterDataType unregisterNodesCount;

  public SessionDiagnosticsDataType(
      NodeId sessionId,
      @Nullable String sessionName,
      ApplicationDescription clientDescription,
      @Nullable String serverUri,
      @Nullable String endpointUrl,
      String @Nullable [] localeIds,
      Double actualSessionTimeout,
      UInteger maxResponseMessageSize,
      DateTime clientConnectionTime,
      DateTime clientLastContactTime,
      UInteger currentSubscriptionsCount,
      UInteger currentMonitoredItemsCount,
      UInteger currentPublishRequestsInQueue,
      ServiceCounterDataType totalRequestCount,
      UInteger unauthorizedRequestCount,
      ServiceCounterDataType readCount,
      ServiceCounterDataType historyReadCount,
      ServiceCounterDataType writeCount,
      ServiceCounterDataType historyUpdateCount,
      ServiceCounterDataType callCount,
      ServiceCounterDataType createMonitoredItemsCount,
      ServiceCounterDataType modifyMonitoredItemsCount,
      ServiceCounterDataType setMonitoringModeCount,
      ServiceCounterDataType setTriggeringCount,
      ServiceCounterDataType deleteMonitoredItemsCount,
      ServiceCounterDataType createSubscriptionCount,
      ServiceCounterDataType modifySubscriptionCount,
      ServiceCounterDataType setPublishingModeCount,
      ServiceCounterDataType publishCount,
      ServiceCounterDataType republishCount,
      ServiceCounterDataType transferSubscriptionsCount,
      ServiceCounterDataType deleteSubscriptionsCount,
      ServiceCounterDataType addNodesCount,
      ServiceCounterDataType addReferencesCount,
      ServiceCounterDataType deleteNodesCount,
      ServiceCounterDataType deleteReferencesCount,
      ServiceCounterDataType browseCount,
      ServiceCounterDataType browseNextCount,
      ServiceCounterDataType translateBrowsePathsToNodeIdsCount,
      ServiceCounterDataType queryFirstCount,
      ServiceCounterDataType queryNextCount,
      ServiceCounterDataType registerNodesCount,
      ServiceCounterDataType unregisterNodesCount) {
    this.sessionId = sessionId;
    this.sessionName = sessionName;
    this.clientDescription = clientDescription;
    this.serverUri = serverUri;
    this.endpointUrl = endpointUrl;
    this.localeIds = localeIds;
    this.actualSessionTimeout = actualSessionTimeout;
    this.maxResponseMessageSize = maxResponseMessageSize;
    this.clientConnectionTime = clientConnectionTime;
    this.clientLastContactTime = clientLastContactTime;
    this.currentSubscriptionsCount = currentSubscriptionsCount;
    this.currentMonitoredItemsCount = currentMonitoredItemsCount;
    this.currentPublishRequestsInQueue = currentPublishRequestsInQueue;
    this.totalRequestCount = totalRequestCount;
    this.unauthorizedRequestCount = unauthorizedRequestCount;
    this.readCount = readCount;
    this.historyReadCount = historyReadCount;
    this.writeCount = writeCount;
    this.historyUpdateCount = historyUpdateCount;
    this.callCount = callCount;
    this.createMonitoredItemsCount = createMonitoredItemsCount;
    this.modifyMonitoredItemsCount = modifyMonitoredItemsCount;
    this.setMonitoringModeCount = setMonitoringModeCount;
    this.setTriggeringCount = setTriggeringCount;
    this.deleteMonitoredItemsCount = deleteMonitoredItemsCount;
    this.createSubscriptionCount = createSubscriptionCount;
    this.modifySubscriptionCount = modifySubscriptionCount;
    this.setPublishingModeCount = setPublishingModeCount;
    this.publishCount = publishCount;
    this.republishCount = republishCount;
    this.transferSubscriptionsCount = transferSubscriptionsCount;
    this.deleteSubscriptionsCount = deleteSubscriptionsCount;
    this.addNodesCount = addNodesCount;
    this.addReferencesCount = addReferencesCount;
    this.deleteNodesCount = deleteNodesCount;
    this.deleteReferencesCount = deleteReferencesCount;
    this.browseCount = browseCount;
    this.browseNextCount = browseNextCount;
    this.translateBrowsePathsToNodeIdsCount = translateBrowsePathsToNodeIdsCount;
    this.queryFirstCount = queryFirstCount;
    this.queryNextCount = queryNextCount;
    this.registerNodesCount = registerNodesCount;
    this.unregisterNodesCount = unregisterNodesCount;
  }

  @Override
  public ExpandedNodeId getTypeId() {
    return TYPE_ID;
  }

  @Override
  public ExpandedNodeId getBinaryEncodingId() {
    return BINARY_ENCODING_ID;
  }

  @Override
  public ExpandedNodeId getXmlEncodingId() {
    return XML_ENCODING_ID;
  }

  @Override
  public ExpandedNodeId getJsonEncodingId() {
    return JSON_ENCODING_ID;
  }

  public NodeId getSessionId() {
    return sessionId;
  }

  public @Nullable String getSessionName() {
    return sessionName;
  }

  public ApplicationDescription getClientDescription() {
    return clientDescription;
  }

  public @Nullable String getServerUri() {
    return serverUri;
  }

  public @Nullable String getEndpointUrl() {
    return endpointUrl;
  }

  public String @Nullable [] getLocaleIds() {
    return localeIds;
  }

  public Double getActualSessionTimeout() {
    return actualSessionTimeout;
  }

  public UInteger getMaxResponseMessageSize() {
    return maxResponseMessageSize;
  }

  public DateTime getClientConnectionTime() {
    return clientConnectionTime;
  }

  public DateTime getClientLastContactTime() {
    return clientLastContactTime;
  }

  public UInteger getCurrentSubscriptionsCount() {
    return currentSubscriptionsCount;
  }

  public UInteger getCurrentMonitoredItemsCount() {
    return currentMonitoredItemsCount;
  }

  public UInteger getCurrentPublishRequestsInQueue() {
    return currentPublishRequestsInQueue;
  }

  public ServiceCounterDataType getTotalRequestCount() {
    return totalRequestCount;
  }

  public UInteger getUnauthorizedRequestCount() {
    return unauthorizedRequestCount;
  }

  public ServiceCounterDataType getReadCount() {
    return readCount;
  }

  public ServiceCounterDataType getHistoryReadCount() {
    return historyReadCount;
  }

  public ServiceCounterDataType getWriteCount() {
    return writeCount;
  }

  public ServiceCounterDataType getHistoryUpdateCount() {
    return historyUpdateCount;
  }

  public ServiceCounterDataType getCallCount() {
    return callCount;
  }

  public ServiceCounterDataType getCreateMonitoredItemsCount() {
    return createMonitoredItemsCount;
  }

  public ServiceCounterDataType getModifyMonitoredItemsCount() {
    return modifyMonitoredItemsCount;
  }

  public ServiceCounterDataType getSetMonitoringModeCount() {
    return setMonitoringModeCount;
  }

  public ServiceCounterDataType getSetTriggeringCount() {
    return setTriggeringCount;
  }

  public ServiceCounterDataType getDeleteMonitoredItemsCount() {
    return deleteMonitoredItemsCount;
  }

  public ServiceCounterDataType getCreateSubscriptionCount() {
    return createSubscriptionCount;
  }

  public ServiceCounterDataType getModifySubscriptionCount() {
    return modifySubscriptionCount;
  }

  public ServiceCounterDataType getSetPublishingModeCount() {
    return setPublishingModeCount;
  }

  public ServiceCounterDataType getPublishCount() {
    return publishCount;
  }

  public ServiceCounterDataType getRepublishCount() {
    return republishCount;
  }

  public ServiceCounterDataType getTransferSubscriptionsCount() {
    return transferSubscriptionsCount;
  }

  public ServiceCounterDataType getDeleteSubscriptionsCount() {
    return deleteSubscriptionsCount;
  }

  public ServiceCounterDataType getAddNodesCount() {
    return addNodesCount;
  }

  public ServiceCounterDataType getAddReferencesCount() {
    return addReferencesCount;
  }

  public ServiceCounterDataType getDeleteNodesCount() {
    return deleteNodesCount;
  }

  public ServiceCounterDataType getDeleteReferencesCount() {
    return deleteReferencesCount;
  }

  public ServiceCounterDataType getBrowseCount() {
    return browseCount;
  }

  public ServiceCounterDataType getBrowseNextCount() {
    return browseNextCount;
  }

  public ServiceCounterDataType getTranslateBrowsePathsToNodeIdsCount() {
    return translateBrowsePathsToNodeIdsCount;
  }

  public ServiceCounterDataType getQueryFirstCount() {
    return queryFirstCount;
  }

  public ServiceCounterDataType getQueryNextCount() {
    return queryNextCount;
  }

  public ServiceCounterDataType getRegisterNodesCount() {
    return registerNodesCount;
  }

  public ServiceCounterDataType getUnregisterNodesCount() {
    return unregisterNodesCount;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    SessionDiagnosticsDataType that = (SessionDiagnosticsDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getSessionId(), that.getSessionId());
    eqb.append(getSessionName(), that.getSessionName());
    eqb.append(getClientDescription(), that.getClientDescription());
    eqb.append(getServerUri(), that.getServerUri());
    eqb.append(getEndpointUrl(), that.getEndpointUrl());
    eqb.append(getLocaleIds(), that.getLocaleIds());
    eqb.append(getActualSessionTimeout(), that.getActualSessionTimeout());
    eqb.append(getMaxResponseMessageSize(), that.getMaxResponseMessageSize());
    eqb.append(getClientConnectionTime(), that.getClientConnectionTime());
    eqb.append(getClientLastContactTime(), that.getClientLastContactTime());
    eqb.append(getCurrentSubscriptionsCount(), that.getCurrentSubscriptionsCount());
    eqb.append(getCurrentMonitoredItemsCount(), that.getCurrentMonitoredItemsCount());
    eqb.append(getCurrentPublishRequestsInQueue(), that.getCurrentPublishRequestsInQueue());
    eqb.append(getTotalRequestCount(), that.getTotalRequestCount());
    eqb.append(getUnauthorizedRequestCount(), that.getUnauthorizedRequestCount());
    eqb.append(getReadCount(), that.getReadCount());
    eqb.append(getHistoryReadCount(), that.getHistoryReadCount());
    eqb.append(getWriteCount(), that.getWriteCount());
    eqb.append(getHistoryUpdateCount(), that.getHistoryUpdateCount());
    eqb.append(getCallCount(), that.getCallCount());
    eqb.append(getCreateMonitoredItemsCount(), that.getCreateMonitoredItemsCount());
    eqb.append(getModifyMonitoredItemsCount(), that.getModifyMonitoredItemsCount());
    eqb.append(getSetMonitoringModeCount(), that.getSetMonitoringModeCount());
    eqb.append(getSetTriggeringCount(), that.getSetTriggeringCount());
    eqb.append(getDeleteMonitoredItemsCount(), that.getDeleteMonitoredItemsCount());
    eqb.append(getCreateSubscriptionCount(), that.getCreateSubscriptionCount());
    eqb.append(getModifySubscriptionCount(), that.getModifySubscriptionCount());
    eqb.append(getSetPublishingModeCount(), that.getSetPublishingModeCount());
    eqb.append(getPublishCount(), that.getPublishCount());
    eqb.append(getRepublishCount(), that.getRepublishCount());
    eqb.append(getTransferSubscriptionsCount(), that.getTransferSubscriptionsCount());
    eqb.append(getDeleteSubscriptionsCount(), that.getDeleteSubscriptionsCount());
    eqb.append(getAddNodesCount(), that.getAddNodesCount());
    eqb.append(getAddReferencesCount(), that.getAddReferencesCount());
    eqb.append(getDeleteNodesCount(), that.getDeleteNodesCount());
    eqb.append(getDeleteReferencesCount(), that.getDeleteReferencesCount());
    eqb.append(getBrowseCount(), that.getBrowseCount());
    eqb.append(getBrowseNextCount(), that.getBrowseNextCount());
    eqb.append(
        getTranslateBrowsePathsToNodeIdsCount(), that.getTranslateBrowsePathsToNodeIdsCount());
    eqb.append(getQueryFirstCount(), that.getQueryFirstCount());
    eqb.append(getQueryNextCount(), that.getQueryNextCount());
    eqb.append(getRegisterNodesCount(), that.getRegisterNodesCount());
    eqb.append(getUnregisterNodesCount(), that.getUnregisterNodesCount());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getSessionId());
    hcb.append(getSessionName());
    hcb.append(getClientDescription());
    hcb.append(getServerUri());
    hcb.append(getEndpointUrl());
    hcb.append(getLocaleIds());
    hcb.append(getActualSessionTimeout());
    hcb.append(getMaxResponseMessageSize());
    hcb.append(getClientConnectionTime());
    hcb.append(getClientLastContactTime());
    hcb.append(getCurrentSubscriptionsCount());
    hcb.append(getCurrentMonitoredItemsCount());
    hcb.append(getCurrentPublishRequestsInQueue());
    hcb.append(getTotalRequestCount());
    hcb.append(getUnauthorizedRequestCount());
    hcb.append(getReadCount());
    hcb.append(getHistoryReadCount());
    hcb.append(getWriteCount());
    hcb.append(getHistoryUpdateCount());
    hcb.append(getCallCount());
    hcb.append(getCreateMonitoredItemsCount());
    hcb.append(getModifyMonitoredItemsCount());
    hcb.append(getSetMonitoringModeCount());
    hcb.append(getSetTriggeringCount());
    hcb.append(getDeleteMonitoredItemsCount());
    hcb.append(getCreateSubscriptionCount());
    hcb.append(getModifySubscriptionCount());
    hcb.append(getSetPublishingModeCount());
    hcb.append(getPublishCount());
    hcb.append(getRepublishCount());
    hcb.append(getTransferSubscriptionsCount());
    hcb.append(getDeleteSubscriptionsCount());
    hcb.append(getAddNodesCount());
    hcb.append(getAddReferencesCount());
    hcb.append(getDeleteNodesCount());
    hcb.append(getDeleteReferencesCount());
    hcb.append(getBrowseCount());
    hcb.append(getBrowseNextCount());
    hcb.append(getTranslateBrowsePathsToNodeIdsCount());
    hcb.append(getQueryFirstCount());
    hcb.append(getQueryNextCount());
    hcb.append(getRegisterNodesCount());
    hcb.append(getUnregisterNodesCount());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", SessionDiagnosticsDataType.class.getSimpleName() + "[", "]");
    joiner.add("sessionId=" + getSessionId());
    joiner.add("sessionName='" + getSessionName() + "'");
    joiner.add("clientDescription=" + getClientDescription());
    joiner.add("serverUri='" + getServerUri() + "'");
    joiner.add("endpointUrl='" + getEndpointUrl() + "'");
    joiner.add("localeIds=" + java.util.Arrays.toString(getLocaleIds()));
    joiner.add("actualSessionTimeout=" + getActualSessionTimeout());
    joiner.add("maxResponseMessageSize=" + getMaxResponseMessageSize());
    joiner.add("clientConnectionTime=" + getClientConnectionTime());
    joiner.add("clientLastContactTime=" + getClientLastContactTime());
    joiner.add("currentSubscriptionsCount=" + getCurrentSubscriptionsCount());
    joiner.add("currentMonitoredItemsCount=" + getCurrentMonitoredItemsCount());
    joiner.add("currentPublishRequestsInQueue=" + getCurrentPublishRequestsInQueue());
    joiner.add("totalRequestCount=" + getTotalRequestCount());
    joiner.add("unauthorizedRequestCount=" + getUnauthorizedRequestCount());
    joiner.add("readCount=" + getReadCount());
    joiner.add("historyReadCount=" + getHistoryReadCount());
    joiner.add("writeCount=" + getWriteCount());
    joiner.add("historyUpdateCount=" + getHistoryUpdateCount());
    joiner.add("callCount=" + getCallCount());
    joiner.add("createMonitoredItemsCount=" + getCreateMonitoredItemsCount());
    joiner.add("modifyMonitoredItemsCount=" + getModifyMonitoredItemsCount());
    joiner.add("setMonitoringModeCount=" + getSetMonitoringModeCount());
    joiner.add("setTriggeringCount=" + getSetTriggeringCount());
    joiner.add("deleteMonitoredItemsCount=" + getDeleteMonitoredItemsCount());
    joiner.add("createSubscriptionCount=" + getCreateSubscriptionCount());
    joiner.add("modifySubscriptionCount=" + getModifySubscriptionCount());
    joiner.add("setPublishingModeCount=" + getSetPublishingModeCount());
    joiner.add("publishCount=" + getPublishCount());
    joiner.add("republishCount=" + getRepublishCount());
    joiner.add("transferSubscriptionsCount=" + getTransferSubscriptionsCount());
    joiner.add("deleteSubscriptionsCount=" + getDeleteSubscriptionsCount());
    joiner.add("addNodesCount=" + getAddNodesCount());
    joiner.add("addReferencesCount=" + getAddReferencesCount());
    joiner.add("deleteNodesCount=" + getDeleteNodesCount());
    joiner.add("deleteReferencesCount=" + getDeleteReferencesCount());
    joiner.add("browseCount=" + getBrowseCount());
    joiner.add("browseNextCount=" + getBrowseNextCount());
    joiner.add("translateBrowsePathsToNodeIdsCount=" + getTranslateBrowsePathsToNodeIdsCount());
    joiner.add("queryFirstCount=" + getQueryFirstCount());
    joiner.add("queryNextCount=" + getQueryNextCount());
    joiner.add("registerNodesCount=" + getRegisterNodesCount());
    joiner.add("unregisterNodesCount=" + getUnregisterNodesCount());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 867),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "SessionId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SessionName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ClientDescription",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 308),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ServerUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "EndpointUrl",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "LocaleIds",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 295),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ActualSessionTimeout",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MaxResponseMessageSize",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ClientConnectionTime",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 294),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ClientLastContactTime",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 294),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "CurrentSubscriptionsCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "CurrentMonitoredItemsCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "CurrentPublishRequestsInQueue",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TotalRequestCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UnauthorizedRequestCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ReadCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "HistoryReadCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "WriteCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "HistoryUpdateCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "CallCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "CreateMonitoredItemsCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ModifyMonitoredItemsCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SetMonitoringModeCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SetTriggeringCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DeleteMonitoredItemsCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "CreateSubscriptionCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ModifySubscriptionCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SetPublishingModeCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "PublishCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RepublishCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TransferSubscriptionsCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DeleteSubscriptionsCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AddNodesCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AddReferencesCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DeleteNodesCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DeleteReferencesCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "BrowseCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "BrowseNextCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TranslateBrowsePathsToNodeIdsCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "QueryFirstCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "QueryNextCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RegisterNodesCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UnregisterNodesCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 871),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<SessionDiagnosticsDataType> {
    @Override
    public Class<SessionDiagnosticsDataType> getType() {
      return SessionDiagnosticsDataType.class;
    }

    @Override
    public SessionDiagnosticsDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final NodeId sessionId;
      final String sessionName;
      final ApplicationDescription clientDescription;
      final String serverUri;
      final String endpointUrl;
      final String[] localeIds;
      final Double actualSessionTimeout;
      final UInteger maxResponseMessageSize;
      final DateTime clientConnectionTime;
      final DateTime clientLastContactTime;
      final UInteger currentSubscriptionsCount;
      final UInteger currentMonitoredItemsCount;
      final UInteger currentPublishRequestsInQueue;
      final ServiceCounterDataType totalRequestCount;
      final UInteger unauthorizedRequestCount;
      final ServiceCounterDataType readCount;
      final ServiceCounterDataType historyReadCount;
      final ServiceCounterDataType writeCount;
      final ServiceCounterDataType historyUpdateCount;
      final ServiceCounterDataType callCount;
      final ServiceCounterDataType createMonitoredItemsCount;
      final ServiceCounterDataType modifyMonitoredItemsCount;
      final ServiceCounterDataType setMonitoringModeCount;
      final ServiceCounterDataType setTriggeringCount;
      final ServiceCounterDataType deleteMonitoredItemsCount;
      final ServiceCounterDataType createSubscriptionCount;
      final ServiceCounterDataType modifySubscriptionCount;
      final ServiceCounterDataType setPublishingModeCount;
      final ServiceCounterDataType publishCount;
      final ServiceCounterDataType republishCount;
      final ServiceCounterDataType transferSubscriptionsCount;
      final ServiceCounterDataType deleteSubscriptionsCount;
      final ServiceCounterDataType addNodesCount;
      final ServiceCounterDataType addReferencesCount;
      final ServiceCounterDataType deleteNodesCount;
      final ServiceCounterDataType deleteReferencesCount;
      final ServiceCounterDataType browseCount;
      final ServiceCounterDataType browseNextCount;
      final ServiceCounterDataType translateBrowsePathsToNodeIdsCount;
      final ServiceCounterDataType queryFirstCount;
      final ServiceCounterDataType queryNextCount;
      final ServiceCounterDataType registerNodesCount;
      final ServiceCounterDataType unregisterNodesCount;
      sessionId = decoder.decodeNodeId("SessionId");
      sessionName = decoder.decodeString("SessionName");
      clientDescription =
          (ApplicationDescription)
              decoder.decodeStruct("ClientDescription", ApplicationDescription.TYPE_ID);
      serverUri = decoder.decodeString("ServerUri");
      endpointUrl = decoder.decodeString("EndpointUrl");
      localeIds = decoder.decodeStringArray("LocaleIds");
      actualSessionTimeout = decoder.decodeDouble("ActualSessionTimeout");
      maxResponseMessageSize = decoder.decodeUInt32("MaxResponseMessageSize");
      clientConnectionTime = decoder.decodeDateTime("ClientConnectionTime");
      clientLastContactTime = decoder.decodeDateTime("ClientLastContactTime");
      currentSubscriptionsCount = decoder.decodeUInt32("CurrentSubscriptionsCount");
      currentMonitoredItemsCount = decoder.decodeUInt32("CurrentMonitoredItemsCount");
      currentPublishRequestsInQueue = decoder.decodeUInt32("CurrentPublishRequestsInQueue");
      totalRequestCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("TotalRequestCount", ServiceCounterDataType.TYPE_ID);
      unauthorizedRequestCount = decoder.decodeUInt32("UnauthorizedRequestCount");
      readCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("ReadCount", ServiceCounterDataType.TYPE_ID);
      historyReadCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("HistoryReadCount", ServiceCounterDataType.TYPE_ID);
      writeCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("WriteCount", ServiceCounterDataType.TYPE_ID);
      historyUpdateCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("HistoryUpdateCount", ServiceCounterDataType.TYPE_ID);
      callCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("CallCount", ServiceCounterDataType.TYPE_ID);
      createMonitoredItemsCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("CreateMonitoredItemsCount", ServiceCounterDataType.TYPE_ID);
      modifyMonitoredItemsCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("ModifyMonitoredItemsCount", ServiceCounterDataType.TYPE_ID);
      setMonitoringModeCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("SetMonitoringModeCount", ServiceCounterDataType.TYPE_ID);
      setTriggeringCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("SetTriggeringCount", ServiceCounterDataType.TYPE_ID);
      deleteMonitoredItemsCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("DeleteMonitoredItemsCount", ServiceCounterDataType.TYPE_ID);
      createSubscriptionCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("CreateSubscriptionCount", ServiceCounterDataType.TYPE_ID);
      modifySubscriptionCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("ModifySubscriptionCount", ServiceCounterDataType.TYPE_ID);
      setPublishingModeCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("SetPublishingModeCount", ServiceCounterDataType.TYPE_ID);
      publishCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("PublishCount", ServiceCounterDataType.TYPE_ID);
      republishCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("RepublishCount", ServiceCounterDataType.TYPE_ID);
      transferSubscriptionsCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("TransferSubscriptionsCount", ServiceCounterDataType.TYPE_ID);
      deleteSubscriptionsCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("DeleteSubscriptionsCount", ServiceCounterDataType.TYPE_ID);
      addNodesCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("AddNodesCount", ServiceCounterDataType.TYPE_ID);
      addReferencesCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("AddReferencesCount", ServiceCounterDataType.TYPE_ID);
      deleteNodesCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("DeleteNodesCount", ServiceCounterDataType.TYPE_ID);
      deleteReferencesCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("DeleteReferencesCount", ServiceCounterDataType.TYPE_ID);
      browseCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("BrowseCount", ServiceCounterDataType.TYPE_ID);
      browseNextCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("BrowseNextCount", ServiceCounterDataType.TYPE_ID);
      translateBrowsePathsToNodeIdsCount =
          (ServiceCounterDataType)
              decoder.decodeStruct(
                  "TranslateBrowsePathsToNodeIdsCount", ServiceCounterDataType.TYPE_ID);
      queryFirstCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("QueryFirstCount", ServiceCounterDataType.TYPE_ID);
      queryNextCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("QueryNextCount", ServiceCounterDataType.TYPE_ID);
      registerNodesCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("RegisterNodesCount", ServiceCounterDataType.TYPE_ID);
      unregisterNodesCount =
          (ServiceCounterDataType)
              decoder.decodeStruct("UnregisterNodesCount", ServiceCounterDataType.TYPE_ID);
      return new SessionDiagnosticsDataType(
          sessionId,
          sessionName,
          clientDescription,
          serverUri,
          endpointUrl,
          localeIds,
          actualSessionTimeout,
          maxResponseMessageSize,
          clientConnectionTime,
          clientLastContactTime,
          currentSubscriptionsCount,
          currentMonitoredItemsCount,
          currentPublishRequestsInQueue,
          totalRequestCount,
          unauthorizedRequestCount,
          readCount,
          historyReadCount,
          writeCount,
          historyUpdateCount,
          callCount,
          createMonitoredItemsCount,
          modifyMonitoredItemsCount,
          setMonitoringModeCount,
          setTriggeringCount,
          deleteMonitoredItemsCount,
          createSubscriptionCount,
          modifySubscriptionCount,
          setPublishingModeCount,
          publishCount,
          republishCount,
          transferSubscriptionsCount,
          deleteSubscriptionsCount,
          addNodesCount,
          addReferencesCount,
          deleteNodesCount,
          deleteReferencesCount,
          browseCount,
          browseNextCount,
          translateBrowsePathsToNodeIdsCount,
          queryFirstCount,
          queryNextCount,
          registerNodesCount,
          unregisterNodesCount);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, SessionDiagnosticsDataType value) {
      encoder.encodeNodeId("SessionId", value.getSessionId());
      encoder.encodeString("SessionName", value.getSessionName());
      encoder.encodeStruct(
          "ClientDescription", value.getClientDescription(), ApplicationDescription.TYPE_ID);
      encoder.encodeString("ServerUri", value.getServerUri());
      encoder.encodeString("EndpointUrl", value.getEndpointUrl());
      encoder.encodeStringArray("LocaleIds", value.getLocaleIds());
      encoder.encodeDouble("ActualSessionTimeout", value.getActualSessionTimeout());
      encoder.encodeUInt32("MaxResponseMessageSize", value.getMaxResponseMessageSize());
      encoder.encodeDateTime("ClientConnectionTime", value.getClientConnectionTime());
      encoder.encodeDateTime("ClientLastContactTime", value.getClientLastContactTime());
      encoder.encodeUInt32("CurrentSubscriptionsCount", value.getCurrentSubscriptionsCount());
      encoder.encodeUInt32("CurrentMonitoredItemsCount", value.getCurrentMonitoredItemsCount());
      encoder.encodeUInt32(
          "CurrentPublishRequestsInQueue", value.getCurrentPublishRequestsInQueue());
      encoder.encodeStruct(
          "TotalRequestCount", value.getTotalRequestCount(), ServiceCounterDataType.TYPE_ID);
      encoder.encodeUInt32("UnauthorizedRequestCount", value.getUnauthorizedRequestCount());
      encoder.encodeStruct("ReadCount", value.getReadCount(), ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct(
          "HistoryReadCount", value.getHistoryReadCount(), ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct("WriteCount", value.getWriteCount(), ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct(
          "HistoryUpdateCount", value.getHistoryUpdateCount(), ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct("CallCount", value.getCallCount(), ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct(
          "CreateMonitoredItemsCount",
          value.getCreateMonitoredItemsCount(),
          ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct(
          "ModifyMonitoredItemsCount",
          value.getModifyMonitoredItemsCount(),
          ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct(
          "SetMonitoringModeCount",
          value.getSetMonitoringModeCount(),
          ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct(
          "SetTriggeringCount", value.getSetTriggeringCount(), ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct(
          "DeleteMonitoredItemsCount",
          value.getDeleteMonitoredItemsCount(),
          ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct(
          "CreateSubscriptionCount",
          value.getCreateSubscriptionCount(),
          ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct(
          "ModifySubscriptionCount",
          value.getModifySubscriptionCount(),
          ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct(
          "SetPublishingModeCount",
          value.getSetPublishingModeCount(),
          ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct("PublishCount", value.getPublishCount(), ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct(
          "RepublishCount", value.getRepublishCount(), ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct(
          "TransferSubscriptionsCount",
          value.getTransferSubscriptionsCount(),
          ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct(
          "DeleteSubscriptionsCount",
          value.getDeleteSubscriptionsCount(),
          ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct(
          "AddNodesCount", value.getAddNodesCount(), ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct(
          "AddReferencesCount", value.getAddReferencesCount(), ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct(
          "DeleteNodesCount", value.getDeleteNodesCount(), ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct(
          "DeleteReferencesCount",
          value.getDeleteReferencesCount(),
          ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct("BrowseCount", value.getBrowseCount(), ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct(
          "BrowseNextCount", value.getBrowseNextCount(), ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct(
          "TranslateBrowsePathsToNodeIdsCount",
          value.getTranslateBrowsePathsToNodeIdsCount(),
          ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct(
          "QueryFirstCount", value.getQueryFirstCount(), ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct(
          "QueryNextCount", value.getQueryNextCount(), ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct(
          "RegisterNodesCount", value.getRegisterNodesCount(), ServiceCounterDataType.TYPE_ID);
      encoder.encodeStruct(
          "UnregisterNodesCount", value.getUnregisterNodesCount(), ServiceCounterDataType.TYPE_ID);
    }
  }
}
