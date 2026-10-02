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

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.access.AccessController.AccessResult;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** The check shared by the built-in policies: group by Session, check, collect. */
final class ReadAccessPolicies {

  private static final Logger logger = LoggerFactory.getLogger(ReadAccessPolicies.class);

  private ReadAccessPolicies() {}

  /**
   * Check each Session's items with {@code check} and collect the results by item.
   *
   * <p>Items are grouped by the Session they belong to now, and each group is checked for that
   * Session. An item without a Session, or whose Session has closed, is skipped. A check that
   * throws is logged and leaves its Session's items out of the result; the other Sessions are still
   * checked.
   */
  static Map<DataItem, AccessResult> check(
      OpcUaServer server,
      List<DataItem> items,
      BiFunction<Session, List<ReadValueId>, Map<ReadValueId, AccessResult>> check) {

    var results = new IdentityHashMap<DataItem, AccessResult>(items.size());

    groupBySession(items)
        .forEach(
            (session, sessionItems) -> {
              List<ReadValueId> readValueIds =
                  sessionItems.stream().map(MonitoredItem::getReadValueId).toList();

              Map<ReadValueId, AccessResult> bySessionItem;
              try {
                bySessionItem = check.apply(session, readValueIds);
              } catch (Exception e) {
                logger.warn(
                    "Read access check failed for Session {}: {}",
                    session.getSessionId(),
                    e.getMessage(),
                    e);
                return;
              }

              for (DataItem item : sessionItems) {
                AccessResult result = bySessionItem.get(item.getReadValueId());

                if (result != null) {
                  results.put(item, result);
                }
              }
            });

    return results;
  }

  private static Map<Session, List<DataItem>> groupBySession(List<DataItem> items) {
    var bySession = new LinkedHashMap<Session, List<DataItem>>();

    for (DataItem item : items) {
      Session session = item.getSession();

      if (session != null && !session.isClosed()) {
        bySession.computeIfAbsent(session, s -> new ArrayList<>()).add(item);
      }
    }

    return bySession;
  }
}
