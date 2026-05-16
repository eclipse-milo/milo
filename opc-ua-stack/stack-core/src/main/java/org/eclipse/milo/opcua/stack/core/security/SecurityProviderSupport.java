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

import java.security.GeneralSecurityException;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.milo.opcua.stack.core.security.SecurityProviderResolver.ProviderProfile;
import org.jspecify.annotations.NullMarked;

/**
 * Applies a resolved provider profile to JCA operation creation.
 *
 * <p>The public resolver decides which provider family can satisfy a security policy. This helper
 * is the package-local bridge used by primitive code when it needs an actual {@link Provider}
 * instance for a signature, key agreement, key factory, or MAC operation.
 */
@NullMarked
final class SecurityProviderSupport {

  private static final String BC_PROVIDER_NAME = "BC";

  private SecurityProviderSupport() {}

  static <T> T withProviderProfile(
      ProviderProfile providerProfile, String operationName, ProviderOperation<T> operation)
      throws GeneralSecurityException {

    return switch (providerProfile) {
      case BOUNCY_CASTLE -> operation.apply(bouncyCastleProvider());
      case JDK -> withJdkProvider(operationName, operation);
    };
  }

  private static Provider bouncyCastleProvider() {
    Provider provider = Security.getProvider(BC_PROVIDER_NAME);

    return provider != null ? provider : new BouncyCastleProvider();
  }

  private static <T> T withJdkProvider(String operationName, ProviderOperation<T> operation)
      throws GeneralSecurityException {

    List<String> attempts = new ArrayList<>();

    for (Provider provider : Security.getProviders()) {
      if (BC_PROVIDER_NAME.equals(provider.getName())) {
        continue;
      }

      try {
        return operation.apply(provider);
      } catch (GeneralSecurityException | RuntimeException e) {
        attempts.add(provider.getName() + ": " + e.getClass().getSimpleName());
      }
    }

    throw new GeneralSecurityException(
        "no built-in JDK/JCA provider for " + operationName + "; attempted " + attempts);
  }

  @FunctionalInterface
  interface ProviderOperation<T> {
    T apply(Provider provider) throws GeneralSecurityException;
  }
}
