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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.jspecify.annotations.Nullable;

/**
 * A {@link PublishedDataSetSourceConfig} sourcing dataset values from events emitted by an event
 * notifier Node: an ordered list of {@link EventFieldDefinition}s selecting event fields, plus a
 * where-clause {@link ContentFilter}.
 *
 * <p>Corresponds to the Part 14 {@code PublishedEventsDataType} source. Field order defines wire
 * order; a dataset with zero fields is legal but produces no publishable content.
 *
 * <p>The canonical form of {@code eventNotifier} is the namespace-URI {@link ExpandedNodeId}
 * produced by {@link NodeId#expanded(NamespaceTable)}, which is the form the Part 14 import mapping
 * produces; author configs in that form so an imported config compares equal to its authored
 * counterpart.
 *
 * <p>NodeIds inside {@code filter} operands and {@link EventFieldDefinition} selected fields are
 * carried verbatim and are NOT remapped across namespace tables, so configs whose filters or
 * selected fields reference non-zero namespace indices are not portable across processes with
 * differing namespace tables.
 */
public final class PublishedEventsConfig implements PublishedDataSetSourceConfig {

  private final ExpandedNodeId eventNotifier;
  private final List<EventFieldDefinition> fields;
  private final ContentFilter filter;

  private PublishedEventsConfig(Builder builder) {
    this.eventNotifier = builder.eventNotifier;
    this.fields = List.copyOf(builder.fields);
    this.filter = builder.filter;
  }

  /**
   * Get the {@link ExpandedNodeId} of the event notifier Node whose events feed this dataset.
   *
   * @return the event notifier {@link ExpandedNodeId}; canonically in namespace-URI form.
   */
  public ExpandedNodeId getEventNotifier() {
    return eventNotifier;
  }

  /**
   * Get the event field definitions, in dataset (wire) order.
   *
   * @return the event field definitions; possibly empty.
   */
  public List<EventFieldDefinition> getFields() {
    return fields;
  }

  /**
   * Get the where-clause filter events must match to be published.
   *
   * @return the {@link ContentFilter}; a filter with {@code null} elements (the default) matches
   *     every event.
   */
  public ContentFilter getFilter() {
    return filter;
  }

  /**
   * Create a {@link Builder} pre-populated with the values of this config.
   *
   * @return a new {@link Builder}.
   */
  public Builder toBuilder() {
    Builder builder = new Builder(eventNotifier);
    builder.fields.addAll(fields);
    builder.filter = filter;
    return builder;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof PublishedEventsConfig that)) {
      return false;
    }
    return eventNotifier.equals(that.eventNotifier)
        && fields.equals(that.fields)
        && filter.equals(that.filter);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventNotifier, fields, filter);
  }

  @Override
  public String toString() {
    return "PublishedEventsConfig{eventNotifier=%s, fields=%s, filter=%s}"
        .formatted(eventNotifier, fields, filter);
  }

  /**
   * Create a new {@link Builder}.
   *
   * @param eventNotifier the {@link ExpandedNodeId} of the event notifier Node; canonically in
   *     namespace-URI form (see the class Javadoc).
   * @return a new {@link Builder}.
   */
  public static Builder builder(ExpandedNodeId eventNotifier) {
    return new Builder(eventNotifier);
  }

  /** A builder of {@link PublishedEventsConfig} instances. */
  public static final class Builder {

    private final ExpandedNodeId eventNotifier;
    private final List<EventFieldDefinition> fields = new ArrayList<>();
    private ContentFilter filter = new ContentFilter(null);

    private Builder(ExpandedNodeId eventNotifier) {
      this.eventNotifier = eventNotifier;
    }

    /**
     * Add an event field definition. Field order defines wire order.
     *
     * @param field the event field definition to add.
     * @return this {@link Builder}.
     */
    public Builder field(EventFieldDefinition field) {
      fields.add(field);
      return this;
    }

    /**
     * Set the where-clause filter events must match to be published.
     *
     * @param filter the {@link ContentFilter}; defaults to a filter with {@code null} elements,
     *     which matches every event.
     * @return this {@link Builder}.
     */
    public Builder filter(ContentFilter filter) {
      this.filter = filter;
      return this;
    }

    /**
     * Build a {@link PublishedEventsConfig} from the configured values.
     *
     * @return a new {@link PublishedEventsConfig}.
     * @throws PubSubConfigValidationException if two fields share a name or a DataSetFieldId.
     */
    public PublishedEventsConfig build() {
      Set<String> fieldNames = new HashSet<>();
      Set<UUID> fieldIds = new HashSet<>();
      for (EventFieldDefinition field : fields) {
        if (!fieldNames.add(field.getName())) {
          throw new PubSubConfigValidationException(
              "PublishedEventsConfig: duplicate field name '%s'".formatted(field.getName()));
        }
        if (!fieldIds.add(field.getDataSetFieldId())) {
          throw new PubSubConfigValidationException(
              "PublishedEventsConfig: duplicate dataSetFieldId %s on field '%s'"
                  .formatted(field.getDataSetFieldId(), field.getName()));
        }
      }

      return new PublishedEventsConfig(this);
    }
  }
}
