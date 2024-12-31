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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.jspecify.annotations.Nullable;

public class AttributeFilterChain {

  private final ConcurrentLinkedDeque<AttributeFilter> filters = new ConcurrentLinkedDeque<>();

  /** Create an empty {@link AttributeFilterChain}. */
  public AttributeFilterChain() {}

  /**
   * Create an {@link AttributeFilterChain} with {@code filter} added to it.
   *
   * @param filter the filter to add.
   */
  public AttributeFilterChain(AttributeFilter filter) {
    filters.add(filter);
  }

  /**
   * Create an {@link AttributeFilterChain} with {@code filters} added to it.
   *
   * @param filters the filters to add.
   */
  public AttributeFilterChain(List<AttributeFilter> filters) {
    this.filters.addAll(filters);
  }

  /**
   * Using the next {@link AttributeFilter} in the chain, get the value for the attribute identified
   * by {@code attributeId} from {@code node}.
   *
   * @param node the {@link UaNode} to get the attribute value from.
   * @param attributeId the {@link AttributeId} of the attribute to get the value of.
   * @return the value for the attribute identified by {@code attributeId} from {@code node}.
   */
  public Object getAttribute(UaNode node, AttributeId attributeId) {
    return getAttribute(null, node, attributeId);
  }

  /**
   * Using the next {@link AttributeFilter} in the chain, get the value for the attribute identified
   * by {@code attributeId} from {@code node}.
   *
   * @param session the {@link Session} the attribute request is originating from, if there is one.
   * @param node the {@link UaNode} to get the attribute value from.
   * @param attributeId the {@link AttributeId} of the attribute to get the value of.
   * @return the value for the attribute identified by {@code attributeId} from {@code node}.
   */
  public Object getAttribute(@Nullable Session session, UaNode node, AttributeId attributeId) {
    Iterator<AttributeFilter> filterIterator = filters.iterator();

    AttributeFilter filter =
        filterIterator.hasNext() ? filterIterator.next() : AttributeFilter.DEFAULT_INSTANCE;

    var ctx = new AttributeFilterContext(session, node, filterIterator);

    Object value = filter.getAttribute(ctx, attributeId);

    if (ctx.isObservable()) {
      node.fireAttributeChanged(attributeId, value);
    }

    return value;
  }

  /**
   * Read the value for the attribute identified by {@code attributeId} from {@code node}.
   *
   * @param session the {@link Session} the attribute request is originating from, if there is one.
   * @param node the {@link UaNode} to read the attribute value from.
   * @param attributeId the {@link AttributeId} of the attribute to read the value of.
   * @return the value for the attribute identified by {@code attributeId} from {@code node}.
   * @throws UaException if the attribute cannot be read.
   */
  public Object readAttribute(@Nullable Session session, UaNode node, AttributeId attributeId)
      throws UaException {
    Iterator<AttributeFilter> filterIterator = filters.iterator();

    AttributeFilter filter =
        filterIterator.hasNext() ? filterIterator.next() : AttributeFilter.DEFAULT_INSTANCE;

    var ctx = new AttributeFilterContext(session, node, filterIterator);

    Object value = filter.readAttribute(ctx, attributeId);

    if (ctx.isObservable()) {
      node.fireAttributeChanged(attributeId, value);
    }

    return value;
  }

  /**
   * Set the value for the attribute identified by {@code attributeId}.
   *
   * @param node the {@link UaNode} to set the attribute value on.
   * @param attributeId the {@link AttributeId} of the attribute to set the value of.
   * @param value the value to set.
   */
  public void setAttribute(UaNode node, AttributeId attributeId, Object value) {
    setAttribute(null, node, attributeId, value);
  }

