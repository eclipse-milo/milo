/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.8">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.8</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ServerDiagnosticsSummaryType extends BaseDataVariableType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getServerViewCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getServerViewCount();

  /** Sets the existing node's local value. */
  void setServerViewCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCurrentSessionCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getCurrentSessionCount();

  /** Sets the existing node's local value. */
  void setCurrentSessionCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCumulatedSessionCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getCumulatedSessionCount();

  /** Sets the existing node's local value. */
  void setCumulatedSessionCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSecurityRejectedSessionCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getSecurityRejectedSessionCount();

  /** Sets the existing node's local value. */
  void setSecurityRejectedSessionCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getRejectedSessionCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getRejectedSessionCount();

  /** Sets the existing node's local value. */
  void setRejectedSessionCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSessionTimeoutCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getSessionTimeoutCount();

  /** Sets the existing node's local value. */
  void setSessionTimeoutCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSessionAbortCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getSessionAbortCount();

  /** Sets the existing node's local value. */
  void setSessionAbortCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getPublishingIntervalCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getPublishingIntervalCount();

  /** Sets the existing node's local value. */
  void setPublishingIntervalCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCurrentSubscriptionCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getCurrentSubscriptionCount();

  /** Sets the existing node's local value. */
  void setCurrentSubscriptionCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCumulatedSubscriptionCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getCumulatedSubscriptionCount();

  /** Sets the existing node's local value. */
  void setCumulatedSubscriptionCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSecurityRejectedRequestsCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getSecurityRejectedRequestsCount();

  /** Sets the existing node's local value. */
  void setSecurityRejectedRequestsCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getRejectedRequestsCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getRejectedRequestsCount();

  /** Sets the existing node's local value. */
  void setRejectedRequestsCount(@Nullable UInteger value);
}
