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
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnFailureCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnListenerStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnTalkerStatus;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.9">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.9</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface IIeeeBaseTsnStatusStreamType extends BaseInterfaceType {
  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getTalkerStatusNode();

  /** Gets the existing node's local value. */
  @Nullable TsnTalkerStatus getTalkerStatus();

  /** Sets the existing node's local value. */
  void setTalkerStatus(@Nullable TsnTalkerStatus value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getListenerStatusNode();

  /** Gets the existing node's local value. */
  @Nullable TsnListenerStatus getListenerStatus();

  /** Sets the existing node's local value. */
  void setListenerStatus(@Nullable TsnListenerStatus value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getFailureCodeNode();

  /** Gets the existing node's local value. */
  @Nullable TsnFailureCode getFailureCode();

  /** Sets the existing node's local value. */
  void setFailureCode(@Nullable TsnFailureCode value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getFailureSystemIdentifierNode();

  /** Gets the existing node's local value. */
  @Nullable Object getFailureSystemIdentifier();

  /** Sets the existing node's local value. */
  void setFailureSystemIdentifier(@Nullable Object value);
}
