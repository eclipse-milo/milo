package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerConfigurationTypeGetCertificates;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the ServerConfigurationType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.3">Model
 *     documentation</a>
 */
public interface ServerConfigurationType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 12581L);

  QualifiedProperty<String> ProductUri_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ProductUri",
          ExpandedNodeId.of(Namespaces.OPC_UA, 23751L),
          -1,
          String.class);

  QualifiedProperty<String> ApplicationUri_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ApplicationUri",
          ExpandedNodeId.of(Namespaces.OPC_UA, 23751L),
          -1,
          String.class);

  QualifiedProperty<ApplicationType> ApplicationType_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ApplicationType",
          ExpandedNodeId.of(Namespaces.OPC_UA, 307L),
          -1,
          ApplicationType.class);

  QualifiedProperty<LocalizedText[]> ApplicationNames_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ApplicationNames",
          ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
          1,
          LocalizedText[].class);

  QualifiedProperty<Boolean> HasSecureElement_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "HasSecureElement",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<UInteger> MaxTrustListSize_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxTrustListSize",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<Boolean> InApplicationSetup_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "InApplicationSetup",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<String[]> ServerCapabilities_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ServerCapabilities",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          1,
          String[].class);

  QualifiedProperty<Boolean> MulticastDnsEnabled_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MulticastDnsEnabled",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> SupportsTransactions_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SupportsTransactions",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<String[]> SupportedPrivateKeyFormats_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SupportedPrivateKeyFormats",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          1,
          String[].class);

  /**
   * Resolves the optional ProductUri child, a PropertyType with DataType UriString.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getProductUriNode() throws UaException;

  /** Asynchronous form of {@link #getProductUriNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getProductUriNodeAsync();

  /**
   * Reads the Value of the ProductUri child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readProductUri() throws UaException;

  /**
   * Writes the Value of the ProductUri child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeProductUri(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readProductUri()}. */
  CompletableFuture<? extends @Nullable String> readProductUriAsync();

  /** Asynchronous form of {@link #writeProductUri}; completes with the operation status. */
  CompletableFuture<StatusCode> writeProductUriAsync(@Nullable String value);

  /**
   * Resolves the optional ApplicationUri child, a PropertyType with DataType UriString.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getApplicationUriNode() throws UaException;

  /** Asynchronous form of {@link #getApplicationUriNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getApplicationUriNodeAsync();

  /**
   * Reads the Value of the ApplicationUri child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readApplicationUri() throws UaException;

  /**
   * Writes the Value of the ApplicationUri child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeApplicationUri(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readApplicationUri()}. */
  CompletableFuture<? extends @Nullable String> readApplicationUriAsync();

  /** Asynchronous form of {@link #writeApplicationUri}; completes with the operation status. */
  CompletableFuture<StatusCode> writeApplicationUriAsync(@Nullable String value);

  /**
   * Resolves the optional ApplicationType child, a PropertyType with DataType ApplicationType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getApplicationTypeNode() throws UaException;

  /** Asynchronous form of {@link #getApplicationTypeNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getApplicationTypeNodeAsync();

  /**
   * Reads the Value of the ApplicationType child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ApplicationType readApplicationType() throws UaException;

  /**
   * Writes the Value of the ApplicationType child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeApplicationType(@Nullable ApplicationType value) throws UaException;

  /** Asynchronous form of {@link #readApplicationType()}. */
  CompletableFuture<? extends @Nullable ApplicationType> readApplicationTypeAsync();

  /** Asynchronous form of {@link #writeApplicationType}; completes with the operation status. */
  CompletableFuture<StatusCode> writeApplicationTypeAsync(@Nullable ApplicationType value);

  /**
   * Resolves the optional ApplicationNames child, a PropertyType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getApplicationNamesNode() throws UaException;

  /** Asynchronous form of {@link #getApplicationNamesNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getApplicationNamesNodeAsync();

  /**
   * Reads the Value of the ApplicationNames child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  LocalizedText @Nullable [] readApplicationNames() throws UaException;

  /**
   * Writes the Value of the ApplicationNames child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeApplicationNames(LocalizedText @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readApplicationNames()}. */
  CompletableFuture<? extends LocalizedText @Nullable []> readApplicationNamesAsync();

  /** Asynchronous form of {@link #writeApplicationNames}; completes with the operation status. */
  CompletableFuture<StatusCode> writeApplicationNamesAsync(LocalizedText @Nullable [] value);

  /**
   * Resolves the optional HasSecureElement child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getHasSecureElementNode() throws UaException;

  /** Asynchronous form of {@link #getHasSecureElementNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getHasSecureElementNodeAsync();

  /**
   * Reads the Value of the HasSecureElement child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readHasSecureElement() throws UaException;

  /**
   * Writes the Value of the HasSecureElement child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeHasSecureElement(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readHasSecureElement()}. */
  CompletableFuture<? extends @Nullable Boolean> readHasSecureElementAsync();

  /** Asynchronous form of {@link #writeHasSecureElement}; completes with the operation status. */
  CompletableFuture<StatusCode> writeHasSecureElementAsync(@Nullable Boolean value);

  /**
   * Resolves the mandatory MaxTrustListSize child, a PropertyType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getMaxTrustListSizeNode() throws UaException;

  /** Asynchronous form of {@link #getMaxTrustListSizeNode()}. */
  CompletableFuture<? extends PropertyType> getMaxTrustListSizeNodeAsync();

  /**
   * Reads the Value of the MaxTrustListSize child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxTrustListSize() throws UaException;

  /**
   * Writes the Value of the MaxTrustListSize child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxTrustListSize(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxTrustListSize()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxTrustListSizeAsync();

  /** Asynchronous form of {@link #writeMaxTrustListSize}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxTrustListSizeAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory CertificateGroups child, a CertificateGroupFolderType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.3">CertificateGroupFolderType
   *     documentation</a>
   */
  CertificateGroupFolderType getCertificateGroupsNode() throws UaException;

  /** Asynchronous form of {@link #getCertificateGroupsNode()}. */
  CompletableFuture<? extends CertificateGroupFolderType> getCertificateGroupsNodeAsync();

  /**
   * Resolves the optional ConfigurationFile child, a ApplicationConfigurationFileType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part26/5.4">Model
   *     documentation</a>
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.20">ApplicationConfigurationFileType
   *     documentation</a>
   */
  @Nullable ApplicationConfigurationFileType getConfigurationFileNode() throws UaException;

  /** Asynchronous form of {@link #getConfigurationFileNode()}. */
  CompletableFuture<? extends @Nullable ApplicationConfigurationFileType>
      getConfigurationFileNodeAsync();

  /**
   * Resolves the optional InApplicationSetup child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getInApplicationSetupNode() throws UaException;

  /** Asynchronous form of {@link #getInApplicationSetupNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getInApplicationSetupNodeAsync();

  /**
   * Reads the Value of the InApplicationSetup child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readInApplicationSetup() throws UaException;

  /**
   * Writes the Value of the InApplicationSetup child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeInApplicationSetup(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readInApplicationSetup()}. */
  CompletableFuture<? extends @Nullable Boolean> readInApplicationSetupAsync();

  /** Asynchronous form of {@link #writeInApplicationSetup}; completes with the operation status. */
  CompletableFuture<StatusCode> writeInApplicationSetupAsync(@Nullable Boolean value);

  /**
   * Resolves the mandatory ServerCapabilities child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getServerCapabilitiesNode() throws UaException;

  /** Asynchronous form of {@link #getServerCapabilitiesNode()}. */
  CompletableFuture<? extends PropertyType> getServerCapabilitiesNodeAsync();

  /**
   * Reads the Value of the ServerCapabilities child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String @Nullable [] readServerCapabilities() throws UaException;

  /**
   * Writes the Value of the ServerCapabilities child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeServerCapabilities(@Nullable String @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readServerCapabilities()}. */
  CompletableFuture<? extends @Nullable String @Nullable []> readServerCapabilitiesAsync();

  /** Asynchronous form of {@link #writeServerCapabilities}; completes with the operation status. */
  CompletableFuture<StatusCode> writeServerCapabilitiesAsync(@Nullable String @Nullable [] value);

  /**
   * Resolves the mandatory MulticastDnsEnabled child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getMulticastDnsEnabledNode() throws UaException;

  /** Asynchronous form of {@link #getMulticastDnsEnabledNode()}. */
  CompletableFuture<? extends PropertyType> getMulticastDnsEnabledNodeAsync();

  /**
   * Reads the Value of the MulticastDnsEnabled child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readMulticastDnsEnabled() throws UaException;

  /**
   * Writes the Value of the MulticastDnsEnabled child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMulticastDnsEnabled(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readMulticastDnsEnabled()}. */
  CompletableFuture<? extends @Nullable Boolean> readMulticastDnsEnabledAsync();

  /**
   * Asynchronous form of {@link #writeMulticastDnsEnabled}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeMulticastDnsEnabledAsync(@Nullable Boolean value);

  /**
   * Resolves the optional SupportsTransactions child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getSupportsTransactionsNode() throws UaException;

  /** Asynchronous form of {@link #getSupportsTransactionsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getSupportsTransactionsNodeAsync();

  /**
   * Reads the Value of the SupportsTransactions child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readSupportsTransactions() throws UaException;

  /**
   * Writes the Value of the SupportsTransactions child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSupportsTransactions(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readSupportsTransactions()}. */
  CompletableFuture<? extends @Nullable Boolean> readSupportsTransactionsAsync();

  /**
   * Asynchronous form of {@link #writeSupportsTransactions}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeSupportsTransactionsAsync(@Nullable Boolean value);

  /**
   * Resolves the optional TransactionDiagnostics child, a TransactionDiagnosticsType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.17">TransactionDiagnosticsType
   *     documentation</a>
   */
  @Nullable TransactionDiagnosticsType getTransactionDiagnosticsNode() throws UaException;

  /** Asynchronous form of {@link #getTransactionDiagnosticsNode()}. */
  CompletableFuture<? extends @Nullable TransactionDiagnosticsType>
      getTransactionDiagnosticsNodeAsync();

  /**
   * Resolves the mandatory SupportedPrivateKeyFormats child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSupportedPrivateKeyFormatsNode() throws UaException;

  /** Asynchronous form of {@link #getSupportedPrivateKeyFormatsNode()}. */
  CompletableFuture<? extends PropertyType> getSupportedPrivateKeyFormatsNodeAsync();

  /**
   * Reads the Value of the SupportedPrivateKeyFormats child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String @Nullable [] readSupportedPrivateKeyFormats() throws UaException;

  /**
   * Writes the Value of the SupportedPrivateKeyFormats child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSupportedPrivateKeyFormats(@Nullable String @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readSupportedPrivateKeyFormats()}. */
  CompletableFuture<? extends @Nullable String @Nullable []> readSupportedPrivateKeyFormatsAsync();

  /**
   * Asynchronous form of {@link #writeSupportedPrivateKeyFormats}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeSupportedPrivateKeyFormatsAsync(
      @Nullable String @Nullable [] value);

  /**
   * Resolves the mandatory ApplyChanges Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.9">Model
   *     documentation</a>
   */
  UaMethodNode getApplyChangesMethodNode() throws UaException;

  /** Asynchronous form of {@link #getApplyChangesMethodNode()}. */
  CompletableFuture<UaMethodNode> getApplyChangesMethodNodeAsync();

  /**
   * Calls the ApplyChanges Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.9">Model
   *     documentation</a>
   */
  void applyChanges() throws UaException;

  /**
   * Calls the ApplyChanges Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callApplyChanges() throws UaException;

  /**
   * Calls the ApplyChanges Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callApplyChangesWith(MethodCallOptions options) throws UaException;

  /** Asynchronous form of {@link #applyChanges}. */
  CompletableFuture<Void> applyChangesAsync();

  /** Asynchronous form of {@link #callApplyChanges}. */
  CompletableFuture<MethodCallResult<Void>> callApplyChangesAsync();

  /** Asynchronous form of {@link #callApplyChangesWith}. */
  CompletableFuture<MethodCallResult<Void>> callApplyChangesWithAsync(MethodCallOptions options);

  /**
   * Resolves the optional CancelChanges Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.11">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getCancelChangesMethodNode() throws UaException;

  /** Asynchronous form of {@link #getCancelChangesMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getCancelChangesMethodNodeAsync();

  /**
   * Calls the CancelChanges Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.11">Model
   *     documentation</a>
   */
  void cancelChanges() throws UaException;

  /**
   * Calls the CancelChanges Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callCancelChanges() throws UaException;

  /**
   * Calls the CancelChanges Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callCancelChangesWith(MethodCallOptions options) throws UaException;

  /** Asynchronous form of {@link #cancelChanges}. */
  CompletableFuture<Void> cancelChangesAsync();

  /** Asynchronous form of {@link #callCancelChanges}. */
  CompletableFuture<MethodCallResult<Void>> callCancelChangesAsync();

  /** Asynchronous form of {@link #callCancelChangesWith}. */
  CompletableFuture<MethodCallResult<Void>> callCancelChangesWithAsync(MethodCallOptions options);

  /**
   * Resolves the optional CreateSelfSignedCertificate Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getCreateSelfSignedCertificateMethodNode() throws UaException;

  /** Asynchronous form of {@link #getCreateSelfSignedCertificateMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getCreateSelfSignedCertificateMethodNodeAsync();

  /**
   * Calls the CreateSelfSignedCertificate Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6">Model
   *     documentation</a>
   */
  @Nullable ByteString createSelfSignedCertificate(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable String @Nullable [] dnsNames,
      @Nullable String @Nullable [] ipAddresses,
      @Nullable UShort lifetimeInDays,
      @Nullable UShort keySizeInBits)
      throws UaException;

  /**
   * Calls the CreateSelfSignedCertificate Method and returns the complete result, including a Bad
   * status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable ByteString> callCreateSelfSignedCertificate(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable String @Nullable [] dnsNames,
      @Nullable String @Nullable [] ipAddresses,
      @Nullable UShort lifetimeInDays,
      @Nullable UShort keySizeInBits)
      throws UaException;

  /**
   * Calls the CreateSelfSignedCertificate Method with explicit options and returns the complete
   * result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable ByteString> callCreateSelfSignedCertificateWith(
      MethodCallOptions options,
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable String @Nullable [] dnsNames,
      @Nullable String @Nullable [] ipAddresses,
      @Nullable UShort lifetimeInDays,
      @Nullable UShort keySizeInBits)
      throws UaException;

  /** Asynchronous form of {@link #createSelfSignedCertificate}. */
  CompletableFuture<@Nullable ByteString> createSelfSignedCertificateAsync(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable String @Nullable [] dnsNames,
      @Nullable String @Nullable [] ipAddresses,
      @Nullable UShort lifetimeInDays,
      @Nullable UShort keySizeInBits);

  /** Asynchronous form of {@link #callCreateSelfSignedCertificate}. */
  CompletableFuture<MethodCallResult<@Nullable ByteString>> callCreateSelfSignedCertificateAsync(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable String @Nullable [] dnsNames,
      @Nullable String @Nullable [] ipAddresses,
      @Nullable UShort lifetimeInDays,
      @Nullable UShort keySizeInBits);

  /** Asynchronous form of {@link #callCreateSelfSignedCertificateWith}. */
  CompletableFuture<MethodCallResult<@Nullable ByteString>>
      callCreateSelfSignedCertificateWithAsync(
          MethodCallOptions options,
          @Nullable NodeId certificateGroupId,
          @Nullable NodeId certificateTypeId,
          @Nullable String subjectName,
          @Nullable String @Nullable [] dnsNames,
          @Nullable String @Nullable [] ipAddresses,
          @Nullable UShort lifetimeInDays,
          @Nullable UShort keySizeInBits);

  /**
   * Resolves the mandatory CreateSigningRequest Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.10">Model
   *     documentation</a>
   */
  UaMethodNode getCreateSigningRequestMethodNode() throws UaException;

  /** Asynchronous form of {@link #getCreateSigningRequestMethodNode()}. */
  CompletableFuture<UaMethodNode> getCreateSigningRequestMethodNodeAsync();

  /**
   * Calls the CreateSigningRequest Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.10">Model
   *     documentation</a>
   */
  @Nullable ByteString createSigningRequest(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable Boolean regeneratePrivateKey,
      @Nullable ByteString nonce)
      throws UaException;

  /**
   * Calls the CreateSigningRequest Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable ByteString> callCreateSigningRequest(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable Boolean regeneratePrivateKey,
      @Nullable ByteString nonce)
      throws UaException;

  /**
   * Calls the CreateSigningRequest Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable ByteString> callCreateSigningRequestWith(
      MethodCallOptions options,
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable Boolean regeneratePrivateKey,
      @Nullable ByteString nonce)
      throws UaException;

  /** Asynchronous form of {@link #createSigningRequest}. */
  CompletableFuture<@Nullable ByteString> createSigningRequestAsync(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable Boolean regeneratePrivateKey,
      @Nullable ByteString nonce);

  /** Asynchronous form of {@link #callCreateSigningRequest}. */
  CompletableFuture<MethodCallResult<@Nullable ByteString>> callCreateSigningRequestAsync(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable Boolean regeneratePrivateKey,
      @Nullable ByteString nonce);

  /** Asynchronous form of {@link #callCreateSigningRequestWith}. */
  CompletableFuture<MethodCallResult<@Nullable ByteString>> callCreateSigningRequestWithAsync(
      MethodCallOptions options,
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable Boolean regeneratePrivateKey,
      @Nullable ByteString nonce);

  /**
   * Resolves the optional DeleteCertificate Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.7">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getDeleteCertificateMethodNode() throws UaException;

  /** Asynchronous form of {@link #getDeleteCertificateMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getDeleteCertificateMethodNodeAsync();

  /**
   * Calls the DeleteCertificate Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.7">Model
   *     documentation</a>
   */
  void deleteCertificate(@Nullable NodeId certificateGroupId, @Nullable NodeId certificateTypeId)
      throws UaException;

  /**
   * Calls the DeleteCertificate Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callDeleteCertificate(
      @Nullable NodeId certificateGroupId, @Nullable NodeId certificateTypeId) throws UaException;

  /**
   * Calls the DeleteCertificate Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callDeleteCertificateWith(
      MethodCallOptions options,
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId)
      throws UaException;

  /** Asynchronous form of {@link #deleteCertificate}. */
  CompletableFuture<Void> deleteCertificateAsync(
      @Nullable NodeId certificateGroupId, @Nullable NodeId certificateTypeId);

  /** Asynchronous form of {@link #callDeleteCertificate}. */
  CompletableFuture<MethodCallResult<Void>> callDeleteCertificateAsync(
      @Nullable NodeId certificateGroupId, @Nullable NodeId certificateTypeId);

  /** Asynchronous form of {@link #callDeleteCertificateWith}. */
  CompletableFuture<MethodCallResult<Void>> callDeleteCertificateWithAsync(
      MethodCallOptions options,
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId);

  /**
   * Resolves the optional GetCertificates Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.8">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getGetCertificatesMethodNode() throws UaException;

  /** Asynchronous form of {@link #getGetCertificatesMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getGetCertificatesMethodNodeAsync();

  /**
   * Calls the GetCertificates Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.8">Model
   *     documentation</a>
   */
  ServerConfigurationTypeGetCertificates.Outputs getCertificates(
      @Nullable NodeId certificateGroupId) throws UaException;

  /**
   * Calls the GetCertificates Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<ServerConfigurationTypeGetCertificates.Outputs> callGetCertificates(
      @Nullable NodeId certificateGroupId) throws UaException;

  /**
   * Calls the GetCertificates Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<ServerConfigurationTypeGetCertificates.Outputs> callGetCertificatesWith(
      MethodCallOptions options, @Nullable NodeId certificateGroupId) throws UaException;

  /** Asynchronous form of {@link #getCertificates}. */
  CompletableFuture<ServerConfigurationTypeGetCertificates.Outputs> getCertificatesAsync(
      @Nullable NodeId certificateGroupId);

  /** Asynchronous form of {@link #callGetCertificates}. */
  CompletableFuture<MethodCallResult<ServerConfigurationTypeGetCertificates.Outputs>>
      callGetCertificatesAsync(@Nullable NodeId certificateGroupId);

  /** Asynchronous form of {@link #callGetCertificatesWith}. */
  CompletableFuture<MethodCallResult<ServerConfigurationTypeGetCertificates.Outputs>>
      callGetCertificatesWithAsync(MethodCallOptions options, @Nullable NodeId certificateGroupId);

  /**
   * Resolves the mandatory GetRejectedList Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.12">Model
   *     documentation</a>
   */
  UaMethodNode getGetRejectedListMethodNode() throws UaException;

  /** Asynchronous form of {@link #getGetRejectedListMethodNode()}. */
  CompletableFuture<UaMethodNode> getGetRejectedListMethodNodeAsync();

  /**
   * Calls the GetRejectedList Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.12">Model
   *     documentation</a>
   */
  ByteString @Nullable [] getRejectedList() throws UaException;

  /**
   * Calls the GetRejectedList Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<ByteString @Nullable []> callGetRejectedList() throws UaException;

  /**
   * Calls the GetRejectedList Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<ByteString @Nullable []> callGetRejectedListWith(MethodCallOptions options)
      throws UaException;

  /** Asynchronous form of {@link #getRejectedList}. */
  CompletableFuture<ByteString @Nullable []> getRejectedListAsync();

  /** Asynchronous form of {@link #callGetRejectedList}. */
  CompletableFuture<MethodCallResult<ByteString @Nullable []>> callGetRejectedListAsync();

  /** Asynchronous form of {@link #callGetRejectedListWith}. */
  CompletableFuture<MethodCallResult<ByteString @Nullable []>> callGetRejectedListWithAsync(
      MethodCallOptions options);

  /**
   * Resolves the optional ResetToServerDefaults Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.13">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getResetToServerDefaultsMethodNode() throws UaException;

  /** Asynchronous form of {@link #getResetToServerDefaultsMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getResetToServerDefaultsMethodNodeAsync();

  /**
   * Calls the ResetToServerDefaults Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.13">Model
   *     documentation</a>
   */
  void resetToServerDefaults() throws UaException;

  /**
   * Calls the ResetToServerDefaults Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callResetToServerDefaults() throws UaException;

  /**
   * Calls the ResetToServerDefaults Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callResetToServerDefaultsWith(MethodCallOptions options)
      throws UaException;

  /** Asynchronous form of {@link #resetToServerDefaults}. */
  CompletableFuture<Void> resetToServerDefaultsAsync();

  /** Asynchronous form of {@link #callResetToServerDefaults}. */
  CompletableFuture<MethodCallResult<Void>> callResetToServerDefaultsAsync();

  /** Asynchronous form of {@link #callResetToServerDefaultsWith}. */
  CompletableFuture<MethodCallResult<Void>> callResetToServerDefaultsWithAsync(
      MethodCallOptions options);

  /**
   * Resolves the mandatory UpdateCertificate Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.5">Model
   *     documentation</a>
   */
  UaMethodNode getUpdateCertificateMethodNode() throws UaException;

  /** Asynchronous form of {@link #getUpdateCertificateMethodNode()}. */
  CompletableFuture<UaMethodNode> getUpdateCertificateMethodNodeAsync();

  /**
   * Calls the UpdateCertificate Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.5">Model
   *     documentation</a>
   */
  @Nullable Boolean updateCertificate(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable ByteString certificate,
      ByteString @Nullable [] issuerCertificates,
      @Nullable String privateKeyFormat,
      @Nullable ByteString privateKey)
      throws UaException;

  /**
   * Calls the UpdateCertificate Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable Boolean> callUpdateCertificate(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable ByteString certificate,
      ByteString @Nullable [] issuerCertificates,
      @Nullable String privateKeyFormat,
      @Nullable ByteString privateKey)
      throws UaException;

  /**
   * Calls the UpdateCertificate Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable Boolean> callUpdateCertificateWith(
      MethodCallOptions options,
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable ByteString certificate,
      ByteString @Nullable [] issuerCertificates,
      @Nullable String privateKeyFormat,
      @Nullable ByteString privateKey)
      throws UaException;

  /** Asynchronous form of {@link #updateCertificate}. */
  CompletableFuture<@Nullable Boolean> updateCertificateAsync(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable ByteString certificate,
      ByteString @Nullable [] issuerCertificates,
      @Nullable String privateKeyFormat,
      @Nullable ByteString privateKey);

  /** Asynchronous form of {@link #callUpdateCertificate}. */
  CompletableFuture<MethodCallResult<@Nullable Boolean>> callUpdateCertificateAsync(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable ByteString certificate,
      ByteString @Nullable [] issuerCertificates,
      @Nullable String privateKeyFormat,
      @Nullable ByteString privateKey);

  /** Asynchronous form of {@link #callUpdateCertificateWith}. */
  CompletableFuture<MethodCallResult<@Nullable Boolean>> callUpdateCertificateWithAsync(
      MethodCallOptions options,
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable ByteString certificate,
      ByteString @Nullable [] issuerCertificates,
      @Nullable String privateKeyFormat,
      @Nullable ByteString privateKey);
}
