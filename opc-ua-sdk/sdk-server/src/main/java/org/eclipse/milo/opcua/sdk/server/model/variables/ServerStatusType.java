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

import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.6">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.6</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ServerStatusType extends BaseDataVariableType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getStartTimeNode();

  /** Gets the existing node's local value. */
  @Nullable DateTime getStartTime();

  /** Sets the existing node's local value. */
  void setStartTime(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCurrentTimeNode();

  /** Gets the existing node's local value. */
  @Nullable DateTime getCurrentTime();

  /** Sets the existing node's local value. */
  void setCurrentTime(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getStateNode();

  /** Gets the existing node's local value. */
  @Nullable ServerState getState();

  /** Sets the existing node's local value. */
  void setState(@Nullable ServerState value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BuildInfoType getBuildInfoNode();

  /** Gets the existing node's local value. */
  @Nullable BuildInfo getBuildInfo();

  /** Sets the existing node's local value. */
  void setBuildInfo(@Nullable BuildInfo value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSecondsTillShutdownNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getSecondsTillShutdown();

  /** Sets the existing node's local value. */
  void setSecondsTillShutdown(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getShutdownReasonNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getShutdownReason();

  /** Sets the existing node's local value. */
  void setShutdownReason(@Nullable LocalizedText value);
}
