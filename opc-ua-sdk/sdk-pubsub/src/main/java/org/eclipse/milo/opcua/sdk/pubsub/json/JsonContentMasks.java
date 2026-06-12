/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.json;

import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonEncoder;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonNetworkMessageContentMask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Effective JSON content mask resolution.
 *
 * <p>Empty configured masks (the config default) are replaced by the recommended defaults of Part
 * 14 §7.2.5.4.2; non-empty masks are honored exactly, except that the deprecated field encodings of
 * Table 112 (OPC 10000-6 Annex H) are upgraded to their modern equivalents on encode:
 * NonReversibleEncoding (FieldEncoding1=0, FieldEncoding2=0) becomes VerboseEncoding and
 * ReversibleFieldEncoding (FieldEncoding1=1, FieldEncoding2=0) becomes CompactEncoding, each with a
 * one-time WARN.
 *
 * <p>The effective-mask methods are public because the engine consults the same resolution outside
 * the mapping: startup/reconfigure validation and the writer runtime's delta-frame capability check
 * (a JSON delta frame requires the DataSetMessageHeader and the MessageType member, Part 14 Annex
 * A.3.3.4/A.3.4.4).
 */
public final class JsonContentMasks {

  private static final Logger LOGGER = LoggerFactory.getLogger(JsonContentMasks.class);

  private static final AtomicBoolean NON_REVERSIBLE_UPGRADE_WARNED = new AtomicBoolean(false);
  private static final AtomicBoolean REVERSIBLE_UPGRADE_WARNED = new AtomicBoolean(false);

  /**
   * The effective JsonNetworkMessageContentMask used when the configured mask is empty:
   * NetworkMessageHeader | DataSetMessageHeader | PublisherId.
   */
  static final JsonNetworkMessageContentMask DEFAULT_NETWORK_MESSAGE_MASK =
      JsonNetworkMessageContentMask.of(
          JsonNetworkMessageContentMask.Field.NetworkMessageHeader,
          JsonNetworkMessageContentMask.Field.DataSetMessageHeader,
          JsonNetworkMessageContentMask.Field.PublisherId);

  /**
   * The effective JsonDataSetMessageContentMask used when the configured mask is empty:
   * DataSetWriterId | SequenceNumber | MetaDataVersion | Timestamp | Status | FieldEncoding2 (=
   * VerboseEncoding, the recommended default of §7.2.5.4.2).
   */
  static final JsonDataSetMessageContentMask DEFAULT_DATA_SET_MESSAGE_MASK =
      JsonDataSetMessageContentMask.of(
          JsonDataSetMessageContentMask.Field.DataSetWriterId,
          JsonDataSetMessageContentMask.Field.SequenceNumber,
          JsonDataSetMessageContentMask.Field.MetaDataVersion,
          JsonDataSetMessageContentMask.Field.Timestamp,
          JsonDataSetMessageContentMask.Field.Status,
          JsonDataSetMessageContentMask.Field.FieldEncoding2);

  private JsonContentMasks() {}

  /**
   * The effective NetworkMessage content mask: the configured mask, or the default when empty.
   *
   * @param configured the configured {@link JsonNetworkMessageContentMask}.
   * @return the effective {@link JsonNetworkMessageContentMask}.
   */
  public static JsonNetworkMessageContentMask effectiveNetworkMessageMask(
      JsonNetworkMessageContentMask configured) {

    if (configured.getValue().longValue() == 0L) {
      return DEFAULT_NETWORK_MESSAGE_MASK;
    } else {
      return configured;
    }
  }

  /**
   * The effective DataSetMessage content mask: the configured mask, or the default when empty.
   *
   * @param configured the configured {@link JsonDataSetMessageContentMask}.
   * @return the effective {@link JsonDataSetMessageContentMask}.
   */
  public static JsonDataSetMessageContentMask effectiveDataSetMessageMask(
      JsonDataSetMessageContentMask configured) {

    if (configured.getValue().longValue() == 0L) {
      return DEFAULT_DATA_SET_MESSAGE_MASK;
    } else {
      return configured;
    }
  }

  /**
   * Resolve the field encoding selected by {@code effectiveMask} per Table 112, upgrading the
   * deprecated encodings to their modern equivalents with a one-time WARN.
   *
   * @param effectiveMask the effective DataSetMessage content mask, see {@link
   *     #effectiveDataSetMessageMask(JsonDataSetMessageContentMask)}.
   * @return the {@link OpcUaJsonEncoder.Encoding} to encode DataSet field values with.
   */
  static OpcUaJsonEncoder.Encoding resolveFieldEncoding(
      JsonDataSetMessageContentMask effectiveMask) {

    boolean fieldEncoding1 = effectiveMask.getFieldEncoding1();
    boolean fieldEncoding2 = effectiveMask.getFieldEncoding2();

    if (fieldEncoding2) {
      return fieldEncoding1 ? OpcUaJsonEncoder.Encoding.COMPACT : OpcUaJsonEncoder.Encoding.VERBOSE;
    } else if (fieldEncoding1) {
      if (REVERSIBLE_UPGRADE_WARNED.compareAndSet(false, true)) {
        LOGGER.warn(
            "deprecated JSON ReversibleFieldEncoding selected"
                + " (FieldEncoding1=1, FieldEncoding2=0); encoding with CompactEncoding instead"
                + " (OPC 10000-6 Annex H.1)");
      }
      return OpcUaJsonEncoder.Encoding.COMPACT;
    } else {
      if (NON_REVERSIBLE_UPGRADE_WARNED.compareAndSet(false, true)) {
        LOGGER.warn(
            "deprecated JSON NonReversibleEncoding selected"
                + " (FieldEncoding1=0, FieldEncoding2=0); encoding with VerboseEncoding instead"
                + " (OPC 10000-6 Annex H.1)");
      }
      return OpcUaJsonEncoder.Encoding.VERBOSE;
    }
  }
}
