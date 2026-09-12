/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
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
  @Nullable String @Nullable [] getServerProfileArray();

  /** Sets the existing node's local value. */
  void setServerProfileArray(@Nullable String @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getServerProfileArrayNode();

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getLocaleIdArray();

  /** Sets the existing node's local value. */
  void setLocaleIdArray(@Nullable String @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLocaleIdArrayNode();

  /** Gets the existing node's local value. */
  @Nullable Double getMinSupportedSampleRate();

  /** Sets the existing node's local value. */
  void setMinSupportedSampleRate(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMinSupportedSampleRateNode();

  /** Gets the existing node's local value. */
  @Nullable UShort getMaxBrowseContinuationPoints();

  /** Sets the existing node's local value. */
  void setMaxBrowseContinuationPoints(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxBrowseContinuationPointsNode();

  /** Gets the existing node's local value. */
  @Nullable UShort getMaxQueryContinuationPoints();

  /** Sets the existing node's local value. */
  void setMaxQueryContinuationPoints(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxQueryContinuationPointsNode();

  /** Gets the existing node's local value. */
  @Nullable UShort getMaxHistoryContinuationPoints();

  /** Sets the existing node's local value. */
  void setMaxHistoryContinuationPoints(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxHistoryContinuationPointsNode();

  /** Gets the existing node's local value. */
  @Nullable UShort getMaxLogObjectContinuationPoints();

  /** Sets the existing node's local value. */
  void setMaxLogObjectContinuationPoints(@Nullable UShort value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxLogObjectContinuationPointsNode();

  /** Gets the existing node's local value. */
  @Nullable SignedSoftwareCertificate @Nullable [] getSoftwareCertificates();

  /** Sets the existing node's local value. */
  void setSoftwareCertificates(@Nullable SignedSoftwareCertificate @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSoftwareCertificatesNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxArrayLength();

  /** Sets the existing node's local value. */
  void setMaxArrayLength(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxArrayLengthNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxStringLength();

  /** Sets the existing node's local value. */
  void setMaxStringLength(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxStringLengthNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxByteStringLength();

  /** Sets the existing node's local value. */
  void setMaxByteStringLength(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxByteStringLengthNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxSessions();

  /** Sets the existing node's local value. */
  void setMaxSessions(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxSessionsNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxSubscriptions();

  /** Sets the existing node's local value. */
  void setMaxSubscriptions(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxSubscriptionsNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxMonitoredItems();

  /** Sets the existing node's local value. */
  void setMaxMonitoredItems(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxMonitoredItemsNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxSubscriptionsPerSession();

  /** Sets the existing node's local value. */
  void setMaxSubscriptionsPerSession(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxSubscriptionsPerSessionNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxMonitoredItemsPerSubscription();

  /** Sets the existing node's local value. */
  void setMaxMonitoredItemsPerSubscription(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxMonitoredItemsPerSubscriptionNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxSelectClauseParameters();

  /** Sets the existing node's local value. */
  void setMaxSelectClauseParameters(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxSelectClauseParametersNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxWhereClauseParameters();

  /** Sets the existing node's local value. */
  void setMaxWhereClauseParameters(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxWhereClauseParametersNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxMonitoredItemsQueueSize();

  /** Sets the existing node's local value. */
  void setMaxMonitoredItemsQueueSize(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxMonitoredItemsQueueSizeNode();

  /** Gets the existing node's local value. */
  @Nullable QualifiedName @Nullable [] getConformanceUnits();

  /** Sets the existing node's local value. */
  void setConformanceUnits(@Nullable QualifiedName @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getConformanceUnitsNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable OperationLimitsType getOperationLimitsNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  FolderType getModellingRulesNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  FolderType getAggregateFunctionsNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable RoleSetType getRoleSetNode();
}
