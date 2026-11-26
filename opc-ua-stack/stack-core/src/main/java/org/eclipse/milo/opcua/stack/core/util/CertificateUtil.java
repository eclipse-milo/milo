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
import java.net.InetAddress;
import java.security.KeyPair;
import java.security.Provider;
import java.security.cert.CRL;
import java.security.cert.CRLException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1IA5String;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x500.style.RFC4519Style;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.ExtensionsGenerator;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
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
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CertificateUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(CertificateUtil.class);

  public static final int SUBJECT_ALT_NAME_DNS_NAME = GeneralName.dNSName;
  public static final int SUBJECT_ALT_NAME_IP_ADDRESS = GeneralName.iPAddress;
  public static final int SUBJECT_ALT_NAME_URI = GeneralName.uniformResourceIdentifier;

  /**
   * Decode a DER-encoded X.509 certificate.
   *
   * @param certificateBytes the DER-encoded certificate bytes.
   * @return an {@link X509Certificate}.
   * @throws UaException if decoding the certificate fails.
   */
  public static X509Certificate decodeCertificate(byte[] certificateBytes) throws UaException {
    Preconditions.checkNotNull(certificateBytes, "certificateBytes cannot be null");

    return decodeCertificate(new ByteArrayInputStream(certificateBytes));
  }

  /**
   * Decode a DER-encoded X.509 certificate.
   *
   * @param inputStream the {@link InputStream} containing DER-encoded certificate bytes.
   * @return an {@link X509Certificate}.
   * @throws UaException if decoding the certificate fails.
   */
  public static X509Certificate decodeCertificate(InputStream inputStream) throws UaException {
    return decodeCertificates(inputStream).get(0);
  }

  /**
   * Decode either a sequence of DER-encoded X.509 certificates or a PKCS#7 certificate chain.
   *
   * @param certificateBytes the byte[] to decode from.
   * @return a {@link List} of certificates decoded from {@code certificateBytes}.
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
   * Decode a DER-encoded X.509 CRL.
   *
   * @param crlBytes the DER-encoded CRL bytes.
   * @return an {@link X509CRL}.
   * @throws UaException if decoding the CRL fails or the bytes contain no CRL.
   */
  public static X509CRL decodeCrl(byte[] crlBytes) throws UaException {
    Preconditions.checkNotNull(crlBytes, "crlBytes cannot be null");

    return decodeCrl(new ByteArrayInputStream(crlBytes));
  }

  /**
   * Decode a DER-encoded X.509 CRL.
   *
   * @param inputStream the {@link InputStream} containing DER-encoded CRL bytes.
   * @return an {@link X509CRL}.
   * @throws UaException if decoding the CRL fails or the stream contains no CRL.
   */
  public static X509CRL decodeCrl(InputStream inputStream) throws UaException {
    List<X509CRL> crls = decodeCrls(inputStream);

    if (crls.isEmpty()) {
      throw new UaException(StatusCodes.Bad_DecodingError, "no CRL found");
    }

    return crls.get(0);
  }

  /**
   * Decode either a sequence of DER-encoded X.509 CRLs or a PKCS#7 CRL set.
   *
   * @param crlBytes the byte[] to decode from.
   * @return a {@link List} of CRLs decoded from {@code crlBytes}.
   * @throws UaException if decoding fails.
   */
  public static List<X509CRL> decodeCrls(byte[] crlBytes) throws UaException {
    Preconditions.checkNotNull(crlBytes, "crlBytes cannot be null");

    return decodeCrls(new ByteArrayInputStream(crlBytes));
  }

  /**
   * Decode either a sequence of DER-encoded X.509 CRLs or a PKCS#7 CRL set.
   *
   * @param inputStream the {@link InputStream} to decode from.
   * @return a {@link List} of CRLs decoded from {@code inputStream}.
   * @throws UaException if decoding fails.
   */
  public static List<X509CRL> decodeCrls(InputStream inputStream) throws UaException {
    Preconditions.checkNotNull(inputStream, "inputStream cannot be null");

    CertificateFactory factory;

    try {
      factory = CertificateFactory.getInstance("X.509");
    } catch (CertificateException e) {
      throw new UaException(StatusCodes.Bad_InternalError, e);
    }

    try {
      Collection<? extends CRL> crls = factory.generateCRLs(inputStream);

      return crls.stream()
          .filter(X509CRL.class::isInstance)
          .map(X509CRL.class::cast)
          .collect(Collectors.toList());
    } catch (CRLException e) {
      throw new UaException(StatusCodes.Bad_DecodingError, e);
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

    List<GeneralName> subjectAltNames = getSubjectAltNames(certificate);

    ExtensionsGenerator extGen = new ExtensionsGenerator();
    if (!subjectAltNames.isEmpty()) {
      extGen.addExtension(
          Extension.subjectAlternativeName,
          false,
          new GeneralNames(subjectAltNames.toArray(new GeneralName[0])));
    }

    KeyUsage keyUsage =
        new KeyUsage(
            KeyUsage.digitalSignature
                | KeyUsage.nonRepudiation
                | KeyUsage.keyEncipherment
                | KeyUsage.dataEncipherment);
    extGen.addExtension(Extension.keyUsage, true, keyUsage);

    ExtendedKeyUsage extendedKeyUsage =
        new ExtendedKeyUsage(
            new KeyPurposeId[] {KeyPurposeId.id_kp_clientAuth, KeyPurposeId.id_kp_serverAuth});
    extGen.addExtension(Extension.extendedKeyUsage, false, extendedKeyUsage);

    BasicConstraints basicConstraints = new BasicConstraints(false);
    extGen.addExtension(Extension.basicConstraints, true, basicConstraints);

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
   * @param subjectName the subject name, in RFC 4519 style, for example {@code CN=foo,O=bar}.
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

    return generateCsr(
        keyPair,
        subject,
        sanUri,
        sanDnsNames,
        sanIpAddresses,
        signatureAlgorithm,
        KeyUsage.digitalSignature
            | KeyUsage.nonRepudiation
            | KeyUsage.keyEncipherment
            | KeyUsage.dataEncipherment,
        List.of(KeyPurposeId.id_kp_clientAuth, KeyPurposeId.id_kp_serverAuth));
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
   * @param keyUsageFlags the requested KeyUsage flags.
   * @param extendedKeyUsageIds the requested ExtendedKeyUsage IDs, or {@code null} to omit the
   *     extension.
   * @return a {@link PKCS10CertificationRequest}.
   * @throws Exception if creating the signing request fails for any reason.
   */
  public static PKCS10CertificationRequest generateCsr(
      KeyPair keyPair,
      X500Name subject,
      String sanUri,
      List<String> sanDnsNames,
      List<String> sanIpAddresses,
      String signatureAlgorithm,
      int keyUsageFlags,
      @Nullable List<KeyPurposeId> extendedKeyUsageIds)
      throws Exception {

    return generateCsr(
        keyPair,
        subject,
        sanUri,
        sanDnsNames,
        sanIpAddresses,
        signatureAlgorithm,
        keyUsageFlags,
        extendedKeyUsageIds,
        null);
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
   * @param keyUsageFlags the requested KeyUsage flags.
   * @param extendedKeyUsageIds the requested ExtendedKeyUsage IDs, or {@code null} to omit the
   *     extension.
   * @param provider the JCA provider to use for signing, or {@code null} for normal provider
   *     lookup. Use this when the private key or signature algorithm requires a provider that the
   *     JVM would not reliably choose by default.
   * @return a {@link PKCS10CertificationRequest}.
   * @throws Exception if creating the signing request fails for any reason.
   */
  public static PKCS10CertificationRequest generateCsr(
      KeyPair keyPair,
      X500Name subject,
      String sanUri,
      List<String> sanDnsNames,
      List<String> sanIpAddresses,
      String signatureAlgorithm,
      int keyUsageFlags,
      @Nullable List<KeyPurposeId> extendedKeyUsageIds,
      @Nullable Provider provider)
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

    KeyUsage keyUsage = new KeyUsage(keyUsageFlags);
    extGen.addExtension(Extension.keyUsage, true, keyUsage);

    if (extendedKeyUsageIds != null && !extendedKeyUsageIds.isEmpty()) {
      ExtendedKeyUsage extendedKeyUsage =
          new ExtendedKeyUsage(extendedKeyUsageIds.toArray(new KeyPurposeId[0]));
      extGen.addExtension(Extension.extendedKeyUsage, false, extendedKeyUsage);
    }

    BasicConstraints basicConstraints = new BasicConstraints(false);
    extGen.addExtension(Extension.basicConstraints, true, basicConstraints);

    builder.addAttribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest, extGen.generate());

    JcaContentSignerBuilder signerBuilder = new JcaContentSignerBuilder(signatureAlgorithm);
    if (provider != null) {
      signerBuilder.setProvider(provider);
    }

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

  private record SubjectAltNameEntry(GeneralName generalName, Object value) {}

  private static @Nullable ASN1Sequence readSubjectAltNameSequence(X509Certificate certificate) {
    try {
      byte[] encoded = certificate.getEncoded();
      if (encoded == null) {
        return null;
      }

      var certificateHolder = new X509CertificateHolder(encoded);
      Extensions extensions = certificateHolder.getExtensions();
      if (extensions == null) {
        return null;
      }

      Extension sanExtension = extensions.getExtension(Extension.subjectAlternativeName);
      if (sanExtension == null) {
        return null;
      }

      ASN1OctetString extnValue = sanExtension.getExtnValue();
      if (extnValue == null) {
        return null;
      }

      ASN1Primitive parsed = ASN1Primitive.fromByteArray(extnValue.getOctets());
      return ASN1Sequence.getInstance(parsed);
    } catch (CertificateEncodingException
        | IOException
        | IllegalArgumentException
        | IllegalStateException e) {
      LOGGER.debug("Failed to parse SubjectAlternativeName extension", e);
      return null;
    }
  }

  private static List<SubjectAltNameEntry> readSubjectAltNameEntries(X509Certificate certificate) {
    ASN1Sequence sequence = readSubjectAltNameSequence(certificate);
    if (sequence == null) {
      return Collections.emptyList();
    }

    List<SubjectAltNameEntry> entries = new ArrayList<>(sequence.size());

    for (ASN1Encodable element : sequence) {
      try {
        GeneralName generalName = GeneralName.getInstance(element);
        Object value = extractGeneralNameValue(generalName);
        if (value != null) {
          entries.add(new SubjectAltNameEntry(generalName, value));
        }
      } catch (IllegalArgumentException | IllegalStateException e) {
        LOGGER.debug("Skipping malformed SubjectAlternativeName entry", e);
      }
    }

    return entries;
  }

  private static @Nullable Object extractGeneralNameValue(GeneralName generalName) {
    ASN1Encodable name = generalName.getName();

    try {
      return switch (generalName.getTagNo()) {
        case GeneralName.rfc822Name, GeneralName.dNSName -> {
          String value = ASN1IA5String.getInstance(name).getString();
          yield isIa5(value) ? value : null;
        }
        case GeneralName.uniformResourceIdentifier -> {
          String value = ASN1IA5String.getInstance(name).getString();
          yield (!value.isBlank() && isIa5(value)) ? value : null;
        }
        case GeneralName.directoryName ->
            new X500Principal(X500Name.getInstance(name).getEncoded(ASN1Encoding.DER))
                .getName(X500Principal.RFC2253);
        case GeneralName.iPAddress -> {
          byte[] octets = ASN1OctetString.getInstance(name).getOctets();
          if (octets.length != 4 && octets.length != 16) {
            yield null;
          }
          yield InetAddress.getByAddress(octets).getHostAddress();
        }
        case GeneralName.registeredID -> ASN1ObjectIdentifier.getInstance(name).getId();
        case GeneralName.otherName, GeneralName.x400Address, GeneralName.ediPartyName ->
            name.toASN1Primitive().getEncoded(ASN1Encoding.DER);
        default -> null;
      };
    } catch (IOException | IllegalArgumentException | IllegalStateException e) {
      LOGGER.debug(
          "Failed to extract SubjectAlternativeName entry with tag {}", generalName.getTagNo(), e);
      return null;
    }
  }

  private static boolean isIa5(String value) {
    for (int i = 0; i < value.length(); i++) {
      if (value.charAt(i) > 0x7F) {
        return false;
      }
    }

    return true;
  }

  private static List<GeneralName> getSubjectAltNames(X509Certificate certificate) {
    List<GeneralName> generalNames = new ArrayList<>();

    for (SubjectAltNameEntry entry : readSubjectAltNameEntries(certificate)) {
      int tagNo = entry.generalName().getTagNo();
      if (tagNo == SUBJECT_ALT_NAME_DNS_NAME
          || tagNo == SUBJECT_ALT_NAME_IP_ADDRESS
          || tagNo == SUBJECT_ALT_NAME_URI) {
        generalNames.add(entry.generalName());
      }
    }

    return generalNames;
  }

  /**
   * Extract the values of a given SubjectAltName field from an {@link X509Certificate}.
   *
   * @param certificate the certificate.
   * @param field the field number.
   * @return a list of values for the given field, or an empty list if none are present.
   * @see #SUBJECT_ALT_NAME_IP_ADDRESS
   * @see #SUBJECT_ALT_NAME_DNS_NAME
   * @see #SUBJECT_ALT_NAME_URI
   */
  public static List<Object> getSubjectAltNameField(X509Certificate certificate, int field) {
    List<Object> values = new ArrayList<>();

    for (SubjectAltNameEntry entry : readSubjectAltNameEntries(certificate)) {
      if (entry.generalName().getTagNo() == field) {
        values.add(entry.value());
      }
    }

    return values;
  }

  /**
   * Count the raw SubjectAltName entries with the given {@link GeneralName} tag number, including
   * entries whose values fail decoding or validation.
   *
   * @param certificate the certificate.
   * @param field the {@link GeneralName} tag number.
   * @return the number of raw SubjectAltName entries with tag {@code field}.
   */
  public static int getSubjectAltNameEntryCount(X509Certificate certificate, int field) {
    ASN1Sequence sequence = readSubjectAltNameSequence(certificate);
    if (sequence == null) {
      return 0;
    }

    int count = 0;

    for (ASN1Encodable element : sequence) {
      if (element instanceof ASN1TaggedObject taggedObject && taggedObject.hasContextTag(field)) {
        count++;
      }
    }

    return count;
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
