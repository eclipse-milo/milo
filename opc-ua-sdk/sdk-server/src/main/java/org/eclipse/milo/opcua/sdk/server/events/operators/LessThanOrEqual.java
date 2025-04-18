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

import org.eclipse.milo.opcua.sdk.server.events.OperatorContext;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.jspecify.annotations.Nullable;

public class LessThanOrEqual extends ImplicitConversionBinaryOperator<Boolean> {

  LessThanOrEqual() {}

  @Nullable
  @Override
  protected Boolean apply(
      OperatorContext context,
      BaseEventTypeNode eventNode,
      OpcUaDataType dataType,
      @Nullable Object operand0,
      @Nullable Object operand1) {

    if (operand0 instanceof Number n0 && operand1 instanceof Number n1) {
      switch (dataType) {
        case SByte:
        case Int16:
        case Int32:
        case Int64:
        case Byte:
        case UInt16:
        case UInt32:
          return n0.longValue() <= n1.longValue();

        case UInt64:
          return Long.compareUnsigned(n0.longValue(), n1.longValue()) <= 0;

        case Float:
          return n0.floatValue() <= n1.floatValue();

        case Double:
          return n0.doubleValue() <= n1.doubleValue();

        default:
          return false;
      }
    } else {
      return false;
    }
  }
}
