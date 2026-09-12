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
import org.eclipse.milo.opcua.sdk.core.model.methods.PublishedDataItemsTypeAddVariablesOutputs;
import org.eclipse.milo.opcua.sdk.core.model.methods.PublishedDataItemsTypeRemoveVariablesOutputs;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedVariableDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface PublishedDataItemsType extends PublishedDataSetType {
  QualifiedProperty<PublishedVariableDataType[]> PUBLISHED_DATA =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PublishedData",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14273"),
          1,
          PublishedVariableDataType[].class);

  /** Gets the existing node's local value. */
  @Nullable PublishedVariableDataType @Nullable [] getPublishedData();

  /** Sets the existing node's local value. */
  void setPublishedData(@Nullable PublishedVariableDataType @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPublishedDataNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.2
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getAddVariablesMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.2 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddVariables(MethodBindings bindings, AddVariablesHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.2 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddVariablesDetailed(
      MethodBindings bindings, AddVariablesDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getRemoveVariablesMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.3 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveVariables(MethodBindings bindings, RemoveVariablesHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.3 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveVariablesDetailed(
      MethodBindings bindings, RemoveVariablesDetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.2 */
  @FunctionalInterface
  interface AddVariablesHandler {
    /**
     * @return a non-null container holding all output values
     * @throws UaException for an operation failure
     */
    PublishedDataItemsTypeAddVariablesOutputs invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable ConfigurationVersionDataType configurationVersion,
        @Nullable String @Nullable [] fieldNameAliases,
        @Nullable Boolean @Nullable [] promotedFields,
        @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.2 */
  @FunctionalInterface
  interface AddVariablesDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<PublishedDataItemsTypeAddVariablesOutputs> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable ConfigurationVersionDataType configurationVersion,
        @Nullable String @Nullable [] fieldNameAliases,
        @Nullable Boolean @Nullable [] promotedFields,
        @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.3 */
  @FunctionalInterface
  interface RemoveVariablesHandler {
    /**
     * @return a non-null container holding all output values
     * @throws UaException for an operation failure
     */
    PublishedDataItemsTypeRemoveVariablesOutputs invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable ConfigurationVersionDataType configurationVersion,
        @Nullable UInteger @Nullable [] variablesToRemove)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.3 */
  @FunctionalInterface
  interface RemoveVariablesDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<PublishedDataItemsTypeRemoveVariablesOutputs> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable ConfigurationVersionDataType configurationVersion,
        @Nullable UInteger @Nullable [] variablesToRemove)
        throws UaException;
  }
}
