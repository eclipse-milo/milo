/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.events.operators;

import org.eclipse.milo.opcua.sdk.server.events.FilterContext;
import org.eclipse.milo.opcua.sdk.server.events.OperatorContext;
import org.eclipse.milo.opcua.sdk.server.events.ValidationException;
import org.eclipse.milo.opcua.sdk.server.events.conversions.ImplicitConversions;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.util.LikeMatcher;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.structured.FilterOperand;
import org.jspecify.annotations.Nullable;

public class Like implements Operator<Boolean> {

  private final LikeMatcher matcher = new LikeMatcher();

  Like() {}

  @Override
  public void validate(FilterContext context, FilterOperand[] operands) throws ValidationException {
    OperatorUtil.validateMinOperandCount(operands, 2);
  }

  @Nullable
  @Override
  public Boolean apply(
      OperatorContext context, BaseEventTypeNode eventNode, FilterOperand[] operands)
      throws UaException {

    validate(context, operands);

    Object lhs = OperatorUtil.resolve(context, eventNode, operands[0]);
    Object rhs = OperatorUtil.resolve(context, eventNode, operands[1]);

    // Three-valued logic: a null operand is indeterminate, consistent with the comparison
    // operators.
    if (lhs == null || rhs == null) {
      return null;
    }

    String value = asString(lhs);
    String pattern = asString(rhs);

    // An operand that is present but not convertible to a String cannot match; FALSE rather than
    // indeterminate, mirroring a type mismatch in the comparison operators.
    if (value == null || pattern == null) {
      return false;
    }

    try {
      return matcher.matches(value, pattern);
    } catch (IllegalArgumentException e) {
      // A malformed pattern cannot match anything; FALSE rather than a filter error.
      return false;
    }
  }

  @Nullable
  private static String asString(@Nullable Object value) {
    value = OperatorUtil.toScalarIfSingleElementArray(value);

    if (value instanceof String s) {
      return s;
    } else if (value != null) {
      Object converted = ImplicitConversions.convert(value, OpcUaDataType.String);

      return converted instanceof String s ? s : null;
    } else {
      return null;
    }
  }
}
