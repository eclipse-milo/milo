package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.TrustListValidationOptions;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the TrustListType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.1">Model
 *     documentation</a>
 */
public interface TrustListType extends FileType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 12522L);

  /**
   * Returns the optional ActivityTimeout child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getActivityTimeoutNode();

  /**
   * Returns the Value of the ActivityTimeout child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getActivityTimeout();

  /**
   * Sets the Value of the ActivityTimeout child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setActivityTimeout(@Nullable Double value);

  /**
   * Returns the optional DefaultValidationOptions child, a PropertyType with DataType
   * TrustListValidationOptions.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getDefaultValidationOptionsNode();

  /**
   * Returns the Value of the DefaultValidationOptions child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable TrustListValidationOptions getDefaultValidationOptions();

  /**
   * Sets the Value of the DefaultValidationOptions child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDefaultValidationOptions(@Nullable TrustListValidationOptions value);

  /**
   * Returns the mandatory LastUpdateTime child, a PropertyType with DataType UtcTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getLastUpdateTimeNode();

  /**
   * Returns the Value of the LastUpdateTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getLastUpdateTime();

  /**
   * Sets the Value of the LastUpdateTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLastUpdateTime(@Nullable DateTime value);

  /**
   * Returns the optional UpdateFrequency child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getUpdateFrequencyNode();

  /**
   * Returns the Value of the UpdateFrequency child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getUpdateFrequency();

  /**
   * Sets the Value of the UpdateFrequency child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setUpdateFrequency(@Nullable Double value);

  /**
   * Returns the mandatory AddCertificate Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.6">Model
   *     documentation</a>
   */
  UaMethodNode getAddCertificateMethodNode();

  /**
   * Sets this instance's AddCertificate handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddCertificateHandler(@Nullable AddCertificateHandler handler);

  /**
   * Returns the mandatory CloseAndUpdate Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.5">Model
   *     documentation</a>
   */
  UaMethodNode getCloseAndUpdateMethodNode();

  /**
   * Sets this instance's CloseAndUpdate handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setCloseAndUpdateHandler(@Nullable CloseAndUpdateHandler handler);

  /**
   * Returns the mandatory OpenWithMasks Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.3">Model
   *     documentation</a>
   */
  UaMethodNode getOpenWithMasksMethodNode();

  /**
   * Sets this instance's OpenWithMasks handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setOpenWithMasksHandler(@Nullable OpenWithMasksHandler handler);

  /**
   * Returns the mandatory RemoveCertificate Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.7">Model
   *     documentation</a>
   */
  UaMethodNode getRemoveCertificateMethodNode();

  /**
   * Sets this instance's RemoveCertificate handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRemoveCertificateHandler(@Nullable RemoveCertificateHandler handler);

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
   * Handles calls to the AddCertificate Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.6">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddCertificateHandler {
    /**
     * Handles a call to the AddCertificate Method.
     *
     * @throws UaException if the call fails.
     */
    void addCertificate(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ByteString certificate,
        @Nullable Boolean isTrustedCertificate)
        throws UaException;
  }

  /**
   * Handles calls to the CloseAndUpdate Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.5">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface CloseAndUpdateHandler {
    /**
     * Handles a call to the CloseAndUpdate Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable Boolean closeAndUpdate(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable UInteger fileHandle)
        throws UaException;
  }

  /**
   * Handles calls to the OpenWithMasks Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface OpenWithMasksHandler {
    /**
     * Handles a call to the OpenWithMasks Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable UInteger openWithMasks(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable UInteger masks)
        throws UaException;
  }

  /**
   * Handles calls to the RemoveCertificate Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.7">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface RemoveCertificateHandler {
    /**
     * Handles a call to the RemoveCertificate Method.
     *
     * @throws UaException if the call fails.
     */
    void removeCertificate(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String thumbprint,
        @Nullable Boolean isTrustedCertificate)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods extends FileType.Methods {
    /**
     * Handles a call to the AddCertificate Method; see {@link
     * AddCertificateHandler#addCertificate}.
     */
    default void addCertificate(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ByteString certificate,
        @Nullable Boolean isTrustedCertificate)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the CloseAndUpdate Method; see {@link
     * CloseAndUpdateHandler#closeAndUpdate}.
     */
    default @Nullable Boolean closeAndUpdate(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable UInteger fileHandle)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the OpenWithMasks Method; see {@link OpenWithMasksHandler#openWithMasks}.
     */
    default @Nullable UInteger openWithMasks(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable UInteger masks)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the RemoveCertificate Method; see {@link
     * RemoveCertificateHandler#removeCertificate}.
     */
    default void removeCertificate(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String thumbprint,
        @Nullable Boolean isTrustedCertificate)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
