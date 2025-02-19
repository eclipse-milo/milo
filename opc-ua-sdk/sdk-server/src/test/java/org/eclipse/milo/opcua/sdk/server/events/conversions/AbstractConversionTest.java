/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.events.conversions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

abstract class AbstractConversionTest<S> {

  @Test
  public void testNullConversion() {
    for (OpcUaDataType targetType : OpcUaDataType.values()) {
      assertNull(convert(null, targetType, true));
      assertNull(convert(null, targetType, false));
    }
  }

  @Test
  public void testAllConversions() {
    for (OpcUaDataType targetType : OpcUaDataType.values()) {
      Conversion[] conversions = getConversions(targetType);
      ConversionType conversionType = getConversionType(targetType);
      OpcUaDataType sourceType = OpcUaDataType.fromBackingClass(getSourceClass());

      if (conversionType != ConversionType.NONE) {
        if (conversions == null || conversions.length == 0) {
          fail("expected conversions for " + targetType);
        }

        System.out.println(String.format("%s -> %s [%s]", sourceType, targetType, conversionType));

        for (Conversion conversion : conversions) {
          assertEquals(conversion.targetType, targetType);

          S fromValue = getSourceClass().cast(conversion.fromValue);
          Object targetValue = conversion.targetValue;

          Object convertedValue =
              convert(fromValue, targetType, conversionType == ConversionType.IMPLICIT);

          System.out.println(
              String.format("\tfromValue=%s targetValue=%s", fromValue, targetValue));

          assertEquals(targetValue, convertedValue);
        }
      } else {
        if (conversions.length != 0) {
          fail(
              String.format(
                  "conversions defined for %s -> %s " + "but no ConversionType is defined",
                  sourceType, targetType));
        }
      }
    }
  }

  @Test
  public void testExplicitConversionsCalledImplicitlyAreNull() {
    for (OpcUaDataType targetType : OpcUaDataType.values()) {
      Conversion[] conversions = getConversions(targetType);

      for (Conversion conversion : conversions) {
        ConversionType conversionType = getConversionType(conversion.targetType);

        if (conversionType == ConversionType.EXPLICIT) {
          S fromValue = getSourceClass().cast(conversion.fromValue);

          Object convertedValue = convert(fromValue, targetType, true);

          assertNull(convertedValue);
        }
      }
    }
  }

  @Test
  public void testImplicitConversionsCalledExplicitly() {
    for (OpcUaDataType targetType : OpcUaDataType.values()) {
      Conversion[] conversions = getConversions(targetType);

      for (Conversion conversion : conversions) {
        ConversionType conversionType = getConversionType(conversion.targetType);

        if (conversionType == ConversionType.IMPLICIT) {
          S fromValue = getSourceClass().cast(conversion.fromValue);

          Object convertedValue = convert(fromValue, targetType, false);

          System.out.println(
              String.format(
                  "[%s] fromValue=%s targetType=%s targetValue=%s",
                  conversionType, fromValue, targetType, conversion.targetValue));

          assertEquals(conversion.targetValue, convertedValue);
        }
      }
    }
  }

  protected Conversion c(@NonNull S fromValue, @NonNull Object targetValue) {
    Conversion c = new Conversion();
    c.fromValue = fromValue;
    c.targetValue = targetValue;
    c.targetType = OpcUaDataType.fromBackingClass(targetValue.getClass());
    return c;
  }

  protected Conversion c(
      @NonNull S fromValue, @Nullable Object targetValue, @NonNull OpcUaDataType targetType) {
    Conversion c = new Conversion();
    c.fromValue = fromValue;
    c.targetValue = targetValue;
    c.targetType = targetType;
    return c;
  }

  protected abstract Class<S> getSourceClass();

  public abstract Conversion[] getConversions(OpcUaDataType targetType);

  public abstract ConversionType getConversionType(OpcUaDataType targetType);

  /*
  {
      //@formatter:off
      switch (targetType) {
          case Boolean:   return ConversionType.IMPLICIT;
          case Byte:      return ConversionType.IMPLICIT;
          case Float:     return ConversionType.IMPLICIT;
          case Double:    return ConversionType.IMPLICIT;
          case Int16:     return ConversionType.IMPLICIT;
          case Int32:     return ConversionType.IMPLICIT;
          case Int64:     return ConversionType.IMPLICIT;
          case SByte:     return ConversionType.IMPLICIT;
          case String:    return ConversionType.EXPLICIT;
          case UInt16:    return ConversionType.IMPLICIT;
          case UInt32:    return ConversionType.IMPLICIT;
          case UInt64:    return ConversionType.IMPLICIT;
          default:        return ConversionType.NONE;
      }
      //@formatter:on
  }
  */

  // protected abstract Conversion[] getExplicitConversions();

  // protected abstract Conversion[] getImplicitConversions();

  protected abstract Object convert(Object fromValue, OpcUaDataType targetType, boolean implicit);

  static class Conversion {
    Object fromValue;
    Object targetValue;
    OpcUaDataType targetType;
  }

  enum ConversionType {
    EXPLICIT,
    IMPLICIT,
    NONE
  }
}