  /**
   * Set the value for the attribute identified by {@code attributeId}.
   *
   * @param session the {@link Session} the attribute request is originating from, if there is one.
   * @param node the {@link UaNode} to set the attribute value on.
   * @param attributeId the {@link AttributeId} of the attribute to set the value of.
   * @param value the value to set.
   */
  public void setAttribute(
      @Nullable Session session, UaNode node, AttributeId attributeId, Object value) {
    Iterator<AttributeFilter> filterIterator = filters.iterator();

    AttributeFilter filter =
        filterIterator.hasNext() ? filterIterator.next() : AttributeFilter.DEFAULT_INSTANCE;

    var ctx = new AttributeFilterContext(session, node, filterIterator);

    filter.setAttribute(ctx, attributeId, value);
  }

  /**
   * Write the value for the attribute identified by {@code attributeId}.
   *
   * @param session the {@link Session} the attribute request is originating from, if there is one.
   * @param node the {@link UaNode} to write the attribute value on.
   * @param attributeId the {@link AttributeId} of the attribute to write the value of.
   * @param value the value to write.
   * @throws UaException if the attribute cannot be written.
   */
  public void writeAttribute(
      @Nullable Session session, UaNode node, AttributeId attributeId, Object value)
      throws UaException {

    Iterator<AttributeFilter> filterIterator = filters.iterator();

    AttributeFilter filter =
        filterIterator.hasNext() ? filterIterator.next() : AttributeFilter.DEFAULT_INSTANCE;

    var ctx = new AttributeFilterContext(session, node, filterIterator);

    filter.writeAttribute(ctx, attributeId, value);
  }

  /**
   * Add {@code attributeFilter} to the front of this filter chain.
   *
   * @param attributeFilter the {@link AttributeFilter} to add.
   * @return this {@link AttributeFilterChain}.
   */
  public AttributeFilterChain addFirst(AttributeFilter attributeFilter) {
    filters.addFirst(attributeFilter);

    return this;
  }

  /**
   * Add {@code attributeFilters} (sequentially) to the front of this filter chain.
   *
   * @param attributeFilters the {@link AttributeFilter}s to add.
   * @return this {@link AttributeFilterChain}.
   */
  public AttributeFilterChain addFirst(AttributeFilter... attributeFilters) {
    Arrays.stream(attributeFilters).forEach(this::addFirst);

    return this;
  }

  /**
   * Add {@code attributeFilters} (sequentially) to the front of this filter chain.
   *
   * @param attributeFilters the {@link AttributeFilter}s to add.
   * @return this {@link AttributeFilterChain}.
   */
  public AttributeFilterChain addFirst(Collection<AttributeFilter> attributeFilters) {
    attributeFilters.forEach(this::addFirst);

    return this;
  }

  /**
   * Add {@code attributeFilter} to the end of this filter chain.
   *
   * @param attributeFilter the {@link AttributeFilter} to add.
   * @return this {@link AttributeFilterChain}.
   */
  public AttributeFilterChain addLast(AttributeFilter attributeFilter) {
    filters.addLast(attributeFilter);

    return this;
  }

  /**
   * Add {@code attributeFilters} (sequentially) to the end of this filter chain.
   *
   * @param attributeFilters the {@link AttributeFilter}s to add.
   * @return this {@link AttributeFilterChain}.
   */
  public AttributeFilterChain addLast(AttributeFilter... attributeFilters) {
    Arrays.stream(attributeFilters).forEach(this::addLast);

    return this;
  }

  /**
   * Add {@code attributeFilters} (sequentially) to the end of this filter chain.
   *
   * @param attributeFilters the {@link AttributeFilter}s to add.
   * @return this {@link AttributeFilterChain}.
   */
  public AttributeFilterChain addLast(Collection<AttributeFilter> attributeFilters) {
    attributeFilters.forEach(this::addLast);

    return this;
  }

  /**
   * Get a List containing all the {@link AttributeFilter}s belonging to this chain.
   *
   * @return a List containing all the {@link AttributeFilter}s belonging to this chain.
   */
  public List<AttributeFilter> getFilters() {
    return new ArrayList<>(filters);
  }
}
