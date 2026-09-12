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

import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodHandlerResult;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.PubSubDiagnosticsCounterType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface PubSubDiagnosticsType extends BaseObjectType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getDiagnosticsLevelNode();

  /** Gets the existing node's local value. */
  @Nullable DiagnosticsLevel getDiagnosticsLevel();

  /** Sets the existing node's local value. */
  void setDiagnosticsLevel(@Nullable DiagnosticsLevel value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PubSubDiagnosticsCounterType getTotalInformationNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getTotalInformation();

  /** Sets the existing node's local value. */
  void setTotalInformation(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PubSubDiagnosticsCounterType getTotalErrorNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getTotalError();

  /** Sets the existing node's local value. */
  void setTotalError(@Nullable UInteger value);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.3
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getResetMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.3 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindReset(MethodBindings bindings, ResetHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.3 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindResetDetailed(MethodBindings bindings, ResetDetailedHandler handler)
      throws UaException;

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSubErrorNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getSubError();

  /** Sets the existing node's local value. */
  void setSubError(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseObjectType getCountersNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseObjectType getLiveValuesNode();

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.3 */
  @FunctionalInterface
  interface ResetHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.3 */
  @FunctionalInterface
  interface ResetDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }
}
