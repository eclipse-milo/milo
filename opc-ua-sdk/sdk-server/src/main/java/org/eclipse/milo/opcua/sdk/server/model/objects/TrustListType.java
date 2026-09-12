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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.TrustListValidationOptions;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface TrustListType extends FileType {
  QualifiedProperty<DateTime> LAST_UPDATE_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastUpdateTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<Double> UPDATE_FREQUENCY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UpdateFrequency",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Double> ACTIVITY_TIMEOUT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ActivityTimeout",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<TrustListValidationOptions> DEFAULT_VALIDATION_OPTIONS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DefaultValidationOptions",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23564"),
          -1,
          TrustListValidationOptions.class);

  /** Gets the existing node's local value. */
  @Nullable DateTime getLastUpdateTime();

  /** Sets the existing node's local value. */
  void setLastUpdateTime(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLastUpdateTimeNode();

  /** Gets the existing node's local value. */
  @Nullable Double getUpdateFrequency();

  /** Sets the existing node's local value. */
  void setUpdateFrequency(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getUpdateFrequencyNode();

  /** Gets the existing node's local value. */
  @Nullable Double getActivityTimeout();

  /** Sets the existing node's local value. */
  void setActivityTimeout(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getActivityTimeoutNode();

  /** Gets the existing node's local value. */
  @Nullable TrustListValidationOptions getDefaultValidationOptions();

  /** Sets the existing node's local value. */
  void setDefaultValidationOptions(@Nullable TrustListValidationOptions value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDefaultValidationOptionsNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.3
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getOpenWithMasksMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.3 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindOpenWithMasks(MethodBindings bindings, OpenWithMasksHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.3 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindOpenWithMasksDetailed(
      MethodBindings bindings, OpenWithMasksDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.5
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getCloseAndUpdateMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.5 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindCloseAndUpdate(MethodBindings bindings, CloseAndUpdateHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.5 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindCloseAndUpdateDetailed(
      MethodBindings bindings, CloseAndUpdateDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.6
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getAddCertificateMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.6 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddCertificate(MethodBindings bindings, AddCertificateHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.6 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddCertificateDetailed(
      MethodBindings bindings, AddCertificateDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.7
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getRemoveCertificateMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.7 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveCertificate(MethodBindings bindings, RemoveCertificateHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.7 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveCertificateDetailed(
      MethodBindings bindings, RemoveCertificateDetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.3 */
  @FunctionalInterface
  interface OpenWithMasksHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable UInteger invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger masks)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.3 */
  @FunctionalInterface
  interface OpenWithMasksDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable UInteger> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger masks)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.5 */
  @FunctionalInterface
  interface CloseAndUpdateHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable Boolean invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger fileHandle)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.5 */
  @FunctionalInterface
  interface CloseAndUpdateDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Boolean> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger fileHandle)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.6 */
  @FunctionalInterface
  interface AddCertificateHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable ByteString certificate,
        @Nullable Boolean isTrustedCertificate)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.6 */
  @FunctionalInterface
  interface AddCertificateDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable ByteString certificate,
        @Nullable Boolean isTrustedCertificate)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.7 */
  @FunctionalInterface
  interface RemoveCertificateHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String thumbprint,
        @Nullable Boolean isTrustedCertificate)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.7 */
  @FunctionalInterface
  interface RemoveCertificateDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String thumbprint,
        @Nullable Boolean isTrustedCertificate)
        throws UaException;
  }
}
