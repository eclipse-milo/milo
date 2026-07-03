/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.sks;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.identity.UsernameProvider;
import org.eclipse.milo.opcua.sdk.client.identity.X509IdentityProvider;
import org.eclipse.milo.opcua.sdk.pubsub.security.InMemoryKeyCredentialStore;
import org.eclipse.milo.opcua.sdk.pubsub.sks.SksIdentitySelector.IdentitySelection;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SksIdentitySelectorTest {

  private static final String SKS_URI = "urn:test:sks";

  private static final CertificateValidator VALIDATOR =
      new CertificateValidator.InsecureCertificateValidator();

  private static KeyPair userKeyPair;
  private static X509Certificate userCertificate;

  @BeforeAll
  static void generateUserIdentityCertificate() throws Exception {
    userKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    userCertificate =
        new SelfSignedCertificateBuilder(userKeyPair)
            .setCommonName("TestUserIdentity")
            .setApplicationUri("urn:test:user-identity")
            .build();
  }

  @Test
  void emptyEntryTokensSelectAnonymous() throws UaException {
    IdentitySelection selection =
        SksIdentitySelector.select(
            entry(), endpoint(UserTokenType.UserName), null, VALIDATOR, null, null);

    assertInstanceOf(AnonymousProvider.class, selection.identityProvider());
    assertEquals(UserTokenType.Anonymous, selection.tokenType());
    assertNull(selection.secret());
  }

  @Test
  void listedAnonymousSelectsAnonymousWhenOffered() throws UaException {
    IdentitySelection selection =
        SksIdentitySelector.select(
            entry(UserTokenType.Anonymous),
            endpoint(UserTokenType.Anonymous, UserTokenType.UserName),
            null,
            VALIDATOR,
            null,
            null);

    assertInstanceOf(AnonymousProvider.class, selection.identityProvider());
  }

  @Test
  void usernameResolvesThroughCredentialStore() throws UaException {
    var store = new InMemoryKeyCredentialStore();
    store.put(SKS_URI, "user1", "password1".toCharArray());

    IdentitySelection selection =
        SksIdentitySelector.select(
            entry(UserTokenType.UserName),
            endpoint(UserTokenType.Anonymous, UserTokenType.UserName),
            store,
            VALIDATOR,
            null,
            null);

    assertInstanceOf(UsernameProvider.class, selection.identityProvider());
    assertEquals(UserTokenType.UserName, selection.tokenType());

    char[] secret = selection.secret();
    assertNotNull(secret);
    assertArrayEquals("password1".toCharArray(), secret);

    selection.wipeSecret();
    assertArrayEquals(new char[secret.length], secret);
  }

  @Test
  void usernameWithoutCredentialFallsToNextListedType() throws UaException {
    var store = new InMemoryKeyCredentialStore(); // no credential for SKS_URI

    IdentitySelection selection =
        SksIdentitySelector.select(
            entry(UserTokenType.UserName, UserTokenType.Anonymous),
            endpoint(UserTokenType.Anonymous, UserTokenType.UserName),
            store,
            VALIDATOR,
            null,
            null);

    assertInstanceOf(AnonymousProvider.class, selection.identityProvider());
  }

  @Test
  void usernameWithoutStoreFallsToNextListedType() throws UaException {
    IdentitySelection selection =
        SksIdentitySelector.select(
            entry(UserTokenType.UserName, UserTokenType.Anonymous),
            endpoint(UserTokenType.Anonymous, UserTokenType.UserName),
            null,
            VALIDATOR,
            null,
            null);

    assertInstanceOf(AnonymousProvider.class, selection.identityProvider());
  }

  @Test
  void listedOrderTakesPrecedence() throws UaException {
    var store = new InMemoryKeyCredentialStore();
    store.put(SKS_URI, "user1", "password1".toCharArray());

    IdentitySelection selection =
        SksIdentitySelector.select(
            entry(UserTokenType.UserName, UserTokenType.Anonymous),
            endpoint(UserTokenType.Anonymous, UserTokenType.UserName),
            store,
            VALIDATOR,
            null,
            null);

    assertInstanceOf(UsernameProvider.class, selection.identityProvider());
  }

  @Test
  void nonIntersectionFailsTheEntry() {
    var store = new InMemoryKeyCredentialStore();
    store.put(SKS_URI, "user1", "password1".toCharArray());

    // The entry lists UserName only, but the endpoint offers Anonymous only: K12 pins "try
    // listed types in order, then fail the entry" — never downgrade past the list.
    UaException e =
        assertThrows(
            UaException.class,
            () ->
                SksIdentitySelector.select(
                    entry(UserTokenType.UserName),
                    endpoint(UserTokenType.Anonymous),
                    store,
                    VALIDATOR,
                    null,
                    null));

    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
    assertTrue(e.getMessage().contains("not offered"));
  }

  @Test
  void certificateUsesConfiguredUserIdentity() throws UaException {
    IdentitySelection selection =
        SksIdentitySelector.select(
            entry(UserTokenType.Certificate),
            endpoint(UserTokenType.Certificate),
            null,
            VALIDATOR,
            userCertificate,
            userKeyPair.getPrivate());

    assertInstanceOf(X509IdentityProvider.class, selection.identityProvider());
    assertEquals(UserTokenType.Certificate, selection.tokenType());
  }

  @Test
  void certificateWithoutConfiguredIdentityFails() {
    UaException e =
        assertThrows(
            UaException.class,
            () ->
                SksIdentitySelector.select(
                    entry(UserTokenType.Certificate),
                    endpoint(UserTokenType.Certificate),
                    null,
                    VALIDATOR,
                    null,
                    null));

    assertTrue(e.getMessage().contains("no user identity certificate"));
  }

  @Test
  void issuedTokenIsUnsupported() {
    UaException e =
        assertThrows(
            UaException.class,
            () ->
                SksIdentitySelector.select(
                    entry(UserTokenType.IssuedToken),
                    endpoint(UserTokenType.IssuedToken),
                    null,
                    VALIDATOR,
                    null,
                    null));

    assertTrue(e.getMessage().contains("unsupported token type"));
  }

  @Test
  void utf8BytesEncodesWithoutString() {
    char[] chars = "pässword-ĸ".toCharArray();

    byte[] bytes = SksIdentitySelector.utf8Bytes(chars);

    assertArrayEquals("pässword-ĸ".getBytes(StandardCharsets.UTF_8), bytes);
    // The input is not modified; the fetch wipes it separately.
    assertArrayEquals("pässword-ĸ".toCharArray(), chars);
  }

  private static EndpointDescription entry(UserTokenType... tokenTypes) {
    return new EndpointDescription(
        null,
        new ApplicationDescription(
            SKS_URI,
            null,
            LocalizedText.NULL_VALUE,
            ApplicationType.Server,
            null,
            null,
            new String[] {"opc.tcp://sks:4840"}),
        ByteString.NULL_VALUE,
        MessageSecurityMode.SignAndEncrypt,
        null,
        tokenPolicies(tokenTypes),
        null,
        ubyte(0));
  }

  private static EndpointDescription endpoint(UserTokenType... tokenTypes) {
    return new EndpointDescription(
        "opc.tcp://sks:4840/secure",
        new ApplicationDescription(
            SKS_URI, null, LocalizedText.NULL_VALUE, ApplicationType.Server, null, null, null),
        ByteString.NULL_VALUE,
        MessageSecurityMode.SignAndEncrypt,
        null,
        tokenPolicies(tokenTypes),
        null,
        ubyte(3));
  }

  private static UserTokenPolicy[] tokenPolicies(UserTokenType... tokenTypes) {
    if (tokenTypes.length == 0) {
      return null;
    }
    var policies = new UserTokenPolicy[tokenTypes.length];
    for (int i = 0; i < tokenTypes.length; i++) {
      policies[i] =
          new UserTokenPolicy(
              tokenTypes[i].toString().toLowerCase(), tokenTypes[i], null, null, null);
    }
    return policies;
  }
}
