/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.config;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.time.Duration;
import java.util.*;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldMetaData;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.Structure;
import org.jspecify.annotations.Nullable;

/** Shared helpers for {@link PubSubConfigMapper} and its per-direction mapping classes. */
final class PubSubConfigMapperUtil {

  /**
   * The reserved {@link FieldMetaData} property under which a {@link KeyFieldAddress} key
   * round-trips through the Part 14 configuration model.
   */
  static final QualifiedName MILO_SOURCE_KEY = new QualifiedName(0, "MiloSourceKey");

  /**
   * The reserved DataSetWriter property under which a non-default {@link
   * DataSetWriterConfig#getEventQueueCapacity() eventQueueCapacity} round-trips through the Part 14
   * configuration model.
   */
  static final QualifiedName MILO_EVENT_QUEUE_CAPACITY =
      new QualifiedName(0, "MiloEventQueueCapacity");

  /** The all-zero Guid used where the config model has no DataSetClassId to contribute. */
  static final UUID NULL_UUID = new UUID(0L, 0L);

  static final String PROFILE_UDP_UADP =
      "http://opcfoundation.org/UA-Profile/Transport/pubsub-udp-uadp";
  static final String PROFILE_MQTT_UADP =
      "http://opcfoundation.org/UA-Profile/Transport/pubsub-mqtt-uadp";
  static final String PROFILE_MQTT_JSON =
      "http://opcfoundation.org/UA-Profile/Transport/pubsub-mqtt-json";

  private PubSubConfigMapperUtil() {}

  /**
   * Create an {@link EncodingContext} backed by the shared OPC UA type/encoding managers but using
   * {@code namespaceTable} as its namespace table, for raw escape hatch ExtensionObject
   * encode/decode.
   */
  static EncodingContext newEncodingContext(NamespaceTable namespaceTable) {
    return new DefaultEncodingContext() {
      @Override
      public NamespaceTable getNamespaceTable() {
        return namespaceTable;
      }
    };
  }

  /** Convert a {@link Duration} to the spec Duration representation (Double milliseconds). */
  static Double toMillis(Duration duration) {
    return duration.toNanos() / 1_000_000.0;
  }

  /**
   * Convert a spec Duration (Double milliseconds) to a {@link Duration}; null, NaN, and
   * non-positive values map to {@link Duration#ZERO}.
   */
  static Duration fromMillis(@Nullable Double millis) {
    if (millis == null || millis.isNaN() || millis <= 0.0) {
      return Duration.ZERO;
    } else {
      return Duration.ofNanos(Math.round(millis * 1_000_000.0));
    }
  }

  /** Convert a property map to a KeyValuePair array; {@code null} when empty. */
  static KeyValuePair @Nullable [] toKeyValuePairs(Map<QualifiedName, Variant> properties) {
    if (properties.isEmpty()) {
      return null;
    }

    List<KeyValuePair> pairs = new ArrayList<>(properties.size());
    properties.forEach((key, value) -> pairs.add(new KeyValuePair(key, value)));
    return pairs.toArray(new KeyValuePair[0]);
  }

  /**
   * Convert a KeyValuePair array to a property map, preserving order and tolerating a null array
   * and null entries/keys (skipped); null values map to a null Variant.
   */
  static Map<QualifiedName, Variant> fromKeyValuePairs(KeyValuePair @Nullable [] pairs) {
    Map<QualifiedName, Variant> properties = new LinkedHashMap<>();
    if (pairs != null) {
      for (KeyValuePair pair : pairs) {
        if (pair.getKey() == null) {
          continue;
        }
        Variant value = pair.getValue();
        properties.put(pair.getKey(), value != null ? value : Variant.ofNull());
      }
    }
    return properties;
  }

  /**
   * Derive the {@link FieldMetaData} builtInType for a DataType NodeId: the builtin type id for
   * well-known builtin DataTypes, otherwise ExtensionObject (22).
   */
  static UByte deriveBuiltInType(NodeId dataTypeId) {
    OpcUaDataType dataType =
        Objects.requireNonNullElse(
            OpcUaDataType.fromNodeId(dataTypeId), OpcUaDataType.ExtensionObject);

    return ubyte(dataType.getTypeId());
  }

