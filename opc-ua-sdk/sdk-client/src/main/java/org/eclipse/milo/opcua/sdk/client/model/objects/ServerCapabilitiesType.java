/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.2">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ServerCapabilitiesType extends BaseObjectType {
  QualifiedProperty<String[]> SERVER_PROFILE_ARRAY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ServerProfileArray",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          1,
          String[].class);

  QualifiedProperty<String[]> LOCALE_ID_ARRAY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LocaleIdArray",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=295"),
          1,
          String[].class);

  QualifiedProperty<Double> MIN_SUPPORTED_SAMPLE_RATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MinSupportedSampleRate",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<UShort> MAX_BROWSE_CONTINUATION_POINTS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxBrowseContinuationPoints",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<UShort> MAX_QUERY_CONTINUATION_POINTS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxQueryContinuationPoints",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<UShort> MAX_HISTORY_CONTINUATION_POINTS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxHistoryContinuationPoints",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<UShort> MAX_LOG_OBJECT_CONTINUATION_POINTS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxLogObjectContinuationPoints",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<SignedSoftwareCertificate[]> SOFTWARE_CERTIFICATES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SoftwareCertificates",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=344"),
          1,
          SignedSoftwareCertificate[].class);

  QualifiedProperty<UInteger> MAX_ARRAY_LENGTH =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxArrayLength",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_STRING_LENGTH =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxStringLength",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_BYTE_STRING_LENGTH =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxByteStringLength",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_SESSIONS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxSessions",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_SUBSCRIPTIONS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxSubscriptions",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_MONITORED_ITEMS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxMonitoredItems",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_SUBSCRIPTIONS_PER_SESSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxSubscriptionsPerSession",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_MONITORED_ITEMS_PER_SUBSCRIPTION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxMonitoredItemsPerSubscription",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_SELECT_CLAUSE_PARAMETERS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxSelectClauseParameters",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_WHERE_CLAUSE_PARAMETERS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxWhereClauseParameters",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_MONITORED_ITEMS_QUEUE_SIZE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxMonitoredItemsQueueSize",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<QualifiedName[]> CONFORMANCE_UNITS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConformanceUnits",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20"),
          1,
          QualifiedName[].class);

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getServerProfileArray() throws UaException;

  /** Sets the existing node's local value. */
  void setServerProfileArray(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String @Nullable [] readServerProfileArray() throws UaException;

  /** Writes the value remotely. */
  void writeServerProfileArray(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String @Nullable []> readServerProfileArrayAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeServerProfileArrayAsync(@Nullable String @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getServerProfileArrayNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getServerProfileArrayNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getLocaleIdArray() throws UaException;

  /** Sets the existing node's local value. */
  void setLocaleIdArray(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String @Nullable [] readLocaleIdArray() throws UaException;

  /** Writes the value remotely. */
  void writeLocaleIdArray(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String @Nullable []> readLocaleIdArrayAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLocaleIdArrayAsync(@Nullable String @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLocaleIdArrayNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getLocaleIdArrayNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getMinSupportedSampleRate() throws UaException;

  /** Sets the existing node's local value. */
  void setMinSupportedSampleRate(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readMinSupportedSampleRate() throws UaException;

  /** Writes the value remotely. */
  void writeMinSupportedSampleRate(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readMinSupportedSampleRateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMinSupportedSampleRateAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMinSupportedSampleRateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getMinSupportedSampleRateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UShort getMaxBrowseContinuationPoints() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxBrowseContinuationPoints(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readMaxBrowseContinuationPoints() throws UaException;

  /** Writes the value remotely. */
  void writeMaxBrowseContinuationPoints(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readMaxBrowseContinuationPointsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxBrowseContinuationPointsAsync(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxBrowseContinuationPointsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getMaxBrowseContinuationPointsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UShort getMaxQueryContinuationPoints() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxQueryContinuationPoints(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readMaxQueryContinuationPoints() throws UaException;

  /** Writes the value remotely. */
  void writeMaxQueryContinuationPoints(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readMaxQueryContinuationPointsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxQueryContinuationPointsAsync(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxQueryContinuationPointsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getMaxQueryContinuationPointsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UShort getMaxHistoryContinuationPoints() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxHistoryContinuationPoints(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readMaxHistoryContinuationPoints() throws UaException;

  /** Writes the value remotely. */
  void writeMaxHistoryContinuationPoints(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readMaxHistoryContinuationPointsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxHistoryContinuationPointsAsync(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxHistoryContinuationPointsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getMaxHistoryContinuationPointsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UShort getMaxLogObjectContinuationPoints() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxLogObjectContinuationPoints(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readMaxLogObjectContinuationPoints() throws UaException;

  /** Writes the value remotely. */
  void writeMaxLogObjectContinuationPoints(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readMaxLogObjectContinuationPointsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxLogObjectContinuationPointsAsync(@Nullable UShort value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxLogObjectContinuationPointsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxLogObjectContinuationPointsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable SignedSoftwareCertificate @Nullable [] getSoftwareCertificates() throws UaException;

  /** Sets the existing node's local value. */
  void setSoftwareCertificates(@Nullable SignedSoftwareCertificate @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable SignedSoftwareCertificate @Nullable [] readSoftwareCertificates() throws UaException;

  /** Writes the value remotely. */
  void writeSoftwareCertificates(@Nullable SignedSoftwareCertificate @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable SignedSoftwareCertificate @Nullable []>
      readSoftwareCertificatesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSoftwareCertificatesAsync(
      @Nullable SignedSoftwareCertificate @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSoftwareCertificatesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getSoftwareCertificatesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxArrayLength() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxArrayLength(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxArrayLength() throws UaException;

  /** Writes the value remotely. */
  void writeMaxArrayLength(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxArrayLengthAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxArrayLengthAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxArrayLengthNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxArrayLengthNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxStringLength() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxStringLength(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxStringLength() throws UaException;

  /** Writes the value remotely. */
  void writeMaxStringLength(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxStringLengthAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxStringLengthAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxStringLengthNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxStringLengthNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxByteStringLength() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxByteStringLength(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxByteStringLength() throws UaException;

  /** Writes the value remotely. */
  void writeMaxByteStringLength(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxByteStringLengthAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxByteStringLengthAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxByteStringLengthNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxByteStringLengthNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxSessions() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxSessions(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxSessions() throws UaException;

  /** Writes the value remotely. */
  void writeMaxSessions(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxSessionsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxSessionsAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxSessionsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxSessionsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxSubscriptions() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxSubscriptions(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxSubscriptions() throws UaException;

  /** Writes the value remotely. */
  void writeMaxSubscriptions(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxSubscriptionsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxSubscriptionsAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxSubscriptionsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxSubscriptionsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxMonitoredItems() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxMonitoredItems(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxMonitoredItems() throws UaException;

  /** Writes the value remotely. */
  void writeMaxMonitoredItems(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxMonitoredItemsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxMonitoredItemsAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxMonitoredItemsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxMonitoredItemsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxSubscriptionsPerSession() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxSubscriptionsPerSession(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxSubscriptionsPerSession() throws UaException;

  /** Writes the value remotely. */
  void writeMaxSubscriptionsPerSession(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxSubscriptionsPerSessionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxSubscriptionsPerSessionAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxSubscriptionsPerSessionNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxSubscriptionsPerSessionNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxMonitoredItemsPerSubscription() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxMonitoredItemsPerSubscription(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxMonitoredItemsPerSubscription() throws UaException;

  /** Writes the value remotely. */
  void writeMaxMonitoredItemsPerSubscription(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxMonitoredItemsPerSubscriptionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxMonitoredItemsPerSubscriptionAsync(
      @Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxMonitoredItemsPerSubscriptionNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType>
      getMaxMonitoredItemsPerSubscriptionNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxSelectClauseParameters() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxSelectClauseParameters(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxSelectClauseParameters() throws UaException;

  /** Writes the value remotely. */
  void writeMaxSelectClauseParameters(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxSelectClauseParametersAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxSelectClauseParametersAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxSelectClauseParametersNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxSelectClauseParametersNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxWhereClauseParameters() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxWhereClauseParameters(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxWhereClauseParameters() throws UaException;

  /** Writes the value remotely. */
  void writeMaxWhereClauseParameters(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxWhereClauseParametersAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxWhereClauseParametersAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxWhereClauseParametersNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxWhereClauseParametersNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxMonitoredItemsQueueSize() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxMonitoredItemsQueueSize(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxMonitoredItemsQueueSize() throws UaException;

  /** Writes the value remotely. */
  void writeMaxMonitoredItemsQueueSize(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxMonitoredItemsQueueSizeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxMonitoredItemsQueueSizeAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxMonitoredItemsQueueSizeNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxMonitoredItemsQueueSizeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable QualifiedName @Nullable [] getConformanceUnits() throws UaException;

  /** Sets the existing node's local value. */
  void setConformanceUnits(@Nullable QualifiedName @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable QualifiedName @Nullable [] readConformanceUnits() throws UaException;

  /** Writes the value remotely. */
  void writeConformanceUnits(@Nullable QualifiedName @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable QualifiedName @Nullable []> readConformanceUnitsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeConformanceUnitsAsync(
      @Nullable QualifiedName @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getConformanceUnitsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getConformanceUnitsNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable OperationLimitsType getOperationLimitsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable OperationLimitsType> getOperationLimitsNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  FolderType getModellingRulesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends FolderType> getModellingRulesNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  FolderType getAggregateFunctionsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends FolderType> getAggregateFunctionsNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable RoleSetType getRoleSetNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable RoleSetType> getRoleSetNodeAsync();
}
