/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallOptions;
import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallResult;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerConfigurationTypeGetCertificatesOutputs;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.jspecify.annotations.NullMarked;
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
  @Nullable String getApplicationUri() throws UaException;

  /** Sets the existing node's local value. */
  void setApplicationUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readApplicationUri() throws UaException;

  /** Writes the value remotely. */
  void writeApplicationUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readApplicationUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeApplicationUriAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getApplicationUriNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getApplicationUriNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getProductUri() throws UaException;

  /** Sets the existing node's local value. */
  void setProductUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readProductUri() throws UaException;

  /** Writes the value remotely. */
  void writeProductUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readProductUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeProductUriAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getProductUriNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getProductUriNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ApplicationType getApplicationType() throws UaException;

  /** Sets the existing node's local value. */
  void setApplicationType(@Nullable ApplicationType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ApplicationType readApplicationType() throws UaException;

  /** Writes the value remotely. */
  void writeApplicationType(@Nullable ApplicationType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ApplicationType> readApplicationTypeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeApplicationTypeAsync(@Nullable ApplicationType value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getApplicationTypeNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getApplicationTypeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText @Nullable [] getApplicationNames() throws UaException;

  /** Sets the existing node's local value. */
  void setApplicationNames(@Nullable LocalizedText @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText @Nullable [] readApplicationNames() throws UaException;

  /** Writes the value remotely. */
  void writeApplicationNames(@Nullable LocalizedText @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText @Nullable []> readApplicationNamesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeApplicationNamesAsync(
      @Nullable LocalizedText @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getApplicationNamesNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getApplicationNamesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getServerCapabilities() throws UaException;

  /** Sets the existing node's local value. */
  void setServerCapabilities(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String @Nullable [] readServerCapabilities() throws UaException;

  /** Writes the value remotely. */
  void writeServerCapabilities(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String @Nullable []> readServerCapabilitiesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeServerCapabilitiesAsync(@Nullable String @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getServerCapabilitiesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getServerCapabilitiesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getSupportedPrivateKeyFormats() throws UaException;

  /** Sets the existing node's local value. */
  void setSupportedPrivateKeyFormats(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String @Nullable [] readSupportedPrivateKeyFormats() throws UaException;

  /** Writes the value remotely. */
  void writeSupportedPrivateKeyFormats(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String @Nullable []> readSupportedPrivateKeyFormatsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSupportedPrivateKeyFormatsAsync(
      @Nullable String @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSupportedPrivateKeyFormatsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getSupportedPrivateKeyFormatsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxTrustListSize() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxTrustListSize(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxTrustListSize() throws UaException;

  /** Writes the value remotely. */
  void writeMaxTrustListSize(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxTrustListSizeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxTrustListSizeAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxTrustListSizeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getMaxTrustListSizeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getMulticastDnsEnabled() throws UaException;

  /** Sets the existing node's local value. */
  void setMulticastDnsEnabled(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readMulticastDnsEnabled() throws UaException;

  /** Writes the value remotely. */
  void writeMulticastDnsEnabled(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readMulticastDnsEnabledAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMulticastDnsEnabledAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMulticastDnsEnabledNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getMulticastDnsEnabledNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getHasSecureElement() throws UaException;

  /** Sets the existing node's local value. */
  void setHasSecureElement(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readHasSecureElement() throws UaException;

  /** Writes the value remotely. */
  void writeHasSecureElement(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readHasSecureElementAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeHasSecureElementAsync(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getHasSecureElementNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getHasSecureElementNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getSupportsTransactions() throws UaException;

  /** Sets the existing node's local value. */
  void setSupportsTransactions(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readSupportsTransactions() throws UaException;

  /** Writes the value remotely. */
  void writeSupportsTransactions(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readSupportsTransactionsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSupportsTransactionsAsync(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSupportsTransactionsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getSupportsTransactionsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getInApplicationSetup() throws UaException;

  /** Sets the existing node's local value. */
  void setInApplicationSetup(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readInApplicationSetup() throws UaException;

  /** Writes the value remotely. */
  void writeInApplicationSetup(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readInApplicationSetupAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeInApplicationSetupAsync(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getInApplicationSetupNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getInApplicationSetupNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.5
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getUpdateCertificateMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.5
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getUpdateCertificateMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.5
   *
   * <p>Invokes <code>UpdateCertificate</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable Boolean callUpdateCertificate(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable ByteString certificate,
      @Nullable ByteString @Nullable [] issuerCertificates,
      @Nullable String privateKeyFormat,
      @Nullable ByteString privateKey)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.5
   *
   * <p>Invokes <code>UpdateCertificate</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Boolean> callUpdateCertificateAsync(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable ByteString certificate,
      @Nullable ByteString @Nullable [] issuerCertificates,
      @Nullable String privateKeyFormat,
      @Nullable ByteString privateKey);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.5
   *
   * <p>Invokes <code>UpdateCertificate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Boolean> callUpdateCertificateDetailed(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable ByteString certificate,
      @Nullable ByteString @Nullable [] issuerCertificates,
      @Nullable String privateKeyFormat,
      @Nullable ByteString privateKey)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.5
   *
   * <p>Invokes <code>UpdateCertificate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Boolean> callUpdateCertificateDetailed(
      MethodCallOptions options,
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable ByteString certificate,
      @Nullable ByteString @Nullable [] issuerCertificates,
      @Nullable String privateKeyFormat,
      @Nullable ByteString privateKey)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.5
   *
   * <p>Invokes <code>UpdateCertificate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Boolean>>
      callUpdateCertificateDetailedAsync(
          @Nullable NodeId certificateGroupId,
          @Nullable NodeId certificateTypeId,
          @Nullable ByteString certificate,
          @Nullable ByteString @Nullable [] issuerCertificates,
          @Nullable String privateKeyFormat,
          @Nullable ByteString privateKey);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.5
   *
   * <p>Invokes <code>UpdateCertificate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Boolean>>
      callUpdateCertificateDetailedAsync(
          MethodCallOptions options,
          @Nullable NodeId certificateGroupId,
          @Nullable NodeId certificateTypeId,
          @Nullable ByteString certificate,
          @Nullable ByteString @Nullable [] issuerCertificates,
          @Nullable String privateKeyFormat,
          @Nullable ByteString privateKey);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getCreateSelfSignedCertificateMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode>
      getCreateSelfSignedCertificateMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6
   *
   * <p>Invokes <code>CreateSelfSignedCertificate</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable ByteString callCreateSelfSignedCertificate(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable String @Nullable [] dnsNames,
      @Nullable String @Nullable [] ipAddresses,
      @Nullable UShort lifetimeInDays,
      @Nullable UShort keySizeInBits)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6
   *
   * <p>Invokes <code>CreateSelfSignedCertificate</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable ByteString> callCreateSelfSignedCertificateAsync(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable String @Nullable [] dnsNames,
      @Nullable String @Nullable [] ipAddresses,
      @Nullable UShort lifetimeInDays,
      @Nullable UShort keySizeInBits);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6
   *
   * <p>Invokes <code>CreateSelfSignedCertificate</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable ByteString> callCreateSelfSignedCertificateDetailed(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable String @Nullable [] dnsNames,
      @Nullable String @Nullable [] ipAddresses,
      @Nullable UShort lifetimeInDays,
      @Nullable UShort keySizeInBits)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6
   *
   * <p>Invokes <code>CreateSelfSignedCertificate</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable ByteString> callCreateSelfSignedCertificateDetailed(
      MethodCallOptions options,
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable String @Nullable [] dnsNames,
      @Nullable String @Nullable [] ipAddresses,
      @Nullable UShort lifetimeInDays,
      @Nullable UShort keySizeInBits)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6
   *
   * <p>Invokes <code>CreateSelfSignedCertificate</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable ByteString>>
      callCreateSelfSignedCertificateDetailedAsync(
          @Nullable NodeId certificateGroupId,
          @Nullable NodeId certificateTypeId,
          @Nullable String subjectName,
          @Nullable String @Nullable [] dnsNames,
          @Nullable String @Nullable [] ipAddresses,
          @Nullable UShort lifetimeInDays,
          @Nullable UShort keySizeInBits);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6
   *
   * <p>Invokes <code>CreateSelfSignedCertificate</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable ByteString>>
      callCreateSelfSignedCertificateDetailedAsync(
          MethodCallOptions options,
          @Nullable NodeId certificateGroupId,
          @Nullable NodeId certificateTypeId,
          @Nullable String subjectName,
          @Nullable String @Nullable [] dnsNames,
          @Nullable String @Nullable [] ipAddresses,
          @Nullable UShort lifetimeInDays,
          @Nullable UShort keySizeInBits);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.7
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getDeleteCertificateMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.7
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getDeleteCertificateMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.7
   *
   * <p>Invokes <code>DeleteCertificate</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callDeleteCertificate(
      @Nullable NodeId certificateGroupId, @Nullable NodeId certificateTypeId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.7
   *
   * <p>Invokes <code>DeleteCertificate</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callDeleteCertificateAsync(
      @Nullable NodeId certificateGroupId, @Nullable NodeId certificateTypeId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.7
   *
   * <p>Invokes <code>DeleteCertificate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callDeleteCertificateDetailed(
      @Nullable NodeId certificateGroupId, @Nullable NodeId certificateTypeId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.7
   *
   * <p>Invokes <code>DeleteCertificate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callDeleteCertificateDetailed(
      MethodCallOptions options,
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.7
   *
   * <p>Invokes <code>DeleteCertificate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callDeleteCertificateDetailedAsync(
          @Nullable NodeId certificateGroupId, @Nullable NodeId certificateTypeId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.7
   *
   * <p>Invokes <code>DeleteCertificate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callDeleteCertificateDetailedAsync(
          MethodCallOptions options,
          @Nullable NodeId certificateGroupId,
          @Nullable NodeId certificateTypeId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.8
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getGetCertificatesMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.8
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getGetCertificatesMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.8
   *
   * <p>Invokes <code>GetCertificates</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  ServerConfigurationTypeGetCertificatesOutputs callGetCertificates(
      @Nullable NodeId certificateGroupId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.8
   *
   * <p>Invokes <code>GetCertificates</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends ServerConfigurationTypeGetCertificatesOutputs>
      callGetCertificatesAsync(@Nullable NodeId certificateGroupId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.8
   *
   * <p>Invokes <code>GetCertificates</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends ServerConfigurationTypeGetCertificatesOutputs>
      callGetCertificatesDetailed(@Nullable NodeId certificateGroupId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.8
   *
   * <p>Invokes <code>GetCertificates</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends ServerConfigurationTypeGetCertificatesOutputs>
      callGetCertificatesDetailed(MethodCallOptions options, @Nullable NodeId certificateGroupId)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.8
   *
   * <p>Invokes <code>GetCertificates</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<
          ? extends MethodCallResult<? extends ServerConfigurationTypeGetCertificatesOutputs>>
      callGetCertificatesDetailedAsync(@Nullable NodeId certificateGroupId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.8
   *
   * <p>Invokes <code>GetCertificates</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<
          ? extends MethodCallResult<? extends ServerConfigurationTypeGetCertificatesOutputs>>
      callGetCertificatesDetailedAsync(
          MethodCallOptions options, @Nullable NodeId certificateGroupId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.9
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getApplyChangesMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.9
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getApplyChangesMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.9
   *
   * <p>Invokes <code>ApplyChanges</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callApplyChanges() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.9
   *
   * <p>Invokes <code>ApplyChanges</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callApplyChangesAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.9
   *
   * <p>Invokes <code>ApplyChanges</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callApplyChangesDetailed() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.9
   *
   * <p>Invokes <code>ApplyChanges</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callApplyChangesDetailed(MethodCallOptions options)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.9
   *
   * <p>Invokes <code>ApplyChanges</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callApplyChangesDetailedAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.9
   *
   * <p>Invokes <code>ApplyChanges</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callApplyChangesDetailedAsync(MethodCallOptions options);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.11
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getCancelChangesMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.11
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getCancelChangesMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.11
   *
   * <p>Invokes <code>CancelChanges</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callCancelChanges() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.11
   *
   * <p>Invokes <code>CancelChanges</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callCancelChangesAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.11
   *
   * <p>Invokes <code>CancelChanges</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callCancelChangesDetailed() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.11
   *
   * <p>Invokes <code>CancelChanges</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callCancelChangesDetailed(MethodCallOptions options)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.11
   *
   * <p>Invokes <code>CancelChanges</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callCancelChangesDetailedAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.11
   *
   * <p>Invokes <code>CancelChanges</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callCancelChangesDetailedAsync(MethodCallOptions options);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.10
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getCreateSigningRequestMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.10
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getCreateSigningRequestMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.10
   *
   * <p>Invokes <code>CreateSigningRequest</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable ByteString callCreateSigningRequest(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable Boolean regeneratePrivateKey,
      @Nullable ByteString nonce)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.10
   *
   * <p>Invokes <code>CreateSigningRequest</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable ByteString> callCreateSigningRequestAsync(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable Boolean regeneratePrivateKey,
      @Nullable ByteString nonce);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.10
   *
   * <p>Invokes <code>CreateSigningRequest</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable ByteString> callCreateSigningRequestDetailed(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable Boolean regeneratePrivateKey,
      @Nullable ByteString nonce)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.10
   *
   * <p>Invokes <code>CreateSigningRequest</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable ByteString> callCreateSigningRequestDetailed(
      MethodCallOptions options,
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable Boolean regeneratePrivateKey,
      @Nullable ByteString nonce)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.10
   *
   * <p>Invokes <code>CreateSigningRequest</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable ByteString>>
      callCreateSigningRequestDetailedAsync(
          @Nullable NodeId certificateGroupId,
          @Nullable NodeId certificateTypeId,
          @Nullable String subjectName,
          @Nullable Boolean regeneratePrivateKey,
          @Nullable ByteString nonce);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.10
   *
   * <p>Invokes <code>CreateSigningRequest</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable ByteString>>
      callCreateSigningRequestDetailedAsync(
          MethodCallOptions options,
          @Nullable NodeId certificateGroupId,
          @Nullable NodeId certificateTypeId,
          @Nullable String subjectName,
          @Nullable Boolean regeneratePrivateKey,
          @Nullable ByteString nonce);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.12
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getGetRejectedListMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.12
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getGetRejectedListMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.12
   *
   * <p>Invokes <code>GetRejectedList</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable ByteString @Nullable [] callGetRejectedList() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.12
   *
   * <p>Invokes <code>GetRejectedList</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable ByteString @Nullable []> callGetRejectedListAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.12
   *
   * <p>Invokes <code>GetRejectedList</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable ByteString @Nullable []> callGetRejectedListDetailed()
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.12
   *
   * <p>Invokes <code>GetRejectedList</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable ByteString @Nullable []> callGetRejectedListDetailed(
      MethodCallOptions options) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.12
   *
   * <p>Invokes <code>GetRejectedList</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable ByteString @Nullable []>>
      callGetRejectedListDetailedAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.12
   *
   * <p>Invokes <code>GetRejectedList</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable ByteString @Nullable []>>
      callGetRejectedListDetailedAsync(MethodCallOptions options);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.13
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getResetToServerDefaultsMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.13
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getResetToServerDefaultsMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.13
   *
   * <p>Invokes <code>ResetToServerDefaults</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callResetToServerDefaults() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.13
   *
   * <p>Invokes <code>ResetToServerDefaults</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callResetToServerDefaultsAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.13
   *
   * <p>Invokes <code>ResetToServerDefaults</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callResetToServerDefaultsDetailed() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.13
   *
   * <p>Invokes <code>ResetToServerDefaults</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callResetToServerDefaultsDetailed(
      MethodCallOptions options) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.13
   *
   * <p>Invokes <code>ResetToServerDefaults</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callResetToServerDefaultsDetailedAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.13
   *
   * <p>Invokes <code>ResetToServerDefaults</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callResetToServerDefaultsDetailedAsync(MethodCallOptions options);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  CertificateGroupFolderType getCertificateGroupsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends CertificateGroupFolderType> getCertificateGroupsNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable TransactionDiagnosticsType getTransactionDiagnosticsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable TransactionDiagnosticsType>
      getTransactionDiagnosticsNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part26/5.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable ApplicationConfigurationFileType getConfigurationFileNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part26/5.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable ApplicationConfigurationFileType>
      getConfigurationFileNodeAsync();
}
