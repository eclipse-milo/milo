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

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.PublicKeyAlgorithm;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;

/**
 * Enforcement of the OPC UA Part 4 (7.41) rules that constrain how a user identity token's {@link
 * SecurityPolicy} may relate to the carrying SecureChannel.
 *
 * <p>The rules are independent of the identity provider, so client and server username and
 * certificate identity paths share them here rather than each re-deriving the constraint. They have
 * different applicability and are exposed separately:
 *
 * <ul>
 *   <li>{@link #requireSecuredChannelForEnhancedSecret} applies only to token types whose secret is
 *       encrypted (such as username tokens): an ECC or RSA-DH secret cannot be protected on a
 *       {@code None} SecureChannel, which cannot negotiate the ephemeral key material those
 *       policies need. It must not be applied to signed certificate tokens, whose enhanced
 *       signatures are explicitly supported on a {@code None} channel (the reduced Part 4 6.1.8
 *       Table 101 layout).
 *   <li>{@link #requireSamePublicKeyAlgorithmAsChannel} applies to every user-token type: an
 *       explicitly specified user-token SecurityPolicy must share the SecureChannel's public-key
 *       algorithm family (RSA vs ECC).
 * </ul>
 *
 * @see <a href="https://reference.opcfoundation.org/Core/Part4/v105/docs/7.41">
 *     https://reference.opcfoundation.org/Core/Part4/v105/docs/7.41</a>
 */
public final class UserTokenSecurityPolicyRules {

  private UserTokenSecurityPolicyRules() {}

  /**
   * Reject an enhanced (ECC or RSA-DH) user-token secret policy on a {@code None} SecureChannel.
   *
   * <p>Part 4 (7.41): when the SecurityMode is None, Clients shall not use UserTokenPolicies that
   * require encryption with an ECC or RSA-DH SecurityPolicy, because a None channel cannot
   * negotiate the ephemeral key material those policies need to protect the token secret. This is
   * meaningful only for token types that encrypt a secret (such as username tokens); it must not be
   * used for signed certificate tokens, whose enhanced signatures are supported on a None channel.
   *
   * @param endpoint the endpoint whose SecurityMode describes the SecureChannel.
   * @param userTokenSecurityPolicy the resolved user-token security policy.
   * @throws UaException with {@link StatusCodes#Bad_SecurityPolicyRejected} if an enhanced secret
   *     policy is used on a None SecureChannel.
   */
  public static void requireSecuredChannelForEnhancedSecret(
      EndpointDescription endpoint, SecurityPolicy userTokenSecurityPolicy) throws UaException {

    requireSecuredChannelForEnhancedSecret(endpoint.getSecurityMode(), userTokenSecurityPolicy);
  }

  /**
   * Reject an enhanced (ECC or RSA-DH) user-token secret policy on a {@code None} SecureChannel.
   *
   * @param channelSecurityMode the SecureChannel security mode.
   * @param userTokenSecurityPolicy the resolved user-token security policy.
   * @throws UaException with {@link StatusCodes#Bad_SecurityPolicyRejected} if an enhanced secret
   *     policy is used on a None SecureChannel.
   */
  public static void requireSecuredChannelForEnhancedSecret(
      MessageSecurityMode channelSecurityMode, SecurityPolicy userTokenSecurityPolicy)
      throws UaException {

    if (channelSecurityMode == MessageSecurityMode.None
        && userTokenSecurityPolicy.getProfile().usesEnhancedUserTokenSecret()) {

      throw new UaException(
          StatusCodes.Bad_SecurityPolicyRejected,
          "ECC and RSA-DH user-token security policies require encryption that a None SecureChannel"
              + " cannot negotiate; refusing to use "
              + userTokenSecurityPolicy.getUri()
              + " on a None SecureChannel");
    }
  }

  /**
   * Require an explicitly specified user-token SecurityPolicy to share the SecureChannel's
   * public-key algorithm family.
   *
   * <p>Part 4 (7.41): if a UserTokenPolicy specifies a SecurityPolicy, it shall use the same
   * PublicKey algorithm (RSA vs ECC) as the SecureChannel. Only an explicitly specified, encrypting
   * (RSA or ECC) policy is constrained: an inherited policy already matches the channel, and an
   * explicit {@code None} policy (the plaintext-secret case) carries no PublicKey algorithm. The
   * rule is independent of the user-token type, so username and certificate tokens both apply it.
   *
   * @param endpoint the endpoint whose SecurityMode and SecurityPolicy describe the SecureChannel.
   * @param userTokenSecurityPolicy the resolved user-token security policy.
   * @param explicitlySpecified whether the token policy named its own {@code securityPolicyUri}
   *     rather than inheriting the endpoint's.
   * @throws UaException with {@link StatusCodes#Bad_SecurityPolicyRejected} if the explicit
   *     policy's public-key algorithm differs from the SecureChannel's.
   */
  public static void requireSamePublicKeyAlgorithmAsChannel(
      EndpointDescription endpoint,
      SecurityPolicy userTokenSecurityPolicy,
      boolean explicitlySpecified)
      throws UaException {

    SecurityPolicy channelSecurityPolicy = SecurityPolicy.fromUri(endpoint.getSecurityPolicyUri());

    requireSamePublicKeyAlgorithmAsChannel(
        endpoint.getSecurityMode(),
        channelSecurityPolicy,
        userTokenSecurityPolicy,
        explicitlySpecified);
  }

  /**
   * Require an explicitly specified user-token SecurityPolicy to share the SecureChannel's
   * public-key algorithm family.
   *
   * @param channelSecurityMode the SecureChannel security mode.
   * @param channelSecurityPolicy the SecureChannel security policy.
   * @param userTokenSecurityPolicy the resolved user-token security policy.
   * @param explicitlySpecified whether the token policy named its own {@code securityPolicyUri}
   *     rather than inheriting the channel's.
   * @throws UaException with {@link StatusCodes#Bad_SecurityPolicyRejected} if the explicit
   *     policy's public-key algorithm differs from the SecureChannel's.
   */
  public static void requireSamePublicKeyAlgorithmAsChannel(
      MessageSecurityMode channelSecurityMode,
      SecurityPolicy channelSecurityPolicy,
      SecurityPolicy userTokenSecurityPolicy,
      boolean explicitlySpecified)
      throws UaException {

    if (channelSecurityMode == MessageSecurityMode.None || !explicitlySpecified) {
      return;
    }

    PublicKeyAlgorithm userTokenAlgorithm =
        userTokenSecurityPolicy.getProfile().publicKeyAlgorithm();
    if (userTokenAlgorithm == PublicKeyAlgorithm.NONE) {
      return;
    }

    PublicKeyAlgorithm channelAlgorithm = channelSecurityPolicy.getProfile().publicKeyAlgorithm();

    if (userTokenAlgorithm != channelAlgorithm) {
      throw new UaException(
          StatusCodes.Bad_SecurityPolicyRejected,
          "user-token security policy "
              + userTokenSecurityPolicy.getUri()
              + " ("
              + userTokenAlgorithm
              + ") must use the same public-key algorithm as the SecureChannel policy "
              + channelSecurityPolicy.getUri()
              + " ("
              + channelAlgorithm
              + ")");
    }
  }
}
