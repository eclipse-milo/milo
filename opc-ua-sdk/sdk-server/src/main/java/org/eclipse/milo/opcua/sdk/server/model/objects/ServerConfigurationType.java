package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.model.methods.ServerConfigurationTypeGetCertificates;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the ServerConfigurationType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.3">Model
 *     documentation</a>
 */
public interface ServerConfigurationType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 12581L);

  /**
   * Returns the optional ApplicationNames child, a PropertyType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getApplicationNamesNode();

  /**
   * Returns the Value of the ApplicationNames child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  LocalizedText @Nullable [] getApplicationNames();

  /**
   * Sets the Value of the ApplicationNames child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setApplicationNames(LocalizedText @Nullable [] value);

  /**
   * Returns the optional ApplicationType child, a PropertyType with DataType ApplicationType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getApplicationTypeNode();

  /**
   * Returns the Value of the ApplicationType child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ApplicationType getApplicationType();

  /**
   * Sets the Value of the ApplicationType child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setApplicationType(@Nullable ApplicationType value);

  /**
   * Returns the optional ApplicationUri child, a PropertyType with DataType UriString.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getApplicationUriNode();

  /**
   * Returns the Value of the ApplicationUri child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getApplicationUri();

  /**
   * Sets the Value of the ApplicationUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setApplicationUri(@Nullable String value);

  /**
   * Returns the mandatory CertificateGroups child, a CertificateGroupFolderType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.3">CertificateGroupFolderType
   *     documentation</a>
   */
  CertificateGroupFolderTypeNode getCertificateGroupsNode();

  /**
   * Returns the optional ConfigurationFile child, a ApplicationConfigurationFileType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part26/5.4">Model
   *     documentation</a>
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.20">ApplicationConfigurationFileType
   *     documentation</a>
   */
  @Nullable ApplicationConfigurationFileTypeNode getConfigurationFileNode();

  /**
   * Returns the optional HasSecureElement child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getHasSecureElementNode();

  /**
   * Returns the Value of the HasSecureElement child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getHasSecureElement();

  /**
   * Sets the Value of the HasSecureElement child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setHasSecureElement(@Nullable Boolean value);

  /**
   * Returns the optional InApplicationSetup child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getInApplicationSetupNode();

  /**
   * Returns the Value of the InApplicationSetup child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getInApplicationSetup();

  /**
   * Sets the Value of the InApplicationSetup child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setInApplicationSetup(@Nullable Boolean value);

  /**
   * Returns the mandatory MaxTrustListSize child, a PropertyType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getMaxTrustListSizeNode();

  /**
   * Returns the Value of the MaxTrustListSize child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxTrustListSize();

  /**
   * Sets the Value of the MaxTrustListSize child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxTrustListSize(@Nullable UInteger value);

  /**
   * Returns the mandatory MulticastDnsEnabled child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getMulticastDnsEnabledNode();

  /**
   * Returns the Value of the MulticastDnsEnabled child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getMulticastDnsEnabled();

  /**
   * Sets the Value of the MulticastDnsEnabled child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMulticastDnsEnabled(@Nullable Boolean value);

  /**
   * Returns the optional ProductUri child, a PropertyType with DataType UriString.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getProductUriNode();

  /**
   * Returns the Value of the ProductUri child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getProductUri();

  /**
   * Sets the Value of the ProductUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setProductUri(@Nullable String value);

  /**
   * Returns the mandatory ServerCapabilities child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getServerCapabilitiesNode();

  /**
   * Returns the Value of the ServerCapabilities child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String @Nullable [] getServerCapabilities();

  /**
   * Sets the Value of the ServerCapabilities child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setServerCapabilities(@Nullable String @Nullable [] value);

  /**
   * Returns the mandatory SupportedPrivateKeyFormats child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getSupportedPrivateKeyFormatsNode();

  /**
   * Returns the Value of the SupportedPrivateKeyFormats child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String @Nullable [] getSupportedPrivateKeyFormats();

  /**
   * Sets the Value of the SupportedPrivateKeyFormats child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSupportedPrivateKeyFormats(@Nullable String @Nullable [] value);

  /**
   * Returns the optional SupportsTransactions child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getSupportsTransactionsNode();

  /**
   * Returns the Value of the SupportsTransactions child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getSupportsTransactions();

  /**
   * Sets the Value of the SupportsTransactions child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSupportsTransactions(@Nullable Boolean value);

  /**
   * Returns the optional TransactionDiagnostics child, a TransactionDiagnosticsType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.17">TransactionDiagnosticsType
   *     documentation</a>
   */
  @Nullable TransactionDiagnosticsTypeNode getTransactionDiagnosticsNode();

  /**
   * Returns the mandatory ApplyChanges Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.9">Model
   *     documentation</a>
   */
  UaMethodNode getApplyChangesMethodNode();

  /**
   * Sets this instance's ApplyChanges handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setApplyChangesHandler(@Nullable ApplyChangesHandler handler);

  /**
   * Returns the optional CancelChanges Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.11">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getCancelChangesMethodNode();

  /**
   * Sets this instance's CancelChanges handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setCancelChangesHandler(@Nullable CancelChangesHandler handler);

  /**
   * Returns the optional CreateSelfSignedCertificate Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getCreateSelfSignedCertificateMethodNode();

  /**
   * Sets this instance's CreateSelfSignedCertificate handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setCreateSelfSignedCertificateHandler(@Nullable CreateSelfSignedCertificateHandler handler);

  /**
   * Returns the mandatory CreateSigningRequest Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.10">Model
   *     documentation</a>
   */
  UaMethodNode getCreateSigningRequestMethodNode();

  /**
   * Sets this instance's CreateSigningRequest handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setCreateSigningRequestHandler(@Nullable CreateSigningRequestHandler handler);

  /**
   * Returns the optional DeleteCertificate Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.7">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getDeleteCertificateMethodNode();

  /**
   * Sets this instance's DeleteCertificate handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setDeleteCertificateHandler(@Nullable DeleteCertificateHandler handler);

  /**
   * Returns the optional GetCertificates Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.8">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getGetCertificatesMethodNode();

  /**
   * Sets this instance's GetCertificates handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setGetCertificatesHandler(@Nullable GetCertificatesHandler handler);

  /**
   * Returns the mandatory GetRejectedList Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.12">Model
   *     documentation</a>
   */
  UaMethodNode getGetRejectedListMethodNode();

  /**
   * Sets this instance's GetRejectedList handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setGetRejectedListHandler(@Nullable GetRejectedListHandler handler);

  /**
   * Returns the optional ResetToServerDefaults Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.13">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getResetToServerDefaultsMethodNode();

  /**
   * Sets this instance's ResetToServerDefaults handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setResetToServerDefaultsHandler(@Nullable ResetToServerDefaultsHandler handler);

  /**
   * Returns the mandatory UpdateCertificate Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.5">Model
   *     documentation</a>
   */
  UaMethodNode getUpdateCertificateMethodNode();

  /**
   * Sets this instance's UpdateCertificate handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setUpdateCertificateHandler(@Nullable UpdateCertificateHandler handler);

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
   * Handles calls to the ApplyChanges Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.9">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface ApplyChangesHandler {
    /**
     * Handles a call to the ApplyChanges Method.
     *
     * @throws UaException if the call fails.
     */
    void applyChanges(AbstractMethodInvocationHandler.InvocationContext context) throws UaException;
  }

  /**
   * Handles calls to the CancelChanges Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.11">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface CancelChangesHandler {
    /**
     * Handles a call to the CancelChanges Method.
     *
     * @throws UaException if the call fails.
     */
    void cancelChanges(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException;
  }

  /**
   * Handles calls to the CreateSelfSignedCertificate Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface CreateSelfSignedCertificateHandler {
    /**
     * Handles a call to the CreateSelfSignedCertificate Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable ByteString createSelfSignedCertificate(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId certificateGroupId,
        @Nullable NodeId certificateTypeId,
        @Nullable String subjectName,
        @Nullable String @Nullable [] dnsNames,
        @Nullable String @Nullable [] ipAddresses,
        @Nullable UShort lifetimeInDays,
        @Nullable UShort keySizeInBits)
        throws UaException;
  }

  /**
   * Handles calls to the CreateSigningRequest Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.10">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface CreateSigningRequestHandler {
    /**
     * Handles a call to the CreateSigningRequest Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable ByteString createSigningRequest(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId certificateGroupId,
        @Nullable NodeId certificateTypeId,
        @Nullable String subjectName,
        @Nullable Boolean regeneratePrivateKey,
        @Nullable ByteString nonce)
        throws UaException;
  }

  /**
   * Handles calls to the DeleteCertificate Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.7">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface DeleteCertificateHandler {
    /**
     * Handles a call to the DeleteCertificate Method.
     *
     * @throws UaException if the call fails.
     */
    void deleteCertificate(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId certificateGroupId,
        @Nullable NodeId certificateTypeId)
        throws UaException;
  }

  /**
   * Handles calls to the GetCertificates Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.8">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface GetCertificatesHandler {
    /**
     * Handles a call to the GetCertificates Method.
     *
     * @throws UaException if the call fails.
     */
    ServerConfigurationTypeGetCertificates.Outputs getCertificates(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId certificateGroupId)
        throws UaException;
  }

  /**
   * Handles calls to the GetRejectedList Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.12">Model
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

  /**
   * Handles calls to the ResetToServerDefaults Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.13">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface ResetToServerDefaultsHandler {
    /**
     * Handles a call to the ResetToServerDefaults Method.
     *
     * @throws UaException if the call fails.
     */
    void resetToServerDefaults(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException;
  }

  /**
   * Handles calls to the UpdateCertificate Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.5">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface UpdateCertificateHandler {
    /**
     * Handles a call to the UpdateCertificate Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable Boolean updateCertificate(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId certificateGroupId,
        @Nullable NodeId certificateTypeId,
        @Nullable ByteString certificate,
        ByteString @Nullable [] issuerCertificates,
        @Nullable String privateKeyFormat,
        @Nullable ByteString privateKey)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /** Handles a call to the ApplyChanges Method; see {@link ApplyChangesHandler#applyChanges}. */
    default void applyChanges(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the CancelChanges Method; see {@link CancelChangesHandler#cancelChanges}.
     */
    default void cancelChanges(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the CreateSelfSignedCertificate Method; see {@link
     * CreateSelfSignedCertificateHandler#createSelfSignedCertificate}.
     */
    default @Nullable ByteString createSelfSignedCertificate(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId certificateGroupId,
        @Nullable NodeId certificateTypeId,
        @Nullable String subjectName,
        @Nullable String @Nullable [] dnsNames,
        @Nullable String @Nullable [] ipAddresses,
        @Nullable UShort lifetimeInDays,
        @Nullable UShort keySizeInBits)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the CreateSigningRequest Method; see {@link
     * CreateSigningRequestHandler#createSigningRequest}.
     */
    default @Nullable ByteString createSigningRequest(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId certificateGroupId,
        @Nullable NodeId certificateTypeId,
        @Nullable String subjectName,
        @Nullable Boolean regeneratePrivateKey,
        @Nullable ByteString nonce)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the DeleteCertificate Method; see {@link
     * DeleteCertificateHandler#deleteCertificate}.
     */
    default void deleteCertificate(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId certificateGroupId,
        @Nullable NodeId certificateTypeId)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the GetCertificates Method; see {@link
     * GetCertificatesHandler#getCertificates}.
     */
    default ServerConfigurationTypeGetCertificates.Outputs getCertificates(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId certificateGroupId)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the GetRejectedList Method; see {@link
     * GetRejectedListHandler#getRejectedList}.
     */
    default ByteString @Nullable [] getRejectedList(
        AbstractMethodInvocationHandler.InvocationContext context) throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the ResetToServerDefaults Method; see {@link
     * ResetToServerDefaultsHandler#resetToServerDefaults}.
     */
    default void resetToServerDefaults(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the UpdateCertificate Method; see {@link
     * UpdateCertificateHandler#updateCertificate}.
     */
    default @Nullable Boolean updateCertificate(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId certificateGroupId,
        @Nullable NodeId certificateTypeId,
        @Nullable ByteString certificate,
        ByteString @Nullable [] issuerCertificates,
        @Nullable String privateKeyFormat,
        @Nullable ByteString privateKey)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
