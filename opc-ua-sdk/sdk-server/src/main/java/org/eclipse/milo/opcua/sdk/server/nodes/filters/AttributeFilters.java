/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes.filters;

import java.util.function.BiConsumer;
import java.util.function.Function;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.jspecify.annotations.Nullable;

public final class AttributeFilters {

  private AttributeFilters() {}

  public static AttributeFilter getValue(Function<AttributeFilterContext, DataValue> get) {
    return new AttributeFilter() {
      @Override
      public Object getAttribute(AttributeFilterContext ctx, AttributeId attributeId) {
        if (attributeId == AttributeId.Value) {
          return get.apply(ctx);
        } else {
          return ctx.getAttribute(attributeId);
        }
      }
    };
  }

  public static AttributeFilter setValue(BiConsumer<AttributeFilterContext, DataValue> set) {
    return new AttributeFilter() {
      @Override
      public void setAttribute(AttributeFilterContext ctx, AttributeId attributeId, Object value) {
        if (attributeId == AttributeId.Value) {
          set.accept(ctx, (DataValue) value);
        } else {
          ctx.setAttribute(attributeId, value);
        }
      }
    };
  }

  public static AttributeFilter getSetValue(
      Function<AttributeFilterContext, DataValue> get,
      BiConsumer<AttributeFilterContext, DataValue> set) {

    return new AttributeFilter() {
      @Override
      public @Nullable Object getAttribute(AttributeFilterContext ctx, AttributeId attributeId) {
        if (attributeId == AttributeId.Value) {
          return get.apply(ctx);
        } else {
          return ctx.getAttribute(attributeId);
        }
      }

      @Override
      public void setAttribute(
          AttributeFilterContext ctx, AttributeId attributeId, @Nullable Object value) {
        if (attributeId == AttributeId.Value) {
          set.accept(ctx, (DataValue) value);
        } else {
          ctx.setAttribute(attributeId, value);
        }
      }
    };
  }
}
