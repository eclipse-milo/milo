/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.gds;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.failedFuture;

import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.gds.model.ObjectTypeInitializer;
import org.eclipse.milo.opcua.sdk.client.gds.model.VariableTypeInitializer;
import org.eclipse.milo.opcua.sdk.client.methods.UaMethodException;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.gds.DataTypeInitializer;
import org.eclipse.milo.opcua.stack.core.gds.GdsNodeIds;
import org.eclipse.milo.opcua.stack.core.gds.types.ApplicationRecordDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerOnNetwork;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.jspecify.annotations.Nullable;

/**
 * Typed access to the {@code Directory} object of an OPC UA Global Discovery Server (OPC 10000-12).
 *
 * <p>A {@link GdsClient} wraps an {@link OpcUaClient} that is already connected to a GDS. {@link
 * #create(OpcUaClient)} resolves the GDS namespace index, registers the GDS DataType and ObjectType
 * model with the client, and locates the {@code Directory} object; every method wrapper then issues
 * a single Call request. Each wrapper has a blocking form and an {@code ...Async} twin returning a
 * {@link CompletableFuture}.
 *
 * <p>Wrappers do not interpret results. A Bad operation-level result surfaces as a {@link
 * UaMethodException} (a {@link UaException}) carrying the GDS's {@link StatusCode}, so callers
 * branch on it directly, for example {@link StatusCodes#Bad_NothingToDo} from {@link
 * #finishRequest} means "poll again later" and {@link StatusCodes#Bad_RequestNotAllowed} means the
 * request was rejected.
 *
 * <p>{@link #resolveCertificateTypeId(NodeId, NodeId)} is the exception to this pass-through rule:
 * it interprets a group's advertised CertificateTypes locally and reports {@link
 * StatusCodes#Bad_NotSupported} when none is compatible with the desired type.
 *
 * <p>The Pull Model sequence (Part 12 §7.6) is:
 *
 * <pre>{@code
 * GdsClient gds = GdsClient.create(client);
 *
 * ApplicationRecordDataType[] found = gds.findApplications(applicationUri);
 * NodeId applicationId =
 *     found.length == 0 ? gds.registerApplication(record) : found[0].getApplicationId();
 *
 * NodeId desiredTypeId = NodeIds.RsaSha256ApplicationCertificateType;
 * for (NodeId groupId : gds.getCertificateGroups(applicationId)) {
 *   // Groups such as DefaultUserTokenGroup do not issue application certificates and fail
 *   // with Bad_NotSupported; skip signing for them but still pull their TrustList.
 *   NodeId requestTypeId = null;
 *   boolean issuesDesiredType = true;
 *   try {
 *     requestTypeId = gds.resolveCertificateTypeId(groupId, desiredTypeId);
 *   } catch (UaException e) {
 *     if (e.getStatusCode().value() != StatusCodes.Bad_NotSupported) throw e;
 *     issuesDesiredType = false;
 *   }
 *   if (issuesDesiredType && gds.getCertificateStatus(applicationId, groupId, requestTypeId)) {
 *     NodeId requestId = gds.startSigningRequest(applicationId, groupId, requestTypeId, csr);
 *     // later, repeat until it no longer fails with Bad_NothingToDo:
 *     FinishRequestResult issued = gds.finishRequest(applicationId, requestId);
 *   }
 *   NodeId trustListId = gds.getTrustList(applicationId, groupId);
 *   TrustListDataType trustList = TrustListReader.read(client, trustListId);
 *   TrustListApplier.apply(trustList, trustListManager);
 * }
 * }</pre>
 *
 * <p>The {@code Directory} NodeId is resolved when the client is created and is valid for the GDS
 * the {@link OpcUaClient} was connected to; create a new {@link GdsClient} if the client is pointed
 * at a different server. Instances are safe to share between threads.
 */
public final class GdsClient {

  /** The URI of the GDS namespace, {@code http://opcfoundation.org/UA/GDS/}. */
  public static final String NAMESPACE_URI = "http://opcfoundation.org/UA/GDS/";

  private static final Map<NodeId, NodeId> CERTIFICATE_TYPE_SUPERTYPES =
      Map.ofEntries(
          Map.entry(NodeIds.ApplicationCertificateType, NodeIds.CertificateType),
          Map.entry(NodeIds.HttpsCertificateType, NodeIds.CertificateType),
          Map.entry(NodeIds.UserCertificateType, NodeIds.CertificateType),
          Map.entry(NodeIds.TlsCertificateType, NodeIds.CertificateType),
          Map.entry(NodeIds.TlsServerCertificateType, NodeIds.TlsCertificateType),
          Map.entry(NodeIds.TlsClientCertificateType, NodeIds.TlsCertificateType),
          Map.entry(NodeIds.RsaMinApplicationCertificateType, NodeIds.ApplicationCertificateType),
          Map.entry(
              NodeIds.RsaSha256ApplicationCertificateType, NodeIds.ApplicationCertificateType),
          Map.entry(NodeIds.EccApplicationCertificateType, NodeIds.ApplicationCertificateType),
          Map.entry(
              NodeIds.EccNistP256ApplicationCertificateType, NodeIds.EccApplicationCertificateType),
          Map.entry(
              NodeIds.EccNistP384ApplicationCertificateType, NodeIds.EccApplicationCertificateType),
          Map.entry(
              NodeIds.EccBrainpoolP256r1ApplicationCertificateType,
              NodeIds.EccApplicationCertificateType),
          Map.entry(
              NodeIds.EccBrainpoolP384r1ApplicationCertificateType,
              NodeIds.EccApplicationCertificateType),
          Map.entry(
              NodeIds.EccCurve25519ApplicationCertificateType,
              NodeIds.EccApplicationCertificateType),
          Map.entry(
              NodeIds.EccCurve448ApplicationCertificateType,
              NodeIds.EccApplicationCertificateType));

