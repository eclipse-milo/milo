/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultDiscoveryServiceSet;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.security.CertificateFactory;
import org.eclipse.milo.opcua.stack.core.security.CertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.security.TrustListManager;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.GetEndpointsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.GetEndpointsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.junit.jupiter.api.Test;

public class EndpointConfigTest {

  @Test
  public void securityMismatchThrows() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            // mismatch between securityPolicy and securityMode
            EndpointConfig.newBuilder()
                .setSecurityPolicy(SecurityPolicy.Basic128Rsa15)
                .setSecurityMode(MessageSecurityMode.None)
                .build());

    assertThrows(
        IllegalArgumentException.class,
        () ->
            // mismatch between securityPolicy and securityMode
            EndpointConfig.newBuilder()
                .setSecurityPolicy(SecurityPolicy.None)
                .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
                .build());
  }

  @Test
  public void missingCertificateThrows() {
    assertThrows(
        IllegalStateException.class,
        () ->
            // missing certificate
            EndpointConfig.newBuilder()
                .setSecurityPolicy(SecurityPolicy.Basic128Rsa15)
                .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
                .build());
  }

  // Managed certificate selection lets secure endpoints defer certificate choice until
  // advertisement time.
  @Test
  public void certificateSelectionConfigAllowsSecureEndpointWithoutFixedCertificate() {
    EndpointCertificateConfig certificateConfig =
        EndpointCertificateConfig.newBuilder()
            .setCertificateTypeId(NodeIds.RsaSha256ApplicationCertificateType)
            .build();

    EndpointConfig endpointConfig =
        EndpointConfig.newBuilder()
            .setEndpointCertificateConfig(certificateConfig)
            .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
            .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
            .build();

    assertEquals(Optional.of(certificateConfig), endpointConfig.getEndpointCertificateConfig());
  }

  // Copying endpoint configs must preserve the legacy fixed-certificate API exactly.
  @Test
  public void legacyFixedCertificateApiIsPreservedByCopy() throws Exception {
    CertificateMaterial certificate = rsaCertificate("legacy");

    EndpointConfig.Builder builder =
        EndpointConfig.newBuilder().setCertificate(certificate.certificate());
    EndpointConfig copy = builder.copy().build();

    assertSame(certificate.certificate(), copy.getCertificate());
    assertEquals(Optional.empty(), copy.getEndpointCertificateConfig());
  }

  @Test
  public void unsupportedTransportThrows() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            EndpointConfig.newBuilder().setTransportProfile(TransportProfile.HTTPS_UAXML).build());

    assertThrows(
        IllegalArgumentException.class,
        () ->
            EndpointConfig.newBuilder().setTransportProfile(TransportProfile.HTTPS_UAJSON).build());

    assertThrows(
        IllegalArgumentException.class,
        () -> EndpointConfig.newBuilder().setTransportProfile(TransportProfile.WSS_UAJSON).build());

    assertThrows(
        IllegalArgumentException.class,
        () ->
            EndpointConfig.newBuilder()
                .setTransportProfile(TransportProfile.WSS_UASC_UABINARY)
                .build());
  }

  @Test
  public void missingTokenPolicyDefaultsToAnonymous() {
    EndpointConfig endpointConfig = EndpointConfig.newBuilder().build();

    List<UserTokenPolicy> tokenPolicies = endpointConfig.getTokenPolicies();
    assertEquals(1, tokenPolicies.size());
    assertEquals(EndpointConfig.Builder.USER_TOKEN_POLICY_ANONYMOUS, tokenPolicies.get(0));
  }

  // Discovery clients must see the concrete certificate selected from the managed identity set.
  @Test
  public void endpointCertificateConfigSelectsAdvertisedCertificate() throws Exception {
    CertificateMaterial certificate = rsaCertificate("selected");
    CertificateManager certificateManager =
        manager(
            group(
                NodeIds.ServerConfiguration_CertificateGroups_DefaultApplicationGroup,
                certificate));

    EndpointConfig endpoint =
        EndpointConfig.newBuilder()
            .setEndpointCertificateConfig(
                EndpointCertificateConfig.newBuilder()
                    .setCertificateTypeId(NodeIds.RsaSha256ApplicationCertificateType)
                    .build())
            .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
            .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
            .build();

    OpcUaServer server = server(Set.of(endpoint), certificateManager);

    EndpointDescription description =
        server.getApplicationContext().getEndpointDescriptions().get(0);

    assertArrayEquals(
        certificate.certificate().getEncoded(), description.getServerCertificate().bytesOrEmpty());
  }

  // Legacy fixed-certificate endpoints remain authoritative even when other managed identities
  // exist.
  @Test
  public void legacyFixedCertificateIsNotReplacedByAnotherManagedIdentity() throws Exception {
    CertificateMaterial fixed = rsaCertificate("fixed");
    CertificateMaterial managed = rsaCertificate("managed");
    CertificateManager certificateManager =
        manager(
            group(NodeIds.ServerConfiguration_CertificateGroups_DefaultApplicationGroup, managed));

    EndpointConfig endpoint =
        EndpointConfig.newBuilder()
            .setCertificate(fixed.certificate())
            .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
            .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
            .build();

    OpcUaServer server = server(Set.of(endpoint), certificateManager);

    EndpointDescription description =
        server.getApplicationContext().getEndpointDescriptions().get(0);

    assertArrayEquals(
        fixed.certificate().getEncoded(), description.getServerCertificate().bytesOrEmpty());
  }

  // Endpoints that ask for an ECC certificate type must be omitted when the configured certificate
  // manager only has incompatible identities.
  @Test
  public void unsupportedEccEndpointIsOmittedFromDiscoveryAdvertisement() throws Exception {
    CertificateMaterial certificate = rsaCertificate("rsa-only");
    CertificateManager certificateManager =
        manager(
            group(
                NodeIds.ServerConfiguration_CertificateGroups_DefaultApplicationGroup,
                certificate));

    EndpointConfig endpoint =
        EndpointConfig.newBuilder()
            .setEndpointCertificateConfig(
                EndpointCertificateConfig.newBuilder()
                    .setCertificateTypeId(NodeIds.EccNistP256ApplicationCertificateType)
                    .build())
            .setSecurityPolicy(SecurityPolicy.ECC_nistP256_AesGcm)
            .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
            .build();

    OpcUaServer server = server(Set.of(endpoint), certificateManager);

    DefaultDiscoveryServiceSet discoveryServiceSet = new DefaultDiscoveryServiceSet(server);
    GetEndpointsResponse response =
        discoveryServiceSet.onGetEndpoints(
            null, new GetEndpointsRequest(requestHeader(), endpoint.getEndpointUrl(), null, null));

    assertEquals(0, Objects.requireNonNull(response.getEndpoints()).length);
  }

  // RSA-DH Sign endpoints are executable: AEAD is used as a tag-only signature footer without
  // symmetric encryption.
  @Test
  public void rsaDhSignEndpointsAreAdvertisedWithCompatibleIdentities() throws Exception {
    assertEndpointAdvertised(
        SecurityPolicy.RSA_DH_AesGcm,
        MessageSecurityMode.Sign,
        NodeIds.RsaSha256ApplicationCertificateType,
        rsaCertificate("rsa-dh-aesgcm-sign"));
    assertEndpointAdvertised(
        SecurityPolicy.RSA_DH_ChaChaPoly,
        MessageSecurityMode.Sign,
        NodeIds.RsaSha256ApplicationCertificateType,
        rsaCertificate("rsa-dh-chacha-sign"));
  }

  // ECC AEAD Sign endpoints use the same tag-only symmetric footer as RSA-DH Sign endpoints, so
  // discovery should advertise them when a compatible application identity exists.
  @Test
  public void currentEccSignEndpointsAreAdvertisedWithCompatibleIdentities() throws Exception {
    assertEndpointAdvertised(
        SecurityPolicy.ECC_nistP256_AesGcm,
        MessageSecurityMode.Sign,
        NodeIds.EccNistP256ApplicationCertificateType,
        nistP256Certificate());
    assertEndpointAdvertised(
        SecurityPolicy.ECC_nistP256_ChaChaPoly,
        MessageSecurityMode.Sign,
        NodeIds.EccNistP256ApplicationCertificateType,
        nistP256Certificate());
    assertEndpointAdvertised(
        SecurityPolicy.ECC_curve25519_AesGcm,
        MessageSecurityMode.Sign,
        NodeIds.EccCurve25519ApplicationCertificateType,
        ed25519Certificate());
    assertEndpointAdvertised(
        SecurityPolicy.ECC_curve25519_ChaChaPoly,
        MessageSecurityMode.Sign,
        NodeIds.EccCurve25519ApplicationCertificateType,
        ed25519Certificate());
    assertEndpointAdvertised(
        SecurityPolicy.ECC_brainpoolP384r1_AesGcm,
        MessageSecurityMode.Sign,
        NodeIds.EccBrainpoolP384r1ApplicationCertificateType,
        brainpoolP384Certificate());
    assertEndpointAdvertised(
        SecurityPolicy.ECC_brainpoolP384r1_ChaChaPoly,
        MessageSecurityMode.Sign,
        NodeIds.EccBrainpoolP384r1ApplicationCertificateType,
        brainpoolP384Certificate());
  }

  // Current ECC endpoints are advertised only when the server can pair the policy with a matching
  // local application certificate identity.
  @Test
  public void currentEccEndpointsAreAdvertisedWithCompatibleIdentities() throws Exception {
    assertEndpointAdvertised(
        SecurityPolicy.ECC_nistP256_AesGcm,
        NodeIds.EccNistP256ApplicationCertificateType,
        nistP256Certificate());
    assertEndpointAdvertised(
        SecurityPolicy.ECC_nistP256_ChaChaPoly,
        NodeIds.EccNistP256ApplicationCertificateType,
        nistP256Certificate());
    assertEndpointAdvertised(
        SecurityPolicy.ECC_curve25519_AesGcm,
        NodeIds.EccCurve25519ApplicationCertificateType,
        ed25519Certificate());
    assertEndpointAdvertised(
        SecurityPolicy.ECC_curve25519_ChaChaPoly,
        NodeIds.EccCurve25519ApplicationCertificateType,
        ed25519Certificate());
    assertEndpointAdvertised(
        SecurityPolicy.ECC_brainpoolP384r1_AesGcm,
        NodeIds.EccBrainpoolP384r1ApplicationCertificateType,
        brainpoolP384Certificate());
    assertEndpointAdvertised(
        SecurityPolicy.ECC_brainpoolP384r1_ChaChaPoly,
        NodeIds.EccBrainpoolP384r1ApplicationCertificateType,
        brainpoolP384Certificate());
  }

  // RSA-DH endpoints use RSA application identities even though their SecureChannel key agreement
  // uses finite-field DH.
  @Test
  public void rsaDhEndpointsAreAdvertisedWithCompatibleIdentities() throws Exception {
    assertEndpointAdvertised(
        SecurityPolicy.RSA_DH_AesGcm,
        NodeIds.RsaSha256ApplicationCertificateType,
        rsaCertificate("rsa-dh-aesgcm"));
    assertEndpointAdvertised(
        SecurityPolicy.RSA_DH_ChaChaPoly,
        NodeIds.RsaSha256ApplicationCertificateType,
        rsaCertificate("rsa-dh-chacha"));
  }

  private static void assertEndpointAdvertised(
      SecurityPolicy securityPolicy, NodeId certificateTypeId, CertificateMaterial certificate)
      throws Exception {

    assertEndpointAdvertised(
        securityPolicy, MessageSecurityMode.SignAndEncrypt, certificateTypeId, certificate);
  }

  private static void assertEndpointAdvertised(
      SecurityPolicy securityPolicy,
      MessageSecurityMode securityMode,
      NodeId certificateTypeId,
      CertificateMaterial certificate)
      throws Exception {

    CertificateManager certificateManager =
        manager(
            group(
                NodeIds.ServerConfiguration_CertificateGroups_DefaultApplicationGroup,
                certificate));

    EndpointConfig endpoint =
        EndpointConfig.newBuilder()
            .setEndpointCertificateConfig(
                EndpointCertificateConfig.newBuilder()
                    .setCertificateTypeId(certificateTypeId)
                    .build())
            .setSecurityPolicy(securityPolicy)
            .setSecurityMode(securityMode)
            .build();

    OpcUaServer server = server(Set.of(endpoint), certificateManager);

    List<EndpointDescription> descriptions =
        server.getApplicationContext().getEndpointDescriptions();

    assertEquals(1, descriptions.size());
    assertEquals(securityPolicy.getUri(), descriptions.get(0).getSecurityPolicyUri());
    assertEquals(securityMode, descriptions.get(0).getSecurityMode());
    assertArrayEquals(
        certificate.certificate().getEncoded(),
        descriptions.get(0).getServerCertificate().bytesOrEmpty());
  }

  // Dynamic legacy certificate suppliers are only re-evaluated after the advertised endpoint cache
  // is reset.
  @Test
  public void resetEndpointDescriptionCacheReselectsLegacySupplierCertificate() throws Exception {
    CertificateMaterial first = rsaCertificate("first");
    CertificateMaterial second = rsaCertificate("second");
    CertificateManager certificateManager =
        manager(group(new NodeId(2, "group-a"), first), group(new NodeId(2, "group-b"), second));
    AtomicReference<X509Certificate> certificateRef = new AtomicReference<>(first.certificate());

    EndpointConfig endpoint =
        EndpointConfig.newBuilder()
            .setCertificate(certificateRef::get)
            .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
            .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
            .build();

    OpcUaServer server = server(Set.of(endpoint), certificateManager);

    EndpointDescription cachedDescription =
        server.getApplicationContext().getEndpointDescriptions().get(0);
    certificateRef.set(second.certificate());
    EndpointDescription stillCachedDescription =
        server.getApplicationContext().getEndpointDescriptions().get(0);

    assertSame(cachedDescription, stillCachedDescription);
    assertArrayEquals(
        first.certificate().getEncoded(),
        stillCachedDescription.getServerCertificate().bytesOrEmpty());

    server.resetEndpointDescriptionCache();

    EndpointDescription refreshedDescription =
        server.getApplicationContext().getEndpointDescriptions().get(0);

    assertArrayEquals(
        second.certificate().getEncoded(),
        refreshedDescription.getServerCertificate().bytesOrEmpty());
  }

  private static RequestHeader requestHeader() {
    return new RequestHeader(
        NodeId.NULL_VALUE, DateTime.now(), uint(1), uint(0), null, uint(0), null);
  }

  private static OpcUaServer server(
      Set<EndpointConfig> endpoints, CertificateManager certificateManager) {

    OpcUaServerConfig config =
        OpcUaServerConfig.builder()
            .setEndpoints(endpoints)
            .setCertificateManager(certificateManager)
            .setApplicationUri("urn:test:server")
            .setProductUri("urn:test:product")
            .build();

    return new OpcUaServer(config, transportProfile -> null);
  }

  private static CertificateManager manager(TestCertificateGroup... groups) {
    List<CertificateGroup> certificateGroups =
        Arrays.stream(groups).map(CertificateGroup.class::cast).toList();

    return new DefaultCertificateManager(new MemoryCertificateQuarantine(), certificateGroups);
  }

  private static TestCertificateGroup group(NodeId groupId, CertificateMaterial... certificates) {
    return new TestCertificateGroup(groupId, List.of(certificates));
  }

  private static CertificateMaterial rsaCertificate(String commonName) throws Exception {

    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate certificate =
        new SelfSignedCertificateBuilder(keyPair)
            .setCommonName(commonName)
            .setOrganization("Eclipse Milo")
            .setApplicationUri("urn:test:" + commonName)
            .build();

    return new CertificateMaterial(
        NodeIds.RsaSha256ApplicationCertificateType, keyPair, new X509Certificate[] {certificate});
  }

  private static CertificateMaterial nistP256Certificate() throws Exception {
    return eccCertificate(
        NodeIds.EccNistP256ApplicationCertificateType,
        SelfSignedCertificateGenerator.generateNistP256KeyPair(),
        "ecc-nist");
  }

  private static CertificateMaterial ed25519Certificate() throws Exception {
    return eccCertificate(
        NodeIds.EccCurve25519ApplicationCertificateType,
        SelfSignedCertificateGenerator.generateEd25519KeyPair(),
        "ecc-curve25519");
  }

  private static CertificateMaterial brainpoolP384Certificate() throws Exception {
    return eccCertificate(
        NodeIds.EccBrainpoolP384r1ApplicationCertificateType,
        SelfSignedCertificateGenerator.generateBrainpoolP384r1KeyPair(),
        "ecc-brainpoolp384");
  }

  private static CertificateMaterial eccCertificate(
      NodeId certificateTypeId, KeyPair keyPair, String commonName) throws Exception {

    X509Certificate certificate =
        SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair)
            .setCommonName(commonName)
            .setOrganization("Eclipse Milo")
            .setApplicationUri("urn:test:" + commonName)
            .build();

    return new CertificateMaterial(certificateTypeId, keyPair, new X509Certificate[] {certificate});
  }

  private record CertificateMaterial(
      NodeId certificateTypeId, KeyPair keyPair, X509Certificate[] certificateChain) {

    X509Certificate certificate() {
      return certificateChain[0];
    }
  }

  private record TestCertificateGroup(
      NodeId certificateGroupId, Map<NodeId, CertificateMaterial> certificates)
      implements CertificateGroup {

    private TestCertificateGroup(
        NodeId certificateGroupId, List<CertificateMaterial> certificates) {
      this(certificateGroupId, toCertificateMap(certificates));
    }

    private static Map<NodeId, CertificateMaterial> toCertificateMap(
        List<CertificateMaterial> certificates) {

      Map<NodeId, CertificateMaterial> certificateMap =
          certificates.stream()
              .collect(
                  Collectors.toMap(
                      CertificateMaterial::certificateTypeId,
                      Function.identity(),
                      (left, right) -> right,
                      LinkedHashMap::new));

      return Collections.unmodifiableMap(certificateMap);
    }

    @Override
    public NodeId getCertificateGroupId() {
      return certificateGroupId;
    }

    @Override
    public List<NodeId> getSupportedCertificateTypeIds() {
      return List.copyOf(certificates.keySet());
    }

    @Override
    public TrustListManager getTrustListManager() {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<Entry> getCertificateEntries() {
      return certificates.values().stream()
          .map(
              certificate ->
                  new CertificateGroup.Entry(
                      certificateGroupId,
                      certificate.certificateTypeId(),
                      certificate.certificateChain()))
          .toList();
    }

    @Override
    public Optional<KeyPair> getKeyPair(NodeId certificateTypeId) {
      return Optional.ofNullable(certificates.get(certificateTypeId))
          .map(CertificateMaterial::keyPair);
    }

    @Override
    public Optional<X509Certificate[]> getCertificateChain(NodeId certificateTypeId) {
      return Optional.ofNullable(certificates.get(certificateTypeId))
          .map(CertificateMaterial::certificateChain)
          .map(X509Certificate[]::clone);
    }

    @Override
    public void updateCertificate(
        NodeId certificateTypeId, KeyPair keyPair, X509Certificate[] certificateChain) {
      throw new UnsupportedOperationException();
    }

    @Override
    public CertificateFactory getCertificateFactory() {
      throw new UnsupportedOperationException();
    }

    @Override
    public CertificateValidator getCertificateValidator() {
      return new CertificateValidator.InsecureCertificateValidator();
    }
  }
}
