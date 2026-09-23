/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.jspecify.annotations.Nullable;

/**
 * Operation limits of the Server, obtained by reading Variables of the OperationLimits Object.
 *
 * <p>The limits returned by {@link OpcUaClient#getOperationLimits()} are the effective limits: the
 * values the Server advertised combined with any overrides configured through {@link
 * OpcUaClientConfig#getOperationLimitOverrides()}.
 *
 * @see <a href="https://reference.opcfoundation.org/Core/Part5/v105/docs/6.3.11">
 *     https://reference.opcfoundation.org/Core/Part5/v105/docs/6.3.11</a>
 */
public class OperationLimits {

  private static final List<OperationLimit> LIMITS = List.of(OperationLimit.values());

  private static final List<NodeId> LIMIT_NODE_IDS =
      LIMITS.stream().map(OperationLimit::getNodeId).toList();

  private final Map<OperationLimit, UInteger> limits;

  /**
   * Create an {@link OperationLimits} from a map of known limit values.
   *
   * <p>A limit missing from {@code limits} is reported as absent.
   *
   * @param limits the known limit values.
   */
  public OperationLimits(Map<OperationLimit, UInteger> limits) {
    this.limits = new EnumMap<>(OperationLimit.class);
    this.limits.putAll(limits);
  }

  /**
   * Create an {@link OperationLimits} from individual limit values.
   *
   * <p>A {@code null} argument means the limit is absent.
   */
  public OperationLimits(
      @Nullable UInteger maxNodesPerRead,
      @Nullable UInteger maxNodesPerWrite,
      @Nullable UInteger maxNodesPerMethodCall,
      @Nullable UInteger maxNodesPerBrowse,
      @Nullable UInteger maxNodesPerRegisterNodes,
      @Nullable UInteger maxNodesPerTranslateBrowsePathsToNodeIds,
      @Nullable UInteger maxNodesPerNodeManagement,
      @Nullable UInteger maxMonitoredItemsPerCall,
      @Nullable UInteger maxNodesPerHistoryReadData,
      @Nullable UInteger maxNodesPerHistoryReadEvents,
      @Nullable UInteger maxNodesPerHistoryUpdateData,
      @Nullable UInteger maxNodesPerHistoryUpdateEvents) {

    this(
        toMap(
            Arrays.asList(
                maxNodesPerRead,
                maxNodesPerWrite,
                maxNodesPerMethodCall,
                maxNodesPerBrowse,
                maxNodesPerRegisterNodes,
                maxNodesPerTranslateBrowsePathsToNodeIds,
                maxNodesPerNodeManagement,
                maxMonitoredItemsPerCall,
                maxNodesPerHistoryReadData,
                maxNodesPerHistoryReadEvents,
                maxNodesPerHistoryUpdateData,
                maxNodesPerHistoryUpdateEvents)));
  }

  /**
   * Get the value of {@code limit}.
   *
   * @param limit the limit to get.
   * @return the value of {@code limit}, or empty if the limit is absent.
   */
  public Optional<UInteger> get(OperationLimit limit) {
    return Optional.ofNullable(limits.get(limit));
  }

  /**
   * @return the maximum size of the nodesToRead array when a Client calls the Read Service.
   */
  public Optional<UInteger> maxNodesPerRead() {
    return get(OperationLimit.MaxNodesPerRead);
  }

  /**
   * @return the maximum size of the nodesToWrite array when a Client calls the Write Service.
   */
  public Optional<UInteger> maxNodesPerWrite() {
    return get(OperationLimit.MaxNodesPerWrite);
  }

  /**
   * @return the maximum size of the methodsToCall array when a Client calls the Call Service.
   */
  public Optional<UInteger> maxNodesPerMethodCall() {
    return get(OperationLimit.MaxNodesPerMethodCall);
  }

  /**
   * @return the maximum size of the nodesToBrowse array when a Client calls the Browse Service, or
   *     the continuationPoints array when a Client calls the BrowseNext Service.
   */
  public Optional<UInteger> maxNodesPerBrowse() {
    return get(OperationLimit.MaxNodesPerBrowse);
  }

  /**
   * @return the maximum size of the nodesToRegister array when a Client calls the RegisterNodes
   *     Service and the maximum size of the nodesToUnregister array when a Client calls the
   *     UnregisterNodes Service.
   */
  public Optional<UInteger> maxNodesPerRegisterNodes() {
    return get(OperationLimit.MaxNodesPerRegisterNodes);
  }

  /**
   * @return the maximum size of the browsePaths array when a Client calls the
   *     TranslateBrowsePathsToNodeIds Service.
   */
  public Optional<UInteger> maxNodesPerTranslateBrowsePathsToNodeIds() {
    return get(OperationLimit.MaxNodesPerTranslateBrowsePathsToNodeIds);
  }

  /**
   * @return the maximum size of the nodesToAdd, referencesToAdd, nodesToDelete, and
   *     referencesToDelete arrays when a Client calls the AddNodes, AddReferences, DeleteNodes, and
   *     DeleteReferences Services.
   */
  public Optional<UInteger> maxNodesPerNodeManagement() {
    return get(OperationLimit.MaxNodesPerNodeManagement);
  }

  /**
   * @return the maximum size of the array when a Client calls the CreateMonitoredItems,
   *     ModifyMonitoredItems, DeleteMonitoredItems, SetMonitoringMode, and SetTriggering services.
   */
  public Optional<UInteger> maxMonitoredItemsPerCall() {
    return get(OperationLimit.MaxMonitoredItemsPerCall);
  }

  /**
   * @return the maximum size of the nodesToRead array when a Client calls the HistoryRead Service
   *     for data.
   */
  public Optional<UInteger> maxNodesPerHistoryReadData() {
    return get(OperationLimit.MaxNodesPerHistoryReadData);
  }

