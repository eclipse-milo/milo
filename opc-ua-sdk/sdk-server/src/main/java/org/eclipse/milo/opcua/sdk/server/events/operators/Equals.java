/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.events.operators;

import java.lang.reflect.Array;
import java.util.Objects;
import org.eclipse.milo.opcua.sdk.server.events.FilterContext;
import org.eclipse.milo.opcua.sdk.server.events.OperatorContext;
import org.eclipse.milo.opcua.sdk.server.events.ValidationException;
import org.eclipse.milo.opcua.sdk.server.events.conversions.ImplicitConversions;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.structured.FilterOperand;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class Equals implements Operator<Boolean> {

  Equals() {}

  @Override
  public void validate(FilterContext context, FilterOperand[] operands) throws ValidationException {
    if (operands.length < 2) {
      throw new ValidationException(StatusCodes.Bad_FilterOperandCountMismatch);
    }
  }

  @Nullable
  @Override
  public Boolean apply(
      OperatorContext context, BaseEventTypeNode eventNode, FilterOperand[] operands)
      throws UaException {

    validate(context, operands);

    FilterOperand op0 = operands[0];
    FilterOperand op1 = operands[1];

    Object value0 = context.resolve(op0, eventNode);
    Object value1 = context.resolve(op1, eventNode);

    if (value0 == null || value1 == null) {
      return null;
    }

    OpcUaDataType dt0 = getType(value0);
    OpcUaDataType dt1 = getType(value1);

    if (dt0 == null || dt1 == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }

    int p0 = ImplicitConversions.getPrecedence(dt0);
    int p1 = ImplicitConversions.getPrecedence(dt1);

    if (p0 == p1) {
      assert dt0 == dt1;

      return equals(value0, value1);
    } else if (p0 >= p1) {
      // convert value1 to type of value0 (dt0)
      Object converted1 = convert(value1, dt0);

      return equals(value0, converted1);
    } else {
      // convert value0 to type of value1 (dt1)
      Object converted0 = convert(value0, dt1);

      return equals(converted0, value1);
    }
  }

  @Nullable
  private static Object convert(@NonNull Object value, OpcUaDataType targetType) {
    if (value.getClass().isArray()) {
      return convertArray(value, targetType);
    } else {
      return ImplicitConversions.convert(value, targetType);
    }
  }

  private static Object convertArray(@NonNull Object array, OpcUaDataType targetType) {
    int[] dimensions = ArrayUtil.getDimensions(array);

    Object flattened = ArrayUtil.flatten(array);
    int length = Array.getLength(flattened);

    Object transformed = Array.newInstance(targetType.getBackingClass(), length);

    for (int i = 0; i < length; i++) {
      Object sourceValue = Array.get(flattened, i);
      Object targetValue = ImplicitConversions.convert(sourceValue, targetType);
      Array.set(transformed, i, targetValue);
    }

    return ArrayUtil.unflatten(transformed, dimensions);
  }

  private static boolean equals(Object lhs, Object rhs) {
    if (lhs == null || rhs == null) {
      return Objects.equals(lhs, rhs);
    } else if (lhs.getClass().isArray()) {
      return Objects.deepEquals(lhs, rhs);
    } else {
      return Objects.equals(lhs, rhs);
    }
  }

  private static OpcUaDataType getType(@NonNull Object o) {
    if (o.getClass().isArray()) {
      return OpcUaDataType.fromBackingClass(ArrayUtil.getType(o));
    } else {
      return OpcUaDataType.fromBackingClass(o.getClass());
    }
  }
}
