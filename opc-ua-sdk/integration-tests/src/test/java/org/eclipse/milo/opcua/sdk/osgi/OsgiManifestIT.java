/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.osgi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Checks the OSGi manifests of the packaged Milo bundles on the test class path.
 *
 * <p>maven-shade-plugin relocates Guava to {@code org.eclipse.milo.shaded.com.google.common} when
 * it packages each module, and milo-stack-core embeds the relocated classes. These checks read the
 * packaged jars, so they run as failsafe integration tests after the reactor packages the
 * dependencies. When the dependencies resolve to class directories instead, the tests are skipped.
 */
class OsgiManifestIT {

  private static final String SHADED_GUAVA = "org.eclipse.milo.shaded.com.google.common";

  // milo-guava-dependencies holds the unrelocated Guava classes that milo-stack-core embeds. It is
  // not published, and it is only on this class path because the reactor does not use the
  // dependency-reduced POMs.
  private static final String GUAVA_DEPENDENCIES = "org.eclipse.milo.guava-dependencies";

  private static final Pattern SHADED_GUAVA_CLASS_REFERENCE =
      Pattern.compile("org/eclipse/milo/shaded/com/google/common/[A-Za-z0-9_$/]+");

  private static List<MiloBundle> bundles;

  static synchronized List<MiloBundle> bundles() throws IOException {
    if (bundles == null) {
      bundles = findMiloBundles();
    }
    assumeFalse(bundles.isEmpty(), "packaged Milo bundles are not on the class path");
    return bundles;
  }

  // Bytecode that refers to a relocated Guava class the bundle neither contains nor imports fails
  // at runtime with NoClassDefFoundError (issue #2030).
  @ParameterizedTest
  @MethodSource("bundles")
  void importsEveryRelocatedGuavaPackageItsBytecodeUses(MiloBundle bundle) {
    Set<String> missing = new TreeSet<>(bundle.referencedShadedPackages());
    missing.removeAll(bundle.containedPackages());
    missing.removeAll(bundle.importedPackages());

    assertEquals(Set.of(), missing, bundle.symbolicName() + " is missing Import-Package entries");
  }

  // milo-stack-core embeds the relocated Guava classes, so it must export every relocated package
  // another Milo bundle imports.
  @Test
  void everyImportedRelocatedGuavaPackageIsExported() throws IOException {
    Set<String> exported = new TreeSet<>();
    Set<String> imported = new TreeSet<>();
    for (MiloBundle bundle : bundles()) {
      exported.addAll(bundle.exportedPackages());
      bundle.importedPackages().stream()
          .filter(OsgiManifestIT::isShadedGuava)
          .forEach(imported::add);
    }

    assertTrue(
        imported.stream().anyMatch(p -> p.startsWith(SHADED_GUAVA + ".")),
        "expected at least one bundle to import a relocated Guava package");

    imported.removeAll(exported);
    assertEquals(Set.of(), imported, "imported but not exported by any Milo bundle");
  }

  // The bytecode never refers to com.google.common after relocation. Importing it would force
  // users to install a Guava bundle that Milo never loads classes from.
  @ParameterizedTest
  @MethodSource("bundles")
  void doesNotImportUnrelocatedGuava(MiloBundle bundle) {
    List<String> guavaImports =
        bundle.importedPackages().stream().filter(p -> p.startsWith("com.google.common")).toList();

    assertEquals(List.of(), guavaImports, bundle.symbolicName() + " imports unrelocated Guava");
  }

  private static boolean isShadedGuava(String packageName) {
    return packageName.equals(SHADED_GUAVA) || packageName.startsWith(SHADED_GUAVA + ".");
  }

  private static List<MiloBundle> findMiloBundles() throws IOException {
    var found = new ArrayList<MiloBundle>();

    ClassLoader classLoader = OsgiManifestIT.class.getClassLoader();
    for (URL url : Collections.list(classLoader.getResources(JarFile.MANIFEST_NAME))) {
      if (!"jar".equals(url.getProtocol())) {
        continue;
      }
      var connection = (JarURLConnection) url.openConnection();
      connection.setUseCaches(false);
      try (JarFile jar = connection.getJarFile()) {
        Manifest manifest = jar.getManifest();
        String symbolicName =
            manifest == null ? null : manifest.getMainAttributes().getValue("Bundle-SymbolicName");
        if (symbolicName != null
            && symbolicName.startsWith("org.eclipse.milo.")
            && !symbolicName.equals(GUAVA_DEPENDENCIES)) {
          found.add(MiloBundle.read(symbolicName, jar, manifest));
        }
      }
    }

    return found;
  }

  record MiloBundle(
      String symbolicName,
      Set<String> importedPackages,
      Set<String> exportedPackages,
      Set<String> containedPackages,
      Set<String> referencedShadedPackages) {

    static MiloBundle read(String symbolicName, JarFile jar, Manifest manifest) {
      var contained = new TreeSet<String>();
      var referenced = new TreeSet<String>();

      for (JarEntry entry : Collections.list(jar.entries())) {
        String name = entry.getName();
        if (!name.endsWith(".class") || name.startsWith("META-INF/")) {
          continue;
        }
        contained.add(packageOf(name));

        // Class names in the constant pool are ASCII, so a Latin-1 view of the bytes finds them.
        String bytes = new String(readAllBytes(jar, entry), StandardCharsets.ISO_8859_1);
        Matcher matcher = SHADED_GUAVA_CLASS_REFERENCE.matcher(bytes);
        while (matcher.find()) {
          referenced.add(packageOf(matcher.group()));
        }
      }

      return new MiloBundle(
          symbolicName,
          packageNames(manifest.getMainAttributes().getValue("Import-Package")),
          packageNames(manifest.getMainAttributes().getValue("Export-Package")),
          contained,
          referenced);
    }

    @Override
    public String toString() {
      return symbolicName;
    }

    private static String packageOf(String classPath) {
      return classPath.substring(0, classPath.lastIndexOf('/')).replace('/', '.');
    }

    private static byte[] readAllBytes(JarFile jar, JarEntry entry) {
      try (InputStream in = jar.getInputStream(entry)) {
        return in.readAllBytes();
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

    /**
     * Returns the package names in an Import-Package or Export-Package header.
     *
     * <p>Clauses are separated by commas outside quoted values. Within a clause, the names come
     * before the first attribute or directive, separated by semicolons.
     */
    private static Set<String> packageNames(String header) {
      var names = new TreeSet<String>();
      if (header == null) {
        return names;
      }

      var clause = new StringBuilder();
      boolean quoted = false;
      for (char c : (header + ",").toCharArray()) {
        if (c == '"') {
          quoted = !quoted;
        }
        if (c == ',' && !quoted) {
          for (String part : clause.toString().split(";")) {
            if (part.contains("=")) {
              break;
            }
            names.add(part.trim());
          }
          clause.setLength(0);
        } else {
          clause.append(c);
        }
      }

      return names;
    }
  }
}