  /** Abstract CertificateTypes in the standard hierarchy. */
  private static final Set<NodeId> ABSTRACT_CERTIFICATE_TYPES =
      Set.of(
          NodeIds.CertificateType,
          NodeIds.ApplicationCertificateType,
          NodeIds.UserCertificateType,
          NodeIds.TlsCertificateType,
          NodeIds.EccApplicationCertificateType);

  private final OpcUaClient client;
  private final NodeId directoryId;

  private GdsClient(OpcUaClient client, NodeId directoryId) {
    this.client = client;
    this.directoryId = directoryId;
  }

  /**
   * Create a {@link GdsClient} for a connected {@link OpcUaClient}.
   *
   * <p>Registers the {@code ApplicationRecordDataType} codec with the client's static {@link
   * org.eclipse.milo.opcua.stack.core.types.DataTypeManager} and the GDS ObjectTypes with its
   * {@link org.eclipse.milo.opcua.sdk.client.ObjectTypeManager}. If the client's local copy of the
   * namespace array does not contain the GDS namespace it is re-read from the server once.
   *
   * @param client an {@link OpcUaClient} connected to a GDS.
   * @return a new {@link GdsClient}.
   * @throws UaException with {@link StatusCodes#Bad_NotFound} if the server's namespace array does
   *     not contain {@link #NAMESPACE_URI}, or if reading the namespace array fails.
   */
  public static GdsClient create(OpcUaClient client) throws UaException {
    NamespaceTable namespaceTable = client.getNamespaceTable();
    UShort namespaceIndex = namespaceTable.getIndex(NAMESPACE_URI);

    if (namespaceIndex == null) {
      namespaceTable = client.readNamespaceTable();
      namespaceIndex = namespaceTable.getIndex(NAMESPACE_URI);
    }

    if (namespaceIndex == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound, "GDS namespace not found in NamespaceArray: " + NAMESPACE_URI);
    }

    DataTypeInitializer.initialize(namespaceTable, client.getStaticDataTypeManager());
    ObjectTypeInitializer.initialize(namespaceTable, client.getObjectTypeManager());
    VariableTypeInitializer.initialize(namespaceTable, client.getVariableTypeManager());

    NodeId directoryId = GdsNodeIds.Directory.toNodeIdOrThrow(namespaceTable);

