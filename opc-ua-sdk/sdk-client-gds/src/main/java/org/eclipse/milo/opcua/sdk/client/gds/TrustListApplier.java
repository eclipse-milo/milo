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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.security.GeneralSecurityException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.TrustListManager;
import org.eclipse.milo.opcua.stack.core.security.TrustListSnapshot;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TrustListMasks;
import org.eclipse.milo.opcua.stack.core.types.structured.TrustListDataType;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.jspecify.annotations.Nullable;

/**
 * Converts between {@link TrustListDataType} and {@link TrustListManager}.
 *
 * <p>{@link #apply} installs a list pulled from a GDS: each list whose bit is set in {@code
 * SpecifiedLists} replaces the manager's corresponding list in full, and lists whose bit is clear
 * are left untouched, which is what Part 12 §7.8.2 defines the masks to mean. {@link
 * #toTrustListDataType} is the inverse, for a server exposing its trust list through the Push Model
 * or for tests.
 */
public final class TrustListApplier {

  private TrustListApplier() {}

  /**
   * Replace the lists of {@code manager} named by {@code trustList}'s {@code SpecifiedLists} with
   * the certificates and CRLs it carries.
   *
   * <p>All entries are decoded before anything is replaced, so a malformed entry rejects the whole
   * update and the manager is unchanged. Specified lists are merged with one current snapshot and
   * the resulting snapshot is committed in one replacement.
   *
   * @param trustList the list to apply.
   * @param manager the {@link TrustListManager} to update.
   * @throws UaException if an entry cannot be decoded; {@link StatusCodes#Bad_CertificateInvalid}
   *     for a certificate, {@link StatusCodes#Bad_DecodingError} for a CRL.
   */
  public static void apply(TrustListDataType trustList, TrustListManager manager)
      throws UaException {

    int masks = trustList.getSpecifiedLists().intValue();

    List<X509Certificate> issuerCertificates =
        isSet(masks, TrustListMasks.IssuerCertificates)
            ? decodeCertificates("IssuerCertificates", trustList.getIssuerCertificates())
            : null;
    List<X509Certificate> trustedCertificates =
        isSet(masks, TrustListMasks.TrustedCertificates)
            ? decodeCertificates("TrustedCertificates", trustList.getTrustedCertificates())
            : null;
    List<X509CRL> issuerCrls =
        isSet(masks, TrustListMasks.IssuerCrls)
            ? decodeCrls("IssuerCrls", trustList.getIssuerCrls())
            : null;
    List<X509CRL> trustedCrls =
        isSet(masks, TrustListMasks.TrustedCrls)
            ? decodeCrls("TrustedCrls", trustList.getTrustedCrls())
            : null;

    if (issuerCertificates == null
        && trustedCertificates == null
        && issuerCrls == null
        && trustedCrls == null) {
      return;
    }

    TrustListSnapshot current = manager.getSnapshot();

    manager.replaceAll(
        new TrustListSnapshot(
            issuerCertificates != null ? issuerCertificates : current.issuerCertificates(),
            issuerCrls != null ? issuerCrls : current.issuerCrls(),
            trustedCertificates != null ? trustedCertificates : current.trustedCertificates(),
            trustedCrls != null ? trustedCrls : current.trustedCrls(),
            DateTime.now()));
  }

  /**
   * Build a {@link TrustListDataType} from the lists of {@code manager} selected by {@code masks}.
   *
   * @param manager the {@link TrustListManager} to read.
   * @param masks a bit set of {@link TrustListMasks} values selecting the lists to include; lists
   *     not selected are left null.
   * @return the encoded lists with {@code SpecifiedLists} set to {@code masks}.
   * @throws UaException {@link StatusCodes#Bad_EncodingError} if a certificate or CRL cannot be
   *     DER-encoded.
   */
  public static TrustListDataType toTrustListDataType(TrustListManager manager, int masks)
      throws UaException {

    TrustListSnapshot snapshot = manager.getSnapshot();

    return new TrustListDataType(
        uint(masks),
        isSet(masks, TrustListMasks.TrustedCertificates)
            ? encodeCertificates(snapshot.trustedCertificates())
            : null,
        isSet(masks, TrustListMasks.TrustedCrls) ? encodeCrls(snapshot.trustedCrls()) : null,
        isSet(masks, TrustListMasks.IssuerCertificates)
            ? encodeCertificates(snapshot.issuerCertificates())
            : null,
        isSet(masks, TrustListMasks.IssuerCrls) ? encodeCrls(snapshot.issuerCrls()) : null);
  }

  private static boolean isSet(int masks, TrustListMasks mask) {
    return (masks & mask.getValue()) != 0;
  }

  private static List<X509Certificate> decodeCertificates(
      String listName, ByteString @Nullable [] entries) throws UaException {
    return decodeAll(listName, entries, CertificateUtil::decodeCertificate);
  }

  private static List<X509CRL> decodeCrls(String listName, ByteString @Nullable [] entries)
      throws UaException {
    return decodeAll(listName, entries, CertificateUtil::decodeCrl);
  }

  private static <T> List<T> decodeAll(
      String listName, ByteString @Nullable [] entries, Decoder<T> decoder) throws UaException {

    var decoded = new ArrayList<T>();

    if (entries != null) {
      for (int i = 0; i < entries.length; i++) {
        try {
          decoded.add(decoder.decode(entries[i].bytesOrEmpty()));
        } catch (UaException e) {
          throw new UaException(
              e.getStatusCode().value(), listName + "[" + i + "]: " + e.getMessage(), e);
        }
      }
    }

    return decoded;
  }

  private static ByteString[] encodeCertificates(List<X509Certificate> certificates)
      throws UaException {
    return encodeAll(certificates, X509Certificate::getEncoded);
  }

  private static ByteString[] encodeCrls(List<X509CRL> crls) throws UaException {
    return encodeAll(crls, X509CRL::getEncoded);
  }

  private static <T> ByteString[] encodeAll(List<T> items, Encoder<T> encoder) throws UaException {

    var encoded = new ByteString[items.size()];

    for (int i = 0; i < encoded.length; i++) {
      try {
        encoded[i] = ByteString.of(encoder.encode(items.get(i)));
      } catch (GeneralSecurityException e) {
        throw new UaException(StatusCodes.Bad_EncodingError, e);
      }
    }

    return encoded;
  }

  @FunctionalInterface
  private interface Decoder<T> {
    T decode(byte[] der) throws UaException;
  }

  @FunctionalInterface
  private interface Encoder<T> {
    byte[] encode(T item) throws GeneralSecurityException;
  }
}
