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
import org.eclipse.milo.opcua.sdk.core.model.methods.SecurityGroupFolderTypeAddSecurityGroupOutputs;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface SecurityGroupFolderType extends FolderType {
  QualifiedProperty<String[]> SUPPORTED_SECURITY_POLICY_URIS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SupportedSecurityPolicyUris",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          1,
          String[].class);

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getSupportedSecurityPolicyUris();

  /** Sets the existing node's local value. */
  void setSupportedSecurityPolicyUris(@Nullable String @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSupportedSecurityPolicyUrisNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.2
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getAddSecurityGroupMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.2 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddSecurityGroup(MethodBindings bindings, AddSecurityGroupHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.2 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddSecurityGroupDetailed(
      MethodBindings bindings, AddSecurityGroupDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.3
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getRemoveSecurityGroupMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.3 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveSecurityGroup(MethodBindings bindings, RemoveSecurityGroupHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.3 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveSecurityGroupDetailed(
      MethodBindings bindings, RemoveSecurityGroupDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getAddSecurityGroupFolderMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.4 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddSecurityGroupFolder(
      MethodBindings bindings, AddSecurityGroupFolderHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.4 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddSecurityGroupFolderDetailed(
      MethodBindings bindings, AddSecurityGroupFolderDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getRemoveSecurityGroupFolderMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.5 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveSecurityGroupFolder(
      MethodBindings bindings, RemoveSecurityGroupFolderHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.5 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveSecurityGroupFolderDetailed(
      MethodBindings bindings, RemoveSecurityGroupFolderDetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.2 */
  @FunctionalInterface
  interface AddSecurityGroupHandler {
    /**
     * @return a non-null container holding all output values
     * @throws UaException for an operation failure
     */
    SecurityGroupFolderTypeAddSecurityGroupOutputs invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String securityGroupName,
        @Nullable Double keyLifetime,
        @Nullable String securityPolicyUri,
        @Nullable UInteger maxFutureKeyCount,
        @Nullable UInteger maxPastKeyCount)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.2 */
  @FunctionalInterface
  interface AddSecurityGroupDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<SecurityGroupFolderTypeAddSecurityGroupOutputs> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String securityGroupName,
        @Nullable Double keyLifetime,
        @Nullable String securityPolicyUri,
        @Nullable UInteger maxFutureKeyCount,
        @Nullable UInteger maxPastKeyCount)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.3 */
  @FunctionalInterface
  interface RemoveSecurityGroupHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId securityGroupNodeId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.3 */
  @FunctionalInterface
  interface RemoveSecurityGroupDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId securityGroupNodeId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.4 */
  @FunctionalInterface
  interface AddSecurityGroupFolderHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable NodeId invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String name)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.4 */
  @FunctionalInterface
  interface AddSecurityGroupFolderDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable NodeId> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String name)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.5 */
  @FunctionalInterface
  interface RemoveSecurityGroupFolderHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId securityGroupFolderNodeId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.5 */
  @FunctionalInterface
  interface RemoveSecurityGroupFolderDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId securityGroupFolderNodeId)
        throws UaException;
  }
}
