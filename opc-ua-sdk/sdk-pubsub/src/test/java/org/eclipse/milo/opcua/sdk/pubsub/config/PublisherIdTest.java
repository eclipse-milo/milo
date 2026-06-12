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
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class PublisherIdTest {

  @Test
  void ubyteFactory() {
    PublisherId publisherId = PublisherId.ubyte(ubyte(7));

    PublisherId.ByteId byteId = assertInstanceOf(PublisherId.ByteId.class, publisherId);
    assertEquals(ubyte(7), byteId.value());
  }

  @Test
  void uint16Factory() {
    PublisherId publisherId = PublisherId.uint16(ushort(7));

    PublisherId.UInt16Id uint16Id = assertInstanceOf(PublisherId.UInt16Id.class, publisherId);
    assertEquals(ushort(7), uint16Id.value());
  }

  @Test
  void uint32Factory() {
    PublisherId publisherId = PublisherId.uint32(uint(7));

    PublisherId.UInt32Id uint32Id = assertInstanceOf(PublisherId.UInt32Id.class, publisherId);
    assertEquals(uint(7), uint32Id.value());
  }

  @Test
  void uint64Factory() {
    PublisherId publisherId = PublisherId.uint64(ulong(7));

    PublisherId.UInt64Id uint64Id = assertInstanceOf(PublisherId.UInt64Id.class, publisherId);
    assertEquals(ulong(7), uint64Id.value());
  }

  @Test
  void stringFactory() {
    PublisherId publisherId = PublisherId.string("publisher");

    PublisherId.StringId stringId = assertInstanceOf(PublisherId.StringId.class, publisherId);
    assertEquals("publisher", stringId.value());
  }

  @Test
  void toVariantUsesPart14WireRepresentation() {
    assertEquals(Variant.ofByte(ubyte(1)), PublisherId.ubyte(ubyte(1)).toVariant());
    assertEquals(Variant.ofUInt16(ushort(2)), PublisherId.uint16(ushort(2)).toVariant());
    assertEquals(Variant.ofUInt32(uint(3)), PublisherId.uint32(uint(3)).toVariant());
    assertEquals(Variant.ofUInt64(ulong(4)), PublisherId.uint64(ulong(4)).toVariant());
    assertEquals(Variant.ofString("p"), PublisherId.string("p").toVariant());
  }

  @ParameterizedTest
  @MethodSource("allPublisherIdTypes")
  void variantRoundTrip(PublisherId publisherId) {
    assertEquals(publisherId, PublisherId.fromVariant(publisherId.toVariant()));
  }

  private static Stream<PublisherId> allPublisherIdTypes() {
    return Stream.of(
        PublisherId.ubyte(ubyte(255)),
        PublisherId.uint16(ushort(65535)),
        PublisherId.uint32(uint(4294967295L)),
        PublisherId.uint64(ulong(Long.MAX_VALUE)),
        PublisherId.string("urn:milo:publisher"));
  }

  @Test
  void fromVariantSelectsTypeByVariantContent() {
    assertInstanceOf(PublisherId.ByteId.class, PublisherId.fromVariant(Variant.ofByte(ubyte(1))));
    assertInstanceOf(
        PublisherId.UInt16Id.class, PublisherId.fromVariant(Variant.ofUInt16(ushort(1))));
    assertInstanceOf(
        PublisherId.UInt32Id.class, PublisherId.fromVariant(Variant.ofUInt32(uint(1))));
    assertInstanceOf(
        PublisherId.UInt64Id.class, PublisherId.fromVariant(Variant.ofUInt64(ulong(1))));
    assertInstanceOf(PublisherId.StringId.class, PublisherId.fromVariant(Variant.ofString("p")));
  }

  @ParameterizedTest
  @MethodSource("unsupportedVariants")
  void fromVariantRejectsUnsupportedVariantContent(Variant variant) {
    PubSubConfigValidationException e =
        assertThrows(PubSubConfigValidationException.class, () -> PublisherId.fromVariant(variant));

    assertTrue(e.getMessage().contains("publisherId"), e.getMessage());
  }

  private static Stream<Variant> unsupportedVariants() {
    return Stream.of(
        Variant.NULL_VALUE,
        Variant.ofInt32(42),
        Variant.ofInt64(42L),
        Variant.ofDouble(1.0),
        Variant.ofBoolean(true),
        // array of an otherwise-permitted type is not a permitted PublisherId
        Variant.ofByteArray(new UByte[] {ubyte(1)}));
  }

  @Test
  void equalityIsByTypeAndValue() {
    assertEquals(PublisherId.uint16(ushort(1)), PublisherId.uint16(ushort(1)));
    assertEquals(
        PublisherId.uint16(ushort(1)).hashCode(), PublisherId.uint16(ushort(1)).hashCode());

    assertNotEquals(PublisherId.uint16(ushort(1)), PublisherId.uint16(ushort(2)));

    // the same numeric value in a different wire type is a different PublisherId
    assertNotEquals(PublisherId.uint16(ushort(1)), PublisherId.uint32(uint(1)));
    assertNotEquals(PublisherId.ubyte(ubyte(1)), PublisherId.uint16(ushort(1)));
  }
}
