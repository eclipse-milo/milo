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

public class GreaterThanOrEqual extends ImplicitConversionBinaryOperator {

  GreaterThanOrEqual() {}

  @Nullable
  @Override
  protected Boolean apply(
      OperatorContext context,
      BaseEventTypeNode eventNode,
      OpcUaDataType dataType,
      @Nullable Object operand0,
      @Nullable Object operand1) {

    if (operand0 != null && operand1 != null) {
      Integer comparison = OperatorUtil.compareOrdered(dataType, operand0, operand1);

      return comparison != null && comparison >= 0;
    } else {
      return false;
    }
  }
}
