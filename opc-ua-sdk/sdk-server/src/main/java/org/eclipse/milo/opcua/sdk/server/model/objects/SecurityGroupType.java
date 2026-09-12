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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface SecurityGroupType extends BaseObjectType {
  QualifiedProperty<String> SECURITY_GROUP_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityGroupId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<Double> KEY_LIFETIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "KeyLifetime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<String> SECURITY_POLICY_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityPolicyUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<UInteger> MAX_FUTURE_KEY_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxFutureKeyCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_PAST_KEY_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxPastKeyCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  /** Gets the existing node's local value. */
  @Nullable String getSecurityGroupId();

  /** Sets the existing node's local value. */
  void setSecurityGroupId(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSecurityGroupIdNode();

  /** Gets the existing node's local value. */
  @Nullable Double getKeyLifetime();

  /** Sets the existing node's local value. */
  void setKeyLifetime(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getKeyLifetimeNode();

  /** Gets the existing node's local value. */
  @Nullable String getSecurityPolicyUri();

  /** Sets the existing node's local value. */
  void setSecurityPolicyUri(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSecurityPolicyUriNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxFutureKeyCount();

  /** Sets the existing node's local value. */
  void setMaxFutureKeyCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxFutureKeyCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxPastKeyCount();

  /** Sets the existing node's local value. */
  void setMaxPastKeyCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxPastKeyCountNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.2
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getInvalidateKeysMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.2 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindInvalidateKeys(MethodBindings bindings, InvalidateKeysHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.2 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindInvalidateKeysDetailed(
      MethodBindings bindings, InvalidateKeysDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getForceKeyRotationMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.3 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindForceKeyRotation(MethodBindings bindings, ForceKeyRotationHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.3 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindForceKeyRotationDetailed(
      MethodBindings bindings, ForceKeyRotationDetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.2 */
  @FunctionalInterface
  interface InvalidateKeysHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.2 */
  @FunctionalInterface
  interface InvalidateKeysDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.3 */
  @FunctionalInterface
  interface ForceKeyRotationHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.3 */
  @FunctionalInterface
  interface ForceKeyRotationDetailedHandler {
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
