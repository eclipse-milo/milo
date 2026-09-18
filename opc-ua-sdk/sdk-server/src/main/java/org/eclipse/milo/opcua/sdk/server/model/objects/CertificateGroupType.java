package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the CertificateGroupType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.1">Model
 *     documentation</a>
 */
public interface CertificateGroupType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 12555L);

  /**
   * Returns the optional CertificateExpired child, a CertificateExpirationAlarmType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.24/#5.8.24.7">CertificateExpirationAlarmType
   *     documentation</a>
   */
  @Nullable CertificateExpirationAlarmTypeNode getCertificateExpiredNode();

  /**
   * Returns the mandatory CertificateTypes child, a PropertyType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getCertificateTypesNode();

  /**
   * Returns the Value of the CertificateTypes child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  NodeId @Nullable [] getCertificateTypes();

  /**
   * Sets the Value of the CertificateTypes child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCertificateTypes(NodeId @Nullable [] value);

  /**
   * Returns the optional Purpose child, a PropertyType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getPurposeNode();

  /**
   * Returns the Value of the Purpose child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable NodeId getPurpose();

  /**
   * Sets the Value of the Purpose child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPurpose(@Nullable NodeId value);

  /**
   * Returns the mandatory TrustList child, a TrustListType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.1">TrustListType
   *     documentation</a>
   */
  TrustListTypeNode getTrustListNode();

  /**
   * Returns the optional TrustListOutOfDate child, a TrustListOutOfDateAlarmType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.11">TrustListOutOfDateAlarmType
   *     documentation</a>
   */
  @Nullable TrustListOutOfDateAlarmTypeNode getTrustListOutOfDateNode();

  /**
   * Returns the optional GetRejectedList Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.2">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getGetRejectedListMethodNode();

  /**
   * Sets this instance's GetRejectedList handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setGetRejectedListHandler(@Nullable GetRejectedListHandler handler);

  /**
   * Sets this instance's Method handlers, including inherited handlers, from one implementation;
   * null clears them and restores Method-node fallback. Absent optional Methods are skipped.
   * Changes are applied in order; a failure does not roll back earlier changes.
   *
   * @throws UaRuntimeException if a mandatory Method is absent, or a Method is ambiguous or
   *     incompatible.
   */
  void setMethods(@Nullable Methods methods);

  /**
   * Handles calls to the GetRejectedList Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.2">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface GetRejectedListHandler {
    /**
     * Handles a call to the GetRejectedList Method.
     *
     * @throws UaException if the call fails.
     */
    ByteString @Nullable [] getRejectedList(
        AbstractMethodInvocationHandler.InvocationContext context) throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /**
     * Handles a call to the GetRejectedList Method; see {@link
     * GetRejectedListHandler#getRejectedList}.
     */
    default ByteString @Nullable [] getRejectedList(
        AbstractMethodInvocationHandler.InvocationContext context) throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
