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
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerConfigurationTypeGetCertificatesOutputs;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.3">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.3</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ServerConfigurationType extends BaseObjectType {
  QualifiedProperty<String> APPLICATION_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ApplicationUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23751"),
          -1,
          String.class);

  QualifiedProperty<String> PRODUCT_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ProductUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23751"),
          -1,
          String.class);

  QualifiedProperty<ApplicationType> APPLICATION_TYPE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ApplicationType",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=307"),
          -1,
          ApplicationType.class);

  QualifiedProperty<LocalizedText[]> APPLICATION_NAMES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ApplicationNames",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          1,
          LocalizedText[].class);

  QualifiedProperty<String[]> SERVER_CAPABILITIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ServerCapabilities",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          1,
          String[].class);

  QualifiedProperty<String[]> SUPPORTED_PRIVATE_KEY_FORMATS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SupportedPrivateKeyFormats",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          1,
          String[].class);

  QualifiedProperty<UInteger> MAX_TRUST_LIST_SIZE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxTrustListSize",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<Boolean> MULTICAST_DNS_ENABLED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MulticastDnsEnabled",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> HAS_SECURE_ELEMENT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "HasSecureElement",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> SUPPORTS_TRANSACTIONS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SupportsTransactions",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> IN_APPLICATION_SETUP =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InApplicationSetup",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  /** Gets the existing node's local value. */
  @Nullable String getApplicationUri();

  /** Sets the existing node's local value. */
  void setApplicationUri(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getApplicationUriNode();

  /** Gets the existing node's local value. */
  @Nullable String getProductUri();

  /** Sets the existing node's local value. */
  void setProductUri(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getProductUriNode();

  /** Gets the existing node's local value. */
  @Nullable ApplicationType getApplicationType();

  /** Sets the existing node's local value. */
  void setApplicationType(@Nullable ApplicationType value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getApplicationTypeNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText @Nullable [] getApplicationNames();

  /** Sets the existing node's local value. */
  void setApplicationNames(@Nullable LocalizedText @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getApplicationNamesNode();

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getServerCapabilities();

  /** Sets the existing node's local value. */
  void setServerCapabilities(@Nullable String @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getServerCapabilitiesNode();

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getSupportedPrivateKeyFormats();

  /** Sets the existing node's local value. */
  void setSupportedPrivateKeyFormats(@Nullable String @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSupportedPrivateKeyFormatsNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxTrustListSize();

  /** Sets the existing node's local value. */
  void setMaxTrustListSize(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxTrustListSizeNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getMulticastDnsEnabled();

  /** Sets the existing node's local value. */
  void setMulticastDnsEnabled(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMulticastDnsEnabledNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getHasSecureElement();

  /** Sets the existing node's local value. */
  void setHasSecureElement(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getHasSecureElementNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getSupportsTransactions();

  /** Sets the existing node's local value. */
  void setSupportsTransactions(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSupportsTransactionsNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getInApplicationSetup();

  /** Sets the existing node's local value. */
  void setInApplicationSetup(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getInApplicationSetupNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.5
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getUpdateCertificateMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.5 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindUpdateCertificate(MethodBindings bindings, UpdateCertificateHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.5 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindUpdateCertificateDetailed(
      MethodBindings bindings, UpdateCertificateDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getCreateSelfSignedCertificateMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindCreateSelfSignedCertificate(
      MethodBindings bindings, CreateSelfSignedCertificateHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindCreateSelfSignedCertificateDetailed(
      MethodBindings bindings, CreateSelfSignedCertificateDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.7
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getDeleteCertificateMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.7 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindDeleteCertificate(MethodBindings bindings, DeleteCertificateHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.7 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindDeleteCertificateDetailed(
      MethodBindings bindings, DeleteCertificateDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.8
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getGetCertificatesMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.8 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindGetCertificates(MethodBindings bindings, GetCertificatesHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.8 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindGetCertificatesDetailed(
      MethodBindings bindings, GetCertificatesDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.9
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getApplyChangesMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.9 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindApplyChanges(MethodBindings bindings, ApplyChangesHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.9 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindApplyChangesDetailed(
      MethodBindings bindings, ApplyChangesDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.11
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getCancelChangesMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.11 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindCancelChanges(MethodBindings bindings, CancelChangesHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.11 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindCancelChangesDetailed(
      MethodBindings bindings, CancelChangesDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.10
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getCreateSigningRequestMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.10 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindCreateSigningRequest(
      MethodBindings bindings, CreateSigningRequestHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.10 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindCreateSigningRequestDetailed(
      MethodBindings bindings, CreateSigningRequestDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.12
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getGetRejectedListMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.12 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindGetRejectedList(MethodBindings bindings, GetRejectedListHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.12 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindGetRejectedListDetailed(
      MethodBindings bindings, GetRejectedListDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.13
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getResetToServerDefaultsMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.13 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindResetToServerDefaults(
      MethodBindings bindings, ResetToServerDefaultsHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.13 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindResetToServerDefaultsDetailed(
      MethodBindings bindings, ResetToServerDefaultsDetailedHandler handler) throws UaException;

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  CertificateGroupFolderType getCertificateGroupsNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable TransactionDiagnosticsType getTransactionDiagnosticsNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part26/5.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable ApplicationConfigurationFileType getConfigurationFileNode();

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.5 */
  @FunctionalInterface
  interface UpdateCertificateHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable Boolean invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId certificateGroupId,
        @Nullable NodeId certificateTypeId,
        @Nullable ByteString certificate,
        @Nullable ByteString @Nullable [] issuerCertificates,
        @Nullable String privateKeyFormat,
        @Nullable ByteString privateKey)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.5 */
  @FunctionalInterface
  interface UpdateCertificateDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Boolean> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId certificateGroupId,
        @Nullable NodeId certificateTypeId,
        @Nullable ByteString certificate,
        @Nullable ByteString @Nullable [] issuerCertificates,
        @Nullable String privateKeyFormat,
        @Nullable ByteString privateKey)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6 */
  @FunctionalInterface
  interface CreateSelfSignedCertificateHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable ByteString invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId certificateGroupId,
        @Nullable NodeId certificateTypeId,
        @Nullable String subjectName,
        @Nullable String @Nullable [] dnsNames,
        @Nullable String @Nullable [] ipAddresses,
        @Nullable UShort lifetimeInDays,
        @Nullable UShort keySizeInBits)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6 */
  @FunctionalInterface
  interface CreateSelfSignedCertificateDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable ByteString> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId certificateGroupId,
        @Nullable NodeId certificateTypeId,
        @Nullable String subjectName,
        @Nullable String @Nullable [] dnsNames,
        @Nullable String @Nullable [] ipAddresses,
        @Nullable UShort lifetimeInDays,
        @Nullable UShort keySizeInBits)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.7 */
  @FunctionalInterface
  interface DeleteCertificateHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId certificateGroupId,
        @Nullable NodeId certificateTypeId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.7 */
  @FunctionalInterface
  interface DeleteCertificateDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId certificateGroupId,
        @Nullable NodeId certificateTypeId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.8 */
  @FunctionalInterface
  interface GetCertificatesHandler {
    /**
     * @return a non-null container holding all output values
     * @throws UaException for an operation failure
     */
    ServerConfigurationTypeGetCertificatesOutputs invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId certificateGroupId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.8 */
  @FunctionalInterface
  interface GetCertificatesDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<ServerConfigurationTypeGetCertificatesOutputs> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId certificateGroupId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.9 */
  @FunctionalInterface
  interface ApplyChangesHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.9 */
  @FunctionalInterface
  interface ApplyChangesDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.11 */
  @FunctionalInterface
  interface CancelChangesHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.11 */
  @FunctionalInterface
  interface CancelChangesDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.10 */
  @FunctionalInterface
  interface CreateSigningRequestHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable ByteString invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId certificateGroupId,
        @Nullable NodeId certificateTypeId,
        @Nullable String subjectName,
        @Nullable Boolean regeneratePrivateKey,
        @Nullable ByteString nonce)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.10 */
  @FunctionalInterface
  interface CreateSigningRequestDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable ByteString> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId certificateGroupId,
        @Nullable NodeId certificateTypeId,
        @Nullable String subjectName,
        @Nullable Boolean regeneratePrivateKey,
        @Nullable ByteString nonce)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.12 */
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

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.12 */
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

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.13 */
  @FunctionalInterface
  interface ResetToServerDefaultsHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.13 */
  @FunctionalInterface
  interface ResetToServerDefaultsDetailedHandler {
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
