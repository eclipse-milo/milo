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

import static org.eclipse.milo.opcua.stack.core.security.SecurityProviderResolver.ProviderProfile.BOUNCY_CASTLE;
import static org.eclipse.milo.opcua.stack.core.security.SecurityProviderResolver.ProviderProfile.JDK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.Provider;
import java.security.Security;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

@NullMarked
public class SecurityProviderResolverTest {

  // Endpoint advertisement should get a deterministic provider answer for every executable ECC
  // policy without depending on global JVM provider ordering.
  @Test
  public void testDefaultResolverPrefersBouncyCastleForCurrentEccPolicies() throws UaException {
    SecurityProviderResolver resolver = SecurityProviderResolver.create();

    assertEquals(BOUNCY_CASTLE, resolver.resolve(SecurityPolicy.ECC_nistP256_AesGcm.getProfile()));
    assertEquals(
        BOUNCY_CASTLE, resolver.resolve(SecurityPolicy.ECC_nistP256_ChaChaPoly.getProfile()));
    assertEquals(
        BOUNCY_CASTLE, resolver.resolve(SecurityPolicy.ECC_curve25519_AesGcm.getProfile()));
    assertEquals(
        BOUNCY_CASTLE, resolver.resolve(SecurityPolicy.ECC_curve25519_ChaChaPoly.getProfile()));
  }

  // The same policy tuples should remain executable on the built-in JDK providers for deployments
  // that do not want the Bouncy Castle profile.
  @Test
  public void testCurrentEccPoliciesCanUseJdkProviderProfile() throws UaException {
    SecurityProviderResolver resolver = SecurityProviderResolver.withProviderProfiles(List.of(JDK));

    assertEquals(JDK, resolver.resolve(SecurityPolicy.ECC_nistP256_AesGcm.getProfile()));
    assertEquals(JDK, resolver.resolve(SecurityPolicy.ECC_nistP256_ChaChaPoly.getProfile()));
    assertEquals(JDK, resolver.resolve(SecurityPolicy.ECC_curve25519_AesGcm.getProfile()));
    assertEquals(JDK, resolver.resolve(SecurityPolicy.ECC_curve25519_ChaChaPoly.getProfile()));
  }

  @Test
  public void testProviderProfileResolutionIsCachedByPolicy() throws UaException {
    SecurityProviderResolver resolver = SecurityProviderResolver.create();

    assertEquals(BOUNCY_CASTLE, resolver.resolve(SecurityPolicy.ECC_nistP256_AesGcm.getProfile()));
    assertEquals(BOUNCY_CASTLE, resolver.resolve(SecurityPolicy.ECC_nistP256_AesGcm.getProfile()));
  }

  @Test
  public void testResolverDoesNotInstallBouncyCastleGlobally() throws UaException {
    Provider installedBefore = Security.getProvider("BC");

    SecurityProviderResolver.create().resolve(SecurityPolicy.ECC_nistP256_AesGcm.getProfile());

    assertSame(installedBefore, Security.getProvider("BC"));
  }

  @Test
  public void testUnsupportedProviderConfigurationFailsClearly() {
    SecurityProviderResolver resolver = SecurityProviderResolver.withProviderProfiles(List.of());

    UaException exception =
        assertThrows(
            UaException.class,
            () -> resolver.resolve(SecurityPolicy.ECC_nistP256_AesGcm.getProfile()));

    assertEquals(StatusCodes.Bad_ConfigurationError, exception.getStatusCode().getValue());
    assertTrue(exception.getMessage().contains(SecurityPolicy.ECC_nistP256_AesGcm.getUri()));
    assertTrue(exception.getMessage().contains("configured provider profiles: <none>"));
  }

  @Test
  public void testExistingRsaPoliciesKeepCurrentJcaBehavior() throws UaException {
    SecurityProviderResolver resolver = SecurityProviderResolver.withProviderProfiles(List.of());

    assertEquals(JDK, resolver.resolve(SecurityPolicy.Basic256Sha256.getProfile()));
  }
}
