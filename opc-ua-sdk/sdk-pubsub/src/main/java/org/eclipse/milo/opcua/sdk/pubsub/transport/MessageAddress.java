/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.transport;

import io.netty.buffer.ByteBuf;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.jspecify.annotations.Nullable;

/**
 * Per-message addressing for {@link PublisherChannel#send(ByteBuf, MessageAddress)}: everything a
 * broker transport needs to route one encoded NetworkMessage, fully resolved by the engine.
 *
 * <p>The engine resolves all values before sending (queue names per the configured {@code
 * BrokerTransportSettings} or the OPC UA Part 14 §7.3.4.7 topic tree via {@link BrokerTopics}, the
 * delivery guarantee per §7.3.4.5 via {@link BrokerQualityOfService}), so transports apply the
 * address verbatim and never consult configuration themselves. Transports without broker semantics
 * (e.g. UDP) ignore the address entirely via the default {@link PublisherChannel#send(ByteBuf,
 * MessageAddress)} delegation.
 *
 * @param queueName the broker queue (MQTT topic) to publish to, or {@code null} on transports
 *     without queue addressing (e.g. UDP).
 * @param deliveryGuarantee the resolved delivery guarantee; the engine only ever passes {@link
 *     BrokerTransportQualityOfService#AtMostOnce} (MQTT QoS 0), {@link
 *     BrokerTransportQualityOfService#AtLeastOnce} (QoS 1), or {@link
 *     BrokerTransportQualityOfService#ExactlyOnce} (QoS 2) — never {@code NotSpecified} or {@code
 *     BestEffort}.
 * @param retain whether the message is published retained (Part 14 Table 204 defaults: data
 *     messages {@code false}, metadata messages {@code true}).
 * @param kind whether the message carries DataSetMessages, DataSetMetaData, or PubSub status.
 * @param contentTypeHint the MIME content type of the payload for transports that can signal it
 *     (e.g. the MQTT 5.0 Content Type property), or {@code null} when the mapping has no defined
 *     content type. {@link #CONTENT_TYPE_UADP} and {@link #CONTENT_TYPE_JSON} are the Part 14
 *     §7.3.4.9 values for the built-in mappings.
 * @apiNote Create instances via {@link #of(String, BrokerTransportQualityOfService, boolean, Kind,
 *     String)} rather than the canonical constructor; the factory methods are stable while the
 *     canonical constructor is not.
 */
public record MessageAddress(
    @Nullable String queueName,
    BrokerTransportQualityOfService deliveryGuarantee,
    boolean retain,
    Kind kind,
    @Nullable String contentTypeHint) {

  /** The Part 14 §7.3.4.9.2 content type of UADP-mapped payloads. */
  public static final String CONTENT_TYPE_UADP = "application/opcua+uadp";

  /** The Part 14 §7.3.4.9.1 content type of (uncompressed) JSON-mapped payloads. */
  public static final String CONTENT_TYPE_JSON = "application/json";

  /** The kind of message an address routes. */
  public enum Kind {

    /** A NetworkMessage carrying DataSetMessages (or keep-alives). */
    DATA,

    /** A NetworkMessage carrying DataSetMetaData. */
    METADATA,

    /** A PubSub Status message. */
    STATUS
  }

  /**
   * Create a {@link MessageAddress}.
   *
   * @param queueName the broker queue (MQTT topic) to publish to, or {@code null} on transports
   *     without queue addressing.
   * @param deliveryGuarantee the resolved delivery guarantee; one of {@code AtMostOnce}, {@code
   *     AtLeastOnce}, or {@code ExactlyOnce}.
   * @param retain whether the message is published retained.
   * @param kind whether the message carries DataSetMessages, DataSetMetaData, or PubSub status.
   * @param contentTypeHint the MIME content type of the payload, or {@code null} when the mapping
   *     has no defined content type.
   * @return a new {@link MessageAddress}.
   */
  public static MessageAddress of(
      @Nullable String queueName,
      BrokerTransportQualityOfService deliveryGuarantee,
      boolean retain,
      Kind kind,
      @Nullable String contentTypeHint) {

    return new MessageAddress(queueName, deliveryGuarantee, retain, kind, contentTypeHint);
  }

  /**
   * The content type of payloads encoded by the named message mapping, or {@code null} when the
   * mapping has no defined content type.
   *
   * @param mappingName a message mapping name, e.g. {@code "uadp"} or {@code "json"}.
   * @return {@link #CONTENT_TYPE_UADP} for {@code "uadp"}, {@link #CONTENT_TYPE_JSON} for {@code
   *     "json"}, otherwise {@code null}.
   */
  public static @Nullable String contentTypeOfMapping(String mappingName) {
    return switch (mappingName) {
      case "uadp" -> CONTENT_TYPE_UADP;
      case "json" -> CONTENT_TYPE_JSON;
      default -> null;
    };
  }
}
