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

import com.google.common.collect.Maps;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManifestUtil {

  private static final Logger logger = LoggerFactory.getLogger(ManifestUtil.class);
  private static final Map<String, String> ATTRIBUTES = readAttributes();

  public static Optional<String> read(String name) {
    return Optional.ofNullable(ATTRIBUTES.get(name));
  }

  public static boolean exists(String name) {
    return ATTRIBUTES.containsKey(name);
  }

  private static Map<String, String> readAttributes() {
    Map<String, String> attributes = Maps.newConcurrentMap();

    try {
      Enumeration<URL> resources =
          ManifestUtil.class.getClassLoader().getResources("META-INF/MANIFEST.MF");

      while (resources.hasMoreElements()) {
        URL url = resources.nextElement();

        attributes.putAll(readAttributes(url));
      }
    } catch (Throwable t) {
      logger.error("readAttributes() failed", t);
    }

    return attributes;
  }

  private static Map<String, String> readAttributes(URL url) {
    Map<String, String> props = new HashMap<>();

    try (InputStream stream = url.openStream()) {
      Manifest manifest = new Manifest(stream);
      Attributes attributes = manifest.getMainAttributes();

      for (Object key : attributes.keySet()) {
        String value = attributes.getValue((Attributes.Name) key);
        props.put(key.toString(), value);
      }
    } catch (Throwable t) {
      logger.error("readAttributes(): '{}' failed", url, t);
    }

    return props;
  }
}
