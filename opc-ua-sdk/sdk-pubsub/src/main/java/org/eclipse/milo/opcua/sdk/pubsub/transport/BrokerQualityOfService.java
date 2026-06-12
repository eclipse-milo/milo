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

import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.jspecify.annotations.Nullable;

/**
 * Delivery guarantee (QoS) resolution for broker transports per OPC UA Part 14 §6.4.2.1 and
 * §7.3.4.5.
 *
 * <p>Resolution: settings at the DataSetWriter or DataSetReader level override those of the
 * containing WriterGroup ({@code NotSpecified} means "inherit from the parent", §6.4.2.1); the
 * resolved value is then normalized for the MQTT QoS mapping of §7.3.4.5 — {@code BestEffort} and
 * {@code AtMostOnce} both map to QoS 0 and are normalized to {@link
 * BrokerTransportQualityOfService#AtMostOnce}. A value that is still {@code NotSpecified} after
 * full resolution defaults to {@code AtMostOnce} (QoS 0) for data messages and {@code AtLeastOnce}
 * (QoS 1) for metadata messages, matching the Part 14 Table 204 retention defaults (retained
 * metadata wants at-least-once delivery, §6.4.2.5.6).
 *
 * <p>The resolved value is always one of {@code AtMostOnce} (MQTT QoS 0), {@code AtLeastOnce} (QoS
 * 1), or {@code ExactlyOnce} (QoS 2) — exactly what the engine places in {@link
 * MessageAddress#deliveryGuarantee()} and what a broker subscriber channel uses for its SUBSCRIBE.
 *
 * <p>This helper is shared by the runtime engine and broker transport modules such as {@code
 * sdk-pubsub-mqtt} (reader-side resolution happens in the transport, which subscribes from the
 * connection config).
 */
public final class BrokerQualityOfService {

  private BrokerQualityOfService() {}

  /**
   * The requested delivery guarantee after applying the level override: the {@code override}
   * settings' value when present and not {@code NotSpecified}, otherwise the {@code group}
   * settings' value when present, otherwise {@code NotSpecified}.
   *
   * @param group the WriterGroup-level settings, or {@code null} when not configured (always {@code
   *     null} for DataSetReaders: the ReaderGroup has no broker transport parameters, §6.4.2.4).
   * @param override the DataSetWriter- or DataSetReader-level settings, or {@code null} when not
   *     configured.
   * @return the requested delivery guarantee; possibly {@code NotSpecified}.
   */
  public static BrokerTransportQualityOfService requestedDeliveryGuarantee(
      @Nullable BrokerTransportSettings group, @Nullable BrokerTransportSettings override) {

    if (override != null
        && override.getRequestedDeliveryGuarantee()
            != BrokerTransportQualityOfService.NotSpecified) {
      return override.getRequestedDeliveryGuarantee();
    }
    if (group != null) {
      return group.getRequestedDeliveryGuarantee();
    }
    return BrokerTransportQualityOfService.NotSpecified;
  }

  /**
   * Resolve the delivery guarantee for data messages: level override per {@link
   * #requestedDeliveryGuarantee(BrokerTransportSettings, BrokerTransportSettings)}, then normalized
   * with {@code NotSpecified} defaulting to {@code AtMostOnce} (MQTT QoS 0).
   *
   * @param group the WriterGroup-level settings, or {@code null} when not configured.
   * @param override the DataSetWriter- or DataSetReader-level settings, or {@code null} when not
   *     configured.
   * @return one of {@code AtMostOnce}, {@code AtLeastOnce}, or {@code ExactlyOnce}.
   */
  public static BrokerTransportQualityOfService resolveData(
      @Nullable BrokerTransportSettings group, @Nullable BrokerTransportSettings override) {

    return normalize(
        requestedDeliveryGuarantee(group, override), BrokerTransportQualityOfService.AtMostOnce);
  }

  /**
   * Resolve the delivery guarantee for metadata messages: level override per {@link
   * #requestedDeliveryGuarantee(BrokerTransportSettings, BrokerTransportSettings)}, then normalized
   * with {@code NotSpecified} defaulting to {@code AtLeastOnce} (MQTT QoS 1).
   *
   * @param group the WriterGroup-level settings, or {@code null} when not configured.
   * @param override the DataSetWriter- or DataSetReader-level settings, or {@code null} when not
   *     configured.
   * @return one of {@code AtMostOnce}, {@code AtLeastOnce}, or {@code ExactlyOnce}.
   */
  public static BrokerTransportQualityOfService resolveMetaData(
      @Nullable BrokerTransportSettings group, @Nullable BrokerTransportSettings override) {

    return normalize(
        requestedDeliveryGuarantee(group, override), BrokerTransportQualityOfService.AtLeastOnce);
  }

  private static BrokerTransportQualityOfService normalize(
      BrokerTransportQualityOfService requested,
      BrokerTransportQualityOfService notSpecifiedDefault) {

    return switch (requested) {
      case NotSpecified -> notSpecifiedDefault;
      case BestEffort, AtMostOnce -> BrokerTransportQualityOfService.AtMostOnce;
      case AtLeastOnce -> BrokerTransportQualityOfService.AtLeastOnce;
      case ExactlyOnce -> BrokerTransportQualityOfService.ExactlyOnce;
    };
  }
}
