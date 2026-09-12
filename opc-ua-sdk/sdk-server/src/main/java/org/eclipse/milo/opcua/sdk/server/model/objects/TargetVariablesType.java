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
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldTargetDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface TargetVariablesType extends SubscribedDataSetType {
  QualifiedProperty<FieldTargetDataType[]> TARGET_VARIABLES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "TargetVariables",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14744"),
          1,
          FieldTargetDataType[].class);

  /** Gets the existing node's local value. */
  @Nullable FieldTargetDataType @Nullable [] getTargetVariables();

  /** Sets the existing node's local value. */
  void setTargetVariables(@Nullable FieldTargetDataType @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getTargetVariablesNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.2
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getAddTargetVariablesMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.2 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddTargetVariables(MethodBindings bindings, AddTargetVariablesHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.2 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddTargetVariablesDetailed(
      MethodBindings bindings, AddTargetVariablesDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getRemoveTargetVariablesMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.3 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveTargetVariables(
      MethodBindings bindings, RemoveTargetVariablesHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.3 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveTargetVariablesDetailed(
      MethodBindings bindings, RemoveTargetVariablesDetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.2 */
  @FunctionalInterface
  interface AddTargetVariablesHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable StatusCode @Nullable [] invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable ConfigurationVersionDataType configurationVersion,
        @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.2 */
  @FunctionalInterface
  interface AddTargetVariablesDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable StatusCode @Nullable []> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable ConfigurationVersionDataType configurationVersion,
        @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.3 */
  @FunctionalInterface
  interface RemoveTargetVariablesHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable StatusCode @Nullable [] invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable ConfigurationVersionDataType configurationVersion,
        @Nullable UInteger @Nullable [] targetsToRemove)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.3 */
  @FunctionalInterface
  interface RemoveTargetVariablesDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable StatusCode @Nullable []> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable ConfigurationVersionDataType configurationVersion,
        @Nullable UInteger @Nullable [] targetsToRemove)
        throws UaException;
  }
}