  /**
   * Decode a raw settings escape hatch ExtensionObject into the structure type required by the Part
   * 14 slot it is destined for.
   *
   * @throws PubSubConfigValidationException if the ExtensionObject cannot be decoded with the
   *     shared OPC UA codecs or decodes to an incompatible type.
   */
  static <T extends Structure> T decodeRaw(
      ExtensionObject raw, Class<T> expectedType, EncodingContext context, String path) {

    UaStructuredType decoded;
    try {
      decoded = raw.decode(context);
    } catch (UaSerializationException e) {
      throw new PubSubConfigValidationException(
          path + ": failed to decode raw settings ExtensionObject", e);
    }

    if (expectedType.isInstance(decoded)) {
      return expectedType.cast(decoded);
    } else {
      throw new PubSubConfigValidationException(
          "%s: raw settings ExtensionObject decoded to %s, expected an instance of %s"
              .formatted(path, decoded.getClass().getName(), expectedType.getName()));
    }
  }

  /**
   * Encode a settings structure that has no typed config representation into a raw escape hatch
   * ExtensionObject (OPC UA Binary).
   *
   * @throws PubSubConfigValidationException if no codec is registered for the structure.
   */
  static ExtensionObject encodeRaw(
      UaStructuredType settings, EncodingContext context, String path) {
    try {
      return ExtensionObject.encode(context, settings);
    } catch (UaSerializationException e) {
      throw new PubSubConfigValidationException(
          "%s: failed to preserve settings of type %s in a raw escape hatch"
              .formatted(path, settings.getClass().getName()),
          e);
    }
  }

  /** Build the empty {@link DataSetMetaDataType} emitted when a reader has no metadata. */
  static DataSetMetaDataType emptyMetaData() {
    return new DataSetMetaDataType(
        null,
        null,
        null,
        null,
        null,
        LocalizedText.NULL_VALUE,
        null,
        NULL_UUID,
        new ConfigurationVersionDataType(uint(0), uint(0)));
  }

  /** Check if {@code value} is the empty metadata shape emitted by {@link #emptyMetaData()}. */
  static boolean isEmptyMetaData(DataSetMetaDataType value) {
    FieldMetaData[] fields = value.getFields();
    ConfigurationVersionDataType version = value.getConfigurationVersion();

    return nullOrEmpty(value.getName())
        && (fields == null || fields.length == 0)
        && isNullUuid(value.getDataSetClassId())
        && (version == null
            || (isZero(version.getMajorVersion()) && isZero(version.getMinorVersion())));
  }

  static boolean nullOrEmpty(@Nullable String s) {
    return s == null || s.isEmpty();
  }

  static boolean isZero(@Nullable Double d) {
    return d == null || d == 0.0;
  }

  static boolean isZero(@Nullable UInteger value) {
    return value == null || value.longValue() == 0L;
  }

  static boolean isNullUuid(@Nullable UUID id) {
    return id == null || NULL_UUID.equals(id);
  }

  /**
   * Require a non-blank element name.
   *
   * @throws PubSubConfigValidationException if {@code name} is null or blank.
   */
  static String requireName(@Nullable String name, String path) {
    if (name == null || name.isBlank()) {
      throw new PubSubConfigValidationException(path + ": name is required");
    }
    return name;
  }

  /**
   * Check if {@code host} is a multicast group address, using a purely textual test (no DNS
   * resolution): IPv4 224.0.0.0/4 and IPv6 ff00::/8 literals are multicast; host names are treated
   * as unicast.
   */
  static boolean isMulticastAddress(String host) {
    String h = host;
    if (h.startsWith("[") && h.endsWith("]")) {
      h = h.substring(1, h.length() - 1);
    }

    if (h.indexOf(':') >= 0) {
      // IPv6 literal
      return h.length() >= 2
          && (h.charAt(0) == 'f' || h.charAt(0) == 'F')
          && (h.charAt(1) == 'f' || h.charAt(1) == 'F');
    }

    int dot = h.indexOf('.');
    if (dot > 0) {
      try {
        int firstOctet = Integer.parseInt(h.substring(0, dot));
        return firstOctet >= 224 && firstOctet <= 239;
      } catch (NumberFormatException e) {
        return false;
      }
    }

    return false;
  }
}
