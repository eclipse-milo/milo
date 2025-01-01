/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.2">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.2</a>
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

  /**
   * Get the local value of the ServerProfileArray Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ServerProfileArray Node.
   * @throws UaException if an error occurs creating or getting the ServerProfileArray Node.
   */
  String[] getServerProfileArray() throws UaException;

  /**
   * Set the local value of the ServerProfileArray Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ServerProfileArray Node.
   * @throws UaException if an error occurs creating or getting the ServerProfileArray Node.
   */
  void setServerProfileArray(String[] value) throws UaException;

  /**
   * Read the value of the ServerProfileArray Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String[] readServerProfileArray() throws UaException;

  /**
   * Write a new value for the ServerProfileArray Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link String[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeServerProfileArray(String[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readServerProfileArray}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String[]> readServerProfileArrayAsync();

  /**
   * An asynchronous implementation of {@link #writeServerProfileArray}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeServerProfileArrayAsync(String[] value);

  /**
   * Get the ServerProfileArray {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ServerProfileArray {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getServerProfileArrayNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getServerProfileArrayNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getServerProfileArrayNodeAsync();

  /**
   * Get the local value of the LocaleIdArray Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LocaleIdArray Node.
   * @throws UaException if an error occurs creating or getting the LocaleIdArray Node.
   */
  String[] getLocaleIdArray() throws UaException;

  /**
   * Set the local value of the LocaleIdArray Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LocaleIdArray Node.
   * @throws UaException if an error occurs creating or getting the LocaleIdArray Node.
   */
  void setLocaleIdArray(String[] value) throws UaException;

  /**
   * Read the value of the LocaleIdArray Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String[] readLocaleIdArray() throws UaException;

  /**
   * Write a new value for the LocaleIdArray Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLocaleIdArray(String[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLocaleIdArray}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String[]> readLocaleIdArrayAsync();

  /**
   * An asynchronous implementation of {@link #writeLocaleIdArray}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLocaleIdArrayAsync(String[] value);

  /**
   * Get the LocaleIdArray {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LocaleIdArray {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getLocaleIdArrayNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLocaleIdArrayNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getLocaleIdArrayNodeAsync();

  /**
   * Get the local value of the MinSupportedSampleRate Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MinSupportedSampleRate Node.
   * @throws UaException if an error occurs creating or getting the MinSupportedSampleRate Node.
   */
  Double getMinSupportedSampleRate() throws UaException;

  /**
   * Set the local value of the MinSupportedSampleRate Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MinSupportedSampleRate Node.
   * @throws UaException if an error occurs creating or getting the MinSupportedSampleRate Node.
   */
  void setMinSupportedSampleRate(Double value) throws UaException;

  /**
   * Read the value of the MinSupportedSampleRate Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readMinSupportedSampleRate() throws UaException;

  /**
   * Write a new value for the MinSupportedSampleRate Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMinSupportedSampleRate(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMinSupportedSampleRate}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readMinSupportedSampleRateAsync();

  /**
   * An asynchronous implementation of {@link #writeMinSupportedSampleRate}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMinSupportedSampleRateAsync(Double value);

  /**
   * Get the MinSupportedSampleRate {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MinSupportedSampleRate {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMinSupportedSampleRateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMinSupportedSampleRateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMinSupportedSampleRateNodeAsync();

  /**
   * Get the local value of the MaxBrowseContinuationPoints Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxBrowseContinuationPoints Node.
   * @throws UaException if an error occurs creating or getting the MaxBrowseContinuationPoints
   *     Node.
   */
  UShort getMaxBrowseContinuationPoints() throws UaException;

  /**
   * Set the local value of the MaxBrowseContinuationPoints Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxBrowseContinuationPoints Node.
   * @throws UaException if an error occurs creating or getting the MaxBrowseContinuationPoints
   *     Node.
   */
  void setMaxBrowseContinuationPoints(UShort value) throws UaException;

  /**
   * Read the value of the MaxBrowseContinuationPoints Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readMaxBrowseContinuationPoints() throws UaException;

  /**
   * Write a new value for the MaxBrowseContinuationPoints Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxBrowseContinuationPoints(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxBrowseContinuationPoints}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readMaxBrowseContinuationPointsAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxBrowseContinuationPoints}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxBrowseContinuationPointsAsync(UShort value);

  /**
   * Get the MaxBrowseContinuationPoints {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxBrowseContinuationPoints {@link PropertyType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxBrowseContinuationPointsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxBrowseContinuationPointsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxBrowseContinuationPointsNodeAsync();

  /**
   * Get the local value of the MaxQueryContinuationPoints Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxQueryContinuationPoints Node.
   * @throws UaException if an error occurs creating or getting the MaxQueryContinuationPoints Node.
   */
  UShort getMaxQueryContinuationPoints() throws UaException;

  /**
   * Set the local value of the MaxQueryContinuationPoints Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxQueryContinuationPoints Node.
   * @throws UaException if an error occurs creating or getting the MaxQueryContinuationPoints Node.
   */
  void setMaxQueryContinuationPoints(UShort value) throws UaException;

  /**
   * Read the value of the MaxQueryContinuationPoints Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readMaxQueryContinuationPoints() throws UaException;

  /**
   * Write a new value for the MaxQueryContinuationPoints Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxQueryContinuationPoints(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxQueryContinuationPoints}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readMaxQueryContinuationPointsAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxQueryContinuationPoints}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxQueryContinuationPointsAsync(UShort value);

  /**
   * Get the MaxQueryContinuationPoints {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxQueryContinuationPoints {@link PropertyType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxQueryContinuationPointsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxQueryContinuationPointsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxQueryContinuationPointsNodeAsync();

  /**
   * Get the local value of the MaxHistoryContinuationPoints Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxHistoryContinuationPoints Node.
   * @throws UaException if an error occurs creating or getting the MaxHistoryContinuationPoints
   *     Node.
   */
  UShort getMaxHistoryContinuationPoints() throws UaException;

  /**
   * Set the local value of the MaxHistoryContinuationPoints Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxHistoryContinuationPoints Node.
   * @throws UaException if an error occurs creating or getting the MaxHistoryContinuationPoints
   *     Node.
   */
  void setMaxHistoryContinuationPoints(UShort value) throws UaException;

  /**
   * Read the value of the MaxHistoryContinuationPoints Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readMaxHistoryContinuationPoints() throws UaException;

  /**
   * Write a new value for the MaxHistoryContinuationPoints Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxHistoryContinuationPoints(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxHistoryContinuationPoints}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readMaxHistoryContinuationPointsAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxHistoryContinuationPoints}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxHistoryContinuationPointsAsync(UShort value);

  /**
   * Get the MaxHistoryContinuationPoints {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxHistoryContinuationPoints {@link PropertyType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxHistoryContinuationPointsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxHistoryContinuationPointsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxHistoryContinuationPointsNodeAsync();

  /**
   * Get the local value of the SoftwareCertificates Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SoftwareCertificates Node.
   * @throws UaException if an error occurs creating or getting the SoftwareCertificates Node.
   */
  SignedSoftwareCertificate[] getSoftwareCertificates() throws UaException;

  /**
   * Set the local value of the SoftwareCertificates Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SoftwareCertificates Node.
   * @throws UaException if an error occurs creating or getting the SoftwareCertificates Node.
   */
  void setSoftwareCertificates(SignedSoftwareCertificate[] value) throws UaException;

  /**
   * Read the value of the SoftwareCertificates Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link SignedSoftwareCertificate[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  SignedSoftwareCertificate[] readSoftwareCertificates() throws UaException;

  /**
   * Write a new value for the SoftwareCertificates Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link SignedSoftwareCertificate[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSoftwareCertificates(SignedSoftwareCertificate[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSoftwareCertificates}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends SignedSoftwareCertificate[]> readSoftwareCertificatesAsync();

  /**
   * An asynchronous implementation of {@link #writeSoftwareCertificates}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSoftwareCertificatesAsync(SignedSoftwareCertificate[] value);

  /**
   * Get the SoftwareCertificates {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SoftwareCertificates {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSoftwareCertificatesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSoftwareCertificatesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSoftwareCertificatesNodeAsync();

  /**
   * Get the local value of the MaxArrayLength Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxArrayLength Node.
   * @throws UaException if an error occurs creating or getting the MaxArrayLength Node.
   */
  UInteger getMaxArrayLength() throws UaException;

  /**
   * Set the local value of the MaxArrayLength Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxArrayLength Node.
   * @throws UaException if an error occurs creating or getting the MaxArrayLength Node.
   */
  void setMaxArrayLength(UInteger value) throws UaException;

  /**
   * Read the value of the MaxArrayLength Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxArrayLength() throws UaException;

  /**
   * Write a new value for the MaxArrayLength Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxArrayLength(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxArrayLength}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxArrayLengthAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxArrayLength}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxArrayLengthAsync(UInteger value);

  /**
   * Get the MaxArrayLength {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxArrayLength {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxArrayLengthNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxArrayLengthNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxArrayLengthNodeAsync();

  /**
   * Get the local value of the MaxStringLength Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxStringLength Node.
   * @throws UaException if an error occurs creating or getting the MaxStringLength Node.
   */
  UInteger getMaxStringLength() throws UaException;

  /**
   * Set the local value of the MaxStringLength Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxStringLength Node.
   * @throws UaException if an error occurs creating or getting the MaxStringLength Node.
   */
  void setMaxStringLength(UInteger value) throws UaException;

  /**
   * Read the value of the MaxStringLength Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxStringLength() throws UaException;

  /**
   * Write a new value for the MaxStringLength Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxStringLength(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxStringLength}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxStringLengthAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxStringLength}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxStringLengthAsync(UInteger value);

  /**
   * Get the MaxStringLength {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxStringLength {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxStringLengthNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxStringLengthNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxStringLengthNodeAsync();

  /**
   * Get the local value of the MaxByteStringLength Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxByteStringLength Node.
   * @throws UaException if an error occurs creating or getting the MaxByteStringLength Node.
   */
  UInteger getMaxByteStringLength() throws UaException;

  /**
   * Set the local value of the MaxByteStringLength Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxByteStringLength Node.
   * @throws UaException if an error occurs creating or getting the MaxByteStringLength Node.
   */
  void setMaxByteStringLength(UInteger value) throws UaException;

  /**
   * Read the value of the MaxByteStringLength Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxByteStringLength() throws UaException;

  /**
   * Write a new value for the MaxByteStringLength Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxByteStringLength(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxByteStringLength}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxByteStringLengthAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxByteStringLength}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxByteStringLengthAsync(UInteger value);

  /**
   * Get the MaxByteStringLength {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxByteStringLength {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxByteStringLengthNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxByteStringLengthNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxByteStringLengthNodeAsync();

  /**
   * Get the local value of the MaxSessions Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxSessions Node.
   * @throws UaException if an error occurs creating or getting the MaxSessions Node.
   */
  UInteger getMaxSessions() throws UaException;

  /**
   * Set the local value of the MaxSessions Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxSessions Node.
   * @throws UaException if an error occurs creating or getting the MaxSessions Node.
   */
  void setMaxSessions(UInteger value) throws UaException;

  /**
   * Read the value of the MaxSessions Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxSessions() throws UaException;

  /**
   * Write a new value for the MaxSessions Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxSessions(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxSessions}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxSessionsAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxSessions}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxSessionsAsync(UInteger value);

  /**
   * Get the MaxSessions {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxSessions {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxSessionsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxSessionsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxSessionsNodeAsync();

  /**
   * Get the local value of the MaxSubscriptions Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxSubscriptions Node.
   * @throws UaException if an error occurs creating or getting the MaxSubscriptions Node.
   */
  UInteger getMaxSubscriptions() throws UaException;

  /**
   * Set the local value of the MaxSubscriptions Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxSubscriptions Node.
   * @throws UaException if an error occurs creating or getting the MaxSubscriptions Node.
   */
  void setMaxSubscriptions(UInteger value) throws UaException;

  /**
   * Read the value of the MaxSubscriptions Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxSubscriptions() throws UaException;

  /**
   * Write a new value for the MaxSubscriptions Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxSubscriptions(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxSubscriptions}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxSubscriptionsAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxSubscriptions}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxSubscriptionsAsync(UInteger value);

  /**
   * Get the MaxSubscriptions {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxSubscriptions {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxSubscriptionsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxSubscriptionsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxSubscriptionsNodeAsync();

  /**
   * Get the local value of the MaxMonitoredItems Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxMonitoredItems Node.
   * @throws UaException if an error occurs creating or getting the MaxMonitoredItems Node.
   */
  UInteger getMaxMonitoredItems() throws UaException;

  /**
   * Set the local value of the MaxMonitoredItems Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxMonitoredItems Node.
   * @throws UaException if an error occurs creating or getting the MaxMonitoredItems Node.
   */
  void setMaxMonitoredItems(UInteger value) throws UaException;

  /**
   * Read the value of the MaxMonitoredItems Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxMonitoredItems() throws UaException;

  /**
   * Write a new value for the MaxMonitoredItems Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxMonitoredItems(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxMonitoredItems}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxMonitoredItemsAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxMonitoredItems}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxMonitoredItemsAsync(UInteger value);

  /**
   * Get the MaxMonitoredItems {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxMonitoredItems {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxMonitoredItemsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxMonitoredItemsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxMonitoredItemsNodeAsync();

  /**
   * Get the local value of the MaxSubscriptionsPerSession Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxSubscriptionsPerSession Node.
   * @throws UaException if an error occurs creating or getting the MaxSubscriptionsPerSession Node.
   */
  UInteger getMaxSubscriptionsPerSession() throws UaException;

  /**
   * Set the local value of the MaxSubscriptionsPerSession Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxSubscriptionsPerSession Node.
   * @throws UaException if an error occurs creating or getting the MaxSubscriptionsPerSession Node.
   */
  void setMaxSubscriptionsPerSession(UInteger value) throws UaException;

  /**
   * Read the value of the MaxSubscriptionsPerSession Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxSubscriptionsPerSession() throws UaException;

  /**
   * Write a new value for the MaxSubscriptionsPerSession Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxSubscriptionsPerSession(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxSubscriptionsPerSession}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxSubscriptionsPerSessionAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxSubscriptionsPerSession}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxSubscriptionsPerSessionAsync(UInteger value);

  /**
   * Get the MaxSubscriptionsPerSession {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxSubscriptionsPerSession {@link PropertyType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxSubscriptionsPerSessionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxSubscriptionsPerSessionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxSubscriptionsPerSessionNodeAsync();

  /**
   * Get the local value of the MaxMonitoredItemsPerSubscription Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxMonitoredItemsPerSubscription Node.
   * @throws UaException if an error occurs creating or getting the MaxMonitoredItemsPerSubscription
   *     Node.
   */
  UInteger getMaxMonitoredItemsPerSubscription() throws UaException;

  /**
   * Set the local value of the MaxMonitoredItemsPerSubscription Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxMonitoredItemsPerSubscription Node.
   * @throws UaException if an error occurs creating or getting the MaxMonitoredItemsPerSubscription
   *     Node.
   */
  void setMaxMonitoredItemsPerSubscription(UInteger value) throws UaException;

  /**
   * Read the value of the MaxMonitoredItemsPerSubscription Node from the server and update the
   * local value if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxMonitoredItemsPerSubscription() throws UaException;

  /**
   * Write a new value for the MaxMonitoredItemsPerSubscription Node to the server and update the
   * local value if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxMonitoredItemsPerSubscription(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxMonitoredItemsPerSubscription}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxMonitoredItemsPerSubscriptionAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxMonitoredItemsPerSubscription}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxMonitoredItemsPerSubscriptionAsync(UInteger value);

  /**
   * Get the MaxMonitoredItemsPerSubscription {@link PropertyType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxMonitoredItemsPerSubscription {@link PropertyType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxMonitoredItemsPerSubscriptionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxMonitoredItemsPerSubscriptionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxMonitoredItemsPerSubscriptionNodeAsync();

  /**
   * Get the local value of the MaxSelectClauseParameters Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxSelectClauseParameters Node.
   * @throws UaException if an error occurs creating or getting the MaxSelectClauseParameters Node.
   */
  UInteger getMaxSelectClauseParameters() throws UaException;

  /**
   * Set the local value of the MaxSelectClauseParameters Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxSelectClauseParameters Node.
   * @throws UaException if an error occurs creating or getting the MaxSelectClauseParameters Node.
   */
  void setMaxSelectClauseParameters(UInteger value) throws UaException;

  /**
   * Read the value of the MaxSelectClauseParameters Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxSelectClauseParameters() throws UaException;

  /**
   * Write a new value for the MaxSelectClauseParameters Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxSelectClauseParameters(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxSelectClauseParameters}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxSelectClauseParametersAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxSelectClauseParameters}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxSelectClauseParametersAsync(UInteger value);

  /**
   * Get the MaxSelectClauseParameters {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxSelectClauseParameters {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxSelectClauseParametersNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxSelectClauseParametersNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxSelectClauseParametersNodeAsync();

  /**
   * Get the local value of the MaxWhereClauseParameters Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxWhereClauseParameters Node.
   * @throws UaException if an error occurs creating or getting the MaxWhereClauseParameters Node.
   */
  UInteger getMaxWhereClauseParameters() throws UaException;

  /**
   * Set the local value of the MaxWhereClauseParameters Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxWhereClauseParameters Node.
   * @throws UaException if an error occurs creating or getting the MaxWhereClauseParameters Node.
   */
  void setMaxWhereClauseParameters(UInteger value) throws UaException;

  /**
   * Read the value of the MaxWhereClauseParameters Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxWhereClauseParameters() throws UaException;

  /**
   * Write a new value for the MaxWhereClauseParameters Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxWhereClauseParameters(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxWhereClauseParameters}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxWhereClauseParametersAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxWhereClauseParameters}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxWhereClauseParametersAsync(UInteger value);

  /**
   * Get the MaxWhereClauseParameters {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxWhereClauseParameters {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxWhereClauseParametersNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxWhereClauseParametersNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxWhereClauseParametersNodeAsync();

  /**
   * Get the local value of the MaxMonitoredItemsQueueSize Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxMonitoredItemsQueueSize Node.
   * @throws UaException if an error occurs creating or getting the MaxMonitoredItemsQueueSize Node.
   */
  UInteger getMaxMonitoredItemsQueueSize() throws UaException;

  /**
   * Set the local value of the MaxMonitoredItemsQueueSize Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxMonitoredItemsQueueSize Node.
   * @throws UaException if an error occurs creating or getting the MaxMonitoredItemsQueueSize Node.
   */
  void setMaxMonitoredItemsQueueSize(UInteger value) throws UaException;

  /**
   * Read the value of the MaxMonitoredItemsQueueSize Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxMonitoredItemsQueueSize() throws UaException;

  /**
   * Write a new value for the MaxMonitoredItemsQueueSize Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxMonitoredItemsQueueSize(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxMonitoredItemsQueueSize}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxMonitoredItemsQueueSizeAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxMonitoredItemsQueueSize}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxMonitoredItemsQueueSizeAsync(UInteger value);

  /**
   * Get the MaxMonitoredItemsQueueSize {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxMonitoredItemsQueueSize {@link PropertyType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxMonitoredItemsQueueSizeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxMonitoredItemsQueueSizeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxMonitoredItemsQueueSizeNodeAsync();

  /**
   * Get the local value of the ConformanceUnits Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ConformanceUnits Node.
   * @throws UaException if an error occurs creating or getting the ConformanceUnits Node.
   */
  QualifiedName[] getConformanceUnits() throws UaException;

  /**
   * Set the local value of the ConformanceUnits Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ConformanceUnits Node.
   * @throws UaException if an error occurs creating or getting the ConformanceUnits Node.
   */
  void setConformanceUnits(QualifiedName[] value) throws UaException;

  /**
   * Read the value of the ConformanceUnits Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link QualifiedName[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  QualifiedName[] readConformanceUnits() throws UaException;

  /**
   * Write a new value for the ConformanceUnits Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link QualifiedName[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeConformanceUnits(QualifiedName[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readConformanceUnits}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends QualifiedName[]> readConformanceUnitsAsync();

  /**
   * An asynchronous implementation of {@link #writeConformanceUnits}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeConformanceUnitsAsync(QualifiedName[] value);

  /**
   * Get the ConformanceUnits {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ConformanceUnits {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getConformanceUnitsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getConformanceUnitsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getConformanceUnitsNodeAsync();

  /**
   * Get the OperationLimits {@link OperationLimitsType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the OperationLimits {@link OperationLimitsType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  OperationLimitsType getOperationLimitsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getOperationLimitsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the OperationLimitsType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends OperationLimitsType> getOperationLimitsNodeAsync();

  /**
   * Get the ModellingRules {@link FolderType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ModellingRules {@link FolderType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  FolderType getModellingRulesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getModellingRulesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the FolderType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends FolderType> getModellingRulesNodeAsync();

  /**
   * Get the AggregateFunctions {@link FolderType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AggregateFunctions {@link FolderType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  FolderType getAggregateFunctionsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAggregateFunctionsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the FolderType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends FolderType> getAggregateFunctionsNodeAsync();

  /**
   * Get the RoleSet {@link RoleSetType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RoleSet {@link RoleSetType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  RoleSetType getRoleSetNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRoleSetNode()}.
   *
   * @return a CompletableFuture that completes successfully with the RoleSetType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends RoleSetType> getRoleSetNodeAsync();
}
