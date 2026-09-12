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
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointType;
import org.eclipse.milo.opcua.stack.core.types.structured.IdentityMappingRuleType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.1">https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface RoleType extends BaseObjectType {
  QualifiedProperty<IdentityMappingRuleType[]> IDENTITIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Identities",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15634"),
          1,
          IdentityMappingRuleType[].class);

  QualifiedProperty<Boolean> APPLICATIONS_EXCLUDE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ApplicationsExclude",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<String[]> APPLICATIONS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Applications",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          1,
          String[].class);

  QualifiedProperty<Boolean> ENDPOINTS_EXCLUDE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EndpointsExclude",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<EndpointType[]> ENDPOINTS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Endpoints",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15528"),
          1,
          EndpointType[].class);

  QualifiedProperty<Boolean> CUSTOM_CONFIGURATION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CustomConfiguration",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  /** Gets the existing node's local value. */
  @Nullable IdentityMappingRuleType @Nullable [] getIdentities();

  /** Sets the existing node's local value. */
  void setIdentities(@Nullable IdentityMappingRuleType @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getIdentitiesNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getApplicationsExclude();

  /** Sets the existing node's local value. */
  void setApplicationsExclude(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getApplicationsExcludeNode();

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getApplications();

  /** Sets the existing node's local value. */
  void setApplications(@Nullable String @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getApplicationsNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getEndpointsExclude();

  /** Sets the existing node's local value. */
  void setEndpointsExclude(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getEndpointsExcludeNode();

  /** Gets the existing node's local value. */
  @Nullable EndpointType @Nullable [] getEndpoints();

  /** Sets the existing node's local value. */
  void setEndpoints(@Nullable EndpointType @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getEndpointsNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getCustomConfiguration();

  /** Sets the existing node's local value. */
  void setCustomConfiguration(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getCustomConfigurationNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getAddIdentityMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.5 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddIdentity(MethodBindings bindings, AddIdentityHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.5 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddIdentityDetailed(MethodBindings bindings, AddIdentityDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.6
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getRemoveIdentityMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.6 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveIdentity(MethodBindings bindings, RemoveIdentityHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.6 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveIdentityDetailed(
      MethodBindings bindings, RemoveIdentityDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.7
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getAddApplicationMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.7 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddApplication(MethodBindings bindings, AddApplicationHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.7 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddApplicationDetailed(
      MethodBindings bindings, AddApplicationDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.8
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getRemoveApplicationMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.8 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveApplication(MethodBindings bindings, RemoveApplicationHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.8 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveApplicationDetailed(
      MethodBindings bindings, RemoveApplicationDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.9
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getAddEndpointMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.9 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddEndpoint(MethodBindings bindings, AddEndpointHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.9 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddEndpointDetailed(MethodBindings bindings, AddEndpointDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.10
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getRemoveEndpointMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.10 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveEndpoint(MethodBindings bindings, RemoveEndpointHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.10 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveEndpointDetailed(
      MethodBindings bindings, RemoveEndpointDetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.5 */
  @FunctionalInterface
  interface AddIdentityHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable IdentityMappingRuleType rule)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.5 */
  @FunctionalInterface
  interface AddIdentityDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable IdentityMappingRuleType rule)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.6 */
  @FunctionalInterface
  interface RemoveIdentityHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable IdentityMappingRuleType rule)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.6 */
  @FunctionalInterface
  interface RemoveIdentityDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable IdentityMappingRuleType rule)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.7 */
  @FunctionalInterface
  interface AddApplicationHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String applicationUri)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.7 */
  @FunctionalInterface
  interface AddApplicationDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String applicationUri)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.8 */
  @FunctionalInterface
  interface RemoveApplicationHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String applicationUri)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.8 */
  @FunctionalInterface
  interface RemoveApplicationDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String applicationUri)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.9 */
  @FunctionalInterface
  interface AddEndpointHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable EndpointType endpoint)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.9 */
  @FunctionalInterface
  interface AddEndpointDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable EndpointType endpoint)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.10 */
  @FunctionalInterface
  interface RemoveEndpointHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable EndpointType endpoint)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.10 */
  @FunctionalInterface
  interface RemoveEndpointDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable EndpointType endpoint)
        throws UaException;
  }
}