  /**
   * @return the maximum size of the nodesToRead array when a Client calls the HistoryRead Service
   *     for events.
   */
  public Optional<UInteger> maxNodesPerHistoryReadEvents() {
    return get(OperationLimit.MaxNodesPerHistoryReadEvents);
  }

  /**
   * @return the maximum size of the historyUpdateDetails array when a Client calls the
   *     HistoryUpdate Service for data.
   */
  public Optional<UInteger> maxNodesPerHistoryUpdateData() {
    return get(OperationLimit.MaxNodesPerHistoryUpdateData);
  }

  /**
   * @return the maximum size of the historyUpdateDetails array when a Client calls the
   *     HistoryUpdate Service for events.
   */
  public Optional<UInteger> maxNodesPerHistoryUpdateEvents() {
    return get(OperationLimit.MaxNodesPerHistoryUpdateEvents);
  }

  static OperationLimits read(OpcUaClient client) throws UaException {
    try {
      return readAllNodes(client);
    } catch (UaException e) {
      if (e.getStatusCode().value() == StatusCodes.Bad_TooManyOperations) {
        // Whelp, the read operation limit is so low we can't read all the nodes at once.
        // Read them individually instead.
        return readIndividualNodes(client);
      } else {
        throw e;
      }
    }
  }

  private static OperationLimits readAllNodes(OpcUaClient client) throws UaException {
    List<DataValue> values = client.readValues(0.0, TimestampsToReturn.Neither, LIMIT_NODE_IDS);

    if (values == null || values.size() != LIMIT_NODE_IDS.size()) {
      throw new UaException(
          StatusCodes.Bad_UnexpectedError,
          "Read returned %s OperationLimits results, expected %s"
              .formatted(values == null ? "null" : values.size(), LIMIT_NODE_IDS.size()));
    }

    return new OperationLimits(toMap(values.stream().map(OperationLimits::toUInteger).toList()));
  }

  private static OperationLimits readIndividualNodes(OpcUaClient client) throws UaException {
    var values = new ArrayList<@Nullable UInteger>(LIMITS.size());

    for (OperationLimit limit : LIMITS) {
      values.add(readNode(client, limit.getNodeId()));
    }

    return new OperationLimits(toMap(values));
  }

  /**
   * Combine these limits, as advertised by a Server, with overrides.
   *
   * <p>For each limit, an empty override or an override of 0 leaves the advertised value alone. An
   * override replaces a limit that is absent or advertised as 0 ("no limit"). When both are
   * non-zero, the smaller value wins.
   *
   * <p>{@link OpcUaClient#getOperationLimits()} already applies the overrides from {@link
   * OpcUaClientConfig#getOperationLimitOverrides()}. Use this method for limits obtained some other
   * way.
   *
   * @param overrides the override for each limit, or empty to leave the advertised value alone.
   * @return the effective limits.
   */
  public OperationLimits withOverrides(Function<OperationLimit, Optional<UInteger>> overrides) {
    var effective = new EnumMap<OperationLimit, UInteger>(OperationLimit.class);

    for (OperationLimit limit : LIMITS) {
      UInteger value = resolve(limits.get(limit), overrides.apply(limit).orElse(null));

      if (value != null) {
        effective.put(limit, value);
      }
    }

    return new OperationLimits(effective);
  }

  /**
   * Resolve the effective value of one limit from the value the Server advertised and a configured
   * override.
   *
   * <p>Part 5 defines 0 as "no limit", so an advertised 0 does not constrain the override, and an
   * override of 0 leaves the advertised value alone. When both are non-zero, the smaller wins.
   *
   * @param advertised the value the Server advertised, or {@code null} if absent.
   * @param override the configured override, or {@code null} if not configured.
   * @return the effective value, or {@code null} if absent.
   */
  static @Nullable UInteger resolve(@Nullable UInteger advertised, @Nullable UInteger override) {
    if (override == null || override.longValue() == 0L) {
      return advertised;
    }
    if (advertised == null || advertised.longValue() == 0L) {
      return override;
    }
    return advertised.compareTo(override) <= 0 ? advertised : override;
  }

  /**
   * Map values listed in {@link OperationLimit} declaration order to their limits, omitting {@code
   * null} values.
   */
  private static Map<OperationLimit, UInteger> toMap(List<@Nullable UInteger> values) {
    var map = new EnumMap<OperationLimit, UInteger>(OperationLimit.class);

    for (int i = 0; i < LIMITS.size(); i++) {
      UInteger value = values.get(i);
      if (value != null) {
        map.put(LIMITS.get(i), value);
      }
    }

    return map;
  }

  private static @Nullable UInteger readNode(OpcUaClient client, NodeId nodeId) throws UaException {
    DataValue value = client.readValue(0.0, TimestampsToReturn.Neither, nodeId);

    return toUInteger(value);
  }

  private static @Nullable UInteger toUInteger(@Nullable DataValue value) {
    if (value == null || value.statusCode().isBad()) {
      return null;
    }

    return toUInteger(value.value().value());
  }

  /**
   * Converts a value to a {@link UInteger}, handling various numeric types that a server might
   * return.
   *
   * @param value the value to convert.
   * @return the value as a {@link UInteger}, or {@code null} if the value is {@code null} or cannot
   *     be converted.
   */
  private static @Nullable UInteger toUInteger(@Nullable Object value) {
    if (value == null) {
      return null;
    }

    if (value instanceof UInteger uInteger) {
      return uInteger;
    }

    if (value instanceof Number number) {
      return UInteger.valueOf(number.longValue());
    }

    return null;
  }
}
