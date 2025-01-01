/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.identity;

import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserIdentityToken;

public class SignedIdentityToken {

  private final UserIdentityToken token;
  private final SignatureData signature;

  public SignedIdentityToken(UserIdentityToken token, SignatureData signature) {
    this.token = token;
    this.signature = signature;
  }

  public UserIdentityToken getToken() {
    return token;
  }

  public SignatureData getSignature() {
    return signature;
  }
}
