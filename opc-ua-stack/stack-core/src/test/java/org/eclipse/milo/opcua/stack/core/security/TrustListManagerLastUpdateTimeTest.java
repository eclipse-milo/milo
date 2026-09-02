/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.CrlTestUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * {@link TrustListManager#getLastUpdateTime()} is what a GDS Pull client compares against the
 * server's {@code TrustListType.LastUpdateTime} (Part 12 §7.6) and what a Push server exposes as
 * that property (Part 12 §7.8.2.1). Both roles need it to move on every change to the list, not
 * only at initialization.
 */
public class TrustListManagerLastUpdateTimeTest {

  private static final X509Certificate CERTIFICATE;
  private static final X509CRL CRL;

  static {
    try {
      KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
      CERTIFICATE =
          new SelfSignedCertificateBuilder(keyPair)
              .setApplicationUri("urn:eclipse:milo:test")
              .build();
      CRL = CrlTestUtil.generateCrl(CERTIFICATE, keyPair.getPrivate());
    } catch (Exception e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  static Stream<Arguments> mutations() {
    return Stream.of(
        mutation("setTrustedCertificates", m -> m.setTrustedCertificates(List.of(CERTIFICATE))),
        mutation("setIssuerCertificates", m -> m.setIssuerCertificates(List.of(CERTIFICATE))),
        mutation("setTrustedCrls", m -> m.setTrustedCrls(List.of(CRL))),
        mutation("setIssuerCrls", m -> m.setIssuerCrls(List.of(CRL))),
        mutation("addTrustedCertificate", m -> m.addTrustedCertificate(CERTIFICATE)),
        mutation("addIssuerCertificate", m -> m.addIssuerCertificate(CERTIFICATE)),
        mutation(
            "removeTrustedCertificate",
            m -> {
              m.addTrustedCertificate(CERTIFICATE);
              m.removeTrustedCertificate(thumbprint());
            }),
        mutation(
            "removeIssuerCertificate",
            m -> {
              m.addIssuerCertificate(CERTIFICATE);
              m.removeIssuerCertificate(thumbprint());
            }));
  }

  private static Arguments mutation(String name, Consumer<TrustListManager> mutation) {
    return Arguments.of(name, mutation);
  }

  private static ByteString thumbprint() {
    try {
      return CertificateUtil.thumbprint(CERTIFICATE);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Wait until the clock has moved past {@code time}, so a following mutation cannot land on the
   * same {@link DateTime#now()} tick and a strict "after" comparison is meaningful.
   */
  private static void awaitClockTickAfter(DateTime time) {
    while (DateTime.now().getUtcTime() <= time.getUtcTime()) {
      Thread.onSpinWait();
    }
  }

  abstract static class Contract {

    TrustListManager manager;

    abstract TrustListManager createManager() throws Exception;

    @BeforeEach
    void setUp() throws Exception {
      manager = createManager();
    }

    @AfterEach
    void tearDown() throws Exception {
      manager.close();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource(
        "org.eclipse.milo.opcua.stack.core.security.TrustListManagerLastUpdateTimeTest#mutations")
    void mutationAdvancesLastUpdateTime(String name, Consumer<TrustListManager> mutation) {
      DateTime before = manager.getLastUpdateTime();
      awaitClockTickAfter(before);

      mutation.accept(manager);

      DateTime after = manager.getLastUpdateTime();
      assertTrue(
          after.getUtcTime() > before.getUtcTime(),
          name + " must advance lastUpdateTime: before=" + before + ", after=" + after);
    }

    // Removing a certificate that is not in the list changes nothing, so the timestamp must not
    // claim an update happened.
    @Test
    void removeOfUnknownThumbprintDoesNotAdvanceLastUpdateTime() {
      DateTime before = manager.getLastUpdateTime();
      awaitClockTickAfter(before);

      boolean removed = manager.removeTrustedCertificate(ByteString.of(new byte[] {1, 2, 3}));

      assertFalse(removed);
      assertEquals(before, manager.getLastUpdateTime());
    }
  }

  @Nested
  class Memory extends Contract {
    @Override
    TrustListManager createManager() {
      return new MemoryTrustListManager();
    }
  }

  @Nested
  class FileBased extends Contract {
    @TempDir Path tempDir;

    @Override
    TrustListManager createManager() throws Exception {
      return FileBasedTrustListManager.createAndInitialize(
          Files.createTempDirectory(tempDir, "pki"));
    }
  }
}
