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

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;

/**
 *
 *
 * <pre>
 * <b>P_SHA-1(secret, seed)</b> =
 * HMAC_SHA-1(secret, A(1) + seed) +
 * HMAC_SHA-1(secret, A(2) + seed) +
 * HMAC_SHA-1(secret, A(3) + seed) + ...
 * <i>Where + indicates concatenation.</i>
 * <br>
 * A() is defined as:
 * A(0) = seed
 * A(i) = HMAC_SHA-1(secret, A(i-1))
 * </pre>
 */
public class PShaUtil {

  public static byte[] createPSha1Key(byte[] secret, byte[] seed, int offset, int length) {
    return createKey("HmacSHA1", secret, seed, offset, length);
  }

  public static byte[] createPSha256Key(byte[] secret, byte[] seed, int offset, int length) {
    return createKey("HmacSHA256", secret, seed, offset, length);
  }

  private static byte[] createKey(
      String transformation, byte[] secret, byte[] seed, int offset, int length) {
    try {
      Mac mac = Mac.getInstance(transformation);

      byte[] tempBytes = hash(transformation, secret, seed, mac, offset + length);
      byte[] key = new byte[length];

      System.arraycopy(tempBytes, offset, key, 0, key.length);

      return key;
    } catch (Exception e) {
      throw new UaRuntimeException(StatusCodes.Bad_InternalError, e);
    }
  }

  private static byte[] hash(
      String transformation, byte[] secret, byte[] seed, Mac mac, int required) throws Exception {

    byte[] out = new byte[required];
    int offset = 0;
    int toCopy;
    byte[] a = seed;
    byte[] tmp;

    while (required > 0) {
      SecretKeySpec key = new SecretKeySpec(secret, transformation);
      mac.init(key);
      mac.update(a);
      a = mac.doFinal();
      mac.reset();
      mac.init(key);
      mac.update(a);
      mac.update(seed);
      tmp = mac.doFinal();
      toCopy = Math.min(required, tmp.length);
      System.arraycopy(tmp, 0, out, offset, toCopy);
      offset += toCopy;
      required -= toCopy;
    }

    return out;
  }
}
