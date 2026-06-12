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

/**
 * The result of decoding one UADP NetworkMessage with {@link
 * UadpMessageMapping#decodeMessage(DecodeContext, io.netty.buffer.ByteBuf)}: exactly one of the
 * permitted types, selected by the NetworkMessage type bits in ExtendedFlags2 (OPC UA Part 14
 * §7.2.4.4.2).
 *
 * <ul>
 *   <li>{@link DecodedNetworkMessage} — a data-plane NetworkMessage carrying DataSetMessages. Also
 *       returned (header-only, with empty {@code messages()} and {@code metaData()}) for discovery
 *       content that is tolerated but not surfaced: FindApplications probes, probe InformationTypes
 *       other than DataSetMetaData, announcement types other than DataSetMetaData, and chunked
 *       discovery NetworkMessages.
 *   <li>{@link UadpDiscoveryProbe} — a Publisher information probe requesting DataSetMetaData (Part
 *       14 §7.2.4.6.12).
 *   <li>{@link UadpMetaDataAnnouncement} — a DataSetMetaData discovery announcement (Part 14
 *       §7.2.4.6.4), surfaced regardless of its StatusCode; a Bad status is a denial.
 * </ul>
 *
 * <p>This interface is sealed; adding a permitted implementation is a source-compatible change only
 * for callers that switch over it with a {@code default} branch.
 */
public sealed interface UadpDecodedMessage
    permits DecodedNetworkMessage, UadpDiscoveryProbe, UadpMetaDataAnnouncement {}
