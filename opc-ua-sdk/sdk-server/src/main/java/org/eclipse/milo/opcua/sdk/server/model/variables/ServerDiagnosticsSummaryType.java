/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.8">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.8</a>
 */
public interface ServerDiagnosticsSummaryType extends BaseDataVariableType {
  BaseDataVariableType getServerViewCountNode();

  UInteger getServerViewCount();

  void setServerViewCount(UInteger value);

  BaseDataVariableType getCurrentSessionCountNode();

  UInteger getCurrentSessionCount();

  void setCurrentSessionCount(UInteger value);

  BaseDataVariableType getCumulatedSessionCountNode();

  UInteger getCumulatedSessionCount();

  void setCumulatedSessionCount(UInteger value);

  BaseDataVariableType getSecurityRejectedSessionCountNode();

  UInteger getSecurityRejectedSessionCount();

  void setSecurityRejectedSessionCount(UInteger value);

  BaseDataVariableType getRejectedSessionCountNode();

  UInteger getRejectedSessionCount();

  void setRejectedSessionCount(UInteger value);

  BaseDataVariableType getSessionTimeoutCountNode();

  UInteger getSessionTimeoutCount();

  void setSessionTimeoutCount(UInteger value);

  BaseDataVariableType getSessionAbortCountNode();

  UInteger getSessionAbortCount();

  void setSessionAbortCount(UInteger value);

  BaseDataVariableType getPublishingIntervalCountNode();

  UInteger getPublishingIntervalCount();

  void setPublishingIntervalCount(UInteger value);

  BaseDataVariableType getCurrentSubscriptionCountNode();

  UInteger getCurrentSubscriptionCount();

  void setCurrentSubscriptionCount(UInteger value);

  BaseDataVariableType getCumulatedSubscriptionCountNode();

  UInteger getCumulatedSubscriptionCount();

  void setCumulatedSubscriptionCount(UInteger value);

  BaseDataVariableType getSecurityRejectedRequestsCountNode();

  UInteger getSecurityRejectedRequestsCount();

  void setSecurityRejectedRequestsCount(UInteger value);

  BaseDataVariableType getRejectedRequestsCountNode();

  UInteger getRejectedRequestsCount();

  void setRejectedRequestsCount(UInteger value);
}
