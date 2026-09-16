/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.methods;

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.NullMarked;

/**
 * Owns one ObjectId-specific Method registration.
 *
 * <p>Close the token when removing its owner node. Closing is idempotent and removes only this
 * registration, so an old token cannot unregister its replacement. A callback selected before close
 * may finish afterward.
 */
@NullMarked
public interface MethodBinding extends AutoCloseable {
  /**
   * @return the invocation owner's NodeId.
   */
  NodeId objectId();

  /**
   * @return the shared Method's NodeId.
   */
  NodeId methodId();

  /** Release this registration without waiting for selected invocations to finish. */
  @Override
  void close();
}
