/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util;

import static org.eclipse.milo.opcua.stack.core.util.DigestUtil.sha1;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Bytes;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.KeyPair;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x500.style.RFC4519Style;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.ExtensionsGenerator;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.MiscPEMGenerator;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.io.pem.PemWriter;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;

public class CertificateUtil {

  public static final int SUBJECT_ALT_NAME_DNS_NAME = GeneralName.dNSName;
  public static final int SUBJECT_ALT_NAME_IP_ADDRESS = GeneralName.iPAddress;
  public static final int SUBJECT_ALT_NAME_URI = GeneralName.uniformResourceIdentifier;

  /**
   * Decode a DER-encoded X.509 certificate.
   *
   * @param certificateBytes DER-encoded certificate bytes.
   * @return an {@link X509Certificate}
   * @throws UaException if decoding the certificate fails.
   */
  public static X509Certificate decodeCertificate(byte[] certificateBytes) throws UaException {
    Preconditions.checkNotNull(certificateBytes, "certificateBytes cannot be null");

    return decodeCertificate(new ByteArrayInputStream(certificateBytes));
  }

  /**
   * Decode a DER-encoded X.509 certificate.
   *
   * @param inputStream {@link InputStream} containing DER-encoded certificate bytes.
   * @return an {@link X509Certificate}
   * @throws UaException if decoding the certificate fails.
   */
  public static X509Certificate decodeCertificate(InputStream inputStream) throws UaException {
    return decodeCertificates(inputStream).get(0);
  }

  /**
   * Decode either a sequence of DER-encoded X.509 certificates or a PKCS#7 certificate chain.
   *
   * @param certificateBytes the byte[] to decode from.
   * @return a {@link List} of certificates deocded from {@code certificateBytes}.
   * @throws UaException if decoding fails.
   */
  public static List<X509Certificate> decodeCertificates(byte[] certificateBytes)
      throws UaException {
    return decodeCertificates(new ByteArrayInputStream(certificateBytes));
  }

  /**
   * Decode either a sequence of DER-encoded X.509 certificates or a PKCS#7 certificate chain.
   *
   * @param inputStream the {@link InputStream} to decode from.
   * @return a {@link List} of certificates decoded from {@code inputStream}.
   * @throws UaException if decoding fails.
   */
  public static List<X509Certificate> decodeCertificates(InputStream inputStream)
      throws UaException {
    Preconditions.checkNotNull(inputStream, "inputStream cannot be null");

    CertificateFactory factory;

    try {
      factory = CertificateFactory.getInstance("X.509");
    } catch (CertificateException e) {
      throw new UaException(StatusCodes.Bad_InternalError, e);
    }

    try {
      Collection<? extends Certificate> certificates = factory.generateCertificates(inputStream);

      return certificates.stream().map(X509Certificate.class::cast).collect(Collectors.toList());
    } catch (CertificateException e) {
      throw new UaException(StatusCodes.Bad_CertificateInvalid, e);
    }
  }

  /**
   * Generate a {@link PKCS10CertificationRequest} for the provided {@code certificate} and {@code
   * keyPair}.
   *
   * @param keyPair the {@link KeyPair} for {@code certificate}.
   * @param certificate the {@link X509Certificate} to request signing for.
   * @return a {@link PKCS10CertificationRequest}.
   * @throws Exception if creating the signing request fails for any reason.
   */
  public static PKCS10CertificationRequest generateCsr(KeyPair keyPair, X509Certificate certificate)
      throws Exception {

    PKCS10CertificationRequestBuilder builder =
        new JcaPKCS10CertificationRequestBuilder(
            certificate.getSubjectX500Principal(), certificate.getPublicKey());

    GeneralNames subjectAltNames =
        new GeneralNames(getSubjectAltNames(certificate).toArray(new GeneralName[0]));

    ExtensionsGenerator extGen = new ExtensionsGenerator();
    extGen.addExtension(Extension.subjectAlternativeName, false, subjectAltNames);
    builder.addAttribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest, extGen.generate());

    JcaContentSignerBuilder signerBuilder =
        new JcaContentSignerBuilder(certificate.getSigAlgName());

    ContentSigner signer = signerBuilder.build(keyPair.getPrivate());

