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

import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;

/**
 * A UADP DataSetMetaData discovery announcement (OPC UA Part 14 §7.2.4.6.4, Table 170): a
 * Publisher's metadata for one DataSetWriter, sent in response to a {@link UadpDiscoveryProbe} or
 * unsolicited when the metadata changes.
 *
 * <p>The PublisherId is the <b>sender's</b> own id — the same value (and DataType) the Publisher
 * uses in its data-plane NetworkMessages for the connection. The sequence number is a separate
 * UInt16 counter incremented per announcement in the scope of a PublisherId, independent of all
 * data-plane sequence numbers.
 *
 * <p>A Bad {@code statusCode} is a denial: the Publisher cannot provide metadata for the requested
 * DataSetWriterId. The {@code metaData} structure is present on the wire either way. Per
 * §7.2.4.6.4, the ConfigurationVersion in {@code metaData} shall match the ConfigurationVersion in
 * the DataSetMessage headers of the corresponding writer; supplying a consistent value is the
 * caller's responsibility when encoding.
 *
 * @param publisherId the id of the announcing Publisher, carried as the NetworkMessage header
 *     PublisherId.
 * @param sequenceNumber the per-PublisherId announcement sequence number (Table 168).
 * @param dataSetWriterId the id of the DataSetWriter the metadata describes; one writer per
 *     announcement.
 * @param metaData the announced metadata, encoded inline (no ExtensionObject wrapper).
 * @param statusCode the capability of the Publisher to provide metadata for the DataSetWriterId; a
 *     Bad status is a denial.
 * @apiNote Create instances via {@link #of(PublisherId, UShort, UShort, DataSetMetaDataType,
 *     StatusCode)} rather than the canonical constructor; the factory methods are stable while the
 *     canonical constructor is not.
 */
public record UadpMetaDataAnnouncement(
    PublisherId publisherId,
    UShort sequenceNumber,
    UShort dataSetWriterId,
    DataSetMetaDataType metaData,
    StatusCode statusCode)
    implements UadpDecodedMessage {

  /**
   * Create a {@link UadpMetaDataAnnouncement}.
   *
   * @param publisherId the id of the announcing Publisher.
   * @param sequenceNumber the per-PublisherId announcement sequence number.
   * @param dataSetWriterId the id of the DataSetWriter the metadata describes.
   * @param metaData the announced metadata.
   * @param statusCode the capability of the Publisher to provide metadata for the DataSetWriterId;
   *     a Bad status is a denial.
   * @return a new {@link UadpMetaDataAnnouncement}.
   */
  public static UadpMetaDataAnnouncement of(
      PublisherId publisherId,
      UShort sequenceNumber,
      UShort dataSetWriterId,
      DataSetMetaDataType metaData,
      StatusCode statusCode) {

    return new UadpMetaDataAnnouncement(
        publisherId, sequenceNumber, dataSetWriterId, metaData, statusCode);
  }
}
