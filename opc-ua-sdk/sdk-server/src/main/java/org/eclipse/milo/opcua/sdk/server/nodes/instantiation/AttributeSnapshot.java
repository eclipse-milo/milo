/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes.instantiation;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * An immutable snapshot of a node's attributes, covering the full OPC UA 1.04+ attribute set and
 * preserving the absent / explicit-null / value distinction.
 *
 * <p>Each attribute is in exactly one of three states:
 *
 * <ul>
 *   <li><b>absent</b> — the attribute is not provided by the node (an optional attribute the node
 *       does not carry): {@link #contains} is {@code false};
 *   <li><b>explicit null</b> — the attribute is provided with a null value: {@link #contains} is
 *       {@code true}, {@link #isNull} is {@code true};
 *   <li><b>value</b> — the attribute is provided with a non-null value.
 * </ul>
 *
 * <p>Snapshots taken from {@link UaNode}s ({@link #of(UaNode)}) map a node's optional-per-spec
 * attributes that are unset ({@code null} fields — e.g. ArrayDimensions, AccessLevelEx,
 * RolePermissions, a VariableType's Value) to <b>absent</b>; a Variable's Value — a mandatory
 * attribute — is always present, explicit null if the node stores none. Variable and VariableType
 * Value snapshots are normalized to carry no server timestamp: {@code UaVariableNode} re-stamps
 * serverTime on every read, so a snapshot retaining it would never be stable across compilations
 * (the source-side constituents — Variant, StatusCode, sourceTime — are preserved verbatim).
 *
 * <p>Mutable attribute values — arrays (e.g. ArrayDimensions, RolePermissions) and array-valued
 * {@link DataValue}s — are defensively copied on ingress and egress, so neither later mutation of
 * the node-owned value nor mutation of a value read from this snapshot can change the snapshot.
 */
public final class AttributeSnapshot {

  private static final Object EXPLICIT_NULL = new Object();

  private final Map<AttributeId, Object> values;

  private AttributeSnapshot(EnumMap<AttributeId, Object> values) {
    this.values = Collections.unmodifiableMap(values);
  }

  /**
   * Take a snapshot of {@code node}'s attributes.
   *
   * @param node the node to snapshot.
   * @return an {@link AttributeSnapshot} of the attributes applicable to the node's NodeClass.
   */
  public static AttributeSnapshot of(UaNode node) {
    Builder b = builder();

    copy(b, node, AttributeId.NodeClass);
    copy(b, node, AttributeId.BrowseName);
    copy(b, node, AttributeId.DisplayName);
    copyIfProvided(b, node, AttributeId.Description);
    copyIfProvided(b, node, AttributeId.WriteMask);
    copyIfProvided(b, node, AttributeId.UserWriteMask);
    copyIfProvided(b, node, AttributeId.RolePermissions);
    copyIfProvided(b, node, AttributeId.UserRolePermissions);
    copyIfProvided(b, node, AttributeId.AccessRestrictions);

    if (node instanceof UaVariableNode) {
      b.put(AttributeId.Value, normalizeValue((DataValue) node.getAttribute(AttributeId.Value)));
      copy(b, node, AttributeId.DataType);
      copy(b, node, AttributeId.ValueRank);
      copyIfProvided(b, node, AttributeId.ArrayDimensions);
      copyIfProvided(b, node, AttributeId.AccessLevel);
      copyIfProvided(b, node, AttributeId.UserAccessLevel);
      copyIfProvided(b, node, AttributeId.MinimumSamplingInterval);
      copyIfProvided(b, node, AttributeId.Historizing);
      copyIfProvided(b, node, AttributeId.AccessLevelEx);
    } else if (node instanceof UaVariableTypeNode) {
      Object value = node.getAttribute(AttributeId.Value);
      if (value != null) {
        b.put(AttributeId.Value, normalizeValue((DataValue) value));
      }
      copy(b, node, AttributeId.DataType);
      copy(b, node, AttributeId.ValueRank);
      copyIfProvided(b, node, AttributeId.ArrayDimensions);
      copy(b, node, AttributeId.IsAbstract);
    } else if (node instanceof UaObjectNode) {
      copyIfProvided(b, node, AttributeId.EventNotifier);
    } else if (node instanceof UaObjectTypeNode) {
      copy(b, node, AttributeId.IsAbstract);
    } else if (node instanceof UaMethodNode) {
      copyIfProvided(b, node, AttributeId.Executable);
      copyIfProvided(b, node, AttributeId.UserExecutable);
    }

    return b.build();
  }

  /** Record {@code node}'s {@code id} attribute as present (explicit-null if the node has none). */
  private static void copy(Builder b, UaNode node, AttributeId id) {
    b.put(id, node.getAttribute(id));
  }

  /** Record {@code node}'s {@code id} attribute if the node provides one; leave it absent else. */
  private static void copyIfProvided(Builder b, UaNode node, AttributeId id) {
    b.putIfProvided(id, node.getAttribute(id));
  }

  private static @Nullable DataValue normalizeValue(@Nullable DataValue value) {
    if (value == null) {
      return null;
    }
    if (value.getServerTime() == null && value.getServerPicoseconds() == null) {
      return value;
    }
    return value.copy(
        b -> {
          b.setServerTime(null);
          b.setServerPicoseconds(null);
        });
  }

  /**
   * @return a new {@link Builder}.
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * @return the ids of every attribute present in this snapshot (with a value or explicit null).
   */
  public Set<AttributeId> attributeIds() {
    return values.keySet();
  }

  /**
   * @param attributeId the attribute to check.
   * @return {@code true} if the attribute is present (with a value or explicit null).
   */
  public boolean contains(AttributeId attributeId) {
    return values.containsKey(attributeId);
  }

  /**
   * @param attributeId the attribute to check.
   * @return {@code true} if the attribute is present with an explicit null value.
   */
  public boolean isNull(AttributeId attributeId) {
    return values.get(attributeId) == EXPLICIT_NULL;
  }

  /**
   * @param attributeId the attribute to read.
   * @return the attribute value; {@code null} if the attribute is absent <em>or</em> explicitly
   *     null (use {@link #contains} / {@link #isNull} to distinguish).
   */
  public @Nullable Object getOrNull(AttributeId attributeId) {
    Object v = values.get(attributeId);
    return v == null || v == EXPLICIT_NULL ? null : defensiveCopy(v);
  }

  /**
   * @param attributeId the attribute to read.
   * @return the attribute value; empty if the attribute is absent or explicitly null.
   */
  public Optional<Object> get(AttributeId attributeId) {
    return Optional.ofNullable(getOrNull(attributeId));
  }

  /**
   * @return the DataType attribute value, or {@code null} if absent or null.
   */
  public @Nullable NodeId dataType() {
    return (NodeId) getOrNull(AttributeId.DataType);
  }

  /**
   * @return the ValueRank attribute value, or {@code null} if absent or null.
   */
  public @Nullable Integer valueRank() {
    return (Integer) getOrNull(AttributeId.ValueRank);
  }

  /**
   * @return the ArrayDimensions attribute value, or {@code null} if absent or null.
   */
  public UInteger @Nullable [] arrayDimensions() {
    return (UInteger[]) getOrNull(AttributeId.ArrayDimensions);
  }

  /**
   * @return the Value attribute value, or {@code null} if absent or null.
   */
  public @Nullable DataValue value() {
    return (DataValue) getOrNull(AttributeId.Value);
  }

  /**
   * @return a deterministic content key — attribute ids in enum order paired with a canonical
   *     (array-aware) string of each value — used to fingerprint a compiled model. Full value
   *     content is rendered rather than a lossy Java hash so distinct values never collide
   *     deterministically.
   */
  String contentHash() {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<AttributeId, Object> entry : values.entrySet()) {
      Object v = entry.getValue() == EXPLICIT_NULL ? null : entry.getValue();
      sb.append(entry.getKey().name()).append('=').append(canonicalString(v)).append(';');
    }
    return sb.toString();
  }

  /**
   * A canonical, content-complete string of an attribute value. Arrays render element-wise
   * (identity-based {@code Object.toString} never leaks in: {@link DataValue} and {@link Variant}
   * are decomposed so contained arrays also render element-wise).
   */
  private static String canonicalString(@Nullable Object value) {
    if (value == null) {
      return "null";
    }
    if (value instanceof Object[] array) {
      return Arrays.deepToString(array);
    }
    if (value.getClass().isArray()) {
      int length = Array.getLength(value);
      StringBuilder sb = new StringBuilder("[");
      for (int i = 0; i < length; i++) {
        if (i > 0) {
          sb.append(", ");
        }
        sb.append(Array.get(value, i));
      }
      return sb.append(']').toString();
    }
    if (value instanceof DataValue dataValue) {
      return "DataValue{"
          + canonicalString(dataValue.getValue().getValue())
          + ", status="
          + dataValue.getStatusCode()
          + ", sourceTime="
          + dataValue.getSourceTime()
          + ", sourcePicoseconds="
          + dataValue.getSourcePicoseconds()
          + "}";
    }
    if (value instanceof Variant variant) {
      return "Variant{" + canonicalString(variant.getValue()) + "}";
    }
    return String.valueOf(value);
  }

  /** Array-aware, null-safe hash of an attribute value, used by {@link #hashCode}. */
  private static int valueHash(@Nullable Object value) {
    return value instanceof Object[] array ? Arrays.deepHashCode(array) : Objects.hashCode(value);
  }

  /**
   * Copy {@code value} if it is mutable: arrays are cloned (element-wise for nested mutability) and
   * an array-valued {@link DataValue} is rebuilt around a cloned array. Immutable values pass
   * through.
   */
  private static Object defensiveCopy(Object value) {
    if (value.getClass().isArray()) {
      return copyArray(value);
    }
    if (value instanceof DataValue dataValue) {
      Object contained = dataValue.getValue().getValue();
      if (contained != null && contained.getClass().isArray()) {
        Object containedCopy = copyArray(contained);
        return dataValue.copy(b -> b.setValue(new Variant(containedCopy)));
      }
    }
    return value;
  }

  private static Object copyArray(Object array) {
    int length = Array.getLength(array);
    Object copy = Array.newInstance(array.getClass().getComponentType(), length);
    //noinspection SuspiciousSystemArraycopy
    System.arraycopy(array, 0, copy, 0, length);
    if (copy instanceof @Nullable Object[] objects) {
      for (int i = 0; i < objects.length; i++) {
        Object element = objects[i];
        if (element != null) {
          objects[i] = defensiveCopy(element);
        }
      }
    }
    return copy;
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (!(o instanceof AttributeSnapshot other) || !values.keySet().equals(other.values.keySet())) {
      return false;
    }
    for (Map.Entry<AttributeId, Object> entry : values.entrySet()) {
      if (!Objects.deepEquals(entry.getValue(), other.values.get(entry.getKey()))) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    int h = 0;
    for (Map.Entry<AttributeId, Object> entry : values.entrySet()) {
      h += entry.getKey().hashCode() ^ valueHash(entry.getValue());
    }
    return h;
  }

  @Override
  public String toString() {
    return "AttributeSnapshot" + values.keySet();
  }

  /** Builder for {@link AttributeSnapshot}s. */
  public static final class Builder {

    private final EnumMap<AttributeId, Object> values = new EnumMap<>(AttributeId.class);

    private Builder() {}

    /**
     * Record {@code attributeId} as present; a {@code null} value records an explicit null.
     *
     * @param attributeId the attribute to record.
     * @param value the value, or {@code null} for explicit null.
     * @return this builder.
     */
    public Builder put(AttributeId attributeId, @Nullable Object value) {
      values.put(attributeId, value != null ? defensiveCopy(value) : EXPLICIT_NULL);
      return this;
    }

    /**
     * Record {@code attributeId} with {@code value} if non-null; leave it absent otherwise.
     *
     * @param attributeId the attribute to record.
     * @param value the value, or {@code null} to leave the attribute absent.
     * @return this builder.
     */
    public Builder putIfProvided(AttributeId attributeId, @Nullable Object value) {
      if (value != null) {
        values.put(attributeId, defensiveCopy(value));
      }
      return this;
    }

    /**
     * @return the built {@link AttributeSnapshot}.
     */
    public AttributeSnapshot build() {
      return new AttributeSnapshot(new EnumMap<>(values));
    }
  }
}
