/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub;

import java.util.Map;
import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * Diagnostics view of a {@link PubSubService}: immutable per-component counter snapshots, keyed by
 * component path.
 *
 * <p>Counters that do not apply to a component type, e.g. {@code networkMessagesReceived} on a
 * WriterGroup, remain zero.
 */
public interface PubSubDiagnostics {

  /**
   * Get an immutable snapshot of the diagnostics of every component, keyed by component path, e.g.
   * {@code "conn/group/writer"}.
   *
   * @return an immutable snapshot of the diagnostics of every component.
   */
  Map<String, ComponentDiagnostics> snapshot();

  /**
   * Get an immutable snapshot of the diagnostics of the component at {@code path}.
   *
   * @param path the path of the component, e.g. {@code "conn/group/writer"}.
   * @return the component diagnostics, or empty if no component exists at {@code path}.
   */
  default Optional<ComponentDiagnostics> component(String path) {
    return Optional.ofNullable(snapshot().get(path));
  }

  /**
   * An immutable snapshot of the diagnostic counters of one PubSub component.
   *
   * @param path the path of the component, e.g. {@code "conn/group/writer"}.
   * @param networkMessagesSent the number of NetworkMessages sent.
   * @param networkMessagesReceived the number of NetworkMessages received.
   * @param dataSetMessagesSent the number of DataSetMessages sent.
   * @param dataSetMessagesReceived the number of DataSetMessages received.
   * @param decodeErrors the number of messages dropped because they could not be decoded.
   * @param sourceErrors the number of {@link PublishedDataSetSource} read failures.
   * @param lastError the status code of the most recent error, or {@code null} if no error has
   *     occurred.
   */
  record ComponentDiagnostics(
      String path,
      long networkMessagesSent,
      long networkMessagesReceived,
      long dataSetMessagesSent,
      long dataSetMessagesReceived,
      long decodeErrors,
      long sourceErrors,
      @Nullable StatusCode lastError) {}
}
