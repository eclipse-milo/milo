/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DefaultDataTypeManagerTest {
  private static final NodeId TYPE = new NodeId(2, 1);
  private static final NodeId BINARY = new NodeId(2, 2);
  private static final NodeId XML = new NodeId(2, 3);
  private static final NodeId JSON = new NodeId(2, 4);
  private final DefaultDataTypeManager manager = new DefaultDataTypeManager();
  private final DataTypeCodec codec = new Range.Codec();

  @Test
  void lastReleaseRemovesEveryLookupAndAllowsReacquisition() {
    var handle = acquire();
    assertInstalled(codec);
    handle.close();
    assertRemoved();
    var next = acquire();
    handle.close();
    assertInstalled(codec);
    next.close();
    assertRemoved();
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void sharedRegistrationSurvivesUntilBothHoldersRelease(boolean reverse) {
    var first = acquire();
    var second = acquire();
    var released = reverse ? second : first;
    var retained = reverse ? first : second;
    released.close();
    released.close();
    assertInstalled(codec);
    retained.close();
    assertRemoved();
  }

  @Test
  void borrowedApplicationRegistrationSurvivesRelease() {
    register(codec);
    acquire().close();
    assertInstalled(codec);
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void applicationReplacementSurvivesOldHandleRelease(boolean sameCodec) {
    var handle = acquire();
    DataTypeCodec replacement = sameCodec ? codec : new Range.Codec();
    register(replacement);
    handle.close();
    assertInstalled(replacement);
  }

  // Every requested id shares a lookup namespace, including type/encoding role collisions.
  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4})
  void encodingCollisionRejectsEntireRegistration(int id) {
    NodeId otherType = new NodeId(2, 10);
    NodeId occupied = new NodeId(2, id);
    DataTypeCodec other = new Range.Codec();
    manager.registerType(otherType, other, occupied, null, null);
    assertThrows(IllegalStateException.class, this::acquire);
    assertSame(other, manager.getCodec(otherType));
    assertSame(other, manager.getCodec(occupied));
    assertEquals(occupied, manager.getBinaryEncodingId(otherType));
    for (int candidate = 1; candidate <= 4; candidate++) {
      if (candidate != id) assertNull(manager.getCodec(new NodeId(2, candidate)));
    }
    assertNull(manager.getBinaryEncodingId(TYPE));
    assertNull(manager.getXmlEncodingId(TYPE));
    assertNull(manager.getJsonEncodingId(TYPE));
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4})
  void dataTypeCollisionRejectsEntireRegistration(int id) {
    NodeId occupied = new NodeId(2, id);
    DataTypeCodec other = new Range.Codec();
    manager.registerType(occupied, other, null, null, null);
    assertThrows(IllegalStateException.class, this::acquire);
    assertSame(other, manager.getCodec(occupied));
    assertNull(manager.getBinaryEncodingId(TYPE));
    assertNull(manager.getXmlEncodingId(TYPE));
    assertNull(manager.getJsonEncodingId(TYPE));
  }

  @Test
  void differentMappingsCannotBorrowEvenWithSameCodec() {
    register(codec);
    assertThrows(
        IllegalStateException.class, () -> manager.acquireType(TYPE, codec, BINARY, XML, null));
    assertThrows(
        IllegalStateException.class, () -> manager.acquireType(TYPE, codec, BINARY, JSON, XML));
    assertInstalled(codec);
  }

  @Test
  void nullEncodingIdsAreEquivalentToAbsentEncodings() {
    var first = manager.acquireType(TYPE, codec, null, NodeId.NULL_VALUE, null);
    var second = manager.acquireType(TYPE, codec, NodeId.NULL_VALUE, null, NodeId.NULL_VALUE);
    first.close();
    assertSame(codec, manager.getCodec(TYPE));
    assertNull(manager.getXmlEncodingId(TYPE));
    second.close();
    assertRemoved();
  }

  // A legacy write may replace only one encoding lookup belonging to another type.
  @Test
  void partialReplacementSurvivesAndPreventsBorrowingDamagedRegistration() {
    var handle = acquire();
    NodeId otherType = new NodeId(2, 10);
    manager.registerType(otherType, codec, BINARY, null, null);
    assertThrows(IllegalStateException.class, this::acquire);
    handle.close();
    assertSame(codec, manager.getCodec(BINARY));
    assertEquals(otherType, manager.getDataTypeId(BINARY));
    assertSame(codec, manager.getCodec(otherType));
    assertEquals(BINARY, manager.getBinaryEncodingId(otherType));
    assertNull(manager.getCodec(TYPE));
    assertNull(manager.getCodec(XML));
    assertNull(manager.getCodec(JSON));
    assertNull(manager.getBinaryEncodingId(TYPE));
  }

  @Test
  void ordinaryRegistrationRetainsUnspecifiedLegacyEncodingMappings() {
    register(codec);
    DataTypeCodec replacement = new Range.Codec();
    manager.registerType(TYPE, replacement, null, null, null);
    assertSame(replacement, manager.getCodec(TYPE));
    assertSame(codec, manager.getCodec(BINARY));
    assertEquals(BINARY, manager.getBinaryEncodingId(TYPE));
    assertThrows(
        IllegalStateException.class,
        () -> manager.acquireType(TYPE, replacement, null, null, null));
  }

  // Competing acquisitions must share one lifetime, regardless of which wins installation.
  @Test
  void concurrentAcquisitionsRetainRegistrationUntilLastRelease() throws Exception {
    var executor = Executors.newFixedThreadPool(2);
    var start = new CountDownLatch(1);
    try {
      var first =
          executor.submit(
              () -> {
                start.await();
                return acquire();
              });
      var second =
          executor.submit(
              () -> {
                start.await();
                return acquire();
              });
      start.countDown();
      var firstHandle = first.get(5, TimeUnit.SECONDS);
      var secondHandle = second.get(5, TimeUnit.SECONDS);
      firstHandle.close();
      assertInstalled(codec);
      secondHandle.close();
      assertRemoved();
    } finally {
      executor.shutdownNow();
    }
  }

  // Exercise all three operations together, including replacement with the same codec identity.
  @RepeatedTest(100)
  void concurrentAcquisitionReleaseAndReplacementPreserveApplicationRegistration()
      throws Exception {
    var executor = Executors.newFixedThreadPool(3);
    try {
      var initial = acquire();
      var start = new CountDownLatch(1);
      var release =
          executor.submit(
              () -> {
                start.await();
                initial.close();
                return null;
              });
      var replacement =
          executor.submit(
              () -> {
                start.await();
                register(codec);
                return null;
              });
      var acquisition =
          executor.submit(
              () -> {
                start.await();
                acquire().close();
                return null;
              });
      start.countDown();
      release.get(5, TimeUnit.SECONDS);
      replacement.get(5, TimeUnit.SECONDS);
      acquisition.get(5, TimeUnit.SECONDS);
      assertInstalled(codec);
    } finally {
      executor.shutdownNow();
    }
  }

  // Closing one handle from multiple threads must not consume another holder's reference.
  @Test
  void concurrentCloseIsIdempotent() throws Exception {
    var released = acquire();
    var retained = acquire();
    var executor = Executors.newFixedThreadPool(2);
    var start = new CountDownLatch(1);
    try {
      var first =
          executor.submit(
              () -> {
                start.await();
                released.close();
                return null;
              });
      var second =
          executor.submit(
              () -> {
                start.await();
                released.close();
                return null;
              });
      start.countDown();
      first.get(5, TimeUnit.SECONDS);
      second.get(5, TimeUnit.SECONDS);
      assertInstalled(codec);
      retained.close();
      assertRemoved();
    } finally {
      executor.shutdownNow();
    }
  }

  private DataTypeManager.RegistrationHandle acquire() {
    return manager.acquireType(TYPE, codec, BINARY, XML, JSON);
  }

  private void register(DataTypeCodec value) {
    manager.registerType(TYPE, value, BINARY, XML, JSON);
  }

  private void assertInstalled(DataTypeCodec value) {
    assertEquals(TYPE, manager.getDataTypeId(TYPE));
    assertEquals(TYPE, manager.getDataTypeId(BINARY));
    assertEquals(TYPE, manager.getDataTypeId(XML));
    assertEquals(TYPE, manager.getDataTypeId(JSON));
    assertSame(value, manager.getCodec(TYPE));
    assertSame(value, manager.getCodec(BINARY));
    assertSame(value, manager.getCodec(XML));
    assertSame(value, manager.getCodec(JSON));
    assertEquals(BINARY, manager.getBinaryEncodingId(TYPE));
    assertEquals(XML, manager.getXmlEncodingId(TYPE));
    assertEquals(JSON, manager.getJsonEncodingId(TYPE));
  }

  private void assertRemoved() {
    assertNull(manager.getDataTypeId(TYPE));
    assertNull(manager.getDataTypeId(BINARY));
    assertNull(manager.getDataTypeId(XML));
    assertNull(manager.getDataTypeId(JSON));
    assertNull(manager.getCodec(TYPE));
    assertNull(manager.getCodec(BINARY));
    assertNull(manager.getCodec(XML));
    assertNull(manager.getCodec(JSON));
    assertNull(manager.getBinaryEncodingId(TYPE));
    assertNull(manager.getXmlEncodingId(TYPE));
    assertNull(manager.getJsonEncodingId(TYPE));
  }
}
