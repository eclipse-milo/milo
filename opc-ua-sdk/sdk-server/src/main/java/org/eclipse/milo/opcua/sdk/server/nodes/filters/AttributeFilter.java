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

import org.eclipse.milo.opcua.sdk.server.nodes.DefaultAttributeFilter;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.jspecify.annotations.Nullable;

public interface AttributeFilter {

  /** A shared instance of {@link DefaultAttributeFilter}. */
  DefaultAttributeFilter DEFAULT_INSTANCE = new DefaultAttributeFilter();

  /**
   * Get the value for the attribute identified by {@code attributeId} or delegate to the next
   * filter in the chain.
   *
   * @param ctx the {@link AttributeFilterContext}.
   * @param attributeId the {@link AttributeId} of the attribute to get the value of.
   * @return the value for the attribute identified by {@code attributeId}.
   * @see AttributeFilterContext#getAttribute(AttributeId)
   */
  default @Nullable Object getAttribute(AttributeFilterContext ctx, AttributeId attributeId) {
    return ctx.getAttribute(attributeId);
  }

  /**
   * Read the value for the attribute identified by {@code attributeId} or delegate to the next
   * filter in the chain.
   *
   * <p>This is similar to {@link #getAttribute(AttributeFilterContext, AttributeId)} except the
   * underlying implementation is allowed to throw {@link UaException} to indicate failure of some
   * kind.
   *
   * @param ctx the {@link AttributeFilterContext}.
   * @param attributeId the {@link AttributeId} of the attribute to read the value of.
   * @return the value for the attribute identified by {@code attributeId}.
   * @throws UaException if the attribute cannot be read.
   */
  default @Nullable Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
      throws UaException {
    return getAttribute(ctx, attributeId);
  }

  /**
   * Set the value for the attribute identified by {@code attributeId} or delegate to the next
   * filter in the chain.
   *
   * @param ctx the {@link AttributeFilterContext}.
   * @param attributeId the {@link AttributeId} of the attribute to set the value of.
   * @param value the value to set.
   * @see AttributeFilterContext#setAttribute(AttributeId, Object)
   */
  default void setAttribute(
      AttributeFilterContext ctx, AttributeId attributeId, @Nullable Object value) {
    ctx.setAttribute(attributeId, value);
  }

  /**
   * Write the value for the attribute identified by {@code attributeId} or delegate to the next
   * filter in the chain.
   *
   * <p>This is similar to {@link #setAttribute(AttributeFilterContext, AttributeId, Object)} except
   * the underlying implementation is allowed to throw {@link UaException} to indicate failure of
   * some kind.
   *
   * @param ctx the {@link AttributeFilterContext}.
   * @param attributeId the {@link AttributeId} of the attribute to write the value of.
   * @param value the value to write.
   * @throws UaException if the attribute cannot be written.
   */
  default void writeAttribute(
      AttributeFilterContext ctx, AttributeId attributeId, @Nullable Object value)
      throws UaException {

    setAttribute(ctx, attributeId, value);
  }
}
