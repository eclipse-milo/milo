/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.diagnostics;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.concurrent.atomic.LongAdder;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.subscriptions.Subscription;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerDiagnosticsSummaryDataType;

public class ServerDiagnosticsSummary {

  private final LongAdder cumulatedSessionCount = new LongAdder();
  private final LongAdder securityRejectedSessionCount = new LongAdder();
  private final LongAdder rejectedSessionCount = new LongAdder();
  private final LongAdder sessionTimeoutCount = new LongAdder();
  private final LongAdder sessionAbortCount = new LongAdder();
  private final LongAdder cumulatedSubscriptionCount = new LongAdder();

  private final LongAdder securityRejectedRequestCount = new LongAdder();

  private final LongAdder rejectedRequestCount = new LongAdder();

  private final OpcUaServer server;

  public ServerDiagnosticsSummary(OpcUaServer server) {
    this.server = server;
  }

  /**
   * @return the number of server-created views in the server.
   */
  public UInteger getCurrentViewCount() {
    return server.getAddressSpaceManager().getViewCount();
  }

  /**
   * @return the number of sessions currently established in the server.
   */
  public UInteger getCurrentSessionCount() {
    return server.getSessionManager().getCurrentSessionCount();
  }

  /**
   * @return the cumulative number of client sessions that have been established in the server since
   *     the server was started (or restarted). This includes the current session count.
   */
  public LongAdder getCumulatedSessionCount() {
    return cumulatedSessionCount;
  }

  /**
   * @return the number of client session establishment requests (ActivateSession and CreateSession)
   *     that were rejected due to security constraints since the server was started (or restarted).
   */
  public LongAdder getSecurityRejectedSessionCount() {
    return securityRejectedSessionCount;
  }

  /**
   * @return the number of client session establishment requests (ActivateSession and CreateSession)
   *     that were rejected since the server was started (or restarted). This number includes the
   *     security rejected session count.
   */
  public LongAdder getRejectedSessionCount() {
    return rejectedSessionCount;
  }

  /**
   * @return the number of client sessions that were closed due to timeout since the server was
   *     started (or restarted).
   */
  public LongAdder getSessionTimeoutCount() {
    return sessionTimeoutCount;
  }

  /**
   * @return the number of client sessions that were closed due to errors since the server was
   *     started (or restarted).
   */
  public LongAdder getSessionAbortCount() {
    return sessionAbortCount;
  }

  /**
   * @return the number of subscriptions currently established in the server.
   */
  public UInteger getCurrentSubscriptionCount() {
    return uint(server.getSubscriptions().size());
  }

  /**
   * @return the cumulative number of subscriptions that have been established in the server since
   *     the server was started (or restarted). This includes the current subscription count.
   * @see #getCurrentSubscriptionCount()
   */
  public LongAdder getCumulatedSubscriptionCount() {
    return cumulatedSubscriptionCount;
  }

  /**
   * @return the number of publishing intervals currently supported in the server.
   */
  public UInteger getPublishingIntervalCount() {
    return uint(
        server.getSubscriptions().values().stream()
            .map(Subscription::getPublishingInterval)
            .distinct()
            .count());
  }

  /**
   * @return the number of requests that were rejected due to security constraints since the server
   *     was started (or restarted). The requests include all Services defined in Part 4, and also
   *     requests to create sessions.
   */
  public UInteger getSecurityRejectedRequestCount() {
    return uint(securityRejectedRequestCount.sum());
  }

  /**
   * @return the number of requests that were rejected since the server was started (or restarted).
   *     The requests include all Services defined in Part 4, and also requests to create sessions.
   *     This number includes the security rejected requests count.
   * @see #getSecurityRejectedRequestCount()
   */
  public UInteger getRejectedRequestCount() {
    return uint(rejectedRequestCount.sum());
  }

  /**
   * @return a {@link ServerDiagnosticsSummaryDataType} containing the most current values.
   */
  public ServerDiagnosticsSummaryDataType getServerDiagnosticsSummaryDataType() {
    return new ServerDiagnosticsSummaryDataType(
        getCurrentViewCount(),
        getCurrentSessionCount(),
        uint(getCumulatedSessionCount().sum()),
        uint(getSecurityRejectedSessionCount().sum()),
        uint(getRejectedSessionCount().sum()),
        uint(getSessionTimeoutCount().sum()),
        uint(getSessionAbortCount().sum()),
        getCurrentSubscriptionCount(),
        uint(getCumulatedSubscriptionCount().sum()),
        getPublishingIntervalCount(),
        getSecurityRejectedRequestCount(),
        getRejectedRequestCount());
  }
}
