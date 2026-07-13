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

import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.jspecify.annotations.Nullable;

/**
 * Context for {@link MessageMappingProvider#decode(DecodeContext, io.netty.buffer.ByteBuf)}.
 *
 * <p>Future versions (e.g. broker transports) will add components to this record via new {@code
 * of(...)} factory overloads; the canonical constructor may change incompatibly when they do.
 *
 * @param encodingContext the {@link EncodingContext} used to decode field values.
 * @param securityResolver resolves key material for received secured NetworkMessages, or {@code
 *     null} if the receiver has no key infrastructure: secured messages are then skipped with
 *     outcome {@link SecurityOutcome#NO_RESOLVER}.
 * @param metaDataResolver resolves the effective DataSetMetaData for received DataSetMessages so
 *     field values can be decoded against their declared types, or {@code null} if no metadata
 *     infrastructure is available: mappings then fall back to self-describing or shape-based field
 *     decoding.
 * @apiNote Create instances via {@link #of(EncodingContext)} rather than the canonical constructor;
 *     the factory methods are stable while the canonical constructor is not.
 */
public record DecodeContext(
    EncodingContext encodingContext,
    @Nullable SecurityContextResolver securityResolver,
    @Nullable DataSetMetaDataResolver metaDataResolver) {

  /**
   * Create a {@link DecodeContext} without a security resolver or metadata resolver.
   *
   * @param encodingContext the {@link EncodingContext} used to decode field values.
   * @return a new {@link DecodeContext}.
   */
  public static DecodeContext of(EncodingContext encodingContext) {
    return of(encodingContext, null);
  }

  /**
   * Create a {@link DecodeContext} without a metadata resolver.
   *
   * @param encodingContext the {@link EncodingContext} used to decode field values.
   * @param securityResolver resolves key material for received secured NetworkMessages, or {@code
   *     null} if the receiver has no key infrastructure.
   * @return a new {@link DecodeContext}.
   */
  public static DecodeContext of(
      EncodingContext encodingContext, @Nullable SecurityContextResolver securityResolver) {
    return new DecodeContext(encodingContext, securityResolver, null);
  }

  /**
   * Create a {@link DecodeContext}.
   *
   * @param encodingContext the {@link EncodingContext} used to decode field values.
   * @param securityResolver resolves key material for received secured NetworkMessages, or {@code
   *     null} if the receiver has no key infrastructure.
   * @param metaDataResolver resolves the effective DataSetMetaData for received DataSetMessages, or
   *     {@code null} if no metadata infrastructure is available.
   * @return a new {@link DecodeContext}.
   */
  public static DecodeContext of(
      EncodingContext encodingContext,
      @Nullable SecurityContextResolver securityResolver,
      @Nullable DataSetMetaDataResolver metaDataResolver) {
    return new DecodeContext(encodingContext, securityResolver, metaDataResolver);
  }
}
