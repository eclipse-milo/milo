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

import static org.junit.jupiter.api.Assertions.assertSame;

import java.security.Provider;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

@NullMarked
public class SecurityProviderSupportTest {

  // Milo never registers Bouncy Castle with java.security.Security, so every enhanced crypto
  // operation falls through to the fallback. The fallback must be a single shared instance rather
  // than a fully-populated BouncyCastleProvider constructed per call.
  @Test
  public void testFallbackBouncyCastleProviderIsSharedAcrossCalls() {
    Provider removed = Security.getProvider("BC");
    if (removed != null) {
      Security.removeProvider("BC");
    }

    try {
      Provider first = SecurityProviderSupport.bouncyCastleProvider();
      Provider second = SecurityProviderSupport.bouncyCastleProvider();

      assertSame(first, second);
    } finally {
      if (removed != null) {
        Security.addProvider(removed);
      }
    }
  }

  // When BC is registered with java.security.Security the registered instance must win, so a
  // deployment that intentionally installs Bouncy Castle still gets the provider it configured.
  @Test
  public void testRegisteredBouncyCastleProviderTakesPrecedenceOverFallback() {
    Provider previous = Security.getProvider("BC");
    if (previous != null) {
      Security.removeProvider("BC");
    }

    BouncyCastleProvider registered = new BouncyCastleProvider();
    Security.addProvider(registered);

    try {
      assertSame(registered, SecurityProviderSupport.bouncyCastleProvider());
    } finally {
      Security.removeProvider("BC");
      if (previous != null) {
        Security.addProvider(previous);
      }
    }
  }
}
