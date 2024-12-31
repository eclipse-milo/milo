/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util.validation;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertPath;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertPathValidatorException.BasicReason;
import java.security.cert.CertStore;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateParsingException;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.PKIXParameters;
import java.security.cert.PKIXRevocationChecker;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CRL;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.cert.X509CertificateHolder;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CertificateValidationUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(CertificateValidationUtil.class);

  private static final String KEY_USAGE_OID = "2.5.29.15";
  private static final String EXTENDED_KEY_USAGE_OID = "2.5.29.37";
  private static final String SERVER_AUTH_OID = "1.3.6.1.5.5.7.3.1";
  private static final String CLIENT_AUTH_OID = "1.3.6.1.5.5.7.3.2";

  private static final int SUBJECT_ALT_NAME_URI = 6;
  private static final int SUBJECT_ALT_NAME_DNS_NAME = 2;
  private static final int SUBJECT_ALT_NAME_IP_ADDRESS = 7;

  /**
   * Given a possibly partial certificate chain with at least one certificate in it, builds a path
   * to a trust anchor using a collection of trusted and issuer certificates as possible
   * intermediate and root CAs, and then ensures that at least one certificate in the resulting path
   * is in the trusted collection.
   *
   * <p>Each certificate has its signature and subject/issuer chaining checked. Revocation and other
   * more detailed checks are not done at this stage.
   *
   * <p>The {@link CertPath} and {@link TrustAnchor} from the result is meant to be further
   * validated by {@link #validateTrustedCertPath(CertPath, TrustAnchor, Collection, Set, boolean)},
   * which can return more detailed failure {@link StatusCodes} in its exceptions because it is
   * dealing with a known trusted path.
   *
   * @param certificateChain a possibly partial certificate chain to build a trusted path from.
   * @param trustedCertificates a collection of known trusted certificates.
   * @param issuerCertificates a collection of known CAs that can be used in path building but are
   *     not considered "trusted" when it comes to determining if the resulting path is "trusted".
   * @return a {@link PKIXCertPathBuilderResult} with a {@link TrustAnchor} and {@link CertPath},
   *     which combined make up the full trusted path.
   * @throws UaException if a trusted path cannot be built.
   */
  public static PKIXCertPathBuilderResult buildTrustedCertPath(
      List<X509Certificate> certificateChain,
      Collection<X509Certificate> trustedCertificates,
      Collection<X509Certificate> issuerCertificates)
      throws UaException {

    Preconditions.checkArgument(!certificateChain.isEmpty(), "certificateChain must not be empty");

    if (LOGGER.isTraceEnabled()) {
      LOGGER.trace("certificateChain: {}", certificateChain);
      LOGGER.trace("trustedCertificates: {}", trustedCertificates);
      LOGGER.trace("issuerCertificates: {}", issuerCertificates);
    }

    Set<TrustAnchor> trustAnchors = new HashSet<>();
    for (X509Certificate c : trustedCertificates) {
      if (certificateIsSelfSigned(c)) {
        if (certificateChain.size() == 1 && certificateChain.get(0).equals(c)) {
          // The end-entity certificate is self-signed and trusted.
          // OPC UA self-signed certificates should indicate that they are not CA
          // certificates, though ones that do are accepted for backwards compatibility.
          trustAnchors.add(new TrustAnchor(c, null));

          if (c.getBasicConstraints() != -1) {
            LOGGER.warn(
                "self-signed certificate '{}' has BasicConstraint cA bit set",
                c.getSubjectX500Principal().getName());
          }
        } else if (certificateIsCa(c)) {
          trustAnchors.add(new TrustAnchor(c, null));
        }
      }
    }
    for (X509Certificate c : issuerCertificates) {
      if (certificateIsCa(c) && certificateIsSelfSigned(c)) {
        trustAnchors.add(new TrustAnchor(c, null));
      }
    }

    PKIXCertPathBuilderResult certPathResult =
        buildCertPath(certificateChain, trustedCertificates, issuerCertificates, trustAnchors);

    CertPath certPath = certPathResult.getCertPath();
    TrustAnchor trustAnchor = certPathResult.getTrustAnchor();

    List<X509Certificate> certificates = new ArrayList<>();
    certPath.getCertificates().stream().map(X509Certificate.class::cast).forEach(certificates::add);
    certificates.add(trustAnchor.getTrustedCert());

    if (LOGGER.isTraceEnabled()) {
      List<String> path =
          certificates.stream()
              .map(c -> c.getSubjectX500Principal().getName())
              .collect(Collectors.toList());
      LOGGER.trace("certificate path: {}", path);
    }

    if (certificates.stream().noneMatch(trustedCertificates::contains)) {
      throw new UaException(
          StatusCodes.Bad_CertificateUntrusted,
          "certificate chain did not contain a trusted certificate");
    }

    return certPathResult;
  }

  /**
   * Validates the trusted certificate path represented by a {@link TrustAnchor} and a {@link
   * CertPath} that may or may not be empty, depending on the length of the path.
   *
   * <p>Each certificate is checked for validity, key usage, and revocation status. Whether a failed
   * check ultimately results in a thrown exception depends on the set of {@link ValidationCheck}s.
   *
   * <p>The function is meant to be used in conjunction with {@link #buildTrustedCertPath(List,
   * Collection, Collection)}, the result of which contains a {@link CertPath} and {@link
   * TrustAnchor} that form a trusted certificate path.
   *
   * @param certPath a {@link CertPath} containing 0 or more certificates leading to the trust
   *     anchor.
   * @param trustAnchor a {@link TrustAnchor} containing the root of trust for the path being
   *     validated.
   * @param crls a collection of {@link X509CRL}s. Every CA certificate in the trusted path except
   *     the leaf should have a CRL, though whether that's enforced or not depends on {@link
   *     ValidationCheck#REVOCATION_LISTS} being present.
   * @param validationChecks the set of {@link ValidationCheck}s to enforce.
   * @param endEntityIsClient {@code true} if the end-entity is a client, {@code false} if it is a
   *     server.
   * @throws UaException if a check from the set of {@link ValidationCheck}s failed.
   */
  public static void validateTrustedCertPath(
      CertPath certPath,
      TrustAnchor trustAnchor,
      Collection<X509CRL> crls,
      Set<ValidationCheck> validationChecks,
      boolean endEntityIsClient)
      throws UaException {

    X509Certificate anchorCert = trustAnchor.getTrustedCert();
    boolean anchorIsEndEntity = certPath.getCertificates().isEmpty();

    checkAnchorValidity(anchorCert, validationChecks, anchorIsEndEntity, endEntityIsClient);

    if (!anchorIsEndEntity) {
      // anchorCert is an issuer; validate the rest of the certPath
      try {
        CertPathValidator certPathValidator = CertPathValidator.getInstance("PKIX", "SUN");

        PKIXParameters parameters = new PKIXParameters(Set.of(trustAnchor));

        parameters.addCertPathChecker(
            new OpcUaCertificateUsageChecker(certPath, validationChecks, endEntityIsClient));

        try {
          // Try to add our own custom revocation checker that can
          // optionally suppress failures to locate the CRLs or allow
          // certificates even if they've been revoked.

          parameters.setRevocationEnabled(true);

          if (!crls.isEmpty()) {
            parameters.addCertStore(
                CertStore.getInstance("Collection", new CollectionCertStoreParameters(crls)));
          }

          parameters.addCertPathChecker(
              new OpcUaCertificateRevocationChecker(
                  certPath, trustAnchor, parameters, validationChecks));
        } catch (Exception e) {
          // Couldn't add our custom revocation checker, so use the
          // default one. It's not as fine-grained as ours - it's
          // either enabled or it isn't, and CRL location is allowed
          // to fail, regardless of the REVOCATION_LIST_FOUND check.

          if (validationChecks.contains(ValidationCheck.REVOCATION)) {
            parameters.setRevocationEnabled(true);

            PKIXRevocationChecker pkixRevocationChecker =
                (PKIXRevocationChecker) certPathValidator.getRevocationChecker();

            pkixRevocationChecker.setOptions(
                Set.of(
                    PKIXRevocationChecker.Option.NO_FALLBACK,
                    PKIXRevocationChecker.Option.PREFER_CRLS,
                    PKIXRevocationChecker.Option.SOFT_FAIL));
          } else {
            parameters.setRevocationEnabled(false);
          }

          LOGGER.warn(
              "Failed to add custom revocation checker; "
                  + "REVOCATION_LIST_FOUND check will be ignored.");
        }

        certPathValidator.validate(certPath, parameters);
      } catch (CertPathValidatorException e) {
        CertPath path = e.getCertPath();
        CertPathValidatorException.Reason reason = e.getReason();

        int failedAtIndex = e.getIndex();

        if (failedAtIndex < 0) {
          throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
        } else {
          X509Certificate failed = (X509Certificate) path.getCertificates().get(failedAtIndex);

          LOGGER.debug(
              "cert path validation failed at index={} reason={}, certificate={}",
              failedAtIndex,
              reason,
              failed.getSubjectX500Principal().getName());

          if (reason == BasicReason.REVOKED) {
            if (failedAtIndex == 0) {
              throw new UaException(StatusCodes.Bad_CertificateRevoked, e);
            } else {
              throw new UaException(StatusCodes.Bad_CertificateIssuerRevoked, e);
            }
          } else if (reason == BasicReason.UNDETERMINED_REVOCATION_STATUS) {
            if (failedAtIndex == 0) {
              throw new UaException(StatusCodes.Bad_CertificateRevocationUnknown, e);
            } else {
              throw new UaException(StatusCodes.Bad_CertificateIssuerRevocationUnknown, e);
            }
          } else if (reason == BasicReason.EXPIRED || reason == BasicReason.NOT_YET_VALID) {
            if (failedAtIndex == 0) {
              throw new UaException(StatusCodes.Bad_CertificateTimeInvalid, e);
            } else {
              throw new UaException(StatusCodes.Bad_CertificateIssuerTimeInvalid, e);
            }
          } else {
            throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
          }
        }
      } catch (GeneralSecurityException e) {
        throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
      }
    }
  }

  private static void checkAnchorValidity(
      X509Certificate anchorCert,
      Set<ValidationCheck> validationChecks,
      boolean endEntity,
      boolean endEntityIsClient)
      throws UaException {

    Set<String> criticalExtensions = anchorCert.getCriticalExtensionOIDs();
    if (criticalExtensions == null) criticalExtensions = Collections.emptySet();

    try {
      checkValidity(anchorCert, endEntity);
    } catch (UaException e) {
      if (validationChecks.contains(ValidationCheck.VALIDITY)) {
        throw e;
      } else {
        LOGGER.warn(
            "check suppressed: certificate failed end-entity validity check: {}",
            anchorCert.getSubjectX500Principal().getName());
      }
    }

    if (endEntity) {
      try {
        checkEndEntityKeyUsage(anchorCert);
      } catch (UaException e) {
        if (validationChecks.contains(ValidationCheck.KEY_USAGE_END_ENTITY)
            || criticalExtensions.contains(KEY_USAGE_OID)) {

          throw e;
        } else {
          LOGGER.warn(
              "check suppressed: certificate failed end-entity KeyUsage check: {}",
              anchorCert.getSubjectX500Principal().getName());
        }
      }

      try {
        checkEndEntityExtendedKeyUsage(anchorCert, endEntityIsClient);
      } catch (UaException e) {
        if (validationChecks.contains(ValidationCheck.EXTENDED_KEY_USAGE_END_ENTITY)
            || criticalExtensions.contains(EXTENDED_KEY_USAGE_OID)) {

          throw e;
        } else {
          LOGGER.warn(
              "check suppressed: certificate failed end-entity ExtendedKeyUsage check: {}",
              anchorCert.getSubjectX500Principal().getName());
        }
      }
    }
  }

  private static PKIXCertPathBuilderResult buildCertPath(
      List<X509Certificate> certificateChain,
      Collection<X509Certificate> trustedCertificates,
      Collection<X509Certificate> issuerCertificates,
      Set<TrustAnchor> trustAnchors)
      throws UaException {

    X509Certificate certificate = certificateChain.get(0);

    X509CertSelector selector = new X509CertSelector();
    selector.setCertificate(certificate);

    try {
      PKIXBuilderParameters builderParams = new PKIXBuilderParameters(trustAnchors, selector);

      // Add a CertStore containing potential intermediate certs
      var intermediates = new ArrayList<>(certificateChain.subList(1, certificateChain.size()));

      for (X509Certificate c : trustedCertificates) {
        if (certificateIsCa(c) && !certificateIsSelfSigned(c)) {
          intermediates.add(c);
        }
      }
      for (X509Certificate c : issuerCertificates) {
        if (certificateIsCa(c) && !certificateIsSelfSigned(c)) {
          intermediates.add(c);
        }
      }

      if (!intermediates.isEmpty()) {
        CertStore certStore =
            CertStore.getInstance("Collection", new CollectionCertStoreParameters(intermediates));

        builderParams.addCertStore(certStore);
      }

      // Disable revocation checking in the CertPathBuilder; it will be
      // checked by a PKIXCertPathValidator after the CertPath is built.
      builderParams.setRevocationEnabled(false);

      CertPathBuilder builder = CertPathBuilder.getInstance("PKIX");

      return (PKIXCertPathBuilderResult) builder.build(builderParams);
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
    }
  }

  /**
   * Check if {@code certificate}'s KeyUsage and/or BasicConstraints extensions indicate it is a CA.
   *
   * @param certificate the {@link X509Certificate} to check.
   * @return {@code true} if {@code certificate}'s KeyUsage and/or BasicConstraints extensions
   *     indicates it is a CA.
   */
  static boolean certificateIsCa(X509Certificate certificate) {
    boolean[] keyUsage = certificate.getKeyUsage();
    int basicConstraints = certificate.getBasicConstraints();

    if (keyUsage == null) {
      // no KeyUsage, just check if the cA BasicConstraint is set.
      return basicConstraints >= 0;
    } else {
      if (keyUsage[5] && basicConstraints == -1) {
        // KeyUsage is present and the keyCertSign bit is set.
        // According to RFC 5280 the BasicConstraint cA bit must also
        // be set, but it's not!
        LOGGER.debug(
            "'{}' violates RFC 5280: KeyUsage keyCertSign "
                + "bit set without BasicConstraint cA bit set",
            certificate.getSubjectX500Principal().getName());
      }

      return keyUsage[5] || basicConstraints >= 0;
    }
  }

  /**
   * Return {@code true} if a given {@link X509Certificate} is self-signed.
   *
   * @return {@code true} if a given {@link X509Certificate} is self-signed.
   */
  private static boolean certificateIsSelfSigned(X509Certificate cert) throws UaException {
    try {
      // Verify certificate signature with its own public key
      PublicKey key = cert.getPublicKey();
      cert.verify(key);

      // Check that subject and issuer are the same
      return Objects.equals(cert.getSubjectX500Principal(), cert.getIssuerX500Principal());
    } catch (SignatureException | InvalidKeyException sigEx) {
      // Invalid signature or key: not self-signed
      return false;
    } catch (Exception e) {
      throw new UaException(StatusCodes.Bad_CertificateInvalid, e);
    }
  }

  /**
   * Check that {@code certificate} is valid (the current date and time are within the validity
   * period).
   *
   * @param certificate the {@link X509Certificate} to check.
   * @param endEntity {@code true} if the certificate is the end entity, {@code false} if it's an
   *     issuer.
   * @throws UaException if the certificate is not valid.
   */
  public static void checkValidity(X509Certificate certificate, boolean endEntity)
      throws UaException {
    try {
      certificate.checkValidity();
    } catch (CertificateExpiredException e) {
      throw new UaException(
          endEntity
              ? StatusCodes.Bad_CertificateTimeInvalid
              : StatusCodes.Bad_CertificateIssuerTimeInvalid,
          String.format(
              "certificate is expired: %s - %s",
              certificate.getNotBefore(), certificate.getNotAfter()));
    } catch (CertificateNotYetValidException e) {
      throw new UaException(
          endEntity
              ? StatusCodes.Bad_CertificateTimeInvalid
              : StatusCodes.Bad_CertificateIssuerTimeInvalid,
          String.format(
              "certificate not yet valid: %s - %s",
              certificate.getNotBefore(), certificate.getNotAfter()));
    }
  }

  /**
   * Validate that one of {@code hostNames} matches a SubjectAltName DNSName or IPAddress entry in
   * the certificate.
   *
   * @param certificate the certificate to validate against.
   * @param hostNames the host names or ip addresses to look for.
   * @throws UaException if there is no matching DNSName or IPAddress entry.
   */
  public static void checkHostnameOrIpAddress(X509Certificate certificate, String... hostNames)
      throws UaException {

    boolean dnsNameMatch =
        Arrays.stream(hostNames)
            .anyMatch(
                n -> {
                  try {
                    return checkSubjectAltNameField(
                        certificate,
                        SUBJECT_ALT_NAME_DNS_NAME,
                        fieldValue ->
                            (fieldValue instanceof String dnsName) && dnsName.equalsIgnoreCase(n));
                  } catch (Throwable t) {
                    return false;
                  }
                });

    boolean ipAddressMatch =
        Arrays.stream(hostNames)
            .anyMatch(
                n -> {
                  try {
                    return checkSubjectAltNameField(
                        certificate, SUBJECT_ALT_NAME_IP_ADDRESS, n::equals);
                  } catch (Throwable t) {
                    return false;
                  }
                });

    if (!(dnsNameMatch || ipAddressMatch)) {
      throw new UaException(StatusCodes.Bad_CertificateHostNameInvalid);
    }
  }

  public static void checkEndEntityKeyUsage(X509Certificate certificate) throws UaException {
    boolean[] keyUsage = certificate.getKeyUsage();

    if (keyUsage == null) {
      throw new UaException(
          StatusCodes.Bad_CertificateUseNotAllowed, "KeyUsage extension not found");
    }

    boolean digitalSignature = keyUsage[0];
    boolean nonRepudiation = keyUsage[1];
    boolean keyEncipherment = keyUsage[2];
    boolean dataEncipherment = keyUsage[3];
    boolean keyCertSign = keyUsage[5];

    if (!digitalSignature) {
      throw new UaException(
          StatusCodes.Bad_CertificateUseNotAllowed,
          "required KeyUsage 'digitalSignature' not found");
    }

    if (!nonRepudiation) {
      throw new UaException(
          StatusCodes.Bad_CertificateUseNotAllowed, "required KeyUsage 'nonRepudiation' not found");
    }

    if (!keyEncipherment) {
      throw new UaException(
          StatusCodes.Bad_CertificateUseNotAllowed,
          "required KeyUsage 'keyEncipherment' not found");
    }

    if (!dataEncipherment) {
      throw new UaException(
          StatusCodes.Bad_CertificateUseNotAllowed,
          "required KeyUsage 'dataEncipherment' not found");
    }

    if (!keyCertSign && certificateIsSelfSigned(certificate)) {
      throw new UaException(
          StatusCodes.Bad_CertificateUseNotAllowed, "required KeyUsage 'keyCertSign' not found");
    }
  }

  public static void checkEndEntityExtendedKeyUsage(
      X509Certificate certificate, boolean endEntityIsClient) throws UaException {

    try {
      List<String> extendedKeyUsage = certificate.getExtendedKeyUsage();

      if (extendedKeyUsage == null) {
        throw new UaException(
            StatusCodes.Bad_CertificateUseNotAllowed, "ExtendedKeyUsage extension not found");
      }

      if (endEntityIsClient && !extendedKeyUsage.contains(CLIENT_AUTH_OID)) {
        throw new UaException(
            StatusCodes.Bad_CertificateUseNotAllowed,
            "required ExtendedKeyUsage 'clientAuth' not found");
      }

      if (!endEntityIsClient && !extendedKeyUsage.contains(SERVER_AUTH_OID)) {
        throw new UaException(
            StatusCodes.Bad_CertificateUseNotAllowed,
            "required ExtendedKeyUsage 'serverAuth' not found");
      }
    } catch (CertificateParsingException e) {
      throw new UaException(StatusCodes.Bad_CertificateUseNotAllowed);
    }
  }

  /**
   * Validate that the application URI matches the SubjectAltName URI in the given certificate.
   *
   * @param certificate the certificate to validate against.
   * @param applicationUri the URI to validate.
   * @throws UaException if the certificate is invalid, does not contain a uri, or contains a uri
   *     that does not match.
   */
  public static void checkApplicationUri(X509Certificate certificate, String applicationUri)
      throws UaException {
    if (!checkSubjectAltNameField(certificate, SUBJECT_ALT_NAME_URI, applicationUri::equals)) {
      // First check failed, maybe it was because Java's X509Certificate represents the
      // SAN URI extension as a java.net.URI and therefore can't handle an invalid URI.
      // Try again Using BouncyCastle to read and compare the URIs.
      try {
        X509CertificateHolder certificateHolder =
            new X509CertificateHolder(certificate.getEncoded());

        GeneralNames generalNames =
            GeneralNames.fromExtensions(
                certificateHolder.getExtensions(), Extension.subjectAlternativeName);

        if (generalNames != null) {
          for (GeneralName generalName : generalNames.getNames()) {
            if (generalName.getTagNo() == GeneralName.uniformResourceIdentifier) {
              String uri = generalName.getName().toString();
              if (!Objects.equals(applicationUri, uri)) {
                throw new UaException(StatusCodes.Bad_CertificateUriInvalid);
              }
              return;
            }
          }
        }

        throw new UaException(
            StatusCodes.Bad_CertificateUriInvalid,
            "no match in certificate for application URI '" + applicationUri + "'");
      } catch (IOException | CertificateEncodingException e) {
        throw new UaException(StatusCodes.Bad_CertificateUriInvalid, e);
      }
    }
  }

  /**
   * Test the value of some SubjectAlternativeNames field against a predicate.
   *
   * @param certificate an {@link X509Certificate}.
   * @param field the field id.
   * @param fieldPredicate a predicate to test the field value.
   * @return {@code true} if the field was found and the predicate tested true.
   */
  private static boolean checkSubjectAltNameField(
      X509Certificate certificate, int field, Predicate<Object> fieldPredicate) {

    try {
      Collection<List<?>> subjectAltNames = certificate.getSubjectAlternativeNames();
      if (subjectAltNames == null) subjectAltNames = Collections.emptyList();

      for (List<?> idAndValue : subjectAltNames) {
        if (idAndValue != null && idAndValue.size() == 2) {
          if (idAndValue.get(0).equals(field)) {
            if (fieldPredicate.test(idAndValue.get(1))) {
              return true;
            }
          }
        }
      }

      return false;
    } catch (CertificateParsingException e) {
      return false;
    }
  }

  public static String getSubjectAltNameUri(X509Certificate certificate) throws UaException {
    try {
      Collection<List<?>> subjectAltNames = certificate.getSubjectAlternativeNames();
      if (subjectAltNames == null) subjectAltNames = Collections.emptyList();

      for (List<?> idAndValue : subjectAltNames) {
        if (idAndValue != null && idAndValue.size() == 2) {
          if (idAndValue.get(0).equals(SUBJECT_ALT_NAME_URI)) {
            Object uri = idAndValue.get(1);
            return uri != null ? uri.toString() : null;
          }
        }
      }

      return null;
    } catch (CertificateParsingException e) {
      throw new UaException(StatusCodes.Bad_CertificateInvalid, e);
    }
  }
}
