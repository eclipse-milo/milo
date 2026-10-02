/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.sampling;

import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.access.AccessController.AccessResult;
import org.eclipse.milo.opcua.sdk.server.access.ReadAccessCache;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;

/** Answers from the server's {@link ReadAccessCache}, checking only the misses. */
final class CachedReadAccessPolicy implements ReadAccessPolicy {

  static final ReadAccessPolicy INSTANCE = new CachedReadAccessPolicy();

  private CachedReadAccessPolicy() {}

  @Override
  public Map<DataItem, AccessResult> check(OpcUaServer server, List<DataItem> items) {
    return ReadAccessPolicies.check(
        server,
        items,
        (session, readValueIds) ->
            server
                .getAccessControlManager()
                .getReadAccessCache()
                .getOrCheck(session, readValueIds));
  }

  @Override
  public String toString() {
    return "ReadAccessPolicy.cached()";
  }
}
