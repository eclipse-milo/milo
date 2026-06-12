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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link BrokerQualityOfService}: writer/reader settings override the group ({@code
 * NotSpecified} inherits, §6.4.2.1), {@code BestEffort} normalizes to {@code AtMostOnce} (both are
 * MQTT QoS 0, §7.3.4.5), and a value still {@code NotSpecified} after full resolution defaults to
 * {@code AtMostOnce} for data and {@code AtLeastOnce} for metadata.
 *
 * <p>Note: the generated {@link BrokerTransportQualityOfService} enum has no {@code Transactional}
 * member (NotSpecified/BestEffort/AtLeastOnce/AtMostOnce/ExactlyOnce only), so no configuration can
 * express the plan's "Transactional" case and no rejection path exists to test.
 */
class BrokerQualityOfServiceTest {

  private static BrokerTransportSettings settings(BrokerTransportQualityOfService qos) {
    return BrokerTransportSettings.builder().requestedDeliveryGuarantee(qos).build();
  }

  // region requestedDeliveryGuarantee (level override)

  @Test
  void overrideWinsWhenSpecified() {
    assertEquals(
        BrokerTransportQualityOfService.ExactlyOnce,
        BrokerQualityOfService.requestedDeliveryGuarantee(
            settings(BrokerTransportQualityOfService.AtLeastOnce),
            settings(BrokerTransportQualityOfService.ExactlyOnce)));
  }

  @Test
  void notSpecifiedOverrideInheritsGroupValue() {
    assertEquals(
        BrokerTransportQualityOfService.AtLeastOnce,
        BrokerQualityOfService.requestedDeliveryGuarantee(
            settings(BrokerTransportQualityOfService.AtLeastOnce),
            settings(BrokerTransportQualityOfService.NotSpecified)));
  }

  @Test
  void absentOverrideInheritsGroupValue() {
    assertEquals(
        BrokerTransportQualityOfService.BestEffort,
        BrokerQualityOfService.requestedDeliveryGuarantee(
            settings(BrokerTransportQualityOfService.BestEffort), null));
  }

  @Test
  void bothAbsentIsNotSpecified() {
    assertEquals(
        BrokerTransportQualityOfService.NotSpecified,
        BrokerQualityOfService.requestedDeliveryGuarantee(null, null));
    assertEquals(
        BrokerTransportQualityOfService.NotSpecified,
        BrokerQualityOfService.requestedDeliveryGuarantee(
            null, settings(BrokerTransportQualityOfService.NotSpecified)));
  }

  // endregion

  // region resolveData

  @Test
  void resolveDataNotSpecifiedDefaultsToAtMostOnce() {
    assertEquals(
        BrokerTransportQualityOfService.AtMostOnce, BrokerQualityOfService.resolveData(null, null));
    assertEquals(
        BrokerTransportQualityOfService.AtMostOnce,
        BrokerQualityOfService.resolveData(
            settings(BrokerTransportQualityOfService.NotSpecified),
            settings(BrokerTransportQualityOfService.NotSpecified)));
  }

  @Test
  void resolveDataNormalizesBestEffortToAtMostOnce() {
    assertEquals(
        BrokerTransportQualityOfService.AtMostOnce,
        BrokerQualityOfService.resolveData(
            settings(BrokerTransportQualityOfService.BestEffort), null));
    assertEquals(
        BrokerTransportQualityOfService.AtMostOnce,
        BrokerQualityOfService.resolveData(
            null, settings(BrokerTransportQualityOfService.BestEffort)));
  }

  @Test
  void resolveDataPassesThroughAtLeastOnceAtMostOnceExactlyOnce() {
    assertEquals(
        BrokerTransportQualityOfService.AtLeastOnce,
        BrokerQualityOfService.resolveData(
            settings(BrokerTransportQualityOfService.AtLeastOnce), null));
    assertEquals(
        BrokerTransportQualityOfService.AtMostOnce,
        BrokerQualityOfService.resolveData(
            settings(BrokerTransportQualityOfService.AtMostOnce), null));
    assertEquals(
        BrokerTransportQualityOfService.ExactlyOnce,
        BrokerQualityOfService.resolveData(
            settings(BrokerTransportQualityOfService.ExactlyOnce), null));
  }

  @Test
  void resolveDataWriterOverrideBeatsGroup() {
    assertEquals(
        BrokerTransportQualityOfService.AtLeastOnce,
        BrokerQualityOfService.resolveData(
            settings(BrokerTransportQualityOfService.ExactlyOnce),
            settings(BrokerTransportQualityOfService.AtLeastOnce)));

    // NotSpecified at the writer level inherits the group value rather than overriding it
    assertEquals(
        BrokerTransportQualityOfService.ExactlyOnce,
        BrokerQualityOfService.resolveData(
            settings(BrokerTransportQualityOfService.ExactlyOnce),
            settings(BrokerTransportQualityOfService.NotSpecified)));
  }

  // endregion

  // region resolveMetaData

  @Test
  void resolveMetaDataNotSpecifiedDefaultsToAtLeastOnce() {
    assertEquals(
        BrokerTransportQualityOfService.AtLeastOnce,
        BrokerQualityOfService.resolveMetaData(null, null));
    assertEquals(
        BrokerTransportQualityOfService.AtLeastOnce,
        BrokerQualityOfService.resolveMetaData(
            settings(BrokerTransportQualityOfService.NotSpecified), null));
  }

  @Test
  void resolveMetaDataNormalizesBestEffortToAtMostOnce() {
    // an explicitly configured BestEffort wins over the metadata AtLeastOnce default and
    // normalizes to the QoS 0 representative
    assertEquals(
        BrokerTransportQualityOfService.AtMostOnce,
        BrokerQualityOfService.resolveMetaData(
            settings(BrokerTransportQualityOfService.BestEffort), null));
  }

  @Test
  void resolveMetaDataWriterOverrideBeatsGroup() {
    assertEquals(
        BrokerTransportQualityOfService.ExactlyOnce,
        BrokerQualityOfService.resolveMetaData(
            settings(BrokerTransportQualityOfService.AtLeastOnce),
            settings(BrokerTransportQualityOfService.ExactlyOnce)));
  }

  // endregion

  // region reader-side resolution (group always null: the ReaderGroup has no broker parameters)

  @Test
  void readerResolutionPassesNullGroup() {
    assertEquals(
        BrokerTransportQualityOfService.AtLeastOnce,
        BrokerQualityOfService.resolveData(
            null, settings(BrokerTransportQualityOfService.AtLeastOnce)));
    assertEquals(
        BrokerTransportQualityOfService.AtMostOnce, BrokerQualityOfService.resolveData(null, null));
    assertEquals(
        BrokerTransportQualityOfService.AtLeastOnce,
        BrokerQualityOfService.resolveMetaData(null, null));
    assertEquals(
        BrokerTransportQualityOfService.ExactlyOnce,
        BrokerQualityOfService.resolveMetaData(
            null, settings(BrokerTransportQualityOfService.ExactlyOnce)));
  }

  // endregion
}
