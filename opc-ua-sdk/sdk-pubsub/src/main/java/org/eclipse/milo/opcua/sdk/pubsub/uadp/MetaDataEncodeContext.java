/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.uadp;

import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;

/**
 * Context for {@link MessageMappingProvider#encodeMetaData(MetaDataEncodeContext,
 * DataSetMetaDataType, UShort)}: the writer whose DataSetMetaData is being published.
 *
 * <p>Future versions will add components to this record via new {@code of(...)} factory overloads;
 * the canonical constructor may change incompatibly when they do.
 *
 * @param encodingContext the {@link EncodingContext} used to encode the metadata structure.
 * @param publisherId the publisher id of the connection.
 * @param writerGroup the config of the WriterGroup containing the writer.
 * @param writer the config of the DataSetWriter whose metadata is being published.
 * @apiNote Create instances via {@link #of(EncodingContext, PublisherId, WriterGroupConfig,
 *     DataSetWriterConfig)} rather than the canonical constructor; the factory methods are stable
 *     while the canonical constructor is not.
 */
public record MetaDataEncodeContext(
    EncodingContext encodingContext,
    PublisherId publisherId,
    WriterGroupConfig writerGroup,
    DataSetWriterConfig writer) {

  /**
   * Create a {@link MetaDataEncodeContext}.
   *
   * @param encodingContext the {@link EncodingContext} used to encode the metadata structure.
   * @param publisherId the publisher id of the connection.
   * @param writerGroup the config of the WriterGroup containing the writer.
   * @param writer the config of the DataSetWriter whose metadata is being published.
   * @return a new {@link MetaDataEncodeContext}.
   */
  public static MetaDataEncodeContext of(
      EncodingContext encodingContext,
      PublisherId publisherId,
      WriterGroupConfig writerGroup,
      DataSetWriterConfig writer) {

    return new MetaDataEncodeContext(encodingContext, publisherId, writerGroup, writer);
  }
}
