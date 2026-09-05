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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class GdsModulePathTest {
  @TempDir Path directory;

  // Java's module resolver rejects split packages even though ordinary classpath calls work.
  // Resolve the actual production classes from both required artifacts together.
  @Test
  void clientAndGdsArtifactsResolveTogetherAsAutomaticModules() throws Exception {
    Path client = moduleJar(OpcUaClient.class, "org.eclipse.milo.opcua.sdk.client");
    Path gds = moduleJar(GdsClient.class, "org.eclipse.milo.opcua.sdk.client.gds");
    Configuration resolved =
        ModuleLayer.boot()
            .configuration()
            .resolve(
                ModuleFinder.of(client, gds),
                ModuleFinder.of(),
                Set.of(
                    "org.eclipse.milo.opcua.sdk.client", "org.eclipse.milo.opcua.sdk.client.gds"));
    assertEquals(2, resolved.modules().size());
  }

  private Path moduleJar(Class<?> representative, String moduleName) throws Exception {
    Path source =
        Path.of(representative.getProtectionDomain().getCodeSource().getLocation().toURI());
    if (Files.isRegularFile(source)) {
      return source;
    }
    // Maven may expose a reactor dependency's classes directory instead of its packaged JAR.
    var manifest = new Manifest();
    manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
    manifest.getMainAttributes().putValue("Automatic-Module-Name", moduleName);
    Path jar = directory.resolve(moduleName + ".jar");
    try (var output = new JarOutputStream(Files.newOutputStream(jar), manifest);
        var files = Files.walk(source)) {
      for (Path file : files.filter(Files::isRegularFile).toList()) {
        String name = source.relativize(file).toString().replace('\\', '/');
        if (name.equals("META-INF/MANIFEST.MF")) continue;
        output.putNextEntry(new JarEntry(name));
        Files.copy(file, output);
        output.closeEntry();
      }
    }
    return jar;
  }
}
