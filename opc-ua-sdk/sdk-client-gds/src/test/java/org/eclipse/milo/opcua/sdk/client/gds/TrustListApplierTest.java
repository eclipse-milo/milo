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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Set;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.TrustListManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TrustListMasks;
import org.eclipse.milo.opcua.stack.core.types.structured.TrustListDataType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class TrustListApplierTest {

  private static final X509Certificate OLD_TRUSTED = TestPki.certificate("old-trusted");
  private static final X509Certificate OLD_ISSUER = TestPki.certificate("old-issuer");
  private static final X509CRL OLD_TRUSTED_CRL = TestPki.crl();
  private static final X509CRL OLD_ISSUER_CRL = TestPki.crl();

  private static final X509Certificate NEW_TRUSTED = TestPki.certificate("new-trusted");
  private static final X509Certificate NEW_ISSUER = TestPki.certificate("new-issuer");
  private static final X509CRL NEW_TRUSTED_CRL = TestPki.crl();
  private static final X509CRL NEW_ISSUER_CRL = TestPki.crl();

  private MemoryTrustListManager manager;

  @BeforeEach
  void populateManagerWithOldLists() {
    manager = new MemoryTrustListManager();
    manager.setTrustedCertificates(List.of(OLD_TRUSTED));
    manager.setIssuerCertificates(List.of(OLD_ISSUER));
    manager.setTrustedCrls(List.of(OLD_TRUSTED_CRL));
    manager.setIssuerCrls(List.of(OLD_ISSUER_CRL));
  }

  /** A trust list carrying the "new" material in every field, specified by {@code masks}. */
  private static TrustListDataType newTrustList(int masks) {
    return new TrustListDataType(
        uint(masks),
        new ByteString[] {TestPki.der(NEW_TRUSTED)},
        new ByteString[] {TestPki.der(NEW_TRUSTED_CRL)},
        new ByteString[] {TestPki.der(NEW_ISSUER)},
        new ByteString[] {TestPki.der(NEW_ISSUER_CRL)});
  }

  @Nested
  class Apply {

    // Part 12 §7.8.2: SpecifiedLists tells the receiver which fields carry meaning. A list whose
    // bit is clear must be left alone even if the field happens to contain data.
    @ParameterizedTest
    @EnumSource(
        value = TrustListMasks.class,
        names = {"TrustedCertificates", "TrustedCrls", "IssuerCertificates", "IssuerCrls"})
    void applyReplacesOnlyTheSpecifiedList(TrustListMasks mask) throws Exception {
      TrustListApplier.apply(newTrustList(mask.getValue()), manager);

      boolean trustedCerts = mask == TrustListMasks.TrustedCertificates;
      boolean trustedCrls = mask == TrustListMasks.TrustedCrls;
      boolean issuerCerts = mask == TrustListMasks.IssuerCertificates;
      boolean issuerCrls = mask == TrustListMasks.IssuerCrls;

      assertEquals(
          List.of(trustedCerts ? NEW_TRUSTED : OLD_TRUSTED), manager.getTrustedCertificates());
      assertEquals(
          List.of(trustedCrls ? NEW_TRUSTED_CRL : OLD_TRUSTED_CRL), manager.getTrustedCrls());
      assertEquals(List.of(issuerCerts ? NEW_ISSUER : OLD_ISSUER), manager.getIssuerCertificates());
      assertEquals(List.of(issuerCrls ? NEW_ISSUER_CRL : OLD_ISSUER_CRL), manager.getIssuerCrls());
    }

    @Test
    void applyWithAllMasksReplacesEveryList() throws Exception {
      TrustListApplier.apply(newTrustList(TrustListMasks.All.getValue()), manager);

      assertEquals(List.of(NEW_TRUSTED), manager.getTrustedCertificates());
      assertEquals(List.of(NEW_TRUSTED_CRL), manager.getTrustedCrls());
      assertEquals(List.of(NEW_ISSUER), manager.getIssuerCertificates());
      assertEquals(List.of(NEW_ISSUER_CRL), manager.getIssuerCrls());
    }

    // The GDS delivers the complete authoritative list; a certificate it dropped must disappear
    // locally, which is why replacement rather than merging is required.
    @Test
    void applyWithSpecifiedButEmptyListClearsIt() throws Exception {
      var trustList =
          new TrustListDataType(
              uint(TrustListMasks.TrustedCertificates.getValue()),
              new ByteString[0],
              null,
              null,
              null);

      TrustListApplier.apply(trustList, manager);

      assertEquals(List.of(), manager.getTrustedCertificates());
      assertEquals(List.of(OLD_ISSUER), manager.getIssuerCertificates(), "unspecified, untouched");
    }

    // A half-applied update would leave the manager trusting a new certificate set without the
    // CRLs that revoke members of it; decoding must complete before anything is replaced.
    @Test
    void malformedCrlRejectsTheWholeUpdateAndLeavesManagerUnchanged() {
      var trustList =
          new TrustListDataType(
              uint(TrustListMasks.All.getValue()),
              new ByteString[] {TestPki.der(NEW_TRUSTED)},
              new ByteString[] {ByteString.of(new byte[] {0x30, 0x03, 0x02, 0x01})},
              new ByteString[] {TestPki.der(NEW_ISSUER)},
              new ByteString[] {TestPki.der(NEW_ISSUER_CRL)});

      UaException e =
          assertThrows(UaException.class, () -> TrustListApplier.apply(trustList, manager));

      assertEquals(StatusCodes.Bad_DecodingError, e.getStatusCode().value());
      assertEquals(List.of(OLD_TRUSTED), manager.getTrustedCertificates());
      assertEquals(List.of(OLD_ISSUER), manager.getIssuerCertificates());
      assertEquals(List.of(OLD_ISSUER_CRL), manager.getIssuerCrls());
    }

    @Test
    void malformedCertificateRejectsTheWholeUpdateWithBadCertificateInvalid() {
      var trustList =
          new TrustListDataType(
              uint(TrustListMasks.All.getValue()),
              new ByteString[] {ByteString.of(new byte[] {1, 2, 3})},
              new ByteString[] {TestPki.der(NEW_TRUSTED_CRL)},
              new ByteString[] {TestPki.der(NEW_ISSUER)},
              new ByteString[] {TestPki.der(NEW_ISSUER_CRL)});

      UaException e =
          assertThrows(UaException.class, () -> TrustListApplier.apply(trustList, manager));

      assertEquals(StatusCodes.Bad_CertificateInvalid, e.getStatusCode().value());
      assertEquals(List.of(OLD_TRUSTED_CRL), manager.getTrustedCrls());
    }
  }

  @Nested
  class ToTrustListDataType {

    // What a Push server hands out must be exactly what a Pull client would install.
    @Test
    void roundTripsThroughApply() throws Exception {
      TrustListDataType trustList =
          TrustListApplier.toTrustListDataType(manager, TrustListMasks.All.getValue());

      TrustListManager copy = new MemoryTrustListManager();
      TrustListApplier.apply(trustList, copy);

      assertEquals(uint(TrustListMasks.All.getValue()), trustList.getSpecifiedLists());
      assertEquals(
          Set.copyOf(manager.getTrustedCertificates()), Set.copyOf(copy.getTrustedCertificates()));
      assertEquals(
          Set.copyOf(manager.getIssuerCertificates()), Set.copyOf(copy.getIssuerCertificates()));
      assertEquals(Set.copyOf(manager.getTrustedCrls()), Set.copyOf(copy.getTrustedCrls()));
      assertEquals(Set.copyOf(manager.getIssuerCrls()), Set.copyOf(copy.getIssuerCrls()));
    }

    @Test
    void leavesUnselectedListsNull() throws Exception {
      int masks =
          TrustListMasks.TrustedCertificates.getValue() | TrustListMasks.IssuerCrls.getValue();

      TrustListDataType trustList = TrustListApplier.toTrustListDataType(manager, masks);

      assertEquals(uint(masks), trustList.getSpecifiedLists());
      assertNotNull(trustList.getTrustedCertificates());
      assertNotNull(trustList.getIssuerCrls());
      assertEquals(1, trustList.getTrustedCertificates().length);
      assertEquals(1, trustList.getIssuerCrls().length);
      assertNull(trustList.getTrustedCrls());
      assertNull(trustList.getIssuerCertificates());
    }
  }
}
