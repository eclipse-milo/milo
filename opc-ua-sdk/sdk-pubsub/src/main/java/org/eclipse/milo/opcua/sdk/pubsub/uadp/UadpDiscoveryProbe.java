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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

/**
 * A UADP discovery probe (OPC UA Part 14 §7.2.4.6.12): a Subscriber's request for discovery
 * information from one Publisher.
 *
 * <p>The PublisherId in a probe's NetworkMessage header identifies the <b>probed</b> Publisher, not
 * the sender ({@code targetPublisherId} here); a Publisher receiving a probe whose PublisherId is
 * not its own ignores it. Probes carry no sequence number.
 *
 * <p>Only the Publisher information probe requesting DataSetMetaData ({@code probeType} {@link
 * #PROBE_TYPE_PUBLISHER_INFORMATION}, {@code informationType} {@link
 * #INFORMATION_TYPE_DATA_SET_META_DATA}, Tables 178-180) is supported by the codec: it is the only
 * combination the decoder surfaces and the encoder accepts. The Publisher answers with one {@link
 * UadpMetaDataAnnouncement} per requested DataSetWriterId; an empty {@code dataSetWriterIds} list
 * requests nothing.
 *
 * @param probeType the ProbeType header value (Table 178); always {@link
 *     #PROBE_TYPE_PUBLISHER_INFORMATION} for instances produced by the decoder.
 * @param informationType the InformationType value (Table 179); always {@link
 *     #INFORMATION_TYPE_DATA_SET_META_DATA} for instances produced by the decoder.
 * @param targetPublisherId the id of the probed Publisher, carried as the NetworkMessage header
 *     PublisherId.
 * @param dataSetWriterIds the ids of the DataSetWriters the information is requested for (Table
 *     180); possibly empty.
 * @apiNote Create instances via the {@code of(...)} factories rather than the canonical
 *     constructor; the factory methods are stable while the canonical constructor is not.
 */
public record UadpDiscoveryProbe(
    int probeType,
    int informationType,
    PublisherId targetPublisherId,
    List<UShort> dataSetWriterIds)
    implements UadpDecodedMessage {

  /** ProbeType value of the Publisher information probe (Part 14 §7.2.4.6.12.3, Table 178). */
  public static final int PROBE_TYPE_PUBLISHER_INFORMATION = 1;

  /** InformationType value requesting DataSetMetaData (Part 14 §7.2.4.6.12.4, Table 179). */
  public static final int INFORMATION_TYPE_DATA_SET_META_DATA = 2;

  /**
   * Create a new {@link UadpDiscoveryProbe}.
   *
   * @param probeType the ProbeType header value (Table 178).
   * @param informationType the InformationType value (Table 179).
   * @param targetPublisherId the id of the probed Publisher.
   * @param dataSetWriterIds the ids of the DataSetWriters the information is requested for;
   *     possibly empty.
   */
  public UadpDiscoveryProbe {
    dataSetWriterIds = List.copyOf(dataSetWriterIds);
  }

  /**
   * Create a {@link UadpDiscoveryProbe}.
   *
   * @param probeType the ProbeType header value (Table 178).
   * @param informationType the InformationType value (Table 179).
   * @param targetPublisherId the id of the probed Publisher.
   * @param dataSetWriterIds the ids of the DataSetWriters the information is requested for;
   *     possibly empty.
   * @return a new {@link UadpDiscoveryProbe}.
   */
  public static UadpDiscoveryProbe of(
      int probeType,
      int informationType,
      PublisherId targetPublisherId,
      List<UShort> dataSetWriterIds) {

    return new UadpDiscoveryProbe(probeType, informationType, targetPublisherId, dataSetWriterIds);
  }

  /**
   * Create a DataSetMetaData {@link UadpDiscoveryProbe}, the only probe kind the codec encodes:
   * ProbeType {@link #PROBE_TYPE_PUBLISHER_INFORMATION}, InformationType {@link
   * #INFORMATION_TYPE_DATA_SET_META_DATA}.
   *
   * @param targetPublisherId the id of the probed Publisher.
   * @param dataSetWriterIds the ids of the DataSetWriters metadata is requested for; possibly
   *     empty.
   * @return a new {@link UadpDiscoveryProbe}.
   */
  public static UadpDiscoveryProbe of(
      PublisherId targetPublisherId, List<UShort> dataSetWriterIds) {

    return new UadpDiscoveryProbe(
        PROBE_TYPE_PUBLISHER_INFORMATION,
        INFORMATION_TYPE_DATA_SET_META_DATA,
        targetPublisherId,
        dataSetWriterIds);
  }
}
