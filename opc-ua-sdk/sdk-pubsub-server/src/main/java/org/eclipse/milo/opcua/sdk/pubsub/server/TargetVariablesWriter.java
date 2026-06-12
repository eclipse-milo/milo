/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.core.NumericRange;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetFieldValue;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetListener;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldIdSelector;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldIndexSelector;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldNameSelector;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldSelector;
import org.eclipse.milo.opcua.sdk.pubsub.config.NodeFieldAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigValidationException;
import org.eclipse.milo.opcua.sdk.pubsub.config.TargetVariableConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.TargetVariablesConfig;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.WriteContext;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.OverrideValueHandling;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link DataSetListener} that writes the fields received by one DataSetReader to the
 * address-space variables mapped by its {@link TargetVariablesConfig}, implementing the Part 14
 * §6.2.11.1 Table 80 message-to-target mapping.
 *
 * <p>Message-driven behavior, per received field with a matching mapping: Good or Uncertain fields
 * are written through with the received value (after the configured receiver index range is
 * applied), status, and timestamps; Bad fields are handled per the mapping's {@link
 * OverrideValueHandling}:
 *
 * <ul>
 *   <li>{@code OverrideValue}: the configured override value with {@code Good_LocalOverride}.
 *   <li>{@code LastUsableValue}: the last received usable value with {@code
 *       Uncertain_LastUsableValue}.
 *   <li>{@code Disabled}: a null value with the received Bad status.
 * </ul>
 *
 * <p>State-driven behavior, once per reader state change into Disabled, Paused, or Error (see
 * {@link #onStateChange}): {@code OverrideValue} and {@code LastUsableValue} mappings are written
 * per the rows above; {@code Disabled} mappings get a null value with {@code Bad_OutOfService}
 * (Disabled, Paused) or {@code Bad_NoCommunication} (Error). Transitions into PreOperational or
 * Operational have no Table 80 row and write nothing.
 *
 * <p>The DataSet dispatch queue and the state-event queue are not ordered relative to each other,
 * so a message that passed the engine's receiving-state check just before the reader was disabled
 * could otherwise be written after the state-change row. To keep the state-change write the final
 * word, DataSet events are dropped while the last observed reader state is Disabled or Paused (no
 * later delivery would correct the target); Error-state arrivals are still written, since received
 * data recovers an Error reader.
 *
 * <p>Deviation from Table 80 footnote (b), documented here per the pinned Phase 2 plan: when no
 * usable value was ever received, {@code LastUsableValue} writes a null {@link Variant} with {@code
 * Uncertain_LastUsableValue} instead of synthesizing a DataType default value.
 *
 * <p>Writes go through the server's internal write path, which enforces {@code
 * AccessLevel.CurrentWrite} even for internal operations: target variables must be writable. Write
 * failures are logged at WARN and counted per target, exposed via {@link
 * ServerPubSub#targetWriteErrors()}.
 */
final class TargetVariablesWriter implements DataSetListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(TargetVariablesWriter.class);

  private final OpcUaServer server;
  private final String readerPath;
  private final List<Target> targets;
  private final ConcurrentMap<String, AtomicLong> errorCounters;

  /**
   * Cleared by {@link #deactivate()} at {@link ServerPubSub} shutdown; checked under the monitor.
   */
  private volatile boolean active = true;

  /**
   * The reader state last observed by {@link #onStateChange}, or {@code null} before the first
   * state change event. Guarded by this writer's monitor.
   */
  private @Nullable PubSubState lastState;

  /**
   * Create a new {@link TargetVariablesWriter}, eagerly resolving every target {@link
   * NodeFieldAddress} against the server's {@link NamespaceTable} and parsing the configured index
   * ranges.
   *
   * @param server the server whose address space is written.
   * @param readerPath the path of the DataSetReader, {@code "connection/group/reader"}.
   * @param config the TargetVariables configuration of the reader.
   * @param errorCounters the shared per-target write error counters, keyed by {@code
   *     "<reader-path>/<targetNodeId>"}.
   * @throws PubSubConfigValidationException if a target address cannot be resolved or an index
   *     range cannot be parsed.
   */
  TargetVariablesWriter(
      OpcUaServer server,
      String readerPath,
      TargetVariablesConfig config,
      ConcurrentMap<String, AtomicLong> errorCounters) {

    this.server = server;
    this.readerPath = readerPath;
    this.errorCounters = errorCounters;

    NamespaceTable namespaceTable = server.getNamespaceTable();

    var targets = new ArrayList<Target>(config.getMappings().size());
    for (TargetVariablesConfig.Mapping mapping : config.getMappings()) {
      targets.add(new Target(mapping, namespaceTable, readerPath));
    }
    this.targets = List.copyOf(targets);
  }

  @Override
  public synchronized void onDataSet(DataSetReceivedEvent event) {
    if (!active) {
      return;
    }
    if (lastState == PubSubState.Disabled || lastState == PubSubState.Paused) {
      // the message passed the engine's receiving-state check before the reader was disabled
      // or paused; writing it now would overwrite the Table 80 state-change row, and no later
      // delivery would correct the target
      return;
    }

    var writeValues = new ArrayList<WriteValue>(targets.size());
    var writeTargets = new ArrayList<Target>(targets.size());

    for (Target target : targets) {
      DataSetFieldValue field = findField(event.fields(), target.selector);
      if (field == null) {
        // a field absent from the message (e.g. delta frame) produces no write
        continue;
      }

      DataValue received = field.value();
      DataValue toWrite;

      if (received.statusCode().isBad()) {
        if (target.overrideHandling == OverrideValueHandling.OverrideValue) {
          toWrite =
              new DataValue(
                  target.overrideValue, new StatusCode(StatusCodes.Good_LocalOverride), null, null);
        } else if (target.overrideHandling == OverrideValueHandling.LastUsableValue) {
          toWrite = lastUsableValue(target);
        } else {
          // Disabled: null value, the received Bad status, the received timestamps
          toWrite = received.copy(builder -> builder.setValue(Variant.NULL_VALUE));
        }
      } else {
        // Good or Uncertain: pass through with received value, status, and timestamps
        if (target.receiverIndexRange != null) {
          try {
            toWrite = slice(received, target.receiverIndexRange);
          } catch (UaException e) {
            recordFailure(target, e.getStatusCode());
            continue;
          }
        } else {
          toWrite = received;
        }
        target.lastUsable = toWrite.value();
      }

      writeValues.add(
          new WriteValue(target.nodeId, target.attributeId, target.writeIndexRange, toWrite));
      writeTargets.add(target);
    }

    write(writeValues, writeTargets);
  }

  /**
   * Apply the Table 80 state-change rows: write each target once for a reader transition into
   * Disabled, Paused, or Error. Transitions into PreOperational or Operational write nothing.
   *
   * @param newState the state the reader transitioned into.
   */
  synchronized void onStateChange(PubSubState newState) {
    if (!active) {
      return;
    }

    lastState = newState;

    StatusCode disabledHandlingStatus;
    if (newState == PubSubState.Disabled || newState == PubSubState.Paused) {
      disabledHandlingStatus = new StatusCode(StatusCodes.Bad_OutOfService);
    } else if (newState == PubSubState.Error) {
      disabledHandlingStatus = new StatusCode(StatusCodes.Bad_NoCommunication);
    } else {
      return;
    }

    var writeValues = new ArrayList<WriteValue>(targets.size());

    for (Target target : targets) {
      DataValue toWrite;
      if (target.overrideHandling == OverrideValueHandling.OverrideValue) {
        toWrite =
            new DataValue(
                target.overrideValue, new StatusCode(StatusCodes.Good_LocalOverride), null, null);
      } else if (target.overrideHandling == OverrideValueHandling.LastUsableValue) {
        toWrite = lastUsableValue(target);
      } else {
        toWrite = new DataValue(Variant.NULL_VALUE, disabledHandlingStatus, null, null);
      }

      writeValues.add(
          new WriteValue(target.nodeId, target.attributeId, target.writeIndexRange, toWrite));
    }

    write(writeValues, targets);
  }

  /**
   * Permanently deactivate this writer: no further writes are issued (and no further error counters
   * recorded) by {@link #onDataSet} or {@link #onStateChange} once this method returns.
   *
   * <p>Synchronized so deactivation joins any write already in progress: when the caller returns,
   * no write is in flight. Called by {@link ServerPubSub#shutdown()} after the service shutdown
   * future (which includes delivery of the shutdown-induced state change events, and so the Table
   * 80 into-Disabled writes) has completed.
   */
  synchronized void deactivate() {
    active = false;
  }

  private void write(List<WriteValue> writeValues, List<Target> writeTargets) {
    if (writeValues.isEmpty()) {
      return;
    }

    var writeContext = new WriteContext(server, null);

    List<StatusCode> results = server.getAddressSpaceManager().write(writeContext, writeValues);

    for (int i = 0; i < results.size() && i < writeTargets.size(); i++) {
      StatusCode result = results.get(i);
      if (result.isBad()) {
        recordFailure(writeTargets.get(i), result);
      }
    }
  }

  private void recordFailure(Target target, StatusCode statusCode) {
    LOGGER.warn(
        "write to target {} failed for reader {}: {}", target.nodeId, readerPath, statusCode);

    errorCounters.computeIfAbsent(target.errorKey, k -> new AtomicLong()).incrementAndGet();
  }

  private static DataValue lastUsableValue(Target target) {
    Variant last = target.lastUsable;

    return new DataValue(
        last != null ? last : Variant.NULL_VALUE,
        new StatusCode(StatusCodes.Uncertain_LastUsableValue),
        null,
        null);
  }

  private static @Nullable DataSetFieldValue findField(
      List<DataSetFieldValue> fields, FieldSelector selector) {

    for (DataSetFieldValue field : fields) {
      if (matches(field, selector)) {
        return field;
      }
    }
    return null;
  }

  private static boolean matches(DataSetFieldValue field, FieldSelector selector) {
    if (selector instanceof FieldIdSelector byId) {
      return byId.fieldId().equals(field.dataSetFieldId());
    } else if (selector instanceof FieldNameSelector byName) {
      return byName.fieldName().equals(field.name());
    } else if (selector instanceof FieldIndexSelector byIndex) {
      return byIndex.index() == field.index();
    } else {
      return false;
    }
  }

  /**
   * Apply {@code range} to the received value, mirroring the unwrap/re-wrap the server applies for
   * write index ranges: {@link Matrix} values are sliced via their nested array form and re-wrapped
   * when the result rank is greater than one.
   */
  private static DataValue slice(DataValue received, NumericRange range) throws UaException {
    Object value = received.value().value();
    if (value instanceof Matrix matrix) {
      value = matrix.nestedArrayValue();
    }

    Object sliced = NumericRange.readFromValueAtRange(value, range);

    if (ArrayUtil.getValueRank(sliced) > 1) {
      sliced = new Matrix(sliced);
    }

    Variant slicedVariant = new Variant(sliced);
    return received.copy(builder -> builder.setValue(slicedVariant));
  }

  /** One resolved TargetVariables mapping. Mutable state is guarded by the writer's monitor. */
  private static final class Target {

    final FieldSelector selector;
    final NodeId nodeId;
    final UInteger attributeId;
    final @Nullable NumericRange receiverIndexRange;
    final @Nullable String writeIndexRange;
    final OverrideValueHandling overrideHandling;
    final Variant overrideValue;
    final String errorKey;

    @Nullable Variant lastUsable;

    Target(TargetVariablesConfig.Mapping mapping, NamespaceTable namespaceTable, String path) {
      TargetVariableConfig config = mapping.target();
      NodeFieldAddress address = config.getTarget();

      this.selector = mapping.selector();
      this.nodeId =
          address
              .nodeId()
              .toNodeId(namespaceTable)
              .orElseThrow(
                  () ->
                      new PubSubConfigValidationException(
                          "DataSetReader '%s': cannot resolve %s against the server NamespaceTable"
                              .formatted(path, address.nodeId())));
      this.attributeId = address.attributeId().uid();
      this.receiverIndexRange =
          config.getReceiverIndexRange().map(range -> parseRange(range, path)).orElse(null);
      this.writeIndexRange = config.getWriteIndexRange().orElse(null);
      if (writeIndexRange != null) {
        parseRange(writeIndexRange, path); // eager syntax validation only
      }
      this.overrideHandling = config.getOverrideHandling();
      this.overrideValue = config.getOverrideValue().orElse(Variant.NULL_VALUE);
      this.errorKey = path + "/" + nodeId.toParseableString();
    }

    private static NumericRange parseRange(String range, String path) {
      try {
        return NumericRange.parse(range);
      } catch (UaException e) {
        throw new PubSubConfigValidationException(
            "DataSetReader '%s': invalid index range '%s'".formatted(path, range), e);
      }
    }
  }
}
