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

/**
 * Context for {@link MessageMappingProvider#decode(DecodeContext, io.netty.buffer.ByteBuf)}.
 *
 * <p>Future versions (e.g. JSON mapping, broker transports) will add components to this record via
 * new {@code of(...)} factory overloads; the canonical constructor may change incompatibly when
 * they do.
 *
 * @param encodingContext the {@link EncodingContext} used to decode field values.
 * @apiNote Create instances via {@link #of(EncodingContext)} rather than the canonical constructor;
 *     the factory methods are stable while the canonical constructor is not.
 */
public record DecodeContext(EncodingContext encodingContext) {

  /**
   * Create a {@link DecodeContext}.
   *
   * @param encodingContext the {@link EncodingContext} used to decode field values.
   * @return a new {@link DecodeContext}.
   */
  public static DecodeContext of(EncodingContext encodingContext) {
    return new DecodeContext(encodingContext);
  }
}
