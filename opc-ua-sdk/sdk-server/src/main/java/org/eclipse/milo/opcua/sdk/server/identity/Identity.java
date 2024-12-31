/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.identity;

import java.security.cert.X509Certificate;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.AnonymousIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.IssuedIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.UserNameIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.X509IdentityToken;
import org.jspecify.annotations.Nullable;

public interface Identity {

  /**
   * @return the {@link UserTokenType} for this identity.
   */
  UserTokenType getUserTokenType();

  /**
   * @return the user data object associated with this identity.
   */
  @Nullable Object getUserData();

  /**
   * Associate an arbitrary user data object with this identity.
   *
   * @param userData the user data object.
   */
  void setUserData(@Nullable Object userData);

  /**
   * Check if this {@link Identity} is equal to another, i.e. it represents the same user.
   *
   * @param identity the {@link Identity} to compare to.
   * @return true if the identities are equal.
   */
  boolean equalTo(Identity identity);

  /** An {@link Identity} derived from validation of an {@link AnonymousIdentityToken}. */
  interface AnonymousIdentity extends Identity {

    @Override
    default boolean equalTo(Identity identity) {
      // any anonymous identity is equal to any other anonymous identity
      return identity instanceof Identity.AnonymousIdentity;
    }
  }

  /** An {@link Identity} derived from validation of a {@link UserNameIdentityToken}. */
  interface UsernameIdentity extends Identity {

    /**
     * @return the username for this identity.
     */
    String getUsername();
  }

  /** An {@link Identity} derived from validation of an {@link X509IdentityToken}. */
  interface X509UserIdentity extends Identity {

    /**
     * @return the {@link X509Certificate} for this identity.
     */
    X509Certificate getCertificate();
  }

  /** An {@link Identity} derived from validation of an {@link IssuedIdentityToken}. */
  interface IssuedIdentity extends Identity {

    /**
     * @return a {@link ByteString} containing opaque issued token data.
     */
    ByteString getTokenData();
  }
}
