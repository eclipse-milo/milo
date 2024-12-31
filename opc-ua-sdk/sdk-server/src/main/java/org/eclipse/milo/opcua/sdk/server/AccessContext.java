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

import java.util.Optional;

public interface AccessContext {

  AccessContext INTERNAL = Optional::empty;

  /**
   * Get the {@link Session} associated with this operation, if present.
   *
   * <p>If empty, the operation is internal and no user- or session-related restrictions should be
   * applied.
   *
   * @return the {@link Session} associated with this operation, if present.
   */
  Optional<Session> getSession();
}
