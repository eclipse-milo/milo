/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnStreamState;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.7">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.7</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface IIeeeBaseTsnStreamType extends BaseInterfaceType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getStreamIdNode();

  /** Gets the existing node's local value. */
  @Nullable UByte @Nullable [] getStreamId();

  /** Sets the existing node's local value. */
  void setStreamId(@Nullable UByte @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getStreamNameNode();

  /** Gets the existing node's local value. */
  @Nullable String getStreamName();

  /** Sets the existing node's local value. */
  void setStreamName(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getStateNode();

  /** Gets the existing node's local value. */
  @Nullable TsnStreamState getState();

  /** Sets the existing node's local value. */
  void setState(@Nullable TsnStreamState value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getAccumulatedLatencyNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getAccumulatedLatency();

  /** Sets the existing node's local value. */
  void setAccumulatedLatency(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getSrClassIdNode();

  /** Gets the existing node's local value. */
  @Nullable UByte getSrClassId();

  /** Sets the existing node's local value. */
  void setSrClassId(@Nullable UByte value);
}
