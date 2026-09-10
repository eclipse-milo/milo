/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util.validation;

import static org.eclipse.milo.opcua.stack.core.util.validation.CertificateValidationUtil.buildTrustedCertPath;
import static org.eclipse.milo.opcua.stack.core.util.validation.CertificateValidationUtil.validateIssuedCertPath;
import static org.eclipse.milo.opcua.stack.core.util.validation.CertificateValidationUtil.validateTrustedCertPath;
import static org.eclipse.milo.opcua.stack.core.util.validation.TestCertificateGenerator.ALIAS_CA_INTERMEDIATE;
import static org.eclipse.milo.opcua.stack.core.util.validation.TestCertificateGenerator.ALIAS_CA_ROOT;
import static org.eclipse.milo.opcua.stack.core.util.validation.TestCertificateGenerator.ALIAS_LEAF_INTERMEDIATE_SIGNED;
import static org.eclipse.milo.opcua.stack.core.util.validation.TestCertificateGenerator.ALIAS_LEAF_SELF_SIGNED;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertPathValidatorException.BasicReason;
import java.security.cert.CertStore;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.PKIXParameters;
import java.security.cert.PKIXRevocationChecker;
import java.security.cert.PKIXRevocationChecker.Option;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.util.CrlTestUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.core.util.validation.TestCertificateGenerator.TestCertificates;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Revocation checking against the CRLs in a trust list.
 *
 * <p>Part 4 §6.1.3 separates two outcomes: an unknown revocation status, whose error may be
 * suppressed, and a revocation established by an applicable CRL, which may not be. {@link
 * ValidationCheck#REVOCATION_LISTS} selects how unknown status is handled. The path under test is
 * leaf, intermediate, root: the root's CRL covers the intermediate and the intermediate's CRL
 * covers the leaf, so a "complete" set of CRLs has one from each CA.
 */
@NullMarked
class CertificateRevocationTest {

  private static final Set<ValidationCheck> TOLERATE_UNKNOWN_STATUS =
      ValidationCheck.NO_OPTIONAL_CHECKS;

  private static final Set<ValidationCheck> REQUIRE_CRLS = Set.of(ValidationCheck.REVOCATION_LISTS);

  private static final long ONE_HOUR_MS = 60L * 60 * 1000;

  private static X509Certificate root;
  private static X509Certificate intermediate;
  private static X509Certificate leaf;
  private static X509Certificate selfSigned;
  private static PrivateKey rootKey;
  private static PrivateKey intermediateKey;

  @BeforeAll
  static void generateCertificates() throws Exception {
    TestCertificates certificates = TestCertificateGenerator.generateAll();

    root = certificates.getCertificate(ALIAS_CA_ROOT);
    intermediate = certificates.getCertificate(ALIAS_CA_INTERMEDIATE);
    leaf = certificates.getCertificate(ALIAS_LEAF_INTERMEDIATE_SIGNED);
    selfSigned = certificates.getCertificate(ALIAS_LEAF_SELF_SIGNED);
    rootKey = certificates.getPrivateKey(ALIAS_CA_ROOT);
    intermediateKey = certificates.getPrivateKey(ALIAS_CA_INTERMEDIATE);
  }

  @Nested
  class UnknownStatusTolerated {

    // Part 4 §6.1.3 allows the missing-revocation-list error to be suppressed. Users must be able
    // to trust a CA-signed chain without supplying a CRL for any CA in it.
    @Test
    void caSignedChainWithoutCrlsIsAccepted() {
      assertDoesNotThrow(() -> validate(leaf, List.of(), TOLERATE_UNKNOWN_STATUS));
    }

    // Partial coverage: the CRLs that are present are checked and the rest is tolerated.
    @ParameterizedTest
    @MethodSource(
        "org.eclipse.milo.opcua.stack.core.util.validation.CertificateRevocationTest#partialCrls")
    void chainWithCrlsForOnlySomeIssuersIsAccepted(String coverage, Collection<X509CRL> crls) {
      assertDoesNotThrow(() -> validate(leaf, crls, TOLERATE_UNKNOWN_STATUS), coverage);
    }

    // A CRL outside its validity window is not evidence of anything: it neither establishes a good
    // status nor a revocation. Under this policy that unknown status is tolerated.
    @ParameterizedTest
    @MethodSource(
        "org.eclipse.milo.opcua.stack.core.util.validation.CertificateRevocationTest#unusableCrls")
    void unusableCrlIsTreatedAsUnknownStatus(String shape, X509CRL intermediateCrl)
        throws Exception {
      Collection<X509CRL> crls = List.of(rootCrl(), intermediateCrl);

      assertDoesNotThrow(() -> validate(leaf, crls, TOLERATE_UNKNOWN_STATUS), shape);
    }

    // Tolerating an unknown issuer status must not stop the check before it reaches a CRL that
    // does list the leaf as revoked.
    @Test
    void revokedLeafIsRejectedEvenWhenTheRootCrlIsMissing() throws Exception {
      Collection<X509CRL> crls = List.of(intermediateCrl(leaf));

      UaException e =
          assertThrows(UaException.class, () -> validate(leaf, crls, TOLERATE_UNKNOWN_STATUS));

      assertEquals(StatusCodes.Bad_CertificateRevoked, e.getStatusCode().value());
    }
  }

  @Nested
  class CrlsRequired {

    @Test
    void completeCrlsAreAccepted() throws Exception {
      assertDoesNotThrow(() -> validate(leaf, List.of(rootCrl(), intermediateCrl()), REQUIRE_CRLS));
    }

    // Without any CRL, no status in the path can be established. The failure is reported against
    // the issuer whose status could not be established.
    @Test
    void caSignedChainWithoutCrlsIsRejected() {
      UaException e =
          assertThrows(UaException.class, () -> validate(leaf, List.of(), REQUIRE_CRLS));

      assertEquals(StatusCodes.Bad_CertificateIssuerRevocationUnknown, e.getStatusCode().value());
    }

    // The root's CRL establishes the intermediate's status, so the only unknown is the leaf's.
    @Test
    void missingLeafIssuerCrlIsRejectedAsRevocationUnknown() throws Exception {
      Collection<X509CRL> crls = List.of(rootCrl());

      UaException e = assertThrows(UaException.class, () -> validate(leaf, crls, REQUIRE_CRLS));

      assertEquals(StatusCodes.Bad_CertificateRevocationUnknown, e.getStatusCode().value());
    }

    // A trusted CA-issued leaf is still validated as a chain, and the trust anchor's own CRL is
    // needed to establish the status of the certificate it issued.
    @Test
    void missingRootCrlIsRejectedAsIssuerRevocationUnknown() throws Exception {
      Collection<X509CRL> crls = List.of(intermediateCrl());

      UaException e = assertThrows(UaException.class, () -> validate(leaf, crls, REQUIRE_CRLS));

      assertEquals(StatusCodes.Bad_CertificateIssuerRevocationUnknown, e.getStatusCode().value());
    }

    // An unusable CRL must never establish a good status. Requiring CRLs means requiring usable
    // ones.
    @ParameterizedTest
    @MethodSource(
        "org.eclipse.milo.opcua.stack.core.util.validation.CertificateRevocationTest#unusableCrls")
    void unusableCrlIsRejectedAsRevocationUnknown(String shape, X509CRL intermediateCrl)
        throws Exception {
      Collection<X509CRL> crls = List.of(rootCrl(), intermediateCrl);

      UaException e =
          assertThrows(UaException.class, () -> validate(leaf, crls, REQUIRE_CRLS), shape);

      assertEquals(StatusCodes.Bad_CertificateRevocationUnknown, e.getStatusCode().value(), shape);
    }
  }

  @Nested
  class EnforcedUnderEitherPolicy {

    // A directly trusted self-signed application certificate has no issuing CA whose list must be
    // obtained, so it needs no CRL even when CRLs are required.
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void trustedSelfSignedCertificateNeedsNoCrl(boolean requireCrls) throws Exception {
      PKIXCertPathBuilderResult path =
          buildTrustedCertPath(List.of(selfSigned), Set.of(selfSigned), Set.of());

      assertDoesNotThrow(
          () ->
              validateTrustedCertPath(
                  path.getCertPath(), path.getTrustAnchor(), List.of(), policy(requireCrls), true));
    }

    // Part 4 §6.1.3: once a revocation list establishes that a certificate is revoked, the failure
    // may not be suppressed.
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void revokedLeafIsRejected(boolean requireCrls) throws Exception {
      Collection<X509CRL> crls = List.of(rootCrl(), intermediateCrl(leaf));

      UaException e =
          assertThrows(UaException.class, () -> validate(leaf, crls, policy(requireCrls)));

      assertEquals(StatusCodes.Bad_CertificateRevoked, e.getStatusCode().value());
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void revokedIntermediateIsRejectedAsIssuerRevoked(boolean requireCrls) throws Exception {
      Collection<X509CRL> crls = List.of(rootCrl(intermediate), intermediateCrl());

      UaException e =
          assertThrows(UaException.class, () -> validate(leaf, crls, policy(requireCrls)));

      assertEquals(StatusCodes.Bad_CertificateIssuerRevoked, e.getStatusCode().value());
    }
  }

  /**
   * Part 4 §6.1.3 requires a suppressed validation failure to be reported. These tests pin that a
   * tolerated unknown status is handed back with enough context to name the certificate and reason.
   */
  @Nested
  class ToleratedFailureReporting {

    @Test
    void eachCertificateWithUnknownStatusIsReported() throws Exception {
      PKIXCertPathBuilderResult path = buildPath(leaf);

      List<CertPathValidatorException> reported =
          validateIssuedCertPath(
              path.getCertPath(),
              path.getTrustAnchor(),
              List.of(),
              TOLERATE_UNKNOWN_STATUS,
              true,
              null);

      assertEquals(
          List.of(0, 1),
          reported.stream().map(CertPathValidatorException::getIndex).toList(),
          "one report per certificate in the path, leaf first");
      assertTrue(
          reported.stream()
              .allMatch(e -> e.getReason() == BasicReason.UNDETERMINED_REVOCATION_STATUS));
      assertEquals(leaf, reported.get(0).getCertPath().getCertificates().get(0));
      assertEquals(intermediate, reported.get(1).getCertPath().getCertificates().get(1));
    }

    @Test
    void nothingIsReportedWhenEveryStatusIsEstablished() throws Exception {
      PKIXCertPathBuilderResult path = buildPath(leaf);

      List<CertPathValidatorException> reported =
          validateIssuedCertPath(
              path.getCertPath(),
              path.getTrustAnchor(),
              List.of(rootCrl(), intermediateCrl()),
              TOLERATE_UNKNOWN_STATUS,
              true,
              null);

      assertEquals(List.of(), reported);
    }

    // Each validation configures a fresh checker; reports from one validation must not leak into
    // the next.
    @Test
    void reportsDoNotAccumulateAcrossValidations() throws Exception {
      PKIXCertPathBuilderResult path = buildPath(leaf);

      validateIssuedCertPath(
          path.getCertPath(),
          path.getTrustAnchor(),
          List.of(),
          TOLERATE_UNKNOWN_STATUS,
          true,
          null);

      List<CertPathValidatorException> reported =
          validateIssuedCertPath(
              path.getCertPath(),
              path.getTrustAnchor(),
              List.of(),
              TOLERATE_UNKNOWN_STATUS,
              true,
              null);

      assertEquals(2, reported.size());
    }
  }

  /**
   * The JDK checker can retrieve a CRL from a distribution point named in a certificate. Trust-list
   * CRLs are the intended source, so when they cover every issuer the distribution point must not
   * be contacted; validation then has no network dependency or delay.
   */
  @Nested
  class DistributionPoints {

    private final AtomicInteger requests = new AtomicInteger();

    private HttpServer distributionPoint;
    private X509Certificate leafWithDistributionPoint;

    @BeforeEach
    void startDistributionPoint() throws Exception {
      byte[] servedCrl = intermediateCrl().getEncoded();

      distributionPoint = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
      distributionPoint.createContext(
          "/intermediate.crl",
          exchange -> {
            requests.incrementAndGet();
            exchange.sendResponseHeaders(200, servedCrl.length);
            try (OutputStream body = exchange.getResponseBody()) {
              body.write(servedCrl);
            }
          });
      distributionPoint.start();

      String url =
          "http://127.0.0.1:" + distributionPoint.getAddress().getPort() + "/intermediate.crl";

      leafWithDistributionPoint =
          new CaSignedCertificateBuilder(
                  SelfSignedCertificateGenerator.generateRsaKeyPair(2048),
                  intermediate,
                  intermediateKey)
              .setCommonName("Test Leaf With Distribution Point")
              .setOrganization("Eclipse Milo")
              .setApplicationUri("urn:eclipse:milo:test:leaf-distribution-point")
              .addDnsName("localhost")
              .setIsCa(false)
              .setKeyUsage(
                  KeyUsage.digitalSignature
                      | KeyUsage.nonRepudiation
                      | KeyUsage.keyEncipherment
                      | KeyUsage.dataEncipherment)
              .setExtendedKeyUsage(
                  List.of(KeyPurposeId.id_kp_serverAuth, KeyPurposeId.id_kp_clientAuth))
              .addCrlDistributionPoint(url)
              .build();
    }

    @AfterEach
    void stopDistributionPoint() {
      distributionPoint.stop(0);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void suppliedCrlsAreUsedWithoutContactingTheDistributionPoint(boolean requireCrls)
        throws Exception {
      Collection<X509CRL> crls = List.of(rootCrl(), intermediateCrl());

      assertDoesNotThrow(() -> validate(leafWithDistributionPoint, crls, policy(requireCrls)));
      assertEquals(0, requests.get(), "distribution point was contacted");
    }

    // A supplied CRL that revokes the leaf is final; the checker must not fetch another copy that
    // might say otherwise.
    @Test
    void suppliedRevokingCrlIsEnforcedWithoutContactingTheDistributionPoint() throws Exception {
      Collection<X509CRL> crls = List.of(rootCrl(), intermediateCrl(leafWithDistributionPoint));

      UaException e =
          assertThrows(
              UaException.class,
              () -> validate(leafWithDistributionPoint, crls, TOLERATE_UNKNOWN_STATUS));

      assertEquals(StatusCodes.Bad_CertificateRevoked, e.getStatusCode().value());
      assertEquals(0, requests.get(), "distribution point was contacted");
    }
  }

  /**
   * The JDK initializes its revocation checker from the JVM-wide {@code ocsp.*} security properties
   * even when the checker will only evaluate CRLs. Another component in the same JVM may
   * legitimately set {@code ocsp.responderURL} or {@code ocsp.responderCertSubjectName} for its own
   * OCSP use, and neither may break Milo's CRL validation. A security property cannot be unset once
   * set, so the properties are applied in a child JVM rather than in this one.
   */
  @Nested
  class UnrelatedOcspConfiguration {

    @Test
    void globalOcspResponderPropertyDoesNotBreakCrlValidation() throws Exception {
      List<String> output = runInChildJvm(OcspResponderPropertyMain.class);

      // Control: the properties were in effect, because the bare JDK checker rejects them.
      String jdk = line(output, "JDK=");
      assertTrue(jdk.contains("ocsp.responder"), "control failed, JDK outcome was: " + jdk);

      assertEquals("MILO_TOLERATE_UNKNOWN=ACCEPTED", line(output, "MILO_TOLERATE_UNKNOWN="));
      assertEquals("MILO_REQUIRE_CRLS=ACCEPTED", line(output, "MILO_REQUIRE_CRLS="));
    }

    private String line(List<String> output, String prefix) {
      return output.stream()
          .filter(l -> l.startsWith(prefix))
          .findFirst()
          .orElseThrow(
              () ->
                  new AssertionError("no '" + prefix + "' line in:\n" + String.join("\n", output)));
    }
  }

  /** Entry point of the child JVM started by {@link UnrelatedOcspConfiguration}. */
  public static final class OcspResponderPropertyMain {

    public static void main(String[] args) throws Exception {
      // The JDK parses the URL first and looks the responder certificate up second; each step
      // fails on its own, so Milo's validation only passes if both are bypassed.
      Security.setProperty("ocsp.responderURL", "http://ocsp.example/not a uri");
      Security.setProperty("ocsp.responderCertSubjectName", "CN=Unrelated OCSP Responder");

      TestCertificates certificates = TestCertificateGenerator.generateAll();
      X509Certificate root = certificates.getCertificate(ALIAS_CA_ROOT);
      X509Certificate intermediate = certificates.getCertificate(ALIAS_CA_INTERMEDIATE);
      X509Certificate leaf = certificates.getCertificate(ALIAS_LEAF_INTERMEDIATE_SIGNED);

      PKIXCertPathBuilderResult path =
          buildTrustedCertPath(List.of(leaf), Set.of(intermediate), Set.of(root));
      List<X509CRL> crls =
          List.of(
              CrlTestUtil.generateCrl(root, certificates.getPrivateKey(ALIAS_CA_ROOT)),
              CrlTestUtil.generateCrl(
                  intermediate, certificates.getPrivateKey(ALIAS_CA_INTERMEDIATE)));

      System.out.println("JDK=" + jdkOutcome(path, crls));
      System.out.println(
          "MILO_TOLERATE_UNKNOWN=" + miloOutcome(path, crls, TOLERATE_UNKNOWN_STATUS));
      System.out.println("MILO_REQUIRE_CRLS=" + miloOutcome(path, crls, REQUIRE_CRLS));
    }

    /** The JDK checker configured as Milo configures it, minus Milo's isolation. */
    private static String jdkOutcome(PKIXCertPathBuilderResult path, List<X509CRL> crls) {
      try {
        CertPathValidator validator = CertPathValidator.getInstance("PKIX", "SUN");
        PKIXParameters parameters = new PKIXParameters(Set.of(path.getTrustAnchor()));
        parameters.setRevocationEnabled(false);
        parameters.addCertStore(
            CertStore.getInstance("Collection", new CollectionCertStoreParameters(crls)));

        PKIXRevocationChecker checker = (PKIXRevocationChecker) validator.getRevocationChecker();
        checker.setOptions(EnumSet.of(Option.PREFER_CRLS, Option.NO_FALLBACK, Option.SOFT_FAIL));
        parameters.addCertPathChecker(checker);

        validator.validate(path.getCertPath(), parameters);
        return "ACCEPTED";
      } catch (Exception e) {
        return e.getMessage();
      }
    }

    private static String miloOutcome(
        PKIXCertPathBuilderResult path, List<X509CRL> crls, Set<ValidationCheck> checks) {
      try {
        validateTrustedCertPath(path.getCertPath(), path.getTrustAnchor(), crls, checks, true);
        return "ACCEPTED";
      } catch (UaException e) {
        return e.getStatusCode().toString();
      }
    }
  }

  // Output goes to a file rather than a pipe so the wait below is bounded by the timeout rather
  // than by the child closing its stdout, and so a chatty child cannot block on a full pipe.
  private static List<String> runInChildJvm(Class<?> mainClass) throws Exception {
    String java = Path.of(System.getProperty("java.home"), "bin", "java").toString();
    String classPath =
        System.getProperty("surefire.test.class.path", System.getProperty("java.class.path"));
    Path outputFile = Files.createTempFile(mainClass.getSimpleName(), ".out");

    try {
      Process process =
          new ProcessBuilder(java, "-cp", classPath, mainClass.getName())
              .redirectErrorStream(true)
              .redirectOutput(outputFile.toFile())
              .start();

      if (!process.waitFor(2, TimeUnit.MINUTES)) {
        process.destroyForcibly();
        fail("child JVM did not exit:\n" + Files.readString(outputFile));
      }

      String output = Files.readString(outputFile);
      assertEquals(0, process.exitValue(), output);

      return output.lines().toList();
    } finally {
      Files.deleteIfExists(outputFile);
    }
  }

  static Stream<Arguments> partialCrls() throws Exception {
    return Stream.of(
        Arguments.of("root CRL only", List.of(rootCrl())),
        Arguments.of("intermediate CRL only", List.of(intermediateCrl())));
  }

  // Offsets exceed the 15 minutes of clock skew the JDK checker allows.
  static Stream<Arguments> unusableCrls() throws Exception {
    long now = System.currentTimeMillis();

    return Stream.of(
        Arguments.of(
            "expired",
            CrlTestUtil.generateCrl(
                intermediate,
                intermediateKey,
                new Date(now - 2 * ONE_HOUR_MS),
                new Date(now - ONE_HOUR_MS))),
        Arguments.of(
            "not yet valid",
            CrlTestUtil.generateCrl(
                intermediate,
                intermediateKey,
                new Date(now + ONE_HOUR_MS),
                new Date(now + 2 * ONE_HOUR_MS))));
  }

  private static Set<ValidationCheck> policy(boolean requireCrls) {
    return requireCrls ? REQUIRE_CRLS : TOLERATE_UNKNOWN_STATUS;
  }

  private static X509CRL rootCrl(X509Certificate... revoked) throws Exception {
    return CrlTestUtil.generateCrl(root, rootKey, revoked);
  }

  private static X509CRL intermediateCrl(X509Certificate... revoked) throws Exception {
    return CrlTestUtil.generateCrl(intermediate, intermediateKey, revoked);
  }

  /** Builds the path leaf, intermediate with the root as trust anchor. */
  private static PKIXCertPathBuilderResult buildPath(X509Certificate endEntity) throws UaException {
    return buildTrustedCertPath(List.of(endEntity), Set.of(intermediate), Set.of(root));
  }

  private static void validate(
      X509Certificate endEntity, Collection<X509CRL> crls, Set<ValidationCheck> checks)
      throws UaException {

    PKIXCertPathBuilderResult path = buildPath(endEntity);

    validateTrustedCertPath(path.getCertPath(), path.getTrustAnchor(), crls, checks, true);
  }
}
