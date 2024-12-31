/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import java.util.List;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;

/**
 * Continuation point used to store excess references from a Browse service call for use in
 * subsequent BrowseNext service call.
 *
 * @param id a unique identifier for this continuation point.
 * @param references the references that were not returned in the initial Browse service call.
 * @param max the maximum number of references specified in the original Browse service call.
 */
public record ContinuationPoint(ByteString id, List<ReferenceDescription> references, int max) {

  public ContinuationPoint(List<ReferenceDescription> references, int max) {
    this(NonceUtil.generateNonce(16), references, max);
  }
}
