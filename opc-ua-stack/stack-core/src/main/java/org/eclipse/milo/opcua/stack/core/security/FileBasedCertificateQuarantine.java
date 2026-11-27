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
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link CertificateQuarantine} that stores rejected certificates as files in a directory, named
 * by the lowercase hex SHA-1 thumbprint of each certificate's DER encoding ({@code
 * <thumbprint>.der}).
 *
 * <p>Every regular file in the directory is read as a certificate; files that fail to decode are
 * logged and skipped, and entries that vanish concurrently are skipped. Duplicate certificates
 * present under multiple filenames are deduplicated when listed and all matching files are removed
 * on {@link #removeRejectedCertificate(X509Certificate)}. Hidden files, whose names start with
 * {@code .}, and subdirectories are not read, so files such as the {@code .DS_Store} that macOS
 * Finder creates are not listed, do not count toward the rejected-certificate limit, and are never
 * pruned.
 *
 * <p>{@link #getRejectedCertificates()} and {@link #removeRejectedCertificate(X509Certificate)}
 * report directory-listing, per-file read, encoding, and deletion failures as {@link
 * UncheckedIOException}. On removal, every matching copy in the directory is still attempted before
 * throwing, and any additional I/O failures are attached as suppressed exceptions. {@link
 * #addRejectedCertificate(X509Certificate)} logs storage and pruning failures without throwing so
 * certificate validation results are never replaced.
 */
public class FileBasedCertificateQuarantine implements CertificateQuarantine {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(FileBasedCertificateQuarantine.class);

  /**
   * The maximum number of certificates that can accumulate in the rejected directory before the
   * oldest certificates are deleted to make room for newer ones.
   */
  static final int DEFAULT_MAX_REJECTED_CERTIFICATES = 128;

  private final Path rejectedDir;
  private final int maxRejectedCertificates;

  public FileBasedCertificateQuarantine(File rejectedDir) {
    this(rejectedDir, DEFAULT_MAX_REJECTED_CERTIFICATES);
  }

  public FileBasedCertificateQuarantine(File rejectedDir, int maxRejectedCertificates) {
    this.rejectedDir = rejectedDir.toPath();
    this.maxRejectedCertificates = maxRejectedCertificates;

    assert rejectedDir.exists();
  }

  /**
   * {@inheritDoc}
   *
   * <p>Files whose contents fail to decode are logged and skipped, and entries that vanish while
   * listing are skipped. Directory-listing, metadata-lookup, and file-read I/O errors fail the
   * call.
   *
   * @throws UncheckedIOException if the rejected directory cannot be listed or any entry cannot be
   *     inspected or read.
   */
  @Override
  public List<X509Certificate> getRejectedCertificates() {
    try {
      var certificates = new LinkedHashSet<X509Certificate>();
      for (Path entry : listEntries(rejectedDir)) {
        if (isRegularFile(entry)) {
          decodeCertificateFile(entry).ifPresent(certificates::add);
        }
      }
      return List.copyOf(certificates);
    } catch (IOException e) {
      LOGGER.warn("Failed to read rejected certificates from {}", rejectedDir, e);
      throw new UncheckedIOException(e);
    }
  }

  /**
   * {@inheritDoc}
   *
   * <p>Pruning and write failures are logged and never thrown so certificate validation errors are
   * preserved.
   */
  @Override
  public void addRejectedCertificate(X509Certificate certificate) {
    pruneOldCertificates();

    writeCertificateFile(rejectedDir, certificate);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Every matching non-hidden regular file in the directory is attempted before throwing. If
   * more than one per-file read or deletion fails, the first failure is the cause of the thrown
   * {@link UncheckedIOException} and subsequent failures are attached as suppressed exceptions.
   *
   * @throws UncheckedIOException if {@code certificate} cannot be encoded, the rejected directory
   *     cannot be listed, or inspecting, reading, or deleting any file fails.
   */
  @Override
  public void removeRejectedCertificate(X509Certificate certificate) {
    ByteString thumbprint;
    try {
      thumbprint = ByteString.of(sha1(certificate.getEncoded()));
    } catch (CertificateEncodingException e) {
      throw new UncheckedIOException(new IOException("Failed to encode certificate", e));
    }

    try {
      deleteCertificateFiles(rejectedDir, thumbprint);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static List<Path> listEntries(Path directory) throws IOException {
    try (Stream<Path> entries = Files.list(directory)) {
      return entries.filter(path -> !path.getFileName().toString().startsWith(".")).toList();
    } catch (UncheckedIOException e) {
      throw e.getCause();
    }
  }

  BasicFileAttributes readFileAttributes(Path path) throws IOException {
    return Files.readAttributes(path, BasicFileAttributes.class);
  }

  private boolean isRegularFile(Path path) throws IOException {
    try {
      return readFileAttributes(path).isRegularFile();
    } catch (NoSuchFileException ignored) {
      return false;
    }
  }

  InputStream openFileInputStream(Path path) throws IOException {
    return Files.newInputStream(path);
  }

  private Optional<X509Certificate> decodeCertificateFile(Path path) throws IOException {
    InputStream raw;
    try {
      raw = openFileInputStream(path);
    } catch (NoSuchFileException ignored) {
      return Optional.empty();
    }

    try (var in = new TrackingInputStream(raw)) {
      try {
        X509Certificate certificate = CertificateUtil.decodeCertificate(in);
        IOException failure = in.readFailure();
        if (failure != null) {
          throw failure;
        }
        return Optional.of(certificate);
      } catch (UaException | RuntimeException e) {
        IOException failure = in.readFailure();
        if (failure != null) {
          throw failure;
        }

        // This repeats every time the directory is listed, so the stack trace is logged at DEBUG.
        LOGGER.warn("Error decoding certificate file {}: {}", path, e.toString());
        LOGGER.debug("Error decoding certificate file {}", path, e);

        return Optional.empty();
      }
    }
  }

  private static void writeCertificateFile(Path dir, X509Certificate certificate) {
    try {
      byte[] encoded = certificate.getEncoded();
      String thumbprint = ByteBufUtil.hexDump(sha1(encoded));
      String filename = String.format("%s.der", thumbprint);
      Path file = dir.resolve(filename);

      try (OutputStream out = Files.newOutputStream(file)) {
        out.write(encoded);
        out.flush();
      }

      LOGGER.debug("Wrote certificate entry: {}", file.toAbsolutePath());
    } catch (Exception e) {
      LOGGER.error("Error writing certificate", e);
    }
  }

  private synchronized void deleteCertificateFiles(Path dir, ByteString thumbprint)
      throws IOException {
    List<Path> entries;
    try {
      entries = listEntries(dir);
    } catch (IOException e) {
      LOGGER.warn("Failed to list rejected certificates in {}", dir, e);
      throw e;
    }

    IOException failure = null;

    for (Path entry : entries) {
      try {
        if (!isRegularFile(entry)) {
          continue;
        }

        boolean matchesThumbprint =
            decodeCertificateFile(entry)
                .map(
                    c -> {
                      try {
                        return CertificateUtil.thumbprint(c).equals(thumbprint);
                      } catch (UaException e) {
                        return false;
                      }
                    })
                .orElse(false);

        if (matchesThumbprint) {
          try {
            deleteCertificateFile(entry);
          } catch (IOException e) {
            LOGGER.warn("Failed to delete rejected certificate: {}", entry, e);
            failure = combineFailures(failure, e);
          }
        }
      } catch (IOException e) {
        LOGGER.warn("Failed to read rejected certificate: {}", entry, e);
        failure = combineFailures(failure, e);
      }
    }

    if (failure != null) {
      throw failure;
    }
  }

  boolean deleteCertificateFile(Path path) throws IOException {
    return Files.deleteIfExists(path);
  }

  /**
   * If {@code rejectedDir} contains at least {@code maxRejectedCertificates}, delete older
   * certificates until there are {@code maxRejectedCertificates} - 1 certificates (thus creating
   * room to add a new one).
   */
  private synchronized void pruneOldCertificates() {
    List<Path> entries;
    try {
      entries = listEntries(rejectedDir);
    } catch (IOException e) {
      LOGGER.warn("Error listing rejected certificates for pruning in {}", rejectedDir, e);
      return;
    }

    var files = new ArrayList<EntryTimestamp>(entries.size());
    for (Path entry : entries) {
      try {
        BasicFileAttributes attributes = readFileAttributes(entry);
        if (attributes.isRegularFile()) {
          files.add(new EntryTimestamp(entry, attributes.lastModifiedTime()));
        }
      } catch (NoSuchFileException ignored) {
        // Entry disappeared before its attributes were read.
      } catch (IOException e) {
        LOGGER.warn("Error reading rejected certificate attributes: {}", entry, e);
      }
    }

    if (files.size() >= maxRejectedCertificates) {
      int excessCount = files.size() - maxRejectedCertificates;

      // Capture lastModifiedTime up front so concurrent mtime changes cannot lead
      // to "IllegalArgumentException: Comparison method violates its general contract!"
      // thrown from Java's TimSort implementation.
      files.stream()
          .sorted(Comparator.comparing(EntryTimestamp::lastModifiedTime))
          .limit(excessCount + 1)
          .forEach(
              entry -> {
                try {
                  deleteCertificateFile(entry.path());
                } catch (IOException e) {
                  LOGGER.warn("Unable to delete rejected certificate: {}", entry.path(), e);
                }
              });
    }
  }

  private static IOException combineFailures(@Nullable IOException primary, IOException next) {
    if (primary == null) {
      return next;
    }
    primary.addSuppressed(next);
    return primary;
  }

  private record EntryTimestamp(Path path, FileTime lastModifiedTime) {}

  private static final class TrackingInputStream extends FilterInputStream {

    private @Nullable IOException readFailure;

    TrackingInputStream(InputStream in) {
      super(in);
    }

    @Nullable IOException readFailure() {
      return readFailure;
    }

    private IOException record(IOException e) {
      if (readFailure == null) {
        readFailure = e;
      }
      return e;
    }

    @Override
    public int read() throws IOException {
      try {
        return in.read();
      } catch (IOException e) {
        throw record(e);
      }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
      try {
        return in.read(b, off, len);
      } catch (IOException e) {
        throw record(e);
      }
    }

    @Override
    public long skip(long n) throws IOException {
      try {
        return in.skip(n);
      } catch (IOException e) {
        throw record(e);
      }
    }

    @Override
    public int available() throws IOException {
      try {
        return in.available();
      } catch (IOException e) {
        throw record(e);
      }
    }

    @Override
    public synchronized void reset() throws IOException {
      try {
        in.reset();
      } catch (IOException e) {
        throw record(e);
      }
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