    return builder.build(signer);
  }

  /**
   * Generate a {@link PKCS10CertificationRequest}.
   *
   * @param keyPair the {@link KeyPair} containing Public and Private keys.
   * @param subjectName the subject name, in RFC 4519 style. (CN=foo,O=bar)
   * @param sanUri the URI to request in the SAN.
   * @param sanDnsNames the DNS names to request in the SAN.
   * @param sanIpAddresses the IP addresses to request in the SAN.
   * @param signatureAlgorithm the signature algorithm to use when generating the signature to
   *     validate the certificate.
   * @return a {@link PKCS10CertificationRequest}.
   * @throws Exception if creating the signing request fails for any reason.
   */
  public static PKCS10CertificationRequest generateCsr(
      KeyPair keyPair,
      String subjectName,
      String sanUri,
      List<String> sanDnsNames,
      List<String> sanIpAddresses,
      String signatureAlgorithm)
      throws Exception {

    X500Name subject = new X500Name(IETFUtils.rDNsFromString(subjectName, RFC4519Style.INSTANCE));

    return generateCsr(keyPair, subject, sanUri, sanDnsNames, sanIpAddresses, signatureAlgorithm);
  }

  /**
   * Generate a {@link PKCS10CertificationRequest}.
   *
   * @param keyPair the {@link KeyPair} containing Public and Private keys.
   * @param subject the subject name {@link X500Name}.
   * @param sanUri the URI to request in the SAN.
   * @param sanDnsNames the DNS names to request in the SAN.
   * @param sanIpAddresses the IP addresses to request in the SAN.
   * @param signatureAlgorithm the signature algorithm to use when generating the signature to
   *     validate the certificate.
   * @return a {@link PKCS10CertificationRequest}.
   * @throws Exception if creating the signing request fails for any reason.
   */
  public static PKCS10CertificationRequest generateCsr(
      KeyPair keyPair,
      X500Name subject,
      String sanUri,
      List<String> sanDnsNames,
      List<String> sanIpAddresses,
      String signatureAlgorithm)
      throws Exception {

    PKCS10CertificationRequestBuilder builder =
        new PKCS10CertificationRequestBuilder(
            subject, SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded()));

    List<GeneralName> generalNames = new ArrayList<>();

    generalNames.add(new GeneralName(SUBJECT_ALT_NAME_URI, sanUri));

    sanDnsNames.stream()
        .map(n -> new GeneralName(SUBJECT_ALT_NAME_DNS_NAME, n))
        .forEach(generalNames::add);

    sanIpAddresses.stream()
        .map(n -> new GeneralName(SUBJECT_ALT_NAME_IP_ADDRESS, n))
        .forEach(generalNames::add);

    ExtensionsGenerator extGen = new ExtensionsGenerator();

    extGen.addExtension(
        Extension.subjectAlternativeName,
        false,
        new GeneralNames(generalNames.toArray(new GeneralName[0])));

    builder.addAttribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest, extGen.generate());

    JcaContentSignerBuilder signerBuilder = new JcaContentSignerBuilder(signatureAlgorithm);

    ContentSigner signer = signerBuilder.build(keyPair.getPrivate());

    return builder.build(signer);
  }

  /**
   * Get a PEM-encoded String containing a PKCS #10 certificate signing request.
   *
   * @param csr the {@link X509Certificate} to request signing for.
   * @return a PEM-encoded String containing a PKCS #10 CSR.
   * @throws IOException if creating the signing request fails for any reason.
   */
  public static String getCsrPem(PKCS10CertificationRequest csr) throws IOException {
    StringWriter stringWriter = new StringWriter();

    try (PemWriter pemWriter = new PemWriter(stringWriter)) {
      pemWriter.writeObject(new MiscPEMGenerator(csr));
      pemWriter.flush();
    }

    return stringWriter.toString();
  }

  private static List<GeneralName> getSubjectAltNames(X509Certificate certificate) {
    try {
      List<GeneralName> generalNames = new ArrayList<>();

      Collection<List<?>> subjectAltNames = certificate.getSubjectAlternativeNames();
      if (subjectAltNames == null) subjectAltNames = Collections.emptyList();

      for (List<?> idAndValue : subjectAltNames) {
        if (idAndValue != null && idAndValue.size() == 2) {
          Object id = idAndValue.get(0);
          String value = Objects.toString(idAndValue.get(1));

          if (Objects.equals(id, SUBJECT_ALT_NAME_DNS_NAME)) {
            generalNames.add(new GeneralName(SUBJECT_ALT_NAME_DNS_NAME, value));
          } else if (Objects.equals(id, SUBJECT_ALT_NAME_IP_ADDRESS)) {
            generalNames.add(new GeneralName(SUBJECT_ALT_NAME_IP_ADDRESS, value));
          } else if (Objects.equals(id, SUBJECT_ALT_NAME_URI)) {
            generalNames.add(new GeneralName(SUBJECT_ALT_NAME_URI, value));
          }
        }
      }

      return generalNames;
    } catch (CertificateParsingException e) {
      return Collections.emptyList();
    }
  }

  /**
   * Extract the value of a given SubjectAltName field from a {@link X509Certificate}.
   *
   * @param certificate the certificate.
   * @param field the field number.
   * @return an {@link Optional} containing the value in the field.
   * @see #SUBJECT_ALT_NAME_IP_ADDRESS
   * @see #SUBJECT_ALT_NAME_DNS_NAME
   * @see #SUBJECT_ALT_NAME_URI
   */
  public static List<Object> getSubjectAltNameField(X509Certificate certificate, int field) {
    try {
      List<Object> values = new ArrayList<>();

      Collection<List<?>> subjectAltNames = certificate.getSubjectAlternativeNames();
      if (subjectAltNames == null) subjectAltNames = Collections.emptyList();

      for (List<?> idAndValue : subjectAltNames) {
        if (idAndValue != null && idAndValue.size() == 2) {
          if (idAndValue.get(0).equals(field)) {
            Object value = idAndValue.get(1);
            if (value != null) values.add(value);
          }
        }
      }

      return values;
    } catch (CertificateParsingException e) {
      return Collections.emptyList();
    }
  }

  /**
   * Get the URI from the {@code certificate}'s Subject Alternative Name extension, if it's present.
   *
   * @param certificate the certificate to get the URI from.
   * @return the value of the SAN URI, if present.
   */
  public static Optional<String> getSanUri(X509Certificate certificate) {
    List<Object> values = getSubjectAltNameField(certificate, SUBJECT_ALT_NAME_URI);

    return values.stream().filter(v -> v instanceof String).map(String.class::cast).findFirst();
  }

  /**
   * Get the DNS names from the {@code certificate}'s Subject Alternative Name extension, if it's
   * present.
   *
   * @param certificate the certificate to get the DNS names from.
   * @return the values of the SAN DNS names, or empty list if none are present.
   */
  public static List<String> getSanDnsNames(X509Certificate certificate) {
    List<Object> values = getSubjectAltNameField(certificate, SUBJECT_ALT_NAME_DNS_NAME);

    return values.stream()
        .filter(v -> v instanceof String)
        .map(String.class::cast)
        .collect(Collectors.toList());
  }

  /**
   * Get the IP addresses from the {@code certificate}'s Subject Alternative Name extension, if it's
   * present.
   *
   * @param certificate the certificate to get the IP addresses from.
   * @return the values of the SAN IP addresses, or empty list if none are present.
   */
  public static List<String> getSanIpAddresses(X509Certificate certificate) {
    List<Object> values = getSubjectAltNameField(certificate, SUBJECT_ALT_NAME_IP_ADDRESS);

    return values.stream()
        .filter(v -> v instanceof String)
        .map(String.class::cast)
        .collect(Collectors.toList());
  }

  public static ByteString thumbprint(X509Certificate certificate) throws UaException {
    try {
      return ByteString.of(sha1(certificate.getEncoded()));
    } catch (CertificateEncodingException e) {
      throw new UaException(StatusCodes.Bad_CertificateInvalid, e);
    }
  }

  public static ByteString getCertificateChainBytes(List<X509Certificate> certificateChain)
      throws UaException {
    List<byte[]> certificates = new ArrayList<>(certificateChain.size());

    for (X509Certificate certificate : certificateChain) {
      try {
        certificates.add(certificate.getEncoded());
      } catch (CertificateEncodingException e) {
        throw new UaException(StatusCodes.Bad_CertificateInvalid, e);
      }
    }

    byte[] encoded = certificates.stream().reduce(new byte[0], Bytes::concat);

    return ByteString.of(encoded);
  }
}
