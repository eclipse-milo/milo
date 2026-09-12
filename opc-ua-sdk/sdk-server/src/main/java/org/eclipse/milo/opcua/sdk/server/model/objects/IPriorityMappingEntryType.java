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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.15">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.15</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface IPriorityMappingEntryType extends BaseInterfaceType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMappingUriNode();

  /** Gets the existing node's local value. */
  @Nullable String getMappingUri();

  /** Sets the existing node's local value. */
  void setMappingUri(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getPriorityLabelNode();

  /** Gets the existing node's local value. */
  @Nullable String getPriorityLabel();

  /** Sets the existing node's local value. */
  void setPriorityLabel(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getPriorityValuePcpNode();

  /** Gets the existing node's local value. */
  @Nullable UByte getPriorityValuePcp();

  /** Sets the existing node's local value. */
  void setPriorityValuePcp(@Nullable UByte value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getPriorityValueDscpNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getPriorityValueDscp();

  /** Sets the existing node's local value. */
  void setPriorityValueDscp(@Nullable UInteger value);
}
