/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.security;

import static org.eclipse.milo.opcua.stack.core.util.DigestUtil.sha1;

import io.netty.buffer.ByteBufUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileBasedCertificateQuarantine implements CertificateQuarantine {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(FileBasedCertificateQuarantine.class);

  /**
   * The maximum number of certificates that can accumulate in the rejected directory before the
   * oldest certificates are deleted to make room for newer ones.
   */
  static final int DEFAULT_MAX_REJECTED_CERTIFICATES = 128;

  private final File rejectedDir;
  private final int maxRejectedCertificates;

  public FileBasedCertificateQuarantine(File rejectedDir) {
    this(rejectedDir, DEFAULT_MAX_REJECTED_CERTIFICATES);
  }

  public FileBasedCertificateQuarantine(File rejectedDir, int maxRejectedCertificates) {
    this.rejectedDir = rejectedDir;
    this.maxRejectedCertificates = maxRejectedCertificates;

    assert rejectedDir.exists();
  }

  @Override
  public List<X509Certificate> getRejectedCertificates() {
    File[] files = rejectedDir.listFiles();
    if (files == null) files = new File[0];

    return List.copyOf(
        Arrays.stream(files)
            .flatMap(cert -> decodeCertificateFile(cert).stream())
            .collect(Collectors.toList()));
  }

  @Override
  public void addRejectedCertificate(X509Certificate certificate) {
    pruneOldCertificates(rejectedDir, maxRejectedCertificates);

    writeCertificateFile(rejectedDir, certificate);
  }

  @Override
  public void removeRejectedCertificate(X509Certificate certificate) {
    try {
      ByteString thumbprint = CertificateUtil.thumbprint(certificate);

      deleteCertificateFile(rejectedDir, thumbprint);
    } catch (Exception ignored) {
    }
  }

  private static Optional<X509Certificate> decodeCertificateFile(File f) {
    try {
      try (FileInputStream inputStream = new FileInputStream(f)) {
        return Optional.of(CertificateUtil.decodeCertificate(inputStream));
      }
    } catch (Throwable t) {
      LOGGER.warn("Error decoding certificate file: {}", f.toString(), t);

      return Optional.empty();
    }
  }

  private static void writeCertificateFile(File dir, X509Certificate certificate) {
    try {
      String thumbprint = ByteBufUtil.hexDump(sha1(certificate.getSignature()));
      String filename = String.format("%s.der", thumbprint);
      File file = dir.toPath().resolve(filename).toFile();

      try (FileOutputStream fos = new FileOutputStream(file)) {
        fos.write(certificate.getEncoded());
        fos.flush();
      }

      LOGGER.debug("Wrote certificate entry: {}", file.getAbsolutePath());
    } catch (Exception e) {
      LOGGER.error("Error writing certificate", e);
    }
  }

  private synchronized boolean deleteCertificateFile(File dir, ByteString thumbprint) {
    File[] files = dir.listFiles();
    if (files == null) files = new File[0];

    for (File file : files) {
      boolean matchesThumbprint =
          decodeCertificateFile(file)
              .map(
                  c -> {
                    try {
                      ByteString bs = CertificateUtil.thumbprint(c);

                      return bs.equals(thumbprint);
                    } catch (UaException e) {
                      return false;
                    }
                  })
              .orElse(false);

      if (matchesThumbprint) {
        if (!file.delete()) {
          LOGGER.warn("Failed to delete issuer certificate: {}", file);
        }
        return true;
      }
    }

    return false;
  }

  /**
   * If {@code rejectedDir} contains more than {@code MAX_REJECTED_CERTIFICATES}, delete older
   * certificates until there are {@code MAX_REJECTED_CERTIFICATES} - 1 certificates (thus creating
   * room to add a new one).
   */
  static synchronized void pruneOldCertificates(File rejectedDir, int maxRejectedCertificates) {
    File[] files = rejectedDir.listFiles();

    if (files != null && files.length >= maxRejectedCertificates) {
      int excessCount = files.length - maxRejectedCertificates;

      // If last modified of any file changes during the sort it can lead
      // to "IllegalArgumentException: Comparison method violates its general contract!"
      // thrown from Java's TimSort implementation.
      Map<File, Long> stableLastModified = new HashMap<>();
      Arrays.stream(files).forEach(f -> stableLastModified.put(f, f.lastModified()));

      Arrays.stream(files)
          .sorted(
              (f1, f2) ->
                  Long.compareUnsigned(stableLastModified.get(f1), stableLastModified.get(f2)))
          .limit(excessCount + 1)
          .forEach(
              file -> {
                if (!file.delete()) {
                  LOGGER.warn("Unable to delete rejected certificate: {}", file);
                }
              });
    }
  }

  /**
   * Create a {@link FileBasedCertificateQuarantine} in {@code dir}. If the directory does not exist
   * an attempt will be made to create it.
   *
   * @param dir the directory to use for the quarantine.
   * @return a new {@link FileBasedCertificateQuarantine} instance.
   * @throws IOException if the directory does not exist and cannot be created.
   */
  public static FileBasedCertificateQuarantine create(File dir) throws IOException {
    return create(dir.toPath());
  }

  /**
   * Create a {@link FileBasedCertificateQuarantine} in {@code dir}. If the directory does not exist
   * an attempt will be made to create it.
   *
   * @param dir the directory to use for the quarantine.
   * @return a new {@link FileBasedCertificateQuarantine} instance.
   * @throws IOException if the directory does not exist and cannot be created.
   */
  public static FileBasedCertificateQuarantine create(Path dir) throws IOException {
    if (!Files.exists(dir)) {
      Files.createDirectories(dir);
    }
    return new FileBasedCertificateQuarantine(dir.toFile());
  }
}
