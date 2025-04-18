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

import com.google.common.base.Objects;
import java.security.cert.X509Certificate;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;

public class DefaultX509UserIdentity extends AbstractIdentity implements Identity.X509UserIdentity {

  private final X509Certificate certificate;

  public DefaultX509UserIdentity(X509Certificate certificate) {
    this.certificate = certificate;
  }

  @Override
  public UserTokenType getUserTokenType() {
    return UserTokenType.Certificate;
  }

  @Override
  public X509Certificate getCertificate() {
    return certificate;
  }

  @Override
  public boolean equalTo(Identity identity) {
    if (identity instanceof X509UserIdentity other) {
      return Objects.equal(getCertificate(), other.getCertificate());
    }

    return false;
  }
}
