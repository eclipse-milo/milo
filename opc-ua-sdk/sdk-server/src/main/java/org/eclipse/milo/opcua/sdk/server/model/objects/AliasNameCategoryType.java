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
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AliasNameDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.AliasNameVerboseDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AliasNameCategoryType extends FolderType {
  QualifiedProperty<UInteger> LAST_CHANGE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastChange",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20998"),
          -1,
          UInteger.class);

  /** Gets the existing node's local value. */
  @Nullable UInteger getLastChange();

  /** Sets the existing node's local value. */
  void setLastChange(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getLastChangeNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.2
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getFindAliasMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.2 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindFindAlias(MethodBindings bindings, FindAliasHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.2 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindFindAliasDetailed(MethodBindings bindings, FindAliasDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getFindAliasVerboseMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.3 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindFindAliasVerbose(MethodBindings bindings, FindAliasVerboseHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.3 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindFindAliasVerboseDetailed(
      MethodBindings bindings, FindAliasVerboseDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getAddAliasesToCategoryMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.4 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddAliasesToCategory(
      MethodBindings bindings, AddAliasesToCategoryHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.4 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddAliasesToCategoryDetailed(
      MethodBindings bindings, AddAliasesToCategoryDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getDeleteAliasesFromCategoryMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.5 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindDeleteAliasesFromCategory(
      MethodBindings bindings, DeleteAliasesFromCategoryHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.5 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindDeleteAliasesFromCategoryDetailed(
      MethodBindings bindings, DeleteAliasesFromCategoryDetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.2 */
  @FunctionalInterface
  interface FindAliasHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable AliasNameDataType @Nullable [] invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String aliasNameSearchPattern,
        @Nullable NodeId referenceTypeFilter)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.2 */
  @FunctionalInterface
  interface FindAliasDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable AliasNameDataType @Nullable []> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String aliasNameSearchPattern,
        @Nullable NodeId referenceTypeFilter)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.3 */
  @FunctionalInterface
  interface FindAliasVerboseHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable AliasNameVerboseDataType @Nullable [] invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String aliasNameSearchPattern,
        @Nullable NodeId referenceTypeFilter)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.3 */
  @FunctionalInterface
  interface FindAliasVerboseDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable AliasNameVerboseDataType @Nullable []> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String aliasNameSearchPattern,
        @Nullable NodeId referenceTypeFilter)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.4 */
  @FunctionalInterface
  interface AddAliasesToCategoryHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable StatusCode @Nullable [] invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String @Nullable [] aliasNames,
        @Nullable ExpandedNodeId @Nullable [] targetNodes,
        @Nullable String @Nullable [] targetServers,
        @Nullable NodeId targetReferenceType)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.4 */
  @FunctionalInterface
  interface AddAliasesToCategoryDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable StatusCode @Nullable []> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String @Nullable [] aliasNames,
        @Nullable ExpandedNodeId @Nullable [] targetNodes,
        @Nullable String @Nullable [] targetServers,
        @Nullable NodeId targetReferenceType)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.5 */
  @FunctionalInterface
  interface DeleteAliasesFromCategoryHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable StatusCode @Nullable [] invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String @Nullable [] aliasNames,
        @Nullable ExpandedNodeId @Nullable [] targetNodes)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.5 */
  @FunctionalInterface
  interface DeleteAliasesFromCategoryDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable StatusCode @Nullable []> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String @Nullable [] aliasNames,
        @Nullable ExpandedNodeId @Nullable [] targetNodes)
        throws UaException;
  }
}
