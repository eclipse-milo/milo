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

import java.util.List;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.Nullable;

/**
 * Context for {@link MessageMappingProvider#encode(EncodeContext)}: everything needed to encode one
 * NetworkMessage for a WriterGroup publish cycle.
 *
 * <p>Future versions (e.g. JSON mapping, broker transports) will add components to this record via
 * new {@code of(...)} factory overloads; the canonical constructor may change incompatibly when
 * they do.
 *
 * @param encodingContext the {@link EncodingContext} used to encode field values.
 * @param publisherId the publisher id of the connection.
 * @param writerGroup the config of the WriterGroup being published.
 * @param groupVersion the GroupVersion header value, derived at component start.
 * @param networkMessageNumber the NetworkMessageNumber header value.
 * @param networkMessageSequenceNumber the NetworkMessage SequenceNumber header value.
 * @param timestamp the NetworkMessage timestamp, or {@code null} if not included.
 * @param messages the draft DataSetMessages to include, in payload order.
 * @apiNote Create instances via {@link #of(EncodingContext, PublisherId, WriterGroupConfig,
 *     UInteger, UShort, UShort, DateTime, List)} rather than the canonical constructor; the factory
 *     methods are stable while the canonical constructor is not.
 */
public record EncodeContext(
    EncodingContext encodingContext,
    PublisherId publisherId,
    WriterGroupConfig writerGroup,
    UInteger groupVersion,
    UShort networkMessageNumber,
    UShort networkMessageSequenceNumber,
    @Nullable DateTime timestamp,
    List<DataSetMessageDraft> messages) {

  /**
   * Create a new {@link EncodeContext}.
   *
   * @param encodingContext the {@link EncodingContext} used to encode field values.
   * @param publisherId the publisher id of the connection.
   * @param writerGroup the config of the WriterGroup being published.
   * @param groupVersion the GroupVersion header value, derived at component start.
   * @param networkMessageNumber the NetworkMessageNumber header value.
   * @param networkMessageSequenceNumber the NetworkMessage SequenceNumber header value.
   * @param timestamp the NetworkMessage timestamp, or {@code null} if not included.
   * @param messages the draft DataSetMessages to include, in payload order.
   */
  public EncodeContext {
    messages = List.copyOf(messages);
  }

  /**
   * Create an {@link EncodeContext}.
   *
   * @param encodingContext the {@link EncodingContext} used to encode field values.
   * @param publisherId the publisher id of the connection.
   * @param writerGroup the config of the WriterGroup being published.
   * @param groupVersion the GroupVersion header value, derived at component start.
   * @param networkMessageNumber the NetworkMessageNumber header value.
   * @param networkMessageSequenceNumber the NetworkMessage SequenceNumber header value.
   * @param timestamp the NetworkMessage timestamp, or {@code null} if not included.
   * @param messages the draft DataSetMessages to include, in payload order.
   * @return a new {@link EncodeContext}.
   */
  public static EncodeContext of(
      EncodingContext encodingContext,
      PublisherId publisherId,
      WriterGroupConfig writerGroup,
      UInteger groupVersion,
      UShort networkMessageNumber,
      UShort networkMessageSequenceNumber,
      @Nullable DateTime timestamp,
      List<DataSetMessageDraft> messages) {

    return new EncodeContext(
        encodingContext,
        publisherId,
        writerGroup,
        groupVersion,
        networkMessageNumber,
        networkMessageSequenceNumber,
        timestamp,
        messages);
  }
}
