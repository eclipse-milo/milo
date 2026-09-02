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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class TrustListApplierTest {

  private static X509Certificate oldTrusted;
  private static X509Certificate oldIssuer;
  private static X509CRL oldTrustedCrl;
  private static X509CRL oldIssuerCrl;

  private static X509Certificate newTrusted;
  private static X509Certificate newIssuer;
  private static X509CRL newTrustedCrl;
  private static X509CRL newIssuerCrl;

  private MemoryTrustListManager manager;

  @BeforeAll
  static void generateMaterial() throws Exception {
    oldTrusted = TestPki.certificate("old-trusted");
    oldIssuer = TestPki.certificate("old-issuer");
    oldTrustedCrl = TestPki.crl();
    oldIssuerCrl = TestPki.crl();

    newTrusted = TestPki.certificate("new-trusted");
    newIssuer = TestPki.certificate("new-issuer");
    newTrustedCrl = TestPki.crl();
    newIssuerCrl = TestPki.crl();
  }

  @BeforeEach
  void populateManagerWithOldLists() {
    manager = new MemoryTrustListManager();
    manager.setTrustedCertificates(List.of(oldTrusted));
    manager.setIssuerCertificates(List.of(oldIssuer));
    manager.setTrustedCrls(List.of(oldTrustedCrl));
    manager.setIssuerCrls(List.of(oldIssuerCrl));
  }

  /** A trust list carrying the "new" material in every field, specified by {@code masks}. */
  private static TrustListDataType newTrustList(int masks) throws Exception {
    return new TrustListDataType(
        uint(masks),
        new ByteString[] {TestPki.der(newTrusted)},
        new ByteString[] {TestPki.der(newTrustedCrl)},
        new ByteString[] {TestPki.der(newIssuer)},
        new ByteString[] {TestPki.der(newIssuerCrl)});
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
          List.of(trustedCerts ? newTrusted : oldTrusted), manager.getTrustedCertificates());
      assertEquals(List.of(trustedCrls ? newTrustedCrl : oldTrustedCrl), manager.getTrustedCrls());
      assertEquals(List.of(issuerCerts ? newIssuer : oldIssuer), manager.getIssuerCertificates());
      assertEquals(List.of(issuerCrls ? newIssuerCrl : oldIssuerCrl), manager.getIssuerCrls());
    }

    @Test
    void applyWithAllMasksReplacesEveryList() throws Exception {
      TrustListApplier.apply(newTrustList(TrustListMasks.All.getValue()), manager);

      assertEquals(List.of(newTrusted), manager.getTrustedCertificates());
      assertEquals(List.of(newTrustedCrl), manager.getTrustedCrls());
      assertEquals(List.of(newIssuer), manager.getIssuerCertificates());
      assertEquals(List.of(newIssuerCrl), manager.getIssuerCrls());
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
      assertEquals(List.of(oldIssuer), manager.getIssuerCertificates(), "unspecified, untouched");
    }

    // A half-applied update would leave the manager trusting a new certificate set without the
    // CRLs that revoke members of it; decoding must complete before anything is replaced.
    @Test
    void malformedCrlRejectsTheWholeUpdateAndLeavesManagerUnchanged() throws Exception {
      var trustList =
          new TrustListDataType(
              uint(TrustListMasks.All.getValue()),
              new ByteString[] {TestPki.der(newTrusted)},
              new ByteString[] {ByteString.of(new byte[] {0x30, 0x03, 0x02, 0x01})},
              new ByteString[] {TestPki.der(newIssuer)},
              new ByteString[] {TestPki.der(newIssuerCrl)});

      UaException e =
          assertThrows(UaException.class, () -> TrustListApplier.apply(trustList, manager));

      assertEquals(StatusCodes.Bad_DecodingError, e.getStatusCode().value());
      assertEquals(List.of(oldTrusted), manager.getTrustedCertificates());
      assertEquals(List.of(oldIssuer), manager.getIssuerCertificates());
      assertEquals(List.of(oldIssuerCrl), manager.getIssuerCrls());
    }

    @Test
    void malformedCertificateRejectsTheWholeUpdateWithBadCertificateInvalid() throws Exception {
      var trustList =
          new TrustListDataType(
              uint(TrustListMasks.All.getValue()),
              new ByteString[] {ByteString.of(new byte[] {1, 2, 3})},
              new ByteString[] {TestPki.der(newTrustedCrl)},
              new ByteString[] {TestPki.der(newIssuer)},
              new ByteString[] {TestPki.der(newIssuerCrl)});

      UaException e =
          assertThrows(UaException.class, () -> TrustListApplier.apply(trustList, manager));

      assertEquals(StatusCodes.Bad_CertificateInvalid, e.getStatusCode().value());
      assertEquals(List.of(oldTrustedCrl), manager.getTrustedCrls());
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
      assertEquals(1, trustList.getTrustedCertificates().length);
      assertEquals(1, trustList.getIssuerCrls().length);
      assertNull(trustList.getTrustedCrls());
      assertNull(trustList.getIssuerCertificates());
    }
  }
}
