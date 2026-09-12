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
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface CertificateGroupType extends BaseObjectType {
  QualifiedProperty<NodeId[]> CERTIFICATE_TYPES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CertificateTypes",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          1,
          NodeId[].class);

  QualifiedProperty<NodeId> PURPOSE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Purpose",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  /** Gets the existing node's local value. */
  @Nullable NodeId @Nullable [] getCertificateTypes();

  /** Sets the existing node's local value. */
  void setCertificateTypes(@Nullable NodeId @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getCertificateTypesNode();

  /** Gets the existing node's local value. */
  @Nullable NodeId getPurpose();

  /** Sets the existing node's local value. */
  void setPurpose(@Nullable NodeId value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getPurposeNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TrustListType getTrustListNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable CertificateExpirationAlarmType getCertificateExpiredNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable TrustListOutOfDateAlarmType getTrustListOutOfDateNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.2
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getGetRejectedListMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.2 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindGetRejectedList(MethodBindings bindings, GetRejectedListHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.2 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindGetRejectedListDetailed(
      MethodBindings bindings, GetRejectedListDetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.2 */
  @FunctionalInterface
  interface GetRejectedListHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable ByteString @Nullable [] invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.2 */
  @FunctionalInterface
  interface GetRejectedListDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable ByteString @Nullable []> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }
}
