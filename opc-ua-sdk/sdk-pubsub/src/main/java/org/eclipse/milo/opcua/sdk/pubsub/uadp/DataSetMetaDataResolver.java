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

import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.jspecify.annotations.Nullable;

/**
 * Resolves the effective DataSetMetaData describing a DataSetMessage while it is being decoded, so
 * mappings whose wire form is not self-describing can decode field values against their declared
 * types (Part 14 §6.2.3: the DataSetMetaData carries "the information necessary to decode
 * DataSetMessages").
 *
 * <p>Like {@link SecurityContextResolver}, resolution happens by plaintext wire identity BEFORE
 * reader matching, because decode runs once per (connection, mapping): the resolver is consulted
 * with whatever values the message carries, and any of them may be absent on the wire (collapsed
 * JSON layouts, disabled UADP payload headers).
 *
 * <p>Version checking is the implementation's responsibility: the caller passes the transmitted
 * metadata version through unexamined and uses whatever the resolver returns. Returning {@code
 * null} means no usable metadata is known for the request; the mapping falls back to its
 * self-describing or shape-based decode.
 */
@FunctionalInterface
public interface DataSetMetaDataResolver {

  /**
   * Resolve the effective metadata for one DataSetMessage.
   *
   * @param request the wire values of the DataSetMessage being decoded.
   * @return the effective metadata, or {@code null} if none is known (or none is usable) for the
   *     request.
   */
  @Nullable DataSetMetaDataConfig resolve(ResolveRequest request);

  /**
   * The wire values available to metadata resolution.
   *
   * @param publisherId the PublisherId carried by the NetworkMessage or DataSetMessage, or {@code
   *     null} when the message carries none.
   * @param dataSetWriterId the DataSetWriterId carried by the DataSetMessage, or {@code null} when
   *     the message carries none.
   * @param configurationVersion the metadata version transmitted by the DataSetMessage, or {@code
   *     null} when the message carries none; implementations refuse metadata whose major version
   *     mismatches a transmitted, checkable major version (a VersionTime of 0 means "not used" per
   *     Part 14 and cannot be checked).
   */
  record ResolveRequest(
      @Nullable PublisherId publisherId,
      @Nullable UShort dataSetWriterId,
      @Nullable ConfigurationVersionDataType configurationVersion) {}
}
