/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.tcp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * {@code Stack.ConnectionLimits} reads system properties once, during class initialization, and the
 * shared {@link RateLimitingHandler} reads those fields once, when it is first requested. Each test
 * loads both classes again in an {@link IsolatedStackClassLoader} so that it starts from a
 * JVM-start state, with the property set before either class initializes.
 */
class RateLimitingHandlerPropertiesTest {

  private static final String PROPERTY =
      Stack.ConnectionLimits.RATE_LIMIT_MAX_CONNECTIONS_PER_ADDRESS_PROPERTY;

  @BeforeEach
  void setProperty() {
    System.setProperty(PROPERTY, "250");
  }

  @AfterEach
  void clearProperty() {
    System.clearProperty(PROPERTY);
  }

  @Test
  void sharedHandlerUsesLimitFromSystemProperty() throws Exception {
    try (var loader = new IsolatedStackClassLoader()) {
      assertEquals(250, sharedHandlerMaxConnectionsPerAddress(loader));
    }
  }

  // Applications that already assign the fields in code must keep control over the limits.
  @Test
  void fieldAssignmentBeforeFirstUseOverridesSystemProperty() throws Exception {
    try (var loader = new IsolatedStackClassLoader()) {
      Field field =
          loader
              .loadClass(Stack.ConnectionLimits.class.getName())
              .getField("RATE_LIMIT_MAX_CONNECTIONS_PER_ADDRESS");

      assertEquals(250, field.getInt(null), "field initialized from the system property");

      field.setInt(null, 7);

      assertEquals(7, sharedHandlerMaxConnectionsPerAddress(loader));
    }
  }

  private static int sharedHandlerMaxConnectionsPerAddress(ClassLoader loader) throws Exception {
    Class<?> handlerClass = loader.loadClass(RateLimitingHandler.class.getName());
    Object handler = handlerClass.getMethod("getInstance").invoke(null);

    Field field = handlerClass.getDeclaredField("maxConnectionsPerAddress");
    field.setAccessible(true);

    return field.getInt(handler);
  }

  /**
   * Defines its own copies of the stack-core and transport classes, so their static state starts
   * fresh, and delegates everything else, such as Netty, to the test class loader.
   */
  private static final class IsolatedStackClassLoader extends URLClassLoader {

    IsolatedStackClassLoader() {
      super(
          new URL[] {location(Stack.class), location(RateLimitingHandler.class)},
          RateLimitingHandlerPropertiesTest.class.getClassLoader());
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
      if (!name.startsWith("org.eclipse.milo.opcua.stack.")) {
        return super.loadClass(name, resolve);
      }

      synchronized (getClassLoadingLock(name)) {
        Class<?> c = findLoadedClass(name);

        if (c == null) {
          try {
            c = findClass(name);
          } catch (ClassNotFoundException e) {
            return super.loadClass(name, resolve);
          }
        }

        if (resolve) {
          resolveClass(c);
        }

        return c;
      }
    }

    private static URL location(Class<?> c) {
      return c.getProtectionDomain().getCodeSource().getLocation();
    }
  }
}