    return new GdsClient(client, directoryId);
  }

  /**
   * @return the {@link OpcUaClient} this GDS client wraps.
   */
  public OpcUaClient getClient() {
    return client;
  }

  /**
   * @return the {@link NodeId} of the GDS {@code Directory} object.
   */
  public NodeId getDirectoryId() {
    return directoryId;
  }

  // region Directory (Part 12 §6.5)

  /**
   * Find the applications registered with {@code applicationUri} (Part 12 §6.5.7).
   *
   * @param applicationUri the ApplicationUri to search for.
   * @return the matching records, empty if none are registered.
   * @throws UaException if the call fails.
   */
  public ApplicationRecordDataType[] findApplications(String applicationUri) throws UaException {
    return ClientCalls.await(findApplicationsAsync(applicationUri));
  }

  /**
   * Asynchronous form of {@link #findApplications(String)}.
   *
   * @param applicationUri the ApplicationUri to search for.
   * @return a future completing with the matching records.
   */
  public CompletableFuture<ApplicationRecordDataType[]> findApplicationsAsync(
      String applicationUri) {

    return call(
        GdsNodeIds.Directory_FindApplications,
        "FindApplications",
        new Variant[] {Variant.ofString(applicationUri)},
        1,
        outputs ->
            outputs.structArray(
                0, ApplicationRecordDataType.class, client.getStaticEncodingContext()));
  }

  /**
   * Register an application (Part 12 §6.5.6).
   *
   * <p>Requires the DiscoveryAdmin or ApplicationAdmin role on most servers; Part 12 §6.4 says to
   * call {@link #findApplications(String)} first and register only when it returns nothing. The
   * {@code applicationId} of {@code application} is ignored and assigned by the GDS.
   *
   * @param application the record to register.
   * @return the ApplicationId assigned by the GDS.
   * @throws UaException if the call fails, e.g. {@link StatusCodes#Bad_UserAccessDenied}.
   */
  public NodeId registerApplication(ApplicationRecordDataType application) throws UaException {
    return ClientCalls.await(registerApplicationAsync(application));
  }

  /**
   * Asynchronous form of {@link #registerApplication(ApplicationRecordDataType)}.
   *
   * @param application the record to register.
   * @return a future completing with the assigned ApplicationId.
   */
  public CompletableFuture<NodeId> registerApplicationAsync(ApplicationRecordDataType application) {
    return call(
        GdsNodeIds.Directory_RegisterApplication,
        "RegisterApplication",
        new Variant[] {Variant.ofStruct(application)},
        1,
        outputs -> outputs.scalar(0, NodeId.class));
  }

  /**
   * Update an existing registration (Part 12 §6.5.8).
   *
   * @param application the record to store; its {@code applicationId} identifies the registration.
   * @throws UaException if the call fails.
   */
  public void updateApplication(ApplicationRecordDataType application) throws UaException {
    ClientCalls.await(updateApplicationAsync(application));
  }

  /**
   * Asynchronous form of {@link #updateApplication(ApplicationRecordDataType)}.
   *
   * @param application the record to store.
   * @return a future completing when the update is done.
   */
  public CompletableFuture<Unit> updateApplicationAsync(ApplicationRecordDataType application) {
    return call(
        GdsNodeIds.Directory_UpdateApplication,
        "UpdateApplication",
        new Variant[] {Variant.ofStruct(application)},
        0,
        outputs -> Unit.VALUE);
  }

  /**
   * Remove a registration (Part 12 §6.5.9).
   *
   * @param applicationId the ApplicationId returned by registration.
   * @throws UaException if the call fails.
   */
  public void unregisterApplication(NodeId applicationId) throws UaException {
    ClientCalls.await(unregisterApplicationAsync(applicationId));
  }

  /**
   * Asynchronous form of {@link #unregisterApplication(NodeId)}.
   *
   * @param applicationId the ApplicationId returned by registration.
   * @return a future completing when the registration is removed.
   */
  public CompletableFuture<Unit> unregisterApplicationAsync(NodeId applicationId) {
    return call(
        GdsNodeIds.Directory_UnregisterApplication,
        "UnregisterApplication",
        new Variant[] {Variant.ofNodeId(applicationId)},
        0,
        outputs -> Unit.VALUE);
  }

  /**
   * Get a registration (Part 12 §6.5.10).
   *
   * @param applicationId the ApplicationId returned by registration.
   * @return the stored record.
   * @throws UaException if the call fails, e.g. {@link StatusCodes#Bad_NotFound}.
   */
  public ApplicationRecordDataType getApplication(NodeId applicationId) throws UaException {
    return ClientCalls.await(getApplicationAsync(applicationId));
  }

  /**
   * Asynchronous form of {@link #getApplication(NodeId)}.
   *
   * @param applicationId the ApplicationId returned by registration.
   * @return a future completing with the stored record.
   */
  public CompletableFuture<ApplicationRecordDataType> getApplicationAsync(NodeId applicationId) {
    return call(
        GdsNodeIds.Directory_GetApplication,
        "GetApplication",
        new Variant[] {Variant.ofNodeId(applicationId)},
        1,
        outputs ->
            outputs.struct(0, ApplicationRecordDataType.class, client.getStaticEncodingContext()));
  }

  /**
   * Query registered applications (Part 12 §6.5.11).
   *
   * <p>String filters accept the wildcards defined for the query, and a null or empty value means
   * "any". {@code applicationType} is the numeric filter defined by the spec, e.g. 0 for any.
   *
   * @param startingRecordId the record id to start at, 0 for the first.
   * @param maxRecordsToReturn the maximum number of records, 0 for no limit.
   * @param applicationName the ApplicationName filter.
   * @param applicationUri the ApplicationUri filter.
   * @param applicationType the ApplicationType filter.
   * @param productUri the ProductUri filter.
   * @param serverCapabilities the ServerCapabilities filter.
   * @return the matching applications and paging information.
   * @throws UaException if the call fails.
   */
  public QueryApplicationsResult queryApplications(
      UInteger startingRecordId,
      UInteger maxRecordsToReturn,
      @Nullable String applicationName,
      @Nullable String applicationUri,
      UInteger applicationType,
      @Nullable String productUri,
      String @Nullable [] serverCapabilities)
      throws UaException {

    return ClientCalls.await(
        queryApplicationsAsync(
            startingRecordId,
            maxRecordsToReturn,
            applicationName,
            applicationUri,
            applicationType,
            productUri,
            serverCapabilities));
  }

  /**
   * Asynchronous form of {@link #queryApplications}.
   *
   * @return a future completing with the matching applications and paging information.
   */
  public CompletableFuture<QueryApplicationsResult> queryApplicationsAsync(
      UInteger startingRecordId,
      UInteger maxRecordsToReturn,
      @Nullable String applicationName,
      @Nullable String applicationUri,
      UInteger applicationType,
      @Nullable String productUri,
      String @Nullable [] serverCapabilities) {

    return call(
        GdsNodeIds.Directory_QueryApplications,
        "QueryApplications",
        new Variant[] {
          Variant.ofUInt32(startingRecordId),
          Variant.ofUInt32(maxRecordsToReturn),
          Variant.of(applicationName),
          Variant.of(applicationUri),
          Variant.ofUInt32(applicationType),
          Variant.of(productUri),
          Variant.of(serverCapabilities)
        },
        3,
        outputs ->
            new QueryApplicationsResult(
                outputs.scalar(0, DateTime.class),
                outputs.scalar(1, UInteger.class),
                outputs.structArray(
                    2, ApplicationDescription.class, client.getStaticEncodingContext())));
  }

  /**
   * Query registered servers (Part 12 §6.5.12).
   *
   * <p>String filters accept the wildcards defined for the query, and a null or empty value means
   * "any".
   *
   * @param startingRecordId the record id to start at, 0 for the first.
   * @param maxRecordsToReturn the maximum number of records, 0 for no limit.
   * @param applicationName the ApplicationName filter.
   * @param applicationUri the ApplicationUri filter.
   * @param productUri the ProductUri filter.
   * @param serverCapabilities the ServerCapabilities filter.
   * @return the matching servers and the last counter reset time.
   * @throws UaException if the call fails.
   */
  public QueryServersResult queryServers(
      UInteger startingRecordId,
      UInteger maxRecordsToReturn,
      @Nullable String applicationName,
      @Nullable String applicationUri,
      @Nullable String productUri,
      String @Nullable [] serverCapabilities)
      throws UaException {

    return ClientCalls.await(
        queryServersAsync(
            startingRecordId,
            maxRecordsToReturn,
            applicationName,
            applicationUri,
            productUri,
            serverCapabilities));
  }

  /**
   * Asynchronous form of {@link #queryServers}.
   *
   * @return a future completing with the matching servers and the last counter reset time.
   */
  public CompletableFuture<QueryServersResult> queryServersAsync(
      UInteger startingRecordId,
      UInteger maxRecordsToReturn,
      @Nullable String applicationName,
      @Nullable String applicationUri,
      @Nullable String productUri,
      String @Nullable [] serverCapabilities) {

    return call(
        GdsNodeIds.Directory_QueryServers,
        "QueryServers",
        new Variant[] {
          Variant.ofUInt32(startingRecordId),
          Variant.ofUInt32(maxRecordsToReturn),
          Variant.of(applicationName),
          Variant.of(applicationUri),
          Variant.of(productUri),
          Variant.of(serverCapabilities)
        },
        2,
        outputs ->
            new QueryServersResult(
                outputs.scalar(0, DateTime.class),
                outputs.structArray(1, ServerOnNetwork.class, client.getStaticEncodingContext())));
  }

  // endregion

  // region CertificateDirectory (Part 12 §7.9)

  /**
   * Submit a PKCS#10 certificate signing request (Part 12 §7.9.3).
   *
   * <p>The request's ApplicationUri must match the application record, or the GDS rejects it with
   * {@link StatusCodes#Bad_CertificateUriInvalid}; server applications also carry their domain
   * names. Requires an encrypted channel. Poll {@link #finishRequest(NodeId, NodeId)} for the
   * result.
   *
   * @param applicationId the ApplicationId returned by registration.
   * @param certificateGroupId the CertificateGroup the certificate belongs to, or null for the
   *     default group.
   * @param certificateTypeId the CertificateType to issue, or null for the group's default; use
   *     {@link #resolveCertificateTypeId(NodeId, NodeId)} to select this value from an advertised
   *     group.
   * @param certificateRequest the DER-encoded PKCS#10 request.
   * @return the RequestId to pass to {@link #finishRequest(NodeId, NodeId)}.
   * @throws UaException if the call fails.
   */
  public NodeId startSigningRequest(
      NodeId applicationId,
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      ByteString certificateRequest)
      throws UaException {

    return ClientCalls.await(
        startSigningRequestAsync(
            applicationId, certificateGroupId, certificateTypeId, certificateRequest));
  }

  /**
   * Asynchronous form of {@link #startSigningRequest}.
   *
   * @return a future completing with the RequestId.
   */
  public CompletableFuture<NodeId> startSigningRequestAsync(
      NodeId applicationId,
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      ByteString certificateRequest) {

    return call(
        GdsNodeIds.Directory_StartSigningRequest,
        "StartSigningRequest",
        new Variant[] {
          Variant.ofNodeId(applicationId),
          Variant.ofNodeId(orNull(certificateGroupId)),
          Variant.ofNodeId(orNull(certificateTypeId)),
          Variant.ofByteString(certificateRequest)
        },
        1,
        outputs -> outputs.scalar(0, NodeId.class));
  }

  /**
   * Ask the GDS to generate a key pair and certificate (Part 12 §7.9.4).
   *
   * <p>The private key is transported from the GDS to the caller in {@link
   * FinishRequestResult#privateKey()}; prefer {@link #startSigningRequest} whenever the key pair
   * can be generated locally, and protect the returned key material when it cannot. No helper is
   * provided for decoding the PFX or PEM formats. Requires an encrypted channel.
   *
   * @param applicationId the ApplicationId returned by registration.
   * @param certificateGroupId the CertificateGroup the certificate belongs to, or null for the
   *     default group.
   * @param certificateTypeId the CertificateType to issue, or null for the group's default; use
   *     {@link #resolveCertificateTypeId(NodeId, NodeId)} to select this value from an advertised
   *     group.
   * @param subjectName the subject name to put in the certificate, or null to let the GDS choose.
   * @param domainNames the domain names to put in the certificate, or null to let the GDS choose.
   * @param privateKeyFormat {@code "PFX"} or {@code "PEM"}, or null for the GDS default.
   * @param privateKeyPassword the password to protect the private key with, or null for none.
   * @return the RequestId to pass to {@link #finishRequest(NodeId, NodeId)}.
   * @throws UaException if the call fails.
   */
  public NodeId startNewKeyPairRequest(
      NodeId applicationId,
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      String @Nullable [] domainNames,
      @Nullable String privateKeyFormat,
      @Nullable String privateKeyPassword)
      throws UaException {

    return ClientCalls.await(
        startNewKeyPairRequestAsync(
            applicationId,
            certificateGroupId,
            certificateTypeId,
            subjectName,
            domainNames,
            privateKeyFormat,
            privateKeyPassword));
  }

  /**
   * Asynchronous form of {@link #startNewKeyPairRequest}.
   *
   * @return a future completing with the RequestId.
   */
  public CompletableFuture<NodeId> startNewKeyPairRequestAsync(
      NodeId applicationId,
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      String @Nullable [] domainNames,
      @Nullable String privateKeyFormat,
      @Nullable String privateKeyPassword) {

    return call(
        GdsNodeIds.Directory_StartNewKeyPairRequest,
        "StartNewKeyPairRequest",
        new Variant[] {
          Variant.ofNodeId(applicationId),
          Variant.ofNodeId(orNull(certificateGroupId)),
          Variant.ofNodeId(orNull(certificateTypeId)),
          Variant.of(subjectName),
          Variant.of(domainNames),
          Variant.of(privateKeyFormat),
          Variant.of(privateKeyPassword)
        },
        1,
        outputs -> outputs.scalar(0, NodeId.class));
  }

  /**
   * Fetch the result of a signing or key pair request (Part 12 §7.9.5).
   *
   * <p>Fails with {@link StatusCodes#Bad_NothingToDo} while the request is still pending, in which
   * case the caller polls again later; any other Bad code means the request was rejected and a new
   * one must be submitted. Should be called on a channel using the same certificate as the matching
   * start request. The returned certificate is not checked against anything; use {@link
   * #verifyIssuedCertificate(X509Certificate, PublicKey, String)} before installing it.
   *
   * @param applicationId the ApplicationId returned by registration.
   * @param requestId the RequestId returned by the start request.
   * @return the issued certificate, the private key for key pair requests, and the issuer chain.
   * @throws UaException if the call fails or the request is not complete.
   */
  public FinishRequestResult finishRequest(NodeId applicationId, NodeId requestId)
      throws UaException {

    return ClientCalls.await(finishRequestAsync(applicationId, requestId));
  }

  /**
   * Asynchronous form of {@link #finishRequest(NodeId, NodeId)}.
   *
   * @return a future completing with the issued material.
   */
  public CompletableFuture<FinishRequestResult> finishRequestAsync(
      NodeId applicationId, NodeId requestId) {

    // Only the certificate is required; a GDS may omit the optional trailing outputs entirely.
    return call(
        GdsNodeIds.Directory_FinishRequest,
        "FinishRequest",
        new Variant[] {Variant.ofNodeId(applicationId), Variant.ofNodeId(requestId)},
        1,
        outputs -> {
          ByteString privateKey = outputs.nullableScalar(1, ByteString.class);

          return new FinishRequestResult(
              outputs.scalar(0, ByteString.class),
              privateKey != null && !privateKey.isNullOrEmpty() ? privateKey : null,
              outputs.array(2, ByteString.class));
        });
  }

  /**
   * Get the CertificateGroups the GDS manages for an application (Part 12 §7.9.7).
   *
   * <p>The returned ids identify {@code CertificateGroupType} instances on the GDS; read their
   * {@code CertificateTypes} with {@link #readCertificateTypes(NodeId)}.
   *
   * @param applicationId the ApplicationId returned by registration.
   * @return the CertificateGroup ids.
   * @throws UaException if the call fails.
   */
  public NodeId[] getCertificateGroups(NodeId applicationId) throws UaException {
    return ClientCalls.await(getCertificateGroupsAsync(applicationId));
  }

  /**
   * Asynchronous form of {@link #getCertificateGroups(NodeId)}.
   *
   * @return a future completing with the CertificateGroup ids.
   */
  public CompletableFuture<NodeId[]> getCertificateGroupsAsync(NodeId applicationId) {
    return call(
        GdsNodeIds.Directory_GetCertificateGroups,
        "GetCertificateGroups",
        new Variant[] {Variant.ofNodeId(applicationId)},
        1,
        outputs -> outputs.array(0, NodeId.class));
  }

  /**
   * Get the certificates the GDS has issued to an application in a group (Part 12 §7.9.8).
   *
   * <p>Defined from GDS NodeSet 1.05.07; older servers fail with {@link
   * StatusCodes#Bad_MethodInvalid} or {@link StatusCodes#Bad_NodeIdUnknown}.
   *
   * @param applicationId the ApplicationId returned by registration.
   * @param certificateGroupId the CertificateGroup, or null for all groups.
   * @return the CertificateType of each certificate and the certificates themselves.
   * @throws UaException if the call fails.
   */
  public CertificatesResult getCertificates(
      NodeId applicationId, @Nullable NodeId certificateGroupId) throws UaException {

    return ClientCalls.await(getCertificatesAsync(applicationId, certificateGroupId));
  }

  /**
   * Asynchronous form of {@link #getCertificates(NodeId, NodeId)}.
   *
   * @return a future completing with the certificates.
   */
  public CompletableFuture<CertificatesResult> getCertificatesAsync(
      NodeId applicationId, @Nullable NodeId certificateGroupId) {

    return call(
        GdsNodeIds.Directory_GetCertificates,
        "GetCertificates",
        new Variant[] {
          Variant.ofNodeId(applicationId), Variant.ofNodeId(orNull(certificateGroupId))
        },
        2,
        outputs ->
            new CertificatesResult(
                outputs.array(0, NodeId.class), outputs.array(1, ByteString.class)));
  }

  /**
   * Get the TrustList object for an application's CertificateGroup (Part 12 §7.9.9).
   *
   * <p>The returned id identifies a {@code TrustListType} instance on the GDS; read its contents
   * with {@link TrustListReader} and its {@code LastUpdateTime} and {@code UpdateFrequency} with
   * {@link #readTrustListInfo(NodeId)}.
   *
   * @param applicationId the ApplicationId returned by registration.
   * @param certificateGroupId the CertificateGroup, or null for the default group.
   * @return the TrustList id.
   * @throws UaException if the call fails.
   */
  public NodeId getTrustList(NodeId applicationId, @Nullable NodeId certificateGroupId)
      throws UaException {

    return ClientCalls.await(getTrustListAsync(applicationId, certificateGroupId));
  }

  /**
   * Asynchronous form of {@link #getTrustList(NodeId, NodeId)}.
   *
   * @return a future completing with the TrustList id.
   */
  public CompletableFuture<NodeId> getTrustListAsync(
      NodeId applicationId, @Nullable NodeId certificateGroupId) {

    return call(
        GdsNodeIds.Directory_GetTrustList,
        "GetTrustList",
        new Variant[] {
          Variant.ofNodeId(applicationId), Variant.ofNodeId(orNull(certificateGroupId))
        },
        1,
        outputs -> outputs.scalar(0, NodeId.class));
  }

  /**
   * Ask whether the GDS wants the application to request a new certificate (Part 12 §7.9.10).
   *
   * @param applicationId the ApplicationId returned by registration.
   * @param certificateGroupId the CertificateGroup, or null for the default group.
   * @param certificateTypeId the CertificateType, or null for the group's default; use {@link
   *     #resolveCertificateTypeId(NodeId, NodeId)} to select this value from an advertised group.
   * @return {@code true} if a new certificate should be requested.
   * @throws UaException if the call fails.
   */
  public boolean getCertificateStatus(
      NodeId applicationId, @Nullable NodeId certificateGroupId, @Nullable NodeId certificateTypeId)
      throws UaException {

    return ClientCalls.await(
        getCertificateStatusAsync(applicationId, certificateGroupId, certificateTypeId));
  }

  /**
   * Asynchronous form of {@link #getCertificateStatus(NodeId, NodeId, NodeId)}.
   *
   * @return a future completing with {@code true} if a new certificate should be requested.
   */
  public CompletableFuture<Boolean> getCertificateStatusAsync(
      NodeId applicationId,
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId) {

    return call(
        GdsNodeIds.Directory_GetCertificateStatus,
        "GetCertificateStatus",
        new Variant[] {
          Variant.ofNodeId(applicationId),
          Variant.ofNodeId(orNull(certificateGroupId)),
          Variant.ofNodeId(orNull(certificateTypeId))
        },
        1,
        outputs -> outputs.scalar(0, Boolean.class));
  }

  /**
   * Revoke a certificate the GDS issued to an application (Part 12 §7.9.6).
   *
   * <p>Defined from GDS NodeSet 1.05.07; older servers fail with {@link
   * StatusCodes#Bad_MethodInvalid} or {@link StatusCodes#Bad_NodeIdUnknown}.
   *
   * @param applicationId the ApplicationId returned by registration.
   * @param certificate the DER-encoded certificate to revoke.
   * @throws UaException if the call fails.
   */
  public void revokeCertificate(NodeId applicationId, ByteString certificate) throws UaException {
    ClientCalls.await(revokeCertificateAsync(applicationId, certificate));
  }

  /**
   * Asynchronous form of {@link #revokeCertificate(NodeId, ByteString)}.
   *
   * @return a future completing when the certificate is revoked.
   */
  public CompletableFuture<Unit> revokeCertificateAsync(
      NodeId applicationId, ByteString certificate) {

    return call(
        GdsNodeIds.Directory_RevokeCertificate,
        "RevokeCertificate",
        new Variant[] {Variant.ofNodeId(applicationId), Variant.ofByteString(certificate)},
        0,
        outputs -> Unit.VALUE);
  }

  /**
   * Check the revocation status of a certificate (Part 12 §7.9.11).
   *
   * <p>Defined from GDS NodeSet 1.05.07; older servers fail with {@link
   * StatusCodes#Bad_MethodInvalid} or {@link StatusCodes#Bad_NodeIdUnknown}.
   *
   * @param certificate the DER-encoded certificate to check.
   * @return the certificate's status and how long the answer is valid for.
   * @throws UaException if the call fails.
   */
  public RevocationStatus checkRevocationStatus(ByteString certificate) throws UaException {
    return ClientCalls.await(checkRevocationStatusAsync(certificate));
  }

  /**
   * Asynchronous form of {@link #checkRevocationStatus(ByteString)}.
   *
   * @return a future completing with the certificate's status.
   */
  public CompletableFuture<RevocationStatus> checkRevocationStatusAsync(ByteString certificate) {
    return call(
        GdsNodeIds.Directory_CheckRevocationStatus,
        "CheckRevocationStatus",
        new Variant[] {Variant.ofByteString(certificate)},
        2,
        outputs ->
            new RevocationStatus(
                outputs.scalar(0, StatusCode.class), outputs.scalar(1, DateTime.class)));
  }

  // endregion

  // region Convenience reads

  /**
   * Read the {@code CertificateTypes} property of a CertificateGroup on the GDS.
   *
   * <p>The returned ids may identify abstract types such as {@link
   * NodeIds#ApplicationCertificateType}. Use {@link #resolveCertificateTypeId(NodeId, NodeId)} when
   * selecting the value to pass to a certificate request method.
   *
   * @param certificateGroupId a group id returned by {@link #getCertificateGroups(NodeId)}.
   * @return the CertificateType ids the group issues, e.g. {@code
   *     RsaSha256ApplicationCertificateType}.
   * @throws UaException if the property does not exist or cannot be read.
   */
  public NodeId[] readCertificateTypes(NodeId certificateGroupId) throws UaException {
    return ClientCalls.await(readCertificateTypesAsync(certificateGroupId));
  }

  /**
   * Asynchronous form of {@link #readCertificateTypes(NodeId)}.
   *
   * @return a future completing with the CertificateType ids.
   */
  public CompletableFuture<NodeId[]> readCertificateTypesAsync(NodeId certificateGroupId) {
    return ClientCalls.readProperties(client, certificateGroupId, List.of("CertificateTypes"))
        .thenCompose(
            values -> {
              try {
                NodeId[] certificateTypes =
                    requiredProperty(
                        "CertificateTypes", certificateGroupId, values.get(0), NodeId[].class);

                return completedFuture(certificateTypes != null ? certificateTypes : new NodeId[0]);
              } catch (UaException e) {
                return failedFuture(e);
              }
            });
  }

  /**
   * Resolve the CertificateTypeId to request for a desired certificate type.
   *
   * <p>An exact advertised match returns {@code desiredTypeId}. If the group advertises only
   * abstract types and every advertised non-null type is an ancestor of the desired type, this
   * returns {@code null} so the request selects the group's default. Any other advertised list,
   * including one that holds an incompatible abstract type or a concrete sibling of the desired
   * type, fails with {@link StatusCodes#Bad_NotSupported}.
   *
   * <p>The standard CertificateType hierarchy is evaluated locally without browsing the server.
   * CertificateTypes outside namespace 0 are matched exactly only; their subtype relationships are
   * not evaluated. Passing an abstract type as {@code desiredTypeId} returns it unchanged on an
   * exact match, which a GDS may reject.
   *
   * @param certificateGroupId a group id returned by {@link #getCertificateGroups(NodeId)}.
   * @param desiredTypeId the concrete CertificateType the application needs.
   * @return the desired type for an exact match, or {@code null} to request a compatible group
   *     default.
   * @throws UaException if the property cannot be read or the group cannot issue the desired type.
   */
  public @Nullable NodeId resolveCertificateTypeId(NodeId certificateGroupId, NodeId desiredTypeId)
      throws UaException {

    return ClientCalls.await(resolveCertificateTypeIdAsync(certificateGroupId, desiredTypeId));
  }

  /**
   * Asynchronous form of {@link #resolveCertificateTypeId(NodeId, NodeId)}.
   *
   * @param certificateGroupId a group id returned by {@link #getCertificateGroups(NodeId)}.
   * @param desiredTypeId the concrete CertificateType the application needs.
   * @return a future completing with the desired type for an exact match, or {@code null} to
   *     request a compatible group default. The future completes exceptionally with {@link
   *     UaException} carrying {@link StatusCodes#Bad_NotSupported} when the advertised types are
   *     incompatible.
   */
  public CompletableFuture<@Nullable NodeId> resolveCertificateTypeIdAsync(
      NodeId certificateGroupId, NodeId desiredTypeId) {

    return readCertificateTypesAsync(certificateGroupId)
        .thenCompose(
            advertisedTypeIds -> {
              try {
                @Nullable NodeId requestTypeId =
                    selectCertificateTypeId(certificateGroupId, desiredTypeId, advertisedTypeIds);

                return completedFuture(requestTypeId);
              } catch (UaException e) {
                return failedFuture(e);
              }
            });
  }

  /**
   * Read the {@code LastUpdateTime} and optional {@code UpdateFrequency} properties of a TrustList
   * on the GDS.
   *
   * <p>A Pull client compares {@code lastUpdateTime} against the time it last applied the list to
   * decide whether to read it again, and uses {@code updateFrequency} (milliseconds) as its cycle
   * period; Part 12 §7.8.2.1 says to check within twice that value.
   *
   * @param trustListId a TrustList id returned by {@link #getTrustList(NodeId, NodeId)}.
   * @return the property values; {@code updateFrequency} is null when the property is absent.
   * @throws UaException if {@code LastUpdateTime} does not exist or cannot be read.
   */
  public TrustListInfo readTrustListInfo(NodeId trustListId) throws UaException {
    return ClientCalls.await(readTrustListInfoAsync(trustListId));
  }

  /**
   * Asynchronous form of {@link #readTrustListInfo(NodeId)}.
   *
   * @return a future completing with the property values.
   */
  public CompletableFuture<TrustListInfo> readTrustListInfoAsync(NodeId trustListId) {
    return ClientCalls.readProperties(
            client, trustListId, List.of("LastUpdateTime", "UpdateFrequency"))
        .thenCompose(
            values -> {
              try {
                DateTime lastUpdateTime =
                    requiredProperty("LastUpdateTime", trustListId, values.get(0), DateTime.class);

                if (lastUpdateTime == null) {
                  throw new UaException(
                      StatusCodes.Bad_UnexpectedError, "LastUpdateTime: value is null");
                }

                DataValue updateFrequency = values.get(1);
                Double frequency = null;

                if (updateFrequency != null
                    && updateFrequency.getStatusCode().isGood()
                    && updateFrequency.value().value() instanceof Double d) {
                  frequency = d;
                }

                return completedFuture(new TrustListInfo(lastUpdateTime, frequency));
              } catch (UaException e) {
                return failedFuture(e);
              }
            });
  }

  /**
   * Validate a required Property read by {@link ClientCalls#readProperties}.
   *
   * @return the value, or null when the Property exists and reads Good but holds a null value.
   * @throws UaException if the Property is missing, read with a Bad status, or holds another type.
   */
  private static <T> @Nullable T requiredProperty(
      String name, NodeId objectId, @Nullable DataValue value, Class<T> type) throws UaException {

    if (value == null) {
      throw new UaException(StatusCodes.Bad_NotFound, name + " property not found on " + objectId);
    } else if (!value.getStatusCode().isGood()) {
      throw new UaException(value.getStatusCode());
    }

    Object v = value.value().value();

    if (v == null) {
      return null;
    } else if (type.isInstance(v)) {
      return type.cast(v);
    } else {
      throw new UaException(
          StatusCodes.Bad_UnexpectedError,
          String.format(
              "%s: expected %s, received %s",
              name, type.getSimpleName(), v.getClass().getSimpleName()));
    }
  }

  // endregion

  /**
   * Check that a certificate returned by {@link #finishRequest(NodeId, NodeId)} was issued for the
   * caller's key pair and application.
   *
   * <p>The GDS is trusted to sign, not to be infallible: a certificate for the wrong key cannot be
   * used for the channel, and one with the wrong ApplicationUri is rejected by every peer (Part 12
   * §7.9.3). Neither problem is visible until a connection fails, so check before installing.
   *
   * @param issued the certificate from {@link FinishRequestResult#certificate()}.
   * @param publicKey the public key of the key pair the signing request was created for.
   * @param applicationUri the ApplicationUri of the application record.
   * @throws UaException {@link StatusCodes#Bad_CertificateInvalid} if the public key differs, or
   *     {@link StatusCodes#Bad_CertificateUriInvalid} if the ApplicationUri SAN differs.
   */
  public static void verifyIssuedCertificate(
      X509Certificate issued, PublicKey publicKey, String applicationUri) throws UaException {

    if (!issued.getPublicKey().equals(publicKey)) {
      throw new UaException(
          StatusCodes.Bad_CertificateInvalid,
          "issued certificate public key does not match the requested key pair");
    }

    String sanUri = CertificateUtil.getSanUri(issued).orElse(null);

    if (!Objects.equals(applicationUri, sanUri)) {
      throw new UaException(
          StatusCodes.Bad_CertificateUriInvalid,
          String.format(
              "issued certificate ApplicationUri '%s' does not match '%s'",
              sanUri, applicationUri));
    }
  }

  private <T> CompletableFuture<T> call(
      ExpandedNodeId methodId,
      String methodName,
      Variant[] inputs,
      int requiredOutputs,
      ClientCalls.OutputDecoder<T> decoder) {

    NodeId localMethodId;
    try {
      localMethodId = methodId.toNodeIdOrThrow(client.getNamespaceTable());
    } catch (UaException e) {
      return failedFuture(e);
    }

    return ClientCalls.call(
        client, directoryId, localMethodId, methodName, inputs, requiredOutputs, decoder);
  }

  private static NodeId orNull(@Nullable NodeId nodeId) {
    return nodeId != null ? nodeId : NodeId.NULL_VALUE;
  }

  private static @Nullable NodeId selectCertificateTypeId(
      NodeId certificateGroupId, NodeId desiredTypeId, NodeId[] advertisedTypeIds)
      throws UaException {

    for (NodeId advertisedTypeId : advertisedTypeIds) {
      if (desiredTypeId.equals(advertisedTypeId)) {
        return desiredTypeId;
      }
    }

    // Null selects the group's default, which is only predictable when every advertised non-null
    // type is an abstract ancestor; any other type could identify a different profile.
    boolean ancestorAdvertised = false;

    for (NodeId advertisedTypeId : advertisedTypeIds) {
      if (advertisedTypeId == null || advertisedTypeId.isNull()) {
        continue;
      }
      if (!ABSTRACT_CERTIFICATE_TYPES.contains(advertisedTypeId)
          || !isStrictCertificateSubtypeOf(desiredTypeId, advertisedTypeId)) {
        ancestorAdvertised = false;
        break;
      }
      ancestorAdvertised = true;
    }

    if (ancestorAdvertised) {
      return null;
    }

    throw new UaException(
        StatusCodes.Bad_NotSupported,
        String.format(
            "CertificateGroup %s cannot issue desired CertificateType %s",
            certificateGroupId, desiredTypeId));
  }

  private static boolean isStrictCertificateSubtypeOf(NodeId typeId, NodeId potentialSupertypeId) {
    NodeId supertypeId = CERTIFICATE_TYPE_SUPERTYPES.get(typeId);

    while (supertypeId != null) {
      if (supertypeId.equals(potentialSupertypeId)) {
        return true;
      }

      supertypeId = CERTIFICATE_TYPE_SUPERTYPES.get(supertypeId);
    }

    return false;
  }

  /**
   * The outputs of {@link GdsClient#finishRequest(NodeId, NodeId)}.
   *
   * <p>A GDS with nothing to return for an optional output may send a null Variant or a value with
   * no content; both are normalized here, so {@code privateKey} is null exactly when no key was
   * returned and {@code issuerCertificates} is empty exactly when no chain was returned.
   *
   * @param certificate the DER-encoded issued certificate.
   * @param privateKey the private key in the requested format for a key pair request; null for a
   *     signing request.
   * @param issuerCertificates the DER-encoded issuer chain; empty when the GDS provides the chain
   *     through the TrustList instead.
   */
  public record FinishRequestResult(
      ByteString certificate, @Nullable ByteString privateKey, ByteString[] issuerCertificates) {}

  /**
   * The outputs of {@link GdsClient#getCertificates(NodeId, NodeId)}; the arrays are parallel.
   *
   * @param certificateTypeIds the CertificateType of each certificate.
   * @param certificates the DER-encoded certificates.
   */
  public record CertificatesResult(NodeId[] certificateTypeIds, ByteString[] certificates) {}

  /**
   * The outputs of {@link GdsClient#checkRevocationStatus(ByteString)}.
   *
   * @param certificateStatus Good if the certificate is not revoked, otherwise the reason.
   * @param validityTime the time until which the answer can be cached.
   */
  public record RevocationStatus(StatusCode certificateStatus, DateTime validityTime) {}

  /**
   * The outputs of {@link GdsClient#queryApplications}.
   *
   * @param lastCounterResetTime the last time the record id counter was reset.
   * @param nextRecordId the record id to continue paging from, 0 when there are no more.
   * @param applications the matching applications.
   */
  public record QueryApplicationsResult(
      DateTime lastCounterResetTime,
      UInteger nextRecordId,
      ApplicationDescription[] applications) {}

  /**
   * The outputs of {@link GdsClient#queryServers}.
   *
   * @param lastCounterResetTime the last time the record id counter was reset.
   * @param servers the matching servers.
   */
  public record QueryServersResult(DateTime lastCounterResetTime, ServerOnNetwork[] servers) {}

  /**
   * The TrustList properties read by {@link GdsClient#readTrustListInfo(NodeId)}.
   *
   * @param lastUpdateTime the last time the TrustList changed.
   * @param updateFrequency the interval, in milliseconds, at which the GDS expects the list to be
   *     re-read; null when the GDS does not expose the property.
   */
  public record TrustListInfo(DateTime lastUpdateTime, @Nullable Double updateFrequency) {}
}
