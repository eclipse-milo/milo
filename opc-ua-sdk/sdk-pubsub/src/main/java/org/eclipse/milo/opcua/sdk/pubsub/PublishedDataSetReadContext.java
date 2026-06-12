/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub;

import java.util.List;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.jspecify.annotations.Nullable;

/**
 * Context for a {@link PublishedDataSetSource} read: identifies the dataset being read and the
 * fields a snapshot must supply, in wire order.
 *
 * @param dataSet the reference to the PublishedDataSet being read.
 * @param fields the field definitions of the dataset; field order defines the wire order.
 * @param metaData the configured metadata of the dataset, or {@code null} if none is configured.
 */
public record PublishedDataSetReadContext(
    PublishedDataSetRef dataSet,
    List<FieldDefinition> fields,
    @Nullable DataSetMetaDataConfig metaData) {

  /**
   * Create a new {@link PublishedDataSetReadContext}.
   *
   * @param dataSet the reference to the PublishedDataSet being read.
   * @param fields the field definitions of the dataset; field order defines the wire order.
   * @param metaData the configured metadata of the dataset, or {@code null} if none is configured.
   */
  public PublishedDataSetReadContext {
    fields = List.copyOf(fields);
  }
}
