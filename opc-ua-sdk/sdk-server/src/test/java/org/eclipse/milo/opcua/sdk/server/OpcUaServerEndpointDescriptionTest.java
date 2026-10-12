/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.security.CertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.CertificateStore;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateStore;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OpcUaServerEndpointDescriptionTest {

  private static final NodeId SECOND_GROUP_ID = new NodeId(2, "second-group");
  private static final NodeId MISSING_GROUP_ID = new NodeId(2, "missing-group");

  private static CertificateGroupFixture defaultGroup;
  private static CertificateGroupFixture secondGroup;

  @BeforeAll
  static void generateCertificateGroups() throws Exception {
    defaultGroup = rsaGroup("default-group");
    secondGroup = rsaGroup("second-group");
  }

  // Part 4 §7.41 requires policy IDs to be unique across the Server, while null or empty token
  // security policy URIs inherit their Endpoint's policy.
  @Test
  void userTokenPolicyIdsAreUniqueWhenEffectiveSecurityPolicyDiffers() throws Exception {
    X509Certificate certificate = mock(X509Certificate.class);
    when(certificate.getEncoded()).thenReturn(new byte[] {1, 2, 3});

    UserTokenPolicy anonymousPolicy = OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS;

    EndpointConfig noneEndpoint =
        endpoint(SecurityPolicy.None, MessageSecurityMode.None, anonymousPolicy, null);
    EndpointConfig signEndpoint =
        endpoint(
            SecurityPolicy.Basic256Sha256, MessageSecurityMode.Sign, anonymousPolicy, certificate);
    EndpointConfig signAndEncryptEndpoint =
        endpoint(
            SecurityPolicy.Basic256Sha256,
            MessageSecurityMode.SignAndEncrypt,
            anonymousPolicy,
            certificate);

    Set<EndpointConfig> endpoints =
        new LinkedHashSet<>(List.of(noneEndpoint, signEndpoint, signAndEncryptEndpoint));

    OpcUaServerConfig config =
        OpcUaServerConfig.builder()
            .setCertificateManager(new DefaultCertificateManager())
            .setEndpoints(endpoints)
            .build();

    OpcUaServer server = new OpcUaServer(config, transportProfile -> null);

    List<EndpointDescription> endpointDescriptions =
        server.getApplicationContext().getEndpointDescriptions();

    EndpointDescription noneEndpointDescription =
        getEndpointDescription(endpointDescriptions, SecurityPolicy.None, MessageSecurityMode.None);
    EndpointDescription signEndpointDescription =
        getEndpointDescription(
            endpointDescriptions, SecurityPolicy.Basic256Sha256, MessageSecurityMode.Sign);
    EndpointDescription signAndEncryptEndpointDescription =
        getEndpointDescription(
            endpointDescriptions,
            SecurityPolicy.Basic256Sha256,
            MessageSecurityMode.SignAndEncrypt);

    UserTokenPolicy nonePolicy = noneEndpointDescription.getUserIdentityTokens()[0];
    UserTokenPolicy signPolicy = signEndpointDescription.getUserIdentityTokens()[0];
    UserTokenPolicy signAndEncryptPolicy =
        signAndEncryptEndpointDescription.getUserIdentityTokens()[0];

    assertEquals("anonymous", nonePolicy.getPolicyId());
    assertEquals(SecurityPolicy.None.getUri(), nonePolicy.getSecurityPolicyUri());
    assertEquals("anonymous-Basic256Sha256", signPolicy.getPolicyId());
    assertEquals(SecurityPolicy.Basic256Sha256.getUri(), signPolicy.getSecurityPolicyUri());
    assertEquals("anonymous-Basic256Sha256", signAndEncryptPolicy.getPolicyId());
    assertEquals(
        SecurityPolicy.Basic256Sha256.getUri(), signAndEncryptPolicy.getSecurityPolicyUri());

    assertEquals("anonymous", noneEndpoint.getTokenPolicies().get(0).getPolicyId());
    assertNull(noneEndpoint.getTokenPolicies().get(0).getSecurityPolicyUri());
    assertEquals("anonymous", OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS.getPolicyId());
    assertNull(OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS.getSecurityPolicyUri());
  }

  /**
   * Endpoints name their certificate group by the NodeId it is registered under with the {@link
   * CertificateManager}. The server must resolve that id against the registry rather than assume
   * the DefaultApplicationGroup, or a misconfigured endpoint would silently advertise the wrong
   * certificate.
   */
  @Nested
  class CertificateGroupResolution {

    // A group the CertificateManager does not know has no certificate to advertise. The endpoint
    // must be dropped with a diagnosable reason instead of failing the whole server or borrowing
    // the DefaultApplicationGroup's certificate, and sibling endpoints must be unaffected.
    @Test
    void endpointNamingUnregisteredGroupIsOmittedWithLoggedReason() {
      CertificateManager certificateManager = new DefaultCertificateManager(defaultGroup.group());

      OpcUaServer server =
          server(
              certificateManager,
              securedEndpoint("/default", null),
              securedEndpoint("/missing", MISSING_GROUP_ID));

      List<EndpointDescription> endpointDescriptions;
      String stderr;
      try (StderrCapture capture = new StderrCapture()) {
        endpointDescriptions = server.getApplicationContext().getEndpointDescriptions();
        stderr = capture.text();
      }

      assertEquals(
          List.of(endpointUrl("/default")),
          endpointDescriptions.stream().map(EndpointDescription::getEndpointUrl).toList(),
          "only the endpoint on the registered group is advertised");

      String expectedReason =
          "reason=certificate group not registered: " + MISSING_GROUP_ID.toParseableString();
      assertTrue(
          stderr.contains("WARN") && stderr.contains("Omitting endpoint advertisement"),
          "omission must be logged at WARN, stderr was:\n" + stderr);
      assertTrue(
          stderr.contains(expectedReason),
          "omission log must name the unregistered group, stderr was:\n" + stderr);
    }

    // A client that trusts the advertised certificate opens its SecureChannel against it, so each
    // endpoint must advertise the certificate of the group it names, and an endpoint without an
    // EndpointCertificateConfig must fall back to the DefaultApplicationGroup.
    @Test
    void endpointAdvertisesCertificateOfItsNamedGroup() throws Exception {
      var certificateManager = new DefaultCertificateManager(defaultGroup.group());
      certificateManager.addCertificateGroup(SECOND_GROUP_ID, secondGroup.group());

      OpcUaServer server =
          server(
              certificateManager,
              securedEndpoint("/default", null),
              securedEndpoint("/second", SECOND_GROUP_ID));

      List<EndpointDescription> endpointDescriptions =
          server.getApplicationContext().getEndpointDescriptions();

      byte[] defaultCertificate = defaultGroup.certificate().getEncoded();
      byte[] secondCertificate = secondGroup.certificate().getEncoded();
      assertFalse(
          Arrays.equals(defaultCertificate, secondCertificate),
          "control: the two groups must hold distinct certificates");

      assertArrayEquals(
          defaultCertificate,
          getEndpointDescription(endpointDescriptions, "/default")
              .getServerCertificate()
              .bytesOrEmpty(),
          "endpoint without EndpointCertificateConfig uses the DefaultApplicationGroup");
      assertArrayEquals(
          secondCertificate,
          getEndpointDescription(endpointDescriptions, "/second")
              .getServerCertificate()
              .bytesOrEmpty(),
          "endpoint naming the second group uses that group's certificate");
    }
  }

  private static OpcUaServer server(
      CertificateManager certificateManager, EndpointConfig... endpoints) {

    OpcUaServerConfig config =
        OpcUaServerConfig.builder()
            .setCertificateManager(certificateManager)
            .setEndpoints(new LinkedHashSet<>(List.of(endpoints)))
            .setApplicationUri("urn:test:server")
            .setProductUri("urn:test:product")
            .build();

    return new OpcUaServer(config, transportProfile -> null);
  }

  private static String endpointUrl(String path) {
    return "opc.tcp://localhost:4840" + path;
  }

  private static EndpointConfig securedEndpoint(String path, @Nullable NodeId certificateGroupId) {
    EndpointConfig.Builder builder =
        EndpointConfig.newBuilder()
            .setBindPort(4840)
            .setHostname("localhost")
            .setPath(path)
            .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
            .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
            .addTokenPolicy(OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS);

    if (certificateGroupId != null) {
      builder.setEndpointCertificateConfig(
          EndpointCertificateConfig.newBuilder().setCertificateGroupId(certificateGroupId).build());
    }

    return builder.build();
  }

  private EndpointConfig endpoint(
      SecurityPolicy securityPolicy,
      MessageSecurityMode securityMode,
      UserTokenPolicy userTokenPolicy,
      X509Certificate certificate) {

    return EndpointConfig.newBuilder()
        .setBindPort(4840)
        .setHostname("localhost")
        .setPath("/milo")
        .setSecurityPolicy(securityPolicy)
        .setSecurityMode(securityMode)
        .setCertificate(certificate)
        .addTokenPolicy(userTokenPolicy)
        .build();
  }

  private EndpointDescription getEndpointDescription(
      List<EndpointDescription> endpointDescriptions,
      SecurityPolicy securityPolicy,
      MessageSecurityMode securityMode) {

    return endpointDescriptions.stream()
        .filter(e -> securityPolicy.getUri().equals(e.getSecurityPolicyUri()))
        .filter(e -> securityMode == e.getSecurityMode())
        .findFirst()
        .orElseThrow();
  }

  private static EndpointDescription getEndpointDescription(
      List<EndpointDescription> endpointDescriptions, String path) {

    return endpointDescriptions.stream()
        .filter(e -> endpointUrl(path).equals(e.getEndpointUrl()))
        .findFirst()
        .orElseThrow(() -> new AssertionError("no endpoint advertised for " + path));
  }

  /**
   * A {@link DefaultCertificateGroup} holding one self-signed RSA application certificate, paired
   * with that certificate so tests can compare it against advertised endpoints.
   */
  private record CertificateGroupFixture(CertificateGroup group, X509Certificate certificate) {}

  private static CertificateGroupFixture rsaGroup(String commonName) throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate certificate =
        new SelfSignedCertificateBuilder(keyPair)
            .setCommonName(commonName)
            .setApplicationUri("urn:test:" + commonName)
            .addDnsName("localhost")
            .build();

    var certificateStore = new MemoryCertificateStore();
    certificateStore.set(
        NodeIds.RsaSha256ApplicationCertificateType,
        new CertificateStore.Entry(keyPair.getPrivate(), new X509Certificate[] {certificate}));

    var group =
        new DefaultCertificateGroup(
            new MemoryTrustListManager(),
            certificateStore,
            new MemoryCertificateQuarantine(),
            new CertificateValidator.InsecureCertificateValidator());

    return new CertificateGroupFixture(group, certificate);
  }

  /**
   * Captures everything written to {@link System#err} while installed.
   *
   * <p>slf4j-simple, the SLF4J binding on this module's test classpath, uses its default uncached
   * {@code System.err} target and resolves {@code System.err} on every write, so installing this
   * stream is enough to observe log records emitted afterwards.
   */
  private static final class StderrCapture implements AutoCloseable {

    private final ByteArrayOutputStream captured = new ByteArrayOutputStream();
    private final PrintStream original = System.err;

    StderrCapture() {
      System.setErr(new PrintStream(captured, true, StandardCharsets.UTF_8));
    }

    String text() {
      System.err.flush();
      return captured.toString(StandardCharsets.UTF_8);
    }

    @Override
    public void close() {
      System.setErr(original);
    }
  }
}
